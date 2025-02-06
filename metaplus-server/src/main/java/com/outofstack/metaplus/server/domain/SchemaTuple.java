package com.outofstack.metaplus.server.domain;

import com.outofstack.metaplus.common.model.schema.Schema;

public class SchemaTuple {
    Schema origSchema;
    Schema richSchema;
    Schema pureSchema;

    SchemaTuple(Schema origSchema, Schema richSchema, Schema pureSchema) {
        this.origSchema = origSchema;
        this.richSchema = richSchema;
        this.pureSchema = pureSchema;
    }

    public Schema getOrigSchema() {
        return origSchema;
    }
    public Schema getRichSchema() {
        return richSchema;
    }
    public Schema getPureSchema() {
        return pureSchema;
    }
}