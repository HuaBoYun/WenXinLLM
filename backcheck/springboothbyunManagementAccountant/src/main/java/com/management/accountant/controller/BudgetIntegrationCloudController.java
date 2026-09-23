package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationCloudService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算云平台集成Controller
 * 
 * @description 预算云平台集成接口，支持阿里云、腾讯云、AWS等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-云平台集成"})
@RequestMapping(value = "/accountant/budget/integration/cloud")
@Slf4j
public class BudgetIntegrationCloudController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationCloudService integrationCloudService;

    /**
     * 配置云平台
     */
    @Operation(summary = "配置云平台")
    @ApiOperation("配置云平台")
    @PostMapping("/config")
    public MyJsonBean<Map<String, Object>> configCloud(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> config = integrationCloudService.configCloud(params);
            result.setCode(1);
            result.setMsg("配置成功");
            result.setData(config);
        } catch (ServiceException ex) {
            log.error("配置云平台失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("配置云平台异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 对象存储操作
     */
    @Operation(summary = "对象存储操作")
    @ApiOperation("对象存储操作")
    @PostMapping("/storage/operate")
    public MyJsonBean<Map<String, Object>> operateStorage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> storage = integrationCloudService.operateStorage(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(storage);
        } catch (ServiceException ex) {
            log.error("对象存储操作失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("对象存储操作异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 云数据库操作
     */
    @Operation(summary = "云数据库操作")
    @ApiOperation("云数据库操作")
    @PostMapping("/database/operate")
    public MyJsonBean<Map<String, Object>> operateDatabase(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> database = integrationCloudService.operateDatabase(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(database);
        } catch (ServiceException ex) {
            log.error("云数据库操作失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("云数据库操作异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 云函数调用
     */
    @Operation(summary = "云函数调用")
    @ApiOperation("云函数调用")
    @PostMapping("/function/invoke")
    public MyJsonBean<Map<String, Object>> invokeFunction(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> function = integrationCloudService.invokeFunction(params);
            result.setCode(1);
            result.setMsg("调用成功");
            result.setData(function);
        } catch (ServiceException ex) {
            log.error("云函数调用失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("云函数调用异常", e);
            result.setCode(0);
            result.setMsg("调用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 云监控
     */
    @Operation(summary = "云监控")
    @ApiOperation("云监控")
    @PostMapping("/monitor")
    public MyJsonBean<Map<String, Object>> monitorCloud(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> monitoring = integrationCloudService.monitorCloud(params);
            result.setCode(1);
            result.setMsg("监控成功");
            result.setData(monitoring);
        } catch (ServiceException ex) {
            log.error("云监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("云监控异常", e);
            result.setCode(0);
            result.setMsg("监控失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 云日志查询
     */
    @Operation(summary = "云日志查询")
    @ApiOperation("云日志查询")
    @PostMapping("/log/query")
    public MyJsonBean<Map<String, Object>> queryLog(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> logs = integrationCloudService.queryLog(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logs);
        } catch (ServiceException ex) {
            log.error("云日志查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("云日志查询异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 云资源管理
     */
    @Operation(summary = "云资源管理")
    @ApiOperation("云资源管理")
    @PostMapping("/resource/manage")
    public MyJsonBean<Map<String, Object>> manageResource(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> resource = integrationCloudService.manageResource(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(resource);
        } catch (ServiceException ex) {
            log.error("云资源管理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("云资源管理异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }
}

