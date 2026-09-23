package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.dto.TblBudgetControlRuleQueryParam;
import com.financial.sharing.dto.TblBudgetControlRuleSaveParam;
import com.financial.sharing.entity.TblBudgetControlRule;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算控制规则Service接口
 */
public interface TblBudgetControlRuleService extends IService<TblBudgetControlRule> {

    /**
     * 分页查询预算控制规则列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult> getList(TblBudgetControlRuleQueryParam param);

    /**
     * 根据规则ID查询详情
     *
     * @param ruleId 规则ID
     * @return 规则详情
     */
    MyJsonBean getById(String ruleId);

    /**
     * 保存或更新预算控制规则
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblBudgetControlRuleSaveParam param);

    /**
     * 删除预算控制规则
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
     * 预算检查
     *
     * @param ruleId 规则ID
     * @param amount 金额
     * @param checkData 检查数据（包含部门ID、项目ID等）
     * @return 检查结果
     */
    MyJsonBean checkBudget(String ruleId, BigDecimal amount, Map<String, Object> checkData);

    /**
     * 获取控制范围
     *
     * @param ruleId 规则ID
     * @return 控制范围
     */
    MyJsonBean getControlScopes(String ruleId);

    /**
     * 保存控制范围
     *
     * @param ruleId 规则ID
     * @param scopes 范围列表
     * @return 操作结果
     */
    MyJsonBean saveControlScopes(String ruleId, List<Map<String, String>> scopes);

    /**
     * 获取执行记录
     *
     * @param ruleId 规则ID
     * @return 执行记录
     */
    MyJsonBean getExecutionRecords(String ruleId);
}
