

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.enterprisesearch.action;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Collections;

public class EncryptRequest  extends ActionRequest implements IndicesRequest, ToXContentObject {
    private String index;
    private String id;
    private String field;
    private String value;

    public EncryptRequest(){
        super();
    }

    public EncryptRequest(StreamInput in) throws IOException {
        super(in);
        this.index = in.readString();
        this.id = in.readString();
        this.field = in.readString();
        this.value = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(this.index);
        out.writeString(this.id);
        out.writeString(this.field);
        out.writeString(this.value);
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
       return builder
            .startObject()
            .field("id", params.param("id"))
            .field("index", params.param("index"))
            .field("field", params.param("field"))
            .field("value", params.param("value"))
            .endObject();
    }

    @Override
    public ActionRequestValidationException validate() {
        return null; // TODO
    }

    @Override
    public String[] indices() {
        return Collections.singletonList(index).toArray(new String[1]);
    }

    @Override
    public IndicesOptions indicesOptions() {
        return IndicesOptions.STRICT_EXPAND_OPEN_CLOSED_HIDDEN; // TODO no idea what this is about
    }
}
