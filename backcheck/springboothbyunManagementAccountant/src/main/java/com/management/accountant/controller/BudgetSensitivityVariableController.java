package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetSensitivityVariableService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算敏感性变量 Controller
 */
@RestController
@Api(tags = {"NCV65全面预算-敏感性变量"})
@RequestMapping(value = "/accountant/budget/sensitivity/variable")
@Slf4j
public class BudgetSensitivityVariableController {

    @Resource
    private BudgetSensitivityVariableService variableService;

    @Operation(summary = "分页查询敏感性变量列表")
    @ApiOperation("分页查询敏感性变量列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(variableService.getPage(params));
        } catch (Exception e) {
            log.error("分页查询敏感性变量列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "新增敏感性变量")
    @ApiOperation("新增敏感性变量")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createVariable(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("新增成功");
            result.setData(variableService.createVariable(params));
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("新增敏感性变量异常", e);
            result.setCode(0);
            result.setMsg("新增失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新敏感性变量")
    @ApiOperation("更新敏感性变量")
    @PutMapping("/update")
    public MyJsonBean<Map<String, Object>> updateVariable(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(variableService.updateVariable(params));
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新敏感性变量异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除敏感性变量")
    @ApiOperation("删除敏感性变量")
    @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> deleteVariable(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            variableService.deleteVariable(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除敏感性变量异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新启用状态")
    @ApiOperation("更新启用状态")
    @PutMapping("/enabled/{id}")
    public MyJsonBean<Map<String, Object>> updateEnabled(@PathVariable String id,
                                                          @RequestParam Integer enabled) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(variableService.updateEnabled(id, enabled));
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新启用状态异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }
}
