package com.ls.framework.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
                    addFileClass(classSet, url.getFile(), packageName);
                } else if ("jar".equals(protocol)) {
                    addJarClass(classSet, (JarURLConnection) url.openConnection(), packageName);
                }
            }
            return classSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    private static void addFileClass(Set<Class<?>> classSet, String packagePath, String packageName) {
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
                addClass(classSet, className);
            }
        }
    }

    private static void addJarClass(Set<Class<?>> classSet, JarURLConnection urlConnection, String pkgName) {
        if (urlConnection == null)
            return;
        try {
            JarFile jarFile = urlConnection.getJarFile();
            if (jarFile == null)
                return;
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                String entryName = jarEntry.getName().replaceAll("/", ".");
                if (entryName.endsWith(".class") && entryName.startsWith(pkgName)) {
                    addClass(classSet, entryName.replace(".class", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void addClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            classSet.add(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can not load class:" + className);
        } catch (NoClassDefFoundError e) {

        }
    }

    public static String getFullMethodName(Method method) {
        return String.format("%s.%s", method.getDeclaringClass().getName(), method.getName());
    }

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass))
            return true;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass))
                return true;
        }
        return false;
    }

}
