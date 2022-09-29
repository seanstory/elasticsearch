/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.crypto;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.settings.MockSecureSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xpack.enterprisesearch.setting.EntSearchField;
import org.junit.Before;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class CryptoServiceTest extends ESTestCase {

    private final String testKey = "abc123xyz";
    private final String expectedDecryptedStr = "input string";
    private final String expectedEncryptedStr = "::es_encrypted::BGPWwBuQwk98AS9pKk12yhtzpCSa5gg8WAYucg==";
    private Settings settings;

    @Before
    public void init() throws Exception {
        MockSecureSettings mockSecureSettings = new MockSecureSettings();
        mockSecureSettings.setString(EntSearchField.ENCRYPTION_KEY_SETTING.getKey(), testKey);
        settings = Settings.builder().setSecureSettings(mockSecureSettings).build();
    }

    public void testDecrypt() throws IOException {
        CryptoService service = new CryptoService(settings);
        String decryptedStr = new String(service.decrypt(expectedEncryptedStr.toCharArray()));
        assertEquals(decryptedStr, expectedDecryptedStr);
    }

    public void testEncryptionAndDecryptionChars() throws Exception {
        CryptoService service = new CryptoService(settings);
        final char[] chars = randomAlphaOfLengthBetween(0, 1000).toCharArray();
        final char[] encrypted = service.encrypt(chars);
        assertThat(encrypted, notNullValue());
        assertThat(Arrays.equals(encrypted, chars), is(false));

        final char[] decrypted = service.decrypt(encrypted);
        assertThat(Arrays.equals(chars, decrypted), is(true));
    }

    public void testEncryptedChar() throws Exception {
        CryptoService service = new CryptoService(settings);

        assertThat(service.isEncrypted((char[]) null), is(false));
        assertThat(service.isEncrypted(new char[0]), is(false));
        assertThat(service.isEncrypted(new char[CryptoService.ENCRYPTED_TEXT_PREFIX.length()]), is(false));
        assertThat(service.isEncrypted(CryptoService.ENCRYPTED_TEXT_PREFIX.toCharArray()), is(true));
        assertThat(service.isEncrypted(randomAlphaOfLengthBetween(0, 100).toCharArray()), is(false));
        assertThat(service.isEncrypted(service.encrypt(randomAlphaOfLength(10).toCharArray())), is(true));
    }

    public void testErrorMessageWhenSecureEncryptionKeySettingDoesNotExist() throws Exception {
        final ElasticsearchException e = expectThrows(ElasticsearchException.class, () -> new CryptoService(Settings.EMPTY));
        assertThat(e.getMessage(), is("setting [" + EntSearchField.ENCRYPTION_KEY_SETTING.getKey() + "] must be set in keystore"));
    }
}
