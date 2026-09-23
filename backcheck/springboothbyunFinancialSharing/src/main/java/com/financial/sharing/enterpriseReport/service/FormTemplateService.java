package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.FormTemplateQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormTemplate;

import java.util.List;

/**
 * 表单模板Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormTemplateService extends IService<TblFormTemplate> {

    /**
     * 查询表单模板列表
     * 
     * @param param 查询参数
     * @return 表单模板列表
     */
    List<TblFormTemplate> getList(FormTemplateQueryParam param);

    /**
     * 根据ID查询表单模板详情
     * 
     * @param templateId 模板ID
     * @return 表单模板详情
     */
    TblFormTemplate getDetail(String templateId);

    /**
     * 保存表单模板(新增或修改)
     * 
     * @param formTemplate 表单模板信息
     * @return 保存结果
     */
    boolean saveFormTemplate(TblFormTemplate formTemplate);

    /**
     * 删除表单模板
     * 
     * @param templateId 模板ID
     * @return 删除结果
     */
    boolean deleteFormTemplate(String templateId);

    /**
     * 设置默认版本
     * 
     * @param templateId 模板ID
     * @return 设置结果
     */
    boolean setDefaultVersion(String templateId);

    /**
     * 复制表单模板
     * 
     * @param templateId 源模板ID
     * @param newTemplateName 新模板名称
     * @param newVersionNo 新版本号
     * @return 复制结果
     */
    boolean copyTemplate(String templateId, String newTemplateName, String newVersionNo);

    /**
     * 根据表单组ID查询表单模板列表
     * 
     * @param groupId 表单组ID
     * @return 表单模板列表
     */
    List<TblFormTemplate> getListByGroupId(String groupId);
}

