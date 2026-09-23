package com.huabo.contract.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.RiskAssessment;
import com.huabo.contract.service.RiskAssessmentService;
import com.huabo.contract.service.RiskWarningService;
import com.huabo.contract.vo.RiskWarningQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险预警管理控制器
 * 实现智能风险识别和预警提示功能
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/riskWarning")
@Tag(name="风险预警管理",description="风险预警管理")
@Validated
public class RiskWarningController {

    @Autowired
    private RiskWarningService riskWarningService;

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    /**
     * 获取风险预警列表
     *
     * @param param 查询参数
     * @return 预警列表
     */
    @PostMapping("/list")
    @Operation(summary = "获取风险预警列表", description = "获取当前系统中的风险预警信息")
    public String getRiskWarningList(@RequestBody RiskWarningQueryParam param) {
        try {
            log.info("获取风险预警列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNum() == null || param.getPageNum() <= 0) {
                param.setPageNum(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<Map<String, Object>> pageInfo = riskWarningService.getRiskWarningList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("获取风险预警列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险预警统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取风险预警统计", description = "获取各类风险预警的统计数据")
    public String getRiskWarningStatistics() {
        try {
            log.info("获取风险预警统计信息");

            Map<String, Object> statistics = riskWarningService.getRiskWarningStatistics();

            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取风险预警统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高风险项目列表
     *
     * @return 高风险项目列表
     */
    @GetMapping("/highRiskProjects")
    @Operation(summary = "获取高风险项目", description = "获取风险等级为高或极高的项目列表")
    public String getHighRiskProjects() {
        try {
            log.info("获取高风险项目列表");

            List<Map<String, Object>> highRiskProjects = riskWarningService.getHighRiskProjects();

            return JsonBean.success("查询成功", highRiskProjects);
        } catch (Exception e) {
            log.error("获取高风险项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险趋势分析
     *
     * @param months 分析月数，默认12个月
     * @return 风险趋势数据
     */
    @GetMapping("/trendAnalysis")
    @Operation(summary = "风险趋势分析", description = "分析指定时间段内的风险变化趋势")
    public String getRiskTrendAnalysis(@RequestParam(defaultValue = "12") Integer months) {
        try {
            log.info("获取风险趋势分析，分析月数：{}", months);

            if (months <= 0 || months > 24) {
                return JsonBean.error("分析月数必须在1-24之间");
            }

            Map<String, Object> trendData = riskWarningService.getRiskTrendAnalysis(months);

            return JsonBean.success("查询成功", trendData);
        } catch (Exception e) {
            log.error("获取风险趋势分析失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 执行风险自动评估
     *
     * @param projectId 项目ID，如果为空则对所有项目进行评估
     * @return 评估结果
     */
    @PostMapping("/autoAssessment")
    @Operation(summary = "执行风险自动评估", description = "对指定项目或所有项目执行自动风险评估")
    public String executeAutoRiskAssessment(@RequestParam(required = false) Long projectId) {
        try {
            log.info("执行风险自动评估，项目ID：{}", projectId);

            Map<String, Object> result = riskWarningService.executeAutoRiskAssessment(projectId);

            return JsonBean.success("自动评估完成", result);
        } catch (Exception e) {
            log.error("执行风险自动评估失败", e);
            return JsonBean.error("自动评估失败：" + e.getMessage());
        }
    }

    /**
     * 获取相对方风险评估
     *
     * @param counterpartId 相对方ID
     * @return 相对方风险评估结果
     */
    @GetMapping("/counterpartRisk/{counterpartId}")
    @Operation(summary = "获取相对方风险评估", description = "获取指定相对方的风险评估信息")
    public String getCounterpartRiskAssessment(@PathVariable Long counterpartId) {
        try {
            log.info("获取相对方风险评估，相对方ID：{}", counterpartId);

            if (counterpartId == null) {
                return JsonBean.error("相对方ID不能为空");
            }

            Map<String, Object> riskInfo = riskWarningService.getCounterpartRiskAssessment(counterpartId);

            return JsonBean.success("查询成功", riskInfo);
        } catch (Exception e) {
            log.error("获取相对方风险评估失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成风险评估报告
     *
     * @param assessmentId 风险评估ID
     * @return 报告生成结果
     */
    @PostMapping("/generateReport/{assessmentId}")
    @Operation(summary = "生成风险评估报告", description = "生成指定风险评估的详细报告")
    public String generateRiskAssessmentReport(@PathVariable Long assessmentId) {
        try {
            log.info("生成风险评估报告，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            // 检查评估是否存在
            RiskAssessment assessment = riskAssessmentService.getRiskAssessmentById(assessmentId);
            if (assessment == null) {
                return JsonBean.error("风险评估不存在");
            }

            Map<String, Object> report = riskWarningService.generateRiskAssessmentReport(assessmentId);

            return JsonBean.success("报告生成成功", report);
        } catch (Exception e) {
            log.error("生成风险评估报告失败", e);
            return JsonBean.error("报告生成失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险预警配置
     *
     * @return 预警配置信息
     */
    @GetMapping("/warningConfig")
    @Operation(summary = "获取风险预警配置", description = "获取系统风险预警的配置参数")
    public String getRiskWarningConfig() {
        try {
            log.info("获取风险预警配置");

            Map<String, Object> config = riskWarningService.getRiskWarningConfig();

            return JsonBean.success("查询成功", config);
        } catch (Exception e) {
            log.error("获取风险预警配置失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 更新风险预警配置
     *
     * @param config 配置参数
     * @return 更新结果
     */
    @PostMapping("/warningConfig")
    @Operation(summary = "更新风险预警配置", description = "更新系统风险预警的配置参数")
    public String updateRiskWarningConfig(@RequestBody Map<String, Object> config) {
        try {
            log.info("更新风险预警配置，配置：{}", config);

            if (config == null || config.isEmpty()) {
                return JsonBean.error("配置参数不能为空");
            }

            boolean result = riskWarningService.updateRiskWarningConfig(config);
            if (result) {
                return JsonBean.success("配置更新成功");
            } else {
                return JsonBean.error("配置更新失败");
            }
        } catch (Exception e) {
            log.error("更新风险预警配置失败", e);
            return JsonBean.error("配置更新失败：" + e.getMessage());
        }
    }

    /**
     * 处理风险预警
     *
     * @param warningId 预警ID
     * @param action 处理动作：1-忽略，2-处理中，3-已处理
     * @param remarks 处理备注
     * @return 处理结果
     */
    @PostMapping("/handleWarning")
    @Operation(summary = "处理风险预警", description = "对风险预警进行处理操作")
    public String handleRiskWarning(@RequestParam Long warningId,
                                   @RequestParam Integer action,
                                   @RequestParam(required = false) String remarks) {
        try {
            log.info("处理风险预警，预警ID：{}，动作：{}，备注：{}", warningId, action, remarks);

            if (warningId == null) {
                return JsonBean.error("预警ID不能为空");
            }
            if (action == null || action < 1 || action > 3) {
                return JsonBean.error("处理动作无效");
            }

            boolean result = riskWarningService.handleRiskWarning(warningId, action, remarks);
            if (result) {
                return JsonBean.success("预警处理成功");
            } else {
                return JsonBean.error("预警处理失败");
            }
        } catch (Exception e) {
            log.error("处理风险预警失败", e);
            return JsonBean.error("预警处理失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险评估模板
     *
     * @param assessmentType 评估类型
     * @return 评估模板
     */
    @GetMapping("/assessmentTemplate/{assessmentType}")
    @Operation(summary = "获取风险评估模板", description = "获取指定类型的风险评估模板")
    public String getRiskAssessmentTemplate(@PathVariable Integer assessmentType) {
        try {
            log.info("获取风险评估模板，评估类型：{}", assessmentType);

            if (assessmentType == null) {
                return JsonBean.error("评估类型不能为空");
            }

            Map<String, Object> template = riskWarningService.getRiskAssessmentTemplate(assessmentType);

            return JsonBean.success("查询成功", template);
        } catch (Exception e) {
            log.error("获取风险评估模板失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 导出风险评估报告
     *
     * @param assessmentId 评估ID
     * @return 导出结果
     */
    @GetMapping("/exportReport/{assessmentId}")
    @Operation(summary = "导出风险评估报告", description = "导出指定风险评估的报告文件")
    public String exportRiskAssessmentReport(@PathVariable Long assessmentId) {
        try {
            log.info("导出风险评估报告，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            Map<String, Object> exportResult = riskWarningService.exportRiskAssessmentReport(assessmentId);

            return JsonBean.success("导出成功", exportResult);
        } catch (Exception e) {
            log.error("导出风险评估报告失败", e);
            return JsonBean.error("导出失败：" + e.getMessage());
        }
    }
}
