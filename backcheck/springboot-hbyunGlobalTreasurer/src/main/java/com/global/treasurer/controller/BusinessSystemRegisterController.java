package com.global.treasurer.controller;

import com.global.treasurer.entity.BusinessSystemRegister;
import com.global.treasurer.service.BusinessSystemRegisterService;
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
 * 业务系统注册控制器
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@RestController
@RequestMapping("/financial/basicConfig/systemRegister")
@Api(tags = "业务系统注册")
public class BusinessSystemRegisterController {
    private static final Logger log = LoggerFactory.getLogger(BusinessSystemRegisterController.class);

    @Resource
    private BusinessSystemRegisterService businessSystemRegisterService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询业务系统列表
     */
    @GetMapping("/list")
    @ApiOperation("分页查询业务系统列表")
    public String getSystemList(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = businessSystemRegisterService.getSystemPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询业务系统列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询业务系统列表(POST方式,兼容分页)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询业务系统列表")
    public String getSystemPage(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = businessSystemRegisterService.getSystemPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询业务系统列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询业务系统
     */
    @GetMapping("/detail")
    @ApiOperation("根据ID查询业务系统")
    public String getSystemById(@RequestParam Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            BusinessSystemRegister system = businessSystemRegisterService.getSystemById(id);
            return JsonBean.success(system);
        } catch (Exception e) {
            log.error("查询业务系统详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建业务系统
     */
    @PostMapping("/create")
    @ApiOperation("创建业务系统")
    public String createSystem(@RequestBody BusinessSystemRegister system) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = businessSystemRegisterService.createSystem(system);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("创建业务系统失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新业务系统
     */
    @PostMapping("/update")
    @ApiOperation("更新业务系统")
    public String updateSystem(@RequestBody BusinessSystemRegister system) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = businessSystemRegisterService.updateSystem(system);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新业务系统失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除业务系统
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除业务系统")
    public String deleteSystem(@RequestBody List<Long> ids) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = businessSystemRegisterService.batchDelete(ids);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除业务系统失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 测试连接
     */
    @PostMapping("/testConnection")
    @ApiOperation("测试连接")
    public String testConnection(@RequestBody Map<String, Long> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = params.get("id");
            Map<String, Object> result = businessSystemRegisterService.testConnection(id);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("测试连接失败", e);
            return new JsonBean(0, "测试失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 切换状态
     */
    @PutMapping("/status")
    @ApiOperation("切换状态")
    public String toggleStatus(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = Long.valueOf(params.get("id").toString());
            Integer status = Integer.valueOf(params.get("status").toString());
            int result = businessSystemRegisterService.toggleStatus(id, status);
            if (result > 0) {
                return JsonBean.success("状态更新成功");
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("切换状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 同步状态
     */
    @PostMapping("/sync-status")
    @ApiOperation("同步状态")
    public String syncStatus() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = businessSystemRegisterService.syncStatus();
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("同步状态失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }
}
