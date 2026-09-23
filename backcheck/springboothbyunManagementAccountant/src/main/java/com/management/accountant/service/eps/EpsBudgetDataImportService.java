package com.management.accountant.service.eps;

import com.management.accountant.entity.eps.EpsBudgetImportLog;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预算数据导入导出服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetDataImportService {

    /**
     * 上传预算数据文件
     * 
     * @param file 上传文件
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param templateId 模板ID
     * @param importType 导入类型
     * @return 上传结果
     */
    Map<String, Object> uploadBudgetDataFile(MultipartFile file, Long systemId, Long versionId, 
                                           Long templateId, String importType);

    /**
     * 验证预算数据
     * 
     * @param batchNumber 导入批次号
     * @param validationRules 验证规则
     * @return 验证结果
     */
    Map<String, Object> validateBudgetData(String batchNumber, String validationRules);

    /**
     * 确认导入预算数据
     * 
     * @param batchNumber 导入批次号
     * @param overwrite 是否覆盖已有数据
     * @return 导入结果
     */
    boolean confirmImportBudgetData(String batchNumber, Boolean overwrite);

    /**
     * 取消导入
     * 
     * @param batchNumber 导入批次号
     * @return 取消结果
     */
    boolean cancelImport(String batchNumber);

    /**
     * 导出预算数据
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param exportFormat 导出格式
     * @param exportScope 导出范围
     * @param subjectIds 科目ID列表
     * @param orgIds 组织ID列表
     * @param response HTTP响应
     */
    void exportBudgetData(Long systemId, Long versionId, String exportFormat, String exportScope,
                         List<Long> subjectIds, List<Long> orgIds, HttpServletResponse response);

    /**
     * 下载导入模板
     * 
     * @param templateId 模板ID
     * @param templateFormat 模板格式
     * @param response HTTP响应
     */
    void downloadImportTemplate(Long templateId, String templateFormat, HttpServletResponse response);

    /**
     * 查询导入历史
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param importStatus 导入状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param current 当前页
     * @param size 每页大小
     * @return 导入历史列表
     */
    List<EpsBudgetImportLog> getImportHistory(Long systemId, Long versionId, String importStatus,
                                            String startDate, String endDate, Long current, Long size);

    /**
     * 查询导入详情
     * 
     * @param batchNumber 导入批次号
     * @return 导入详情
     */
    Map<String, Object> getImportDetail(String batchNumber);

    /**
     * 查询导入进度
     * 
     * @param batchNumber 导入批次号
     * @return 导入进度
     */
    Map<String, Object> getImportProgress(String batchNumber);

    /**
     * 重新导入
     * 
     * @param batchNumber 导入批次号
     * @return 重新导入结果
     */
    boolean retryImport(String batchNumber);

    /**
     * 删除导入记录
     * 
     * @param batchNumber 导入批次号
     * @return 删除结果
     */
    boolean deleteImportRecord(String batchNumber);

    /**
     * 批量删除导入记录
     * 
     * @param batchNumbers 导入批次号列表
     * @return 删除结果
     */
    boolean batchDeleteImportRecords(List<String> batchNumbers);

    /**
     * 清理过期导入记录
     * 
     * @param retentionDays 保留天数
     * @return 清理数量
     */
    int cleanupExpiredImportRecords(Integer retentionDays);

    /**
     * 解析Excel文件
     * 
     * @param file Excel文件
     * @param templateId 模板ID
     * @return 解析结果
     */
    Map<String, Object> parseExcelFile(MultipartFile file, Long templateId);

    /**
     * 解析CSV文件
     * 
     * @param file CSV文件
     * @param templateId 模板ID
     * @return 解析结果
     */
    Map<String, Object> parseCsvFile(MultipartFile file, Long templateId);

    /**
     * 生成导入批次号
     * 
     * @return 批次号
     */
    String generateBatchNumber();

    /**
     * 保存导入日志
     * 
     * @param importLog 导入日志
     * @return 保存结果
     */
    boolean saveImportLog(EpsBudgetImportLog importLog);

    /**
     * 更新导入状态
     * 
     * @param batchNumber 导入批次号
     * @param status 状态
     * @param message 消息
     * @return 更新结果
     */
    boolean updateImportStatus(String batchNumber, String status, String message);

    /**
     * 数据格式转换
     * 
     * @param rawData 原始数据
     * @param templateId 模板ID
     * @return 转换后的数据
     */
    List<Map<String, Object>> convertDataFormat(List<Map<String, Object>> rawData, Long templateId);

    /**
     * 数据完整性检查
     * 
     * @param data 数据
     * @param templateId 模板ID
     * @return 检查结果
     */
    Map<String, Object> checkDataIntegrity(List<Map<String, Object>> data, Long templateId);

    /**
     * 数据业务规则验证
     * 
     * @param data 数据
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @return 验证结果
     */
    Map<String, Object> validateBusinessRules(List<Map<String, Object>> data, Long systemId, Long versionId);

    /**
     * 保存临时数据
     * 
     * @param batchNumber 批次号
     * @param data 数据
     * @return 保存结果
     */
    boolean saveTempData(String batchNumber, List<Map<String, Object>> data);

    /**
     * 获取临时数据
     * 
     * @param batchNumber 批次号
     * @return 临时数据
     */
    List<Map<String, Object>> getTempData(String batchNumber);

    /**
     * 清理临时数据
     * 
     * @param batchNumber 批次号
     * @return 清理结果
     */
    boolean cleanupTempData(String batchNumber);

    /**
     * 数据入库
     * 
     * @param batchNumber 批次号
     * @param overwrite 是否覆盖
     * @return 入库结果
     */
    boolean importDataToDatabase(String batchNumber, Boolean overwrite);

    /**
     * 生成Excel导出文件
     * 
     * @param data 数据
     * @param templateId 模板ID
     * @param response HTTP响应
     */
    void generateExcelExport(List<Map<String, Object>> data, Long templateId, HttpServletResponse response);

    /**
     * 生成CSV导出文件
     * 
     * @param data 数据
     * @param templateId 模板ID
     * @param response HTTP响应
     */
    void generateCsvExport(List<Map<String, Object>> data, Long templateId, HttpServletResponse response);

    /**
     * 查询导出数据
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param exportScope 导出范围
     * @param subjectIds 科目ID列表
     * @param orgIds 组织ID列表
     * @return 导出数据
     */
    List<Map<String, Object>> queryExportData(Long systemId, Long versionId, String exportScope,
                                             List<Long> subjectIds, List<Long> orgIds);

    /**
     * 生成导入模板文件
     * 
     * @param templateId 模板ID
     * @param templateFormat 模板格式
     * @param response HTTP响应
     */
    void generateImportTemplate(Long templateId, String templateFormat, HttpServletResponse response);

    /**
     * 异步导入处理
     * 
     * @param batchNumber 批次号
     */
    void asyncImportProcess(String batchNumber);

    /**
     * 异步导出处理
     * 
     * @param exportParams 导出参数
     * @return 导出任务ID
     */
    String asyncExportProcess(Map<String, Object> exportParams);

    /**
     * 查询异步任务状态
     * 
     * @param taskId 任务ID
     * @return 任务状态
     */
    Map<String, Object> getAsyncTaskStatus(String taskId);
}
