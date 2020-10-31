package com.ls.framework.jdbc.util;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.jdbc.exception.LSJdbcException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class JdbcConvertKit {

    public static Object convert(ResultSet resultSet, String name, Class<?> typeClass) {
        try {
            if (typeClass == LocalDate.class) {
                return Optional.ofNullable(resultSet.getTimestamp(name)).map(Timestamp::toInstant).map(LocalDate::from).orElse(null);
            } else if (typeClass == LocalDateTime.class) {
                return Optional.ofNullable(resultSet.getTimestamp(name)).map(Timestamp::toLocalDateTime).orElse(null);
            } else {
                return resultSet.getObject(name, typeClass);
            }
        } catch (Exception e) {
            throw new LSJdbcException(e);
        }
//        try {
//            if (typeClass == String.class) {
//                return resultSet.getString(name);
//            } else if (typeClass == Integer.class || typeClass == int.class){
//                return resultSet.getInt(name);
//            } else if (typeClass == Long.class || typeClass == long.class){
//                return resultSet.get(name);
//            } else if (typeClass == Boolean.class || typeClass == boolean.class){
//                return resultSet.getBoolean(name);
//            } else if (typeClass == Float.class || typeClass == float.class){
//                return resultSet.getFloat(name);
//            } else if (typeClass == Double.class || typeClass == double.class){
//                return resultSet.getDouble(name);
//            }  else if (typeClass == BigDecimal.class){
//                return resultSet.getBigDecimal(name);
//            }  else if (typeClass == BigInteger.class){
//                return resultSet.getBigDecimal(name).toBigInteger();
//            } else if (typeClass == Date.class){
//                return resultSet.getDate(name);
//            } else if (typeClass == LocalDate.class) {
//                return Optional.ofNullable(resultSet.getTimestamp(name)).map(Timestamp::toInstant).map(LocalDate::from).orElse(null);
//            } else if (typeClass == LocalDateTime.class) {
//                return Optional.ofNullable(resultSet.getTimestamp(name)).map(Timestamp::toLocalDateTime).orElse(null);
//            }
//            throw new LSException("unsupported type: " + typeClass);
//        } catch (Exception e) {
//            throw new LSException(e);
//        }
    }

}
