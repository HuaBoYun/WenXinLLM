package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetLimit;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.oracle.entity.budget.BudgetLimitHistory;
import com.management.accountant.service.BudgetLimitService;
import com.management.accountant.service.BudgetLimitHistoryService;
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
 * 预算限额Controller
 * 
 * @description 预算限额管理接口，支持单次限额、累计限额、周期限额等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算限额"})
@RequestMapping(value = "/accountant/budget/limit")
@Slf4j
public class BudgetLimitController {

    @Resource
    private BudgetLimitService limitService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    @Resource
    private BudgetLimitHistoryService limitHistoryService;

    /**
     * 创建预算限额
     */
    @Operation(summary = "创建预算限额")
    @ApiOperation("创建预算限额")
    @PostMapping("/create")
    public MyJsonBean<BudgetLimit> create(@RequestBody @Validated BudgetLimit limit) {
        MyJsonBean<BudgetLimit> result = new MyJsonBean<>();
        try {
            BudgetLimit created = limitService.create(limit);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
            // 记录历史
            try { limitHistoryService.recordHistory(created.getLimitId(), "CREATE", "创建预算限额: " + created.getLimitName(), null, created.getLimitAmount() != null ? created.getLimitAmount().toString() : null, null); } catch (Exception ex) { log.warn("记录创建历史失败", ex); }
        } catch (ServiceException ex) {
            log.error("创建预算限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预算限额异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询限额详情
     */
    @Operation(summary = "查询限额详情")
    @ApiOperation("查询限额详情")
    @GetMapping("/detail/{limitId}")
    public MyJsonBean<BudgetLimit> getDetail(
            @ApiParam(value = "限额ID", required = true) @PathVariable String limitId) {
        MyJsonBean<BudgetLimit> result = new MyJsonBean<>();
        try {
            BudgetLimit limit = limitService.getById(limitId);
            if (limit != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(limit);
            } else {
                result.setCode(0);
                result.setMsg("预算限额不存在");
            }
        } catch (Exception e) {
            log.error("查询限额详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预算限额
     */
    @Operation(summary = "更新预算限额")
    @ApiOperation("更新预算限额")
    @PutMapping("/update/{limitId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "限额ID", required = true) @PathVariable String limitId,
            @RequestBody @Validated BudgetLimit limit) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            limit.setLimitId(limitId);
            BudgetLimit oldLimit = limitService.getById(limitId);
            limitService.update(limit);
            result.setCode(1);
            result.setMsg("更新成功");
            // 记录历史
            try { limitHistoryService.recordHistory(limitId, "UPDATE", "更新预算限额", oldLimit != null && oldLimit.getLimitAmount() != null ? oldLimit.getLimitAmount().toString() : null, limit.getLimitAmount() != null ? limit.getLimitAmount().toString() : null, null); } catch (Exception ex) { log.warn("记录更新历史失败", ex); }
        } catch (ServiceException ex) {
            log.error("更新预算限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预算限额异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预算限额
     */
    @Operation(summary = "删除预算限额")
    @ApiOperation("删除预算限额")
    @DeleteMapping("/delete/{limitId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "限额ID", required = true) @PathVariable String limitId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            limitService.delete(limitId);
            result.setCode(1);
            result.setMsg("删除成功");
            try { limitHistoryService.recordHistory(limitId, "DELETE", "删除预算限额", null, null, null); } catch (Exception ex) { log.warn("记录删除历史失败", ex); }
        } catch (ServiceException ex) {
            log.error("删除预算限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预算限额异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询限额列表
     */
    @Operation(summary = "分页查询限额列表")
    @ApiOperation("分页查询限额列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetLimit>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetLimit>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetLimit> pageResult = limitService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询限额列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用限额
     */
    @Operation(summary = "启用限额")
    @ApiOperation("启用限额")
    @PostMapping("/enable/{limitId}")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "限额ID", required = true) @PathVariable String limitId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            limitService.enable(limitId);
            result.setCode(1);
            result.setMsg("启用成功");
            try { limitHistoryService.recordHistory(limitId, "ENABLE", "启用限额", "DISABLED", "NORMAL", null); } catch (Exception ex) { log.warn("记录启用历史失败", ex); }
        } catch (ServiceException ex) {
            log.error("启用限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用限额异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停用限额
     */
    @Operation(summary = "停用限额")
    @ApiOperation("停用限额")
    @PostMapping("/disable/{limitId}")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "限额ID", required = true) @PathVariable String limitId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            limitService.disable(limitId);
            result.setCode(1);
            result.setMsg("停用成功");
            try { limitHistoryService.recordHistory(limitId, "DISABLE", "停用限额", "NORMAL", "DISABLED", null); } catch (Exception ex) { log.warn("记录停用历史失败", ex); }
        } catch (ServiceException ex) {
            log.error("停用限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停用限额异常", e);
            result.setCode(0);
            result.setMsg("停用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 检查限额
     */
    @Operation(summary = "检查限额")
    @ApiOperation("检查限额")
    @PostMapping("/check")
    public MyJsonBean<Map<String, Object>> checkLimit(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> checkResult = limitService.checkLimit(params);
            result.setCode(1);
            result.setMsg("检查完成");
            result.setData(checkResult);
        } catch (ServiceException ex) {
            log.error("检查限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("检查限额异常", e);
            result.setCode(0);
            result.setMsg("检查失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量启用限额
     */
    @Operation(summary = "批量启用限额")
    @ApiOperation("批量启用限额")
    @PostMapping("/batch/enable")
    public MyJsonBean<Map<String, Object>> batchEnable(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = limitService.batchEnable(params);
            result.setCode(1);
            result.setMsg("批量启用完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量启用限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量启用限额异常", e);
            result.setCode(0);
            result.setMsg("批量启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量停用限额
     */
    @Operation(summary = "批量停用限额")
    @ApiOperation("批量停用限额")
    @PostMapping("/batch/disable")
    public MyJsonBean<Map<String, Object>> batchDisable(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = limitService.batchDisable(params);
            result.setCode(1);
            result.setMsg("批量停用完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量停用限额失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量停用限额异常", e);
            result.setCode(0);
            result.setMsg("批量停用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出限额数据
     */
    @Operation(summary = "导出限额数据")
    @ApiOperation("导出限额数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetLimit> dataList = limitService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算限额数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算限额", BudgetLimit.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算限额数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算限额数据异常", e);
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
            wrapper.eq("DEL_FLAG", 0);
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
            wrapper.eq("DEL_FLAG", 0);
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

    @Operation(summary = "计算使用量")
    @ApiOperation("计算使用量")
    @PostMapping("/calculate-usage")
    public MyJsonBean<Map<String, Object>> calculateUsage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String limitId = params.get("limitId") != null ? params.get("limitId").toString() : null;
            Map<String, Object> usage = new HashMap<>();
            if (limitId != null) {
                BudgetLimit limit = limitService.getById(limitId);
                if (limit != null) {
                    usage.put("totalBudget", limit.getLimitAmount() != null ? limit.getLimitAmount() : 0);
                    usage.put("usedAmount", limit.getUsedAmount() != null ? limit.getUsedAmount() : 0);
                    usage.put("remainingAmount", limit.getAvailableAmount() != null ? limit.getAvailableAmount() : 0);
                    java.math.BigDecimal total = limit.getLimitAmount() != null ? limit.getLimitAmount() : java.math.BigDecimal.ZERO;
                    java.math.BigDecimal used = limit.getUsedAmount() != null ? limit.getUsedAmount() : java.math.BigDecimal.ZERO;
                    double usageRate = total.compareTo(java.math.BigDecimal.ZERO) > 0
                            ? used.multiply(new java.math.BigDecimal(100)).divide(total, 2, java.math.RoundingMode.HALF_UP).doubleValue() : 0;
                    usage.put("usageRate", usageRate);
                }
            } else {
                // 汇总所有限额
                QueryWrapper<BudgetLimit> qw = new QueryWrapper<>();
                qw.eq("DEL_FLAG", 0);
                List<BudgetLimit> limits = limitService.list(qw);
                java.math.BigDecimal totalBudget = java.math.BigDecimal.ZERO;
                java.math.BigDecimal totalUsed = java.math.BigDecimal.ZERO;
                for (BudgetLimit l : limits) {
                    if (l.getLimitAmount() != null) totalBudget = totalBudget.add(l.getLimitAmount());
                    if (l.getUsedAmount() != null) totalUsed = totalUsed.add(l.getUsedAmount());
                }
                usage.put("totalBudget", totalBudget);
                usage.put("usedAmount", totalUsed);
                usage.put("remainingAmount", totalBudget.subtract(totalUsed));
                double usageRate = totalBudget.compareTo(java.math.BigDecimal.ZERO) > 0
                        ? totalUsed.multiply(new java.math.BigDecimal(100)).divide(totalBudget, 2, java.math.RoundingMode.HALF_UP).doubleValue() : 0;
                usage.put("usageRate", usageRate);
            }
            result.setCode(1);
            result.setMsg("计算成功");
            result.setData(usage);
        } catch (Exception e) {
            log.error("计算使用量异常", e);
            result.setCode(0);
            result.setMsg("计算失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量调整限额")
    @ApiOperation("批量调整限额")
    @PostMapping("/batch/adjust")
    public MyJsonBean<Map<String, Object>> batchAdjust(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            int totalCount = ids != null ? ids.size() : 0;
            int successCount = 0;
            if (ids != null) {
                for (String id : ids) {
                    try {
                        BudgetLimit limit = limitService.getById(id);
                        if (limit != null) {
                            limit.setUpdateTime(new java.util.Date());
                            limitService.updateById(limit);
                            successCount++;
                        }
                    } catch (Exception ex) {
                        log.warn("调整限额失败, id={}", id, ex);
                    }
                }
            }
            Map<String, Object> adjustResult = new HashMap<>();
            adjustResult.put("totalCount", totalCount);
            adjustResult.put("successCount", successCount);
            result.setCode(1);
            result.setMsg("批量调整完成，成功" + successCount + "条");
            result.setData(adjustResult);
        } catch (Exception e) {
            log.error("批量调整限额异常", e);
            result.setCode(0);
            result.setMsg("批量调整失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "冻结限额")
    @ApiOperation("冻结限额")
    @PostMapping("/freeze/{id}")
    public MyJsonBean<Void> freezeLimit(
            @ApiParam(value = "限额ID", required = true) @PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetLimit limit = limitService.getById(id);
            if (limit == null) {
                result.setCode(0);
                result.setMsg("限额不存在");
                return result;
            }
            limit.setStatus("FROZEN");
            limit.setUpdateTime(new java.util.Date());
            String oldStatus = limit.getStatus();
            limitService.updateById(limit);
            result.setCode(1);
            result.setMsg("冻结成功");
            try { limitHistoryService.recordHistory(id, "FREEZE", "冻结限额", oldStatus, "FROZEN", null); } catch (Exception ex) { log.warn("记录冻结历史失败", ex); }
        } catch (Exception e) {
            log.error("冻结限额异常", e);
            result.setCode(0);
            result.setMsg("冻结失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个限额数据")
    @ApiOperation("导出单个限额数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "限额ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=limit_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,限额名称,限额金额,状态\n");
            BudgetLimit limit = limitService.getById(id);
            if (limit != null) {
                sb.append(limit.getLimitId() != null ? limit.getLimitId() : id).append(",");
                sb.append(limit.getLimitName() != null ? limit.getLimitName() : "").append(",");
                sb.append(limit.getLimitAmount() != null ? limit.getLimitAmount() : "").append(",");
                sb.append(limit.getStatus() != null ? limit.getStatus() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个限额数据异常", e);
        }
    }

    /**
     * 获取限额统计信息
     */
    @Operation(summary = "获取限额统计信息")
    @ApiOperation("获取限额统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = limitService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取限额统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询限额操作历史
     */
    @Operation(summary = "查询限额操作历史")
    @ApiOperation("查询限额操作历史")
    @GetMapping("/history/{limitId}")
    public MyJsonBean<List<BudgetLimitHistory>> getHistory(
            @ApiParam(value = "限额ID", required = true) @PathVariable String limitId) {
        MyJsonBean<List<BudgetLimitHistory>> result = new MyJsonBean<>();
        try {
            List<BudgetLimitHistory> historyList = limitHistoryService.getHistoryByLimitId(limitId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(historyList);
        } catch (Exception e) {
            log.error("查询限额操作历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}