package com.management.accountant.controller.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ts.TsTaxCompliance;
import com.management.accountant.service.ts.TsTaxComplianceService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务合规检查控制器
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ts/compliance")
@Api(tags = "税务合规检查管理")
public class TsTaxComplianceController {

    @Autowired
    private TsTaxComplianceService complianceService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建合规检查")
    public MyJsonBean createCompliance(@RequestHeader("tenantId") Long tenantId,
                                      @RequestBody TsTaxCompliance compliance) {
        try {
            TsTaxCompliance result = complianceService.createCompliance(tenantId, compliance);
            return MyJsonBean.success("合规检查创建成功", result);
        } catch (Exception e) {
            log.error("创建合规检查失败", e);
            return MyJsonBean.error("创建合规检查失败: " + e.getMessage());
        }
    }

    @PutMapping("/update/{complianceId}")
    @ApiOperation("更新合规检查")
    public MyJsonBean updateCompliance(@RequestHeader("tenantId") Long tenantId,
                                      @PathVariable Long complianceId,
                                      @RequestBody TsTaxCompliance compliance) {
        try {
            TsTaxCompliance result = complianceService.updateCompliance(tenantId, complianceId, compliance);
            return MyJsonBean.success("合规检查更新成功", result);
        } catch (Exception e) {
            log.error("更新合规检查失败", e);
            return MyJsonBean.error("更新合规检查失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{complianceId}")
    @ApiOperation("删除合规检查")
    public MyJsonBean deleteCompliance(@RequestHeader("tenantId") Long tenantId,
                                      @PathVariable Long complianceId) {
        try {
            boolean result = complianceService.deleteCompliance(tenantId, complianceId);
            return MyJsonBean.success("合规检查删除成功", result);
        } catch (Exception e) {
            log.error("删除合规检查失败", e);
            return MyJsonBean.error("删除合规检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{complianceId}")
    @ApiOperation("获取合规检查详情")
    public MyJsonBean getComplianceDetail(@RequestHeader("tenantId") Long tenantId,
                                         @PathVariable Long complianceId) {
        try {
            TsTaxCompliance result = complianceService.getComplianceDetail(tenantId, complianceId);
            return MyJsonBean.success("获取合规检查详情成功", result);
        } catch (Exception e) {
            log.error("获取合规检查详情失败", e);
            return MyJsonBean.error("获取合规检查详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/code/{complianceCode}")
    @ApiOperation("根据编号获取合规检查详情")
    public MyJsonBean getComplianceByCode(@RequestHeader("tenantId") Long tenantId,
                                         @PathVariable String complianceCode) {
        try {
            TsTaxCompliance result = complianceService.getComplianceByCode(tenantId, complianceCode);
            return MyJsonBean.success("获取合规检查详情成功", result);
        } catch (Exception e) {
            log.error("获取合规检查详情失败", e);
            return MyJsonBean.error("获取合规检查详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询合规检查")
    public MyJsonBean getCompliancePage(@RequestHeader("tenantId") Long tenantId,
                                       @RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam Map<String, Object> params) {
        try {
            IPage<TsTaxCompliance> result = complianceService.getCompliancePage(tenantId, current, size, params);
            return MyJsonBean.success("分页查询合规检查成功", result);
        } catch (Exception e) {
            log.error("分页查询合规检查失败", e);
            return MyJsonBean.error("分页查询合规检查失败: " + e.getMessage());
        }
    }

    // ==================== 合规检查管理功能 ====================

    @PostMapping("/generate-code")
    @ApiOperation("生成合规检查编号")
    public MyJsonBean generateComplianceCode(@RequestHeader("tenantId") Long tenantId) {
        try {
            String result = complianceService.generateComplianceCode(tenantId);
            return MyJsonBean.success("生成合规检查编号成功", result);
        } catch (Exception e) {
            log.error("生成合规检查编号失败", e);
            return MyJsonBean.error("生成合规检查编号失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @ApiOperation("验证合规检查数据")
    public MyJsonBean validateComplianceData(@RequestHeader("tenantId") Long tenantId,
                                            @RequestBody TsTaxCompliance compliance) {
        try {
            Map<String, Object> result = complianceService.validateComplianceData(tenantId, compliance);
            return MyJsonBean.success("验证合规检查数据成功", result);
        } catch (Exception e) {
            log.error("验证合规检查数据失败", e);
            return MyJsonBean.error("验证合规检查数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/start/{complianceId}")
    @ApiOperation("启动合规检查")
    public MyJsonBean startComplianceCheck(@RequestHeader("tenantId") Long tenantId,
                                          @PathVariable Long complianceId) {
        try {
            boolean result = complianceService.startComplianceCheck(tenantId, complianceId);
            return MyJsonBean.success("启动合规检查成功", result);
        } catch (Exception e) {
            log.error("启动合规检查失败", e);
            return MyJsonBean.error("启动合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/pause/{complianceId}")
    @ApiOperation("暂停合规检查")
    public MyJsonBean pauseComplianceCheck(@RequestHeader("tenantId") Long tenantId,
                                          @PathVariable Long complianceId) {
        try {
            boolean result = complianceService.pauseComplianceCheck(tenantId, complianceId);
            return MyJsonBean.success("暂停合规检查成功", result);
        } catch (Exception e) {
            log.error("暂停合规检查失败", e);
            return MyJsonBean.error("暂停合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/resume/{complianceId}")
    @ApiOperation("恢复合规检查")
    public MyJsonBean resumeComplianceCheck(@RequestHeader("tenantId") Long tenantId,
                                           @PathVariable Long complianceId) {
        try {
            boolean result = complianceService.resumeComplianceCheck(tenantId, complianceId);
            return MyJsonBean.success("恢复合规检查成功", result);
        } catch (Exception e) {
            log.error("恢复合规检查失败", e);
            return MyJsonBean.error("恢复合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/complete/{complianceId}")
    @ApiOperation("完成合规检查")
    public MyJsonBean completeComplianceCheck(@RequestHeader("tenantId") Long tenantId,
                                             @PathVariable Long complianceId,
                                             @RequestBody Map<String, Object> completionData) {
        try {
            boolean result = complianceService.completeComplianceCheck(tenantId, complianceId, completionData);
            return MyJsonBean.success("完成合规检查成功", result);
        } catch (Exception e) {
            log.error("完成合规检查失败", e);
            return MyJsonBean.error("完成合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{complianceId}")
    @ApiOperation("取消合规检查")
    public MyJsonBean cancelComplianceCheck(@RequestHeader("tenantId") Long tenantId,
                                           @PathVariable Long complianceId,
                                           @RequestParam String reason) {
        try {
            boolean result = complianceService.cancelComplianceCheck(tenantId, complianceId, reason);
            return MyJsonBean.success("取消合规检查成功", result);
        } catch (Exception e) {
            log.error("取消合规检查失败", e);
            return MyJsonBean.error("取消合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/update-progress/{complianceId}")
    @ApiOperation("更新检查进度")
    public MyJsonBean updateCheckProgress(@RequestHeader("tenantId") Long tenantId,
                                         @PathVariable Long complianceId,
                                         @RequestParam Double progress) {
        try {
            boolean result = complianceService.updateCheckProgress(tenantId, complianceId, progress);
            return MyJsonBean.success("更新检查进度成功", result);
        } catch (Exception e) {
            log.error("更新检查进度失败", e);
            return MyJsonBean.error("更新检查进度失败: " + e.getMessage());
        }
    }

    // ==================== 合规规则管理 ====================

    @PostMapping("/configure-rule/{complianceId}")
    @ApiOperation("配置合规规则")
    public MyJsonBean configureComplianceRule(@RequestHeader("tenantId") Long tenantId,
                                             @PathVariable Long complianceId,
                                             @RequestBody Map<String, Object> ruleConfig) {
        try {
            boolean result = complianceService.configureComplianceRule(tenantId, complianceId, ruleConfig);
            return MyJsonBean.success("配置合规规则成功", result);
        } catch (Exception e) {
            log.error("配置合规规则失败", e);
            return MyJsonBean.error("配置合规规则失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute/{complianceId}")
    @ApiOperation("执行合规检查")
    public MyJsonBean executeComplianceCheck(@RequestHeader("tenantId") Long tenantId,
                                            @PathVariable Long complianceId) {
        try {
            Map<String, Object> result = complianceService.executeComplianceCheck(tenantId, complianceId);
            return MyJsonBean.success("执行合规检查成功", result);
        } catch (Exception e) {
            log.error("执行合规检查失败", e);
            return MyJsonBean.error("执行合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/assess-risk/{complianceId}")
    @ApiOperation("评估合规风险")
    public MyJsonBean assessComplianceRisk(@RequestHeader("tenantId") Long tenantId,
                                          @PathVariable Long complianceId) {
        try {
            Map<String, Object> result = complianceService.assessComplianceRisk(tenantId, complianceId);
            return MyJsonBean.success("评估合规风险成功", result);
        } catch (Exception e) {
            log.error("评估合规风险失败", e);
            return MyJsonBean.error("评估合规风险失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-report/{complianceId}")
    @ApiOperation("生成合规报告")
    public MyJsonBean generateComplianceReport(@RequestHeader("tenantId") Long tenantId,
                                              @PathVariable Long complianceId) {
        try {
            Map<String, Object> result = complianceService.generateComplianceReport(tenantId, complianceId);
            return MyJsonBean.success("生成合规报告成功", result);
        } catch (Exception e) {
            log.error("生成合规报告失败", e);
            return MyJsonBean.error("生成合规报告失败: " + e.getMessage());
        }
    }

    @PostMapping("/analyze-result/{complianceId}")
    @ApiOperation("分析合规结果")
    public MyJsonBean analyzeComplianceResult(@RequestHeader("tenantId") Long tenantId,
                                             @PathVariable Long complianceId) {
        try {
            Map<String, Object> result = complianceService.analyzeComplianceResult(tenantId, complianceId);
            return MyJsonBean.success("分析合规结果成功", result);
        } catch (Exception e) {
            log.error("分析合规结果失败", e);
            return MyJsonBean.error("分析合规结果失败: " + e.getMessage());
        }
    }

    // ==================== 整改管理功能 ====================

    @PostMapping("/create-rectification-plan/{complianceId}")
    @ApiOperation("创建整改计划")
    public MyJsonBean createRectificationPlan(@RequestHeader("tenantId") Long tenantId,
                                             @PathVariable Long complianceId,
                                             @RequestBody Map<String, Object> planData) {
        try {
            boolean result = complianceService.createRectificationPlan(tenantId, complianceId, planData);
            return MyJsonBean.success("创建整改计划成功", result);
        } catch (Exception e) {
            log.error("创建整改计划失败", e);
            return MyJsonBean.error("创建整改计划失败: " + e.getMessage());
        }
    }

    @PostMapping("/update-rectification-progress/{complianceId}")
    @ApiOperation("更新整改进度")
    public MyJsonBean updateRectificationProgress(@RequestHeader("tenantId") Long tenantId,
                                                 @PathVariable Long complianceId,
                                                 @RequestParam Double progress) {
        try {
            boolean result = complianceService.updateRectificationProgress(tenantId, complianceId, progress);
            return MyJsonBean.success("更新整改进度成功", result);
        } catch (Exception e) {
            log.error("更新整改进度失败", e);
            return MyJsonBean.error("更新整改进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/complete-rectification/{complianceId}")
    @ApiOperation("完成整改")
    public MyJsonBean completeRectification(@RequestHeader("tenantId") Long tenantId,
                                           @PathVariable Long complianceId,
                                           @RequestBody Map<String, Object> completionData) {
        try {
            boolean result = complianceService.completeRectification(tenantId, complianceId, completionData);
            return MyJsonBean.success("完成整改成功", result);
        } catch (Exception e) {
            log.error("完成整改失败", e);
            return MyJsonBean.error("完成整改失败: " + e.getMessage());
        }
    }

    @PostMapping("/request-recheck/{complianceId}")
    @ApiOperation("申请复查")
    public MyJsonBean requestRecheck(@RequestHeader("tenantId") Long tenantId,
                                    @PathVariable Long complianceId) {
        try {
            boolean result = complianceService.requestRecheck(tenantId, complianceId);
            return MyJsonBean.success("申请复查成功", result);
        } catch (Exception e) {
            log.error("申请复查失败", e);
            return MyJsonBean.error("申请复查失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute-recheck/{complianceId}")
    @ApiOperation("执行复查")
    public MyJsonBean executeRecheck(@RequestHeader("tenantId") Long tenantId,
                                    @PathVariable Long complianceId) {
        try {
            Map<String, Object> result = complianceService.executeRecheck(tenantId, complianceId);
            return MyJsonBean.success("执行复查成功", result);
        } catch (Exception e) {
            log.error("执行复查失败", e);
            return MyJsonBean.error("执行复查失败: " + e.getMessage());
        }
    }

    @PostMapping("/complete-recheck/{complianceId}")
    @ApiOperation("完成复查")
    public MyJsonBean completeRecheck(@RequestHeader("tenantId") Long tenantId,
                                     @PathVariable Long complianceId,
                                     @RequestBody Map<String, Object> recheckResult) {
        try {
            boolean result = complianceService.completeRecheck(tenantId, complianceId, recheckResult);
            return MyJsonBean.success("完成复查成功", result);
        } catch (Exception e) {
            log.error("完成复查失败", e);
            return MyJsonBean.error("完成复查失败: " + e.getMessage());
        }
    }

    // ==================== 查询统计功能 ====================

    @GetMapping("/list/by-type")
    @ApiOperation("根据检查类型查询")
    public MyJsonBean getCompliancesByType(@RequestHeader("tenantId") Long tenantId,
                                          @RequestParam String complianceType) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByType(tenantId, complianceType);
            return MyJsonBean.success("根据检查类型查询成功", result);
        } catch (Exception e) {
            log.error("根据检查类型查询失败", e);
            return MyJsonBean.error("根据检查类型查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-check-status")
    @ApiOperation("根据检查状态查询")
    public MyJsonBean getCompliancesByCheckStatus(@RequestHeader("tenantId") Long tenantId,
                                                 @RequestParam String checkStatus) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByCheckStatus(tenantId, checkStatus);
            return MyJsonBean.success("根据检查状态查询成功", result);
        } catch (Exception e) {
            log.error("根据检查状态查询失败", e);
            return MyJsonBean.error("根据检查状态查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-compliance-status")
    @ApiOperation("根据合规状态查询")
    public MyJsonBean getCompliancesByComplianceStatus(@RequestHeader("tenantId") Long tenantId,
                                                      @RequestParam String complianceStatus) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByComplianceStatus(tenantId, complianceStatus);
            return MyJsonBean.success("根据合规状态查询成功", result);
        } catch (Exception e) {
            log.error("根据合规状态查询失败", e);
            return MyJsonBean.error("根据合规状态查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-risk-level")
    @ApiOperation("根据风险等级查询")
    public MyJsonBean getCompliancesByRiskLevel(@RequestHeader("tenantId") Long tenantId,
                                               @RequestParam String riskLevel) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByRiskLevel(tenantId, riskLevel);
            return MyJsonBean.success("根据风险等级查询成功", result);
        } catch (Exception e) {
            log.error("根据风险等级查询失败", e);
            return MyJsonBean.error("根据风险等级查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-priority")
    @ApiOperation("根据优先级查询")
    public MyJsonBean getCompliancesByPriority(@RequestHeader("tenantId") Long tenantId,
                                              @RequestParam String priority) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByPriority(tenantId, priority);
            return MyJsonBean.success("根据优先级查询成功", result);
        } catch (Exception e) {
            log.error("根据优先级查询失败", e);
            return MyJsonBean.error("根据优先级查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-checker")
    @ApiOperation("根据检查人员查询")
    public MyJsonBean getCompliancesByChecker(@RequestHeader("tenantId") Long tenantId,
                                             @RequestParam String checker) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByChecker(tenantId, checker);
            return MyJsonBean.success("根据检查人员查询成功", result);
        } catch (Exception e) {
            log.error("根据检查人员查询失败", e);
            return MyJsonBean.error("根据检查人员查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/by-rectification-status")
    @ApiOperation("根据整改状态查询")
    public MyJsonBean getCompliancesByRectificationStatus(@RequestHeader("tenantId") Long tenantId,
                                                         @RequestParam String rectificationStatus) {
        try {
            List<TsTaxCompliance> result = complianceService.getCompliancesByRectificationStatus(tenantId, rectificationStatus);
            return MyJsonBean.success("根据整改状态查询成功", result);
        } catch (Exception e) {
            log.error("根据整改状态查询失败", e);
            return MyJsonBean.error("根据整改状态查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/expiring-soon")
    @ApiOperation("查询即将到期的检查")
    public MyJsonBean getExpiringSoonCompliances(@RequestHeader("tenantId") Long tenantId,
                                                @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<TsTaxCompliance> result = complianceService.getExpiringSoonCompliances(tenantId, days);
            return MyJsonBean.success("查询即将到期的检查成功", result);
        } catch (Exception e) {
            log.error("查询即将到期的检查失败", e);
            return MyJsonBean.error("查询即将到期的检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/overdue")
    @ApiOperation("查询逾期的检查")
    public MyJsonBean getOverdueCompliances(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<TsTaxCompliance> result = complianceService.getOverdueCompliances(tenantId);
            return MyJsonBean.success("查询逾期的检查成功", result);
        } catch (Exception e) {
            log.error("查询逾期的检查失败", e);
            return MyJsonBean.error("查询逾期的检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/high-risk")
    @ApiOperation("查询高风险检查")
    public MyJsonBean getHighRiskCompliances(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<TsTaxCompliance> result = complianceService.getHighRiskCompliances(tenantId);
            return MyJsonBean.success("查询高风险检查成功", result);
        } catch (Exception e) {
            log.error("查询高风险检查失败", e);
            return MyJsonBean.error("查询高风险检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/list/need-rectification")
    @ApiOperation("查询需要整改的检查")
    public MyJsonBean getNeedRectificationCompliances(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<TsTaxCompliance> result = complianceService.getNeedRectificationCompliances(tenantId);
            return MyJsonBean.success("查询需要整改的检查成功", result);
        } catch (Exception e) {
            log.error("查询需要整改的检查失败", e);
            return MyJsonBean.error("查询需要整改的检查失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析功能 ====================

    @GetMapping("/overview")
    @ApiOperation("获取合规检查概览")
    public MyJsonBean getComplianceOverview(@RequestHeader("tenantId") Long tenantId) {
        try {
            Map<String, Object> result = complianceService.getComplianceOverview(tenantId);
            return MyJsonBean.success("获取合规检查概览成功", result);
        } catch (Exception e) {
            log.error("获取合规检查概览失败", e);
            return MyJsonBean.error("获取合规检查概览失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-check-status")
    @ApiOperation("按检查状态统计数量")
    public MyJsonBean countCompliancesByCheckStatus(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.countCompliancesByCheckStatus(tenantId);
            return MyJsonBean.success("按检查状态统计数量成功", result);
        } catch (Exception e) {
            log.error("按检查状态统计数量失败", e);
            return MyJsonBean.error("按检查状态统计数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-compliance-status")
    @ApiOperation("按合规状态统计数量")
    public MyJsonBean countCompliancesByComplianceStatus(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.countCompliancesByComplianceStatus(tenantId);
            return MyJsonBean.success("按合规状态统计数量成功", result);
        } catch (Exception e) {
            log.error("按合规状态统计数量失败", e);
            return MyJsonBean.error("按合规状态统计数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-type")
    @ApiOperation("按检查类型统计数量")
    public MyJsonBean countCompliancesByType(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.countCompliancesByType(tenantId);
            return MyJsonBean.success("按检查类型统计数量成功", result);
        } catch (Exception e) {
            log.error("按检查类型统计数量失败", e);
            return MyJsonBean.error("按检查类型统计数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-risk-level")
    @ApiOperation("按风险等级统计数量")
    public MyJsonBean countCompliancesByRiskLevel(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.countCompliancesByRiskLevel(tenantId);
            return MyJsonBean.success("按风险等级统计数量成功", result);
        } catch (Exception e) {
            log.error("按风险等级统计数量失败", e);
            return MyJsonBean.error("按风险等级统计数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-priority")
    @ApiOperation("按优先级统计数量")
    public MyJsonBean countCompliancesByPriority(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.countCompliancesByPriority(tenantId);
            return MyJsonBean.success("按优先级统计数量成功", result);
        } catch (Exception e) {
            log.error("按优先级统计数量失败", e);
            return MyJsonBean.error("按优先级统计数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/by-rectification-status")
    @ApiOperation("按整改状态统计数量")
    public MyJsonBean countCompliancesByRectificationStatus(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.countCompliancesByRectificationStatus(tenantId);
            return MyJsonBean.success("按整改状态统计数量成功", result);
        } catch (Exception e) {
            log.error("按整改状态统计数量失败", e);
            return MyJsonBean.error("按整改状态统计数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend/check")
    @ApiOperation("获取检查趋势数据")
    public MyJsonBean getCheckTrend(@RequestHeader("tenantId") Long tenantId,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                   @RequestParam(defaultValue = "day") String groupBy) {
        try {
            List<Map<String, Object>> result = complianceService.getCheckTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("获取检查趋势数据成功", result);
        } catch (Exception e) {
            log.error("获取检查趋势数据失败", e);
            return MyJsonBean.error("获取检查趋势数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/trend/compliance-score")
    @ApiOperation("获取合规评分趋势")
    public MyJsonBean getComplianceScoreTrend(@RequestHeader("tenantId") Long tenantId,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                             @RequestParam(defaultValue = "day") String groupBy) {
        try {
            List<Map<String, Object>> result = complianceService.getComplianceScoreTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("获取合规评分趋势成功", result);
        } catch (Exception e) {
            log.error("获取合规评分趋势失败", e);
            return MyJsonBean.error("获取合规评分趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/risk")
    @ApiOperation("获取风险分布数据")
    public MyJsonBean getRiskDistribution(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.getRiskDistribution(tenantId);
            return MyJsonBean.success("获取风险分布数据成功", result);
        } catch (Exception e) {
            log.error("获取风险分布数据失败", e);
            return MyJsonBean.error("获取风险分布数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/effectiveness")
    @ApiOperation("获取效果评估数据")
    public MyJsonBean getEffectivenessData(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.getEffectivenessData(tenantId);
            return MyJsonBean.success("获取效果评估数据成功", result);
        } catch (Exception e) {
            log.error("获取效果评估数据失败", e);
            return MyJsonBean.error("获取效果评估数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/ranking")
    @ApiOperation("获取检查排行榜")
    public MyJsonBean getCheckRanking(@RequestHeader("tenantId") Long tenantId,
                                     @RequestParam(defaultValue = "complianceScore") String rankBy,
                                     @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> result = complianceService.getCheckRanking(tenantId, rankBy, limit);
            return MyJsonBean.success("获取检查排行榜成功", result);
        } catch (Exception e) {
            log.error("获取检查排行榜失败", e);
            return MyJsonBean.error("获取检查排行榜失败: " + e.getMessage());
        }
    }

    @GetMapping("/efficiency-stats")
    @ApiOperation("获取检查效率统计")
    public MyJsonBean getCheckEfficiencyStats(@RequestHeader("tenantId") Long tenantId) {
        try {
            Map<String, Object> result = complianceService.getCheckEfficiencyStats(tenantId);
            return MyJsonBean.success("获取检查效率统计成功", result);
        } catch (Exception e) {
            log.error("获取检查效率统计失败", e);
            return MyJsonBean.error("获取检查效率统计失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作功能 ====================

    @PostMapping("/batch/create")
    @ApiOperation("批量创建合规检查")
    public MyJsonBean batchCreateCompliances(@RequestHeader("tenantId") Long tenantId,
                                            @RequestBody List<TsTaxCompliance> compliances) {
        try {
            List<TsTaxCompliance> result = complianceService.batchCreateCompliances(tenantId, compliances);
            return MyJsonBean.success("批量创建合规检查成功", result);
        } catch (Exception e) {
            log.error("批量创建合规检查失败", e);
            return MyJsonBean.error("批量创建合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/update-check-status")
    @ApiOperation("批量更新检查状态")
    public MyJsonBean batchUpdateCheckStatus(@RequestHeader("tenantId") Long tenantId,
                                            @RequestBody List<Long> complianceIds,
                                            @RequestParam String checkStatus) {
        try {
            boolean result = complianceService.batchUpdateCheckStatus(tenantId, complianceIds, checkStatus);
            return MyJsonBean.success("批量更新检查状态成功", result);
        } catch (Exception e) {
            log.error("批量更新检查状态失败", e);
            return MyJsonBean.error("批量更新检查状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/update-compliance-status")
    @ApiOperation("批量更新合规状态")
    public MyJsonBean batchUpdateComplianceStatus(@RequestHeader("tenantId") Long tenantId,
                                                 @RequestBody List<Long> complianceIds,
                                                 @RequestParam String complianceStatus) {
        try {
            boolean result = complianceService.batchUpdateComplianceStatus(tenantId, complianceIds, complianceStatus);
            return MyJsonBean.success("批量更新合规状态成功", result);
        } catch (Exception e) {
            log.error("批量更新合规状态失败", e);
            return MyJsonBean.error("批量更新合规状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/update-rectification-status")
    @ApiOperation("批量更新整改状态")
    public MyJsonBean batchUpdateRectificationStatus(@RequestHeader("tenantId") Long tenantId,
                                                    @RequestBody List<Long> complianceIds,
                                                    @RequestParam String rectificationStatus) {
        try {
            boolean result = complianceService.batchUpdateRectificationStatus(tenantId, complianceIds, rectificationStatus);
            return MyJsonBean.success("批量更新整改状态成功", result);
        } catch (Exception e) {
            log.error("批量更新整改状态失败", e);
            return MyJsonBean.error("批量更新整改状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/delete")
    @ApiOperation("批量删除合规检查")
    public MyJsonBean batchDeleteCompliances(@RequestHeader("tenantId") Long tenantId,
                                            @RequestBody List<Long> complianceIds) {
        try {
            boolean result = complianceService.batchDeleteCompliances(tenantId, complianceIds);
            return MyJsonBean.success("批量删除合规检查成功", result);
        } catch (Exception e) {
            log.error("批量删除合规检查失败", e);
            return MyJsonBean.error("批量删除合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/archive")
    @ApiOperation("批量归档合规检查")
    public MyJsonBean batchArchiveCompliances(@RequestHeader("tenantId") Long tenantId,
                                             @RequestBody List<Long> complianceIds) {
        try {
            boolean result = complianceService.batchArchiveCompliances(tenantId, complianceIds);
            return MyJsonBean.success("批量归档合规检查成功", result);
        } catch (Exception e) {
            log.error("批量归档合规检查失败", e);
            return MyJsonBean.error("批量归档合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/activate")
    @ApiOperation("批量激活合规检查")
    public MyJsonBean batchActivateCompliances(@RequestHeader("tenantId") Long tenantId,
                                              @RequestBody List<Long> complianceIds) {
        try {
            boolean result = complianceService.batchActivateCompliances(tenantId, complianceIds);
            return MyJsonBean.success("批量激活合规检查成功", result);
        } catch (Exception e) {
            log.error("批量激活合规检查失败", e);
            return MyJsonBean.error("批量激活合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/import")
    @ApiOperation("批量导入合规检查")
    public MyJsonBean batchImportCompliances(@RequestHeader("tenantId") Long tenantId,
                                            @RequestBody List<Map<String, Object>> complianceData) {
        try {
            Map<String, Object> result = complianceService.batchImportCompliances(tenantId, complianceData);
            return MyJsonBean.success("批量导入合规检查成功", result);
        } catch (Exception e) {
            log.error("批量导入合规检查失败", e);
            return MyJsonBean.error("批量导入合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/export")
    @ApiOperation("批量导出合规检查")
    public MyJsonBean batchExportCompliances(@RequestHeader("tenantId") Long tenantId,
                                            @RequestBody List<Long> complianceIds) {
        try {
            List<Map<String, Object>> result = complianceService.batchExportCompliances(tenantId, complianceIds);
            return MyJsonBean.success("批量导出合规检查成功", result);
        } catch (Exception e) {
            log.error("批量导出合规检查失败", e);
            return MyJsonBean.error("批量导出合规检查失败: " + e.getMessage());
        }
    }

    // ==================== 工具功能 ====================

    @PostMapping("/copy/{sourceComplianceId}")
    @ApiOperation("复制合规检查")
    public MyJsonBean copyCompliance(@RequestHeader("tenantId") Long tenantId,
                                    @PathVariable Long sourceComplianceId,
                                    @RequestParam String newComplianceName) {
        try {
            TsTaxCompliance result = complianceService.copyCompliance(tenantId, sourceComplianceId, newComplianceName);
            return MyJsonBean.success("复制合规检查成功", result);
        } catch (Exception e) {
            log.error("复制合规检查失败", e);
            return MyJsonBean.error("复制合规检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-reminder/{complianceId}")
    @ApiOperation("发送合规提醒")
    public MyJsonBean sendComplianceReminder(@RequestHeader("tenantId") Long tenantId,
                                            @PathVariable Long complianceId,
                                            @RequestParam String reminderType) {
        try {
            boolean result = complianceService.sendComplianceReminder(tenantId, complianceId, reminderType);
            return MyJsonBean.success("发送合规提醒成功", result);
        } catch (Exception e) {
            log.error("发送合规提醒失败", e);
            return MyJsonBean.error("发送合规提醒失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护功能 ====================

    @GetMapping("/system/health-check")
    @ApiOperation("系统健康检查")
    public MyJsonBean systemHealthCheck(@RequestHeader("tenantId") Long tenantId) {
        try {
            Map<String, Object> result = complianceService.systemHealthCheck(tenantId);
            return MyJsonBean.success("系统健康检查成功", result);
        } catch (Exception e) {
            log.error("系统健康检查失败", e);
            return MyJsonBean.error("系统健康检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/data-consistency-check")
    @ApiOperation("数据一致性检查")
    public MyJsonBean dataConsistencyCheck(@RequestHeader("tenantId") Long tenantId) {
        try {
            List<Map<String, Object>> result = complianceService.dataConsistencyCheck(tenantId);
            return MyJsonBean.success("数据一致性检查成功", result);
        } catch (Exception e) {
            log.error("数据一致性检查失败", e);
            return MyJsonBean.error("数据一致性检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/performance-stats")
    @ApiOperation("性能统计")
    public MyJsonBean performanceStats(@RequestHeader("tenantId") Long tenantId) {
        try {
            Map<String, Object> result = complianceService.performanceStats(tenantId);
            return MyJsonBean.success("性能统计成功", result);
        } catch (Exception e) {
            log.error("性能统计失败", e);
            return MyJsonBean.error("性能统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/system/cleanup-expired-data")
    @ApiOperation("清理过期数据")
    public MyJsonBean cleanupExpiredData(@RequestHeader("tenantId") Long tenantId,
                                        @RequestParam(defaultValue = "30") Integer days) {
        try {
            boolean result = complianceService.cleanupExpiredData(tenantId, days);
            return MyJsonBean.success("清理过期数据成功", result);
        } catch (Exception e) {
            log.error("清理过期数据失败", e);
            return MyJsonBean.error("清理过期数据失败: " + e.getMessage());
        }
    }
}
