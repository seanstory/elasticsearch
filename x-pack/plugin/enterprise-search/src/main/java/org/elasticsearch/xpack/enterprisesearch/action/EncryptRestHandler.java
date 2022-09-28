/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.action.RestStatusToXContentListener;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.rest.RestRequest.Method.POST;

public class EncryptRestHandler extends BaseRestHandler {

    protected Logger logger = LogManager.getLogger(getClass());

    @Override
    public String getName() {
        return "rest_ent_search_action";
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
