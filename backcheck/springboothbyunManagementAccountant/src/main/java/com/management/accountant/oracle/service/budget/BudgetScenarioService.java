package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetScenario;

import java.util.List;

/**
 * 预算场景Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetScenarioService extends IService<BudgetScenario> {

    /**
     * 根据场景编码查询场景
     * 
     * @param scenarioCode 场景编码
     * @return 预算场景
     */
    BudgetScenario getByScenarioCode(String scenarioCode);

    /**
     * 查询默认场景
     * 
     * @return 默认场景
     */
    BudgetScenario getDefaultScenario();

    /**
     * 根据场景类型查询场景列表
     * 
     * @param scenarioType 场景类型
     * @return 场景列表
     */
    List<BudgetScenario> getByScenarioType(String scenarioType);

    /**
     * 创建预算场景
     * 
     * @param scenario 预算场景
     * @return 是否成功
     */
    boolean createScenario(BudgetScenario scenario);

    /**
     * 更新预算场景
     * 
     * @param scenario 预算场景
     * @return 是否成功
     */
    boolean updateScenario(BudgetScenario scenario);

    /**
     * 复制场景
     * 
     * @param scenarioId 场景ID
     * @param newScenarioName 新场景名称
     * @return 新场景
     */
    BudgetScenario copyScenario(String scenarioId, String newScenarioName);

    /**
     * 设置默认场景
     * 
     * @param scenarioId 场景ID
     * @return 是否成功
     */
    boolean setDefaultScenario(String scenarioId);

    /**
     * 启用/禁用场景
     * 
     * @param scenarioId 场景ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean toggleScenario(String scenarioId, boolean enabled);

    /**
     * 批量启用/禁用场景
     * 
     * @param scenarioIds 场景ID列表
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean batchToggleScenarios(List<String> scenarioIds, boolean enabled);

    /**
     * 批量删除场景
     * 
     * @param scenarioIds 场景ID列表
     * @return 是否成功
     */
    boolean batchDeleteScenarios(List<String> scenarioIds);

    /**
     * 分页查询场景列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param scenario 查询条件
     * @return 场景列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetScenario> pageQuery(
        int pageNum, int pageSize, BudgetScenario scenario);
}

