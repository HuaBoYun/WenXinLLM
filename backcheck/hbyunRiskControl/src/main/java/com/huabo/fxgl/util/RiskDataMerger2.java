package com.huabo.fxgl.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import com.huabo.fxgl.entity.TblRiskMonitoringFill;

public class RiskDataMerger2 {

    /**
     * 合并数据
     * @param records 原始数据列表
     * @return 合并后的数据列表
     */
    public static List<TblRiskMonitoringFill> mergeRiskData(List<TblRiskMonitoringFill> records) {
        // 1. 按年份和季度分组
        Map<String, List<TblRiskMonitoringFill>> groupedRecords = records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getRiskyear() + "|" + r.getQuartername()
                ));
        
        // 2. 为每个分组创建合并记录
        List<TblRiskMonitoringFill> mergedRecords = new ArrayList<>();
        
        for (Map.Entry<String, List<TblRiskMonitoringFill>> entry : groupedRecords.entrySet()) {
            // 获取分组键
            String[] keys = entry.getKey().split("\\|");
            int riskYear = Integer.parseInt(keys[0]);
            String quarterName = keys[1];
            
            // 创建新的合并记录
            TblRiskMonitoringFill merged = new TblRiskMonitoringFill();
            merged.setRiskyear(riskYear);
            merged.setQuartername(quarterName);
            
            // 合并同一分组的所有记录
            List<TblRiskMonitoringFill> groupList = entry.getValue();
            for (TblRiskMonitoringFill record : groupList) {
                mergeFields(merged, record);
            }
            
            // 设置合并后的元数据
            setMergedMetadata(merged, groupList);
            
            mergedRecords.add(merged);
        }
        
        // 3. 按年份和季度排序
        mergedRecords.sort(Comparator.comparing(TblRiskMonitoringFill::getRiskyear)
                .thenComparing(TblRiskMonitoringFill::getQuartername));
        
        return mergedRecords;
    }
    
    /**
     * 设置合并后的元数据信息
     */
    private static void setMergedMetadata(TblRiskMonitoringFill merged, 
                                         List<TblRiskMonitoringFill> groupList) {
        // 设置notes（取第一条记录的notes）
        if (groupList.get(0).getNotes() != null) {
            merged.setNotes(groupList.get(0).getNotes());
        }
        
        // 设置monitorId（取第一条记录的monitorId）
        if (groupList.get(0).getMonitorId() != null) {
            merged.setMonitorId(groupList.get(0).getMonitorId());
        }
        
        // 设置secrectLevelId（取第一条记录的secrectLevelId）
        if (groupList.get(0).getSecrectLevelId() != null) {
            merged.setSecrectLevelId(groupList.get(0).getSecrectLevelId());
        }
        
        // 设置创建人信息（合并所有创建人）
        String createstaffids = groupList.stream()
                .map(r -> r.getCreatestaffid().toString())
                .distinct()
                .collect(Collectors.joining(","));
     //   merged.setCreatestaffid(createstaffids);
        
        // 设置创建时间（取最新时间）
        Date latestCreateTime = groupList.stream()
                .map(TblRiskMonitoringFill::getCreatetime)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);
        merged.setCreatetime(latestCreateTime);
        
        // 设置部门信息（合并所有部门）
        String linkDeptIds = groupList.stream()
                .map(r -> r.getLinkDeptId().toString())
                .distinct()
                .collect(Collectors.joining(","));
      //  merged.setLinkDeptId(linkDeptIds);
    }
    
    /**
     * 合并字段数据
     */
    private static void mergeFields(TblRiskMonitoringFill target, TblRiskMonitoringFill source) {
        // 获取目标类的所有字段
        Field[] fields = TblRiskMonitoringFill.class.getDeclaredFields();
        
        for (Field field : fields) {
            try {
                // 设置可访问
                field.setAccessible(true);
                
                // 跳过不需要合并的元数据字段
                if (shouldSkipField(field.getName())) {
                    continue;
                }
                
                // 获取源字段值
                Object sourceValue = field.get(source);
                
                // 如果源字段值不为空，且目标字段值为空，则复制值
                if (sourceValue != null) {
                    Object targetValue = field.get(target);
                    if (targetValue == null) {
                        field.set(target, sourceValue);
                    }
                }
            } catch (IllegalAccessException e) {
                // 处理异常，实际应用中应记录日志
                System.err.println("字段访问错误: " + field.getName());
            }
        }
    }
    
    /**
     * 判断字段是否需要跳过
     */
    private static boolean shouldSkipField(String fieldName) {
        // 跳过元数据字段
        List<String> skipFields = Arrays.asList(
            "id", "riskyear", "quartername", "notes", "monitorId", "createstaffid", 
            "createname", "createtime", "contact", "status", "secrectLevelId", 
            "staffScopeIds", "staffScopeNames", "linkOrgId", "linkOrgName", 
            "linkDeptId", "linkDeptName", "toReportDate", "reportStaffid", 
            "reportStaffName", "reportstatus", "deptNotes", "tblFillIssued"
        );
        
        return skipFields.contains(fieldName);
    }
    
   

}