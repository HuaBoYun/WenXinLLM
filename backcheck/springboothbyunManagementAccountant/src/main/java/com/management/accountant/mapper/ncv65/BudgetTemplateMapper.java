package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算模板数据访问接口
 * 
 * @description 预算模板数据访问层，提供预算模板的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetTemplateMapper extends BaseMapper<BudgetTemplate> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据模板编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_CODE = #{templateCode} AND IS_DELETED = 0")
    BudgetTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 根据模板类型查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_TYPE = #{templateType} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByTemplateType(@Param("templateType") String templateType);

    /**
     * 根据模板分类查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_CATEGORY = #{templateCategory} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByTemplateCategory(@Param("templateCategory") String templateCategory);

    /**
     * 根据模板用途查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_PURPOSE = #{templatePurpose} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByTemplatePurpose(@Param("templatePurpose") String templatePurpose);

    /**
     * 根据适用范围查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE APPLICABLE_SCOPE = #{applicableScope} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByApplicableScope(@Param("applicableScope") String applicableScope);

    /**
     * 根据模板状态查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_STATUS = #{templateStatus} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByTemplateStatus(@Param("templateStatus") String templateStatus);

    /**
     * 根据审批状态查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE APPROVAL_STATUS = #{approvalStatus} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    /**
     * 根据发布状态查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE PUBLISH_STATUS = #{publishStatus} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByPublishStatus(@Param("publishStatus") String publishStatus);

    /**
     * 根据模板版本查询模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_VERSION = #{templateVersion} AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectByTemplateVersion(@Param("templateVersion") String templateVersion);

    /**
     * 查询默认模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE IS_DEFAULT = 1 AND TEMPLATE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectDefaultTemplates();

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算模板
     */
    IPage<BudgetTemplate> selectBudgetTemplatePage(Page<BudgetTemplate> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectActiveTemplates();

    /**
     * 查询我创建的模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE CREATE_BY = #{userId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetTemplate> selectMyTemplates(@Param("userId") String userId);

    /**
     * 查询待我审批的模板列表
     */
    List<BudgetTemplate> selectPendingApprovals(@Param("userId") String userId);

    /**
     * 查询已发布的模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE PUBLISH_STATUS = 'PUBLISHED' AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC")
    List<BudgetTemplate> selectPublishedTemplates();

    /**
     * 查询系统模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE IS_SYSTEM = 1 AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectSystemTemplates();

    /**
     * 查询自定义模板列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE IS_SYSTEM = 0 AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectCustomTemplates();

    /**
     * 查询可用的模板列表
     */
    List<BudgetTemplate> selectAvailableTemplates(@Param("templateType") String templateType, 
                                                 @Param("organizationId") String organizationId,
                                                 @Param("userId") String userId);

    /**
     * 查询模板版本列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_CODE = #{templateCode} AND IS_DELETED = 0 ORDER BY TEMPLATE_VERSION DESC")
    List<BudgetTemplate> selectTemplateVersions(@Param("templateCode") String templateCode);

    /**
     * 查询最新版本模板
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_CODE = #{templateCode} AND IS_DELETED = 0 ORDER BY TEMPLATE_VERSION DESC LIMIT 1")
    BudgetTemplate selectLatestTemplateVersion(@Param("templateCode") String templateCode);

    /**
     * 查询模板统计信息
     */
    Map<String, Object> selectTemplateStatistics();

    /**
     * 根据关键字搜索模板
     */
    List<BudgetTemplate> searchTemplates(@Param("keyword") String keyword);

    /**
     * 查询热门模板
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE USAGE_COUNT > 0 AND TEMPLATE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY USAGE_COUNT DESC LIMIT #{limit}")
    List<BudgetTemplate> selectPopularTemplates(@Param("limit") Integer limit);

    /**
     * 查询最新模板
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC LIMIT #{limit}")
    List<BudgetTemplate> selectLatestTemplates(@Param("limit") Integer limit);

    /**
     * 查询推荐模板
     */
    @Select("SELECT * FROM NCV65_BUDGET_TEMPLATE WHERE IS_RECOMMENDED = 1 AND TEMPLATE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY SORT_ORDER, TEMPLATE_CODE")
    List<BudgetTemplate> selectRecommendedTemplates();

    // ==================== 业务操作方法 ====================

    /**
     * 激活模板
     */
    int activateTemplate(@Param("templateId") String templateId, @Param("updateBy") String updateBy);

    /**
     * 停用模板
     */
    int deactivateTemplate(@Param("templateId") String templateId, @Param("updateBy") String updateBy);

    /**
     * 归档模板
     */
    int archiveTemplate(@Param("templateId") String templateId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveTemplate(@Param("templateId") String templateId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 审批拒绝
     */
    int rejectTemplate(@Param("templateId") String templateId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 发布模板
     */
    int publishTemplate(@Param("templateId") String templateId, @Param("publishedBy") String publishedBy);

    /**
     * 取消发布
     */
    int unpublishTemplate(@Param("templateId") String templateId, @Param("updateBy") String updateBy);

    /**
     * 设置默认模板
     */
    int setDefaultTemplate(@Param("templateId") String templateId, @Param("templateType") String templateType, @Param("updateBy") String updateBy);

    /**
     * 清除默认模板标记
     */
    int clearDefaultTemplate(@Param("templateType") String templateType, @Param("updateBy") String updateBy);

    /**
     * 复制模板
     */
    int copyTemplate(@Param("sourceTemplateId") String sourceTemplateId, @Param("newTemplateCode") String newTemplateCode, @Param("newTemplateName") String newTemplateName, @Param("createBy") String createBy);

    /**
     * 增加使用次数
     */
    int incrementUsageCount(@Param("templateId") String templateId);

    /**
     * 增加下载次数
     */
    int incrementDownloadCount(@Param("templateId") String templateId);

    /**
     * 批量更新模板状态
     */
    int batchUpdateTemplateStatus(@Param("templateIds") List<String> templateIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量审批模板
     */
    int batchApproveTemplates(@Param("templateIds") List<String> templateIds, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 批量发布模板
     */
    int batchPublishTemplates(@Param("templateIds") List<String> templateIds, @Param("publishedBy") String publishedBy);

    /**
     * 批量归档模板
     */
    int batchArchiveTemplates(@Param("templateIds") List<String> templateIds, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计模板总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE IS_DELETED = 0")
    int countTotalTemplates();

    /**
     * 按模板类型统计数量
     */
    List<Map<String, Object>> countTemplatesByType();

    /**
     * 按模板分类统计数量
     */
    List<Map<String, Object>> countTemplatesByCategory();

    /**
     * 按模板用途统计数量
     */
    List<Map<String, Object>> countTemplatesByPurpose();

    /**
     * 按模板状态统计数量
     */
    List<Map<String, Object>> countTemplatesByStatus();

    /**
     * 按审批状态统计数量
     */
    List<Map<String, Object>> countTemplatesByApprovalStatus();

    /**
     * 按发布状态统计数量
     */
    List<Map<String, Object>> countTemplatesByPublishStatus();

    /**
     * 按适用范围统计数量
     */
    List<Map<String, Object>> countTemplatesByApplicableScope();

    /**
     * 统计使用次数汇总
     */
    Map<String, Object> sumTemplateUsageCount();

    /**
     * 统计下载次数汇总
     */
    Map<String, Object> sumTemplateDownloadCount();

    /**
     * 统计系统模板数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE IS_SYSTEM = 1 AND IS_DELETED = 0")
    int countSystemTemplates();

    /**
     * 统计自定义模板数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE IS_SYSTEM = 0 AND IS_DELETED = 0")
    int countCustomTemplates();

    /**
     * 统计默认模板数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE IS_DEFAULT = 1 AND IS_DELETED = 0")
    int countDefaultTemplates();

    /**
     * 统计推荐模板数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE IS_RECOMMENDED = 1 AND IS_DELETED = 0")
    int countRecommendedTemplates();

    // ==================== 数据验证方法 ====================

    /**
     * 检查模板编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_CODE = #{templateCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkTemplateCodeExists(@Param("templateCode") String templateCode, @Param("excludeId") String excludeId);

    /**
     * 检查模板名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_NAME = #{templateName} AND TEMPLATE_TYPE = #{templateType} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkTemplateNameExists(@Param("templateName") String templateName, @Param("templateType") String templateType, @Param("excludeId") String excludeId);

    /**
     * 检查是否存在默认模板
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_TYPE = #{templateType} AND IS_DEFAULT = 1 AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkDefaultTemplateExists(@Param("templateType") String templateType, @Param("excludeId") String excludeId);

    /**
     * 检查模板是否可以删除
     */
    boolean checkTemplateCanDelete(@Param("templateId") String templateId);

    /**
     * 检查模板是否可以修改
     */
    boolean checkTemplateCanModify(@Param("templateId") String templateId);

    /**
     * 检查模板是否在使用中
     */
    boolean checkTemplateInUse(@Param("templateId") String templateId);

    /**
     * 检查模板权限
     */
    boolean checkTemplatePermission(@Param("templateId") String templateId, @Param("userId") String userId, @Param("operation") String operation);

    /**
     * 验证模板内容
     */
    boolean validateTemplateContent(@Param("templateContent") String templateContent);

    /**
     * 验证模板结构
     */
    boolean validateTemplateStructure(@Param("templateStructure") String templateStructure);

    /**
     * 验证字段定义
     */
    boolean validateFieldDefinitions(@Param("fieldDefinitions") String fieldDefinitions);

    /**
     * 验证验证规则
     */
    boolean validateValidationRules(@Param("validationRules") String validationRules);

    /**
     * 检查模板版本冲突
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_TEMPLATE WHERE TEMPLATE_CODE = #{templateCode} AND TEMPLATE_VERSION = #{templateVersion} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkTemplateVersionConflict(@Param("templateCode") String templateCode, @Param("templateVersion") String templateVersion, @Param("excludeId") String excludeId);
}
