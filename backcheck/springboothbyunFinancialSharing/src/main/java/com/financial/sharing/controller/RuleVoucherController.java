package com.financial.sharing.controller;

import com.financial.sharing.service.RuleVoucherService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.RuleVoucherQueryParam;
import com.financial.sharing.vo.param.RuleVoucherSaveParam;
import com.financial.sharing.vo.result.RuleVoucherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 规则凭证控制器
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/rules/voucher")
@Validated
public class RuleVoucherController {

    @Autowired
    private RuleVoucherService ruleVoucherService;

    /**
     * 分页查询规则凭证
     */
    @PostMapping("/page")
    public MyJsonBean<PageResult<RuleVoucherVO>> getRuleVoucherPage(@Valid @RequestBody RuleVoucherQueryParam param) {
        try {
            PageResult<RuleVoucherVO> result = ruleVoucherService.getRuleVoucherPage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("分页查询规则凭证失败", e);
            return MyJsonBean.errorData("分页查询规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新规则凭证
     */
    @PostMapping("/save")
    public MyJsonBean<RuleVoucherVO> saveOrUpdateRuleVoucher(@Valid @RequestBody RuleVoucherSaveParam param) {
        try {
            RuleVoucherVO result = ruleVoucherService.saveOrUpdateRuleVoucher(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("保存规则凭证失败", e);
            return MyJsonBean.errorData("保存规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询规则凭证详情
     */
    @GetMapping("/detail/{ruleVoucherId}")
    public MyJsonBean<RuleVoucherVO> getRuleVoucherById(@PathVariable @NotNull Long ruleVoucherId) {
        try {
            RuleVoucherVO result = ruleVoucherService.getRuleVoucherById(ruleVoucherId);
            if (result == null) {
                return MyJsonBean.errorData("规则凭证不存在");
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询规则凭证详情失败", e);
            return MyJsonBean.errorData("查询规则凭证详情失败：" + e.getMessage());
        }
    }

    /**
     * 删除规则凭证
     */
    @DeleteMapping("/delete/{ruleVoucherId}")
    public MyJsonBean<Boolean> deleteRuleVoucher(@PathVariable @NotNull Long ruleVoucherId) {
        try {
            boolean result = ruleVoucherService.deleteRuleVoucher(ruleVoucherId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("删除规则凭证失败", e);
            return MyJsonBean.errorData("删除规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除规则凭证
     */
    @DeleteMapping("/batchDelete")
    public MyJsonBean<Boolean> batchDeleteRuleVouchers(@RequestBody @NotEmpty List<Long> ruleVoucherIds) {
        try {
            boolean result = ruleVoucherService.batchDeleteRuleVouchers(ruleVoucherIds);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("批量删除规则凭证失败", e);
            return MyJsonBean.errorData("批量删除规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 更新规则凭证启用状态
     */
    @PutMapping("/updateEnabled/{ruleVoucherId}/{isEnabled}")
    public MyJsonBean<Boolean> updateRuleVoucherEnabled(@PathVariable @NotNull Long ruleVoucherId, 
                                                        @PathVariable @NotNull Integer isEnabled) {
        try {
            boolean result = ruleVoucherService.updateRuleVoucherEnabled(ruleVoucherId, isEnabled);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("更新规则凭证启用状态失败", e);
            return MyJsonBean.errorData("更新规则凭证启用状态失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新规则凭证启用状态
     */
    @PutMapping("/batchUpdateEnabled/{isEnabled}")
    public MyJsonBean<Boolean> batchUpdateRuleVoucherEnabled(@RequestBody @NotEmpty List<Long> ruleVoucherIds, 
                                                             @PathVariable @NotNull Integer isEnabled) {
        try {
            boolean result = ruleVoucherService.batchUpdateRuleVoucherEnabled(ruleVoucherIds, isEnabled);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("批量更新规则凭证启用状态失败", e);
            return MyJsonBean.errorData("批量更新规则凭证启用状态失败：" + e.getMessage());
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
            boolean exists = ruleVoucherService.checkRuleCodeExists(ruleCode, bookId, tenantId, excludeId);
            return MyJsonBean.successData(exists);
        } catch (Exception e) {
            log.error("检查规则编码失败", e);
            return MyJsonBean.errorData("检查规则编码失败：" + e.getMessage());
        }
    }

    /**
     * 根据规则类型查询规则凭证列表
     */
    @GetMapping("/listByType")
    public MyJsonBean<List<RuleVoucherVO>> getRuleVouchersByType(@RequestParam Integer ruleType,
                                                                 @RequestParam Long bookId,
                                                                 @RequestParam Long tenantId) {
        try {
            List<RuleVoucherVO> result = ruleVoucherService.getRuleVouchersByType(ruleType, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据规则类型查询规则凭证失败", e);
            return MyJsonBean.errorData("根据规则类型查询规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 根据执行期间查询规则凭证列表
     */
    @GetMapping("/listByExecutionPeriod")
    public MyJsonBean<List<RuleVoucherVO>> getRuleVouchersByExecutionPeriod(@RequestParam String executionPeriod,
                                                                            @RequestParam Long bookId,
                                                                            @RequestParam Long tenantId) {
        try {
            List<RuleVoucherVO> result = ruleVoucherService.getRuleVouchersByExecutionPeriod(executionPeriod, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据执行期间查询规则凭证失败", e);
            return MyJsonBean.errorData("根据执行期间查询规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 根据启用状态查询规则凭证列表
     */
    @GetMapping("/listByEnabled")
    public MyJsonBean<List<RuleVoucherVO>> getRuleVouchersByEnabled(@RequestParam Integer isEnabled,
                                                                    @RequestParam Long bookId,
                                                                    @RequestParam Long tenantId) {
        try {
            List<RuleVoucherVO> result = ruleVoucherService.getRuleVouchersByEnabled(isEnabled, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据启用状态查询规则凭证失败", e);
            return MyJsonBean.errorData("根据启用状态查询规则凭证失败：" + e.getMessage());
        }
    }

    /**
     * 获取规则类型列表
     */
    @GetMapping("/ruleTypes")
    public MyJsonBean<List<Integer>> getRuleTypes(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<Integer> result = ruleVoucherService.getRuleTypes(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取规则类型列表失败", e);
            return MyJsonBean.errorData("获取规则类型列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取执行期间列表
     */
    @GetMapping("/executionPeriods")
    public MyJsonBean<List<String>> getExecutionPeriods(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<String> result = ruleVoucherService.getExecutionPeriods(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取执行期间列表失败", e);
            return MyJsonBean.errorData("获取执行期间列表失败：" + e.getMessage());
        }
    }

    /**
     * 统计规则凭证数量按类型分组
     */
    @GetMapping("/countByType")
    public MyJsonBean<List<RuleVoucherVO>> countRuleVouchersByType(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<RuleVoucherVO> result = ruleVoucherService.countRuleVouchersByType(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计规则凭证数量按类型分组失败", e);
            return MyJsonBean.errorData("统计规则凭证数量按类型分组失败：" + e.getMessage());
        }
    }

    /**
     * 统计规则凭证数量按期间分组
     */
    @GetMapping("/countByExecutionPeriod")
    public MyJsonBean<List<RuleVoucherVO>> countRuleVouchersByExecutionPeriod(@RequestParam Long bookId, @RequestParam Long tenantId) {
        try {
            List<RuleVoucherVO> result = ruleVoucherService.countRuleVouchersByExecutionPeriod(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计规则凭证数量按期间分组失败", e);
            return MyJsonBean.errorData("统计规则凭证数量按期间分组失败：" + e.getMessage());
        }
    }

    /**
     * 执行规则凭证
     */
    @PostMapping("/execute/{ruleVoucherId}")
    public MyJsonBean<Object> executeRuleVoucher(@PathVariable @NotNull Long ruleVoucherId, 
                                                 @RequestParam String executionPeriod) {
        try {
            Object result = ruleVoucherService.executeRuleVoucher(ruleVoucherId, executionPeriod);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("执行规则凭证失败", e);
            return MyJsonBean.errorData("执行规则凭证失败：" + e.getMessage());
        }
    }
}
