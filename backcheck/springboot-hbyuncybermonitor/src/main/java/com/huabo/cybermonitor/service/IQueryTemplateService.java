package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.QueryTemplate;
import com.huabo.cybermonitor.vo.QueryTemplateQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 查询模板服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IQueryTemplateService extends IService<QueryTemplate> {

    /**
     * 分页查询查询模板列表
     *
     * @param queryVO 查询条件
     * @return 查询模板列表
     */
    IPage<QueryTemplate> getTemplateList(QueryTemplateQueryVO queryVO);

    /**
     * 获取查询模板详情
     *
     * @param templateId 模板ID
     * @return 查询模板详情
     */
    QueryTemplate getTemplateDetail(String templateId);

    /**
     * 新增查询模板
     *
     * @param template 查询模板
     * @return 是否成功
     */
    boolean addTemplate(QueryTemplate template);

    /**
     * 更新查询模板
     *
     * @param template 查询模板
     * @return 是否成功
     */
    boolean updateTemplate(QueryTemplate template);

    /**
     * 删除查询模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteTemplate(String templateId);

    /**
     * 批量删除查询模板
     *
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchDeleteTemplate(List<String> templateIds);

    /**
     * 启用查询模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean enableTemplate(String templateId);

    /**
     * 禁用查询模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean disableTemplate(String templateId);

    /**
     * 批量启用查询模板
     *
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchEnableTemplate(List<String> templateIds);

    /**
     * 批量禁用查询模板
     *
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchDisableTemplate(List<String> templateIds);

    /**
     * 根据模板类型查询模板列表
     *
     * @param templateType 模板类型
     * @return 查询模板列表
     */
    List<QueryTemplate> getTemplatesByType(String templateType);

    /**
     * 根据创建人查询模板列表
     *
     * @param createBy 创建人
     * @return 查询模板列表
     */
    List<QueryTemplate> getTemplatesByCreateBy(String createBy);

    /**
     * 查询公开的模板列表
     *
     * @return 查询模板列表
     */
    List<QueryTemplate> getPublicTemplates();

    /**
     * 查询启用的模板列表
     *
     * @return 查询模板列表
     */
    List<QueryTemplate> getEnabledTemplates();

    /**
     * 验证模板名称唯一性
     *
     * @param templateName 模板名称
     * @param excludeId    排除的ID
     * @return 是否重复
     */
    boolean validateTemplateName(String templateName, String excludeId);

    /**
     * 复制查询模板
     *
     * @param templateId 源模板ID
     * @param newName    新模板名称
     * @return 是否成功
     */
    boolean copyTemplate(String templateId, String newName);

    /**
     * 执行查询模板
     *
     * @param templateId 模板ID
     * @param params     查询参数
     * @return 查询结果
     */
    List<Map<String, Object>> executeTemplate(String templateId, Map<String, Object> params);

    /**
     * 验证查询模板SQL
     *
     * @param querySql 查询SQL
     * @return 验证结果
     */
    Map<String, Object> validateTemplateSql(String querySql);

    /**
     * 获取模板统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getTemplateStatistics();

    /**
     * 获取模板类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getTemplateTypeDistribution();

    /**
     * 获取模板使用频率统计
     *
     * @return 使用频率统计
     */
    List<Map<String, Object>> getTemplateUsageStatistics();

    /**
     * 导出模板列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportTemplateList(QueryTemplateQueryVO queryVO, HttpServletResponse response);

    /**
     * 下载模板导入模板
     *
     * @param response HTTP响应
     */
    void downloadTemplateTemplate(HttpServletResponse response);

    /**
     * 批量导入模板
     *
     * @param file 导入文件
     * @return 导入结果
     */
    Map<String, Object> importTemplateList(MultipartFile file);

    /**
     * 获取模板类型标签
     *
     * @param templateType 模板类型
     * @return 类型标签
     */
    String getTemplateTypeLabel(String templateType);
}
