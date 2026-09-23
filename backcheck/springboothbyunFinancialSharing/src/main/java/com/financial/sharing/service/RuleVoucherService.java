package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.RuleVoucherQueryParam;
import com.financial.sharing.vo.param.RuleVoucherSaveParam;
import com.financial.sharing.vo.result.RuleVoucherVO;

import java.util.List;

/**
 * 规则凭证服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface RuleVoucherService {

    /**
     * 分页查询规则凭证
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<RuleVoucherVO> getRuleVoucherPage(RuleVoucherQueryParam param);

    /**
     * 保存或更新规则凭证
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    RuleVoucherVO saveOrUpdateRuleVoucher(RuleVoucherSaveParam param);

    /**
     * 根据ID查询规则凭证详情
     * 
     * @param ruleVoucherId 规则凭证ID
     * @return 规则凭证详情
     */
    RuleVoucherVO getRuleVoucherById(Long ruleVoucherId);

    /**
     * 删除规则凭证
     * 
     * @param ruleVoucherId 规则凭证ID
     * @return 是否成功
     */
    boolean deleteRuleVoucher(Long ruleVoucherId);

    /**
     * 批量删除规则凭证
     * 
     * @param ruleVoucherIds 规则凭证ID列表
     * @return 是否成功
     */
    boolean batchDeleteRuleVouchers(List<Long> ruleVoucherIds);

    /**
     * 更新规则凭证启用状态
     * 
     * @param ruleVoucherId 规则凭证ID
     * @param isEnabled 是否启用
     * @return 是否成功
     */
    boolean updateRuleVoucherEnabled(Long ruleVoucherId, Integer isEnabled);

    /**
     * 批量更新规则凭证启用状态
     * 
     * @param ruleVoucherIds 规则凭证ID列表
     * @param isEnabled 是否启用
     * @return 是否成功
     */
    boolean batchUpdateRuleVoucherEnabled(List<Long> ruleVoucherIds, Integer isEnabled);

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
     * 根据规则类型查询规则凭证列表
     * 
     * @param ruleType 规则类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则凭证列表
     */
    List<RuleVoucherVO> getRuleVouchersByType(Integer ruleType, Long bookId, Long tenantId);

    /**
     * 根据执行期间查询规则凭证列表
     * 
     * @param executionPeriod 执行期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则凭证列表
     */
    List<RuleVoucherVO> getRuleVouchersByExecutionPeriod(String executionPeriod, Long bookId, Long tenantId);

    /**
     * 根据启用状态查询规则凭证列表
     * 
     * @param isEnabled 是否启用
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则凭证列表
     */
    List<RuleVoucherVO> getRuleVouchersByEnabled(Integer isEnabled, Long bookId, Long tenantId);

    /**
     * 获取规则类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则类型列表
     */
    List<Integer> getRuleTypes(Long bookId, Long tenantId);

    /**
     * 获取执行期间列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 执行期间列表
     */
    List<String> getExecutionPeriods(Long bookId, Long tenantId);

    /**
     * 统计规则凭证数量按类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<RuleVoucherVO> countRuleVouchersByType(Long bookId, Long tenantId);

    /**
     * 统计规则凭证数量按期间分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<RuleVoucherVO> countRuleVouchersByExecutionPeriod(Long bookId, Long tenantId);

    /**
     * 执行规则凭证
     * 
     * @param ruleVoucherId 规则凭证ID
     * @param executionPeriod 执行期间
     * @return 执行结果
     */
    Object executeRuleVoucher(Long ruleVoucherId, String executionPeriod);
}
