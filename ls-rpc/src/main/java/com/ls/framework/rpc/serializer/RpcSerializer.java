package com.ls.framework.rpc.serializer;

public interface RpcSerializer {

    <T> T deserialize(Class<T> clazz, byte[] bytes);

    byte[] serialize(Object src);

}
