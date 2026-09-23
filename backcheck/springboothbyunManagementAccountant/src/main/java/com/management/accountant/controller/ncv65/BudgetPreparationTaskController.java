package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ncv65.BudgetPreparationTask;
import com.management.accountant.service.ncv65.IBudgetPreparationTaskService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算编制任务控制器
 * 
 * @description 预算编制任务管理API接口，支持编制任务的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算编制任务管理")
@RestController
@RequestMapping("/budget/preparation/task")
@Validated
public class BudgetPreparationTaskController {

    @Resource
    private IBudgetPreparationTaskService budgetPreparationTaskService;

    /**
     * 创建编制任务
     */
    @ApiOperation("创建编制任务")
    @PostMapping
    public MyJsonBean<Boolean> createPreparationTask(@Valid @RequestBody BudgetPreparationTask task) {
        try {
            boolean result = budgetPreparationTaskService.createPreparationTask(task);
            return MyJsonBean.successData(result,  "创建编制任务成功");
        } catch (Exception e) {
            log.error("创建编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("创建编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 更新编制任务
     */
    @ApiOperation("更新编制任务")
    @PutMapping("/{id}")
    public MyJsonBean<Boolean> updatePreparationTask(
            @ApiParam("任务ID") @PathVariable String id,
            @Valid @RequestBody BudgetPreparationTask task) {
        try {
            task.setId(id);
            boolean result = budgetPreparationTaskService.updatePreparationTask(task);
            return MyJsonBean.successData(result,  "更新编制任务成功");
        } catch (Exception e) {
            log.error("更新编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除编制任务
     */
    @ApiOperation("删除编制任务")
    @DeleteMapping("/{id}")
    public MyJsonBean<Boolean> deletePreparationTask(@ApiParam("任务ID") @PathVariable String id) {
        try {
            boolean result = budgetPreparationTaskService.deletePreparationTask(id);
            return MyJsonBean.successData(result,  "删除编制任务成功");
        } catch (Exception e) {
            log.error("删除编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("删除编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除编制任务
     */
    @ApiOperation("批量删除编制任务")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeletePreparationTasks(@RequestBody List<String> ids) {
        try {
            boolean result = budgetPreparationTaskService.batchDeletePreparationTasks(ids);
            return MyJsonBean.successData(result,  "批量删除编制任务成功");
        } catch (Exception e) {
            log.error("批量删除编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量删除编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询编制任务详情
     */
    @ApiOperation("查询编制任务详情")
    @GetMapping("/{id}")
    public MyJsonBean<BudgetPreparationTask> getPreparationTask(@ApiParam("任务ID") @PathVariable String id) {
        try {
            BudgetPreparationTask task = budgetPreparationTaskService.getPreparationTaskById(id);
            return MyJsonBean.successData(task,  "查询编制任务成功");
        } catch (Exception e) {
            log.error("查询编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询编制任务
     */
    @ApiOperation("根据编码查询编制任务")
    @GetMapping("/code/{taskCode}")
    public MyJsonBean<BudgetPreparationTask> getPreparationTaskByCode(@ApiParam("任务编码") @PathVariable String taskCode) {
        try {
            BudgetPreparationTask task = budgetPreparationTaskService.getPreparationTaskByCode(taskCode);
            return MyJsonBean.successData(task,  "查询编制任务成功");
        } catch (Exception e) {
            log.error("根据编码查询编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据编码查询编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询编制任务
     */
    @ApiOperation("分页查询编制任务")
    @PostMapping("/page")
    public MyJsonBean<IPage<BudgetPreparationTask>> getPreparationTaskPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @RequestBody(required = false) Map<String, Object> params) {
        try {
            IPage<BudgetPreparationTask> page = budgetPreparationTaskService.getPreparationTaskPage(current, size, params);
            return MyJsonBean.successData(page,  "查询编制任务成功");
        } catch (Exception e) {
            log.error("分页查询编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分页查询编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务类型查询编制任务
     */
    @ApiOperation("根据任务类型查询编制任务")
    @GetMapping("/type/{taskType}")
    public MyJsonBean<List<BudgetPreparationTask>> getPreparationTasksByType(@ApiParam("任务类型") @PathVariable String taskType) {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getPreparationTasksByType(taskType);
            return MyJsonBean.successData(tasks,  "查询编制任务成功");
        } catch (Exception e) {
            log.error("根据任务类型查询编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据任务类型查询编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算年度查询编制任务
     */
    @ApiOperation("根据预算年度查询编制任务")
    @GetMapping("/year/{fiscalYear}")
    public MyJsonBean<List<BudgetPreparationTask>> getPreparationTasksByFiscalYear(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getPreparationTasksByFiscalYear(fiscalYear);
            return MyJsonBean.successData(tasks,  "查询编制任务成功");
        } catch (Exception e) {
            log.error("根据预算年度查询编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据预算年度查询编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务状态查询编制任务
     */
    @ApiOperation("根据任务状态查询编制任务")
    @GetMapping("/status/{taskStatus}")
    public MyJsonBean<List<BudgetPreparationTask>> getPreparationTasksByStatus(@ApiParam("任务状态") @PathVariable String taskStatus) {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getPreparationTasksByStatus(taskStatus);
            return MyJsonBean.successData(tasks,  "查询编制任务成功");
        } catch (Exception e) {
            log.error("根据任务状态查询编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据任务状态查询编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询我的编制任务
     */
    @ApiOperation("查询我的编制任务")
    @GetMapping("/my")
    public MyJsonBean<List<BudgetPreparationTask>> getMyPreparationTasks(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getMyPreparationTasks(userId);
            return MyJsonBean.successData(tasks,  "查询我的编制任务成功");
        } catch (Exception e) {
            log.error("查询我的编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询我的编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询待处理的编制任务
     */
    @ApiOperation("查询待处理的编制任务")
    @GetMapping("/pending")
    public MyJsonBean<List<BudgetPreparationTask>> getPendingPreparationTasks(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getPendingPreparationTasks(userId);
            return MyJsonBean.successData(tasks,  "查询待处理编制任务成功");
        } catch (Exception e) {
            log.error("查询待处理编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询待处理编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询即将到期的编制任务
     */
    @ApiOperation("查询即将到期的编制任务")
    @GetMapping("/expiring")
    public MyJsonBean<List<BudgetPreparationTask>> getExpiringPreparationTasks(@ApiParam("天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getExpiringPreparationTasks(days);
            return MyJsonBean.successData(tasks,  "查询即将到期编制任务成功");
        } catch (Exception e) {
            log.error("查询即将到期编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询即将到期编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询已过期的编制任务
     */
    @ApiOperation("查询已过期的编制任务")
    @GetMapping("/expired")
    public MyJsonBean<List<BudgetPreparationTask>> getExpiredPreparationTasks() {
        try {
            List<BudgetPreparationTask> tasks = budgetPreparationTaskService.getExpiredPreparationTasks();
            return MyJsonBean.successData(tasks,  "查询已过期编制任务成功");
        } catch (Exception e) {
            log.error("查询已过期编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询已过期编制任务失败：" + e.getMessage());
        }
    }

    // ==================== 业务操作接口 ====================

    /**
     * 启动编制任务
     */
    @ApiOperation("启动编制任务")
    @PostMapping("/{id}/start")
    public MyJsonBean<Boolean> startPreparationTask(@ApiParam("任务ID") @PathVariable String id) {
        try {
            boolean result = budgetPreparationTaskService.startPreparationTask(id);
            return MyJsonBean.successData(result,  "启动编制任务成功");
        } catch (Exception e) {
            log.error("启动编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("启动编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 完成编制任务
     */
    @ApiOperation("完成编制任务")
    @PostMapping("/{id}/complete")
    public MyJsonBean<Boolean> completePreparationTask(@ApiParam("任务ID") @PathVariable String id) {
        try {
            boolean result = budgetPreparationTaskService.completePreparationTask(id);
            return MyJsonBean.successData(result,  "完成编制任务成功");
        } catch (Exception e) {
            log.error("完成编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("完成编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 取消编制任务
     */
    @ApiOperation("取消编制任务")
    @PostMapping("/{id}/cancel")
    public MyJsonBean<Boolean> cancelPreparationTask(@ApiParam("任务ID") @PathVariable String id) {
        try {
            boolean result = budgetPreparationTaskService.cancelPreparationTask(id);
            return MyJsonBean.successData(result,  "取消编制任务成功");
        } catch (Exception e) {
            log.error("取消编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("取消编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 批量启动编制任务
     */
    @ApiOperation("批量启动编制任务")
    @PostMapping("/batch/start")
    public MyJsonBean<Integer> batchStartPreparationTasks(@RequestBody List<String> taskIds) {
        try {
            int result = budgetPreparationTaskService.batchStartPreparationTasks(taskIds);
            return MyJsonBean.successData(result,  "批量启动编制任务成功");
        } catch (Exception e) {
            log.error("批量启动编制任务失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量启动编制任务失败：" + e.getMessage());
        }
    }

    /**
     * 获取编制任务统计信息
     */
    @ApiOperation("获取编制任务统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getPreparationTaskStatistics() {
        try {
            Map<String, Object> statistics = budgetPreparationTaskService.getPreparationTaskStatistics();
            return MyJsonBean.successData(statistics,  "获取编制任务统计信息成功");
        } catch (Exception e) {
            log.error("获取编制任务统计信息失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取编制任务统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 按状态统计编制任务数量
     */
    @ApiOperation("按状态统计编制任务数量")
    @GetMapping("/statistics/status")
    public MyJsonBean<List<Map<String, Object>>> getPreparationTaskCountByStatus() {
        try {
            List<Map<String, Object>> statistics = budgetPreparationTaskService.getPreparationTaskCountByStatus();
            return MyJsonBean.successData(statistics,  "按状态统计编制任务数量成功");
        } catch (Exception e) {
            log.error("按状态统计编制任务数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按状态统计编制任务数量失败：" + e.getMessage());
        }
    }

    /**
     * 检查任务编码是否存在
     */
    @ApiOperation("检查任务编码是否存在")
    @GetMapping("/check-code")
    public MyJsonBean<Boolean> checkTaskCodeExists(
            @ApiParam("任务编码") @RequestParam String taskCode,
            @ApiParam("排除的任务ID") @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = budgetPreparationTaskService.checkTaskCodeExists(taskCode, excludeId);
            return MyJsonBean.successData(exists,  "检查任务编码成功");
        } catch (Exception e) {
            log.error("检查任务编码失败：{}", e.getMessage(), e);
            return MyJsonBean.error("检查任务编码失败：" + e.getMessage());
        }
    }
}
