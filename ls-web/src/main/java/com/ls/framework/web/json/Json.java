package com.ls.framework.web.json;

public interface Json {
    String toJson(Object object);
    <T> T parse(String json, Class<T> typeClass);
}
