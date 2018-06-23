package com.ls.framework.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParamKit {

    public static Map<String, String> buildPathParam(String url, Pattern pattern, List<String> paramNames) {
        Matcher matcher = pattern.matcher(url);
        Map<String, String> resultMap = new HashMap<>();
        if (matcher.find()) {
            for (int i = 0; i < paramNames.size(); i++) {
                String key = paramNames.get(i);
                String value = matcher.group(i + 1);
                resultMap.put(key, value);
            }
        }
        return resultMap;
    }

}
