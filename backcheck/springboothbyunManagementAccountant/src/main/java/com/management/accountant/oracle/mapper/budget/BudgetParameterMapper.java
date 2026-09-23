package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算参数Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetParameterMapper extends BaseMapper<BudgetParameter> {

    /**
     * 根据参数编码查询参数
     */
    BudgetParameter selectByParameterCode(@Param("parameterCode") String parameterCode);

    /**
     * 根据参数类型查询参数列表
     */
    List<BudgetParameter> selectByParameterType(@Param("parameterType") String parameterType);

    /**
     * 根据参数分类查询参数列表
     */
    List<BudgetParameter> selectByParameterCategory(@Param("parameterCategory") String parameterCategory);

    /**
     * 查询启用的参数列表
     */
    List<BudgetParameter> selectEnabledParameters();

    /**
     * 批量删除参数
     */
    int batchDeleteParameters(@Param("parameterIds") List<String> parameterIds);
}

