package com.management.accountant.mapper.ts;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsTaxDeclaration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务申报 Mapper 接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface TsTaxDeclarationMapper extends BaseMapper<TsTaxDeclaration> {

    /**
     * 分页查询税务申报列表
     */
    IPage<TsTaxDeclaration> selectTaxDeclarationPage(Page<TsTaxDeclaration> page,
                                                    @Param("tenantId") Long tenantId,
                                                    @Param("declarationCode") String declarationCode,
                                                    @Param("declarationName") String declarationName,
                                                    @Param("taxType") String taxType,
                                                    @Param("declarationType") String declarationType,
                                                    @Param("declarationStatus") String declarationStatus,
                                                    @Param("taxpayerName") String taxpayerName,
                                                    @Param("declarationPeriod") String declarationPeriod,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    @Param("businessCategory") String businessCategory);

    /**
     * 根据申报编号查询申报
     */
    TsTaxDeclaration selectByDeclarationCode(@Param("tenantId") Long tenantId,
                                           @Param("declarationCode") String declarationCode);

    /**
     * 根据纳税人识别号查询申报列表
     */
    List<TsTaxDeclaration> selectByTaxpayerId(@Param("tenantId") Long tenantId,
                                            @Param("taxpayerId") String taxpayerId);

    /**
     * 查询待申报的申报列表
     */
    List<TsTaxDeclaration> selectPendingDeclarations(@Param("tenantId") Long tenantId,
                                                   @Param("limit") Integer limit);

    /**
     * 查询待审核的申报列表
     */
    List<TsTaxDeclaration> selectPendingReviewDeclarations(@Param("tenantId") Long tenantId,
                                                         @Param("limit") Integer limit);

    /**
     * 查询逾期申报列表
     */
    List<TsTaxDeclaration> selectOverdueDeclarations(@Param("tenantId") Long tenantId,
                                                   @Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询即将到期申报列表
     */
    List<TsTaxDeclaration> selectUpcomingDeclarations(@Param("tenantId") Long tenantId,
                                                    @Param("days") Integer days);

    /**
     * 查询异常申报列表
     */
    List<TsTaxDeclaration> selectAbnormalDeclarations(@Param("tenantId") Long tenantId,
                                                    @Param("errorTypes") List<String> errorTypes);

    /**
     * 查询需要重试的申报列表
     */
    List<TsTaxDeclaration> selectRetryDeclarations(@Param("tenantId") Long tenantId);

    /**
     * 统计申报数量按状态分组
     */
    List<Map<String, Object>> countDeclarationsByStatus(@Param("tenantId") Long tenantId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    /**
     * 统计申报数量按税种分组
     */
    List<Map<String, Object>> countDeclarationsByTaxType(@Param("tenantId") Long tenantId,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);

    /**
     * 统计申报数量按类型分组
     */
    List<Map<String, Object>> countDeclarationsByType(@Param("tenantId") Long tenantId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    /**
     * 统计申报金额按月份分组
     */
    List<Map<String, Object>> sumTaxAmountByMonth(@Param("tenantId") Long tenantId,
                                                 @Param("year") Integer year);

    /**
     * 统计申报金额按税种分组
     */
    List<Map<String, Object>> sumTaxAmountByTaxType(@Param("tenantId") Long tenantId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * 计算申报及时率
     */
    Map<String, Object> calculateTimelyRate(@Param("tenantId") Long tenantId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * 计算申报成功率
     */
    Map<String, Object> calculateSuccessRate(@Param("tenantId") Long tenantId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    /**
     * 获取申报概览统计
     */
    Map<String, Object> getDeclarationOverview(@Param("tenantId") Long tenantId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * 统计申报处理时长
     */
    Map<String, Object> calculateProcessingTimeStats(@Param("tenantId") Long tenantId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    /**
     * 获取申报趋势数据
     */
    List<Map<String, Object>> getDeclarationTrend(@Param("tenantId") Long tenantId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate,
                                                 @Param("groupBy") String groupBy);

    /**
     * 获取税额趋势数据
     */
    List<Map<String, Object>> getTaxAmountTrend(@Param("tenantId") Long tenantId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("groupBy") String groupBy);

    /**
     * 获取申报效率统计
     */
    Map<String, Object> getDeclarationEfficiencyStats(@Param("tenantId") Long tenantId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * 获取申报质量统计
     */
    Map<String, Object> getDeclarationQualityStats(@Param("tenantId") Long tenantId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * 获取合规性统计
     */
    Map<String, Object> getComplianceStats(@Param("tenantId") Long tenantId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * 获取风险分析
     */
    Map<String, Object> getRiskAnalysis(@Param("tenantId") Long tenantId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * 获取成本分析
     */
    Map<String, Object> getCostAnalysis(@Param("tenantId") Long tenantId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * 批量更新申报状态
     */
    int batchUpdateStatus(@Param("tenantId") Long tenantId,
                         @Param("declarationIds") List<Long> declarationIds,
                         @Param("status") String status,
                         @Param("updatedBy") String updatedBy,
                         @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 批量删除申报
     */
    int batchDeleteDeclarations(@Param("tenantId") Long tenantId,
                               @Param("declarationIds") List<Long> declarationIds,
                               @Param("updatedBy") String updatedBy,
                               @Param("updatedTime") LocalDateTime updatedTime);

    /**
     * 高级搜索申报
     */
    IPage<TsTaxDeclaration> advancedSearchDeclarations(Page<TsTaxDeclaration> page,
                                                      @Param("tenantId") Long tenantId,
                                                      @Param("searchParams") Map<String, Object> searchParams);

    /**
     * 全文搜索申报
     */
    IPage<TsTaxDeclaration> fullTextSearchDeclarations(Page<TsTaxDeclaration> page,
                                                      @Param("tenantId") Long tenantId,
                                                      @Param("keyword") String keyword);

    /**
     * 清理过期申报
     */
    int cleanExpiredDeclarations(@Param("tenantId") Long tenantId,
                                @Param("expiredDate") LocalDateTime expiredDate);

    /**
     * 获取系统性能指标
     */
    Map<String, Object> getPerformanceMetrics(@Param("tenantId") Long tenantId);

    /**
     * 系统健康检查
     */
    Map<String, Object> healthCheck(@Param("tenantId") Long tenantId);
}
