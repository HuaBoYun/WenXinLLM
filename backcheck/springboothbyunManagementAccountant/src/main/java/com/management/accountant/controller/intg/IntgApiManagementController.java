package com.management.accountant.controller.intg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.intg.IntgApiManagement;
import com.management.accountant.service.intg.IntgApiManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API接口管理控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/intg/api-management")
@Api(tags = "API接口管理")
public class IntgApiManagementController {

    @Autowired
    private IntgApiManagementService apiManagementService;

    // 基础CRUD操作

    @PostMapping("/create")
    @ApiOperation("创建API管理")
    public Map<String, Object> createApiManagement(@RequestBody IntgApiManagement apiManagement) {
        Map<String, Object> result = new HashMap<>();
        try {
            IntgApiManagement created = apiManagementService.createApiManagement(apiManagement);
            result.put("success", true);
            result.put("message", "API管理创建成功");
            result.put("data", created);
        } catch (Exception e) {
            log.error("创建API管理失败", e);
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    @ApiOperation("更新API管理")
    public Map<String, Object> updateApiManagement(@RequestBody IntgApiManagement apiManagement) {
        Map<String, Object> result = new HashMap<>();
        try {
            IntgApiManagement updated = apiManagementService.updateApiManagement(apiManagement);
            result.put("success", true);
            result.put("message", "API管理更新成功");
            result.put("data", updated);
        } catch (Exception e) {
            log.error("更新API管理失败", e);
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete/{apiId}")
    @ApiOperation("删除API管理")
    public Map<String, Object> deleteApiManagement(@PathVariable String apiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = apiManagementService.deleteApiManagement(apiId);
            result.put("success", deleted);
            result.put("message", deleted ? "API管理删除成功" : "删除失败");
        } catch (Exception e) {
            log.error("删除API管理失败", e);
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get/{apiId}")
    @ApiOperation("根据ID获取API管理")
    public Map<String, Object> getApiManagementById(@PathVariable String apiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            IntgApiManagement apiManagement = apiManagementService.getApiManagementById(apiId);
            result.put("success", true);
            result.put("data", apiManagement);
        } catch (Exception e) {
            log.error("获取API管理失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get-by-code/{apiCode}")
    @ApiOperation("根据编码获取API管理")
    public Map<String, Object> getApiManagementByCode(@PathVariable String apiCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            IntgApiManagement apiManagement = apiManagementService.getApiManagementByCode(apiCode);
            result.put("success", true);
            result.put("data", apiManagement);
        } catch (Exception e) {
            log.error("根据编码获取API管理失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 查询操作

    @GetMapping("/page")
    @ApiOperation("分页查询API管理")
    public Map<String, Object> getApiManagementPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String apiName,
            @RequestParam(required = false) String apiType,
            @RequestParam(required = false) String apiCategory,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String httpMethod,
            @RequestParam(required = false) String serviceProvider,
            @RequestParam(required = false) String businessDomain) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("apiName", apiName);
            params.put("apiType", apiType);
            params.put("apiCategory", apiCategory);
            params.put("status", status);
            params.put("httpMethod", httpMethod);
            params.put("serviceProvider", serviceProvider);
            params.put("businessDomain", businessDomain);
            
            IPage<IntgApiManagement> page = apiManagementService.getApiManagementPage(current, size, params);
            result.put("success", true);
            result.put("data", page);
        } catch (Exception e) {
            log.error("分页查询API管理失败", e);
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-type/{apiType}")
    @ApiOperation("根据API类型获取列表")
    public Map<String, Object> getApiManagementsByType(@PathVariable String apiType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.getApiManagementsByType(apiType);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("根据API类型获取列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-category/{apiCategory}")
    @ApiOperation("根据API分类获取列表")
    public Map<String, Object> getApiManagementsByCategory(@PathVariable String apiCategory) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.getApiManagementsByCategory(apiCategory);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("根据API分类获取列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-status/{status}")
    @ApiOperation("根据状态获取列表")
    public Map<String, Object> getApiManagementsByStatus(@PathVariable String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.getApiManagementsByStatus(status);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("根据状态获取列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-active")
    @ApiOperation("获取启用的API列表")
    public Map<String, Object> getActiveApiManagements() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.getActiveApiManagements();
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("获取启用的API列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-deprecated")
    @ApiOperation("获取已废弃的API列表")
    public Map<String, Object> getDeprecatedApiManagements() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.getDeprecatedApiManagements();
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("获取已废弃的API列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // API管理操作

    @PutMapping("/enable/{apiId}")
    @ApiOperation("启用API")
    public Map<String, Object> enableApiManagement(@PathVariable String apiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean enabled = apiManagementService.enableApiManagement(apiId);
            result.put("success", enabled);
            result.put("message", enabled ? "API启用成功" : "启用失败");
        } catch (Exception e) {
            log.error("启用API失败", e);
            result.put("success", false);
            result.put("message", "启用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/disable/{apiId}")
    @ApiOperation("禁用API")
    public Map<String, Object> disableApiManagement(@PathVariable String apiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean disabled = apiManagementService.disableApiManagement(apiId);
            result.put("success", disabled);
            result.put("message", disabled ? "API禁用成功" : "禁用失败");
        } catch (Exception e) {
            log.error("禁用API失败", e);
            result.put("success", false);
            result.put("message", "禁用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/deprecate/{apiId}")
    @ApiOperation("设置API为废弃")
    public Map<String, Object> deprecateApiManagement(
            @PathVariable String apiId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime deprecationDate,
            @RequestParam(required = false) String replacementApiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deprecated = apiManagementService.deprecateApiManagement(apiId, deprecationDate, replacementApiId);
            result.put("success", deprecated);
            result.put("message", deprecated ? "API设置为废弃成功" : "设置失败");
        } catch (Exception e) {
            log.error("设置API为废弃失败", e);
            result.put("success", false);
            result.put("message", "设置失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-update-status")
    @ApiOperation("批量更新API状态")
    public Map<String, Object> batchUpdateApiStatus(
            @RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> apiIds = (List<String>) request.get("apiIds");
            String status = (String) request.get("status");
            
            boolean updated = apiManagementService.batchUpdateApiStatus(apiIds, status);
            result.put("success", updated);
            result.put("message", updated ? "批量更新状态成功" : "更新失败");
        } catch (Exception e) {
            log.error("批量更新API状态失败", e);
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-enable")
    @ApiOperation("批量启用API")
    public Map<String, Object> batchEnableApiManagements(@RequestBody List<String> apiIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean enabled = apiManagementService.batchEnableApiManagements(apiIds);
            result.put("success", enabled);
            result.put("message", enabled ? "批量启用成功" : "启用失败");
        } catch (Exception e) {
            log.error("批量启用API失败", e);
            result.put("success", false);
            result.put("message", "启用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-disable")
    @ApiOperation("批量禁用API")
    public Map<String, Object> batchDisableApiManagements(@RequestBody List<String> apiIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean disabled = apiManagementService.batchDisableApiManagements(apiIds);
            result.put("success", disabled);
            result.put("message", disabled ? "批量禁用成功" : "禁用失败");
        } catch (Exception e) {
            log.error("批量禁用API失败", e);
            result.put("success", false);
            result.put("message", "禁用失败：" + e.getMessage());
        }
        return result;
    }

    // API版本管理

    @GetMapping("/versions/{apiCode}")
    @ApiOperation("获取API的所有版本")
    public Map<String, Object> getApiVersions(@PathVariable String apiCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> versions = apiManagementService.getApiVersions(apiCode);
            result.put("success", true);
            result.put("data", versions);
        } catch (Exception e) {
            log.error("获取API版本失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/latest-version/{apiCode}")
    @ApiOperation("获取API的最新版本")
    public Map<String, Object> getLatestApiVersion(@PathVariable String apiCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            IntgApiManagement latestVersion = apiManagementService.getLatestApiVersion(apiCode);
            result.put("success", true);
            result.put("data", latestVersion);
        } catch (Exception e) {
            log.error("获取API最新版本失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create-version")
    @ApiOperation("创建API新版本")
    public Map<String, Object> createApiVersion(
            @RequestParam String baseApiId,
            @RequestParam String newVersion) {
        Map<String, Object> result = new HashMap<>();
        try {
            IntgApiManagement newVersionApi = apiManagementService.createApiVersion(baseApiId, newVersion);
            result.put("success", true);
            result.put("message", "API新版本创建成功");
            result.put("data", newVersionApi);
        } catch (Exception e) {
            log.error("创建API新版本失败", e);
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/publish-version/{apiId}")
    @ApiOperation("发布API版本")
    public Map<String, Object> publishApiVersion(@PathVariable String apiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean published = apiManagementService.publishApiVersion(apiId);
            result.put("success", published);
            result.put("message", published ? "API版本发布成功" : "发布失败");
        } catch (Exception e) {
            log.error("发布API版本失败", e);
            result.put("success", false);
            result.put("message", "发布失败：" + e.getMessage());
        }
        return result;
    }

    // API查找操作

    @GetMapping("/search-by-keyword")
    @ApiOperation("根据关键词搜索API")
    public Map<String, Object> searchApiManagementsByKeyword(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.searchApiManagementsByKeyword(keyword);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("根据关键词搜索API失败", e);
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/search-by-tags")
    @ApiOperation("根据标签搜索API")
    public Map<String, Object> searchApiManagementsByTags(@RequestBody List<String> tags) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<IntgApiManagement> list = apiManagementService.searchApiManagementsByTags(tags);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            log.error("根据标签搜索API失败", e);
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

    // API验证操作

    @PostMapping("/validate-configuration")
    @ApiOperation("验证API配置")
    public Map<String, Object> validateApiConfiguration(@RequestBody IntgApiManagement apiManagement) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> validation = apiManagementService.validateApiConfiguration(apiManagement);
            result.put("success", true);
            result.put("data", validation);
        } catch (Exception e) {
            log.error("验证API配置失败", e);
            result.put("success", false);
            result.put("message", "验证失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/test-connection/{apiId}")
    @ApiOperation("测试API连接")
    public Map<String, Object> testApiConnection(@PathVariable String apiId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> testResult = apiManagementService.testApiConnection(apiId);
            result.put("success", true);
            result.put("data", testResult);
        } catch (Exception e) {
            log.error("测试API连接失败", e);
            result.put("success", false);
            result.put("message", "测试失败：" + e.getMessage());
        }
        return result;
    }

    // 统计分析操作

    @GetMapping("/count")
    @ApiOperation("统计API总数")
    public Map<String, Object> countApiManagements() {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = apiManagementService.countApiManagements();
            result.put("success", true);
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计API总数失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-type")
    @ApiOperation("按API类型统计数量")
    public Map<String, Object> countByApiType() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = apiManagementService.countByApiType();
            result.put("success", true);
            result.put("data", stats);
        } catch (Exception e) {
            log.error("按API类型统计失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-status")
    @ApiOperation("按状态统计数量")
    public Map<String, Object> countByStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = apiManagementService.countByStatus();
            result.put("success", true);
            result.put("data", stats);
        } catch (Exception e) {
            log.error("按状态统计失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/system-overview")
    @ApiOperation("获取系统概览信息")
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> overview = apiManagementService.getSystemOverview();
            result.put("success", true);
            result.put("data", overview);
        } catch (Exception e) {
            log.error("获取系统概览失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}
