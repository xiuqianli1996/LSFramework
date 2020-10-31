package com.ls.framework.core.resource;

import com.ls.framework.core.constant.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClassPathResource implements Resource {

    private String location;

    public ClassPathResource(String location) {
        this.location = location;
        if (location.startsWith(Constants.CLASS_RESOURCE_PREFIX)) {
            this.location = location.substring(Constants.CLASS_RESOURCE_PREFIX.length());
        }
    }

    @Override
    public File getFile() {
        return new File(location);
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
    }

    @Override
    public String toString() {
        return "ClassPathResource location=" + location;
    }
}
