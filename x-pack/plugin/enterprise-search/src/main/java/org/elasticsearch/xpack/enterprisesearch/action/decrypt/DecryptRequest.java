

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action.decrypt;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;

public class DecryptRequest extends ActionRequest implements IndicesRequest, ToXContentObject {

    private String index;
    private String id;
    private String field;

    public DecryptRequest(){
        super();
    }

    public DecryptRequest(StreamInput in) throws IOException {
        super(in);
        this.index = in.readString();
        this.id = in.readString();
        this.field = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(index);
        out.writeString(id);
        out.writeString(field);
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return builder
            .startObject()
            .field("index", params.param("index"))
            .field("id", params.param("id"))
            .field("field", params.param("field"))
            .endObject();
    }

    @Override
    public ActionRequestValidationException validate() {
        return null; // TODO
    }

    /**
     * Returns the array of indices that the action relates to
     */
    @Override
    public String[] indices() {
        String[] indices = new String[1];
        indices[0] = index;
        return indices;
    }

    /**
     * Returns the indices options used to resolve indices. They tell for instance whether a single index is
     * accepted, whether an empty array will be converted to _all, and how wildcards will be expanded if needed.
     */
    @Override
    public IndicesOptions indicesOptions() {
        return IndicesOptions.STRICT_EXPAND_OPEN_CLOSED_HIDDEN; // TODO no idea what this is about
    }
}
