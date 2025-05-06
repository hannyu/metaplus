package com.outofstack.metaplus.common.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.Reader;

public class JsonArrayProxy4Jackson implements JsonArrayProxy {
    protected ArrayNode an;

    public JsonArrayProxy4Jackson() {
        an = new ObjectMapper().createArrayNode();
    }

    protected JsonArrayProxy4Jackson(ArrayNode an) {
        this.an = an;
    }

    public static JsonArrayProxy4Jackson parse(Reader reader) {
        JsonNode jn;
        try {
            jn = new ObjectMapper().readTree(reader);
        } catch (RuntimeException | IOException e) {
            throw new JsonException(e);
        }
        if (null == jn) {
            throw new JsonException("Parse to JsonObject failed");
        } else if (jn.isArray()) {
            return new JsonArrayProxy4Jackson((ArrayNode) jn);
        } else {
            throw new JsonException("Can not parse '" + jn.getNodeType() + "' to JsonArray");
        }
    }

    @Override
    public String toJson() {
        try {
            return an.toString();
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int hashCode() {
        return an.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JsonArrayProxy4Jackson) {
            return an.equals(((JsonArrayProxy4Jackson) object).an);
        }
        return false;
    }

    @Override
    public int size() {
        return an.size();
    }

    @Override
    public boolean isEmpty() {
        return an.isEmpty();
    }

    @Override
    public Object getObject(int idx) {
        try {
            JsonNode v = an.get(idx);
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
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }

    }

    @Override
    public String getString(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else {
            return v.asText();
        }
    }

    @Override
    public Long getLong(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.asLong();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Long");
        }
    }

    @Override
    public Integer getInteger(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.asInt();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Integer");
        }
    }

    @Override
    public Short getShort(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return (short) v.asInt();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Short");
        }
    }

    @Override
    public Double getDouble(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return v.asDouble();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Double");
        }
    }

    @Override
    public Float getFloat(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isNumber()) {
            return (float) v.asDouble();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Float");
        }
    }

    @Override
    public Boolean getBoolean(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isBoolean()) {
            return v.asBoolean();
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to Boolean");
        }
    }

//    @Override
//    public byte[] getBytes(int idx) {
//        return ja.getBytes(idx);
//    }

    @Override
    public JsonObjectProxy getJsonObjectProxy(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isObject()) {
            return new JsonObjectProxy4Jackson((ObjectNode) v);
        } else if (v.isArray()) {
            throw new JsonException("Can not cast JsonArray to JsonObject");
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to JsonObject");
        }
    }

    @Override
    public JsonArrayProxy getJsonArrayProxy(int idx) {
        JsonNode v = an.get(idx);
        if (null == v || v.isNull()) {
            return null;
        } else if (v.isArray()) {
            return new JsonArrayProxy4Jackson((ArrayNode) v);
        } else if (v.isObject()) {
            throw new JsonException("Can not cast JsonObject to JsonArray");
        } else {
            throw new JsonException("Can not cast '" + v.getNodeType() + "' to JsonArray");
        }
    }

    @Override
    public void add(Object value) {
        try {
            if (value instanceof JsonObjectProxy4Jackson) {
                an.add(((JsonObjectProxy4Jackson) value).on);
            } else if (value instanceof JsonArrayProxy4Jackson) {
                an.add(((JsonArrayProxy4Jackson) value).an);
            } else if (value instanceof Long) {
                an.add((Long) value);
            } else if (value instanceof Integer || value instanceof Short) {
                an.add((Integer) value);
            } else if (value instanceof Double || value instanceof Float) {
                an.add((Double) value);
            } else if (value instanceof String) {
                an.add((String) value);
            } else if (value instanceof Boolean) {
                an.add((Boolean) value);
            } else if (null == value) {
                an.add((String)null);
            } else {
                an.add(value.toString());
            }
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void insert(int idx, Object value) {
        try {
            if (value instanceof JsonObjectProxy4Jackson) {
                an.insert(idx, ((JsonObjectProxy4Jackson) value).on);
            } else if (value instanceof JsonArrayProxy4Jackson) {
                an.insert(idx, ((JsonArrayProxy4Jackson) value).an);
            } else if (value instanceof Long) {
                an.insert(idx, (Long) value);
            } else if (value instanceof Integer || value instanceof Short) {
                an.insert(idx, (Integer) value);
            } else if (value instanceof Double || value instanceof Float) {
                an.insert(idx, (Double) value);
            } else if (value instanceof String) {
                an.insert(idx, (String) value);
            } else if (value instanceof Boolean) {
                an.insert(idx, (Boolean) value);
            } else if (null == value) {
                an.insert(idx, (String)null);
            } else {
                an.insert(idx, value.toString());
            }
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void remove(int idx) {
        try {
            an.remove(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

}
