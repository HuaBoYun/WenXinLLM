package com.financial.sharing.controller;

import com.financial.sharing.dto.TblAuditRuleQueryParam;
import com.financial.sharing.dto.TblAuditRuleSaveParam;
import com.financial.sharing.entity.TblAuditRuleCondition;
import com.financial.sharing.service.TblAuditRuleService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 稽核规则管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "稽核规则管理")
@RestController
@RequestMapping("/audit/rules")
@CrossOrigin
public class AuditRuleController {

    @Autowired
    private TblAuditRuleService auditRuleService;

    @ApiOperation("查询稽核规则列表")
    @GetMapping
    public MyJsonBean getAuditRuleList(@RequestParam(required = false) String ruleName,
                                     @RequestParam(required = false) String ruleCode,
                                     @RequestParam(required = false) String ruleType,
                                     @RequestParam(required = false) Integer isEnabled,
                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            TblAuditRuleQueryParam param = new TblAuditRuleQueryParam();
            param.setRuleName(ruleName);
            param.setRuleCode(ruleCode);
            param.setRuleType(ruleType);
            param.setIsEnabled(isEnabled);
            param.setPageNo(pageNo);
            param.setPageSize(pageSize);

            return auditRuleService.getList(param);
        } catch (Exception e) {
            log.error("查询稽核规则列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存稽核规则")
    @PostMapping
    public MyJsonBean saveAuditRule(@RequestBody TblAuditRuleSaveParam param) {
        try {
            return auditRuleService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存稽核规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除稽核规则")
    @DeleteMapping("/{ruleId}")
    public MyJsonBean deleteAuditRule(@PathVariable String ruleId) {
        try {
            return auditRuleService.delete(ruleId);
        } catch (Exception e) {
            log.error("删除稽核规则失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取稽核规则详情")
    @GetMapping("/{ruleId}")
    public MyJsonBean getAuditRuleDetail(@PathVariable String ruleId) {
        try {
            return auditRuleService.getById(ruleId);
        } catch (Exception e) {
            log.error("获取稽核规则详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("测试稽核规则")
    @PostMapping("/{ruleId}/test")
    public MyJsonBean testAuditRule(@PathVariable String ruleId,
                                   @RequestBody Map<String, Object> testData) {
        try {
            return auditRuleService.testRule(ruleId, testData);
        } catch (Exception e) {
            log.error("测试稽核规则失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取稽核规则条件")
    @GetMapping("/{ruleId}/conditions")
    public MyJsonBean getAuditRuleConditions(@PathVariable String ruleId) {
        try {
            return auditRuleService.getRuleConditions(ruleId);
        } catch (Exception e) {
            log.error("获取稽核规则条件失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用稽核规则")
    @PutMapping("/{ruleId}/status")
    public MyJsonBean updateAuditRuleStatus(@PathVariable String ruleId,
                                           @RequestParam Integer isEnabled) {
        try {
            return auditRuleService.updateStatus(ruleId, isEnabled);
        } catch (Exception e) {
            log.error("更新稽核规则状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取稽核规则执行日志")
    @GetMapping("/{ruleId}/logs")
    public MyJsonBean getAuditRuleLogs(@PathVariable String ruleId) {
        try {
            return auditRuleService.getExecutionLogs(ruleId);
        } catch (Exception e) {
            log.error("获取稽核规则执行日志失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存单个条件")
    @PostMapping("/conditions")
    public MyJsonBean saveCondition(@RequestBody TblAuditRuleCondition condition) {
        try {
            return auditRuleService.saveCondition(condition);
        } catch (Exception e) {
            log.error("保存条件失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除单个条件")
    @DeleteMapping("/conditions/{conditionId}")
    public MyJsonBean deleteCondition(@PathVariable String conditionId) {
        try {
            return auditRuleService.deleteCondition(conditionId);
        } catch (Exception e) {
            log.error("删除条件失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }
}
