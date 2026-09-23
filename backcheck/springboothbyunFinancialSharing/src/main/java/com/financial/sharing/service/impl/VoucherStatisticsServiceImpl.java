package com.financial.sharing.service.impl;

import com.financial.sharing.dto.VoucherStatisticsQueryParam;
import com.financial.sharing.dto.VoucherTrendParam;
import com.financial.sharing.service.VoucherStatisticsService;
import com.financial.sharing.oracle.mapper.VoucherStatisticsMapper;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 凭证统计服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Service
public class VoucherStatisticsServiceImpl implements VoucherStatisticsService {

    @Resource
    private VoucherStatisticsMapper voucherStatisticsMapper;

    @Override
    @Cacheable(value = "voucherStatistics", key = "#param.toString()")
    public Map<String, Object> getVoucherStatistics(VoucherStatisticsQueryParam param) {
        Map<String, Object> result = new HashMap<>();

        // 根据统计维度返回不同的统计数据
        String dimension = param.getDimension();
        if ("STATUS".equals(dimension)) {
            result.put("statusStatistics", getStatusStatistics(param));
        } else if ("TYPE".equals(dimension)) {
            result.put("typeStatistics", getTypeStatistics(param));
        } else if ("DATE".equals(dimension)) {
            result.put("dateStatistics", getDateStatistics(param));
        } else if ("USER".equals(dimension)) {
            result.put("userStatistics", getUserStatistics(param));
        }
        return result;
    }

    @Override
    @Cacheable(value = "voucherStatistics", key = "'dashboard:' + #bookId + ':' + #tenantId")
    public Map<String, Object> getDashboardStatistics(Long bookId, Long tenantId) {
        Map<String, Object> dashboard = new HashMap<>();

        // 今日统计
        LocalDate today = LocalDate.now();
        dashboard.put("todayCount", getTodayVoucherCount(bookId, tenantId));
        dashboard.put("todayAmount", getTodayVoucherAmount(bookId, tenantId));

        // 本周统计
        dashboard.put("weekCount", getWeekVoucherCount(bookId, tenantId));
        dashboard.put("weekAmount", getWeekVoucherAmount(bookId, tenantId));

        // 本月统计
        dashboard.put("monthCount", getMonthVoucherCount(bookId, tenantId));
        dashboard.put("monthAmount", getMonthVoucherAmount(bookId, tenantId));

        // 待处理统计
        dashboard.put("pendingCount", getPendingVoucherCount(bookId, tenantId));

        // 趋势数据
        dashboard.put("trendData", getRecentTrend(bookId, tenantId, 7));

        return dashboard;
    }

