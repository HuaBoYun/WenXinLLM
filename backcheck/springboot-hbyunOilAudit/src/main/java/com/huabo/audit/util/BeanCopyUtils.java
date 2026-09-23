package com.huabo.audit.util;

import com.huabo.audit.exception.CommercialException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/** 反射实现 效率一般
 * @author lyz
 * @description
 */
public class BeanCopyUtils {

    public static void copyProperties(Object source, Object target) throws Exception {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 拷贝Bean对象
     *
     * @param source 源对象
     * @param clazz  目标对象额Class
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        Object target = null;
        try {
            target = clazz.newInstance();
            copyProperties(source, target);
            return (T) target;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommercialException(e.getMessage());
        }

    }

    /**
     * 拷贝List集合
     *
     * @param source 源对象集合
     * @param clazz  目标对象Class
     * @param <T>
     * @return
     */
    public static <T> List<T> copyListProperties(List source, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (Object o : source) {
            Object target = copyProperties(o, clazz);
            list.add((T) target);
        }
        return list;
    }
}