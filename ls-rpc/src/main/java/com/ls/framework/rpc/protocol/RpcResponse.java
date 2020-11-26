package com.ls.framework.rpc.protocol;

import lombok.Data;

import java.util.Map;

@Data
public class RpcResponse {
    /**
     * 请求id
     */
    private long requestId;
    /**
     * 是否有异常
     */
    private boolean hasException;
    /**
     * 调用结果
     */
    private Object data;

    /**
     * 隐式出参
     */
    private Map<String, Object> attachments;
}
