package com.outofstack.metaplus.common.model.search;

import com.outofstack.metaplus.common.json.JsonObject;

public class Query extends JsonObject {
    public static final String KEY_QUERY = "query";

    public Query() {
        super();
        put(KEY_QUERY, new JsonObject());
    }

    public Query(JsonObject jsonObject) {
        super(jsonObject);
        checkAndLoad();
    }

    private void checkAndLoad() {
        JsonObject query = getJsonObject(KEY_QUERY);
        if (null == query) {
            throw new IllegalArgumentException("A Query must have 'query'");
        }
    }

    public void setQuery(JsonObject queryBody) {
        put(KEY_QUERY, queryBody);
    }

    public JsonObject getQuery() {
        return getJsonObject(KEY_QUERY);
    }
}
