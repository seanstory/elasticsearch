

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.setting;

import org.elasticsearch.common.settings.SecureSetting;
import org.elasticsearch.common.settings.SecureString;
import org.elasticsearch.common.settings.Setting;

public final class EntSearchField {
    public static final Setting<SecureString> ENCRYPTION_KEY_SETTING = SecureSetting.secureString("xpack.ingestion.encryption_key", null);

}
