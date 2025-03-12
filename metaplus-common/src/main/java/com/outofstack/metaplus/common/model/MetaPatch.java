package com.outofstack.metaplus.common.model;


import com.outofstack.metaplus.common.json.JsonObject;


/**
 * A document shows how to modify MetaDoc, created by primary data store, transmitted as a message,
 * yet a JsonObject.
 *
 * {
 *     "method": "...",
 *     "fqmn": {...},
 *     "sync": {...},
 *     "metaPatch": {
 *         "set": {...}
 *     },
 * }
 *
 */
@Deprecated
public class MetaPatch extends OldMetaplusPatch {
    public final static String KEY_META_PATCH = "metaPatch";

    public MetaPatch(JsonObject target) {
        super(target);

        JsonObject patchjo = target.getJsonObject(KEY_META_PATCH);
        if (null == patchjo) {
            if (null == getMeta()) {
                throw new IllegalArgumentException("A metaPatch must has '" + KEY_META_PATCH + "' or 'meta'");
            }
        } else {
            setPatchBody(patchjo);
        }
    }

//    public MetaPatch(PatchMethod patchMethod, String corp, String domain, String name) {
//        super(patchMethod, corp, domain, name);
//    }
//
//    public MetaPatch(PatchMethod patchMethod, String fqmn) {
//        super(patchMethod, fqmn);
//    }

//    /**
//     * not deep copy
//     *
//     * @param jsonObject
//     * @return
//     */
//    public static MetaPatch of(JsonObject jsonObject) {
//        if (null == jsonObject.getJsonObject(KEY_META_PATCH)) {
//            throw new IllegalArgumentException("MetaPatch must has '" + KEY_META_PATCH + "'");
//        }
//
//        return (MetaPatch) MetaplusPatch.of(jsonObject);
//    }

//    /**
//     * not deep copy
//     *
//     * @param method
//     * @param doc
//     * @return
//     */
//    public static MetaPatch copyOf(PatchMethod method, MetaplusDoc doc) {
//        if (null == method) throw new IllegalArgumentException("Method can not be null");
//        if (null == doc) throw new IllegalArgumentException("Doc can not be null");
//
//        MetaPatch patch = new MetaPatch(method, doc.getFqmnCorp(), doc.getFqmnDomain(), doc.getFqmnName());
//        if (null != doc.getSync()) patch.setSync(doc.getSync());
//        if (null != doc.getMeta()) patch.setMeta(doc.getMeta());
//        if (null != doc.getPlus()) patch.setPlus(doc.getPlus());
//        return patch;
//    }

    @Override
    public JsonObject getPatchBody() {
        return getJsonObject(KEY_META_PATCH);
    }

    @Override
    public void setPatchBody(JsonObject patchBody) {
        put(KEY_META_PATCH, patchBody);
    }

//    @Override
//    public void removePatchBody() {
//        remove(KEY_META_PATCH);
//    }


}
