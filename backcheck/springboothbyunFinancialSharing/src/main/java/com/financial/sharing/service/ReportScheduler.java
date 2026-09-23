package com.financial.sharing.service;

import com.financial.sharing.oracle.entity.ReportScheduleEntity;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报告调度服务接口
 */
public interface ReportScheduler {

    /**
     * 创建调度任务
     */
    String createSchedule(ReportScheduleParam param);

    /**
     * 执行调度任务
     */
    @Scheduled(cron = "${report.schedule.cron:0 0 8 * * MON}") // 每周一早上8点
    void executeScheduledReport();

    /**
     * 手动触发调度
     */
    void triggerSchedule(String scheduleId);

    /**
     * 暂停/恢复调度
     */
    void toggleSchedule(String scheduleId, boolean enabled);

    /**
     * 更新调度配置
     */
    void updateSchedule(String scheduleId, ReportScheduleParam param);

    /**
     * 删除调度
     */
    void deleteSchedule(String scheduleId);

    /**
     * 获取调度列表
     */
    List<ReportScheduleEntity> getScheduleList(Map<String, Object> queryParams);

    /**
     * 获取调度详情
     */
    ReportScheduleEntity getScheduleById(String scheduleId);

    /**
     * 获取调度历史
     */
    List<ScheduleHistory> getScheduleHistory(String scheduleId, int limit);

    /**
     * 验证调度配置
     */
    ValidationResult validateSchedule(ReportScheduleParam param);

    /**
     * 预览下次执行时间
     */
    List<LocalDateTime> previewNextRunTimes(String cronExpression, int count);

    /**
     * 获取调度统计信息
     */
    ScheduleStatistics getScheduleStatistics(String scheduleId);

    /**
     * 批量操作调度
     */
    void batchOperationSchedule(BatchScheduleParam param);

    // 内部类定义
    class ReportScheduleParam {
        private String scheduleName;
        private String reportType;
        private String templateId;
        private String cronExpression;
        private String scheduleType;
        private Map<String, Object> parameters;
        private String outputFormat;
        private Map<String, Object> distributionConfig;
        private Boolean isEnabled;
        private String createdBy;
        private Long tenantId;

        // getters and setters
        public String getScheduleName() { return scheduleName; }
        public void setScheduleName(String scheduleName) { this.scheduleName = scheduleName; }
        public String getReportType() { return reportType; }
        public void setReportType(String reportType) { this.reportType = reportType; }
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public String getCronExpression() { return cronExpression; }
        public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
        public String getScheduleType() { return scheduleType; }
        public void setScheduleType(String scheduleType) { this.scheduleType = scheduleType; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getOutputFormat() { return outputFormat; }
        public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
        public Map<String, Object> getDistributionConfig() { return distributionConfig; }
        public void setDistributionConfig(Map<String, Object> distributionConfig) { this.distributionConfig = distributionConfig; }
        public Boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    }

    class ScheduleHistory {
        private String historyId;
        private String scheduleId;
        private String scheduleName;
        private LocalDateTime runTime;
        private String status; // SUCCESS, FAILED, RUNNING
        private String reportId;
        private String errorMessage;
        private long duration;

        // getters and setters
        public String getHistoryId() { return historyId; }
        public void setHistoryId(String historyId) { this.historyId = historyId; }
        public String getScheduleId() { return scheduleId; }
        public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
        public String getScheduleName() { return scheduleName; }
        public void setScheduleName(String scheduleName) { this.scheduleName = scheduleName; }
        public LocalDateTime getRunTime() { return runTime; }
        public void setRunTime(LocalDateTime runTime) { this.runTime = runTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
    }

    class ScheduleStatistics {
        private String scheduleId;
        private int totalRuns;
        private int successRuns;
        private int failedRuns;
        private double successRate;
        private long averageDuration;
        private LocalDateTime lastRunTime;
        private LocalDateTime nextRunTime;

        // getters and setters
        public String getScheduleId() { return scheduleId; }
        public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
        public int getTotalRuns() { return totalRuns; }
        public void setTotalRuns(int totalRuns) { this.totalRuns = totalRuns; }
        public int getSuccessRuns() { return successRuns; }
        public void setSuccessRuns(int successRuns) { this.successRuns = successRuns; }
        public int getFailedRuns() { return failedRuns; }
        public void setFailedRuns(int failedRuns) { this.failedRuns = failedRuns; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public long getAverageDuration() { return averageDuration; }
        public void setAverageDuration(long averageDuration) { this.averageDuration = averageDuration; }
        public LocalDateTime getLastRunTime() { return lastRunTime; }
        public void setLastRunTime(LocalDateTime lastRunTime) { this.lastRunTime = lastRunTime; }
        public LocalDateTime getNextRunTime() { return nextRunTime; }
        public void setNextRunTime(LocalDateTime nextRunTime) { this.nextRunTime = nextRunTime; }
    }

    class BatchScheduleParam {
        private List<String> scheduleIds;
        private String operation; // ENABLE, DISABLE, DELETE, TRIGGER
        private Map<String, Object> parameters;

        // getters and setters
        public List<String> getScheduleIds() { return scheduleIds; }
        public void setScheduleIds(List<String> scheduleIds) { this.scheduleIds = scheduleIds; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    class ValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;

        public ValidationResult() {
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }

        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }
    }
}