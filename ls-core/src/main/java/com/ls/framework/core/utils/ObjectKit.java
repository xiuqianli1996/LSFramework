package com.ls.framework.core.utils;

/**
 * 辅助使用stream api进行filter
 */
public class ObjectKit {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean notNull(Object obj) {
        return obj != null;
    }

    public static Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
