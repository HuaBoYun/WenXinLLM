package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.RiskAssessment;
import com.huabo.contract.vo.RiskAssessmentQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 风险评估主表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface RiskAssessmentService extends IService<RiskAssessment> {

    /**
     * 分页查询风险评估列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<RiskAssessment> getRiskAssessmentList(RiskAssessmentQueryParam param);

    /**
     * 保存风险评估（新增或修改）
     * 
     * @param riskAssessment 风险评估
     * @return 保存结果
     */
    boolean saveRiskAssessment(RiskAssessment riskAssessment);

    /**
     * 根据ID获取风险评估详情
     * 
     * @param id 主键ID
     * @return 风险评估详情
     */
    RiskAssessment getRiskAssessmentById(Long id);

    /**
     * 根据评估编号获取风险评估
     * 
     * @param assessmentNo 评估编号
     * @return 风险评估
     */
    RiskAssessment getRiskAssessmentByAssessmentNo(String assessmentNo);

    /**
     * 删除风险评估
     * 
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteRiskAssessment(Long id);

    /**
     * 批量删除风险评估
     * 
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteRiskAssessment(List<Long> ids);

    /**
     * 生成评估编号
     * 
     * @return 评估编号
     */
    String generateAssessmentNo();

    /**
     * 检查评估编号是否存在
     * 
     * @param assessmentNo 评估编号
     * @param excludeId 排除的ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsAssessmentNo(String assessmentNo, Long excludeId);

    /**
     * 更新评估状态
     * 
     * @param id 主键ID
     * @param assessmentStatus 评估状态
     * @return 更新结果
     */
    boolean updateAssessmentStatus(Long id, Integer assessmentStatus);

    /**
     * 批量更新评估状态
     * 
     * @param ids 主键ID列表
     * @param assessmentStatus 评估状态
     * @return 更新结果
     */
    boolean batchUpdateAssessmentStatus(List<Long> ids, Integer assessmentStatus);

    /**
     * 更新审批状态
     * 
     * @param id 主键ID
     * @param approvalStatus 审批状态
     * @param approverId 审批人ID
     * @return 更新结果
     */
    boolean updateApprovalStatus(Long id, Integer approvalStatus, Long approverId);

    /**
     * 批量更新审批状态
     * 
     * @param ids 主键ID列表
     * @param approvalStatus 审批状态
     * @param approverId 审批人ID
     * @return 更新结果
     */
    boolean batchUpdateApprovalStatus(List<Long> ids, Integer approvalStatus, Long approverId);

    /**
     * 获取待审批的风险评估列表
     * 
     * @param approverId 审批人ID（可选）
     * @return 待审批的风险评估列表
     */
    List<RiskAssessment> getPendingApprovalList(Long approverId);

    /**
     * 获取高风险评估列表
     * 
     * @param riskLevel 风险等级阈值
     * @return 高风险评估列表
     */
    List<RiskAssessment> getHighRiskList(Integer riskLevel);

    /**
     * 根据项目ID查询风险评估列表
     * 
     * @param projectId 项目ID
     * @return 风险评估列表
     */
    List<RiskAssessment> getRiskAssessmentByProjectId(Long projectId);

    /**
     * 根据相对方ID查询风险评估列表
     * 
     * @param counterpartId 相对方ID
     * @return 风险评估列表
     */
    List<RiskAssessment> getRiskAssessmentByCounterpartId(Long counterpartId);

    /**
     * 统计风险评估数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getRiskAssessmentStatistics(RiskAssessmentQueryParam param);

    /**
     * 获取风险等级分布统计
     * 
     * @param param 查询参数
     * @return 风险等级分布
     */
    List<Map<String, Object>> getRiskLevelDistribution(RiskAssessmentQueryParam param);

    /**
     * 获取评估状态分布统计
     * 
     * @param param 查询参数
     * @return 评估状态分布
     */
    List<Map<String, Object>> getAssessmentStatusDistribution(RiskAssessmentQueryParam param);

    /**
     * 获取月度评估趋势
     * 
     * @param param 查询参数
     * @return 月度评估趋势
     */
    List<Map<String, Object>> getMonthlyAssessmentTrend(RiskAssessmentQueryParam param);

    /**
     * 计算风险评估总分和等级
     * 
     * @param id 评估ID
     * @return 计算结果
     */
    boolean calculateRiskScoreAndLevel(Long id);

    /**
     * 提交评估（完成评估并提交审批）
     * 
     * @param id 评估ID
     * @return 提交结果
     */
    boolean submitAssessment(Long id);

    /**
     * 审批评估
     * 
     * @param id 评估ID
     * @param approvalStatus 审批状态
     * @param approverId 审批人ID
     * @param remarks 审批意见
     * @return 审批结果
     */
    boolean approveAssessment(Long id, Integer approvalStatus, Long approverId, String remarks);
}
