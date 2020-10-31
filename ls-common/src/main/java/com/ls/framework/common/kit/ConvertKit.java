package com.ls.framework.common.kit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

public class ConvertKit {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化， 不带时间
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    /**
     * 日期+时间格式化
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static Object convert(Object obj, Class typeClass){
        if (obj == null) {
            return null;
        }
        String str = obj.toString();
        if (typeClass == String.class)
            return str;
        str = str.trim();
        if (typeClass == Integer.class || typeClass == int.class){
            return Integer.parseInt(str);
        } else if (typeClass == Long.class || typeClass == long.class){
            return Long.parseLong(str);
        } else if (typeClass == Boolean.class || typeClass == boolean.class){
            return Boolean.parseBoolean(str);
        } else if (typeClass == Float.class || typeClass == float.class){
            return Float.parseFloat(str);
        } else if (typeClass == Double.class || typeClass == double.class){
            return Double.parseDouble(str);
        }  else if (typeClass == BigDecimal.class){
            return new BigDecimal(str);
        }  else if (typeClass == BigInteger.class){
            return new BigInteger(str);
        } else if (typeClass == Date.class){
            if(str.length() > DATE_PATTERN.length()){
                //字符串长度大于日期格式按日期时间格式算
                return localDateTimeToDate(LocalDateTime.parse(str, DATE_TIME_FORMATTER));
            } else {
                return localDateToDate(LocalDate.parse(str, DATE_FORMATTER));
            }
        } else if (typeClass == LocalDate.class) {
            return LocalDate.parse(str, DATE_FORMATTER);
        } else if (typeClass == LocalDateTime.class) {
            return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
        }
        return null;
    }

    public static Date localDateToDate(LocalDate localDate) {
        return localDateTimeToDate(localDate.atStartOfDay());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
