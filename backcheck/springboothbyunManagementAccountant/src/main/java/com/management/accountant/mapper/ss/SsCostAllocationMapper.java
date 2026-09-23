package com.management.accountant.mapper.ss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsCostAllocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 成本分摊 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Mapper
public interface SsCostAllocationMapper extends BaseMapper<SsCostAllocation> {

    /**
     * 分页查询成本分摊列表
     */
    IPage<SsCostAllocation> selectCostAllocationPage(Page<SsCostAllocation> page, @Param("params") Map<String, Object> params);

    /**
     * 根据状态查询成本分摊列表
     */
    List<SsCostAllocation> selectByStatus(@Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 根据类型查询成本分摊列表
     */
    List<SsCostAllocation> selectByType(@Param("type") String type, @Param("tenantId") Long tenantId);

    /**
     * 根据成本中心查询成本分摊列表
     */
    List<SsCostAllocation> selectByCostCenter(@Param("costCenterId") Long costCenterId, @Param("tenantId") Long tenantId);

    /**
     * 根据分摊方法查询成本分摊列表
     */
    List<SsCostAllocation> selectByMethod(@Param("method") String method, @Param("tenantId") Long tenantId);

    /**
     * 根据分摊周期查询成本分摊列表
     */
    List<SsCostAllocation> selectByPeriod(@Param("period") String period, @Param("tenantId") Long tenantId);

    /**
     * 根据时间范围查询成本分摊列表
     */
    List<SsCostAllocation> selectByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime, 
                                           @Param("tenantId") Long tenantId);

    /**
     * 根据金额范围查询成本分摊列表
     */
    List<SsCostAllocation> selectByAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                             @Param("maxAmount") BigDecimal maxAmount, 
                                             @Param("tenantId") Long tenantId);

    /**
     * 根据优先级查询成本分摊列表
     */
    List<SsCostAllocation> selectByPriority(@Param("priority") Integer priority, @Param("tenantId") Long tenantId);

    /**
     * 根据审批状态查询成本分摊列表
     */
    List<SsCostAllocation> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据计算状态查询成本分摊列表
     */
    List<SsCostAllocation> selectByCalculationStatus(@Param("calculationStatus") String calculationStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据创建人查询成本分摊列表
     */
    List<SsCostAllocation> selectByCreator(@Param("createdBy") Long createdBy, @Param("tenantId") Long tenantId);

    /**
     * 根据审批人查询成本分摊列表
     */
    List<SsCostAllocation> selectByApprover(@Param("approverId") Long approverId, @Param("tenantId") Long tenantId);

    /**
     * 查询待分摊的成本分摊列表
     */
    List<SsCostAllocation> selectPendingAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询进行中的成本分摊列表
     */
    List<SsCostAllocation> selectActiveAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询已完成的成本分摊列表
     */
    List<SsCostAllocation> selectCompletedAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询失败的成本分摊列表
     */
    List<SsCostAllocation> selectFailedAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询超时的成本分摊列表
     */
    List<SsCostAllocation> selectTimeoutAllocation(@Param("timeoutMinutes") Integer timeoutMinutes, @Param("tenantId") Long tenantId);

    /**
     * 查询高优先级的成本分摊列表
     */
    List<SsCostAllocation> selectHighPriorityAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询自动分摊的成本分摊列表
     */
    List<SsCostAllocation> selectAutoAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询实时分摊的成本分摊列表
     */
    List<SsCostAllocation> selectRealtimeAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询需要审批的成本分摊列表
     */
    List<SsCostAllocation> selectPendingApproval(@Param("tenantId") Long tenantId);

    /**
     * 查询已审批的成本分摊列表
     */
    List<SsCostAllocation> selectApprovedAllocation(@Param("tenantId") Long tenantId);

    /**
     * 查询被拒绝的成本分摊列表
     */
    List<SsCostAllocation> selectRejectedAllocation(@Param("tenantId") Long tenantId);

    /**
     * 根据父分摊ID查询子分摊列表
     */
    List<SsCostAllocation> selectByParentId(@Param("parentAllocationId") Long parentAllocationId, @Param("tenantId") Long tenantId);

    /**
     * 根据分摊层级查询成本分摊列表
     */
    List<SsCostAllocation> selectByLevel(@Param("level") Integer level, @Param("tenantId") Long tenantId);

    /**
     * 根据分摊深度查询成本分摊列表
     */
    List<SsCostAllocation> selectByDepth(@Param("depth") Integer depth, @Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊统计信息
     */
    Map<String, Object> selectAllocationStatistics(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊状态分布
     */
    List<Map<String, Object>> selectStatusDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊类型分布
     */
    List<Map<String, Object>> selectTypeDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊方法分布
     */
    List<Map<String, Object>> selectMethodDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊周期分布
     */
    List<Map<String, Object>> selectPeriodDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊优先级分布
     */
    List<Map<String, Object>> selectPriorityDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊金额统计
     */
    Map<String, Object> selectAmountStatistics(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊成功率统计
     */
    Map<String, Object> selectSuccessRateStatistics(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊耗时统计
     */
    Map<String, Object> selectDurationStatistics(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊趋势数据
     */
    List<Map<String, Object>> selectAllocationTrend(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate, 
                                                   @Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊排行榜
     */
    List<Map<String, Object>> selectAllocationRanking(@Param("rankType") String rankType, 
                                                     @Param("limit") Integer limit, 
                                                     @Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊效率分析
     */
    List<Map<String, Object>> selectEfficiencyAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊质量分析
     */
    List<Map<String, Object>> selectQualityAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊成本分析
     */
    List<Map<String, Object>> selectCostAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 查询成本分摊风险分析
     */
    List<Map<String, Object>> selectRiskAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 批量插入成本分摊
     */
    int batchInsert(@Param("list") List<SsCostAllocation> list);

    /**
     * 批量更新成本分摊状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 批量更新成本分摊优先级
     */
    int batchUpdatePriority(@Param("ids") List<Long> ids, @Param("priority") Integer priority, @Param("tenantId") Long tenantId);

    /**
     * 批量删除成本分摊
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("tenantId") Long tenantId);

    /**
     * 更新分摊状态
     */
    int updateAllocationStatus(@Param("allocationId") Long allocationId, @Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 更新计算状态
     */
    int updateCalculationStatus(@Param("allocationId") Long allocationId, @Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 更新审批状态
     */
    int updateApprovalStatus(@Param("allocationId") Long allocationId, @Param("status") String status, @Param("tenantId") Long tenantId);

    /**
     * 更新分摊结果
     */
    int updateAllocationResult(@Param("allocationId") Long allocationId, @Param("result") String result, @Param("tenantId") Long tenantId);

    /**
     * 更新分摊进度
     */
    int updateAllocationProgress(@Param("allocationId") Long allocationId, @Param("completedCount") Integer completedCount, @Param("tenantId") Long tenantId);

    /**
     * 清理过期数据
     */
    int cleanExpiredData(@Param("expiredDays") Integer expiredDays, @Param("tenantId") Long tenantId);

    /**
     * 统计记录数量
     */
    Long countByCondition(@Param("params") Map<String, Object> params);

    /**
     * 检查编码是否存在
     */
    int checkCodeExists(@Param("allocationCode") String allocationCode, @Param("allocationId") Long allocationId, @Param("tenantId") Long tenantId);

    /**
     * 检查名称是否存在
     */
    int checkNameExists(@Param("allocationName") String allocationName, @Param("allocationId") Long allocationId, @Param("tenantId") Long tenantId);
}
