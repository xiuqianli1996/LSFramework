package com.ls.framework.common.kit;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropKit {
    private static Properties properties = new Properties();

    public static void use(String fileName){
        InputStream inputStream = null;
        inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(fileName + "not found!");
        } finally {
            if(null != inputStream)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static String get(String key){
        return get(key, "");
    }

    public static String get(String key, String defaultValue){
        return properties.getProperty(key, defaultValue);
    }

    public static Enumeration<?> propertyNames() {
        return properties.propertyNames();
    }
}
