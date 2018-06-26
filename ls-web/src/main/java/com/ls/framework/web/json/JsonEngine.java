package com.ls.framework.web.json;

public interface JsonEngine {
    String toJson(Object object);
    <T> T parse(String json, Class<T> typeClass);
}
