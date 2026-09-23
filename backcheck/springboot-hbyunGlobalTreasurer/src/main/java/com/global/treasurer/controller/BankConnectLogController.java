package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcBankConnectLog;
import com.global.treasurer.service.TcBankConnectLogService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;

/**
 * 银企直连日志管理控制器
 * @author AI Assistant
 * @date 2025-01-26
 */
@RestController
@RequestMapping("/settlement/bank-connect-log")
@Api(tags = "银企直连日志管理")
public class BankConnectLogController {
    @Resource
    private TcBankConnectLogService bankConnectLogService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/page")
    @ApiOperation("分页查询日志列表")
    public String getPage(@RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            PageInfo<TcBankConnectLog> pageInfo = bankConnectLogService.list(pageNum, pageSize, params);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{logId}")
    @ApiOperation("获取日志详情")
    public String getDetail(@PathVariable String logId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            TcBankConnectLog log = bankConnectLogService.getById(logId);
            if (log == null) return new JsonBean(0, "日志不存在", null).toJson();
            return JsonBean.success(log);
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取日志统计")
    public String getStatistics() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            return JsonBean.success(bankConnectLogService.getStatistics());
        } catch (Exception e) {
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/retry/{logId}")
    @ApiOperation("重试请求")
    public String retryRequest(@PathVariable String logId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            Map<String, Object> result = bankConnectLogService.retryRequest(logId);
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "重试失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出日志")
    public String export(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            List<TcBankConnectLog> list = bankConnectLogService.exportLogs(params);
            Map<String, Object> result = new HashMap<>();
            result.put("data", list);
            result.put("exportTime", new Date());
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/clean")
    @ApiOperation("清理历史日志")
    public String cleanHistoryLogs(@RequestParam(defaultValue = "30") Integer days) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) return new JsonBean(401, "用户已失效", null).toJson();
            int count = bankConnectLogService.cleanHistoryLogs(days);
            Map<String, Object> result = new HashMap<>();
            result.put("deletedCount", count);
            result.put("message", "成功清理" + count + "条历史日志");
            return JsonBean.success(result);
        } catch (Exception e) {
            return new JsonBean(0, "清理失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/interface-types")
    @ApiOperation("获取接口类型列表")
    public String getInterfaceTypes() {
        try {
            List<Map<String, String>> types = new ArrayList<>();
            types.add(createOption("BALANCE_QUERY", "余额查询"));
            types.add(createOption("TRANSACTION_DETAIL", "交易明细"));
            types.add(createOption("PAYMENT", "支付"));
            types.add(createOption("TRANSFER", "转账"));
            types.add(createOption("RECONCILIATION", "对账"));
            return JsonBean.success(types);
        } catch (Exception e) {
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

