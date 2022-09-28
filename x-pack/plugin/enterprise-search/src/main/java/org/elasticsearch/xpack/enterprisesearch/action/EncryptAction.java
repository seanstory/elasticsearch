

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action;

import org.elasticsearch.action.ActionType;

public class EncryptAction extends ActionType<EncryptResponse> {


    public static final EncryptAction INSTANCE = new EncryptAction();
    public static final String NAME = "indices:data/write/ent-search-encrypt";

    private EncryptAction() {
        super(NAME, EncryptResponse::new);
    }
}
