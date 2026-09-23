package com.financial.sharing.oracle.mapper;

import com.financial.sharing.dto.VoucherStatisticsQueryParam;
import com.financial.sharing.dto.VoucherTrendParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 凭证统计 Mapper接口 - Oracle/达梦版本
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface VoucherStatisticsMapper {

    /**
     * 查询凭证总数
     *
     * @param param 查询参数
     * @return 凭证总数
     */
    Long selectTotalCount(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询凭证总金额
     *
     * @param param 查询参数
     * @return 凭证总金额
     */
    BigDecimal selectTotalAmount(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 按状态统计凭证数量
     *
     * @param param 查询参数
     * @return 状态统计列表
     */
    List<Map<String, Object>> selectCountByStatus(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 按期间统计凭证数量
     *
     * @param param 查询参数
     * @return 期间统计列表
     */
    List<Map<String, Object>> selectCountByPeriod(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 按凭证类型统计凭证数量
     *
     * @param param 查询参数
     * @return 类型统计列表
     */
    List<Map<String, Object>> selectCountByType(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 按制单人统计凭证数量
     *
     * @param param 查询参数
     * @return 制单人统计列表
     */
    List<Map<String, Object>> selectCountByPreparer(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 按币种统计凭证数量
     *
     * @param param 查询参数
     * @return 币种统计列表
     */
    List<Map<String, Object>> selectCountByCurrency(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询凭证分录总数
     *
     * @param param 查询参数
     * @return 分录总数
     */
    Long selectTotalEntryCount(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询借方总金额
     *
     * @param param 查询参数
     * @return 借方总金额
     */
    BigDecimal selectTotalDebitAmount(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询贷方总金额
     *
     * @param param 查询参数
     * @return 贷方总金额
     */
    BigDecimal selectTotalCreditAmount(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询统计趋势数据
     *
     * @param param 查询参数
     * @return 趋势数据列表
     */
    List<Map<String, Object>> selectStatisticsTrend(@Param("param") VoucherTrendParam param);

    /**
     * 获取会计期间列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 期间列表
     */
    List<String> selectAccountingPeriods(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取凭证类型列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 凭证类型列表
     */
    List<Map<String, Object>> selectVoucherTypes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取制单人列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 制单人列表
     */
    List<Map<String, Object>> selectPreparers(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取币种列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种列表
     */
    List<Map<String, Object>> selectCurrencies(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 查询凭证汇总统计（多维度）
     *
     * @param param 查询参数
     * @return 汇总统计数据
     */
    Map<String, Object> selectVoucherSummaryStatistics(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询每日凭证统计
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日统计列表
     */
    List<Map<String, Object>> selectDailyStatistics(@Param("bookId") Long bookId,
                                                   @Param("tenantId") Long tenantId,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    /**
     * 查询每月凭证统计
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param year 年份
     * @return 每月统计列表
     */
    List<Map<String, Object>> selectMonthlyStatistics(@Param("bookId") Long bookId,
                                                     @Param("tenantId") Long tenantId,
                                                     @Param("year") Integer year);

    /**
     * 查询凭证分录明细统计
     *
     * @param param 查询参数
     * @return 分录明细统计
     */
    List<Map<String, Object>> selectEntryDetailStatistics(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 查询科目使用频率统计
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 限制数量
     * @return 科目使用频率统计
     */
    List<Map<String, Object>> selectAccountUsageFrequency(@Param("bookId") Long bookId,
                                                          @Param("tenantId") Long tenantId,
                                                          @Param("startDate") String startDate,
                                                          @Param("endDate") String endDate,
                                                          @Param("limit") Integer limit);

    // ==================== 凭证分析功能需要的方法 ====================

    /**
     * 凭证金额统计
     *
     * @param param 查询参数
     * @return 金额统计数据
     */
    Map<String, Object> getVoucherAmountStatistics(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 凭证生成趋势分析
     *
     * @param param 查询参数
     * @return 趋势数据列表
     */
    List<Map<String, Object>> getVoucherGenerationTrend(@Param("param") VoucherTrendParam param);

    /**
     * 科目使用频次统计
     *
     * @param param 查询参数
     * @return 科目使用统计列表
     */
    List<Map<String, Object>> getSubjectUsageStatistics(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 凭证效率分析
     *
     * @param param 查询参数
     * @return 效率统计数据
     */
    Map<String, Object> getVoucherEfficiencyAnalysis(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 异常凭证分析
     *
     * @param param 查询参数
     * @return 异常凭证列表
     */
    List<Map<String, Object>> getAbnormalVoucherAnalysis(@Param("param") VoucherStatisticsQueryParam param);

    /**
     * 凭证复核质量分析
     *
     * @param param 查询参数
     * @return 复核质量统计数据
     */
    List<Map<String, Object>> getVoucherReviewQualityAnalysis(@Param("param") VoucherStatisticsQueryParam param);
}