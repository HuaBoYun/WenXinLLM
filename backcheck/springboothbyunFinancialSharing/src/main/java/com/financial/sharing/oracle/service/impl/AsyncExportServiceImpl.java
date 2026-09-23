package com.financial.sharing.oracle.service.impl;

import com.financial.sharing.oracle.entity.ExportTaskEntity;
import com.financial.sharing.oracle.dto.ExportTaskDTO;
import com.financial.sharing.oracle.mapper.ExportTaskMapper;
import com.financial.sharing.oracle.service.AsyncExportService;
import com.hbfk.util.JsonBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 异步导出服务实现
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Slf4j
@Service
public class AsyncExportServiceImpl implements AsyncExportService {

    @Autowired
    private ExportTaskMapper exportTaskMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<Long, ExportTaskEntity> runningTasks = new ConcurrentHashMap<>();
    private final Map<Long, AtomicLong> progressMap = new ConcurrentHashMap<>();

    @Override
    public JsonBean createExportTask(ExportTaskDTO exportTaskDTO) {
        try {
            ExportTaskEntity task = new ExportTaskEntity();
            task.setTaskId(generateTaskId());
            task.setTaskName(exportTaskDTO.getTaskName());
            task.setTaskType(exportTaskDTO.getTaskType());
            task.setExportFormat(exportTaskDTO.getExportFormat());
            task.setQueryCondition(objectMapper.writeValueAsString(exportTaskDTO.getQueryCondition()));
            task.setExportConfig(objectMapper.writeValueAsString(exportTaskDTO.getExportConfig()));
            task.setStatus(0); // PENDING
            task.setProgressPercent(BigDecimal.ZERO);
            task.setProcessedRecords(0L);
            task.setBatchSize(exportTaskDTO.getBatchSize());
            task.setParallelism(exportTaskDTO.getParallelism());
            task.setCreateTime(new Date());
            task.setVersion(1);
            task.setIsDeleted(0);

            // 设置过期时间
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, exportTaskDTO.getExpireHours() != null ? exportTaskDTO.getExpireHours() : 24);
            task.setExpireTime(cal.getTime());

            exportTaskMapper.insertExportTask(task);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", task.getTaskId());
            result.put("message", "导出任务创建成功");

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("创建导出任务失败", e);
            return createErrorJson("创建导出任务失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean startExport(Long taskId) {
        try {
            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task == null) {
                return createErrorJson("任务不存在");
            }

            if (task.getStatus() != 0) { // 不是PENDING状态
                return createErrorJson("任务状态不正确，无法开始");
            }

            task.setStatus(1); // RUNNING
            task.setStartTime(new Date());
            exportTaskMapper.updateExportTask(task);

            // 将任务加入运行队列
            runningTasks.put(taskId, task);
            progressMap.put(taskId, new AtomicLong(0));

            // 异步执行导出
            executeExportAsync(task);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("message", "导出任务已开始");

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("开始导出失败, taskId: {}", taskId, e);
            return createErrorJson("开始导出失败: " + e.getMessage());
        }
    }

    @Async
    public CompletableFuture<Void> executeExportAsync(ExportTaskEntity task) {
        try {
            log.info("开始执行导出任务, taskId: {}, taskType: {}", task.getTaskId(), task.getTaskType());

            // 根据任务类型执行不同的导出逻辑
            switch (task.getTaskType()) {
                case "cost_allocation":
                    exportCostAllocation(task);
                    break;
                case "voucher_entry":
                    exportVoucherEntry(task);
                    break;
                case "financial_report":
                    exportFinancialReport(task);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的任务类型: " + task.getTaskType());
            }

            // 更新任务完成状态
            task.setStatus(2); // COMPLETED
            task.setEndTime(new Date());
            if (task.getStartTime() != null) {
                task.setDuration((task.getEndTime().getTime() - task.getStartTime().getTime()) / 1000);
            }
            task.setProgressPercent(new BigDecimal("100"));
            exportTaskMapper.updateExportTask(task);

            log.info("导出任务完成, taskId: {}, filePath: {}", task.getTaskId(), task.getFilePath());

        } catch (Exception e) {
            log.error("导出任务执行失败, taskId: {}", task.getTaskId(), e);
            task.setStatus(3); // FAILED
            task.setEndTime(new Date());
            task.setErrorMessage(e.getMessage());
            exportTaskMapper.updateExportTask(task);
        } finally {
            // 从运行队列中移除
            runningTasks.remove(task.getTaskId());
            progressMap.remove(task.getTaskId());
        }

        return CompletableFuture.completedFuture(null);
    }

