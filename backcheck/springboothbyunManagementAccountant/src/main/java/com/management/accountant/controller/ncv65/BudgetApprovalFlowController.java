package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ncv65.BudgetApprovalFlow;
import com.management.accountant.service.ncv65.IBudgetApprovalFlowService;
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
 * NCV65全面预算系统 - 预算审批流程控制器
 * 
 * @description 预算审批流程管理API接口，支持审批流程的完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算审批流程管理")
@RestController
@RequestMapping("/budget/approval/flow")
@Validated
public class BudgetApprovalFlowController {

    @Resource
    private IBudgetApprovalFlowService budgetApprovalFlowService;

    /**
     * 创建审批流程
     */
    @ApiOperation("创建审批流程")
    @PostMapping
    public MyJsonBean<Boolean> createApprovalFlow(@Valid @RequestBody BudgetApprovalFlow flow) {
        try {
            boolean result = budgetApprovalFlowService.createApprovalFlow(flow);
            return MyJsonBean.successData(result,  "创建审批流程成功");
        } catch (Exception e) {
            log.error("创建审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("创建审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 更新审批流程
     */
    @ApiOperation("更新审批流程")
    @PutMapping("/{id}")
    public MyJsonBean<Boolean> updateApprovalFlow(
            @ApiParam("流程ID") @PathVariable String id,
            @Valid @RequestBody BudgetApprovalFlow flow) {
        try {
            flow.setId(id);
            boolean result = budgetApprovalFlowService.updateApprovalFlow(flow);
            return MyJsonBean.successData(result,  "更新审批流程成功");
        } catch (Exception e) {
            log.error("更新审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 删除审批流程
     */
    @ApiOperation("删除审批流程")
    @DeleteMapping("/{id}")
    public MyJsonBean<Boolean> deleteApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            boolean result = budgetApprovalFlowService.deleteApprovalFlow(id);
            return MyJsonBean.successData(result,  "删除审批流程成功");
        } catch (Exception e) {
            log.error("删除审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("删除审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除审批流程
     */
    @ApiOperation("批量删除审批流程")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteApprovalFlows(@RequestBody List<String> ids) {
        try {
            boolean result = budgetApprovalFlowService.batchDeleteApprovalFlows(ids);
            return MyJsonBean.successData(result,  "批量删除审批流程成功");
        } catch (Exception e) {
            log.error("批量删除审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量删除审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 查询审批流程详情
     */
    @ApiOperation("查询审批流程详情")
    @GetMapping("/{id}")
    public MyJsonBean<BudgetApprovalFlow> getApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            BudgetApprovalFlow flow = budgetApprovalFlowService.getApprovalFlowById(id);
            return MyJsonBean.successData(flow,  "查询审批流程成功");
        } catch (Exception e) {
            log.error("查询审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询审批流程
     */
    @ApiOperation("根据编码查询审批流程")
    @GetMapping("/code/{flowCode}")
    public MyJsonBean<BudgetApprovalFlow> getApprovalFlowByCode(@ApiParam("流程编码") @PathVariable String flowCode) {
        try {
            BudgetApprovalFlow flow = budgetApprovalFlowService.getApprovalFlowByCode(flowCode);
            return MyJsonBean.successData(flow,  "查询审批流程成功");
        } catch (Exception e) {
            log.error("根据编码查询审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据编码查询审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询审批流程
     */
    @ApiOperation("分页查询审批流程")
    @PostMapping("/page")
    public MyJsonBean<IPage<BudgetApprovalFlow>> getApprovalFlowPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @RequestBody(required = false) Map<String, Object> params) {
        try {
            IPage<BudgetApprovalFlow> page = budgetApprovalFlowService.getApprovalFlowPage(current, size, params);
            return MyJsonBean.successData(page,  "查询审批流程成功");
        } catch (Exception e) {
            log.error("分页查询审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分页查询审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 根据流程类型查询审批流程
     */
    @ApiOperation("根据流程类型查询审批流程")
    @GetMapping("/type/{flowType}")
    public MyJsonBean<List<BudgetApprovalFlow>> getApprovalFlowsByType(@ApiParam("流程类型") @PathVariable String flowType) {
        try {
            List<BudgetApprovalFlow> flows = budgetApprovalFlowService.getApprovalFlowsByType(flowType);
            return MyJsonBean.successData(flows,  "查询审批流程成功");
        } catch (Exception e) {
            log.error("根据流程类型查询审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据流程类型查询审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 查询已发布的审批流程
     */
    @ApiOperation("查询已发布的审批流程")
    @GetMapping("/published")
    public MyJsonBean<List<BudgetApprovalFlow>> getPublishedApprovalFlows() {
        try {
            List<BudgetApprovalFlow> flows = budgetApprovalFlowService.getPublishedApprovalFlows();
            return MyJsonBean.successData(flows,  "查询已发布审批流程成功");
        } catch (Exception e) {
            log.error("查询已发布审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询已发布审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 查询可用的审批流程
     */
    @ApiOperation("查询可用的审批流程")
    @GetMapping("/available")
    public MyJsonBean<List<BudgetApprovalFlow>> getAvailableApprovalFlows(
            @ApiParam("流程类型") @RequestParam String flowType,
            @ApiParam("组织ID") @RequestParam String orgId) {
        try {
            List<BudgetApprovalFlow> flows = budgetApprovalFlowService.getAvailableApprovalFlows(flowType, orgId);
            return MyJsonBean.successData(flows,  "查询可用审批流程成功");
        } catch (Exception e) {
            log.error("查询可用审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询可用审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 查询默认审批流程
     */
    @ApiOperation("查询默认审批流程")
    @GetMapping("/default")
    public MyJsonBean<BudgetApprovalFlow> getDefaultApprovalFlow(
            @ApiParam("流程类型") @RequestParam String flowType,
            @ApiParam("组织ID") @RequestParam String orgId) {
        try {
            BudgetApprovalFlow flow = budgetApprovalFlowService.getDefaultApprovalFlow(flowType, orgId);
            return MyJsonBean.successData(flow,  "查询默认审批流程成功");
        } catch (Exception e) {
            log.error("查询默认审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询默认审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 查询流程版本列表
     */
    @ApiOperation("查询流程版本列表")
    @GetMapping("/versions/{flowCode}")
    public MyJsonBean<List<BudgetApprovalFlow>> getFlowVersions(@ApiParam("流程编码") @PathVariable String flowCode) {
        try {
            List<BudgetApprovalFlow> versions = budgetApprovalFlowService.getFlowVersions(flowCode);
            return MyJsonBean.successData(versions,  "查询流程版本成功");
        } catch (Exception e) {
            log.error("查询流程版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询流程版本失败：" + e.getMessage());
        }
    }

    /**
     * 查询最新流程版本
     */
    @ApiOperation("查询最新流程版本")
    @GetMapping("/latest/{flowCode}")
    public MyJsonBean<BudgetApprovalFlow> getLatestFlowVersion(@ApiParam("流程编码") @PathVariable String flowCode) {
        try {
            BudgetApprovalFlow flow = budgetApprovalFlowService.getLatestFlowVersion(flowCode);
            return MyJsonBean.successData(flow,  "查询最新流程版本成功");
        } catch (Exception e) {
            log.error("查询最新流程版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询最新流程版本失败：" + e.getMessage());
        }
    }

    // ==================== 业务操作接口 ====================

    /**
     * 发布审批流程
     */
    @ApiOperation("发布审批流程")
    @PostMapping("/{id}/publish")
    public MyJsonBean<Boolean> publishApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            boolean result = budgetApprovalFlowService.publishApprovalFlow(id);
            return MyJsonBean.successData(result,  "发布审批流程成功");
        } catch (Exception e) {
            log.error("发布审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("发布审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 取消发布审批流程
     */
    @ApiOperation("取消发布审批流程")
    @PostMapping("/{id}/unpublish")
    public MyJsonBean<Boolean> unpublishApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            boolean result = budgetApprovalFlowService.unpublishApprovalFlow(id);
            return MyJsonBean.successData(result,  "取消发布审批流程成功");
        } catch (Exception e) {
            log.error("取消发布审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("取消发布审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 归档审批流程
     */
    @ApiOperation("归档审批流程")
    @PostMapping("/{id}/archive")
    public MyJsonBean<Boolean> archiveApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            boolean result = budgetApprovalFlowService.archiveApprovalFlow(id);
            return MyJsonBean.successData(result,  "归档审批流程成功");
        } catch (Exception e) {
            log.error("归档审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("归档审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 启用审批流程
     */
    @ApiOperation("启用审批流程")
    @PostMapping("/{id}/enable")
    public MyJsonBean<Boolean> enableApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            boolean result = budgetApprovalFlowService.enableApprovalFlow(id);
            return MyJsonBean.successData(result,  "启用审批流程成功");
        } catch (Exception e) {
            log.error("启用审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("启用审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 停用审批流程
     */
    @ApiOperation("停用审批流程")
    @PostMapping("/{id}/disable")
    public MyJsonBean<Boolean> disableApprovalFlow(@ApiParam("流程ID") @PathVariable String id) {
        try {
            boolean result = budgetApprovalFlowService.disableApprovalFlow(id);
            return MyJsonBean.successData(result,  "停用审批流程成功");
        } catch (Exception e) {
            log.error("停用审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("停用审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 设置默认审批流程
     */
    @ApiOperation("设置默认审批流程")
    @PostMapping("/{id}/set-default")
    public MyJsonBean<Boolean> setDefaultApprovalFlow(
            @ApiParam("流程ID") @PathVariable String id,
            @ApiParam("流程类型") @RequestParam String flowType,
            @ApiParam("组织ID") @RequestParam String orgId) {
        try {
            boolean result = budgetApprovalFlowService.setDefaultApprovalFlow(id, flowType, orgId);
            return MyJsonBean.successData(result,  "设置默认审批流程成功");
        } catch (Exception e) {
            log.error("设置默认审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("设置默认审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 复制审批流程
     */
    @ApiOperation("复制审批流程")
    @PostMapping("/{id}/copy")
    public MyJsonBean<String> copyApprovalFlow(
            @ApiParam("源流程ID") @PathVariable String id,
            @ApiParam("新流程编码") @RequestParam String newFlowCode,
            @ApiParam("新流程名称") @RequestParam String newFlowName) {
        try {
            String newFlowId = budgetApprovalFlowService.copyApprovalFlow(id, newFlowCode, newFlowName);
            return MyJsonBean.successData(newFlowId,  "复制审批流程成功");
        } catch (Exception e) {
            log.error("复制审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("复制审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 批量发布审批流程
     */
    @ApiOperation("批量发布审批流程")
    @PostMapping("/batch/publish")
    public MyJsonBean<Integer> batchPublishApprovalFlows(@RequestBody List<String> flowIds) {
        try {
            int result = budgetApprovalFlowService.batchPublishApprovalFlows(flowIds);
            return MyJsonBean.successData(result,  "批量发布审批流程成功");
        } catch (Exception e) {
            log.error("批量发布审批流程失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量发布审批流程失败：" + e.getMessage());
        }
    }

    /**
     * 验证流程配置
     */
    @ApiOperation("验证流程配置")
    @PostMapping("/{id}/validate")
    public MyJsonBean<Map<String, Object>> validateFlowConfig(@ApiParam("流程ID") @PathVariable String id) {
        try {
            Map<String, Object> result = budgetApprovalFlowService.validateFlowConfig(id);
            return MyJsonBean.successData(result,  "验证流程配置成功");
        } catch (Exception e) {
            log.error("验证流程配置失败：{}", e.getMessage(), e);
            return MyJsonBean.error("验证流程配置失败：" + e.getMessage());
        }
    }

    /**
     * 获取审批流程统计信息
     */
    @ApiOperation("获取审批流程统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getApprovalFlowStatistics() {
        try {
            Map<String, Object> statistics = budgetApprovalFlowService.getApprovalFlowStatistics();
            return MyJsonBean.successData(statistics,  "获取审批流程统计信息成功");
        } catch (Exception e) {
            log.error("获取审批流程统计信息失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取审批流程统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 按流程类型统计审批流程数量
     */
    @ApiOperation("按流程类型统计审批流程数量")
    @GetMapping("/statistics/type")
    public MyJsonBean<List<Map<String, Object>>> getApprovalFlowCountByType() {
        try {
            List<Map<String, Object>> statistics = budgetApprovalFlowService.getApprovalFlowCountByType();
            return MyJsonBean.successData(statistics,  "按流程类型统计审批流程数量成功");
        } catch (Exception e) {
            log.error("按流程类型统计审批流程数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按流程类型统计审批流程数量失败：" + e.getMessage());
        }
    }

    /**
     * 检查流程编码是否存在
     */
    @ApiOperation("检查流程编码是否存在")
    @GetMapping("/check-code")
    public MyJsonBean<Boolean> checkFlowCodeExists(
            @ApiParam("流程编码") @RequestParam String flowCode,
            @ApiParam("排除的流程ID") @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = budgetApprovalFlowService.checkFlowCodeExists(flowCode, excludeId);
            return MyJsonBean.successData(exists,  "检查流程编码成功");
        } catch (Exception e) {
            log.error("检查流程编码失败：{}", e.getMessage(), e);
            return MyJsonBean.error("检查流程编码失败：" + e.getMessage());
        }
    }
}
