package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.eps.EpsBudgetTemplate;

import java.util.List;

/**
 * 预算模板管理服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetTemplateService extends IService<EpsBudgetTemplate> {

    /**
     * 分页查询预算模板
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param templateName 模板名称（模糊查询）
     * @param templateType 模板类型
     * @param templateCategory 模板分类
     * @param systemId 预算体系ID
     * @param status 状态
     * @return 分页结果
     */
    IPage<EpsBudgetTemplate> queryBudgetTemplatePage(Long current, Long size, String templateName, 
                                                   String templateType, String templateCategory, 
                                                   Long systemId, String status);

    /**
     * 创建预算模板
     * 
     * @param budgetTemplate 预算模板信息
     * @return 创建结果
     */
    boolean createBudgetTemplate(EpsBudgetTemplate budgetTemplate);

    /**
     * 更新预算模板
     * 
     * @param budgetTemplate 预算模板信息
     * @return 更新结果
     */
    boolean updateBudgetTemplate(EpsBudgetTemplate budgetTemplate);

    /**
     * 删除预算模板
     * 
     * @param templateId 模板ID
     * @return 删除结果
     */
    boolean deleteBudgetTemplate(Long templateId);

    /**
     * 批量删除预算模板
     * 
     * @param templateIds 模板ID列表
     * @return 删除结果
     */
    boolean batchDeleteBudgetTemplate(List<Long> templateIds);

    /**
     * 根据ID查询预算模板详情
     * 
     * @param templateId 模板ID
     * @return 预算模板详情
     */
    EpsBudgetTemplate getBudgetTemplateById(Long templateId);

    /**
     * 根据模板编码查询预算模板
     * 
     * @param templateCode 模板编码
     * @return 预算模板
     */
    EpsBudgetTemplate getBudgetTemplateByCode(String templateCode);

    /**
     * 根据预算体系ID查询模板列表
     * 
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> getBudgetTemplatesBySystemId(Long systemId);

    /**
     * 查询默认模板
     * 
     * @param systemId 预算体系ID
     * @param templateCategory 模板分类
     * @return 默认模板
     */
    EpsBudgetTemplate getDefaultBudgetTemplate(Long systemId, String templateCategory);

    /**
     * 设置默认模板
     * 
     * @param templateId 模板ID
     * @param systemId 预算体系ID
     * @param templateCategory 模板分类
     * @return 设置结果
     */
    boolean setDefaultBudgetTemplate(Long templateId, Long systemId, String templateCategory);

    /**
     * 激活预算模板
     * 
     * @param templateId 模板ID
     * @return 激活结果
     */
    boolean activateBudgetTemplate(Long templateId);

    /**
     * 停用预算模板
     * 
     * @param templateId 模板ID
     * @return 停用结果
     */
    boolean deactivateBudgetTemplate(Long templateId);

    /**
     * 归档预算模板
     * 
     * @param templateId 模板ID
     * @return 归档结果
     */
    boolean archiveBudgetTemplate(Long templateId);

    /**
     * 复制预算模板
     * 
     * @param sourceTemplateId 源模板ID
     * @param targetTemplateCode 目标模板编码
     * @param targetTemplateName 目标模板名称
     * @return 复制结果
     */
    boolean copyBudgetTemplate(Long sourceTemplateId, String targetTemplateCode, String targetTemplateName);

    /**
     * 检查模板编码是否存在
     * 
     * @param templateCode 模板编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 是否存在
     */
    boolean checkTemplateCodeExists(String templateCode, Long excludeId);

    /**
     * 批量更新状态
     * 
     * @param templateIds 模板ID列表
     * @param status 状态
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> templateIds, String status, Long updatedBy);

    /**
     * 根据模板类型查询模板
     * 
     * @param templateType 模板类型
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> getBudgetTemplatesByType(String templateType, Long systemId);

    /**
     * 根据模板分类查询模板
     * 
     * @param templateCategory 模板分类
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> getBudgetTemplatesByCategory(String templateCategory, Long systemId);

    /**
     * 更新使用次数
     * 
     * @param templateId 模板ID
     * @return 更新结果
     */
    boolean updateUsageCount(Long templateId);

    /**
     * 查询热门模板
     * 
     * @param systemId 预算体系ID
     * @param limit 限制数量
     * @return 热门模板列表
     */
    List<EpsBudgetTemplate> getPopularTemplates(Long systemId, Integer limit);

    /**
     * 查询最近使用的模板
     * 
     * @param systemId 预算体系ID
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用的模板列表
     */
    List<EpsBudgetTemplate> getRecentlyUsedTemplates(Long systemId, Long userId, Integer limit);

    /**
     * 根据标签查询模板
     * 
     * @param tags 标签列表
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> getBudgetTemplatesByTags(List<String> tags, Long systemId);

    /**
     * 查询共享模板
     * 
     * @param systemId 预算体系ID
     * @return 共享模板列表
     */
    List<EpsBudgetTemplate> getSharedTemplates(Long systemId);

    /**
     * 查询系统模板
     * 
     * @return 系统模板列表
     */
    List<EpsBudgetTemplate> getSystemTemplates();

    /**
     * 导入模板
     * 
     * @param templateData 模板数据
     * @param systemId 预算体系ID
     * @return 导入结果
     */
    boolean importTemplate(String templateData, Long systemId);

    /**
     * 导出模板
     * 
     * @param templateId 模板ID
     * @return 模板数据
     */
    String exportTemplate(Long templateId);

    /**
     * 验证模板结构
     * 
     * @param templateStructure 模板结构
     * @return 验证结果
     */
    boolean validateTemplateStructure(String templateStructure);
}
