package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetFormula;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算公式Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetFormulaMapper extends BaseMapper<BudgetFormula> {

    /**
     * 根据公式编码查询公式
     * 
     * @param formulaCode 公式编码
     * @return 预算公式
     */
    BudgetFormula selectByFormulaCode(@Param("formulaCode") String formulaCode);

    /**
     * 根据公式类型查询公式列表
     * 
     * @param formulaType 公式类型
     * @return 公式列表
     */
    List<BudgetFormula> selectByFormulaType(@Param("formulaType") String formulaType);

    /**
     * 查询启用的公式列表
     * 
     * @return 公式列表
     */
    List<BudgetFormula> selectEnabledFormulas();

    /**
     * 批量启用/禁用公式
     * 
     * @param formulaIds 公式ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("formulaIds") List<String> formulaIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 批量删除公式
     *
     * @param formulaIds 公式ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("formulaIds") List<String> formulaIds);

    /**
     * 启用/禁用公式
     *
     * @param formulaId 公式ID
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int toggleFormula(@Param("formulaId") String formulaId, @Param("isEnabled") int isEnabled);

    /**
     * 批量启用/禁用公式
     *
     * @param formulaIds 公式ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchToggleFormulas(@Param("formulaIds") List<String> formulaIds, @Param("isEnabled") int isEnabled);
}

