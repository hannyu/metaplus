package com.outofstack.metaplus.common.model;

import com.outofstack.metaplus.common.json.JsonObject;


public class ModelUtil {

    /**
     * metaplus::meta::none --> metaplus, meta, none
     *
     * @param fqmn  metaplus::meta::none
     * @return      [corp, domain, name], throw IllegalArgumentException if fail
     */
    public static String[] checkAndSplitFqmn(String fqmn) {
        if (null == fqmn) throw new IllegalArgumentException("fqmn can not be null");

        String[] ss = fqmn.split("::");
        if (ss.length != 3) {
            throw new IllegalArgumentException("Wrong format of fqmn: " + fqmn);
        }
        if (ss[1].isEmpty()) throw new IllegalArgumentException("fqmn.domain can not be empty");
        if (ss[2].isEmpty()) throw new IllegalArgumentException("fqmn.name can not be empty");
        return ss;
    }

    /**
     * metaplus, meta, none  -->  metaplus::meta::none
     *
     * @param corp
     * @param domain
     * @param name
     * @return
     */
    public static String packFqmn(String corp, String domain, String name) {
        return (corp == null ? "" : corp) + "::" + domain + "::" + name;
    }


    /**
     * transfer:
     * {
     *     "set": ...,
     *     "add": ...,
     *     "delete": ...,
     *     "replace": ...,
     * }
     *
     * to:
     * {
     *      "doc": ...,
     * }
     * or:
     * {
     *      "script": ...,
     * }
     *
     * TODO:
     * 1、not only support 'set'
     * 2、might be better to put it in a new class
     *
     * @param patchBody     JsonObject in metaPatch or plusPatch
     * @return              JsonObject can be used directly in create/update method
     */
    public static JsonObject transformPatchBody2DocBody(JsonObject patchBody) {
        if (null == patchBody) return null;

        JsonObject docBody = new JsonObject();
        for (String key : patchBody.keySet()) {
            if ("set".equals(key)) {
                docBody.merge(patchBody.getJsonObject(key));
            } else {
                throw new IllegalArgumentException("Sorry, currently only 'set' is supported, not '" + key + "'");
            }
        }
        return docBody;
    }

}
