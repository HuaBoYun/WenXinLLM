package com.financial.sharing.budgetControl.service;

import com.financial.sharing.budgetControl.dto.ControlStrategyConfigDTO;
import com.financial.sharing.budgetControl.dto.ControlStrategyQueryParam;
import com.financial.sharing.util.MyJsonBean;

/**
 * 控制策略配置Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface ControlStrategyService {

    /**
     * 分页查询控制策略
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryPage(ControlStrategyQueryParam param);

    /**
     * 根据规则ID查询控制策略配置
     *
     * @param ruleId 规则ID
     * @return 控制策略配置
     */
    MyJsonBean queryByRuleId(String ruleId);

    /**
     * 保存控制策略配置
     *
     * @param config 控制策略配置
     * @return 操作结果
     */
    MyJsonBean saveStrategy(ControlStrategyConfigDTO config);

    /**
     * 测试控制策略
     *
     * @param config 控制策略配置
     * @param testAmount 测试金额
     * @param testOrgId 测试组织ID
     * @param testSubjectCode 测试科目编码
     * @param testPeriod 测试期间
     * @return 测试结果
     */
    MyJsonBean testStrategy(ControlStrategyConfigDTO config, String testAmount, 
                           String testOrgId, String testSubjectCode, String testPeriod);

    /**
     * 复制控制策略
     *
     * @param ruleId 源规则ID
     * @param newRuleCode 新规则编码
     * @param newRuleName 新规则名称
     * @return 操作结果
     */
    MyJsonBean copyStrategy(String ruleId, String newRuleCode, String newRuleName);
}

