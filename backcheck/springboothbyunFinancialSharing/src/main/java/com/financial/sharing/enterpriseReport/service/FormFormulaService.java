package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.FormFormulaQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormFormula;

import java.util.List;

/**
 * 表单公式Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormFormulaService extends IService<TblFormFormula> {

    /**
     * 查询表单公式列表
     * 
     * @param param 查询参数
     * @return 表单公式列表
     */
    List<TblFormFormula> getList(FormFormulaQueryParam param);

    /**
     * 根据ID查询表单公式详情
     * 
     * @param formulaId 公式ID
     * @return 表单公式详情
     */
    TblFormFormula getDetail(String formulaId);

    /**
     * 保存表单公式(新增或修改)
     * 
     * @param formFormula 表单公式信息
     * @return 保存结果
     */
    boolean saveFormFormula(TblFormFormula formFormula);

    /**
     * 删除表单公式
     * 
     * @param formulaId 公式ID
     * @return 删除结果
     */
    boolean deleteFormFormula(String formulaId);

    /**
     * 根据模板ID查询表单公式列表
     * 
     * @param templateId 模板ID
     * @return 表单公式列表
     */
    List<TblFormFormula> getListByTemplateId(String templateId);

    /**
     * 批量删除表单公式
     * 
     * @param formulaIds 公式ID列表
     * @return 删除结果
     */
    boolean batchDelete(List<String> formulaIds);
}

