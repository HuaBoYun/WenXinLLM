package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetModelQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算模型Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetModelMapper extends BaseMapper<TblBudgetModel> {

    /**
     * 查询预算模型列表
     * 
     * @param param 查询参数
     * @return 预算模型列表
     */
    List<TblBudgetModel> selectModelList(@Param("param") BudgetModelQueryParam param);

    /**
     * 根据模型编码查询预算模型
     * 
     * @param modelCode 模型编码
     * @return 预算模型
     */
    TblBudgetModel selectByModelCode(@Param("modelCode") String modelCode);

    /**
     * 检查模型编码是否存在
     * 
     * @param modelCode 模型编码
     * @param excludeId 排除的模型ID(用于编辑时排除自己)
     * @return 存在数量
     */
    int checkModelCodeExists(@Param("modelCode") String modelCode, @Param("excludeId") String excludeId);
}

