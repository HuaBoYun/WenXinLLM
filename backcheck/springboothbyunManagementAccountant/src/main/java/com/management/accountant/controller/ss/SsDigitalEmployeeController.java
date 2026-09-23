package com.management.accountant.controller.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsDigitalEmployee;
import com.management.accountant.service.ss.SsDigitalEmployeeService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数字员工控制器
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ss/digitalEmployee")
@Api(tags = "数字员工管理")
public class SsDigitalEmployeeController {

    @Autowired
    private SsDigitalEmployeeService digitalEmployeeService;

    @GetMapping("/page")
    @ApiOperation("分页查询数字员工列表")
    public MyJsonBean getDigitalEmployeePage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("数字员工名称") @RequestParam(required = false) String robotName,
            @ApiParam("数字员工编码") @RequestParam(required = false) String robotCode,
            @ApiParam("数字员工状态") @RequestParam(required = false) String robotStatus,
            @ApiParam("数字员工类型") @RequestParam(required = false) String robotType,
            @ApiParam("数字员工分类") @RequestParam(required = false) String robotCategory,
            @ApiParam("部门ID") @RequestParam(required = false) Long departmentId,
            @ApiParam("负责人ID") @RequestParam(required = false) Long ownerId) {
        try {
            Page<SsDigitalEmployee> page = new Page<>(current, size);
            Map<String, Object> params = new java.util.HashMap<>();
            params.put("robotName", robotName);
            params.put("robotCode", robotCode);
            params.put("robotStatus", robotStatus);
            params.put("robotType", robotType);
            params.put("robotCategory", robotCategory);
            params.put("departmentId", departmentId);
            params.put("ownerId", ownerId);
            params.put("tenantId", tenantId);

            IPage<SsDigitalEmployee> result = digitalEmployeeService.getDigitalEmployeePage(page, params);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询数字员工列表失败", e);
            return MyJsonBean.error("分页查询数字员工列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{robotId}")
    @ApiOperation("根据ID查询数字员工详情")
    public MyJsonBean getDigitalEmployeeById(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            SsDigitalEmployee digitalEmployee = digitalEmployeeService.getDigitalEmployeeById(robotId, tenantId);
            return MyJsonBean.success(digitalEmployee);
        } catch (Exception e) {
            log.error("根据ID查询数字员工详情失败", e);
            return MyJsonBean.error("根据ID查询数字员工详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/code/{robotCode}")
    @ApiOperation("根据编码查询数字员工")
    public MyJsonBean getDigitalEmployeeByCode(
            @ApiParam("数字员工编码") @PathVariable String robotCode,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            SsDigitalEmployee digitalEmployee = digitalEmployeeService.getDigitalEmployeeByCode(robotCode, tenantId);
            return MyJsonBean.success(digitalEmployee);
        } catch (Exception e) {
            log.error("根据编码查询数字员工失败", e);
            return MyJsonBean.error("根据编码查询数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("创建数字员工")
    public MyJsonBean createDigitalEmployee(
            @ApiParam("数字员工信息") @RequestBody SsDigitalEmployee digitalEmployee,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.createDigitalEmployee(digitalEmployee, tenantId);
            return success ? MyJsonBean.success("创建数字员工成功") : MyJsonBean.error("创建数字员工失败");
        } catch (Exception e) {
            log.error("创建数字员工失败", e);
            return MyJsonBean.error("创建数字员工失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("更新数字员工")
    public MyJsonBean updateDigitalEmployee(
            @ApiParam("数字员工信息") @RequestBody SsDigitalEmployee digitalEmployee,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.updateDigitalEmployee(digitalEmployee, tenantId);
            return success ? MyJsonBean.success("更新数字员工成功") : MyJsonBean.error("更新数字员工失败");
        } catch (Exception e) {
            log.error("更新数字员工失败", e);
            return MyJsonBean.error("更新数字员工失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{robotId}")
    @ApiOperation("删除数字员工")
    public MyJsonBean deleteDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.deleteDigitalEmployee(robotId, tenantId);
            return success ? MyJsonBean.success("删除数字员工成功") : MyJsonBean.error("删除数字员工失败");
        } catch (Exception e) {
            log.error("删除数字员工失败", e);
            return MyJsonBean.error("删除数字员工失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除数字员工")
    public MyJsonBean batchDeleteDigitalEmployee(
            @ApiParam("数字员工ID列表") @RequestBody List<Long> robotIds,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.batchDeleteDigitalEmployee(robotIds, tenantId);
            return success ? MyJsonBean.success("批量删除数字员工成功") : MyJsonBean.error("批量删除数字员工失败");
        } catch (Exception e) {
            log.error("批量删除数字员工失败", e);
            return MyJsonBean.error("批量删除数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/activate")
    @ApiOperation("激活数字员工")
    public MyJsonBean activateDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.activateDigitalEmployee(robotId, tenantId);
            return success ? MyJsonBean.success("激活数字员工成功") : MyJsonBean.error("激活数字员工失败");
        } catch (Exception e) {
            log.error("激活数字员工失败", e);
            return MyJsonBean.error("激活数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/deactivate")
    @ApiOperation("停用数字员工")
    public MyJsonBean deactivateDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("停用原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.deactivateDigitalEmployee(robotId, reason, tenantId);
            return success ? MyJsonBean.success("停用数字员工成功") : MyJsonBean.error("停用数字员工失败");
        } catch (Exception e) {
            log.error("停用数字员工失败", e);
            return MyJsonBean.error("停用数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/activate")
    @ApiOperation("批量激活数字员工")
    public MyJsonBean batchActivateDigitalEmployee(
            @ApiParam("数字员工ID列表") @RequestBody List<Long> robotIds,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.batchActivateDigitalEmployee(robotIds, tenantId);
            return success ? MyJsonBean.success("批量激活数字员工成功") : MyJsonBean.error("批量激活数字员工失败");
        } catch (Exception e) {
            log.error("批量激活数字员工失败", e);
            return MyJsonBean.error("批量激活数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/deactivate")
    @ApiOperation("批量停用数字员工")
    public MyJsonBean batchDeactivateDigitalEmployee(
            @ApiParam("数字员工ID列表") @RequestBody List<Long> robotIds,
            @ApiParam("停用原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.batchDeactivateDigitalEmployee(robotIds, reason, tenantId);
            return success ? MyJsonBean.success("批量停用数字员工成功") : MyJsonBean.error("批量停用数字员工失败");
        } catch (Exception e) {
            log.error("批量停用数字员工失败", e);
            return MyJsonBean.error("批量停用数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/maintenance/start")
    @ApiOperation("启动维护模式")
    public MyJsonBean startMaintenance(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("维护原因") @RequestParam String reason,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.startMaintenance(robotId, reason, startTime, endTime, tenantId);
            return success ? MyJsonBean.success("启动维护模式成功") : MyJsonBean.error("启动维护模式失败");
        } catch (Exception e) {
            log.error("启动维护模式失败", e);
            return MyJsonBean.error("启动维护模式失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/maintenance/end")
    @ApiOperation("结束维护模式")
    public MyJsonBean endMaintenance(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("维护结果") @RequestParam String result,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.endMaintenance(robotId, result, tenantId);
            return success ? MyJsonBean.success("结束维护模式成功") : MyJsonBean.error("结束维护模式失败");
        } catch (Exception e) {
            log.error("结束维护模式失败", e);
            return MyJsonBean.error("结束维护模式失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/deploy")
    @ApiOperation("部署数字员工")
    public MyJsonBean deployDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("部署环境") @RequestParam String deploymentEnvironment,
            @ApiParam("部署配置") @RequestBody Map<String, Object> deploymentConfig,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.deployDigitalEmployee(robotId, deploymentEnvironment, deploymentConfig, tenantId);
            return success ? MyJsonBean.success("部署数字员工成功") : MyJsonBean.error("部署数字员工失败");
        } catch (Exception e) {
            log.error("部署数字员工失败", e);
            return MyJsonBean.error("部署数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/rollback")
    @ApiOperation("回滚数字员工")
    public MyJsonBean rollbackDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("版本号") @RequestParam String version,
            @ApiParam("回滚原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.rollbackDigitalEmployee(robotId, version, reason, tenantId);
            return success ? MyJsonBean.success("回滚数字员工成功") : MyJsonBean.error("回滚数字员工失败");
        } catch (Exception e) {
            log.error("回滚数字员工失败", e);
            return MyJsonBean.error("回滚数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/upgrade")
    @ApiOperation("升级数字员工")
    public MyJsonBean upgradeDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("新版本号") @RequestParam String newVersion,
            @ApiParam("升级配置") @RequestBody Map<String, Object> upgradeConfig,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.upgradeDigitalEmployee(robotId, newVersion, upgradeConfig, tenantId);
            return success ? MyJsonBean.success("升级数字员工成功") : MyJsonBean.error("升级数字员工失败");
        } catch (Exception e) {
            log.error("升级数字员工失败", e);
            return MyJsonBean.error("升级数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/configure")
    @ApiOperation("配置数字员工")
    public MyJsonBean configureDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("配置信息") @RequestBody Map<String, Object> configuration,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.configureDigitalEmployee(robotId, configuration, tenantId);
            return success ? MyJsonBean.success("配置数字员工成功") : MyJsonBean.error("配置数字员工失败");
        } catch (Exception e) {
            log.error("配置数字员工失败", e);
            return MyJsonBean.error("配置数字员工失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/task/assign")
    @ApiOperation("分配任务给数字员工")
    public MyJsonBean assignTask(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("任务ID") @RequestParam Long taskId,
            @ApiParam("任务类型") @RequestParam String taskType,
            @ApiParam("任务配置") @RequestBody Map<String, Object> taskConfig,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.assignTask(robotId, taskId, taskType, taskConfig, tenantId);
            return success ? MyJsonBean.success("分配任务成功") : MyJsonBean.error("分配任务失败");
        } catch (Exception e) {
            log.error("分配任务失败", e);
            return MyJsonBean.error("分配任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/task/batch/assign")
    @ApiOperation("批量分配任务")
    public MyJsonBean batchAssignTask(
            @ApiParam("数字员工ID列表") @RequestBody List<Long> robotIds,
            @ApiParam("任务ID") @RequestParam Long taskId,
            @ApiParam("任务类型") @RequestParam String taskType,
            @ApiParam("任务配置") @RequestBody Map<String, Object> taskConfig,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.batchAssignTask(robotIds, taskId, taskType, taskConfig, tenantId);
            return success ? MyJsonBean.success("批量分配任务成功") : MyJsonBean.error("批量分配任务失败");
        } catch (Exception e) {
            log.error("批量分配任务失败", e);
            return MyJsonBean.error("批量分配任务失败: " + e.getMessage());
        }
    }

    @GetMapping("/{robotId}/monitor")
    @ApiOperation("监控数字员工状态")
    public MyJsonBean monitorDigitalEmployee(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> monitorData = digitalEmployeeService.monitorDigitalEmployee(robotId, tenantId);
            return MyJsonBean.success(monitorData);
        } catch (Exception e) {
            log.error("监控数字员工状态失败", e);
            return MyJsonBean.error("监控数字员工状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/healthCheck")
    @ApiOperation("健康检查")
    public MyJsonBean performHealthCheck(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> healthResult = digitalEmployeeService.performHealthCheck(robotId, tenantId);
            return MyJsonBean.success(healthResult);
        } catch (Exception e) {
            log.error("健康检查失败", e);
            return MyJsonBean.error("健康检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/{robotId}/performance/evaluate")
    @ApiOperation("性能评估")
    public MyJsonBean evaluatePerformance(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> performance = digitalEmployeeService.evaluatePerformance(robotId, startTime, endTime, tenantId);
            return MyJsonBean.success(performance);
        } catch (Exception e) {
            log.error("性能评估失败", e);
            return MyJsonBean.error("性能评估失败: " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}/priority/set")
    @ApiOperation("设置优先级")
    public MyJsonBean setPriority(
            @ApiParam("数字员工ID") @PathVariable Long robotId,
            @ApiParam("优先级") @RequestParam Integer priority,
            @ApiParam("设置原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = digitalEmployeeService.setPriority(robotId, priority, reason, tenantId);
            return success ? MyJsonBean.success("设置优先级成功") : MyJsonBean.error("设置优先级失败");
        } catch (Exception e) {
            log.error("设置优先级失败", e);
            return MyJsonBean.error("设置优先级失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("查询统计信息")
    public MyJsonBean getStatistics(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> statistics = digitalEmployeeService.getStatistics(startTime, endTime, tenantId);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询统计信息失败", e);
            return MyJsonBean.error("查询统计信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/status")
    @ApiOperation("查询状态分布")
    public MyJsonBean getStatusDistribution(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = digitalEmployeeService.getStatusDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询状态分布失败", e);
            return MyJsonBean.error("查询状态分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/distribution/type")
    @ApiOperation("查询类型分布")
    public MyJsonBean getTypeDistribution(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = digitalEmployeeService.getTypeDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询类型分布失败", e);
            return MyJsonBean.error("查询类型分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/ranking")
    @ApiOperation("查询排行榜")
    public MyJsonBean getRanking(
            @ApiParam("排行类型") @RequestParam String rankingType,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> ranking = digitalEmployeeService.getRanking(rankingType, limit, tenantId);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询排行榜失败", e);
            return MyJsonBean.error("查询排行榜失败: " + e.getMessage());
        }
    }

    @GetMapping("/alerts")
    @ApiOperation("查询告警信息")
    public MyJsonBean getAlerts(
            @ApiParam("告警类型") @RequestParam(required = false) String alertType,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> alerts = digitalEmployeeService.getAlerts(alertType, tenantId);
            return MyJsonBean.success(alerts);
        } catch (Exception e) {
            log.error("查询告警信息失败", e);
            return MyJsonBean.error("查询告警信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    @ApiOperation("查询待处理事项")
    public MyJsonBean getPendingItems(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> items = digitalEmployeeService.getPendingItems(tenantId);
            return MyJsonBean.success(items);
        } catch (Exception e) {
            log.error("查询待处理事项失败", e);
            return MyJsonBean.error("查询待处理事项失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/code")
    @ApiOperation("检查编码是否存在")
    public MyJsonBean checkCodeExists(
            @ApiParam("数字员工编码") @RequestParam String robotCode,
            @ApiParam("数字员工ID") @RequestParam(required = false) Long robotId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean exists = digitalEmployeeService.checkCodeExists(robotCode, robotId, tenantId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查编码是否存在失败", e);
            return MyJsonBean.error("检查编码是否存在失败: " + e.getMessage());
        }
    }

}