package com.ls.framework.rpc.protocol;

public interface RpcProtocol {

    /**
     * 将request对象写到二进制流用于请求。
     * @param rpcRequest
     * @return
     */
    byte[] serializeRequest(RpcRequest rpcRequest);

    RpcRequest deserializeRequest(byte[] bytes);

    byte[] serializeResponse(RpcResponse rpcResponse);

    RpcResponse deserializeResponse(byte[] bytes);

}
