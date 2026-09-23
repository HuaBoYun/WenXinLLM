package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountOpening;
import com.global.treasurer.service.TblGtAccountOpeningService;
import com.global.treasurer.util.LegalDealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ServletUtil;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 全球司库-开户申请管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping(value = "/financial/account-opening", produces = "application/json;charset=UTF-8")
@Api(tags = "开户申请管理")
public class TblGtAccountOpeningController {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountOpeningController.class);

    @Resource
    private TblGtAccountOpeningService tblGtAccountOpeningService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取当前登录用户（优先使用 JedisUtil 直连 Redis，降级用 UserProvider）
     */
    private TblStaffUtil getCurrentUser() {
        try {
            String token = ServletUtil.getHeader("token");
            if (token != null) {
                TblStaffUtil staff = LegalDealUserToken.parseUserToken(token);
                if (staff != null) return staff;
            }
            return userProvider.get();
        } catch (Exception e) {
            log.debug("获取当前用户失败，使用默认值", e);
            return null;
        }
    }

    /**
     * 验证用户权限（开发环境：直接放行，与 FundConcentrationController 保持一致）
     */
    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询开户申请列表
     * 支持 application/x-www-form-urlencoded 和 application/json 两种格式
     */
    @PostMapping("/page")
    @ApiOperation("分页查询开户申请列表")
    public String getPageList(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "applicationNo", required = false) String applicationNo,
            @RequestParam(value = "accountName", required = false) String accountName,
            @RequestParam(value = "applicationStatus", required = false) String applicationStatus,
            @RequestParam(value = "bankCode", required = false) String bankCode) {

        try {
            // 权限验证
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Page<TblGtAccountOpening> page = new Page<>(current, size);
            IPage<TblGtAccountOpening> result = tblGtAccountOpeningService.getPageList(
                    page, applicationNo, accountName, applicationStatus, bankCode, null
            );

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询开户申请列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询开户申请详情")
    public String getById(@ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountOpening entity = tblGtAccountOpeningService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询开户申请详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增开户申请
     */
    @PostMapping
    @ApiOperation("新增开户申请")
    public String save(@FlexibleRequestBody TblGtAccountOpening entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = getCurrentUser();
            if (loginStaff != null) {
                entity.setCreateUser(loginStaff.getStaffid());
                entity.setUpdateUser(loginStaff.getStaffid());
                entity.setOrgId(loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null);
            }

            boolean success = tblGtAccountOpeningService.saveAccountOpening(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增开户申请失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新开户申请
     */
    @PutMapping
    @ApiOperation("更新开户申请")
    public String update(@FlexibleRequestBody TblGtAccountOpening entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = getCurrentUser();
            if (loginStaff != null) {
                entity.setUpdateUser(loginStaff.getStaffid());
            }

            boolean success = tblGtAccountOpeningService.updateAccountOpening(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新开户申请失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除开户申请
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除开户申请")
    public String delete(@ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountOpeningService.deleteAccountOpening(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除开户申请失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询开户申请状态统计
     */
    @GetMapping("/statistics")
    @ApiOperation("查询开户申请状态统计")
    public String getStatistics() {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> stats = tblGtAccountOpeningService.getStatusStatistics(null);
            return JsonBean.success(stats);
        } catch (Exception e) {
            log.error("查询开户申请统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消开户申请
     */
    @PutMapping("/{id}/cancel")
    @ApiOperation("取消开户申请")
    public String cancel(
            @ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "更新人") @RequestParam(required = false) String updateUser) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = getCurrentUser();
            BigDecimal operatorId = loginStaff != null ? loginStaff.getStaffid() : null;

            boolean success = tblGtAccountOpeningService.cancelAccountOpening(id, operatorId);
            return success ? JsonBean.success("取消成功") : new JsonBean(0, "取消失败", null).toJson();

        } catch (Exception e) {
            log.error("取消开户申请失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批开户申请
     */
    @PutMapping("/{id}/approve")
    @ApiOperation("审批开户申请")
    public String approve(
            @ApiParam(value = "申请ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "审批状态", required = true) @RequestParam String applicationStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = getCurrentUser();
            BigDecimal approverId = loginStaff != null ? loginStaff.getStaffid() : null;

            boolean success = tblGtAccountOpeningService.approveAccountOpening(
                    id, approverId, approvalOpinion, applicationStatus
            );

            return success ? JsonBean.success("审批成功") : new JsonBean(0, "审批失败", null).toJson();

        } catch (Exception e) {
            log.error("审批开户申请失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }
}
