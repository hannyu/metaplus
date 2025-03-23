package com.outofstack.metaplus.common.model;


/**
 * PatchMethod is an enum
 *
 */
public enum PatchMethod {
    META_CREATE("meta_create"),
    META_UPDATE("meta_update"),
    META_DELETE("meta_delete"),
    PLUS_UPDATE("plus_update"),
    PATCH_RENAME("patch_rename"),
    PATCH_DELETE("patch_delete"),
    PATCH_UPDATE("patch_update"),
    PATCH_SCRIPT("patch_script");

    private final String method;
    private PatchMethod(String method) {
        this.method = method;
    }
    public String methodName() {
        return method;
    }
    @Override
    public String toString() {
        return methodName();
    }

    /**
     * convert string to PatchMethod
     *
     * @param method    method string
     * @return          PatchMethod or null
     */
    public static PatchMethod of(String method) {
        for (PatchMethod pm : PatchMethod.values()) {
            if (pm.methodName().equals(method)) {
                return pm;
            }
        }
        return null;
    }
}