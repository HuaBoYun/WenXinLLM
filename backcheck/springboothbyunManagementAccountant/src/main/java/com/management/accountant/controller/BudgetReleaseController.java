package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetRelease;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetReleaseService;
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

import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 预算释放Controller
 * 
 * @description 预算释放管理接口，支持释放申请、审批、执行等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算释放"})
@RequestMapping(value = "/accountant/budget/release")
@Slf4j
public class BudgetReleaseController {

    @Resource
    private BudgetReleaseService releaseService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    /**
     * 创建释放申请
     * 使用 Map 接收避免前端多余字段导致 JSON 解析报错
     */
    @Operation(summary = "创建释放申请")
    @ApiOperation("创建释放申请")
    @PostMapping("/create")
    public MyJsonBean<BudgetRelease> create(@RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetRelease> result = new MyJsonBean<>();
        try {
            BudgetRelease release = buildReleaseFromMap(params);
            BudgetRelease created = releaseService.create(release);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建释放申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建释放申请异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 从前端 Map 参数构建 BudgetRelease 实体
     * 负责字段名映射和类型转换，前端多余字段直接忽略。
     * 字段说明：
     *  - releaseTitle      → releaseTitle（DB: RELEASE_TITLE）& releaseName（DB: RELEASE_NAME）同时赋值
     *  - organizationId    → organizationId（DB: ORGANIZATION_ID）
     *  - budgetAccountId   → budgetAccountId（DB: BUDGET_ACCOUNT_ID）
     *  - approver          → approveBy（DB: APPROVE_BY）
     *  - plannedReleaseDate→ plannedReleaseDate（DB: PLANNED_RELEASE_DATE）
     */
    private BudgetRelease buildReleaseFromMap(Map<String, Object> p) {
        BudgetRelease r = new BudgetRelease();
        String releaseTitle = getString(p, "releaseTitle");
        r.setReleaseTitle(releaseTitle);
        r.setReleaseCode(getString(p, "releaseCode"));
        // RELEASE_NAME 与 RELEASE_TITLE 保持一致
        r.setReleaseName(releaseTitle);
        r.setReleaseType(getString(p, "releaseType"));
        r.setReleaseReason(getString(p, "releaseReason"));
        r.setReleaseDescription(getString(p, "releaseDescription"));
        r.setOrganizationId(getString(p, "organizationId"));
        r.setBudgetAccountId(getString(p, "budgetAccountId"));
        // 前端 approver（staffId）对应数据库 APPROVE_BY
        r.setApproveBy(getString(p, "approver"));
        if (p.get("releaseAmount") != null) {
            r.setReleaseAmount(new BigDecimal(p.get("releaseAmount").toString()));
        }
        r.setApplyDate(parseDate(p, "applyDate"));
        r.setPlannedReleaseDate(parseDate(p, "plannedReleaseDate"));
        return r;
    }

    private String getString(Map<String, Object> p, String key) {
        Object v = p.get(key);
        return v != null ? v.toString() : null;
    }

    private java.util.Date parseDate(Map<String, Object> p, String key) {
        Object v = p.get(key);
        if (v == null) return null;
        String s = v.toString();
        if (!StringUtils.hasText(s)) return null;
        try {
            // ISO 8601 格式：2026-04-07T16:00:00.000Z
            java.time.Instant instant = java.time.Instant.parse(s);
            return java.util.Date.from(instant);
        } catch (Exception e1) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(s);
            } catch (Exception e2) {
                log.warn("日期解析失败，字段={}, 值={}", key, s);
                return null;
            }
        }
    }

