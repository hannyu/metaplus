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
 *     }
 * }
 *
 */
@Deprecated
public class PlusPatch extends OldMetaplusPatch {
    public final static String KEY_PLUS_PATCH = "plusPatch";

    public PlusPatch(JsonObject target) {
        super(target);

        JsonObject patchjo = target.getJsonObject(KEY_PLUS_PATCH);
        if (null == patchjo) {
            if (null == getPlus()) {
                throw new IllegalArgumentException("A metaPatch must has '" + KEY_PLUS_PATCH + "' or 'plus'");
            }
        } else {
            setPatchBody(patchjo);
        }
    }

//    public PlusPatch(PatchMethod patchMethod, String corp, String domain, String name) {
//        super(patchMethod, corp, domain, name);
//    }
//
//    public PlusPatch(PatchMethod patchMethod, String fqmn) {
//        super(patchMethod, fqmn);
//    }

//    /**
//     * not deep copy
//     *
//     * @param jsonObject
//     * @return
//     */
//    public static PlusPatch of(JsonObject jsonObject) {
//        if (null == jsonObject.getJsonObject(KEY_PLUS_PATCH)) {
//            throw new IllegalArgumentException("PlusPatch must has '" + KEY_PLUS_PATCH + "'");
//        }
//
//        return (PlusPatch) MetaplusPatch.of(jsonObject);
//    }


    @Override
    public JsonObject getPatchBody() {
        return getJsonObject(KEY_PLUS_PATCH);
    }

    @Override
    public void setPatchBody(JsonObject patchBody) {
        put(KEY_PLUS_PATCH, patchBody);
    }

//    @Override
//    public void removePatchBody() {
//        remove(KEY_PLUS_PATCH);
//    }


}
