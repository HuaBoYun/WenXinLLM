package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsBudgetTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算模板表 Mapper 接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetTemplateMapper extends BaseMapper<EpsBudgetTemplate> {

    /**
     * 分页查询预算模板
     * 
     * @param page 分页参数
     * @param templateName 模板名称（模糊查询）
     * @param templateType 模板类型
     * @param templateCategory 模板分类
     * @param systemId 预算体系ID
     * @param status 状态
     * @return 分页结果
     */
    IPage<EpsBudgetTemplate> selectBudgetTemplatePage(
            Page<EpsBudgetTemplate> page,
            @Param("templateName") String templateName,
            @Param("templateType") String templateType,
            @Param("templateCategory") String templateCategory,
            @Param("systemId") Long systemId,
            @Param("status") String status
    );

    /**
     * 根据预算体系ID查询模板列表
     * 
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> selectBySystemId(@Param("systemId") Long systemId);

    /**
     * 根据模板编码查询模板
     * 
     * @param templateCode 模板编码
     * @return 预算模板
     */
    EpsBudgetTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 查询默认模板
     * 
     * @param systemId 预算体系ID
     * @param templateCategory 模板分类
     * @return 默认模板
     */
    EpsBudgetTemplate selectDefaultTemplate(@Param("systemId") Long systemId, 
                                          @Param("templateCategory") String templateCategory);

    /**
     * 设置默认模板
     * 
     * @param templateId 模板ID
     * @param systemId 预算体系ID
     * @param templateCategory 模板分类
     * @return 影响行数
     */
    int setDefaultTemplate(@Param("templateId") Long templateId, 
                          @Param("systemId") Long systemId, 
                          @Param("templateCategory") String templateCategory);

    /**
     * 取消默认模板
     * 
     * @param systemId 预算体系ID
     * @param templateCategory 模板分类
     * @return 影响行数
     */
    int unsetDefaultTemplate(@Param("systemId") Long systemId, 
                           @Param("templateCategory") String templateCategory);

    /**
     * 根据模板类型查询模板
     * 
     * @param templateType 模板类型
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> selectByTemplateType(@Param("templateType") String templateType, 
                                               @Param("systemId") Long systemId);

    /**
     * 根据模板分类查询模板
     * 
     * @param templateCategory 模板分类
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> selectByTemplateCategory(@Param("templateCategory") String templateCategory, 
                                                   @Param("systemId") Long systemId);

    /**
     * 检查模板编码是否存在
     * 
     * @param templateCode 模板编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 数量
     */
    int checkTemplateCodeExists(@Param("templateCode") String templateCode, 
                              @Param("excludeId") Long excludeId);

    /**
     * 批量更新状态
     * 
     * @param templateIds 模板ID列表
     * @param status 状态
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("templateIds") List<Long> templateIds, 
                         @Param("status") String status, 
                         @Param("updatedBy") Long updatedBy);

    /**
     * 更新使用次数
     * 
     * @param templateId 模板ID
     * @return 影响行数
     */
    int updateUsageCount(@Param("templateId") Long templateId);

    /**
     * 查询热门模板
     * 
     * @param systemId 预算体系ID
     * @param limit 限制数量
     * @return 热门模板列表
     */
    List<EpsBudgetTemplate> selectPopularTemplates(@Param("systemId") Long systemId, 
                                                  @Param("limit") Integer limit);

    /**
     * 查询最近使用的模板
     * 
     * @param systemId 预算体系ID
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用的模板列表
     */
    List<EpsBudgetTemplate> selectRecentlyUsedTemplates(@Param("systemId") Long systemId, 
                                                       @Param("userId") Long userId, 
                                                       @Param("limit") Integer limit);

    /**
     * 根据标签查询模板
     * 
     * @param tags 标签列表
     * @param systemId 预算体系ID
     * @return 模板列表
     */
    List<EpsBudgetTemplate> selectByTags(@Param("tags") List<String> tags, 
                                       @Param("systemId") Long systemId);

    /**
     * 查询共享模板
     * 
     * @param systemId 预算体系ID
     * @return 共享模板列表
     */
    List<EpsBudgetTemplate> selectSharedTemplates(@Param("systemId") Long systemId);

    /**
     * 查询系统模板
     * 
     * @return 系统模板列表
     */
    List<EpsBudgetTemplate> selectSystemTemplates();
}
