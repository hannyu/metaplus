package com.outofstack.metaplus.common.model;

import com.outofstack.metaplus.common.json.JsonObject;

public class MetaplusPatch extends MetaplusDoc {

    public final static String KEY_METHOD = "method";

    public MetaplusPatch(PatchMethod method, String fqmn) {
        super(fqmn);
        setMethod(method);
    }

    public MetaplusPatch(PatchMethod method, String corp, String domain, String name) {
        super(corp, domain, name);
        setMethod(method);
    }

    public MetaplusPatch(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        String methodName = getString(KEY_METHOD);
        if (null == methodName || null == PatchMethod.of(methodName)) {
            throw new IllegalArgumentException("Invalid method '" + methodName + "'");
        }
    }

    public String getMethodName() {
        return getString(KEY_METHOD);
    }

    public PatchMethod getMethod() {
        return PatchMethod.of(getString(KEY_METHOD));
    }
    public void setMethod(PatchMethod patchMethod) {
        if (null == patchMethod) {
            throw new IllegalArgumentException("PatchMethod is null");
        }
        put(KEY_METHOD, patchMethod.methodName());
    }

}
