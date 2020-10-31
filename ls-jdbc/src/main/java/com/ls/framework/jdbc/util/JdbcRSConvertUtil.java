package com.ls.framework.jdbc.util;

import com.ls.framework.jdbc.exception.LSJdbcException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcRSConvertUtil {

//    public static <T>  List<T> convertObjectList(Class<T> clazz, List<Map<String, Object>> resultList) {
//        List<T> result = new ArrayList<>();
//        try {
//            for (Map<String, Object> map : resultList){ //遍历结果集
//                T bean = clazz.newInstance();//生成bean实例
//
//                Field[] fields = clazz.getDeclaredFields();//获取所有成员变量
//
//                for (Field field : fields){
//                    field.setAccessible(true);
//                    Object value = map.get(field.getName());
//                    if(null != value)
//                        field.set(bean, ConvertUtil.convert(value, field.getType()));//反射、给成员变量赋值
//                }
//
//                result.add(bean);
//            }
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new LSJdbcException(e.getCause());
//        }
//        return result;
//    }

}
