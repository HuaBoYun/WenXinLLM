package com.global.treasurer.controller;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.global.treasurer.mapper.TreasurerSmartHomeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 全球司库首页Controller
 * 提供首页KPI、欢迎区统计、现金流、融资进度等聚合数据
 *
 * @author 华博云开发团队
 * @since 2025-07-01
 */
@RestController
@RequestMapping("/financial/treasurerSmartHome")
@Api(tags = "全球司库首页")
public class TreasurerSmartHomeController {

    private static final Logger log = LoggerFactory.getLogger(TreasurerSmartHomeController.class);

    @Resource
    private UserProvider userProvider;

    @Autowired
    private TreasurerSmartHomeMapper smartHomeMapper;

    /**
     * 获取首页聚合数据
     */
    @GetMapping("/overview")
    @ApiOperation("全球司库首页聚合数据")
    public String overview(HttpServletRequest request) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户已失效");
            }
            String orgid = staff.getLinkOrg().getOrgid().toString();

            Map<String, Object> resultMap = new HashMap<>();

            // KPI数据
            Map<String, Object> kpi = new HashMap<>();
            Double fundConcentrationRate = smartHomeMapper.getFundConcentrationRate(orgid, null);
            Double financingBalance = smartHomeMapper.getFinancingBalance(orgid, null);
            Double investReturnRate = smartHomeMapper.getInvestReturnRate(orgid, null);
            Double billMaturityAmount = smartHomeMapper.getBillMaturityAmount(orgid, null);
            Double riskScore = smartHomeMapper.getRiskScore(orgid, null);
            Double settlementRate = smartHomeMapper.getSettlementRate(orgid, null);

            kpi.put("fundConcentrationRate", fundConcentrationRate);
            kpi.put("financingBalance", financingBalance);
            kpi.put("investReturnRate", investReturnRate);
            kpi.put("billMaturityAmount", billMaturityAmount);
            kpi.put("riskScore", riskScore);
            kpi.put("settlementRate", settlementRate);
            resultMap.put("kpi", kpi);

            // 欢迎区统计
            Map<String, Object> welcome = new HashMap<>();
            Double totalFundConcentration = smartHomeMapper.getTotalFundConcentration(orgid, null);
            Double totalInvestScale = smartHomeMapper.getTotalInvestScale(orgid, null);
            Double totalBillScale = smartHomeMapper.getTotalBillScale(orgid, null);

            welcome.put("fundConcentration", totalFundConcentration);
            welcome.put("financingBalance", financingBalance);
            welcome.put("investScale", totalInvestScale);
            welcome.put("billScale", totalBillScale);
            welcome.put("riskScore", riskScore);
            resultMap.put("welcome", welcome);

            resultMap.put("codes", "1");
            return new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("全球司库首页数据获取失败", e);
            return JsonBean.error("数据获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取现金流量统计
     */
    @GetMapping("/cashFlow")
    @ApiOperation("现金流量统计")
    public String cashFlow(HttpServletRequest request) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) return JsonBean.error("用户已失效");
            String orgid = staff.getLinkOrg().getOrgid().toString();

            Map<String, Object> resultMap = new HashMap<>();
            Double cashInflow = smartHomeMapper.getCashInflow(orgid, null, null);
            Double cashOutflow = smartHomeMapper.getCashOutflow(orgid, null, null);
            double inVal = cashInflow != null ? cashInflow : 0;
            double outVal = cashOutflow != null ? cashOutflow : 0;

            resultMap.put("cashInflow", cashInflow);
            resultMap.put("cashOutflow", cashOutflow);
            resultMap.put("netCashflow", inVal - outVal);
            resultMap.put("codes", "1");
            return new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("现金流量统计获取失败", e);
            return JsonBean.error("数据获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取融资进度统计
     */
    @GetMapping("/financeProgress")
    @ApiOperation("融资进度统计")
    public String financeProgress(HttpServletRequest request) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) return JsonBean.error("用户已失效");
            String orgid = staff.getLinkOrg().getOrgid().toString();

            Map<String, Object> resultMap = new HashMap<>();
            Double planRate = smartHomeMapper.getFinancingPlanRate(orgid, null);
            Double repayRate = smartHomeMapper.getFinancingRepayRate(orgid, null);
            Double costRate = smartHomeMapper.getFinancingCostRate(orgid, null);

            resultMap.put("planRate", planRate);
            resultMap.put("repayRate", repayRate);
            resultMap.put("costRate", costRate);
            resultMap.put("codes", "1");
            return new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("融资进度统计获取失败", e);
            return JsonBean.error("数据获取失败: " + e.getMessage());
        }
    }
}
