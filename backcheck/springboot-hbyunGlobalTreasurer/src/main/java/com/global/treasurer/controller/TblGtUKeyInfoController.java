package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtUKeyInfo;
import com.global.treasurer.mapper.TblGtUKeyInfoMapper;
import com.global.treasurer.service.TblGtUKeyInfoService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-U盾信息管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/ukey-info")
@Api(tags = "U盾信息管理")
public class TblGtUKeyInfoController {
    private static final Logger log = LoggerFactory.getLogger(TblGtUKeyInfoController.class);

    @Resource
    private TblGtUKeyInfoService tblGtUKeyInfoService;

    @Resource
    private TblGtUKeyInfoMapper tblGtUKeyInfoMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限
     */
    private boolean validateUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null;
        } catch (Exception e) {
            log.error("用户验证失败", e);
            return false;
        }
    }

    /**
     * 分页查询U盾信息列表
     * 支持 GET 和 POST 请求
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询U盾信息列表")
    public String getPageList(
            @ApiParam(value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @ApiParam(value = "U盾ID") @RequestParam(value = "ukeyId", required = false) Long ukeyId,
            @ApiParam(value = "持有人姓名") @RequestParam(value = "holderName", required = false) String holderName,
            @ApiParam(value = "U盾编号") @RequestParam(value = "ukeyNo", required = false) String ukeyNo,
            @ApiParam(value = "U盾状态") @RequestParam(value = "ukeyStatus", required = false) String ukeyStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取组织ID
            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal orgId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null;

            Page<TblGtUKeyInfo> pageObj = new Page<>(page, limit);
            IPage<TblGtUKeyInfo> result = tblGtUKeyInfoService.getPageList(
                    pageObj, orgId, ukeyId, holderName, ukeyNo, ukeyStatus
            );

            Map<String, Object> data = new HashMap<>();
            data.put("records", result.getRecords());
            data.put("tlist", result.getRecords());
            data.put("total", result.getTotal());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            // 统计各状态数量
            List<Map<String, Object>> statusCounts = tblGtUKeyInfoMapper.selectStatusCount(orgId);
            long totalUKeys = 0, active = 0, expiring = 0, blocked = 0;
            for (Map<String, Object> row : statusCounts) {
                String status = row.getOrDefault("ukeyStatus", "").toString();
                Object cntObj = row.get("cnt");
                long cnt = cntObj != null ? Long.parseLong(cntObj.toString()) : 0L;
                totalUKeys += cnt;
                if ("ACTIVE".equals(status)) active = cnt;
                else if ("EXPIRING".equals(status)) expiring = cnt;
                else if ("BLOCKED".equals(status) || "LOCKED".equals(status)) blocked += cnt;
            }
            data.put("totalUKeys", totalUKeys);
            data.put("activeUKeys", active);
            data.put("expiringUKeys", expiring);
            data.put("blockedUKeys", blocked);

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询U盾信息列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询U盾信息详情")
    public String getById(@ApiParam(value = "U盾ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtUKeyInfo entity = tblGtUKeyInfoService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询U盾信息详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增U盾信息
     */
    @PostMapping
    @ApiOperation("新增U盾信息")
    public String save(@FlexibleRequestBody TblGtUKeyInfo entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setCreateUser(loginStaff.getStaffid());
            entity.setUpdateUser(loginStaff.getStaffid());

            boolean success = tblGtUKeyInfoService.saveUKeyInfo(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增U盾信息失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新U盾信息
     */
    @PutMapping
    @ApiOperation("更新U盾信息")
    public String update(@FlexibleRequestBody TblGtUKeyInfo entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setUpdateUser(loginStaff.getStaffid());

            boolean success = tblGtUKeyInfoService.updateUKeyInfo(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新U盾信息失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除U盾信息
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除U盾信息")
    public String delete(@ApiParam(value = "U盾ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtUKeyInfoService.deleteUKeyInfo(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除U盾信息失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 锁定U盾
     */
    @PutMapping("/{id}/lock")
    @ApiOperation("锁定U盾")
    public String lock(@ApiParam(value = "U盾ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            boolean success = tblGtUKeyInfoService.lockUKey(
                    id, loginStaff.getStaffid()
            );

            return success ? JsonBean.success("锁定成功") : new JsonBean(0, "锁定失败", null).toJson();

        } catch (Exception e) {
            log.error("锁定U盾失败", e);
            return new JsonBean(0, "锁定失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 统计U盾各状态数量
     */
    @GetMapping("/status-count")
    @ApiOperation("统计U盾各状态数量")
    public String getStatusCount() {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            // 总数
            long totalUKeys = tblGtUKeyInfoService.count();
            // 正常（ACTIVE 且 30天内不过期）
            java.time.LocalDate in30Days = java.time.LocalDate.now().plusDays(30);
            long activeUKeys = tblGtUKeyInfoService.count(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TblGtUKeyInfo>()
                    .eq("UKEY_STATUS", "ACTIVE")
                    .gt("EXPIRY_DATE", in30Days)
            );
            // 即将过期（ACTIVE 且 30天内到期）
            long expiringUKeys = tblGtUKeyInfoService.count(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TblGtUKeyInfo>()
                    .eq("UKEY_STATUS", "ACTIVE")
                    .le("EXPIRY_DATE", in30Days)
            );
            // 已锁定
            long blockedUKeys = tblGtUKeyInfoService.count(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TblGtUKeyInfo>()
                    .eq("UKEY_STATUS", "LOCKED")
            );
            Map<String, Object> data = new HashMap<>();
            data.put("totalUKeys", totalUKeys);
            data.put("activeUKeys", activeUKeys);
            data.put("expiringUKeys", expiringUKeys);
            data.put("blockedUKeys", blockedUKeys);
            return JsonBean.success(data);
        } catch (Exception e) {
            log.error("统计U盾状态失败", e);
            return new JsonBean(0, "统计失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 解锁U盾
     */
    @PutMapping("/{id}/unlock")
    @ApiOperation("解锁U盾")
    public String unlock(@ApiParam(value = "U盾ID", required = true) @PathVariable BigDecimal id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            boolean success = tblGtUKeyInfoService.unlockUKey(
                    id, loginStaff.getStaffid()
            );

            return success ? JsonBean.success("解锁成功") : new JsonBean(0, "解锁失败", null).toJson();

        } catch (Exception e) {
            log.error("解锁U盾失败", e);
            return new JsonBean(0, "解锁失败: " + e.getMessage(), null).toJson();
        }
    }
}
