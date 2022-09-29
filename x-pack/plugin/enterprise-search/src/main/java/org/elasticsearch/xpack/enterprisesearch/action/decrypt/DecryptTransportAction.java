

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.decrypt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.client.internal.node.NodeClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.xpack.enterprisesearch.crypto.CryptoService;

public class DecryptTransportAction extends HandledTransportAction<DecryptRequest, DecryptResponse> {

    private final NodeClient client;
    private final CryptoService cryptoService;
    protected Logger logger = LogManager.getLogger(getClass());

    @Inject
    public DecryptTransportAction(TransportService transportService, ActionFilters actionFilters, NodeClient client) {
        super(DecryptAction.NAME, transportService, actionFilters, DecryptRequest::new);
        this.client = client;
        this.cryptoService = new CryptoService(client.settings());
    }

    @Override
    protected void doExecute(Task task, DecryptRequest request, ActionListener<DecryptResponse> listener) {
        String[] includes = {request.getField()};
        FetchSourceContext fetchSourceContext = FetchSourceContext.of(true, includes, null);
        GetRequest getRequest = new GetRequest()
            .index(request.getIndex())
            .id(request.getId())
            .fetchSourceContext(fetchSourceContext);
        client.get(getRequest, listener.delegateFailure((l, getResponse) -> {
            try {
                DecryptResponse decryptResponse = new DecryptResponse();
                decryptResponse.setValue(
                    new String(cryptoService.decrypt(getResponse.getSource().get(request.getField()).toString().toCharArray()))
                );
                l.onResponse(decryptResponse);
                logger.info("Successfully responded to decrypt request");
            } catch (Throwable t) {
                Exception e = new Exception("Decrypt task failed.", t);
                logger.error("Failed to decrypt.", e);
                l.onFailure(e);
            }
        }));
    }
}
