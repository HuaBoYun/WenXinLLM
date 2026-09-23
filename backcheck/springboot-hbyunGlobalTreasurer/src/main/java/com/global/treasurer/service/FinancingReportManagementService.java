package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingReportDTO;
import com.global.treasurer.dto.FinancingReportQueryDTO;
import com.global.treasurer.dto.ReportTemplateDTO;
import com.global.treasurer.dto.ReportTemplateQueryDTO;
import com.global.treasurer.entity.TblReportRecord;
import com.global.treasurer.entity.TblReportTemplate;

import java.util.List;
import java.util.Map;

/**
 * 融资报表管理服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
public interface FinancingReportManagementService {

    /**
     * 分页查询报表列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblReportRecord> getReportList(FinancingReportQueryDTO queryDTO);

    /**
     * 根据ID查询报表详情
     *
     * @param recordId 记录ID
     * @return 报表记录
     */
    TblReportRecord getReportById(Long recordId);

    /**
     * 生成报表
     *
     * @param dto 报表DTO
     * @return 生成的报表记录
     */
    TblReportRecord generateReport(FinancingReportDTO dto);

    /**
     * 导出报表
     *
     * @param recordId 记录ID
     * @return 文件路径
     */
    String exportReport(Long recordId);

    /**
     * 获取报表模板列表
     *
     * @return 模板列表
     */
    List<Map<String, Object>> getReportTemplates();

    /**
     * 定时生成报表
     *
     * @param dto 报表DTO
     * @return 生成的报表记录
     */
    TblReportRecord scheduleGenerateReport(FinancingReportDTO dto);

    /**
     * 删除报表记录
     *
     * @param recordId 记录ID
     */
    void deleteReport(Long recordId);

    /**
     * 批量删除报表记录
     *
     * @param recordIds 记录ID列表
     */
    void batchDeleteReports(List<Long> recordIds);

    /**
     * 获取报表统计信息
     *
     * @param companyId 公司ID
     * @return 统计信息
     */
    Map<String, Object> getReportStatistics(Long companyId);

    /**
     * 更新报表记录
     *
     * @param dto 报表DTO
     * @return 更新后的报表记录
     */
    TblReportRecord updateReport(FinancingReportDTO dto);

    /**
     * 重新生成报表
     *
     * @param recordId 记录ID
     * @param generatedBy 生成人ID
     * @param generatedByName 生成人姓名
     * @return 重新生成的报表记录
     */
    TblReportRecord regenerateReport(Long recordId, Long generatedBy, String generatedByName);

    /**
     * 取消报表生成
     *
     * @param recordId 记录ID
     */
    void cancelReport(Long recordId);

    /**
     * 下载报表
     *
     * @param recordId 记录ID
     * @param response HTTP响应
     */
    void downloadReport(Long recordId, javax.servlet.http.HttpServletResponse response);

    // ==================== 模板管理接口 ====================

    /**
     * 分页查询模板列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblReportTemplate> getTemplateList(ReportTemplateQueryDTO queryDTO);

    /**
     * 根据ID查询模板详情
     *
     * @param templateId 模板ID
     * @return 模板信息
     */
    TblReportTemplate getTemplateById(String templateId);

    /**
     * 新增模板
     *
     * @param dto 模板DTO
     * @return 新增的模板
     */
    TblReportTemplate addTemplate(ReportTemplateDTO dto);

    /**
     * 更新模板
     *
     * @param dto 模板DTO
     * @return 更新后的模板
     */
    TblReportTemplate updateTemplate(ReportTemplateDTO dto);

    /**
     * 删除模板
     *
     * @param templateId 模板ID
     */
    void deleteTemplate(String templateId);

    /**
     * 批量删除模板
     *
     * @param templateIds 模板ID列表
     */
    void batchDeleteTemplates(List<String> templateIds);

    /**
     * 更新模板状态
     *
     * @param templateId 模板ID
     * @param isEnabled 是否启用
     */
    void updateTemplateStatus(String templateId, Integer isEnabled);
}
