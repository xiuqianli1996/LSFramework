package com.ls.framework.core.utils;

import java.util.Collection;
import java.util.List;

public class CollectionKit {

    public static boolean isEmptyCollection(Collection collection) {
        return collection == null || collection.isEmpty();
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

    public static boolean inCollection(Collection collection, Object obj) {
        if (isEmptyCollection(collection))
            return false;
        for (Object o : collection) {
            if (o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
