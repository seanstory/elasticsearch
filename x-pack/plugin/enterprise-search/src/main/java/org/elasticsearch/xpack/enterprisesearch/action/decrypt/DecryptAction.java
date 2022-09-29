

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.decrypt;

import org.elasticsearch.action.ActionType;

public class DecryptAction extends ActionType<DecryptResponse> {

    public static final DecryptAction INSTANCE = new DecryptAction();
    public static final String NAME = "indices:data/read/ent-search-decrypt";

    public DecryptAction() {
        super(NAME, DecryptResponse::new);
    }
}
