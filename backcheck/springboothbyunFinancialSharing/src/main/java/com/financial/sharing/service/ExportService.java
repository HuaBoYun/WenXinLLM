package com.financial.sharing.service;

import com.financial.sharing.vo.param.ExportParam;
import com.hbfk.entity.TblStaffUtil;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 通用导出服务接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface ExportService {

    /**
     * 同步导出数据
     *
     * @param exportParam 导出参数
     * @param response HTTP响应
     */
    void exportData(ExportParam exportParam, HttpServletResponse response);

    /**
     * 异步导出数据
     *
     * @param exportParam 导出参数
     * @return 任务ID
     */
    String asyncExport(ExportParam exportParam);

    /**
     * 查询导出进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    ExportProgress getExportProgress(String taskId);

    /**
     * 下载异步导出的文件
     *
     * @param taskId 任务ID
     * @return 文件下载响应
     */
    ResponseEntity<byte[]> downloadExportFile(String taskId);

    /**
     * 取消导出任务
     *
     * @param taskId 任务ID
     * @return 是否成功取消
     */
    boolean cancelExport(String taskId);

    /**
     * 清理过期的导出文件
     *
     * @param expireHours 过期时间（小时）
     * @return 清理的文件数量
     */
    int cleanExpiredFiles(int expireHours);

    /**
     * 获取导出任务列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 任务列表
     */
    java.util.List<ExportTask> getExportTasks(Long bookId, Long tenantId);

    /**
     * 导出进度信息内部类
     */
    class ExportProgress {
        private String taskId;
        private String taskName;
        private String status; // RUNNING, COMPLETED, FAILED, CANCELLED
        private int progress; // 0-100
        private String message;
        private String downloadUrl;
        private java.util.Date createTime;
        private java.util.Date completeTime;
        private Long totalCount;
        private Long processedCount;

        // Getters and Setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
        public java.util.Date getCreateTime() { return createTime; }
        public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
        public java.util.Date getCompleteTime() { return completeTime; }
        public void setCompleteTime(java.util.Date completeTime) { this.completeTime = completeTime; }
        public Long getTotalCount() { return totalCount; }
        public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
        public Long getProcessedCount() { return processedCount; }
        public void setProcessedCount(Long processedCount) { this.processedCount = processedCount; }
    }

    /**
     * 导出任务信息内部类
     */
    class ExportTask {
        private String taskId;
        private String taskName;
        private String exportType;
        private String status;
        private java.util.Date createTime;
        private String createdBy;
        private Long fileSize;
        private String downloadUrl;

        // Getters and Setters
        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        public String getTaskName() { return taskName; }
        public void setTaskName(String taskName) { this.taskName = taskName; }
        public String getExportType() { return exportType; }
        public void setExportType(String exportType) { this.exportType = exportType; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public java.util.Date getCreateTime() { return createTime; }
        public void setCreateTime(java.util.Date createTime) { this.createTime = createTime; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public Long getFileSize() { return fileSize; }
        public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    }

    // ========== 新增方法 ==========

    /**
     * 导出科目余额表
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportAccountBalance(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 导出总账
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportGeneralLedger(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 导出凭证清单
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportVoucherList(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 导出辅助核算明细
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportAuxiliaryDetail(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 导出明细账
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportDetailLedger(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 导出日记账
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 导出任务信息
     */
    Map<String, Object> exportJournal(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 批量导出
     *
     * @param params 导出参数
     * @param loginStaff 登录用户信息
     * @return 批量导出任务信息
     */
    Map<String, Object> batchExport(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 查询导出任务列表
     *
     * @param params 查询参数
     * @param loginStaff 登录用户信息
     * @return 任务列表
     */
    Map<String, Object> getExportTasks(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 查询导出任务详情
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户信息
     * @return 任务详情
     */
    Map<String, Object> getExportTaskDetail(String taskId, TblStaffUtil loginStaff);

    /**
     * 查询导出进度
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户信息
     * @return 导出进度
     */
    Map<String, Object> getExportProgress(String taskId, TblStaffUtil loginStaff);

    /**
     * 取消导出任务
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户信息
     * @return 是否取消成功
     */
    boolean cancelExportTask(String taskId, TblStaffUtil loginStaff);

    /**
     * 重新执行导出任务
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户信息
     * @return 新任务信息
     */
    Map<String, Object> retryExportTask(String taskId, TblStaffUtil loginStaff);

    /**
     * 下载导出文件
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户信息
     * @param request HTTP请求
     * @param response HTTP响应
     */
    void downloadExportFile(String taskId, TblStaffUtil loginStaff, HttpServletRequest request, HttpServletResponse response);

    /**
     * 删除导出任务
     *
     * @param taskId 任务ID
     * @param loginStaff 登录用户信息
     * @return 是否删除成功
     */
    boolean deleteExportTask(String taskId, TblStaffUtil loginStaff);

    /**
     * 获取导出历史统计
     *
     * @param statisticsType 统计类型
     * @param loginStaff 登录用户信息
     * @return 统计数据
     */
    Map<String, Object> getExportStatistics(String statisticsType, TblStaffUtil loginStaff);

    /**
     * 获取导出模板列表
     *
     * @param loginStaff 登录用户信息
     * @return 模板列表
     */
    List<Map<String, Object>> getExportTemplates(TblStaffUtil loginStaff);
}