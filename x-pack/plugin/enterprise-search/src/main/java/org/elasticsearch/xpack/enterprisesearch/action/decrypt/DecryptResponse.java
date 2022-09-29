

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.decrypt;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.StatusToXContentObject;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

public class DecryptResponse extends ActionResponse implements StatusToXContentObject {

    String value;

    public DecryptResponse(){
        super();
    }

    public DecryptResponse(StreamInput in) throws IOException {
        super(in);
        this.value = in.readString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return builder.startObject()
            .field("value", value)
            .endObject();
    }

    /**
     * Write this into the {@linkplain StreamOutput}.
     *
     * @param out
     */
    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(value);
    }

    /**
     * Returns the REST status to make sure it is returned correctly
     */
    @Override
    public RestStatus status() {
        if (value != null) {
            return RestStatus.OK;
        } else {
            return RestStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
