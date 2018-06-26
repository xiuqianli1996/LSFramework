package com.ls.framework.web.json.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.aop.AopCallback;
import com.ls.framework.core.ioc.BeanInfo;
import com.ls.framework.web.exception.JsonSerializeException;
import com.ls.framework.web.json.JsonEngine;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.util.List;

@LSBean("DEFAULT_JSON")
public class GsonJsonEngine implements JsonEngine {
    private Gson gson = new Gson();
    @Override
    public String toJson(Object object) {
        if (Enhancer.isEnhanced(object.getClass())) { //除了测试的时候应该不会出现需要序列化强化类的情况下，应该不会进入这个分支
            Class<?> clazz = object.getClass();
            Field field = null;
            try {
                field = clazz.getDeclaredField("CGLIB$CALLBACK_0");
                field.setAccessible(true);
                Object fieldVal = field.get(object);
                if (!(fieldVal instanceof AopCallback)) {
                    throw new JsonSerializeException(clazz.getName());
                }
                AopCallback aopCallback = (AopCallback) fieldVal;
                object = aopCallback.getTarget();
            } catch (Exception e) {
                e.printStackTrace();
                throw new JsonSerializeException(clazz.getName());
            }
        }
        return gson.toJson(object);
    }

    @Override
    public <T> T parse(String json, Class<T> typeClass) {
        return (T) gson.fromJson(json, new TypeToken<List<BeanInfo>>(){}.getType());
    }
}
