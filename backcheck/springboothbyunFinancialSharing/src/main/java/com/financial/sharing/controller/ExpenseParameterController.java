package com.financial.sharing.controller;

import com.financial.sharing.service.TblExpenseParameterService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 报账参数配置控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "报账参数配置")
@RestController
@RequestMapping("/expense/parameters")
@CrossOrigin
public class ExpenseParameterController {

    @Autowired
    private TblExpenseParameterService expenseParameterService;

    @ApiOperation("查询报账参数配置列表")
    @GetMapping
    public MyJsonBean getExpenseParameterList(@RequestParam(required = false) String orgId,
                                             @RequestParam(required = false) String parameterName,
                                             @RequestParam(required = false) String parameterType,
                                             @RequestParam(required = false) Integer isEnabled,
                                             @RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("orgId", orgId);
            param.put("parameterName", parameterName);
            param.put("parameterType", parameterType);
            param.put("isEnabled", isEnabled);
            param.put("pageNo", pageNo);
            param.put("pageSize", pageSize);

            return expenseParameterService.getList(param);
        } catch (Exception e) {
            log.error("查询报账参数配置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存报账参数配置")
    @PostMapping
    public MyJsonBean saveExpenseParameter(@RequestBody Map<String, Object> param) {
        try {
            return expenseParameterService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存报账参数配置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除报账参数配置")
    @DeleteMapping("/{parameterId}")
    public MyJsonBean deleteExpenseParameter(@PathVariable String parameterId) {
        try {
            return expenseParameterService.delete(parameterId);
        } catch (Exception e) {
            log.error("删除报账参数配置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取报账参数配置详情")
    @GetMapping("/{parameterId}")
    public MyJsonBean getExpenseParameterDetail(@PathVariable String parameterId) {
        try {
            return expenseParameterService.getById(parameterId);
        } catch (Exception e) {
            log.error("获取报账参数配置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取报账参数配置选项")
    @GetMapping("/{parameterId}/options")
    public MyJsonBean getExpenseParameterOptions(@PathVariable String parameterId) {
        try {
            return expenseParameterService.getOptions(parameterId);
        } catch (Exception e) {
            log.error("获取报账参数配置选项失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存报账参数配置选项")
    @PostMapping("/{parameterId}/options")
    public MyJsonBean saveExpenseParameterOptions(@PathVariable String parameterId,
                                                 @RequestBody java.util.List<com.financial.sharing.entity.TblExpenseParameterOption> options) {
        try {
            return expenseParameterService.saveOptions(parameterId, options);
        } catch (Exception e) {
            log.error("保存报账参数配置选项失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取报账参数配置权限")
    @GetMapping("/{parameterId}/permissions")
    public MyJsonBean getExpenseParameterPermissions(@PathVariable String parameterId) {
        try {
            return expenseParameterService.getPermissions(parameterId);
        } catch (Exception e) {
            log.error("获取报账参数配置权限失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存报账参数配置权限")
    @PostMapping("/{parameterId}/permissions")
    public MyJsonBean saveExpenseParameterPermissions(@PathVariable String parameterId,
                                                     @RequestBody java.util.List<com.financial.sharing.entity.TblExpenseParameterPermission> permissions) {
        try {
            return expenseParameterService.savePermissions(parameterId, permissions);
        } catch (Exception e) {
            log.error("保存报账参数配置权限失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用报账参数配置")
    @PutMapping("/{parameterId}/status")
    public MyJsonBean updateExpenseParameterStatus(@PathVariable String parameterId,
                                                  @RequestParam Integer isEnabled) {
        try {
            return expenseParameterService.updateStatus(parameterId, isEnabled);
        } catch (Exception e) {
            log.error("更新报账参数配置状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }
}
