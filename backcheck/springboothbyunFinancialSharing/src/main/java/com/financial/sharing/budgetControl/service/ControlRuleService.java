package com.financial.sharing.budgetControl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.budgetControl.dto.ControlRuleQueryParam;
import com.financial.sharing.budgetControl.entity.TblControlRule;
import com.financial.sharing.util.MyJsonBean;

/**
 * 控制规则Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface ControlRuleService extends IService<TblControlRule> {

    /**
     * 分页查询控制规则
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryPage(ControlRuleQueryParam param);

    /**
     * 根据ID查询控制规则
     *
     * @param ruleId 规则ID
     * @return 控制规则
     */
    MyJsonBean queryById(String ruleId);

    /**
     * 新增控制规则
     *
     * @param rule 控制规则
     * @return 操作结果
     */
    MyJsonBean add(TblControlRule rule);

    /**
     * 修改控制规则
     *
     * @param rule 控制规则
     * @return 操作结果
     */
    MyJsonBean modify(TblControlRule rule);

    /**
     * 删除控制规则
     *
     * @param ruleId 规则ID
     * @return 操作结果
     */
    MyJsonBean remove(String ruleId);

    /**
     * 启用/禁用控制规则
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    MyJsonBean toggleStatus(String ruleId, String isEnabled);
}

