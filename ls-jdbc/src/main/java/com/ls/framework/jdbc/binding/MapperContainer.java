package com.ls.framework.jdbc.binding;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperContainer {
    public static Map<String, MapperData> mapperMethodMap = new ConcurrentHashMap<>();
}
