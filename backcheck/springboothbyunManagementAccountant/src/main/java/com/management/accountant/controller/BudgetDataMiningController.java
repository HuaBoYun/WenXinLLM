package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetDataMiningService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算数据挖掘Controller
 * 
 * @description 预算数据挖掘接口，支持模式识别、异常检测、关联分析等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-数据挖掘"})
@RequestMapping(value = "/accountant/advanced/data-mining")
@Slf4j
public class BudgetDataMiningController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDataMiningService dataMiningService;

    /**
     * 模式识别
     */
    @Operation(summary = "模式识别")
    @ApiOperation("模式识别")
    @PostMapping("/pattern/recognition")
    public MyJsonBean<Map<String, Object>> patternRecognition(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> patterns = dataMiningService.patternRecognition(params);
            result.setCode(1);
            result.setMsg("识别成功");
            result.setData(patterns);
        } catch (ServiceException ex) {
            log.error("模式识别失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("模式识别异常", e);
            result.setCode(0);
            result.setMsg("识别失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 异常检测
     */
    @Operation(summary = "异常检测")
    @ApiOperation("异常检测")
    @PostMapping("/anomaly/detection")
    public MyJsonBean<Map<String, Object>> anomalyDetection(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> anomalies = dataMiningService.anomalyDetection(params);
            result.setCode(1);
            result.setMsg("检测成功");
            result.setData(anomalies);
        } catch (ServiceException ex) {
            log.error("异常检测失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("异常检测异常", e);
            result.setCode(0);
            result.setMsg("检测失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 关联分析
     */
    @Operation(summary = "关联分析")
    @ApiOperation("关联分析")
    @PostMapping("/association/analysis")
    public MyJsonBean<Map<String, Object>> associationAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> associations = dataMiningService.associationAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(associations);
        } catch (ServiceException ex) {
            log.error("关联分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("关联分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 聚类分析
     */
    @Operation(summary = "聚类分析")
    @ApiOperation("聚类分析")
    @PostMapping("/clustering")
    public MyJsonBean<Map<String, Object>> clustering(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> clusters = dataMiningService.clustering(params);
            result.setCode(1);
            result.setMsg("聚类成功");
            result.setData(clusters);
        } catch (ServiceException ex) {
            log.error("聚类分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("聚类分析异常", e);
            result.setCode(0);
            result.setMsg("聚类失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成挖掘报告
     */
    @Operation(summary = "生成挖掘报告")
    @ApiOperation("生成挖掘报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generateMiningReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = dataMiningService.generateMiningReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("生成挖掘报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成挖掘报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    // list/stats/create/update/delete/copy/run/stop/export/results/logs 端点已在 BudgetAdvancedFeaturesController 中实现
}

