package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.AccountingRuleQueryParam;
import com.financial.sharing.vo.param.AccountingRuleSaveParam;
import com.financial.sharing.vo.result.AccountingRuleVO;

import java.util.List;

/**
 * 会计规则服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface AccountingRuleService {

    /**
     * 分页查询会计规则
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<AccountingRuleVO> getAccountingRulePage(AccountingRuleQueryParam param);

    /**
     * 保存或更新会计规则
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    AccountingRuleVO saveOrUpdateAccountingRule(AccountingRuleSaveParam param);

    /**
     * 根据ID查询会计规则详情
     * 
     * @param ruleId 规则ID
     * @return 会计规则详情
     */
    AccountingRuleVO getAccountingRuleById(Long ruleId);

    /**
     * 删除会计规则
     * 
     * @param ruleId 规则ID
     * @return 是否成功
     */
    boolean deleteAccountingRule(Long ruleId);

    /**
     * 批量删除会计规则
     * 
     * @param ruleIds 规则ID列表
     * @return 是否成功
     */
    boolean batchDeleteAccountingRules(List<Long> ruleIds);

    /**
     * 更新规则启用状态
     * 
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @return 是否成功
     */
    boolean updateRuleEnabled(Long ruleId, Integer isEnabled);

    /**
     * 批量更新规则启用状态
     * 
     * @param ruleIds 规则ID列表
     * @param isEnabled 是否启用
     * @return 是否成功
     */
    boolean batchUpdateRuleEnabled(List<Long> ruleIds, Integer isEnabled);

    /**
     * 检查规则编码是否存在
     * 
     * @param ruleCode 规则编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkRuleCodeExists(String ruleCode, Long bookId, Long tenantId, Long excludeId);

    /**
     * 根据规则类型查询会计规则列表
     * 
     * @param ruleType 规则类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计规则列表
     */
    List<AccountingRuleVO> getAccountingRulesByType(Integer ruleType, Long bookId, Long tenantId);

    /**
     * 根据事项类型查询会计规则列表
     * 
     * @param transactionType 事项类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计规则列表
     */
    List<AccountingRuleVO> getAccountingRulesByTransactionType(String transactionType, Long bookId, Long tenantId);

    /**
     * 根据启用状态查询会计规则列表
     * 
     * @param isEnabled 是否启用
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计规则列表
     */
    List<AccountingRuleVO> getAccountingRulesByEnabled(Integer isEnabled, Long bookId, Long tenantId);

    /**
     * 获取规则类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则类型列表
     */
    List<Integer> getRuleTypes(Long bookId, Long tenantId);

    /**
     * 获取事项类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 事项类型列表
     */
    List<String> getTransactionTypes(Long bookId, Long tenantId);

    /**
     * 统计规则数量按类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<AccountingRuleVO> countRulesByType(Long bookId, Long tenantId);

    /**
     * 统计规则数量按事项类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<AccountingRuleVO> countRulesByTransactionType(Long bookId, Long tenantId);

    /**
     * 测试会计规则执行
     * 
     * @param ruleId 规则ID
     * @param testData 测试数据
     * @return 测试结果
     */
    Object testAccountingRule(Long ruleId, Object testData);
}
