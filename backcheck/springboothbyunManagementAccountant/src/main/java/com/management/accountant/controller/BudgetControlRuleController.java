package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetControlRule;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetControlRuleService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.excel.ExcelExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算控制规则Controller
 * 
 * @description 预算控制规则管理接口，支持规则配置、启用/禁用、规则校验等功能
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-控制规则"})
@RequestMapping(value = "/accountant/budget/control/rule")
@Slf4j
public class BudgetControlRuleController {

    @Resource
    private BudgetControlRuleService controlRuleService;

    @Resource
    private TblStaffOracleMapper staffMapper;

    /**
     * 创建控制规则
     */
    @Operation(summary = "创建控制规则")
    @ApiOperation("创建控制规则")
    @PostMapping("/create")
    public MyJsonBean<BudgetControlRule> create(@RequestBody @Validated BudgetControlRule rule) {
        MyJsonBean<BudgetControlRule> result = new MyJsonBean<>();
        try {
            BudgetControlRule created = controlRuleService.create(rule);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建控制规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建控制规则异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询规则详情
     */
    @Operation(summary = "查询规则详情")
    @ApiOperation("查询规则详情")
    @GetMapping("/detail/{ruleId}")
    public MyJsonBean<BudgetControlRule> getDetail(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId) {
        MyJsonBean<BudgetControlRule> result = new MyJsonBean<>();
        try {
            BudgetControlRule rule = controlRuleService.getById(ruleId);
            if (rule != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(rule);
            } else {
                result.setCode(0);
                result.setMsg("控制规则不存在");
            }
        } catch (Exception e) {
            log.error("查询规则详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新控制规则
     */
    @Operation(summary = "更新控制规则")
    @ApiOperation("更新控制规则")
    @PutMapping("/update/{ruleId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId,
            @RequestBody @Validated BudgetControlRule rule) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            rule.setRuleId(ruleId);
            controlRuleService.update(rule);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新控制规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新控制规则异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除控制规则
     */
    @Operation(summary = "删除控制规则")
    @ApiOperation("删除控制规则")
    @DeleteMapping("/delete/{ruleId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            controlRuleService.delete(ruleId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除控制规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除控制规则异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询控制规则列表
     */
    @Operation(summary = "分页查询控制规则列表")
    @ApiOperation("分页查询控制规则列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetControlRule>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetControlRule>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetControlRule> pageResult = controlRuleService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询控制规则列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用规则
     */
    @Operation(summary = "启用规则")
    @ApiOperation("启用规则")
    @PostMapping("/enable/{ruleId}")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            controlRuleService.enable(ruleId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (ServiceException ex) {
            log.error("启用规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用规则异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 禁用规则
     */
    @Operation(summary = "禁用规则")
    @ApiOperation("禁用规则")
    @PostMapping("/disable/{ruleId}")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            controlRuleService.disable(ruleId);
            result.setCode(1);
            result.setMsg("禁用成功");
        } catch (ServiceException ex) {
            log.error("禁用规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("禁用规则异常", e);
            result.setCode(0);
            result.setMsg("禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除
     */
    @Operation(summary = "批量删除")
    @ApiOperation("批量删除")
    @PostMapping("/batch-delete")
    public MyJsonBean<Void> batchDelete(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要删除的记录");
                return result;
            }
            controlRuleService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 校验规则
     */
    @Operation(summary = "校验规则")
    @ApiOperation("校验规则")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = controlRuleService.validateRule(params);
            result.setCode(1);
            result.setMsg("校验完成");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("校验规则异常", e);
            result.setCode(0);
            result.setMsg("校验失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取规则统计信息
     */
    @Operation(summary = "获取规则统计信息")
    @ApiOperation("获取规则统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = controlRuleService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取规则统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出控制规则数据
     */
    @Operation(summary = "导出控制规则数据")
    @ApiOperation("导出控制规则数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetControlRule> dataList = controlRuleService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算控制规则数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算控制规则", BudgetControlRule.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算控制规则数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算控制规则数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (java.io.IOException ex) {
                log.error("响应写入异常", ex);
            }
        }
    }

    @Operation(summary = "获取用户列表")
    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public MyJsonBean<List<Map<String, Object>>> getUsers() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<TblStaffOracle> staffList = staffMapper.selectAll();
            List<Map<String, Object>> users = new ArrayList<>();
            for (TblStaffOracle staff : staffList) {
                if (staff.getStatus() != null && staff.getStatus() == 1) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", staff.getStaffId());
                    map.put("name", staff.getRealName());
                    map.put("department", staff.getWorkUnitName());
                    users.add(map);
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(users);
        } catch (Exception e) {
            log.error("获取用户列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个控制规则")
    @ApiOperation("导出单个控制规则")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "规则ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=control_rule_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,规则名称,控制类型,状态\n");
            BudgetControlRule rule = controlRuleService.getById(id);
            if (rule != null) {
                sb.append(rule.getRuleId() != null ? rule.getRuleId() : id).append(",");
                sb.append(rule.getRuleName() != null ? rule.getRuleName() : "").append(",");
                sb.append(rule.getControlType() != null ? rule.getControlType() : "").append(",");
                sb.append(rule.getIsEnabled() != null ? (rule.getIsEnabled() ? "启用" : "禁用") : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个控制规则异常", e);
        }
    }
}

