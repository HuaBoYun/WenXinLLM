package com.management.accountant.controller.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsIntelligentClassification;
import com.management.accountant.service.as.AsIntelligentClassificationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案智能分类控制器
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/as/intelligent-classification")
@Api(tags = "档案智能分类管理")
public class AsIntelligentClassificationController {

    @Autowired
    private AsIntelligentClassificationService classificationService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建智能分类")
    public MyJsonBean createClassification(@RequestBody AsIntelligentClassification classification) {
        try {
            AsIntelligentClassification result = classificationService.createClassification(classification);
            return MyJsonBean.success("创建智能分类成功", result);
        } catch (Exception e) {
            log.error("创建智能分类失败", e);
            return MyJsonBean.error("创建智能分类失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新智能分类")
    public MyJsonBean updateClassification(@RequestBody AsIntelligentClassification classification) {
        try {
            AsIntelligentClassification result = classificationService.updateClassification(classification);
            return MyJsonBean.success("更新智能分类成功", result);
        } catch (Exception e) {
            log.error("更新智能分类失败", e);
            return MyJsonBean.error("更新智能分类失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{tenantId}/{classificationId}")
    @ApiOperation("删除智能分类")
    public MyJsonBean deleteClassification(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.deleteClassification(tenantId, classificationId);
            return MyJsonBean.success("删除智能分类成功", result);
        } catch (Exception e) {
            log.error("删除智能分类失败", e);
            return MyJsonBean.error("删除智能分类失败: " + e.getMessage());
        }
    }

    @GetMapping("/get/{tenantId}/{classificationId}")
    @ApiOperation("根据ID查询智能分类")
    public MyJsonBean getClassificationById(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            AsIntelligentClassification result = classificationService.getClassificationById(tenantId, classificationId);
            return MyJsonBean.success("查询智能分类成功", result);
        } catch (Exception e) {
            log.error("查询智能分类失败", e);
            return MyJsonBean.error("查询智能分类失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-code/{tenantId}/{classificationCode}")
    @ApiOperation("根据编号查询智能分类")
    public MyJsonBean getClassificationByCode(@PathVariable Long tenantId, @PathVariable String classificationCode) {
        try {
            AsIntelligentClassification result = classificationService.getClassificationByCode(tenantId, classificationCode);
            return MyJsonBean.success("查询智能分类成功", result);
        } catch (Exception e) {
            log.error("查询智能分类失败", e);
            return MyJsonBean.error("查询智能分类失败: " + e.getMessage());
        }
    }

    // ==================== 分页查询操作 ====================

    @GetMapping("/page")
    @ApiOperation("分页查询智能分类")
    public MyJsonBean getClassificationPage(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam Long tenantId,
                                           @RequestParam(required = false) String classificationName,
                                           @RequestParam(required = false) String classificationType,
                                           @RequestParam(required = false) String classificationStatus,
                                           @RequestParam(required = false) String classificationAlgorithm) {
        try {
            Page<AsIntelligentClassification> page = new Page<>(current, size);
            IPage<AsIntelligentClassification> result = classificationService.getClassificationPage(page, tenantId, 
                    classificationName, classificationType, classificationStatus, classificationAlgorithm);
            return MyJsonBean.success("查询智能分类分页数据成功", result);
        } catch (Exception e) {
            log.error("查询智能分类分页数据失败", e);
            return MyJsonBean.error("查询智能分类分页数据失败: " + e.getMessage());
        }
    }

    // ==================== 分类管理操作 ====================

    @PostMapping("/start-training/{tenantId}/{classificationId}")
    @ApiOperation("启动分类训练")
    public MyJsonBean startTraining(@PathVariable Long tenantId, 
                                   @PathVariable Long classificationId,
                                   @RequestBody(required = false) Map<String, Object> trainingConfig) {
        try {
            boolean result = classificationService.startTraining(tenantId, classificationId, trainingConfig);
            return MyJsonBean.success("启动分类训练成功", result);
        } catch (Exception e) {
            log.error("启动分类训练失败", e);
            return MyJsonBean.error("启动分类训练失败: " + e.getMessage());
        }
    }

    @PostMapping("/stop-training/{tenantId}/{classificationId}")
    @ApiOperation("停止分类训练")
    public MyJsonBean stopTraining(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.stopTraining(tenantId, classificationId);
            return MyJsonBean.success("停止分类训练成功", result);
        } catch (Exception e) {
            log.error("停止分类训练失败", e);
            return MyJsonBean.error("停止分类训练失败: " + e.getMessage());
        }
    }

    @PostMapping("/deploy-model/{tenantId}/{classificationId}")
    @ApiOperation("部署分类模型")
    public MyJsonBean deployModel(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.deployModel(tenantId, classificationId);
            return MyJsonBean.success("部署分类模型成功", result);
        } catch (Exception e) {
            log.error("部署分类模型失败", e);
            return MyJsonBean.error("部署分类模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/undeploy-model/{tenantId}/{classificationId}")
    @ApiOperation("取消部署分类模型")
    public MyJsonBean undeployModel(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.undeployModel(tenantId, classificationId);
            return MyJsonBean.success("取消部署分类模型成功", result);
        } catch (Exception e) {
            log.error("取消部署分类模型失败", e);
            return MyJsonBean.error("取消部署分类模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/activate/{tenantId}/{classificationId}")
    @ApiOperation("激活分类模型")
    public MyJsonBean activateClassification(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.activateClassification(tenantId, classificationId);
            return MyJsonBean.success("激活分类模型成功", result);
        } catch (Exception e) {
            log.error("激活分类模型失败", e);
            return MyJsonBean.error("激活分类模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/deactivate/{tenantId}/{classificationId}")
    @ApiOperation("停用分类模型")
    public MyJsonBean deactivateClassification(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.deactivateClassification(tenantId, classificationId);
            return MyJsonBean.success("停用分类模型成功", result);
        } catch (Exception e) {
            log.error("停用分类模型失败", e);
            return MyJsonBean.error("停用分类模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/test/{tenantId}/{classificationId}")
    @ApiOperation("测试分类模型")
    public MyJsonBean testClassification(@PathVariable Long tenantId, 
                                        @PathVariable Long classificationId,
                                        @RequestBody Map<String, Object> testData) {
        try {
            Map<String, Object> result = classificationService.testClassification(tenantId, classificationId, testData);
            return MyJsonBean.success("测试分类模型成功", result);
        } catch (Exception e) {
            log.error("测试分类模型失败", e);
            return MyJsonBean.error("测试分类模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/predict/{tenantId}/{classificationId}")
    @ApiOperation("预测分类结果")
    public MyJsonBean predictClassification(@PathVariable Long tenantId, 
                                           @PathVariable Long classificationId,
                                           @RequestBody Map<String, Object> inputData) {
        try {
            Map<String, Object> result = classificationService.predictClassification(tenantId, classificationId, inputData);
            return MyJsonBean.success("预测分类结果成功", result);
        } catch (Exception e) {
            log.error("预测分类结果失败", e);
            return MyJsonBean.error("预测分类结果失败: " + e.getMessage());
        }
    }

    // ==================== 模型管理操作 ====================

    @PutMapping("/update-model-version/{tenantId}/{classificationId}")
    @ApiOperation("更新模型版本")
    public MyJsonBean updateModelVersion(@PathVariable Long tenantId, 
                                        @PathVariable Long classificationId,
                                        @RequestParam String newVersion) {
        try {
            boolean result = classificationService.updateModelVersion(tenantId, classificationId, newVersion);
            return MyJsonBean.success("更新模型版本成功", result);
        } catch (Exception e) {
            log.error("更新模型版本失败", e);
            return MyJsonBean.error("更新模型版本失败: " + e.getMessage());
        }
    }

    @PostMapping("/rollback-model-version/{tenantId}/{classificationId}")
    @ApiOperation("回滚模型版本")
    public MyJsonBean rollbackModelVersion(@PathVariable Long tenantId, 
                                          @PathVariable Long classificationId,
                                          @RequestParam String targetVersion) {
        try {
            boolean result = classificationService.rollbackModelVersion(tenantId, classificationId, targetVersion);
            return MyJsonBean.success("回滚模型版本成功", result);
        } catch (Exception e) {
            log.error("回滚模型版本失败", e);
            return MyJsonBean.error("回滚模型版本失败: " + e.getMessage());
        }
    }

    @GetMapping("/compare-model-versions/{tenantId}/{classificationId}")
    @ApiOperation("比较模型版本")
    public MyJsonBean compareModelVersions(@PathVariable Long tenantId, 
                                          @PathVariable Long classificationId,
                                          @RequestParam String version1,
                                          @RequestParam String version2) {
        try {
            Map<String, Object> result = classificationService.compareModelVersions(tenantId, classificationId, version1, version2);
            return MyJsonBean.success("比较模型版本成功", result);
        } catch (Exception e) {
            log.error("比较模型版本失败", e);
            return MyJsonBean.error("比较模型版本失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-model-info/{tenantId}/{classificationId}")
    @ApiOperation("获取模型信息")
    public MyJsonBean getModelInfo(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            Map<String, Object> result = classificationService.getModelInfo(tenantId, classificationId);
            return MyJsonBean.success("获取模型信息成功", result);
        } catch (Exception e) {
            log.error("获取模型信息失败", e);
            return MyJsonBean.error("获取模型信息失败: " + e.getMessage());
        }
    }

    @PutMapping("/update-model-config/{tenantId}/{classificationId}")
    @ApiOperation("更新模型配置")
    public MyJsonBean updateModelConfig(@PathVariable Long tenantId, 
                                       @PathVariable Long classificationId,
                                       @RequestBody Map<String, Object> config) {
        try {
            boolean result = classificationService.updateModelConfig(tenantId, classificationId, config);
            return MyJsonBean.success("更新模型配置成功", result);
        } catch (Exception e) {
            log.error("更新模型配置失败", e);
            return MyJsonBean.error("更新模型配置失败: " + e.getMessage());
        }
    }

    // ==================== 性能监控操作 ====================

    @GetMapping("/get-performance-metrics/{tenantId}/{classificationId}")
    @ApiOperation("获取分类性能指标")
    public MyJsonBean getPerformanceMetrics(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            Map<String, Object> result = classificationService.getPerformanceMetrics(tenantId, classificationId);
            return MyJsonBean.success("获取分类性能指标成功", result);
        } catch (Exception e) {
            log.error("获取分类性能指标失败", e);
            return MyJsonBean.error("获取分类性能指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-accuracy-history/{tenantId}/{classificationId}")
    @ApiOperation("获取分类准确率历史")
    public MyJsonBean getAccuracyHistory(@PathVariable Long tenantId,
                                        @PathVariable Long classificationId,
                                        @RequestParam LocalDateTime startTime,
                                        @RequestParam LocalDateTime endTime) {
        try {
            List<Map<String, Object>> result = classificationService.getAccuracyHistory(tenantId, classificationId, startTime, endTime);
            return MyJsonBean.success("获取分类准确率历史成功", result);
        } catch (Exception e) {
            log.error("获取分类准确率历史失败", e);
            return MyJsonBean.error("获取分类准确率历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-prediction-stats/{tenantId}/{classificationId}")
    @ApiOperation("获取预测统计")
    public MyJsonBean getPredictionStats(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            Map<String, Object> result = classificationService.getPredictionStats(tenantId, classificationId);
            return MyJsonBean.success("获取预测统计成功", result);
        } catch (Exception e) {
            log.error("获取预测统计失败", e);
            return MyJsonBean.error("获取预测统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-resource-usage/{tenantId}/{classificationId}")
    @ApiOperation("获取资源使用情况")
    public MyJsonBean getResourceUsage(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            Map<String, Object> result = classificationService.getResourceUsage(tenantId, classificationId);
            return MyJsonBean.success("获取资源使用情况成功", result);
        } catch (Exception e) {
            log.error("获取资源使用情况失败", e);
            return MyJsonBean.error("获取资源使用情况失败: " + e.getMessage());
        }
    }

    @GetMapping("/monitor-health/{tenantId}/{classificationId}")
    @ApiOperation("监控分类健康状态")
    public MyJsonBean monitorClassificationHealth(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            Map<String, Object> result = classificationService.monitorClassificationHealth(tenantId, classificationId);
            return MyJsonBean.success("监控分类健康状态成功", result);
        } catch (Exception e) {
            log.error("监控分类健康状态失败", e);
            return MyJsonBean.error("监控分类健康状态失败: " + e.getMessage());
        }
    }

    // ==================== 查询统计操作 ====================

    @GetMapping("/get-by-type/{tenantId}")
    @ApiOperation("根据分类类型查询")
    public MyJsonBean getClassificationsByType(@PathVariable Long tenantId, @RequestParam String classificationType) {
        try {
            List<AsIntelligentClassification> result = classificationService.getClassificationsByType(tenantId, classificationType);
            return MyJsonBean.success("根据分类类型查询成功", result);
        } catch (Exception e) {
            log.error("根据分类类型查询失败", e);
            return MyJsonBean.error("根据分类类型查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-status/{tenantId}")
    @ApiOperation("根据分类状态查询")
    public MyJsonBean getClassificationsByStatus(@PathVariable Long tenantId, @RequestParam String classificationStatus) {
        try {
            List<AsIntelligentClassification> result = classificationService.getClassificationsByStatus(tenantId, classificationStatus);
            return MyJsonBean.success("根据分类状态查询成功", result);
        } catch (Exception e) {
            log.error("根据分类状态查询失败", e);
            return MyJsonBean.error("根据分类状态查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-algorithm/{tenantId}")
    @ApiOperation("根据分类算法查询")
    public MyJsonBean getClassificationsByAlgorithm(@PathVariable Long tenantId, @RequestParam String classificationAlgorithm) {
        try {
            List<AsIntelligentClassification> result = classificationService.getClassificationsByAlgorithm(tenantId, classificationAlgorithm);
            return MyJsonBean.success("根据分类算法查询成功", result);
        } catch (Exception e) {
            log.error("根据分类算法查询失败", e);
            return MyJsonBean.error("根据分类算法查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-active/{tenantId}")
    @ApiOperation("查询活跃的分类")
    public MyJsonBean getActiveClassifications(@PathVariable Long tenantId) {
        try {
            List<AsIntelligentClassification> result = classificationService.getActiveClassifications(tenantId);
            return MyJsonBean.success("查询活跃的分类成功", result);
        } catch (Exception e) {
            log.error("查询活跃的分类失败", e);
            return MyJsonBean.error("查询活跃的分类失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-deployed/{tenantId}")
    @ApiOperation("查询已部署的分类")
    public MyJsonBean getDeployedClassifications(@PathVariable Long tenantId) {
        try {
            List<AsIntelligentClassification> result = classificationService.getDeployedClassifications(tenantId);
            return MyJsonBean.success("查询已部署的分类成功", result);
        } catch (Exception e) {
            log.error("查询已部署的分类失败", e);
            return MyJsonBean.error("查询已部署的分类失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-training/{tenantId}")
    @ApiOperation("查询训练中的分类")
    public MyJsonBean getTrainingClassifications(@PathVariable Long tenantId) {
        try {
            List<AsIntelligentClassification> result = classificationService.getTrainingClassifications(tenantId);
            return MyJsonBean.success("查询训练中的分类成功", result);
        } catch (Exception e) {
            log.error("查询训练中的分类失败", e);
            return MyJsonBean.error("查询训练中的分类失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-accuracy-range/{tenantId}")
    @ApiOperation("根据准确率范围查询")
    public MyJsonBean getClassificationsByAccuracyRange(@PathVariable Long tenantId,
                                                        @RequestParam BigDecimal minAccuracy,
                                                        @RequestParam BigDecimal maxAccuracy) {
        try {
            List<AsIntelligentClassification> result = classificationService.getClassificationsByAccuracyRange(tenantId, minAccuracy, maxAccuracy);
            return MyJsonBean.success("根据准确率范围查询成功", result);
        } catch (Exception e) {
            log.error("根据准确率范围查询失败", e);
            return MyJsonBean.error("根据准确率范围查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-latest-version/{tenantId}")
    @ApiOperation("查询最新版本的分类")
    public MyJsonBean getLatestVersionByType(@PathVariable Long tenantId, @RequestParam String classificationType) {
        try {
            AsIntelligentClassification result = classificationService.getLatestVersionByType(tenantId, classificationType);
            return MyJsonBean.success("查询最新版本的分类成功", result);
        } catch (Exception e) {
            log.error("查询最新版本的分类失败", e);
            return MyJsonBean.error("查询最新版本的分类失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-best-performance/{tenantId}")
    @ApiOperation("查询性能最佳的分类")
    public MyJsonBean getBestPerformanceByType(@PathVariable Long tenantId, @RequestParam String classificationType) {
        try {
            AsIntelligentClassification result = classificationService.getBestPerformanceByType(tenantId, classificationType);
            return MyJsonBean.success("查询性能最佳的分类成功", result);
        } catch (Exception e) {
            log.error("查询性能最佳的分类失败", e);
            return MyJsonBean.error("查询性能最佳的分类失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析操作 ====================

    @GetMapping("/count-by-status/{tenantId}")
    @ApiOperation("统计分类状态分布")
    public MyJsonBean countByClassificationStatus(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = classificationService.countByClassificationStatus(tenantId);
            return MyJsonBean.success("统计分类状态分布成功", result);
        } catch (Exception e) {
            log.error("统计分类状态分布失败", e);
            return MyJsonBean.error("统计分类状态分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/count-by-type/{tenantId}")
    @ApiOperation("统计分类类型分布")
    public MyJsonBean countByClassificationType(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = classificationService.countByClassificationType(tenantId);
            return MyJsonBean.success("统计分类类型分布成功", result);
        } catch (Exception e) {
            log.error("统计分类类型分布失败", e);
            return MyJsonBean.error("统计分类类型分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/count-by-algorithm/{tenantId}")
    @ApiOperation("统计分类算法分布")
    public MyJsonBean countByClassificationAlgorithm(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = classificationService.countByClassificationAlgorithm(tenantId);
            return MyJsonBean.success("统计分类算法分布成功", result);
        } catch (Exception e) {
            log.error("统计分类算法分布失败", e);
            return MyJsonBean.error("统计分类算法分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-classification-trend/{tenantId}")
    @ApiOperation("获取分类趋势")
    public MyJsonBean getClassificationTrend(@PathVariable Long tenantId,
                                            @RequestParam LocalDateTime startDate,
                                            @RequestParam LocalDateTime endDate,
                                            @RequestParam String granularity) {
        try {
            List<Map<String, Object>> result = classificationService.getClassificationTrend(tenantId, startDate, endDate, granularity);
            return MyJsonBean.success("获取分类趋势成功", result);
        } catch (Exception e) {
            log.error("获取分类趋势失败", e);
            return MyJsonBean.error("获取分类趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-training-trend/{tenantId}")
    @ApiOperation("获取训练趋势")
    public MyJsonBean getTrainingTrend(@PathVariable Long tenantId,
                                      @RequestParam LocalDateTime startDate,
                                      @RequestParam LocalDateTime endDate,
                                      @RequestParam String granularity) {
        try {
            List<Map<String, Object>> result = classificationService.getTrainingTrend(tenantId, startDate, endDate, granularity);
            return MyJsonBean.success("获取训练趋势成功", result);
        } catch (Exception e) {
            log.error("获取训练趋势失败", e);
            return MyJsonBean.error("获取训练趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-prediction-trend/{tenantId}")
    @ApiOperation("获取预测趋势")
    public MyJsonBean getPredictionTrend(@PathVariable Long tenantId,
                                        @RequestParam LocalDateTime startDate,
                                        @RequestParam LocalDateTime endDate,
                                        @RequestParam String granularity) {
        try {
            List<Map<String, Object>> result = classificationService.getPredictionTrend(tenantId, startDate, endDate, granularity);
            return MyJsonBean.success("获取预测趋势成功", result);
        } catch (Exception e) {
            log.error("获取预测趋势失败", e);
            return MyJsonBean.error("获取预测趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/get-ranking/{tenantId}")
    @ApiOperation("获取分类排行榜")
    public MyJsonBean getClassificationRanking(@PathVariable Long tenantId,
                                              @RequestParam String rankBy,
                                              @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> result = classificationService.getClassificationRanking(tenantId, rankBy, limit);
            return MyJsonBean.success("获取分类排行榜成功", result);
        } catch (Exception e) {
            log.error("获取分类排行榜失败", e);
            return MyJsonBean.error("获取分类排行榜失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作 ====================

    @PostMapping("/batch-create")
    @ApiOperation("批量创建分类")
    public MyJsonBean batchCreateClassifications(@RequestBody List<AsIntelligentClassification> classifications) {
        try {
            List<AsIntelligentClassification> result = classificationService.batchCreateClassifications(classifications);
            return MyJsonBean.success("批量创建分类成功", result);
        } catch (Exception e) {
            log.error("批量创建分类失败", e);
            return MyJsonBean.error("批量创建分类失败: " + e.getMessage());
        }
    }

    @PutMapping("/batch-update-status/{tenantId}")
    @ApiOperation("批量更新分类状态")
    public MyJsonBean batchUpdateStatus(@PathVariable Long tenantId,
                                       @RequestBody List<Long> classificationIds,
                                       @RequestParam String status) {
        try {
            boolean result = classificationService.batchUpdateStatus(tenantId, classificationIds, status);
            return MyJsonBean.success("批量更新分类状态成功", result);
        } catch (Exception e) {
            log.error("批量更新分类状态失败", e);
            return MyJsonBean.error("批量更新分类状态失败: " + e.getMessage());
        }
    }

    @PutMapping("/batch-update-algorithm/{tenantId}")
    @ApiOperation("批量更新分类算法")
    public MyJsonBean batchUpdateAlgorithm(@PathVariable Long tenantId,
                                          @RequestBody List<Long> classificationIds,
                                          @RequestParam String algorithm) {
        try {
            boolean result = classificationService.batchUpdateAlgorithm(tenantId, classificationIds, algorithm);
            return MyJsonBean.success("批量更新分类算法成功", result);
        } catch (Exception e) {
            log.error("批量更新分类算法失败", e);
            return MyJsonBean.error("批量更新分类算法失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch-delete/{tenantId}")
    @ApiOperation("批量删除分类")
    public MyJsonBean batchDeleteClassifications(@PathVariable Long tenantId,
                                                 @RequestBody List<Long> classificationIds) {
        try {
            boolean result = classificationService.batchDeleteClassifications(tenantId, classificationIds);
            return MyJsonBean.success("批量删除分类成功", result);
        } catch (Exception e) {
            log.error("批量删除分类失败", e);
            return MyJsonBean.error("批量删除分类失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-train/{tenantId}")
    @ApiOperation("批量训练分类")
    public MyJsonBean batchTrainClassifications(@PathVariable Long tenantId,
                                               @RequestBody List<Long> classificationIds,
                                               @RequestParam(required = false) Map<String, Object> trainingConfig) {
        try {
            boolean result = classificationService.batchTrainClassifications(tenantId, classificationIds, trainingConfig);
            return MyJsonBean.success("批量训练分类成功", result);
        } catch (Exception e) {
            log.error("批量训练分类失败", e);
            return MyJsonBean.error("批量训练分类失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-deploy/{tenantId}")
    @ApiOperation("批量部署分类")
    public MyJsonBean batchDeployClassifications(@PathVariable Long tenantId,
                                                @RequestBody List<Long> classificationIds) {
        try {
            boolean result = classificationService.batchDeployClassifications(tenantId, classificationIds);
            return MyJsonBean.success("批量部署分类成功", result);
        } catch (Exception e) {
            log.error("批量部署分类失败", e);
            return MyJsonBean.error("批量部署分类失败: " + e.getMessage());
        }
    }

    // ==================== 数据管理操作 ====================

    @GetMapping("/export/{tenantId}")
    @ApiOperation("导出分类数据")
    public MyJsonBean exportClassificationData(@PathVariable Long tenantId,
                                              @RequestParam List<Long> classificationIds) {
        try {
            List<Map<String, Object>> result = classificationService.exportClassificationData(tenantId, classificationIds);
            return MyJsonBean.success("导出分类数据成功", result);
        } catch (Exception e) {
            log.error("导出分类数据失败", e);
            return MyJsonBean.error("导出分类数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/import/{tenantId}")
    @ApiOperation("导入分类数据")
    public MyJsonBean importClassificationData(@PathVariable Long tenantId,
                                              @RequestBody List<Map<String, Object>> classificationData) {
        try {
            boolean result = classificationService.importClassificationData(tenantId, classificationData);
            return MyJsonBean.success("导入分类数据成功", result);
        } catch (Exception e) {
            log.error("导入分类数据失败", e);
            return MyJsonBean.error("导入分类数据失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/cleanup-expired/{tenantId}")
    @ApiOperation("清理过期数据")
    public MyJsonBean cleanupExpiredData(@PathVariable Long tenantId,
                                        @RequestParam LocalDateTime expiredDate) {
        try {
            boolean result = classificationService.cleanupExpiredData(tenantId, expiredDate);
            return MyJsonBean.success("清理过期数据成功", result);
        } catch (Exception e) {
            log.error("清理过期数据失败", e);
            return MyJsonBean.error("清理过期数据失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/cleanup-invalid/{tenantId}")
    @ApiOperation("清理无效模型")
    public MyJsonBean cleanupInvalidModels(@PathVariable Long tenantId) {
        try {
            boolean result = classificationService.cleanupInvalidModels(tenantId);
            return MyJsonBean.success("清理无效模型成功", result);
        } catch (Exception e) {
            log.error("清理无效模型失败", e);
            return MyJsonBean.error("清理无效模型失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护操作 ====================

    @PostMapping("/rebuild-index/{tenantId}")
    @ApiOperation("重建分类索引")
    public MyJsonBean rebuildClassificationIndex(@PathVariable Long tenantId) {
        try {
            boolean result = classificationService.rebuildClassificationIndex(tenantId);
            return MyJsonBean.success("重建分类索引成功", result);
        } catch (Exception e) {
            log.error("重建分类索引失败", e);
            return MyJsonBean.error("重建分类索引失败: " + e.getMessage());
        }
    }

    @PostMapping("/optimize-performance/{tenantId}")
    @ApiOperation("优化分类性能")
    public MyJsonBean optimizeClassificationPerformance(@PathVariable Long tenantId) {
        try {
            boolean result = classificationService.optimizeClassificationPerformance(tenantId);
            return MyJsonBean.success("优化分类性能成功", result);
        } catch (Exception e) {
            log.error("优化分类性能失败", e);
            return MyJsonBean.error("优化分类性能失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-health/{tenantId}")
    @ApiOperation("检查分类健康状态")
    public MyJsonBean checkClassificationHealth(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = classificationService.checkClassificationHealth(tenantId);
            return MyJsonBean.success("检查分类健康状态成功", result);
        } catch (Exception e) {
            log.error("检查分类健康状态失败", e);
            return MyJsonBean.error("检查分类健康状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/generate-report/{tenantId}/{classificationId}")
    @ApiOperation("生成分类报告")
    public MyJsonBean generateClassificationReport(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            Map<String, Object> result = classificationService.generateClassificationReport(tenantId, classificationId);
            return MyJsonBean.success("生成分类报告成功", result);
        } catch (Exception e) {
            log.error("生成分类报告失败", e);
            return MyJsonBean.error("生成分类报告失败: " + e.getMessage());
        }
    }

    @GetMapping("/system-overview/{tenantId}")
    @ApiOperation("获取系统概览")
    public MyJsonBean getSystemOverview(@PathVariable Long tenantId) {
        try {
            Map<String, Object> result = classificationService.getSystemOverview(tenantId);
            return MyJsonBean.success("获取系统概览成功", result);
        } catch (Exception e) {
            log.error("获取系统概览失败", e);
            return MyJsonBean.error("获取系统概览失败: " + e.getMessage());
        }
    }

    // ==================== 通知提醒操作 ====================

    @PostMapping("/send-training-notification/{tenantId}/{classificationId}")
    @ApiOperation("发送训练完成通知")
    public MyJsonBean sendTrainingCompletionNotification(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.sendTrainingCompletionNotification(tenantId, classificationId);
            return MyJsonBean.success("发送训练完成通知成功", result);
        } catch (Exception e) {
            log.error("发送训练完成通知失败", e);
            return MyJsonBean.error("发送训练完成通知失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-deployment-notification/{tenantId}/{classificationId}")
    @ApiOperation("发送部署成功通知")
    public MyJsonBean sendDeploymentSuccessNotification(@PathVariable Long tenantId, @PathVariable Long classificationId) {
        try {
            boolean result = classificationService.sendDeploymentSuccessNotification(tenantId, classificationId);
            return MyJsonBean.success("发送部署成功通知成功", result);
        } catch (Exception e) {
            log.error("发送部署成功通知失败", e);
            return MyJsonBean.error("发送部署成功通知失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-alert-notification/{tenantId}/{classificationId}")
    @ApiOperation("发送性能告警通知")
    public MyJsonBean sendPerformanceAlertNotification(@PathVariable Long tenantId,
                                                      @PathVariable Long classificationId,
                                                      @RequestParam String alertType) {
        try {
            boolean result = classificationService.sendPerformanceAlertNotification(tenantId, classificationId, alertType);
            return MyJsonBean.success("发送性能告警通知成功", result);
        } catch (Exception e) {
            log.error("发送性能告警通知失败", e);
            return MyJsonBean.error("发送性能告警通知失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-error-notification/{tenantId}/{classificationId}")
    @ApiOperation("发送错误通知")
    public MyJsonBean sendErrorNotification(@PathVariable Long tenantId,
                                           @PathVariable Long classificationId,
                                           @RequestParam String errorMessage) {
        try {
            boolean result = classificationService.sendErrorNotification(tenantId, classificationId, errorMessage);
            return MyJsonBean.success("发送错误通知成功", result);
        } catch (Exception e) {
            log.error("发送错误通知失败", e);
            return MyJsonBean.error("发送错误通知失败: " + e.getMessage());
        }
    }
}
