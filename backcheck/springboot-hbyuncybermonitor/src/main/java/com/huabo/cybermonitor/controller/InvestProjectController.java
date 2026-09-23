package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.GzctInvestProject;
import com.huabo.cybermonitor.mapper.GzctInvestProjectMapper;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资穿透式监管 - 投资项目台账控制器
 */
@Tag(name = "投资穿透式监管", description = "投资项目全生命周期监管")
@RestController
@RequestMapping({"/v1/supervision/investment/project", "/v1/supervision/investment/projects"})
@Slf4j
public class InvestProjectController {

    @Autowired
    private GzctInvestProjectMapper projectMapper;

    @Operation(summary = "分页查询投资项目列表")
    @PostMapping("/list")
    public R<PageResult<GzctInvestProject>> list(@RequestBody Map<String, Object> params) {
        try {
            // 兼容前端 pageNum 和 pageNumber 两种参数名
            int pageNum = 1;
            if (params.get("pageNum") != null) {
                pageNum = Integer.parseInt(params.get("pageNum").toString());
            } else if (params.get("pageNumber") != null) {
                pageNum = Integer.parseInt(params.get("pageNumber").toString());
            }
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            String projectName = params.get("projectName") != null ? params.get("projectName").toString() : null;
            if (StringUtils.isNotEmpty(projectName)) {
                wrapper.like(GzctInvestProject::getProjectName, projectName);
            }
            String companyName = params.get("companyName") != null ? params.get("companyName").toString() : null;
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(GzctInvestProject::getCompanyName, companyName);
            }
            String investType = params.get("investType") != null ? params.get("investType").toString().trim() : null;
            if (StringUtils.isNotEmpty(investType)) {
                // 同时支持中文简称、全称和英文代码，兼容不同数据集
                java.util.List<String> typeVariants = new java.util.ArrayList<>();
                typeVariants.add(investType);
                java.util.Map<String, String[]> investTypeMap = new java.util.HashMap<>();
                investTypeMap.put("直接", new String[]{"直接", "直接投资", "DIRECT"});
                investTypeMap.put("股权", new String[]{"股权", "股权投资", "EQUITY"});
                investTypeMap.put("并购", new String[]{"并购", "并购投资", "MIXED"});
                investTypeMap.put("基金", new String[]{"基金", "基金投资", "FUND"});
                investTypeMap.put("直接投资", new String[]{"直接", "直接投资", "DIRECT"});
                investTypeMap.put("股权投资", new String[]{"股权", "股权投资", "EQUITY"});
                investTypeMap.put("并购投资", new String[]{"并购", "并购投资", "MIXED"});
                investTypeMap.put("基金投资", new String[]{"基金", "基金投资", "FUND"});
                investTypeMap.put("DIRECT", new String[]{"直接", "直接投资", "DIRECT"});
                investTypeMap.put("EQUITY", new String[]{"股权", "股权投资", "EQUITY"});
                investTypeMap.put("MIXED", new String[]{"并购", "并购投资", "MIXED"});
                investTypeMap.put("FUND", new String[]{"基金", "基金投资", "FUND"});
                investTypeMap.put("DEBT", new String[]{"债权投资", "DEBT"});
                investTypeMap.put("OTHER", new String[]{"其他", "OTHER"});
                String[] variants = investTypeMap.get(investType);
                if (variants != null) {
                    typeVariants.clear();
                    java.util.Collections.addAll(typeVariants, variants);
                }
                wrapper.in(GzctInvestProject::getInvestType, typeVariants);
            }
            String projectStatus = params.get("projectStatus") != null ? params.get("projectStatus").toString() : null;
            if (StringUtils.isNotEmpty(projectStatus)) {
                wrapper.eq(GzctInvestProject::getProjectStatus, projectStatus);
            }
            String companyId = params.get("companyId") != null ? params.get("companyId").toString() : null;
            if (StringUtils.isNotEmpty(companyId)) {
                // 穿透查询：通过 ORG_ID 关联 TBL_ORGANIZATION 递归查当前公司及所有下级公司
                wrapper.inSql(GzctInvestProject::getOrgId,
                    "SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = '" + companyId + "' CONNECT BY PRIOR ORGID = FATHERORGID");
            }
            Object isMainBiz = params.get("isMainBiz");
            if (isMainBiz != null && StringUtils.isNotEmpty(isMainBiz.toString())) {
                String mainBizValue = isMainBiz.toString();
                if ("true".equalsIgnoreCase(mainBizValue)) mainBizValue = "Y";
                else if ("false".equalsIgnoreCase(mainBizValue)) mainBizValue = "N";
                wrapper.eq(GzctInvestProject::getIsMainBiz, mainBizValue);
            }
            // 投资金额范围
            String minAmount = params.get("minAmount") != null ? params.get("minAmount").toString().trim() : null;
            if (StringUtils.isNotEmpty(minAmount)) {
                try { wrapper.ge(GzctInvestProject::getInvestAmount, new java.math.BigDecimal(minAmount)); } catch (NumberFormatException ignored) {}
            }
            String maxAmount = params.get("maxAmount") != null ? params.get("maxAmount").toString().trim() : null;
            if (StringUtils.isNotEmpty(maxAmount)) {
                try { wrapper.le(GzctInvestProject::getInvestAmount, new java.math.BigDecimal(maxAmount)); } catch (NumberFormatException ignored) {}
            }
            // 申请时间范围
            String startDate = params.get("startDate") != null ? params.get("startDate").toString().trim() : null;
            if (StringUtils.isNotEmpty(startDate)) {
                wrapper.ge(GzctInvestProject::getCreateTime, java.time.LocalDate.parse(startDate).atStartOfDay());
            }
            String endDate = params.get("endDate") != null ? params.get("endDate").toString().trim() : null;
            if (StringUtils.isNotEmpty(endDate)) {
                wrapper.le(GzctInvestProject::getCreateTime, java.time.LocalDate.parse(endDate).atTime(23, 59, 59));
            }
            wrapper.orderByDesc(GzctInvestProject::getCreateTime);

            Page<GzctInvestProject> page = new Page<>(pageNum, pageSize);
            Page<GzctInvestProject> result = projectMapper.selectPage(page, wrapper);

            PageResult<GzctInvestProject> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(result.getRecords());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询投资项目列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询投资项目详情")
    @GetMapping("/{id}")
    public R<GzctInvestProject> detail(@Parameter(description = "项目ID") @PathVariable String id) {
        try {
            GzctInvestProject project = projectMapper.selectById(id);
            return R.success(project);
        } catch (Exception e) {
            log.error("查询投资项目详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询投资项目详情(POST)")
    @PostMapping("/detail")
    public R<GzctInvestProject> detailByPost(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            if (StringUtils.isEmpty(projectId)) {
                return R.fail("项目ID不能为空");
            }
            GzctInvestProject project = projectMapper.selectById(projectId);
            if (project == null) {
                return R.fail("项目不存在");
            }
            return R.success(project);
        } catch (Exception e) {
            log.error("查询投资项目详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除投资项目(POST)")
    @PostMapping("/delete")
    public R<Boolean> deleteByPost(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            if (StringUtils.isEmpty(projectId)) {
                return R.fail("项目ID不能为空");
            }
            int rows = projectMapper.deleteById(projectId);
            return rows > 0 ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除投资项目失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增投资项目")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) {
                params = new HashMap<>();
            }
            GzctInvestProject project = new GzctInvestProject();
            project.setProjectId(null); // auto-generate UUID
            project.setProjectName(params.get("projectName") != null ? params.get("projectName").toString() : null);
            project.setInvestType(params.get("investType") != null && !params.get("investType").toString().isEmpty() ? params.get("investType").toString() : "股权投资");
            // companyId: 如果前端没传，用companyName生成一个默认ID
            String companyId = params.get("companyId") != null && !params.get("companyId").toString().isEmpty() ? params.get("companyId").toString() : null;
            if (companyId == null) {
                companyId = "CMP" + System.currentTimeMillis();
            }
            project.setCompanyId(companyId);
            project.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : null);
            if (params.get("investAmount") != null && !params.get("investAmount").toString().isEmpty()) {
                project.setInvestAmount(new BigDecimal(params.get("investAmount").toString()));
            }
            project.setIsMainBiz(params.get("isMainBiz") != null && !params.get("isMainBiz").toString().isEmpty() ? params.get("isMainBiz").toString() : "N");
            project.setProjectStatus(params.get("projectStatus") != null && !params.get("projectStatus").toString().isEmpty() ? params.get("projectStatus").toString() : "PLANNING");
            if (params.get("expectedReturn") != null && !params.get("expectedReturn").toString().isEmpty()) {
                project.setExpectedReturn(new BigDecimal(params.get("expectedReturn").toString()));
            }
            if (params.get("actualReturn") != null && !params.get("actualReturn").toString().isEmpty()) {
                project.setActualReturn(new BigDecimal(params.get("actualReturn").toString()));
            }
            if (params.get("approvalDate") != null && !params.get("approvalDate").toString().isEmpty()) {
                project.setApprovalDate(LocalDate.parse(params.get("approvalDate").toString()));
            }
            project.setTargetCompany(params.get("targetCompany") != null ? params.get("targetCompany").toString() : null);
            project.setApprovalStatus(params.get("approvalStatus") != null && !params.get("approvalStatus").toString().isEmpty() ? params.get("approvalStatus").toString() : "PENDING");
            project.setRemark(params.get("remark") != null ? params.get("remark").toString() : null);
            project.setCreateTime(LocalDateTime.now());
            project.setUpdateTime(LocalDateTime.now());

            int rows = projectMapper.insert(project);
            return rows > 0 ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增投资项目失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "审批投资项目")
    @PostMapping("/approve")
    public R<Boolean> approve(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String projectId = params != null && params.get("projectId") != null ? params.get("projectId").toString() : null;
            if (StringUtils.isEmpty(projectId)) return R.fail("项目ID不能为空");
            GzctInvestProject project = projectMapper.selectById(projectId);
            if (project == null) return R.fail("项目不存在");
            String action = params.get("action") != null ? params.get("action").toString() : "APPROVED";
            project.setApprovalStatus(action);
            project.setApprovalDate(LocalDate.now());
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            return R.success(true);
        } catch (Exception e) {
            log.error("审批投资项目失败", e);
            return R.fail("审批失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量审批")
    @PostMapping("/batch/approve")
    public R<Boolean> batchApprove(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<String> projectIds = params != null ? (List<String>) params.get("projectIds") : null;
            if (projectIds == null || projectIds.isEmpty()) return R.fail("请选择项目");
            for (String id : projectIds) {
                GzctInvestProject project = projectMapper.selectById(id);
                if (project != null) {
                    project.setApprovalStatus("APPROVED");
                    project.setApprovalDate(LocalDate.now());
                    project.setUpdateTime(LocalDateTime.now());
                    projectMapper.updateById(project);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量审批失败", e);
            return R.fail("批量审批失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出投资项目数据")
    @PostMapping("/export")
    public void exportData(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            if (params != null) {
                String projectName = params.get("projectName") != null ? params.get("projectName").toString() : null;
                if (StringUtils.isNotEmpty(projectName)) wrapper.like(GzctInvestProject::getProjectName, projectName);
                String companyName = params.get("companyName") != null ? params.get("companyName").toString() : null;
                if (StringUtils.isNotEmpty(companyName)) wrapper.like(GzctInvestProject::getCompanyName, companyName);
                String projectStatus = params.get("projectStatus") != null ? params.get("projectStatus").toString() : null;
                if (StringUtils.isNotEmpty(projectStatus)) wrapper.eq(GzctInvestProject::getProjectStatus, projectStatus);
                String investType = params.get("investType") != null ? params.get("investType").toString().trim() : null;
                if (StringUtils.isNotEmpty(investType)) {
                    java.util.List<String> tv = new java.util.ArrayList<>();
                    tv.add(investType);
                    java.util.Map<String, String[]> itm = new java.util.HashMap<>();
                    itm.put("直接", new String[]{"直接", "直接投资", "DIRECT"});
                    itm.put("股权", new String[]{"股权", "股权投资", "EQUITY"});
                    itm.put("并购", new String[]{"并购", "并购投资", "MIXED"});
                    itm.put("基金", new String[]{"基金", "基金投资", "FUND"});
                    itm.put("直接投资", new String[]{"直接", "直接投资", "DIRECT"});
                    itm.put("股权投资", new String[]{"股权", "股权投资", "EQUITY"});
                    itm.put("并购投资", new String[]{"并购", "并购投资", "MIXED"});
                    itm.put("基金投资", new String[]{"基金", "基金投资", "FUND"});
                    itm.put("DIRECT", new String[]{"直接", "直接投资", "DIRECT"});
                    itm.put("EQUITY", new String[]{"股权", "股权投资", "EQUITY"});
                    itm.put("MIXED", new String[]{"并购", "并购投资", "MIXED"});
                    itm.put("FUND", new String[]{"基金", "基金投资", "FUND"});
                    itm.put("DEBT", new String[]{"债权投资", "DEBT"});
                    itm.put("OTHER", new String[]{"其他", "OTHER"});
                    String[] v = itm.get(investType);
                    if (v != null) { tv.clear(); java.util.Collections.addAll(tv, v); }
                    wrapper.in(GzctInvestProject::getInvestType, tv);
                }
            }
            wrapper.orderByDesc(GzctInvestProject::getCreateTime);
            List<GzctInvestProject> list = projectMapper.selectList(wrapper);

            // 使用项目 ExcelUtil 工具类（HSSFWorkbook，.xls 格式）
            String[] headers = {"项目名称", "投资企业", "被投资企业", "投资金额(万元)", "投资类型",
                    "项目状态", "是否主业", "审批日期", "预期收益率(%)", "实际收益率(%)",
                    "审批状态", "备注", "创建时间"};
            ExcelUtil excelUtil = new ExcelUtil("投资决策项目", headers);

            for (int i = 0; i < list.size(); i++) {
                GzctInvestProject p = list.get(i);
                Object[] rowData = {
                    p.getProjectName() != null ? p.getProjectName() : "",
                    p.getCompanyName() != null ? p.getCompanyName() : "",
                    p.getTargetCompany() != null ? p.getTargetCompany() : "",
                    p.getInvestAmount() != null ? p.getInvestAmount().toPlainString() : "0",
                    p.getInvestType() != null ? p.getInvestType() : "",
                    p.getProjectStatus() != null ? p.getProjectStatus() : "",
                    "Y".equals(p.getIsMainBiz()) ? "是" : "否",
                    p.getApprovalDate() != null ? p.getApprovalDate().toString() : "",
                    p.getExpectedReturn() != null ? p.getExpectedReturn().toPlainString() : "0",
                    p.getActualReturn() != null ? p.getActualReturn().toPlainString() : "0",
                    p.getApprovalStatus() != null ? p.getApprovalStatus() : "",
                    p.getRemark() != null ? p.getRemark() : "",
                    p.getCreateTime() != null ? p.getCreateTime().toString() : ""
                };
                excelUtil.addRow(i + 1, rowData);
            }

            String fileName = "投资决策项目数据_" + System.currentTimeMillis() + ".xls";
            excelUtil.exportExcel(response, fileName);
        } catch (Exception e) {
            log.error("导出投资项目数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "审批历史")
    @PostMapping("/approval/history")
    public R<List<Map<String, Object>>> approvalHistory(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String projectId = params != null && params.get("projectId") != null ? params.get("projectId").toString() : null;
            List<Map<String, Object>> history = new java.util.ArrayList<>();
            if (projectId != null) {
                GzctInvestProject project = projectMapper.selectById(projectId);
                if (project != null) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("time", project.getCreateTime() != null ? project.getCreateTime().toString() : "");
                    entry.put("action", "项目创建");
                    entry.put("status", "CREATED");
                    entry.put("operator", "system");
                    history.add(entry);
                    if (project.getApprovalDate() != null) {
                        Map<String, Object> approveEntry = new HashMap<>();
                        approveEntry.put("time", project.getApprovalDate().toString());
                        approveEntry.put("action", "APPROVED".equals(project.getApprovalStatus()) ? "审批通过" : "审批拒绝");
                        approveEntry.put("status", project.getApprovalStatus());
                        approveEntry.put("operator", "admin");
                        history.add(approveEntry);
                    }
                }
            }
            return R.success(history);
        } catch (Exception e) {
            log.error("查询审批历史失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新投资项目")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody GzctInvestProject project) {
        try {
            if (StringUtils.isEmpty(project.getProjectId())) {
                return R.fail("项目ID不能为空");
            }
            project.setUpdateTime(LocalDateTime.now());
            int rows = projectMapper.updateById(project);
            return rows > 0 ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新投资项目失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除投资项目")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Parameter(description = "项目ID") @PathVariable String id) {
        try {
            int rows = projectMapper.deleteById(id);
            return rows > 0 ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除投资项目失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除投资项目")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的项目");
            }
            int rows = projectMapper.deleteBatchIds(ids);
            return rows > 0 ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除投资项目失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取投资统计数据")
    @RequestMapping(value = "/statistics", method = {RequestMethod.GET, RequestMethod.POST})
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId, @RequestBody(required = false) Map<String, Object> params) {
        try {
            // 兼容GET参数和POST body
            if (companyId == null && params != null && params.get("companyId") != null) {
                companyId = params.get("companyId").toString();
            }
            final String orgId = companyId;
            Map<String, Object> stats = new HashMap<>();
            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            // 穿透查询：通过 ORG_ID 关联 TBL_ORGANIZATION 递归查当前公司及所有下级公司
            if (StringUtils.isNotEmpty(orgId)) {
                wrapper.inSql(GzctInvestProject::getOrgId,
                    "SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = '" + orgId + "' CONNECT BY PRIOR ORGID = FATHERORGID");
            }
            List<GzctInvestProject> projects = projectMapper.selectList(wrapper);

            stats.put("totalProjects", projects.size());
            stats.put("pendingApproval", projects.stream().filter(p -> "PLANNING".equals(p.getProjectStatus()) || "PENDING".equals(p.getApprovalStatus())).count());
            stats.put("approvedProjects", projects.stream().filter(p -> "APPROVED".equals(p.getApprovalStatus())).count());
            long nonMainBiz = projects.stream().filter(p -> "N".equals(p.getIsMainBiz())).count();
            stats.put("riskProjects", nonMainBiz);
            stats.put("highRiskCount", nonMainBiz);
            // 合规率 = 已审批且主业投资 / 总项目数 × 100
            long approvedMainBiz = projects.stream().filter(p -> "APPROVED".equals(p.getApprovalStatus()) && "Y".equals(p.getIsMainBiz())).count();
            stats.put("complianceRate", projects.size() > 0 ? Math.round(approvedMainBiz * 1000.0 / projects.size()) / 10.0 : 0);

            BigDecimal totalAmount = projects.stream()
                .map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalInvestAmount", totalAmount);

            long returnCount = projects.stream().filter(p -> p.getActualReturn() != null).count();
            BigDecimal avgReturn = BigDecimal.ZERO;
            if (returnCount > 0) {
                BigDecimal totalReturn = projects.stream()
                    .filter(p -> p.getActualReturn() != null)
                    .map(GzctInvestProject::getActualReturn)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                avgReturn = totalReturn.divide(BigDecimal.valueOf(returnCount), 2, BigDecimal.ROUND_HALF_UP);
            }
            stats.put("avgReturnRate", avgReturn);

            return R.success(stats);
        } catch (Exception e) {
            log.error("获取投资统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }
}

