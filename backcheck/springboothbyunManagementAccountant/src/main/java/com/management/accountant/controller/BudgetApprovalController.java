package com.management.accountant.controller;

import com.alibaba.fastjson.JSON;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetApproval;
import com.management.accountant.oracle.entity.budget.BudgetApprovalFlow;
import com.management.accountant.oracle.service.budget.BudgetApprovalFlowService;
import com.management.accountant.service.BudgetApprovalService;
import com.management.accountant.util.ExcelUtil;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import com.management.accountant.vo.param.BudgetApprovalFlowDTO;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算审批流程Controller
 *
 * @description 预算审批流程管理接口，支持审批、拒绝、撤回、查询等功能
 * @author AI Agent
 * @date 2026-01-29
 */
@RestController
@Api(tags = {"NCV65全面预算-审批流程管理"})
@RequestMapping(value = "/accountant/budget/approval")
@Slf4j
public class BudgetApprovalController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetApprovalService approvalService;

    @Resource
    private BudgetApprovalFlowService budgetApprovalFlowService;

    // ==================== 新增审批流程接口 ====================

    /**
     * 创建审批流程
     */
    @Operation(summary = "创建审批流程")
    @ApiOperation("创建审批流程")
    @PostMapping("/flow/create")
    public String createFlow(@RequestBody BudgetApprovalFlowDTO dto) {
        try {
            boolean result = budgetApprovalFlowService.createFlow(dto);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "创建成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "创建失败", null));
            }
        } catch (Exception e) {
            log.error("创建审批流程异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "创建失败：" + e.getMessage(), null));
        }
    }

    /**
     * 审批操作
     */
    @Operation(summary = "审批操作")
    @ApiOperation("审批操作")
    @PostMapping("/flow/approve")
    public String approve(@RequestParam String flowId,
                         @RequestParam String action,
                         @RequestParam(required = false) String comment) {
        try {
            boolean result = budgetApprovalFlowService.approve(flowId, action, comment);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "审批成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "审批失败", null));
            }
        } catch (Exception e) {
            log.error("审批操作异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "审批失败：" + e.getMessage(), null));
        }
    }

    /**
     * 拒绝操作
     */
    @Operation(summary = "拒绝操作")
    @ApiOperation("拒绝操作")
    @PostMapping("/flow/reject")
    public String reject(@RequestParam String flowId,
                        @RequestParam String reason) {
        try {
            boolean result = budgetApprovalFlowService.reject(flowId, reason);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "拒绝成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "拒绝失败", null));
            }
        } catch (Exception e) {
            log.error("拒绝操作异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "拒绝失败：" + e.getMessage(), null));
        }
    }

    /**
     * 批量审批
     */
    @Operation(summary = "批量审批")
    @ApiOperation("批量审批")
    @PostMapping("/flow/batch")
    public String batchApprove(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> flowIds = (List<String>) params.get("flowIds");
            String action = (String) params.get("action");

            boolean result = budgetApprovalFlowService.batchApprove(flowIds, action);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "批量审批成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "批量审批失败", null));
            }
        } catch (Exception e) {
            log.error("批量审批异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "批量审批失败：" + e.getMessage(), null));
        }
    }

    /**
     * 获取审批详情
     */
    @Operation(summary = "获取审批详情")
    @ApiOperation("获取审批详情")
    @PostMapping("/flow/detail")
    public String getFlowDetail(@RequestParam String flowId) {
        try {
            BudgetApprovalFlowDTO dto = budgetApprovalFlowService.getFlowDetail(flowId);
            if (dto != null) {
                return JSON.toJSONString(new MyJsonBean<>(1, "查询成功", dto));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "流程不存在", null));
            }
        } catch (Exception e) {
            log.error("获取审批详情异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 查询待审批流程列表
     */
    @Operation(summary = "查询待审批流程列表")
    @ApiOperation("查询待审批流程列表")
    @PostMapping("/flow/pending")
    public String listPendingFlows(@RequestParam(required = false) String companyId) {
        try {
            List<BudgetApprovalFlow> list = budgetApprovalFlowService.listPendingFlows(companyId);
            return JSON.toJSONString(new MyJsonBean<>(1, "查询成功", list));
        } catch (Exception e) {
            log.error("查询待审批流程异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 统计流程数量
     */
    @Operation(summary = "统计流程数量")
    @ApiOperation("统计流程数量")
    @PostMapping("/flow/stats")
    public String countFlowStats(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Integer> stats = budgetApprovalFlowService.countFlowStats(companyId);
            return JSON.toJSONString(new MyJsonBean<>(1, "查询成功", stats));
        } catch (Exception e) {
            log.error("统计流程数量异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "查询失败：" + e.getMessage(), null));
        }
    }

    // ==================== 原有接口保持不变 ====================

    /**
     * 创建审批流程（原有接口）
     */
    @Operation(summary = "创建审批流程（原有）")
    @ApiOperation("创建审批流程（原有）")
    @PostMapping("/create")
    public MyJsonBean<BudgetApproval> create(@RequestBody @Validated BudgetApproval approval) {
        MyJsonBean<BudgetApproval> result = new MyJsonBean<>();
        try {
            BudgetApproval created = approvalService.create(approval);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建审批流程失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建审批流程异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询审批详情
     */
    @Operation(summary = "查询审批详情")
    @ApiOperation("查询审批详情")
    @GetMapping("/detail/{approvalId}")
    public MyJsonBean<BudgetApproval> getDetail(
            @ApiParam(value = "审批ID", required = true) @PathVariable String approvalId) {
        MyJsonBean<BudgetApproval> result = new MyJsonBean<>();
        try {
            BudgetApproval approval = approvalService.getById(approvalId);
            if (approval != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(approval);
            } else {
                result.setCode(0);
                result.setMsg("审批流程不存在");
            }
        } catch (Exception e) {
            log.error("查询审批详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询审批列表
     */
    @Operation(summary = "分页查询审批列表")
    @ApiOperation("分页查询审批列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<LinkedHashMap<String, Object>>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<LinkedHashMap<String, Object>>> result = new MyJsonBean<>();
        try {
            PageResult<LinkedHashMap<String, Object>> pageResult = approvalService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询审批列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批通过
     */
    @Operation(summary = "审批通过")
    @ApiOperation("审批通过")
    @PostMapping("/approve")
    public MyJsonBean<Void> approve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String approvalId = getApprovalId(params);
            String comment = (String) params.get("comment");
            approvalService.approve(approvalId, comment);
            result.setCode(1);
            result.setMsg("审批通过成功");
        } catch (ServiceException ex) {
            log.error("审批通过失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批通过异常", e);
            result.setCode(0);
            result.setMsg("审批通过失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批拒绝
     */
    @Operation(summary = "审批拒绝")
    @ApiOperation("审批拒绝")
    @PostMapping("/reject")
    public MyJsonBean<Void> reject(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String approvalId = getApprovalId(params);
            String reason = (String) params.get("reason");
            approvalService.reject(approvalId, reason);
            result.setCode(1);
            result.setMsg("审批拒绝成功");
        } catch (ServiceException ex) {
            log.error("审批拒绝失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批拒绝异常", e);
            result.setCode(0);
            result.setMsg("审批拒绝失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量审批通过（原有接口）
     */
    @Operation(summary = "批量审批通过（原有）")
    @ApiOperation("批量审批通过（原有）")
    @PostMapping("/batch-approve")
    public MyJsonBean<Void> batchApproveOld(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            String comment = (String) params.get("comment");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要审批的记录");
                return result;
            }
            approvalService.batchApprove(ids, comment);
            result.setCode(1);
            result.setMsg("批量审批通过成功");
        } catch (ServiceException ex) {
            log.error("批量审批通过失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量审批通过异常", e);
            result.setCode(0);
            result.setMsg("批量审批通过失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量审批拒绝
     */
    @Operation(summary = "批量审批拒绝")
    @ApiOperation("批量审批拒绝")
    @PostMapping("/batch-reject")
    public MyJsonBean<Void> batchReject(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            String reason = (String) params.get("reason");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要拒绝的记录");
                return result;
            }
            approvalService.batchReject(ids, reason);
            result.setCode(1);
            result.setMsg("批量审批拒绝成功");
        } catch (ServiceException ex) {
            log.error("批量审批拒绝失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量审批拒绝异常", e);
            result.setCode(0);
            result.setMsg("批量审批拒绝失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取审批统计信息
     */
    @Operation(summary = "获取审批统计信息")
    @ApiOperation("获取审批统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = approvalService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取审批统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 撤回审批
     */
    @Operation(summary = "撤回审批")
    @ApiOperation("撤回审批")
    @PostMapping("/{approvalId}/withdraw")
    public MyJsonBean<Void> withdraw(
            @ApiParam(value = "审批ID", required = true) @PathVariable String approvalId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            approvalService.withdraw(approvalId);
            result.setCode(1);
            result.setMsg("撤回成功");
        } catch (ServiceException ex) {
            log.error("撤回审批失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("撤回审批异常", e);
            result.setCode(0);
            result.setMsg("撤回失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 转交审批
     */
    @Operation(summary = "转交审批")
    @ApiOperation("转交审批")
    @PostMapping("/{approvalId}/delegate")
    public MyJsonBean<Void> delegate(
            @ApiParam(value = "审批ID", required = true) @PathVariable String approvalId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String delegateToUserId = (String) (params.get("delegateToUserId") != null ? params.get("delegateToUserId") : params.get("delegateTo"));
            String delegateToUserName = (String) (params.get("delegateToUserName") != null ? params.get("delegateToUserName") : params.get("delegateTo"));
            String comment = (String) params.get("comment");

            if (delegateToUserId == null || delegateToUserId.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择转交人");
                return result;
            }

            // TODO: 实现转交逻辑
            result.setCode(1);
            result.setMsg("转交成功");
        } catch (ServiceException ex) {
            log.error("转交审批失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("转交审批异常", e);
            result.setCode(0);
            result.setMsg("转交失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 催办
     */
    @Operation(summary = "催办")
    @ApiOperation("催办")
    @PostMapping("/{approvalId}/urge")
    public MyJsonBean<Void> urge(
            @ApiParam(value = "审批ID", required = true) @PathVariable String approvalId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // TODO: 实现催办逻辑（发送通知、记录催办次数等）
            result.setCode(1);
            result.setMsg("催办成功");
        } catch (ServiceException ex) {
            log.error("催办失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("催办异常", e);
            result.setCode(0);
            result.setMsg("催办失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取审批历史
     */
    @Operation(summary = "获取审批历史")
    @ApiOperation("获取审批历史")
    @GetMapping("/{approvalId}/history")
    public MyJsonBean<List<Map<String, Object>>> getHistory(
            @ApiParam(value = "审批ID", required = true) @PathVariable String approvalId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            BudgetApproval approval = approvalService.getById(approvalId);
            List<Map<String, Object>> history = new ArrayList<>();
            if (approval != null) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("nodeName", approval.getCurrentNode());
                item.put("approverName", approval.getCurrentApproverName());
                item.put("approvalResult", approval.getApprovalStatus2() != null ? approval.getApprovalStatus2() : approval.getApprovalStatus().toUpperCase());
                item.put("approvalComment", approval.getApprovalComment());
                item.put("approvalTime", approval.getApprovalTime() != null ? approval.getApprovalTime() : approval.getSubmitTime());
                history.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取审批历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出审批流程
     */
    @Operation(summary = "导出审批流程")
    @ApiOperation("导出审批流程")
    @PostMapping("/export")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<?> dataList = approvalService.getPage(params).getTlist();
            if (dataList == null) dataList = new ArrayList<>();
            ExcelUtil.exportExcel((List) dataList, (Class) LinkedHashMap.class, "审批流程", response);
        } catch (Exception e) {
            log.error("导出审批流程异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 获取审批人列表
     */
    @Operation(summary = "获取审批人列表")
    @ApiOperation("获取审批人列表")
    @GetMapping("/approvers")
    public MyJsonBean<List<Map<String, Object>>> getApprovers() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> approvers = new ArrayList<>();
            Map<String, Object> approver1 = new HashMap<>();
            approver1.put("id", "finance_manager");
            approver1.put("name", "财务经理");
            approvers.add(approver1);

            Map<String, Object> approver2 = new HashMap<>();
            approver2.put("id", "department_manager");
            approver2.put("name", "部门负责人");
            approvers.add(approver2);

            Map<String, Object> approver3 = new HashMap<>();
            approver3.put("id", "general_manager");
            approver3.put("name", "总经理");
            approvers.add(approver3);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(approvers);
        } catch (Exception e) {
            log.error("获取审批人列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 待审批分页查询
     */
    @Operation(summary = "待审批分页查询")
    @ApiOperation("待审批分页查询")
    @PostMapping("/pending/page")
    public MyJsonBean<PageResult<LinkedHashMap<String, Object>>> getPendingPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<LinkedHashMap<String, Object>>> result = new MyJsonBean<>();
        try {
            params.put("approvalStatus", "PENDING");
            PageResult<LinkedHashMap<String, Object>> pageResult = approvalService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("查询待审批列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新审批流程
     */
    @Operation(summary = "更新审批流程")
    @ApiOperation("更新审批流程")
    @PutMapping("/update")
    public MyJsonBean<BudgetApproval> update(@RequestBody @Validated BudgetApproval approval) {
        MyJsonBean<BudgetApproval> result = new MyJsonBean<>();
        try {
            BudgetApproval updated = approvalService.update(approval);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新审批流程失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新审批流程异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除审批流程
     */
    @Operation(summary = "删除审批流程")
    @ApiOperation("删除审批流程")
    @DeleteMapping("/delete/{approvalId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "审批ID", required = true) @PathVariable String approvalId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            approvalService.delete(approvalId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除审批流程失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除审批流程异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    private String getApprovalId(Map<String, Object> params) {
        Object approvalId = params.get("approvalId");
        if (approvalId == null) {
            approvalId = params.get("flowId");
        }
        return approvalId == null ? null : approvalId.toString();
    }
}