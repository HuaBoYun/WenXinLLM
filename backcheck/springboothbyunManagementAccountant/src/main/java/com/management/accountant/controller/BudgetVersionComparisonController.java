package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetVersionComparisonService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算版本对比Controller
 * 
 * @description 预算版本对比接口，支持版本对比、差异分析、变更追踪、历史查询、对比报告
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-版本对比"})
@RequestMapping(value = "/accountant/budget/version")
@Slf4j
public class BudgetVersionComparisonController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetVersionComparisonService versionComparisonService;

    /**
     * 版本对比
     */
    @ApiOperation("版本对比")
    @PostMapping("/compare")
    public MyJsonBean<Map<String, Object>> compareVersions(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> comparison = versionComparisonService.compareVersions(params);
            result.setCode(1);
            result.setMsg("对比成功");
            result.setData(comparison);
        } catch (ServiceException ex) {
            log.error("版本对比失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("版本对比异常", e);
            result.setCode(0);
            result.setMsg("对比失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 差异分析
     */
    @ApiOperation("差异分析")
    @PostMapping("/difference/analyze")
    public MyJsonBean<Map<String, Object>> analyzeDifference(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = versionComparisonService.analyzeDifference(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysis);
        } catch (ServiceException ex) {
            log.error("差异分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("差异分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 变更追踪
     */
    @ApiOperation("变更追踪")
    @PostMapping("/change/track")
    public MyJsonBean<Map<String, Object>> trackChanges(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> tracking = versionComparisonService.trackChanges(params);
            result.setCode(1);
            result.setMsg("追踪成功");
            result.setData(tracking);
        } catch (ServiceException ex) {
            log.error("变更追踪失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("变更追踪异常", e);
            result.setCode(0);
            result.setMsg("追踪失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 历史查询
     */
    @ApiOperation("历史查询")
    @PostMapping("/history/query")
    public MyJsonBean<Map<String, Object>> queryHistory(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> history = versionComparisonService.queryHistory(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (ServiceException ex) {
            log.error("历史查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("历史查询异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成对比报告
     */
    @ApiOperation("生成对比报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generateComparisonReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = versionComparisonService.generateComparisonReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("生成对比报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成对比报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取版本对比日志
     */
    @ApiOperation("获取版本对比日志")
    @GetMapping("/logs/{comparisonId}")
    public MyJsonBean<Map<String, Object>> getComparisonLogs(@PathVariable String comparisonId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> logs = versionComparisonService.getComparisonLogs(comparisonId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logs);
        } catch (Exception e) {
            log.error("获取版本对比日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出版本对比结果
     */
    @ApiOperation("导出版本对比结果")
    @GetMapping("/export/{comparisonId}")
    public MyJsonBean<Map<String, Object>> exportComparison(@PathVariable String comparisonId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = versionComparisonService.exportComparison(comparisonId);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (Exception e) {
            log.error("导出版本对比结果异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除版本对比
     */
    @ApiOperation("删除版本对比")
    @DeleteMapping("/delete/{comparisonId}")
    public MyJsonBean<Void> deleteComparison(@PathVariable String comparisonId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            versionComparisonService.deleteComparison(comparisonId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除版本对比异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制版本对比
     */
    @ApiOperation("复制版本对比")
    @PostMapping("/copy/{comparisonId}")
    public MyJsonBean<Map<String, Object>> copyComparison(@PathVariable String comparisonId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> copyResult = versionComparisonService.copyComparison(comparisonId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copyResult);
        } catch (Exception e) {
            log.error("复制版本对比异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 合并版本
     */
    @ApiOperation("合并版本")
    @PostMapping("/merge")
    public MyJsonBean<Map<String, Object>> mergeVersions(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> mergeResult = versionComparisonService.mergeVersions(params);
            result.setCode(1);
            result.setMsg("合并成功");
            result.setData(mergeResult);
        } catch (Exception e) {
            log.error("合并版本异常", e);
            result.setCode(0);
            result.setMsg("合并失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 回滚版本
     */
    @ApiOperation("回滚版本")
    @PostMapping("/rollback")
    public MyJsonBean<Map<String, Object>> rollbackVersion(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> rollbackResult = versionComparisonService.rollbackVersion(params);
            result.setCode(1);
            result.setMsg("回滚成功");
            result.setData(rollbackResult);
        } catch (Exception e) {
            log.error("回滚版本异常", e);
            result.setCode(0);
            result.setMsg("回滚失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取历史版本列表
     */
    @ApiOperation("获取历史版本列表")
    @PostMapping("/history")
    public MyJsonBean<Map<String, Object>> getHistoryVersions(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> history = versionComparisonService.getHistoryVersions(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取历史版本列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取版本对比列表
     */
    @ApiOperation("获取版本对比列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> getComparisonList(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> list = versionComparisonService.getComparisonList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取版本对比列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取版本对比统计数据
     */
    @ApiOperation("获取版本对比统计数据")
    @PostMapping("/stats")
    public MyJsonBean<Map<String, Object>> getComparisonStats(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = versionComparisonService.getComparisonStats(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取版本对比统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取可用版本列表
     */
    @ApiOperation("获取可用版本列表")
    @GetMapping("/available")
    public MyJsonBean<Map<String, Object>> getAvailableVersions() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> versions = versionComparisonService.getAvailableVersions();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(versions);
        } catch (Exception e) {
            log.error("获取可用版本列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建版本对比
     */
    @ApiOperation("创建版本对比")
    @PostMapping("/create")
    public MyJsonBean<Map<String, Object>> createComparison(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> comparison = versionComparisonService.createComparison(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(comparison);
        } catch (Exception e) {
            log.error("创建版本对比异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 重新对比
     */
    @ApiOperation("重新对比")
    @PostMapping("/recompare/{comparisonId}")
    public MyJsonBean<Map<String, Object>> recompare(@PathVariable String comparisonId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> recompareResult = versionComparisonService.recompare(comparisonId);
            result.setCode(1);
            result.setMsg("重新对比成功");
            result.setData(recompareResult);
        } catch (Exception e) {
            log.error("重新对比异常", e);
            result.setCode(0);
            result.setMsg("重新对比失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取对比差异
     */
    @ApiOperation("获取对比差异")
    @GetMapping("/differences/{comparisonId}")
    public MyJsonBean<Map<String, Object>> getDifferences(@PathVariable String comparisonId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> differences = versionComparisonService.getDifferences(comparisonId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(differences);
        } catch (Exception e) {
            log.error("获取对比差异异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

