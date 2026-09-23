package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InternalSettlementEntity;
import com.financial.sharing.vo.param.InternalSettlementQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 内部结算Mapper接口 - Oracle/达梦数据库版本
 * 
 * @author system
 * @since 2024-12-19
 */
public interface InternalSettlementMapper extends BaseMapper<InternalSettlementEntity> {

    /**
     * 分页查询内部结算列表
     */
    IPage<InternalSettlementEntity> selectInternalSettlementPage(Page<InternalSettlementEntity> page, 
                                                                @Param("param") InternalSettlementQueryParam param);

    /**
     * 查询内部结算详情
     */
    InternalSettlementEntity selectInternalSettlementDetail(@Param("settlementId") Long settlementId);

    /**
     * 根据结算单号查询
     */
    InternalSettlementEntity selectBySettlementNo(@Param("settlementNo") String settlementNo);

    /**
     * 查询内部结算统计信息
     */
    Map<String, Object> selectInternalSettlementStats(@Param("param") InternalSettlementQueryParam param);

    /**
     * 批量插入内部结算记录
     */
    int batchInsertInternalSettlement(@Param("list") List<InternalSettlementEntity> list);

    /**
     * 批量更新内部结算状态
     */
    int batchUpdateSettlementStatus(@Param("settlementIds") List<Long> settlementIds, 
                                   @Param("status") Integer status, 
                                   @Param("updater") Long updater);

    /**
     * 根据条件删除内部结算记录
     */
    int deleteByCondition(@Param("param") InternalSettlementQueryParam param);

    // ==================== 内部交易相关方法 ====================

    /**
     * 查询内部交易列表
     */
    IPage<InternalSettlementEntity> selectInternalTransactionPage(Page<InternalSettlementEntity> page, 
                                                                 @Param("param") InternalSettlementQueryParam param);

    /**
     * 查询内部交易统计
     */
    Map<String, Object> selectInternalTransactionStats(@Param("param") InternalSettlementQueryParam param);

    /**
     * 根据交易单号查询
     */
    InternalSettlementEntity selectByTransactionNo(@Param("transactionNo") String transactionNo);

    /**
     * 更新交易状态
     */
    int updateTransactionStatus(@Param("transactionId") Long transactionId, 
                               @Param("status") Integer status, 
                               @Param("updater") Long updater);

    /**
     * 批量审批交易
     */
    int batchApproveTransactions(@Param("transactionIds") List<Long> transactionIds, 
                                @Param("approver") Long approver, 
                                @Param("comment") String comment);

    // ==================== 转移定价相关方法 ====================

    /**
     * 查询转移定价策略列表
     */
    IPage<InternalSettlementEntity> selectTransferPricingPage(Page<InternalSettlementEntity> page, 
                                                             @Param("param") InternalSettlementQueryParam param);

    /**
     * 查询转移定价统计
     */
    Map<String, Object> selectTransferPricingStats(@Param("param") InternalSettlementQueryParam param);

    /**
     * 根据策略编码查询
     */
    InternalSettlementEntity selectByPolicyCode(@Param("policyCode") String policyCode);

    /**
     * 查询有效的定价策略
     */
    List<InternalSettlementEntity> selectActivePricingPolicies(@Param("productName") String productName, 
                                                              @Param("effectiveDate") LocalDate effectiveDate);

    /**
     * 批量更新定价策略状态
     */
    int batchUpdatePolicyStatus(@Param("policyIds") List<Long> policyIds, 
                               @Param("status") Integer status, 
                               @Param("updater") Long updater);

    // ==================== 利润中心相关方法 ====================

    /**
     * 查询利润中心列表
     */
    IPage<InternalSettlementEntity> selectProfitCenterPage(Page<InternalSettlementEntity> page, 
                                                          @Param("param") InternalSettlementQueryParam param);

    /**
     * 查询利润中心统计
     */
    Map<String, Object> selectProfitCenterStats(@Param("param") InternalSettlementQueryParam param);

    /**
     * 根据中心编码查询
     */
    InternalSettlementEntity selectByCenterCode(@Param("centerCode") String centerCode);

    /**
     * 查询利润中心层级结构
     */
    List<InternalSettlementEntity> selectProfitCenterTree(@Param("parentId") Long parentId);

    /**
     * 计算利润中心绩效
     */
    Map<String, Object> calculateCenterPerformance(@Param("centerId") Long centerId, 
                                                  @Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);

    // ==================== 结算处理相关方法 ====================

    /**
     * 查询结算处理列表
     */
    IPage<InternalSettlementEntity> selectSettlementProcessPage(Page<InternalSettlementEntity> page, 
                                                               @Param("param") InternalSettlementQueryParam param);

