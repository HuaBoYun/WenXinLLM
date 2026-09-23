package com.global.treasurer.controller;

import com.global.treasurer.service.BondIssuanceService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 债券概览数据Controller
 * 提供债券发行的统计概览和图表数据
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@RestController
@RequestMapping({"/financial/rzgl/bond"})
@Api(tags = "债券概览数据")
public class BondOverviewController {
    private static final Logger log = LoggerFactory.getLogger(BondOverviewController.class);

    @Autowired
    private BondIssuanceService bondIssuanceService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取债券融资概览数据
     */
    @GetMapping("/overview")
    @ApiOperation(value = "获取债券融资概览数据", notes = "返回债券发行总数、存续债券余额、平均票面利率、平均信用评级")
    public String getOverview() {
        try {
            // 获取用户信息,但不强制要求登录
            // 强制查询所有公司的数据
            Long companyId = null;
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                    companyId = loginStaff.getCurrentOrg().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败,将查询所有公司的数据: {}", e.getMessage());
            }

            // 强制查询所有公司数据
            companyId = null;
            log.info("已强制设置companyId=null，将查询所有公司的债券概览数据");

            Map<String, Object> summary = bondIssuanceService.getIssuanceSummary(companyId);
            
            // 构建概览数据
            Map<String, Object> overview = new HashMap<>();
            if (summary != null) {
                overview.put("totalBonds", summary.getOrDefault("totalBonds", 0));
                overview.put("outstandingAmount", summary.getOrDefault("outstandingAmount", BigDecimal.ZERO));
                overview.put("averageCouponRate", summary.getOrDefault("averageCouponRate", BigDecimal.ZERO));
                overview.put("averageRating", summary.getOrDefault("averageRating", "N/A"));
            } else {
                overview.put("totalBonds", 0);
                overview.put("outstandingAmount", BigDecimal.ZERO);
                overview.put("averageCouponRate", BigDecimal.ZERO);
                overview.put("averageRating", "N/A");
            }
            
            return new JsonBean(1, "查询成功", overview).toJson();
        } catch (Exception e) {
            log.error("获取债券概览数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取债券类型分布图表数据
     */
    @GetMapping("/chart/type-distribution")
    @ApiOperation(value = "获取债券类型分布", notes = "返回各类型债券的数量分布，用于饼图/柱状图展示")
    public String getTypeDistribution() {
        try {
            // 获取用户信息,但不强制要求登录
            // 强制查询所有公司的数据
            Long companyId = null;
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                    companyId = loginStaff.getCurrentOrg().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败,将查询所有公司的数据: {}", e.getMessage());
            }

            // 强制查询所有公司数据
            companyId = null;
            log.info("已强制设置companyId=null，将查询所有公司的债券类型分布数据");

            // 从数据库查询实际数据
            List<Map<String, Object>> distribution = bondIssuanceService.getBondTypeDistribution(companyId);

            // 如果没有数据，返回默认的空数据结构
            if (distribution == null || distribution.isEmpty()) {
                distribution = new ArrayList<>();
                distribution.add(createDistributionItem("企业债券", 0));
                distribution.add(createDistributionItem("可转换债券", 0));
                distribution.add(createDistributionItem("政府债券", 0));
                distribution.add(createDistributionItem("永续债券", 0));
            }

            return new JsonBean(1, "查询成功", distribution).toJson();
        } catch (Exception e) {
            log.error("获取债券类型分布失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取债券发行趋势图表数据
     */
    @GetMapping("/chart/trend")
    @ApiOperation(value = "获取债券发行趋势", notes = "返回指定时间段内的债券发行趋势数据")
    public String getTrend(@ApiParam("时间周期: 6M/1Y/2Y") @RequestParam(defaultValue = "6M") String period) {
        try {
            // 获取用户信息,但不强制要求登录
            // 强制查询所有公司的数据
            Long companyId = null;
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                    companyId = loginStaff.getCurrentOrg().getOrgid().longValue();
                }
            } catch (Exception e) {
                log.warn("获取用户信息失败,将查询所有公司的数据: {}", e.getMessage());
            }

            // 强制查询所有公司数据
            companyId = null;
            log.info("已强制设置companyId=null，将查询所有公司的债券趋势数据");

            // 根据period计算月份数
            int months = 6;
            if ("1Y".equals(period)) {
                months = 12;
            } else if ("2Y".equals(period)) {
                months = 24;
            }

            // 从数据库查询实际数据
            List<Map<String, Object>> dbTrend = bondIssuanceService.getIssuanceTrend(companyId, months);

            // 构建返回数据结构
            List<String> monthLabels = new ArrayList<>();
            List<Object> issuanceCount = new ArrayList<>();
            List<Object> issuanceAmount = new ArrayList<>();

            if (dbTrend != null && !dbTrend.isEmpty()) {
                for (Map<String, Object> item : dbTrend) {
                    monthLabels.add((String) item.get("month"));
                    issuanceCount.add(item.get("issuanceCount"));
                    issuanceAmount.add(item.get("issuanceAmount"));
                }
            } else {
                // 如果没有数据，生成空的月份标签
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -months + 1);
                for (int i = 0; i < months; i++) {
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH) + 1;
                    monthLabels.add(year + "-" + String.format("%02d", month));
                    issuanceCount.add(0);
                    issuanceAmount.add(BigDecimal.ZERO);
                    cal.add(Calendar.MONTH, 1);
                }
            }

            Map<String, Object> trend = new HashMap<>();
            trend.put("months", monthLabels);
            trend.put("issuanceCount", issuanceCount);
            trend.put("issuanceAmount", issuanceAmount);

            return new JsonBean(1, "查询成功", trend).toJson();
        } catch (Exception e) {
            log.error("获取债券发行趋势失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    private Map<String, Object> createDistributionItem(String name, int value) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }
}

