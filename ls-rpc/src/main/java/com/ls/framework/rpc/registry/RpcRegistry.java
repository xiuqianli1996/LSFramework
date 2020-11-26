package com.ls.framework.rpc.registry;

import java.util.List;

public interface RpcRegistry {

    void register(InstanceObject instanceObject);

    void unregister(InstanceObject instanceObject);

    List<InstanceObject> discover(String serviceName, String group);

}
