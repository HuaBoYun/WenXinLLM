package com.global.treasurer.controller;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 成本分析Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Controller
@RequestMapping({"/rzgl/cost", "/financial/rzgl/cost", 
"/centralaudit/financial/rzgl/cost"})
@Api(tags = "成本分析管理")
public class CostAnalysisController {
    private static final Logger log = LoggerFactory.getLogger(CostAnalysisController.class);

    @Resource
    private UserProvider userProvider;

    /**
     * 获取成本分析图表数据（GET请求）
     */
    @GetMapping("/chart")
    @ResponseBody
    @ApiOperation("获取成本分析图表数据(GET)")
    public String getCostChartByGet(
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        return getCostChart(token, response);
    }

    /**
     * 获取成本分析图表数据（POST请求）
     */
    @PostMapping("/chart")
    @ResponseBody
    @ApiOperation("获取成本分析图表数据(POST)")
    public String getCostChart(
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 构建成本分析数据
            Map<String, Object> costData = new HashMap<>();

            // 融资成本趋势数据
            List<Map<String, Object>> trendData = new ArrayList<>();
            Map<String, Object> month1 = new HashMap<>();
            month1.put("month", "2025-07");
            month1.put("amount", 12500.00);
            month1.put("cost", 625.00);
            month1.put("rate", 5.0);
            trendData.add(month1);

            Map<String, Object> month2 = new HashMap<>();
            month2.put("month", "2025-08");
            month2.put("amount", 13200.00);
            month2.put("cost", 692.40);
            month2.put("rate", 5.25);
            trendData.add(month2);

            Map<String, Object> month3 = new HashMap<>();
            month3.put("month", "2025-09");
            month3.put("amount", 12800.00);
            month3.put("cost", 652.80);
            month3.put("rate", 5.1);
            trendData.add(month3);

            Map<String, Object> month4 = new HashMap<>();
            month4.put("month", "2025-10");
            month4.put("amount", 14500.00);
            month4.put("cost", 783.00);
            month4.put("rate", 5.4);
            trendData.add(month4);

            Map<String, Object> month5 = new HashMap<>();
            month5.put("month", "2025-11");
            month5.put("amount", 15200.00);
            month5.put("cost", 820.80);
            month5.put("rate", 5.4);
            trendData.add(month5);

            Map<String, Object> month6 = new HashMap<>();
            month6.put("month", "2025-12");
            month6.put("amount", 16800.00);
            month6.put("cost", 940.80);
            month6.put("rate", 5.6);
            trendData.add(month6);

            costData.put("trendData", trendData);

            // 融资结构数据
            List<Map<String, Object>> structureData = new ArrayList<>();
            Map<String, Object> item1 = new HashMap<>();
            item1.put("type", "银行贷款");
            item1.put("amount", 85000.00);
            item1.put("percentage", 50.6);
            item1.put("cost", 4590.00);
            structureData.add(item1);

            Map<String, Object> item2 = new HashMap<>();
            item2.put("type", "债券融资");
            item2.put("amount", 42000.00);
            item2.put("percentage", 25.0);
            item2.put("cost", 2100.00);
            structureData.add(item2);

            Map<String, Object> item3 = new HashMap<>();
            item3.put("type", "融资租赁");
            item3.put("amount", 28000.00);
            item3.put("percentage", 16.7);
            item3.put("cost", 1848.00);
            structureData.add(item3);

            Map<String, Object> item4 = new HashMap<>();
            item4.put("type", "信托融资");
            item4.put("amount", 13000.00);
            item4.put("percentage", 7.7);
            item4.put("cost", 975.00);
            structureData.add(item4);

            costData.put("structureData", structureData);

            // 汇总数据
            costData.put("totalFinancingAmount", 168000.00);
            costData.put("totalCost", 9513.00);
            costData.put("averageCostRate", 5.66);

            return JsonBean.success(costData);
        } catch (Exception e) {
            log.error("获取成本分析图表数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
