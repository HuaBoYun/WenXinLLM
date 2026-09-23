package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountChange;
import com.global.treasurer.service.TblGtAccountChangeService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import com.global.treasurer.mapper.TblGtAccountChangeMapper;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 全球司库-账户变更管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/account-change")
@Api(tags = "账户变更管理")
public class TblGtAccountChangeController {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountChangeController.class);

    @Resource
    private TblGtAccountChangeService tblGtAccountChangeService;

    @Resource
    private UserProvider userProvider;

    @Resource
    private TblGtAccountChangeMapper tblGtAccountChangeMapper;

    /**
     * 验证用户权限（开发环境：直接放行）
     */
    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询账户变更列表
     */
    @PostMapping("/page")
    @ApiOperation("分页查询账户变更列表")
    public String getPageList(@FlexibleRequestBody Map<String, Object> params) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            // 优先使用前端传入的 orgId，否则取登录用户的机构
            BigDecimal orgId = null;
            if (params != null && params.get("orgId") != null) {
                orgId = new BigDecimal(params.get("orgId").toString());
            } else {
                orgId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null;
            }

            // 从参数中获取分页和查询条件（兼容 pageNo/pageSize 和 page/limit 两种命名）
            Integer pageNo = 1;
            if (params != null) {
                if (params.get("pageNo") != null) pageNo = Integer.parseInt(params.get("pageNo").toString());
                else if (params.get("page") != null) pageNo = Integer.parseInt(params.get("page").toString());
            }
            Integer pageSize = 10;
            if (params != null) {
                if (params.get("pageSize") != null) pageSize = Integer.parseInt(params.get("pageSize").toString());
                else if (params.get("limit") != null) pageSize = Integer.parseInt(params.get("limit").toString());
            }
            String accountName = params != null ? (String) params.get("accountName") : null;
            String changeType = params != null ? (String) params.get("changeType") : null;
            String applicationStatus = params != null ? (String) params.get("applicationStatus") : null;

            Page<TblGtAccountChange> page = new Page<>(pageNo, pageSize);
            IPage<TblGtAccountChange> result = tblGtAccountChangeService.getPageList(
                    page, accountName, changeType, applicationStatus, orgId
            );

            // 添加空值检查
            if (result == null) {
                log.warn("查询结果为null");
                Map<String, Object> emptyData = new HashMap<>();
                emptyData.put("tlist", new ArrayList<>());
                emptyData.put("totalRecord", 0);
                emptyData.put("pageNo", pageNo);
                emptyData.put("pageSize", pageSize);
                return JsonBean.success(emptyData);
            }

            // 统计各状态数量
            List<Map<String, Object>> statusCounts = tblGtAccountChangeMapper.selectStatusCount(orgId);
            long total = 0, pending = 0, approved = 0, rejected = 0;
            for (Map<String, Object> row : statusCounts) {
                String status = row.get("APPLICATIONSTATUS") != null ? row.get("APPLICATIONSTATUS").toString()
                        : (row.get("applicationStatus") != null ? row.get("applicationStatus").toString() : "");
                long cnt = row.get("CNT") != null ? Long.parseLong(row.get("CNT").toString())
                        : (row.get("cnt") != null ? Long.parseLong(row.get("cnt").toString()) : 0L);
                total += cnt;
                if ("PENDING".equals(status)) pending = cnt;
                else if ("APPROVED".equals(status)) approved = cnt;
                else if ("REJECTED".equals(status)) rejected = cnt;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());
            data.put("totalApplications", total);
            data.put("pendingApplications", pending);
            data.put("approvedApplications", approved);
            data.put("rejectedApplications", rejected);

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询账户变更列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询账户变更详情")
    public String getById(@ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountChange entity = tblGtAccountChangeService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询账户变更详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增账户变更
     */
    @PostMapping("/")
    @ApiOperation("新增账户变更")
    public String save(@FlexibleRequestBody TblGtAccountChange entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setCreateUser(loginStaff.getStaffid());
            // 优先使用前端传入的 orgId，否则取登录用户的机构
            if (entity.getOrgId() == null) {
                entity.setOrgId(loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null);
            }

            boolean success = tblGtAccountChangeService.saveAccountChange(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增账户变更失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新账户变更
     */
    @PutMapping("/")
    @ApiOperation("更新账户变更")
    public String update(@FlexibleRequestBody TblGtAccountChange entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();

            boolean success = tblGtAccountChangeService.updateAccountChange(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新账户变更失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除账户变更
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除账户变更")
    public String delete(@ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountChangeService.deleteAccountChange(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除账户变更失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批账户变更
     */
    @PutMapping("/{id}/approve")
    @ApiOperation("审批账户变更")
    public String approve(
            @ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "申请状态", required = true) @RequestParam String applicationStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal approverId = loginStaff.getStaffid();

            boolean success = tblGtAccountChangeService.approveAccountChange(
                    id, approverId, approvalOpinion, applicationStatus
            );

            return success ? JsonBean.success("审批成功") : new JsonBean(0, "审批失败", null).toJson();

        } catch (Exception e) {
            log.error("审批账户变更失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除账户变更
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除账户变更")
    public String batchDelete(@RequestParam List<BigDecimal> ids) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 批量删除：循环调用删除方法
            int successCount = 0;
            for (BigDecimal id : ids) {
                if (tblGtAccountChangeService.deleteAccountChange(id)) {
                    successCount++;
                }
            }
            boolean success = successCount > 0;
            return success ? JsonBean.success("批量删除成功") : new JsonBean(0, "批量删除失败", null).toJson();

        } catch (Exception e) {
            log.error("批量删除账户变更失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出账户变更数据
     */
    @GetMapping("/export")
    @ApiOperation("导出账户变更数据")
    public void export(@RequestParam(required = false) Map<String, Object> params,
                     javax.servlet.http.HttpServletResponse response) {
        try {
            if (!validateUser()) {
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal orgId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null;

            // 从params中提取查询参数
            String accountName = params != null ? (String) params.get("accountName") : null;
            String changeType = params != null ? (String) params.get("changeType") : null;
            String applicationStatus = params != null ? (String) params.get("applicationStatus") : null;

            // 创建分页对象，设置大页码以获取所有数据
            Page<TblGtAccountChange> page = new Page<>(1, Integer.MAX_VALUE);

            // 获取所有符合条件的数据
            com.baomidou.mybatisplus.core.metadata.IPage<TblGtAccountChange> pageResult =
                tblGtAccountChangeService.getPageList(page, accountName, changeType, applicationStatus, orgId);
            List<TblGtAccountChange> list = pageResult.getRecords();

            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("账户变更申请表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");

            // 导出Excel文件（简化版，实际应该使用Apache POI或EasyExcel）
            java.io.PrintWriter out = response.getWriter();
            out.println("申请编号\t账户号码\t账户名称\t变更类型\t申请状态\t申请人\t申请日期\t审批日期");
            for (TblGtAccountChange item : list) {
                out.println(
                    (item.getApplicationNo() != null ? item.getApplicationNo() : "") + "\t" +
                    (item.getAccountNumber() != null ? item.getAccountNumber() : "") + "\t" +
                    (item.getAccountName() != null ? item.getAccountName() : "") + "\t" +
                    (item.getChangeType() != null ? item.getChangeType() : "") + "\t" +
                    (item.getApplicationStatus() != null ? item.getApplicationStatus() : "") + "\t" +
                    (item.getApplicantName() != null ? item.getApplicantName() : "") + "\t" +
                    (item.getApplicationDate() != null ? item.getApplicationDate().toString() : "") + "\t" +
                    (item.getApprovalDate() != null ? item.getApprovalDate().toString() : "")
                );
            }
            out.flush();

        } catch (Exception e) {
            log.error("导出账户变更数据失败", e);
            try {
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入响应失败", ex);
            }
        }
    }
}
