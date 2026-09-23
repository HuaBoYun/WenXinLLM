package com.financial.sharing.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据同步服务接口
 */
public interface DataSyncService {

    /**
     * 创建同步任务
     */
    String createSyncTask(DataSyncTaskParam param);

    /**
     * 执行同步任务
     */
    void executeSyncTask(String taskId);

    /**
     * 异步执行同步任务
     */
    @Async("syncTaskExecutor")
    void executeSyncTaskAsync(String taskId);

    /**
     * 定时同步
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    void scheduledSync();

    /**
     * 查询同步进度
     */
    SyncProgress getSyncProgress(String taskId);

    /**
     * 取消同步任务
     */
    void cancelSyncTask(String taskId);

    /**
     * 重试同步任务
     */
    void retrySyncTask(String taskId);

    /**
     * 获取同步任务列表
     */
    List<DataSyncTask> getSyncTaskList(Map<String, Object> queryParams);

    /**
     * 获取同步历史记录
     */
    List<SyncHistory> getSyncHistory(Long dataSourceId, int limit);

    /**
     * 获取同步统计信息
     */
    SyncStatistics getSyncStatistics(Long dataSourceId, String dateRange);

    /**
     * 验证同步配置
     */
    ValidationResult validateSyncConfig(DataSyncTaskParam param);

    /**
     * 预览同步数据
     */
    Map<String, Object> previewSyncData(DataSyncTaskParam param, int limit);

    // 内部类定义
    class DataSyncTaskParam {
        private Long dataSourceId;
        private String targetTable;
        private String syncType; // FULL, INCREMENTAL
        private String syncMode; // MANUAL, SCHEDULED
        private String cronExpression;
        private Map<String, Object> syncOptions;
        private List<FieldMapping> fieldMappings;
        private Map<String, Object> filterConditions;
        private Long tenantId;

        // getters and setters
        public Long getDataSourceId() { return dataSourceId; }
        public void setDataSourceId(Long dataSourceId) { this.dataSourceId = dataSourceId; }
        public String getTargetTable() { return targetTable; }
        public void setTargetTable(String targetTable) { this.targetTable = targetTable; }
        public String getSyncType() { return syncType; }
        public void setSyncType(String syncType) { this.syncType = syncType; }
        public String getSyncMode() { return syncMode; }
        public void setSyncMode(String syncMode) { this.syncMode = syncMode; }
        public String getCronExpression() { return cronExpression; }
        public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
        public Map<String, Object> getSyncOptions() { return syncOptions; }
        public void setSyncOptions(Map<String, Object> syncOptions) { this.syncOptions = syncOptions; }
        public List<FieldMapping> getFieldMappings() { return fieldMappings; }
        public void setFieldMappings(List<FieldMapping> fieldMappings) { this.fieldMappings = fieldMappings; }
        public Map<String, Object> getFilterConditions() { return filterConditions; }
        public void setFilterConditions(Map<String, Object> filterConditions) { this.filterConditions = filterConditions; }
        public Long getTenantId() { return tenantId; }
        public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    }

    class SyncProgress {
        private String taskId;
        private String status; // RUNNING, COMPLETED, FAILED, CANCELLED
        private int totalRecords;
        private int processedRecords;
        private int successRecords;
        private int failedRecords;
        private double progressPercentage;
        private long startTime;
        private long estimatedEndTime;
        private String errorMessage;

