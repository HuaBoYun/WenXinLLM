package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TreasuryBusinessRule;

import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 业务活动规则管理Service接口
 * 
 * @author HuaBo Cloud
 * @since 2025-09-22
 */
public interface TreasuryBusinessRuleService extends IService<TreasuryBusinessRule> {

    /**
     * 分页查询业务活动规则列表
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param ruleCode 规则编码
     * @param ruleName 规则名称
     * @param ruleType 规则类型
     * @param businessModule 业务模块
     * @param businessDefinitionId 业务品种ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @return 分页结果
     */
    IPage<TreasuryBusinessRule> getBusinessRulePage(int pageNum, int pageSize, String ruleCode,
                                                    String ruleName, String ruleType, String businessModule,
                                                    Long businessDefinitionId, Integer isEnabled, Long orgId);

    /**
     * 保存或更新业务活动规则
     *
     * @param businessRule 业务活动规则信息
     * @return 业务活动规则信息
     */
    TreasuryBusinessRule saveOrUpdateBusinessRule(TreasuryBusinessRule businessRule);

    /**
     * 删除业务活动规则
     * 
     * @param id 规则ID
     * @return 是否成功
     */
    boolean deleteBusinessRule(Long id);

    /**
     * 根据规则编码和组织ID查询业务活动规则
     * 
     * @param ruleCode 规则编码
     * @param orgId 组织ID
     * @return 业务活动规则
     */
    TreasuryBusinessRule getByCodeAndOrg(String ruleCode, Long orgId);

    /**
     * 查询所有启用的业务活动规则
     * 
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> getEnabledRules(Long orgId);

    /**
     * 根据规则类型查询业务活动规则列表
     * 
     * @param ruleType 规则类型
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> getByRuleType(String ruleType, Long orgId);

    /**
     * 根据业务模块查询业务活动规则列表
     * 
     * @param businessModule 业务模块
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> getByBusinessModule(String businessModule, Long orgId);

    /**
     * 根据业务品种ID查询业务活动规则列表
     * 
     * @param businessDefinitionId 业务品种ID
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> getByBusinessDefinition(Long businessDefinitionId, Long orgId);

    /**
     * 检查规则编码是否存在
     * 
     * @param ruleCode 规则编码
     * @param orgId 组织ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkRuleCodeExists(String ruleCode, Long orgId, Long excludeId);

    /**
     * 根据优先级查询业务活动规则列表
     * 
     * @param ruleType 规则类型
     * @param businessModule 业务模块
     * @param orgId 组织ID
     * @return 业务活动规则列表（按优先级排序）
     */
    List<TreasuryBusinessRule> getByPriority(String ruleType, String businessModule, Long orgId);

    /**
     * 查询规则引擎类型列表
     * 
     * @param orgId 组织ID
     * @return 规则引擎类型列表
     */
    List<String> getRuleEngineTypes(Long orgId);

    /**
     * 根据规则版本查询业务活动规则列表
     * 
     * @param ruleVersion 规则版本
     * @param orgId 组织ID
     * @return 业务活动规则列表
     */
    List<TreasuryBusinessRule> getByRuleVersion(String ruleVersion, Long orgId);

    /**
     * 批量删除业务活动规则
     * 
     * @param ids 规则ID列表
     * @return 是否成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新业务活动规则状态
     * 
     * @param id 规则ID
     * @param isEnabled 是否启用
     * @param updateUser 更新用户
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer isEnabled, Long updateUser);

    /**
     * 执行业务规则
     * 
     * @param ruleId 规则ID
     * @param context 执行上下文
     * @return 执行结果
     */
    Map<String, Object> executeRule(Long ruleId, Map<String, Object> context);

    /**
     * 验证规则脚本
     * 
     * @param ruleScript 规则脚本
     * @param ruleEngine 规则引擎类型
     * @return 验证结果
     */
    boolean validateRuleScript(String ruleScript, String ruleEngine);

    /**
     * 复制规则
     * 
     * @param sourceRuleId 源规则ID
     * @param newRuleCode 新规则编码
     * @param newRuleName 新规则名称
     * @param createUser 创建用户
     * @return 新规则信息
     */
    TreasuryBusinessRule copyRule(Long sourceRuleId, String newRuleCode, String newRuleName, Long createUser);
}
