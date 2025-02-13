package com.outofstack.metaplus.common.model;

import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.schema.Schema;

public class DomainDoc extends MetaplusDoc {

    public static final String DOMAIN_DOMAIN = "domain";
    public static final String KEY_INDEX = "index";
    public static final String KEY_SCHEMA = "schema";

    public DomainDoc(String domain) {
        super("", DOMAIN_DOMAIN, domain);
        getMeta().put(KEY_INDEX, new JsonObject());
        getMeta().put(KEY_SCHEMA, new Schema());
    }

    public DomainDoc(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        if (!DOMAIN_DOMAIN.equals(getFqmnDomain())) {
            throw new IllegalArgumentException("The domain of a domainDoc must be '" + DOMAIN_DOMAIN + "'");
        }

        JsonObject index = getMeta().getJsonObject(KEY_INDEX);
        if (null == index) {
            throw new IllegalArgumentException("A domainDoc must have 'index'");
        }

        JsonObject schema = getMeta().getJsonObject(KEY_SCHEMA);
        if (null == schema) {
            throw new IllegalArgumentException("A domainDoc must have 'schema'");
        }
    }

    public Schema getSchema() {
        JsonObject schema = getMeta().getJsonObject(KEY_SCHEMA);
        if (null == schema) {
            return null;
        } else if (schema instanceof Schema) {
            return (Schema) schema;
        } else {
            return new Schema(schema);
        }
    }

    public void setSchema(Schema schema) {
        getMeta().put(KEY_SCHEMA, schema);
    }

}
