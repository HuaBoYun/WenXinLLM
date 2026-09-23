package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountCheck;
import com.global.treasurer.service.TblGtAccountCheckService;
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
 * 全球司库-账户检查管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/account-check")
@Api(tags = "账户检查管理")
public class TblGtAccountCheckController {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountCheckController.class);

    @Resource
    private TblGtAccountCheckService tblGtAccountCheckService;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限（开发环境：直接放行）
     */
    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询账户检查列表
     */
    @PostMapping("/page")
    @ApiOperation("分页查询账户检查列表")
    public String getPageList(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam(value = "每页数量", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "账户号码") @RequestParam(required = false) String accountNumber,
            @ApiParam(value = "检查类型") @RequestParam(required = false) String checkType,
            @ApiParam(value = "检查状态") @RequestParam(required = false) String checkStatus,
            @ApiParam(value = "检查ID") @RequestParam(required = false) Long checkId,
            @ApiParam(value = "开始日期") @RequestParam(required = false) String startDate,
            @ApiParam(value = "结束日期") @RequestParam(required = false) String endDate) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal orgId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null;

            Page<TblGtAccountCheck> page = new Page<>(pageNo, pageSize);
            IPage<TblGtAccountCheck> result = tblGtAccountCheckService.getPageList(
                    page, accountNumber, checkType, checkStatus, checkId, startDate, endDate, orgId
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
            log.error("查询账户检查列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询账户检查详情")
    public String getById(@ApiParam(value = "检查ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtAccountCheck entity = tblGtAccountCheckService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询账户检查详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增账户检查
     */
    @PostMapping
    @ApiOperation("新增账户检查")
    public String save(@FlexibleRequestBody TblGtAccountCheck entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setCreateUser(loginStaff.getStaffid());
            entity.setUpdateUser(loginStaff.getStaffid());
            entity.setCheckUser(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            entity.setOrgId(loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null);

            boolean success = tblGtAccountCheckService.saveAccountCheck(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增账户检查失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新账户检查
     */
    @PutMapping
    @ApiOperation("更新账户检查")
    public String update(@FlexibleRequestBody TblGtAccountCheck entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setUpdateUser(loginStaff.getStaffid());

            boolean success = tblGtAccountCheckService.updateAccountCheck(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新账户检查失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除账户检查
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除账户检查")
    public String delete(@ApiParam(value = "检查ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountCheckService.deleteAccountCheck(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除账户检查失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 停止账户检查
     */
    @PutMapping("/{id}/stop")
    @ApiOperation("停止账户检查")
    public String stop(@ApiParam(value = "检查ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblStaffUtil loginStaff = userProvider.get();
            TblGtAccountCheck entity = new TblGtAccountCheck();
            entity.setCheckId(id.longValue());
            entity.setCheckStatus("COMPLETED");
            entity.setUpdateUser(loginStaff.getStaffid());
            boolean success = tblGtAccountCheckService.updateAccountCheck(entity);
            return success ? JsonBean.success("已停止") : new JsonBean(0, "停止失败", null).toJson();
        } catch (Exception e) {
            log.error("停止账户检查失败", e);
            return new JsonBean(0, "停止失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 重新检查
     */
    @PutMapping("/{id}/recheck")
    @ApiOperation("重新检查账户")
    public String recheck(@ApiParam(value = "检查ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblStaffUtil loginStaff = userProvider.get();
            boolean success = tblGtAccountCheckService.recheckAccountCheck(id.longValue(), loginStaff.getStaffid());
            return success ? JsonBean.success("重检已启动") : new JsonBean(0, "重检失败", null).toJson();
        } catch (Exception e) {
            log.error("重新检查失败", e);
            return new JsonBean(0, "重检失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批账户检查
     */
    @PutMapping("/{id}/approve")
    @ApiOperation("审批账户检查")
    public String approve(
            @ApiParam(value = "检查ID", required = true) @PathVariable BigDecimal id,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "审批状态", required = true) @RequestParam String checkStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal approverId = loginStaff.getStaffid();

            boolean success = tblGtAccountCheckService.approveAccountCheck(
                    id, approverId, approvalOpinion, checkStatus
            );

            return success ? JsonBean.success("审批成功") : new JsonBean(0, "审批失败", null).toJson();

        } catch (Exception e) {
            log.error("审批账户检查失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }
}
