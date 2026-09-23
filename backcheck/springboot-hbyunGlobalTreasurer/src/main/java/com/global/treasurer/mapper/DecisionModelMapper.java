package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.DecisionModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 决策模型Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@Mapper
public interface DecisionModelMapper extends BaseMapper<DecisionModel> {

    /**
     * 根据条件查询决策模型列表(支持动态条件)
     *
     * @param modelCode 模型编号(模糊查询)
     * @param modelName 模型名称(模糊查询)
     * @param modelType 模型类型(精确查询)
     * @param algorithmType 算法类型(精确查询)
     * @param modelStatus 模型状态(精确查询)
     * @param orgId 组织ID(精确查询)
     * @return 决策模型列表
     */
    List<DecisionModel> selectByCondition(@Param("modelCode") String modelCode,
                                         @Param("modelName") String modelName,
                                         @Param("modelType") String modelType,
                                         @Param("algorithmType") String algorithmType,
                                         @Param("modelStatus") String modelStatus,
                                         @Param("orgId") Long orgId);
}
