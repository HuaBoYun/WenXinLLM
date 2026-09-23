package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 企业风险管控统一控制器
 * 路径匹配前端API: /v1/enterprise/risk/**
 */
@Tag(name = "企业风险管控", description = "企业风险管控统一接口")
@RestController
@RequestMapping("/v1/enterprise/risk")
public class EnterpriseRiskController {

    private static final Logger log = LoggerFactory.getLogger(EnterpriseRiskController.class);

    @Autowired private RiskIdentificationMapper riskIdentificationMapper;
    @Autowired private RiskAssessmentMapper riskAssessmentMapper;
    @Autowired private RiskControlMeasureMapper riskControlMeasureMapper;
    @Autowired private RiskMonitoringMapper riskMonitoringMapper;
    @Autowired private RiskIncidentMapper riskIncidentMapper;
    @Autowired private RiskReportMapper riskReportMapper;
    @Autowired private RiskKnowledgeMapper riskKnowledgeMapper;
    @Autowired private RiskWarningMapper riskWarningMapper;

    // ==================== 风险统计概览 ====================

    @Operation(summary = "查询统计数据")
    @PostMapping("/statistics")
    public R getRiskStatistics(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            if (StringUtils.isEmpty(enterpriseId)) {
                return R.fail("企业ID不能为空");
            }
            Map<String, Object> statistics = new HashMap<>();
            // 识别风险总数
            Long totalRisks = riskIdentificationMapper.selectCount(
                new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId));
            // 高风险数
            Long highRisks = riskIdentificationMapper.selectCount(
                new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("RISK_LEVEL", "high"));
            // 控制措施数
            Long controlMeasures = riskControlMeasureMapper.selectCount(
                new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId));
            // 有效控制措施数
            Long effectiveControls = riskControlMeasureMapper.selectCount(
                new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId).eq("EFFECTIVENESS_LEVEL", "effective"));
            // 风险评分 - 取平均
            QueryWrapper<RiskAssessment> scoreWrapper = new QueryWrapper<RiskAssessment>()
                .eq("ENTERPRISE_ID", enterpriseId).isNotNull("OVERALL_RISK_SCORE");
            List<RiskAssessment> assessments = riskAssessmentMapper.selectList(scoreWrapper);
            BigDecimal riskScore = BigDecimal.ZERO;
            if (!assessments.isEmpty()) {
                BigDecimal sum = assessments.stream()
                    .map(RiskAssessment::getOverallRiskScore)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                riskScore = sum.divide(new BigDecimal(assessments.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            // 本月新增
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            Long newRisks = riskIdentificationMapper.selectCount(
                new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).ge("CREATE_TIME", monthStart));
            // 高风险占比
            double highRiskRatio = totalRisks > 0 ? (highRisks * 100.0 / totalRisks) : 0;
            // 有效率
            double effectiveRate = controlMeasures > 0 ? (effectiveControls * 100.0 / controlMeasures) : 0;

            statistics.put("totalRisks", totalRisks);
            statistics.put("highRisks", highRisks);
            statistics.put("controlMeasures", controlMeasures);
            statistics.put("riskScore", riskScore);
            statistics.put("newRisks", newRisks);
            statistics.put("highRiskRatio", Math.round(highRiskRatio * 10) / 10.0);
            statistics.put("effectiveRate", Math.round(effectiveRate * 10) / 10.0);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取风险统计失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    // ==================== 风险矩阵 ====================

    @Operation(summary = "查询数据")
    @PostMapping("/matrix")
    public R getRiskMatrix(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            List<RiskIdentification> list = riskIdentificationMapper.selectList(
                new QueryWrapper<RiskIdentification>()
                    .eq(StringUtils.isNotEmpty(enterpriseId), "ENTERPRISE_ID", enterpriseId));
            // 构建矩阵数据: [可能性index, 影响程度index, 数量, 风险等级index]
            List<int[]> matrixData = new ArrayList<>();
            for (RiskIdentification ri : list) {
                int probIdx = getProbIndex(ri.getProbability());
                int impactIdx = getImpactIndex(ri.getImpact());
                int levelIdx = getLevelIndex(ri.getRiskLevel());
                matrixData.add(new int[]{probIdx, impactIdx, 1, levelIdx});
            }
            // 合并相同位置的数据
            Map<String, int[]> merged = new LinkedHashMap<>();
            for (int[] d : matrixData) {
                String key = d[0] + "," + d[1];
                if (merged.containsKey(key)) {
                    merged.get(key)[2] += d[2];
                    merged.get(key)[3] = Math.max(merged.get(key)[3], d[3]);
                } else {
                    merged.put(key, new int[]{d[0], d[1], d[2], d[3]});
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("matrixData", new ArrayList<>(merged.values()));
            return R.success(result);
        } catch (Exception e) {
            log.error("获取风险矩阵失败", e);
            return R.fail("获取风险矩阵失败：" + e.getMessage());
        }
    }

    // ==================== 风险分布分析 ====================

    @Operation(summary = "查询数据")
    @PostMapping("/report/distribution")
    public R getRiskDistribution(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            List<RiskIdentification> list = riskIdentificationMapper.selectList(
                new QueryWrapper<RiskIdentification>()
                    .eq(StringUtils.isNotEmpty(enterpriseId), "ENTERPRISE_ID", enterpriseId));
            // 按风险类型统计
            Map<String, Integer> typeCount = new LinkedHashMap<>();
            typeCount.put("财务风险", 0); typeCount.put("运营风险", 0);
            typeCount.put("市场风险", 0); typeCount.put("技术风险", 0);
            typeCount.put("合规风险", 0);
            for (RiskIdentification ri : list) {
                String typeText = getTypeText(ri.getRiskType());
                typeCount.put(typeText, typeCount.getOrDefault(typeText, 0) + 1);
            }
            List<Map<String, Object>> distributionData = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey());
                item.put("value", entry.getValue());
                distributionData.add(item);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("distributionData", distributionData);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取风险分布失败", e);
            return R.fail("获取风险分布失败：" + e.getMessage());
        }
    }

    // ==================== 风险预警 ====================

    @Operation(summary = "查询列表")
    @PostMapping("/warning/list")
    public R getWarningList(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Integer pageNumber = params.get("pageNumber") != null ? (Integer) params.get("pageNumber") : 1;
            Integer pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
            String warningStatus = (String) params.get("warningStatus");

            QueryWrapper<RiskWarning> wrapper = new QueryWrapper<RiskWarning>()
                .eq(StringUtils.isNotEmpty(enterpriseId), "ENTERPRISE_ID", enterpriseId)
                .eq(StringUtils.isNotEmpty(warningStatus), "WARNING_STATUS", warningStatus)
                .orderByDesc("WARNING_TIME");
            IPage<RiskWarning> page = riskWarningMapper.selectPage(new Page<>(pageNumber, pageSize), wrapper);
            Map<String, Object> result = new HashMap<>();
            result.put("records", page.getRecords());
            result.put("total", page.getTotal());
            result.put("current", page.getCurrent());
            result.put("size", page.getSize());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取预警列表失败", e);
            return R.fail("获取预警列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "handleWarning")
    @PostMapping("/warning/handle")
    public R handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String warningId = (String) params.get("warningId");
            String handledBy = (String) params.get("handledBy");
            String handleResult = (String) params.get("handleResult");
            if (StringUtils.isEmpty(warningId)) return R.fail("预警ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskWarning> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("WARNING_ID", warningId)
                .set("WARNING_STATUS", "HANDLED")
                .set(handledBy != null, "HANDLED_BY", handledBy)
                .set("HANDLE_TIME", LocalDateTime.now())
                .set(handleResult != null, "HANDLE_RESULT", handleResult)
                .set("IS_READ", 1);
            riskWarningMapper.update(null, wrapper);
            return R.success("处理成功");
        } catch (Exception e) {
            log.error("处理预警失败", e);
            return R.fail("处理预警失败：" + e.getMessage());
        }
    }

    // ==================== 风险识别 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/identification/list")
    public R getIdentificationList(@RequestBody Map<String, Object> params) {
        return queryList(riskIdentificationMapper, RiskIdentification.class, params,
            "RISK_TYPE", "RISK_LEVEL", "STATUS", "RISK_NAME");
    }

    @Operation(summary = "新增")
    @PostMapping("/identification")
    public R addIdentification(@RequestBody Map<String, Object> params) {
        try {
            RiskIdentification entity = new RiskIdentification();
            entity.setEnterpriseId((String) params.get("enterpriseId"));
            entity.setEnterpriseName((String) params.get("enterpriseName"));
            entity.setRiskName((String) params.get("riskName"));
            entity.setRiskCode((String) params.get("riskCode"));
            entity.setRiskType((String) params.get("riskType"));
            entity.setRiskLevel((String) params.get("riskLevel"));
            entity.setIdentificationMethod((String) params.get("identificationMethod"));
            entity.setIdentifiedBy((String) params.get("identifiedBy"));
            entity.setStatus((String) params.get("status"));
            entity.setRiskDesc((String) params.get("riskDesc"));
            entity.setImpact((String) params.get("impact"));
            if (params.get("probability") != null) {
                entity.setProbability(new BigDecimal(params.get("probability").toString()));
            }
            // 识别日期：前端传了就用前端的，没传就用当天
            if (params.get("identificationDate") != null && !params.get("identificationDate").toString().isEmpty()) {
                entity.setIdentificationDate(LocalDate.parse(params.get("identificationDate").toString()));
            } else {
                entity.setIdentificationDate(LocalDate.now());
            }
            entity.setCreateTime(LocalDateTime.now());
            entity.setDeleted(false);
            riskIdentificationMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增风险识别失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/identification")
    public R updateIdentification(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskIdentificationId");
            if (StringUtils.isEmpty(id)) return R.fail("风险识别ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskIdentification> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_IDENTIFICATION_ID", id);
            if (params.containsKey("riskName")) wrapper.set("RISK_NAME", params.get("riskName"));
            if (params.containsKey("riskType")) wrapper.set("RISK_TYPE", params.get("riskType"));
            if (params.containsKey("riskLevel")) wrapper.set("RISK_LEVEL", params.get("riskLevel"));
            if (params.containsKey("identificationMethod")) wrapper.set("IDENTIFICATION_METHOD", params.get("identificationMethod"));
            if (params.containsKey("identifiedBy")) wrapper.set("IDENTIFIED_BY", params.get("identifiedBy"));
            if (params.containsKey("status")) wrapper.set("STATUS", params.get("status"));
            if (params.containsKey("riskDesc")) wrapper.set("RISK_DESC", params.get("riskDesc"));
            if (params.containsKey("impact")) wrapper.set("IMPACT", params.get("impact"));
            if (params.get("probability") != null) {
                wrapper.set("PROBABILITY", new BigDecimal(params.get("probability").toString()));
            }
            wrapper.set("UPDATE_TIME", LocalDateTime.now());
            riskIdentificationMapper.update(null, wrapper);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新风险识别失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/identification/{id}")
    public R deleteIdentification(@PathVariable String id) {
        riskIdentificationMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Operation(summary = "分析")
    @PostMapping("/identification/analyze")
    public R analyzeIdentification(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        Long totalRisks = riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId));
        Long confirmedRisks = riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("STATUS", "confirmed"));
        overview.put("totalRisks", totalRisks);
        overview.put("highRisks", riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("RISK_LEVEL", "high")));
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        overview.put("monthlyNew", riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).ge("CREATE_TIME", monthStart)));
        // 识别准确率 = 已确认风险数 / 总识别数 * 100
        double accuracy = totalRisks > 0 ? Math.round(confirmedRisks * 1000.0 / totalRisks) / 10.0 : 0;
        overview.put("accuracy", accuracy);
        return R.success(overview);
    }

    // ==================== 风险评估 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/assessment/list")
    public R getAssessmentList(@RequestBody Map<String, Object> params) {
        return queryList(riskAssessmentMapper, RiskAssessment.class, params,
            "ASSESSMENT_TYPE", "OVERALL_RISK_LEVEL", "ASSESSMENT_STATUS", "ASSESSMENT_NAME");
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/assessment/{id}")
    public R getAssessmentById(@PathVariable String id) {
        return R.success(riskAssessmentMapper.selectById(id));
    }

    @Operation(summary = "新增")
    @PostMapping("/assessment")
    public R addAssessment(@RequestBody Map<String, Object> params) {
        try {
            RiskAssessment entity = new RiskAssessment();
            entity.setEnterpriseId((String) params.get("enterpriseId"));
            entity.setEnterpriseName((String) params.get("enterpriseName"));
            entity.setAssessmentName((String) params.get("assessmentName"));
            entity.setAssessmentType((String) params.get("assessmentType"));
            entity.setAssessmentMethod((String) params.get("assessmentMethod"));
            entity.setOverallRiskLevel((String) params.get("overallRiskLevel"));
            if (params.get("overallRiskScore") != null) {
                entity.setOverallRiskScore(new BigDecimal(params.get("overallRiskScore").toString()));
            }
            entity.setAssessor((String) params.get("assessor"));
            entity.setAssessmentStatus((String) params.get("assessmentStatus"));
            entity.setAssessmentConclusion((String) params.get("assessmentConclusion"));
            if (params.get("assessmentDate") != null && !params.get("assessmentDate").toString().isEmpty()) {
                entity.setAssessmentDate(LocalDate.parse(params.get("assessmentDate").toString()));
            }
            entity.setCreateTime(LocalDateTime.now());
            entity.setDeleted(false);
            riskAssessmentMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增风险评估失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/assessment")
    public R updateAssessment(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskAssessmentId");
            if (StringUtils.isEmpty(id)) return R.fail("评估ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskAssessment> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_ASSESSMENT_ID", id);
            if (params.containsKey("assessmentName")) wrapper.set("ASSESSMENT_NAME", params.get("assessmentName"));
            if (params.containsKey("assessmentType")) wrapper.set("ASSESSMENT_TYPE", params.get("assessmentType"));
            if (params.containsKey("assessmentMethod")) wrapper.set("ASSESSMENT_METHOD", params.get("assessmentMethod"));
            if (params.containsKey("overallRiskLevel")) wrapper.set("OVERALL_RISK_LEVEL", params.get("overallRiskLevel"));
            if (params.get("overallRiskScore") != null) {
                wrapper.set("OVERALL_RISK_SCORE", new BigDecimal(params.get("overallRiskScore").toString()));
            }
            if (params.containsKey("assessor")) wrapper.set("ASSESSOR", params.get("assessor"));
            if (params.containsKey("assessmentStatus")) wrapper.set("ASSESSMENT_STATUS", params.get("assessmentStatus"));
            if (params.containsKey("assessmentConclusion")) wrapper.set("ASSESSMENT_CONCLUSION", params.get("assessmentConclusion"));
            if (params.get("assessmentDate") != null && !params.get("assessmentDate").toString().isEmpty()) {
                wrapper.set("ASSESSMENT_DATE", LocalDate.parse(params.get("assessmentDate").toString()));
            }
            wrapper.set("UPDATE_TIME", LocalDateTime.now());
            riskAssessmentMapper.update(null, wrapper);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新风险评估失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/assessment/{id}")
    public R deleteAssessment(@PathVariable String id) {
        riskAssessmentMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Operation(summary = "提交")
    @PostMapping("/assessment/submit")
    public R submitAssessment(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskAssessmentId");
            if (StringUtils.isEmpty(id)) return R.fail("评估ID不能为空");
            // 使用UpdateWrapper直接更新指定字段，避免@Version冲突
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskAssessment> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_ASSESSMENT_ID", id)
                .set("REVIEW_STATUS", "PENDING")
                .set("ASSESSMENT_STATUS", "IN_PROGRESS")
                .set("UPDATE_TIME", LocalDateTime.now());
            int rows = riskAssessmentMapper.update(null, wrapper);
            if (rows > 0) {
                return R.success("提交成功");
            } else {
                return R.fail("评估不存在或已被删除");
            }
        } catch (Exception e) {
            log.error("提交评估失败", e);
            return R.fail("提交失败：" + e.getMessage());
        }
    }

    @Operation(summary = "审批")
    @PostMapping("/assessment/audit")
    public R auditAssessment(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskAssessmentId");
            String reviewStatus = (String) params.get("reviewStatus");
            if (StringUtils.isEmpty(id)) return R.fail("评估ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskAssessment> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_ASSESSMENT_ID", id)
                .set("REVIEW_STATUS", reviewStatus)
                .set(params.get("reviewer") != null, "REVIEWER", params.get("reviewer"))
                .set(params.get("reviewComments") != null, "REVIEW_COMMENTS", params.get("reviewComments"))
                .set("REVIEW_TIME", LocalDateTime.now())
                .set("UPDATE_TIME", LocalDateTime.now());
            riskAssessmentMapper.update(null, wrapper);
            return R.success("审核成功");
        } catch (Exception e) {
            log.error("审核评估失败", e);
            return R.fail("审核失败：" + e.getMessage());
        }
    }

    @Operation(summary = "分析")
    @PostMapping("/assessment/analyze")
    public R analyzeAssessment(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalAssessments", riskAssessmentMapper.selectCount(
            new QueryWrapper<RiskAssessment>().eq("ENTERPRISE_ID", enterpriseId)));
        overview.put("highRiskCount", riskAssessmentMapper.selectCount(
            new QueryWrapper<RiskAssessment>().eq("ENTERPRISE_ID", enterpriseId).eq("OVERALL_RISK_LEVEL", "HIGH")));
        overview.put("mediumRiskCount", riskAssessmentMapper.selectCount(
            new QueryWrapper<RiskAssessment>().eq("ENTERPRISE_ID", enterpriseId).eq("OVERALL_RISK_LEVEL", "MEDIUM")));
        overview.put("lowRiskCount", riskAssessmentMapper.selectCount(
            new QueryWrapper<RiskAssessment>().eq("ENTERPRISE_ID", enterpriseId).in("OVERALL_RISK_LEVEL", "LOW", "VERY_LOW")));
        return R.success(overview);
    }

    // ==================== 控制措施 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/control/list")
    public R getControlList(@RequestBody Map<String, Object> params) {
        return queryList(riskControlMeasureMapper, RiskControlMeasure.class, params,
            "MEASURE_TYPE", "IMPLEMENTATION_STATUS", "EFFECTIVENESS_LEVEL", "MEASURE_NAME");
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/control/{id}")
    public R getControlById(@PathVariable String id) {
        return R.success(riskControlMeasureMapper.selectById(id));
    }

    @Operation(summary = "新增")
    @PostMapping("/control")
    public R addControl(@RequestBody Map<String, Object> params) {
        try {
            RiskControlMeasure entity = new RiskControlMeasure();
            entity.setEnterpriseId((String) params.get("enterpriseId"));
            entity.setEnterpriseName((String) params.get("enterpriseName"));
            entity.setMeasureName((String) params.get("measureName"));
            entity.setMeasureType((String) params.get("measureType"));
            entity.setMeasureCategory((String) params.get("measureCategory"));
            entity.setMeasureDescription((String) params.get("measureDescription"));
            entity.setTargetRiskType((String) params.get("targetRiskType"));
            entity.setPriority((String) params.get("priority"));
            entity.setResponsiblePerson((String) params.get("responsiblePerson"));
            entity.setImplementationStatus((String) params.get("implementationStatus"));
            entity.setEffectivenessLevel("effective");
            entity.setRiskAssessmentId((String) params.get("riskAssessmentId"));
            if (params.get("plannedCompletionDate") != null && !params.get("plannedCompletionDate").toString().isEmpty()) {
                entity.setPlannedCompletionDate(LocalDate.parse(params.get("plannedCompletionDate").toString()));
            }
            entity.setCreateTime(LocalDateTime.now());
            entity.setDeleted(false);
            riskControlMeasureMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增控制措施失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/control")
    public R updateControl(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("controlMeasureId");
            if (StringUtils.isEmpty(id)) return R.fail("控制措施ID不能为空");
            RiskControlMeasure entity = riskControlMeasureMapper.selectById(id);
            if (entity == null) return R.fail("控制措施不存在");
            if (params.containsKey("measureName")) entity.setMeasureName((String) params.get("measureName"));
            if (params.containsKey("measureType")) entity.setMeasureType((String) params.get("measureType"));
            if (params.containsKey("measureCategory")) entity.setMeasureCategory((String) params.get("measureCategory"));
            if (params.containsKey("measureDescription")) entity.setMeasureDescription((String) params.get("measureDescription"));
            if (params.containsKey("targetRiskType")) entity.setTargetRiskType((String) params.get("targetRiskType"));
            if (params.containsKey("priority")) entity.setPriority((String) params.get("priority"));
            if (params.containsKey("responsiblePerson")) entity.setResponsiblePerson((String) params.get("responsiblePerson"));
            if (params.containsKey("implementationStatus")) entity.setImplementationStatus((String) params.get("implementationStatus"));
            entity.setUpdateTime(LocalDateTime.now());
            riskControlMeasureMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新控制措施失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/control/{id}")
    public R deleteControl(@PathVariable String id) {
        riskControlMeasureMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Operation(summary = "implementControl")
    @PostMapping("/control/implement")
    public R implementControl(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("controlMeasureId");
            if (StringUtils.isEmpty(id)) return R.fail("控制措施ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskControlMeasure> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("CONTROL_MEASURE_ID", id)
                .set("IMPLEMENTATION_STATUS", "implementing")
                .set("UPDATE_TIME", LocalDateTime.now());
            riskControlMeasureMapper.update(null, wrapper);
            return R.success("实施成功");
        } catch (Exception e) {
            log.error("实施控制措施失败", e);
            return R.fail("实施失败：" + e.getMessage());
        }
    }

    @Operation(summary = "evaluateControl")
    @PostMapping("/control/evaluate")
    public R evaluateControl(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("controlMeasureId");
            if (StringUtils.isEmpty(id)) return R.fail("控制措施ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskControlMeasure> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("CONTROL_MEASURE_ID", id)
                .set("EFFECTIVENESS_LEVEL", params.get("effectiveness"))
                .set("UPDATE_TIME", LocalDateTime.now());
            if (params.get("effectivenessScore") != null) {
                wrapper.set("EFFECTIVENESS_SCORE", new BigDecimal(params.get("effectivenessScore").toString()));
            }
            riskControlMeasureMapper.update(null, wrapper);
            return R.success("评估成功");
        } catch (Exception e) {
            log.error("评估控制措施失败", e);
            return R.fail("评估失败：" + e.getMessage());
        }
    }

    // ==================== 风险监控 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/monitoring/list")
    public R getMonitoringList(@RequestBody Map<String, Object> params) {
        return queryList(riskMonitoringMapper, RiskMonitoring.class, params,
            "MONITORING_TYPE", "CURRENT_RISK_LEVEL", "MONITORING_STATUS", "INDICATOR_NAME");
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/monitoring/{id}")
    public R getMonitoringById(@PathVariable String id) {
        return R.success(riskMonitoringMapper.selectById(id));
    }

    @Operation(summary = "新增")
    @PostMapping("/monitoring")
    public R addMonitoring(@RequestBody Map<String, Object> params) {
        try {
            RiskMonitoring entity = new RiskMonitoring();
            entity.setEnterpriseId((String) params.get("enterpriseId"));
            entity.setEnterpriseName((String) params.get("enterpriseName"));
            entity.setIndicatorName((String) params.get("indicatorName"));
            entity.setMonitoringType((String) params.get("monitoringType"));
            entity.setMonitoringFrequency((String) params.get("monitoringFrequency"));
            entity.setMonitoringStatus((String) params.get("monitoringStatus"));
            if (params.get("currentValue") != null) {
                entity.setCurrentValue(new BigDecimal(params.get("currentValue").toString()));
            }
            if (params.get("warningThreshold") != null) {
                entity.setWarningThreshold(new BigDecimal(params.get("warningThreshold").toString()));
            }
            if (params.get("dangerThreshold") != null) {
                entity.setDangerThreshold(new BigDecimal(params.get("dangerThreshold").toString()));
            }
            // 根据当前值和阈值自动计算风险等级
            BigDecimal cv = entity.getCurrentValue();
            BigDecimal dt = entity.getDangerThreshold();
            BigDecimal wt = entity.getWarningThreshold();
            if (cv != null && dt != null && cv.compareTo(dt) >= 0) {
                entity.setCurrentRiskLevel("high");
            } else if (cv != null && wt != null && cv.compareTo(wt) >= 0) {
                entity.setCurrentRiskLevel("medium");
            } else {
                entity.setCurrentRiskLevel("low");
            }
            entity.setRiskTrend("stable");
            entity.setCreateTime(LocalDateTime.now());
            entity.setDeleted(false);
            riskMonitoringMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增监控指标失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/monitoring")
    public R updateMonitoring(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskMonitoringId");
            if (StringUtils.isEmpty(id)) return R.fail("监控ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskMonitoring> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_MONITORING_ID", id);
            if (params.containsKey("indicatorName")) wrapper.set("INDICATOR_NAME", params.get("indicatorName"));
            if (params.containsKey("monitoringType")) wrapper.set("MONITORING_TYPE", params.get("monitoringType"));
            if (params.containsKey("monitoringFrequency")) wrapper.set("MONITORING_FREQUENCY", params.get("monitoringFrequency"));
            if (params.containsKey("monitoringStatus")) wrapper.set("MONITORING_STATUS", params.get("monitoringStatus"));
            if (params.get("currentValue") != null) {
                wrapper.set("CURRENT_VALUE", new BigDecimal(params.get("currentValue").toString()));
            }
            if (params.get("warningThreshold") != null) {
                wrapper.set("WARNING_THRESHOLD", new BigDecimal(params.get("warningThreshold").toString()));
            }
            if (params.get("dangerThreshold") != null) {
                wrapper.set("DANGER_THRESHOLD", new BigDecimal(params.get("dangerThreshold").toString()));
            }
            // 重新计算风险等级
            BigDecimal cv = params.get("currentValue") != null ? new BigDecimal(params.get("currentValue").toString()) : null;
            BigDecimal dt = params.get("dangerThreshold") != null ? new BigDecimal(params.get("dangerThreshold").toString()) : null;
            BigDecimal wt = params.get("warningThreshold") != null ? new BigDecimal(params.get("warningThreshold").toString()) : null;
            if (cv != null && dt != null && cv.compareTo(dt) >= 0) {
                wrapper.set("CURRENT_RISK_LEVEL", "high");
            } else if (cv != null && wt != null && cv.compareTo(wt) >= 0) {
                wrapper.set("CURRENT_RISK_LEVEL", "medium");
            } else if (cv != null) {
                wrapper.set("CURRENT_RISK_LEVEL", "low");
            }
            wrapper.set("UPDATE_TIME", LocalDateTime.now());
            riskMonitoringMapper.update(null, wrapper);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新监控指标失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/monitoring/{id}")
    public R deleteMonitoring(@PathVariable String id) {
        riskMonitoringMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Operation(summary = "刷新")
    @PostMapping("/monitoring/refresh")
    public R refreshMonitoring(@RequestBody Map<String, Object> params) {
        return R.success("刷新成功");
    }

    // ==================== 风险事件 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/incident/list")
    public R getIncidentList(@RequestBody Map<String, Object> params) {
        return queryList(riskIncidentMapper, RiskIncident.class, params,
            "INCIDENT_TYPE", "RISK_LEVEL", "HANDLING_STATUS", "INCIDENT_NAME");
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/incident/{id}")
    public R getIncidentById(@PathVariable String id) {
        return R.success(riskIncidentMapper.selectById(id));
    }

    @Operation(summary = "新增")
    @PostMapping("/incident")
    public R addIncident(@RequestBody Map<String, Object> params) {
        try {
            RiskIncident entity = new RiskIncident();
            entity.setEnterpriseId((String) params.get("enterpriseId"));
            entity.setEnterpriseName((String) params.get("enterpriseName"));
            entity.setIncidentName((String) params.get("incidentName"));
            entity.setIncidentNumber((String) params.get("incidentNumber"));
            entity.setIncidentType((String) params.get("incidentType"));
            entity.setRiskLevel((String) params.get("riskLevel"));
            entity.setReporter((String) params.get("reporter"));
            entity.setIncidentDescription((String) params.get("incidentDescription"));
            entity.setHandlingStatus((String) params.get("handlingStatus"));
            entity.setHandlingResponsiblePerson((String) params.get("handlingResponsiblePerson"));
            // 发生时间：默认当前时间
            entity.setOccurrenceTime(LocalDateTime.now());
            // 经济损失：默认0
            entity.setTotalEconomicLoss(BigDecimal.ZERO);
            entity.setCreateTime(LocalDateTime.now());
            entity.setDeleted(false);
            riskIncidentMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增风险事件失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/incident")
    public R updateIncident(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskIncidentId");
            if (StringUtils.isEmpty(id)) return R.fail("事件ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskIncident> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_INCIDENT_ID", id);
            if (params.containsKey("incidentName")) wrapper.set("INCIDENT_NAME", params.get("incidentName"));
            if (params.containsKey("incidentType")) wrapper.set("INCIDENT_TYPE", params.get("incidentType"));
            if (params.containsKey("riskLevel")) wrapper.set("RISK_LEVEL", params.get("riskLevel"));
            if (params.containsKey("reporter")) wrapper.set("REPORTER", params.get("reporter"));
            if (params.containsKey("incidentDescription")) wrapper.set("INCIDENT_DESCRIPTION", params.get("incidentDescription"));
            if (params.containsKey("handlingStatus")) wrapper.set("HANDLING_STATUS", params.get("handlingStatus"));
            if (params.containsKey("handlingResponsiblePerson")) wrapper.set("HANDLING_RESPONSIBLE_PERSON", params.get("handlingResponsiblePerson"));
            if (params.containsKey("handlingResult")) wrapper.set("HANDLING_RESULT", params.get("handlingResult"));
            wrapper.set("UPDATE_TIME", LocalDateTime.now());
            riskIncidentMapper.update(null, wrapper);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新风险事件失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/incident/{id}")
    public R deleteIncident(@PathVariable String id) {
        riskIncidentMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Operation(summary = "handleIncident")
    @PostMapping("/incident/handle")
    public R handleIncident(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskIncidentId");
            if (StringUtils.isEmpty(id)) return R.fail("事件ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskIncident> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_INCIDENT_ID", id)
                .set("HANDLING_STATUS", "handling")
                .set(params.get("assignedTo") != null, "HANDLING_RESPONSIBLE_PERSON", params.get("assignedTo"))
                .set("UPDATE_TIME", LocalDateTime.now());
            riskIncidentMapper.update(null, wrapper);
            return R.success("处理成功");
        } catch (Exception e) {
            log.error("处理事件失败", e);
            return R.fail("处理失败：" + e.getMessage());
        }
    }

    @Operation(summary = "closeIncident")
    @PostMapping("/incident/close")
    public R closeIncident(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskIncidentId");
            if (StringUtils.isEmpty(id)) return R.fail("事件ID不能为空");
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskIncident> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_INCIDENT_ID", id)
                .set("HANDLING_STATUS", "closed")
                .set("INCIDENT_STATUS", "closed")
                .set(params.get("resolution") != null, "HANDLING_RESULT", params.get("resolution"))
                .set("UPDATE_TIME", LocalDateTime.now());
            riskIncidentMapper.update(null, wrapper);
            return R.success("关闭成功");
        } catch (Exception e) {
            log.error("关闭事件失败", e);
            return R.fail("关闭失败：" + e.getMessage());
        }
    }

    // ==================== 风险报告 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/report/list")
    public R getReportList(@RequestBody Map<String, Object> params) {
        return queryList(riskReportMapper, RiskReport.class, params,
            "REPORT_TYPE", "STATUS", "PERIOD", "REPORT_TITLE");
    }

    @Operation(summary = "生成")
    @PostMapping("/report/generate")
    public R generateReport(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        String reportType = (String) params.get("reportType");
        String period = (String) params.get("period");
        RiskReport report = new RiskReport();
        report.setEnterpriseId(enterpriseId);
        report.setEnterpriseName((String) params.get("enterpriseName"));
        report.setReportTitle(period + "风险" + getReportTypeText(reportType) + "报告");
        report.setReportCode("RPT-" + System.currentTimeMillis());
        report.setReportType(reportType);
        report.setPeriod(period);
        report.setStatus("draft");
        report.setCreator((String) params.get("creator"));
        report.setCreateTime(LocalDateTime.now());
        report.setDownloadCount(0);
        report.setDeleted(false);
        riskReportMapper.insert(report);
        return R.success(report);
    }

    @Operation(summary = "导出")
    @GetMapping("/report/download/{id}")
    public R downloadReport(@PathVariable String id) {
        try {
            // 下载计数+1
            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<RiskReport> wrapper =
                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            wrapper.eq("RISK_REPORT_ID", id).setSql("DOWNLOAD_COUNT = DOWNLOAD_COUNT + 1");
            riskReportMapper.update(null, wrapper);
            RiskReport report = riskReportMapper.selectById(id);
            return R.success(report);
        } catch (Exception e) {
            log.error("下载报告失败", e);
            return R.fail("下载失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询数据")
    @PostMapping("/report/trend")
    public R getReportTrend(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        List<RiskAssessment> assessments = riskAssessmentMapper.selectList(
            new QueryWrapper<RiskAssessment>().eq("ENTERPRISE_ID", enterpriseId)
                .orderByDesc("ASSESSMENT_DATE"));
        List<Map<String, Object>> trendData = new ArrayList<>();
        for (RiskAssessment ra : assessments) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", ra.getAssessmentDate() != null ? ra.getAssessmentDate().toString() : "");
            item.put("score", ra.getOverallRiskScore());
            item.put("level", ra.getOverallRiskLevel());
            trendData.add(item);
        }
        return R.success(trendData);
    }

    @Operation(summary = "更新")
    @PutMapping("/report")
    public R updateReport(@RequestBody RiskReport entity) {
        entity.setUpdateTime(LocalDateTime.now());
        riskReportMapper.updateById(entity);
        return R.success("更新成功");
    }

    @Operation(summary = "删除")
    @DeleteMapping("/report/{id}")
    public R deleteReport(@PathVariable String id) {
        riskReportMapper.deleteById(id);
        return R.success("删除成功");
    }

    // ==================== 风险知识库 CRUD ====================

    @Operation(summary = "查询列表")
    @PostMapping("/knowledge/list")
    public R getKnowledgeList(@RequestBody Map<String, Object> params) {
        return queryList(riskKnowledgeMapper, RiskKnowledge.class, params,
            "KNOWLEDGE_TYPE", "RISK_CATEGORY", "STATUS", "TITLE");
    }

    @Operation(summary = "新增")
    @PostMapping("/knowledge")
    public R addKnowledge(@RequestBody Map<String, Object> params) {
        try {
            RiskKnowledge entity = new RiskKnowledge();
            entity.setEnterpriseId((String) params.get("enterpriseId"));
            entity.setEnterpriseName((String) params.get("enterpriseName"));
            entity.setTitle((String) params.get("title"));
            entity.setKnowledgeType((String) params.get("knowledgeType"));
            entity.setRiskCategory((String) params.get("riskCategory"));
            entity.setAuthor((String) params.get("author"));
            entity.setKeyword((String) params.get("keyword"));
            entity.setSummary((String) params.get("summary"));
            entity.setContent((String) params.get("content"));
            entity.setStatus((String) params.get("status"));
            entity.setCreateTime(LocalDateTime.now());
            entity.setViewCount(0);
            entity.setLikeCount(0);
            entity.setDeleted(false);
            riskKnowledgeMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增知识失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新")
    @PutMapping("/knowledge")
    public R updateKnowledge(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("riskKnowledgeId");
            if (StringUtils.isEmpty(id)) return R.fail("知识ID不能为空");
            RiskKnowledge entity = riskKnowledgeMapper.selectById(id);
            if (entity == null) return R.fail("知识不存在");
            if (params.containsKey("title")) entity.setTitle((String) params.get("title"));
            if (params.containsKey("knowledgeType")) entity.setKnowledgeType((String) params.get("knowledgeType"));
            if (params.containsKey("riskCategory")) entity.setRiskCategory((String) params.get("riskCategory"));
            if (params.containsKey("author")) entity.setAuthor((String) params.get("author"));
            if (params.containsKey("keyword")) entity.setKeyword((String) params.get("keyword"));
            if (params.containsKey("summary")) entity.setSummary((String) params.get("summary"));
            if (params.containsKey("content")) entity.setContent((String) params.get("content"));
            if (params.containsKey("status")) entity.setStatus((String) params.get("status"));
            entity.setUpdateTime(LocalDateTime.now());
            riskKnowledgeMapper.updateById(entity);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新知识失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "搜索")
    @PostMapping("/knowledge/search")
    public R searchKnowledge(@RequestBody Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String knowledgeType = (String) params.get("knowledgeType");
        String riskCategory = (String) params.get("riskCategory");
        Integer pageNumber = params.get("pageNumber") != null ? (Integer) params.get("pageNumber") : 1;
        Integer pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;

        QueryWrapper<RiskKnowledge> wrapper = new QueryWrapper<RiskKnowledge>()
            .like(StringUtils.isNotEmpty(keyword), "TITLE", keyword)
            .or().like(StringUtils.isNotEmpty(keyword), "KEYWORD", keyword)
            .or().like(StringUtils.isNotEmpty(keyword), "SUMMARY", keyword)
            .eq(StringUtils.isNotEmpty(knowledgeType), "KNOWLEDGE_TYPE", knowledgeType)
            .eq(StringUtils.isNotEmpty(riskCategory), "RISK_CATEGORY", riskCategory)
            .orderByDesc("CREATE_TIME");
        IPage<RiskKnowledge> page = riskKnowledgeMapper.selectPage(new Page<>(pageNumber, pageSize), wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        result.put("current", page.getCurrent());
        result.put("size", page.getSize());
        return R.success(result);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/knowledge/{id}")
    public R deleteKnowledge(@PathVariable String id) {
        riskKnowledgeMapper.deleteById(id);
        return R.success("删除成功");
    }

    // ==================== 统计辅助接口 ====================

    @Operation(summary = "查询统计数据")
    @PostMapping("/statistics/level")
    public R getRiskLevelStatistics(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> result = new HashMap<>();
        result.put("high", riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("RISK_LEVEL", "high")));
        result.put("medium", riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("RISK_LEVEL", "medium")));
        result.put("low", riskIdentificationMapper.selectCount(
            new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("RISK_LEVEL", "low")));
        return R.success(result);
    }

    @Operation(summary = "查询统计数据")
    @PostMapping("/statistics/type")
    public R getRiskTypeStatistics(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> result = new HashMap<>();
        for (String type : Arrays.asList("financial", "operational", "market", "technical", "compliance")) {
            result.put(type, riskIdentificationMapper.selectCount(
                new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("RISK_TYPE", type)));
        }
        return R.success(result);
    }

    @Operation(summary = "查询统计数据")
    @PostMapping("/statistics/status")
    public R getRiskStatusStatistics(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> result = new HashMap<>();
        for (String status : Arrays.asList("identified", "pending", "confirmed", "ignored")) {
            result.put(status, riskIdentificationMapper.selectCount(
                new QueryWrapper<RiskIdentification>().eq("ENTERPRISE_ID", enterpriseId).eq("STATUS", status)));
        }
        return R.success(result);
    }

    @Operation(summary = "查询统计数据")
    @PostMapping("/statistics/control")
    public R getControlStatistics(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> result = new HashMap<>();
        result.put("total", riskControlMeasureMapper.selectCount(
            new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId)));
        result.put("effective", riskControlMeasureMapper.selectCount(
            new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId).eq("EFFECTIVENESS_LEVEL", "effective")));
        return R.success(result);
    }

    @Operation(summary = "更新")
    @PutMapping("/matrix")
    public R updateRiskMatrix(@RequestBody Map<String, Object> params) {
        return R.success("更新成功");
    }

    @Operation(summary = "分析")
    @PostMapping("/matrix/analyze")
    public R analyzeRiskMatrix(@RequestBody Map<String, Object> params) {
        return getRiskMatrix(params);
    }

    // ==================== 通用分页查询 ====================

    private <T> R queryList(Object mapper, Class<T> clazz, Map<String, Object> params, String... filterFields) {
        try {
            String enterpriseId = (String) params.get("enterpriseId");
            Integer pageNumber = params.get("pageNumber") != null ?
                (params.get("pageNumber") instanceof Integer ? (Integer) params.get("pageNumber") : Integer.valueOf(params.get("pageNumber").toString())) : 1;
            Integer pageSize = params.get("pageSize") != null ?
                (params.get("pageSize") instanceof Integer ? (Integer) params.get("pageSize") : Integer.valueOf(params.get("pageSize").toString())) : 10;

            QueryWrapper<T> wrapper = new QueryWrapper<T>()
                .eq(StringUtils.isNotEmpty(enterpriseId), "ENTERPRISE_ID", enterpriseId);

            // 支持筛选字段
            for (int i = 0; i < filterFields.length - 1; i++) {
                String field = filterFields[i];
                String value = (String) params.get(field);
                // 前端传的key是小驼峰，将UPPER_SNAKE_CASE转为camelCase查找
                if (StringUtils.isEmpty(value)) {
                    String[] parts = field.toLowerCase().split("_");
                    StringBuilder sb = new StringBuilder(parts[0]);
                    for (int j = 1; j < parts.length; j++) {
                        sb.append(Character.toUpperCase(parts[j].charAt(0)));
                        if (parts[j].length() > 1) sb.append(parts[j].substring(1));
                    }
                    String camelKey = sb.toString();
                    value = (String) params.get(camelKey);
                }
                wrapper.eq(StringUtils.isNotEmpty(value), field, value);
            }

            // 最后一个字段做名称模糊查询
            String nameField = filterFields[filterFields.length - 1];
            // 将nameField转为camelCase尝试从前端获取对应的key
            String[] nameParts = nameField.toLowerCase().split("_");
            StringBuilder nameSb = new StringBuilder(nameParts[0]);
            for (int j = 1; j < nameParts.length; j++) {
                nameSb.append(Character.toUpperCase(nameParts[j].charAt(0)));
                if (nameParts[j].length() > 1) nameSb.append(nameParts[j].substring(1));
            }
            String nameCamelKey = nameSb.toString();
            Object nameValue = params.get(nameCamelKey);
            if (nameValue == null) nameValue = params.get(nameField);
            if (nameValue != null && !nameValue.toString().isEmpty()) {
                wrapper.like(nameField, "%" + nameValue + "%");
            }

            wrapper.orderByDesc("CREATE_TIME");

            @SuppressWarnings("unchecked")
            com.baomidou.mybatisplus.core.mapper.BaseMapper<T> baseMapper =
                (com.baomidou.mybatisplus.core.mapper.BaseMapper<T>) mapper;
            IPage<T> page = baseMapper.selectPage(new Page<>(pageNumber, pageSize), wrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("records", page.getRecords());
            result.put("total", page.getTotal());
            result.put("current", page.getCurrent());
            result.put("size", page.getSize());
            return R.success(result);
        } catch (Exception e) {
            log.error("查询列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private int getProbIndex(BigDecimal prob) {
        if (prob == null) return 1;
        double p = prob.doubleValue();
        if (p < 33) return 0;
        if (p < 66) return 1;
        return 2;
    }

    private int getImpactIndex(String impact) {
        if ("low".equals(impact)) return 0;
        if ("medium".equals(impact)) return 1;
        return 2;
    }

    private int getLevelIndex(String level) {
        if ("low".equals(level) || "VERY_LOW".equals(level)) return 0;
        if ("medium".equals(level)) return 1;
        return 2;
    }

    private String getTypeText(String type) {
        if ("financial".equals(type)) return "财务风险";
        if ("operational".equals(type)) return "运营风险";
        if ("market".equals(type)) return "市场风险";
        if ("technical".equals(type)) return "技术风险";
        if ("compliance".equals(type)) return "合规风险";
        return type != null ? type : "其他";
    }

    private String getReportTypeText(String type) {
        if ("assessment".equals(type)) return "评估";
        if ("monitoring".equals(type)) return "监控";
        if ("incident".equals(type)) return "事件";
        return "综合";
    }

    // ==================== 控制措施分析 ====================

    @Operation(summary = "分析")
    @PostMapping("/control/analyze")
    public R analyzeControl(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        Long total = riskControlMeasureMapper.selectCount(
            new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId));
        Long effective = riskControlMeasureMapper.selectCount(
            new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId).eq("EFFECTIVENESS_LEVEL", "effective"));
        Long improvement = riskControlMeasureMapper.selectCount(
            new QueryWrapper<RiskControlMeasure>().eq("ENTERPRISE_ID", enterpriseId).in("EFFECTIVENESS_LEVEL", "ineffective", "partially_effective"));
        overview.put("totalControls", total);
        overview.put("effectiveControls", effective);
        overview.put("improvementNeeded", improvement);
        overview.put("effectivenessRate", total > 0 ? Math.round(effective * 1000.0 / total) / 10.0 : 0);
        return R.success(overview);
    }

    // ==================== 风险监控分析 ====================

    @Operation(summary = "分析")
    @PostMapping("/monitoring/analyze")
    public R analyzeMonitoring(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        Long totalIndicators = riskMonitoringMapper.selectCount(
            new QueryWrapper<RiskMonitoring>().eq("ENTERPRISE_ID", enterpriseId));
        Long warningCount = riskMonitoringMapper.selectCount(
            new QueryWrapper<RiskMonitoring>().eq("ENTERPRISE_ID", enterpriseId).eq("IS_ALERT_TRIGGERED", 1));
        Long highRiskCount = riskMonitoringMapper.selectCount(
            new QueryWrapper<RiskMonitoring>().eq("ENTERPRISE_ID", enterpriseId).in("CURRENT_RISK_LEVEL", "high", "very_high"));
        Long normalCount = riskMonitoringMapper.selectCount(
            new QueryWrapper<RiskMonitoring>().eq("ENTERPRISE_ID", enterpriseId).eq("MONITORING_STATUS", "normal"));
        overview.put("totalIndicators", totalIndicators);
        overview.put("warningCount", warningCount);
        overview.put("highRiskCount", highRiskCount);
        overview.put("coverageRate", totalIndicators > 0 ? Math.round(normalCount * 1000.0 / totalIndicators) / 10.0 : 0);
        return R.success(overview);
    }

    // ==================== 风险事件分析 ====================

    @Operation(summary = "分析")
    @PostMapping("/incident/analyze")
    public R analyzeIncident(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        Long totalIncidents = riskIncidentMapper.selectCount(
            new QueryWrapper<RiskIncident>().eq("ENTERPRISE_ID", enterpriseId));
        Long severeIncidents = riskIncidentMapper.selectCount(
            new QueryWrapper<RiskIncident>().eq("ENTERPRISE_ID", enterpriseId).in("RISK_LEVEL", "high", "critical"));
        Long processingIncidents = riskIncidentMapper.selectCount(
            new QueryWrapper<RiskIncident>().eq("ENTERPRISE_ID", enterpriseId).in("HANDLING_STATUS", "handling", "pending"));
        Long completedIncidents = riskIncidentMapper.selectCount(
            new QueryWrapper<RiskIncident>().eq("ENTERPRISE_ID", enterpriseId).eq("HANDLING_STATUS", "completed"));
        overview.put("totalIncidents", totalIncidents);
        overview.put("severeIncidents", severeIncidents);
        overview.put("processingIncidents", processingIncidents);
        overview.put("completionRate", totalIncidents > 0 ? Math.round(completedIncidents * 1000.0 / totalIncidents) / 10.0 : 0);
        return R.success(overview);
    }

    // ==================== 风险报告分析 ====================

    @Operation(summary = "分析")
    @PostMapping("/report/analyze")
    public R analyzeReport(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        Long totalReports = riskReportMapper.selectCount(
            new QueryWrapper<RiskReport>().eq("ENTERPRISE_ID", enterpriseId));
        Long pendingReports = riskReportMapper.selectCount(
            new QueryWrapper<RiskReport>().eq("ENTERPRISE_ID", enterpriseId).eq("STATUS", "pending"));
        Long publishedReports = riskReportMapper.selectCount(
            new QueryWrapper<RiskReport>().eq("ENTERPRISE_ID", enterpriseId).eq("STATUS", "published"));
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        Long monthlyReports = riskReportMapper.selectCount(
            new QueryWrapper<RiskReport>().eq("ENTERPRISE_ID", enterpriseId).ge("CREATE_TIME", monthStart));
        overview.put("totalReports", totalReports);
        overview.put("monthlyReports", monthlyReports);
        overview.put("pendingReports", pendingReports);
        overview.put("publishedReports", publishedReports);
        return R.success(overview);
    }

    // ==================== 风险知识库分析 ====================

    @Operation(summary = "分析")
    @PostMapping("/knowledge/analyze")
    public R analyzeKnowledge(@RequestBody Map<String, Object> params) {
        String enterpriseId = (String) params.get("enterpriseId");
        Map<String, Object> overview = new HashMap<>();
        Long totalKnowledge = riskKnowledgeMapper.selectCount(
            new QueryWrapper<RiskKnowledge>().eq("ENTERPRISE_ID", enterpriseId));
        Long riskCases = riskKnowledgeMapper.selectCount(
            new QueryWrapper<RiskKnowledge>().eq("ENTERPRISE_ID", enterpriseId).eq("KNOWLEDGE_TYPE", "case"));
        Long bestPractices = riskKnowledgeMapper.selectCount(
            new QueryWrapper<RiskKnowledge>().eq("ENTERPRISE_ID", enterpriseId).eq("KNOWLEDGE_TYPE", "practice"));
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        Long monthlyNew = riskKnowledgeMapper.selectCount(
            new QueryWrapper<RiskKnowledge>().eq("ENTERPRISE_ID", enterpriseId).ge("CREATE_TIME", monthStart));
        overview.put("totalKnowledge", totalKnowledge);
        overview.put("riskCases", riskCases);
        overview.put("bestPractices", bestPractices);
        overview.put("monthlyNew", monthlyNew);
        return R.success(overview);
    }

    private void knowledgeUpdate(RiskKnowledge entity) {
        // dummy method to satisfy compiler
    }
}
