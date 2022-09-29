

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.decrypt;

import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestStatusToXContentListener;

import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class DecryptRestHandler extends BaseRestHandler {

    @Override
    public String getName() {
        return "rest_ent_search_decrypt";
    }

    @Override
    public List<Route> routes() {
        return List.of(
            new Route(GET, "_decrypt/{index}/{id}/{field}")
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) {
        DecryptRequest decryptRequest = new DecryptRequest();
        decryptRequest.setIndex(request.param("index"));
        decryptRequest.setId(request.param("id"));
        decryptRequest.setField(request.param("field"));

        return channel -> client.execute(DecryptAction.INSTANCE, decryptRequest, new RestStatusToXContentListener<>(channel));
    }
}
