package com.outofstack.metaplus.common.json;

import java.io.Reader;
import java.io.StringReader;

public class JsonArray {
    protected JsonArrayProxy jap;

    public JsonArray() {
        jap = JsonProxyFactory.createJsonArrayProxy();
    }

    public JsonArray(Object... objects) {
        this();
        add(objects);
    }

    protected JsonArray(JsonArrayProxy jap) {
        this.jap = jap;
    }

    public static JsonArray parse(Reader reader) {
        if (null == reader) throw new JsonException("reader is null");
        JsonArrayProxy jap = JsonProxyFactory.parseJsonArrayProxy(reader);
        return new JsonArray(jap);
    }

    public static JsonArray parse(String json) {
        return parse(new StringReader(json));
    }

    public JsonArray toCopy() {
        JsonArray copy = new JsonArray();
        int size = size();
        for (int i=0; i < size; i++) {
            Object value = getObject(i);
            copy.add(value);
        }
        return copy;
    }

    public JsonArray deepCopy() {
        JsonArray copy = new JsonArray();
        int size = size();
        for (int i=0; i < size; i++) {
            Object value = getObject(i);
            if (value instanceof JsonObject) {
                copy.add(((JsonObject) value).deepCopy());
            } else if (value instanceof JsonArray) {
                copy.add(((JsonArray) value).deepCopy());
            } else {
                copy.add(value);
            }
        }
        return copy;
    }

    public String toJson() {
        return jap.toJson();
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public int hashCode() {
        return jap.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JsonArray) {
            return jap.equals(((JsonArray) object).jap);
        }
        return false;
    }

    public int size() {
        return jap.size();
    }

    public boolean isEmpty() {
        return jap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int idx, T... reified) {
        if (reified.length > 0) throw new IllegalArgumentException("`reified` should be empty.");

        Class<?> rt = reified.getClass().getComponentType();
        if (rt.equals(JsonObjectProxy.class)) {
            return (T) getJsonObject(idx);
        } else if (rt.equals(JsonArray.class)) {
            return (T) getJsonArray(idx);
        } else if (rt.equals(String.class)) {
            return (T) getString(idx);
        } else if (rt.equals(Integer.class)) {
            return (T) getInteger(idx);
        } else if (rt.equals(Long.class)) {
            return (T) getLong(idx);
        } else if (rt.equals(Short.class)) {
            return (T) getShort(idx);
        } else if (rt.equals(Double.class)) {
            return (T) getDouble(idx);
        } else if (rt.equals(Float.class)) {
            return (T) getFloat(idx);
        } else if (rt.equals(Boolean.class)) {
            return (T) getBoolean(idx);
        }
        return (T) getObject(idx);
    }

    public Object getObject(int idx) {
        Object v = jap.getObject(idx);
        if (v instanceof JsonObjectProxy) {
            return new JsonObject((JsonObjectProxy) v);
        } else if (v instanceof JsonArrayProxy) {
            return new JsonArray((JsonArrayProxy) v);
        } else {
            return v;
        }
    }

    public String getString(int idx) {
        return jap.getString(idx);
    }

    public Long getLong(int idx) {
        return jap.getLong(idx);
    }

    public Integer getInteger(int idx) {
        return jap.getInteger(idx);
    }

    public Short getShort(int idx) {
        return jap.getShort(idx);
    }

    public Double getDouble(int idx) {
        return jap.getDouble(idx);
    }

    public Float getFloat(int idx) {
        return jap.getFloat(idx);
    }

    public Boolean getBoolean(int idx) {
        return jap.getBoolean(idx);
    }

    public JsonObject getJsonObject(int idx) {
        JsonObjectProxy v = jap.getJsonObjectProxy(idx);
        if (v == null) {
            return null;
        } else {
            return new JsonObject(v);
        }
    }

    public JsonArray getJsonArray(int idx) {
        JsonArrayProxy v = jap.getJsonArrayProxy(idx);
        if (v == null) {
            return null;
        } else {
            return new JsonArray(v);
        }
    }

    public JsonArray add(int idx, Object value) {
        if (value instanceof JsonObject) {
            jap.insert(idx, ((JsonObject) value).jop);
        } else if (value instanceof  JsonArray) {
            jap.insert(idx, ((JsonArray) value).jap);
        } else {
            jap.insert(idx, value);
        }
        return this;
    }

    public JsonArray add(Object value) {
        if (value instanceof JsonObject) {
            jap.add(((JsonObject) value).jop);
        } else if (value instanceof  JsonArray) {
            jap.add(((JsonArray) value).jap);
        } else {
            jap.add(value);
        }
        return this;
    }


    public JsonArray add(Object... values) {
        for (Object v : values) {
            if (v instanceof JsonObject) {
                jap.add(((JsonObject) v).jop);
            } else if (v instanceof  JsonArray) {
                jap.add(((JsonArray) v).jap);
            } else {
                jap.add(v);
            }
        }
        return this;
    }

    public JsonArray addAll(Object[] values) {
        for (Object v : values) {
            if (v instanceof JsonObject) {
                jap.add(((JsonObject) v).jop);
            } else if (v instanceof  JsonArray) {
                jap.add(((JsonArray) v).jap);
            } else {
                jap.add(v);
            }
        }
        return this;
    }

    public JsonArray addAll(JsonArray jsonArray) {
        int size = jsonArray.size();
        for (int i=0; i < size; i++) {
            this.add(jsonArray.getObject(i));
        }
        return this;
    }

    public JsonArray remove(int idx) {
        jap.remove(idx);
        return this;
    }

    public Object[] toArray() {
        Object[] arr = new Object[size()];
        for (int i=0; i<size(); i++) {
            arr[i] = getObject(i);
        }
        return arr;
    }
}
