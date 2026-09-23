package com.financial.sharing.controller;

import com.financial.sharing.service.InventoryAlertService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存预警控制器
 * @author system
 * @date 2026-01-29
 */
@Slf4j
@Api(tags = "库存预警管理")
@RestController
@RequestMapping("/inventory/alert")
public class InventoryAlertController {

    @Autowired(required = false)
    private InventoryAlertService inventoryAlertService;

    // ==================== 预警记录管理 ====================

    @PostMapping(value = "/getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("分页查询库存预警列表")
    public MyJsonBean getInventoryAlertList(@RequestBody Map<String, Object> params) {
        try {
            log.info("查询库存预警列表, 参数: {}", params);
            PageResult<Map<String, Object>> result = inventoryAlertService.getAlertPage(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询库存预警列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/statistics", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("获取预警统计数据")
    public MyJsonBean getAlertStatistics(@RequestBody Map<String, Object> params) {
        try {
            log.info("获取预警统计数据, 参数: {}", params);
            Map<String, Object> result = inventoryAlertService.getAlertStatistics(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取预警统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/process/{alertId}")
    @ApiOperation("处理预警")
    public MyJsonBean processAlert(@PathVariable @ApiParam("预警ID") Long alertId,
                                   @RequestBody Map<String, Object> param) {
        try {
            log.info("处理预警, alertId: {}, param: {}", alertId, param);
            boolean result = inventoryAlertService.processAlert(alertId, param);
            if (result) {
                return MyJsonBean.successData("处理成功", null);
            } else {
                return MyJsonBean.errorData("处理失败");
            }
        } catch (Exception e) {
            log.error("处理预警失败, alertId: {}", alertId, e);
            return MyJsonBean.errorData("处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/ignore/{alertId}")
    @ApiOperation("忽略预警")
    public MyJsonBean ignoreAlert(@PathVariable @ApiParam("预警ID") Long alertId) {
        try {
            log.info("忽略预警, alertId: {}", alertId);
            boolean result = inventoryAlertService.ignoreAlert(alertId);
            if (result) {
                return MyJsonBean.successData("操作成功", null);
            } else {
                return MyJsonBean.errorData("操作失败");
            }
        } catch (Exception e) {
            log.error("忽略预警失败, alertId: {}", alertId, e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchProcess")
    @ApiOperation("批量处理预警")
    public MyJsonBean batchProcessAlert(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> alertIds = (List<Long>) param.get("alertIds");
            log.info("批量处理预警, alertIds: {}", alertIds);
            boolean result = inventoryAlertService.batchProcessAlert(alertIds, param);
            if (result) {
                return MyJsonBean.successData("批量处理成功", null);
            } else {
                return MyJsonBean.errorData("批量处理失败");
            }
        } catch (Exception e) {
            log.error("批量处理预警失败", e);
            return MyJsonBean.errorData("批量处理失败: " + e.getMessage());
        }
    }

    // ==================== 预警规则管理 ====================

    @PostMapping(value = "/rule/getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("分页查询预警规则列表")
    public MyJsonBean getAlertRuleList(@RequestBody Map<String, Object> params) {
        try {
            log.info("查询预警规则列表, 参数: {}", params);
            PageResult<Map<String, Object>> result = inventoryAlertService.getRulePage(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询预警规则列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/rule/saveOrUpdate")
    @ApiOperation("保存或更新预警规则")
    public MyJsonBean saveOrUpdateRule(@RequestBody Map<String, Object> param) {
        try {
            log.info("保存或更新预警规则, 参数: {}", param);
            boolean result = inventoryAlertService.saveOrUpdateRule(param);
            if (result) {
                return MyJsonBean.successData(param.get("ruleId") != null ? "更新成功" : "新增成功", null);
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新预警规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/rule/delete/{ruleId}")
    @ApiOperation("删除预警规则")
    public MyJsonBean deleteRule(@PathVariable @ApiParam("规则ID") Long ruleId) {
        try {
            log.info("删除预警规则, ruleId: {}", ruleId);
            boolean result = inventoryAlertService.deleteRule(ruleId);
            if (result) {
                return MyJsonBean.successData("删除成功", null);
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预警规则失败, ruleId: {}", ruleId, e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/rule/toggleStatus")
    @ApiOperation("启用/禁用预警规则")
    public MyJsonBean toggleRuleStatus(@RequestBody Map<String, Object> param) {
        try {
            Long ruleId = Long.valueOf(param.get("ruleId").toString());
            Integer status = Integer.valueOf(param.get("status").toString());
            log.info("切换预警规则状态, ruleId: {}, status: {}", ruleId, status);
            boolean result = inventoryAlertService.toggleRuleStatus(ruleId, status);
            if (result) {
                return MyJsonBean.successData("操作成功", null);
            } else {
                return MyJsonBean.errorData("操作失败");
            }
        } catch (Exception e) {
            log.error("切换预警规则状态失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/rule/batchToggleStatus")
    @ApiOperation("批量启用/禁用预警规则")
    public MyJsonBean batchToggleRuleStatus(@RequestBody Map<String, Object> param) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ruleIds = (List<Long>) param.get("ruleIds");
            Integer status = Integer.valueOf(param.get("status").toString());
            log.info("批量切换预警规则状态, ruleIds: {}, status: {}", ruleIds, status);
            boolean result = inventoryAlertService.batchToggleRuleStatus(ruleIds, status);
            if (result) {
                return MyJsonBean.successData("批量操作成功", null);
            } else {
                return MyJsonBean.errorData("批量操作失败");
            }
        } catch (Exception e) {
            log.error("批量切换预警规则状态失败", e);
            return MyJsonBean.errorData("批量操作失败: " + e.getMessage());
        }
    }

    // ==================== 预警分析 ====================

    @PostMapping(value = "/trend", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("获取预警趋势分析")
    public MyJsonBean getAlertTrend(@RequestBody Map<String, Object> params) {
        try {
            log.info("获取预警趋势分析, 参数: {}", params);
            List<Map<String, Object>> result = inventoryAlertService.getAlertTrend(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取预警趋势分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/distribution", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("获取预警类型分布")
    public MyJsonBean getAlertDistribution(@RequestBody Map<String, Object> params) {
        try {
            log.info("获取预警类型分布, 参数: {}", params);
            List<Map<String, Object>> result = inventoryAlertService.getAlertDistribution(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取预警类型分布失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出预警数据")
    public MyJsonBean exportAlert(@RequestBody Map<String, Object> param) {
        try {
            log.info("导出预警数据, 参数: {}", param);
            String filePath = inventoryAlertService.exportAlert(param);
            Map<String, Object> result = new HashMap<>();
            result.put("filePath", filePath);
            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            log.error("导出预警数据失败", e);
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }
}

