package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcDataMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据映射配置Mapper接口
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@Mapper
public interface TcDataMappingMapper extends BaseMapper<TcDataMapping> {

    /**
     * 自定义查询方法,避免MyBatis-Plus生成的SQL出现WHERE ORDER BY语法错误
     * 使用PageHelper分页,禁用MyBatis-Plus分页
     *
     * @param mappingName 映射名称(可选)
     * @param sourceSystem 源系统(可选)
     * @param targetSystem 目标系统(可选)
     * @param mappingType 映射类型(可选)
     * @param sourceField 源字段(可选)
     * @param targetField 目标字段(可选)
     * @param fieldType 字段类型(可选)
     * @param status 状态(可选)
     * @return 查询结果列表
     */
    List<TcDataMapping> selectListWithConditions(
            @Param("mappingName") String mappingName,
            @Param("sourceSystem") String sourceSystem,
            @Param("targetSystem") String targetSystem,
            @Param("mappingType") String mappingType,
            @Param("sourceField") String sourceField,
            @Param("targetField") String targetField,
            @Param("fieldType") String fieldType,
            @Param("status") String status
    );
}