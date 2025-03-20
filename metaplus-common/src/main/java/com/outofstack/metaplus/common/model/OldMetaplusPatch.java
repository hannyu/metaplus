package com.outofstack.metaplus.common.model;


import com.outofstack.metaplus.common.json.JsonObject;


/**
 * A document shows how to modify MetaDoc, created by primary data store, transmitted as a message,
 * yet a JsonObject.
 *
 * {
 *     "fqmn": {...},
 *     "sync": {...},
 *     "metaPatch": {
 *         "set": {...},
 *         "add": {...},
 *         "delete": {...},
 *         "script": {...},
 *     }
 * }
 *
 */
@Deprecated
public class OldMetaplusPatch extends MetaplusDoc {

    public final static String KEY_META_PATCH = "metaPatch";
    public final static String KEY_PLUS_PATCH = "plusPatch";

    private PatchType patchType;

    public OldMetaplusPatch(JsonObject target) {
        super(target);
        if (null != getJsonObject(KEY_META_PATCH)) {
            this.patchType = PatchType.META_PATCH;
        } else if (null != getJsonObject(KEY_PLUS_PATCH)) {
            this.patchType = PatchType.PLUS_PATCH;
        } else if ( !getMeta().isEmpty()) {
            this.patchType = PatchType.META_PATCH;
            put(KEY_META_PATCH, new JsonObject());
        } else if (!getPlus().isEmpty()) {
            this.patchType = PatchType.PLUS_PATCH;
            put(KEY_PLUS_PATCH, new JsonObject());
        } else {
            throw new IllegalArgumentException("A MetaplusPatch must have '" + KEY_META_PATCH + "' or '" +
                    KEY_PLUS_PATCH + "'");
        }
    }

    public OldMetaplusPatch(PatchType patchType, String domain, String name) {
        this(patchType, "", domain, name);
    }

    public OldMetaplusPatch(PatchType patchType, String corp, String domain, String name) {
        super(corp, domain, name);
        init(patchType);
    }

    public OldMetaplusPatch(PatchType patchType, String fqmn) {
        super(fqmn);
        init(patchType);
    }

    private void init(PatchType patchType) {
        if (null == patchType) throw new IllegalArgumentException("patchType can not be null");
        this.patchType = patchType;
        if (PatchType.META_PATCH == patchType && null == getJsonObject(KEY_META_PATCH)) {
            put(KEY_META_PATCH, new JsonObject());
        } else if (PatchType.PLUS_PATCH == patchType && null == getJsonObject(KEY_PLUS_PATCH)) {
            put(KEY_PLUS_PATCH, new JsonObject());
        }
    }

    public static OldMetaplusPatch copyOf(PatchType patchType, MetaplusDoc doc) {
        if (null == patchType) throw new IllegalArgumentException("patchType can not be null");
        if (null == doc) throw new IllegalArgumentException("Doc can not be null");

        OldMetaplusPatch patch = new OldMetaplusPatch(patchType, doc.getFqmnCorp(), doc.getFqmnDomain(), doc.getFqmnName());
        if (null != doc.getSync()) patch.setSync(doc.getSync());
        if (null != doc.getMeta()) patch.setMeta(doc.getMeta());
        if (null != doc.getPlus()) patch.setPlus(doc.getPlus());
        return patch;
    }

    public JsonObject getPatchBody() {
        if (PatchType.META_PATCH == patchType) {
            return getJsonObject(KEY_META_PATCH);
        } else if (PatchType.PLUS_PATCH == patchType) {
            return getJsonObject(KEY_PLUS_PATCH);
        } else {
            throw new IllegalArgumentException("PatchType '" + patchType.name() + "' is unsupported");
        }
    }

    public void setPatchBody(JsonObject patchBody) {
        if (PatchType.META_PATCH == patchType) {
            put(KEY_META_PATCH, patchBody);
        } else if (PatchType.PLUS_PATCH == patchType) {
            put(KEY_PLUS_PATCH, patchBody);
        } else {
            throw new IllegalArgumentException("PatchType '" + patchType.name() + "' is unsupported");
        }
    }

    public void clearPatchBody() {
        if (PatchType.META_PATCH == patchType) {
            put(KEY_META_PATCH, new JsonObject());
        } else if (PatchType.PLUS_PATCH == patchType) {
            put(KEY_PLUS_PATCH, new JsonObject());
        } else {
            throw new IllegalArgumentException("PatchType '" + patchType.name() + "' is unsupported");
        }
    }

    public PatchType getPatchType() {
        return this.patchType;
    }

    public MetaplusDoc toDoc() {
        if (getPatchBody().isEmpty()) {
            return MetaplusDoc.copyOf(this);
        } else {
            throw new IllegalArgumentException("Transform to MetaplusDoc fail, 'patchBody' is not empty. " +
                    "Maybe run 'transformPatchBody2DocBody()' first");
        }
    }

    /**
     * TODO: It is not elegant
     */
    public void transformPatchBody2DocBody() {
        JsonObject patchBody = getPatchBody();
        if ( !patchBody.isEmpty()) {
            JsonObject docBody = ModelUtil.transformPatchBody2DocBody(patchBody);
            if (PatchType.META_PATCH == getPatchType()) {
                getMeta().merge(docBody);
            } else {
                getPlus().merge(docBody);
            }
            clearPatchBody();
        }

    }

}
