package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationDataFlowService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算数据流集成Controller
 * 
 * @description 预算数据流集成接口，支持ETL、数据管道、流式处理等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-数据流集成"})
@RequestMapping(value = "/accountant/budget/integration/dataflow")
@Slf4j
public class BudgetIntegrationDataFlowController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationDataFlowService integrationDataFlowService;

    /**
     * 创建数据流
     */
    @Operation(summary = "创建数据流")
    @ApiOperation("创建数据流")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createDataFlow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> dataFlow = integrationDataFlowService.createDataFlow(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(dataFlow);
        } catch (ServiceException ex) {
            log.error("创建数据流失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建数据流异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行数据流
     */
    @Operation(summary = "执行数据流")
    @ApiOperation("执行数据流")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeDataFlow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> execution = integrationDataFlowService.executeDataFlow(params);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(execution);
        } catch (ServiceException ex) {
            log.error("执行数据流失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行数据流异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据转换
     */
    @Operation(summary = "数据转换")
    @ApiOperation("数据转换")
    @PostMapping("/transform")
    public MyJsonBean<Map<String, Object>> transformData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> transformation = integrationDataFlowService.transformData(params);
            result.setCode(1);
            result.setMsg("转换成功");
            result.setData(transformation);
        } catch (ServiceException ex) {
            log.error("数据转换失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据转换异常", e);
            result.setCode(0);
            result.setMsg("转换失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据清洗
     */
    @Operation(summary = "数据清洗")
    @ApiOperation("数据清洗")
    @PostMapping("/cleanse")
    public MyJsonBean<Map<String, Object>> cleanseData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> cleansing = integrationDataFlowService.cleanseData(params);
            result.setCode(1);
            result.setMsg("清洗成功");
            result.setData(cleansing);
        } catch (ServiceException ex) {
            log.error("数据清洗失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据清洗异常", e);
            result.setCode(0);
            result.setMsg("清洗失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据聚合
     */
    @Operation(summary = "数据聚合")
    @ApiOperation("数据聚合")
    @PostMapping("/aggregate")
    public MyJsonBean<Map<String, Object>> aggregateData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> aggregation = integrationDataFlowService.aggregateData(params);
            result.setCode(1);
            result.setMsg("聚合成功");
            result.setData(aggregation);
        } catch (ServiceException ex) {
            log.error("数据聚合失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据聚合异常", e);
            result.setCode(0);
            result.setMsg("聚合失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 流式处理
     */
    @Operation(summary = "流式处理")
    @ApiOperation("流式处理")
    @PostMapping("/stream/process")
    public MyJsonBean<Map<String, Object>> processStream(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> streaming = integrationDataFlowService.processStream(params);
            result.setCode(1);
            result.setMsg("处理成功");
            result.setData(streaming);
        } catch (ServiceException ex) {
            log.error("流式处理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("流式处理异常", e);
            result.setCode(0);
            result.setMsg("处理失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据流监控
     */
    @Operation(summary = "数据流监控")
    @ApiOperation("数据流监控")
    @PostMapping("/monitor")
    public MyJsonBean<Map<String, Object>> monitorDataFlow(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> monitoring = integrationDataFlowService.monitorDataFlow(params);
            result.setCode(1);
            result.setMsg("监控成功");
            result.setData(monitoring);
        } catch (ServiceException ex) {
            log.error("数据流监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据流监控异常", e);
            result.setCode(0);
            result.setMsg("监控失败：" + e.getMessage());
        }
        return result;
    }
}

