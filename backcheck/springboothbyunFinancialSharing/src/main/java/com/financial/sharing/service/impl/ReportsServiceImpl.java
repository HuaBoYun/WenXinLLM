package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.FinancialReportEntity;
import com.financial.sharing.oracle.mapper.FinancialReportMapper;
import com.financial.sharing.service.ReportsService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 报表分析服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-01-01
 */
@Slf4j
@Service
public class ReportsServiceImpl implements ReportsService {

    @Resource
    private FinancialReportMapper financialReportMapper;

    @Override
    public MyJsonBean<PageResult<Map<String, Object>>> getFinancialReports(
            PageableParam pageableParam,
            String reportType,
            String period,
            String status,
            Long bookId,
            Long tenantId) {
        try {
            // 构建查询参数
            Map<String, Object> param = new HashMap<>();
            if (reportType != null && !reportType.isEmpty()) {
                param.put("reportType", Integer.parseInt(reportType));
            }
            if (period != null && !period.isEmpty()) {
                param.put("reportPeriod", period);
            }
            if (status != null && !status.isEmpty()) {
                param.put("reportStatus", Integer.parseInt(status));
            }
            if (bookId != null) {
                param.put("bookId", bookId);
            }
            if (tenantId != null) {
                param.put("tenantId", tenantId);
            }

            // 分页查询
            Page<Map<String, Object>> page = new Page<>(pageableParam.getPageNumber(), pageableParam.getPageSize());
            IPage<Map<String, Object>> pageData = financialReportMapper.selectFinancialReportPage(page, param);

            // 构建分页结果
            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setRecords(pageData.getRecords());
            pageResult.setTotal(pageData.getTotal());
            pageResult.setPageNumber((int) pageData.getCurrent());
            pageResult.setPageSize((int) pageData.getSize());
            pageResult.setTotalPage((int) pageData.getPages());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询财务报表列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getStatistics(Long bookId, Long tenantId) {
        try {
            Map<String, Object> param = new HashMap<>();
            if (bookId != null) {
                param.put("bookId", bookId);
            }
            if (tenantId != null) {
                param.put("tenantId", tenantId);
            }

            Map<String, Object> statistics = financialReportMapper.selectReportStatistics(param);

            // 如果没有数据，返回默认值
            if (statistics == null) {
                statistics = new HashMap<>();
                statistics.put("totalReports", 0);
                statistics.put("generatedReports", 0);
                statistics.put("draftReports", 0);
                statistics.put("reviewedReports", 0);
                statistics.put("reportTypes", 0);
                statistics.put("reportPeriods", 0);
            }
            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("查询报表统计信息失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> getFinancialReportDetail(Long reportId) {
        try {
            Map<String, Object> detail = financialReportMapper.selectFinancialReportDetail(reportId);
            if (detail == null) {
                return MyJsonBean.errorData("报表不存在");
            }
            return MyJsonBean.successData("查询成功", detail);
        } catch (Exception e) {
            log.error("查询财务报表详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean<Map<String, Object>> saveOrUpdateFinancialReport(Map<String, Object> reportData) {
        try {
            FinancialReportEntity entity = new FinancialReportEntity();

            // 设置基本信息
            if (reportData.get("reportId") != null) {
                entity.setReportId(Long.valueOf(reportData.get("reportId").toString()));
            }
            if (reportData.get("reportCode") != null) {
                entity.setReportCode(reportData.get("reportCode").toString());
            }
            if (reportData.get("reportName") != null) {
                entity.setReportName(reportData.get("reportName").toString());
            }
            if (reportData.get("reportType") != null) {
                entity.setReportType(Integer.valueOf(reportData.get("reportType").toString()));
            }
            if (reportData.get("reportPeriod") != null) {
                entity.setReportPeriod(reportData.get("reportPeriod").toString());
            }
            if (reportData.get("reportData") != null) {
                entity.setReportData(reportData.get("reportData").toString());
            }
            if (reportData.get("reportStatus") != null) {
                entity.setReportStatus(Integer.valueOf(reportData.get("reportStatus").toString()));
            }
            if (reportData.get("bookId") != null) {
                entity.setBookId(Long.valueOf(reportData.get("bookId").toString()));
            }
            if (reportData.get("tenantId") != null) {
                entity.setTenantId(Long.valueOf(reportData.get("tenantId").toString()));
            }

            // 设置时间戳
            if (entity.getReportId() == null) {
                entity.setCreateTime(new Date());
            }
            entity.setUpdateTime(new Date());

            // 保存或更新
            boolean success;
            if (entity.getReportId() == null) {
                success = financialReportMapper.insert(entity) > 0;
            } else {
                success = financialReportMapper.updateById(entity) > 0;
            }

            if (!success) {
                return MyJsonBean.errorData("保存报表失败");
            }

            // 将entity转换为Map返回
            Map<String, Object> result = new HashMap<>();
            result.put("reportId", entity.getReportId());
            result.put("reportCode", entity.getReportCode());
            result.put("reportName", entity.getReportName());
            result.put("reportType", entity.getReportType());
            result.put("reportPeriod", entity.getReportPeriod());
            result.put("reportStatus", entity.getReportStatus());
            result.put("bookId", entity.getBookId());
            result.put("tenantId", entity.getTenantId());

            return MyJsonBean.successData("保存成功", result);
        } catch (Exception e) {
            log.error("保存财务报表失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean<String> deleteFinancialReport(Long reportId) {
        try {
            int result = financialReportMapper.deleteById(reportId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功", null);
            } else {
                return MyJsonBean.errorData("报表不存在");
            }
        } catch (Exception e) {
            log.error("删除财务报表失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> generateBalanceSheet(Map<String, Object> reportData) {
        try {
            // TODO: 实现资产负债表生成逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("message", "资产负债表生成功能待实现");
            result.putAll(reportData);

            return MyJsonBean.successData("生成成功", result);
        } catch (Exception e) {
            log.error("生成资产负债表失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> generateIncomeStatement(Map<String, Object> reportData) {
        try {
            // TODO: 实现利润表生成逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("message", "利润表生成功能待实现");
            result.putAll(reportData);

            return MyJsonBean.successData("生成成功", result);
        } catch (Exception e) {
            log.error("生成利润表失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> generateCashFlowStatement(Map<String, Object> reportData) {
        try {
            // TODO: 实现现金流量表生成逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("message", "现金流量表生成功能待实现");
            result.putAll(reportData);

            return MyJsonBean.successData("生成成功", result);
        } catch (Exception e) {
            log.error("生成现金流量表失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<Map<String, Object>> exportReport(String reportType, String period, String format) {
        try {
            // TODO: 实现报表导出逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("reportType", reportType);
            result.put("period", period);
            result.put("format", format);
            result.put("message", "报表导出功能待实现");

            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            log.error("导出报表失败", e);
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }
}
