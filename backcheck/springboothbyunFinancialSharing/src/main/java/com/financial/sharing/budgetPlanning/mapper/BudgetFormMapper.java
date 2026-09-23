package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetFormQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算表单配置Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetFormMapper extends BaseMapper<TblBudgetForm> {

    /**
     * 查询预算表单配置列表
     * 
     * @param param 查询参数
     * @return 预算表单配置列表
     */
    List<TblBudgetForm> selectFormList(@Param("param") BudgetFormQueryParam param);

    /**
     * 根据表单编码查询预算表单配置
     * 
     * @param formCode 表单编码
     * @return 预算表单配置
     */
    TblBudgetForm selectByFormCode(@Param("formCode") String formCode);

    /**
     * 检查表单编码是否存在
     * 
     * @param formCode 表单编码
     * @param excludeId 排除的表单ID(用于编辑时排除自己)
     * @return 存在数量
     */
    int checkFormCodeExists(@Param("formCode") String formCode, @Param("excludeId") String excludeId);
}

