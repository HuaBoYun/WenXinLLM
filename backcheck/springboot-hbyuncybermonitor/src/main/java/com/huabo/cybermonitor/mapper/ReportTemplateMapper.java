package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.ReportTemplate;
import com.huabo.cybermonitor.vo.ReportTemplateQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报表模板 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface ReportTemplateMapper extends BaseMapper<ReportTemplate> {

    /**
     * 分页查询报表模板列表
     *
     * @param page    分页参数
     * @param queryVO 查询条件
     * @return 报表模板列表
     */
    IPage<ReportTemplate> selectTemplateList(Page<ReportTemplate> page, @Param("queryVO") ReportTemplateQueryVO queryVO);

    /**
     * 根据报表类型查询模板列表
     *
     * @param reportType 报表类型
     * @return 报表模板列表
     */
    List<ReportTemplate> selectTemplatesByType(@Param("reportType") String reportType);

    /**
     * 根据报表分类查询模板列表
     *
     * @param reportCategory 报表分类
     * @return 报表模板列表
     */
    List<ReportTemplate> selectTemplatesByCategory(@Param("reportCategory") String reportCategory);

    /**
     * 根据创建人查询模板列表
     *
     * @param createBy 创建人
     * @return 报表模板列表
     */
    List<ReportTemplate> selectTemplatesByCreateBy(@Param("createBy") String createBy);

    /**
     * 查询公开的模板列表
     *
     * @return 报表模板列表
     */
    List<ReportTemplate> selectPublicTemplates();

    /**
     * 查询启用的模板列表
     *
     * @return 报表模板列表
     */
    List<ReportTemplate> selectEnabledTemplates();

    /**
     * 根据模板名称查询模板（用于唯一性验证）
     *
     * @param templateName 模板名称
     * @param excludeId    排除的ID
     * @return 报表模板
     */
    ReportTemplate selectByTemplateName(@Param("templateName") String templateName, @Param("excludeId") String excludeId);

    /**
     * 更新模板状态
     *
     * @param templateId 模板ID
     * @param enabled    是否启用
     * @return 更新行数
     */
    int updateTemplateStatus(@Param("templateId") String templateId, @Param("enabled") Boolean enabled);

    /**
     * 批量更新模板状态
     *
     * @param templateIds 模板ID列表
     * @param enabled     是否启用
     * @return 更新行数
     */
    int batchUpdateTemplateStatus(@Param("templateIds") List<String> templateIds, @Param("enabled") Boolean enabled);

    /**
     * 更新排序号
     *
     * @param templateId 模板ID
     * @param sortOrder  排序号
     * @return 更新行数
     */
    int updateSortOrder(@Param("templateId") String templateId, @Param("sortOrder") Integer sortOrder);

    /**
     * 获取模板统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> selectTemplateStatistics();

    /**
     * 获取报表类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectReportTypeDistribution();

    /**
     * 获取报表分类分布统计
     *
     * @return 分类分布统计
     */
    List<Map<String, Object>> selectReportCategoryDistribution();

    /**
     * 获取模板使用频率统计
     *
     * @return 使用频率统计
     */
    List<Map<String, Object>> selectTemplateUsageStatistics();

    /**
     * 导出模板列表
     *
     * @param queryVO 查询条件
     * @return 报表模板列表
     */
    List<ReportTemplate> selectTemplateListForExport(@Param("queryVO") ReportTemplateQueryVO queryVO);
}
