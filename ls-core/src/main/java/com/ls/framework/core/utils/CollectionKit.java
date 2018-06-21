package com.ls.framework.core.utils;

import java.util.List;

public class CollectionKit {

    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmptyArray(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean inArray(Object[] arr, Object obj) {
        if (isEmptyArray(arr))
            return false;
        for (Object o : arr) {
            if (o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
