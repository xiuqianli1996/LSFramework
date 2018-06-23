package com.ls.framework.web.json.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.ioc.BeanInfo;
import com.ls.framework.web.json.Json;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

@LSBean("DEFAULT_JSON")
public class GsonJson implements Json {
    private Gson gson = new Gson();
    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T parse(String json, Class<T> typeClass) {
        return (T) gson.fromJson(json, new TypeToken<List<BeanInfo>>(){}.getType());
    }
}
