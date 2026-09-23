package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hbfk.util.JsonBean;
import com.huabo.system.dto.FeeRecordQueryDTO;
import com.huabo.system.dto.FeeStatisticsQueryDTO;
import com.huabo.system.dto.FeeDrilldownQueryDTO;
import com.huabo.system.service.FeeRecordService;
import com.huabo.system.service.FeeStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 费用记录与统计控制器
 * <p>提供个人费用记录查询、费用导出、费用统计等接口</p>
 *
 * @author hbyun
 */
@Slf4j
@Tag(name = "费用记录与统计")
@RestController
@RequestMapping(value = "/system/fee")
public class FeeRecordController {

    @Resource
    private FeeRecordService feeRecordService;

    @Resource
    private FeeStatisticsService feeStatisticsService;

    @RequestMapping(value = "/record/personal", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "个人费用明细查询")
    public JsonBean personalRecords(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody FeeRecordQueryDTO dto) {
        try {
            return feeRecordService.getPersonalRecords(token, dto);
        } catch (Exception e) {
            log.error("查询个人费用明细失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/record/export", produces = "application/octet-stream", method = {RequestMethod.POST})
    @Operation(summary = "费用数据导出")
    public void exportRecords(HttpServletRequest request, HttpServletResponse response,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody FeeRecordQueryDTO dto) {
        try {
            feeRecordService.exportRecords(token, dto, response);
        } catch (Exception e) {
            log.error("导出费用数据失败", e);
        }
    }

    @RequestMapping(value = "/statistics/query", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "统计费用查询-支持个人/公司/集团三维度")
    public JsonBean statisticsQuery(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody FeeStatisticsQueryDTO dto) {
        try {
            return feeStatisticsService.queryStatistics(token, dto);
        } catch (Exception e) {
            log.error("查询统计费用失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/statistics/drilldown", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "穿透查询-大模块下钻到小模块")
    public JsonBean drilldown(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody FeeDrilldownQueryDTO dto) {
        try {
            return feeStatisticsService.drilldown(token, dto);
        } catch (Exception e) {
            log.error("穿透查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }
}
