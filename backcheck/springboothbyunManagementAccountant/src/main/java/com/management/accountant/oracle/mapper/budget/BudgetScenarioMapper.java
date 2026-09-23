package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetScenario;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算场景Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetScenarioMapper extends BaseMapper<BudgetScenario> {

    /**
     * 根据场景编码查询场景
     * 
     * @param scenarioCode 场景编码
     * @return 预算场景
     */
    BudgetScenario selectByScenarioCode(@Param("scenarioCode") String scenarioCode);

    /**
     * 查询默认场景
     * 
     * @return 默认场景
     */
    BudgetScenario selectDefaultScenario();

    /**
     * 根据场景类型查询场景列表
     * 
     * @param scenarioType 场景类型
     * @return 场景列表
     */
    List<BudgetScenario> selectByScenarioType(@Param("scenarioType") String scenarioType);

    /**
     * 批量启用/禁用场景
     *
     * @param scenarioIds 场景ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("scenarioIds") List<String> scenarioIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 清除默认场景
     *
     * @return 更新数量
     */
    int clearDefaultScenario();

    /**
     * 设置默认场景
     *
     * @param scenarioId 场景ID
     * @return 更新数量
     */
    int setDefaultScenario(@Param("scenarioId") String scenarioId);

    /**
     * 切换场景启用状态
     *
     * @param scenarioId 场景ID
     * @param status 状态
     * @return 更新数量
     */
    int toggleScenario(@Param("scenarioId") String scenarioId, @Param("status") int status);

    /**
     * 批量切换场景启用状态
     *
     * @param scenarioIds 场景ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchToggleScenarios(@Param("scenarioIds") List<String> scenarioIds, @Param("status") int status);

    /**
     * 批量删除场景
     *
     * @param scenarioIds 场景ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("scenarioIds") List<String> scenarioIds);
}

