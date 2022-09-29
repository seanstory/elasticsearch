/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.xpack.enterprisesearch;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SystemIndexPlugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.xpack.enterprisesearch.action.decrypt.DecryptAction;
import org.elasticsearch.xpack.enterprisesearch.action.decrypt.DecryptRestHandler;
import org.elasticsearch.xpack.enterprisesearch.action.decrypt.DecryptTransportAction;
import org.elasticsearch.xpack.enterprisesearch.action.encrypt.EncryptAction;
import org.elasticsearch.xpack.enterprisesearch.action.encrypt.EncryptRestHandler;
import org.elasticsearch.xpack.enterprisesearch.action.encrypt.EncryptTransportAction;
import org.elasticsearch.xpack.enterprisesearch.setting.EntSearchField;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;

public class EnterpriseSearchPlugin extends Plugin implements SystemIndexPlugin {
    public static final String FEATURE_NAME = "Enterprise Search Connectors";
    public static final String DESCRIPTION = "The state and metadata surrounding registered connectors and their sync jobs";

    @Override
    public List<RestHandler> getRestHandlers(final Settings settings,
                                             final RestController restController,
                                             final ClusterSettings clusterSettings,
                                             final IndexScopedSettings indexScopedSettings,
                                             final SettingsFilter settingsFilter,
                                             final IndexNameExpressionResolver indexNameExpressionResolver,
                                             final Supplier<DiscoveryNodes> nodesInCluster) {

        return List.of(
            new EncryptRestHandler(),
            new DecryptRestHandler()
        );
    }

    @Override
    public List<ActionHandler<? extends ActionRequest, ? extends ActionResponse>> getActions() {
        return List.of(
            new ActionHandler<>(EncryptAction.INSTANCE, EncryptTransportAction.class),
            new ActionHandler<>(DecryptAction.INSTANCE, DecryptTransportAction.class)
        );
    }

    @Override
    public List<Setting<?>> getSettings() {
        return singletonList(EntSearchField.ENCRYPTION_KEY_SETTING);
    }

    @Override
    public String getFeatureName() {
        return FEATURE_NAME;
    }

    @Override
    public String getFeatureDescription() {
        return DESCRIPTION;
    }
}
