package com.outofstack.metaplus.common.json;


import com.outofstack.metaplus.common.lang.Pair;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
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

    public JsonObject(Map<String, ?> map) {
        this();
        putAll(map);
    }

    protected JsonObject(JsonObjectProxy jop) {
        this.jop = jop;
    }

    public <T extends JsonObject> T cast(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor(JsonObject.class);
            T newObj = constructor.newInstance(this);
            newObj.jop = jop;
            return newObj;
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

    public static String object2JsonString(Object object) {
        if (object instanceof JsonObject) {
            return ((JsonObject)object).toJson();
        } else if (object instanceof JsonArray) {
            return ((JsonArray)object).toJson();
        } else {
            return JsonProxyFactory.object2JsonString(object);
        }
    }

    public JsonObject toCopy() {
        JsonObject copy = new JsonObject();
        for (String key : keySet()) {
            Object value = getObject(key);
            copy.put(key, value);
        }
        return copy;
    }

    public JsonObject deepCopy() {
        JsonObject copy = new JsonObject();
        for (String key : keySet()) {
            Object value = getObject(key);
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

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T... reified) {
        if (reified.length > 0) throw new IllegalArgumentException("`reified` should be empty.");

        Class<?> rt = reified.getClass().getComponentType();
        if (rt.equals(Object.class)) {
            return (T) getObject(key);
        } else if (rt.equals(JsonObject.class)) {
            return (T) getJsonObject(key);
        } else if (rt.equals(JsonArray.class)) {
            return (T) getJsonArray(key);
        } else if (rt.equals(String.class)) {
            return (T) getString(key);
        } else if (rt.equals(Integer.class)) {
            return (T) getInteger(key);
        } else if (rt.equals(Long.class)) {
            return (T) getLong(key);
        } else if (rt.equals(Short.class)) {
            return (T) getShort(key);
        } else if (rt.equals(Double.class)) {
            return (T) getDouble(key);
        } else if (rt.equals(Float.class)) {
            return (T) getFloat(key);
        } else if (rt.equals(Boolean.class)) {
            return (T) getBoolean(key);
        }
        return (T) getObject(key);
    }

    public Object getObject(String key) {
        Object v = jop.getObject(key);
        if (v instanceof JsonObjectProxy) {
            return new JsonObject((JsonObjectProxy) v);
        } else if (v instanceof JsonArrayProxy) {
            return new JsonArray((JsonArrayProxy) v);
        } else {
            return v;
        }
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
            Object value = jsonObject.getObject(key);
            put(key, value);
        }
        return this;
    }

    public JsonObject putAll(Map<String, ?> map) {
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

    // jsonPath like '$.attr[0].name', support JsonObject and JsonArray
    @SuppressWarnings("unchecked")
    public <T> T getByPath(String jsonPath, T... reified) {
        if (reified.length > 0) throw new IllegalArgumentException("`reified` should be empty.");

        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().get(joNodePair.getRight(), reified);
        } else {
            return jaNodePair.getLeft().get(jaNodePair.getRight(), reified);
        }
    }


//    @SuppressWarnings("unchecked")
//    public <T> T getByPath(String jsonPath, T... reified) {
//        if (reified.length > 0) throw new IllegalArgumentException("This should not have any values.");
//
//        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
//        if (null == joNodePair) return null;
//
//        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
//        if (null == jaNodePair) {
//            return joNodePair.getLeft().get(joNodePair.getRight());
//        } else {
//            return jaNodePair.getLeft().get(jaNodePair.getRight());
//        }
//
//        Class<?> rt = reified.getClass().getComponentType();
//        if (rt.equals(String.class)) {
//            return (T) getString(key);
//        } else if (rt.equals(Short.class)) {
//            return (T) getShort(key);
//        }
//        return (T) get(key);
//    }



    public String getStringByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getString(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getString(jaNodePair.getRight());
        }
    }

    public Long getLongByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getLong(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getLong(jaNodePair.getRight());
        }
    }

    public Integer getIntegerByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getInteger(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getInteger(jaNodePair.getRight());
        }
    }

    public Short getShortByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getShort(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getShort(jaNodePair.getRight());
        }
    }

    public Double getDoubleByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getDouble(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getDouble(jaNodePair.getRight());
        }
    }

    public Float getFloatByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getFloat(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getFloat(jaNodePair.getRight());
        }
    }

    public Boolean getBooleanByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getBoolean(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getBoolean(jaNodePair.getRight());
        }
    }

    public JsonObject getJsonObjectByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getJsonObject(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getJsonObject(jaNodePair.getRight());
        }
    }

    public JsonArray getJsonArrayByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return null;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().getJsonArray(joNodePair.getRight());
        } else {
            return jaNodePair.getLeft().getJsonArray(jaNodePair.getRight());
        }
    }

    public boolean containsByPath(String jsonPath) {
        Pair<JsonObject, String> joNodePair = findLastSecondNode(this, jsonPath);
        if (null == joNodePair) return false;

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(joNodePair.getLeft(), joNodePair.getRight());
        if (null == jaNodePair) {
            return joNodePair.getLeft().containsKey(joNodePair.getRight());
        } else {
            return jaNodePair.getRight() >= 0 && jaNodePair.getRight() < jaNodePair.getLeft().size();
        }
    }

    /**
     *
     * @param jsonPath like '$.a.b.x', only support JsonObject and JsonArray
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
            Pair<JsonArray, Integer> jaNodePair = tryArrayNode(tempJo, nodes[i]);
            if (null == jaNodePair) {
                if (null == tempJo.getJsonObject(nodes[i])) {
                    tempJo.put(nodes[i], new JsonObject());
                }
                tempJo = tempJo.getJsonObject(nodes[i]);
            } else {
                tempJo = jaNodePair.getLeft().getJsonObject(jaNodePair.getRight());
                if (null == tempJo) {
                    throw new JsonException("Invalid pathNode: " + nodes[i]);
                }
            }
        }

        Pair<JsonArray, Integer> jaNodePair = tryArrayNode(tempJo, nodes[nodes.length-1]);
        if (null == jaNodePair) {
            tempJo.put(nodes[nodes.length-1], value);
        } else {
            jaNodePair.getLeft().add(jaNodePair.getRight(), value);
        }

        return this;
    }

    public JsonObject removeByPath(String jsonPath) {
        if (null == jsonPath || !jsonPath.startsWith("$.") || jsonPath.length() < 3) {
            throw new JsonException("Invalid jsonPath: " + jsonPath);
        }

        String[] nodes = jsonPath.split("\\.");
        // $, a, b, ..., x

        JsonObject tempJo = this;
        for (int i=1; i<nodes.length-1; i++) {
            Pair<JsonArray, Integer> jaNodePair = tryArrayNode(tempJo, nodes[i]);
            if (null == jaNodePair) {
                if (null == tempJo.getJsonObject(nodes[i])) {
                    tempJo.put(nodes[i], new JsonObject());
                }
                tempJo = tempJo.getJsonObject(nodes[i]);
            } else {
                tempJo = jaNodePair.getLeft().getJsonObject(jaNodePair.getRight());
                if (null == tempJo) {
                    throw new JsonException("Invalid pathNode: " + nodes[i]);
                }
            }
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
            Object value = target.getObject(key);
            if (value instanceof JsonObject) {
                Object srcvalue = getObject(key);
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
            Object value = target.getObject(key);
            if (value instanceof JsonObject) {
                Object srcValue = getObject(key);
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


    /**
     * abc[12]  => jsonArray, 12
     *
     */
    private static Pair<JsonArray, Integer> tryArrayNode(JsonObject jsonObject, String pathNode) {
        if (pathNode.endsWith("]")) {
            String[] ss = pathNode.split("\\[");
            if (ss.length == 2) {
                String key = ss[0];
                try {
                    int idx = Integer.parseInt(ss[1].substring(0, ss[1].length()-1));
                    JsonArray tempJa = jsonObject.getJsonArray(key);
                    if (null != tempJa) {
                        return new Pair<>(tempJa, idx);
                    }
                } catch (NumberFormatException e) {
                    throw new JsonException("Invalid pathNode: " + pathNode);
                }
            } else {
                throw new JsonException("Invalid pathNode: " + pathNode);
            }
        }
        return null;
    }

    private static Pair<JsonObject, String> findLastSecondNode(JsonObject jsonObject, String jsonPath) {
        if (null == jsonPath || !jsonPath.startsWith("$.") || jsonPath.length() < 3) {
            throw new JsonException("Invalid jsonPath: " + jsonPath);
        }

        String[] nodes = jsonPath.split("\\.");
        // $, a, b, ..., x

        JsonObject tempJo = jsonObject;
        for (int i=1; i<nodes.length-1; i++) {
            Pair<JsonArray, Integer> jaNodePair = tryArrayNode(tempJo, nodes[i]);
            if (null == jaNodePair) {
                tempJo = tempJo.getJsonObject(nodes[i]);
                if (null == tempJo) {
                    return null;
                }
            } else {
                JsonArray ja = jaNodePair.getLeft();
                if (null == ja) {
                    return null;
                } else {
                    tempJo = ja.getJsonObject(jaNodePair.getRight());
                    if (null == tempJo) {
                        return null;
                    }
                }
            }
        }
        return new Pair<>(tempJo, nodes[nodes.length-1]);
    }

}
