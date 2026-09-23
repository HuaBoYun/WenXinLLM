package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.EliminationTemplateQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationTemplate;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 抵消凭证模板Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EliminationTemplateService {

    /**
     * 查询抵消凭证模板列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblEliminationTemplate> getTemplateList(EliminationTemplateQueryParam param);

    /**
     * 根据ID查询抵消凭证模板
     * 
     * @param templateId 模板ID
     * @return 抵消凭证模板
     */
    TblEliminationTemplate getTemplateById(String templateId);

    /**
     * 新增抵消凭证模板
     * 
     * @param template 抵消凭证模板
     */
    void saveTemplate(TblEliminationTemplate template);

    /**
     * 修改抵消凭证模板
     * 
     * @param template 抵消凭证模板
     */
    void updateTemplate(TblEliminationTemplate template);

    /**
     * 删除抵消凭证模板
     * 
     * @param templateId 模板ID
     */
    void deleteTemplate(String templateId);

    /**
     * 更新模板状态
     * 
     * @param templateId 模板ID
     * @param isActive 是否启用
     */
    void updateTemplateStatus(String templateId, String isActive);

    /**
     * 根据模型ID查询抵消凭证模板列表
     * 
     * @param modelId 模型ID
     * @return 抵消凭证模板列表
     */
    List<TblEliminationTemplate> getTemplateListByModelId(String modelId);
}

