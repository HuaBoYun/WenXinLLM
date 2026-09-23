package com.management.accountant.mapper.ts;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsInvoiceManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 发票管理 Mapper 接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface TsInvoiceManagementMapper extends BaseMapper<TsInvoiceManagement> {

    /**
     * 分页查询发票管理列表
     */
    IPage<TsInvoiceManagement> selectInvoiceManagementPage(Page<TsInvoiceManagement> page, 
                                                          @Param("tenantId") Long tenantId,
                                                          @Param("invoiceCode") String invoiceCode,
                                                          @Param("invoiceNumber") String invoiceNumber,
                                                          @Param("invoiceType") String invoiceType,
                                                          @Param("invoiceStatus") String invoiceStatus,
                                                          @Param("sellerName") String sellerName,
                                                          @Param("buyerName") String buyerName,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate,
                                                          @Param("riskLevel") String riskLevel,
                                                          @Param("businessCategory") String businessCategory);

    /**
     * 根据发票代码和号码查询发票
     */
    TsInvoiceManagement selectByInvoiceCodeAndNumber(@Param("tenantId") Long tenantId,
                                                    @Param("invoiceCode") String invoiceCode,
                                                    @Param("invoiceNumber") String invoiceNumber);

    /**
     * 根据销售方税号查询发票列表
     */
    List<TsInvoiceManagement> selectBySellerTaxNumber(@Param("tenantId") Long tenantId,
                                                     @Param("sellerTaxNumber") String sellerTaxNumber);

    /**
     * 根据购买方税号查询发票列表
     */
    List<TsInvoiceManagement> selectByBuyerTaxNumber(@Param("tenantId") Long tenantId,
                                                    @Param("buyerTaxNumber") String buyerTaxNumber);

    /**
     * 查询待识别的发票列表
     */
    List<TsInvoiceManagement> selectPendingOcrInvoices(@Param("tenantId") Long tenantId,
                                                      @Param("limit") Integer limit);

    /**
     * 查询待验真的发票列表
     */
    List<TsInvoiceManagement> selectPendingVerificationInvoices(@Param("tenantId") Long tenantId,
                                                               @Param("limit") Integer limit);

    /**
     * 查询待归档的发票列表
     */
    List<TsInvoiceManagement> selectPendingArchiveInvoices(@Param("tenantId") Long tenantId,
                                                          @Param("limit") Integer limit);

    /**
     * 查询高风险发票列表
     */
    List<TsInvoiceManagement> selectHighRiskInvoices(@Param("tenantId") Long tenantId,
                                                    @Param("riskLevels") List<String> riskLevels);

    /**
     * 查询重复发票
     */
    List<TsInvoiceManagement> selectDuplicateInvoices(@Param("tenantId") Long tenantId,
                                                     @Param("invoiceCode") String invoiceCode,
                                                     @Param("invoiceNumber") String invoiceNumber,
                                                     @Param("excludeId") Long excludeId);

    /**
     * 统计发票数量按状态分组
     */
    List<Map<String, Object>> countInvoicesByStatus(@Param("tenantId") Long tenantId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * 统计发票数量按类型分组
     */
    List<Map<String, Object>> countInvoicesByType(@Param("tenantId") Long tenantId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    /**
     * 统计发票金额按月份分组
     */
    List<Map<String, Object>> sumAmountByMonth(@Param("tenantId") Long tenantId,
                                              @Param("year") Integer year);

    /**
     * 统计发票金额按销售方分组
     */
    List<Map<String, Object>> sumAmountBySeller(@Param("tenantId") Long tenantId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("limit") Integer limit);

    /**
     * 统计发票金额按购买方分组
     */
    List<Map<String, Object>> sumAmountByBuyer(@Param("tenantId") Long tenantId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("limit") Integer limit);

    /**
     * 统计OCR识别成功率
     */
    Map<String, Object> calculateOcrSuccessRate(@Param("tenantId") Long tenantId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * 统计验真成功率
     */
    Map<String, Object> calculateVerificationSuccessRate(@Param("tenantId") Long tenantId,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);

    /**
     * 统计风险发票分布
     */
    List<Map<String, Object>> countInvoicesByRiskLevel(@Param("tenantId") Long tenantId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票处理时长统计
     */
    Map<String, Object> calculateProcessingTimeStats(@Param("tenantId") Long tenantId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票金额统计
     */
    Map<String, Object> calculateAmountStats(@Param("tenantId") Long tenantId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票税额统计
     */
    Map<String, Object> calculateTaxAmountStats(@Param("tenantId") Long tenantId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票数量趋势
     */
    List<Map<String, Object>> getInvoiceCountTrend(@Param("tenantId") Long tenantId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  @Param("groupBy") String groupBy);

    /**
     * 查询发票金额趋势
     */
    List<Map<String, Object>> getInvoiceAmountTrend(@Param("tenantId") Long tenantId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("groupBy") String groupBy);

    /**
     * 查询异常发票列表
     */
    List<TsInvoiceManagement> selectAbnormalInvoices(@Param("tenantId") Long tenantId,
                                                    @Param("abnormalTypes") List<String> abnormalTypes);

    /**
     * 查询需要重试的发票列表
     */
    List<TsInvoiceManagement> selectRetryInvoices(@Param("tenantId") Long tenantId,
                                                 @Param("currentTime") LocalDateTime currentTime);

    /**
     * 批量更新发票状态
     */
    int batchUpdateStatus(@Param("tenantId") Long tenantId,
                         @Param("invoiceIds") List<Long> invoiceIds,
                         @Param("status") String status,
                         @Param("updatedBy") Long updatedBy,
                         @Param("updatedName") String updatedName);

    /**
     * 批量更新OCR状态
     */
    int batchUpdateOcrStatus(@Param("tenantId") Long tenantId,
                            @Param("invoiceIds") List<Long> invoiceIds,
                            @Param("ocrStatus") String ocrStatus,
                            @Param("ocrResult") String ocrResult,
                            @Param("ocrConfidence") BigDecimal ocrConfidence,
                            @Param("updatedBy") Long updatedBy,
                            @Param("updatedName") String updatedName);

    /**
     * 批量更新验真状态
     */
    int batchUpdateVerificationStatus(@Param("tenantId") Long tenantId,
                                     @Param("invoiceIds") List<Long> invoiceIds,
                                     @Param("verificationStatus") String verificationStatus,
                                     @Param("verificationResult") String verificationResult,
                                     @Param("updatedBy") Long updatedBy,
                                     @Param("updatedName") String updatedName);

    /**
     * 批量更新归档状态
     */
    int batchUpdateArchiveStatus(@Param("tenantId") Long tenantId,
                                @Param("invoiceIds") List<Long> invoiceIds,
                                @Param("archiveStatus") String archiveStatus,
                                @Param("archivePath") String archivePath,
                                @Param("updatedBy") Long updatedBy,
                                @Param("updatedName") String updatedName);

    /**
     * 批量删除发票
     */
    int batchDeleteInvoices(@Param("tenantId") Long tenantId,
                           @Param("invoiceIds") List<Long> invoiceIds,
                           @Param("updatedBy") Long updatedBy,
                           @Param("updatedName") String updatedName);

    /**
     * 清理过期发票
     */
    int cleanExpiredInvoices(@Param("tenantId") Long tenantId,
                            @Param("expiredDate") LocalDateTime expiredDate);

    /**
     * 查询发票详细信息（包含关联数据）
     */
    TsInvoiceManagement selectInvoiceDetailById(@Param("tenantId") Long tenantId,
                                               @Param("invoiceId") Long invoiceId);

    /**
     * 查询发票统计概览
     */
    Map<String, Object> getInvoiceOverview(@Param("tenantId") Long tenantId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票处理效率统计
     */
    Map<String, Object> getProcessingEfficiencyStats(@Param("tenantId") Long tenantId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票质量统计
     */
    Map<String, Object> getInvoiceQualityStats(@Param("tenantId") Long tenantId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票合规性统计
     */
    Map<String, Object> getComplianceStats(@Param("tenantId") Long tenantId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票风险分析
     */
    Map<String, Object> getRiskAnalysis(@Param("tenantId") Long tenantId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * 查询发票成本分析
     */
    Map<String, Object> getCostAnalysis(@Param("tenantId") Long tenantId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
}
