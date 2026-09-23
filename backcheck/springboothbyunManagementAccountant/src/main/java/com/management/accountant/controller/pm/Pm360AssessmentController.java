package com.management.accountant.controller.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.pm.Pm360Assessment;
import com.management.accountant.service.pm.Pm360AssessmentService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 360度评估控制器
 * 提供360度评估的完整管理功能
 * 
 * @author 华博云AI助手
 * @since 2024-01-01
 */
@Api(tags = "360度评估管理")
@RestController
@RequestMapping("/pm/360-assessment")
@Slf4j
public class Pm360AssessmentController {

    @Autowired
    private Pm360AssessmentService pm360AssessmentService;

    /**
     * 分页查询360度评估
     */
    @ApiOperation("分页查询360度评估")
    @GetMapping("/page")
    public MyJsonBean<IPage<Pm360Assessment>> queryPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页面大小") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("评估名称") @RequestParam(required = false) String assessmentName,
            @ApiParam("被评估人姓名") @RequestParam(required = false) String assessedUserName,
            @ApiParam("评估类型") @RequestParam(required = false) String assessmentType,
            @ApiParam("评估状态") @RequestParam(required = false) String assessmentStatus,
            @ApiParam("评估年度") @RequestParam(required = false) Integer assessmentYear,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId) {
        try {
            IPage<Pm360Assessment> result = pm360AssessmentService.queryPage(
                current, size, assessmentName, assessedUserName, assessmentType, 
                assessmentStatus, assessmentYear, organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询360度评估失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询360度评估详情
     */
    @ApiOperation("根据ID查询360度评估详情")
    @GetMapping("/{assessmentId}")
    public MyJsonBean<Pm360Assessment> getById(
            @ApiParam("评估ID") @PathVariable Long assessmentId) {
        try {
            Pm360Assessment assessment = pm360AssessmentService.getById(assessmentId);
            return MyJsonBean.success(assessment);
        } catch (Exception e) {
            log.error("查询360度评估详情失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建360度评估
     */
    @ApiOperation("创建360度评估")
    @PostMapping
    public MyJsonBean<Pm360Assessment> create(@RequestBody Pm360Assessment assessment) {
        try {
            Pm360Assessment result = pm360AssessmentService.create(assessment);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("创建360度评估失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新360度评估
     */
    @ApiOperation("更新360度评估")
    @PutMapping
    public MyJsonBean<Pm360Assessment> update(@RequestBody Pm360Assessment assessment) {
        try {
            Pm360Assessment result = pm360AssessmentService.update(assessment);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("更新360度评估失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除360度评估
     */
    @ApiOperation("删除360度评估")
    @DeleteMapping("/{assessmentId}")
    public MyJsonBean<Boolean> delete(@ApiParam("评估ID") @PathVariable Long assessmentId) {
        try {
            boolean result = pm360AssessmentService.delete(assessmentId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("删除360度评估失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 启动评估
     */
    @ApiOperation("启动评估")
    @PostMapping("/{assessmentId}/start")
    public MyJsonBean<Boolean> startAssessment(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> startParams) {
        try {
            boolean result = pm360AssessmentService.startAssessment(assessmentId, startParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("启动评估失败", e);
            return MyJsonBean.error("启动失败：" + e.getMessage());
        }
    }

    /**
     * 完成评估
     */
    @ApiOperation("完成评估")
    @PostMapping("/{assessmentId}/complete")
    public MyJsonBean<Boolean> completeAssessment(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> completeParams) {
        try {
            boolean result = pm360AssessmentService.completeAssessment(assessmentId, completeParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("完成评估失败", e);
            return MyJsonBean.error("完成失败：" + e.getMessage());
        }
    }

    /**
     * 取消评估
     */
    @ApiOperation("取消评估")
    @PostMapping("/{assessmentId}/cancel")
    public MyJsonBean<Boolean> cancelAssessment(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> cancelParams) {
        try {
            boolean result = pm360AssessmentService.cancelAssessment(assessmentId, cancelParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("取消评估失败", e);
            return MyJsonBean.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 提交自评
     */
    @ApiOperation("提交自评")
    @PostMapping("/{assessmentId}/self-evaluation")
    public MyJsonBean<Boolean> submitSelfEvaluation(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> evaluationData) {
        try {
            boolean result = pm360AssessmentService.submitSelfEvaluation(assessmentId, evaluationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("提交自评失败", e);
            return MyJsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 提交上级评价
     */
    @ApiOperation("提交上级评价")
    @PostMapping("/{assessmentId}/superior-evaluation")
    public MyJsonBean<Boolean> submitSuperiorEvaluation(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> evaluationData) {
        try {
            boolean result = pm360AssessmentService.submitSuperiorEvaluation(assessmentId, evaluationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("提交上级评价失败", e);
            return MyJsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 提交同级评价
     */
    @ApiOperation("提交同级评价")
    @PostMapping("/{assessmentId}/peer-evaluation")
    public MyJsonBean<Boolean> submitPeerEvaluation(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> evaluationData) {
        try {
            boolean result = pm360AssessmentService.submitPeerEvaluation(assessmentId, evaluationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("提交同级评价失败", e);
            return MyJsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 提交下级评价
     */
    @ApiOperation("提交下级评价")
    @PostMapping("/{assessmentId}/subordinate-evaluation")
    public MyJsonBean<Boolean> submitSubordinateEvaluation(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> evaluationData) {
        try {
            boolean result = pm360AssessmentService.submitSubordinateEvaluation(assessmentId, evaluationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("提交下级评价失败", e);
            return MyJsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 提交客户评价
     */
    @ApiOperation("提交客户评价")
    @PostMapping("/{assessmentId}/customer-evaluation")
    public MyJsonBean<Boolean> submitCustomerEvaluation(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> evaluationData) {
        try {
            boolean result = pm360AssessmentService.submitCustomerEvaluation(assessmentId, evaluationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("提交客户评价失败", e);
            return MyJsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 计算评估结果
     */
    @ApiOperation("计算评估结果")
    @PostMapping("/{assessmentId}/calculate")
    public MyJsonBean<Map<String, Object>> calculateAssessmentResult(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> calculateParams) {
        try {
            Map<String, Object> result = pm360AssessmentService.calculateAssessmentResult(assessmentId, calculateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("计算评估结果失败", e);
            return MyJsonBean.error("计算失败：" + e.getMessage());
        }
    }

    /**
     * 生成评估报告
     */
    @ApiOperation("生成评估报告")
    @PostMapping("/{assessmentId}/report")
    public MyJsonBean<Map<String, Object>> generateAssessmentReport(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> reportParams) {
        try {
            Map<String, Object> result = pm360AssessmentService.generateAssessmentReport(assessmentId, reportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("生成评估报告失败", e);
            return MyJsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 获取评估统计
     */
    @ApiOperation("获取评估统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getAssessmentStatistics(
            @ApiParam("统计类型") @RequestParam(required = false) String statisticsType,
            @ApiParam("统计周期") @RequestParam(required = false) String statisticsPeriod,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId) {
        try {
            Map<String, Object> result = pm360AssessmentService.getAssessmentStatistics(
                statisticsType, statisticsPeriod, organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取评估统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取评估分析
     */
    @ApiOperation("获取评估分析")
    @GetMapping("/{assessmentId}/analysis")
    public MyJsonBean<Map<String, Object>> getAssessmentAnalysis(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestParam(required = false) String analysisType) {
        try {
            Map<String, Object> result = pm360AssessmentService.getAssessmentAnalysis(assessmentId, analysisType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取评估分析失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作评估
     */
    @ApiOperation("批量操作评估")
    @PostMapping("/batch-operation")
    public MyJsonBean<Boolean> batchOperation(@RequestBody Map<String, Object> batchData) {
        try {
            boolean result = pm360AssessmentService.batchOperation(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作评估失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 导入评估数据
     */
    @ApiOperation("导入评估数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importAssessments(@RequestBody Map<String, Object> importData) {
        try {
            Map<String, Object> result = pm360AssessmentService.importAssessments(importData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入评估数据失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出评估数据
     */
    @ApiOperation("导出评估数据")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportAssessments(@RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = pm360AssessmentService.exportAssessments(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出评估数据失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 复制评估
     */
    @ApiOperation("复制评估")
    @PostMapping("/{assessmentId}/copy")
    public MyJsonBean<Pm360Assessment> copyAssessment(
            @ApiParam("评估ID") @PathVariable Long assessmentId,
            @RequestBody Map<String, Object> copyParams) {
        try {
            Pm360Assessment result = pm360AssessmentService.copyAssessment(assessmentId, copyParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("复制评估失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 获取评估模板
     */
    @ApiOperation("获取评估模板")
    @GetMapping("/templates")
    public MyJsonBean<List<Map<String, Object>>> getAssessmentTemplates(
            @RequestParam(required = false) String templateType) {
        try {
            List<Map<String, Object>> result = pm360AssessmentService.getAssessmentTemplates(templateType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取评估模板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 应用评估模板
     */
    @ApiOperation("应用评估模板")
    @PostMapping("/templates/apply")
    public MyJsonBean<Pm360Assessment> applyAssessmentTemplate(@RequestBody Map<String, Object> templateParams) {
        try {
            Pm360Assessment result = pm360AssessmentService.applyAssessmentTemplate(templateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("应用评估模板失败", e);
            return MyJsonBean.error("应用失败：" + e.getMessage());
        }
    }

    /**
     * 刷新评估缓存
     */
    @ApiOperation("刷新评估缓存")
    @PostMapping("/refresh-cache")
    public MyJsonBean<Boolean> refreshAssessmentCache(@RequestParam(required = false) String cacheType) {
        try {
            boolean result = pm360AssessmentService.refreshAssessmentCache(cacheType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("刷新评估缓存失败", e);
            return MyJsonBean.error("刷新失败：" + e.getMessage());
        }
    }
}
