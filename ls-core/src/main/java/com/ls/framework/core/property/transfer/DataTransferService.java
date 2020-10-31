package com.ls.framework.core.property.transfer;

import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.core.loader.Loader;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
public class DataTransferService implements Loader {

    private static List<DataTransfer> transfers;

    @SuppressWarnings("unchecked")
    public static  <T> T transfer(Object from, Class<T> toClass) {
        for (DataTransfer dataTransfer : transfers) {
            if (dataTransfer.match(from.getClass(), toClass)) {
                Object ret = dataTransfer.transfer(from, toClass);
                log.debug("{} transfer result: {}", dataTransfer.getClass().getName(), ret);
                if (ret != null) {
                    return (T) ret;
                }
            }
        }
        log.debug("can not get matched transfer, from obj: {}", from);
        return (T) from;
    }

    @Override
    public void doLoad(ApplicationContext context, Set<Class<?>> classSet) {
        transfers = ObjectKit.getInstancesByInterface(classSet, DataTransfer.class);
        transfers.sort(Comparator.comparingInt(DataTransfer::order));
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
