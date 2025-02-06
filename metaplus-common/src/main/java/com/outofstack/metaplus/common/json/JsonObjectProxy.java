package com.outofstack.metaplus.common.json;

import java.util.Set;

public interface JsonObjectProxy {

    String toJson();

    int size();
    boolean isEmpty();
    Set<String> keySet();
    boolean containsKey(String key);
    Object get(String key);
    String getString(String key);
    Long getLong(String key);
    Integer getInteger(String key);
    Short getShort(String key);
    Double getDouble(String key);
    Float getFloat(String key);
    Boolean getBoolean(String key);
//    byte[] getBytes(String key);
    JsonObjectProxy getJsonObjectProxy(String key);
    JsonArrayProxy getJsonArrayProxy(String key);

    void put(String key, Object value);
    void remove(String key);

}
