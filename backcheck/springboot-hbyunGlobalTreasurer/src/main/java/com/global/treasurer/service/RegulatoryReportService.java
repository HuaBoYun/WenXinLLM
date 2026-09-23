package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryReport;

import java.util.List;
import java.util.Map;

/**
 * 监管报告服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface RegulatoryReportService {

    /**
     * 分页查询监管报告列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblRegulatoryReport> getReportList(Map<String, Object> params);

    /**
     * 根据ID查询监管报告详情
     *
     * @param reportId 报告ID
     * @return 监管报告
     */
    TblRegulatoryReport getReportById(String reportId);

    /**
     * 保存监管报告（新增或更新）
     *
     * @param report 监管报告
     * @return 保存后的监管报告
     */
    TblRegulatoryReport saveReport(TblRegulatoryReport report);

    /**
     * 删除监管报告
     *
     * @param reportId 报告ID
     */
    void deleteReport(String reportId);

    /**
     * 批量删除监管报告
     *
     * @param reportIds 报告ID列表
     */
    void batchDeleteReports(List<String> reportIds);

    /**
     * 查询逾期报告
     *
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> getOverdueReports();

    /**
     * 查询即将到期的报告
     *
     * @param days 天数
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> getDueSoonReports(Integer days);

    /**
     * 查询需要关注的报告
     *
     * @return 监管报告列表
     */
    List<TblRegulatoryReport> getReportsNeedingAttention();

    /**
     * 生成报告
     *
     * @param reportId 报告ID
     * @return 生成后的报告
     */
    TblRegulatoryReport generateReport(String reportId);

    /**
     * 验证报告
     *
     * @param reportId 报告ID
     * @return 验证后的报告
     */
    TblRegulatoryReport validateReport(String reportId);

    /**
     * 提交报告
     *
     * @param reportId 报告ID
     * @param submissionMethod 提交方式
     * @return 提交后的报告
     */
    TblRegulatoryReport submitReport(String reportId, String submissionMethod);

    /**
     * 接受报告
     *
     * @param reportId 报告ID
     * @param acknowledgmentNo 确认编号
     * @return 接受后的报告
     */
    TblRegulatoryReport acceptReport(String reportId, String acknowledgmentNo);

    /**
     * 拒绝报告
     *
     * @param reportId 报告ID
     * @param rejectReason 拒绝原因
     * @return 拒绝后的报告
     */
    TblRegulatoryReport rejectReport(String reportId, String rejectReason);

    /**
     * 获取报告统计信息
     *
     * @param companyId 公司ID
     * @return 统计信息
     */
    Map<String, Object> getReportStatistics(String companyId);

    /**
     * 批量生成报告
     *
     * @param reportIds 报告ID列表
     */
    void batchGenerateReports(List<String> reportIds);

    /**
     * 批量提交报告
     *
     * @param reportIds 报告ID列表
     * @param submissionMethod 提交方式
     */
    void batchSubmitReports(List<String> reportIds, String submissionMethod);

    /**
     * 复制报告
     *
     * @param sourceReportId 源报告ID
     * @param newReportNo 新报告编号
     * @param newReportName 新报告名称
     * @return 新报告
     */
    TblRegulatoryReport copyReport(String sourceReportId, String newReportNo, String newReportName);

    /**
     * 撤回报告
     */
    TblRegulatoryReport recallReport(String reportId);

    /**
     * 导出报告列表
     */
    List<TblRegulatoryReport> exportReportList(Map<String, Object> params);

    /**
     * 获取报告操作历史
     */
    List<Map<String, Object>> getReportHistory(String reportId);
}

