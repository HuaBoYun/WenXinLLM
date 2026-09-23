package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.DecisionRecommendation;

import java.util.Map;

/**
 * 决策建议Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
public interface IDecisionRecommendationService extends IService<DecisionRecommendation> {

    /**
     * 分页查询决策建议
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<DecisionRecommendation> selectPage(IPage<DecisionRecommendation> page, Map<String, Object> params);

    /**
     * 审核决策建议
     * @param recommendationId 建议ID
     * @param reviewOpinion 审核意见
     * @param reviewUser 审核人ID
     * @return 是否成功
     */
    boolean reviewRecommendation(Long recommendationId, String reviewOpinion, Long reviewUser);

    /**
     * 批准决策建议
     * @param recommendationId 建议ID
     * @param approvalOpinion 批准意见
     * @param approvalUser 批准人ID
     * @return 是否成功
     */
    boolean approveRecommendation(Long recommendationId, String approvalOpinion, Long approvalUser);

    /**
     * 拒绝决策建议
     * @param recommendationId 建议ID
     * @param rejectOpinion 拒绝理由
     * @param rejectUser 拒绝人ID
     * @return 是否成功
     */
    boolean rejectRecommendation(Long recommendationId, String rejectOpinion, Long rejectUser);

    /**
     * 实施决策建议
     * @param recommendationId 建议ID
     * @param implementationResult 实施结果
     * @param implementUser 实施人ID
     * @return 是否成功
     */
    boolean implementRecommendation(Long recommendationId, String implementationResult, Long implementUser);
}
