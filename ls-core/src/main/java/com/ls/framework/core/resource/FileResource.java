package com.ls.framework.core.resource;

import com.ls.framework.core.constant.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResource implements Resource {

    private String location;

    public FileResource(String location) {
        this.location = location;
        if (location.startsWith(Constants.FILE_RESOURCE_PREFIX)) {
            this.location = location.substring(Constants.FILE_RESOURCE_PREFIX.length());
        }
    }

    @Override
    public File getFile() {
        return new File(location);
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(location);
    }

    @Override
    public String toString() {
        return "FileResource location=" + location;
    }
}
