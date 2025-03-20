package com.outofstack.metaplus.common.model;

import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;

public class MetaplusPatch extends JsonObject {

    public final static String KEY_DOMAIN = "domain";
    public final static String KEY_METHOD = "method";
    public final static String KEY_DOC = "doc";
    public final static String KEY_PATCH = "patch";

    public MetaplusPatch(PatchMethod method, String domain) {
        super();
        setMethod(method);
        put(KEY_DOMAIN, domain);
        checkAndLoad();
    }

    public MetaplusPatch(PatchMethod method, MetaplusDoc doc) {
        super();
        setMethod(method);
        setDoc(doc);
        put(KEY_DOMAIN, doc.getFqmnDomain());
        checkAndLoad();
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
        String domain = getString(KEY_DOMAIN);
        if (null == domain || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain must not be empty.");
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
    public String getDomain() {
        return getString(KEY_DOMAIN);
    }
    public void setDomain(String domain) {
        if (null == domain || domain.isEmpty()) {
            throw new IllegalArgumentException("Domain can not be empty.");
        }
        put(KEY_DOMAIN, domain);
    }

    public JsonObject getPatch() {
        return getJsonObject(KEY_PATCH);
    }
    public void setPatch(JsonObject patch) {
        put(KEY_PATCH, patch);
    }

    public void setDoc(MetaplusDoc doc) {
        if (null == doc) {
            throw new IllegalArgumentException("Doc can not be null.");
        }
        put(KEY_DOC, doc);
    }
    public MetaplusDoc getDoc() {
        JsonObject jo = getJsonObject(KEY_DOC);
        return null == jo ? null : new MetaplusDoc(jo);
    }

}
