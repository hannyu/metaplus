package com.outofstack.metaplus.common.model.schema;


import com.outofstack.metaplus.common.json.JsonObject;


public class Field extends JsonObject {
    public static final String KEY_TYPE = "type";
    public static final String KEY_REQUIRED = "#required";
    public static final String KEY_DEFAULT = "#default";
    public static final String KEY_COMMENT = "#comment";
    public static final String KEY_SAMPLES = "#samples";
    public static final String KEY_VALUE_EXPR = "#valueExpr";
    public static final String KEY_EXPR_ORDER = "#exprOrder";
    public static final String KEY_FORCE_LOWERCASE = "#forceLowercase";

//    public static final String TYPE_KEYWORD = "keyword";
//    public static final String TYPE_TEXT = "text";


    public Field(String type, Boolean required, Object oDefault, String comment) {
        super();
        if (null == type) {
            throw new IllegalArgumentException();
        } else {
            setType(type);
        }
        if (null != required) {
            setRequired(required);
        }
        if (null != oDefault) {
            setDefault(oDefault);
        }
        if (null != comment) {
            setComment(comment);
        }
    }
    public Field(String type, Boolean required, Object oDefault) {
        this(type, required, oDefault, null);
    }
    public Field(String type, Boolean required) {
        this(type, required, null);
    }
    public Field(String type) {
        this(type, null);
    }

    public Field(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        Object v = getObject(KEY_TYPE);
        if (! (v instanceof String)) {
            throw new IllegalArgumentException("A Field must have a valid 'type'");
        }
    }

    public static boolean isField(JsonObject jsonObject) {
        if (null == jsonObject) {
            return false;
        } else if (jsonObject instanceof Field) {
            return true;
        } else if (null == jsonObject.getString(KEY_TYPE)) {
            return false;
        } else {
            return true;
        }
    }

    public Field toPureCopy() {
        Field pureCopy = new Field(deepCopy());
        for (String key : pureCopy.keySet()) {
            if (key.startsWith("#")) pureCopy.remove(key);
        }
        if ("text".equals(pureCopy.getType())) {
            pureCopy.put("fields", new JsonObject()
                    .put("keyword", new JsonObject()
                            .put("type", "keyword")
                            .put("ignore_above", 256)
                    )
            );
        }
        return pureCopy;
    }

    public String getType() {
        return getString(KEY_TYPE);
    }
    public void setType(String type) {
        put(KEY_TYPE, type);
    }
    public String getComment() {
        return getString(KEY_COMMENT);
    }
    public void setComment(String comment) {
        put(KEY_COMMENT, comment);
    }
    public Boolean getRequired() {
        return getBoolean(KEY_REQUIRED);
    }
    public void setRequired(Boolean required) {
        put(KEY_REQUIRED, required);
    }
    public Object getDefault() {
        return getObject(KEY_DEFAULT);
    }
    public void setDefault(Object defaultValue) {
        put(KEY_DEFAULT, defaultValue);
    }
    public String getValueExpr() {
        return getString(KEY_VALUE_EXPR);
    }
    public void setValueExpr(String valueExpr) {
        put(KEY_VALUE_EXPR, valueExpr);
    }
    public int getExprOrder() {
        Integer order = getInteger(KEY_EXPR_ORDER);
        return order == null ? 10 : order;
    }
    public void setExprOrder(int exprOrder) {
        put(KEY_EXPR_ORDER, exprOrder);
    }

    public Boolean getForceLowercase() {
        return getBoolean(KEY_FORCE_LOWERCASE);
    }

}