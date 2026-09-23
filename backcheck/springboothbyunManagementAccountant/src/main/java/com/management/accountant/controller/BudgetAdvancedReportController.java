package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetAdvancedReportService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算高级报表Controller
 * 
 * @description 预算高级报表接口，支持自定义报表、可视化报表、交互式报表、报表导出、报表订阅
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-高级报表"})
@RequestMapping(value = "/accountant/advanced/reports")
@Slf4j
public class BudgetAdvancedReportController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetAdvancedReportService advancedReportService;

    /**
     * 创建自定义报表
     */
    @Operation(summary = "创建自定义报表")
    @ApiOperation("创建自定义报表")
    @PostMapping("/custom/create")
    public MyJsonBean<Map<String, Object>> createCustomReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = advancedReportService.createCustomReport(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("创建自定义报表失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建自定义报表异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成可视化报表
     */
    @Operation(summary = "生成可视化报表")
    @ApiOperation("生成可视化报表")
    @PostMapping("/visualization/generate")
    public MyJsonBean<Map<String, Object>> generateVisualization(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> visualization = advancedReportService.generateVisualization(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(visualization);
        } catch (ServiceException ex) {
            log.error("生成可视化报表失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成可视化报表异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建交互式报表
     */
    @Operation(summary = "创建交互式报表")
    @ApiOperation("创建交互式报表")
    @PostMapping("/interactive/create")
    public MyJsonBean<Map<String, Object>> createInteractiveReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = advancedReportService.createInteractiveReport(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("创建交互式报表失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建交互式报表异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出报表
     */
    @Operation(summary = "导出报表")
    @ApiOperation("导出报表")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = advancedReportService.exportReport(params);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (ServiceException ex) {
            log.error("导出报表失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("导出报表异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 订阅报表
     */
    @Operation(summary = "订阅报表")
    @ApiOperation("订阅报表")
    @PostMapping("/subscribe")
    public MyJsonBean<Map<String, Object>> subscribeReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> subscription = advancedReportService.subscribeReport(params);
            result.setCode(1);
            result.setMsg("订阅成功");
            result.setData(subscription);
        } catch (ServiceException ex) {
            log.error("订阅报表失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("订阅报表异常", e);
            result.setCode(0);
            result.setMsg("订阅失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 报表模板管理
     */
    @Operation(summary = "报表模板管理")
    @ApiOperation("报表模板管理")
    @PostMapping("/template/manage")
    public MyJsonBean<Map<String, Object>> manageTemplate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> template = advancedReportService.manageTemplate(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(template);
        } catch (ServiceException ex) {
            log.error("报表模板管理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("报表模板管理异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    // 以下 CRUD/list/stats/generate/download/copy/export-config/subscribers/history 端点
    // 已在 BudgetAdvancedFeaturesController 中实现（/accountant/advanced/reports/...），此处不再重复注册

}

