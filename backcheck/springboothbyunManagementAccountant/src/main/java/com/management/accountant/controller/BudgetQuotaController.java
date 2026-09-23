package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetQuota;
import com.management.accountant.oracle.entity.budget.BudgetQuotaHistory;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetQuotaHistoryMapper;
import com.management.accountant.service.BudgetQuotaService;
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
import java.util.HashMap;

/**
 * 预算配额Controller
 * 
 * @description 预算配额管理接口，支持固定配额、弹性配额、动态配额等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算配额"})
@RequestMapping(value = "/accountant/budget/quota")
@Slf4j
public class BudgetQuotaController {

    @Resource
    private BudgetQuotaService quotaService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private BudgetQuotaHistoryMapper quotaHistoryMapper;

    /**
     * 创建预算配额
     */
    @Operation(summary = "创建预算配额")
    @ApiOperation("创建预算配额")
    @PostMapping("/create")
    public MyJsonBean<BudgetQuota> create(@RequestBody @Validated BudgetQuota quota) {
        MyJsonBean<BudgetQuota> result = new MyJsonBean<>();
        try {
            BudgetQuota created = quotaService.create(quota);
            recordHistory(created.getQuotaId(), "CREATE", "创建配额：" + created.getQuotaName(),
                    created.getQuotaAmount(), null, String.valueOf(created.getQuotaAmount()));
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建预算配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预算配额异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询配额详情
     */
    @Operation(summary = "查询配额详情")
    @ApiOperation("查询配额详情")
    @GetMapping("/detail/{quotaId}")
    public MyJsonBean<BudgetQuota> getDetail(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId) {
        MyJsonBean<BudgetQuota> result = new MyJsonBean<>();
        try {
            BudgetQuota quota = quotaService.getById(quotaId);
            if (quota != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(quota);
            } else {
                result.setCode(0);
                result.setMsg("预算配额不存在");
            }
        } catch (Exception e) {
            log.error("查询配额详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预算配额
     */
    @Operation(summary = "更新预算配额")
    @ApiOperation("更新预算配额")
    @PutMapping("/update/{quotaId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId,
            @RequestBody @Validated BudgetQuota quota) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetQuota before = quotaService.getById(quotaId);
            quota.setQuotaId(quotaId);
            quotaService.update(quota);
            recordHistory(quotaId, "UPDATE", "更新配额：" + quota.getQuotaName(),
                    quota.getQuotaAmount(),
                    before != null ? String.valueOf(before.getQuotaAmount()) : null,
                    String.valueOf(quota.getQuotaAmount()));
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新预算配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预算配额异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预算配额
     */
    @Operation(summary = "删除预算配额")
    @ApiOperation("删除预算配额")
    @DeleteMapping("/delete/{quotaId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetQuota before = quotaService.getById(quotaId);
            quotaService.delete(quotaId);
            recordHistory(quotaId, "DELETE",
                    "删除配额：" + (before != null ? before.getQuotaName() : quotaId),
                    before != null ? before.getQuotaAmount() : null,
                    before != null ? String.valueOf(before.getQuotaAmount()) : null, null);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除预算配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预算配额异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询配额列表
     */
    @Operation(summary = "分页查询配额列表")
    @ApiOperation("分页查询配额列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetQuota>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetQuota>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetQuota> pageResult = quotaService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询配额列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用配额
     */
    @Operation(summary = "启用配额")
    @ApiOperation("启用配额")
    @PostMapping("/enable/{quotaId}")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            quotaService.enable(quotaId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (ServiceException ex) {
            log.error("启用配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用配额异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停用配额
     */
    @Operation(summary = "停用配额")
    @ApiOperation("停用配额")
    @PostMapping("/disable/{quotaId}")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            quotaService.disable(quotaId);
            result.setCode(1);
            result.setMsg("停用成功");
        } catch (ServiceException ex) {
            log.error("停用配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停用配额异常", e);
            result.setCode(0);
            result.setMsg("停用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分配配额
     */
    @Operation(summary = "分配配额")
    @ApiOperation("分配配额")
    @PostMapping("/allocate")
    public MyJsonBean<Map<String, Object>> allocate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> allocateResult = quotaService.allocate(params);
            result.setCode(1);
            result.setMsg("分配成功");
            result.setData(allocateResult);
        } catch (ServiceException ex) {
            log.error("分配配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("分配配额异常", e);
            result.setCode(0);
            result.setMsg("分配失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 调整配额
     */
    @Operation(summary = "调整配额")
    @ApiOperation("调整配额")
    @PostMapping("/adjust/{quotaId}")
    public MyJsonBean<Void> adjust(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetQuota before = quotaService.getById(quotaId);
            params.put("quotaId", quotaId);
            quotaService.adjust(params);
            BudgetQuota after = quotaService.getById(quotaId);
            recordHistory(quotaId, "ADJUST",
                    "调整配额：" + (before != null ? before.getQuotaName() : quotaId),
                    after != null ? after.getQuotaAmount() : null,
                    before != null ? String.valueOf(before.getQuotaAmount()) : null,
                    after != null ? String.valueOf(after.getQuotaAmount()) : null);
            result.setCode(1);
            result.setMsg("调整成功");
        } catch (ServiceException ex) {
            log.error("调整配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("调整配额异常", e);
            result.setCode(0);
            result.setMsg("调整失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量调整配额
     */
    @Operation(summary = "批量调整配额")
    @ApiOperation("批量调整配额")
    @PostMapping("/batch/adjust")
    public MyJsonBean<Map<String, Object>> batchAdjust(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = quotaService.batchAdjust(params);
            result.setCode(1);
            result.setMsg("批量调整完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量调整配额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量调整配额异常", e);
            result.setCode(0);
            result.setMsg("批量调整失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出配额数据
     */
    @Operation(summary = "导出配额数据")
    @ApiOperation("导出配额数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetQuota> dataList = quotaService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算配额数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算配额", BudgetQuota.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算配额数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算配额数据异常", e);
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

    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgList = organizationMapper.selectList(wrapper);
            List<Map<String, Object>> orgs = new ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", org.getOrganizationId());
                map.put("name", org.getOrganizationName());
                map.put("code", org.getOrganizationCode());
                orgs.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(orgs);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetAccount> accList = accountMapper.selectList(wrapper);
            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", acc.getAccountId());
                map.put("name", acc.getAccountName());
                map.put("code", acc.getAccountCode());
                accounts.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "计算配额分配")
    @ApiOperation("计算配额分配")
    @PostMapping("/calculate-allocation")
    public MyJsonBean<Map<String, Object>> calculateAllocation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String quotaId = params.get("quotaId") != null ? params.get("quotaId").toString() : null;
            Map<String, Object> allocation = new HashMap<>();
            if (quotaId != null) {
                BudgetQuota quota = quotaService.getById(quotaId);
                if (quota != null) {
                    allocation.put("totalQuota", quota.getQuotaAmount() != null ? quota.getQuotaAmount() : 0);
                    allocation.put("allocatedAmount", quota.getAllocatedAmount() != null ? quota.getAllocatedAmount() : 0);
                    allocation.put("remainingAmount", quota.getAvailableAmount() != null ? quota.getAvailableAmount() : 0);
                    double totalVal = quota.getQuotaAmount() != null ? quota.getQuotaAmount().doubleValue() : 0;
                    double allocVal = quota.getAllocatedAmount() != null ? quota.getAllocatedAmount().doubleValue() : 0;
                    double rate = totalVal > 0 ? (allocVal / totalVal) * 100 : 0;
                    allocation.put("allocationRate", Math.round(rate * 100.0) / 100.0);
                }
            } else {
                QueryWrapper<BudgetQuota> qw = new QueryWrapper<>();
                qw.eq("DEL_FLAG", 0);
                List<BudgetQuota> quotas = quotaService.list(qw);
                double totalQuota = 0, totalAllocated = 0, totalAvailable = 0;
                for (BudgetQuota q : quotas) {
                    if (q.getQuotaAmount() != null) totalQuota += q.getQuotaAmount().doubleValue();
                    if (q.getAllocatedAmount() != null) totalAllocated += q.getAllocatedAmount().doubleValue();
                    if (q.getAvailableAmount() != null) totalAvailable += q.getAvailableAmount().doubleValue();
                }
                allocation.put("totalQuota", totalQuota);
                allocation.put("allocatedAmount", totalAllocated);
                allocation.put("remainingAmount", totalAvailable);
                double rate = totalQuota > 0 ? (totalAllocated / totalQuota) * 100 : 0;
                allocation.put("allocationRate", Math.round(rate * 100.0) / 100.0);
            }
            result.setCode(1);
            result.setMsg("计算成功");
            result.setData(allocation);
        } catch (Exception e) {
            log.error("计算配额分配异常", e);
            result.setCode(0);
            result.setMsg("计算失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个配额数据")
    @ApiOperation("导出单个配额数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "配额ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=quota_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,配额名称,配额金额,状态\n");
            BudgetQuota quota = quotaService.getById(id);
            if (quota != null) {
                sb.append(quota.getQuotaId() != null ? quota.getQuotaId() : id).append(",");
                sb.append(quota.getQuotaName() != null ? quota.getQuotaName() : "").append(",");
                sb.append(quota.getQuotaAmount() != null ? quota.getQuotaAmount() : "").append(",");
                sb.append(quota.getQuotaStatus() != null ? quota.getQuotaStatus() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个配额数据异常", e);
        }
    }

    /**
     * 获取配额统计信息
     */
    @Operation(summary = "获取配额统计信息")
    @ApiOperation("获取配额统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = quotaService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取配额统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 转移配额
     */
    @Operation(summary = "转移配额")
    @ApiOperation("转移配额")
    @PostMapping("/transfer")
    public MyJsonBean<Void> transfer(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String quotaId = params.get("quotaId") != null ? params.get("quotaId").toString() : null;
            Object transferAmountObj = params.get("transferAmount");
            String targetOrganizationId = params.get("targetOrganizationId") != null ? params.get("targetOrganizationId").toString() : null;

            if (quotaId == null || transferAmountObj == null) {
                result.setCode(0);
                result.setMsg("配额ID和转移金额不能为空");
                return result;
            }

            BudgetQuota sourceQuota = quotaService.getById(quotaId);
            if (sourceQuota == null) {
                result.setCode(0);
                result.setMsg("源配额不存在");
                return result;
            }

            java.math.BigDecimal transferAmount = new java.math.BigDecimal(transferAmountObj.toString());
            if (sourceQuota.getAvailableAmount() != null && sourceQuota.getAvailableAmount().compareTo(transferAmount) < 0) {
                result.setCode(0);
                result.setMsg("转移金额不能超过可用余额");
                return result;
            }

            // 扣减源配额可用金额
            String beforeAvailable = String.valueOf(sourceQuota.getAvailableAmount());
            sourceQuota.setAvailableAmount(sourceQuota.getAvailableAmount().subtract(transferAmount));
            quotaService.update(sourceQuota);
            recordHistory(quotaId, "TRANSFER",
                    "转移配额至组织：" + targetOrganizationId + "，金额：" + transferAmount,
                    transferAmount, beforeAvailable, String.valueOf(sourceQuota.getAvailableAmount()));

            result.setCode(1);
            result.setMsg("转移成功");
            log.info("配额转移成功，源配额: {}, 转移金额: {}, 目标组织: {}", quotaId, transferAmount, targetOrganizationId);
        } catch (Exception e) {
            log.error("转移配额异常", e);
            result.setCode(0);
            result.setMsg("转移失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "查询配额操作历史")
    @ApiOperation("查询配额操作历史")
    @GetMapping("/history/{quotaId}")
    public MyJsonBean<List<BudgetQuotaHistory>> getHistory(
            @ApiParam(value = "配额ID", required = true) @PathVariable String quotaId) {
        MyJsonBean<List<BudgetQuotaHistory>> result = new MyJsonBean<>();
        try {
            List<BudgetQuotaHistory> historyList = quotaHistoryMapper.selectByQuotaId(quotaId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(historyList);
        } catch (Exception e) {
            log.error("查询配额操作历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private void recordHistory(String quotaId, String operationType, String operationDesc,
                               java.math.BigDecimal amount, String beforeValue, String afterValue) {
        try {
            BudgetQuotaHistory history = new BudgetQuotaHistory();
            history.setQuotaId(quotaId);
            history.setOperationType(operationType);
            history.setOperationDesc(operationDesc);
            history.setAmount(amount);
            history.setBeforeValue(beforeValue);
            history.setAfterValue(afterValue);
            history.setOperator("admin");
            history.setOperateTime(new java.util.Date());
            history.setCreateTime(new java.util.Date());
            quotaHistoryMapper.insert(history);
        } catch (Exception e) {
            log.warn("记录配额操作历史失败: {}", e.getMessage());
        }
    }
}