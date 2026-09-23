package com.global.treasurer.controller;

import com.global.treasurer.entity.ETicketAccountConfig;
import com.global.treasurer.entity.TcEticketAccount;
import com.global.treasurer.service.ETicketAccountConfigService;
import com.global.treasurer.service.TcEticketAccountService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

/**
 * 电票账户配置控制器
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@RestController
@RequestMapping("/financial/basicConfig/eTicketAccount")
@Api(tags = "电票账户配置")
public class ETicketAccountConfigController {
    private static final Logger log = LoggerFactory.getLogger(ETicketAccountConfigController.class);

    @Resource
    private ETicketAccountConfigService eTicketAccountConfigService;

    @Resource
    private TcEticketAccountService tcEticketAccountService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询电票账户列表（同时支持GET和POST）
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询电票账户列表")
    public String getAccountList(@RequestBody(required = false) Map<String, Object> body,
                                 @RequestParam(required = false) Map<String, Object> queryParams) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> params = new HashMap<>();
            if (queryParams != null) {
                params.putAll(queryParams);
            }
            if (body != null) {
                params.putAll(body);
            }

            Map<String, Object> result = eTicketAccountConfigService.getAccountPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询电票账户列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询电票账户列表(POST方式,兼容/page路径)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询电票账户列表")
    public String getAccountPage(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = eTicketAccountConfigService.getAccountPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询电票账户列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询电票账户
     */
    @GetMapping("/detail")
    @ApiOperation("根据ID查询电票账户")
    public String getAccountById(@RequestParam String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            ETicketAccountConfig account = eTicketAccountConfigService.getAccountById(id);
            return JsonBean.success(account);
        } catch (Exception e) {
            log.error("查询电票账户详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建电票账户（/create 和 /add 均支持）
     */
    @PostMapping({"/create", "/add"})
    @ApiOperation("创建电票账户")
    public String createAccount(@RequestBody ETicketAccountConfig account) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = eTicketAccountConfigService.createAccount(account);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("创建电票账户失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新电票账户
     */
    @PostMapping("/update")
    @ApiOperation("更新电票账户")
    public String updateAccount(@RequestBody ETicketAccountConfig account) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = eTicketAccountConfigService.updateAccount(account);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新电票账户失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除电票账户
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除电票账户")
    public String deleteAccount(@PathVariable String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<String> ids = new ArrayList<>();
            ids.add(id);
            int result = eTicketAccountConfigService.batchDelete(ids);
            if (result > 0) {
                Map<String, Object> data = new HashMap<>();
                data.put("deleted", true);
                data.put("id", id);
                return new JsonBean(1, "删除成功", data).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除电票账户失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 同步账户状态
     */
    @PostMapping("/sync-status/{id}")
    @ApiOperation("同步账户状态")
    public String syncAccountStatus(@PathVariable String id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = eTicketAccountConfigService.syncAccountStatus(id);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("同步账户状态失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 保存或更新电票账户（前端统一入口）
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新电票账户")
    public String saveOrUpdate(@RequestBody ETicketAccountConfig account) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result;
            if (account.getId() != null) {
                result = eTicketAccountConfigService.updateAccount(account);
            } else {
                result = eTicketAccountConfigService.createAccount(account);
            }
            if (result > 0) {
                return JsonBean.success("操作成功");
            } else {
                return new JsonBean(0, "操作失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("保存或更新电票账户失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除电票账户（POST方式，前端传 {id} body）
     */
    @PostMapping("/delete")
    @ApiOperation("删除电票账户(POST)")
    public String deleteAccountPost(@RequestBody Map<String, Object> body) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Object idObj = body.get("id");
            if (idObj == null) {
                return new JsonBean(0, "id不能为空", null).toJson();
            }
            String id = idObj.toString();
            List<String> ids = new ArrayList<>();
            ids.add(id);
            int result = eTicketAccountConfigService.batchDelete(ids);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除电票账户失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取账户统计信息（与列表使用同一数据源）
     */
    @GetMapping("/statistics")
    @ApiOperation("获取账户统计信息")
    public String getAccountStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = eTicketAccountConfigService.getAccountStatistics();
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("获取账户统计信息失败", e);
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }
}
