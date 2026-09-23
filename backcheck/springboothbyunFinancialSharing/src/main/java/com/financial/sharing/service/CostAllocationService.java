package com.financial.sharing.service;

import com.financial.sharing.oracle.entity.CostAllocationEntity;
import com.financial.sharing.oracle.entity.CostAllocationDetailEntity;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.CostAllocationParam;
import com.financial.sharing.vo.param.CostAllocationQueryParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成本分摊Service接口
 *
 * @author Financial Sharing System
 * @since 2024-12-06
 */
public interface CostAllocationService {

    // ==================== 核心业务方法 ====================

    /**
     * 开始成本分摊
     *
     * @param param 成本分摊参数
     * @return 分摊结果
     */
    MyJsonBean<CostAllocationEntity> startCostAllocation(CostAllocationParam param);

    /**
     * 分页查询成本分摊列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<Map<String, Object>>> getCostAllocationList(CostAllocationQueryParam param);

    /**
     * 获取成本分摊详情
     *
     * @param allocationId 分摊ID
     * @return 分摊详情
     */
    MyJsonBean<Map<String, Object>> getCostAllocationDetail(Long allocationId);

    /**
     * 获取成本分摊明细列表
     *
     * @param allocationId 分摊ID
     * @param pageNumber 页码
     * @param pageSize 页面大小
     * @return 明细列表
     */
    MyJsonBean<PageResult<CostAllocationDetailEntity>> getAllocationDetailList(Long allocationId,
                                                                              Integer pageNumber,
                                                                              Integer pageSize);

    // ==================== 状态管理方法 ====================

    /**
     * 审核成本分摊
     *
     * @param allocationId 分摊ID
     * @param auditData 审核数据
     * @return 审核结果
     */
    MyJsonBean auditCostAllocation(Long allocationId, Map<String, Object> auditData);

    /**
     * 批量审核成本分摊
     *
     * @param allocationIds 分摊ID列表
     * @param auditData 审核数据
     * @return 审核结果
     */
    MyJsonBean batchAuditCostAllocation(List<Long> allocationIds, Map<String, Object> auditData);

    /**
     * 分摊成本分摊
     *
     * @param allocationId 分摊ID
     * @param allocateData 分摊数据
     * @return 分摊结果
     */
    MyJsonBean allocateCostAllocation(Long allocationId, Map<String, Object> allocateData);

    /**
     * 批量分摊成本分摊
     *
     * @param allocationIds 分摊ID列表
     * @param allocateData 分摊数据
     * @return 分摊结果
     */
    MyJsonBean batchAllocateCostAllocation(List<Long> allocationIds, Map<String, Object> allocateData);

    /**
     * 取消成本分摊
     *
     * @param allocationId 分摊ID
     * @param cancelData 取消数据
     * @return 取消结果
     */
    MyJsonBean cancelCostAllocation(Long allocationId, Map<String, Object> cancelData);

    /**
     * 批量取消成本分摊
     *
     * @param allocationIds 分摊ID列表
     * @param cancelData 取消数据
     * @return 取消结果
     */
    MyJsonBean batchCancelCostAllocation(List<Long> allocationIds, Map<String, Object> cancelData);

    // ==================== 统计分析方法 ====================

    /**
     * 获取成本分摊概览数据
     *
     * @param period 分摊期间
     * @return 概览数据
     */
    MyJsonBean<Map<String, Object>> getCostAllocationOverview(String period);

    /**
     * 获取成本分摊统计信息
     *
     * @param param 查询参数
     * @return 统计信息
     */
    MyJsonBean<Map<String, Object>> getCostAllocationStats(CostAllocationQueryParam param);

    /**
     * 获取成本分摊趋势数据
     *
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 趋势数据
     */
    MyJsonBean<List<Map<String, Object>>> getAllocationTrend(String startPeriod, String endPeriod);

    /**
     * 获取各分摊方法的统计数量
     *
     * @param param 查询参数
     * @return 方法统计
     */
    MyJsonBean<List<Map<String, Object>>> getAllocationMethodStats(CostAllocationQueryParam param);

    /**
     * 获取各状态的统计数量
     *
     * @param param 查询参数
     * @return 状态统计
     */
    MyJsonBean<List<Map<String, Object>>> getAllocationStatusStats(CostAllocationQueryParam param);

    // ==================== 成本中心分析 ====================

    /**
     * 获取成本中心的分摊历史
     *
     * @param centerId 成本中心ID
     * @param limit 限制数量
     * @return 分摊历史
     */
    MyJsonBean<List<Map<String, Object>>> getCenterAllocationHistory(String centerId, Integer limit);

    /**
     * 获取源成本中心的分摊去向
     *
     * @param sourceCenterId 源成本中心ID
     * @param period 分摊期间
     * @return 分摊去向
     */
    MyJsonBean<List<Map<String, Object>>> getSourceCenterAllocation(String sourceCenterId, String period);

    /**
     * 获取目标成本中心的分摊来源
     *
     * @param targetCenterId 目标成本中心ID
     * @param period 分摊期间
     * @return 分摊来源
     */
    MyJsonBean<List<Map<String, Object>>> getTargetCenterAllocation(String targetCenterId, String period);

