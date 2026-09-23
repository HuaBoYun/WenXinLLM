package com.management.accountant.controller.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.pm.PmTargetManagement;
import com.management.accountant.service.pm.PmTargetManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 目标管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "目标管理")
@RestController
@RequestMapping("/pm/target-management")
@Validated
public class PmTargetManagementController {

    @Autowired
    private PmTargetManagementService targetManagementService;

    /**
     * 分页查询目标
     */
    @ApiOperation("分页查询目标")
    @GetMapping("/page")
    public MyJsonBean<IPage<PmTargetManagement>> queryTargetPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("目标类型") @RequestParam(required = false) String targetType,
            @ApiParam("目标级别") @RequestParam(required = false) String targetLevel,
            @ApiParam("目标状态") @RequestParam(required = false) String targetStatus,
            @ApiParam("负责人ID") @RequestParam(required = false) Long targetOwnerId,
            @ApiParam("关键词") @RequestParam(required = false) String keyword) {
        try {
            IPage<PmTargetManagement> result = targetManagementService.queryTargetPage(
                    current, size, organizationId, targetType, targetLevel, targetStatus, targetOwnerId, keyword);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询目标失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建目标
     */
    @ApiOperation("创建目标")
    @PostMapping
    public MyJsonBean<Boolean> createTarget(
            @ApiParam("目标信息") @RequestBody @Valid PmTargetManagement target) {
        try {
            boolean result = targetManagementService.createTarget(target);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建目标失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新目标
     */
    @ApiOperation("更新目标")
    @PutMapping
    public MyJsonBean<Boolean> updateTarget(
            @ApiParam("目标信息") @RequestBody @Valid PmTargetManagement target) {
        try {
            boolean result = targetManagementService.updateTarget(target);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新目标失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除目标
     */
    @ApiOperation("删除目标")
    @DeleteMapping("/{targetId}")
    public MyJsonBean<Boolean> deleteTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId) {
        try {
            boolean result = targetManagementService.deleteTarget(targetId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除目标失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询目标详情
     */
    @ApiOperation("根据ID查询目标详情")
    @GetMapping("/{targetId}")
    public MyJsonBean<PmTargetManagement> getTargetById(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId) {
        try {
            PmTargetManagement result = targetManagementService.getTargetById(targetId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("目标不存在");
            }
        } catch (Exception e) {
            log.error("获取目标详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 目标分解
     */
    @ApiOperation("目标分解")
    @PostMapping("/{targetId}/decompose")
    public MyJsonBean<Map<String, Object>> decomposeTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("分解参数") @RequestBody Map<String, Object> decomposeParams) {
        try {
            Map<String, Object> result = targetManagementService.decomposeTarget(targetId, decomposeParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("目标分解失败", e);
            return MyJsonBean.error("分解失败：" + e.getMessage());
        }
    }

    /**
     * 目标跟踪
     */
    @ApiOperation("目标跟踪")
    @PostMapping("/{targetId}/track")
    public MyJsonBean<Map<String, Object>> trackTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("跟踪数据") @RequestBody Map<String, Object> trackingData) {
        try {
            Map<String, Object> result = targetManagementService.trackTarget(targetId, trackingData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("目标跟踪失败", e);
            return MyJsonBean.error("跟踪失败：" + e.getMessage());
        }
    }

    /**
     * 目标调整
     */
    @ApiOperation("目标调整")
    @PostMapping("/{targetId}/adjust")
    public MyJsonBean<Boolean> adjustTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("调整参数") @RequestBody Map<String, Object> adjustParams) {
        try {
            boolean result = targetManagementService.adjustTarget(targetId, adjustParams);
            if (result) {
                return MyJsonBean.success("调整成功", true);
            } else {
                return MyJsonBean.error("调整失败");
            }
        } catch (Exception e) {
            log.error("目标调整失败", e);
            return MyJsonBean.error("调整失败：" + e.getMessage());
        }
    }

    /**
     * 目标评估
     */
    @ApiOperation("目标评估")
    @PostMapping("/{targetId}/evaluate")
    public MyJsonBean<Map<String, Object>> evaluateTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("评估参数") @RequestBody Map<String, Object> evaluationParams) {
        try {
            Map<String, Object> result = targetManagementService.evaluateTarget(targetId, evaluationParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("目标评估失败", e);
            return MyJsonBean.error("评估失败：" + e.getMessage());
        }
    }

    /**
     * 目标激励
     */
    @ApiOperation("目标激励")
    @PostMapping("/{targetId}/incentive")
    public MyJsonBean<Map<String, Object>> incentiveTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("激励参数") @RequestBody Map<String, Object> incentiveParams) {
        try {
            Map<String, Object> result = targetManagementService.incentiveTarget(targetId, incentiveParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("目标激励失败", e);
            return MyJsonBean.error("激励失败：" + e.getMessage());
        }
    }

    /**
     * 目标协商
     */
    @ApiOperation("目标协商")
    @PostMapping("/{targetId}/negotiate")
    public MyJsonBean<Map<String, Object>> negotiateTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("协商参数") @RequestBody Map<String, Object> negotiationParams) {
        try {
            Map<String, Object> result = targetManagementService.negotiateTarget(targetId, negotiationParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("目标协商失败", e);
            return MyJsonBean.error("协商失败：" + e.getMessage());
        }
    }

    /**
     * 目标知识管理
     */
    @ApiOperation("目标知识管理")
    @GetMapping("/{targetId}/knowledge")
    public MyJsonBean<Map<String, Object>> getTargetKnowledge(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("知识类型") @RequestParam(defaultValue = "ALL") String knowledgeType) {
        try {
            Map<String, Object> result = targetManagementService.getTargetKnowledge(targetId, knowledgeType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标知识失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 目标数据分析
     */
    @ApiOperation("目标数据分析")
    @GetMapping("/{targetId}/analysis")
    public MyJsonBean<Map<String, Object>> analyzeTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("分析类型") @RequestParam(defaultValue = "COMPREHENSIVE") String analysisType) {
        try {
            Map<String, Object> result = targetManagementService.analyzeTarget(targetId, analysisType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("目标数据分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取目标树
     */
    @ApiOperation("获取目标树")
    @GetMapping("/tree")
    public MyJsonBean<List<Map<String, Object>>> getTargetTree(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("目标类型") @RequestParam(required = false) String targetType,
            @ApiParam("目标级别") @RequestParam(required = false) String targetLevel) {
        try {
            List<Map<String, Object>> result = targetManagementService.getTargetTree(organizationId, targetType, targetLevel);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标树失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取目标仪表板
     */
    @ApiOperation("获取目标仪表板")
    @GetMapping("/dashboard")
    public MyJsonBean<Map<String, Object>> getTargetDashboard(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("负责人ID") @RequestParam(required = false) Long targetOwnerId,
            @ApiParam("仪表板类型") @RequestParam(defaultValue = "OVERVIEW") String dashboardType) {
        try {
            Map<String, Object> result = targetManagementService.getTargetDashboard(organizationId, targetOwnerId, dashboardType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标仪表板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作目标
     */
    @ApiOperation("批量操作目标")
    @PostMapping("/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateTargets(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = targetManagementService.batchOperateTargets(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作目标失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 导入目标
     */
    @ApiOperation("导入目标")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importTargets(
            @ApiParam("导入数据") @RequestBody Map<String, Object> importData) {
        try {
            Map<String, Object> result = targetManagementService.importTargets(importData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入目标失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出目标
     */
    @ApiOperation("导出目标")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportTargets(
            @ApiParam("导出参数") @RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = targetManagementService.exportTargets(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出目标失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 获取目标统计
     */
    @ApiOperation("获取目标统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getTargetStatistics(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("统计类型") @RequestParam(defaultValue = "SUMMARY") String statisticsType,
            @ApiParam("统计周期") @RequestParam(defaultValue = "CURRENT") String statisticsPeriod) {
        try {
            Map<String, Object> result = targetManagementService.getTargetStatistics(organizationId, statisticsType, statisticsPeriod);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取目标进度报告
     */
    @ApiOperation("获取目标进度报告")
    @GetMapping("/progress-report")
    public MyJsonBean<Map<String, Object>> getTargetProgressReport(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("报告类型") @RequestParam(defaultValue = "SUMMARY") String reportType,
            @ApiParam("报告周期") @RequestParam(defaultValue = "MONTHLY") String reportPeriod) {
        try {
            Map<String, Object> result = targetManagementService.getTargetProgressReport(organizationId, reportType, reportPeriod);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标进度报告失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 复制目标
     */
    @ApiOperation("复制目标")
    @PostMapping("/{targetId}/copy")
    public MyJsonBean<Map<String, Object>> copyTarget(
            @ApiParam("目标ID") @PathVariable @NotNull Long targetId,
            @ApiParam("复制参数") @RequestBody Map<String, Object> copyParams) {
        try {
            Map<String, Object> result = targetManagementService.copyTarget(targetId, copyParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("复制目标失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 获取目标模板
     */
    @ApiOperation("获取目标模板")
    @GetMapping("/templates")
    public MyJsonBean<List<Map<String, Object>>> getTargetTemplates(
            @ApiParam("模板类型") @RequestParam(defaultValue = "ALL") String templateType) {
        try {
            List<Map<String, Object>> result = targetManagementService.getTargetTemplates(templateType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标模板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 应用目标模板
     */
    @ApiOperation("应用目标模板")
    @PostMapping("/templates/apply")
    public MyJsonBean<Map<String, Object>> applyTargetTemplate(
            @ApiParam("模板应用参数") @RequestBody Map<String, Object> templateParams) {
        try {
            Map<String, Object> result = targetManagementService.applyTargetTemplate(templateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("应用目标模板失败", e);
            return MyJsonBean.error("应用失败：" + e.getMessage());
        }
    }

    /**
     * 获取目标建议
     */
    @ApiOperation("获取目标建议")
    @GetMapping("/recommendations")
    public MyJsonBean<List<Map<String, Object>>> getTargetRecommendations(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("负责人ID") @RequestParam(required = false) Long targetOwnerId,
            @ApiParam("建议类型") @RequestParam(defaultValue = "OPTIMIZATION") String recommendationType) {
        try {
            List<Map<String, Object>> result = targetManagementService.getTargetRecommendations(organizationId, targetOwnerId, recommendationType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取目标建议失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 刷新目标缓存
     */
    @ApiOperation("刷新目标缓存")
    @PostMapping("/refresh-cache")
    public MyJsonBean<Boolean> refreshTargetCache(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("缓存类型") @RequestParam(defaultValue = "ALL") String cacheType) {
        try {
            boolean result = targetManagementService.refreshTargetCache(organizationId, cacheType);
            if (result) {
                return MyJsonBean.success("刷新成功", true);
            } else {
                return MyJsonBean.error("刷新失败");
            }
        } catch (Exception e) {
            log.error("刷新目标缓存失败", e);
            return MyJsonBean.error("刷新失败：" + e.getMessage());
        }
    }
}
