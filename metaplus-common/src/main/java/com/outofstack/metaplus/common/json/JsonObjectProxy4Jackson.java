package com.outofstack.metaplus.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JsonObjectProxy4Jackson implements JsonObjectProxy {

    protected ObjectNode on;

    public JsonObjectProxy4Jackson() {
        on = new ObjectMapper().createObjectNode();
    }

    protected JsonObjectProxy4Jackson(ObjectNode on) {
        this.on = on;
    }

    public static JsonObjectProxy4Jackson parse(Reader reader) {
        JsonNode jn;
        try {
            jn = new ObjectMapper().readTree(reader);
        } catch (RuntimeException | IOException e) {
            throw new JsonException(e);
        }
        if (null == jn) {
            throw new JsonException("Parse to JsonObject failed");
        } else if (jn.isObject()) {
            return new JsonObjectProxy4Jackson((ObjectNode) jn);
        } else {
            throw new JsonException("Can not parse '" + jn.getNodeType() + "' to JsonObject");
        }
    }

    public static String object2JsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String toJson() {
        try {
            return on.toString();
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int hashCode() {
        return on.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JsonObjectProxy4Jackson) {
            return on.equals(((JsonObjectProxy4Jackson) object).on);
        }
        return false;
    }

    @Override
    public int size() {
        return on.size();
    }

    @Override
    public boolean isEmpty() {
        return on.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        Set<String> ks = new HashSet<String>();
        Iterator<String> it = on.fieldNames();
        while (it.hasNext()) {
            ks.add(it.next());
        }
        return ks;
    }

    @Override
    public boolean containsKey(String key) {
        return on.has(key);
    }

    @Override
    public Object getObject(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isObject()) {
            return new JsonObjectProxy4Jackson((ObjectNode) v);
        } else if (v.isArray()) {
            return new JsonArrayProxy4Jackson((ArrayNode) v);
        } else if (v.isTextual()) {
            return v.textValue();
        } else if (v.isInt()) {
            return v.intValue();
        } else if (v.isLong()) {
            return v.longValue();
        } else if (v.isShort()) {
            return v.shortValue();
        } else if (v.isDouble()) {
            return v.doubleValue();
        } else if (v.isFloat()) {
            return v.floatValue();
        } else if (v.isBoolean()) {
            return v.booleanValue();
        } else {
            return v.toString();
        }
    }

    @Override
    public String getString(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else {
            return v.asText();
        }
    }

    @Override
    public Long getLong(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.longValue();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Long, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public Integer getInteger(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.intValue();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Integer, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public Short getShort(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.shortValue();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Short, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public Double getDouble(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.doubleValue();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Double, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public Float getFloat(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.floatValue();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Float, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public Boolean getBoolean(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isBoolean()) {
            return v.booleanValue();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Boolean, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

//    @Override
//    public byte[] getBytes(String key) {
//        return jo.getBytes(key);
//    }

    @Override
    public JsonObjectProxy getJsonObjectProxy(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isObject()) {
            return new JsonObjectProxy4Jackson((ObjectNode) v);
        } else if (v.isArray()) {
            throw new JsonException("Can not cast JsonArray to JsonObject, giving key '" + key +
                    "', value '" + v + "'");
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to JsonObject, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public JsonArrayProxy getJsonArrayProxy(String key) {
        JsonNode v = on.get(key);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isArray()) {
            return new JsonArrayProxy4Jackson((ArrayNode) v);
        } else if (v.isObject()) {
            throw new JsonException("Can not cast JsonObject to JsonArray, giving key '" + key +
                    "', value '" + v + "'");
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to JsonArray, giving key '" + key +
                    "', value '" + v + "'");
        }
    }

    @Override
    public void put(String key, Object value) {
        if (value instanceof JsonObjectProxy4Jackson) {
            on.set(key, ((JsonObjectProxy4Jackson) value).on);
        } else if (value instanceof JsonArrayProxy4Jackson) {
            on.set(key, ((JsonArrayProxy4Jackson) value).an);
        } else if (value instanceof Long){
            on.put(key, (Long) value);
        } else if (value instanceof Integer || value instanceof Short){
            on.put(key, (Integer) value);
        } else if (value instanceof Double || value instanceof Float){
            on.put(key, (Double) value);
        } else if (value instanceof Boolean) {
            on.put(key, (Boolean) value);
        } else if (value instanceof String){
            on.put(key, (String) value);
        } else if (null == value) {
            on.put(key, (String) null);
        } else {
            on.put(key, value.toString());
        }
    }

    @Override
    public void remove(String key) {
        on.remove(key);
    }

}
