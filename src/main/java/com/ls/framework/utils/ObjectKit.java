package com.ls.framework.utils;

import java.util.List;

public class ObjectKit {

    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmptyArray(Object[] arr) {
        return arr == null || arr.length == 0;
    }
}
