package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationApiService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算API集成Controller
 * 
 * @description 预算API集成接口，支持RESTful API、GraphQL、WebSocket等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-API集成"})
@RequestMapping(value = "/accountant/budget/integration/api")
@Slf4j
public class BudgetIntegrationApiController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationApiService integrationApiService;

    /**
     * 注册API
     */
    @Operation(summary = "注册API")
    @ApiOperation("注册API")
    @PostMapping("/register")
    public MyJsonBean<Map<String, Object>> registerApi(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> api = integrationApiService.registerApi(params);
            result.setCode(1);
            result.setMsg("注册成功");
            result.setData(api);
        } catch (ServiceException ex) {
            log.error("注册API失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("注册API异常", e);
            result.setCode(0);
            result.setMsg("注册失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 调用API
     */
    @Operation(summary = "调用API")
    @ApiOperation("调用API")
    @PostMapping("/invoke")
    public MyJsonBean<Map<String, Object>> invokeApi(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> response = integrationApiService.invokeApi(params);
            result.setCode(1);
            result.setMsg("调用成功");
            result.setData(response);
        } catch (ServiceException ex) {
            log.error("调用API失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("调用API异常", e);
            result.setCode(0);
            result.setMsg("调用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * API认证
     */
    @Operation(summary = "API认证")
    @ApiOperation("API认证")
    @PostMapping("/authenticate")
    public MyJsonBean<Map<String, Object>> authenticateApi(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> auth = integrationApiService.authenticateApi(params);
            result.setCode(1);
            result.setMsg("认证成功");
            result.setData(auth);
        } catch (ServiceException ex) {
            log.error("API认证失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("API认证异常", e);
            result.setCode(0);
            result.setMsg("认证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * API监控
     */
    @Operation(summary = "API监控")
    @ApiOperation("API监控")
    @PostMapping("/monitor")
    public MyJsonBean<Map<String, Object>> monitorApi(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> monitoring = integrationApiService.monitorApi(params);
            result.setCode(1);
            result.setMsg("监控成功");
            result.setData(monitoring);
        } catch (ServiceException ex) {
            log.error("API监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("API监控异常", e);
            result.setCode(0);
            result.setMsg("监控失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * API文档生成
     */
    @Operation(summary = "API文档生成")
    @ApiOperation("API文档生成")
    @PostMapping("/documentation")
    public MyJsonBean<Map<String, Object>> generateDocumentation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> documentation = integrationApiService.generateDocumentation(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(documentation);
        } catch (ServiceException ex) {
            log.error("API文档生成失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("API文档生成异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * API版本管理
     */
    @Operation(summary = "API版本管理")
    @ApiOperation("API版本管理")
    @PostMapping("/version/manage")
    public MyJsonBean<Map<String, Object>> manageVersion(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> version = integrationApiService.manageVersion(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(version);
        } catch (ServiceException ex) {
            log.error("API版本管理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("API版本管理异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * API限流控制
     */
    @Operation(summary = "API限流控制")
    @ApiOperation("API限流控制")
    @PostMapping("/rate-limit")
    public MyJsonBean<Map<String, Object>> rateLimit(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> limit = integrationApiService.rateLimit(params);
            result.setCode(1);
            result.setMsg("设置成功");
            result.setData(limit);
        } catch (ServiceException ex) {
            log.error("API限流控制失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("API限流控制异常", e);
            result.setCode(0);
            result.setMsg("设置失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 以下为支持 /accountant/integration/third-party/config 路径的接口 ====================

    /**
     * 创建第三方集成配置 (integration路径)
     */
    @Operation(summary = "创建第三方集成配置")
    @ApiOperation("创建第三方集成配置")
    @PostMapping("/accountant/integration/third-party/config")
    public MyJsonBean<Map<String, Object>> createThirdPartyConfig(@RequestBody Map<String, Object> params) {
        return registerApi(params);
    }

    /**
     * 查询第三方集成配置详情 (integration路径)
     */
    @Operation(summary = "查询第三方集成配置详情")
    @ApiOperation("查询第三方集成配置详情")
    @GetMapping("/accountant/integration/third-party/config/{configId}")
    public MyJsonBean<Map<String, Object>> getThirdPartyConfigDetail(@PathVariable String configId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("configId", configId);
            Map<String, Object> config = integrationApiService.getConfig(configId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(config);
        } catch (ServiceException ex) {
            log.error("查询第三方配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("查询第三方配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新第三方集成配置 (integration路径)
     */
    @Operation(summary = "更新第三方集成配置")
    @ApiOperation("更新第三方集成配置")
    @PutMapping("/accountant/integration/third-party/config/{configId}")
    public MyJsonBean<Map<String, Object>> updateThirdPartyConfig(@PathVariable String configId, @RequestBody Map<String, Object> params) {
        params.put("configId", configId);
        return registerApi(params);
    }

    /**
     * 删除第三方集成配置 (integration路径)
     */
    @Operation(summary = "删除第三方集成配置")
    @ApiOperation("删除第三方集成配置")
    @DeleteMapping("/accountant/integration/third-party/config/{configId}")
    public MyJsonBean<Void> deleteThirdPartyConfig(@PathVariable String configId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            integrationApiService.deleteConfig(configId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除第三方配置失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除第三方配置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询第三方集成配置列表 (integration路径)
     */
    @Operation(summary = "分页查询第三方集成配置列表")
    @ApiOperation("分页查询第三方集成配置列表")
    @PostMapping("/accountant/integration/third-party/config/page")
    public MyJsonBean<Map<String, Object>> getThirdPartyConfigPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> pageData = integrationApiService.getConfigPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageData);
        } catch (Exception e) {
            log.error("分页查询第三方配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试第三方连接 (integration路径)
     */
    @Operation(summary = "测试第三方连接")
    @ApiOperation("测试第三方连接")
    @PostMapping("/accountant/integration/third-party/config/{configId}/test-connection")
    public MyJsonBean<Map<String, Object>> testThirdPartyConnection(@PathVariable String configId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("configId", configId);
            Map<String, Object> testResult = integrationApiService.testConnection(configId);
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("测试第三方连接失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("测试第三方连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }
}

