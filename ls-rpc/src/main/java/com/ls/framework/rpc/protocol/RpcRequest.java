package com.ls.framework.rpc.protocol;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RpcRequest {

    /**
     * 请求id, 用于
     */
    private long requestId;
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 接口名
     */
    private String interfaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法请求参数
     */
    private List<Object> params;

    /**
     * 隐式传参
     */
    private Map<String, Object> attachments;

}
