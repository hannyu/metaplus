package com.outofstack.metaplus.common.http;

import com.outofstack.metaplus.common.json.JsonObject;

public class HttpResponse<BodyType extends JsonObject> extends JsonObject {
    public static final String KEY_CODE = "code";
    public static final String KEY_MSG = "msg";
    public static final String KEY_BODY = "body";

    private Class<BodyType> clazz;

    @SuppressWarnings("unchecked")
    public HttpResponse(int code, String msg, BodyType responseBody) {
        super();

        put(KEY_CODE, code);
        put(KEY_MSG, msg);
        if (null != responseBody) {
            put(KEY_BODY, responseBody);
            this.clazz = (Class<BodyType>) responseBody.getClass();
        }
    }

    public HttpResponse(int code, BodyType responseBody) {
        this(code, "", responseBody);
    }

    public HttpResponse(int code, String msg) {
        this(code, msg, null);
    }

    public HttpResponse(JsonObject target, Class<BodyType> clazz) {
        super(target);
        this.clazz = clazz;
        checkAndLoad();
    }

    private void checkAndLoad() {
        if (!(get(KEY_CODE) instanceof Number)) {
            throw new IllegalArgumentException("A HttpResponse must have a valid 'code'");
        }

        JsonObject obj = getJsonObject(KEY_BODY);
        if (null != obj) {
            if (!clazz.isAssignableFrom(obj.getClass())) {
                put(KEY_BODY, obj.cast(clazz));
            }
        }
    }

    public int getCode() {
        return getInteger(KEY_CODE);
    }
    public void setCode(int code) {
        put(KEY_CODE, code);
    }
    public String getMsg() {
        return getString(KEY_MSG);
    }
    public void setMsg(int msg) {
        put(KEY_MSG, msg);
    }

    public BodyType getBody() {
        JsonObject obj = getJsonObject(KEY_BODY);
        if (null == obj || null == clazz) {
            return null;
        } else if (clazz.isAssignableFrom(obj.getClass())) {
            return clazz.cast(obj);
        } else {
            return obj.cast(clazz);
        }
    }
    public void setBody(BodyType body) {
        put(KEY_BODY, body);
    }


    public boolean isSuccess() {
        int code = getInteger(KEY_CODE);
        return code >= 200 && code < 300;
    }

    public boolean isNotFound() {
        int code = getInteger(KEY_CODE);
        return code == 404;
    }

    /// ////////////////////////////////
    /// some final http response
    /// ////////////////////////////////

    private static final HttpResponse<JsonObject> OK = new HttpResponse<JsonObject>(200, "ok");
    public static HttpResponse<JsonObject> ok() {
        return OK;
    }

    private static final HttpResponse<JsonObject> NOT_FOUND = new HttpResponse<JsonObject>(404, "not found");
    public static HttpResponse<JsonObject> notFound() {
        return NOT_FOUND;
    }

}
