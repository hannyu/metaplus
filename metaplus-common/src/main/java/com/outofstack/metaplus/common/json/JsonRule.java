package com.outofstack.metaplus.common.json;


import lombok.Data;

@Data
public class JsonRule {

    private String jsonPath;
    private String comment;
    private Boolean required;
    private Object defaultValue;
    private Object[] samples;

    public JsonRule(String jsonPath, String comment, Boolean required, Object defaultValue, Object[] samples) {
        if (null == jsonPath || !jsonPath.startsWith("$.") || jsonPath.length() < 3) {
            throw new JsonException("Invalid jsonPath: " + jsonPath);
        }
        this.jsonPath = jsonPath;
        this.comment = comment;
        this.required = null != required && required;
        this.defaultValue = defaultValue;
        this.samples = samples;

    }

    public JsonRule(String jsonPath, String comment, Boolean required, Object defaultValue) {
        this(jsonPath, comment, required, defaultValue, null);
    }

    public JsonRule(String jsonPath, String comment, Boolean required) {
        this(jsonPath, comment, required, null);
    }

    public JsonRule(String jsonPath, String comment) {
        this(jsonPath, comment, false);
    }

//    public void enforce(JsonObject target) {
//        if (null == target) {
//            throw new JsonException("JsonRule fail, target is null");
//        }
//        if (null != defaultValue) {
//            Object value = target.getByPath(jsonPath);
//            if (null == value) {
//                target.putByPath(jsonPath, defaultValue);
//            }
//        } else if (required) {
//            Object value = target.getByPath(jsonPath);
//            if (null == value) {
//                throw new JsonException("JsonRule fail, '" + jsonPath + "' is required");
//            }
//        }
//    }

}
