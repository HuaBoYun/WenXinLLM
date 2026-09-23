package com.financial.sharing.service;

import com.financial.sharing.dto.VoucherStatisticsQueryParam;
import com.financial.sharing.dto.VoucherTrendParam;
import com.hbfk.entity.TblStaffUtil;

import java.util.List;
import java.util.Map;

/**
 * 凭证统计服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface VoucherStatisticsService {

    /**
     * 凭证汇总统计
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    Map<String, Object> getVoucherSummary(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证数量统计(按状态)
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    List<Map<String, Object>> getVoucherCountByStatus(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证数量统计(按期间)
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    List<Map<String, Object>> getVoucherCountByPeriod(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证数量统计(按凭证类型)
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    List<Map<String, Object>> getVoucherCountByType(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证数量统计(按制单人)
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    List<Map<String, Object>> getVoucherCountByPreparer(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 分币种统计
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    List<Map<String, Object>> getVoucherCountByCurrency(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证分录统计
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    Map<String, Object> getVoucherEntryStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证统计趋势
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 统计结果
     */
    List<Map<String, Object>> getVoucherStatisticsTrend(VoucherTrendParam param, TblStaffUtil loginStaff);

    /**
     * 获取统计筛选条件
     *
     * @param bookId 账簿ID
     * @param loginStaff 登录用户信息
     * @return 筛选条件
     */
    Map<String, Object> getFilterOptions(Long bookId, TblStaffUtil loginStaff);

    /**
     * 导出凭证统计报表
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportVoucherStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 获取凭证统计仪表盘数据
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 仪表盘数据
     */
    Map<String, Object> getVoucherStatisticsDashboard(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    // ========== 兼容旧接口 ==========

    /**
     * 获取凭证统计信息（兼容旧接口）
     */
    Map<String, Object> getVoucherStatistics(VoucherStatisticsQueryParam param);

    /**
     * 获取仪表板统计数据（兼容旧接口）
     */
    Map<String, Object> getDashboardStatistics(Long bookId, Long tenantId);

    /**
     * 获取凭证趋势数据（兼容旧接口）
     */
    List<Map<String, Object>> getVoucherTrend(VoucherTrendParam param);

    /**
     * 获取凭证汇总数据（兼容旧接口）
     */
    Map<String, Object> getVoucherSummary(Long bookId, Long tenantId, String startDate, String endDate);

    /**
     * 按状态统计凭证（兼容旧接口）
     */
    List<Map<String, Object>> getStatisticsByStatus(Long bookId, Long tenantId, String startDate, String endDate);

    /**
     * 按类型统计凭证（兼容旧接口）
     */
    List<Map<String, Object>> getStatisticsByType(Long bookId, Long tenantId, String startDate, String endDate);

    /**
     * 凭证金额统计
     */
    Map<String, Object> getVoucherAmountStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证生成趋势
     */
    List<Map<String, Object>> getVoucherGenerationTrend(VoucherTrendParam param, TblStaffUtil loginStaff);

    /**
     * 凭证效率分析
     */
    Map<String, Object> getVoucherEfficiencyAnalysis(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 异常凭证分析
     */
    List<Map<String, Object>> getAbnormalVoucherAnalysis(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 科目使用统计
     */
    List<Map<String, Object>> getSubjectUsageStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);

    /**
     * 凭证审核质量分析
     */
    List<Map<String, Object>> getVoucherReviewQualityAnalysis(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff);
}