package com.management.accountant.mapper.pm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.pm.PmIncentiveManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 激励管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Mapper
public interface PmIncentiveManagementMapper extends BaseMapper<PmIncentiveManagement> {

    /**
     * 分页查询激励管理列表
     */
    IPage<PmIncentiveManagement> selectIncentiveManagementPage(Page<PmIncentiveManagement> page, 
                                                               @Param("incentiveTitle") String incentiveTitle,
                                                               @Param("incentiveType") String incentiveType,
                                                               @Param("incentiveStatus") String incentiveStatus,
                                                               @Param("incentiveYear") Integer incentiveYear,
                                                               @Param("targetDeptId") Long targetDeptId,
                                                               @Param("incentiveOwnerId") Long incentiveOwnerId,
                                                               @Param("startTime") LocalDateTime startTime,
                                                               @Param("endTime") LocalDateTime endTime,
                                                               @Param("tenantId") Long tenantId);

    /**
     * 根据激励编码查询
     */
    PmIncentiveManagement selectByIncentiveCode(@Param("incentiveCode") String incentiveCode, 
                                                @Param("tenantId") Long tenantId);

    /**
     * 根据激励类型查询列表
     */
    List<PmIncentiveManagement> selectByIncentiveType(@Param("incentiveType") String incentiveType, 
                                                      @Param("tenantId") Long tenantId);

    /**
     * 根据激励状态查询列表
     */
    List<PmIncentiveManagement> selectByIncentiveStatus(@Param("incentiveStatus") String incentiveStatus, 
                                                        @Param("tenantId") Long tenantId);

    /**
     * 根据部门ID查询激励列表
     */
    List<PmIncentiveManagement> selectByDeptId(@Param("targetDeptId") Long targetDeptId, 
                                               @Param("tenantId") Long tenantId);

    /**
     * 根据负责人ID查询激励列表
     */
    List<PmIncentiveManagement> selectByOwnerId(@Param("incentiveOwnerId") Long incentiveOwnerId, 
                                                @Param("tenantId") Long tenantId);

    /**
     * 根据年度查询激励列表
     */
    List<PmIncentiveManagement> selectByYear(@Param("incentiveYear") Integer incentiveYear, 
                                             @Param("tenantId") Long tenantId);

    /**
     * 根据季度查询激励列表
     */
    List<PmIncentiveManagement> selectByQuarter(@Param("incentiveYear") Integer incentiveYear,
                                                @Param("incentiveQuarter") Integer incentiveQuarter, 
                                                @Param("tenantId") Long tenantId);

    /**
     * 根据月份查询激励列表
     */
    List<PmIncentiveManagement> selectByMonth(@Param("incentiveYear") Integer incentiveYear,
                                              @Param("incentiveMonth") Integer incentiveMonth, 
                                              @Param("tenantId") Long tenantId);

    /**
     * 查询待审批的激励列表
     */
    List<PmIncentiveManagement> selectPendingApproval(@Param("tenantId") Long tenantId);

    /**
     * 查询待发放的激励列表
     */
    List<PmIncentiveManagement> selectPendingDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询需要跟进的激励列表
     */
    List<PmIncentiveManagement> selectNeedFollowUp(@Param("tenantId") Long tenantId);

    /**
     * 查询即将到期的激励列表
     */
    List<PmIncentiveManagement> selectUpcomingDeadline(@Param("deadline") LocalDateTime deadline, 
                                                       @Param("tenantId") Long tenantId);

    /**
     * 查询超期的激励列表
     */
    List<PmIncentiveManagement> selectOverdue(@Param("currentTime") LocalDateTime currentTime, 
                                              @Param("tenantId") Long tenantId);

    /**
     * 统计激励数量
     */
    Map<String, Object> selectIncentiveStatistics(@Param("incentiveYear") Integer incentiveYear, 
                                                   @Param("tenantId") Long tenantId);

    /**
     * 统计激励状态分布
     */
    List<Map<String, Object>> selectIncentiveStatusDistribution(@Param("incentiveYear") Integer incentiveYear, 
                                                                 @Param("tenantId") Long tenantId);

    /**
     * 统计激励类型分布
     */
    List<Map<String, Object>> selectIncentiveTypeDistribution(@Param("incentiveYear") Integer incentiveYear, 
                                                               @Param("tenantId") Long tenantId);

    /**
     * 统计激励完成趋势
     */
    List<Map<String, Object>> selectIncentiveCompletionTrend(@Param("startTime") LocalDateTime startTime,
                                                              @Param("endTime") LocalDateTime endTime, 
                                                              @Param("tenantId") Long tenantId);

    /**
     * 统计激励金额分布
     */
    List<Map<String, Object>> selectIncentiveAmountDistribution(@Param("incentiveYear") Integer incentiveYear, 
                                                                 @Param("tenantId") Long tenantId);

