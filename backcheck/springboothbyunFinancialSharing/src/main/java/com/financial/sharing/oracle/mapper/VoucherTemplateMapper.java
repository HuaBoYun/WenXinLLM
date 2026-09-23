package com.financial.sharing.oracle.mapper;

import com.financial.sharing.dto.VoucherTemplateQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 凭证模板 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-07
 */
public interface VoucherTemplateMapper {

    /**
     * 分页查询凭证模板
     *
     * @param param 查询参数
     * @return 模板列表
     */
    List<Map<String, Object>> selectTemplatePage(@Param("param") VoucherTemplateQueryParam param);

    /**
     * 查询凭证模板总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long selectTemplateCount(@Param("param") VoucherTemplateQueryParam param);

    /**
     * 根据ID查询凭证模板详情
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    Map<String, Object> selectTemplateById(@Param("templateId") Long templateId);

    /**
     * 插入凭证模板
     *
     * @param templateData 模板数据
     * @return 插入结果
     */
    int insertTemplate(@Param("templateData") Map<String, Object> templateData);

    /**
     * 更新凭证模板
     *
     * @param templateData 模板数据
     * @return 更新结果
     */
    int updateTemplate(@Param("templateData") Map<String, Object> templateData);

    /**
     * 删除凭证模板
     *
     * @param templateId 模板ID
     * @return 删除结果
     */
    int deleteTemplate(@Param("templateId") Long templateId);

    /**
     * 检查模板编码是否存在
     *
     * @param templateCode 模板编码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    Long checkTemplateCodeExists(@Param("templateCode") String templateCode,
                                @Param("excludeId") Long excludeId);

    /**
     * 更新模板状态
     *
     * @param templateIds 模板ID列表
     * @param status 状态
     * @return 更新结果
     */
    int updateTemplateStatus(@Param("templateIds") List<Long> templateIds,
                            @Param("status") String status);

    /**
     * 获取模板类型列表
     *
     * @return 模板类型列表
     */
    List<Map<String, Object>> selectTemplateTypes();

    /**
     * 获取模板版本列表
     *
     * @param templateId 模板ID
     * @return 版本列表
     */
    List<Map<String, Object>> selectTemplateVersions(@Param("templateId") Long templateId);

    /**
     * 插入模板版本
     *
     * @param versionData 版本数据
     * @return 插入结果
     */
    int insertTemplateVersion(@Param("versionData") Map<String, Object> versionData);

    /**
     * 更新模板使用次数
     *
     * @param templateId 模板ID
     * @return 更新结果
     */
    int updateTemplateUsageCount(@Param("templateId") Long templateId);

    /**
     * 获取模板使用统计
     *
     * @param templateId 模板ID
     * @return 使用统计
     */
    Map<String, Object> selectTemplateUsageStats(@Param("templateId") Long templateId);

    /**
     * 查询模板使用记录
     *
     * @param templateId 模板ID
     * @param limit 限制条数
     * @return 使用记录
     */
    List<Map<String, Object>> selectTemplateUsageRecords(@Param("templateId") Long templateId,
                                                        @Param("limit") Integer limit);

    /**
     * 复制模板
     *
     * @param sourceTemplateId 源模板ID
     * @param targetTemplateData 目标模板数据
     * @return 复制结果
     */
    int copyTemplate(@Param("sourceTemplateId") Long sourceTemplateId,
                    @Param("targetTemplateData") Map<String, Object> targetTemplateData);

    /**
     * 获取最大模板编码
     *
     * @param prefix 编码前缀
     * @return 最大编码
     */
    String selectMaxTemplateCode(@Param("prefix") String prefix);

    /**
     * 验证模板规则
     *
     * @param templateData 模板数据
     * @return 验证结果
     */
    Map<String, Object> validateTemplateRules(@Param("templateData") Map<String, Object> templateData);

    /**
     * 批量删除模板
     *
     * @param templateIds 模板ID列表
     * @return 删除结果
     */
    int batchDeleteTemplates(@Param("templateIds") List<Long> templateIds);

    // ==================== 版本管理相关方法 ====================

    /**
     * 分页查询模板版本列表
     *
     * @param param 查询参数
     * @return 版本列表
     */
    List<Map<String, Object>> selectVersionPage(@Param("param") Map<String, Object> param);

    /**
     * 查询模板版本总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long selectVersionCount(@Param("param") Map<String, Object> param);

    /**
     * 根据ID查询版本详情
     *
     * @param versionId 版本ID
     * @return 版本详情
     */
    Map<String, Object> selectVersionById(@Param("versionId") Long versionId);

    /**
     * 激活版本
     *
     * @param versionId 版本ID
     * @return 更新结果
     */
    int activateVersion(@Param("versionId") Long versionId);

    /**
     * 取消激活同模板的其他版本
     *
     * @param templateId 模板ID
     * @param excludeVersionId 排除的版本ID
     * @return 更新结果
     */
    int deactivateOtherVersions(@Param("templateId") Long templateId, @Param("excludeVersionId") Long excludeVersionId);

    /**
     * 回滚到指定版本
     *
     * @param versionId 版本ID
     * @return 更新结果
     */
    int rollbackToVersion(@Param("versionId") Long versionId);
}