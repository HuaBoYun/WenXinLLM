package com.huabo.legal.exam.core.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于Spring BeanUtils的Bean映射工具类
 */
public class BeanMapper {

	/**
     * 基于Spring BeanUtils转换对象的类型
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        try {
            T destination = destinationClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, destination);
            return destination;
        } catch (Exception e) {
            throw new RuntimeException("Bean mapping failed", e);
        }
    }

    /**
     * 基于Spring BeanUtils转换Collection中对象的类型
     */
    public static <T> List<T> mapList(Iterable<?> sourceList, Class<T> destinationClass) {
        if (sourceList == null) {
            return new ArrayList<>();
        }
        
        List<T> destinationList = new ArrayList<>();
        for (Object sourceObject : sourceList) {
            if (sourceObject != null) {
                destinationList.add(map(sourceObject, destinationClass));
            }
        }
        return destinationList;
    }

    /**
     * 基于Spring BeanUtils将对象A的值拷贝到对象B中
     */
    public static void copy(Object source, Object destination) {
        if (source != null && destination != null) {
            BeanUtils.copyProperties(source, destination);
        }
    }

    /**
     * 拷贝非空属性
     */
    public static void copyNonNullProperties(Object source, Object destination) {
        if (source != null && destination != null) {
            BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
        }
    }

    /**
     * 获取为null的属性名
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }

    /**
     * 使用Function进行映射（保持原有方法）
     */
    public static <T, S> List<T> mapList(Collection<S> source, Function<? super S, ? extends T> mapper) {
        if (source == null) {
            return new ArrayList<>();
        }
        return source.stream()
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.toList());
    }
}