    /**
     * 查询释放详情
     */
    @Operation(summary = "查询释放详情")
    @ApiOperation("查询释放详情")
    @GetMapping("/detail/{releaseId}")
    public MyJsonBean<BudgetRelease> getDetail(
            @ApiParam(value = "释放ID", required = true) @PathVariable String releaseId) {
        MyJsonBean<BudgetRelease> result = new MyJsonBean<>();
        try {
            BudgetRelease release = releaseService.getById(releaseId);
            if (release != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(release);
            } else {
                result.setCode(0);
                result.setMsg("释放记录不存在");
            }
        } catch (Exception e) {
            log.error("查询释放详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新释放申请
     */
    @Operation(summary = "更新释放申请")
    @ApiOperation("更新释放申请")
    @PutMapping("/update/{releaseId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "释放ID", required = true) @PathVariable String releaseId,
            @RequestBody @Validated BudgetRelease release) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            release.setReleaseId(releaseId);
            releaseService.update(release);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新释放申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新释放申请异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除释放申请
     */
    @Operation(summary = "删除释放申请")
    @ApiOperation("删除释放申请")
    @DeleteMapping("/delete/{releaseId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "释放ID", required = true) @PathVariable String releaseId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            releaseService.delete(releaseId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除释放申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除释放申请异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询释放列表
     */
    @Operation(summary = "分页查询释放列表")
    @ApiOperation("分页查询释放列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetRelease>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetRelease>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetRelease> pageResult = releaseService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询释放列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批释放申请
     */
    @Operation(summary = "审批释放申请")
    @ApiOperation("审批释放申请")
    @PostMapping("/approve/{releaseId}")
    public MyJsonBean<Void> approve(
            @ApiParam(value = "释放ID", required = true) @PathVariable String releaseId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            params.put("releaseId", releaseId);
            releaseService.approve(params);
            result.setCode(1);
            result.setMsg("审批成功");
        } catch (ServiceException ex) {
            log.error("审批释放申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批释放申请异常", e);
            result.setCode(0);
            result.setMsg("审批失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行释放
     */
    @Operation(summary = "执行释放")
    @ApiOperation("执行释放")
    @PostMapping("/execute/{releaseId}")
    public MyJsonBean<Void> execute(
            @ApiParam(value = "释放ID", required = true) @PathVariable String releaseId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            releaseService.execute(releaseId);
            result.setCode(1);
            result.setMsg("执行成功");
        } catch (ServiceException ex) {
            log.error("执行释放失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行释放异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量释放
     */
    @Operation(summary = "批量释放")
    @ApiOperation("批量释放")
    @PostMapping("/batch/release")
    public MyJsonBean<Map<String, Object>> batchRelease(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = releaseService.batchRelease(params);
            result.setCode(1);
            result.setMsg("批量释放成功");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量释放失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量释放异常", e);
            result.setCode(0);
            result.setMsg("批量释放失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出释放数据
     */
    @Operation(summary = "导出释放数据")
    @ApiOperation("导出释放数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetRelease> dataList = releaseService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算释放数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算释放", BudgetRelease.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算释放数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算释放数据异常", e);
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

            // 构建 id -> node 映射
            Map<String, Map<String, Object>> nodeMap = new HashMap<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", org.getOrganizationId());
                map.put("label", org.getOrganizationName());
                map.put("children", new ArrayList<>());
                nodeMap.put(org.getOrganizationId(), map);
            }

            // 组装树形结构
            List<Map<String, Object>> roots = new ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> node = nodeMap.get(org.getOrganizationId());
                if (org.getParentId() != null && nodeMap.containsKey(org.getParentId())) {
                    Map<String, Object> parent = nodeMap.get(org.getParentId());
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
                    children.add(node);
                } else {
                    roots.add(node);
                }
            }

            // 移除空的 children 列表，避免 cascader 显示展开箭头
            removeEmptyChildren(roots);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(roots);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 递归移除空的 children 列表
     */
    @SuppressWarnings("unchecked")
    private void removeEmptyChildren(List<Map<String, Object>> nodes) {
        for (Map<String, Object> node : nodes) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
            if (children == null || children.isEmpty()) {
                node.remove("children");
            } else {
                removeEmptyChildren(children);
            }
        }
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
                map.put("value", acc.getAccountId());
                map.put("label", acc.getAccountName());
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

    @Operation(summary = "验证释放")
    @ApiOperation("验证释放")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateRelease(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String releaseId = params.get("releaseId") != null ? params.get("releaseId").toString() : null;
            Map<String, Object> validateResult = new HashMap<>();
            boolean valid = true;
            String message = "释放验证通过";
            if (releaseId != null) {
                BudgetRelease release = releaseService.getById(releaseId);
                if (release == null) {
                    valid = false;
                    message = "释放记录不存在";
                } else if ("COMPLETED".equals(release.getReleaseStatus())) {
                    valid = false;
                    message = "该释放已完成，不可重复操作";
                } else if (release.getReleaseAmount() == null || release.getReleaseAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                    valid = false;
                    message = "释放金额无效";
                }
            }
            validateResult.put("valid", valid);
            validateResult.put("message", message);
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证释放异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量审批释放")
    @ApiOperation("批量审批释放")
    @PostMapping("/batch/approve")
    public MyJsonBean<Map<String, Object>> batchApprove(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            int totalCount = ids != null ? ids.size() : 0;
            int successCount = 0;
            if (ids != null) {
                for (String id : ids) {
                    try {
                        BudgetRelease release = releaseService.getById(id);
                        if (release != null && "PENDING".equals(release.getReleaseStatus())) {
                            release.setReleaseStatus("APPROVED");
                            release.setApproveDate(new java.util.Date());
                            releaseService.updateById(release);
                            successCount++;
                        }
                    } catch (Exception ex) {
                        log.warn("审批释放失败, id={}", id, ex);
                    }
                }
            }
            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", totalCount);
            batchResult.put("successCount", successCount);
            result.setCode(1);
            result.setMsg("批量审批完成，成功" + successCount + "条");
            result.setData(batchResult);
        } catch (Exception e) {
            log.error("批量审批释放异常", e);
            result.setCode(0);
            result.setMsg("批量审批失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "驳回释放申请")
    @ApiOperation("驳回释放申请")
    @PostMapping("/reject/{releaseId}")
    public MyJsonBean<Void> reject(
            @ApiParam(value = "释放ID", required = true) @PathVariable String releaseId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetRelease release = releaseService.getById(releaseId);
            if (release == null) {
                result.setCode(0);
                result.setMsg("释放记录不存在");
                return result;
            }
            release.setReleaseStatus("REJECTED");
            String rejectReason = params.get("reason") != null ? params.get("reason").toString() : "";
            release.setApproveRemark(rejectReason);
            release.setApproveDate(new java.util.Date());
            releaseService.updateById(release);
            result.setCode(1);
            result.setMsg("驳回成功");
        } catch (Exception e) {
            log.error("驳回释放申请异常", e);
            result.setCode(0);
            result.setMsg("驳回失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个释放数据")
    @ApiOperation("导出单个释放数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "释放ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=release_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,释放名称,金额,状态\n");
            BudgetRelease release = releaseService.getById(id);
            if (release != null) {
                sb.append(release.getReleaseId() != null ? release.getReleaseId() : id).append(",");
                sb.append(release.getReleaseName() != null ? release.getReleaseName() : "").append(",");
                sb.append(release.getReleaseAmount() != null ? release.getReleaseAmount() : "").append(",");
                sb.append(release.getReleaseStatus() != null ? release.getReleaseStatus() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个释放数据异常", e);
        }
    }

    /**
     * 获取释放统计信息
     */
    @Operation(summary = "获取释放统计信息")
    @ApiOperation("获取释放统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = releaseService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取释放统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}