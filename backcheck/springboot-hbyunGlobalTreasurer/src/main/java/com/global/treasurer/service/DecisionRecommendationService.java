package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.DecisionRecommendation;

import java.util.List;
import java.util.Map;

/**
 * 决策建议Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface DecisionRecommendationService extends IService<DecisionRecommendation> {

    /**
     * 分页查询决策建议列表
     * @param params 查询参数
     * @return 决策建议列表
     */
    List<DecisionRecommendation> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询决策建议详情
     * @param id 主键ID
     * @return 决策建议详情
     */
    DecisionRecommendation selectDetailById(Long id);

    /**
     * 保存决策建议
     * @param entity 决策建议实体
     * @return 是否成功
     */
    boolean saveRecommendation(DecisionRecommendation entity);

    /**
     * 更新决策建议
     * @param entity 决策建议实体
     * @return 是否成功
     */
    boolean updateRecommendation(DecisionRecommendation entity);

    /**
     * 删除决策建议
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteRecommendation(Long id);
}

