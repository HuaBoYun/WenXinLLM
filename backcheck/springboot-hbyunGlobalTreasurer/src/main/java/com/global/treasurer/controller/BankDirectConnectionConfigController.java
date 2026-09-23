package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.AmDirectConnectionConfig;
import com.global.treasurer.service.AmDirectConnectionConfigService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/settlement/bank-direct-connection-config")
@Api(tags = "银行直连配置管理")
public class BankDirectConnectionConfigController {
    @Resource
    private AmDirectConnectionConfigService configService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询配置列表")
    public String getPage(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            PageInfo<AmDirectConnectionConfig> pageInfo = configService.list(pageNum, pageSize, params);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("获取配置详情")
    public String getDetail(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            AmDirectConnectionConfig config = configService.getById(id);
            if (config == null) return new JsonBean(0, "配置不存在", null).toJson();
            return JsonBean.success(config);
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增配置")
    public String add(@FlexibleRequestBody AmDirectConnectionConfig config) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            config.setCreateUser(Long.parseLong(loginStaff.getStaffid().toString()));
            boolean result = configService.save(config);
            return result ? JsonBean.success("创建成功") : new JsonBean(0, "创建失败", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("更新配置")
    public String update(@FlexibleRequestBody AmDirectConnectionConfig config) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            config.setUpdateUser(Long.parseLong(loginStaff.getStaffid().toString()));
            boolean result = configService.update(config);
            return result ? JsonBean.success("更新成功") : new JsonBean(0, "更新失败", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除配置")
    public String delete(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            boolean result = configService.delete(id);
            return result ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/test/{id}")
    @ApiOperation("测试单个连接")
    public String testConnection(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            Map<String, Object> result = configService.testConnection(id);
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "测试失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-test")
    @ApiOperation("批量测试连接")
    public String batchTestConnection(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            Map<String, Object> result = configService.batchTestConnection(ids);
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "批量测试失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取配置统计")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            return JsonBean.success(configService.getStatistics());
        } catch (Exception e) {
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出配置")
    public String export(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            List<AmDirectConnectionConfig> list = configService.exportConfigs(params);
            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
            result.put("exportTime", new Date());
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }
}

