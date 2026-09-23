package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.TransformFieldMapping;
import com.huabo.finance.mapper.TransformFieldMappingMapper;
import com.huabo.finance.service.ITransformFieldMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 字段映射配置Service实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
public class TransformFieldMappingServiceImpl extends ServiceImpl<TransformFieldMappingMapper, TransformFieldMapping> 
        implements ITransformFieldMappingService {

    @Autowired
    private TransformFieldMappingMapper fieldMappingMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存字段映射配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveMappings(List<TransformFieldMapping> mappings, String transformTaskId, String createUser) {
        try {
            log.info("开始保存字段映射配置, transformTaskId={}, 映射数量={}", transformTaskId, mappings.size());

            // 1. 删除该任务的旧映射配置
            QueryWrapper<TransformFieldMapping> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("TRANSFORM_TASK_ID", transformTaskId);
            fieldMappingMapper.delete(deleteWrapper);

            // 2. 批量插入新映射配置
            int sortOrder = 1;
            for (TransformFieldMapping mapping : mappings) {
                mapping.setMappingId(UUID.randomUUID().toString().replace("-", ""));
                mapping.setTransformTaskId(transformTaskId);
                mapping.setSortOrder(sortOrder++);
                mapping.setCreateTime(new Date());
                mapping.setUpdateTime(new Date());
                
                fieldMappingMapper.insert(mapping);
            }

            log.info("字段映射配置保存成功, transformTaskId={}", transformTaskId);
            return ResponseFormat.retParam(1, "保存成功", null);

        } catch (Exception e) {
            log.error("保存字段映射配置失败", e);
            return ResponseFormat.retParam(0, "保存失败: " + e.getMessage(), null);
        }
    }

    /**
     * 查询字段映射列表
     */
    @Override
    public JsonBean getMappingList(String transformTaskId) {
        try {
            log.info("查询字段映射列表, transformTaskId={}", transformTaskId);

            List<TransformFieldMapping> mappings = fieldMappingMapper.selectByTransformTaskId(transformTaskId);

            log.info("查询到{}条字段映射配置", mappings.size());
            return ResponseFormat.retParam(1, 200, mappings);

        } catch (Exception e) {
            log.error("查询字段映射列表失败", e);
            return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 智能字段匹配
     */
    @Override
    public JsonBean autoMatchFields(String transformTaskId, String sourceTable, String targetTable) {
        try {
            log.info("开始智能字段匹配, sourceTable={}, targetTable={}", sourceTable, targetTable);

            // 1. 查询源表字段
            List<Map<String, Object>> sourceFields = getTableFields(sourceTable);
            
            // 2. 查询目标表字段
            List<Map<String, Object>> targetFields = getTableFields(targetTable);

            // 3. 智能匹配字段
            List<TransformFieldMapping> matchedMappings = new ArrayList<>();
            
            for (Map<String, Object> targetField : targetFields) {
                String targetFieldName = (String) targetField.get("COLUMN_NAME");
                String targetFieldType = (String) targetField.get("DATA_TYPE");

                // 查找同名字段
                for (Map<String, Object> sourceField : sourceFields) {
                    String sourceFieldName = (String) sourceField.get("COLUMN_NAME");
                    String sourceFieldType = (String) sourceField.get("DATA_TYPE");

                    // 字段名相同或相似
                    if (sourceFieldName.equalsIgnoreCase(targetFieldName) || 
                        sourceFieldName.replace("_", "").equalsIgnoreCase(targetFieldName.replace("_", ""))) {
                        
                        TransformFieldMapping mapping = new TransformFieldMapping();
                        mapping.setSourceTable(sourceTable);
                        mapping.setTargetTable(targetTable);
                        mapping.setSourceField(sourceFieldName);
                        mapping.setTargetField(targetFieldName);
                        mapping.setSourceFieldType(sourceFieldType);
                        mapping.setTargetFieldType(targetFieldType);
                        
                        // 判断转换规则
                        if (sourceFieldType.equals(targetFieldType)) {
                            mapping.setTransformRule("DIRECT"); // 直接映射
                        } else {
                            mapping.setTransformRule("FUNCTION"); // 需要类型转换
                        }
                        
                        matchedMappings.add(mapping);
                        break;
                    }
                }
            }

            log.info("智能匹配完成, 匹配到{}个字段", matchedMappings.size());

            Map<String, Object> result = new HashMap<>();
            result.put("matchedCount", matchedMappings.size());
            result.put("mappings", matchedMappings);

            return ResponseFormat.retParam(1, 200, result);

        } catch (Exception e) {
            log.error("智能字段匹配失败", e);
            return ResponseFormat.retParam(0, "匹配失败: " + e.getMessage(), null);
        }
    }

    /**
     * 删除字段映射
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteMapping(String mappingId) {
        try {
            log.info("删除字段映射, mappingId={}", mappingId);

            fieldMappingMapper.deleteById(mappingId);

            log.info("字段映射删除成功, mappingId={}", mappingId);
            return ResponseFormat.retParam(1, "删除成功", null);

        } catch (Exception e) {
            log.error("删除字段映射失败", e);
            return ResponseFormat.retParam(0, "删除失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取表字段信息
     */
    private List<Map<String, Object>> getTableFields(String tableName) {
        String sql = "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ?";
        return jdbcTemplate.queryForList(sql, tableName.toUpperCase());
    }
}

