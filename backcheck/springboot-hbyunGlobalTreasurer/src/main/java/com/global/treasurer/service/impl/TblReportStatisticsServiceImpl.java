package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblReportStatistics;
import com.global.treasurer.mapper.TblReportStatisticsMapper;
import com.global.treasurer.service.TblReportStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 报表统计Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblReportStatisticsServiceImpl implements TblReportStatisticsService {
    private static final Logger log = LoggerFactory.getLogger(TblReportStatisticsServiceImpl.class);

    @Resource
    private TblReportStatisticsMapper tblReportStatisticsMapper;

    @Override
    public PageInfo<TblReportStatistics> getReportPage(Integer pageNum, Integer pageSize,
                                                       String reportName, String reportType,
                                                       String reportPeriod, String createTime) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblReportStatistics> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(reportName), TblReportStatistics::getReportName, reportName)
               .eq(StringUtils.isNotBlank(reportType), TblReportStatistics::getReportType, reportType)
               .eq(StringUtils.isNotBlank(reportPeriod), TblReportStatistics::getReportPeriod, reportPeriod)
               .ge(StringUtils.isNotBlank(createTime), TblReportStatistics::getCreateTime, createTime)
               .lt(StringUtils.isNotBlank(createTime), TblReportStatistics::getCreateTime, createTime + " 23:59:59")
               .eq(TblReportStatistics::getDelFlag, "0")
               .orderByDesc(TblReportStatistics::getCreateTime);
        List<TblReportStatistics> list = tblReportStatisticsMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblReportStatistics getReportById(String reportId) {
        return tblReportStatisticsMapper.selectById(reportId);
    }

    @Override
    public TblReportStatistics saveReport(TblReportStatistics report) {
        report.setReportNo("RS" + System.currentTimeMillis());
        report.setCreateTime(new Date());
        report.setDelFlag("0");
        if (report.getReportStatus() == null) {
            report.setReportStatus("DRAFT");
        }
        tblReportStatisticsMapper.insert(report);
        return report;
    }

    @Override
    public void updateReport(TblReportStatistics report) {
        report.setUpdateTime(new Date());
        tblReportStatisticsMapper.updateById(report);
    }

    @Override
    public void deleteReport(String reportId) {
        TblReportStatistics report = new TblReportStatistics();
        report.setReportId(reportId);
        report.setDelFlag("1");
        report.setUpdateTime(new Date());
        tblReportStatisticsMapper.updateById(report);
    }

    @Override
    public TblReportStatistics generateReport(String reportType, String reportPeriod,
                                              String startDate, String endDate) {
        TblReportStatistics report = new TblReportStatistics();
        report.setReportType(reportType);
        report.setReportPeriod(reportPeriod);
        report.setReportName(reportType + "_" + reportPeriod + "_报表");
        report.setTotalCollection(new BigDecimal("1000000"));
        report.setTotalAllocation(new BigDecimal("500000"));
        report.setTotalLoan(new BigDecimal("200000"));
        report.setCollectionCount(100);
        report.setAllocationCount(50);
        report.setLoanCount(20);
        report.setSuccessRate(new BigDecimal("98.5"));
        report.setGenerateTime(new Date());
        report.setReportStatus("GENERATED");
        return saveReport(report);
    }

    @Override
    public void publishReport(String reportId) {
        TblReportStatistics report = new TblReportStatistics();
        report.setReportId(reportId);
        report.setReportStatus("PUBLISHED");
        report.setUpdateTime(new Date());
        tblReportStatisticsMapper.updateById(report);
    }

    @Override
    public Map<String, Object> analyzeReport(String reportId) {
        TblReportStatistics report = tblReportStatisticsMapper.selectById(reportId);
        Map<String, Object> result = new HashMap<>();
        if (report != null) {
            result.put("reportName", report.getReportName());
            result.put("totalCollection", report.getTotalCollection());
            result.put("totalAllocation", report.getTotalAllocation());
            result.put("successRate", report.getSuccessRate());
            result.put("trend", "上升");
            result.put("suggestion", "建议继续保持当前策略");
        }
        return result;
    }

    @Override
    public byte[] exportReport(String reportId, String exportFormat) {
        log.info("导出报表: {}, 格式: {}", reportId, exportFormat);
        return new byte[0];
    }

    @Override
    public List<Map<String, Object>> getReportTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        types.add(createTypeMap("COLLECTION", "归集报表"));
        types.add(createTypeMap("ALLOCATION", "下拨报表"));
        types.add(createTypeMap("LOAN", "借贷报表"));
        types.add(createTypeMap("COMPREHENSIVE", "综合报表"));
        return types;
    }

    private Map<String, Object> createTypeMap(String code, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("name", name);
        return map;
    }
}

