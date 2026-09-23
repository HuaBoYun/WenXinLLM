package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.oracle.entity.budget.BudgetAnalysisReport;
import com.management.accountant.oracle.entity.budget.BudgetReportShare;
import com.management.accountant.oracle.entity.budget.BudgetReportSchedule;
import com.management.accountant.oracle.entity.budget.BudgetReportTemplate;
import com.management.accountant.oracle.mapper.budget.BudgetAnalysisReportMapper;
import com.management.accountant.oracle.mapper.budget.BudgetReportShareMapper;
import com.management.accountant.oracle.mapper.budget.BudgetReportScheduleMapper;
import com.management.accountant.oracle.mapper.budget.BudgetReportTemplateMapper;
import com.management.accountant.util.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算分析报告 Service 实现
 */
@Service
public class BudgetAnalysisReportServiceImpl
        extends ServiceImpl<BudgetAnalysisReportMapper, BudgetAnalysisReport>
        implements com.management.accountant.service.BudgetAnalysisReportService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private BudgetReportTemplateMapper templateMapper;
    @Resource
    private BudgetReportShareMapper shareMapper;
    @Resource
    private BudgetReportScheduleMapper scheduleMapper;

    @Override
    public BudgetAnalysisReport create(BudgetAnalysisReport report) {
        if (report.getReportStatus() == null) {
            report.setReportStatus("GENERATING");
        }
        if (report.getGenerateProgress() == null) {
            report.setGenerateProgress(0);
        }
        if (report.getDownloadCount() == null) {
            report.setDownloadCount(0);
        }
        if (report.getDelFlag() == null) {
            report.setDelFlag(0);
        }
        report.setCreateTime(new Date());
        report.setGeneratedTime(new Date());
        report.setGenerateTime(SDF.format(new Date()));
        // 补充模板名称
        if (StringUtils.hasText(report.getTemplateId()) && !StringUtils.hasText(report.getTemplateName())) {
            BudgetReportTemplate tpl = templateMapper.selectById(report.getTemplateId());
            if (tpl != null) {
                report.setTemplateName(tpl.getName());
            }
        }
        // 模拟生成完成
        report.setReportStatus("COMPLETED");
        report.setGenerateProgress(100);
        report.setFileSize(1024L * 1024 * (new Random().nextInt(5) + 1));
        save(report);
        return report;
    }

    @Override
    public BudgetAnalysisReport getById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void update(BudgetAnalysisReport report) {
        report.setUpdateTime(new Date());
        updateById(report);
    }

    @Override
    public void delete(String id) {
        removeById(id);
    }

    @Override
    public PageResult<BudgetAnalysisReport> getPage(Map<String, Object> params) {
        int pageNum  = params.containsKey("pageNum")  ? Integer.parseInt(String.valueOf(params.get("pageNum")))  : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(String.valueOf(params.get("pageSize"))) : 20;

        QueryWrapper<BudgetAnalysisReport> qw = new QueryWrapper<>();
        qw.eq("del_flag", 0);

        Object reportType   = params.get("reportType");
        Object reportStatus = params.get("reportStatus");
        Object creator      = params.get("creator");
        Object reportName   = params.get("reportName");
        Object startTime    = params.get("generateTimeStart");
        Object endTime      = params.get("generateTimeEnd");

        // 处理前端传来的 generateTime 数组
        Object generateTime = params.get("generateTime");
        if (generateTime instanceof List) {
            List<?> timeRange = (List<?>) generateTime;
            if (timeRange.size() >= 2 && timeRange.get(0) != null) {
                startTime = timeRange.get(0);
                endTime   = timeRange.get(1);
            }
        }

        if (reportType   != null && StringUtils.hasText(String.valueOf(reportType)))   qw.eq("report_type",   reportType);
        if (reportStatus != null && StringUtils.hasText(String.valueOf(reportStatus))) qw.eq("report_status", reportStatus);
        if (creator      != null && StringUtils.hasText(String.valueOf(creator)))      qw.like("creator",     creator);
        if (reportName   != null && StringUtils.hasText(String.valueOf(reportName)))   qw.like("report_name", reportName);
        if (startTime    != null && StringUtils.hasText(String.valueOf(startTime)))    qw.ge("generate_time", startTime);
        if (endTime      != null && StringUtils.hasText(String.valueOf(endTime)))      qw.le("generate_time", endTime);

        // 排序
        Object orderByColumn = params.get("orderByColumn");
        Object isAsc         = params.get("isAsc");
        if (orderByColumn != null && StringUtils.hasText(String.valueOf(orderByColumn))) {
            boolean asc = !"desc".equalsIgnoreCase(String.valueOf(isAsc));
            qw.orderBy(true, asc, String.valueOf(orderByColumn));
        } else {
            qw.orderByDesc("create_time");
        }

        Page<BudgetAnalysisReport> page = new Page<>(pageNum, pageSize);
        Page<BudgetAnalysisReport> result = baseMapper.selectPage(page, qw);

        PageResult<BudgetAnalysisReport> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setPageNo(pageNum);
        pr.setPageSize(pageSize);
        pr.setTotalPage((int) result.getPages());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public BudgetAnalysisReport generate(Map<String, Object> params) {
        BudgetAnalysisReport report = new BudgetAnalysisReport();
        Object templateId = params.get("templateId");
        Object id         = params.get("id");

        if (id != null && StringUtils.hasText(String.valueOf(id))) {
            // 重新生成：基于已有报告
            BudgetAnalysisReport existing = baseMapper.selectById(String.valueOf(id));
            if (existing != null) {
                existing.setReportStatus("COMPLETED");
                existing.setGenerateProgress(100);
                existing.setGenerateTime(SDF.format(new Date()));
                existing.setUpdateTime(new Date());
                updateById(existing);
                return existing;
            }
        }

        if (templateId != null && StringUtils.hasText(String.valueOf(templateId))) {
            BudgetReportTemplate tpl = templateMapper.selectById(String.valueOf(templateId));
            if (tpl != null) {
                report.setTemplateId(tpl.getId());
                report.setTemplateName(tpl.getName());
                report.setReportType(tpl.getReportType());
                report.setReportName(tpl.getName() + "_" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
                // 增加使用次数
                tpl.setUsageCount(tpl.getUsageCount() == null ? 1 : tpl.getUsageCount() + 1);
                templateMapper.updateById(tpl);
            }
        }
        return create(report);
    }

    @Override
    public Map<String, Object> getReportStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            QueryWrapper<BudgetAnalysisReport> qw = new QueryWrapper<BudgetAnalysisReport>().eq("del_flag", 0);
            long total = baseMapper.selectCount(qw);

            long scheduled = baseMapper.selectCount(new QueryWrapper<BudgetAnalysisReport>()
                    .eq("del_flag", 0).eq("report_status", "SCHEDULED"));

            long totalTemplates = templateMapper.selectCount(new QueryWrapper<BudgetReportTemplate>()
                    .eq("del_flag", 0).eq("status", 1));

            // 下载次数求和
            List<BudgetAnalysisReport> all = baseMapper.selectList(
                    new QueryWrapper<BudgetAnalysisReport>().eq("del_flag", 0).select("download_count"));
            long totalDownloads = all.stream()
                    .mapToLong(r -> r.getDownloadCount() == null ? 0 : r.getDownloadCount()).sum();

            stats.put("totalReports",    total);
            stats.put("scheduledReports", scheduled);
            stats.put("totalTemplates",  totalTemplates);
            stats.put("totalDownloads",  totalDownloads);
        } catch (Exception e) {
            log.error("获取报告统计数据失败", e);
            stats.put("totalReports", 0);
            stats.put("scheduledReports", 0);
            stats.put("totalTemplates", 0);
            stats.put("totalDownloads", 0);
        }
        return stats;
    }

    @Override
    public Map<String, Object> getReportTemplates() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<BudgetReportTemplate> quickList = templateMapper.selectQuickTemplates();
            List<BudgetReportTemplate> allList   = templateMapper.selectList(
                    new QueryWrapper<BudgetReportTemplate>().eq("del_flag", 0).eq("status", 1)
                            .orderByAsc("sort_order"));
            result.put("quickTemplates", quickList);
            result.put("templates",      allList);
        } catch (Exception e) {
            log.error("获取报告模板失败", e);
            result.put("quickTemplates", Collections.emptyList());
            result.put("templates",      Collections.emptyList());
        }
        return result;
    }

    @Override
    public Map<String, Object> batchExport(List<String> ids) {
        Map<String, Object> result = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            result.put("success", false);
            result.put("count", 0);
            result.put("msg", "未选择报告");
            return result;
        }
        // 查询选中的报告
        List<BudgetAnalysisReport> reports = baseMapper.selectBatchIds(ids);
        List<Map<String, Object>> exportList = reports.stream().map(r -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", r.getId());
            item.put("reportName", r.getReportName());
            item.put("reportType", r.getReportType());
            item.put("templateName", r.getTemplateName());
            item.put("reportStatus", r.getReportStatus());
            item.put("fileSize", r.getFileSize());
            item.put("creator", r.getCreator());
            item.put("generateTime", r.getGenerateTime());
            return item;
        }).collect(Collectors.toList());
        result.put("fileName", "分析报告_批量导出_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip");
        result.put("count", exportList.size());
        result.put("reports", exportList);
        result.put("success", true);
        return result;
    }

    @Override
    public BudgetAnalysisReport regenerate(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return generate(params);
    }

    @Override
    public BudgetAnalysisReport copy(String id) {
        BudgetAnalysisReport source = baseMapper.selectById(id);
        if (source == null) {
            throw new RuntimeException("源报告不存在");
        }
        source.setId(null);
        source.setReportName(source.getReportName() + "_副本");
        source.setDownloadCount(0);
        source.setCreateTime(new Date());
        source.setGenerateTime(SDF.format(new Date()));
        return create(source);
    }

    // ==================== 分享 ====================

    @Override
    public BudgetReportShare shareReport(Map<String, Object> params) {
        String reportId = (String) params.get("reportId");
        BudgetAnalysisReport report = baseMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("报告不存在");
        }
        BudgetReportShare share = new BudgetReportShare();
        share.setReportId(reportId);
        share.setReportName(report.getReportName());
        share.setShareType((String) params.get("shareType"));
        share.setShareTarget((String) params.get("shareTarget"));
        share.setSharePermission((String) params.get("sharePermission"));
        share.setRemark((String) params.get("remark"));
        // 生成分享链接
        share.setShareUrl("/report/share/" + UUID.randomUUID().toString().replace("-", ""));
        // 处理过期时间
        Object expireDays = params.get("expireDays");
        if (expireDays != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(String.valueOf(expireDays)));
            share.setExpireTime(cal.getTime());
        }
        share.setStatus(1);
        share.setDelFlag(0);
        share.setCreateBy("admin");
        share.setCreateTime(new Date());
        shareMapper.insert(share);
        return share;
    }

    @Override
    public List<BudgetReportShare> getSharesByReportId(String reportId) {
        return shareMapper.selectList(
                new QueryWrapper<BudgetReportShare>()
                        .eq("report_id", reportId)
                        .eq("del_flag", 0)
                        .orderByDesc("create_time"));
    }

    // ==================== 定时生成 ====================

    @Override
    public BudgetReportSchedule createSchedule(Map<String, Object> params) {
        BudgetReportSchedule schedule = new BudgetReportSchedule();
        schedule.setReportId((String) params.get("reportId"));
        schedule.setReportName((String) params.get("reportName"));
        schedule.setScheduleName((String) params.get("scheduleName"));
        schedule.setReportType((String) params.get("reportType"));
        schedule.setTemplateId((String) params.get("templateId"));
        schedule.setTemplateName((String) params.get("templateName"));
        schedule.setFrequency((String) params.get("frequency"));
        schedule.setCronExpression((String) params.get("cronExpression"));
        schedule.setRemark((String) params.get("remark"));
        // 计算下次执行时间
        String frequency = schedule.getFrequency();
        Calendar cal = Calendar.getInstance();
        if ("DAILY".equals(frequency)) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 8);
            cal.set(Calendar.MINUTE, 0);
        } else if ("WEEKLY".equals(frequency)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal.set(Calendar.HOUR_OF_DAY, 8);
        } else if ("MONTHLY".equals(frequency)) {
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 8);
        } else {
            // ONCE - 使用前端传来的时间或默认明天
            Object nextRunStr = params.get("nextRunTime");
            if (nextRunStr != null) {
                try { cal.setTime(SDF.parse(String.valueOf(nextRunStr))); } catch (Exception ignored) {}
            } else {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        cal.set(Calendar.SECOND, 0);
        schedule.setNextRunTime(cal.getTime());
        schedule.setStatus(1);
        schedule.setDelFlag(0);
        schedule.setCreateBy("admin");
        schedule.setCreateTime(new Date());
        scheduleMapper.insert(schedule);
        return schedule;
    }

    @Override
    public List<BudgetReportSchedule> getSchedulesByReportId(String reportId) {
        return scheduleMapper.selectList(
                new QueryWrapper<BudgetReportSchedule>()
                        .eq("report_id", reportId)
                        .eq("del_flag", 0)
                        .orderByDesc("create_time"));
    }

    @Override
    public void updateScheduleStatus(String scheduleId, Integer status) {
        BudgetReportSchedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule != null) {
            schedule.setStatus(status);
            schedule.setUpdateTime(new Date());
            scheduleMapper.updateById(schedule);
        }
    }

    @Override
    public void deleteSchedule(String scheduleId) {
        BudgetReportSchedule schedule = scheduleMapper.selectById(scheduleId);
        if (schedule != null) {
            schedule.setDelFlag(1);
            schedule.setUpdateTime(new Date());
            scheduleMapper.updateById(schedule);
        }
    }

    // ==================== 模板管理 ====================

    @Override
    public PageResult<BudgetReportTemplate> getTemplatePage(Map<String, Object> params) {
        int pageNum  = params.containsKey("pageNum")  ? Integer.parseInt(String.valueOf(params.get("pageNum")))  : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(String.valueOf(params.get("pageSize"))) : 20;

        QueryWrapper<BudgetReportTemplate> qw = new QueryWrapper<>();
        qw.eq("del_flag", 0);
        Object name = params.get("name");
        Object reportType = params.get("reportType");
        if (name != null && StringUtils.hasText(String.valueOf(name))) qw.like("name", name);
        if (reportType != null && StringUtils.hasText(String.valueOf(reportType))) qw.eq("report_type", reportType);
        qw.orderByAsc("sort_order");

        Page<BudgetReportTemplate> page = new Page<>(pageNum, pageSize);
        Page<BudgetReportTemplate> result = templateMapper.selectPage(page, qw);

        PageResult<BudgetReportTemplate> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setPageNo(pageNum);
        pr.setPageSize(pageSize);
        pr.setTotalPage((int) result.getPages());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public BudgetReportTemplate createTemplate(BudgetReportTemplate template) {
        template.setUsageCount(0);
        template.setStatus(1);
        template.setDelFlag(0);
        template.setCreateBy("admin");
        template.setCreateTime(new Date());
        templateMapper.insert(template);
        return template;
    }

    @Override
    public void updateTemplate(BudgetReportTemplate template) {
        template.setUpdateTime(new Date());
        templateMapper.updateById(template);
    }

    @Override
    public void deleteTemplate(String templateId) {
        BudgetReportTemplate tpl = templateMapper.selectById(templateId);
        if (tpl != null) {
            tpl.setDelFlag(1);
            tpl.setUpdateTime(new Date());
            templateMapper.updateById(tpl);
        }
    }
}

