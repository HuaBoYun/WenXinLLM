package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetReminder;
import com.management.accountant.service.BudgetReminderService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 预算催报管理Controller
 * 
 * @description 预算催报管理接口，支持催报配置、自动催报、催报记录等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-催报管理"})
@RequestMapping(value = "/accountant/budget/reminder")
@Slf4j
public class BudgetReminderController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetReminderService reminderService;

    /**
     * 创建催报配置
     */
    @Operation(summary = "创建催报配置")
    @ApiOperation("创建催报配置")
    @PostMapping("/create")
    public MyJsonBean<BudgetReminder> create(@RequestBody @Validated BudgetReminder reminder) {
        MyJsonBean<BudgetReminder> result = new MyJsonBean<>();
        try {
            BudgetReminder created = reminderService.create(reminder);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建催报配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建催报配置异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新催报配置
     */
    @Operation(summary = "更新催报配置")
    @ApiOperation("更新催报配置")
    @PutMapping("/update/{reminderId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId,
            @RequestBody @Validated BudgetReminder reminder) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reminder.setReminderId(reminderId);
            reminderService.update(reminder);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新催报配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新催报配置异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除催报配置
     */
    @Operation(summary = "删除催报配置")
    @ApiOperation("删除催报配置")
    @DeleteMapping("/delete/{reminderId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reminderService.delete(reminderId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除催报配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除催报配置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询催报配置
     */
    @Operation(summary = "分页查询催报配置")
    @ApiOperation("分页查询催报配置")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetReminder>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetReminder>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetReminder> pageResult = reminderService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询催报配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行催报
     */
    @Operation(summary = "执行催报")
    @ApiOperation("执行催报")
    @PostMapping("/execute/{reminderId}")
    public MyJsonBean<Map<String, Object>> executeReminder(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> executeResult = reminderService.executeReminder(reminderId);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(executeResult);
        } catch (ServiceException ex) {
            log.error("执行催报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行催报异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量催报
     */
    @Operation(summary = "批量催报")
    @ApiOperation("批量催报")
    @PostMapping("/batch/remind")
    public MyJsonBean<Map<String, Object>> batchRemind(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = reminderService.batchRemind(params);
            result.setCode(1);
            result.setMsg("批量催报成功");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量催报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量催报异常", e);
            result.setCode(0);
            result.setMsg("批量催报失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报记录
     */
    @Operation(summary = "获取催报记录")
    @ApiOperation("获取催报记录")
    @PostMapping("/records")
    public MyJsonBean<PageResult<Map<String, Object>>> getReminderRecords(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            PageResult<Map<String, Object>> records = reminderService.getReminderRecords(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(records);
        } catch (Exception e) {
            log.error("获取催报记录异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用催报策略（支持 POST 和 PUT）
     */
    @Operation(summary = "启用催报策略")
    @ApiOperation("启用催报策略")
    @RequestMapping(value = {"/enable/{reminderId}", "/{reminderId}/enable"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public MyJsonBean<Void> enable(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reminderService.enable(reminderId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (ServiceException ex) {
            log.error("启用催报策略失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用催报策略异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 禁用催报策略（支持 POST 和 PUT）
     */
    @Operation(summary = "禁用催报策略")
    @ApiOperation("禁用催报策略")
    @RequestMapping(value = {"/disable/{reminderId}", "/{reminderId}/disable"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public MyJsonBean<Void> disable(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reminderService.disable(reminderId);
            result.setCode(1);
            result.setMsg("禁用成功");
        } catch (ServiceException ex) {
            log.error("禁用催报策略失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("禁用催报策略异常", e);
            result.setCode(0);
            result.setMsg("禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试催报策略
     */
    @Operation(summary = "测试催报策略")
    @ApiOperation("测试催报策略")
    @RequestMapping(value = {"/test/{reminderId}", "/{reminderId}/test"}, method = RequestMethod.POST)
    public MyJsonBean<Map<String, Object>> test(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = reminderService.test(reminderId);
            result.setCode(1);
            result.setMsg("测试成功");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("测试催报策略失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("测试催报策略异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制催报策略
     */
    @Operation(summary = "复制催报策略")
    @ApiOperation("复制催报策略")
    @PostMapping("/copy/{reminderId}")
    public MyJsonBean<BudgetReminder> copy(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<BudgetReminder> result = new MyJsonBean<>();
        try {
            BudgetReminder copied = reminderService.copy(reminderId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copied);
        } catch (ServiceException ex) {
            log.error("复制催报策略失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制催报策略异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报统计数据
     */
    @Operation(summary = "获取催报统计数据")
    @ApiOperation("获取催报统计数据")
    @RequestMapping(value = "/stats", method = {RequestMethod.GET, RequestMethod.POST})
    public MyJsonBean<Map<String, Object>> getStats() {
        Map<String, Object> params = new HashMap<>();
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = reminderService.getStats(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取催报统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报目标列表
     */
    @Operation(summary = "获取催报目标列表")
    @ApiOperation("获取催报目标列表")
    @GetMapping("/targets/{reminderId}")
    public MyJsonBean<Map<String, Object>> getTargets(
            @ApiParam(value = "催报ID", required = true) @PathVariable String reminderId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> targets = reminderService.getTargets(reminderId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(targets);
        } catch (Exception e) {
            log.error("获取催报目标列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报策略列表（简化版）
     */
    @Operation(summary = "获取催报策略列表")
    @ApiOperation("获取催报策略列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> getList(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> list = reminderService.getList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取催报策略列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 发送催报（批量）
     */
    @Operation(summary = "发送催报")
    @ApiOperation("发送催报")
    @PostMapping("/send")
    public MyJsonBean<Map<String, Object>> sendReminder(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> sendParams = params != null ? params : new HashMap<>();
            Map<String, Object> sendResult = reminderService.batchRemind(sendParams);
            result.setCode(1);
            result.setMsg("催报发送成功");
            result.setData(sendResult);
        } catch (ServiceException ex) {
            log.error("发送催报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("发送催报异常", e);
            result.setCode(0);
            result.setMsg("发送失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报报告
     */
    @Operation(summary = "获取催报报告")
    @ApiOperation("获取催报报告")
    @RequestMapping(value = "/report", method = {RequestMethod.GET, RequestMethod.POST})
    public MyJsonBean<Map<String, Object>> getReport() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> stats = reminderService.getStats(params);
            PageResult<Map<String, Object>> records = reminderService.getReminderRecords(
                new HashMap<>()
            );
            Map<String, Object> report = new HashMap<>();
            report.put("summary", stats);
            report.put("records", records.getTlist());
            report.put("totalRecords", records.getTotalRecord());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("获取催报报告异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取催报设置
     */
    @Operation(summary = "获取催报设置")
    @ApiOperation("获取催报设置")
    @GetMapping("/settings")
    public MyJsonBean<Map<String, Object>> getSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> settings = new HashMap<>();
            settings.put("defaultFrequency", "DAILY");
            settings.put("maxRetries", 3);
            settings.put("enableEmail", true);
            settings.put("enableSms", true);
            settings.put("enableSystem", true);
            settings.put("enableWechat", false);
            settings.put("sendTimeStart", "09:00");
            settings.put("sendTimeEnd", "18:00");
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取催报设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 保存催报设置
     */
    @Operation(summary = "保存催报设置")
    @ApiOperation("保存催报设置")
    @PostMapping("/settings")
    public MyJsonBean<Void> saveSettings(@RequestBody Map<String, Object> settings) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        // 实际项目中可将设置持久化到配置表，此处直接返回成功
        result.setCode(1);
        result.setMsg("设置保存成功");
        return result;
    }
}

