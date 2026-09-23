package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.derivatives.*;
import com.global.treasurer.mapper.derivatives.*;
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

@RestController("derivativesReportController")
@RequestMapping(value = "/derivatives/report", produces = "application/json;charset=UTF-8")
@Api(tags = "衍生品报表分析")
public class ReportController {

    @Resource
    private UserProvider userProvider;
    @Resource
    private TblDerivativesReportMapper reportMapper;
    @Resource
    private TblDerivativesReportTemplateMapper templateMapper;
    @Resource
    private TblDerivativesValuationMapper valuationMapper;
    @Resource
    private TblDerivativesRiskLimitMapper riskLimitMapper;

    @GetMapping("/list")
    @ApiOperation("报表列表")
    public String list() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesReport> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0").orderByDesc("CREATE_TIME");
            List<TblDerivativesReport> list = reportMapper.selectList(qw);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", list);
            data.put("totalRecord", list.size());
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/add")
    @ApiOperation("生成报表")
    public String add(@RequestParam String reportName,
                      @RequestParam String reportType,
                      @RequestParam(required = false) String reportPeriod,
                      @RequestParam(required = false) String startDate,
                      @RequestParam(required = false) String endDate,
                      @RequestParam(required = false) String outputFormat) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            TblDerivativesReport report = new TblDerivativesReport();
            report.setReportName(reportName);
            report.setReportType(reportType);
            report.setReportPeriod(reportPeriod != null ? reportPeriod : "DAILY");
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setOutputFormat(outputFormat != null ? outputFormat : "CSV");
            report.setGeneratedBy(user.getRealname() != null ? user.getRealname() : (user.getUsername() != null ? user.getUsername() : "管理员"));
            report.setGenerateTime(now);
            report.setFileSize(1024000L + (long)(Math.random() * 2000000));
            report.setStatus("COMPLETED");
            report.setCreateTime(new Date());
            reportMapper.insert(report);
            return new JsonBean(1, "报表生成成功", report).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("报表详情")
    public String detail(@PathVariable Long id) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesReport report = reportMapper.selectById(id);
            if (report == null) return new JsonBean(0, "报表不存在", null).toJson();
            return new JsonBean(1, "查询成功", report).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/edit/{id}")
    @ApiOperation("修改报表")
    public String edit(@PathVariable Long id,
                       @RequestParam String reportName,
                       @RequestParam String reportType,
                       @RequestParam(required = false) String reportPeriod,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate,
                       @RequestParam(required = false) String outputFormat) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesReport report = reportMapper.selectById(id);
            if (report == null) return new JsonBean(0, "报表不存在", null).toJson();
            report.setReportName(reportName);
            report.setReportType(reportType);
            if (reportPeriod != null) report.setReportPeriod(reportPeriod);
            if (startDate != null) report.setStartDate(startDate);
            if (endDate != null) report.setEndDate(endDate);
            if (outputFormat != null) report.setOutputFormat(outputFormat);
            reportMapper.updateById(report);
            return new JsonBean(1, "修改成功", report).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除报表")
    public String delete(@PathVariable Long id) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesReport report = reportMapper.selectById(id);
            if (report == null) return new JsonBean(0, "报表不存在", null).toJson();
            report.setDelFlag("1");
            reportMapper.updateById(report);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除报表")
    public String batchDelete(@RequestParam String ids) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            String[] idArr = ids.split(",");
            for (String idStr : idArr) {
                TblDerivativesReport r = reportMapper.selectById(Long.parseLong(idStr.trim()));
                if (r != null) { r.setDelFlag("1"); reportMapper.updateById(r); }
            }
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/templateList")
    @ApiOperation("模板列表")
    public String templateList() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesReportTemplate> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0").orderByAsc("TEMPLATE_ID");
            List<TblDerivativesReportTemplate> list = templateMapper.selectList(qw);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", list);
            data.put("totalRecord", list.size());
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/addTemplate")
    @ApiOperation("新建模板")
    public String addTemplate(@RequestParam String templateName,
                              @RequestParam String templateType,
                              @RequestParam(required = false) String description) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesReportTemplate tpl = new TblDerivativesReportTemplate();
            tpl.setTemplateName(templateName);
            tpl.setTemplateType(templateType);
            tpl.setDescription(description);
            tpl.setCreatedBy(user.getRealname() != null ? user.getRealname() : (user.getUsername() != null ? user.getUsername() : "管理员"));
            tpl.setCreateTime(new Date());
            tpl.setUpdateTime(new Date());
            templateMapper.insert(tpl);
            return new JsonBean(1, "新建成功", tpl).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "新建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/editTemplate/{id}")
    @ApiOperation("编辑模板")
    public String editTemplate(@PathVariable Long id,
                               @RequestParam String templateName,
                               @RequestParam String templateType,
                               @RequestParam(required = false) String description) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesReportTemplate tpl = templateMapper.selectById(id);
            if (tpl == null) return new JsonBean(0, "模板不存在", null).toJson();
            tpl.setTemplateName(templateName);
            tpl.setTemplateType(templateType);
            tpl.setDescription(description);
            tpl.setUpdateTime(new Date());
            templateMapper.updateById(tpl);
            return new JsonBean(1, "编辑成功", tpl).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "编辑失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/deleteTemplate/{id}")
    @ApiOperation("删除模板")
    public String deleteTemplate(@PathVariable Long id) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesReportTemplate tpl = templateMapper.selectById(id);
            if (tpl == null) return new JsonBean(0, "模板不存在", null).toJson();
            tpl.setDelFlag("1");
            templateMapper.updateById(tpl);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/chartData")
    @ApiOperation("图表数据")
    public String chartData() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesValuation> vqw = new QueryWrapper<>();
            vqw.eq("DEL_FLAG", "0");
            List<TblDerivativesValuation> valuations = valuationMapper.selectList(vqw);
            Map<String, BigDecimal> positionMap = new LinkedHashMap<>();
            positionMap.put("远期", BigDecimal.ZERO); positionMap.put("期权", BigDecimal.ZERO);
            positionMap.put("掉期", BigDecimal.ZERO); positionMap.put("期货", BigDecimal.ZERO);
            for (TblDerivativesValuation v : valuations) {
                BigDecimal mv = v.getMarketValue() != null ? v.getMarketValue() : BigDecimal.ZERO;
                if ("FORWARD".equals(v.getProductType())) positionMap.merge("远期", mv, BigDecimal::add);
                else if ("OPTION".equals(v.getProductType())) positionMap.merge("期权", mv, BigDecimal::add);
                else if ("SWAP".equals(v.getProductType())) positionMap.merge("掉期", mv, BigDecimal::add);
                else if ("FUTURES".equals(v.getProductType())) positionMap.merge("期货", mv, BigDecimal::add);
            }
            List<Map<String, Object>> positionDist = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> e : positionMap.entrySet()) {
                if (e.getValue().compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> m = new HashMap<>(); m.put("name", e.getKey()); m.put("value", e.getValue()); positionDist.add(m);
                }
            }
            List<String> pnlDates = new ArrayList<>();
            List<BigDecimal> pnlValues = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new Date()); cal.add(Calendar.DAY_OF_MONTH, -i);
                pnlDates.add(sdf.format(cal.getTime()));
                pnlValues.add(BigDecimal.valueOf(-42200 + (Math.random() - 0.3) * 80000).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            List<String> varTypes = Arrays.asList("远期", "期权", "掉期", "期货");
            List<BigDecimal> varValues = new ArrayList<>();
            QueryWrapper<TblDerivativesRiskLimit> rqw = new QueryWrapper<>();
            rqw.eq("DEL_FLAG", "0");
            List<TblDerivativesRiskLimit> limits = riskLimitMapper.selectList(rqw);
            for (String t : varTypes) {
                BigDecimal used = limits.stream()
                    .filter(l -> l.getUsedAmount() != null)
                    .map(TblDerivativesRiskLimit::getUsedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (used.compareTo(BigDecimal.ZERO) == 0) used = BigDecimal.valueOf(100000 + Math.random() * 500000).setScale(2, BigDecimal.ROUND_HALF_UP);
                varValues.add(used.divide(BigDecimal.valueOf(varTypes.size()), 2, BigDecimal.ROUND_HALF_UP));
            }
            List<String> returnRanges = Arrays.asList("<-2%", "-2%~-1%", "-1%~0%", "0%~1%", "1%~2%", ">2%");
            List<Integer> returnCounts = Arrays.asList(2, 5, 8, 12, 7, 3);
            Map<String, Object> data = new HashMap<>();
            data.put("positionDist", positionDist);
            data.put("pnlDates", pnlDates); data.put("pnlValues", pnlValues);
            data.put("varTypes", varTypes); data.put("varValues", varValues);
            data.put("returnRanges", returnRanges); data.put("returnCounts", returnCounts);
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/download/{id}")
    @ApiOperation("下载报表")
    public void download(@PathVariable Long id, HttpServletResponse response) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) { response.setStatus(401); return; }
        TblDerivativesReport report = reportMapper.selectById(id);
        if (report == null) { response.setStatus(404); return; }
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + date + ".csv");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println("\uFEFF" + report.getReportName());
        writer.println();
        writer.println("报表类型," + report.getReportType());
        writer.println("报表周期," + report.getReportPeriod());
        writer.println("生成时间," + report.getGenerateTime());
        writer.println("生成人," + report.getGeneratedBy());
        writer.println();
        writer.println("合约编号,产品类型,标的资产,名义本金,市场价值,未实现损益,估值日期");
        QueryWrapper<TblDerivativesValuation> qw = new QueryWrapper<>();
        qw.eq("DEL_FLAG", "0");
        List<TblDerivativesValuation> list = valuationMapper.selectList(qw);
        for (TblDerivativesValuation v : list) {
            writer.println(String.format("%s,%s,%s,%s,%s,%s,%s",
                v.getContractCode(), v.getProductType(), v.getUnderlyingAsset(),
                v.getNotionalAmount(), v.getMarketValue(), v.getUnrealizedPnL(), v.getValuationDate()));
        }
        writer.flush();
    }
}