    private void exportCostAllocation(ExportTaskEntity task) throws Exception {
        // 解析查询条件
        Map<String, Object> queryCondition = objectMapper.readValue(
            task.getQueryCondition(), Map.class);

        // 获取总记录数
        Long totalRecords = exportTaskMapper.countCostAllocation(queryCondition);
        task.setTotalRecords(totalRecords);
        exportTaskMapper.updateExportTask(task);

        // 创建导出文件
        String filePath = generateFilePath(task);
        task.setFilePath(filePath);
        task.setFileName(Paths.get(filePath).getFileName().toString());

        if ("excel".equalsIgnoreCase(task.getExportFormat())) {
            exportToExcelForCostAllocation(task, queryCondition);
        } else if ("csv".equalsIgnoreCase(task.getExportFormat())) {
            exportToCsvForCostAllocation(task, queryCondition);
        } else {
            throw new IllegalArgumentException("不支持的导出格式: " + task.getExportFormat());
        }

        // 获取文件大小
        File file = new File(filePath);
        if (file.exists()) {
            task.setFileSize(file.length());
        }
    }

    private void exportVoucherEntry(ExportTaskEntity task) throws Exception {
        // 类似的实现
        Map<String, Object> queryCondition = objectMapper.readValue(
            task.getQueryCondition(), Map.class);

        Long totalRecords = exportTaskMapper.countVoucherEntry(queryCondition);
        task.setTotalRecords(totalRecords);
        exportTaskMapper.updateExportTask(task);

        String filePath = generateFilePath(task);
        task.setFilePath(filePath);
        task.setFileName(Paths.get(filePath).getFileName().toString());

        if ("excel".equalsIgnoreCase(task.getExportFormat())) {
            exportToExcelForVoucherEntry(task, queryCondition);
        } else if ("csv".equalsIgnoreCase(task.getExportFormat())) {
            exportToCsvForVoucherEntry(task, queryCondition);
        }

        File file = new File(filePath);
        if (file.exists()) {
            task.setFileSize(file.length());
        }
    }

    private void exportFinancialReport(ExportTaskEntity task) throws Exception {
        // 类似的实现
        Map<String, Object> queryCondition = objectMapper.readValue(
            task.getQueryCondition(), Map.class);

        Long totalRecords = exportTaskMapper.countFinancialReport(queryCondition);
        task.setTotalRecords(totalRecords);
        exportTaskMapper.updateExportTask(task);

        String filePath = generateFilePath(task);
        task.setFilePath(filePath);
        task.setFileName(Paths.get(filePath).getFileName().toString());

        if ("excel".equalsIgnoreCase(task.getExportFormat())) {
            exportToExcelForFinancialReport(task, queryCondition);
        } else if ("pdf".equalsIgnoreCase(task.getExportFormat())) {
            exportToPdfForFinancialReport(task, queryCondition);
        }

        File file = new File(filePath);
        if (file.exists()) {
            task.setFileSize(file.length());
        }
    }

    private void exportToExcelForCostAllocation(ExportTaskEntity task, Map<String, Object> queryCondition) {
        // 实现Excel导出逻辑
        // 使用SXSSFWorkbook处理大数据量
        // 分批查询数据并写入Excel
        int batchSize = task.getBatchSize();
        int offset = 0;
        AtomicLong processedCount = new AtomicLong(0);

        while (true) {
            List<Map<String, Object>> batchData = exportTaskMapper.selectCostAllocationBatch(
                queryCondition, offset, batchSize);

            if (batchData.isEmpty()) {
                break;
            }

            // 写入Excel
            writeToExcel(batchData, task.getFilePath(), processedCount.get() == 0);

            processedCount.addAndGet(batchData.size());
            task.setProcessedRecords(processedCount.get());

            // 更新进度
            if (task.getTotalRecords() > 0) {
                BigDecimal progress = new BigDecimal(processedCount.get() * 100.0)
                    .divide(new BigDecimal(task.getTotalRecords()), 2, BigDecimal.ROUND_HALF_UP);
                task.setProgressPercent(progress);
            }
            exportTaskMapper.updateExportTask(task);

            offset += batchSize;
        }
    }

