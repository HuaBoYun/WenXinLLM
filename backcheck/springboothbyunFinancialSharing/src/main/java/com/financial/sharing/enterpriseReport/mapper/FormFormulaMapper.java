package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblFormFormula;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单公式Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormFormulaMapper extends BaseMapper<TblFormFormula> {

    /**
     * 查询表单公式列表(关联模板名称)
     * 
     * @param tenantId 租户ID
     * @param templateId 模板ID
     * @param formulaName 公式名称
     * @param formulaType 公式类型
     * @param status 状态
     * @return 表单公式列表
     */
    List<TblFormFormula> selectFormFormulaList(@Param("tenantId") String tenantId,
                                                @Param("templateId") String templateId,
                                                @Param("formulaName") String formulaName,
                                                @Param("formulaType") String formulaType,
                                                @Param("status") String status);

    /**
     * 检查公式编码是否存在
     * 
     * @param formulaCode 公式编码
     * @param tenantId 租户ID
     * @param excludeFormulaId 排除的公式ID
     * @return 数量
     */
    int checkFormulaCodeExists(@Param("formulaCode") String formulaCode,
                               @Param("tenantId") String tenantId,
                               @Param("excludeFormulaId") String excludeFormulaId);
}

