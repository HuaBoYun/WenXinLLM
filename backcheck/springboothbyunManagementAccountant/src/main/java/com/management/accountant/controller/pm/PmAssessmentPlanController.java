package com.management.accountant.controller.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.pm.PmAssessmentPlan;
import com.management.accountant.service.pm.PmAssessmentPlanService;
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
 * 考核方案配置控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "考核方案配置")
@RestController
@RequestMapping("/pm/assessment-plan")
@Validated
public class PmAssessmentPlanController {

    @Autowired
    private PmAssessmentPlanService assessmentPlanService;

    /**
     * 分页查询考核方案
     */
    @ApiOperation("分页查询考核方案")
    @GetMapping("/page")
    public MyJsonBean<IPage<PmAssessmentPlan>> queryAssessmentPlanPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("方案类型") @RequestParam(required = false) String planType,
            @ApiParam("考核模式") @RequestParam(required = false) String assessmentMode,
            @ApiParam("方案状态") @RequestParam(required = false) String planStatus,
            @ApiParam("考核年度") @RequestParam(required = false) Integer assessmentYear,
            @ApiParam("关键词") @RequestParam(required = false) String keyword) {
        try {
            IPage<PmAssessmentPlan> result = assessmentPlanService.queryAssessmentPlanPage(
                    current, size, organizationId, planType, assessmentMode, planStatus, assessmentYear, keyword);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询考核方案失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建考核方案
     */
    @ApiOperation("创建考核方案")
    @PostMapping
    public MyJsonBean<Boolean> createAssessmentPlan(
            @ApiParam("考核方案信息") @RequestBody @Valid PmAssessmentPlan assessmentPlan) {
        try {
            boolean result = assessmentPlanService.createAssessmentPlan(assessmentPlan);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建考核方案失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新考核方案
     */
    @ApiOperation("更新考核方案")
    @PutMapping
    public MyJsonBean<Boolean> updateAssessmentPlan(
            @ApiParam("考核方案信息") @RequestBody @Valid PmAssessmentPlan assessmentPlan) {
        try {
            boolean result = assessmentPlanService.updateAssessmentPlan(assessmentPlan);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新考核方案失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除考核方案
     */
    @ApiOperation("删除考核方案")
    @DeleteMapping("/{planId}")
    public MyJsonBean<Boolean> deleteAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            boolean result = assessmentPlanService.deleteAssessmentPlan(planId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除考核方案失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询考核方案详情
     */
    @ApiOperation("根据ID查询考核方案详情")
    @GetMapping("/{planId}")
    public MyJsonBean<PmAssessmentPlan> getAssessmentPlanById(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            PmAssessmentPlan result = assessmentPlanService.getAssessmentPlanById(planId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("考核方案不存在");
            }
        } catch (Exception e) {
            log.error("获取考核方案详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 配置考核指标
     */
    @ApiOperation("配置考核指标")
    @PostMapping("/{planId}/indicators")
    public MyJsonBean<Map<String, Object>> configureIndicators(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId,
            @ApiParam("指标配置") @RequestBody Map<String, Object> indicatorConfig) {
        try {
            Map<String, Object> result = assessmentPlanService.configureIndicators(planId, indicatorConfig);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("配置考核指标失败", e);
            return MyJsonBean.error("配置失败：" + e.getMessage());
        }
    }

    /**
     * 设置评分规则
     */
    @ApiOperation("设置评分规则")
    @PostMapping("/{planId}/scoring-rules")
    public MyJsonBean<Boolean> setScoringRules(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId,
            @ApiParam("评分规则") @RequestBody Map<String, Object> scoringRules) {
        try {
            boolean result = assessmentPlanService.setScoringRules(planId, scoringRules);
            if (result) {
                return MyJsonBean.success("设置成功", true);
            } else {
                return MyJsonBean.error("设置失败");
            }
        } catch (Exception e) {
            log.error("设置评分规则失败", e);
            return MyJsonBean.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 配置考核流程
     */
    @ApiOperation("配置考核流程")
    @PostMapping("/{planId}/process")
    public MyJsonBean<Map<String, Object>> configureProcess(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId,
            @ApiParam("流程配置") @RequestBody Map<String, Object> processConfig) {
        try {
            Map<String, Object> result = assessmentPlanService.configureProcess(planId, processConfig);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("配置考核流程失败", e);
            return MyJsonBean.error("配置失败：" + e.getMessage());
        }
    }

    /**
     * 设置权重配置
     */
    @ApiOperation("设置权重配置")
    @PostMapping("/{planId}/weights")
    public MyJsonBean<Boolean> setWeightConfig(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId,
            @ApiParam("权重配置") @RequestBody Map<String, Object> weightConfig) {
        try {
            boolean result = assessmentPlanService.setWeightConfig(planId, weightConfig);
            if (result) {
                return MyJsonBean.success("设置成功", true);
            } else {
                return MyJsonBean.error("设置失败");
            }
        } catch (Exception e) {
            log.error("设置权重配置失败", e);
            return MyJsonBean.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 激活考核方案
     */
    @ApiOperation("激活考核方案")
    @PostMapping("/{planId}/activate")
    public MyJsonBean<Boolean> activateAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            boolean result = assessmentPlanService.activateAssessmentPlan(planId);
            if (result) {
                return MyJsonBean.success("激活成功", true);
            } else {
                return MyJsonBean.error("激活失败");
            }
        } catch (Exception e) {
            log.error("激活考核方案失败", e);
            return MyJsonBean.error("激活失败：" + e.getMessage());
        }
    }

    /**
     * 暂停考核方案
     */
    @ApiOperation("暂停考核方案")
    @PostMapping("/{planId}/pause")
    public MyJsonBean<Boolean> pauseAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            boolean result = assessmentPlanService.pauseAssessmentPlan(planId);
            if (result) {
                return MyJsonBean.success("暂停成功", true);
            } else {
                return MyJsonBean.error("暂停失败");
            }
        } catch (Exception e) {
            log.error("暂停考核方案失败", e);
            return MyJsonBean.error("暂停失败：" + e.getMessage());
        }
    }

    /**
     * 完成考核方案
     */
    @ApiOperation("完成考核方案")
    @PostMapping("/{planId}/complete")
    public MyJsonBean<Boolean> completeAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            boolean result = assessmentPlanService.completeAssessmentPlan(planId);
            if (result) {
                return MyJsonBean.success("完成成功", true);
            } else {
                return MyJsonBean.error("完成失败");
            }
        } catch (Exception e) {
            log.error("完成考核方案失败", e);
            return MyJsonBean.error("完成失败：" + e.getMessage());
        }
    }

    /**
     * 复制考核方案
     */
    @ApiOperation("复制考核方案")
    @PostMapping("/{planId}/copy")
    public MyJsonBean<Map<String, Object>> copyAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId,
            @ApiParam("复制参数") @RequestBody Map<String, Object> copyParams) {
        try {
            Map<String, Object> result = assessmentPlanService.copyAssessmentPlan(planId, copyParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("复制考核方案失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 获取方案模板
     */
    @ApiOperation("获取方案模板")
    @GetMapping("/templates")
    public MyJsonBean<List<Map<String, Object>>> getAssessmentPlanTemplates(
            @ApiParam("模板类型") @RequestParam(defaultValue = "ALL") String templateType) {
        try {
            List<Map<String, Object>> result = assessmentPlanService.getAssessmentPlanTemplates(templateType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取方案模板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 应用方案模板
     */
    @ApiOperation("应用方案模板")
    @PostMapping("/templates/apply")
    public MyJsonBean<Map<String, Object>> applyAssessmentPlanTemplate(
            @ApiParam("模板应用参数") @RequestBody Map<String, Object> templateParams) {
        try {
            Map<String, Object> result = assessmentPlanService.applyAssessmentPlanTemplate(templateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("应用方案模板失败", e);
            return MyJsonBean.error("应用失败：" + e.getMessage());
        }
    }

    /**
     * 获取方案统计
     */
    @ApiOperation("获取方案统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getAssessmentPlanStatistics(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("统计类型") @RequestParam(defaultValue = "SUMMARY") String statisticsType,
            @ApiParam("统计周期") @RequestParam(defaultValue = "CURRENT") String statisticsPeriod) {
        try {
            Map<String, Object> result = assessmentPlanService.getAssessmentPlanStatistics(organizationId, statisticsType, statisticsPeriod);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取方案统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作方案
     */
    @ApiOperation("批量操作方案")
    @PostMapping("/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateAssessmentPlans(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = assessmentPlanService.batchOperateAssessmentPlans(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作方案失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 导入方案
     */
    @ApiOperation("导入方案")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importAssessmentPlans(
            @ApiParam("导入数据") @RequestBody Map<String, Object> importData) {
        try {
            Map<String, Object> result = assessmentPlanService.importAssessmentPlans(importData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入方案失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出方案
     */
    @ApiOperation("导出方案")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportAssessmentPlans(
            @ApiParam("导出参数") @RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = assessmentPlanService.exportAssessmentPlans(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出方案失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 验证方案配置
     */
    @ApiOperation("验证方案配置")
    @PostMapping("/{planId}/validate")
    public MyJsonBean<Map<String, Object>> validateAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            Map<String, Object> result = assessmentPlanService.validateAssessmentPlan(planId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("验证方案配置失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 获取方案预览
     */
    @ApiOperation("获取方案预览")
    @GetMapping("/{planId}/preview")
    public MyJsonBean<Map<String, Object>> previewAssessmentPlan(
            @ApiParam("方案ID") @PathVariable @NotNull Long planId) {
        try {
            Map<String, Object> result = assessmentPlanService.previewAssessmentPlan(planId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取方案预览失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 刷新方案缓存
     */
    @ApiOperation("刷新方案缓存")
    @PostMapping("/refresh-cache")
    public MyJsonBean<Boolean> refreshAssessmentPlanCache(
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("缓存类型") @RequestParam(defaultValue = "ALL") String cacheType) {
        try {
            boolean result = assessmentPlanService.refreshAssessmentPlanCache(organizationId, cacheType);
            if (result) {
                return MyJsonBean.success("刷新成功", true);
            } else {
                return MyJsonBean.error("刷新失败");
            }
        } catch (Exception e) {
            log.error("刷新方案缓存失败", e);
            return MyJsonBean.error("刷新失败：" + e.getMessage());
        }
    }
}
