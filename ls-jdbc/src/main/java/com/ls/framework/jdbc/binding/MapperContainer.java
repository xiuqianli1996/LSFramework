package com.ls.framework.jdbc.binding;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperContainer {
    public static Map<Method, MapperData> mapperMethodMap = new ConcurrentHashMap<>();
}
