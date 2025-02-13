package com.outofstack.metaplus.common.json;


import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class JsonObject {
    protected JsonObjectProxy jop;

    public JsonObject() {
        jop = JsonProxyFactory.createJsonObjectProxy();
    }

    public JsonObject(JsonObject target) {
        if (null == target) {
            throw new JsonException("Target JsonObject can not be null");
        }
        this.jop = target.jop;
    }

    public JsonObject(String key, Object value) {
        this();
        put(key, value);
    }

    public JsonObject(Map<String, Object> map) {
        this();
        putAll(map);
    }

    protected JsonObject(JsonObjectProxy jop) {
        this.jop = jop;
    }

    public <T extends JsonObject> T cast(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor(JsonObject.class);
            T newobj = constructor.newInstance(this);
            newobj.jop = jop;
            return newobj;
        } catch (Throwable e) {
            throw new JsonException("Cast 'JsonObject' to '" + clazz.getSimpleName() + "' fail", e);
        }
    }

    public static JsonObject parse(String json) {
        return parse(new StringReader(json));
    }

    public static JsonObject parse(Reader reader) {
        if (null == reader) throw new JsonException("reader is null");
        JsonObjectProxy jop = JsonProxyFactory.parseJsonObjectProxy(reader);
        return new JsonObject(jop);
    }

    public JsonObject toCopy() {
        JsonObject copy = new JsonObject();
        for (String key : keySet()) {
            Object value = get(key);
            copy.put(key, value);
        }
        return copy;
    }

    public JsonObject deepCopy() {
        JsonObject copy = new JsonObject();
        for (String key : keySet()) {
            Object value = get(key);
            if (value instanceof JsonObject) {
                copy.put(key, ((JsonObject) value).deepCopy());
            } else if (value instanceof JsonArray) {
                copy.put(key, ((JsonArray) value).deepCopy());
            } else {
                copy.put(key, value);
            }
        }
        return copy;
    }

    public String toJson() {
        return jop.toJson();
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public int hashCode() {
        return jop.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JsonObject) {
            return jop.equals(((JsonObject) object).jop);
        }
        return false;
    }

    public int size() {
        return jop.size();
    }

    public boolean isEmpty() {
        return jop.isEmpty();
    }

    public Set<String> keySet() {
        return jop.keySet();
    }

    public boolean containsKey(String key) {
        return jop.containsKey(key);
    }

    /**
     *
     * @param key
     * @return null if key is not exist
     */
    public Object get(String key) {
        Object v = jop.get(key);
        if (v instanceof JsonObjectProxy) {
            return new JsonObject((JsonObjectProxy) v);
        } else if (v instanceof JsonArrayProxy) {
            return new JsonArray((JsonArrayProxy) v);
        } else {
            return v;
        }
    }

    public <T extends JsonObject> T get(String key, Class<T> clazz) {
        JsonObject v = getJsonObject(key);
        return v.cast(clazz);
    }

    public String getString(String key) {
        return jop.getString(key);
    }

    public Long getLong(String key) {
        return jop.getLong(key);
    }

    public Integer getInteger(String key) {
        return jop.getInteger(key);
    }

    public Short getShort(String key) {
        return jop.getShort(key);
    }

    public Double getDouble(String key) {
        return jop.getDouble(key);
    }

    public Float getFloat(String key) {
        return jop.getFloat(key);
    }

    public Boolean getBoolean(String key) {
        return jop.getBoolean(key);
    }

//    public byte[] getBytes(String key) {
//        return jop.getBytes(key);
//    }

    public JsonObject getJsonObject(String key) {
        JsonObjectProxy v = jop.getJsonObjectProxy(key);
        if (v == null) {
            return null;
        } else {
            return new JsonObject(v);
        }
    }

