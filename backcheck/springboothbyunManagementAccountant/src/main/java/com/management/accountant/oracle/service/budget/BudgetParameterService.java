package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetParameter;

import java.util.List;

/**
 * 预算参数Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetParameterService extends IService<BudgetParameter> {

    /**
     * 根据参数编码查询参数
     */
    BudgetParameter getByParameterCode(String parameterCode);

    /**
     * 根据参数类型查询参数列表
     */
    List<BudgetParameter> listByParameterType(String parameterType);

    /**
     * 根据参数分类查询参数列表
     */
    List<BudgetParameter> listByParameterCategory(String parameterCategory);

    /**
     * 查询启用的参数列表
     */
    List<BudgetParameter> listEnabledParameters();

    /**
     * 批量删除参数
     */
    boolean batchDeleteParameters(List<String> parameterIds);

    /**
     * 保存或更新参数
     */
    boolean saveOrUpdateParameter(BudgetParameter parameter);
}

