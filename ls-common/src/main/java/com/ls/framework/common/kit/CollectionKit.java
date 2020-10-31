package com.ls.framework.common.kit;

import java.util.Arrays;
import java.util.Collection;

public class CollectionKit {

    public static boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean notEmpty(Object[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean in(Object[] arr, Object obj) {
        if (isEmpty(arr))
            return false;
        return Arrays.asList(arr).contains(obj);
    }

    public static boolean in(Collection collection, Object obj) {
        if (isEmpty(collection))
            return false;
        return collection.contains(obj);
    }

}
