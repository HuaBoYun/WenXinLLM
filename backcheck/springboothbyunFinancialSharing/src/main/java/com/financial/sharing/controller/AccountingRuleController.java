package com.financial.sharing.controller;

import com.financial.sharing.service.AccountingRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.AccountingRuleQueryParam;
import com.financial.sharing.vo.param.AccountingRuleSaveParam;
import com.financial.sharing.vo.result.AccountingRuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 会计规则控制器
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/rules/accounting")
@Validated
public class AccountingRuleController {

    @Autowired
    private AccountingRuleService accountingRuleService;

    /**
     * 分页查询会计规则
     */
    @PostMapping("/page")
    public MyJsonBean<PageResult<AccountingRuleVO>> getAccountingRulePage(@Valid @RequestBody AccountingRuleQueryParam param) {
        try {
            PageResult<AccountingRuleVO> result = accountingRuleService.getAccountingRulePage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("分页查询会计规则失败", e);
            return MyJsonBean.errorData("分页查询会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新会计规则
     */
    @PostMapping("/save")
    public MyJsonBean<AccountingRuleVO> saveOrUpdateAccountingRule(@Valid @RequestBody AccountingRuleSaveParam param) {
        try {
            AccountingRuleVO result = accountingRuleService.saveOrUpdateAccountingRule(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("保存会计规则失败", e);
            return MyJsonBean.errorData("保存会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询会计规则详情
     */
    @GetMapping("/detail/{ruleId}")
    public MyJsonBean<AccountingRuleVO> getAccountingRuleById(@PathVariable @NotNull Long ruleId) {
        try {
            AccountingRuleVO result = accountingRuleService.getAccountingRuleById(ruleId);
            if (result == null) {
                return MyJsonBean.errorData("会计规则不存在");
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询会计规则详情失败", e);
            return MyJsonBean.errorData("查询会计规则详情失败：" + e.getMessage());
        }
    }

    /**
     * 删除会计规则
     */
    @DeleteMapping("/delete/{ruleId}")
    public MyJsonBean<Boolean> deleteAccountingRule(@PathVariable @NotNull Long ruleId) {
        try {
            boolean result = accountingRuleService.deleteAccountingRule(ruleId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除会计规则失败", e);
            return MyJsonBean.errorData("删除会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除会计规则
     */
    @DeleteMapping("/batchDelete")
    public MyJsonBean<Boolean> batchDeleteAccountingRules(@RequestBody @NotEmpty List<Long> ruleIds) {
        try {
            boolean result = accountingRuleService.batchDeleteAccountingRules(ruleIds);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("批量删除会计规则失败", e);
            return MyJsonBean.errorData("批量删除会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 更新规则启用状态
     */
    @PutMapping("/updateEnabled/{ruleId}/{isEnabled}")
    public MyJsonBean<Boolean> updateRuleEnabled(@PathVariable @NotNull Long ruleId, 
                                                 @PathVariable @NotNull Integer isEnabled) {
        try {
            boolean result = accountingRuleService.updateRuleEnabled(ruleId, isEnabled);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("更新规则启用状态失败", e);
            return MyJsonBean.errorData("更新规则启用状态失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新规则启用状态
     */
    @PutMapping("/batchUpdateEnabled/{isEnabled}")
    public MyJsonBean<Boolean> batchUpdateRuleEnabled(@RequestBody @NotEmpty List<Long> ruleIds, 
                                                      @PathVariable @NotNull Integer isEnabled) {
        try {
            boolean result = accountingRuleService.batchUpdateRuleEnabled(ruleIds, isEnabled);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("批量更新规则启用状态失败", e);
            return MyJsonBean.errorData("批量更新规则启用状态失败：" + e.getMessage());
        }
    }

    /**
     * 检查规则编码是否存在
     */
    @GetMapping("/checkRuleCode")
    public MyJsonBean<Boolean> checkRuleCodeExists(@RequestParam String ruleCode,
                                                   @RequestParam Long bookId,
                                                   @RequestParam Long tenantId,
                                                   @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = accountingRuleService.checkRuleCodeExists(ruleCode, bookId, tenantId, excludeId);
            return MyJsonBean.successData(exists);
        } catch (Exception e) {
            log.error("检查规则编码失败", e);
            return MyJsonBean.errorData("检查规则编码失败：" + e.getMessage());
        }
    }

    /**
     * 根据规则类型查询会计规则列表
     */
    @GetMapping("/listByType")
    public MyJsonBean<List<AccountingRuleVO>> getAccountingRulesByType(@RequestParam Integer ruleType,
                                                                       @RequestParam Long bookId,
                                                                       @RequestParam Long tenantId) {
        try {
            List<AccountingRuleVO> result = accountingRuleService.getAccountingRulesByType(ruleType, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据规则类型查询会计规则失败", e);
            return MyJsonBean.errorData("根据规则类型查询会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 根据事项类型查询会计规则列表
     */
    @GetMapping("/listByTransactionType")
    public MyJsonBean<List<AccountingRuleVO>> getAccountingRulesByTransactionType(@RequestParam String transactionType,
                                                                                  @RequestParam Long bookId,
                                                                                  @RequestParam Long tenantId) {
        try {
            List<AccountingRuleVO> result = accountingRuleService.getAccountingRulesByTransactionType(transactionType, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据事项类型查询会计规则失败", e);
            return MyJsonBean.errorData("根据事项类型查询会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 根据启用状态查询会计规则列表
     */
    @GetMapping("/listByEnabled")
    public MyJsonBean<List<AccountingRuleVO>> getAccountingRulesByEnabled(@RequestParam Integer isEnabled,
                                                                          @RequestParam Long bookId,
                                                                          @RequestParam Long tenantId) {
        try {
            List<AccountingRuleVO> result = accountingRuleService.getAccountingRulesByEnabled(isEnabled, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据启用状态查询会计规则失败", e);
            return MyJsonBean.errorData("根据启用状态查询会计规则失败：" + e.getMessage());
        }
    }

    /**
     * 获取规则类型列表
     */
    @GetMapping("/ruleTypes")
    public MyJsonBean<List<Integer>> getRuleTypes(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<Integer> result = accountingRuleService.getRuleTypes(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取规则类型列表失败", e);
            return MyJsonBean.errorData("获取规则类型列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取事项类型列表
     */
    @GetMapping("/transactionTypes")
    public MyJsonBean<List<String>> getTransactionTypes(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<String> result = accountingRuleService.getTransactionTypes(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取事项类型列表失败", e);
            return MyJsonBean.errorData("获取事项类型列表失败：" + e.getMessage());
        }
    }

    /**
     * 统计规则数量按类型分组
     */
    @GetMapping("/countByType")
    public MyJsonBean<List<AccountingRuleVO>> countRulesByType(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<AccountingRuleVO> result = accountingRuleService.countRulesByType(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计规则数量按类型分组失败", e);
            return MyJsonBean.errorData("统计规则数量按类型分组失败：" + e.getMessage());
        }
    }

    /**
     * 统计规则数量按事项类型分组
     */
    @GetMapping("/countByTransactionType")
    public MyJsonBean<List<AccountingRuleVO>> countRulesByTransactionType(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<AccountingRuleVO> result = accountingRuleService.countRulesByTransactionType(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计规则数量按事项类型分组失败", e);
            return MyJsonBean.errorData("统计规则数量按事项类型分组失败：" + e.getMessage());
        }
    }

    /**
     * 测试会计规则执行
     */
    @PostMapping("/test/{ruleId}")
    public MyJsonBean<Object> testAccountingRule(@PathVariable @NotNull Long ruleId, @RequestBody Object testData) {
        try {
            Object result = accountingRuleService.testAccountingRule(ruleId, testData);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试会计规则执行失败", e);
            return MyJsonBean.errorData("测试会计规则执行失败：" + e.getMessage());
        }
    }
}