    // ==================== 验证和检查方法 ====================

    /**
     * 验证成本分摊参数
     *
     * @param param 分摊参数
     * @return 验证结果
     */
    MyJsonBean validateAllocationParameters(CostAllocationParam param);

    /**
     * 检查分摊单号是否可用
     *
     * @param allocationNo 分摊单号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 检查结果
     */
    MyJsonBean checkAllocationNoAvailable(String allocationNo, String bookId, String tenantId);

    /**
     * 检查成本中心是否可以进行分摊
     *
     * @param sourceCenterId 源成本中心ID
     * @param targetCenterIds 目标成本中心ID列表
     * @param period 分摊期间
     * @return 检查结果
     */
    MyJsonBean checkCentersForAllocation(String sourceCenterId, List<String> targetCenterIds, String period);

    // ==================== 数据维护方法 ====================

    /**
     * 重新计算分摊金额
     *
     * @param allocationId 分摊ID
     * @param newTotalAmount 新的分摊总额
     * @return 计算结果
     */
    MyJsonBean recalculateAllocationAmount(Long allocationId, Double newTotalAmount);

    /**
     * 调整分摊明细
     *
     * @param detailId 明细ID
     * @param newRatio 新的分摊比例
     * @return 调整结果
     */
    MyJsonBean adjustAllocationDetail(Long detailId, Double newRatio);

    /**
     * 批量更新分摊明细
     *
     * @param allocationId 分摊ID
     * @param details 新的明细列表
     * @return 更新结果
     */
    MyJsonBean batchUpdateAllocationDetails(Long allocationId, List<CostAllocationDetailEntity> details);

    // ==================== 凭证相关方法 ====================

    /**
     * 生成成本分摊凭证
     *
     * @param allocationId 分摊ID
     * @return 凭证生成结果
     */
    MyJsonBean generateAllocationVoucher(Long allocationId);

    /**
     * 取消成本分摊凭证
     *
     * @param allocationId 分摊ID
     * @return 取消结果
     */
    MyJsonBean cancelAllocationVoucher(Long allocationId);

    /**
     * 批量生成分摊凭证
     *
     * @param allocationIds 分摊ID列表
     * @return 凭证生成结果
     */
    MyJsonBean batchGenerateAllocationVouchers(List<Long> allocationIds);

    // ==================== 导出功能 ====================

    /**
     * 导出成本分摊数据
     *
     * @param param 查询参数
     * @return 导出结果
     */
    MyJsonBean exportAllocationData(CostAllocationQueryParam param);

    /**
     * 导出分摊明细数据
     *
     * @param allocationId 分摊ID
     * @return 导出结果
     */
    MyJsonBean exportAllocationDetailData(Long allocationId);

    /**
     * 导出成本分摊统计报表
     *
     * @param param 查询参数
     * @return 导出结果
     */
    MyJsonBean exportAllocationStatsReport(CostAllocationQueryParam param);

    // ==================== 系统管理方法 ====================

    /**
     * 清理历史数据
     *
     * @param beforeDate 清理日期
     * @return 清理结果
     */
    MyJsonBean cleanupHistoryData(String beforeDate);

    /**
     * 重建统计数据
     *
     * @return 重建结果
     */
    MyJsonBean rebuildStatistics();

    /**
     * 获取系统配置信息
     *
     * @return 配置信息
     */
    MyJsonBean<Map<String, Object>> getSystemConfig();

    // ==================== 批处理方法 ====================

    /**
     * 批量创建成本分摊
     *
     * @param params 分摊参数列表
     * @return 创建结果
     */
    MyJsonBean batchCreateAllocation(List<CostAllocationParam> params);

    /**
     * 自动成本分摊
     *
     * @param period 分摊期间
     * @param centerIds 成本中心ID列表
     * @return 分摊结果
     */
    MyJsonBean autoCostAllocation(String period, List<String> centerIds);

    /**
     * 计划成本分摊
     *
     * @param scheduleConfig 计划配置
     * @return 计划结果
     */
    MyJsonBean scheduleAllocation(Map<String, Object> scheduleConfig);

    // ==================== 监控和预警 ====================

    /**
     * 获取成本分摊执行状态
     *
     * @param allocationId 分摊ID
     * @return 执行状态
     */
    MyJsonBean<Map<String, Object>> getAllocationExecutionStatus(Long allocationId);

    /**
     * 获取异常分摊列表
     *
     * @param param 查询参数
     * @return 异常列表
     */
    MyJsonBean<List<Map<String, Object>>> getExceptionAllocations(CostAllocationQueryParam param);

    /**
     * 获取分摊预警信息
     *
     * @param period 分摊期间
     * @return 预警信息
     */
    MyJsonBean<List<Map<String, Object>>> getAllocationAlerts(String period);

    /**
     * 导出成本分摊结果
     *
     * @param period 分摊期间
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param response HTTP响应
     */
    void exportAllocationResult(String period, Long bookId, Long tenantId, HttpServletResponse response);
}