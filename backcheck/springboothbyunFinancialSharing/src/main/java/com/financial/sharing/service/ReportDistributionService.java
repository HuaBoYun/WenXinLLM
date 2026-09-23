package com.financial.sharing.service;

import com.financial.sharing.entity.ReportDistributionEntity;
import org.springframework.scheduling.annotation.Async;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 报告分发服务接口
 */
public interface ReportDistributionService {

    /**
     * 配置分发规则
     */
    void configureDistribution(ReportDistributionParam param);

    /**
     * 执行报告分发
     */
    @Async("exportTaskExecutor")
    void distributeReport(String reportId, byte[] reportData);

    /**
     * 邮件分发
     */
    void distributeByEmail(List<String> emails, String subject,
                          byte[] attachment, String fileName);

    /**
     * FTP分发
     */
    void distributeToFtp(FTPConfig config, String remotePath,
                         byte[] fileData, String fileName);

    /**
     * 系统内部分发
     */
    void distributeInternal(List<Long> userIds, String reportId);

    /**
     * 重试分发
     */
    void retryDistribution(String distributionId);

    /**
     * 取消分发
     */
    void cancelDistribution(String distributionId);

    /**
     * 获取分发状态
     */
    DistributionStatus getDistributionStatus(String distributionId);

    /**
     * 获取分发历史
     */
    List<ReportDistributionEntity> getDistributionHistory(String reportId, int limit);

    /**
     * 获取分发统计
     */
    DistributionStatistics getDistributionStatistics(String reportId);

    /**
     * 测试分发配置
     */
    boolean testDistributionConfig(DistributionTestParam param);

    /**
     * 批量分发
     */
    void batchDistributeReports(BatchDistributionParam param);

    // 内部类定义
    class ReportDistributionParam {
        private String reportId;
        private String distributionType;
        private Map<String, Object> distributionConfig;
        private List<String> recipients;
        private List<String> ccRecipients;
        private List<String> bccRecipients;
        private String subject;
        private String bodyTemplate;
        private boolean sendImmediate;
        private LocalDateTime scheduleTime;

        // getters and setters
        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public String getDistributionType() { return distributionType; }
        public void setDistributionType(String distributionType) { this.distributionType = distributionType; }
        public Map<String, Object> getDistributionConfig() { return distributionConfig; }
        public void setDistributionConfig(Map<String, Object> distributionConfig) { this.distributionConfig = distributionConfig; }
        public List<String> getRecipients() { return recipients; }
        public void setRecipients(List<String> recipients) { this.recipients = recipients; }
        public List<String> getCcRecipients() { return ccRecipients; }
        public void setCcRecipients(List<String> ccRecipients) { this.ccRecipients = ccRecipients; }
        public List<String> getBccRecipients() { return bccRecipients; }
        public void setBccRecipients(List<String> bccRecipients) { this.bccRecipients = bccRecipients; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getBodyTemplate() { return bodyTemplate; }
        public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }
        public boolean isSendImmediate() { return sendImmediate; }
        public void setSendImmediate(boolean sendImmediate) { this.sendImmediate = sendImmediate; }
        public LocalDateTime getScheduleTime() { return scheduleTime; }
        public void setScheduleTime(LocalDateTime scheduleTime) { this.scheduleTime = scheduleTime; }
    }

    class FTPConfig {
        private String host;
        private int port;
        private String username;
        private String password;
        private String remotePath;
        private boolean passiveMode;
        private String encoding;

        // getters and setters
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRemotePath() { return remotePath; }
        public void setRemotePath(String remotePath) { this.remotePath = remotePath; }
        public boolean isPassiveMode() { return passiveMode; }
        public void setPassiveMode(boolean passiveMode) { this.passiveMode = passiveMode; }
        public String getEncoding() { return encoding; }
        public void setEncoding(String encoding) { this.encoding = encoding; }
    }

    class DistributionStatus {
        private String distributionId;
        private String status;
        private LocalDateTime distributionTime;
        private int retryCount;
        private String errorMessage;
        private String trackingId;

        // getters and setters
        public String getDistributionId() { return distributionId; }
        public void setDistributionId(String distributionId) { this.distributionId = distributionId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalDateTime getDistributionTime() { return distributionTime; }
        public void setDistributionTime(LocalDateTime distributionTime) { this.distributionTime = distributionTime; }
        public int getRetryCount() { return retryCount; }
        public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public String getTrackingId() { return trackingId; }
        public void setTrackingId(String trackingId) { this.trackingId = trackingId; }
    }

    class DistributionStatistics {
        private String reportId;
        private int totalDistributions;
        private int successDistributions;
        private int failedDistributions;
        private int pendingDistributions;
        private double successRate;
        private Map<String, Integer> distributionTypeStats;
        private LocalDateTime lastDistributionTime;

        // getters and setters
        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public int getTotalDistributions() { return totalDistributions; }
        public void setTotalDistributions(int totalDistributions) { this.totalDistributions = totalDistributions; }
        public int getSuccessDistributions() { return successDistributions; }
        public void setSuccessDistributions(int successDistributions) { this.successDistributions = successDistributions; }
        public int getFailedDistributions() { return failedDistributions; }
        public void setFailedDistributions(int failedDistributions) { this.failedDistributions = failedDistributions; }
        public int getPendingDistributions() { return pendingDistributions; }
        public void setPendingDistributions(int pendingDistributions) { this.pendingDistributions = pendingDistributions; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public Map<String, Integer> getDistributionTypeStats() { return distributionTypeStats; }
        public void setDistributionTypeStats(Map<String, Integer> distributionTypeStats) { this.distributionTypeStats = distributionTypeStats; }
        public LocalDateTime getLastDistributionTime() { return lastDistributionTime; }
        public void setLastDistributionTime(LocalDateTime lastDistributionTime) { this.lastDistributionTime = lastDistributionTime; }
    }

    class DistributionTestParam {
        private String distributionType;
        private Map<String, Object> config;
        private String testRecipient;

        // getters and setters
        public String getDistributionType() { return distributionType; }
        public void setDistributionType(String distributionType) { this.distributionType = distributionType; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public String getTestRecipient() { return testRecipient; }
        public void setTestRecipient(String testRecipient) { this.testRecipient = testRecipient; }
    }

    class BatchDistributionParam {
        private List<String> reportIds;
        private String distributionType;
        private Map<String, Object> distributionConfig;
        private List<String> recipients;

        // getters and setters
        public List<String> getReportIds() { return reportIds; }
        public void setReportIds(List<String> reportIds) { this.reportIds = reportIds; }
        public String getDistributionType() { return distributionType; }
        public void setDistributionType(String distributionType) { this.distributionType = distributionType; }
        public Map<String, Object> getDistributionConfig() { return distributionConfig; }
        public void setDistributionConfig(Map<String, Object> distributionConfig) { this.distributionConfig = distributionConfig; }
        public List<String> getRecipients() { return recipients; }
        public void setRecipients(List<String> recipients) { this.recipients = recipients; }
    }
}