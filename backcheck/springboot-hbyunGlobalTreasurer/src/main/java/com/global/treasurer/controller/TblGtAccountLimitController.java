package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountLimit;
import com.global.treasurer.service.TblGtAccountLimitService;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 全球司库-账户限额管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/account-limit")
@Api(tags = "账户限额管理")
public class TblGtAccountLimitController {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountLimitController.class);

    @Resource
    private TblGtAccountLimitService tblGtAccountLimitService;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限（开发环境：直接放行）
     */
    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询账户限额列表
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询账户限额列表")
    public String getPageList(
            @ApiParam(value = "页码", example = "1") @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            @ApiParam(value = "每页数量", example = "10") @RequestParam(value = "limit", defaultValue = "10") Integer pageSize,
            @ApiParam(value = "账户号码") @RequestParam(required = false) String accountNumber,
            @ApiParam(value = "限额类型") @RequestParam(required = false) String limitType,
            @ApiParam(value = "限额状态") @RequestParam(required = false) String limitStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Page<TblGtAccountLimit> page = new Page<>(pageNo, pageSize);
            IPage<TblGtAccountLimit> result = tblGtAccountLimitService.getPageList(
                    page, accountNumber, limitType, limitStatus
            );

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询账户限额列表失败, 异常类型: {}, 异常信息: {}", e.getClass().getName(), e.getMessage(), e);
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            // 检查是否是表不存在的错误
            if (errorMsg.contains("不存在") || errorMsg.contains("does not exist") || errorMsg.contains("doesn't exist")) {
                return new JsonBean(0, "数据库表TBL_GT_ACCOUNT_LIMIT不存在，请先执行建表脚本", null).toJson();
            }
            return new JsonBean(0, "查询失败: " + errorMsg, null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询账户限额详情")
    public String getById(@ApiParam(value = "限额ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountLimit entity = tblGtAccountLimitService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询账户限额详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增账户限额
     */
    @PostMapping("")
    @ApiOperation("新增账户限额")
    public String save(@FlexibleRequestBody TblGtAccountLimit entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            // BigDecimal 转 Long
            entity.setCreateUser(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            entity.setUpdateUser(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);

            boolean success = tblGtAccountLimitService.saveAccountLimit(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增账户限额失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新账户限额
     */
    @PutMapping("")
    @ApiOperation("更新账户限额")
    public String update(@FlexibleRequestBody TblGtAccountLimit entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            // BigDecimal 转 Long
            entity.setUpdateUser(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);

            boolean success = tblGtAccountLimitService.updateAccountLimit(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新账户限额失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除账户限额
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除账户限额")
    public String delete(@ApiParam(value = "限额ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountLimitService.deleteAccountLimit(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除账户限额失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批账户限额
     */
    @PutMapping("/{id}/approve")
    @ApiOperation("审批账户限额")
    public String approve(
            @ApiParam(value = "限额ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "审批状态", required = true) @RequestParam String limitStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal approverId = loginStaff.getStaffid();

            boolean success = tblGtAccountLimitService.approveAccountLimit(
                    id, approverId, approvalOpinion, limitStatus
            );

            return success ? JsonBean.success("审批成功") : new JsonBean(0, "审批失败", null).toJson();

        } catch (Exception e) {
            log.error("审批账户限额失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 暂停账户限额配置
     */
    @PutMapping("/{id}/suspend")
    @ApiOperation("暂停账户限额配置")
    public String suspend(@ApiParam(value = "限额ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            boolean success = tblGtAccountLimitService.suspendAccountLimit(
                    id, loginStaff.getStaffid()
            );

            return success ? JsonBean.success("暂停成功") : new JsonBean(0, "暂停失败", null).toJson();

        } catch (Exception e) {
            log.error("暂停账户限额失败", e);
            return new JsonBean(0, "暂停失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 激活账户限额配置
     */
    @PutMapping("/{id}/activate")
    @ApiOperation("激活账户限额配置")
    public String activate(@ApiParam(value = "限额ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            boolean success = tblGtAccountLimitService.activateAccountLimit(
                    id, loginStaff.getStaffid()
            );

            return success ? JsonBean.success("激活成功") : new JsonBean(0, "激活失败", null).toJson();

        } catch (Exception e) {
            log.error("激活账户限额失败", e);
            return new JsonBean(0, "激活失败: " + e.getMessage(), null).toJson();
        }
    }
}
