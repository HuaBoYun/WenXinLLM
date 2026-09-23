package com.huabo.fxgl.service;

import java.util.List;
import java.util.Map;

/**
 * 表达式模板管理服务接口
 * 
 * @author AI Assistant
 * @date 2025-01-21
 * @version 1.0
 */
public interface ExpressionTemplateService {
    
    /**
     * 获取中文公式模板列表
     * 
     * @param category 模板分类，可选
     * @return 模板列表
     */
    List<Map<String, Object>> getChineseFormulaTemplates(String category);
    
    /**
     * 根据ID获取模板详情
     * 
     * @param templateId 模板ID
     * @return 模板详情
     */
    Map<String, Object> getTemplateById(String templateId);
    
    /**
     * 保存中文公式模板
     * 
     * @param templateData 模板数据
     * @return 保存结果
     */
    Map<String, Object> saveChineseFormulaTemplate(Map<String, Object> templateData);
    
    /**
     * 更新模板使用次数
     * 
     * @param templateId 模板ID
     */
    void incrementTemplateUsage(String templateId);
    
    /**
     * 获取热门模板
     * 
     * @param limit 返回数量限制
     * @return 热门模板列表
     */
    List<Map<String, Object>> getPopularTemplates(int limit);
    
    /**
     * 搜索模板
     * 
     * @param keyword 搜索关键词
     * @param category 模板分类
     * @return 搜索结果
     */
    List<Map<String, Object>> searchTemplates(String keyword, String category);
    
    /**
     * 删除模板
     * 
     * @param templateId 模板ID
     * @return 删除结果
     */
    boolean deleteTemplate(String templateId);
    
    /**
     * 启用/禁用模板
     * 
     * @param templateId 模板ID
     * @param enabled 是否启用
     * @return 操作结果
     */
    boolean toggleTemplateStatus(String templateId, boolean enabled);
    
    /**
     * 获取模板分类列表
     * 
     * @return 分类列表
     */
    List<Map<String, Object>> getTemplateCategories();
    
    /**
     * 验证模板公式
     * 
     * @param chineseFormula 中文公式
     * @param sqlTemplate SQL模板
     * @return 验证结果
     */
    Map<String, Object> validateTemplate(String chineseFormula, String sqlTemplate);
    
    /**
     * 导出模板
     * 
     * @param templateIds 模板ID列表
     * @return 导出数据
     */
    Map<String, Object> exportTemplates(List<String> templateIds);
    
    /**
     * 导入模板
     *
     * @param templateData 模板数据
     * @return 导入结果
     */
    Map<String, Object> importTemplates(List<Map<String, Object>> templateData);

    /**
     * 获取模板列表（支持分页和过滤）
     *
     * @param params 查询参数
     * @return 模板列表
     */
    List<Map<String, Object>> getTemplateList(Map<String, Object> params);
}
