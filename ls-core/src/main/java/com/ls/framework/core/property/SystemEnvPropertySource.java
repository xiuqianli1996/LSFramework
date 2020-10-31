package com.ls.framework.core.property;

import java.util.HashMap;
import java.util.Map;

public class SystemEnvPropertySource implements PropertySource<String> {

    public static final int ORDER = 0;

    private Map<String, String> env;

    public SystemEnvPropertySource() {
        this.env = new HashMap<>();
        System.getenv().forEach((key, val) -> {
            // 环境变量全转为小写，下划线转为点，获取时也不区分大小写
            env.put(key.toLowerCase().replace("_", "."), val);
        });
    }

    @Override
    public String get(String key) {
        if (key == null) {
            return env.get(key);
        }
        return env.get(key.toLowerCase());
    }

    @Override
    public String getOrDefault(String key, String defaultVal) {
        if (key == null) {
            return env.getOrDefault(key, defaultVal);
        }
        return env.getOrDefault(key.toLowerCase(), defaultVal);
    }

    @Override
    public int order() {
        return ORDER;
    }
}
