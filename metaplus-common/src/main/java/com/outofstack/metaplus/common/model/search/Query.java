package com.outofstack.metaplus.common.model.search;

import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;

public class Query extends JsonObject {
    public static final String KEY_QUERY = "query";
    public static final String KEY_SCRIPT = "script";
    public static final String KEY_SOURCE = "_source";
    public static final String KEY_SORT = "sort";
    public static final String KEY_FROM = "from";
    public static final String KEY_SIZE = "size";

    public Query() {
        super();
        put(KEY_QUERY, new JsonObject());
    }

    public Query(JsonObject target) {
        super(target);
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

    public void setScript(JsonObject script) {
        put(KEY_SCRIPT, script);
    }
    public JsonObject getScript() {
        return getJsonObject(KEY_SCRIPT);
    }

    public void setSource(Object source) {
        put(KEY_SOURCE, source);
    }
    public Object getSource() {
        return get(KEY_SOURCE);
    }

    public void setSort(JsonArray sort) {
        put(KEY_SORT, sort);
    }
    public JsonArray getSort() {
        return getJsonArray(KEY_SORT);
    }

    public void setFrom(int from) {
        put(KEY_FROM, from);
    }
    public Integer getFrom() {
        return getInteger(KEY_FROM);
    }

    public void setSize(int size) {
        put(KEY_SIZE, size);
    }
    public Integer getSize() {
        return getInteger(KEY_SIZE);
    }


}
