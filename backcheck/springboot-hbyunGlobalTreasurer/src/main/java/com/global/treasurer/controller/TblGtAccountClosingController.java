package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountClosing;
import com.global.treasurer.service.TblGtAccountClosingService;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 * 全球司库-销户申请管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/account-closing")
@Api(tags = "销户申请管理")
public class TblGtAccountClosingController {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountClosingController.class);

    @Resource
    private TblGtAccountClosingService tblGtAccountClosingService;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限（开发环境：直接放行）
     */
    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询销户申请列表（支持GET和POST请求）
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询销户申请列表")
    public String getPageList(
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @ApiParam(value = "申请编号") @RequestParam(value = "applicationNo", required = false) String applicationNo,
            @ApiParam(value = "账户号码") @RequestParam(value = "accountNumber", required = false) String accountNumber,
            @ApiParam(value = "申请状态") @RequestParam(value = "applicationStatus", required = false) String applicationStatus) {

        try {
            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal orgId = null;
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                orgId = loginStaff.getCurrentOrg().getOrgid();
            }

            Page<TblGtAccountClosing> page = new Page<>(pageNo, pageSize);
            IPage<TblGtAccountClosing> result = tblGtAccountClosingService.getPageList(
                    page, applicationNo, accountNumber, applicationStatus, orgId
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

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询销户申请列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询销户申请详情")
    public String getById(@ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountClosing entity = tblGtAccountClosingService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询销户申请详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增销户申请
     */
    @PostMapping
    @ApiOperation("新增销户申请")
    public String save(@FlexibleRequestBody TblGtAccountClosing entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setCreateUser(loginStaff.getStaffid());
            entity.setUpdateUser(loginStaff.getStaffid());
            entity.setOrgId(loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null);

            boolean success = tblGtAccountClosingService.saveAccountClosing(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增销户申请失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新销户申请
     */
    @PutMapping
    @ApiOperation("更新销户申请")
    public String update(@FlexibleRequestBody TblGtAccountClosing entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setUpdateUser(loginStaff.getStaffid());

            boolean success = tblGtAccountClosingService.updateAccountClosing(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新销户申请失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除销户申请
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除销户申请")
    public String delete(@ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountClosingService.deleteAccountClosing(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除销户申请失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批销户申请
     */
    @PutMapping("/{id}/approve")
    @ApiOperation("审批销户申请")
    public String approve(
            @ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "审批状态(APPROVED/REJECTED)") @RequestParam(required = false) String applicationStatus,
            @ApiParam(value = "是否批准(true/false，与applicationStatus二选一)") @RequestParam(required = false) Boolean approved) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 兼容前端传 approved=true/false 的方式
            String finalStatus = applicationStatus;
            if (finalStatus == null && approved != null) {
                finalStatus = approved ? "APPROVED" : "REJECTED";
            }
            if (finalStatus == null) {
                return new JsonBean(0, "审批状态不能为空", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal approverId = loginStaff.getStaffid();

            boolean success = tblGtAccountClosingService.approveAccountClosing(
                    id, approverId, approvalOpinion, finalStatus
            );

            return success ? JsonBean.success("审批成功") : new JsonBean(0, "审批失败", null).toJson();

        } catch (Exception e) {
            log.error("审批销户申请失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量审批销户申请
     */
    @PostMapping("/batch-approve")
    @ApiOperation("批量审批销户申请")
    public String batchApprove(@RequestBody Map<String, Object> body) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Object> ids = (List<Object>) body.get("applicationIds");
            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "申请ID列表不能为空", null).toJson();
            }
            List<BigDecimal> applicationIds = ids.stream()
                    .map(id -> new BigDecimal(id.toString()))
                    .collect(java.util.stream.Collectors.toList());

            String approvalOpinion = body.get("approvalOpinion") != null ? body.get("approvalOpinion").toString() : null;
            String applicationStatus = body.get("applicationStatus") != null ? body.get("applicationStatus").toString() : null;
            Boolean approved = body.get("approved") != null ? Boolean.valueOf(body.get("approved").toString()) : null;

            String finalStatus = applicationStatus;
            if (finalStatus == null && approved != null) {
                finalStatus = approved ? "APPROVED" : "REJECTED";
            }
            if (finalStatus == null) {
                return new JsonBean(0, "审批状态不能为空", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal approverId = loginStaff.getStaffid();

            int successCount = tblGtAccountClosingService.batchApproveAccountClosing(
                    applicationIds, approverId, approvalOpinion, finalStatus
            );

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("totalCount", applicationIds.size());
            return JsonBean.success(result);

        } catch (Exception e) {
            log.error("批量审批销户申请失败", e);
            return new JsonBean(0, "批量审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消销户申请
     */
    @PostMapping("/{id}/cancel")
    @ApiOperation("取消销户申请")
    public String cancel(
            @ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "更新人") @RequestParam(required = false) String updateUser) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountClosing entity = tblGtAccountClosingService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }
            entity.setApplicationStatus("CANCELLED");
            boolean success = tblGtAccountClosingService.updateAccountClosing(entity);
            return success ? JsonBean.success("取消成功") : new JsonBean(0, "取消失败", null).toJson();

        } catch (Exception e) {
            log.error("取消销户申请失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 完成销户操作
     */
    @PostMapping("/{id}/complete")
    @ApiOperation("完成销户操作")
    public String complete(
            @ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "销户日期") @RequestParam(required = false) String closingDate,
            @ApiParam(value = "更新人") @RequestParam(required = false) String updateUser) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountClosing entity = tblGtAccountClosingService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }
            entity.setApplicationStatus("COMPLETED");
            if (closingDate != null && !closingDate.isEmpty()) {
                entity.setCompleteDate(java.time.LocalDate.parse(closingDate));
            }
            boolean success = tblGtAccountClosingService.updateAccountClosing(entity);
            return success ? JsonBean.success("销户完成") : new JsonBean(0, "操作失败", null).toJson();

        } catch (Exception e) {
            log.error("完成销户操作失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }
}
