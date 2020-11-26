package com.ls.framework.rpc.registry;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstanceObject {
    private String ip;
    private int port;
    /**
     * 服务版本
     */
    private String version;
    /**
     * 服务分组
     */
    private String group;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 接口类名 todo 是否需要？
     */
    private String interfaceName;
}
