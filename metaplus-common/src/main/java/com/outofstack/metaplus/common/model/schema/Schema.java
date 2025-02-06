package com.outofstack.metaplus.common.model.schema;

import com.outofstack.metaplus.common.json.JsonObject;

public class Schema extends JsonObject {

    public static final String KEY_MAPPINGS = "mappings";
    public static final String KEY_SETTINGS = "settings";

    public Schema() {
        super();
        put(KEY_MAPPINGS, new Properties());
        put(KEY_SETTINGS, new JsonObject());
    }

    public Schema(Properties mappings, JsonObject settings) {
        super();
        if (null == mappings) throw new IllegalArgumentException("Giving mappings can not be null");
        if (null == settings) throw new IllegalArgumentException("Giving settings can not be null");
        put(KEY_MAPPINGS, mappings);
        put(KEY_SETTINGS, settings);
    }

    public Schema(JsonObject schema) {
        super(schema);
        checkAndLoad();
    }

    private void checkAndLoad() {
        JsonObject mappings = getJsonObject(KEY_MAPPINGS);
        if (null == mappings) {
            throw new IllegalArgumentException("A Schema must have a valid 'mappings'");
        } else if (! (mappings instanceof Properties)) {
            put(KEY_MAPPINGS, new Properties((JsonObject) mappings));
        }

        JsonObject settings = getJsonObject(KEY_SETTINGS);
        if (null == settings) {
            throw new IllegalArgumentException("A Schema must have a valid 'settings'");
        }
    }

    public Properties getMappings() {
        JsonObject mappings = getJsonObject(KEY_MAPPINGS);
        if (null == mappings) {
            return null;
        } else if (mappings instanceof Properties) {
            return (Properties) mappings;
        } else {
            return new Properties(mappings);
        }
    }
    public void setMappings(Properties mappings) {
        put(KEY_MAPPINGS, mappings);
    }

    public JsonObject getSettings() {
        return getJsonObject(KEY_SETTINGS);
    }
    public void setSettings(JsonObject settings) {
        put(KEY_SETTINGS, settings);
    }

}
