package com.global.treasurer.controller;

import com.global.treasurer.service.IBillStatisticsService;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 票据统计查询Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/statistics")
@Api(tags = "票据统计查询")
public class BillStatisticsController {
    private static final Logger log = LoggerFactory.getLogger(BillStatisticsController.class);

    @Autowired
    private IBillStatisticsService billStatisticsService;

    /**
     * 统计概览
     */
    @PostMapping("/overview")
    @ApiOperation(value = "获取票据统计概览", notes = "综合统计票据数据概览")
    public String overview(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> overviewData = billStatisticsService.getStatisticsOverview(params);
            return JsonBean.success(overviewData);
        } catch (Exception e) {
            log.error("获取票据统计概览失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 类型统计
     */
    @PostMapping("/type")
    @ApiOperation(value = "获取票据类型统计", notes = "按类型统计票据数据")
    public String typeStatistics(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> typeData = billStatisticsService.getTypeStatistics(params);
            return JsonBean.success(typeData);
        } catch (Exception e) {
            log.error("获取票据类型统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 趋势分析
     */
    @PostMapping("/trend")
    @ApiOperation(value = "获取票据趋势分析", notes = "分析票据数据趋势")
    public String trend(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> trendData = billStatisticsService.getTrendAnalysis(params);
            return JsonBean.success(trendData);
        } catch (Exception e) {
            log.error("获取票据趋势分析失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}
