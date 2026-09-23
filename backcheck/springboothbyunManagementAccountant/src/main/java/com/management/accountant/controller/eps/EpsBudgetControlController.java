package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetControl;
import com.management.accountant.service.eps.EpsBudgetControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算控制管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算控制管理")
@RestController
@RequestMapping("/eps/budget-control")
@Validated
public class EpsBudgetControlController {

    @Autowired
    private EpsBudgetControlService budgetControlService;

    /**
     * 分页查询预算控制规则
     */
    @ApiOperation("分页查询预算控制规则")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetControl>> queryBudgetControlPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("版本ID") @RequestParam(required = false) Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("控制类型") @RequestParam(required = false) String controlType,
            @ApiParam("控制状态") @RequestParam(required = false) String controlStatus) {
        try {
            IPage<EpsBudgetControl> result = budgetControlService.queryBudgetControlPage(
                    current, size, versionId, organizationId, subjectId, controlType, controlStatus);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询预算控制规则失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算控制规则
     */
    @ApiOperation("创建预算控制规则")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetControl(
            @ApiParam("预算控制规则") @RequestBody @Valid EpsBudgetControl budgetControl) {
        try {
            boolean result = budgetControlService.createBudgetControl(budgetControl);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算控制规则失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算控制规则
     */
    @ApiOperation("更新预算控制规则")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetControl(
            @ApiParam("预算控制规则") @RequestBody @Valid EpsBudgetControl budgetControl) {
        try {
            boolean result = budgetControlService.updateBudgetControl(budgetControl);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算控制规则失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算控制规则
     */
    @ApiOperation("删除预算控制规则")
    @DeleteMapping("/{controlId}")
    public MyJsonBean<Boolean> deleteBudgetControl(
            @ApiParam("控制规则ID") @PathVariable @NotNull Long controlId) {
        try {
            boolean result = budgetControlService.deleteBudgetControl(controlId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算控制规则失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算控制规则详情
     */
    @ApiOperation("根据ID查询预算控制规则详情")
    @GetMapping("/{controlId}")
    public MyJsonBean<EpsBudgetControl> getBudgetControlById(
            @ApiParam("控制规则ID") @PathVariable @NotNull Long controlId) {
        try {
            EpsBudgetControl result = budgetControlService.getBudgetControlById(controlId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("控制规则不存在");
            }
        } catch (Exception e) {
            log.error("获取预算控制规则详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 启用预算控制规则
     */
    @ApiOperation("启用预算控制规则")
    @PostMapping("/{controlId}/enable")
    public MyJsonBean<Boolean> enableBudgetControl(
            @ApiParam("控制规则ID") @PathVariable @NotNull Long controlId) {
        try {
            boolean result = budgetControlService.enableBudgetControl(controlId);
            if (result) {
                return MyJsonBean.success("启用成功", true);
            } else {
                return MyJsonBean.error("启用失败");
            }
        } catch (Exception e) {
            log.error("启用预算控制规则失败", e);
            return MyJsonBean.error("启用失败：" + e.getMessage());
        }
    }

    /**
     * 禁用预算控制规则
     */
    @ApiOperation("禁用预算控制规则")
    @PostMapping("/{controlId}/disable")
    public MyJsonBean<Boolean> disableBudgetControl(
            @ApiParam("控制规则ID") @PathVariable @NotNull Long controlId) {
        try {
            boolean result = budgetControlService.disableBudgetControl(controlId);
            if (result) {
                return MyJsonBean.success("禁用成功", true);
            } else {
                return MyJsonBean.error("禁用失败");
            }
        } catch (Exception e) {
            log.error("禁用预算控制规则失败", e);
            return MyJsonBean.error("禁用失败：" + e.getMessage());
        }
    }

    /**
     * 执行预算控制检查
     */
    @ApiOperation("执行预算控制检查")
    @PostMapping("/check")
    public MyJsonBean<Map<String, Object>> executeBudgetControlCheck(
            @ApiParam("控制检查参数") @RequestBody Map<String, Object> checkParams) {
        try {
            Map<String, Object> result = budgetControlService.executeBudgetControlCheck(checkParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("执行预算控制检查失败", e);
            return MyJsonBean.error("检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制监控数据
     */
    @ApiOperation("获取预算控制监控数据")
    @GetMapping("/monitor")
    public MyJsonBean<Map<String, Object>> getBudgetControlMonitor(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("监控类型") @RequestParam(defaultValue = "REAL_TIME") String monitorType) {
        try {
            Map<String, Object> result = budgetControlService.getBudgetControlMonitor(versionId, organizationId, monitorType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制监控数据失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制预警信息
     */
    @ApiOperation("获取预算控制预警信息")
    @GetMapping("/alerts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetControlAlerts(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("预警级别") @RequestParam(required = false) String alertLevel,
            @ApiParam("预警状态") @RequestParam(required = false) String alertStatus) {
        try {
            List<Map<String, Object>> result = budgetControlService.getBudgetControlAlerts(
                    versionId, organizationId, alertLevel, alertStatus);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制预警信息失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 处理预算控制预警
     */
    @ApiOperation("处理预算控制预警")
    @PostMapping("/alerts/{alertId}/handle")
    public MyJsonBean<Boolean> handleBudgetControlAlert(
            @ApiParam("预警ID") @PathVariable @NotNull Long alertId,
            @ApiParam("处理参数") @RequestBody Map<String, Object> handleParams) {
        try {
            boolean result = budgetControlService.handleBudgetControlAlert(alertId, handleParams);
            if (result) {
                return MyJsonBean.success("处理成功", true);
            } else {
                return MyJsonBean.error("处理失败");
            }
        } catch (Exception e) {
            log.error("处理预算控制预警失败", e);
            return MyJsonBean.error("处理失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制仪表板
     */
    @ApiOperation("获取预算控制仪表板")
    @GetMapping("/dashboard")
    public MyJsonBean<Map<String, Object>> getBudgetControlDashboard(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("仪表板类型") @RequestParam(defaultValue = "OVERVIEW") String dashboardType) {
        try {
            Map<String, Object> result = budgetControlService.getBudgetControlDashboard(versionId, organizationId, dashboardType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制仪表板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作预算控制规则
     */
    @ApiOperation("批量操作预算控制规则")
    @PostMapping("/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateBudgetControl(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = budgetControlService.batchOperateBudgetControl(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作预算控制规则失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 导入预算控制规则
     */
    @ApiOperation("导入预算控制规则")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importBudgetControl(
            @ApiParam("导入数据") @RequestBody Map<String, Object> importData) {
        try {
            Map<String, Object> result = budgetControlService.importBudgetControl(importData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入预算控制规则失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出预算控制规则
     */
    @ApiOperation("导出预算控制规则")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportBudgetControl(
            @ApiParam("导出参数") @RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = budgetControlService.exportBudgetControl(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出预算控制规则失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制统计
     */
    @ApiOperation("获取预算控制统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetControlStatistics(
            @ApiParam("版本ID") @RequestParam(required = false) Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("统计类型") @RequestParam(defaultValue = "SUMMARY") String statisticsType) {
        try {
            Map<String, Object> result = budgetControlService.getBudgetControlStatistics(versionId, organizationId, statisticsType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制历史
     */
    @ApiOperation("获取预算控制历史")
    @GetMapping("/history")
    public MyJsonBean<List<Map<String, Object>>> getBudgetControlHistory(
            @ApiParam("版本ID") @RequestParam(required = false) Long versionId,
            @ApiParam("控制类型") @RequestParam(required = false) String controlType,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            List<Map<String, Object>> result = budgetControlService.getBudgetControlHistory(versionId, controlType, startDate, endDate);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制历史失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 测试预算控制规则
     */
    @ApiOperation("测试预算控制规则")
    @PostMapping("/test")
    public MyJsonBean<Map<String, Object>> testBudgetControl(
            @ApiParam("测试参数") @RequestBody Map<String, Object> testParams) {
        try {
            Map<String, Object> result = budgetControlService.testBudgetControl(testParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("测试预算控制规则失败", e);
            return MyJsonBean.error("测试失败：" + e.getMessage());
        }
    }

    /**
     * 复制预算控制规则
     */
    @ApiOperation("复制预算控制规则")
    @PostMapping("/{controlId}/copy")
    public MyJsonBean<Map<String, Object>> copyBudgetControl(
            @ApiParam("控制规则ID") @PathVariable @NotNull Long controlId,
            @ApiParam("复制参数") @RequestBody Map<String, Object> copyParams) {
        try {
            Map<String, Object> result = budgetControlService.copyBudgetControl(controlId, copyParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("复制预算控制规则失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制模板
     */
    @ApiOperation("获取预算控制模板")
    @GetMapping("/templates")
    public MyJsonBean<List<Map<String, Object>>> getBudgetControlTemplates(
            @ApiParam("模板类型") @RequestParam(defaultValue = "ALL") String templateType) {
        try {
            List<Map<String, Object>> result = budgetControlService.getBudgetControlTemplates(templateType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制模板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 应用预算控制模板
     */
    @ApiOperation("应用预算控制模板")
    @PostMapping("/templates/apply")
    public MyJsonBean<Map<String, Object>> applyBudgetControlTemplate(
            @ApiParam("模板应用参数") @RequestBody Map<String, Object> templateParams) {
        try {
            Map<String, Object> result = budgetControlService.applyBudgetControlTemplate(templateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("应用预算控制模板失败", e);
            return MyJsonBean.error("应用失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算控制建议
     */
    @ApiOperation("获取预算控制建议")
    @GetMapping("/recommendations")
    public MyJsonBean<List<Map<String, Object>>> getBudgetControlRecommendations(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("建议类型") @RequestParam(defaultValue = "OPTIMIZATION") String recommendationType) {
        try {
            List<Map<String, Object>> result = budgetControlService.getBudgetControlRecommendations(versionId, organizationId, recommendationType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算控制建议失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 优化预算控制策略
     */
    @ApiOperation("优化预算控制策略")
    @PostMapping("/optimize")
    public MyJsonBean<Map<String, Object>> optimizeBudgetControlStrategy(
            @ApiParam("优化参数") @RequestBody Map<String, Object> optimizeParams) {
        try {
            Map<String, Object> result = budgetControlService.optimizeBudgetControlStrategy(optimizeParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("优化预算控制策略失败", e);
            return MyJsonBean.error("优化失败：" + e.getMessage());
        }
    }

    /**
     * 刷新预算控制缓存
     */
    @ApiOperation("刷新预算控制缓存")
    @PostMapping("/refresh-cache")
    public MyJsonBean<Boolean> refreshBudgetControlCache(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("缓存类型") @RequestParam(defaultValue = "ALL") String cacheType) {
        try {
            boolean result = budgetControlService.refreshBudgetControlCache(versionId, cacheType);
            if (result) {
                return MyJsonBean.success("刷新成功", true);
            } else {
                return MyJsonBean.error("刷新失败");
            }
        } catch (Exception e) {
            log.error("刷新预算控制缓存失败", e);
            return MyJsonBean.error("刷新失败：" + e.getMessage());
        }
    }
}
