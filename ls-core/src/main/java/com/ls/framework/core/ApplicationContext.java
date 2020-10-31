package com.ls.framework.core;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.common.kit.*;
import com.ls.framework.core.annotation.LSApplication;
import com.ls.framework.core.constant.Constants;
import com.ls.framework.core.listener.ApplicationContextListener;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.property.*;
import com.ls.framework.core.property.binder.DefaultPropertyBinder;
import com.ls.framework.core.property.binder.PropertyBinder;
import com.ls.framework.core.property.binder.PropertyBinderService;
import com.ls.framework.core.resource.ResourceFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class ApplicationContext {

    @Getter
    private Class<?> sourceClass;

    private Set<Class<?>> classSet;

    private List<Loader> loaderList;
    @Getter
    private List<ApplicationContextListener> applicationContextListeners;

    private CountDownLatch countDownLatch;

    private Set<String> includePackages;

    private Set<String> excludePackages;

    private PropertySourceFactory propertySourceFactory;

    private String propertiesLocation;

    private boolean inited = false;

    @Setter
    private Class<? extends PropertyBinder> propertyBinderClass = DefaultPropertyBinder.class;

    public static ApplicationContext run(Class<?> sourceClass, String... args) {
        return run(sourceClass, null, args);
    }

    /**
     * @param sourceClass
     * @param beforeInitOperation 用于在applicationContext启动前做一些registerClass之类的操作
     * @param args
     * @return
     */
    public static ApplicationContext run(Class<?> sourceClass, Consumer<ApplicationContext> beforeInitOperation, String... args) {
        if (!sourceClass.isAnnotationPresent(LSApplication.class)) {
            throw new LSException("sourceClass should annotated with com.ls.framework.core.annotation.LSApplication, source class: " + sourceClass.getName());
        }
        LSApplication application = sourceClass.getAnnotation(LSApplication.class);

        Set<String> includePackages = new HashSet<>();
        Set<String> excludePackages = Collections.emptySet();

        // 默认扫描框架包名
        includePackages.add("com.ls.framework");
        includePackages.add(sourceClass.getPackage().getName());
        if (CollectionKit.notEmpty(application.includePackage())) {
            includePackages.addAll(new HashSet<>(Arrays.asList(application.includePackage())));
        }

        if (CollectionKit.notEmpty(application.excludePackage())) {
            excludePackages = new HashSet<>(Arrays.asList(application.excludePackage()));
        }

        ApplicationContext applicationContext = new ApplicationContext(application.value(), includePackages, excludePackages);

        applicationContext.sourceClass = sourceClass;
        applicationContext.registerClass(sourceClass);
        if (beforeInitOperation != null) {
            beforeInitOperation.accept(applicationContext);
        }

        applicationContext.init();

        return applicationContext;
    }

    public ApplicationContext(String propertiesLocation, Set<String> includePackages, Set<String> excludePackages) {
        this.propertiesLocation = propertiesLocation;
        this.includePackages = includePackages.stream().filter(StrKit::notBlank).collect(Collectors.toSet());
        this.excludePackages = excludePackages.stream().filter(StrKit::notBlank).collect(Collectors.toSet());
        classSet = initClassSet();
    }

    /**
     * 可以单独注册不方便扫描的class到context, 方便后续loader组件对class进行处理
     * 必须在init调用前注册才能生效
     * @param classes
     * @return
     */
    public ApplicationContext registerClass(Class<?>... classes) {
        classSet.addAll(Arrays.asList(classes));
        return this;
    }

    public String getProperty(String key) {
        return propertySourceFactory.get(key);
    }

    public String getProperty(String key, String defaultVal) {
        return propertySourceFactory.getOrDefault(key, defaultVal);
    }

    public void init() {
        inited = true;

        countDownLatch = new CountDownLatch(1);

        // 初始化
        PropertyBinderService.sharedBinder().setBinder(propertyBinderClass);

        initLoaderAndListener();

        initPropertySource();

        startLoaders();

        registerShutdown();

    }

    private void setApplicationContextToListeners() {
        applicationContextListeners.forEach(listener -> listener.setApplicationContext(this));
    }

    private void initPropertySource() {
        propertySourceFactory = new PropertySourceFactory();

        // 先注册环境相关变量的配置源
        propertySourceFactory.registerPropertySource(new SystemEnvPropertySource());
        propertySourceFactory.registerPropertySource(new SystemPropertySource());

        // 加载默认配置文件配置源
        FilePropertySource defaultFilePropertySource = new FilePropertySource(ResourceFactory.getResource(propertiesLocation));
        propertySourceFactory.registerPropertySource(defaultFilePropertySource);
        // 尝试加载activeProfile配置源
        String profile = propertySourceFactory.get(Constants.CONFIG_ACTIVE_PROFILE);
        log.info("active profile: {}", StrKit.orDefault(profile, () -> "default"));

        if (StrKit.notBlank(profile)) {
            // 转换文件名，classpath:app.properties -> classpath:app-{profile}.properties
            String fileName = propertiesLocation.replace(".properties", "-" + profile + ".properties");

            // profilePropertySource的order = defaultFilePropertySource.order() - 10，保证优先级高于默认配置文件
            FilePropertySource profilePropertySource = new FilePropertySource(ResourceFactory.getResource(fileName), defaultFilePropertySource.order() - 10);
            propertySourceFactory.registerPropertySource(profilePropertySource);
        }

        applicationContextListeners.forEach(listener -> listener.setPropertySourceFactory(propertySourceFactory));
    }

    private Set<Class<?>> initClassSet() {
        log.debug("includePackages: {}", includePackages);
        log.debug("excludePackages: {}", excludePackages);
        if (CollectionKit.isEmpty(includePackages)) {
            throw new RuntimeException("scan package can not be null");
        }
        Set<Class<?>> classSet = new HashSet<>(); //扫描配置包名下的所有类（递归）
        for (String packageName : includePackages) {
            Set<Class<?>> list = ClassKit.scanClassListByPkg(packageName);
            if (!CollectionKit.isEmpty(list))
                classSet.addAll(list);
        }
        // 排除指定包名下的类
        if (CollectionKit.notEmpty(excludePackages)) {
            classSet = classSet.stream()
                    .filter(clazz -> {
                        String packageName = clazz.getPackage().getName();
                        return excludePackages.stream()
                                .noneMatch(packageName::startsWith);
                    })
                    .collect(Collectors.toSet());
        }
        return classSet;
    }

    /**
     * 加载loader及applicationContextListener并获取实例后排序
     */
    private void initLoaderAndListener() {
        loaderList = ObjectKit.getInstancesByInterface(classSet, Loader.class);
        loaderList.sort(Comparator.comparingInt(Loader::order));
        log.debug("loaderList:{}", loaderList);

        applicationContextListeners = ObjectKit.getInstancesByInterface(classSet, ApplicationContextListener.class);
        applicationContextListeners.sort(Comparator.comparingInt(ApplicationContextListener::order));
        log.debug("applicationContextListeners:{}", applicationContextListeners);
    }

    private void startLoaders() {
        applicationContextListeners.forEach(ApplicationContextListener::beforeStartLoaders);
        // 启动
        loaderList.forEach(loader -> {
            Logger log = LoggerFactory.getLogger(loader.getClass());
            log.info("loading...");
            loader.doLoad(this, classSet);
            log.info("load success");
        });
        applicationContextListeners.forEach(ApplicationContextListener::afterStartLoaders);
    }

    public void await() {
        if (!inited) {
            init();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new LSException("applicationContext await error", e);
        }
    }

    public List<Loader> getLoaders() {
        return Collections.unmodifiableList(loaderList);
    }

    private void registerShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void shutdown() {
        countDownLatch.countDown();
        loaderList.forEach(loader -> {
            Logger log = LoggerFactory.getLogger(loader.getClass());
            loader.shutdown();
            log.info("{} shutdown", loader.getClass().getSimpleName());
        });
    }

}
