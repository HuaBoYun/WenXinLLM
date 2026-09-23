package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.advanced.AdvancedReport;
import com.management.accountant.oracle.entity.advanced.ReportConfig;
import com.management.accountant.oracle.entity.advanced.ReportGenHistory;
import com.management.accountant.oracle.entity.advanced.ReportSubscriber;
import com.management.accountant.oracle.mapper.advanced.AdvancedReportMapper;
import com.management.accountant.oracle.mapper.advanced.ReportConfigMapper;
import com.management.accountant.oracle.mapper.advanced.ReportGenHistoryMapper;
import com.management.accountant.oracle.mapper.advanced.ReportSubscriberMapper;
import com.management.accountant.oracle.service.advanced.AdvancedReportService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("advancedReportServiceOracle")
public class AdvancedReportServiceImpl implements AdvancedReportService {

    @Resource
    private AdvancedReportMapper reportMapper;
    @Resource
    private ReportConfigMapper reportConfigMapper;
    @Resource
    private ReportSubscriberMapper reportSubscriberMapper;
    @Resource
    private ReportGenHistoryMapper reportGenHistoryMapper;

    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<AdvancedReport> selectList(Map<String, Object> params) {
        QueryWrapper<AdvancedReport> wrapper = buildQueryWrapper(params);
        return reportMapper.selectList(wrapper);
    }

