/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.settings.MockSecureSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.test.ESIntegTestCase;
import org.elasticsearch.xpack.enterprisesearch.action.decrypt.DecryptAction;
import org.elasticsearch.xpack.enterprisesearch.action.decrypt.DecryptRequest;
import org.elasticsearch.xpack.enterprisesearch.action.decrypt.DecryptResponse;
import org.elasticsearch.xpack.enterprisesearch.action.encrypt.EncryptAction;
import org.elasticsearch.xpack.enterprisesearch.action.encrypt.EncryptRequest;
import org.elasticsearch.xpack.enterprisesearch.action.encrypt.EncryptResponse;
import org.elasticsearch.xpack.enterprisesearch.crypto.CryptoService;
import org.elasticsearch.xpack.enterprisesearch.setting.EntSearchField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@ESIntegTestCase.ClusterScope(scope = ESIntegTestCase.Scope.TEST, numDataNodes = 0)
public class EnterpriseSearchPluginIT extends ESIntegTestCase {

    private String index = "test-index";
    private String id = "test_id";
    private String field = "some_field";
    private String value = "some value";

    @Override
    protected Settings nodeSettings(int nodeOrdinal, Settings otherSettings) {
        Settings.Builder settings = Settings.builder().put(super.nodeSettings(nodeOrdinal, otherSettings));
        MockSecureSettings secureSettingsWithPassword = new MockSecureSettings();
        secureSettingsWithPassword.setString(
            EntSearchField.ENCRYPTION_KEY_SETTING.getKey(),
            randomAlphaOfLength(20)
        );
        settings.setSecureSettings(secureSettingsWithPassword);
        return settings.build();
    }


    @Override
    protected Collection<Class<? extends Plugin>> getMockPlugins() {
        List<Class<? extends Plugin>> plugins = new ArrayList<>(super.getMockPlugins());
        plugins.add(EnterpriseSearchPlugin.class);
        return plugins;
    }

    public void testEncryption() throws ExecutionException, InterruptedException {
        //setup
        internalCluster().startNodes(Settings.EMPTY);
        CreateIndexResponse res = client().admin().indices().prepareCreate(index).get();
        assertTrue(res.isAcknowledged());
        Map<String, Object> sourceDoc = new HashMap<>();
        sourceDoc.put("other_key", "other_value");
        IndexResponse indexRes = client().prepareIndex().setIndex(index).setId(id).setSource(sourceDoc).get();
        assertEquals(indexRes.status(), RestStatus.CREATED);

        //make an encryption request
        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.setIndex(index);
        encryptRequest.setId(id);
        encryptRequest.setField(field);
        encryptRequest.setValue(value);
        EncryptResponse response = client().execute(EncryptAction.INSTANCE, encryptRequest).get();
        assertEquals(response.status(), RestStatus.OK);

        //check that the value is encrypted
        GetResponse getResponse = client().prepareGet().setIndex(index).setId(id).get();
        Map<String, Object> source = getResponse.getSource();
        assertTrue(((String) source.get(field)).startsWith(CryptoService.ENCRYPTED_TEXT_PREFIX));

        //decrypt it
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.setIndex(index);
        decryptRequest.setId(id);
        decryptRequest.setField(field);
        DecryptResponse decryptResponse = client().execute(DecryptAction.INSTANCE, decryptRequest).get();
        assertEquals(decryptResponse.getValue(), value);
    }
}
