

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.encrypt;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.StatusToXContentObject;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

public class EncryptResponse extends ActionResponse implements StatusToXContentObject {

    private UpdateResponse updateResponse = null;

    public EncryptResponse(){
        super();
    }

    public EncryptResponse(StreamInput in) throws IOException {
        super(in);
        this.updateResponse = in.readOptionalWriteable(UpdateResponse::new);
    }

    public UpdateResponse getUpdateResponse() {
        return updateResponse;
    }

    public void setUpdateResponse(UpdateResponse updateResponse) {
        this.updateResponse = updateResponse;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return builder
            .startObject()
            .field("result", updateResponse.getResult().toString())
            .endObject();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeOptionalWriteable(updateResponse);
    }

    @Override
    public RestStatus status() {
        if (updateResponse != null) {
            return updateResponse.status();
        } else {
            return RestStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
