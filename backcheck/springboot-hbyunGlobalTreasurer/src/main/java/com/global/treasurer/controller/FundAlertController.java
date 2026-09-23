package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Claude
 * @date 2026-01-20
 * @description 资金告警管理Controller
 */
@RestController
@RequestMapping("/fund-alert")
@Api(tags = "资金告警管理")
public class FundAlertController {

    private static final Logger log = LoggerFactory.getLogger(FundAlertController.class);

    @Resource
    private TblFundAlertService tblFundAlertService;

    /**
     * 分页查询告警列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询告警列表")
    public String getAlertPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("告警类型") @RequestParam(required = false) String alertType,
            @ApiParam("告警级别") @RequestParam(required = false) String alertLevel,
            @ApiParam("告警状态") @RequestParam(required = false) String alertStatus,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblFundAlert> pageInfo = tblFundAlertService.getAlertPage(pageNo, pageSize, alertType, alertLevel, alertStatus);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询告警分页数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取告警统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取告警统计数据")
    public String getAlertStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> statistics = new HashMap<>();
            // 获取总告警数
            long totalAlerts = tblFundAlertService.count();
            // 获取待处理告警数
            long pendingAlerts = tblFundAlertService.countByStatus("PENDING");
            // 获取处理中告警数
            long processingAlerts = tblFundAlertService.countByStatus("PROCESSING");
            // 获取已解决告警数
            long resolvedAlerts = tblFundAlertService.countByStatus("RESOLVED");
            // 获取已忽略告警数
            long ignoredAlerts = tblFundAlertService.countByStatus("IGNORED");
            // 计算解决率
            double resolutionRate = totalAlerts > 0 ? (double) resolvedAlerts / totalAlerts * 100 : 0;

            statistics.put("totalAlerts", totalAlerts);
            statistics.put("pendingAlerts", pendingAlerts);
            statistics.put("processingAlerts", processingAlerts);
            statistics.put("resolvedAlerts", resolvedAlerts);
            statistics.put("ignoredAlerts", ignoredAlerts);
            statistics.put("resolutionRate", Math.round(resolutionRate * 100) / 100.0);

            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取告警统计数据失败", e);
            return new JsonBean(0, "获取统计数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量处理告警
     */
    @PutMapping("/batch-handle")
    @ApiOperation("批量处理告警")
    public String batchHandleAlerts(
            @ApiParam("告警ID列表") @RequestParam(value = "alertIds", required = false) List<String> alertIds,
            HttpServletResponse response) throws IOException {

        try {
            tblFundAlertService.batchHandleAlerts(alertIds);
            return JsonBean.success("批量处理成功");
        } catch (Exception e) {
            log.error("批量处理告警失败", e);
            return new JsonBean(0, "批量处理失败: " + e.getMessage(), null).toJson();
        }
    }
}