    private void exportToCsvForCostAllocation(ExportTaskEntity task, Map<String, Object> queryCondition) {
        // 实现CSV导出逻辑
        try (PrintWriter writer = new PrintWriter(new FileWriter(task.getFilePath()))) {
            int batchSize = task.getBatchSize();
            int offset = 0;
            AtomicLong processedCount = new AtomicLong(0);
            boolean isFirstBatch = true;

            while (true) {
                List<Map<String, Object>> batchData = exportTaskMapper.selectCostAllocationBatch(
                    queryCondition, offset, batchSize);

                if (batchData.isEmpty()) {
                    break;
                }

                // 写入CSV
                if (isFirstBatch) {
                    // 写入表头
                    writeCsvHeader(writer, batchData.get(0).keySet());
                    isFirstBatch = false;
                }
                writeCsvData(writer, batchData);

                processedCount.addAndGet(batchData.size());
                task.setProcessedRecords(processedCount.get());

                // 更新进度
                if (task.getTotalRecords() > 0) {
                    BigDecimal progress = new BigDecimal(processedCount.get() * 100.0)
                        .divide(new BigDecimal(task.getTotalRecords()), 2, BigDecimal.ROUND_HALF_UP);
                    task.setProgressPercent(progress);
                }
                exportTaskMapper.updateExportTask(task);

                offset += batchSize;
            }
        } catch (IOException e) {
            throw new RuntimeException("CSV导出失败", e);
        }
    }

    // 其他导出方法的实现...

    private String generateFilePath(ExportTaskEntity task) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String fileName = String.format("%s_%s_%s.%s",
            task.getTaskType(),
            task.getTaskName(),
            timestamp,
            task.getExportFormat());

