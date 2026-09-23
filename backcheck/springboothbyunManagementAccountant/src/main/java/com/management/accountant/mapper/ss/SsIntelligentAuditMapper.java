package com.management.accountant.mapper.ss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsIntelligentAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 智能审核Mapper接口
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface SsIntelligentAuditMapper extends BaseMapper<SsIntelligentAudit> {

    /**
     * 分页查询智能审核列表
     */
    IPage<SsIntelligentAudit> selectAuditPage(Page<SsIntelligentAudit> page, @Param("auditTitle") String auditTitle,
                                              @Param("auditType") String auditType, @Param("auditStatus") String auditStatus,
                                              @Param("riskLevel") String riskLevel, @Param("auditorId") Long auditorId,
                                              @Param("auditDeptId") Long auditDeptId, @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 根据审核编码查询
     */
    SsIntelligentAudit selectByAuditCode(@Param("auditCode") String auditCode, @Param("tenantId") Long tenantId);

    /**
     * 根据审核类型查询列表
     */
    List<SsIntelligentAudit> selectByAuditType(@Param("auditType") String auditType, @Param("tenantId") Long tenantId);

    /**
     * 根据审核状态查询列表
     */
    List<SsIntelligentAudit> selectByAuditStatus(@Param("auditStatus") String auditStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据风险等级查询列表
     */
    List<SsIntelligentAudit> selectByRiskLevel(@Param("riskLevel") String riskLevel, @Param("tenantId") Long tenantId);

    /**
     * 根据审核人员ID查询列表
     */
    List<SsIntelligentAudit> selectByAuditorId(@Param("auditorId") Long auditorId, @Param("tenantId") Long tenantId);

    /**
     * 根据审核部门ID查询列表
     */
    List<SsIntelligentAudit> selectByAuditDeptId(@Param("auditDeptId") Long auditDeptId, @Param("tenantId") Long tenantId);

    /**
     * 根据目标对象查询列表
     */
    List<SsIntelligentAudit> selectByTargetObject(@Param("targetObjectId") Long targetObjectId,
                                                  @Param("targetObjectType") String targetObjectType, @Param("tenantId") Long tenantId);

    /**
     * 根据审核规则ID查询列表
     */
    List<SsIntelligentAudit> selectByAuditRuleId(@Param("auditRuleId") Long auditRuleId, @Param("tenantId") Long tenantId);

    /**
     * 根据时间范围查询列表
     */
    List<SsIntelligentAudit> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询待处理的审核列表
     */
    List<SsIntelligentAudit> selectPendingAudits(@Param("tenantId") Long tenantId);

    /**
     * 查询需要复核的审核列表
     */
    List<SsIntelligentAudit> selectNeedReview(@Param("tenantId") Long tenantId);

    /**
     * 查询需要跟进的审核列表
     */
    List<SsIntelligentAudit> selectNeedFollowUp(@Param("tenantId") Long tenantId);

    /**
     * 查询高风险审核列表
     */
    List<SsIntelligentAudit> selectHighRiskAudits(@Param("tenantId") Long tenantId);

    /**
     * 查询异常审核列表
     */
    List<SsIntelligentAudit> selectAnomalyAudits(@Param("tenantId") Long tenantId);

    /**
     * 查询超时审核列表
     */
    List<SsIntelligentAudit> selectOverdueAudits(@Param("currentTime") LocalDateTime currentTime, @Param("tenantId") Long tenantId);

    /**
     * 统计审核数据
     */
    Map<String, Object> selectAuditStatistics(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计审核状态分布
     */
    List<Map<String, Object>> selectAuditStatusDistribution(@Param("startTime") LocalDateTime startTime,
                                                            @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计审核类型分布
     */
    List<Map<String, Object>> selectAuditTypeDistribution(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计风险等级分布
     */
    List<Map<String, Object>> selectRiskLevelDistribution(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计审核趋势
     */
    List<Map<String, Object>> selectAuditTrend(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计审核效率
     */
    Map<String, Object> selectAuditEfficiency(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 统计审核质量
     */
    Map<String, Object> selectAuditQuality(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询审核排行榜
     */
    List<Map<String, Object>> selectAuditRanking(@Param("rankingType") String rankingType,
                                                 @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
                                                 @Param("limit") Integer limit, @Param("tenantId") Long tenantId);

    /**
     * 查询审核人员工作量统计
     */
    List<Map<String, Object>> selectAuditorWorkload(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询部门审核统计
     */
    List<Map<String, Object>> selectDeptAuditStatistics(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 批量更新审核状态
     */
    int batchUpdateAuditStatus(@Param("auditIds") List<Long> auditIds, @Param("auditStatus") String auditStatus,
                               @Param("updatedBy") Long updatedBy, @Param("updatedByName") String updatedByName,
                               @Param("updatedTime") LocalDateTime updatedTime, @Param("tenantId") Long tenantId);

    /**
     * 批量更新处理状态
     */
    int batchUpdateProcessingStatus(@Param("auditIds") List<Long> auditIds, @Param("processingStatus") String processingStatus,
                                    @Param("processorId") Long processorId, @Param("processorName") String processorName,
                                    @Param("processingTime") LocalDateTime processingTime, @Param("tenantId") Long tenantId);

    /**
     * 批量分配审核人员
     */
    int batchAssignAuditor(@Param("auditIds") List<Long> auditIds, @Param("auditorId") Long auditorId,
                           @Param("auditorName") String auditorName, @Param("auditDeptId") Long auditDeptId,
                           @Param("auditDeptName") String auditDeptName, @Param("updatedBy") Long updatedBy,
                           @Param("updatedByName") String updatedByName, @Param("updatedTime") LocalDateTime updatedTime,
                           @Param("tenantId") Long tenantId);

    /**
     * 检查审核编码是否存在
     */
    int checkAuditCodeExists(@Param("auditCode") String auditCode, @Param("auditId") Long auditId, @Param("tenantId") Long tenantId);

    /**
     * 检查目标对象是否有进行中的审核
     */
    int checkTargetObjectInProgress(@Param("targetObjectId") Long targetObjectId, @Param("targetObjectType") String targetObjectType,
                                    @Param("auditId") Long auditId, @Param("tenantId") Long tenantId);

    /**
     * 计算平均审核时长
     */
    BigDecimal calculateAverageAuditDuration(@Param("auditType") String auditType, @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 计算审核成功率
     */
    BigDecimal calculateAuditSuccessRate(@Param("auditType") String auditType, @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询机器学习模型使用统计
     */
    List<Map<String, Object>> selectMlModelUsageStatistics(@Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询审核规则使用统计
     */
    List<Map<String, Object>> selectAuditRuleUsageStatistics(@Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询异常类型统计
     */
    List<Map<String, Object>> selectAnomalyTypeStatistics(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 查询预警级别统计
     */
    List<Map<String, Object>> selectWarningLevelStatistics(@Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime, @Param("tenantId") Long tenantId);

    /**
     * 删除过期审核记录
     */
    int deleteExpiredAudits(@Param("expiredTime") LocalDateTime expiredTime, @Param("tenantId") Long tenantId);

    /**
     * 归档历史审核记录
     */
    int archiveHistoryAudits(@Param("archiveTime") LocalDateTime archiveTime, @Param("tenantId") Long tenantId);
}
