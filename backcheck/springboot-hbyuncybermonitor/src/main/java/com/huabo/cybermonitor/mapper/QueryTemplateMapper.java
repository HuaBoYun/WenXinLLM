package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.QueryTemplate;
import com.huabo.cybermonitor.vo.QueryTemplateQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 查询模板 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface QueryTemplateMapper extends BaseMapper<QueryTemplate> {

    /**
     * 分页查询查询模板列表
     *
     * @param page    分页参数
     * @param queryVO 查询条件
     * @return 查询模板列表
     */
    IPage<QueryTemplate> selectTemplateList(Page<QueryTemplate> page, @Param("queryVO") QueryTemplateQueryVO queryVO);

    /**
     * 根据模板类型查询模板列表
     *
     * @param templateType 模板类型
     * @return 查询模板列表
     */
    List<QueryTemplate> selectTemplatesByType(@Param("templateType") String templateType);

    /**
     * 根据创建人查询模板列表
     *
     * @param createBy 创建人
     * @return 查询模板列表
     */
    List<QueryTemplate> selectTemplatesByCreateBy(@Param("createBy") String createBy);

    /**
     * 查询公开的模板列表
     *
     * @return 查询模板列表
     */
    List<QueryTemplate> selectPublicTemplates();

    /**
     * 查询启用的模板列表
     *
     * @return 查询模板列表
     */
    List<QueryTemplate> selectEnabledTemplates();

    /**
     * 根据模板名称查询模板（用于唯一性验证）
     *
     * @param templateName 模板名称
     * @param excludeId    排除的ID
     * @return 查询模板
     */
    QueryTemplate selectByTemplateName(@Param("templateName") String templateName, @Param("excludeId") String excludeId);

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
     * 获取模板统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> selectTemplateStatistics();

    /**
     * 获取模板类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectTemplateTypeDistribution();

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
     * @return 查询模板列表
     */
    List<QueryTemplate> selectTemplateListForExport(@Param("queryVO") QueryTemplateQueryVO queryVO);
}