    @Override
    @Cacheable(value = "voucherStatistics", key = "#param.toString()")
    public List<Map<String, Object>> getVoucherTrend(VoucherTrendParam param) {
        List<Map<String, Object>> trendData = new ArrayList<>();

        // 根据趋势维度生成模拟数据
        String trendDimension = param.getTrendDimension();
        LocalDate startDate = param.getStartDate();
        LocalDate endDate = param.getEndDate();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("date", currentDate.format(DateTimeFormatter.ISO_DATE));

            // 模拟数据生成
            Random random = new Random();
            if ("AMOUNT".equals(param.getMetricType())) {
                dataPoint.put("value", random.nextDouble() * 100000);
            } else {
                dataPoint.put("value", random.nextInt(100));
            }

            trendData.add(dataPoint);

            // 根据维度调整日期
            if ("DAY".equals(trendDimension)) {
                currentDate = currentDate.plusDays(1);
            } else if ("WEEK".equals(trendDimension)) {
                currentDate = currentDate.plusWeeks(1);
            } else if ("MONTH".equals(trendDimension)) {
                currentDate = currentDate.plusMonths(1);
            } else if ("QUARTER".equals(trendDimension)) {
                currentDate = currentDate.plusMonths(3);
            } else if ("YEAR".equals(trendDimension)) {
                currentDate = currentDate.plusYears(1);
            }
        }
        return trendData;
    }

    @Override
    public Map<String, Object> getVoucherSummary(Long bookId, Long tenantId, String startDate, String endDate) {
        Map<String, Object> summary = new HashMap<>();

        // 模拟汇总数据
        summary.put("totalCount", 156);
        summary.put("totalAmount", 1256789.50);
        summary.put("draftCount", 12);
        summary.put("approvedCount", 89);
        summary.put("postedCount", 55);

        return summary;
    }

    @Override
    @Cacheable(value = "voucherStatistics", key = "'byStatus:' + #bookId + ':' + #tenantId + ':' + #startDate + ':' + #endDate")
    public List<Map<String, Object>> getStatisticsByStatus(Long bookId, Long tenantId, String startDate, String endDate) {
        List<Map<String, Object>> statistics = new ArrayList<>();

        // 模拟按状态统计数据
        Map<String, Object> draft = new HashMap<>();
        draft.put("status", "DRAFT");
        draft.put("statusName", "草稿");
        draft.put("count", 23);
        draft.put("amount", 234567.89);
        statistics.add(draft);

        Map<String, Object> approved = new HashMap<>();
        approved.put("status", "APPROVED");
        approved.put("statusName", "已审核");
        approved.put("count", 67);
        approved.put("amount", 567890.12);
        statistics.add(approved);

        Map<String, Object> posted = new HashMap<>();
        posted.put("status", "POSTED");
        posted.put("statusName", "已过账");
        posted.put("count", 66);
        approved.put("amount", 456789.34);
        statistics.add(posted);

        return statistics;
    }

    @Override
    @Cacheable(value = "voucherStatistics", key = "'byType:' + #bookId + ':' + #tenantId + ':' + #startDate + ':' + #endDate")
    public List<Map<String, Object>> getStatisticsByType(Long bookId, Long tenantId, String startDate, String endDate) {
        List<Map<String, Object>> statistics = new ArrayList<>();

        // 模拟按类型统计数据
        Map<String, Object> receipt = new HashMap<>();
        receipt.put("type", "RECEIPT");
        receipt.put("typeName", "收款凭证");
        receipt.put("count", 45);
        receipt.put("amount", 678901.23);
        statistics.add(receipt);

        Map<String, Object> payment = new HashMap<>();
        payment.put("type", "PAYMENT");
        payment.put("typeName", "付款凭证");
        payment.put("count", 67);
        payment.put("amount", 456789.56);
        statistics.add(payment);

        Map<String, Object> transfer = new HashMap<>();
        transfer.put("type", "TRANSFER");
        transfer.put("typeName", "转账凭证");
        transfer.put("count", 44);
        transfer.put("amount", 123456.78);
        statistics.add(transfer);

        return statistics;
    }

    // 私有辅助方法
    private List<Map<String, Object>> getStatusStatistics(VoucherStatisticsQueryParam param) {
        List<Map<String, Object>> statusStats = new ArrayList<>();
        // 模拟状态统计
        statusStats.add(createStatItem("草稿", 15, 0.1));
        statusStats.add(createStatItem("已审核", 89, 0.6));
        statusStats.add(createStatItem("已过账", 52, 0.3));
        return statusStats;
    }

    private List<Map<String, Object>> getTypeStatistics(VoucherStatisticsQueryParam param) {
        List<Map<String, Object>> typeStats = new ArrayList<>();
        // 模拟类型统计
        typeStats.add(createStatItem("收款凭证", 67, 0.4));
        typeStats.add(createStatItem("付款凭证", 78, 0.5));
        typeStats.add(createStatItem("转账凭证", 11, 0.1));
        return typeStats;
    }

    private List<Map<String, Object>> getDateStatistics(VoucherStatisticsQueryParam param) {
        List<Map<String, Object>> dateStats = new ArrayList<>();
        // 模拟日期统计
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            dateStats.add(createStatItem(date.toString(), new Random().nextInt(20) + 5, 0.0));
        }
        return dateStats;
    }

    private List<Map<String, Object>> getUserStatistics(VoucherStatisticsQueryParam param) {
        List<Map<String, Object>> userStats = new ArrayList<>();
        // 模拟用户统计
        userStats.add(createStatItem("张三", 45, 0.3));
        userStats.add(createStatItem("李四", 38, 0.25));
        userStats.add(createStatItem("王五", 32, 0.21));
        userStats.add(createStatItem("赵六", 41, 0.24));
        return userStats;
    }

    private Map<String, Object> createStatItem(String name, int count, double percentage) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("count", count);
        item.put("percentage", percentage);
        return item;
    }

    private int getTodayVoucherCount(Long bookId, Long tenantId) {
        // 模拟今日凭证数量
        return new Random().nextInt(20) + 5;
    }

    private double getTodayVoucherAmount(Long bookId, Long tenantId) {
        // 模拟今日凭证金额
        return new Random().nextDouble() * 100000;
    }

    private int getWeekVoucherCount(Long bookId, Long tenantId) {
        // 模拟本周凭证数量
        return new Random().nextInt(100) + 50;
    }

    private double getWeekVoucherAmount(Long bookId, Long tenantId) {
        // 模拟本周凭证金额
        return new Random().nextDouble() * 500000;
    }

    private int getMonthVoucherCount(Long bookId, Long tenantId) {
        // 模拟本月凭证数量
        return new Random().nextInt(300) + 200;
    }

    private double getMonthVoucherAmount(Long bookId, Long tenantId) {
        // 模拟本月凭证金额
        return new Random().nextDouble() * 2000000;
    }

    private int getPendingVoucherCount(Long bookId, Long tenantId) {
        // 模拟待处理凭证数量
        return new Random().nextInt(30) + 10;
    }

    private List<Map<String, Object>> getRecentTrend(Long bookId, Long tenantId, int days) {
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Map<String, Object> point = new HashMap<>();
            point.put("date", date.format(DateTimeFormatter.ISO_DATE));
            point.put("count", new Random().nextInt(15) + 5);
            point.put("amount", new Random().nextDouble() * 50000);
            trend.add(point);
        }
        return trend;
    }

    // ========== 新增方法实现 ==========

    @Override
    public Map<String, Object> getVoucherSummary(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证汇总统计，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = new HashMap<>();

            // 设置查询参数
            param.setBookId(param.getBookId() != null ? param.getBookId() : loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setTenantId(param.getTenantId() != null ? param.getTenantId() : loginStaff.getCurrentOrg().getOrgid().longValue());

            // 基础统计
            result.put("totalCount", voucherStatisticsMapper.selectTotalCount(param));
            result.put("totalAmount", voucherStatisticsMapper.selectTotalAmount(param));

            // 按状态统计
            List<Map<String, Object>> statusStatistics = voucherStatisticsMapper.selectCountByStatus(param);
            result.put("statusStatistics", statusStatistics);

            // 按类型统计
            List<Map<String, Object>> typeStatistics = voucherStatisticsMapper.selectCountByType(param);
            result.put("typeStatistics", typeStatistics);

            // 按期间统计
            List<Map<String, Object>> periodStatistics = voucherStatisticsMapper.selectCountByPeriod(param);
            result.put("periodStatistics", periodStatistics);

            // 按币种统计
            List<Map<String, Object>> currencyStatistics = voucherStatisticsMapper.selectCountByCurrency(param);
            result.put("currencyStatistics", currencyStatistics);

            return result;
        } catch (Exception e) {
            log.error("凭证汇总统计失败", e);
            throw new RuntimeException("凭证汇总统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherCountByStatus(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证数量统计(按状态)，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            // 不自动设置 bookId 和 tenantId，允许查询全部数据
            // 如果前端传入了这些参数，则按参数过滤；否则查询全部

            return voucherStatisticsMapper.selectCountByStatus(param);
        } catch (Exception e) {
            log.error("凭证数量统计(按状态)失败", e);
            throw new RuntimeException("凭证数量统计(按状态)失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherCountByPeriod(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证数量统计(按期间)，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            // 不自动设置 bookId 和 tenantId，允许查询全部数据
            // 如果前端传入了这些参数，则按参数过滤；否则查询全部

            return voucherStatisticsMapper.selectCountByPeriod(param);
        } catch (Exception e) {
            log.error("凭证数量统计(按期间)失败", e);
            throw new RuntimeException("凭证数量统计(按期间)失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherCountByType(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证数量统计(按凭证类型)，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            // 不自动设置 bookId 和 tenantId，允许查询全部数据
            // 如果前端传入了这些参数，则按参数过滤；否则查询全部

            return voucherStatisticsMapper.selectCountByType(param);
        } catch (Exception e) {
            log.error("凭证数量统计(按凭证类型)失败", e);
            throw new RuntimeException("凭证数量统计(按凭证类型)失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherCountByPreparer(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证数量统计(按制单人)，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            // 设置查询参数
            param.setBookId(param.getBookId() != null ? param.getBookId() : loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setTenantId(param.getTenantId() != null ? param.getTenantId() : loginStaff.getCurrentOrg().getOrgid().longValue());

            return voucherStatisticsMapper.selectCountByPreparer(param);
        } catch (Exception e) {
            log.error("凭证数量统计(按制单人)失败", e);
            throw new RuntimeException("凭证数量统计(按制单人)失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherCountByCurrency(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行分币种统计，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            // 设置查询参数
            param.setBookId(param.getBookId() != null ? param.getBookId() : loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setTenantId(param.getTenantId() != null ? param.getTenantId() : loginStaff.getCurrentOrg().getOrgid().longValue());

            return voucherStatisticsMapper.selectCountByCurrency(param);
        } catch (Exception e) {
            log.error("分币种统计失败", e);
            throw new RuntimeException("分币种统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVoucherEntryStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证分录统计，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = new HashMap<>();

            // 设置查询参数
            param.setBookId(param.getBookId() != null ? param.getBookId() : loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setTenantId(param.getTenantId() != null ? param.getTenantId() : loginStaff.getCurrentOrg().getOrgid().longValue());

            // 分录基础统计
            result.put("totalEntryCount", voucherStatisticsMapper.selectTotalEntryCount(param));
            result.put("totalDebitAmount", voucherStatisticsMapper.selectTotalDebitAmount(param));
            result.put("totalCreditAmount", voucherStatisticsMapper.selectTotalCreditAmount(param));

            // 分录平均条数
            Long voucherCount = voucherStatisticsMapper.selectTotalCount(param);
            Long entryCount = voucherStatisticsMapper.selectTotalEntryCount(param);
            if (voucherCount != null && voucherCount > 0) {
                result.put("avgEntryPerVoucher", entryCount.doubleValue() / voucherCount.doubleValue());
            }
            return result;
        } catch (Exception e) {
            log.error("凭证分录统计失败", e);
            throw new RuntimeException("凭证分录统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherStatisticsTrend(VoucherTrendParam param, TblStaffUtil loginStaff) {
        try {
            log.info("执行凭证统计趋势，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            // 设置查询参数
            param.setBookId(param.getBookId() != null ? param.getBookId() : loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setTenantId(param.getTenantId() != null ? param.getTenantId() : loginStaff.getCurrentOrg().getOrgid().longValue());

            return voucherStatisticsMapper.selectStatisticsTrend(param);
        } catch (Exception e) {
            log.error("凭证统计趋势失败", e);
            throw new RuntimeException("凭证统计趋势失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getFilterOptions(Long bookId, TblStaffUtil loginStaff) {
        try {
            log.info("获取统计筛选条件，用户：{}，账簿ID：{}", loginStaff.getStaffid(), bookId);

            Map<String, Object> result = new HashMap<>();

            Long targetBookId = bookId != null ? bookId : loginStaff.getCurrentOrg().getOrgid().longValue();
            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();

            // 获取凭证期间列表
            result.put("accountingPeriods", voucherStatisticsMapper.selectAccountingPeriods(targetBookId, tenantId));

            // 获取凭证类型列表
            result.put("voucherTypes", voucherStatisticsMapper.selectVoucherTypes(targetBookId, tenantId));

            // 获取制单人列表
            result.put("preparers", voucherStatisticsMapper.selectPreparers(targetBookId, tenantId));

            // 获取币种列表
            result.put("currencies", voucherStatisticsMapper.selectCurrencies(targetBookId, tenantId));

            return result;
        } catch (Exception e) {
            log.error("获取统计筛选条件失败", e);
            throw new RuntimeException("获取统计筛选条件失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> exportVoucherStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("导出凭证统计报表，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = new HashMap<>();

            // 生成导出任务ID
            String exportId = "VOUCHER_STATS_EXPORT_" + System.currentTimeMillis();
            result.put("exportId", exportId);
            result.put("fileName", "voucher_statistics_" + System.currentTimeMillis() + ".xlsx");
            result.put("exportTime", new Date());
            result.put("status", "PENDING");
            result.put("message", "导出任务已创建，正在处理中");

            // TODO: 异步执行导出任务
            // 这里可以添加异步任务处理逻辑

            return result;
        } catch (Exception e) {
            log.error("导出凭证统计报表失败", e);
            throw new RuntimeException("导出凭证统计报表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVoucherStatisticsDashboard(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            log.info("获取凭证统计仪表盘数据，用户：{}，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> dashboard = new HashMap<>();

            // 设置查询参数
            param.setBookId(param.getBookId() != null ? param.getBookId() : loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setTenantId(param.getTenantId() != null ? param.getTenantId() : loginStaff.getCurrentOrg().getOrgid().longValue());

            // 今日统计
            LocalDate today = LocalDate.now();
            VoucherStatisticsQueryParam todayParam = new VoucherStatisticsQueryParam();
            todayParam.setBookId(param.getBookId());
            todayParam.setTenantId(param.getTenantId());
            todayParam.setStartDate(today);
            todayParam.setEndDate(today);

            dashboard.put("todayCount", voucherStatisticsMapper.selectTotalCount(todayParam));
            dashboard.put("todayAmount", voucherStatisticsMapper.selectTotalAmount(todayParam));

            // 本周统计
            LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
            VoucherStatisticsQueryParam weekParam = new VoucherStatisticsQueryParam();
            weekParam.setBookId(param.getBookId());
            weekParam.setTenantId(param.getTenantId());
            weekParam.setStartDate(weekStart);
            weekParam.setEndDate(today);

            dashboard.put("weekCount", voucherStatisticsMapper.selectTotalCount(weekParam));
            dashboard.put("weekAmount", voucherStatisticsMapper.selectTotalAmount(weekParam));

            // 本月统计
            LocalDate monthStart = today.withDayOfMonth(1);
            VoucherStatisticsQueryParam monthParam = new VoucherStatisticsQueryParam();
            monthParam.setBookId(param.getBookId());
            monthParam.setTenantId(param.getTenantId());
            monthParam.setStartDate(monthStart);
            monthParam.setEndDate(today);

            dashboard.put("monthCount", voucherStatisticsMapper.selectTotalCount(monthParam));
            dashboard.put("monthAmount", voucherStatisticsMapper.selectTotalAmount(monthParam));

            // 待处理统计（草稿状态）
            VoucherStatisticsQueryParam draftParam = new VoucherStatisticsQueryParam();
            draftParam.setBookId(param.getBookId());
            draftParam.setTenantId(param.getTenantId());
            draftParam.setVoucherStatus(0); // 草稿状态

            dashboard.put("pendingCount", voucherStatisticsMapper.selectTotalCount(draftParam));

            // 趋势数据（最近7天）
            VoucherTrendParam trendParam = new VoucherTrendParam();
            trendParam.setBookId(param.getBookId());
            trendParam.setTenantId(param.getTenantId());
            trendParam.setStartDate(today.minusDays(6));
            trendParam.setEndDate(today);
            trendParam.setTrendDimension("DAY");

            dashboard.put("trendData", voucherStatisticsMapper.selectStatisticsTrend(trendParam));

            return dashboard;
        } catch (Exception e) {
            log.error("获取凭证统计仪表盘数据失败", e);
            throw new RuntimeException("获取凭证统计仪表盘数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVoucherAmountStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            Map<String, Object> result = voucherStatisticsMapper.getVoucherAmountStatistics(param);
            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            log.error("获取凭证金额统计失败", e);
            throw new RuntimeException("获取凭证金额统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherGenerationTrend(VoucherTrendParam param, TblStaffUtil loginStaff) {
        try {
            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            List<Map<String, Object>> result = voucherStatisticsMapper.getVoucherGenerationTrend(param);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            log.error("获取凭证生成趋势失败", e);
            throw new RuntimeException("获取凭证生成趋势失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVoucherEfficiencyAnalysis(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            Map<String, Object> result = voucherStatisticsMapper.getVoucherEfficiencyAnalysis(param);
            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            log.error("获取凭证效率分析失败", e);
            throw new RuntimeException("获取凭证效率分析失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getAbnormalVoucherAnalysis(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            List<Map<String, Object>> result = voucherStatisticsMapper.getAbnormalVoucherAnalysis(param);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            log.error("获取异常凭证分析失败", e);
            throw new RuntimeException("获取异常凭证分析失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getSubjectUsageStatistics(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            List<Map<String, Object>> result = voucherStatisticsMapper.getSubjectUsageStatistics(param);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            log.error("获取科目使用统计失败", e);
            throw new RuntimeException("获取科目使用统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVoucherReviewQualityAnalysis(VoucherStatisticsQueryParam param, TblStaffUtil loginStaff) {
        try {
            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            List<Map<String, Object>> result = voucherStatisticsMapper.getVoucherReviewQualityAnalysis(param);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            log.error("获取凭证审核质量分析失败", e);
            throw new RuntimeException("获取凭证审核质量分析失败: " + e.getMessage(), e);
        }
    }
}