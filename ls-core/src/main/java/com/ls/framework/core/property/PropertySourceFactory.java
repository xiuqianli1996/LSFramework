package com.ls.framework.core.property;

import java.util.*;

public class PropertySourceFactory implements PropertySource<String> {

    private List<PropertySource<String>> propertySources = new LinkedList<>();

    public PropertySourceFactory registerPropertySource(PropertySource<String> propertySource) {
        Objects.requireNonNull(propertySource);
        propertySources.add(propertySource);
        propertySources.sort(Comparator.comparingInt(PropertySource::order));
        return this;
    }

    public void resort() {
        propertySources.sort(Comparator.comparingInt(PropertySource::order));
    }

    public List<PropertySource> getPropertySources() {
        return Collections.unmodifiableList(propertySources);
    }

    @Override
    public String get(String key) {
        return propertySources.stream()
                .map(propertySource -> propertySource.get(key))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    @Override
    public String getOrDefault(String key, String defaultVal) {
        return propertySources.stream()
                .map(propertySource -> propertySource.get(key))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(defaultVal);
    }
}
