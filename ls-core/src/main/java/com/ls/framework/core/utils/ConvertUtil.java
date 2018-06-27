package com.ls.framework.core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtil {
    private static final String datePattern = "yyyy-MM-dd";
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public static Object convert(Object obj, Class typeClass){
        if (obj == null) {
            return null;
        }
        if (typeClass == String.class)
            return obj.toString();
        if (typeClass == Integer.class || typeClass == int.class){
            return Integer.parseInt(obj.toString());
        } else if (typeClass == Long.class || typeClass == long.class){
            return Long.parseLong(obj.toString());
        } else if (typeClass == Boolean.class || typeClass == boolean.class){
            return Boolean.parseBoolean(obj.toString());
        } else if (typeClass == Float.class || typeClass == float.class){
            return Float.parseFloat(obj.toString());
        } else if (typeClass == Double.class || typeClass == double.class){
            return Double.parseDouble(obj.toString());
        }  else if (typeClass == BigDecimal.class){
            return new BigDecimal(obj.toString());
        }  else if (typeClass == BigInteger.class){
            return new BigInteger(obj.toString());
        } else if (typeClass == Date.class){
            SimpleDateFormat simpleDateFormat = null;
            String s = obj.toString();
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
        }
        return null;
    }
}
