package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAlert;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAlertMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetAlertService;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算警报Controller
 * 
 * @description 预算警报管理接口，支持警报的创建、查询、确认、升级、解决等操作
 * @author AI Agent
 * @date 2026-02-09
 */
@RestController
@Api(tags = {"NCV65全面预算-预算警报"})
@RequestMapping(value = "/accountant/budget/alert")
@Slf4j
public class BudgetAlertController {

    @Resource
    private BudgetAlertService alertService;

    @Resource
    private BudgetAlertMapper alertMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    /**
     * 创建预算警报
     */
    @Operation(summary = "创建预算警报")
    @ApiOperation("创建预算警报")
    @PostMapping("/create")
    public MyJsonBean<BudgetAlert> create(@RequestBody @Validated BudgetAlert alert) {
        MyJsonBean<BudgetAlert> result = new MyJsonBean<>();
        try {
            BudgetAlert created = alertService.create(alert);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建预算警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预算警报异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询警报详情
     */
    @Operation(summary = "查询警报详情")
    @ApiOperation("查询警报详情")
    @GetMapping("/detail/{alertId}")
    public MyJsonBean<BudgetAlert> getDetail(
            @ApiParam(value = "警报ID", required = true) @PathVariable String alertId) {
        MyJsonBean<BudgetAlert> result = new MyJsonBean<>();
        try {
            BudgetAlert alert = alertService.getById(alertId);
            if (alert != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(alert);
            } else {
                result.setCode(0);
                result.setMsg("预算警报不存在");
            }
        } catch (Exception e) {
            log.error("查询警报详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预算警报
     */
    @Operation(summary = "更新预算警报")
    @ApiOperation("更新预算警报")
    @PutMapping("/update/{alertId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "警报ID", required = true) @PathVariable String alertId,
            @RequestBody @Validated BudgetAlert alert) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            alert.setAlertId(alertId);
            alertService.update(alert);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新预算警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预算警报异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预算警报
     */
    @Operation(summary = "删除预算警报")
    @ApiOperation("删除预算警报")
    @DeleteMapping("/delete/{alertId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "警报ID", required = true) @PathVariable String alertId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            alertService.delete(alertId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除预算警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预算警报异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询警报列表
     */
    @Operation(summary = "分页查询警报列表")
    @ApiOperation("分页查询警报列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetAlert>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetAlert>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetAlert> pageResult = alertService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询警报列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 确认警报
     */
    @Operation(summary = "确认警报")
    @ApiOperation("确认警报")
    @PostMapping("/acknowledge")
    public MyJsonBean<Void> acknowledge(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String alertId = (String) params.get("alertId");
            String acknowledgedBy = (String) params.get("acknowledgedBy");
            // 前端发送 acknowledgeRemark，兼容 note
            String note = params.get("acknowledgeRemark") != null ? (String) params.get("acknowledgeRemark") : (String) params.get("note");

            alertService.acknowledge(alertId, acknowledgedBy, note);
            result.setCode(1);
            result.setMsg("确认成功");
        } catch (ServiceException ex) {
            log.error("确认警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("确认警报异常", e);
            result.setCode(0);
            result.setMsg("确认失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量确认警报
     */
    @Operation(summary = "批量确认警报")
    @ApiOperation("批量确认警报")
    @PostMapping("/batch-acknowledge")
    public MyJsonBean<Map<String, Object>> batchAcknowledge(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            // 前端发送 ids，兼容 alertIds
            List<String> alertIds = params.get("ids") != null ? (List<String>) params.get("ids") : (List<String>) params.get("alertIds");
            String acknowledgedBy = (String) params.get("acknowledgedBy");
            String note = (String) params.get("note");

            Map<String, Object> batchResult = alertService.batchAcknowledge(alertIds, acknowledgedBy, note);
            result.setCode(1);
            result.setMsg("批量确认完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量确认警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量确认警报异常", e);
            result.setCode(0);
            result.setMsg("批量确认失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 升级警报
     */
    @Operation(summary = "升级警报")
    @ApiOperation("升级警报")
    @PostMapping("/escalate")
    public MyJsonBean<Void> escalate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String alertId = (String) params.get("alertId");
            String escalatedBy = (String) params.get("escalatedBy");
            // 前端发送 escalateLevel，兼容 escalatedTo
            String escalatedTo = params.get("escalateLevel") != null ? (String) params.get("escalateLevel") : (String) params.get("escalatedTo");
            // 前端发送 escalateReason，兼容 reason
            String reason = params.get("escalateReason") != null ? (String) params.get("escalateReason") : (String) params.get("reason");

            alertService.escalate(alertId, escalatedBy, escalatedTo, reason);
            result.setCode(1);
            result.setMsg("升级成功");
        } catch (ServiceException ex) {
            log.error("升级警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("升级警报异常", e);
            result.setCode(0);
            result.setMsg("升级失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 解决警报
     */
    @Operation(summary = "解决警报")
    @ApiOperation("解决警报")
    @PostMapping("/resolve")
    public MyJsonBean<Void> resolve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String alertId = (String) params.get("alertId");
            String resolvedBy = (String) params.get("resolvedBy");
            String note = (String) params.get("note");
            
            alertService.resolve(alertId, resolvedBy, note);
            result.setCode(1);
            result.setMsg("解决成功");
        } catch (ServiceException ex) {
            log.error("解决警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("解决警报异常", e);
            result.setCode(0);
            result.setMsg("解决失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 关闭警报
     */
    @Operation(summary = "关闭警报")
    @ApiOperation("关闭警报")
    @PostMapping("/close/{alertId}")
    public MyJsonBean<Void> close(
            @ApiParam(value = "警报ID", required = true) @PathVariable String alertId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String closedBy = (String) params.get("closedBy");
            String note = (String) params.get("note");
            
            alertService.close(alertId, closedBy, note);
            result.setCode(1);
            result.setMsg("关闭成功");
        } catch (ServiceException ex) {
            log.error("关闭警报失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("关闭警报异常", e);
            result.setCode(0);
            result.setMsg("关闭失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取警报统计信息
     */
    @Operation(summary = "获取警报统计信息")
    @ApiOperation("获取警报统计信息")
    @PostMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = alertService.getStatistics(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取警报统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取待处理警报数量
     */
    @Operation(summary = "获取待处理警报数量")
    @ApiOperation("获取待处理警报数量")
    @GetMapping("/pending-count")
    public MyJsonBean<Integer> getPendingCount(
            @ApiParam(value = "公司ID") @RequestParam(required = false) String companyId) {
        MyJsonBean<Integer> result = new MyJsonBean<>();
        try {
            int count = alertService.getPendingCount(companyId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(count);
        } catch (Exception e) {
            log.error("获取待处理警报数量异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取用户相关警报
     */
    @Operation(summary = "获取用户相关警报")
    @ApiOperation("获取用户相关警报")
    @PostMapping("/user-alerts")
    public MyJsonBean<List<BudgetAlert>> getByUser(@RequestBody Map<String, Object> params) {
        MyJsonBean<List<BudgetAlert>> result = new MyJsonBean<>();
        try {
            String userId = (String) params.get("userId");
            List<BudgetAlert> alerts = alertService.getByUser(userId, params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(alerts);
        } catch (Exception e) {
            log.error("获取用户相关警报异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 发送警报通知
     */
    @Operation(summary = "发送警报通知")
    @ApiOperation("发送警报通知")
    @PostMapping("/send-notification/{alertId}")
    public MyJsonBean<Boolean> sendNotification(
            @ApiParam(value = "警报ID", required = true) @PathVariable String alertId) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            boolean success = alertService.sendNotification(alertId);
            result.setCode(1);
            result.setMsg("发送成功");
            result.setData(success);
        } catch (ServiceException ex) {
            log.error("发送警报通知失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("发送警报通知异常", e);
            result.setCode(0);
            result.setMsg("发送失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量发送警报通知
     */
    @Operation(summary = "批量发送警报通知")
    @ApiOperation("批量发送警报通知")
    @PostMapping("/batch-send-notification")
    public MyJsonBean<Map<String, Object>> batchSendNotification(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> alertIds = (List<String>) params.get("alertIds");
            Map<String, Object> batchResult = alertService.batchSendNotification(alertIds);
            result.setCode(1);
            result.setMsg("批量发送完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量发送警报通知失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量发送警报通知异常", e);
            result.setCode(0);
            result.setMsg("批量发送失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出警报数据
     */
    @Operation(summary = "导出警报数据")
    @ApiOperation("导出警报数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetAlert> dataList = alertService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算警报数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算警报", BudgetAlert.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算警报数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算警报数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (IOException ex) {
                log.error("响应写入异常", ex);
            }
        }
    }

    /**
     * 自动关闭过期警报
     */
    /**
     * 获取警报触发历史
     */
    @Operation(summary = "获取警报触发历史")
    @ApiOperation("获取警报触发历史")
    @GetMapping("/trigger-history/{alertId}")
    public MyJsonBean<List<BudgetAlert>> getTriggerHistory(
            @ApiParam(value = "警报ID", required = true) @PathVariable String alertId) {
        MyJsonBean<List<BudgetAlert>> result = new MyJsonBean<>();
        try {
            BudgetAlert alert = alertService.getById(alertId);
            if (alert == null) {
                result.setCode(0);
                result.setMsg("警报不存在");
                return result;
            }
            // 查询同一规则触发的所有历史警报
            QueryWrapper<BudgetAlert> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0);
            if (alert.getRuleId() != null) {
                qw.eq("RULE_ID", alert.getRuleId());
            } else if (alert.getBudgetId() != null) {
                qw.eq("BUDGET_ID", alert.getBudgetId());
            }
            qw.orderByDesc("TRIGGER_TIME");
            List<BudgetAlert> history = alertMapper.selectList(qw);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取警报触发历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "自动关闭过期警报")
    @ApiOperation("自动关闭过期警报")
    @PostMapping("/auto-close-expired")
    public MyJsonBean<Integer> autoCloseExpiredAlerts() {
        MyJsonBean<Integer> result = new MyJsonBean<>();
        try {
            int closedCount = alertService.autoCloseExpiredAlerts();
            result.setCode(1);
            result.setMsg("自动关闭完成");
            result.setData(closedCount);
        } catch (Exception e) {
            log.error("自动关闭过期警报异常", e);
            result.setCode(0);
            result.setMsg("自动关闭失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取用户列表")
    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public MyJsonBean<List<Map<String, Object>>> getUsers() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
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

    @Operation(summary = "处理警报记录")
    @ApiOperation("处理警报记录")
    @PostMapping("/handle/{recordId}")
    public MyJsonBean<Void> handleRecord(
            @ApiParam(value = "记录ID", required = true) @PathVariable String recordId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetAlert alert = alertService.getById(recordId);
            if (alert == null) {
                result.setCode(0);
                result.setMsg("警报记录不存在");
                return result;
            }
            alert.setAlertStatus("HANDLED");
            alert.setAcknowledgedBy(params.get("handleBy") != null ? params.get("handleBy").toString() : null);
            alert.setAcknowledgedNote(params.get("handleNote") != null ? params.get("handleNote").toString() : null);
            alert.setAcknowledgedTime(new Date());
            alertService.update(alert);
            result.setCode(1);
            result.setMsg("处理成功");
        } catch (Exception e) {
            log.error("处理警报记录异常", e);
            result.setCode(0);
            result.setMsg("处理失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个警报")
    @ApiOperation("导出单个警报")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "警报ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=alert_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,警报名称,警报类型,警报级别,状态\n");
            BudgetAlert alert = alertService.getById(id);
            if (alert != null) {
                sb.append(alert.getAlertId() != null ? alert.getAlertId() : id).append(",");
                sb.append(alert.getAlertName() != null ? alert.getAlertName() : "").append(",");
                sb.append(alert.getAlertType() != null ? alert.getAlertType() : "").append(",");
                sb.append(alert.getAlertLevel() != null ? alert.getAlertLevel() : "").append(",");
                sb.append(alert.getAlertStatus() != null ? alert.getAlertStatus() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个警报异常", e);
        }
    }
}