//    public <T extends JsonObject> T getJsonObject(String key, Class<T> klass) {
//        JsonObjectProxy v = jop.getJsonObjectProxy(key);
//        if (v == null) {
//            return null;
//        } else {
//            try {
//                return klass.getDeclaredConstructor().newInstance(v);
//            } catch (Throwable e) {
//                throw new JsonException("Class '" + klass + "' newInstance fail", e);
//            }
//        }
//    }

    public JsonArray getJsonArray(String key) {
        JsonArrayProxy v = jop.getJsonArrayProxy(key);
        if (v == null) {
            return null;
        } else {
            return new JsonArray(v);
        }
    }

    /**
     *
     * @param key       support a.b.c
     * @param value
     * @return
     */
    public JsonObject put(String key, Object value) {
        if (value instanceof JsonObject) {
            jop.put(key, ((JsonObject) value).jop);
        } else if (value instanceof  JsonArray) {
            jop.put(key, ((JsonArray) value).jap);
        } else {
            jop.put(key, value);
        }
        return this;
    }

    public JsonObject putAll(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            put(key, value);
        }
        return this;
    }

    public JsonObject putAll(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            put(key, value);
        }
        return this;
    }

    public JsonObject remove(String key) {
        jop.remove(key);
        return this;
    }

    /**
     * @param jsonPath like '$.attr.name', only support JsonObject, not support JsonArray
     *
     * @return
     */
    public Object getByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().get(entry.getValue());
    }


    public String getStringByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getString(entry.getValue());
    }

    public Long getLongByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getLong(entry.getValue());
    }

    public Integer getIntegerByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getInteger(entry.getValue());
    }

    public Short getShortByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getShort(entry.getValue());
    }

    public Double getDoubleByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getDouble(entry.getValue());
    }

    public Float getFloatByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getFloat(entry.getValue());
    }

    public Boolean getBooleanByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getBoolean(entry.getValue());
    }

    public JsonObject getJsonObjectByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getJsonObject(entry.getValue());
    }

    public JsonArray getJsonArrayByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return null;
        return entry.getKey().getJsonArray(entry.getValue());
    }

    public boolean containsByPath(String jsonPath) {
        Map.Entry<JsonObject, String> entry = findLastSecondNode(jsonPath);
        if (null == entry) return false;
        return entry.getKey().containsKey(entry.getValue());
    }

    private Map.Entry<JsonObject, String> findLastSecondNode(String jsonPath) {
        if (null == jsonPath || !jsonPath.startsWith("$.") || jsonPath.length() < 3) {
            throw new JsonException("Invalid json path: " + jsonPath);
        }

        String[] nodes = jsonPath.split("\\.");
        // $, a, b, ..., x

        JsonObject tempJo = this;
        for (int i=1; i<nodes.length-1; i++) {
            tempJo = tempJo.getJsonObject(nodes[i]);
            if (null == tempJo) {
                return null;
            }
        }
        return Map.entry(tempJo, nodes[nodes.length-1]);
    }



    /**
     *
     * @param jsonPath like '$.a.b.x', only support JsonObject, not support JsonArray
     * @param value
     * @return
     */
    public JsonObject putByPath(String jsonPath, Object value) {
        if (null == jsonPath || !jsonPath.startsWith("$.") || jsonPath.length() < 3) {
            throw new JsonException("Invalid jsonPath: " + jsonPath);
        }

        String[] nodes = jsonPath.split("\\.");
        // $, a, b, ..., x

        JsonObject tempJo = this;
        for (int i=1; i<nodes.length-1; i++) {
            if (null == tempJo.getJsonObject(nodes[i])) {
                tempJo.put(nodes[i], new JsonObject());
            }
            tempJo = tempJo.getJsonObject(nodes[i]);
        }
        tempJo.put(nodes[nodes.length-1], value);
        return this;
    }

    public JsonObject removeByPath(String jsonPath) {
        if (null == jsonPath || !jsonPath.startsWith("$.") || jsonPath.length() < 3) {
            throw new JsonException("Invalid json path: " + jsonPath);
        }

        String[] nodes = jsonPath.split("\\.");
        // $, a, b, ..., x

        JsonObject tempJo = this;
        for (int i=1; i<nodes.length-1; i++) {
            if (null == tempJo.getJsonObject(nodes[i])) {
                tempJo.put(nodes[i], new JsonObject());
            }
            tempJo = tempJo.getJsonObject(nodes[i]);
        }
        tempJo.remove(nodes[nodes.length-1]);
        return this;
    }

    /**
     * Reference target and merge it, and may overwrite the original value.
     * Recursively merge when JsonObject, overwrite when JsonArray.
     *
     * @param target
     * @return  this
     */
    public JsonObject merge(JsonObject target) {
        if (null == target) return this;

        for (String key : target.keySet()) {
            Object value = target.get(key);
            if (value instanceof JsonObject) {
                Object srcvalue = get(key);
                if (srcvalue instanceof JsonObject) {
                    ((JsonObject) srcvalue).merge((JsonObject) value);
                } else {
                    put(key, value);
                }
            } else {
                put(key, value);
            }
        }
        return this;
    }


    /**
     * Copy target and merge it, and may overwrite the original value.
     * Recursively merge when JsonObject, overwrite when JsonArray.
     *
     * @param target
     * @return  this
     */
    public JsonObject copyMerge(JsonObject target) {
        if (null == target) return this;

        for (String key : target.keySet()) {
            Object value = target.get(key);
            if (value instanceof JsonObject) {
                Object srcValue = get(key);
                if (null == srcValue) {
                    put(key, ((JsonObject) value).deepCopy());
                } else if (srcValue instanceof JsonObject) {
                    ((JsonObject) srcValue).copyMerge((JsonObject) value);
                } else {
                    put(key, ((JsonObject) value).deepCopy());
                }
            } else if (value instanceof JsonArray) {
                put(key, ((JsonArray) value).deepCopy());
            } else {
                put(key, value);
            }
        }
        return this;
    }

}
