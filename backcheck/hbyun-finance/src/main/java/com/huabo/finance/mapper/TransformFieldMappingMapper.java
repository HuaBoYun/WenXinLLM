package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.TransformFieldMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字段映射配置Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface TransformFieldMappingMapper extends BaseMapper<TransformFieldMapping> {

    /**
     * 根据转化任务ID查询字段映射列表
     * 
     * @param transformTaskId 转化任务ID
     * @return 字段映射列表
     */
    @Select("SELECT * FROM TBL_TRANSFORM_FIELD_MAPPING WHERE TRANSFORM_TASK_ID = #{transformTaskId} ORDER BY SORT_ORDER")
    List<TransformFieldMapping> selectByTransformTaskId(@Param("transformTaskId") String transformTaskId);

    /**
     * 根据转化任务ID和源表名查询字段映射列表
     * 
     * @param transformTaskId 转化任务ID
     * @param sourceTable 源表名
     * @return 字段映射列表
     */
    @Select("SELECT * FROM TBL_TRANSFORM_FIELD_MAPPING WHERE TRANSFORM_TASK_ID = #{transformTaskId} AND SOURCE_TABLE = #{sourceTable} ORDER BY SORT_ORDER")
    List<TransformFieldMapping> selectByTaskIdAndSourceTable(@Param("transformTaskId") String transformTaskId, @Param("sourceTable") String sourceTable);
}

