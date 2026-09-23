package com.management.accountant.controller.mobile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileAppConfig;
import com.management.accountant.service.mobile.MobileAppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动应用配置控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/mobile/app-config")
@Api(tags = "移动应用配置管理")
public class MobileAppConfigController {

    @Autowired
    private MobileAppConfigService mobileAppConfigService;

    // CRUD操作

    @PostMapping("/create")
    @ApiOperation("创建应用配置")
    public Map<String, Object> createAppConfig(@RequestBody MobileAppConfig appConfig) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.createAppConfig(appConfig);
            result.put("success", success);
            result.put("message", success ? "创建成功" : "创建失败");
            result.put("data", success ? appConfig : null);
        } catch (Exception e) {
            log.error("创建应用配置失败", e);
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    @ApiOperation("更新应用配置")
    public Map<String, Object> updateAppConfig(@RequestBody MobileAppConfig appConfig) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.updateAppConfig(appConfig);
            result.put("success", success);
            result.put("message", success ? "更新成功" : "更新失败");
            result.put("data", success ? appConfig : null);
        } catch (Exception e) {
            log.error("更新应用配置失败", e);
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete/{appConfigId}")
    @ApiOperation("删除应用配置")
    public Map<String, Object> deleteAppConfig(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.deleteAppConfig(appConfigId);
            result.put("success", success);
            result.put("message", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            log.error("删除应用配置失败", e);
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get/{appConfigId}")
    @ApiOperation("根据ID获取应用配置")
    public Map<String, Object> getAppConfigById(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            MobileAppConfig appConfig = mobileAppConfigService.getAppConfigById(appConfigId);
            result.put("success", appConfig != null);
            result.put("message", appConfig != null ? "获取成功" : "应用配置不存在");
            result.put("data", appConfig);
        } catch (Exception e) {
            log.error("获取应用配置失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get-by-code/{appCode}")
    @ApiOperation("根据应用编码获取应用配置")
    public Map<String, Object> getAppConfigByCode(@PathVariable String appCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            MobileAppConfig appConfig = mobileAppConfigService.getAppConfigByCode(appCode);
            result.put("success", appConfig != null);
            result.put("message", appConfig != null ? "获取成功" : "应用配置不存在");
            result.put("data", appConfig);
        } catch (Exception e) {
            log.error("根据应用编码获取应用配置失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 查询操作

    @GetMapping("/list-by-type/{appType}")
    @ApiOperation("根据应用类型获取应用列表")
    public Map<String, Object> getAppConfigsByType(@PathVariable String appType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.getAppConfigsByType(appType);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("根据应用类型获取应用列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-platform/{appPlatform}")
    @ApiOperation("根据应用平台获取应用列表")
    public Map<String, Object> getAppConfigsByPlatform(@PathVariable String appPlatform) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.getAppConfigsByPlatform(appPlatform);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("根据应用平台获取应用列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-category/{appCategory}")
    @ApiOperation("根据应用分类获取应用列表")
    public Map<String, Object> getAppConfigsByCategory(@PathVariable String appCategory) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.getAppConfigsByCategory(appCategory);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("根据应用分类获取应用列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-status/{status}")
    @ApiOperation("根据状态获取应用列表")
    public Map<String, Object> getAppConfigsByStatus(@PathVariable String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.getAppConfigsByStatus(status);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("根据状态获取应用列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-enabled")
    @ApiOperation("获取启用的应用列表")
    public Map<String, Object> getEnabledAppConfigs() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.getEnabledAppConfigs();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("获取启用的应用列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get-default")
    @ApiOperation("获取默认应用")
    public Map<String, Object> getDefaultAppConfig() {
        Map<String, Object> result = new HashMap<>();
        try {
            MobileAppConfig appConfig = mobileAppConfigService.getDefaultAppConfig();
            result.put("success", appConfig != null);
            result.put("message", appConfig != null ? "获取成功" : "未设置默认应用");
            result.put("data", appConfig);
        } catch (Exception e) {
            log.error("获取默认应用失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-published")
    @ApiOperation("获取已发布的应用列表")
    public Map<String, Object> getPublishedAppConfigs() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.getPublishedAppConfigs();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("获取已发布的应用列表失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // 分页查询操作

    @GetMapping("/page")
    @ApiOperation("分页查询应用配置")
    public Map<String, Object> getAppConfigPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String appName,
            @RequestParam(required = false) String appType,
            @RequestParam(required = false) String appPlatform,
            @RequestParam(required = false) String appCategory,
            @RequestParam(required = false) String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<MobileAppConfig> page = new Page<>(current, size);
            Map<String, Object> params = new HashMap<>();
            params.put("appName", appName);
            params.put("appType", appType);
            params.put("appPlatform", appPlatform);
            params.put("appCategory", appCategory);
            params.put("status", status);
            
            IPage<MobileAppConfig> pageResult = mobileAppConfigService.getAppConfigPage(page, params);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", pageResult.getRecords());
            result.put("total", pageResult.getTotal());
            result.put("current", pageResult.getCurrent());
            result.put("size", pageResult.getSize());
            result.put("pages", pageResult.getPages());
        } catch (Exception e) {
            log.error("分页查询应用配置失败", e);
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // 应用管理操作

    @PutMapping("/enable/{appConfigId}")
    @ApiOperation("启用应用")
    public Map<String, Object> enableAppConfig(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.enableAppConfig(appConfigId);
            result.put("success", success);
            result.put("message", success ? "启用成功" : "启用失败");
        } catch (Exception e) {
            log.error("启用应用失败", e);
            result.put("success", false);
            result.put("message", "启用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/disable/{appConfigId}")
    @ApiOperation("禁用应用")
    public Map<String, Object> disableAppConfig(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.disableAppConfig(appConfigId);
            result.put("success", success);
            result.put("message", success ? "禁用成功" : "禁用失败");
        } catch (Exception e) {
            log.error("禁用应用失败", e);
            result.put("success", false);
            result.put("message", "禁用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/publish/{appConfigId}")
    @ApiOperation("发布应用")
    public Map<String, Object> publishAppConfig(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.publishAppConfig(appConfigId);
            result.put("success", success);
            result.put("message", success ? "发布成功" : "发布失败");
        } catch (Exception e) {
            log.error("发布应用失败", e);
            result.put("success", false);
            result.put("message", "发布失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/unpublish/{appConfigId}")
    @ApiOperation("下架应用")
    public Map<String, Object> unpublishAppConfig(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.unpublishAppConfig(appConfigId);
            result.put("success", success);
            result.put("message", success ? "下架成功" : "下架失败");
        } catch (Exception e) {
            log.error("下架应用失败", e);
            result.put("success", false);
            result.put("message", "下架失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/set-default/{appConfigId}")
    @ApiOperation("设置默认应用")
    public Map<String, Object> setDefaultAppConfig(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.setDefaultAppConfig(appConfigId);
            result.put("success", success);
            result.put("message", success ? "设置成功" : "设置失败");
        } catch (Exception e) {
            log.error("设置默认应用失败", e);
            result.put("success", false);
            result.put("message", "设置失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-update-status")
    @ApiOperation("批量更新应用状态")
    public Map<String, Object> batchUpdateStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> appConfigIds = (List<String>) params.get("appConfigIds");
            String status = (String) params.get("status");
            
            boolean success = mobileAppConfigService.batchUpdateStatus(appConfigIds, status);
            result.put("success", success);
            result.put("message", success ? "批量更新成功" : "批量更新失败");
        } catch (Exception e) {
            log.error("批量更新应用状态失败", e);
            result.put("success", false);
            result.put("message", "批量更新失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-enable")
    @ApiOperation("批量启用应用")
    public Map<String, Object> batchEnableAppConfigs(@RequestBody List<String> appConfigIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.batchEnableAppConfigs(appConfigIds);
            result.put("success", success);
            result.put("message", success ? "批量启用成功" : "批量启用失败");
        } catch (Exception e) {
            log.error("批量启用应用失败", e);
            result.put("success", false);
            result.put("message", "批量启用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-disable")
    @ApiOperation("批量禁用应用")
    public Map<String, Object> batchDisableAppConfigs(@RequestBody List<String> appConfigIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.batchDisableAppConfigs(appConfigIds);
            result.put("success", success);
            result.put("message", success ? "批量禁用成功" : "批量禁用失败");
        } catch (Exception e) {
            log.error("批量禁用应用失败", e);
            result.put("success", false);
            result.put("message", "批量禁用失败：" + e.getMessage());
        }
        return result;
    }

    // 应用版本管理

    @GetMapping("/versions/{appCode}")
    @ApiOperation("获取应用的所有版本")
    public Map<String, Object> getAppConfigVersions(@PathVariable String appCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> versions = mobileAppConfigService.getAppConfigVersions(appCode);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", versions);
            result.put("total", versions.size());
        } catch (Exception e) {
            log.error("获取应用版本失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/latest-version/{appCode}")
    @ApiOperation("获取应用的最新版本")
    public Map<String, Object> getLatestAppConfigVersion(@PathVariable String appCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            MobileAppConfig latestVersion = mobileAppConfigService.getLatestAppConfigVersion(appCode);
            result.put("success", latestVersion != null);
            result.put("message", latestVersion != null ? "获取成功" : "未找到版本");
            result.put("data", latestVersion);
        } catch (Exception e) {
            log.error("获取应用最新版本失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create-version")
    @ApiOperation("创建应用版本")
    public Map<String, Object> createAppConfigVersion(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String appConfigId = (String) params.get("appConfigId");
            String newVersion = (String) params.get("newVersion");
            
            boolean success = mobileAppConfigService.createAppConfigVersion(appConfigId, newVersion);
            result.put("success", success);
            result.put("message", success ? "创建版本成功" : "创建版本失败");
        } catch (Exception e) {
            log.error("创建应用版本失败", e);
            result.put("success", false);
            result.put("message", "创建版本失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/publish-version/{appConfigId}")
    @ApiOperation("发布应用版本")
    public Map<String, Object> publishAppConfigVersion(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileAppConfigService.publishAppConfigVersion(appConfigId);
            result.put("success", success);
            result.put("message", success ? "发布版本成功" : "发布版本失败");
        } catch (Exception e) {
            log.error("发布应用版本失败", e);
            result.put("success", false);
            result.put("message", "发布版本失败：" + e.getMessage());
        }
        return result;
    }

    // 应用搜索操作

    @GetMapping("/search-by-keyword")
    @ApiOperation("根据关键词搜索应用")
    public Map<String, Object> searchAppConfigsByKeyword(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.searchAppConfigsByKeyword(keyword);
            result.put("success", true);
            result.put("message", "搜索成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("根据关键词搜索应用失败", e);
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/search-by-tags")
    @ApiOperation("根据标签搜索应用")
    public Map<String, Object> searchAppConfigsByTags(@RequestBody List<String> tags) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileAppConfig> appConfigs = mobileAppConfigService.searchAppConfigsByTags(tags);
            result.put("success", true);
            result.put("message", "搜索成功");
            result.put("data", appConfigs);
            result.put("total", appConfigs.size());
        } catch (Exception e) {
            log.error("根据标签搜索应用失败", e);
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

    // 应用验证操作

    @PostMapping("/validate-config")
    @ApiOperation("验证应用配置")
    public Map<String, Object> validateAppConfig(@RequestBody MobileAppConfig appConfig) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> validationResult = mobileAppConfigService.validateAppConfig(appConfig);
            result.putAll(validationResult);
        } catch (Exception e) {
            log.error("验证应用配置失败", e);
            result.put("success", false);
            result.put("message", "验证失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/test-connection/{appConfigId}")
    @ApiOperation("测试应用连接")
    public Map<String, Object> testAppConfigConnection(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> testResult = mobileAppConfigService.testAppConfigConnection(appConfigId);
            result.putAll(testResult);
        } catch (Exception e) {
            log.error("测试应用连接失败", e);
            result.put("success", false);
            result.put("message", "测试失败：" + e.getMessage());
        }
        return result;
    }

    // 统计操作

    @GetMapping("/count")
    @ApiOperation("统计应用总数")
    public Map<String, Object> countAppConfigs() {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = mobileAppConfigService.countAppConfigs();
            result.put("success", true);
            result.put("message", "统计成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计应用总数失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-type")
    @ApiOperation("按应用类型统计数量")
    public Map<String, Object> countAppConfigsByType() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = mobileAppConfigService.countAppConfigsByType();
            result.put("success", true);
            result.put("message", "统计成功");
            result.put("data", stats);
        } catch (Exception e) {
            log.error("按应用类型统计数量失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-status")
    @ApiOperation("按状态统计数量")
    public Map<String, Object> countAppConfigsByStatus() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> stats = mobileAppConfigService.countAppConfigsByStatus();
            result.put("success", true);
            result.put("message", "统计成功");
            result.put("data", stats);
        } catch (Exception e) {
            log.error("按状态统计数量失败", e);
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
            Map<String, Object> overview = mobileAppConfigService.getSystemOverview();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", overview);
        } catch (Exception e) {
            log.error("获取系统概览信息失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}
