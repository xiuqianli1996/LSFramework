package com.ls.framework.core.constant;

public class Constants {

    public static final String PROTOCOL_SPLIT = ":";

    public static final String CLASS_RESOURCE = "classpath";
    public static final String CLASS_RESOURCE_PREFIX = CLASS_RESOURCE + PROTOCOL_SPLIT;
    public static final String FILE_RESOURCE = "file";
    public static final String FILE_RESOURCE_PREFIX = FILE_RESOURCE + PROTOCOL_SPLIT;

    /**
     * 默认配置文件路径
     */
    public static final String DEFAULT_PROPERTIES_LOCATION = "application.properties";
    /**
     * 启用环境的配置key
     */
    public static final String CONFIG_ACTIVE_PROFILE = "app.profiles.active";

}
