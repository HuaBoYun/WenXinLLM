package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationConfigService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算集成配置Controller
 * 
 * @description 预算集成配置接口，支持集成参数配置、连接管理、映射配置等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-集成配置"})
@RequestMapping(value = "/accountant/budget/integration/config")
@Slf4j
public class BudgetIntegrationConfigController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationConfigService integrationConfigService;

    /**
     * 创建集成配置
     */
    @Operation(summary = "创建集成配置")
    @ApiOperation("创建集成配置")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createConfig(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> config = integrationConfigService.createConfig(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(config);
        } catch (ServiceException ex) {
            log.error("创建集成配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建集成配置异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新集成配置
     */
    @Operation(summary = "更新集成配置")
    @ApiOperation("更新集成配置")
    @PostMapping("/update")
    public MyJsonBean<Map<String, Object>> updateConfig(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> config = integrationConfigService.updateConfig(params);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(config);
        } catch (ServiceException ex) {
            log.error("更新集成配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新集成配置异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除集成配置
     */
    @Operation(summary = "删除集成配置")
    @ApiOperation("删除集成配置")
    @PostMapping("/delete")
    public MyJsonBean<Map<String, Object>> deleteConfig(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> deleteResult = integrationConfigService.deleteConfig(params);
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(deleteResult);
        } catch (ServiceException ex) {
            log.error("删除集成配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除集成配置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询集成配置
     */
    @Operation(summary = "查询集成配置")
    @ApiOperation("查询集成配置")
    @PostMapping("/query")
    public MyJsonBean<Map<String, Object>> queryConfig(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> config = integrationConfigService.queryConfig(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(config);
        } catch (ServiceException ex) {
            log.error("查询集成配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("查询集成配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试集成配置
     */
    @Operation(summary = "测试集成配置")
    @ApiOperation("测试集成配置")
    @PostMapping("/test")
    public MyJsonBean<Map<String, Object>> testConfig(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = integrationConfigService.testConfig(params);
            result.setCode(1);
            result.setMsg("测试成功");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("测试集成配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("测试集成配置异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 字段映射配置
     */
    @Operation(summary = "字段映射配置")
    @ApiOperation("字段映射配置")
    @PostMapping("/mapping")
    public MyJsonBean<Map<String, Object>> configMapping(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> mapping = integrationConfigService.configMapping(params);
            result.setCode(1);
            result.setMsg("配置成功");
            result.setData(mapping);
        } catch (ServiceException ex) {
            log.error("字段映射配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("字段映射配置异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 调度配置
     */
    @Operation(summary = "调度配置")
    @ApiOperation("调度配置")
    @PostMapping("/schedule")
    public MyJsonBean<Map<String, Object>> configSchedule(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> schedule = integrationConfigService.configSchedule(params);
            result.setCode(1);
            result.setMsg("配置成功");
            result.setData(schedule);
        } catch (ServiceException ex) {
            log.error("调度配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("调度配置异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }
}

