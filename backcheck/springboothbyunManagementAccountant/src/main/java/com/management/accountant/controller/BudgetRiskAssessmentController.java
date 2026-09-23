package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetRiskAssessmentService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算风险评估Controller
 * 
 * @description 预算风险评估接口，支持风险识别、评估、预警、应对、监控
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-风险评估"})
@RequestMapping(value = "/accountant/budget/risk")
@Slf4j
public class BudgetRiskAssessmentController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetRiskAssessmentService riskAssessmentService;

    /**
     * 风险识别
     */
    @Operation(summary = "风险识别")
    @ApiOperation("风险识别")
    @PostMapping("/identify")
    public MyJsonBean<Map<String, Object>> identifyRisks(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> risks = riskAssessmentService.identifyRisks(params);
            result.setCode(1);
            result.setMsg("识别成功");
            result.setData(risks);
        } catch (ServiceException ex) {
            log.error("风险识别失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("风险识别异常", e);
            result.setCode(0);
            result.setMsg("识别失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 风险评估
     */
    @Operation(summary = "风险评估")
    @ApiOperation("风险评估")
    @PostMapping("/assess")
    public MyJsonBean<Map<String, Object>> assessRisk(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> assessment = riskAssessmentService.assessRisk(params);
            result.setCode(1);
            result.setMsg("评估成功");
            result.setData(assessment);
        } catch (ServiceException ex) {
            log.error("风险评估失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("风险评估异常", e);
            result.setCode(0);
            result.setMsg("评估失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 风险预警
     */
    @Operation(summary = "风险预警")
    @ApiOperation("风险预警")
    @PostMapping("/alert")
    public MyJsonBean<Map<String, Object>> riskAlert(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> alert = riskAssessmentService.riskAlert(params);
            result.setCode(1);
            result.setMsg("预警成功");
            result.setData(alert);
        } catch (ServiceException ex) {
            log.error("风险预警失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("风险预警异常", e);
            result.setCode(0);
            result.setMsg("预警失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 风险应对
     */
    @Operation(summary = "风险应对")
    @ApiOperation("风险应对")
    @PostMapping("/respond")
    public MyJsonBean<Map<String, Object>> respondToRisk(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> response = riskAssessmentService.respondToRisk(params);
            result.setCode(1);
            result.setMsg("应对成功");
            result.setData(response);
        } catch (ServiceException ex) {
            log.error("风险应对失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("风险应对异常", e);
            result.setCode(0);
            result.setMsg("应对失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 风险监控
     */
    @Operation(summary = "风险监控")
    @ApiOperation("风险监控")
    @PostMapping("/monitor")
    public MyJsonBean<Map<String, Object>> monitorRisk(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> monitoring = riskAssessmentService.monitorRisk(params);
            result.setCode(1);
            result.setMsg("监控成功");
            result.setData(monitoring);
        } catch (ServiceException ex) {
            log.error("风险监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("风险监控异常", e);
            result.setCode(0);
            result.setMsg("监控失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取风险评估列表
     */
    @Operation(summary = "获取风险评估列表")
    @ApiOperation("获取风险评估列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> getRiskAssessmentList(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> list = riskAssessmentService.getRiskAssessmentList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取风险评估列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取风险评估统计数据
     */
    @Operation(summary = "获取风险评估统计数据")
    @ApiOperation("获取风险评估统计数据")
    @PostMapping("/stats")
    public MyJsonBean<Map<String, Object>> getRiskAssessmentStats(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = riskAssessmentService.getRiskAssessmentStats(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取风险评估统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出风险评估
     */
    @Operation(summary = "导出风险评估")
    @ApiOperation("导出风险评估")
    @GetMapping("/export/{assessmentId}")
    public MyJsonBean<Map<String, Object>> exportRiskAssessment(@PathVariable String assessmentId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = riskAssessmentService.exportRiskAssessment(assessmentId);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (Exception e) {
            log.error("导出风险评估异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制风险评估
     */
    @Operation(summary = "复制风险评估")
    @ApiOperation("复制风险评估")
    @PostMapping("/copy/{assessmentId}")
    public MyJsonBean<Map<String, Object>> copyRiskAssessment(@PathVariable String assessmentId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> copyResult = riskAssessmentService.copyRiskAssessment(assessmentId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copyResult);
        } catch (Exception e) {
            log.error("复制风险评估异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建风险评估
     */
    @Operation(summary = "创建风险评估")
    @ApiOperation("创建风险评估")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createRiskAssessment(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> createResult = riskAssessmentService.createRiskAssessment(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(createResult);
        } catch (Exception e) {
            log.error("创建风险评估异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新风险评估
     */
    @Operation(summary = "更新风险评估")
    @ApiOperation("更新风险评估")
    @PostMapping("/update")
    public MyJsonBean<Map<String, Object>> updateRiskAssessment(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> updateResult = riskAssessmentService.updateRiskAssessment(params);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updateResult);
        } catch (Exception e) {
            log.error("更新风险评估异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除风险评估
     */
    @Operation(summary = "删除风险评估")
    @ApiOperation("删除风险评估")
    @DeleteMapping("/delete/{assessmentId}")
    public MyJsonBean<Void> deleteRiskAssessment(@PathVariable String assessmentId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            riskAssessmentService.deleteRiskAssessment(assessmentId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除风险评估异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }
}

