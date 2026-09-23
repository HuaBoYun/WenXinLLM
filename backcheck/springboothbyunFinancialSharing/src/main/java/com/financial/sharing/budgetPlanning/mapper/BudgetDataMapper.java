package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetDataQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算数据编制Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetDataMapper extends BaseMapper<TblBudgetData> {

    /**
     * 查询预算数据列表
     * 
     * @param param 查询参数
     * @return 预算数据列表
     */
    List<TblBudgetData> selectDataList(@Param("param") BudgetDataQueryParam param);

    /**
     * 检查数据是否存在(同一模型、表单、期间、版本、科目、主体下只能有一条数据)
     * 
     * @param modelId 模型ID
     * @param formId 表单ID
     * @param period 期间
     * @param version 版本
     * @param subjectCode 科目编码
     * @param organizationCode 主体编码
     * @param excludeId 排除的数据ID(修改时使用)
     * @return 数量
     */
    int checkDataExists(@Param("modelId") String modelId,
                       @Param("formId") String formId,
                       @Param("period") String period,
                       @Param("version") String version,
                       @Param("subjectCode") String subjectCode,
                       @Param("organizationCode") String organizationCode,
                       @Param("excludeId") String excludeId);
}

