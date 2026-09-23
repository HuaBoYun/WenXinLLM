package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.ReportTemplate;
import com.huabo.cybermonitor.vo.ReportTemplateQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 报表模板服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IReportTemplateService extends IService<ReportTemplate> {

    /**
     * 分页查询报表模板列表
     *
     * @param queryVO 查询条件
     * @return 报表模板列表
     */
    IPage<ReportTemplate> getTemplateList(ReportTemplateQueryVO queryVO);

    /**
     * 获取报表模板详情
     *
     * @param templateId 模板ID
     * @return 报表模板详情
     */
    ReportTemplate getTemplateDetail(String templateId);

    /**
     * 新增报表模板
     *
     * @param template 报表模板
     * @return 是否成功
     */
    boolean addTemplate(ReportTemplate template);

    /**
     * 更新报表模板
     *
     * @param template 报表模板
     * @return 是否成功
     */
    boolean updateTemplate(ReportTemplate template);

    /**
     * 删除报表模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean deleteTemplate(String templateId);

    /**
     * 批量删除报表模板
     *
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchDeleteTemplate(List<String> templateIds);

    /**
     * 启用报表模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean enableTemplate(String templateId);

    /**
     * 禁用报表模板
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    boolean disableTemplate(String templateId);

    /**
     * 批量启用报表模板
     *
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchEnableTemplate(List<String> templateIds);

    /**
     * 批量禁用报表模板
     *
     * @param templateIds 模板ID列表
     * @return 是否成功
     */
    boolean batchDisableTemplate(List<String> templateIds);

    /**
     * 根据报表类型查询模板列表
     *
     * @param reportType 报表类型
     * @return 报表模板列表
     */
    List<ReportTemplate> getTemplatesByType(String reportType);

    /**
     * 根据报表分类查询模板列表
     *
     * @param reportCategory 报表分类
     * @return 报表模板列表
     */
    List<ReportTemplate> getTemplatesByCategory(String reportCategory);

    /**
     * 根据创建人查询模板列表
     *
     * @param createBy 创建人
     * @return 报表模板列表
     */
    List<ReportTemplate> getTemplatesByCreateBy(String createBy);

    /**
     * 查询公开的模板列表
     *
     * @return 报表模板列表
     */
    List<ReportTemplate> getPublicTemplates();

    /**
     * 查询启用的模板列表
     *
     * @return 报表模板列表
     */
    List<ReportTemplate> getEnabledTemplates();

    /**
     * 验证模板名称唯一性
     *
     * @param templateName 模板名称
     * @param excludeId    排除的ID
     * @return 是否重复
     */
    boolean validateTemplateName(String templateName, String excludeId);

    /**
     * 复制报表模板
     *
     * @param templateId 源模板ID
     * @param newName    新模板名称
     * @return 是否成功
     */
    boolean copyTemplate(String templateId, String newName);

    /**
     * 生成报表
     *
     * @param templateId 模板ID
     * @param params     报表参数
     * @return 报表数据
     */
    Map<String, Object> generateReport(String templateId, Map<String, Object> params);

    /**
     * 导出报表
     *
     * @param templateId 模板ID
     * @param params     报表参数
     * @param format     导出格式
     * @param response   HTTP响应
     */
    void exportReport(String templateId, Map<String, Object> params, String format, HttpServletResponse response);

    /**
     * 预览报表
     *
     * @param templateId 模板ID
     * @param params     报表参数
     * @return 预览数据
     */
    Map<String, Object> previewReport(String templateId, Map<String, Object> params);

    /**
     * 验证报表配置
     *
     * @param reportConfig 报表配置
     * @return 验证结果
     */
    Map<String, Object> validateReportConfig(String reportConfig);

    /**
     * 更新排序号
     *
     * @param templateId 模板ID
     * @param sortOrder  排序号
     * @return 是否成功
     */
    boolean updateSortOrder(String templateId, Integer sortOrder);

    /**
     * 批量更新排序号
     *
     * @param sortData 排序数据
     * @return 是否成功
     */
    boolean batchUpdateSortOrder(List<Map<String, Object>> sortData);

    /**
     * 获取模板统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getTemplateStatistics();

    /**
     * 获取报表类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getReportTypeDistribution();

    /**
     * 获取报表分类分布统计
     *
     * @return 分类分布统计
     */
    List<Map<String, Object>> getReportCategoryDistribution();

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
    void exportTemplateList(ReportTemplateQueryVO queryVO, HttpServletResponse response);

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
     * 获取报表类型标签
     *
     * @param reportType 报表类型
     * @return 类型标签
     */
    String getReportTypeLabel(String reportType);

    /**
     * 获取报表分类标签
     *
     * @param reportCategory 报表分类
     * @return 分类标签
     */
    String getReportCategoryLabel(String reportCategory);

    /**
     * 获取导出格式标签
     *
     * @param exportFormat 导出格式
     * @return 格式标签
     */
    String getExportFormatLabel(String exportFormat);
}
