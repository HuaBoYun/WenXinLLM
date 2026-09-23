package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntelligentRecommendationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 预算智能推荐Controller
 * 
 * @description 预算智能推荐接口，支持AI推荐、历史数据分析、智能建议等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-智能推荐"})
@RequestMapping(value = "/accountant/budget/intelligent/recommendation")
@Slf4j
public class BudgetIntelligentRecommendationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntelligentRecommendationService recommendationService;

    /**
     * 获取预算推荐
     */
    @Operation(summary = "获取预算推荐")
    @ApiOperation("获取预算推荐")
    @PostMapping("/recommend")
    public MyJsonBean<Map<String, Object>> getRecommendation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> recommendation = recommendationService.getRecommendation(params);
            result.setCode(1);
            result.setMsg("推荐成功");
            result.setData(recommendation);
        } catch (ServiceException ex) {
            log.error("获取预算推荐失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取预算推荐异常", e);
            result.setCode(0);
            result.setMsg("推荐失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分析历史数据
     */
    @Operation(summary = "分析历史数据")
    @ApiOperation("分析历史数据")
    @PostMapping("/analyze/history")
    public MyJsonBean<Map<String, Object>> analyzeHistory(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = recommendationService.analyzeHistory(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysis);
        } catch (ServiceException ex) {
            log.error("分析历史数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("分析历史数据异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成智能建议
     */
    @Operation(summary = "生成智能建议")
    @ApiOperation("生成智能建议")
    @PostMapping("/suggest")
    public MyJsonBean<Map<String, Object>> generateSuggestions(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> suggestions = recommendationService.generateSuggestions(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(suggestions);
        } catch (ServiceException ex) {
            log.error("生成智能建议失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成智能建议异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 预测预算趋势
     */
    @Operation(summary = "预测预算趋势")
    @ApiOperation("预测预算趋势")
    @PostMapping("/predict/trend")
    public MyJsonBean<Map<String, Object>> predictTrend(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> prediction = recommendationService.predictTrend(params);
            result.setCode(1);
            result.setMsg("预测成功");
            result.setData(prediction);
        } catch (ServiceException ex) {
            log.error("预测预算趋势失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("预测预算趋势异常", e);
            result.setCode(0);
            result.setMsg("预测失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 优化预算分配
     */
    @Operation(summary = "优化预算分配")
    @ApiOperation("优化预算分配")
    @PostMapping("/optimize/allocation")
    public MyJsonBean<Map<String, Object>> optimizeAllocation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimization = recommendationService.optimizeAllocation(params);
            result.setCode(1);
            result.setMsg("优化成功");
            result.setData(optimization);
        } catch (ServiceException ex) {
            log.error("优化预算分配失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("优化预算分配异常", e);
            result.setCode(0);
            result.setMsg("优化失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 应用推荐
     */
    @Operation(summary = "应用推荐")
    @ApiOperation("应用推荐")
    @PostMapping("/apply")
    public MyJsonBean<Map<String, Object>> applyRecommendation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> applyResult = recommendationService.applyRecommendation(params);
            result.setCode(1);
            result.setMsg("应用成功");
            result.setData(applyResult);
        } catch (ServiceException ex) {
            log.error("应用推荐失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("应用推荐异常", e);
            result.setCode(0);
            result.setMsg("应用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除推荐
     */
    @Operation(summary = "删除推荐")
    @ApiOperation("删除推荐")
    @DeleteMapping("/delete/{recommendationId}")
    public MyJsonBean<Void> deleteRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            recommendationService.deleteRecommendation(recommendationId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除推荐失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除推荐异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分享推荐
     */
    @Operation(summary = "分享推荐")
    @ApiOperation("分享推荐")
    @PostMapping("/share")
    public MyJsonBean<Map<String, Object>> shareRecommendation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> shareResult = recommendationService.shareRecommendation(params);
            result.setCode(1);
            result.setMsg("分享成功");
            result.setData(shareResult);
        } catch (ServiceException ex) {
            log.error("分享推荐失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("分享推荐异常", e);
            result.setCode(0);
            result.setMsg("分享失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取推荐反馈
     */
    @Operation(summary = "获取推荐反馈")
    @ApiOperation("获取推荐反馈")
    @PostMapping("/feedback")
    public MyJsonBean<Map<String, Object>> getRecommendationFeedback(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> feedback = recommendationService.getRecommendationFeedback(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(feedback);
        } catch (Exception e) {
            log.error("获取推荐反馈异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取智能推荐列表（支持 form-data 和 JSON 两种传参方式）
     */
    @Operation(summary = "获取智能推荐列表")
    @ApiOperation("获取智能推荐列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> getRecommendationList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "recommendationType", required = false) String recommendationType,
            @RequestParam(value = "status", required = false) String status) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            if (keyword != null) params.put("keyword", keyword);
            if (recommendationType != null) params.put("type", recommendationType);
            if (status != null) params.put("status", status);
            Map<String, Object> list = recommendationService.getRecommendationList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取智能推荐列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取智能推荐统计数据
     */
    @Operation(summary = "获取智能推荐统计数据")
    @ApiOperation("获取智能推荐统计数据")
    @PostMapping("/stats")
    public MyJsonBean<Map<String, Object>> getRecommendationStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = recommendationService.getRecommendationStats(null);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取智能推荐统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成智能推荐
     */
    @Operation(summary = "生成智能推荐")
    @ApiOperation("生成智能推荐")
    @PostMapping("/generate")
    public MyJsonBean<Map<String, Object>> generateRecommendation(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> generated = recommendationService.generateRecommendation(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(generated);
        } catch (Exception e) {
            log.error("生成智能推荐异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 采纳推荐
     */
    @Operation(summary = "采纳推荐")
    @ApiOperation("采纳推荐")
    @PostMapping("/accept/{recommendationId}")
    public MyJsonBean<Map<String, Object>> acceptRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> acceptResult = recommendationService.acceptRecommendation(recommendationId);
            result.setCode(1);
            result.setMsg("采纳成功");
            result.setData(acceptResult);
        } catch (Exception e) {
            log.error("采纳推荐异常", e);
            result.setCode(0);
            result.setMsg("采纳失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 拒绝推荐
     */
    @Operation(summary = "拒绝推荐")
    @ApiOperation("拒绝推荐")
    @PostMapping("/reject/{recommendationId}")
    public MyJsonBean<Map<String, Object>> rejectRecommendation(@PathVariable String recommendationId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> rejectResult = recommendationService.rejectRecommendation(recommendationId);
            result.setCode(1);
            result.setMsg("拒绝成功");
            result.setData(rejectResult);
        } catch (Exception e) {
            log.error("拒绝推荐异常", e);
            result.setCode(0);
            result.setMsg("拒绝失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新推荐
     */
    @Operation(summary = "更新推荐")
    @ApiOperation("更新推荐")
    @PostMapping("/update")
    public MyJsonBean<Map<String, Object>> updateRecommendation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> updateResult = recommendationService.updateRecommendation(params);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updateResult);
        } catch (Exception e) {
            log.error("更新推荐异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取推荐反馈（按ID）
     */
    @Operation(summary = "获取推荐反馈（按ID）")
    @ApiOperation("获取推荐反馈（按ID）")
    @GetMapping("/feedback/{recommendationId}")
    public MyJsonBean<Map<String, Object>> getRecommendationFeedbackById(@PathVariable String recommendationId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> feedback = recommendationService.getRecommendationFeedbackById(recommendationId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(feedback);
        } catch (Exception e) {
            log.error("获取推荐反馈异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 训练模型
     */
    @Operation(summary = "训练模型")
    @ApiOperation("训练模型")
    @PostMapping("/train/model")
    public MyJsonBean<Map<String, Object>> trainModel(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> trainResult = recommendationService.trainModel(params);
            result.setCode(1);
            result.setMsg("训练成功");
            result.setData(trainResult);
        } catch (Exception e) {
            log.error("训练模型异常", e);
            result.setCode(0);
            result.setMsg("训练失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 推荐分析
     */
    @Operation(summary = "推荐分析")
    @ApiOperation("推荐分析")
    @PostMapping("/analysis")
    public MyJsonBean<Map<String, Object>> getRecommendationAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = recommendationService.getRecommendationAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("推荐分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }
}

