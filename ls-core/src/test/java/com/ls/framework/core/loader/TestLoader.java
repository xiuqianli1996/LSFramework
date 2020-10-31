package com.ls.framework.core.loader;

import com.ls.framework.core.ApplicationContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class TestLoader implements Loader {
    @Override
    public void doLoad(ApplicationContext context, Set<Class<?>> classSet) {
        log.info("==== classSet size:{}", classSet.size());
        log.info("==== context:{}", context);
    }
}
