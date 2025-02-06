package com.outofstack.metaplus.common.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.io.Reader;

public class JsonArrayProxy4Fastjson2 implements JsonArrayProxy {
    protected JSONArray ja;

    public JsonArrayProxy4Fastjson2() {
        ja = new JSONArray();
    }

    protected JsonArrayProxy4Fastjson2(JSONArray ja) {
        this.ja = ja;
    }

    public static JsonArrayProxy4Fastjson2 parse(Reader reader) {
        try {
            JSONArray ja = JSON.parseArray(reader);
            return new JsonArrayProxy4Fastjson2(ja);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String toJson() {
        try {
            return ja.toString();
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int hashCode() {
        return ja.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JsonArrayProxy4Fastjson2) {
            return ja.equals(((JsonArrayProxy4Fastjson2) object).ja);
        }
        return false;
    }

    @Override
    public int size() {
        return ja.size();
    }

    @Override
    public boolean isEmpty() {
        return ja.isEmpty();
    }

    @Override
    public Object get(int idx) {
        try {
            Object v = ja.get(idx);
            if (v instanceof JSONObject) {
                return new JsonObjectProxy4Fastjson2((JSONObject) v);
            } else if (v instanceof JSONArray) {
                return new JsonArrayProxy4Fastjson2((JSONArray) v);
            } else {
                return v;
            }
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }

    }

    @Override
    public String getString(int idx) {
        return ja.getString(idx);
    }

    @Override
    public Long getLong(int idx) {
        try {
            return ja.getLong(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Integer getInteger(int idx) {
        try {
            return ja.getInteger(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Short getShort(int idx) {
        try {
            return ja.getShort(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Double getDouble(int idx) {
        try {
            return ja.getDouble(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Float getFloat(int idx) {
        try {
            return ja.getFloat(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Boolean getBoolean(int idx) {
        try {
            return ja.getBoolean(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

//    @Override
//    public byte[] getBytes(int idx) {
//        return ja.getBytes(idx);
//    }

    @Override
    public JsonObjectProxy getJsonObjectProxy(int idx) {
        try {
            Object v = ja.get(idx);
            if (v == null) {
                return null;
            } else if (v instanceof JSONObject) {
                return new JsonObjectProxy4Fastjson2((JSONObject) v);
            } else if (v instanceof JSONArray) {
                throw new JsonException("Can not cast JsonArray to JsonObject");
            } else {
                throw new JsonException("Can not cast '" + v.getClass() + "' to JsonObject");
            }
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public JsonArrayProxy getJsonArrayProxy(int idx) {
        try {
            Object v = ja.get(idx);
            if (v == null) {
                return null;
            } else if (v instanceof JSONArray) {
                return new JsonArrayProxy4Fastjson2((JSONArray) v);
            } else if (v instanceof JSONObject) {
                throw new JsonException("Can not cast JsonObject to JsonArray");
            } else {
                throw new JsonException("Can not cast '" + v.getClass() + "' to JsonArray");
            }
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void add(Object value) {
        if (value instanceof JsonObjectProxy) {
            ja.add(((JsonObjectProxy4Fastjson2) value).jo);
        } else if (value instanceof JsonArrayProxy) {
            ja.add(((JsonArrayProxy4Fastjson2) value).ja);
        } else {
            ja.add(value);
        }
    }

    @Override
    public void insert(int idx, Object value) {
        try {
            if (value instanceof JsonObjectProxy) {
                ja.add(idx, ((JsonObjectProxy4Fastjson2) value).jo);
            } else if (value instanceof JsonArrayProxy) {
                ja.add(idx, ((JsonArrayProxy4Fastjson2) value).ja);
            } else {
                ja.add(idx, value);
            }
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void remove(int idx) {
        try {
            ja.remove(idx);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

}
