package com.ls.framework.utils;

import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.io.File;

public class ClassUtil {

    private static List<String> classNames = new ArrayList<>();

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static List<String> scanClassNamesByPkg(String packageName) {
//        URL url = getClassLoader().getResource(packageName.replace(".", "/"));
        if (StringKit.isBlank(packageName)) {
            throw new RuntimeException("scan package can not be empty");
        }

        try {
            Enumeration<URL> urlEnumeration = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    addClass(url.getFile(), packageName);
                }
                //暂留：扫描jar
//                System.out.println(url);
            }
            return classNames;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<Class<?>> scanClassByAnnotation(Class<? extends Annotation> annotationClass) {
        List<Class<?>> classList = new ArrayList<>();
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(annotationClass)) {
                    classList.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(className + " load error");
            }
        }
        return classList;
    }

    private static void addClass(String packagePath, String packageName) {
        File classDir = new File(packagePath);

        File[] files = classDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        if (ObjectKit.isEmptyArray(files))
            return ;
        for (File file : files) {
            String fileJavaName = StringKit.isBlank(packageName) ? file.getName() : packageName + '.' + file.getName();
            if (file.isDirectory()) {

                List<String> list = scanClassNamesByPkg(fileJavaName);
                if (list != null)
                    classNames.addAll(list);
            } else {
                String className = fileJavaName.replace(".class", "");
                classNames.add(className);
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        Enumeration<URL> urlEnumeration = getClassLoader().getResources("com".replace(".", "/"));
//        while (urlEnumeration.hasMoreElements()) {
//            URL url = urlEnumeration.nextElement();
//            System.out.println(url);
//        }
        System.out.println();
        System.out.println(scanClassNamesByPkg(""));
    }
}
