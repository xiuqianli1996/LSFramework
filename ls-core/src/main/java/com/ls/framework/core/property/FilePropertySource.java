package com.ls.framework.core.property;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.core.resource.Resource;

import java.io.IOException;
import java.util.Properties;

public class FilePropertySource extends AbstractStringPropertySource {

    public FilePropertySource(Resource resource) {
        this(resource, Integer.MAX_VALUE);
    }

    public FilePropertySource(Resource resource, int order) {
        this.properties = new Properties();
        this.order = order;
        try {
            this.properties.load(resource.getInputStream());
        } catch (Exception e) {
            throw new LSException("load property error, please check the file path: " + resource, e);
        }
    }

}
