package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetBIIntegrationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算BI集成Controller
 * 
 * @description 预算BI集成接口，支持数据同步、报表集成等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-BI集成"})
@RequestMapping(value = "/accountant/budget/bi/integration")
@Slf4j
public class BudgetBIIntegrationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetBIIntegrationService biIntegrationService;

    /**
     * 同步数据到BI
     */
    @Operation(summary = "同步数据到BI")
    @ApiOperation("同步数据到BI")
    @PostMapping("/sync")
    public MyJsonBean<Map<String, Object>> syncToBI(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> syncResult = biIntegrationService.syncToBI(params);
            result.setCode(1);
            result.setMsg("同步成功");
            result.setData(syncResult);
        } catch (ServiceException ex) {
            log.error("同步数据到BI失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("同步数据到BI异常", e);
            result.setCode(0);
            result.setMsg("同步失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取BI报表列表
     */
    @Operation(summary = "获取BI报表列表")
    @ApiOperation("获取BI报表列表")
    @GetMapping("/reports")
    public MyJsonBean<Map<String, Object>> getBIReports() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> reports = biIntegrationService.getBIReports();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(reports);
        } catch (Exception e) {
            log.error("获取BI报表列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取BI报表详情
     */
    @Operation(summary = "获取BI报表详情")
    @ApiOperation("获取BI报表详情")
    @GetMapping("/report/{reportId}")
    public MyJsonBean<Map<String, Object>> getBIReportDetail(@PathVariable String reportId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> reportDetail = biIntegrationService.getBIReportDetail(reportId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(reportDetail);
        } catch (Exception e) {
            log.error("获取BI报表详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 刷新BI数据
     */
    @Operation(summary = "刷新BI数据")
    @ApiOperation("刷新BI数据")
    @PostMapping("/refresh")
    public MyJsonBean<Map<String, Object>> refreshBIData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> refreshResult = biIntegrationService.refreshBIData(params);
            result.setCode(1);
            result.setMsg("刷新成功");
            result.setData(refreshResult);
        } catch (ServiceException ex) {
            log.error("刷新BI数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("刷新BI数据异常", e);
            result.setCode(0);
            result.setMsg("刷新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 配置BI连接
     */
    @Operation(summary = "配置BI连接")
    @ApiOperation("配置BI连接")
    @PostMapping("/config")
    public MyJsonBean<Void> configureBIConnection(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            biIntegrationService.configureBIConnection(params);
            result.setCode(1);
            result.setMsg("配置成功");
        } catch (ServiceException ex) {
            log.error("配置BI连接失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("配置BI连接异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试BI连接
     */
    @Operation(summary = "测试BI连接")
    @ApiOperation("测试BI连接")
    @PostMapping("/test")
    public MyJsonBean<Map<String, Object>> testBIConnection(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = biIntegrationService.testBIConnection(params);
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("测试BI连接失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("测试BI连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取同步状态
     */
    @Operation(summary = "获取同步状态")
    @ApiOperation("获取同步状态")
    @GetMapping("/sync/status/{taskId}")
    public MyJsonBean<Map<String, Object>> getSyncStatus(@PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> status = biIntegrationService.getSyncStatus(taskId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(status);
        } catch (Exception e) {
            log.error("获取同步状态异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 以下为支持 /accountant/integration/bi/config 路径的接口 ====================

    /**
     * 创建BI集成配置 (integration路径)
     */
    @Operation(summary = "创建BI集成配置")
    @ApiOperation("创建BI集成配置")
    @PostMapping("/accountant/integration/bi/config")
    public MyJsonBean<Void> createConfig(@RequestBody Map<String, Object> params) {
        return configureBIConnection(params);
    }

    /**
     * 查询BI集成配置详情 (integration路径)
     */
    @Operation(summary = "查询BI集成配置详情")
    @ApiOperation("查询BI集成配置详情")
    @GetMapping("/accountant/integration/bi/config/{configId}")
    public MyJsonBean<Map<String, Object>> getConfigDetail(@PathVariable String configId) {
        return getBIReportDetail(configId);
    }

    /**
     * 更新BI集成配置 (integration路径)
     */
    @Operation(summary = "更新BI集成配置")
    @ApiOperation("更新BI集成配置")
    @PutMapping("/accountant/integration/bi/config/{configId}")
    public MyJsonBean<Void> updateConfig(@PathVariable String configId, @RequestBody Map<String, Object> params) {
        params.put("configId", configId);
        return configureBIConnection(params);
    }

    /**
     * 删除BI集成配置 (integration路径)
     */
    @Operation(summary = "删除BI集成配置")
    @ApiOperation("删除BI集成配置")
    @DeleteMapping("/accountant/integration/bi/config/{configId}")
    public MyJsonBean<Void> deleteConfig(@PathVariable String configId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            biIntegrationService.deleteConfig(configId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除BI配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除BI配置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询BI集成配置列表 (integration路径)
     */
    @Operation(summary = "分页查询BI集成配置列表")
    @ApiOperation("分页查询BI集成配置列表")
    @PostMapping("/accountant/integration/bi/config/page")
    public MyJsonBean<Map<String, Object>> getConfigPage(@RequestBody Map<String, Object> params) {
        return getBIReports();
    }

    /**
     * 测试BI连接 (integration路径)
     */
    @Operation(summary = "测试BI连接")
    @ApiOperation("测试BI连接")
    @PostMapping("/accountant/integration/bi/config/{configId}/test-connection")
    public MyJsonBean<Map<String, Object>> testConfigConnection(@PathVariable String configId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("configId", configId);
            Map<String, Object> testResult = biIntegrationService.testBIConnection(params);
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("测试BI连接失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("测试BI连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 推送数据到BI (integration路径)
     */
    @Operation(summary = "推送数据到BI")
    @ApiOperation("推送数据到BI")
    @PostMapping("/accountant/integration/bi/config/{configId}/push")
    public MyJsonBean<Map<String, Object>> pushDataToBI(@PathVariable String configId, @RequestBody Map<String, Object> params) {
        params.put("configId", configId);
        return syncToBI(params);
    }
}

