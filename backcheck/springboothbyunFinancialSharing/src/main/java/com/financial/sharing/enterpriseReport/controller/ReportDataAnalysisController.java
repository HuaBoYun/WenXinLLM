package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.ReportDataQueryDTO;
import com.financial.sharing.enterpriseReport.service.ReportDataAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 报表数据查询分析Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-报表数据查询分析")
@RestController
@RequestMapping("/enterpriseReport/reportDataAnalysis")
public class ReportDataAnalysisController {

    @Autowired
    private ReportDataAnalysisService reportDataAnalysisService;

    /**
     * 按指标汇总数据
     */
    @ApiOperation("按指标汇总数据")
    @PostMapping("/aggregateByIndicator")
    public MyJsonBean aggregateByIndicator(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            String period = params.get("period");
            
            List<ReportDataQueryDTO> list = reportDataAnalysisService.aggregateByIndicator(taskId, period);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 按组织汇总数据
     */
    @ApiOperation("按组织汇总数据")
    @PostMapping("/aggregateByOrg")
    public MyJsonBean aggregateByOrg(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            String period = params.get("period");
            
            List<ReportDataQueryDTO> list = reportDataAnalysisService.aggregateByOrg(taskId, period);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 按期间汇总数据
     */
    @ApiOperation("按期间汇总数据")
    @PostMapping("/aggregateByPeriod")
    public MyJsonBean aggregateByPeriod(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            String indicatorId = params.get("indicatorId");
            String orgId = params.get("orgId");
            
            List<ReportDataQueryDTO> list = reportDataAnalysisService.aggregateByPeriod(taskId, indicatorId, orgId);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 期间对比分析
     */
    @ApiOperation("期间对比分析")
    @PostMapping("/comparePeriod")
    public MyJsonBean comparePeriod(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String taskId = params.get("taskId");
            String indicatorId = params.get("indicatorId");
            String orgId = params.get("orgId");
            String currentPeriod = params.get("currentPeriod");
            String comparePeriod = params.get("comparePeriod");
            
            if (currentPeriod == null || currentPeriod.isEmpty()) {
                return MyJsonBean.errorData("当前期间不能为空");
            }
            if (comparePeriod == null || comparePeriod.isEmpty()) {
                return MyJsonBean.errorData("对比期间不能为空");
            }
            
            ReportDataQueryDTO result = reportDataAnalysisService.comparePeriod(
                taskId, indicatorId, orgId, currentPeriod, comparePeriod);
            return MyJsonBean.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 多维度数据查询
     */
    @ApiOperation("多维度数据查询")
    @PostMapping("/multiDimensionQuery")
    public MyJsonBean multiDimensionQuery(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<ReportDataQueryDTO> list = reportDataAnalysisService.multiDimensionQuery(params);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 数据趋势分析
     */
    @ApiOperation("数据趋势分析")
    @PostMapping("/trendAnalysis")
    public MyJsonBean trendAnalysis(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String taskId = params.get("taskId");
            String indicatorId = params.get("indicatorId");
            String orgId = params.get("orgId");
            String startPeriod = params.get("startPeriod");
            String endPeriod = params.get("endPeriod");

            List<ReportDataQueryDTO> list = reportDataAnalysisService.trendAnalysis(
                taskId, indicatorId, orgId, startPeriod, endPeriod);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}


