package com.outofstack.metaplus.common.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class JsonObjectProxy4Fastjson2 implements JsonObjectProxy {
    protected JSONObject jo;

    public JsonObjectProxy4Fastjson2() {
        jo = new JSONObject();
    }

    protected JsonObjectProxy4Fastjson2(JSONObject jo) {
        this.jo = jo;
    }

    public static JsonObjectProxy4Fastjson2 parse(Reader reader) {
        JSONObject jo;
        try {
            jo = JSON.parseObject(reader);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
        if (null == jo) throw new JsonException("Parse to JsonObject failed");
        return new JsonObjectProxy4Fastjson2(jo);
    }

    public static String object2JsonString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public String toJson() {
        try {
            return jo.toString();
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public int hashCode() {
        return jo.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof JsonObjectProxy4Fastjson2) {
            return jo.equals(((JsonObjectProxy4Fastjson2) object).jo);
        }
        return false;
    }

    @Override
    public int size() {
        return jo.size();
    }

    @Override
    public boolean isEmpty() {
        return jo.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<String>(jo.keySet());
    }

    @Override
    public boolean containsKey(String key) {
        return jo.containsKey(key);
    }

    @Override
    public Object getObject(String key) {
        Object v = jo.get(key);
        if (v instanceof JSONObject) {
            return new JsonObjectProxy4Fastjson2((JSONObject) v);
        } else if (v instanceof JSONArray) {
            return new JsonArrayProxy4Fastjson2((JSONArray) v);
        } else {
            return v;
        }
    }

    @Override
    public String getString(String key) {
        return jo.getString(key);
    }

    @Override
    public Long getLong(String key) {
        try {
            return jo.getLong(key);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Integer getInteger(String key) {
        try {
            return jo.getInteger(key);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Short getShort(String key) {
        try {
            return jo.getShort(key);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Double getDouble(String key) {
        try {
            return jo.getDouble(key);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Float getFloat(String key) {
        try {
            return jo.getFloat(key);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public Boolean getBoolean(String key) {
        try {
            return jo.getBoolean(key);
        } catch (RuntimeException e) {
            throw new JsonException(e);
        }
    }

//    @Override
//    public byte[] getBytes(String key) {
//        return jo.getBytes(key);
//    }

    @Override
    public JsonObjectProxy getJsonObjectProxy(String key) {
        try {
            Object v = jo.get(key);
            if (null == v) {
                return null;
            } else if (v instanceof JSONObject){
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
    public JsonArrayProxy getJsonArrayProxy(String key) {
        try {
            Object v = jo.get(key);
            if (null == v) {
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
    public void put(String key, Object value) {
        if (value instanceof JsonObjectProxy4Fastjson2) {
            jo.put(key, ((JsonObjectProxy4Fastjson2) value).jo);
        } else if (value instanceof JsonArrayProxy4Fastjson2) {
            jo.put(key, ((JsonArrayProxy4Fastjson2) value).ja);
        } else {
            jo.put(key, value);
        }
    }

    @Override
    public void remove(String key) {
        jo.remove(key);
    }

}
