package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.RiskAssessment;
import com.huabo.contract.vo.RiskAssessmentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 风险评估主表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface RiskAssessmentMapper extends BaseMapper<RiskAssessment> {

    /**
     * 分页查询风险评估列表
     * 
     * @param param 查询参数
     * @return 风险评估列表
     */
    List<RiskAssessment> selectRiskAssessmentList(@Param("param") RiskAssessmentQueryParam param);

    /**
     * 根据项目ID查询风险评估列表
     * 
     * @param projectId 项目ID
     * @return 风险评估列表
     */
    List<RiskAssessment> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据相对方ID查询风险评估列表
     * 
     * @param counterpartId 相对方ID
     * @return 风险评估列表
     */
    List<RiskAssessment> selectByCounterpartId(@Param("counterpartId") Long counterpartId);

    /**
     * 根据评估编号查询风险评估
     * 
     * @param assessmentNo 评估编号
     * @return 风险评估
     */
    RiskAssessment selectByAssessmentNo(@Param("assessmentNo") String assessmentNo);

    /**
     * 检查评估编号是否存在
     * 
     * @param assessmentNo 评估编号
     * @param excludeId 排除的ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsAssessmentNo(@Param("assessmentNo") String assessmentNo, @Param("excludeId") Long excludeId);

    /**
     * 获取待审批的风险评估列表
     * 
     * @param approverId 审批人ID（可选）
     * @return 待审批的风险评估列表
     */
    List<RiskAssessment> selectPendingApprovalList(@Param("approverId") Long approverId);

    /**
     * 获取高风险评估列表
     * 
     * @param riskLevel 风险等级阈值
     * @return 高风险评估列表
     */
    List<RiskAssessment> selectHighRiskList(@Param("riskLevel") Integer riskLevel);

    /**
     * 批量更新评估状态
     * 
     * @param ids 评估ID列表
     * @param assessmentStatus 评估状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateAssessmentStatus(@Param("ids") List<Long> ids, 
                                   @Param("assessmentStatus") Integer assessmentStatus, 
                                   @Param("updateBy") Long updateBy);

    /**
     * 批量更新审批状态
     * 
     * @param ids 评估ID列表
     * @param approvalStatus 审批状态
     * @param approverId 审批人ID
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateApprovalStatus(@Param("ids") List<Long> ids, 
                                 @Param("approvalStatus") Integer approvalStatus, 
                                 @Param("approverId") Long approverId, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 统计风险评估数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectRiskAssessmentStatistics(@Param("param") RiskAssessmentQueryParam param);

    /**
     * 获取风险等级分布统计
     * 
     * @param param 查询参数
     * @return 风险等级分布
     */
    List<Map<String, Object>> selectRiskLevelDistribution(@Param("param") RiskAssessmentQueryParam param);

    /**
     * 获取评估状态分布统计
     * 
     * @param param 查询参数
     * @return 评估状态分布
     */
    List<Map<String, Object>> selectAssessmentStatusDistribution(@Param("param") RiskAssessmentQueryParam param);

    /**
     * 获取月度评估趋势
     *
     * @param param 查询参数
     * @return 月度评估趋势
     */
    List<Map<String, Object>> selectMonthlyAssessmentTrend(@Param("param") RiskAssessmentQueryParam param);

    /**
     * 查询高风险评估列表（用于风险预警）
     *
     * @param param 查询参数
     * @return 高风险评估列表
     */
    List<RiskAssessment> selectHighRiskAssessments(@Param("param") com.huabo.contract.vo.RiskWarningQueryParam param);

    /**
     * 统计高风险评估数量
     *
     * @return 高风险评估数量
     */
    Long countHighRiskAssessments();

    /**
     * 根据风险等级统计数量
     *
     * @param riskLevel 风险等级
     * @return 数量
     */
    Long countByRiskLevel(@Param("riskLevel") int riskLevel);

    /**
     * 根据评估类型统计数量
     *
     * @param assessmentType 评估类型
     * @return 数量
     */
    Long countByAssessmentType(@Param("assessmentType") int assessmentType);

    /**
     * 统计月度高风险评估数量
     *
     * @return 月度高风险评估数量
     */
    Long countMonthlyHighRiskAssessments();

    /**
     * 统计待处理预警数量
     *
     * @return 待处理预警数量
     */
    Long countPendingWarnings();

    /**
     * 查询高风险项目列表
     *
     * @return 高风险项目列表
     */
    List<Map<String, Object>> selectHighRiskProjects();

    /**
     * 查询月度风险趋势
     *
     * @param months 月份数
     * @return 月度风险趋势
     */
    List<Map<String, Object>> selectMonthlyRiskTrend(@Param("months") Integer months);

    /**
     * 查询需要评估的项目
     *
     * @return 需要评估的项目列表
     */
    List<Map<String, Object>> selectProjectsNeedingAssessment();

    /**
     * 查询相对方风险趋势
     *
     * @param counterpartId 相对方ID
     * @return 风险趋势
     */
    List<Map<String, Object>> selectCounterpartRiskTrend(@Param("counterpartId") Long counterpartId);
}
