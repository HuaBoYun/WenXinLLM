package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAdjustment;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.mapper.budget.BudgetAdjustmentMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.service.BudgetAdjustmentService;
import com.management.accountant.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;

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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算调整Controller
 *
 * @description 预算调整管理接口，支持预算增加、减少、转移等调整操作
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算调整"})
@RequestMapping(value = "/financial/ncv65/budget/adjustment")
@Slf4j
public class BudgetAdjustmentController {

    @Resource
    private BudgetAdjustmentService adjustmentService;

    @Resource
    private BudgetAdjustmentMapper adjustmentMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private UserProvider userProvider;

    private String getCurrentUserName() {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff != null) {
                String name = staff.getRealname();
                if (name != null && !name.isEmpty()) return name;
                return staff.getUsername();
            }
        } catch (Exception e) {
            log.debug("获取当前用户失败", e);
        }
        return "admin";
    }

    private String getCurrentUserId() {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff != null && staff.getStaffid() != null) {
                return staff.getStaffid().toString();
            }
        } catch (Exception e) {
            log.debug("获取当前用户ID失败", e);
        }
        return "0";
    }

    /**
     * 创建调整申请
     */
    @Operation(summary = "创建调整申请")
    @ApiOperation("创建调整申请")
    @PostMapping("/create")
    public MyJsonBean<BudgetAdjustment> create(@RequestBody @Validated BudgetAdjustment adjustment) {
        MyJsonBean<BudgetAdjustment> result = new MyJsonBean<>();
        try {
            BudgetAdjustment created = adjustmentService.create(adjustment);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建调整申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建调整申请异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询调整详情
     */
    @Operation(summary = "查询调整详情")
    @ApiOperation("查询调整详情")
    @GetMapping("/detail/{adjustmentId}")
    public MyJsonBean<BudgetAdjustment> getDetail(
            @ApiParam(value = "调整ID", required = true) @PathVariable String adjustmentId) {
        MyJsonBean<BudgetAdjustment> result = new MyJsonBean<>();
        try {
            BudgetAdjustment adjustment = adjustmentService.getById(adjustmentId);
            if (adjustment != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(adjustment);
            } else {
                result.setCode(0);
                result.setMsg("调整记录不存在");
            }
        } catch (Exception e) {
            log.error("查询调整详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新调整申请
     */
    @Operation(summary = "更新调整申请")
    @ApiOperation("更新调整申请")
    @PutMapping("/update/{adjustmentId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "调整ID", required = true) @PathVariable String adjustmentId,
            @RequestBody @Validated BudgetAdjustment adjustment) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            adjustment.setAdjustmentId(adjustmentId);
            adjustmentService.update(adjustment);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新调整申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新调整申请异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询调整列表
     */
    @Operation(summary = "分页查询调整列表")
    @ApiOperation("分页查询调整列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(adjustmentService.getPage(params));
        } catch (Exception e) {
            log.error("分页查询调整列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 提交审批
     */
    @Operation(summary = "提交审批")
    @ApiOperation("提交审批")
    @PostMapping("/submit/{adjustmentId}")
    public MyJsonBean<Void> submit(
            @ApiParam(value = "调整ID", required = true) @PathVariable String adjustmentId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            adjustmentService.submit(adjustmentId);
            result.setCode(1);
            result.setMsg("提交成功");
        } catch (ServiceException ex) {
            log.error("提交审批失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("提交审批异常", e);
            result.setCode(0);
            result.setMsg("提交失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批调整
     */
    @Operation(summary = "审批调整")
    @ApiOperation("审批调整")
    @PostMapping("/approve")
    public MyJsonBean<Void> approve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            adjustmentService.approve(params);
            result.setCode(1);
            result.setMsg("审批成功");
        } catch (ServiceException ex) {
            log.error("审批调整失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批调整异常", e);
            result.setCode(0);
            result.setMsg("审批失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行调整
     */
    @Operation(summary = "执行调整")
    @ApiOperation("执行调整")
    @PostMapping("/execute/{adjustmentId}")
    public MyJsonBean<Void> execute(
            @ApiParam(value = "调整ID", required = true) @PathVariable String adjustmentId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            adjustmentService.execute(adjustmentId);
            result.setCode(1);
            result.setMsg("执行成功");
        } catch (ServiceException ex) {
            log.error("执行调整失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行调整异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 拒绝调整
     */
    @Operation(summary = "拒绝调整")
    @ApiOperation("拒绝调整")
    @PostMapping("/{adjustmentId}/reject")
    public MyJsonBean<Void> reject(
            @ApiParam(value = "调整ID", required = true) @PathVariable String adjustmentId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String reason = (String) params.get("reason");
            if (reason == null || reason.isEmpty()) {
                result.setCode(0);
                result.setMsg("拒绝原因不能为空");
                return result;
            }

            BudgetAdjustment existing = adjustmentService.getById(adjustmentId);
            if (existing == null) {
                result.setCode(0);
                result.setMsg("调整记录不存在");
                return result;
            }

            BudgetAdjustment update = new BudgetAdjustment();
            update.setAdjustmentId(adjustmentId);
            update.setAdjustmentStatus("REJECTED");
            update.setApproveComment(reason);
            update.setApproveDate(new java.util.Date());
            update.setUpdateTime(new java.util.Date());
            update.setApproverName(getCurrentUserName());
            update.setApproverId(getCurrentUserId());
            adjustmentMapper.updateById(update);

            result.setCode(1);
            result.setMsg("拒绝成功");
        } catch (ServiceException ex) {
            log.error("拒绝调整失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("拒绝调整异常", e);
            result.setCode(0);
            result.setMsg("拒绝失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取调整统计信息
     */
    @Operation(summary = "获取调整统计信息")
    @ApiOperation("获取调整统计信息")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            // 从数据库查询真实统计数据
            QueryWrapper<BudgetAdjustment> baseWrapper = new QueryWrapper<>();
            baseWrapper.eq("DEL_FLAG", 0);

            // 待审批: PENDING 和 SUBMITTED 状态
            QueryWrapper<BudgetAdjustment> pendingWrapper = new QueryWrapper<>();
            pendingWrapper.eq("DEL_FLAG", 0).in("ADJUSTMENT_STATUS", "PENDING", "SUBMITTED");
            long pendingCount = adjustmentMapper.selectCount(pendingWrapper);

            // 已批准: APPROVED 状态
            QueryWrapper<BudgetAdjustment> approvedWrapper = new QueryWrapper<>();
            approvedWrapper.eq("DEL_FLAG", 0).eq("ADJUSTMENT_STATUS", "APPROVED");
            long approvedCount = adjustmentMapper.selectCount(approvedWrapper);

            // 已拒绝: REJECTED 状态
            QueryWrapper<BudgetAdjustment> rejectedWrapper = new QueryWrapper<>();
            rejectedWrapper.eq("DEL_FLAG", 0).eq("ADJUSTMENT_STATUS", "REJECTED");
            long rejectedCount = adjustmentMapper.selectCount(rejectedWrapper);

            // 总计: 所有未删除的记录
            long totalCount = adjustmentMapper.selectCount(baseWrapper);

            // 计算各状态的金额合计
            java.math.BigDecimal pendingAmount = java.math.BigDecimal.ZERO;
            java.math.BigDecimal approvedAmount = java.math.BigDecimal.ZERO;
            java.math.BigDecimal rejectedAmount = java.math.BigDecimal.ZERO;
            java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

            // 查询所有未删除记录，按状态汇总金额
            QueryWrapper<BudgetAdjustment> allWrapper = new QueryWrapper<>();
            allWrapper.eq("DEL_FLAG", 0);
            List<BudgetAdjustment> allRecords = adjustmentMapper.selectList(allWrapper);
            for (BudgetAdjustment adj : allRecords) {
                java.math.BigDecimal amount = adj.getAdjustmentAmount() != null ? adj.getAdjustmentAmount() : java.math.BigDecimal.ZERO;
                totalAmount = totalAmount.add(amount);
                String status = adj.getAdjustmentStatus();
                if ("PENDING".equals(status) || "SUBMITTED".equals(status)) {
                    pendingAmount = pendingAmount.add(amount);
                } else if ("APPROVED".equals(status)) {
                    approvedAmount = approvedAmount.add(amount);
                } else if ("REJECTED".equals(status)) {
                    rejectedAmount = rejectedAmount.add(amount);
                }
            }

            // 使用前端期望的字段名
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("pendingCount", pendingCount);
            stats.put("pendingAmount", pendingAmount);
            stats.put("approvedCount", approvedCount);
            stats.put("approvedAmount", approvedAmount);
            stats.put("rejectedCount", rejectedCount);
            stats.put("rejectedAmount", rejectedAmount);
            stats.put("totalCount", totalCount);
            stats.put("totalAmount", totalAmount);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取调整统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出调整记录
     */
    @Operation(summary = "导出调整记录")
    @ApiOperation("导出调整记录")
    @PostMapping("/export")
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetAdjustment> dataList = (List<BudgetAdjustment>) adjustmentService.getPage(params).get("records");
            if (dataList == null) dataList = new java.util.ArrayList<>();

            // 使用ExcelExport工具类导出
            String fileName = "预算调整数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算调整", BudgetAdjustment.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算调整数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出调整记录异常", e);
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

    /**
     * 删除调整申请
     */
    @Operation(summary = "删除调整申请")
    @ApiOperation("删除调整申请")
    @DeleteMapping("/delete/{adjustmentId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "调整ID", required = true) @PathVariable String adjustmentId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            adjustmentService.delete(adjustmentId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除调整申请异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织列表
     */
    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0).orderByAsc("ORGANIZATION_CODE");
            List<BudgetOrganization> orgList = organizationMapper.selectList(qw);
            List<Map<String, Object>> orgs = new java.util.ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("id", org.getOrganizationId());
                item.put("name", org.getOrganizationName());
                item.put("organizationId", org.getOrganizationId());
                item.put("organizationCode", org.getOrganizationCode());
                item.put("organizationName", org.getOrganizationName());
                item.put("label", org.getOrganizationName());
                item.put("value", org.getOrganizationId());
                orgs.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(orgs);
        } catch (Exception e) {
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算科目列表
     */
    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0).orderByAsc("ACCOUNT_CODE");
            List<BudgetAccount> accountList = accountMapper.selectList(qw);
            List<Map<String, Object>> accounts = new java.util.ArrayList<>();
            for (BudgetAccount acc : accountList) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("id", acc.getAccountId());
                item.put("name", acc.getAccountName());
                item.put("accountId", acc.getAccountId());
                item.put("accountCode", acc.getAccountCode());
                item.put("accountName", acc.getAccountName());
                item.put("label", acc.getAccountName());
                item.put("value", acc.getAccountId());
                accounts.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量审批
     */
    @Operation(summary = "批量审批调整")
    @ApiOperation("批量审批调整")
    @PostMapping("/batch-approve")
    public MyJsonBean<Void> batchApprove(@RequestBody List<String> ids) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            for (String id : ids) {
                Map<String, Object> params = new java.util.HashMap<>();
                params.put("adjustmentId", id);
                params.put("action", "approve");
                adjustmentService.approve(params);
            }
            result.setCode(1);
            result.setMsg("批量审批成功");
        } catch (Exception e) {
            result.setCode(0);
            result.setMsg("批量审批失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 取消调整
     */
    @Operation(summary = "取消调整")
    @ApiOperation("取消调整")
    @PostMapping("/{adjustmentId}/cancel")
    public MyJsonBean<Void> cancel(@PathVariable String adjustmentId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetAdjustment existing = adjustmentService.getById(adjustmentId);
            if (existing == null) {
                result.setCode(0);
                result.setMsg("调整记录不存在");
                return result;
            }
            // 设置为已取消状态，保留记录可见
            BudgetAdjustment update = new BudgetAdjustment();
            update.setAdjustmentId(adjustmentId);
            update.setAdjustmentStatus("CANCELLED");
            update.setUpdateTime(new java.util.Date());
            adjustmentMapper.updateById(update);
            result.setCode(1);
            result.setMsg("取消成功");
        } catch (Exception e) {
            result.setCode(0);
            result.setMsg("取消失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "下载导入模板")
    @ApiOperation("下载导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=budget_adjustment_template.xlsx");
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("下载调整模板异常", e);
        }
    }
}