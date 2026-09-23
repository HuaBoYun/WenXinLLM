package com.huabo.fxgl.controller;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.MonthlyEvaluationDTO;
import com.huabo.fxgl.dto.ReminderDTO;
import com.huabo.fxgl.dto.RiskDatabaseDTO;
import com.huabo.fxgl.dto.RiskReviewAnalysisDTO;
import com.huabo.fxgl.dto.RiskEventCountDTO;
import com.huabo.fxgl.dto.RiskMeasureStatusDTO;
import com.huabo.fxgl.dto.RiskCompletionDTO;
import com.huabo.fxgl.service.IReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 催办提醒 前端控制器
 *
 * @author AI Assistant
 * @since 2025-01-07
 */
@Slf4j
@Tag(name = "催办提醒", description = "催办提醒相关接口")
@RestController
@RequestMapping(value = "/reminder", method = {RequestMethod.GET, RequestMethod.POST})
public class ReminderController {

    @Resource
    private IReminderService reminderService;

    /**
     * 查询未读的催办提醒列表
     *
     * @return 催办提醒列表
     */
    @Operation(summary = "查询未读的催办提醒列表 /reminder/getUnreadReminderList")
    @GetMapping(value = "/getUnreadReminderList")
    public String getUnreadReminderList() {
        try {
            log.info("开始查询未读催办提醒列表");

            List<ReminderDTO> list = reminderService.getUnreadReminderList();

            log.info("查询未读催办提醒列表成功, 数量: {}", list != null ? list.size() : 0);

            // 直接返回查询结果
            return JsonBean.success("查询成功", list);

        } catch (Exception e) {
            log.error("查询未读催办提醒列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询月度评估情况一览表
     *
     * @return 月度评估情况列表
     */
    @Operation(summary = "查询月度评估情况一览表 /reminder/getMonthlyEvaluationList")
    @GetMapping(value = "/getMonthlyEvaluationList")
    public String getMonthlyEvaluationList() {
        try {
            log.info("开始查询月度评估情况一览表");

            List<MonthlyEvaluationDTO> list = reminderService.getMonthlyEvaluationList();

            log.info("查询月度评估情况一览表成功, 数量: {}", list != null ? list.size() : 0);

            // 直接返回查询结果
            return JsonBean.success("查询成功", list);

        } catch (Exception e) {
            log.error("查询月度评估情况一览表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询风险数据库一览表
     *
     * @return 风险数据库一览表列表
     */
    @Operation(summary = "查询风险数据库一览表 /reminder/getRiskDatabaseList")
    @GetMapping(value = "/getRiskDatabaseList")
    public String getRiskDatabaseList() {
        try {
            log.info("开始查询风险数据库一览表");

            List<RiskDatabaseDTO> list = reminderService.getRiskDatabaseList();

            log.info("查询风险数据库一览表成功, 数量: {}", list != null ? list.size() : 0);

            // 直接返回查询结果
            return JsonBean.success("查询成功", list);

        } catch (Exception e) {
            log.error("查询风险数据库一览表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询风险审查情况分析(近12个月)
     *
     * @return 风险审查情况分析列表
     */
    @Operation(summary = "查询风险审查情况分析 /reminder/getRiskReviewAnalysis")
    @GetMapping(value = "/getRiskReviewAnalysis")
    public String getRiskReviewAnalysis() {
        try {
            log.info("开始查询风险审查情况分析(近12个月)");

            List<RiskReviewAnalysisDTO> list = reminderService.getRiskReviewAnalysis();

            log.info("查询风险审查情况分析成功, 数量: {}", list != null ? list.size() : 0);

            // 直接返回查询结果
            return JsonBean.success("查询成功", list);

        } catch (Exception e) {
            log.error("查询风险审查情况分析失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询风险事件数统计
     *
     * @return 风险事件数统计列表
     */
    @Operation(summary = "查询风险事件数统计 /reminder/getRiskEventCount")
    @GetMapping(value = "/getRiskEventCount")
    public String getRiskEventCount() {
        try {
            log.info("开始查询风险事件数统计");

            List<RiskEventCountDTO> list = reminderService.getRiskEventCount();

            log.info("查询风险事件数统计成功, 数量: {}", list != null ? list.size() : 0);

            // 直接返回查询结果
            return JsonBean.success("查询成功", list);

        } catch (Exception e) {
            log.error("查询风险事件数统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询风险措施状态统计
     *
     * @return 风险措施状态统计
     */
    @Operation(summary = "查询风险措施状态统计 /reminder/getRiskMeasureStatus")
    @GetMapping(value = "/getRiskMeasureStatus")
    public String getRiskMeasureStatus() {
        try {
            log.info("开始查询风险措施状态统计");

            RiskMeasureStatusDTO result = reminderService.getRiskMeasureStatus();

            log.info("查询风险措施状态统计成功: 未完成={}, 完成={}",
                    result.getNullRisklevelCount(), result.getNotNullRisklevelCount());

            // 直接返回查询结果
            return JsonBean.success("查询成功", result);

        } catch (Exception e) {
            log.error("查询风险措施状态统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询风险完成情况统计
     *
     * @return 风险完成情况统计列表
     */
    @Operation(summary = "查询风险完成情况统计 /reminder/getRiskCompletion")
    @GetMapping(value = "/getRiskCompletion")
    public String getRiskCompletion() {
        try {
            log.info("开始查询风险完成情况统计");

            List<RiskCompletionDTO> list = reminderService.getRiskCompletion();

            log.info("查询风险完成情况统计成功, 数量: {}", list != null ? list.size() : 0);

            // 直接返回查询结果
            return JsonBean.success("查询成功", list);

        } catch (Exception e) {
            log.error("查询风险完成情况统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
