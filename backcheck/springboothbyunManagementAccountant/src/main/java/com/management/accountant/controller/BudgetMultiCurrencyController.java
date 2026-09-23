package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetMultiCurrency;
import com.management.accountant.service.BudgetMultiCurrencyService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算多币种管理Controller
 * 
 * @description 预算多币种管理接口，支持币种配置、汇率管理、币种转换等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-多币种管理"})
@RequestMapping(value = "/accountant/budget/multi/currency")
@Slf4j
public class BudgetMultiCurrencyController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetMultiCurrencyService multiCurrencyService;

    /**
     * 创建币种配置
     */
    @Operation(summary = "创建币种配置")
    @ApiOperation("创建币种配置")
    @PostMapping("/create")
    public MyJsonBean<BudgetMultiCurrency> create(@RequestBody @Validated BudgetMultiCurrency currency) {
        MyJsonBean<BudgetMultiCurrency> result = new MyJsonBean<>();
        try {
            BudgetMultiCurrency created = multiCurrencyService.create(currency);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建币种配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建币种配置异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新币种配置
     */
    @Operation(summary = "更新币种配置")
    @ApiOperation("更新币种配置")
    @PutMapping("/update/{currencyId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "币种ID", required = true) @PathVariable String currencyId,
            @RequestBody @Validated BudgetMultiCurrency currency) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            currency.setCurrencyId(currencyId);
            multiCurrencyService.update(currency);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新币种配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新币种配置异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除币种配置
     */
    @Operation(summary = "删除币种配置")
    @ApiOperation("删除币种配置")
    @DeleteMapping("/delete/{currencyId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "币种ID", required = true) @PathVariable String currencyId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            multiCurrencyService.delete(currencyId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除币种配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除币种配置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询币种配置
     */
    @Operation(summary = "分页查询币种配置")
    @ApiOperation("分页查询币种配置")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetMultiCurrency>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetMultiCurrency>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetMultiCurrency> pageResult = multiCurrencyService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询币种配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新汇率
     */
    @Operation(summary = "更新汇率")
    @ApiOperation("更新汇率")
    @PostMapping("/exchange-rate/update")
    public MyJsonBean<Void> updateExchangeRate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            multiCurrencyService.updateExchangeRate(params);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新汇率失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新汇率异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 币种转换
     */
    @Operation(summary = "币种转换")
    @ApiOperation("币种转换")
    @PostMapping("/convert")
    public MyJsonBean<Map<String, Object>> convertCurrency(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> convertResult = multiCurrencyService.convertCurrency(params);
            result.setCode(1);
            result.setMsg("转换成功");
            result.setData(convertResult);
        } catch (ServiceException ex) {
            log.error("币种转换失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("币种转换异常", e);
            result.setCode(0);
            result.setMsg("转换失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取汇率历史
     */
    @Operation(summary = "获取汇率历史")
    @ApiOperation("获取汇率历史")
    @PostMapping("/exchange-rate/history")
    public MyJsonBean<PageResult<Map<String, Object>>> getExchangeRateHistory(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            PageResult<Map<String, Object>> history = multiCurrencyService.getExchangeRateHistory(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取汇率历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量币种转换
     */
    @Operation(summary = "批量币种转换")
    @ApiOperation("批量币种转换")
    @PostMapping("/batch/convert")
    public MyJsonBean<Map<String, Object>> batchConvert(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = multiCurrencyService.batchConvert(params);
            result.setCode(1);
            result.setMsg("批量转换成功");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量币种转换失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量币种转换异常", e);
            result.setCode(0);
            result.setMsg("批量转换失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取币种列表
     */
    @Operation(summary = "获取币种列表")
    @ApiOperation("获取币种列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> getCurrencyList(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> list = multiCurrencyService.getCurrencyList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取币种列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取币种详情
     */
    @Operation(summary = "获取币种详情")
    @ApiOperation("获取币种详情")
    @GetMapping("/detail/{currencyId}")
    public MyJsonBean<BudgetMultiCurrency> getCurrencyDetail(@PathVariable String currencyId) {
        MyJsonBean<BudgetMultiCurrency> result = new MyJsonBean<>();
        try {
            BudgetMultiCurrency currency = multiCurrencyService.getDetail(currencyId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(currency);
        } catch (Exception e) {
            log.error("获取币种详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取币种统计数据
     */
    @Operation(summary = "获取币种统计数据")
    @ApiOperation("获取币种统计数据")
    @PostMapping("/stats")
    public MyJsonBean<Map<String, Object>> getCurrencyStats(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = multiCurrencyService.getCurrencyStats(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取币种统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取汇率历史（简化版）
     */
    @Operation(summary = "获取汇率历史（简化版）")
    @ApiOperation("获取汇率历史（简化版）")
    @PostMapping("/rate/history")
    public MyJsonBean<Map<String, Object>> getRateHistory(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> history = multiCurrencyService.getRateHistory(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取汇率历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用币种
     */
    @Operation(summary = "启用币种")
    @ApiOperation("启用币种")
    @PutMapping("/{currencyId}/enable")
    public MyJsonBean<Void> enableCurrency(@PathVariable String currencyId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            multiCurrencyService.enableCurrency(currencyId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (ServiceException ex) {
            log.error("启用币种失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用币种异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 禁用币种
     */
    @Operation(summary = "禁用币种")
    @ApiOperation("禁用币种")
    @PutMapping("/{currencyId}/disable")
    public MyJsonBean<Void> disableCurrency(@PathVariable String currencyId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            multiCurrencyService.disableCurrency(currencyId);
            result.setCode(1);
            result.setMsg("禁用成功");
        } catch (ServiceException ex) {
            log.error("禁用币种失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("禁用币种异常", e);
            result.setCode(0);
            result.setMsg("禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 设置基准币种
     */
    @Operation(summary = "设置基准币种")
    @ApiOperation("设置基准币种")
    @PutMapping("/base")
    public MyJsonBean<Void> setBaseCurrency(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String currencyCode = (String) params.get("currencyCode");
            if (currencyCode == null || currencyCode.trim().isEmpty()) {
                result.setCode(0);
                result.setMsg("币种代码不能为空");
                return result;
            }
            multiCurrencyService.setBaseCurrency(currencyCode);
            result.setCode(1);
            result.setMsg("设置成功");
        } catch (ServiceException ex) {
            log.error("设置基准币种失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("设置基准币种异常", e);
            result.setCode(0);
            result.setMsg("设置失败：" + e.getMessage());
        }
        return result;
    }
}

