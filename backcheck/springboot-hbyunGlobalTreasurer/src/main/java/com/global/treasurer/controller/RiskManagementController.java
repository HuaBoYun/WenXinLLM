package com.global.treasurer.controller;

import com.global.treasurer.dto.RiskAssessmentQueryDTO;
import com.global.treasurer.dto.RiskIdentificationQueryDTO;
import com.global.treasurer.service.IRiskAssessmentService;
import com.global.treasurer.service.IRiskIdentificationService;
import com.global.treasurer.service.IRiskTypeService;
import com.hbfk.util.user.UserProvider;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 风险管理统一Controller
 * 处理统计、导出、分析等公共接口
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@RestController
@RequestMapping("/risk-management")
@Api(tags = "风险管理公共接口")
public class RiskManagementController {
    @Autowired
    private IRiskTypeService riskTypeService;

    @Autowired
    private IRiskIdentificationService riskIdentificationService;

    @Autowired
    private IRiskAssessmentService riskAssessmentService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 获取风险统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "获取风险统计信息", notes = "获取风险类型统计数据")
    public String getRiskStatistics(@RequestParam(required = false) Long orgId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> statistics = riskTypeService.getStatistics(orgId);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            return JsonBean.error("获取统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险分布分析
     */
    @GetMapping("/analysis/distribution")
    @ApiOperation(value = "获取风险分布分析", notes = "获取风险评估分布统计数据")
    public String getRiskDistributionAnalysis(@RequestParam(required = false) Long orgId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> analysis = riskAssessmentService.getDistributionAnalysis(orgId != null ? orgId.toString() : null);
            return JsonBean.success(analysis);
        } catch (Exception e) {
            return JsonBean.error("获取分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 导出风险识别报表
     */
    @GetMapping("/export/identification")
    @ApiOperation(value = "导出风险识别报表", notes = "导出风险识别数据到Excel")
    public void exportRiskIdentification(RiskIdentificationQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            riskIdentificationService.exportRiskIdentification(queryDTO, response);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    /**
     * 导出风险评估报表
     */
    @GetMapping("/export/assessment")
    @ApiOperation(value = "导出风险评估报表", notes = "导出风险评估数据到Excel")
    public void exportRiskAssessment(RiskAssessmentQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            riskAssessmentService.exportRiskAssessment(queryDTO, response);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                // ignore
            }
        }
    }
}

