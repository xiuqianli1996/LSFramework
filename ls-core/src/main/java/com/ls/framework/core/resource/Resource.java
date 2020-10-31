package com.ls.framework.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface Resource {

    File getFile();

    default boolean isFile() {
        return false;
    }

    InputStream getInputStream() throws FileNotFoundException;

}
