package com.huabo.fxgl.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.huabo.fxgl.entity.TblRiskMonitoringFill;

public class RiskDataMerger {

	public static List<Map<String, Object>> mergeFillData(List<TblRiskMonitoringFill> dataList) {
	    // 定义需要处理的49个字段
	    final List<String> targetFields = Arrays.asList(
	        "zlString1","zlString2","zlInteger1", "zlInteger2", "zlInteger3", "zlInteger4", "zlInteger5",
	        "zlBigdecimal1", "zlBigdecimal2", "zlBigdecimal3", "zlBigdecimal4", 
	        "zlBigdecimal5", "zlBigdecimal6", "zlBigdecimal7", "zlBigdecimal8", "zlBigdecimal9",
	        "cwInteger1", "cwInteger2", "cwInteger3", "cwInteger4", "cwInteger5",
	        "cwBigdecimal1", "cwBigdecimal2", "cwBigdecimal3", "cwBigdecimal4", "cwBigdecimal5",
	        "cwBigdecimal6", "cwBigdecimal7", "cwBigdecimal8", "cwBigdecimal9", "cwBigdecimal10", "cwBigdecimal11",
	        "scInteger1", "scInteger2",
	        "scBigdecimal1", "scBigdecimal2", "scBigdecimal3", "scBigdecimal4", "scBigdecimal5", "scBigdecimal6",
	        "yyInteger1", "yyInteger2", "yyInteger3", "yyInteger4", "yyInteger5", "yyInteger6",
	        "yyBigdecimal1", "yyBigdecimal2", "yyBigdecimal3", "yyBigdecimal4",
	        "flInteger1", "flInteger2", "flBigdecimal1","qtString1","linkOrgId"
	    );
	    // 结果Map（存放合并后的字段值）
	    Map<String, Object> mergedMap = new HashMap<>();
	    // 字段反射缓存（提高性能）
	    Map<String, Field> fieldCache = new HashMap<>();
	    try {
	        // 初始化字段反射
	        for (String fieldName : targetFields) {
	            Field field = TblRiskMonitoringFill.class.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            fieldCache.put(fieldName.toUpperCase(), field);
	            mergedMap.put(fieldName.toUpperCase(), null); // 初始化所有字段为null
	        }
	        
	        // 遍历每个数据对象
	        for (TblRiskMonitoringFill data : dataList) {
	            // 遍历每个目标字段
	            for (String fieldName : targetFields) {
	                // 如果当前字段已有值则跳过
	                if (mergedMap.get(fieldName.toUpperCase()) != null) continue;
	                // 使用反射获取字段值
	                Field field = fieldCache.get(fieldName.toUpperCase());
	                Object value = field.get(data);
	                // 更新第一个非空值
	                if (isNotNullAndNotZero(value)) {
	                	  mergedMap.put(fieldName.toUpperCase(), value);
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("数据整合失败", e);
	    }
	    
	    // 封装为List<Map>返回
	    return Collections.singletonList(mergedMap);
	}

	
	public static List<Map<String, Object>> mergeFillDataDes(List<TblRiskMonitoringFill> dataList) {
	    // 定义需要处理的49个字段
	    final List<String> targetFields = Arrays.asList(
	        "zlString1Des","zlString2Des","zlInteger1Des", "zlInteger2Des", "zlInteger3Des", "zlInteger4Des", "zlInteger5Des",
	        "zlBigdecimal1Des", "zlBigdecimal2Des", "zlBigdecimal3Des", "zlBigdecimal4Des", 
	        "zlBigdecimal5Des", "zlBigdecimal6Des", "zlBigdecimal7Des", "zlBigdecimal8Des", "zlBigdecimal9Des",
	        "cwInteger1Des", "cwInteger2Des", "cwInteger3Des", "cwInteger4Des", "cwInteger5Des",
	        "cwBigdecimal1Des", "cwBigdecimal2Des", "cwBigdecimal3Des", "cwBigdecimal4Des", "cwBigdecimal5Des",
	        "cwBigdecimal6Des", "cwBigdecimal7Des", "cwBigdecimal8Des", "cwBigdecimal9Des", "cwBigdecimal10Des", "cwBigdecimal11Des",
	        "scInteger1Des", "scInteger2Des",
	        "scBigdecimal1Des", "scBigdecimal2Des", "scBigdecimal3Des", "scBigdecimal4Des", "scBigdecimal5Des", "scBigdecimal6Des",
	        "yyInteger1Des", "yyInteger2Des", "yyInteger3Des", "yyInteger4Des", "yyInteger5Des", "yyInteger6Des",
	        "yyBigdecimal1Des", "yyBigdecimal2Des", "yyBigdecimal3Des", "yyBigdecimal4Des",
	        "flInteger1Des", "flInteger2Des", "flBigdecimal1Des","qtString1Des"
	    );
	    // 结果Map（存放合并后的字段值）
	    Map<String, Object> mergedMap = new HashMap<>();
	    // 字段反射缓存（提高性能）
	    Map<String, Field> fieldCache = new HashMap<>();
	    try {
	        // 初始化字段反射
	        for (String fieldName : targetFields) {
	            Field field = TblRiskMonitoringFill.class.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            fieldCache.put(fieldName.toUpperCase(), field);
	            mergedMap.put(fieldName.toUpperCase(), null); // 初始化所有字段为null
	        }
	        
	        // 遍历每个数据对象
	        for (TblRiskMonitoringFill data : dataList) {
	            // 遍历每个目标字段
	            for (String fieldName : targetFields) {
	                // 如果当前字段已有值则跳过
	                if (mergedMap.get(fieldName.toUpperCase()) != null) continue;
	                
	                // 使用反射获取字段值
	                Field field = fieldCache.get(fieldName.toUpperCase());
	                Object value = field.get(data);
	                
	                // 更新第一个非空值
	                if (value != null) {
	                    mergedMap.put(fieldName.toUpperCase(), value);
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("数据整合失败", e);
	    }
	    
	    // 封装为List<Map>返回
	    return Collections.singletonList(mergedMap);
	}
	
	
	public static List<Map<String, Object>> mergeFillDataZdDes(List<TblRiskMonitoringFill> dataList) {
	    // 定义需要处理的49个字段
	    final List<String> targetFields = Arrays.asList(
	        "aqBigdecimal1Des","aqBigdecimal2Des","aqBigdecimal3Des", "aqBigdecimal4Des", "aqBigdecimal5Des", "aqBigdecimal6Des",
	        "aqBigdecimal11Des","aqBigdecimal12Des", "aqBigdecimal13Des", "aqBigdecimal14Des", 
	        "aqBigdecimal7Des","aqBigdecimal8Des", "aqBigdecimal9Des", "aqBigdecimal10Des"
	        , "cwBigdecimal8Des", "cwBigdecimal12Des"
	    );
	    // 结果Map（存放合并后的字段值）
	    Map<String, Object> mergedMap = new HashMap<>();
	    // 字段反射缓存（提高性能）
	    Map<String, Field> fieldCache = new HashMap<>();
	    try {
	        // 初始化字段反射
	        for (String fieldName : targetFields) {
	            Field field = TblRiskMonitoringFill.class.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            fieldCache.put(fieldName.toUpperCase(), field);
	            mergedMap.put(fieldName.toUpperCase(), null); // 初始化所有字段为null
	        }
	        
	        // 遍历每个数据对象
	        for (TblRiskMonitoringFill data : dataList) {
	            // 遍历每个目标字段
	            for (String fieldName : targetFields) {
	                // 如果当前字段已有值则跳过
	                if (mergedMap.get(fieldName.toUpperCase()) != null) continue;
	                
	                // 使用反射获取字段值
	                Field field = fieldCache.get(fieldName.toUpperCase());
	                Object value = field.get(data);
	                
	                // 更新第一个非空值
	                if (value != null) {
	                    mergedMap.put(fieldName.toUpperCase(), value);
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("数据整合失败", e);
	    }
	    
	    // 封装为List<Map>返回
	    return Collections.singletonList(mergedMap);
	}
	
	public static List<Map<String, Object>> mergeZdFillData(List<TblRiskMonitoringFill> dataList) {
	    // 定义需要处理的字段
	    final List<String> targetFields = Arrays.asList(
		        "aqBigdecimal1","aqBigdecimal2","aqBigdecimal3", "aqBigdecimal4", "aqBigdecimal5", "aqBigdecimal6",
		        "aqBigdecimal11","aqBigdecimal12", "aqBigdecimal13", "aqBigdecimal14", 
		        "aqBigdecimal7","aqBigdecimal8", "aqBigdecimal9", "aqBigdecimal10"
		        , "cwBigdecimal8", "cwBigdecimal12"
		    );
	    // 结果Map（存放合并后的字段值）
	    Map<String, Object> mergedMap = new HashMap<>();
	    // 字段反射缓存（提高性能）
	    Map<String, Field> fieldCache = new HashMap<>();
	    try {
	        // 初始化字段反射
	        for (String fieldName : targetFields) {
	            Field field = TblRiskMonitoringFill.class.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            fieldCache.put(fieldName.toUpperCase(), field);
	            mergedMap.put(fieldName.toUpperCase(), null); // 初始化所有字段为null
	        }
	        
	        // 遍历每个数据对象
	        for (TblRiskMonitoringFill data : dataList) {
	            // 遍历每个目标字段
	            for (String fieldName : targetFields) {
	                // 如果当前字段已有值则跳过
	                if (mergedMap.get(fieldName.toUpperCase()) != null) continue;
	                // 使用反射获取字段值
	                Field field = fieldCache.get(fieldName.toUpperCase());
	                Object value = field.get(data);
	                // 更新第一个非空值
	                if (isNotNullAndNotZero(value)) {
	                	  mergedMap.put(fieldName.toUpperCase(), value);
	                }
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("数据整合失败", e);
	    }
	    
	    // 封装为List<Map>返回
	    return Collections.singletonList(mergedMap);
	}

	
	public static boolean isNotNullAndNotZero(Object obj) {
	    if(obj == null) return false;
	    
	    if(obj instanceof Integer) 
	        return ((Integer)obj) != 0;
	    else if(obj instanceof BigDecimal) 
	        return ((BigDecimal)obj).compareTo(BigDecimal.ZERO) != 0;
	    else if(obj instanceof Double) 
	        return Math.abs((Double)obj) > 0.000001;
	    else if(obj instanceof String) 
	        return !((String)obj).trim().isEmpty() && !"0".equals(((String)obj).trim());
	    
	    return true; // 其他类型默认不为0
	}


}