package com.financial.sharing.controller;

import com.financial.sharing.service.BalanceRefreshService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.BalanceRefreshParam;
import com.financial.sharing.vo.result.BalanceRefreshResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 余额刷新控制器
 *
 * @author system
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/general-ledger")
@CrossOrigin
@Api(tags = "余额刷新管理")
public class BalanceRefreshController {

    @Autowired
    private BalanceRefreshService balanceRefreshService;

    @PostMapping("/balance-refresh/execute")
    @ApiOperation("执行余额刷新")
    public MyJsonBean<BalanceRefreshResult> executeRefresh(@RequestBody BalanceRefreshParam param) {
        try {
            BalanceRefreshResult result = balanceRefreshService.refreshBalance(param);
            return MyJsonBean.successData("刷新任务已创建", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("执行刷新失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/full")
    @ApiOperation("全量刷新余额")
    public MyJsonBean<String> fullRefresh(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period) {
        try {
            balanceRefreshService.fullRefresh(bookId, period);
            return MyJsonBean.successData("全量刷新已启动");
        } catch (Exception e) {
            return MyJsonBean.errorData("全量刷新失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/incremental")
    @ApiOperation("增量刷新余额")
    public MyJsonBean<String> incrementalRefresh(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period,
            @ApiParam("科目ID列表") @RequestParam List<Long> subjectIds) {
        try {
            balanceRefreshService.incrementalRefresh(bookId, period, subjectIds);
            return MyJsonBean.successData("增量刷新已启动");
        } catch (Exception e) {
            return MyJsonBean.errorData("增量刷新失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/smart")
    @ApiOperation("智能刷新余额")
    public MyJsonBean<String> smartRefresh(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period) {
        try {
            balanceRefreshService.smartRefresh(bookId, period);
            return MyJsonBean.successData("智能刷新已启动");
        } catch (Exception e) {
            return MyJsonBean.errorData("智能刷新失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/progress/{taskId}")
    @ApiOperation("查询刷新进度")
    public MyJsonBean getRefreshProgress(@ApiParam("任务ID") @PathVariable String taskId) {
        try {
            Object progress = balanceRefreshService.getRefreshProgress(taskId);
            return MyJsonBean.successData(progress);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/cancel/{taskId}")
    @ApiOperation("取消刷新任务")
    public MyJsonBean cancelRefresh(@ApiParam("任务ID") @PathVariable String taskId) {
        try {
            boolean result = balanceRefreshService.cancelRefresh(taskId);
            if (result) {
                return MyJsonBean.successData("任务已取消");
            } else {
                return MyJsonBean.errorData("取消失败，任务可能已完成或不存在");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("取消任务失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/history")
    @ApiOperation("获取刷新历史")
    public MyJsonBean<List<BalanceRefreshResult>> getRefreshHistory(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("会计期间") @RequestParam(required = false) String period) {
        try {
            List<BalanceRefreshResult> history = balanceRefreshService.getRefreshHistory(bookId, tenantId, period);
            return MyJsonBean.successData(history);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询历史失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/subjects")
    @ApiOperation("获取需要刷新的科目列表")
    public MyJsonBean<List<Long>> getSubjectsToRefresh(@RequestBody BalanceRefreshParam param) {
        try {
            List<Long> subjects = balanceRefreshService.getSubjectsToRefresh(param);
            return MyJsonBean.successData(subjects);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取科目列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/validate")
    @ApiOperation("验证余额数据")
    public MyJsonBean<Map<String, Object>> validateBalanceData(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period) {
        try {
            Map<String, Object> result = balanceRefreshService.validateBalanceData(bookId, period);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("验证失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/statistics")
    @ApiOperation("获取刷新统计信息")
    public MyJsonBean<Map<String, Object>> getRefreshStatistics(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period) {
        try {
            Map<String, Object> statistics = balanceRefreshService.getRefreshStatistics(bookId, period);
            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取统计失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/reset")
    @ApiOperation("重置余额数据")
    public MyJsonBean<String> resetBalanceData(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period) {
        try {
            boolean result = balanceRefreshService.resetBalanceData(bookId, period);
            if (result) {
                return MyJsonBean.successData("重置成功");
            } else {
                return MyJsonBean.errorData("重置失败");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("重置失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/repair")
    @ApiOperation("修复余额数据")
    public MyJsonBean<Map<String, Object>> repairBalanceData(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period,
            @ApiParam("科目ID列表") @RequestParam List<Long> subjectIds) {
        try {
            Map<String, Object> result = balanceRefreshService.repairBalanceData(bookId, period, subjectIds);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("修复失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/recommendations")
    @ApiOperation("获取刷新建议")
    public MyJsonBean<Map<String, Object>> getRefreshRecommendations(
            @ApiParam("账簿ID") @RequestParam Long bookId,
            @ApiParam("会计期间") @RequestParam String period) {
        try {
            Map<String, Object> recommendations = balanceRefreshService.getRefreshRecommendations(bookId, period);
            return MyJsonBean.successData(recommendations);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取建议失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/config")
    @ApiOperation("获取刷新配置")
    public MyJsonBean<Map<String, Object>> getRefreshConfig() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("refreshTypes", Arrays.asList("full", "incremental", "smart"));
            config.put("refreshModes", Arrays.asList("realtime", "batch", "scheduled"));
            config.put("defaultBatchSize", 500);
            config.put("maxRetryCount", 3);
            config.put("defaultTimeout", 60);
            config.put("supportedFormats", Arrays.asList("excel", "csv", "json"));
            return MyJsonBean.successData(config);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/balance-refresh/preview")
    @ApiOperation("预览刷新影响")
    public MyJsonBean<Map<String, Object>> previewRefresh(@RequestBody BalanceRefreshParam param) {
        try {
            List<Long> subjects = balanceRefreshService.getSubjectsToRefresh(param);

            Map<String, Object> preview = new HashMap<>();
            preview.put("subjectCount", subjects.size());
            preview.put("estimatedDuration", subjects.size() * 0.1); // 假设每个科目需要0.1秒
            preview.put("estimatedCost", subjects.size() * 0.01); // 模拟成本
            preview.put("affectedSubjects", subjects.subList(0, Math.min(10, subjects.size()))); // 显示前10个科目

            return MyJsonBean.successData(preview);
        } catch (Exception e) {
            return MyJsonBean.errorData("预览失败: " + e.getMessage());
        }
    }

    @GetMapping("/balance-refresh/health")
    @ApiOperation("检查刷新服务健康状态")
    public MyJsonBean<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("activeTasks", 5); // 模拟活跃任务数
            health.put("maxConcurrentTasks", 10);
            health.put("cpuUsage", 45.2);
            health.put("memoryUsage", 1024.5);
            health.put("lastCheckTime", System.currentTimeMillis());
            return MyJsonBean.successData(health);
        } catch (Exception e) {
            return MyJsonBean.errorData("健康检查失败: " + e.getMessage());
        }
    }
}