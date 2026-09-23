package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryReport;
import com.global.treasurer.mapper.RegulatoryReportMapper;
import com.global.treasurer.service.RegulatoryReportService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 监管报告服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class RegulatoryReportServiceImpl implements RegulatoryReportService {
    @Autowired
    private RegulatoryReportMapper reportMapper;

    @Override
    public PageInfo<TblRegulatoryReport> getReportList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblRegulatoryReport> list = reportMapper.selectReportList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblRegulatoryReport getReportById(String reportId) {
        TblRegulatoryReport report = reportMapper.selectReportById(reportId);
        if (report == null) {
            throw new ServiceException(404, "监管报告不存在");
        }
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport saveReport(TblRegulatoryReport report) {
        if (report.getReportId() == null || report.getReportId().isEmpty()) {
            // 只有当 reportNo 为空时才自动生成，否则使用用户填写的值
            if (report.getReportNo() == null || report.getReportNo().isEmpty()) {
                report.setReportNo(generateReportNo());
            }
            // 只有当状态为空时才设置为草稿，否则使用用户选择的状态
            if (report.getReportStatus() == null || report.getReportStatus().isEmpty()) {
                report.setReportStatus("DRAFT");
            }
            report.setDeleteFlag(0);
            report.setCreatedTime(new Date());
            reportMapper.insert(report);
        } else {
            report.setUpdatedTime(new Date());
            reportMapper.updateById(report);
        }
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(String reportId) {
        TblRegulatoryReport report = getReportById(reportId);
        report.setDeleteFlag(1);
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteReports(List<String> reportIds) {
        reportMapper.batchDeleteByIds(reportIds);
    }

    @Override
    public List<TblRegulatoryReport> getOverdueReports() {
        return reportMapper.selectOverdueReports();
    }

    @Override
    public List<TblRegulatoryReport> getDueSoonReports(Integer days) {
        return reportMapper.selectDueSoonReports(days);
    }

    @Override
    public List<TblRegulatoryReport> getReportsNeedingAttention() {
        return reportMapper.selectReportsNeedingAttention();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport generateReport(String reportId) {
        TblRegulatoryReport report = getReportById(reportId);
        report.setReportStatus("GENERATED");
        report.setGeneratedTime(new Date());
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport validateReport(String reportId) {
        TblRegulatoryReport report = getReportById(reportId);
        report.setReportStatus("VALIDATED");
        report.setValidatedTime(new Date());
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport submitReport(String reportId, String submissionMethod) {
        TblRegulatoryReport report = getReportById(reportId);
        report.setReportStatus("SUBMITTED");
        report.setSubmissionMethod(submissionMethod);
        report.setSubmitDate(new Date());
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport acceptReport(String reportId, String acknowledgmentNo) {
        TblRegulatoryReport report = getReportById(reportId);
        report.setReportStatus("ACCEPTED");
        report.setAcknowledgmentNo(acknowledgmentNo);
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport rejectReport(String reportId, String rejectReason) {
        TblRegulatoryReport report = getReportById(reportId);
        report.setReportStatus("REJECTED");
        report.setRejectReason(rejectReason);
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
        return report;
    }

    @Override
    public Map<String, Object> getReportStatistics(String companyId) {
        return reportMapper.selectReportStatistics(companyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchGenerateReports(List<String> reportIds) {
        if (reportIds == null || reportIds.isEmpty()) {
            throw new ServiceException(400, "请选择要生成的报告");
        }
        for (String reportId : reportIds) {
            generateReport(reportId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSubmitReports(List<String> reportIds, String submissionMethod) {
        if (reportIds == null || reportIds.isEmpty()) {
            throw new ServiceException(400, "请选择要提交的报告");
        }
        for (String reportId : reportIds) {
            submitReport(reportId, submissionMethod);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport copyReport(String sourceReportId, String newReportNo, String newReportName) {
        TblRegulatoryReport source = getReportById(sourceReportId);
        TblRegulatoryReport newReport = new TblRegulatoryReport();
        BeanUtils.copyProperties(source, newReport);
        newReport.setReportId(null);
        newReport.setReportNo(newReportNo);
        newReport.setReportName(newReportName);
        newReport.setReportStatus("DRAFT");
        newReport.setCreatedTime(new Date());
        newReport.setUpdatedTime(null);
        reportMapper.insert(newReport);
        return newReport;
    }

    private String generateReportNo() {
        return "RPT" + System.currentTimeMillis();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryReport recallReport(String reportId) {
        TblRegulatoryReport report = getReportById(reportId);
        String status = report.getReportStatus();
        if (!"SUBMITTED".equals(status) && !"GENERATED".equals(status) && !"VALIDATED".equals(status)) {
            throw new ServiceException(400, "当前状态不允许撤回");
        }
        report.setReportStatus("DRAFT");
        report.setSubmitDate(null);
        report.setUpdatedTime(new Date());
        reportMapper.updateById(report);
        return report;
    }

    @Override
    public List<TblRegulatoryReport> exportReportList(Map<String, Object> params) {
        return reportMapper.selectReportList(params);
    }

    @Override
    public List<Map<String, Object>> getReportHistory(String reportId) {
        // 确保报告存在
        getReportById(reportId);
        List<Map<String, Object>> history = new ArrayList<>();
        Map<String, Object> record = new HashMap<>();
        record.put("reportId", reportId);
        record.put("action", "查看历史");
        record.put("time", new Date());
        record.put("remark", "报告操作历史记录");
        history.add(record);
        return history;
    }
}

