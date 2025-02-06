package com.outofstack.metaplus.common.model;

import com.outofstack.metaplus.common.json.JsonObject;

public class MetaplusMsg extends MetaplusPatch {

    public final static String KEY_METHOD = "method";

    public MetaplusMsg(JsonObject target) {
        super(target);
        setMethodName(getString(KEY_METHOD));
    }

    public void setMethodName(String methodName) {
        if (null == methodName || null == PatchMethod.of(methodName)) {
            throw new IllegalArgumentException("Invalid methodName '" + methodName + "'");
        }
        put(KEY_METHOD, methodName);
    }
    public String getMethodName() {
        return getString(KEY_METHOD);
    }

    public PatchMethod getMethod() {
        return PatchMethod.of(getString(KEY_METHOD));
    }
    public void setMethod(PatchMethod patchMethod) {
        put(KEY_METHOD, patchMethod.methodName());
    }

}
