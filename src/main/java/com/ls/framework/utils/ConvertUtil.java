package com.ls.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtil {
    private static final String datePattern = "yyyy-MM-dd";
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public static Object convert(String s, Class typeClass){
        if (typeClass == Integer.class || typeClass == int.class){
            return Integer.parseInt(s);
        } else if (typeClass == Long.class || typeClass == long.class){
            return Long.parseLong(s);
        } else if (typeClass == Boolean.class || typeClass == boolean.class){
            return Boolean.parseBoolean(s);
        } else if (typeClass == Float.class || typeClass == float.class){
            return Float.parseFloat(s);
        } else if (typeClass == Double.class || typeClass == double.class){
            return Double.parseDouble(s);
        } else if (typeClass == Date.class){
            SimpleDateFormat simpleDateFormat = null;
            s = s.trim();
            if(s.length() > datePattern.length()){//字符串长度大于日期格式按日期时间格式算
                simpleDateFormat = new SimpleDateFormat(dateTimePattern);
            } else {
                simpleDateFormat = new SimpleDateFormat(datePattern);
            }

            try {
                return simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }
        return s;
    }
}
