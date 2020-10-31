package com.ls.framework.core.resource;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceFactory {

    public static Resource getResource(String location) {
        log.debug("getResource, location: {}", location);
        if (StrKit.isBlank(location)) {
            throw new LSException("location can not be blank!!");
        }
        String[] arr = location.split(Constants.PROTOCOL_SPLIT);
        if (arr.length > 2) {
            throw new LSException("location format error!!");
        }
        if (arr.length == 1) {
            return new ClassPathResource(location);
        }

        switch (arr[0]) {
            case Constants.CLASS_RESOURCE:
                return new ClassPathResource(location);
            case Constants.FILE_RESOURCE:
                return new FileResource(location);
        }
        throw new LSException("can not get resource by protocol: " + arr[0]);
    }

}
