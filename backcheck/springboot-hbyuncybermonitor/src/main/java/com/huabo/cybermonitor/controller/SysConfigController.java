package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.SysConfig;
import com.huabo.cybermonitor.service.ISysConfigService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.SysConfigQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 系统配置管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="系统配置管理",description="系统配置管理")
@RestController
@RequestMapping("/v1/system/config")
public class SysConfigController {

	private static final Logger log = LoggerFactory.getLogger(SysConfigController.class);

    @Autowired
    private ISysConfigService configService;

    @Operation(summary = "分页查询系统配置列表")
    @PostMapping("/list")
    public R<PageResult<SysConfig>> getConfigList(@RequestBody SysConfigQueryVO queryVO) {
        try {
            IPage<SysConfig> page = configService.getConfigList(queryVO);
            PageResult<SysConfig> pageResult = new PageResult<SysConfig>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());

            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询系统配置列表失败", e);
            return R.fail("查询系统配置列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取系统配置详情")
    @PostMapping("/detail")
    public R<SysConfig> getConfigDetail(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            if (configId == null || configId.trim().isEmpty()) {
                return R.fail("配置ID不能为空");
            }
            
            SysConfig config = configService.getConfigDetail(configId);
            return R.success(config);
        } catch (Exception e) {
            log.error("获取系统配置详情失败", e);
            return R.fail("获取系统配置详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据配置键获取配置值")
    @PostMapping("/value")
    public R<Map<String, Object>> getConfigValue(@RequestBody Map<String, String> params) {
        try {
            String configKey = params.get("configKey");
            String defaultValue = params.get("defaultValue");
            
            if (configKey == null || configKey.trim().isEmpty()) {
                return R.fail("配置键不能为空");
            }
            
            String value = configService.getConfigValue(configKey, defaultValue);

            Map<String, Object> result = new HashMap<>();
            result.put("configKey", configKey);
            result.put("configValue", value != null ? value : "");
            result.put("hasValue", value != null);
            
            return R.success(result);
        } catch (Exception e) {
            log.error("获取配置值失败", e);
            return R.fail("获取配置值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据配置分组查询配置列表")
    @PostMapping("/group")
    public R<List<SysConfig>> getConfigsByGroup(@RequestBody Map<String, String> params) {
        try {
            String configGroup = params.get("configGroup");
            if (configGroup == null || configGroup.trim().isEmpty()) {
                return R.fail("配置分组不能为空");
            }
            
            List<SysConfig> configs = configService.getConfigsByGroup(configGroup);
            return R.success(configs);
        } catch (Exception e) {
            log.error("根据配置分组查询配置列表失败", e);
            return R.fail("根据配置分组查询配置列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据配置类型查询配置列表")
    @PostMapping("/type")
    public R<List<SysConfig>> getConfigsByType(@RequestBody Map<String, String> params) {
        try {
            String configType = params.get("configType");
            if (configType == null || configType.trim().isEmpty()) {
                return R.fail("配置类型不能为空");
            }
            
            List<SysConfig> configs = configService.getConfigsByType(configType);
            return R.success(configs);
        } catch (Exception e) {
            log.error("根据配置类型查询配置列表失败", e);
            return R.fail("根据配置类型查询配置列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询启用的配置列表")
    @PostMapping("/enabled")
    public R<List<SysConfig>> getEnabledConfigs() {
        try {
            List<SysConfig> configs = configService.getEnabledConfigs();
            return R.success(configs);
        } catch (Exception e) {
            log.error("查询启用的配置列表失败", e);
            return R.fail("查询启用的配置列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询系统配置列表")
    @PostMapping("/system")
    public R<List<SysConfig>> getSystemConfigs() {
        try {
            List<SysConfig> configs = configService.getSystemConfigs();
            return R.success(configs);
        } catch (Exception e) {
            log.error("查询系统配置列表失败", e);
            return R.fail("查询系统配置列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增系统配置")
    @PostMapping("/add")
    public R<String> addConfig(@RequestBody SysConfig config) {
        try {
            boolean success = configService.addConfig(config);
            if (success) {
                return R.success("新增系统配置成功");
            } else {
                return R.fail("新增系统配置失败");
            }
        } catch (Exception e) {
            log.error("新增系统配置失败", e);
            return R.fail("新增系统配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新系统配置")
    @PostMapping("/update")
    public R<String> updateConfig(@RequestBody SysConfig config) {
        try {
            if (config.getConfigId() == null || config.getConfigId().trim().isEmpty()) {
                return R.fail("配置ID不能为空");
            }
            
            boolean success = configService.updateConfig(config);
            if (success) {
                return R.success("更新系统配置成功");
            } else {
                return R.fail("更新系统配置失败");
            }
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            return R.fail("更新系统配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新配置值")
    @PostMapping("/update-value")
    public R<String> updateConfigValue(@RequestBody Map<String, String> params) {
        try {
            String configKey = params.get("configKey");
            String configValue = params.get("configValue");
            
            if (configKey == null || configKey.trim().isEmpty()) {
                return R.fail("配置键不能为空");
            }
            
            boolean success = configService.updateConfigValue(configKey, configValue);
            if (success) {
                return R.success("更新配置值成功");
            } else {
                return R.fail("更新配置值失败");
            }
        } catch (Exception e) {
            log.error("更新配置值失败", e);
            return R.fail("更新配置值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新配置值")
    @PostMapping("/batch-update-value")
    public R<String> batchUpdateConfigValue(@RequestBody Map<String, String> configMap) {
        try {
            if (configMap == null || configMap.isEmpty()) {
                return R.fail("配置数据不能为空");
            }
            
            boolean success = configService.batchUpdateConfigValue(configMap);
            if (success) {
                return R.success("批量更新配置值成功");
            } else {
                return R.fail("批量更新配置值失败");
            }
        } catch (Exception e) {
            log.error("批量更新配置值失败", e);
            return R.fail("批量更新配置值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除系统配置")
    @PostMapping("/delete")
    public R<String> deleteConfig(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            if (configId == null || configId.trim().isEmpty()) {
                return R.fail("配置ID不能为空");
            }
            
            boolean success = configService.deleteConfig(configId);
            if (success) {
                return R.success("删除系统配置成功");
            } else {
                return R.fail("删除系统配置失败");
            }
        } catch (Exception e) {
            log.error("删除系统配置失败", e);
            return R.fail("删除系统配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除系统配置")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteConfig(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> configIds = params.get("configIds");
            if (configIds == null || configIds.isEmpty()) {
                return R.fail("配置ID列表不能为空");
            }
            
            boolean success = configService.batchDeleteConfig(configIds);
            if (success) {
                return R.success("批量删除系统配置成功");
            } else {
                return R.fail("批量删除系统配置失败");
            }
        } catch (Exception e) {
            log.error("批量删除系统配置失败", e);
            return R.fail("批量删除系统配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "启用配置")
    @PostMapping("/enable")
    public R<String> enableConfig(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            if (configId == null || configId.trim().isEmpty()) {
                return R.fail("配置ID不能为空");
            }
            
            boolean success = configService.enableConfig(configId);
            if (success) {
                return R.success("启用配置成功");
            } else {
                return R.fail("启用配置失败");
            }
        } catch (Exception e) {
            log.error("启用配置失败", e);
            return R.fail("启用配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "禁用配置")
    @PostMapping("/disable")
    public R<String> disableConfig(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            if (configId == null || configId.trim().isEmpty()) {
                return R.fail("配置ID不能为空");
            }
            
            boolean success = configService.disableConfig(configId);
            if (success) {
                return R.success("禁用配置成功");
            } else {
                return R.fail("禁用配置失败");
            }
        } catch (Exception e) {
            log.error("禁用配置失败", e);
            return R.fail("禁用配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量启用配置")
    @PostMapping("/batch-enable")
    public R<String> batchEnableConfig(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> configIds = params.get("configIds");
            if (configIds == null || configIds.isEmpty()) {
                return R.fail("配置ID列表不能为空");
            }
            
            boolean success = configService.batchEnableConfig(configIds);
            if (success) {
                return R.success("批量启用配置成功");
            } else {
                return R.fail("批量启用配置失败");
            }
        } catch (Exception e) {
            log.error("批量启用配置失败", e);
            return R.fail("批量启用配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量禁用配置")
    @PostMapping("/batch-disable")
    public R<String> batchDisableConfig(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> configIds = params.get("configIds");
            if (configIds == null || configIds.isEmpty()) {
                return R.fail("配置ID列表不能为空");
            }
            
            boolean success = configService.batchDisableConfig(configIds);
            if (success) {
                return R.success("批量禁用配置成功");
            } else {
                return R.fail("批量禁用配置失败");
            }
        } catch (Exception e) {
            log.error("批量禁用配置失败", e);
            return R.fail("批量禁用配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证配置键")
    @PostMapping("/validate-key")
    public R<Map<String, Object>> validateConfigKey(@RequestBody Map<String, String> params) {
        try {
            String configKey = params.get("configKey");
            String excludeId = params.get("excludeId");
            
            if (configKey == null || configKey.trim().isEmpty()) {
                return R.fail("配置键不能为空");
            }
            
            boolean isDuplicate = configService.validateConfigKey(configKey, excludeId);

            Map<String, Object> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            result.put("message", isDuplicate ? "配置键已存在" : "配置键可用");

            return R.success(result);
        } catch (Exception e) {
            log.error("验证配置键失败", e);
            return R.fail("验证配置键失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证配置值格式")
    @PostMapping("/validate-value")
    public R<Map<String, Object>> validateConfigValue(@RequestBody Map<String, String> params) {
        try {
            String configType = params.get("configType");
            String configValue = params.get("configValue");
            
            if (configType == null || configType.trim().isEmpty()) {
                return R.fail("配置类型不能为空");
            }
            
            Map<String, Object> result = configService.validateConfigValue(configType, configValue);
            return R.success(result);
        } catch (Exception e) {
            log.error("验证配置值格式失败", e);
            return R.fail("验证配置值格式失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取数据采集相关配置")
    @PostMapping("/data-collection")
    public R<List<SysConfig>> getDataCollectionConfigs() {
        try {
            List<SysConfig> configs = configService.getDataCollectionConfigs();
            return R.success(configs);
        } catch (Exception e) {
            log.error("获取数据采集相关配置失败", e);
            return R.fail("获取数据采集相关配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知相关配置")
    @PostMapping("/notification")
    public R<List<SysConfig>> getNotificationConfigs() {
        try {
            List<SysConfig> configs = configService.getNotificationConfigs();
            return R.success(configs);
        } catch (Exception e) {
            log.error("获取通知相关配置失败", e);
            return R.fail("获取通知相关配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取报表相关配置")
    @PostMapping("/report")
    public R<List<SysConfig>> getReportConfigs() {
        try {
            List<SysConfig> configs = configService.getReportConfigs();
            return R.success(configs);
        } catch (Exception e) {
            log.error("获取报表相关配置失败", e);
            return R.fail("获取报表相关配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取集成相关配置")
    @PostMapping("/integration")
    public R<List<SysConfig>> getIntegrationConfigs() {
        try {
            List<SysConfig> configs = configService.getIntegrationConfigs();
            return R.success(configs);
        } catch (Exception e) {
            log.error("获取集成相关配置失败", e);
            return R.fail("获取集成相关配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "重置配置为默认值")
    @PostMapping("/reset-default")
    public R<String> resetConfigToDefault(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            if (configId == null || configId.trim().isEmpty()) {
                return R.fail("配置ID不能为空");
            }
            
            boolean success = configService.resetConfigToDefault(configId);
            if (success) {
                return R.success("重置配置为默认值成功");
            } else {
                return R.fail("重置配置为默认值失败");
            }
        } catch (Exception e) {
            log.error("重置配置为默认值失败", e);
            return R.fail("重置配置为默认值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量重置配置为默认值")
    @PostMapping("/batch-reset-default")
    public R<String> batchResetConfigToDefault(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> configIds = params.get("configIds");
            if (configIds == null || configIds.isEmpty()) {
                return R.fail("配置ID列表不能为空");
            }
            
            boolean success = configService.batchResetConfigToDefault(configIds);
            if (success) {
                return R.success("批量重置配置为默认值成功");
            } else {
                return R.fail("批量重置配置为默认值失败");
            }
        } catch (Exception e) {
            log.error("批量重置配置为默认值失败", e);
            return R.fail("批量重置配置为默认值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "刷新配置缓存")
    @PostMapping("/refresh-cache")
    public R<String> refreshConfigCache() {
        try {
            boolean success = configService.refreshConfigCache();
            if (success) {
                return R.success("刷新配置缓存成功");
            } else {
                return R.fail("刷新配置缓存失败");
            }
        } catch (Exception e) {
            log.error("刷新配置缓存失败", e);
            return R.fail("刷新配置缓存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取配置缓存信息")
    @PostMapping("/cache-info")
    public R<Map<String, Object>> getConfigCacheInfo() {
        try {
            Map<String, Object> info = configService.getConfigCacheInfo();
            return R.success(info);
        } catch (Exception e) {
            log.error("获取配置缓存信息失败", e);
            return R.fail("获取配置缓存信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "清空配置缓存")
    @PostMapping("/clear-cache")
    public R<String> clearConfigCache() {
        try {
            boolean success = configService.clearConfigCache();
            if (success) {
                return R.success("清空配置缓存成功");
            } else {
                return R.fail("清空配置缓存失败");
            }
        } catch (Exception e) {
            log.error("清空配置缓存失败", e);
            return R.fail("清空配置缓存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取配置变更历史")
    @PostMapping("/change-history")
    public R<List<Map<String, Object>>> getConfigChangeHistory(@RequestBody Map<String, String> params) {
        try {
            String configKey = params.get("configKey");
            if (configKey == null || configKey.trim().isEmpty()) {
                return R.fail("配置键不能为空");
            }
            
            List<Map<String, Object>> history = configService.getConfigChangeHistory(configKey);
            return R.success(history);
        } catch (Exception e) {
            log.error("获取配置变更历史失败", e);
            return R.fail("获取配置变更历史失败：" + e.getMessage());
        }
    }

    @Operation(summary = "备份配置")
    @PostMapping("/backup")
    public R<Map<String, Object>> backupConfigs() {
        try {
            Map<String, Object> result = configService.backupConfigs();
            return R.success(result);
        } catch (Exception e) {
            log.error("备份配置失败", e);
            return R.fail("备份配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "恢复配置")
    @PostMapping("/restore")
    public R<Map<String, Object>> restoreConfigs(@RequestBody Map<String, String> params) {
        try {
            String backupData = params.get("backupData");
            if (backupData == null || backupData.trim().isEmpty()) {
                return R.fail("备份数据不能为空");
            }
            
            Map<String, Object> result = configService.restoreConfigs(backupData);
            return R.success(result);
        } catch (Exception e) {
            log.error("恢复配置失败", e);
            return R.fail("恢复配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取配置统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getConfigStatistics() {
        try {
            Map<String, Object> statistics = configService.getConfigStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取配置统计信息失败", e);
            return R.fail("获取配置统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取配置分组分布统计")
    @PostMapping("/statistics/group-distribution")
    public R<List<Map<String, Object>>> getConfigGroupDistribution() {
        try {
            List<Map<String, Object>> distribution = configService.getConfigGroupDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取配置分组分布统计失败", e);
            return R.fail("获取配置分组分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取配置类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getConfigTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = configService.getConfigTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取配置类型分布统计失败", e);
            return R.fail("获取配置类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "检查配置健康状态")
    @PostMapping("/health-check")
    public R<Map<String, Object>> checkConfigHealth() {
        try {
            Map<String, Object> health = configService.checkConfigHealth();
            return R.success(health);
        } catch (Exception e) {
            log.error("检查配置健康状态失败", e);
            return R.fail("检查配置健康状态失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出配置列表")
    @PostMapping("/export")
    public void exportConfigList(@RequestBody SysConfigQueryVO queryVO, HttpServletResponse response) {
        try {
            configService.exportConfigList(queryVO, response);
        } catch (Exception e) {
            log.error("导出配置列表失败", e);
            throw new RuntimeException("导出配置列表失败：" + e.getMessage());
        }
    }
}
