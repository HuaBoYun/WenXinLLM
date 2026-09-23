package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblReportTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报告模板Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface ReportTemplateMapper extends BaseMapper<TblReportTemplate> {

    /**
     * 分页查询报告模板列表
     *
     * @param params 查询参数
     * @return 报告模板列表
     */
    List<TblReportTemplate> selectTemplateList(Map<String, Object> params);

    /**
     * 根据ID查询报告模板详情
     *
     * @param templateId 模板ID
     * @return 报告模板
     */
    TblReportTemplate selectTemplateById(@Param("templateId") String templateId);

    /**
     * 根据模板代码查询
     *
     * @param templateCode 模板代码
     * @return 报告模板
     */
    TblReportTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 根据监管机构查询模板
     *
     * @param authorityId 监管机构ID
     * @return 报告模板列表
     */
    List<TblReportTemplate> selectByAuthorityId(@Param("authorityId") String authorityId);

    /**
     * 查询可使用的模板
     *
     * @return 报告模板列表
     */
    List<TblReportTemplate> selectUsableTemplates();

    /**
     * 查询即将过期的模板
     *
     * @param days 天数
     * @return 报告模板列表
     */
    List<TblReportTemplate> selectExpiringSoonTemplates(@Param("days") Integer days);

    /**
     * 批量删除报告模板（逻辑删除）
     *
     * @param templateIds 模板ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("templateIds") List<String> templateIds);

    /**
     * 更新模板状态
     *
     * @param templateId 模板ID
     * @param isEnabled 是否启用
     * @return 影响行数
     */
    int updateTemplateStatus(@Param("templateId") String templateId, @Param("isEnabled") Integer isEnabled);
}

