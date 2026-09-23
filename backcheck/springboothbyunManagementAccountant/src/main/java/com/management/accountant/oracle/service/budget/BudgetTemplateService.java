package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetTemplate;
import com.management.accountant.vo.param.BudgetTemplateDTO;

import java.util.List;
import java.util.Map;

/**
 * 预算模板Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetTemplateService extends IService<BudgetTemplate> {

    /**
     * 根据模板编码查询模板
     * 
     * @param templateCode 模板编码
     * @return 预算模板
     */
    BudgetTemplate getByTemplateCode(String templateCode);

    /**
     * 查询默认模板
     * 
     * @return 默认模板
     */
    BudgetTemplate getDefaultTemplate();

    /**
     * 根据模板类型查询模板列表
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<BudgetTemplate> getByTemplateType(String templateType);

    /**
     * 创建预算模板
     * 
     * @param template 预算模板
     * @return 是否成功
     */
    boolean createTemplate(BudgetTemplate template);

    /**
     * 更新预算模板
     * 
     * @param template 预算模板
     * @return 是否成功
     */
    boolean updateTemplate(BudgetTemplate template);

    /**
     * 复制模板
     * 
     * @param templateId 模板ID
     * @param newTemplateName 新模板名称
     * @return 新模板
     */
    BudgetTemplate copyTemplate(String templateId, String newTemplateName);

    /**
     * 设置默认模板
     * 
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean setDefaultTemplate(String templateId);

    /**
     * 启用/禁用模板
     * 
     * @param templateId 模板ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean toggleTemplate(String templateId, boolean enabled);

    /**
     * 批量启用/禁用模板
     * 
     * @param templateIds 模板ID列表
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean batchToggleTemplates(List<String> templateIds, boolean enabled);

    /**
     * 批量删除模板
     * 
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchDeleteTemplates(List<String> templateIds);

    /**
     * 分页查询模板列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param template 查询条件
     * @return 模板列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetTemplate> pageQuery(
        int pageNum, int pageSize, BudgetTemplate template);

    /**
     * 保存模板
     *
     * @param dto 模板DTO
     * @return 是否成功
     */
    boolean saveTemplate(BudgetTemplateDTO dto);

    /**
     * 应用模板
     *
     * @param templateId 模板ID
     * @param targetId 目标ID
     * @return 是否成功
     */
    boolean applyTemplate(String templateId, String targetId);

    /**
     * 复制模板(单参数)
     *
     * @param templateId 模板ID
     * @return 新模板
     */
    BudgetTemplate copyTemplate(String templateId);

    /**
     * 根据类型列出模板
     *
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<BudgetTemplate> listByType(String templateType);

    /**
     * 获取模板详情
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    Map<String, Object> getTemplateDetail(String templateId);

    /**
     * 切换状态
     *
     * @param templateId 模板ID
     * @param status 状态
     * @return 是否成功
     */
    boolean toggleStatus(String templateId, Integer status);

    /**
     * 列出系统模板
     *
     * @return 系统模板列表
     */
    List<BudgetTemplate> listSystemTemplates();

    /**
     * 删除模板
     *
     * @param templateId 模板ID
     */
    void deleteTemplate(String templateId);

    /**
     * 分页查询模板列表
     *
     * @param params 查询参数
     * @return 模板列表
     */
    Object listTemplates(Map<String, Object> params);
}

