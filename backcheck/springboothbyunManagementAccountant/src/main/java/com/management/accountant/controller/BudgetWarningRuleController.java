package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetWarningRule;
import com.management.accountant.oracle.entity.budget.BudgetWarningRecord;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.oracle.mapper.budget.BudgetWarningRecordMapper;
import com.management.accountant.service.BudgetWarningRuleService;
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
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * 预算预警规则Controller
 * 
 * @description 预算预警规则管理接口，支持预警规则配置、触发、通知等功能
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算预警"})
@RequestMapping(value = "/accountant/budget/control/warning")
@Slf4j
public class BudgetWarningRuleController {

    @Resource
    private BudgetWarningRuleService warningRuleService;

    @Resource
    private TblStaffOracleMapper staffMapper;

    @Resource
    private BudgetWarningRecordMapper warningRecordMapper;

    @Resource
    private com.management.accountant.oracle.mapper.budget.BudgetWarningRuleMapper warningRuleMapper;

    /**
     * 创建预警规则
     */
    @Operation(summary = "创建预警规则")
    @ApiOperation("创建预警规则")
    @PostMapping("/create")
    public MyJsonBean<BudgetWarningRule> create(@RequestBody @Validated BudgetWarningRule rule) {
        MyJsonBean<BudgetWarningRule> result = new MyJsonBean<>();
        try {
            BudgetWarningRule created = warningRuleService.create(rule);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建预警规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预警规则异常", e);
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
    public MyJsonBean<BudgetWarningRule> getDetail(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId) {
        MyJsonBean<BudgetWarningRule> result = new MyJsonBean<>();
        try {
            BudgetWarningRule rule = warningRuleService.getById(ruleId);
            if (rule != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(rule);
            } else {
                result.setCode(0);
                result.setMsg("预警规则不存在");
            }
        } catch (Exception e) {
            log.error("查询规则详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预警规则
     */
    @Operation(summary = "更新预警规则")
    @ApiOperation("更新预警规则")
    @PutMapping("/update/{ruleId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId,
            @RequestBody @Validated BudgetWarningRule rule) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            rule.setWarningRuleId(ruleId);
            warningRuleService.update(rule);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新预警规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预警规则异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预警规则
     */
    @Operation(summary = "删除预警规则")
    @ApiOperation("删除预警规则")
    @DeleteMapping("/delete/{ruleId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "规则ID", required = true) @PathVariable String ruleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            warningRuleService.delete(ruleId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除预警规则失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预警规则异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询预警规则列表
     */
    @Operation(summary = "分页查询预警规则列表")
    @ApiOperation("分页查询预警规则列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetWarningRule>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetWarningRule>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetWarningRule> pageResult = warningRuleService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询预警规则列表异常", e);
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
            warningRuleService.enable(ruleId);
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
            warningRuleService.disable(ruleId);
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
            warningRuleService.batchDelete(ids);
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
     * 触发预警检查
     */
    @Operation(summary = "触发预警检查")
    @ApiOperation("触发预警检查")
    @PostMapping("/trigger")
    public MyJsonBean<Map<String, Object>> trigger(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> triggerResult = warningRuleService.triggerWarning(params);
            result.setCode(1);
            result.setMsg("检查完成");
            result.setData(triggerResult);
        } catch (Exception e) {
            log.error("触发预警检查异常", e);
            result.setCode(0);
            result.setMsg("检查失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预警统计信息
     */
    @Operation(summary = "获取预警统计信息")
    @ApiOperation("获取预警统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = warningRuleService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取预警统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量处理预警
     */
    @Operation(summary = "批量处理预警")
    @ApiOperation("批量处理预警")
    @PostMapping("/batch-process")
    public MyJsonBean<Map<String, Object>> batchProcess(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = warningRuleService.batchProcess(params);
            result.setCode(1);
            result.setMsg("批量处理完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量处理预警失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量处理预警异常", e);
            result.setCode(0);
            result.setMsg("批量处理失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出预警规则数据
     */
    @Operation(summary = "导出预警规则数据")
    @ApiOperation("导出预警规则数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetWarningRule> dataList = warningRuleService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算预警规则数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算预警规则", BudgetWarningRule.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算预警规则数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算预警规则数据异常", e);
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

    @Operation(summary = "分页查询预警记录")
    @ApiOperation("分页查询预警记录")
    @PostMapping("/record/page")
    public MyJsonBean<PageResult<BudgetWarningRecord>> getRecordPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetWarningRecord>> result = new MyJsonBean<>();
        try {
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

            QueryWrapper<BudgetWarningRecord> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0);
            if (params.get("ruleId") != null && !"".equals(params.get("ruleId").toString())) {
                qw.eq("RULE_ID", params.get("ruleId").toString());
            }
            if (params.get("warningLevel") != null && !"".equals(params.get("warningLevel").toString())) {
                qw.eq("WARNING_LEVEL", params.get("warningLevel").toString());
            }
            if (params.get("handleStatus") != null && !"".equals(params.get("handleStatus").toString())) {
                qw.eq("HANDLE_STATUS", params.get("handleStatus").toString());
            }
            qw.orderByDesc("CREATE_TIME");

            Page<BudgetWarningRecord> page = new Page<>(pageNum, pageSize);
            IPage<BudgetWarningRecord> pageData = warningRecordMapper.selectPage(page, qw);

            PageResult<BudgetWarningRecord> pageResult = new PageResult<>();
            pageResult.setTlist(pageData.getRecords());
            pageResult.setTotalRecord((int) pageData.getTotal());
            pageResult.setPageNo(pageNum);
            pageResult.setPageSize(pageSize);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("查询预警记录异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "处理预警记录")
    @ApiOperation("处理预警记录")
    @PostMapping("/record/handle/{recordId}")
    public MyJsonBean<Void> handleRecord(
            @ApiParam(value = "记录ID", required = true) @PathVariable String recordId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetWarningRecord record = warningRecordMapper.selectById(recordId);
            if (record == null) {
                result.setCode(0);
                result.setMsg("预警记录不存在");
                return result;
            }
            record.setHandleStatus("HANDLED");
            record.setHandleBy(params.get("handleBy") != null ? params.get("handleBy").toString() : null);
            record.setHandleNote(params.get("handleNote") != null ? params.get("handleNote").toString() : null);
            record.setHandleTime(new Date());
            record.setUpdateTime(new Date());
            warningRecordMapper.updateById(record);
            result.setCode(1);
            result.setMsg("处理成功");
        } catch (Exception e) {
            log.error("处理预警记录异常", e);
            result.setCode(0);
            result.setMsg("处理失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取用户列表")
    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public MyJsonBean<List<Map<String, Object>>> getUsers() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            // 使用自定义查询方法，避免TkMapper的selectAll()与MyBatis-Plus冲突
            List<TblStaffOracle> staffList = staffMapper.selectAllActiveStaff();
            List<Map<String, Object>> users = new ArrayList<>();
            for (TblStaffOracle staff : staffList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", staff.getStaffId());
                map.put("name", staff.getRealName());
                map.put("department", staff.getOrgId());
                users.add(map);
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

    @Operation(summary = "处理预警")
    @ApiOperation("处理预警")
    @PostMapping("/process")
    public MyJsonBean<Void> processWarning(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // 兼容前端发送的 warningId 参数（规则级别处理）
            String warningId = params.get("warningId") != null ? params.get("warningId").toString() : null;
            String recordId = params.get("recordId") != null ? params.get("recordId").toString() : null;

            // 优先使用 recordId，其次使用 warningId
            String processId = recordId != null ? recordId : warningId;
            if (processId == null) {
                result.setCode(0);
                result.setMsg("warningId或recordId不能为空");
                return result;
            }

            // 如果传的是 warningId，按规则处理
            if (recordId == null && warningId != null) {
                BudgetWarningRule rule = warningRuleMapper.selectById(warningId);
                if (rule == null) {
                    result.setCode(0);
                    result.setMsg("预警规则不存在");
                    return result;
                }
                String processType = params.get("processType") != null ? params.get("processType").toString() : null;
                String processRemark = params.get("processRemark") != null ? params.get("processRemark").toString() : null;

                // 更新规则的处理状态
                if ("RESOLVE".equals(processType)) {
                    rule.setProcessStatus("RESOLVED");
                } else if ("IGNORE".equals(processType)) {
                    rule.setProcessStatus("IGNORED");
                } else {
                    rule.setProcessStatus("PROCESSING");
                }
                rule.setUpdateTime(new Date());
                warningRuleMapper.updateById(rule);
                result.setCode(1);
                result.setMsg("处理成功");
            } else {
                // 按记录处理
                BudgetWarningRecord record = warningRecordMapper.selectById(recordId);
                if (record == null) {
                    result.setCode(0);
                    result.setMsg("预警记录不存在");
                    return result;
                }
                record.setHandleStatus("PROCESSING");
                record.setHandleBy(params.get("handleBy") != null ? params.get("handleBy").toString() : null);
                record.setHandleNote(params.get("handleNote") != null ? params.get("handleNote").toString() : null);
                record.setUpdateTime(new Date());
                warningRecordMapper.updateById(record);
                result.setCode(1);
                result.setMsg("处理成功");
            }
        } catch (Exception e) {
            log.error("处理预警异常", e);
            result.setCode(0);
            result.setMsg("处理失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "确认预警")
    @ApiOperation("确认预警")
    @PostMapping("/acknowledge")
    public MyJsonBean<Void> acknowledgeAlert(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String recordId = params.get("recordId") != null ? params.get("recordId").toString() : null;
            if (recordId == null) {
                result.setCode(0);
                result.setMsg("recordId不能为空");
                return result;
            }
            BudgetWarningRecord record = warningRecordMapper.selectById(recordId);
            if (record == null) {
                result.setCode(0);
                result.setMsg("预警记录不存在");
                return result;
            }
            record.setHandleStatus("HANDLED");
            record.setHandleBy(params.get("handleBy") != null ? params.get("handleBy").toString() : null);
            record.setHandleNote(params.get("acknowledgeRemark") != null ? params.get("acknowledgeRemark").toString() :
                    (params.get("handleNote") != null ? params.get("handleNote").toString() : null));
            record.setHandleTime(new Date());
            record.setUpdateTime(new Date());
            warningRecordMapper.updateById(record);
            result.setCode(1);
            result.setMsg("确认成功");
        } catch (Exception e) {
            log.error("确认预警异常", e);
            result.setCode(0);
            result.setMsg("确认失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "升级预警")
    @ApiOperation("升级预警")
    @PostMapping("/escalate")
    public MyJsonBean<Void> escalateAlert(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String recordId = params.get("recordId") != null ? params.get("recordId").toString() : null;
            if (recordId == null) {
                result.setCode(0);
                result.setMsg("recordId不能为空");
                return result;
            }
            BudgetWarningRecord record = warningRecordMapper.selectById(recordId);
            if (record == null) {
                result.setCode(0);
                result.setMsg("预警记录不存在");
                return result;
            }
            String escalateLevel = params.get("escalateLevel") != null ? params.get("escalateLevel").toString() :
                    (params.get("warningLevel") != null ? params.get("warningLevel").toString() : "HIGH");
            record.setWarningLevel(escalateLevel);
            record.setHandleStatus("PROCESSING");
            record.setHandleNote(params.get("escalateReason") != null ? params.get("escalateReason").toString() :
                    (params.get("handleNote") != null ? params.get("handleNote").toString() : null));
            record.setUpdateTime(new Date());
            warningRecordMapper.updateById(record);
            result.setCode(1);
            result.setMsg("升级成功");
        } catch (Exception e) {
            log.error("升级预警异常", e);
            result.setCode(0);
            result.setMsg("升级失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取触发历史")
    @ApiOperation("获取触发历史")
    @GetMapping("/trigger-history/{alertId}")
    public MyJsonBean<List<BudgetWarningRecord>> getTriggerHistory(
            @ApiParam(value = "预警ID", required = true) @PathVariable String alertId) {
        MyJsonBean<List<BudgetWarningRecord>> result = new MyJsonBean<>();
        try {
            // 先查询当前记录获取ruleId，再查同一规则的所有历史记录
            BudgetWarningRecord current = warningRecordMapper.selectById(alertId);
            QueryWrapper<BudgetWarningRecord> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0);
            if (current != null && current.getRuleId() != null) {
                qw.eq("RULE_ID", current.getRuleId());
            } else {
                qw.eq("RECORD_ID", alertId);
            }
            qw.orderByDesc("CREATE_TIME");
            List<BudgetWarningRecord> history = warningRecordMapper.selectList(qw);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取触发历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量确认预警")
    @ApiOperation("批量确认预警")
    @PostMapping("/batch-acknowledge")
    @SuppressWarnings("unchecked")
    public MyJsonBean<Map<String, Object>> batchAcknowledge(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<String> ids = params.get("ids") != null ? (List<String>) params.get("ids") :
                    (params.get("recordIds") != null ? (List<String>) params.get("recordIds") : null);
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("ids不能为空");
                return result;
            }
            int successCount = 0;
            for (String id : ids) {
                BudgetWarningRecord record = warningRecordMapper.selectById(id);
                if (record != null) {
                    record.setHandleStatus("HANDLED");
                    record.setHandleBy(params.get("handleBy") != null ? params.get("handleBy").toString() : null);
                    record.setHandleTime(new Date());
                    record.setUpdateTime(new Date());
                    warningRecordMapper.updateById(record);
                    successCount++;
                }
            }
            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", ids.size());
            batchResult.put("successCount", successCount);
            result.setCode(1);
            result.setMsg("批量确认成功");
            result.setData(batchResult);
        } catch (Exception e) {
            log.error("批量确认预警异常", e);
            result.setCode(0);
            result.setMsg("批量确认失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个预警规则")
    @ApiOperation("导出单个预警规则")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "规则ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=warning_rule_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,规则名称,预警级别,状态\n");
            BudgetWarningRule rule = warningRuleService.getById(id);
            if (rule != null) {
                sb.append(rule.getWarningRuleId() != null ? rule.getWarningRuleId() : id).append(",");
                sb.append(rule.getRuleName() != null ? rule.getRuleName() : "").append(",");
                sb.append(rule.getWarningLevel() != null ? rule.getWarningLevel() : "").append(",");
                sb.append(Integer.valueOf(1).equals(rule.getIsEnabled()) ? "启用" : "停用").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个预警规则异常", e);
        }
    }

    @Operation(summary = "导入预警规则")
    @ApiOperation("导入预警规则")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importData(
            @ApiParam(value = "导入文件", required = true) @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (file == null || file.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要导入的文件");
                return result;
            }
            Map<String, Object> importResult = warningRuleService.importData(file);
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("导入预警规则异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }
}