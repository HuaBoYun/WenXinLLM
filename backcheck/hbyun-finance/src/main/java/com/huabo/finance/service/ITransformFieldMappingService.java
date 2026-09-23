package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.TransformFieldMapping;

import java.util.List;

/**
 * 字段映射配置Service接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface ITransformFieldMappingService extends IService<TransformFieldMapping> {

    /**
     * 保存字段映射配置
     *
     * @param mappings 字段映射列表
     * @param transformTaskId 转化任务ID
     * @param createUser 创建人
     * @return 操作结果
     */
    JsonBean saveMappings(List<TransformFieldMapping> mappings, String transformTaskId, String createUser);

    /**
     * 查询字段映射列表
     *
     * @param transformTaskId 转化任务ID
     * @return 字段映射列表
     */
    JsonBean getMappingList(String transformTaskId);

    /**
     * 智能字段匹配
     *
     * @param transformTaskId 转化任务ID
     * @param sourceTable 源表名
     * @param targetTable 目标表名
     * @return 匹配结果
     */
    JsonBean autoMatchFields(String transformTaskId, String sourceTable, String targetTable);

    /**
     * 删除字段映射
     *
     * @param mappingId 映射ID
     * @return 操作结果
     */
    JsonBean deleteMapping(String mappingId);
}