    /**
     * 查询结算处理统计
     */
    Map<String, Object> selectSettlementProcessStats(@Param("param") InternalSettlementQueryParam param);

    /**
     * 执行自动结算
     */
    int executeAutoSettlement(@Param("param") InternalSettlementQueryParam param);

    /**
     * 生成结算凭证
     */
    int generateSettlementVoucher(@Param("settlementId") Long settlementId, 
                                 @Param("voucherId") Long voucherId, 
                                 @Param("updater") Long updater);

    /**
     * 确认结算结果
     */
    int confirmSettlementResult(@Param("settlementId") Long settlementId, 
                               @Param("confirmer") Long confirmer, 
                               @Param("comment") String comment);

    // ==================== 资金管理相关方法 ====================

    /**
     * 查询资金管理列表
     */
    IPage<InternalSettlementEntity> selectFundManagementPage(Page<InternalSettlementEntity> page, 
                                                            @Param("param") InternalSettlementQueryParam param);

    /**
     * 查询资金管理统计
     */
    Map<String, Object> selectFundManagementStats(@Param("param") InternalSettlementQueryParam param);

    /**
     * 根据调配单号查询
     */
    InternalSettlementEntity selectByAllocationNo(@Param("allocationNo") String allocationNo);

    /**
     * 计算利息
     */
    BigDecimal calculateInterest(@Param("allocationId") Long allocationId, 
                                @Param("calculateDate") LocalDate calculateDate);

    /**
     * 执行资金调配
     */
    int executeFundAllocation(@Param("allocationId") Long allocationId, 
                             @Param("executor") Long executor);

    /**
     * 查询资金池余额
     */
    Map<String, Object> selectFundPoolBalance(@Param("centerId") Long centerId);

    // ==================== 结算分析相关方法 ====================

    /**
     * 查询结算分析数据
     */
    List<Map<String, Object>> selectSettlementAnalysisData(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询结算效率分析
     */
    List<Map<String, Object>> selectSettlementEfficiencyAnalysis(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询交易结构分析
     */
    List<Map<String, Object>> selectTransactionStructureAnalysis(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询利润贡献分析
     */
    List<Map<String, Object>> selectProfitContributionAnalysis(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询成本效益分析
     */
    List<Map<String, Object>> selectCostBenefitAnalysis(@Param("param") InternalSettlementQueryParam param);

    /**
     * 生成优化建议
     */
    List<Map<String, Object>> generateOptimizationSuggestions(@Param("param") InternalSettlementQueryParam param);

    // ==================== 报表相关方法 ====================

    /**
     * 查询内部结算报表数据
     */
    List<Map<String, Object>> selectSettlementReportData(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询内部交易报表数据
     */
    List<Map<String, Object>> selectTransactionReportData(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询转移定价报表数据
     */
    List<Map<String, Object>> selectPricingReportData(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询利润中心报表数据
     */
    List<Map<String, Object>> selectProfitCenterReportData(@Param("param") InternalSettlementQueryParam param);

    /**
     * 查询资金管理报表数据
     */
    List<Map<String, Object>> selectFundManagementReportData(@Param("param") InternalSettlementQueryParam param);

    // ==================== 审计相关方法 ====================

    /**
     * 查询审计日志
     */
    IPage<Map<String, Object>> selectAuditLogPage(Page<Map<String, Object>> page, 
                                                  @Param("param") InternalSettlementQueryParam param);

    /**
     * 记录操作日志
     */
    int insertOperationLog(@Param("settlementId") Long settlementId, 
                          @Param("operation") String operation, 
                          @Param("operator") Long operator, 
                          @Param("content") String content);

    /**
     * 查询变更历史
     */
    List<Map<String, Object>> selectChangeHistory(@Param("settlementId") Long settlementId);

    // ==================== 维护相关方法 ====================

    /**
     * 数据清理
     */
    int cleanupExpiredData(@Param("beforeDate") LocalDate beforeDate);

    /**
     * 数据归档
     */
    int archiveHistoryData(@Param("beforeDate") LocalDate beforeDate);

    /**
     * 重新计算统计数据
     */
    int recalculateStatistics(@Param("param") InternalSettlementQueryParam param);

    /**
     * 数据一致性检查
     */
    List<Map<String, Object>> checkDataConsistency(@Param("param") InternalSettlementQueryParam param);

    /**
     * 检查责任中心ID是否存在
     *
     * @param centerIds 需要检查的中心ID列表
     * @return 存在的中心ID列表
     */
    List<Map<String, Object>> checkResponsibilityCenterIds(@Param("centerIds") List<String> centerIds);
}
