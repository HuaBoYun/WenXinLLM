package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.derivatives.TblDerivativesRiskLimit;
import com.global.treasurer.entity.derivatives.TblDerivativesStressTest;
import com.global.treasurer.mapper.derivatives.TblDerivativesRiskLimitMapper;
import com.global.treasurer.mapper.derivatives.TblDerivativesStressTestMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController("derivativesRiskManagementController")
@RequestMapping("/derivatives/risk")
@Api(tags = "衍生品风险管理")
public class RiskManagementController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private TblDerivativesRiskLimitMapper riskLimitMapper;

    @Resource
    private TblDerivativesStressTestMapper stressTestMapper;

    @GetMapping("/limits")
    @ApiOperation("获取风险限额数据")
    public String getRiskLimits() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            QueryWrapper<TblDerivativesRiskLimit> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0");
            List<TblDerivativesRiskLimit> list = riskLimitMapper.selectList(qw);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", list);
            result.put("totalRecord", list.size());
            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/limits")
    @ApiOperation("更新风险限额")
    public String updateRiskLimit(
            @RequestParam(required = false) Long limitId,
            @RequestParam(required = false) String limitType,
            @RequestParam(required = false) Double limitAmount,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String status) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblDerivativesRiskLimit entity;
            if (limitId != null) {
                entity = riskLimitMapper.selectById(limitId);
                if (entity == null) return new JsonBean(0, "记录不存在", null).toJson();
            } else {
                QueryWrapper<TblDerivativesRiskLimit> qw = new QueryWrapper<>();
                qw.eq("LIMIT_TYPE", limitType).eq("DEL_FLAG", "0");
                entity = riskLimitMapper.selectOne(qw);
                if (entity == null) return new JsonBean(0, "记录不存在", null).toJson();
            }
            if (limitAmount != null) {
                entity.setLimitAmount(BigDecimal.valueOf(limitAmount));
                BigDecimal used = entity.getUsedAmount() != null ? entity.getUsedAmount() : BigDecimal.ZERO;
                entity.setAvailableAmount(entity.getLimitAmount().subtract(used));
                if (entity.getLimitAmount().compareTo(BigDecimal.ZERO) > 0) {
                    entity.setUsageRatio(used.divide(entity.getLimitAmount(), 4, BigDecimal.ROUND_HALF_UP));
                }
            }
            if (currency != null) entity.setCurrency(currency);
            if (status != null) entity.setStatus(status);
            entity.setUpdateTime(new Date());
            riskLimitMapper.updateById(entity);
            return new JsonBean(1, "更新成功", entity).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @RequestMapping(value = {"/var", "/vaR"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取VaR数据")
    public String getVaRData() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            QueryWrapper<TblDerivativesRiskLimit> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0");
            List<TblDerivativesRiskLimit> limits = riskLimitMapper.selectList(qw);
            BigDecimal totalUsed = limits.stream()
                .filter(l -> "总风险限额".equals(l.getLimitType()))
                .map(l -> l.getUsedAmount() != null ? l.getUsedAmount() : BigDecimal.ZERO)
                .findFirst().orElse(BigDecimal.ZERO);
            BigDecimal var95 = totalUsed.multiply(BigDecimal.valueOf(0.026));
            BigDecimal var99 = totalUsed.multiply(BigDecimal.valueOf(0.038));
            Map<String, Object> varData = new HashMap<>();
            varData.put("var95", var95);
            varData.put("var99", var99);
            varData.put("varChange", var95.multiply(BigDecimal.valueOf(-0.03)));
            varData.put("varChangeRatio", -0.03);
            varData.put("es95", var95.multiply(BigDecimal.valueOf(1.29)));
            varData.put("es99", var99.multiply(BigDecimal.valueOf(1.20)));
            varData.put("esChange", var95.multiply(BigDecimal.valueOf(-0.027)));
            varData.put("varTrend", Collections.emptyList());
            varData.put("varByCurrency", Collections.emptyList());
            return new JsonBean(1, "查询成功", varData).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @RequestMapping(value = "/stress-test", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取压力测试数据")
    public String getStressTestData() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            QueryWrapper<TblDerivativesStressTest> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0");
            List<TblDerivativesStressTest> list = stressTestMapper.selectList(qw);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", list);
            result.put("totalRecord", list.size());
            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/report")
    @ApiOperation("导出风险报告")
    public void exportRiskReport(HttpServletResponse response) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            response.setStatus(401);
            return;
        }
        QueryWrapper<TblDerivativesRiskLimit> lqw = new QueryWrapper<>();
        lqw.eq("DEL_FLAG", "0");
        List<TblDerivativesRiskLimit> limits = riskLimitMapper.selectList(lqw);

        QueryWrapper<TblDerivativesStressTest> sqw = new QueryWrapper<>();
        sqw.eq("DEL_FLAG", "0");
        List<TblDerivativesStressTest> stressTests = stressTestMapper.selectList(sqw);

        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String filename = "risk_report_" + date + ".csv";
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println("\uFEFF衍生品风险报告 - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        writer.println();
        writer.println("一、风险限额管理");
        writer.println("限额类型,限额值,已用金额,可用金额,使用率,币种,状态");
        for (TblDerivativesRiskLimit l : limits) {
            writer.println(String.format("%s,%s,%s,%s,%.2f%%,%s,%s",
                l.getLimitType(),
                l.getLimitAmount(),
                l.getUsedAmount(),
                l.getAvailableAmount(),
                l.getUsageRatio() != null ? l.getUsageRatio().multiply(BigDecimal.valueOf(100)).doubleValue() : 0,
                l.getCurrency(),
                l.getStatus()
            ));
        }
        writer.println();
        writer.println("二、压力测试结果");
        writer.println("场景名称,场景描述,组合价值,压力价值,损失金额,损失比例,严重程度,状态");
        for (TblDerivativesStressTest s : stressTests) {
            writer.println(String.format("%s,%s,%s,%s,%s,%.2f%%,%s,%s",
                s.getScenarioName(),
                s.getScenarioDesc(),
                s.getPortfolioValue(),
                s.getStressValue(),
                s.getLoss(),
                s.getLossRatio() != null ? s.getLossRatio().multiply(BigDecimal.valueOf(100)).doubleValue() : 0,
                s.getSeverity(),
                s.getStatus()
            ));
        }
        writer.flush();
    }
}
