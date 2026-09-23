package com.management.accountant.mapper.ts;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsTaxPlanning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务筹划 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface TsTaxPlanningMapper extends BaseMapper<TsTaxPlanning> {

    /**
     * 分页查询税务筹划
     */
    IPage<TsTaxPlanning> selectPlanningPage(Page<TsTaxPlanning> page, @Param("tenantId") Long tenantId, 
                                           @Param("planningCode") String planningCode,
                                           @Param("planningName") String planningName,
                                           @Param("planningType") String planningType,
                                           @Param("planningStatus") String planningStatus,
                                           @Param("priority") String priority,
                                           @Param("taxType") String taxType,
                                           @Param("riskLevel") String riskLevel,
                                           @Param("executionStatus") String executionStatus,
                                           @Param("responsiblePerson") String responsiblePerson,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 根据筹划编号查询
     */
    TsTaxPlanning selectByPlanningCode(@Param("tenantId") Long tenantId, @Param("planningCode") String planningCode);

    /**
     * 根据筹划类型查询
     */
    List<TsTaxPlanning> selectByPlanningType(@Param("tenantId") Long tenantId, @Param("planningType") String planningType);

    /**
     * 根据筹划状态查询
     */
    List<TsTaxPlanning> selectByPlanningStatus(@Param("tenantId") Long tenantId, @Param("planningStatus") String planningStatus);

    /**
     * 根据执行状态查询
     */
    List<TsTaxPlanning> selectByExecutionStatus(@Param("tenantId") Long tenantId, @Param("executionStatus") String executionStatus);

    /**
     * 根据风险等级查询
     */
    List<TsTaxPlanning> selectByRiskLevel(@Param("tenantId") Long tenantId, @Param("riskLevel") String riskLevel);

    /**
     * 根据税种查询
     */
    List<TsTaxPlanning> selectByTaxType(@Param("tenantId") Long tenantId, @Param("taxType") String taxType);

    /**
     * 根据责任人查询
     */
    List<TsTaxPlanning> selectByResponsiblePerson(@Param("tenantId") Long tenantId, @Param("responsiblePerson") String responsiblePerson);

    /**
     * 根据时间范围查询
     */
    List<TsTaxPlanning> selectByTimeRange(@Param("tenantId") Long tenantId, 
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 查询即将到期的筹划
     */
    List<TsTaxPlanning> selectExpiringSoon(@Param("tenantId") Long tenantId, @Param("days") Integer days);

    /**
     * 查询逾期的筹划
     */
    List<TsTaxPlanning> selectOverdue(@Param("tenantId") Long tenantId);

    /**
     * 查询高风险筹划
     */
    List<TsTaxPlanning> selectHighRisk(@Param("tenantId") Long tenantId);

    /**
     * 查询高收益筹划
     */
    List<TsTaxPlanning> selectHighBenefit(@Param("tenantId") Long tenantId, @Param("minBenefit") BigDecimal minBenefit);

    /**
     * 统计筹划数量
     */
    Long countPlannings(@Param("tenantId") Long tenantId);

    /**
     * 按状态统计筹划数量
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") Long tenantId);

    /**
     * 按类型统计筹划数量
     */
    List<Map<String, Object>> countByType(@Param("tenantId") Long tenantId);

    /**
     * 按税种统计筹划数量
     */
    List<Map<String, Object>> countByTaxType(@Param("tenantId") Long tenantId);

    /**
     * 按风险等级统计筹划数量
     */
    List<Map<String, Object>> countByRiskLevel(@Param("tenantId") Long tenantId);

    /**
     * 按执行状态统计筹划数量
     */
    List<Map<String, Object>> countByExecutionStatus(@Param("tenantId") Long tenantId);

    /**
     * 统计总节税金额
     */
    BigDecimal sumTaxSavingAmount(@Param("tenantId") Long tenantId);

    /**
     * 统计总净收益
     */
    BigDecimal sumNetBenefit(@Param("tenantId") Long tenantId);

    /**
     * 统计平均投资回报率
     */
    BigDecimal avgRoi(@Param("tenantId") Long tenantId);

    /**
     * 统计筹划成功率
     */
    BigDecimal calculateSuccessRate(@Param("tenantId") Long tenantId);

    /**
     * 获取筹划趋势数据
     */
    List<Map<String, Object>> getPlanningTrend(@Param("tenantId") Long tenantId, 
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("groupBy") String groupBy);

    /**
     * 获取节税趋势数据
     */
    List<Map<String, Object>> getTaxSavingTrend(@Param("tenantId") Long tenantId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("groupBy") String groupBy);

    /**
     * 获取收益趋势数据
     */
    List<Map<String, Object>> getBenefitTrend(@Param("tenantId") Long tenantId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                             @Param("groupBy") String groupBy);

    /**
     * 获取风险分布数据
     */
    List<Map<String, Object>> getRiskDistribution(@Param("tenantId") Long tenantId);

    /**
     * 获取效果评估数据
     */
    List<Map<String, Object>> getEffectivenessData(@Param("tenantId") Long tenantId);

    /**
     * 获取筹划排行榜
     */
    List<Map<String, Object>> getPlanningRanking(@Param("tenantId") Long tenantId, 
                                                 @Param("rankBy") String rankBy,
                                                 @Param("limit") Integer limit);

    /**
     * 批量更新筹划状态
     */
    int batchUpdateStatus(@Param("tenantId") Long tenantId, 
                         @Param("planningIds") List<Long> planningIds, 
                         @Param("status") String status,
                         @Param("updatedBy") String updatedBy);

    /**
     * 批量更新执行状态
     */
    int batchUpdateExecutionStatus(@Param("tenantId") Long tenantId,
                                  @Param("planningIds") List<Long> planningIds,
                                  @Param("executionStatus") String executionStatus,
                                  @Param("updatedBy") String updatedBy);

    /**
     * 批量删除筹划
     */
    int batchDelete(@Param("tenantId") Long tenantId, 
                   @Param("planningIds") List<Long> planningIds,
                   @Param("updatedBy") String updatedBy);

    /**
     * 复制筹划
     */
    int copyPlanning(@Param("tenantId") Long tenantId,
                    @Param("sourcePlanningId") Long sourcePlanningId,
                    @Param("newPlanningCode") String newPlanningCode,
                    @Param("newPlanningName") String newPlanningName,
                    @Param("createdBy") String createdBy);

    /**
     * 归档筹划
     */
    int archivePlanning(@Param("tenantId") Long tenantId,
                       @Param("planningId") Long planningId,
                       @Param("updatedBy") String updatedBy);

    /**
     * 激活筹划
     */
    int activatePlanning(@Param("tenantId") Long tenantId,
                        @Param("planningId") Long planningId,
                        @Param("updatedBy") String updatedBy);

    /**
     * 暂停筹划
     */
    int pausePlanning(@Param("tenantId") Long tenantId,
                     @Param("planningId") Long planningId,
                     @Param("updatedBy") String updatedBy);

    /**
     * 恢复筹划
     */
    int resumePlanning(@Param("tenantId") Long tenantId,
                      @Param("planningId") Long planningId,
                      @Param("updatedBy") String updatedBy);

    /**
     * 完成筹划
     */
    int completePlanning(@Param("tenantId") Long tenantId,
                        @Param("planningId") Long planningId,
                        @Param("actualTaxSaving") BigDecimal actualTaxSaving,
                        @Param("actualRoi") BigDecimal actualRoi,
                        @Param("executionResult") String executionResult,
                        @Param("effectivenessEvaluation") String effectivenessEvaluation,
                        @Param("updatedBy") String updatedBy);

    /**
     * 更新执行进度
     */
    int updateExecutionProgress(@Param("tenantId") Long tenantId,
                               @Param("planningId") Long planningId,
                               @Param("progress") BigDecimal progress,
                               @Param("updatedBy") String updatedBy);

    /**
     * 系统健康检查
     */
    Map<String, Object> systemHealthCheck(@Param("tenantId") Long tenantId);

    /**
     * 数据一致性检查
     */
    List<Map<String, Object>> dataConsistencyCheck(@Param("tenantId") Long tenantId);

    /**
     * 性能统计
     */
    Map<String, Object> performanceStats(@Param("tenantId") Long tenantId);

    /**
     * 清理过期数据
     */
    int cleanupExpiredData(@Param("tenantId") Long tenantId, @Param("beforeDate") LocalDateTime beforeDate);
}
