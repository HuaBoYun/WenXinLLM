package com.management.accountant.controller;

import com.management.accountant.service.PeriodReportService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Api(tags = {"NCV65全面预算-期间报告"})
@RequestMapping(value = "/accountant/budget/period/report")
@Slf4j
public class PeriodReportController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private PeriodReportService periodReportService;

    @Operation(summary = "获取期间报告数据")
    @ApiOperation("获取期间报告数据") @GetMapping("/data/{periodId}")
    public MyJsonBean<Map<String, Object>> getReportData(@PathVariable String periodId) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(periodReportService.getReportData(periodId));
        } catch (Exception e) {
            log.error("获取期间报告数据异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "获取报告详细数据")
    @ApiOperation("获取报告详细数据") @PostMapping("/detail")
    public MyJsonBean<Map<String, Object>> getDetailPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(periodReportService.getDetailPage(params));
        } catch (Exception e) {
            log.error("获取报告详细数据异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "导出期间报告")
    @ApiOperation("导出期间报告") @GetMapping("/export/{periodId}")
    public void exportReport(@PathVariable String periodId, HttpServletResponse response) {
        periodReportService.exportReport(periodId, response);
    }
}
