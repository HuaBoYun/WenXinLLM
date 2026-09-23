package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblReportTemplate;

import java.util.List;
import java.util.Map;

/**
 * 报告模板服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface ReportTemplateService {

    /**
     * 分页查询报告模板列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblReportTemplate> getTemplateList(Map<String, Object> params);

    /**
     * 根据ID查询报告模板详情
     *
     * @param templateId 模板ID
     * @return 报告模板
     */
    TblReportTemplate getTemplateById(String templateId);

    /**
     * 保存报告模板（新增或更新）
     *
     * @param template 报告模板
     * @return 保存后的报告模板
     */
    TblReportTemplate saveTemplate(TblReportTemplate template);

    /**
     * 删除报告模板
     *
     * @param templateId 模板ID
     */
    void deleteTemplate(String templateId);

    /**
     * 批量删除报告模板
     *
     * @param templateIds 模板ID列表
     */
    void batchDeleteTemplates(List<String> templateIds);

    /**
     * 根据监管机构查询模板
     *
     * @param authorityId 监管机构ID
     * @return 报告模板列表
     */
    List<TblReportTemplate> getTemplatesByAuthority(String authorityId);

    /**
     * 查询可使用的模板
     *
     * @return 报告模板列表
     */
    List<TblReportTemplate> getUsableTemplates();

    /**
     * 查询即将过期的模板
     *
     * @param days 天数
     * @return 报告模板列表
     */
    List<TblReportTemplate> getExpiringSoonTemplates(Integer days);

    /**
     * 启用/停用报告模板
     *
     * @param templateId 模板ID
     * @param isEnabled 是否启用
     */
    void toggleTemplateStatus(String templateId, Integer isEnabled);

    /**
     * 复制报告模板
     *
     * @param sourceTemplateId 源模板ID
     * @param newTemplateCode 新模板代码
     * @param newTemplateName 新模板名称
     * @return 新模板
     */
    TblReportTemplate copyTemplate(String sourceTemplateId, String newTemplateCode, String newTemplateName);

    /**
     * 创建模板新版本
     *
     * @param sourceTemplateId 源模板ID
     * @param newVersion 新版本号
     * @return 新版本模板
     */
    TblReportTemplate createTemplateVersion(String sourceTemplateId, String newVersion);
}

