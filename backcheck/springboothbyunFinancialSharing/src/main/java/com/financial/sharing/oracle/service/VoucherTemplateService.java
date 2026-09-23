package com.financial.sharing.oracle.service;

import com.financial.sharing.dto.VoucherTemplateQueryParam;
import com.financial.sharing.util.PageResult;
import com.hbfk.entity.TblStaffUtil;
import java.util.List;
import java.util.Map;

/**
 * 凭证模板服务接口
 *
 * @author system
 * @since 2024-12-07
 */
public interface VoucherTemplateService {

    /**
     * 分页查询凭证模板
     *
     * @param queryParam 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> selectTemplatePage(VoucherTemplateQueryParam queryParam);

    /**
     * 根据ID查询凭证模板详情
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    Map<String, Object> selectTemplateById(Long templateId);

    /**
     * 保存或更新凭证模板
     *
     * @param templateData 模板数据
     * @param loginStaff 登录用户
     * @return 模板ID
     */
    Long saveOrUpdateTemplate(Map<String, Object> templateData, TblStaffUtil loginStaff);

    /**
     * 删除凭证模板
     *
     * @param templateId 模板ID
     * @param loginStaff 登录用户
     * @return 删除结果
     */
    boolean deleteTemplate(Long templateId, TblStaffUtil loginStaff);

    /**
     * 复制凭证模板
     *
     * @param templateId 原模板ID
     * @param newTemplateName 新模板名称
     * @param loginStaff 登录用户
     * @return 新模板ID
     */
    Long copyTemplate(Long templateId, String newTemplateName, TblStaffUtil loginStaff);

    /**
     * 测试凭证模板
     *
     * @param templateId 模板ID
     * @param testData 测试数据
     * @param loginStaff 登录用户
     * @return 测试结果
     */
    Map<String, Object> testTemplate(Long templateId, Map<String, Object> testData, TblStaffUtil loginStaff);

    /**
     * 获取模板版本列表
     *
     * @param templateId 模板ID
     * @return 版本列表
     */
    List<Map<String, Object>> getTemplateVersions(Long templateId);

    /**
     * 导入凭证模板
     *
     * @param fileData 文件数据
     * @param loginStaff 登录用户
     * @return 导入结果
     */
    Map<String, Object> importTemplate(String fileData, TblStaffUtil loginStaff);

    /**
     * 导出凭证模板
     *
     * @param templateIds 模板ID列表
     * @param loginStaff 登录用户
     * @return 导出结果
     */
    Map<String, Object> exportTemplate(List<Long> templateIds, TblStaffUtil loginStaff);

    /**
     * 批量更新模板状态
     *
     * @param templateIds 模板ID列表
     * @param status 状态
     * @param loginStaff 登录用户
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> templateIds, String status, TblStaffUtil loginStaff);

    /**
     * 获取模板类型列表
     *
     * @return 模板类型列表
     */
    List<Map<String, Object>> getTemplateTypes();

    /**
     * 验证模板语法
     *
     * @param templateData 模板数据
     * @return 验证结果
     */
    Map<String, Object> validateTemplateSyntax(Map<String, Object> templateData);

    /**
     * 获取模板使用统计
     *
     * @param templateId 模板ID
     * @return 使用统计
     */
    Map<String, Object> getTemplateUsageStats(Long templateId);

    // ==================== 版本管理相关方法 ====================

    /**
     * 分页查询模板版本列表
     *
     * @param queryParam 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> selectVersionPage(Map<String, Object> queryParam);

    /**
     * 根据ID查询版本详情
     *
     * @param versionId 版本ID
     * @return 版本详情
     */
    Map<String, Object> selectVersionById(Long versionId);

    /**
     * 激活版本
     *
     * @param versionId 版本ID
     * @param loginStaff 登录用户
     * @return 操作结果
     */
    boolean activateVersion(Long versionId, TblStaffUtil loginStaff);

    /**
     * 回滚到指定版本
     *
     * @param versionId 版本ID
     * @param loginStaff 登录用户
     * @return 操作结果
     */
    boolean rollbackToVersion(Long versionId, TblStaffUtil loginStaff);
}