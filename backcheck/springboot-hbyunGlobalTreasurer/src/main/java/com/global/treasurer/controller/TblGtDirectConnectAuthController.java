package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtDirectConnectAuth;
import com.global.treasurer.mapper.TblGtDirectConnectAuthMapper;
import com.global.treasurer.service.TblGtDirectConnectAuthService;
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
import java.util.List;
import java.util.Map;

/**
 * 全球司库-直联授权管理控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/financial/direct-connect-auth")
@Api(tags = "直联授权管理")
public class TblGtDirectConnectAuthController {
    private static final Logger log = LoggerFactory.getLogger(TblGtDirectConnectAuthController.class);

    @Resource
    private TblGtDirectConnectAuthService tblGtDirectConnectAuthService;

    @Resource
    private TblGtDirectConnectAuthMapper tblGtDirectConnectAuthMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限（开发环境：直接放行）
     */
    private boolean validateUser() {
        return true;
    }

    /**
     * 分页查询直联授权列表
     * 支持 GET 和 POST 请求
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询直联授权列表")
    public String getPageList(
            @ApiParam(value = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @ApiParam(value = "授权ID") @RequestParam(value = "authId", required = false) String authId,
            @ApiParam(value = "账户号码") @RequestParam(value = "accountNumber", required = false) String accountNumber,
            @ApiParam(value = "授权类型") @RequestParam(value = "authType", required = false) String authType,
            @ApiParam(value = "授权状态") @RequestParam(value = "authStatus", required = false) String authStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Page<TblGtDirectConnectAuth> pageObj = new Page<>(page, limit);
            IPage<TblGtDirectConnectAuth> result = tblGtDirectConnectAuthService.getPageList(
                    pageObj, accountNumber, authType, authStatus
            );

            Map<String, Object> data = new HashMap<>();
            data.put("records", result.getRecords());
            data.put("tlist", result.getRecords());
            data.put("total", result.getTotal());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            // 统计各状态数量
            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal orgId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null;
            List<Map<String, Object>> statusCounts = tblGtDirectConnectAuthMapper.selectStatusCount(orgId);
            long total = 0, active = 0, expiring = 0, suspended = 0;
            for (Map<String, Object> row : statusCounts) {
                String status = row.get("AUTH_STATUS") != null ? row.get("AUTH_STATUS").toString()
                        : (row.get("authStatus") != null ? row.get("authStatus").toString() : "");
                long cnt = row.get("CNT") != null ? Long.parseLong(row.get("CNT").toString())
                        : (row.get("cnt") != null ? Long.parseLong(row.get("cnt").toString()) : 0L);
                total += cnt;
                if ("ACTIVE".equals(status)) active = cnt;
                else if ("EXPIRING".equals(status)) expiring = cnt;
                else if ("SUSPENDED".equals(status)) suspended = cnt;
            }
            data.put("totalAuths", total);
            data.put("activeAuths", active);
            data.put("expiringAuths", expiring);
            data.put("suspendedAuths", suspended);

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("查询直联授权列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询直联授权详情")
    public String getById(@ApiParam(value = "授权ID", required = true) @PathVariable Long id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtDirectConnectAuth entity = tblGtDirectConnectAuthService.getById(id);
            if (entity == null) {
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            return JsonBean.success(entity);

        } catch (Exception e) {
            log.error("查询直联授权详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增直联授权
     */
    @PostMapping
    @ApiOperation("新增直联授权")
    public String save(@FlexibleRequestBody TblGtDirectConnectAuth entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setCreateUser(loginStaff.getStaffid());
            entity.setUpdateUser(loginStaff.getStaffid());
            entity.setAuthorizerId(loginStaff.getStaffid());
            entity.setAuthorizerName(loginStaff.getRealname());
            entity.setOrgId(loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null);

            boolean success = tblGtDirectConnectAuthService.saveDirectConnectAuth(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "新增失败", null).toJson();

        } catch (Exception e) {
            log.error("新增直联授权失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新直联授权
     */
    @PutMapping
    @ApiOperation("更新直联授权")
    public String update(@FlexibleRequestBody TblGtDirectConnectAuth entity) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            entity.setUpdateUser(loginStaff.getStaffid());

            boolean success = tblGtDirectConnectAuthService.updateDirectConnectAuth(entity);
            return success ? JsonBean.success(entity) : new JsonBean(0, "更新失败", null).toJson();

        } catch (Exception e) {
            log.error("更新直联授权失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除直联授权
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除直联授权")
    public String delete(@ApiParam(value = "授权ID", required = true) @PathVariable Long id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtDirectConnectAuthService.deleteDirectConnectAuth(id);
            return success ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();

        } catch (Exception e) {
            log.error("删除直联授权失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批直联授权
     */
    @PutMapping("/{id}/approve")
    @ApiOperation("审批直联授权")
    public String approve(
            @ApiParam(value = "授权ID", required = true) @PathVariable Long id,
            @ApiParam(value = "审批意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "审批状态", required = true) @RequestParam String authStatus) {

        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            Long approverId = loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null;

            boolean success = tblGtDirectConnectAuthService.approveDirectConnectAuth(
                    id, approverId, approvalOpinion, authStatus
            );

            return success ? JsonBean.success("审批成功") : new JsonBean(0, "审批失败", null).toJson();

        } catch (Exception e) {
            log.error("审批直联授权失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 测试银行连接
     */
    @PostMapping("/{id}/test-connection")
    @ApiOperation("测试银行连接")
    public String testConnection(@ApiParam(value = "授权ID", required = true) @PathVariable Long id) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = tblGtDirectConnectAuthService.testBankConnection(id);
            return JsonBean.success(result);

        } catch (Exception e) {
            log.error("测试银行连接失败", e);
            return new JsonBean(0, "测试连接失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除直联授权
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除直联授权")
    public String batchDelete(@RequestBody Map<String, Object> params) {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Object authIdsObj = params.get("authIds");
            if (authIdsObj == null) {
                return new JsonBean(0, "authIds不能为空", null).toJson();
            }
            @SuppressWarnings("unchecked")
            List<Object> rawIds = (List<Object>) authIdsObj;
            List<Long> authIds = new ArrayList<>();
            for (Object id : rawIds) {
                authIds.add(Long.valueOf(id.toString()));
            }
            boolean success = tblGtDirectConnectAuthService.removeByIds(authIds);
            return success ? JsonBean.success("批量删除成功") : new JsonBean(0, "批量删除失败", null).toJson();
        } catch (Exception e) {
            log.error("批量删除直联授权失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出直联授权数据
     */
    @GetMapping("/export")
    @ApiOperation("导出直联授权数据")
    public void export(
            @ApiParam(value = "账户号码") @RequestParam(required = false) String accountNumber,
            @ApiParam(value = "授权类型") @RequestParam(required = false) String authType,
            @ApiParam(value = "授权状态") @RequestParam(required = false) String authStatus,
            javax.servlet.http.HttpServletResponse response) {
        try {
            Page<TblGtDirectConnectAuth> pageObj = new Page<>(1, Integer.MAX_VALUE);
            IPage<TblGtDirectConnectAuth> result = tblGtDirectConnectAuthService.getPageList(
                    pageObj, accountNumber, authType, authStatus);
            List<TblGtDirectConnectAuth> list = result.getRecords();
            com.global.treasurer.util.excel.ExcelExport ee = new com.global.treasurer.util.excel.ExcelExport(
                    "银企直联授权列表", TblGtDirectConnectAuth.class);
            ee.setDataList(list).write(response, "银企直联授权列表.xlsx");
        } catch (Exception e) {
            log.error("导出直联授权数据失败", e);
        }
    }
}
