package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.RiskAssessmentDetail;
import com.huabo.contract.service.RiskAssessmentDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险评估明细管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/risk/assessment/detail")
@Tag(name="风险评估明细管理",description="风险评估明细管理")
@Validated
public class RiskAssessmentDetailController {

    @Autowired
    private RiskAssessmentDetailService riskAssessmentDetailService;

    /**
     * 根据评估ID获取明细列表
     *
     * @param assessmentId 评估ID
     * @return 明细列表
     */
    @GetMapping("/list/{assessmentId}")
    @Operation(summary = "获取评估明细列表", description = "根据评估ID获取明细列表")
    public String getDetailsByAssessmentId(@PathVariable Long assessmentId) {
        try {
            log.info("获取评估明细列表，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            List<RiskAssessmentDetail> detailList = riskAssessmentDetailService.getDetailsByAssessmentId(assessmentId);
            return JsonBean.success("查询成功", detailList);
        } catch (Exception e) {
            log.error("获取评估明细列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存评估明细（批量保存）
     *
     * @param request 保存请求
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存评估明细", description = "批量保存评估明细")
    public String saveAssessmentDetails(@RequestBody Map<String, Object> request) {
        try {
            Long assessmentId = Long.valueOf(request.get("assessmentId").toString());
            @SuppressWarnings("unchecked")
            List<RiskAssessmentDetail> detailList = (List<RiskAssessmentDetail>) request.get("detailList");

            log.info("保存评估明细，评估ID：{}，明细数量：{}", assessmentId, detailList != null ? detailList.size() : 0);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            // 验证明细数据
            Map<String, Object> validateResult = riskAssessmentDetailService.validateAssessmentDetails(detailList);
            if (!(Boolean) validateResult.get("valid")) {
                @SuppressWarnings("unchecked")
                List<String> errors = (List<String>) validateResult.get("errors");
                return JsonBean.error("数据验证失败：" + String.join("; ", errors));
            }

            boolean result = riskAssessmentDetailService.saveAssessmentDetails(assessmentId, detailList);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存评估明细失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除评估明细
     *
     * @param assessmentId 评估ID
     * @return 删除结果
     */
    @DeleteMapping("/{assessmentId}")
    @Operation(summary = "删除评估明细", description = "根据评估ID删除所有明细")
    public String deleteDetailsByAssessmentId(@PathVariable Long assessmentId) {
        try {
            log.info("删除评估明细，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            boolean result = riskAssessmentDetailService.deleteDetailsByAssessmentId(assessmentId);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除评估明细失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 计算评估总分
     *
     * @param assessmentId 评估ID
     * @return 总分
     */
    @GetMapping("/calculateTotalScore/{assessmentId}")
    @Operation(summary = "计算评估总分", description = "根据明细数据计算总分")
    public String calculateTotalScore(@PathVariable Long assessmentId) {
        try {
            log.info("计算评估总分，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            BigDecimal totalScore = riskAssessmentDetailService.calculateTotalScore(assessmentId);
            return JsonBean.success("计算成功", totalScore);
        } catch (Exception e) {
            log.error("计算评估总分失败", e);
            return JsonBean.error("计算失败：" + e.getMessage());
        }
    }

    /**
     * 获取最高风险等级
     *
     * @param assessmentId 评估ID
     * @return 最高风险等级
     */
    @GetMapping("/getMaxRiskLevel/{assessmentId}")
    @Operation(summary = "获取最高风险等级", description = "根据明细数据获取最高风险等级")
    public String getMaxRiskLevel(@PathVariable Long assessmentId) {
        try {
            log.info("获取最高风险等级，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            Integer maxRiskLevel = riskAssessmentDetailService.getMaxRiskLevel(assessmentId);
            return JsonBean.success("查询成功", maxRiskLevel);
        } catch (Exception e) {
            log.error("获取最高风险等级失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据风险类别获取明细
     *
     * @param assessmentId 评估ID
     * @param riskCategory 风险类别
     * @return 明细列表
     */
    @GetMapping("/listByCategory/{assessmentId}/{riskCategory}")
    @Operation(summary = "根据风险类别获取明细", description = "根据评估ID和风险类别获取明细列表")
    public String getDetailsByRiskCategory(@PathVariable Long assessmentId, @PathVariable Integer riskCategory) {
        try {
            log.info("根据风险类别获取明细，评估ID：{}，风险类别：{}", assessmentId, riskCategory);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }
            if (riskCategory == null) {
                return JsonBean.error("风险类别不能为空");
            }

            List<RiskAssessmentDetail> detailList = riskAssessmentDetailService.getDetailsByRiskCategory(assessmentId, riskCategory);
            return JsonBean.success("查询成功", detailList);
        } catch (Exception e) {
            log.error("根据风险类别获取明细失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高风险项目明细
     *
     * @param assessmentId 评估ID
     * @param minRiskScore 最小风险得分（可选）
     * @return 高风险项目明细
     */
    @GetMapping("/highRiskItems/{assessmentId}")
    @Operation(summary = "获取高风险项目明细", description = "获取高风险项目明细")
    public String getHighRiskItems(@PathVariable Long assessmentId, 
                                  @RequestParam(required = false) BigDecimal minRiskScore) {
        try {
            log.info("获取高风险项目明细，评估ID：{}，最小风险得分：{}", assessmentId, minRiskScore);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            List<RiskAssessmentDetail> highRiskItems = riskAssessmentDetailService.getHighRiskItems(assessmentId, minRiskScore);
            return JsonBean.success("查询成功", highRiskItems);
        } catch (Exception e) {
            log.error("获取高风险项目明细失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险类别分布统计
     *
     * @param assessmentId 评估ID
     * @return 风险类别分布
     */
    @GetMapping("/riskCategoryDistribution/{assessmentId}")
    @Operation(summary = "获取风险类别分布统计", description = "统计各风险类别的分布情况")
    public String getRiskCategoryDistribution(@PathVariable Long assessmentId) {
        try {
            log.info("获取风险类别分布统计，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            List<Map<String, Object>> distribution = riskAssessmentDetailService.getRiskCategoryDistribution(assessmentId);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取风险类别分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险影响分布统计
     *
     * @param assessmentId 评估ID
     * @return 风险影响分布
     */
    @GetMapping("/riskImpactDistribution/{assessmentId}")
    @Operation(summary = "获取风险影响分布统计", description = "统计各风险影响等级的分布情况")
    public String getRiskImpactDistribution(@PathVariable Long assessmentId) {
        try {
            log.info("获取风险影响分布统计，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            List<Map<String, Object>> distribution = riskAssessmentDetailService.getRiskImpactDistribution(assessmentId);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取风险影响分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取需要缓解措施的风险项目
     *
     * @param assessmentId 评估ID
     * @return 需要缓解措施的风险项目
     */
    @GetMapping("/itemsNeedMitigation/{assessmentId}")
    @Operation(summary = "获取需要缓解措施的风险项目", description = "获取需要制定缓解措施的风险项目")
    public String getItemsNeedMitigation(@PathVariable Long assessmentId) {
        try {
            log.info("获取需要缓解措施的风险项目，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            List<RiskAssessmentDetail> items = riskAssessmentDetailService.getItemsNeedMitigation(assessmentId);
            return JsonBean.success("查询成功", items);
        } catch (Exception e) {
            log.error("获取需要缓解措施的风险项目失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 自动计算所有明细的风险得分
     *
     * @param assessmentId 评估ID
     * @return 计算结果
     */
    @PostMapping("/autoCalculateRiskScores/{assessmentId}")
    @Operation(summary = "自动计算风险得分", description = "自动计算所有明细的风险得分")
    public String autoCalculateRiskScores(@PathVariable Long assessmentId) {
        try {
            log.info("自动计算风险得分，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            boolean result = riskAssessmentDetailService.autoCalculateRiskScores(assessmentId);
            if (result) {
                return JsonBean.success("计算成功");
            } else {
                return JsonBean.error("计算失败");
            }
        } catch (Exception e) {
            log.error("自动计算风险得分失败", e);
            return JsonBean.error("计算失败：" + e.getMessage());
        }
    }

    /**
     * 生成风险评估报告数据
     *
     * @param assessmentId 评估ID
     * @return 报告数据
     */
    @GetMapping("/generateReport/{assessmentId}")
    @Operation(summary = "生成风险评估报告", description = "生成完整的风险评估报告数据")
    public String generateAssessmentReport(@PathVariable Long assessmentId) {
        try {
            log.info("生成风险评估报告，评估ID：{}", assessmentId);

            if (assessmentId == null) {
                return JsonBean.error("评估ID不能为空");
            }

            Map<String, Object> report = riskAssessmentDetailService.generateAssessmentReport(assessmentId);
            return JsonBean.success("生成成功", report);
        } catch (Exception e) {
            log.error("生成风险评估报告失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 复制评估明细到新评估
     *
     * @param request 复制请求
     * @return 复制结果
     */
    @PostMapping("/copyDetails")
    @Operation(summary = "复制评估明细", description = "将一个评估的明细复制到另一个评估")
    public String copyAssessmentDetails(@RequestBody Map<String, Object> request) {
        try {
            Long sourceAssessmentId = Long.valueOf(request.get("sourceAssessmentId").toString());
            Long targetAssessmentId = Long.valueOf(request.get("targetAssessmentId").toString());

            log.info("复制评估明细，源评估ID：{}，目标评估ID：{}", sourceAssessmentId, targetAssessmentId);

            if (sourceAssessmentId == null) {
                return JsonBean.error("源评估ID不能为空");
            }
            if (targetAssessmentId == null) {
                return JsonBean.error("目标评估ID不能为空");
            }

            boolean result = riskAssessmentDetailService.copyAssessmentDetails(sourceAssessmentId, targetAssessmentId);
            if (result) {
                return JsonBean.success("复制成功");
            } else {
                return JsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制评估明细失败", e);
            return JsonBean.error("复制失败：" + e.getMessage());
        }
    }
}
