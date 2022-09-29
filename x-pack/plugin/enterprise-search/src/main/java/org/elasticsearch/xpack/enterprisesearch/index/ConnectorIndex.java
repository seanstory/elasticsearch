

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.index;

import org.elasticsearch.common.settings.Settings;

public class ConnectorIndex {
    public static final String MAPPING_JSON = "{\n" +
        "  \"_meta\": {\n" +
        "    \"pipeline\": {\n" +
        "      \"default_extract_binary_content\": true,\n" +
        "      \"default_name\": \"ent-search-generic-ingestion\",\n" +
        "      \"default_reduce_whitespace\": true,\n" +
        "      \"default_run_ml_inference\": true\n" +
        "    },\n" +
        "    \"version\": 1,\n" +
        "    \"es-version\": \"8.6.0\"" +
        "  },\n" +
        "  \"properties\": {\n" +
        "    \"api_key_id\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"configuration\": {\n" +
        "      \"type\": \"object\"\n" +
        "    },\n" +
        "    \"error\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"index_name\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"language\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"last_seen\": {\n" +
        "      \"type\": \"date\"\n" +
        "    },\n" +
        "    \"last_sync_error\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"last_sync_status\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"last_synced\": {\n" +
        "      \"type\": \"date\"\n" +
        "    },\n" +
        "    \"name\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"pipeline\": {\n" +
        "      \"properties\": {\n" +
        "        \"name\": {\n" +
        "          \"type\": \"keyword\"\n" +
        "        },\n" +
        "        \"extract_binary_content\": {\n" +
        "          \"type\": \"boolean\"\n" +
        "        },\n" +
        "        \"reduce_whitespace\": {\n" +
        "          \"type\": \"boolean\"\n" +
        "        },\n" +
        "        \"run_ml_inference\": {\n" +
        "          \"type\": \"boolean\"\n" +
        "        }\n" +
        "      }\n" +
        "    },\n" +
        "    \"scheduling\": {\n" +
        "      \"properties\": {\n" +
        "        \"enabled\": {\n" +
        "          \"type\": \"boolean\"\n" +
        "        },\n" +
        "        \"interval\": {\n" +
        "          \"type\": \"text\"\n" +
        "        }\n" +
        "      }\n" +
        "    },\n" +
        "    \"service_type\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"status\": {\n" +
        "      \"type\": \"keyword\"\n" +
        "    },\n" +
        "    \"sync_now\": {\n" +
        "      \"type\": \"boolean\"\n" +
        "    }\n" +
        "  }\n" +
        "}";

    public static final Settings SETTINGS = Settings.builder()
        .put("auto_expand_replicas", "0-3")
        .put("hidden", true)
        .put("number_of_replicas", 0)
        .build();

}
