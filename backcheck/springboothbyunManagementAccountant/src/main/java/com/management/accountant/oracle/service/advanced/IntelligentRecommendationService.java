package com.management.accountant.oracle.service.advanced;

import com.management.accountant.oracle.entity.advanced.IntelligentRecommendation;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 智能推荐Service接口
 * 
 * @description 智能推荐业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-06
 */
public interface IntelligentRecommendationService {

    /**
     * 获取统计数据
     * 
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> getStats(String companyId);

    /**
     * 获取推荐列表（分页）
     * 
     * @param keyword 关键词
     * @param status 状态
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<IntelligentRecommendation> getRecommendationList(String keyword, String status, 
                                                                 String companyId, Integer pageNo, 
                                                                 Integer pageSize);

    /**
     * 生成智能推荐
     * 
     * @param budgetId 预算ID
     * @param recommendationType 推荐类型
     * @param companyId 公司ID
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 推荐ID和建议内容
     */
    Map<String, Object> generateRecommendation(String budgetId, String recommendationType,
                                               String companyId, String userId, String userName);

    /**
     * 接受推荐
     * 
     * @param recommendationId 推荐ID
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 是否成功
     */
    boolean acceptRecommendation(String recommendationId, String userId, String userName);

    /**
     * 拒绝推荐
     * 
     * @param recommendationId 推荐ID
     * @param userId 用户ID
     * @param userName 用户名称
     * @param reason 拒绝原因
     * @return 是否成功
     */
    boolean rejectRecommendation(String recommendationId, String userId, String userName, String reason);

    /**
     * 删除推荐
     * 
     * @param recommendationId 推荐ID
     * @return 是否成功
     */
    boolean deleteRecommendation(String recommendationId);

    /**
     * 根据ID获取推荐详情
     * 
     * @param recommendationId 推荐ID
     * @return 推荐详情
     */
    IntelligentRecommendation getRecommendationById(String recommendationId);
}

