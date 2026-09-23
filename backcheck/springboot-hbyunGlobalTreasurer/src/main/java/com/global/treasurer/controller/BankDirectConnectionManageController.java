package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcBankDirectConnection;
import com.global.treasurer.service.TcBankDirectConnectionService;
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
@RequestMapping("/settlement/bank-direct-connection")
@Api(tags = "银行直连管理")
public class BankDirectConnectionManageController {
    @Resource
    private TcBankDirectConnectionService connectionService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询连接列表")
    public String getPage(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            PageInfo<TcBankDirectConnection> pageInfo = connectionService.list(pageNum, pageSize, params);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{configId}")
    @ApiOperation("获取连接详情")
    public String getDetail(@PathVariable String configId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            TcBankDirectConnection connection = connectionService.getById(configId);
            if (connection == null) return new JsonBean(0, "连接不存在", null).toJson();
            return JsonBean.success(connection);
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增连接")
    public String add(@FlexibleRequestBody TcBankDirectConnection connection) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            connection.setCreateUser(loginStaff.getStaffid().toString());
            boolean result = connectionService.save(connection);
            return result ? JsonBean.success("创建成功") : new JsonBean(0, "创建失败", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("更新连接")
    public String update(@FlexibleRequestBody TcBankDirectConnection connection) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            connection.setUpdateUser(loginStaff.getStaffid().toString());
            boolean result = connectionService.update(connection);
            return result ? JsonBean.success("更新成功") : new JsonBean(0, "更新失败", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/delete/{configId}")
    @ApiOperation("删除连接")
    public String delete(@PathVariable String configId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            boolean result = connectionService.delete(configId);
            return result ? JsonBean.success("删除成功") : new JsonBean(0, "删除失败", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-test")
    @ApiOperation("批量测试连接")
    public String batchTestConnection() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            Map<String, Object> result = connectionService.batchTestConnection();
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "批量测试失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取连接统计")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            return JsonBean.success(connectionService.getStatistics());
        } catch (Exception e) {
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出配置")
    public String export(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            List<TcBankDirectConnection> list = connectionService.exportConnections(params);
            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
            result.put("exportTime", new Date());
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }
}

