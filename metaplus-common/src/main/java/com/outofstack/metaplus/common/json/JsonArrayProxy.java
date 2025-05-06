package com.outofstack.metaplus.common.json;

public interface JsonArrayProxy {

    String toJson();

    int size();
    boolean isEmpty();
    Object getObject(int idx);
    String getString(int idx);
    Long getLong(int idx);
    Integer getInteger(int idx);
    Short getShort(int idx);
    Double getDouble(int idx);
    Float getFloat(int idx);
    Boolean getBoolean(int idx);
//    byte[] getBytes(int idx);
    JsonObjectProxy getJsonObjectProxy(int idx);
    JsonArrayProxy getJsonArrayProxy(int idx);

    void add(Object value);
    void insert(int idx, Object value);
    void remove(int idx);
}
