package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.dto.TblAuditRuleQueryParam;
import com.financial.sharing.dto.TblAuditRuleSaveParam;
import com.financial.sharing.entity.TblAuditRule;
import com.financial.sharing.entity.TblAuditRuleCondition;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 审批规则Service接口
 */
public interface TblAuditRuleService extends IService<TblAuditRule> {

    /**
     * 分页查询审批规则列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult> getList(TblAuditRuleQueryParam param);

    /**
     * 根据规则ID查询详情
     *
     * @param ruleId 规则ID
     * @return 规则详情
     */
    MyJsonBean getById(String ruleId);

    /**
     * 保存或更新审批规则
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblAuditRuleSaveParam param);

    /**
     * 删除审批规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    MyJsonBean delete(String ruleId);

    /**
     * 更新规则状态
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    MyJsonBean updateStatus(String ruleId, Integer isEnabled);

    /**
     * 测试规则
     *
     * @param ruleId 规则ID
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean testRule(String ruleId, Map<String, Object> testData);

    /**
     * 获取规则条件列表
     *
     * @param ruleId 规则ID
     * @return 条件列表
     */
    MyJsonBean getRuleConditions(String ruleId);

    /**
     * 获取规则动作列表
     *
     * @param ruleId 规则ID
     * @return 动作列表
     */
    MyJsonBean getRuleActions(String ruleId);

    /**
     * 获取规则执行日志
     *
     * @param ruleId 规则ID
     * @return 执行日志列表
     */
    MyJsonBean getExecutionLogs(String ruleId);

    /**
     * 保存单个条件
     *
     * @param condition 条件对象
     * @return 操作结果
     */
    MyJsonBean saveCondition(TblAuditRuleCondition condition);

    /**
     * 删除单个条件
     *
     * @param conditionId 条件ID
     * @return 操作结果
     */
    MyJsonBean deleteCondition(String conditionId);
}
