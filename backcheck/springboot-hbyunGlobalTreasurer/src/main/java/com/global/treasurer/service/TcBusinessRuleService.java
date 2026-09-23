package com.global.treasurer.service;

import com.global.treasurer.entity.TcBusinessRule;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 业务规则管理Service
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcBusinessRuleService {

    /**
     * 分页查询业务规则列表
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param ruleCode 规则编码
     * @param ruleName 规则名称
     * @param ruleType 规则类型
     * @param ruleCategory 规则分类
     * @param businessDomain 业务域
     * @param isActive 是否激活
     * @param status 状态
     * @return 分页结果
     */
    PageInfo<TcBusinessRule> getList(int pageNum, int pageSize, String ruleCode, String ruleName, String ruleType,
                                     String ruleCategory, String businessDomain, String isActive, String status);

    /**
     * 新增或更新业务规则
     * 
     * @param businessRule 业务规则信息
     * @return 保存后的业务规则信息
     */
    TcBusinessRule saveOrUpdate(TcBusinessRule businessRule);

    /**
     * 根据ID删除业务规则
     * 
     * @param id 规则ID
     */
    void delete(String id);

    /**
     * 根据ID查询业务规则详情
     * 
     * @param id 规则ID
     * @return 业务规则信息
     */
    TcBusinessRule getById(String id);

    /**
     * 根据规则编码查询
     * 
     * @param ruleCode 规则编码
     * @return 业务规则信息
     */
    TcBusinessRule getByRuleCode(String ruleCode);

    /**
     * 根据规则类型查询
     * 
     * @param ruleType 规则类型
     * @return 业务规则列表
     */
    List<TcBusinessRule> getByRuleType(String ruleType);

    /**
     * 根据业务域查询
     * 
     * @param businessDomain 业务域
     * @return 业务规则列表
     */
    List<TcBusinessRule> getByBusinessDomain(String businessDomain);

    /**
     * 根据规则分类查询
     * 
     * @param ruleCategory 规则分类
     * @return 业务规则列表
     */
    List<TcBusinessRule> getByRuleCategory(String ruleCategory);

    /**
     * 查询激活的规则
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 激活的业务规则列表
     */
    List<TcBusinessRule> getActiveRules(String ruleType, String businessDomain);

    /**
     * 根据执行顺序查询规则
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 按执行顺序排序的业务规则列表
     */
    List<TcBusinessRule> getByExecutionOrder(String ruleType, String businessDomain);

    /**
     * 查询有效期内的规则
     * 
     * @param currentDate 当前日期
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 有效期内的业务规则列表
     */
    List<TcBusinessRule> getValidRules(Date currentDate, String ruleType, String businessDomain);

    /**
     * 查询所有规则类型
     * 
     * @return 规则类型列表
     */
    List<String> getAllRuleTypes();

    /**
     * 查询所有规则分类
     * 
     * @return 规则分类列表
     */
    List<String> getAllRuleCategories();

    /**
     * 查询所有业务域
     * 
     * @return 业务域列表
     */
    List<String> getAllBusinessDomains();

    /**
     * 查询所有规则语法
     * 
     * @return 规则语法列表
     */
    List<String> getAllRuleSyntax();

    /**
     * 批量导入业务规则
     * 
     * @param businessRules 业务规则列表
     * @param updateMode 更新模式：INSERT-仅插入，UPDATE-仅更新，UPSERT-插入或更新
     * @return 导入结果
     */
    Map<String, Object> batchImport(List<TcBusinessRule> businessRules, String updateMode);

    /**
     * 批量更新业务规则
     * 
     * @param businessRules 业务规则列表
     * @return 更新结果
     */
    Map<String, Object> batchUpdate(List<TcBusinessRule> businessRules);

    /**
     * 更新规则状态
     * 
     * @param id 规则ID
     * @param status 状态
     * @param updateUser 更新人
     */
    void updateStatus(String id, String status, String updateUser);

    /**
     * 更新规则激活状态
     * 
     * @param id 规则ID
     * @param isActive 是否激活
     * @param updateUser 更新人
     */
    void updateActiveStatus(String id, String isActive, String updateUser);

    /**
     * 获取规则统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getStatistics();

    /**
     * 检查规则编码是否存在
     * 
     * @param ruleCode 规则编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkRuleCodeExists(String ruleCode, String excludeId);

    /**
     * 根据规则类型和业务域统计规则数量
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 规则数量
     */
    int countByTypeAndDomain(String ruleType, String businessDomain);

    /**
     * 查询即将过期的规则
     * 
     * @param days 天数
     * @return 即将过期的规则列表
     */
    List<TcBusinessRule> getExpiringRules(int days);

    /**
     * 执行业务规则
     * 
     * @param ruleCode 规则编码
     * @param inputData 输入数据
     * @param businessKey 业务键
     * @param businessType 业务类型
     * @param executionUser 执行人
     * @return 执行结果
     */
    Map<String, Object> executeRule(String ruleCode, Map<String, Object> inputData, String businessKey, 
                                    String businessType, String executionUser);

    /**
     * 批量执行业务规则
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @param inputData 输入数据
     * @param businessKey 业务键
     * @param businessType 业务类型
     * @param executionUser 执行人
     * @return 执行结果
     */
    Map<String, Object> executeRules(String ruleType, String businessDomain, Map<String, Object> inputData, 
                                     String businessKey, String businessType, String executionUser);

    /**
     * 验证规则表达式
     * 
     * @param ruleExpression 规则表达式
     * @param ruleSyntax 规则语法
     * @param testData 测试数据
     * @return 验证结果
     */
    Map<String, Object> validateRuleExpression(String ruleExpression, String ruleSyntax, Map<String, Object> testData);

    /**
     * 复制规则
     * 
     * @param sourceRuleId 源规则ID
     * @param newRuleCode 新规则编码
     * @param newRuleName 新规则名称
     * @param createUser 创建人
     * @return 复制后的规则信息
     */
    TcBusinessRule copyRule(String sourceRuleId, String newRuleCode, String newRuleName, String createUser);
}
