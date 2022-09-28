

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.transport.TransportService;

import java.util.HashMap;
import java.util.Map;


public class TransportEncryptAction extends HandledTransportAction<EncryptRequest, EncryptResponse> {

    protected Logger logger = LogManager.getLogger(getClass());
    private final NodeClient client;

    @Inject
    public TransportEncryptAction(TransportService transportService, ActionFilters actionFilters, NodeClient client) {
        super(EncryptAction.NAME, transportService, actionFilters, EncryptRequest::new);
        this.client = client;
    }

    @Override
    protected void doExecute(Task task, EncryptRequest request, ActionListener<EncryptResponse> listener) {
        final EncryptResponse encryptResponse = new EncryptResponse();

        // encrypt the value
        String encryptedValue = "WOAH_'"+request.getValue()+"'_IT_IS_ENCRYPTED"; // TODO

        // update the document's field
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(request.getField(), encryptedValue);

        UpdateRequest updateRequest = new UpdateRequest(request.getIndex(), request.getId()).doc(jsonMap);
        logger.info("Made the request");
        client.update(updateRequest, listener.delegateFailure((l, updateResponse) -> {
            try {
                encryptResponse.setUpdateResponse(updateResponse);
                l.onResponse(encryptResponse);
            } catch (Throwable t) {
                l.onFailure(new Exception("Encrypt task failed.", t));
            }
        }));
    }
}
