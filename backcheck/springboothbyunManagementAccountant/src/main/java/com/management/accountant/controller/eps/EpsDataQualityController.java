package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsDataQuality;
import com.management.accountant.service.eps.EpsDataQualityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据质量管理控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/eps/data-quality")
@Api(tags = "数据质量管理")
public class EpsDataQualityController {

    @Autowired
    private EpsDataQualityService dataQualityService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建数据质量")
    public Map<String, Object> createDataQuality(@RequestBody EpsDataQuality dataQuality) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.createDataQuality(dataQuality);
            if (success) {
                result.put("success", true);
                result.put("message", "创建数据质量成功");
                result.put("data", dataQuality);
            } else {
                result.put("success", false);
                result.put("message", "创建数据质量失败");
            }
        } catch (Exception e) {
            log.error("创建数据质量异常", e);
            result.put("success", false);
            result.put("message", "创建数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    @ApiOperation("更新数据质量")
    public Map<String, Object> updateDataQuality(@RequestBody EpsDataQuality dataQuality) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.updateDataQuality(dataQuality);
            if (success) {
                result.put("success", true);
                result.put("message", "更新数据质量成功");
                result.put("data", dataQuality);
            } else {
                result.put("success", false);
                result.put("message", "更新数据质量失败");
            }
        } catch (Exception e) {
            log.error("更新数据质量异常", e);
            result.put("success", false);
            result.put("message", "更新数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete/{qualityId}")
    @ApiOperation("删除数据质量")
    public Map<String, Object> deleteDataQuality(@ApiParam("数据质量ID") @PathVariable String qualityId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.deleteDataQuality(qualityId);
            if (success) {
                result.put("success", true);
                result.put("message", "删除数据质量成功");
            } else {
                result.put("success", false);
                result.put("message", "删除数据质量失败");
            }
        } catch (Exception e) {
            log.error("删除数据质量异常", e);
            result.put("success", false);
            result.put("message", "删除数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get/{qualityId}")
    @ApiOperation("根据ID获取数据质量")
    public Map<String, Object> getDataQualityById(@ApiParam("数据质量ID") @PathVariable String qualityId) {
        Map<String, Object> result = new HashMap<>();
        try {
            EpsDataQuality dataQuality = dataQualityService.getDataQualityById(qualityId);
            if (dataQuality != null) {
                result.put("success", true);
                result.put("message", "获取数据质量成功");
                result.put("data", dataQuality);
            } else {
                result.put("success", false);
                result.put("message", "数据质量不存在");
            }
        } catch (Exception e) {
            log.error("获取数据质量异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get-by-code/{qualityCode}")
    @ApiOperation("根据编码获取数据质量")
    public Map<String, Object> getDataQualityByCode(@ApiParam("数据质量编码") @PathVariable String qualityCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            EpsDataQuality dataQuality = dataQualityService.getDataQualityByCode(qualityCode);
            if (dataQuality != null) {
                result.put("success", true);
                result.put("message", "获取数据质量成功");
                result.put("data", dataQuality);
            } else {
                result.put("success", false);
                result.put("message", "数据质量不存在");
            }
        } catch (Exception e) {
            log.error("获取数据质量异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 查询操作 ====================

    @GetMapping("/list-by-type/{qualityType}")
    @ApiOperation("根据质量类型获取数据质量列表")
    public Map<String, Object> getDataQualityByType(@ApiParam("质量类型") @PathVariable String qualityType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getDataQualityByType(qualityType);
            result.put("success", true);
            result.put("message", "获取数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-category/{qualityCategory}")
    @ApiOperation("根据质量分类获取数据质量列表")
    public Map<String, Object> getDataQualityByCategory(@ApiParam("质量分类") @PathVariable String qualityCategory) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getDataQualityByCategory(qualityCategory);
            result.put("success", true);
            result.put("message", "获取数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-module/{qualityModule}")
    @ApiOperation("根据质量模块获取数据质量列表")
    public Map<String, Object> getDataQualityByModule(@ApiParam("质量模块") @PathVariable String qualityModule) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getDataQualityByModule(qualityModule);
            result.put("success", true);
            result.put("message", "获取数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-data-source/{dataSourceId}")
    @ApiOperation("根据数据源ID获取数据质量列表")
    public Map<String, Object> getDataQualityByDataSourceId(@ApiParam("数据源ID") @PathVariable String dataSourceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getDataQualityByDataSourceId(dataSourceId);
            result.put("success", true);
            result.put("message", "获取数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-check-status/{checkStatus}")
    @ApiOperation("根据检查状态获取数据质量列表")
    public Map<String, Object> getDataQualityByCheckStatus(@ApiParam("检查状态") @PathVariable String checkStatus) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getDataQualityByCheckStatus(checkStatus);
            result.put("success", true);
            result.put("message", "获取数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-check-result/{checkResult}")
    @ApiOperation("根据检查结果获取数据质量列表")
    public Map<String, Object> getDataQualityByCheckResult(@ApiParam("检查结果") @PathVariable String checkResult) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getDataQualityByCheckResult(checkResult);
            result.put("success", true);
            result.put("message", "获取数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-pending-check")
    @ApiOperation("获取待检查的数据质量列表")
    public Map<String, Object> getPendingCheckDataQuality() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getPendingCheckDataQuality();
            result.put("success", true);
            result.put("message", "获取待检查数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取待检查数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取待检查数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-failed-check")
    @ApiOperation("获取检查失败的数据质量列表")
    public Map<String, Object> getFailedCheckDataQuality() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getFailedCheckDataQuality();
            result.put("success", true);
            result.put("message", "获取检查失败数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取检查失败数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取检查失败数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-need-fix")
    @ApiOperation("获取需要修复的数据质量列表")
    public Map<String, Object> getNeedFixDataQuality() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.getNeedFixDataQuality();
            result.put("success", true);
            result.put("message", "获取需要修复数据质量列表成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("获取需要修复数据质量列表异常", e);
            result.put("success", false);
            result.put("message", "获取需要修复数据质量列表异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 分页查询操作 ====================

    @GetMapping("/page")
    @ApiOperation("分页查询数据质量")
    public Map<String, Object> getDataQualityPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("查询参数") @RequestParam(required = false) Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<EpsDataQuality> page = new Page<>(pageNum, pageSize);
            IPage<EpsDataQuality> pageResult = dataQualityService.getDataQualityPage(page, params);
            result.put("success", true);
            result.put("message", "分页查询数据质量成功");
            result.put("data", pageResult);
        } catch (Exception e) {
            log.error("分页查询数据质量异常", e);
            result.put("success", false);
            result.put("message", "分页查询数据质量异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据质量检查操作 ====================

    @PostMapping("/execute-check/{qualityId}")
    @ApiOperation("执行数据质量检查")
    public Map<String, Object> executeDataQualityCheck(@ApiParam("数据质量ID") @PathVariable String qualityId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.executeDataQualityCheck(qualityId);
            if (success) {
                result.put("success", true);
                result.put("message", "执行数据质量检查成功");
            } else {
                result.put("success", false);
                result.put("message", "执行数据质量检查失败");
            }
        } catch (Exception e) {
            log.error("执行数据质量检查异常", e);
            result.put("success", false);
            result.put("message", "执行数据质量检查异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/batch-execute-check")
    @ApiOperation("批量执行数据质量检查")
    public Map<String, Object> batchExecuteDataQualityCheck(@RequestBody List<String> qualityIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.batchExecuteDataQualityCheck(qualityIds);
            if (success) {
                result.put("success", true);
                result.put("message", "批量执行数据质量检查成功");
            } else {
                result.put("success", false);
                result.put("message", "批量执行数据质量检查失败");
            }
        } catch (Exception e) {
            log.error("批量执行数据质量检查异常", e);
            result.put("success", false);
            result.put("message", "批量执行数据质量检查异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/auto-execute-check")
    @ApiOperation("自动执行数据质量检查")
    public Map<String, Object> autoExecuteDataQualityCheck() {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.autoExecuteDataQualityCheck();
            if (success) {
                result.put("success", true);
                result.put("message", "自动执行数据质量检查成功");
            } else {
                result.put("success", false);
                result.put("message", "自动执行数据质量检查失败");
            }
        } catch (Exception e) {
            log.error("自动执行数据质量检查异常", e);
            result.put("success", false);
            result.put("message", "自动执行数据质量检查异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据质量修复操作 ====================

    @PostMapping("/fix-issue/{qualityId}")
    @ApiOperation("修复数据质量问题")
    public Map<String, Object> fixDataQualityIssue(@ApiParam("数据质量ID") @PathVariable String qualityId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.fixDataQualityIssue(qualityId);
            if (success) {
                result.put("success", true);
                result.put("message", "修复数据质量问题成功");
            } else {
                result.put("success", false);
                result.put("message", "修复数据质量问题失败");
            }
        } catch (Exception e) {
            log.error("修复数据质量问题异常", e);
            result.put("success", false);
            result.put("message", "修复数据质量问题异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/batch-fix-issue")
    @ApiOperation("批量修复数据质量问题")
    public Map<String, Object> batchFixDataQualityIssue(@RequestBody List<String> qualityIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.batchFixDataQualityIssue(qualityIds);
            if (success) {
                result.put("success", true);
                result.put("message", "批量修复数据质量问题成功");
            } else {
                result.put("success", false);
                result.put("message", "批量修复数据质量问题失败");
            }
        } catch (Exception e) {
            log.error("批量修复数据质量问题异常", e);
            result.put("success", false);
            result.put("message", "批量修复数据质量问题异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/manual-fix-issue/{qualityId}")
    @ApiOperation("手动修复数据质量问题")
    public Map<String, Object> manualFixDataQualityIssue(
            @ApiParam("数据质量ID") @PathVariable String qualityId,
            @ApiParam("修复方案") @RequestParam String fixSolution) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.manualFixDataQualityIssue(qualityId, fixSolution);
            if (success) {
                result.put("success", true);
                result.put("message", "手动修复数据质量问题成功");
            } else {
                result.put("success", false);
                result.put("message", "手动修复数据质量问题失败");
            }
        } catch (Exception e) {
            log.error("手动修复数据质量问题异常", e);
            result.put("success", false);
            result.put("message", "手动修复数据质量问题异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/ignore-issue/{qualityId}")
    @ApiOperation("忽略数据质量问题")
    public Map<String, Object> ignoreDataQualityIssue(
            @ApiParam("数据质量ID") @PathVariable String qualityId,
            @ApiParam("忽略原因") @RequestParam String reason) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.ignoreDataQualityIssue(qualityId, reason);
            if (success) {
                result.put("success", true);
                result.put("message", "忽略数据质量问题成功");
            } else {
                result.put("success", false);
                result.put("message", "忽略数据质量问题失败");
            }
        } catch (Exception e) {
            log.error("忽略数据质量问题异常", e);
            result.put("success", false);
            result.put("message", "忽略数据质量问题异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 批量操作 ====================

    @PutMapping("/batch-enable")
    @ApiOperation("批量启用数据质量")
    public Map<String, Object> batchEnableDataQuality(@RequestBody List<String> qualityIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.batchEnableDataQuality(qualityIds);
            if (success) {
                result.put("success", true);
                result.put("message", "批量启用数据质量成功");
            } else {
                result.put("success", false);
                result.put("message", "批量启用数据质量失败");
            }
        } catch (Exception e) {
            log.error("批量启用数据质量异常", e);
            result.put("success", false);
            result.put("message", "批量启用数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-disable")
    @ApiOperation("批量禁用数据质量")
    public Map<String, Object> batchDisableDataQuality(@RequestBody List<String> qualityIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.batchDisableDataQuality(qualityIds);
            if (success) {
                result.put("success", true);
                result.put("message", "批量禁用数据质量成功");
            } else {
                result.put("success", false);
                result.put("message", "批量禁用数据质量失败");
            }
        } catch (Exception e) {
            log.error("批量禁用数据质量异常", e);
            result.put("success", false);
            result.put("message", "批量禁用数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/batch-delete")
    @ApiOperation("批量删除数据质量")
    public Map<String, Object> batchDeleteDataQuality(@RequestBody List<String> qualityIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = dataQualityService.batchDeleteDataQuality(qualityIds);
            if (success) {
                result.put("success", true);
                result.put("message", "批量删除数据质量成功");
            } else {
                result.put("success", false);
                result.put("message", "批量删除数据质量失败");
            }
        } catch (Exception e) {
            log.error("批量删除数据质量异常", e);
            result.put("success", false);
            result.put("message", "批量删除数据质量异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 搜索功能 ====================

    @GetMapping("/search-by-keyword")
    @ApiOperation("根据关键词搜索数据质量")
    public Map<String, Object> searchDataQualityByKeyword(@ApiParam("关键词") @RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.searchDataQualityByKeyword(keyword);
            result.put("success", true);
            result.put("message", "搜索数据质量成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("搜索数据质量异常", e);
            result.put("success", false);
            result.put("message", "搜索数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/search-by-tags")
    @ApiOperation("根据标签搜索数据质量")
    public Map<String, Object> searchDataQualityByTags(@RequestBody List<String> tags) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.searchDataQualityByTags(tags);
            result.put("success", true);
            result.put("message", "搜索数据质量成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("搜索数据质量异常", e);
            result.put("success", false);
            result.put("message", "搜索数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/find-similar/{qualityId}")
    @ApiOperation("查找相似数据质量")
    public Map<String, Object> findSimilarDataQuality(@ApiParam("数据质量ID") @PathVariable String qualityId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.findSimilarDataQuality(qualityId);
            result.put("success", true);
            result.put("message", "查找相似数据质量成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("查找相似数据质量异常", e);
            result.put("success", false);
            result.put("message", "查找相似数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/find-popular")
    @ApiOperation("查找热门数据质量")
    public Map<String, Object> findPopularDataQuality(@ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.findPopularDataQuality(limit);
            result.put("success", true);
            result.put("message", "查找热门数据质量成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("查找热门数据质量异常", e);
            result.put("success", false);
            result.put("message", "查找热门数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/find-high-risk")
    @ApiOperation("查找高风险数据质量")
    public Map<String, Object> findHighRiskDataQuality() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EpsDataQuality> dataQualityList = dataQualityService.findHighRiskDataQuality();
            result.put("success", true);
            result.put("message", "查找高风险数据质量成功");
            result.put("data", dataQualityList);
        } catch (Exception e) {
            log.error("查找高风险数据质量异常", e);
            result.put("success", false);
            result.put("message", "查找高风险数据质量异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 验证功能 ====================

    @PostMapping("/validate")
    @ApiOperation("验证数据质量")
    public Map<String, Object> validateDataQuality(@RequestBody EpsDataQuality dataQuality) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean valid = dataQualityService.validateDataQuality(dataQuality);
            result.put("success", true);
            result.put("message", "验证数据质量完成");
            result.put("data", valid);
        } catch (Exception e) {
            log.error("验证数据质量异常", e);
            result.put("success", false);
            result.put("message", "验证数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/test-connection/{qualityId}")
    @ApiOperation("测试数据质量连接")
    public Map<String, Object> testDataQualityConnection(@ApiParam("数据质量ID") @PathVariable String qualityId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean connected = dataQualityService.testDataQualityConnection(qualityId);
            result.put("success", true);
            result.put("message", "测试数据质量连接完成");
            result.put("data", connected);
        } catch (Exception e) {
            log.error("测试数据质量连接异常", e);
            result.put("success", false);
            result.put("message", "测试数据质量连接异常：" + e.getMessage());
        }
        return result;
    }

    // ==================== 统计分析功能 ====================

    @GetMapping("/count")
    @ApiOperation("统计数据质量总数")
    public Map<String, Object> countDataQuality() {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = dataQualityService.countDataQuality();
            result.put("success", true);
            result.put("message", "统计数据质量总数成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计数据质量总数异常", e);
            result.put("success", false);
            result.put("message", "统计数据质量总数异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-type")
    @ApiOperation("根据质量类型统计数据质量")
    public Map<String, Object> countDataQualityByType(@ApiParam("质量类型") @RequestParam String qualityType) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = dataQualityService.countDataQualityByType(qualityType);
            result.put("success", true);
            result.put("message", "统计数据质量成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计数据质量异常", e);
            result.put("success", false);
            result.put("message", "统计数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-status")
    @ApiOperation("根据检查状态统计数据质量")
    public Map<String, Object> countDataQualityByCheckStatus(@ApiParam("检查状态") @RequestParam String checkStatus) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = dataQualityService.countDataQualityByCheckStatus(checkStatus);
            result.put("success", true);
            result.put("message", "统计数据质量成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计数据质量异常", e);
            result.put("success", false);
            result.put("message", "统计数据质量异常：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/system-overview")
    @ApiOperation("获取系统概览")
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> overview = dataQualityService.getSystemOverview();
            result.put("success", true);
            result.put("message", "获取系统概览成功");
            result.put("data", overview);
        } catch (Exception e) {
            log.error("获取系统概览异常", e);
            result.put("success", false);
            result.put("message", "获取系统概览异常：" + e.getMessage());
        }
        return result;
    }
}
