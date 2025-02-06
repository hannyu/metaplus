package com.outofstack.metaplus.server.storage;

import com.outofstack.metaplus.common.json.JsonObject;

public class EsResponse extends JsonObject {
    private static final String KEY_STATUS_CODE = "status";
    private static final String KEY_BODY = "body";
    private static final String KEY_HEADER = "header";


    public EsResponse(int statusCode) {
        this(statusCode, null);
    }

    public EsResponse(int statusCode, JsonObject body) {
        super();
        put(KEY_STATUS_CODE, statusCode);
        if (null != body) put(KEY_BODY, body);
    }

    public int getStatusCode() {
        return getInteger(KEY_STATUS_CODE);
    }

    public JsonObject getBody() {
        return getJsonObject(KEY_BODY);
    }

    public boolean isSuccess() {
        int code = getInteger(KEY_STATUS_CODE);
        return code >= 200 && code < 300;
    }
}
