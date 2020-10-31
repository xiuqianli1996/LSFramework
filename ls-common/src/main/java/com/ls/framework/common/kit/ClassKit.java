package com.ls.framework.common.kit;


import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassKit {

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
                .filter(clazz -> CollectionKit.in(clazz.getInterfaces(), interfaceClass))
                .collect(Collectors.toSet());
    }

    /**
     * 递归遍历包名下所有类
     * @param packageName 包名
     * @return
     */
    public static Set<Class<?>> scanClassListByPkg(String packageName) {
        if (StrKit.isBlank(packageName)) {
            throw new RuntimeException("scan package can not be empty");
        }
        Set<Class<?>> classSet = new HashSet<>();

        try {
            Enumeration<URL> urlEnumeration = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    // class文件
                    addFileClass(classSet, url.getFile(), packageName);
                } else if ("jar".equals(protocol)) {
                    // jar包里的class
                    addJarClass(classSet, (JarURLConnection) url.openConnection(), packageName);
                }
            }
            return classSet;
        } catch (IOException e) {
            throw new IllegalStateException("scan class error", e);
        }
    }
    
    private static void addFileClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File classDir = new File(packagePath);
        File[] files = classDir.listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        if (CollectionKit.isEmpty(files)) {
            return;
        }
        for (File file : files) {
            String fileJavaName = StrKit.isBlank(packageName) ? file.getName() : packageName + '.' + file.getName();
            if (file.isDirectory()) {
                Set<Class<?>> set = scanClassListByPkg(fileJavaName);
                if (CollectionKit.notEmpty(set)) {
                    classSet.addAll(set);
                }
            } else {
                String className = fileJavaName.replace(".class", "");
                addClass(classSet, className);
            }
        }
    }

    /**
     * 添加jar包里的类
     * @param classSet
     * @param urlConnection
     * @param pkgName
     */
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
            throw new IllegalStateException("add jar class error", e);
        }

    }

    /**
     * 根据类名加载类加入结果集
     * @param classSet
     * @param className
     */
    private static void addClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            classSet.add(clazz);
        } catch (Exception e) {
            throw new IllegalStateException("add class error, name:" + className, e);
        }
    }

    public static String getFullMethodName(Method method) {
        return String.format("%s#%s", method.getDeclaringClass().getName(), method.getName());
    }

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass))
            return true;
        return Arrays.stream(clazz.getDeclaredMethods()).anyMatch(method -> method.isAnnotationPresent(annotationClass));
    }

    public static boolean notAbstract(Class<?> clazz) {
        return !Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 递归获取父类
     * @param clazz
     * @param predicate 中断的过滤条件，通常: clazz -> clazz != Object.class
     * @param consumer
     */
    public static void doWithSuperClass(Class<?> clazz, Predicate<Class<?>> predicate, Consumer<Class<?>> consumer) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || !predicate.test(superClass)) {
            return;
        }
        consumer.accept(superClass);
        doWithSuperClass(superClass, predicate, consumer);
    }
    public static void doWithInterfaces(Class<?> clazz, Consumer<Class<?>> consumer) {
        doWithInterfaces(clazz, c -> true, consumer);
    }

    /**
     * 递归获取实现的接口，消费实现接口类
     * @param clazz
     * @param predicate
     * @param consumer
     */
    public static void doWithInterfaces(Class<?> clazz, Predicate<Class<?>> predicate, Consumer<Class<?>> consumer) {
        if (clazz == null) {
            return;
        }
        Arrays.stream(clazz.getInterfaces())
                .filter(Objects::nonNull)
                .filter(predicate)
                .forEach(consumer);
        if (clazz.getSuperclass() != Object.class) {
            doWithInterfaces(clazz.getSuperclass(), predicate, consumer);
        }
    }

    public static Class<?> getGenericTypeClass(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

}
