package com.management.accountant.controller;

import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.service.BudgetForecastAnalysisService;
import com.management.accountant.util.MyJsonBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(tags = {"NCV65全面预算-预测分析"})
@RequestMapping(value = "/accountant/budget/analysis/forecast")
@Slf4j
public class BudgetForecastAnalysisController {

    @Resource
    private BudgetForecastAnalysisService forecastAnalysisService;

    @Resource
    private BudgetAccountMapper accountMapper;

    /** 分页查询预测分析列表 */
    @Operation(summary = "执行预测分析（分页列表）")
    @ApiOperation("执行预测分析（分页列表）")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeForecastAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(forecastAnalysisService.executeForecastAnalysis(params));
        } catch (Exception e) {
            log.error("查询预测分析列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /** 创建预测分析 */
    @Operation(summary = "创建预测分析")
    @ApiOperation("创建预测分析")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createForecast(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(forecastAnalysisService.createForecast(params));
        } catch (Exception e) {
            log.error("创建预测分析失败", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /** 编辑预测分析 */
    @Operation(summary = "编辑预测分析")
    @ApiOperation("编辑预测分析")
    @PutMapping("/update")
    public MyJsonBean<Map<String, Object>> updateForecast(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("修改成功");
            result.setData(forecastAnalysisService.updateForecast(params));
        } catch (Exception e) {
            log.error("编辑预测分析失败", e);
            result.setCode(0);
            result.setMsg("修改失败：" + e.getMessage());
        }
        return result;
    }

    /** 获取预测图表数据 */
    @Operation(summary = "获取预测图表数据")
    @ApiOperation("获取预测图表数据")
    @PostMapping("/chart")
    public MyJsonBean<Map<String, Object>> getForecastChart(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(forecastAnalysisService.getForecastChart(params));
        } catch (Exception e) {
            log.error("获取预测图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /** 导出预测分析报告 */
    @Operation(summary = "导出预测分析报告")
    @ApiOperation("导出预测分析报告")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportForecastReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(forecastAnalysisService.exportForecastReport(params));
        } catch (Exception e) {
            log.error("导出预测分析报告失败", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /** 验证预测模型 */
    @Operation(summary = "验证预测模型")
    @ApiOperation("验证预测模型")
    @PostMapping("/validate/{modelId}")
    public MyJsonBean<Map<String, Object>> validateModel(@PathVariable String modelId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(forecastAnalysisService.validateModel(modelId));
        } catch (Exception e) {
            log.error("验证预测模型失败", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    /** 训练预测模型 */
    @Operation(summary = "训练预测模型")
    @ApiOperation("训练预测模型")
    @PostMapping("/train")
    public MyJsonBean<Map<String, Object>> trainModel(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("训练成功");
            result.setData(forecastAnalysisService.trainModel(params));
        } catch (Exception e) {
            log.error("训练预测模型失败", e);
            result.setCode(0);
            result.setMsg("训练失败：" + e.getMessage());
        }
        return result;
    }

    /** 验证预测结果 */
    @Operation(summary = "验证预测结果")
    @ApiOperation("验证预测结果")
    @PostMapping("/validate-result/{id}")
    public MyJsonBean<Map<String, Object>> validateResult(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(forecastAnalysisService.validateForecast(id));
        } catch (Exception e) {
            log.error("验证预测结果失败", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    /** 删除预测分析 */
    @Operation(summary = "删除预测分析")
    @ApiOperation("删除预测分析")
    @DeleteMapping("/delete/{id}")
    public MyJsonBean<Map<String, Object>> deleteForecast(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            forecastAnalysisService.deleteForecast(id);
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(new HashMap<>());
        } catch (Exception e) {
            log.error("删除预测分析失败", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /** 统计卡片数据 */
    @Operation(summary = "获取预测分析统计数据")
    @ApiOperation("获取预测分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(forecastAnalysisService.getForecastStats());
        } catch (Exception e) {
            log.error("获取预测分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /** 4个图表数据 */
    @Operation(summary = "获取预测分析图表数据")
    @ApiOperation("获取预测分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(forecastAnalysisService.getForecastChartData(params != null ? params : new HashMap<>()));
        } catch (Exception e) {
            log.error("获取预测分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /** 模型信息 */
    @Operation(summary = "获取预测模型信息")
    @ApiOperation("获取预测模型信息")
    @GetMapping("/model-info")
    public MyJsonBean<Map<String, Object>> getModelInfo() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(forecastAnalysisService.getForecastModelInfo());
        } catch (Exception e) {
            log.error("获取预测模型信息失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /** 获取预算科目下拉列表 */
    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0).eq("IS_ENABLED", 1).orderByAsc("SORT_ORDER").orderByAsc("ACCOUNT_CODE");
            List<BudgetAccount> list = accountMapper.selectList(qw);
            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : list) {
                Map<String, Object> item = new HashMap<>();
                item.put("value", acc.getAccountId());
                item.put("label", acc.getAccountName());
                item.put("code", acc.getAccountCode());
                accounts.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

