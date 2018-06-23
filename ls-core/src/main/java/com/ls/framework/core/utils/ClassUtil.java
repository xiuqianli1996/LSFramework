package com.ls.framework.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ClassUtil {

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Set<Class<?>> getClassesByAnnotation(Set<Class<?>> classSet, Class<? extends Annotation> annotationClass) {
        return classSet.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotationClass))
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getClassesBySuper(Set<Class<?>> classSet, Class<?> superClass) {
        return classSet.stream()
                .filter(superClass::isAssignableFrom)
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getClassesByPkg(Set<Class<?>> classSet, String pkg) {
        return classSet.stream()
                .filter(clazz -> clazz.getName().startsWith(pkg))
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getClassesByInterface(Set<Class<?>> classSet, Class<?> interfaceClass) {
        return classSet.stream()
                .filter(clazz -> CollectionKit.inArray(clazz.getInterfaces(), interfaceClass))
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> scanClassListByPkg(String packageName) {
//        URL url = getClassLoader().getResource(packageName.replace(".", "/"));
        if (StringKit.isBlank(packageName)) {
            throw new RuntimeException("scan package can not be empty");
        }
        Set<Class<?>> classSet = new HashSet<>();

        try {
            Enumeration<URL> urlEnumeration = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
//                System.out.println(url);
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    addClass(classSet, url.getFile(), packageName);
                }
                //暂留：扫描jar
//                System.out.println(url);
            }
            return classSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File classDir = new File(packagePath);
//        System.out.println(classDir.exists());
        File[] files = classDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        if (CollectionKit.isEmptyArray(files))
            return ;
        for (File file : files) {
            String fileJavaName = StringKit.isBlank(packageName) ? file.getName() : packageName + '.' + file.getName();
            if (file.isDirectory()) {

                Set<Class<?>> set = scanClassListByPkg(fileJavaName);
                if (set != null)
                    classSet.addAll(set);
            } else {
                String className = fileJavaName.replace(".class", "");
                Class<?> clazz;
				try {
					clazz = Class.forName(className);
                    classSet.add(clazz);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("Can not load class:" + className);
				}
                
            }
        }
    }

}
