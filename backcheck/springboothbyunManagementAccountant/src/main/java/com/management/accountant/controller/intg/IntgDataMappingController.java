package com.management.accountant.controller.intg;

import com.management.accountant.common.J8;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.intg.IntgDataMapping;
import com.management.accountant.service.intg.IntgDataMappingService;
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
 * 数据映射配置控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/intg/data-mapping")
@Api(tags = "数据映射配置管理")
public class IntgDataMappingController {

    @Autowired
    private IntgDataMappingService dataMappingService;

    // 基础CRUD操作

    @PostMapping
    @ApiOperation("创建数据映射")
    public ResponseEntity<Map<String, Object>> createDataMapping(
            @ApiParam("数据映射信息") @Valid @RequestBody IntgDataMapping dataMapping) {
        try {
            IntgDataMapping result = dataMappingService.createDataMapping(dataMapping);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "数据映射创建成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("创建数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "创建数据映射失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{mappingId}")
    @ApiOperation("更新数据映射")
    public ResponseEntity<Map<String, Object>> updateDataMapping(
            @ApiParam("映射ID") @PathVariable String mappingId,
            @ApiParam("数据映射信息") @Valid @RequestBody IntgDataMapping dataMapping) {
        try {
            dataMapping.setMappingId(mappingId);
            IntgDataMapping result = dataMappingService.updateDataMapping(dataMapping);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "数据映射更新成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("更新数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "更新数据映射失败: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{mappingId}")
    @ApiOperation("删除数据映射")
    public ResponseEntity<Map<String, Object>> deleteDataMapping(
            @ApiParam("映射ID") @PathVariable String mappingId) {
        try {
            boolean result = dataMappingService.deleteDataMapping(mappingId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "数据映射删除成功" : "数据映射删除失败"
            ));
        } catch (Exception e) {
            log.error("删除数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "删除数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{mappingId}")
    @ApiOperation("根据ID获取数据映射")
    public ResponseEntity<Map<String, Object>> getDataMappingById(
            @ApiParam("映射ID") @PathVariable String mappingId) {
        try {
            IntgDataMapping result = dataMappingService.getDataMappingById(mappingId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/code/{mappingCode}")
    @ApiOperation("根据编码获取数据映射")
    public ResponseEntity<Map<String, Object>> getDataMappingByCode(
            @ApiParam("映射编码") @PathVariable String mappingCode) {
        try {
            IntgDataMapping result = dataMappingService.getDataMappingByCode(mappingCode);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取数据映射失败: " + e.getMessage()
            ));
        }
    }

    // 查询操作

    @GetMapping("/page")
    @ApiOperation("分页查询数据映射")
    public ResponseEntity<Map<String, Object>> getDataMappingPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            IPage<IntgDataMapping> result = dataMappingService.getDataMappingPage(current, size, params);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/config/{configId}")
    @ApiOperation("根据系统配置ID查询映射列表")
    public ResponseEntity<Map<String, Object>> getDataMappingsByConfigId(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            List<IntgDataMapping> result = dataMappingService.getDataMappingsByConfigId(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/type/{mappingType}")
    @ApiOperation("根据映射类型查询映射列表")
    public ResponseEntity<Map<String, Object>> getDataMappingsByType(
            @ApiParam("映射类型") @PathVariable String mappingType) {
        try {
            List<IntgDataMapping> result = dataMappingService.getDataMappingsByType(mappingType);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/direction/{mappingDirection}")
    @ApiOperation("根据映射方向查询映射列表")
    public ResponseEntity<Map<String, Object>> getDataMappingsByDirection(
            @ApiParam("映射方向") @PathVariable String mappingDirection) {
        try {
            List<IntgDataMapping> result = dataMappingService.getDataMappingsByDirection(mappingDirection);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/source-table/{sourceTableName}")
    @ApiOperation("根据源表名查询映射列表")
    public ResponseEntity<Map<String, Object>> getDataMappingsBySourceTable(
            @ApiParam("源表名") @PathVariable String sourceTableName) {
        try {
            List<IntgDataMapping> result = dataMappingService.getDataMappingsBySourceTable(sourceTableName);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/target-table/{targetTableName}")
    @ApiOperation("根据目标表名查询映射列表")
    public ResponseEntity<Map<String, Object>> getDataMappingsByTargetTable(
            @ApiParam("目标表名") @PathVariable String targetTableName) {
        try {
            List<IntgDataMapping> result = dataMappingService.getDataMappingsByTargetTable(targetTableName);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询数据映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询数据映射失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/enabled/{configId}")
    @ApiOperation("查询启用的映射列表")
    public ResponseEntity<Map<String, Object>> getEnabledDataMappings(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            List<IntgDataMapping> result = dataMappingService.getEnabledDataMappings(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询启用映射成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询启用映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询启用映射失败: " + e.getMessage()
            ));
        }
    }

    // 映射管理操作

    @PutMapping("/{mappingId}/enable")
    @ApiOperation("启用数据映射")
    public ResponseEntity<Map<String, Object>> enableDataMapping(
            @ApiParam("映射ID") @PathVariable String mappingId) {
        try {
            boolean result = dataMappingService.enableDataMapping(mappingId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "数据映射启用成功" : "数据映射启用失败"
            ));
        } catch (Exception e) {
            log.error("启用数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "启用数据映射失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{mappingId}/disable")
    @ApiOperation("禁用数据映射")
    public ResponseEntity<Map<String, Object>> disableDataMapping(
            @ApiParam("映射ID") @PathVariable String mappingId) {
        try {
            boolean result = dataMappingService.disableDataMapping(mappingId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "数据映射禁用成功" : "数据映射禁用失败"
            ));
        } catch (Exception e) {
            log.error("禁用数据映射失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "禁用数据映射失败: " + e.getMessage()
            ));
        }
    }

    // TODO: 继续实现其他API接口
    // 由于接口太多，这里只实现了部分核心接口
    // 其他接口的实现可以根据具体需求逐步完善
}
