package com.global.treasurer.controller;

import com.global.treasurer.service.IBillQueryService;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 票据查询Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@RestController
@RequestMapping("/bill/query")
@Api(tags = "票据查询")
public class BillQueryController {
    private static final Logger log = LoggerFactory.getLogger(BillQueryController.class);

    @Autowired
    private IBillQueryService billQueryService;

     /**
      * 从 HttpServletRequest 中提取 form 参数到 Map（兼容 form-urlencoded 和 query string）
      */
     private Map<String, Object> extractParams(HttpServletRequest request) {
         Map<String, Object> params = new HashMap<>();
         request.getParameterMap().forEach((key, values) -> {
             if (values != null && values.length > 0) {
                 params.put(key, values[0]);
             }
         });
         return params;
     }
 
    @PostMapping("/ledger")
    @ApiOperation(value = "票据台账查询", notes = "综合查询票据台账")
     public String getLedger(HttpServletRequest request) {
        try {
             Map<String, Object> params = extractParams(request);
            log.info("票据台账查询参数: {}", params);
            Map<String, Object> ledgerData = billQueryService.getInstrumentLedger(params);
            return JsonBean.success(ledgerData);
        } catch (Exception e) {
            log.error("查询票据台账失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/statistics")
    @ApiOperation(value = "票据统计分析", notes = "统计分析票据数据")
     public String getStatistics(HttpServletRequest request) {
        try {
             Map<String, Object> params = extractParams(request);
            Map<String, Object> statisticsData = billQueryService.getInstrumentStatistics(params);
            return JsonBean.success(statisticsData);
        } catch (Exception e) {
            log.error("查询票据统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/maturity-alerts")
    @ApiOperation(value = "票据到期提醒", notes = "获取票据到期提醒列表")
     public String getMaturityAlerts(HttpServletRequest request) {
        try {
             Map<String, Object> params = extractParams(request);
            List<Map<String, Object>> alerts = billQueryService.getMaturityAlerts(params);
            return JsonBean.success(alerts);
        } catch (Exception e) {
            log.error("查询票据到期提醒失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/maturity-calendar")
    @ApiOperation(value = "票据到期日历", notes = "获取票据到期日历数据")
     public String getMaturityCalendar(HttpServletRequest request) {
        try {
             Map<String, Object> params = extractParams(request);
            List<Map<String, Object>> calendarData = billQueryService.getMaturityCalendar(params);
            return JsonBean.success(calendarData);
        } catch (Exception e) {
            log.error("查询票据到期日历失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/report")
    @ApiOperation(value = "票据报表查询", notes = "查询票据报表数据")
     public String getReport(HttpServletRequest request) {
        try {
             Map<String, Object> params = extractParams(request);
            Map<String, Object> reportData = billQueryService.getInstrumentReport(params);
            return JsonBean.success(reportData);
        } catch (Exception e) {
            log.error("查询票据报表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{instrumentId}")
    @ApiOperation(value = "票据详情查询", notes = "查询票据详细信息")
    public String getDetail(@PathVariable Long instrumentId) {
        try {
            Map<String, Object> detail = billQueryService.getInstrumentDetail(instrumentId);
            if (detail == null) {
                return JsonBean.error("票据不存在");
            }
            return JsonBean.success(detail);
        } catch (BizException e) {
            log.warn("查询票据详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询票据详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/history/{instrumentId}")
    @ApiOperation(value = "票据流转历史查询", notes = "查询票据流转历史记录")
    public String getHistory(@PathVariable Long instrumentId) {
        try {
            List<Map<String, Object>> history = billQueryService.getInstrumentHistory(instrumentId);
            return JsonBean.success(history);
        } catch (BizException e) {
            log.warn("查询票据流转历史失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("查询票据流转历史失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