    /**
     * 统计激励效果分布
     */
    List<Map<String, Object>> selectIncentiveEffectivenessDistribution(@Param("incentiveYear") Integer incentiveYear, 
                                                                        @Param("tenantId") Long tenantId);

    /**
     * 查询激励排行榜
     */
    List<Map<String, Object>> selectIncentiveRanking(@Param("incentiveYear") Integer incentiveYear,
                                                      @Param("rankingType") String rankingType,
                                                      @Param("limit") Integer limit, 
                                                      @Param("tenantId") Long tenantId);

    /**
     * 批量更新激励状态
     */
    int batchUpdateIncentiveStatus(@Param("incentiveIds") List<Long> incentiveIds,
                                   @Param("incentiveStatus") String incentiveStatus,
                                   @Param("updatedBy") Long updatedBy,
                                   @Param("updatedByName") String updatedByName,
                                   @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 批量更新发放状态
     */
    int batchUpdateDistributionStatus(@Param("incentiveIds") List<Long> incentiveIds,
                                      @Param("distributionStatus") String distributionStatus,
                                      @Param("distributionTime") LocalDateTime distributionTime,
                                      @Param("updatedBy") Long updatedBy,
                                      @Param("updatedByName") String updatedByName,
                                      @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 批量更新审批状态
     */
    int batchUpdateApprovalStatus(@Param("incentiveIds") List<Long> incentiveIds,
                                  @Param("approvalStatus") String approvalStatus,
                                  @Param("approverId") Long approverId,
                                  @Param("approverName") String approverName,
                                  @Param("approvalTime") LocalDateTime approvalTime,
                                  @Param("approvalComments") String approvalComments,
                                  @Param("updatedBy") Long updatedBy,
                                  @Param("updatedByName") String updatedByName,
                                  @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 批量更新跟进状态
     */
    int batchUpdateFollowUpStatus(@Param("incentiveIds") List<Long> incentiveIds,
                                  @Param("followUpStatus") String followUpStatus,
                                  @Param("updatedBy") Long updatedBy,
                                  @Param("updatedByName") String updatedByName,
                                  @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 检查激励编码是否存在
     */
    int checkIncentiveCodeExists(@Param("incentiveCode") String incentiveCode, 
                                 @Param("incentiveId") Long incentiveId, 
                                 @Param("tenantId") Long tenantId);

    /**
     * 检查时间冲突
     */
    int checkTimeConflict(@Param("incentiveId") Long incentiveId,
                          @Param("targetDeptId") Long targetDeptId,
                          @Param("incentiveType") String incentiveType,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime,
                          @Param("tenantId") Long tenantId);

    /**
     * 计算激励总金额
     */
    BigDecimal calculateTotalIncentiveAmount(@Param("incentiveYear") Integer incentiveYear, 
                                             @Param("tenantId") Long tenantId);

    /**
     * 计算部门激励金额
     */
    BigDecimal calculateDeptIncentiveAmount(@Param("targetDeptId") Long targetDeptId,
                                            @Param("incentiveYear") Integer incentiveYear, 
                                            @Param("tenantId") Long tenantId);

    /**
     * 查询激励详情（包含关联信息）
     */
    Map<String, Object> selectIncentiveDetailWithRelations(@Param("incentiveId") Long incentiveId, 
                                                            @Param("tenantId") Long tenantId);

    /**
     * 查询激励历史记录
     */
    List<Map<String, Object>> selectIncentiveHistory(@Param("incentiveId") Long incentiveId, 
                                                      @Param("tenantId") Long tenantId);

    /**
     * 导出激励数据
     */
    List<Map<String, Object>> selectIncentiveExportData(@Param("incentiveYear") Integer incentiveYear,
                                                         @Param("incentiveType") String incentiveType,
                                                         @Param("incentiveStatus") String incentiveStatus,
                                                         @Param("targetDeptId") Long targetDeptId,
                                                         @Param("tenantId") Long tenantId);

    /**
     * 智能推荐激励方案
     */
    List<Map<String, Object>> selectRecommendedIncentiveSchemes(@Param("targetDeptId") Long targetDeptId,
                                                                 @Param("incentiveType") String incentiveType,
                                                                 @Param("budgetRange") BigDecimal budgetRange,
                                                                 @Param("tenantId") Long tenantId);

    /**
     * 分析激励效果
     */
    Map<String, Object> analyzeIncentiveEffectiveness(@Param("incentiveId") Long incentiveId, 
                                                       @Param("tenantId") Long tenantId);

    /**
     * 生成激励报告数据
     */
    Map<String, Object> generateIncentiveReportData(@Param("incentiveYear") Integer incentiveYear,
                                                     @Param("reportType") String reportType,
                                                     @Param("tenantId") Long tenantId);

    /**
     * 查询激励优化建议
     */
    List<Map<String, Object>> selectIncentiveOptimizationSuggestions(@Param("incentiveId") Long incentiveId, 
                                                                      @Param("tenantId") Long tenantId);
}
