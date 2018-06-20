package com.ls.framework.utils;

import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;

public class ClassUtil {

    private static List<Class<?>> allClassList = new ArrayList<>();

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    public static void init(String param) {
        String[] pkgNames = param.split(",");
        for (String packageName : pkgNames) {
            List<Class<?>> list = scanClassListByPkg(packageName);
            if (!CollectionKit.isEmptyList(list))
                allClassList.addAll(list);
        }

	}

    public static List<Class<?>> scanClassListByPkg(String packageName) {
//        URL url = getClassLoader().getResource(packageName.replace(".", "/"));
        if (StringKit.isBlank(packageName)) {
            throw new RuntimeException("scan package can not be empty");
        }
        List<Class<?>> classList = new ArrayList<>();

        try {
            Enumeration<URL> urlEnumeration = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
//                System.out.println(url);
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    addClass(classList, url.getFile(), packageName);
                }
                //暂留：扫描jar
//                System.out.println(url);
            }
            return classList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    private static void addClass(List<Class<?>> classList, String packagePath, String packageName) {
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

                List<Class<?>> list = scanClassListByPkg(fileJavaName);
                if (list != null)
                    classList.addAll(list);
            } else {
                String className = fileJavaName.replace(".class", "");
                Class<?> clazz;
				try {
					clazz = Class.forName(className);
					classList.add(clazz);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("Can not load class:" + className);
				}
                
            }
        }
    }

    public static List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotationClass) {
        return allClassList.stream()
        		.filter(clazz -> clazz.isAnnotationPresent(annotationClass))
        		.collect(Collectors.toList());
    }
    
    public static List<Class<?>> getClassesBySuper(Class<?> superClass) {
    	return allClassList.stream()
        		.filter(superClass::isAssignableFrom)
        		.collect(Collectors.toList());
    }

    public static List<Class<?>> getClassesByPkg(String pkg) {
    	return allClassList.stream()
        		.filter(clazz -> clazz.getName().startsWith(pkg))
        		.collect(Collectors.toList());
    }
    

    public static void main(String[] args) throws IOException {
//        Enumeration<URL> urlEnumeration = getClassLoader().getResources("com".replace(".", "/"));
//        while (urlEnumeration.hasMoreElements()) {
//            URL url = urlEnumeration.nextElement();
//            System.out.println(url);
//        }
    	PropKit.use("application.properties");
    	ClassUtil.init(PropKit.get("app.scanPackage"));
        System.out.println(getAllClasses());
//        System.out.println(scanClassNamesByPkg(""));
    }

	public static List<String> getAllClassNames() {
		
		return allClassList.stream().map(clazz -> clazz.getName()).collect(Collectors.toList());
	}
	
	public static List<Class<?>> getAllClasses() {
		return allClassList;
	}
}
