package com.outofstack.metaplus.common.model.search;


import com.outofstack.metaplus.common.json.JsonArray;
import com.outofstack.metaplus.common.json.JsonObject;
import com.outofstack.metaplus.common.model.MetaplusDoc;

public class Hits extends JsonObject {

    public static final String PATH_HITS = "$.hits.hits";
    public static final String PATH_TOTAL_VALUE = "$.hits.total.value";

    public Hits(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        Integer totalCnt = getIntegerByPath(PATH_TOTAL_VALUE);
        if (null == totalCnt) {
            throw new IllegalArgumentException("A Hits must have '" + PATH_TOTAL_VALUE + "'");
        }

        JsonArray hits = getJsonArrayByPath(PATH_HITS);
        if (null == hits) {
            throw new IllegalArgumentException("A Hits must have '" + PATH_HITS + "'");
        }
    }

    public int getTotalValue() {
        return getIntegerByPath(PATH_TOTAL_VALUE);
    }

    public int getHitsSize() {
        return getJsonArrayByPath(PATH_HITS).size();
    }

    public JsonObject getHitsItem(int idx) {
        return getJsonArrayByPath(PATH_HITS).getJsonObject(idx);
    }

    public MetaplusDoc getHitsDoc(int idx) {
        JsonObject item = getHitsItem(idx);
        if (null == item) {
            throw new IllegalArgumentException("Hits has null item at idx '" + idx + "'");
        } else {
            try {
                MetaplusDoc doc = new MetaplusDoc(item.getJsonObject("_source"));
                doc.internalSetId(item.getString("_id"));
                doc.setSyncVersion(item.getInteger("_version"));
                return doc;
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Hits has NOT a MetaplusDoc item, see: " + item);
            }
        }
    }
}