        String baseDir = System.getProperty("user.home") + "/financial_exports";
        Path dirPath = Paths.get(baseDir);
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            log.error("创建导出目录失败", e);
        }

        return dirPath.resolve(fileName).toString();
    }

    private Long generateTaskId() {
        return System.currentTimeMillis() + (long)(Math.random() * 1000);
    }

    private void writeToExcel(List<Map<String, Object>> data, String filePath, boolean writeHeader) {
        // Excel写入实现
        // 使用Apache POI的SXSSFWorkbook
    }

    private void writeCsvHeader(PrintWriter writer, Set<String> headers) {
        String headerLine = String.join(",", headers);
        writer.println(headerLine);
    }

    private void writeCsvData(PrintWriter writer, List<Map<String, Object>> data) {
        for (Map<String, Object> row : data) {
            List<String> values = new ArrayList<>();
            for (Object value : row.values()) {
                values.add(value != null ? value.toString() : "");
            }
            writer.println(String.join(",", values));
        }
    }

    @Override
    public JsonBean cancelExport(Long taskId) {
        try {
            ExportTaskEntity task = runningTasks.get(taskId);
            if (task == null) {
                task = exportTaskMapper.selectExportTaskById(taskId);
            }

            if (task == null) {
                return createErrorJson("任务不存在");
            }

            if (task.getStatus() == 4) { // CANCELLED
                return createSuccessJson("任务已取消");
            }

            if (task.getStatus() == 2) { // COMPLETED
                return createErrorJson("任务已完成，无法取消");
            }

            // 更新任务状态
            task.setStatus(4); // CANCELLED
            task.setEndTime(new Date());
            exportTaskMapper.updateExportTask(task);

            // 从运行队列中移除
            runningTasks.remove(taskId);

            return createSuccessJson("任务已取消");
        } catch (Exception e) {
            log.error("取消导出失败, taskId: {}", taskId, e);
            return createErrorJson("取消导出失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getExportStatus(Long taskId) {
        try {
            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task == null) {
                return createErrorJson("任务不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("status", task.getStatus());
            result.put("statusText", getStatusText(task.getStatus()));
            result.put("progressPercent", task.getProgressPercent());
            result.put("processedRecords", task.getProcessedRecords());
            result.put("totalRecords", task.getTotalRecords());
            result.put("startTime", task.getStartTime());
            result.put("endTime", task.getEndTime());
            result.put("duration", task.getDuration());
            result.put("errorMessage", task.getErrorMessage());

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("获取导出状态失败, taskId: {}", taskId, e);
            return createErrorJson("获取导出状态失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getExportProgress(Long taskId) {
        try {
            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task == null) {
                return createErrorJson("任务不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("progressPercent", task.getProgressPercent());
            result.put("processedRecords", task.getProcessedRecords());
            result.put("totalRecords", task.getTotalRecords());

            if (task.getTotalRecords() != null && task.getTotalRecords() > 0) {
                Long remainingRecords = task.getTotalRecords() - task.getProcessedRecords();
                result.put("remainingRecords", remainingRecords);

                if (task.getProcessedRecords() > 0 && task.getStartTime() != null) {
                    long elapsedSeconds = (System.currentTimeMillis() - task.getStartTime().getTime()) / 1000;
                    double recordsPerSecond = (double) task.getProcessedRecords() / elapsedSeconds;

                    if (recordsPerSecond > 0) {
                        long estimatedRemainingSeconds = (long) (remainingRecords / recordsPerSecond);
                        result.put("estimatedRemainingTime", estimatedRemainingSeconds);
                    }
                }
            }

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("获取导出进度失败, taskId: {}", taskId, e);
            return createErrorJson("获取导出进度失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getMyExportTasks(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("status", status);
            params.put("offset", (pageNum - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<Map<String, Object>> tasks = exportTaskMapper.selectExportTasksByUserId(params);
            Long total = exportTaskMapper.countExportTasksByUserId(params);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", tasks);
            result.put("totalRecord", total);
            result.put("pageNo", pageNum);
            result.put("pageSize", pageSize);

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("获取我的导出任务失败", e);
            return createErrorJson("获取我的导出任务失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getAllExportTasks(Integer status, String taskType, Integer pageNum, Integer pageSize) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("status", status);
            params.put("taskType", taskType);
            params.put("offset", (pageNum - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<Map<String, Object>> tasks = exportTaskMapper.selectAllExportTasks(params);
            Long total = exportTaskMapper.countAllExportTasks(params);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", tasks);
            result.put("totalRecord", total);
            result.put("pageNo", pageNum);
            result.put("pageSize", pageSize);

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("获取所有导出任务失败", e);
            return createErrorJson("获取所有导出任务失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean downloadExportFile(Long taskId) {
        try {
            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task == null) {
                return createErrorJson("任务不存在");
            }

            if (task.getStatus() != 2) { // 不是COMPLETED状态
                return createErrorJson("任务未完成，无法下载");
            }

            if (task.getFilePath() == null || !Files.exists(Paths.get(task.getFilePath()))) {
                return createErrorJson("导出文件不存在");
            }

            // 更新下载次数
            task.setDownloadCount((task.getDownloadCount() == null ? 0 : task.getDownloadCount()) + 1);
            task.setLastDownloadTime(new Date());
            exportTaskMapper.updateExportTask(task);

            String downloadUrl = "/financial/export/download/" + taskId;
            Map<String, Object> result = new HashMap<>();
            result.put("downloadUrl", downloadUrl);
            result.put("fileName", task.getFileName());
            result.put("fileSize", task.getFileSize());

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("下载导出文件失败, taskId: {}", taskId, e);
            return createErrorJson("下载导出文件失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean deleteExportTask(Long taskId) {
        try {
            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task == null) {
                return createErrorJson("任务不存在");
            }

            // 删除文件
            if (task.getFilePath() != null && Files.exists(Paths.get(task.getFilePath()))) {
                Files.delete(Paths.get(task.getFilePath()));
            }

            // 软删除任务记录
            task.setIsDeleted(1);
            task.setUpdateTime(new Date());
            exportTaskMapper.updateExportTask(task);

            return createSuccessJson("删除成功");
        } catch (Exception e) {
            log.error("删除导出任务失败, taskId: {}", taskId, e);
            return createErrorJson("删除导出任务失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean retryExport(Long taskId) {
        try {
            ExportTaskEntity originalTask = exportTaskMapper.selectExportTaskById(taskId);
            if (originalTask == null) {
                return createErrorJson("原任务不存在");
            }

            // 创建新任务
            ExportTaskDTO exportTaskDTO = new ExportTaskDTO();
            exportTaskDTO.setTaskName(originalTask.getTaskName() + "_重试");
            exportTaskDTO.setTaskType(originalTask.getTaskType());
            exportTaskDTO.setExportFormat(originalTask.getExportFormat());
            exportTaskDTO.setBatchSize(originalTask.getBatchSize());
            exportTaskDTO.setParallelism(originalTask.getParallelism());

            // 解析原任务的查询条件
            if (originalTask.getQueryCondition() != null) {
                Map<String, Object> queryCondition = objectMapper.readValue(
                    originalTask.getQueryCondition(), Map.class);
                exportTaskDTO.setQueryCondition(queryCondition);
            }

            // 解析原任务的导出配置
            if (originalTask.getExportConfig() != null) {
                Map<String, Object> exportConfig = objectMapper.readValue(
                    originalTask.getExportConfig(), Map.class);
                exportTaskDTO.setExportConfig(exportConfig);
            }

            // 创建新任务
            JsonBean createResult = createExportTask(exportTaskDTO);
            if (createResult.getCode() != 1) {
                return createResult;
            }

            // 开始新任务
            Map<String, Object> resultData = (Map<String, Object>) createResult.getData();
            Long newTaskId = (Long) resultData.get("taskId");
            startExport(newTaskId);

            Map<String, Object> result = new HashMap<>();
            result.put("originalTaskId", taskId);
            result.put("newTaskId", newTaskId);
            result.put("message", "重试任务已创建并开始");

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("重试导出失败, taskId: {}", taskId, e);
            return createErrorJson("重试导出失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean cleanupExpiredTasks(Integer days) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -days);
            Date expireDate = cal.getTime();

            int deletedCount = exportTaskMapper.deleteExpiredTasks(expireDate);

            Map<String, Object> result = new HashMap<>();
            result.put("deletedCount", deletedCount);
            result.put("expireDate", expireDate);
            result.put("message", String.format("清理了%d个过期任务", deletedCount));

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("清理过期任务失败", e);
            return createErrorJson("清理过期任务失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getExportStatistics(Integer days) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -days);
            Date startDate = cal.getTime();

            Map<String, Object> params = new HashMap<>();
            params.put("startDate", startDate);

            Map<String, Object> statistics = exportTaskMapper.selectExportStatistics(params);

            return createSuccessJson(statistics);
        } catch (Exception e) {
            log.error("获取导出统计失败", e);
            return createErrorJson("获取导出统计失败: " + e.getMessage());
        }
    }

    // 其他方法的实现...

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "等待中";
            case 1: return "导出中";
            case 2: return "已完成";
            case 3: return "失败";
            case 4: return "已取消";
            default: return "未知";
        }
    }

    // 空实现其他方法，保持接口完整性
    @Override public JsonBean previewExportData(ExportTaskDTO exportTask, Integer previewRows) { return createSuccessJson(new Object()); }
    @Override public JsonBean getExportTemplate(String taskType, String exportFormat) { return createSuccessJson(new Object()); }
    @Override public JsonBean updateExportProgress(Long taskId, Map<String, Object> progress) { return createSuccessJson(new Object()); }
    @Override public JsonBean getExportFileInfo(Long taskId) { return createSuccessJson(new Object()); }
    @Override public JsonBean setExportConfig(Long taskId, Map<String, Object> config) { return createSuccessJson(new Object()); }
    @Override public JsonBean getExportLogs(Long taskId) { return createSuccessJson(new Object()); }
    @Override public JsonBean pauseExport(Long taskId) { return createSuccessJson(new Object()); }
    @Override public JsonBean resumeExport(Long taskId) { return createSuccessJson(new Object()); }

    // JsonBean 辅助方法
    private JsonBean createSuccessJson(Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg("操作成功");
        json.setData(data);
        return json;
    }

    private JsonBean createSuccessJson(String message) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg(message);
        return json;
    }

    private JsonBean createErrorJson(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return json;
    }

  // 其他导出方法的空实现
    private void exportToExcelForVoucherEntry(ExportTaskEntity task, Map<String, Object> queryCondition) throws Exception {}
    private void exportToCsvForVoucherEntry(ExportTaskEntity task, Map<String, Object> queryCondition) throws Exception {}
    private void exportToExcelForFinancialReport(ExportTaskEntity task, Map<String, Object> queryCondition) throws Exception {}
    private void exportToPdfForFinancialReport(ExportTaskEntity task, Map<String, Object> queryCondition) throws Exception {}
}