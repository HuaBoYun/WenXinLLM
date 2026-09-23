package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.RiskAssessmentDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 风险评估明细表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface RiskAssessmentDetailService extends IService<RiskAssessmentDetail> {

    /**
     * 根据评估ID查询明细列表
     * 
     * @param assessmentId 评估ID
     * @return 明细列表
     */
    List<RiskAssessmentDetail> getDetailsByAssessmentId(Long assessmentId);

    /**
     * 保存评估明细（批量保存）
     * 
     * @param assessmentId 评估ID
     * @param detailList 明细列表
     * @return 保存结果
     */
    boolean saveAssessmentDetails(Long assessmentId, List<RiskAssessmentDetail> detailList);

    /**
     * 删除评估明细
     * 
     * @param assessmentId 评估ID
     * @return 删除结果
     */
    boolean deleteDetailsByAssessmentId(Long assessmentId);

    /**
     * 计算评估总分
     * 
     * @param assessmentId 评估ID
     * @return 总分
     */
    BigDecimal calculateTotalScore(Long assessmentId);

    /**
     * 获取最高风险等级
     * 
     * @param assessmentId 评估ID
     * @return 最高风险等级
     */
    Integer getMaxRiskLevel(Long assessmentId);

    /**
     * 根据风险类别查询明细
     * 
     * @param assessmentId 评估ID
     * @param riskCategory 风险类别
     * @return 明细列表
     */
    List<RiskAssessmentDetail> getDetailsByRiskCategory(Long assessmentId, Integer riskCategory);

    /**
     * 获取高风险项目明细
     * 
     * @param assessmentId 评估ID
     * @param minRiskScore 最小风险得分
     * @return 高风险项目明细
     */
    List<RiskAssessmentDetail> getHighRiskItems(Long assessmentId, BigDecimal minRiskScore);

    /**
     * 统计风险类别分布
     * 
     * @param assessmentId 评估ID
     * @return 风险类别分布
     */
    List<Map<String, Object>> getRiskCategoryDistribution(Long assessmentId);

    /**
     * 统计风险影响分布
     * 
     * @param assessmentId 评估ID
     * @return 风险影响分布
     */
    List<Map<String, Object>> getRiskImpactDistribution(Long assessmentId);

    /**
     * 获取需要缓解措施的风险项目
     * 
     * @param assessmentId 评估ID
     * @return 需要缓解措施的风险项目
     */
    List<RiskAssessmentDetail> getItemsNeedMitigation(Long assessmentId);

    /**
     * 更新风险得分
     * 
     * @param id 明细ID
     * @param riskScore 风险得分
     * @return 更新结果
     */
    boolean updateRiskScore(Long id, BigDecimal riskScore);

    /**
     * 批量更新风险得分
     * 
     * @param assessmentId 评估ID
     * @return 更新结果
     */
    boolean batchUpdateRiskScore(Long assessmentId);

    /**
     * 自动计算所有明细的风险得分
     * 
     * @param assessmentId 评估ID
     * @return 计算结果
     */
    boolean autoCalculateRiskScores(Long assessmentId);

    /**
     * 验证评估明细数据
     * 
     * @param detailList 明细列表
     * @return 验证结果
     */
    Map<String, Object> validateAssessmentDetails(List<RiskAssessmentDetail> detailList);

    /**
     * 生成风险评估报告数据
     * 
     * @param assessmentId 评估ID
     * @return 报告数据
     */
    Map<String, Object> generateAssessmentReport(Long assessmentId);

    /**
     * 复制评估明细到新评估
     * 
     * @param sourceAssessmentId 源评估ID
     * @param targetAssessmentId 目标评估ID
     * @return 复制结果
     */
    boolean copyAssessmentDetails(Long sourceAssessmentId, Long targetAssessmentId);
}
