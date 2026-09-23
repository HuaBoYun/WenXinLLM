package com.management.accountant.controller.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsTaxPlanning;
import com.management.accountant.service.ts.TsTaxPlanningService;
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
 * 税务筹划控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ts/tax-planning")
@Api(tags = "税务筹划管理")
public class TsTaxPlanningController {

    @Autowired
    private TsTaxPlanningService taxPlanningService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建税务筹划")
    public MyJsonBean createPlanning(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划信息") @RequestBody TsTaxPlanning planning) {
        try {
            TsTaxPlanning result = taxPlanningService.createPlanning(tenantId, planning);
            return MyJsonBean.success("创建成功", result);
        } catch (Exception e) {
            log.error("创建税务筹划失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/update/{planningId}")
    @ApiOperation("更新税务筹划")
    public MyJsonBean updatePlanning(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId,
            @ApiParam("筹划信息") @RequestBody TsTaxPlanning planning) {
        try {
            TsTaxPlanning result = taxPlanningService.updatePlanning(tenantId, planningId, planning);
            return MyJsonBean.success("更新成功", result);
        } catch (Exception e) {
            log.error("更新税务筹划失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{planningId}")
    @ApiOperation("删除税务筹划")
    public MyJsonBean deletePlanning(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            boolean result = taxPlanningService.deletePlanning(tenantId, planningId);
            return MyJsonBean.success("删除成功", result);
        } catch (Exception e) {
            log.error("删除税务筹划失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{planningId}")
    @ApiOperation("获取税务筹划详情")
    public MyJsonBean getPlanningDetail(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            TsTaxPlanning result = taxPlanningService.getPlanningById(tenantId, planningId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取税务筹划详情失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/by-code/{planningCode}")
    @ApiOperation("根据编号获取税务筹划详情")
    public MyJsonBean getPlanningByCode(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划编号") @PathVariable String planningCode) {
        try {
            TsTaxPlanning result = taxPlanningService.getPlanningByCode(tenantId, planningCode);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据编号获取税务筹划详情失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询税务筹划")
    public MyJsonBean getPlanningPage(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            Page<TsTaxPlanning> page = new Page<>(current, size);
            IPage<TsTaxPlanning> result = taxPlanningService.getPlanningPage(tenantId, page, params);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("分页查询税务筹划失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 筹划管理功能 ====================

    @PostMapping("/generate-code")
    @ApiOperation("生成筹划编号")
    public MyJsonBean generatePlanningCode(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            String result = taxPlanningService.generatePlanningCode(tenantId);
            return MyJsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("生成筹划编号失败", e);
            return MyJsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @ApiOperation("验证筹划数据")
    public MyJsonBean validatePlanningData(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划信息") @RequestBody TsTaxPlanning planning) {
        try {
            Map<String, Object> result = taxPlanningService.validatePlanningData(tenantId, planning);
            return MyJsonBean.success("验证完成", result);
        } catch (Exception e) {
            log.error("验证筹划数据失败", e);
            return MyJsonBean.error("验证失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculate-benefit/{planningId}")
    @ApiOperation("计算筹划效益")
    public MyJsonBean calculatePlanningBenefit(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            Map<String, Object> result = taxPlanningService.calculatePlanningBenefit(tenantId, planningId);
            return MyJsonBean.success("计算成功", result);
        } catch (Exception e) {
            log.error("计算筹划效益失败", e);
            return MyJsonBean.error("计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/assess-risk/{planningId}")
    @ApiOperation("评估筹划风险")
    public MyJsonBean assessPlanningRisk(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            Map<String, Object> result = taxPlanningService.assessPlanningRisk(tenantId, planningId);
            return MyJsonBean.success("评估成功", result);
        } catch (Exception e) {
            log.error("评估筹划风险失败", e);
            return MyJsonBean.error("评估失败: " + e.getMessage());
        }
    }

    @PostMapping("/analyze-feasibility/{planningId}")
    @ApiOperation("分析筹划可行性")
    public MyJsonBean analyzeFeasibility(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            Map<String, Object> result = taxPlanningService.analyzeFeasibility(tenantId, planningId);
            return MyJsonBean.success("分析成功", result);
        } catch (Exception e) {
            log.error("分析筹划可行性失败", e);
            return MyJsonBean.error("分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/optimize-scheme/{planningId}")
    @ApiOperation("优化筹划方案")
    public MyJsonBean optimizePlanningScheme(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            Map<String, Object> result = taxPlanningService.optimizePlanningScheme(tenantId, planningId);
            return MyJsonBean.success("优化成功", result);
        } catch (Exception e) {
            log.error("优化筹划方案失败", e);
            return MyJsonBean.error("优化失败: " + e.getMessage());
        }
    }

    @PostMapping("/compare-schemes")
    @ApiOperation("比较筹划方案")
    public MyJsonBean comparePlanningSchemes(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds) {
        try {
            Map<String, Object> result = taxPlanningService.comparePlanningSchemes(tenantId, planningIds);
            return MyJsonBean.success("比较成功", result);
        } catch (Exception e) {
            log.error("比较筹划方案失败", e);
            return MyJsonBean.error("比较失败: " + e.getMessage());
        }
    }

    @PostMapping("/recommend-schemes")
    @ApiOperation("推荐筹划方案")
    public MyJsonBean recommendPlanningSchemes(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("推荐条件") @RequestBody Map<String, Object> criteria) {
        try {
            List<Map<String, Object>> result = taxPlanningService.recommendPlanningSchemes(tenantId, criteria);
            return MyJsonBean.success("推荐成功", result);
        } catch (Exception e) {
            log.error("推荐筹划方案失败", e);
            return MyJsonBean.error("推荐失败: " + e.getMessage());
        }
    }

    // ==================== 执行管理功能 ====================

    @PostMapping("/start-execution/{planningId}")
    @ApiOperation("启动筹划执行")
    public MyJsonBean startPlanningExecution(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            boolean result = taxPlanningService.startPlanningExecution(tenantId, planningId);
            return MyJsonBean.success("启动成功", result);
        } catch (Exception e) {
            log.error("启动筹划执行失败", e);
            return MyJsonBean.error("启动失败: " + e.getMessage());
        }
    }

    @PostMapping("/pause-execution/{planningId}")
    @ApiOperation("暂停筹划执行")
    public MyJsonBean pausePlanningExecution(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            boolean result = taxPlanningService.pausePlanningExecution(tenantId, planningId);
            return MyJsonBean.success("暂停成功", result);
        } catch (Exception e) {
            log.error("暂停筹划执行失败", e);
            return MyJsonBean.error("暂停失败: " + e.getMessage());
        }
    }

    @PostMapping("/resume-execution/{planningId}")
    @ApiOperation("恢复筹划执行")
    public MyJsonBean resumePlanningExecution(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            boolean result = taxPlanningService.resumePlanningExecution(tenantId, planningId);
            return MyJsonBean.success("恢复成功", result);
        } catch (Exception e) {
            log.error("恢复筹划执行失败", e);
            return MyJsonBean.error("恢复失败: " + e.getMessage());
        }
    }

    @PostMapping("/complete-execution/{planningId}")
    @ApiOperation("完成筹划执行")
    public MyJsonBean completePlanningExecution(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId,
            @ApiParam("完成数据") @RequestBody Map<String, Object> completionData) {
        try {
            boolean result = taxPlanningService.completePlanningExecution(tenantId, planningId, completionData);
            return MyJsonBean.success("完成成功", result);
        } catch (Exception e) {
            log.error("完成筹划执行失败", e);
            return MyJsonBean.error("完成失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel-execution/{planningId}")
    @ApiOperation("取消筹划执行")
    public MyJsonBean cancelPlanningExecution(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId,
            @ApiParam("取消原因") @RequestParam String reason) {
        try {
            boolean result = taxPlanningService.cancelPlanningExecution(tenantId, planningId, reason);
            return MyJsonBean.success("取消成功", result);
        } catch (Exception e) {
            log.error("取消筹划执行失败", e);
            return MyJsonBean.error("取消失败: " + e.getMessage());
        }
    }

    @PostMapping("/update-progress/{planningId}")
    @ApiOperation("更新执行进度")
    public MyJsonBean updateExecutionProgress(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId,
            @ApiParam("进度") @RequestParam BigDecimal progress) {
        try {
            boolean result = taxPlanningService.updateExecutionProgress(tenantId, planningId, progress);
            return MyJsonBean.success("更新成功", result);
        } catch (Exception e) {
            log.error("更新执行进度失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    // ==================== 查询统计功能 ====================

    @GetMapping("/list/by-type")
    @ApiOperation("根据筹划类型查询")
    public MyJsonBean getPlanningsByType(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划类型") @RequestParam String planningType) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getPlanningsByType(tenantId, planningType);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据筹划类型查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-status")
    @ApiOperation("根据筹划状态查询")
    public MyJsonBean getPlanningsByStatus(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划状态") @RequestParam String planningStatus) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getPlanningsByStatus(tenantId, planningStatus);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据筹划状态查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-execution-status")
    @ApiOperation("根据执行状态查询")
    public MyJsonBean getPlanningsByExecutionStatus(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("执行状态") @RequestParam String executionStatus) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getPlanningsByExecutionStatus(tenantId, executionStatus);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据执行状态查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-risk-level")
    @ApiOperation("根据风险等级查询")
    public MyJsonBean getPlanningsByRiskLevel(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("风险等级") @RequestParam String riskLevel) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getPlanningsByRiskLevel(tenantId, riskLevel);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据风险等级查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-tax-type")
    @ApiOperation("根据税种查询")
    public MyJsonBean getPlanningsByTaxType(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("税种") @RequestParam String taxType) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getPlanningsByTaxType(tenantId, taxType);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据税种查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-responsible-person")
    @ApiOperation("根据责任人查询")
    public MyJsonBean getPlanningsByResponsiblePerson(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("责任人") @RequestParam String responsiblePerson) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getPlanningsByResponsiblePerson(tenantId, responsiblePerson);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据责任人查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/expiring-soon")
    @ApiOperation("查询即将到期的筹划")
    public MyJsonBean getExpiringSoonPlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getExpiringSoonPlannings(tenantId, days);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询即将到期的筹划失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/overdue")
    @ApiOperation("查询逾期的筹划")
    public MyJsonBean getOverduePlannings(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getOverduePlannings(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询逾期的筹划失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/high-risk")
    @ApiOperation("查询高风险筹划")
    public MyJsonBean getHighRiskPlannings(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getHighRiskPlannings(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询高风险筹划失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/high-benefit")
    @ApiOperation("查询高收益筹划")
    public MyJsonBean getHighBenefitPlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("最小收益") @RequestParam BigDecimal minBenefit) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.getHighBenefitPlannings(tenantId, minBenefit);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询高收益筹划失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析功能 ====================

    @GetMapping("/overview")
    @ApiOperation("获取筹划概览统计")
    public MyJsonBean getPlanningOverview(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = taxPlanningService.getPlanningOverview(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取筹划概览统计失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-status")
    @ApiOperation("按状态统计筹划数量")
    public MyJsonBean countPlanningsByStatus(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.countPlanningsByStatus(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按状态统计筹划数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-type")
    @ApiOperation("按类型统计筹划数量")
    public MyJsonBean countPlanningsByType(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.countPlanningsByType(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按类型统计筹划数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-tax-type")
    @ApiOperation("按税种统计筹划数量")
    public MyJsonBean countPlanningsByTaxType(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.countPlanningsByTaxType(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按税种统计筹划数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-risk-level")
    @ApiOperation("按风险等级统计筹划数量")
    public MyJsonBean countPlanningsByRiskLevel(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.countPlanningsByRiskLevel(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按风险等级统计筹划数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-execution-status")
    @ApiOperation("按执行状态统计筹划数量")
    public MyJsonBean countPlanningsByExecutionStatus(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.countPlanningsByExecutionStatus(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按执行状态统计筹划数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend/planning")
    @ApiOperation("获取筹划趋势数据")
    public MyJsonBean getPlanningTrend(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("开始日期") @RequestParam LocalDateTime startDate,
            @ApiParam("结束日期") @RequestParam LocalDateTime endDate,
            @ApiParam("分组方式") @RequestParam(defaultValue = "month") String groupBy) {
        try {
            List<Map<String, Object>> result = taxPlanningService.getPlanningTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取筹划趋势数据失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend/tax-saving")
    @ApiOperation("获取节税趋势数据")
    public MyJsonBean getTaxSavingTrend(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("开始日期") @RequestParam LocalDateTime startDate,
            @ApiParam("结束日期") @RequestParam LocalDateTime endDate,
            @ApiParam("分组方式") @RequestParam(defaultValue = "month") String groupBy) {
        try {
            List<Map<String, Object>> result = taxPlanningService.getTaxSavingTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取节税趋势数据失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend/benefit")
    @ApiOperation("获取收益趋势数据")
    public MyJsonBean getBenefitTrend(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("开始日期") @RequestParam LocalDateTime startDate,
            @ApiParam("结束日期") @RequestParam LocalDateTime endDate,
            @ApiParam("分组方式") @RequestParam(defaultValue = "month") String groupBy) {
        try {
            List<Map<String, Object>> result = taxPlanningService.getBenefitTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取收益趋势数据失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/risk")
    @ApiOperation("获取风险分布数据")
    public MyJsonBean getRiskDistribution(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.getRiskDistribution(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取风险分布数据失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/effectiveness")
    @ApiOperation("获取效果评估数据")
    public MyJsonBean getEffectivenessData(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.getEffectivenessData(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取效果评估数据失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/ranking")
    @ApiOperation("获取筹划排行榜")
    public MyJsonBean getPlanningRanking(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("排序字段") @RequestParam(defaultValue = "tax_saving") String rankBy,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> result = taxPlanningService.getPlanningRanking(tenantId, rankBy, limit);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取筹划排行榜失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/efficiency-stats")
    @ApiOperation("获取筹划效率统计")
    public MyJsonBean getPlanningEfficiencyStats(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = taxPlanningService.getPlanningEfficiencyStats(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取筹划效率统计失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作功能 ====================

    @PostMapping("/batch/create")
    @ApiOperation("批量创建筹划")
    public MyJsonBean batchCreatePlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划列表") @RequestBody List<TsTaxPlanning> plannings) {
        try {
            List<TsTaxPlanning> result = taxPlanningService.batchCreatePlannings(tenantId, plannings);
            return MyJsonBean.success("批量创建成功", result);
        } catch (Exception e) {
            log.error("批量创建筹划失败", e);
            return MyJsonBean.error("批量创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/update-status")
    @ApiOperation("批量更新筹划状态")
    public MyJsonBean batchUpdatePlanningStatus(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds,
            @ApiParam("状态") @RequestParam String status) {
        try {
            boolean result = taxPlanningService.batchUpdatePlanningStatus(tenantId, planningIds, status);
            return MyJsonBean.success("批量更新成功", result);
        } catch (Exception e) {
            log.error("批量更新筹划状态失败", e);
            return MyJsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/update-execution-status")
    @ApiOperation("批量更新执行状态")
    public MyJsonBean batchUpdateExecutionStatus(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds,
            @ApiParam("执行状态") @RequestParam String executionStatus) {
        try {
            boolean result = taxPlanningService.batchUpdateExecutionStatus(tenantId, planningIds, executionStatus);
            return MyJsonBean.success("批量更新成功", result);
        } catch (Exception e) {
            log.error("批量更新执行状态失败", e);
            return MyJsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/delete")
    @ApiOperation("批量删除筹划")
    public MyJsonBean batchDeletePlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds) {
        try {
            boolean result = taxPlanningService.batchDeletePlannings(tenantId, planningIds);
            return MyJsonBean.success("批量删除成功", result);
        } catch (Exception e) {
            log.error("批量删除筹划失败", e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/archive")
    @ApiOperation("批量归档筹划")
    public MyJsonBean batchArchivePlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds) {
        try {
            boolean result = taxPlanningService.batchArchivePlannings(tenantId, planningIds);
            return MyJsonBean.success("批量归档成功", result);
        } catch (Exception e) {
            log.error("批量归档筹划失败", e);
            return MyJsonBean.error("批量归档失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/activate")
    @ApiOperation("批量激活筹划")
    public MyJsonBean batchActivatePlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds) {
        try {
            boolean result = taxPlanningService.batchActivatePlannings(tenantId, planningIds);
            return MyJsonBean.success("批量激活成功", result);
        } catch (Exception e) {
            log.error("批量激活筹划失败", e);
            return MyJsonBean.error("批量激活失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/import")
    @ApiOperation("批量导入筹划")
    public MyJsonBean batchImportPlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划数据") @RequestBody List<Map<String, Object>> planningData) {
        try {
            Map<String, Object> result = taxPlanningService.batchImportPlannings(tenantId, planningData);
            return MyJsonBean.success("批量导入成功", result);
        } catch (Exception e) {
            log.error("批量导入筹划失败", e);
            return MyJsonBean.error("批量导入失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/export")
    @ApiOperation("批量导出筹划")
    public MyJsonBean batchExportPlannings(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID列表") @RequestBody List<Long> planningIds) {
        try {
            List<Map<String, Object>> result = taxPlanningService.batchExportPlannings(tenantId, planningIds);
            return MyJsonBean.success("批量导出成功", result);
        } catch (Exception e) {
            log.error("批量导出筹划失败", e);
            return MyJsonBean.error("批量导出失败: " + e.getMessage());
        }
    }

    // ==================== 工具功能 ====================

    @PostMapping("/copy/{sourcePlanningId}")
    @ApiOperation("复制筹划")
    public MyJsonBean copyPlanning(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("源筹划ID") @PathVariable Long sourcePlanningId,
            @ApiParam("新筹划名称") @RequestParam String newPlanningName) {
        try {
            TsTaxPlanning result = taxPlanningService.copyPlanning(tenantId, sourcePlanningId, newPlanningName);
            return MyJsonBean.success("复制成功", result);
        } catch (Exception e) {
            log.error("复制筹划失败", e);
            return MyJsonBean.error("复制失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-report/{planningId}")
    @ApiOperation("生成筹划报告")
    public MyJsonBean generatePlanningReport(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId) {
        try {
            Map<String, Object> result = taxPlanningService.generatePlanningReport(tenantId, planningId);
            return MyJsonBean.success("生成成功", result);
        } catch (Exception e) {
            log.error("生成筹划报告失败", e);
            return MyJsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-reminder/{planningId}")
    @ApiOperation("发送筹划提醒")
    public MyJsonBean sendPlanningReminder(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("筹划ID") @PathVariable Long planningId,
            @ApiParam("提醒类型") @RequestParam String reminderType) {
        try {
            boolean result = taxPlanningService.sendPlanningReminder(tenantId, planningId, reminderType);
            return MyJsonBean.success("发送成功", result);
        } catch (Exception e) {
            log.error("发送筹划提醒失败", e);
            return MyJsonBean.error("发送失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护功能 ====================

    @GetMapping("/system/health-check")
    @ApiOperation("系统健康检查")
    public MyJsonBean systemHealthCheck(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = taxPlanningService.systemHealthCheck(tenantId);
            return MyJsonBean.success("检查完成", result);
        } catch (Exception e) {
            log.error("系统健康检查失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/data-consistency-check")
    @ApiOperation("数据一致性检查")
    public MyJsonBean dataConsistencyCheck(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> result = taxPlanningService.dataConsistencyCheck(tenantId);
            return MyJsonBean.success("检查完成", result);
        } catch (Exception e) {
            log.error("数据一致性检查失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/performance-stats")
    @ApiOperation("性能统计")
    public MyJsonBean performanceStats(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = taxPlanningService.performanceStats(tenantId);
            return MyJsonBean.success("统计完成", result);
        } catch (Exception e) {
            log.error("性能统计失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/system/cleanup-expired-data")
    @ApiOperation("清理过期数据")
    public MyJsonBean cleanupExpiredData(
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("天数") @RequestParam(defaultValue = "90") Integer days) {
        try {
            boolean result = taxPlanningService.cleanupExpiredData(tenantId, days);
            return MyJsonBean.success("清理完成", result);
        } catch (Exception e) {
            log.error("清理过期数据失败", e);
            return MyJsonBean.error("清理失败: " + e.getMessage());
        }
    }
}