    @Override
    public Page<AdvancedReport> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        Page<AdvancedReport> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AdvancedReport> wrapper = buildQueryWrapper(params);
        return reportMapper.selectPage(page, wrapper);
    }

    @Override
    public AdvancedReport selectById(String reportId) {
        return reportMapper.selectById(reportId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(AdvancedReport report) {
        report.setReportId("RPT" + idWorker.nextId());
        report.setCreateTime(new Date());
        report.setDelFlag(0);
        return reportMapper.insert(report) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(AdvancedReport report) {
        report.setUpdateTime(new Date());
        return reportMapper.updateById(report) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String reportId) {
        AdvancedReport report = reportMapper.selectById(reportId);
        if (report != null) {
            report.setDelFlag(1);
            report.setUpdateTime(new Date());
            return reportMapper.updateById(report) > 0;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyReport(String reportId) {
        AdvancedReport source = reportMapper.selectById(reportId);
        if (source == null) return false;
        AdvancedReport copy = new AdvancedReport();
        copy.setReportId("RPT" + idWorker.nextId());
        copy.setReportName(source.getReportName() + " - 副本");
        copy.setReportType(source.getReportType());
        copy.setDataSource(source.getDataSource());
        copy.setGenerationFrequency(source.getGenerationFrequency());
        copy.setReportStatus("DRAFT");
        copy.setDescription(source.getDescription());
        copy.setCreateBy(source.getCreateBy());
        copy.setCreateTime(new Date());
        copy.setDelFlag(0);
        return reportMapper.insert(copy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateReport(String reportId) {
        AdvancedReport report = reportMapper.selectById(reportId);
        if (report == null) return false;
        report.setReportStatus("ACTIVE");
        report.setLastGeneratedTime(new Date());
        report.setUpdateTime(new Date());
        reportMapper.updateById(report);

        ReportGenHistory history = new ReportGenHistory();
        history.setReportId(reportId);
        history.setGenerateType("MANUAL");
        history.setGenerateTime(new Date());
        history.setDataRows(0);
        history.setFileSize("0KB");
        history.setGenerateDuration("0秒");
        history.setStatus("SUCCESS");
        history.setCreateTime(new Date());
        history.setDelFlag(0);
        reportGenHistoryMapper.insert(history);

        return true;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<AdvancedReport> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);
        stats.put("totalReports", reportMapper.selectCount(wrapper));

        QueryWrapper<ReportGenHistory> historyWrapper = new QueryWrapper<>();
        historyWrapper.eq("DEL_FLAG", 0);
        historyWrapper.apply("GENERATE_TIME >= TRUNC(SYSDATE)");
        stats.put("generatedToday", reportGenHistoryMapper.selectCount(historyWrapper));

        QueryWrapper<ReportConfig> configWrapper = new QueryWrapper<>();
        configWrapper.eq("DEL_FLAG", 0);
        stats.put("totalTemplates", reportConfigMapper.selectCount(configWrapper));

        QueryWrapper<ReportSubscriber> subWrapper = new QueryWrapper<>();
        subWrapper.eq("DEL_FLAG", 0).eq("STATUS", "ACTIVE");
        stats.put("totalSubscribers", reportSubscriberMapper.selectCount(subWrapper));

        return stats;
    }

    @Override
    public List<ReportSubscriber> getSubscribers(String reportId) {
        QueryWrapper<ReportSubscriber> wrapper = new QueryWrapper<>();
        wrapper.eq("REPORT_ID", reportId).eq("DEL_FLAG", 0);
        wrapper.orderByDesc("SUBSCRIBE_TIME");
        return reportSubscriberMapper.selectList(wrapper);
    }

    @Override
    public List<ReportGenHistory> getGenerationHistory(String reportId) {
        QueryWrapper<ReportGenHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("REPORT_ID", reportId).eq("DEL_FLAG", 0);
        wrapper.orderByDesc("GENERATE_TIME");
        return reportGenHistoryMapper.selectList(wrapper);
    }

    @Override
    public ReportConfig getReportConfig(String reportId) {
        QueryWrapper<ReportConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("REPORT_ID", reportId).eq("DEL_FLAG", 0);
        wrapper.last("FETCH FIRST 1 ROWS ONLY");
        return reportConfigMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> getTypeStats() {
        Map<String, Object> typeStats = new HashMap<>();
        String[] types = {"FINANCIAL_REPORT", "BUDGET_ANALYSIS", "PERFORMANCE_REPORT", "CUSTOM_REPORT"};
        for (String type : types) {
            QueryWrapper<AdvancedReport> w = new QueryWrapper<>();
            w.eq("DEL_FLAG", 0).eq("REPORT_TYPE", type);
            typeStats.put(type, reportMapper.selectCount(w));
        }
        return typeStats;
    }

    private QueryWrapper<AdvancedReport> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<AdvancedReport> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) {
                wrapper.like("REPORT_NAME", keyword);
            }
            String type = (String) params.get("type");
            if (StringUtils.hasText(type)) {
                wrapper.eq("REPORT_TYPE", type);
            }
            String reportType = (String) params.get("reportType");
            if (StringUtils.hasText(reportType)) {
                wrapper.eq("REPORT_TYPE", reportType);
            }
        }
        wrapper.orderByDesc("CREATE_TIME");
        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSchedule(String reportId, String frequency) {
        AdvancedReport report = reportMapper.selectById(reportId);
        if (report == null) return false;
        report.setGenerationFrequency(frequency);
        report.setUpdateTime(new Date());
        return reportMapper.updateById(report) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSubscriber(ReportSubscriber subscriber) {
        subscriber.setSubscribeTime(new Date());
        subscriber.setCreateTime(new Date());
        subscriber.setDelFlag(0);
        if (subscriber.getStatus() == null) {
            subscriber.setStatus("ACTIVE");
        }
        int rows = reportSubscriberMapper.insert(subscriber);
        if (rows > 0) {
            // 更新报表订阅数
            QueryWrapper<ReportSubscriber> w = new QueryWrapper<>();
            w.eq("REPORT_ID", subscriber.getReportId()).eq("DEL_FLAG", 0).eq("STATUS", "ACTIVE");
            Long countLong = reportSubscriberMapper.selectCount(w);
            int count = countLong != null ? countLong.intValue() : 0;
            AdvancedReport report = reportMapper.selectById(subscriber.getReportId());
            if (report != null) {
                report.setSubscriberCount(count);
                report.setUpdateTime(new Date());
                reportMapper.updateById(report);
            }
        }
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSubscriber(String subscriberId) {
        ReportSubscriber subscriber = reportSubscriberMapper.selectById(subscriberId);
        if (subscriber == null) return false;
        subscriber.setDelFlag(1);
        subscriber.setUpdateTime(new Date());
        int rows = reportSubscriberMapper.updateById(subscriber);
        if (rows > 0) {
            // 更新报表订阅数
            QueryWrapper<ReportSubscriber> w = new QueryWrapper<>();
            w.eq("REPORT_ID", subscriber.getReportId()).eq("DEL_FLAG", 0).eq("STATUS", "ACTIVE");
            Long countLong2 = reportSubscriberMapper.selectCount(w);
            int count = countLong2 != null ? countLong2.intValue() : 0;
            AdvancedReport report = reportMapper.selectById(subscriber.getReportId());
            if (report != null) {
                report.setSubscriberCount(count);
                report.setUpdateTime(new Date());
                reportMapper.updateById(report);
            }
        }
        return rows > 0;
    }
}
