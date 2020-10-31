package com.ls.framework.core.property;

public class SystemPropertySource extends AbstractStringPropertySource {

    private static final int ORDER = SystemEnvPropertySource.ORDER - 1;

    public SystemPropertySource() {
        this.properties = System.getProperties();
        this.order = ORDER;
    }

}
