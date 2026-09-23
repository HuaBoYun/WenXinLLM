package com.management.accountant.controller.intg;

import com.management.accountant.common.J8;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.intg.IntgSystemConfig;
import com.management.accountant.service.intg.IntgSystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ERP系统集成配置控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/intg/system-config")
@Api(tags = "ERP系统集成配置管理")
public class IntgSystemConfigController {

    @Autowired
    private IntgSystemConfigService systemConfigService;

    // 基础CRUD操作

    @PostMapping
    @ApiOperation("创建系统配置")
    public ResponseEntity<Map<String, Object>> createSystemConfig(
            @ApiParam("系统配置信息") @Valid @RequestBody IntgSystemConfig systemConfig) {
        try {
            IntgSystemConfig result = systemConfigService.createSystemConfig(systemConfig);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "系统配置创建成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("创建系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "创建系统配置失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{configId}")
    @ApiOperation("更新系统配置")
    public ResponseEntity<Map<String, Object>> updateSystemConfig(
            @ApiParam("配置ID") @PathVariable String configId,
            @ApiParam("系统配置信息") @Valid @RequestBody IntgSystemConfig systemConfig) {
        try {
            systemConfig.setConfigId(configId);
            IntgSystemConfig result = systemConfigService.updateSystemConfig(systemConfig);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "系统配置更新成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "更新系统配置失败: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{configId}")
    @ApiOperation("删除系统配置")
    public ResponseEntity<Map<String, Object>> deleteSystemConfig(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            boolean result = systemConfigService.deleteSystemConfig(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "系统配置删除成功" : "系统配置删除失败"
            ));
        } catch (Exception e) {
            log.error("删除系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "删除系统配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{configId}")
    @ApiOperation("根据ID获取系统配置")
    public ResponseEntity<Map<String, Object>> getSystemConfigById(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            IntgSystemConfig result = systemConfigService.getSystemConfigById(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取系统配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取系统配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/code/{configCode}")
    @ApiOperation("根据编码获取系统配置")
    public ResponseEntity<Map<String, Object>> getSystemConfigByCode(
            @ApiParam("配置编码") @PathVariable String configCode) {
        try {
            IntgSystemConfig result = systemConfigService.getSystemConfigByCode(configCode);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取系统配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取系统配置失败: " + e.getMessage()
            ));
        }
    }

    // 查询操作

    @GetMapping("/page")
    @ApiOperation("分页查询系统配置")
    public ResponseEntity<Map<String, Object>> getSystemConfigPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            IPage<IntgSystemConfig> result = systemConfigService.getSystemConfigPage(current, size, params);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询系统配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询系统配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/type/{systemType}")
    @ApiOperation("根据系统类型查询配置列表")
    public ResponseEntity<Map<String, Object>> getSystemConfigsByType(
            @ApiParam("系统类型") @PathVariable String systemType) {
        try {
            List<IntgSystemConfig> result = systemConfigService.getSystemConfigsByType(systemType);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询系统配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询系统配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/connection-type/{connectionType}")
    @ApiOperation("根据连接类型查询配置列表")
    public ResponseEntity<Map<String, Object>> getSystemConfigsByConnectionType(
            @ApiParam("连接类型") @PathVariable String connectionType) {
        try {
            List<IntgSystemConfig> result = systemConfigService.getSystemConfigsByConnectionType(connectionType);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询系统配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询系统配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/status/{configStatus}")
    @ApiOperation("根据配置状态查询配置列表")
    public ResponseEntity<Map<String, Object>> getSystemConfigsByStatus(
            @ApiParam("配置状态") @PathVariable String configStatus) {
        try {
            List<IntgSystemConfig> result = systemConfigService.getSystemConfigsByStatus(configStatus);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询系统配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询系统配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/enabled")
    @ApiOperation("查询启用的配置列表")
    public ResponseEntity<Map<String, Object>> getEnabledSystemConfigs() {
        try {
            List<IntgSystemConfig> result = systemConfigService.getEnabledSystemConfigs();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询启用配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询启用配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询启用配置失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/default/{systemType}")
    @ApiOperation("查询默认配置")
    public ResponseEntity<Map<String, Object>> getDefaultSystemConfig(
            @ApiParam("系统类型") @PathVariable String systemType) {
        try {
            IntgSystemConfig result = systemConfigService.getDefaultSystemConfig(systemType);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询默认配置成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询默认配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询默认配置失败: " + e.getMessage()
            ));
        }
    }

    // 配置管理操作

    @PostMapping("/{configId}/test-connection")
    @ApiOperation("测试连接配置")
    public ResponseEntity<Map<String, Object>> testConnection(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            Map<String, Object> result = systemConfigService.testConnection(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "连接测试完成",
                "data", result
            ));
        } catch (Exception e) {
            log.error("连接测试失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "连接测试失败: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/batch-test-connection")
    @ApiOperation("批量测试连接")
    public ResponseEntity<Map<String, Object>> batchTestConnection(
            @ApiParam("配置ID列表") @RequestBody List<String> configIds) {
        try {
            List<Map<String, Object>> result = systemConfigService.batchTestConnection(configIds);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "批量连接测试完成",
                "data", result
            ));
        } catch (Exception e) {
            log.error("批量连接测试失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "批量连接测试失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{configId}/enable")
    @ApiOperation("启用系统配置")
    public ResponseEntity<Map<String, Object>> enableSystemConfig(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            boolean result = systemConfigService.enableSystemConfig(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "系统配置启用成功" : "系统配置启用失败"
            ));
        } catch (Exception e) {
            log.error("启用系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "启用系统配置失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{configId}/disable")
    @ApiOperation("禁用系统配置")
    public ResponseEntity<Map<String, Object>> disableSystemConfig(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            boolean result = systemConfigService.disableSystemConfig(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "系统配置禁用成功" : "系统配置禁用失败"
            ));
        } catch (Exception e) {
            log.error("禁用系统配置失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "禁用系统配置失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/batch-update-status")
    @ApiOperation("批量更新配置状态")
    public ResponseEntity<Map<String, Object>> batchUpdateConfigStatus(
            @ApiParam("配置ID列表") @RequestParam List<String> configIds,
            @ApiParam("配置状态") @RequestParam String configStatus) {
        try {
            boolean result = systemConfigService.batchUpdateConfigStatus(configIds, configStatus);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "批量更新配置状态成功" : "批量更新配置状态失败"
            ));
        } catch (Exception e) {
            log.error("批量更新配置状态失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "批量更新配置状态失败: " + e.getMessage()
            ));
        }
    }

    // 健康检查操作

    @PostMapping("/{configId}/health-check")
    @ApiOperation("执行健康检查")
    public ResponseEntity<Map<String, Object>> performHealthCheck(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            Map<String, Object> result = systemConfigService.performHealthCheck(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "健康检查完成",
                "data", result
            ));
        } catch (Exception e) {
            log.error("健康检查失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "健康检查失败: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/batch-health-check")
    @ApiOperation("批量健康检查")
    public ResponseEntity<Map<String, Object>> batchPerformHealthCheck(
            @ApiParam("配置ID列表") @RequestBody List<String> configIds) {
        try {
            List<Map<String, Object>> result = systemConfigService.batchPerformHealthCheck(configIds);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "批量健康检查完成",
                "data", result
            ));
        } catch (Exception e) {
            log.error("批量健康检查失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "批量健康检查失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/health-report")
    @ApiOperation("获取系统健康报告")
    public ResponseEntity<Map<String, Object>> getSystemHealthReport() {
        try {
            Map<String, Object> result = systemConfigService.getSystemHealthReport();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取系统健康报告成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取系统健康报告失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取系统健康报告失败: " + e.getMessage()
            ));
        }
    }

    // 统计分析操作

    @GetMapping("/count")
    @ApiOperation("统计配置总数")
    public ResponseEntity<Map<String, Object>> countSystemConfigs() {
        try {
            Long result = systemConfigService.countSystemConfigs();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "统计配置总数成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("统计配置总数失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "统计配置总数失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/count/by-system-type")
    @ApiOperation("按系统类型统计配置数量")
    public ResponseEntity<Map<String, Object>> countBySystemType() {
        try {
            List<Map<String, Object>> result = systemConfigService.countBySystemType();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "按系统类型统计成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("按系统类型统计失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "按系统类型统计失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/count/by-connection-type")
    @ApiOperation("按连接类型统计配置数量")
    public ResponseEntity<Map<String, Object>> countByConnectionType() {
        try {
            List<Map<String, Object>> result = systemConfigService.countByConnectionType();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "按连接类型统计成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("按连接类型统计失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "按连接类型统计失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/count/by-config-status")
    @ApiOperation("按配置状态统计配置数量")
    public ResponseEntity<Map<String, Object>> countByConfigStatus() {
        try {
            List<Map<String, Object>> result = systemConfigService.countByConfigStatus();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "按配置状态统计成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("按配置状态统计失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "按配置状态统计失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/count/by-connection-status")
    @ApiOperation("按连接状态统计配置数量")
    public ResponseEntity<Map<String, Object>> countByConnectionStatus() {
        try {
            List<Map<String, Object>> result = systemConfigService.countByConnectionStatus();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "按连接状态统计成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("按连接状态统计失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "按连接状态统计失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/count/by-health-status")
    @ApiOperation("按健康状态统计配置数量")
    public ResponseEntity<Map<String, Object>> countByHealthStatus() {
        try {
            List<Map<String, Object>> result = systemConfigService.countByHealthStatus();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "按健康状态统计成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("按健康状态统计失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "按健康状态统计失败: " + e.getMessage()
            ));
        }
    }

    // 系统维护操作

    @GetMapping("/overview")
    @ApiOperation("获取系统概览信息")
    public ResponseEntity<Map<String, Object>> getSystemOverview() {
        try {
            Map<String, Object> result = systemConfigService.getSystemOverview();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取系统概览成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取系统概览失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取系统概览失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/report")
    @ApiOperation("生成配置报告")
    public ResponseEntity<Map<String, Object>> generateConfigReport(
            @ApiParam("报告类型") @RequestParam String reportType,
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        try {
            List<Map<String, Object>> result = systemConfigService.generateConfigReport(reportType, startTime, endTime);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "生成配置报告成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("生成配置报告失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "生成配置报告失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/check-health")
    @ApiOperation("检查系统健康状态")
    public ResponseEntity<Map<String, Object>> checkSystemHealth() {
        try {
            Map<String, Object> result = systemConfigService.checkSystemHealth();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "检查系统健康状态成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("检查系统健康状态失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "检查系统健康状态失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/assess-quality")
    @ApiOperation("评估配置质量")
    public ResponseEntity<Map<String, Object>> assessConfigQuality() {
        try {
            List<Map<String, Object>> result = systemConfigService.assessConfigQuality();
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "评估配置质量成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("评估配置质量失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "评估配置质量失败: " + e.getMessage()
            ));
        }
    }
}
