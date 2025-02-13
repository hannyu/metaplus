package com.outofstack.metaplus.common.model;


import com.outofstack.metaplus.common.json.JsonObject;


/**
 * A document stored in Elasticsearch, yet a JsonObject.
 *
 * {
 *     "fqmn": {
 *         "corp": ...,
 *         "domain": ...,
 *         "name": ...,
 *     },
 *     "cts": {...},
 *     "meta": {
 *         ...
 *     },
 *     "plus": {
 *         ...
 *     }
 * }
 *
 */
public class MetaplusDoc extends MetaplusTemplate {
    public final static String KEY_META = "meta";
    public final static String KEY_PLUS = "plus";

    public MetaplusDoc(String domain, String name) {
        this("", domain, name);
    }

    public MetaplusDoc(String corp, String domain, String name) {
        super(corp, domain, name);
        setMeta(new JsonObject());
        setPlus(new JsonObject());
    }

    public MetaplusDoc(String fqmn) {
        super(fqmn);
        setMeta(new JsonObject());
        setPlus(new JsonObject());
    }

    public MetaplusDoc(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        if (null == getJsonObject(KEY_META)) {
            setMeta(new JsonObject());
        }
        if (null != getJsonObject(KEY_PLUS)) {
            setPlus(new JsonObject());
        }
    }
//    /**
//     * not deep copy
//     *
//     * @param jsonObject
//     * @return
//     */
//    public static MetaplusDoc of(JsonObject jsonObject) {
//        if (null == jsonObject.getJsonObject(KEY_META) && null == jsonObject.getJsonObject(KEY_PLUS)) {
//            throw new IllegalArgumentException("MetaDoc must has " + KEY_META + " or " + KEY_PLUS);
//        }
//        return (MetaplusDoc) MetaplusTemplate.of(jsonObject);
//    }

    /**
     * not deep copy
     *
     * @param target
     * @return
     */
    public static MetaplusDoc copyOf(MetaplusDoc target) {
        if (null == target) throw new IllegalArgumentException("Target doc can not be null");
        MetaplusDoc newdoc = new MetaplusDoc(target.getFqmnCorp(), target.getFqmnDomain(), target.getFqmnName());
        JsonObject cts = target.getCts();
        if (null != cts) newdoc.setCts(cts);
        JsonObject meta = target.getMeta();
        if (null != meta) newdoc.setMeta(meta);
        JsonObject plus = target.getPlus();
        if (null != plus) newdoc.setPlus(plus);
        return newdoc;
    }

    public JsonObject getMeta() {
        return getJsonObject(KEY_META);
    }
    public void setMeta(JsonObject metaJsonObject) {
        put(KEY_META, metaJsonObject);
    }
    public void clearMeta() {
        put(KEY_META, new JsonObject());
    }

    public JsonObject getPlus() {
        return getJsonObject(KEY_PLUS);
    }
    public void setPlus(JsonObject plusJsonObject) {
        put(KEY_PLUS, plusJsonObject);
    }
    public void clearPlus() {
        put(KEY_PLUS, new JsonObject());
    }

}
