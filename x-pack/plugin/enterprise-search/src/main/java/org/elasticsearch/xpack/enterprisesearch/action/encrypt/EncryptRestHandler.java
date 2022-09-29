/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.encrypt;

import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestStatusToXContentListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.rest.RestRequest.Method.POST;

public class EncryptRestHandler extends BaseRestHandler {

    @Override
    public String getName() {
        return "rest_ent_search_encrypt";
    }

    @Override
    public List<Route> routes() {
        return List.of(
            new Route(POST, "_encrypt/{index}/{id}")
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {

        EncryptRequest encryptRequest = new EncryptRequest();
        encryptRequest.setIndex(request.param("index"));
        encryptRequest.setId(request.param("id"));
        Map<String, Object> body = request.contentParser().map();
        encryptRequest.setField((String) body.get("field"));
        encryptRequest.setValue((String) body.get("value"));

        return channel -> client.execute(EncryptAction.INSTANCE, encryptRequest, new RestStatusToXContentListener<>(channel));
    }
}
