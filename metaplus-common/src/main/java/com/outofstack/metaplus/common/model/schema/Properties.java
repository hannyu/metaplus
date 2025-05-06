package com.outofstack.metaplus.common.model.schema;


import com.outofstack.metaplus.common.json.JsonObject;

import java.util.Set;

public class Properties extends JsonObject {

    public static final String KEY_PROPERTIES = "properties";

    public Properties() {
        super();
        put(KEY_PROPERTIES, new JsonObject());
    }

    public Properties(JsonObject target) {
        super(target);
        checkAndLoad();
    }

    private void checkAndLoad() {
        JsonObject properties = getJsonObject(KEY_PROPERTIES);
        if (null == properties) {
            throw new IllegalArgumentException("A Properties '" + toJson() + "' must have 'properties'");
        }

        for (String key : properties.keySet()) {
            Object v = properties.getObject(key);
            if (!(v instanceof JsonObject)) {
                throw new IllegalArgumentException("A Properties key '" + key + "' has a valid value '" + v + "'");
            } else {
                JsonObject jo = (JsonObject) v;
                if (jo instanceof Properties || jo instanceof Field){
                    // nothing
                } else if (Properties.isProperties(jo)) {
                    properties.put(key, new Properties(jo));
                } else if (Field.isField(jo)) {
                    properties.put(key, new Field(jo));
                } else {
                    throw new IllegalArgumentException("A Properties key '" + key + "' has a valid value '" + jo + "'");
                }
            }
        }
    }

    public static boolean isProperties(JsonObject jsonObject) {
        if (null == jsonObject) {
            return false;
        } else if (jsonObject instanceof Properties) {
            return true;
        } else if (null == jsonObject.getJsonObject(KEY_PROPERTIES)) {
            return false;
        } else {
            return true;
        }
    }

    public Set<String> propertyKeySet() {
        return getJsonObject(KEY_PROPERTIES).keySet();
    }

    public JsonObject getProperty(String key) {
        return getJsonObject(KEY_PROPERTIES).getJsonObject(key);
    }

    public void putProperty(String key, JsonObject property) {
        getJsonObject(KEY_PROPERTIES).put(key, property);
    }

    public Properties getProperties(String key) {
        JsonObject node = getJsonObject(KEY_PROPERTIES).getJsonObject(key);
        if (null == node) {
            return null;
        } else if (node instanceof Properties) {
            return (Properties) node;
        } else {
            return new Properties(node);
        }
    }

    public Field getField(String key) {
        JsonObject node = getJsonObject(KEY_PROPERTIES).getJsonObject(key);
        if (null == node) {
            return null;
        } else if (node instanceof Field) {
            return (Field) node;
        } else {
            return new Field(node);
        }
    }

    public boolean isField(String key) {
        JsonObject node = getJsonObject(KEY_PROPERTIES).getJsonObject(key);
        if (null == node) {
            return false;
        } else if (node instanceof Field) {
            return true;
        } else if (null == getString("type")) {
            return false;
        } else {
            return true;
        }
    }

}
