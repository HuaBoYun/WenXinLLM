package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.DecisionRecommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 决策建议Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Mapper
public interface DecisionRecommendationMapper extends BaseMapper<DecisionRecommendation> {

    /**
     * 根据条件查询决策建议列表(支持动态条件)
     *
     * @param recommendationNo 建议编号(模糊查询)
     * @param recommendationName 建议名称(模糊查询)
     * @param recommendationType 建议类型(精确查询)
     * @param recommendationStatus 建议状态(精确查询)
     * @param priority 优先级(精确查询)
     * @param orgId 组织ID(精确查询)
     * @return 决策建议列表
     */
    List<DecisionRecommendation> selectByCondition(@Param("recommendationNo") String recommendationNo,
                                                   @Param("recommendationName") String recommendationName,
                                                   @Param("recommendationType") String recommendationType,
                                                   @Param("recommendationStatus") String recommendationStatus,
                                                   @Param("priority") String priority,
                                                   @Param("orgId") Long orgId);

    /**
     * 分页查询决策建议列表
     * @param params 查询参数
     * @return 决策建议列表
     */
    List<DecisionRecommendation> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询决策建议详情
     * @param id 主键ID
     * @return 决策建议详情
     */
    DecisionRecommendation selectDetailById(@Param("id") Long id);

    /**
     * 统计决策建议数量
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);
}

