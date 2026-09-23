package com.global.treasurer.controller;

import com.global.treasurer.entity.DataMappingConfig;
import com.global.treasurer.service.DataMappingConfigService;
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
 * 数据映射配置控制器
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@RestController
@RequestMapping("/financial/basicConfig/dataMappingConfig")
@Api(tags = "数据映射配置")
public class DataMappingConfigController {
    private static final Logger log = LoggerFactory.getLogger(DataMappingConfigController.class);

    @Resource
    private DataMappingConfigService dataMappingConfigService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询数据映射列表(GET方式)
     */
    @GetMapping("/list")
    @ApiOperation("分页查询数据映射列表(GET)")
    public String getMappingList(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = dataMappingConfigService.getMappingPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询数据映射列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询数据映射列表(POST方式,兼容分页)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询数据映射列表")
    public String getMappingPage(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = dataMappingConfigService.getMappingPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询数据映射列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建数据映射
     */
    @PostMapping("/create")
    @ApiOperation("创建数据映射")
    public String createMapping(@RequestBody DataMappingConfig mapping) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = dataMappingConfigService.createMapping(mapping);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("创建数据映射失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新数据映射
     */
    @PostMapping("/update")
    @ApiOperation("更新数据映射")
    public String updateMapping(@RequestBody DataMappingConfig mapping) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = dataMappingConfigService.updateMapping(mapping);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新数据映射失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除数据映射
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除数据映射")
    public String deleteMapping(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = new ArrayList<>();
            ids.add(id);
            int result = dataMappingConfigService.batchDelete(ids);
            if (result > 0) {
                Map<String, Object> data = new HashMap<>();
                data.put("deleted", true);
                data.put("id", id);
                return new JsonBean(1, "删除成功", data).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除数据映射失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 测试数据映射
     */
    @PostMapping("/test")
    @ApiOperation("测试数据映射")
    public String testMapping(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = dataMappingConfigService.testMapping(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("测试数据映射失败", e);
            return new JsonBean(0, "测试失败: " + e.getMessage(), null).toJson();
        }
    }
}