        // getters and setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getTotalRecords() { return totalRecords; }
        public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
        public int getProcessedRecords() { return processedRecords; }
        public void setProcessedRecords(int processedRecords) { this.processedRecords = processedRecords; }
        public int getSuccessRecords() { return successRecords; }
        public void setSuccessRecords(int successRecords) { this.successRecords = successRecords; }
        public int getFailedRecords() { return failedRecords; }
        public void setFailedRecords(int failedRecords) { this.failedRecords = failedRecords; }
        public double getProgressPercentage() { return progressPercentage; }
        public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEstimatedEndTime() { return estimatedEndTime; }
        public void setEstimatedEndTime(long estimatedEndTime) { this.estimatedEndTime = estimatedEndTime; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    class DataSyncTask {
        private String taskId;
        private String taskName;
        private Long dataSourceId;
        private String targetTable;
        private String syncType;
        private String syncMode;
        private String status;
        private String cronExpression;
        private long lastSyncTime;
        private long nextSyncTime;
        private String createdBy;

        // getters and setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public Long getDataSourceId() { return dataSourceId; }
        public void setDataSourceId(Long dataSourceId) { this.dataSourceId = dataSourceId; }
        public String getTargetTable() { return targetTable; }
        public void setTargetTable(String targetTable) { this.targetTable = targetTable; }
        public String getSyncType() { return syncType; }
        public void setSyncType(String syncType) { this.syncType = syncType; }
        public String getSyncMode() { return syncMode; }
        public void setSyncMode(String syncMode) { this.syncMode = syncMode; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getCronExpression() { return cronExpression; }
        public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
        public long getLastSyncTime() { return lastSyncTime; }
        public void setLastSyncTime(long lastSyncTime) { this.lastSyncTime = lastSyncTime; }
        public long getNextSyncTime() { return nextSyncTime; }
        public void setNextSyncTime(long nextSyncTime) { this.nextSyncTime = nextSyncTime; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    }

    class SyncHistory {
        private String historyId;
        private String taskId;
        private String status;
        private long startTime;
        private long endTime;
        private int totalRecords;
        private int successRecords;
        private int failedRecords;
        private String errorMessage;

        // getters and setters
        public String getHistoryId() { return historyId; }
        public void setHistoryId(String historyId) { this.historyId = historyId; }
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public int getTotalRecords() { return totalRecords; }
        public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
        public int getSuccessRecords() { return successRecords; }
        public void setSuccessRecords(int successRecords) { this.successRecords = successRecords; }
        public int getFailedRecords() { return failedRecords; }
        public void setFailedRecords(int failedRecords) { this.failedRecords = failedRecords; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    class SyncStatistics {
        private Long dataSourceId;
        private int totalTasks;
        private int runningTasks;
        private int completedTasks;
        private int failedTasks;
        private long totalSyncRecords;
        private long successSyncRecords;
        private long failedSyncRecords;
        private double averageSyncTime;
        private Map<String, Integer> statusDistribution;

        // getters and setters
        public Long getDataSourceId() { return dataSourceId; }
        public void setDataSourceId(Long dataSourceId) { this.dataSourceId = dataSourceId; }
        public int getTotalTasks() { return totalTasks; }
        public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }
        public int getRunningTasks() { return runningTasks; }
        public void setRunningTasks(int runningTasks) { this.runningTasks = runningTasks; }
        public int getCompletedTasks() { return completedTasks; }
        public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }
        public int getFailedTasks() { return failedTasks; }
        public void setFailedTasks(int failedTasks) { this.failedTasks = failedTasks; }
        public long getTotalSyncRecords() { return totalSyncRecords; }
        public void setTotalSyncRecords(long totalSyncRecords) { this.totalSyncRecords = totalSyncRecords; }
        public long getSuccessSyncRecords() { return successSyncRecords; }
        public void setSuccessSyncRecords(long successSyncRecords) { this.successSyncRecords = successSyncRecords; }
        public long getFailedSyncRecords() { return failedSyncRecords; }
        public void setFailedSyncRecords(long failedSyncRecords) { this.failedSyncRecords = failedSyncRecords; }
        public double getAverageSyncTime() { return averageSyncTime; }
        public void setAverageSyncTime(double averageSyncTime) { this.averageSyncTime = averageSyncTime; }
        public Map<String, Integer> getStatusDistribution() { return statusDistribution; }
        public void setStatusDistribution(Map<String, Integer> statusDistribution) { this.statusDistribution = statusDistribution; }
    }

    class FieldMapping {
        private String sourceField;
        private String targetField;
        private String fieldType;
        private String transformationRule;

        // getters and setters
        public String getSourceField() { return sourceField; }
        public void setSourceField(String sourceField) { this.sourceField = sourceField; }
        public String getTargetField() { return targetField; }
        public void setTargetField(String targetField) { this.targetField = targetField; }
        public String getFieldType() { return fieldType; }
        public void setFieldType(String fieldType) { this.fieldType = fieldType; }
        public String getTransformationRule() { return transformationRule; }
        public void setTransformationRule(String transformationRule) { this.transformationRule = transformationRule; }
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