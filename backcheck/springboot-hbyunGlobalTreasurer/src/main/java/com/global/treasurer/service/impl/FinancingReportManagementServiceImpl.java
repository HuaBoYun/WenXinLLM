package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingReportDTO;
import com.global.treasurer.dto.FinancingReportQueryDTO;
import com.global.treasurer.dto.ReportTemplateDTO;
import com.global.treasurer.dto.ReportTemplateQueryDTO;
import com.global.treasurer.entity.TblReportRecord;
import com.global.treasurer.entity.TblReportTemplate;
import com.global.treasurer.mapper.FinancingReportManagementMapper;
import com.global.treasurer.mapper.ReportTemplateMapper;
import com.global.treasurer.service.FinancingReportManagementService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 融资报表管理服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Service
public class FinancingReportManagementServiceImpl implements FinancingReportManagementService {
    private static final Logger log = LoggerFactory.getLogger(FinancingReportManagementServiceImpl.class);

    @Autowired
    private FinancingReportManagementMapper financingReportManagementMapper;

    @Autowired
    private ReportTemplateMapper reportTemplateMapper;

    @Override
    public PageInfo<TblReportRecord> getReportList(FinancingReportQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("reportName", queryDTO.getReportName());
        params.put("reportType", queryDTO.getReportType());
        params.put("generationStatus", queryDTO.getGenerationStatus());
        params.put("templateId", queryDTO.getTemplateId());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("generatedBy", queryDTO.getGeneratedBy());
        params.put("periodType", queryDTO.getPeriodType());
        params.put("generationTimeStart", queryDTO.getGenerationTimeStart());
        params.put("generationTimeEnd", queryDTO.getGenerationTimeEnd());
        params.put("periodStartDateStart", queryDTO.getPeriodStartDateStart());
        params.put("periodStartDateEnd", queryDTO.getPeriodStartDateEnd());
        params.put("periodEndDateStart", queryDTO.getPeriodEndDateStart());
        params.put("periodEndDateEnd", queryDTO.getPeriodEndDateEnd());
        List<TblReportRecord> list = financingReportManagementMapper.selectReportList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblReportRecord getReportById(Long recordId) {
        TblReportRecord record = financingReportManagementMapper.selectReportById(recordId);
        if (record == null) {
            throw new ServiceException(404, "报表记录不存在");
        }
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportRecord generateReport(FinancingReportDTO dto) {
        log.info("开始生成融资报表, 模板ID: {}, 报表名称: {}", dto.getTemplateId(), dto.getReportName());

        // 创建报表记录
        TblReportRecord record = new TblReportRecord();
        BeanUtils.copyProperties(dto, record);

        // 生成唯一ID（使用雪花算法）
        record.setRecordId(com.baomidou.mybatisplus.core.toolkit.IdWorker.getId());

        record.setGenerationStatus("GENERATING");
        record.setGenerationTime(new Date());
        record.setDeleteFlag(0);
        record.setCreatedTime(new Date());
        record.setDownloadCount(0);

        // 插入记录
        financingReportManagementMapper.insert(record);

        try {
            // TODO: 实际生成报表文件的逻辑
            // 这里应该调用报表引擎生成实际的报表文件
            // 模拟生成过程
            Thread.sleep(1000);

            // 更新生成状态
            record.setGenerationStatus("COMPLETED");
            record.setFilePath("/reports/financing/" + record.getRecordId() + ".xlsx");
            record.setFileSize(1024L);
            financingReportManagementMapper.updateById(record);

            log.info("融资报表生成成功, 记录ID: {}", record.getRecordId());
        } catch (Exception e) {
            log.error("生成融资报表失败", e);
            record.setGenerationStatus("FAILED");
            financingReportManagementMapper.updateById(record);
            throw new ServiceException(500, "生成报表失败: " + e.getMessage());
        }

        return record;
    }

    @Override
    public String exportReport(Long recordId) {
        TblReportRecord record = getReportById(recordId);

        if (!"COMPLETED".equals(record.getGenerationStatus())) {
            throw new ServiceException(400, "只能导出已生成的报表");
        }

        if (record.getFilePath() == null || record.getFilePath().isEmpty()) {
            throw new ServiceException(404, "报表文件不存在");
        }

        log.info("导出报表, 记录ID: {}, 文件路径: {}", recordId, record.getFilePath());
        return record.getFilePath();
    }

    @Override
    public List<Map<String, Object>> getReportTemplates() {
        List<Map<String, Object>> templates = financingReportManagementMapper.selectTemplateList();

        // 如果数据库中没有模板数据,返回默认模板列表
        if (templates == null || templates.isEmpty()) {
            templates = getDefaultTemplates();
        }

        return templates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportRecord scheduleGenerateReport(FinancingReportDTO dto) {
        log.info("定时生成融资报表, 模板ID: {}, 报表名称: {}", dto.getTemplateId(), dto.getReportName());

        // 定时生成报表的逻辑与手动生成类似
        // 可以添加定时任务相关的元数据
        return generateReport(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(Long recordId) {
        TblReportRecord record = getReportById(recordId);
        record.setDeleteFlag(1);
        record.setCreatedTime(new Date());
        financingReportManagementMapper.updateById(record);
        log.info("删除报表记录, 记录ID: {}", recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteReports(List<Long> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            throw new ServiceException(400, "记录ID列表不能为空");
        }
        financingReportManagementMapper.batchDeleteByIds(recordIds);
        log.info("批量删除报表记录, 数量: {}", recordIds.size());
    }

    @Override
    public Map<String, Object> getReportStatistics(Long companyId) {
        Map<String, Object> statistics = financingReportManagementMapper.selectReportStatistics(companyId);

        // 添加默认值，兼容大小写字段名（达梦数据库可能返回大写）
        if (statistics == null) {
            statistics = new HashMap<>();
        }

        // 确保所有字段都有值
        ensureStatisticsField(statistics, "totalCount", 0);
        ensureStatisticsField(statistics, "completedCount", 0);
        ensureStatisticsField(statistics, "generatingCount", 0);
        ensureStatisticsField(statistics, "failedCount", 0);
        ensureStatisticsField(statistics, "totalDownloads", 0);
        ensureStatisticsField(statistics, "totalSize", 0L);

        return statistics;
    }

    /**
     * 确保统计字段存在，兼容大小写
     */
    private void ensureStatisticsField(Map<String, Object> map, String fieldName, Object defaultValue) {
        // 先检查小写
        if (map.containsKey(fieldName) && map.get(fieldName) != null) {
            return;
        }
        // 再检查大写
        String upperFieldName = fieldName.toUpperCase();
        if (map.containsKey(upperFieldName) && map.get(upperFieldName) != null) {
            map.put(fieldName, map.get(upperFieldName));
            return;
        }
        // 都没有则设置默认值
        map.put(fieldName, defaultValue);
    }

    /**
     * 获取默认报表模板列表
     */
    private List<Map<String, Object>> getDefaultTemplates() {
        List<Map<String, Object>> templates = new ArrayList<>();

        Map<String, Object> template1 = new HashMap<>();
        template1.put("templateId", 1L);
        template1.put("templateName", "融资日报表");
        template1.put("templateCode", "FINANCING_DAILY");
        template1.put("templateType", "DAILY");
        template1.put("periodType", "DAILY");
        templates.add(template1);

        Map<String, Object> template2 = new HashMap<>();
        template2.put("templateId", 2L);
        template2.put("templateName", "融资周报表");
        template2.put("templateCode", "FINANCING_WEEKLY");
        template2.put("templateType", "WEEKLY");
        template2.put("periodType", "WEEKLY");
        templates.add(template2);

        Map<String, Object> template3 = new HashMap<>();
        template3.put("templateId", 3L);
        template3.put("templateName", "融资月报表");
        template3.put("templateCode", "FINANCING_MONTHLY");
        template3.put("templateType", "MONTHLY");
        template3.put("periodType", "MONTHLY");
        templates.add(template3);

        Map<String, Object> template4 = new HashMap<>();
        template4.put("templateId", 4L);
        template4.put("templateName", "融资季报表");
        template4.put("templateCode", "FINANCING_QUARTERLY");
        template4.put("templateType", "QUARTERLY");
        template4.put("periodType", "QUARTERLY");
        templates.add(template4);

        Map<String, Object> template5 = new HashMap<>();
        template5.put("templateId", 5L);
        template5.put("templateName", "融资年报表");
        template5.put("templateCode", "FINANCING_ANNUAL");
        template5.put("templateType", "ANNUAL");
        template5.put("periodType", "ANNUAL");
        templates.add(template5);

        Map<String, Object> template6 = new HashMap<>();
        template6.put("templateId", 6L);
        template6.put("templateName", "融资结构分析表");
        template6.put("templateCode", "FINANCING_STRUCTURE");
        template6.put("templateType", "ANALYSIS");
        template6.put("periodType", "CUSTOM");
        templates.add(template6);

        Map<String, Object> template7 = new HashMap<>();
        template7.put("templateId", 7L);
        template7.put("templateName", "融资成本分析表");
        template7.put("templateCode", "FINANCING_COST");
        template7.put("templateType", "ANALYSIS");
        template7.put("periodType", "CUSTOM");
        templates.add(template7);

        Map<String, Object> template8 = new HashMap<>();
        template8.put("templateId", 8L);
        template8.put("templateName", "融资余额统计表");
        template8.put("templateCode", "FINANCING_BALANCE");
        template8.put("templateType", "STATISTICS");
        template8.put("periodType", "CUSTOM");
        templates.add(template8);

        return templates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportRecord updateReport(FinancingReportDTO dto) {
        log.info("更新报表记录, 记录ID: {}", dto.getRecordId());

        TblReportRecord record = getReportById(dto.getRecordId());

        // 更新可修改的字段
        if (dto.getReportName() != null) {
            record.setReportName(dto.getReportName());
        }
        if (dto.getReportType() != null) {
            record.setReportType(dto.getReportType());
        }
        if (dto.getPeriodType() != null) {
            record.setPeriodType(dto.getPeriodType());
        }
        if (dto.getPeriodStartDate() != null) {
            record.setPeriodStartDate(dto.getPeriodStartDate());
        }
        if (dto.getPeriodEndDate() != null) {
            record.setPeriodEndDate(dto.getPeriodEndDate());
        }
        if (dto.getRemark() != null) {
            record.setRemark(dto.getRemark());
        }
        record.setUpdatedTime(new Date());

        financingReportManagementMapper.updateById(record);
        log.info("报表记录更新成功, 记录ID: {}", dto.getRecordId());

        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportRecord regenerateReport(Long recordId, Long generatedBy, String generatedByName) {
        log.info("重新生成报表, 记录ID: {}", recordId);

        TblReportRecord record = getReportById(recordId);

        // 只有失败或已完成的报表才能重新生成
        if (!"FAILED".equals(record.getGenerationStatus()) && !"COMPLETED".equals(record.getGenerationStatus())) {
            throw new ServiceException(400, "只有失败或已完成的报表才能重新生成");
        }

        // 更新状态为生成中
        record.setGenerationStatus("GENERATING");
        record.setGeneratedBy(generatedBy);
        record.setGeneratedByName(generatedByName);
        record.setGenerationTime(new Date());
        financingReportManagementMapper.updateById(record);

        try {
            // TODO: 实际重新生成报表文件的逻辑
            Thread.sleep(1000);

            // 更新生成状态
            record.setGenerationStatus("COMPLETED");
            record.setFilePath("/reports/financing/" + record.getRecordId() + "_" + System.currentTimeMillis() + ".xlsx");
            record.setFileSize(1024L);
            financingReportManagementMapper.updateById(record);

            log.info("报表重新生成成功, 记录ID: {}", recordId);
        } catch (Exception e) {
            log.error("重新生成报表失败", e);
            record.setGenerationStatus("FAILED");
            financingReportManagementMapper.updateById(record);
            throw new ServiceException(500, "重新生成报表失败: " + e.getMessage());
        }

        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReport(Long recordId) {
        log.info("取消报表生成, 记录ID: {}", recordId);

        TblReportRecord record = getReportById(recordId);

        // 只有生成中的报表才能取消
        if (!"GENERATING".equals(record.getGenerationStatus())) {
            throw new ServiceException(400, "只有生成中的报表才能取消");
        }

        record.setGenerationStatus("CANCELLED");
        record.setUpdatedTime(new Date());
        financingReportManagementMapper.updateById(record);

        log.info("报表生成已取消, 记录ID: {}", recordId);
    }

    @Override
    public void downloadReport(Long recordId, javax.servlet.http.HttpServletResponse response) {
        log.info("下载报表, 记录ID: {}", recordId);

        TblReportRecord record = getReportById(recordId);

        if (!"COMPLETED".equals(record.getGenerationStatus())) {
            throw new ServiceException(400, "只能下载已生成的报表");
        }

        try {
            String fileName = record.getReportName() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // 使用 POI 生成真正的 Excel 文件
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet("报表数据");

            // 创建标题样式
            org.apache.poi.xssf.usermodel.XSSFCellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.xssf.usermodel.XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);

            // 创建标题行
            org.apache.poi.xssf.usermodel.XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"报表名称", "报表类型", "周期类型", "生成状态", "生成时间", "生成人", "文件大小", "下载次数"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.xssf.usermodel.XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            // 创建数据行
            org.apache.poi.xssf.usermodel.XSSFRow dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(record.getReportName() != null ? record.getReportName() : "");
            dataRow.createCell(1).setCellValue(getReportTypeName(record.getReportType()));
            dataRow.createCell(2).setCellValue(getPeriodTypeName(record.getPeriodType()));
            dataRow.createCell(3).setCellValue(getStatusName(record.getGenerationStatus()));
            dataRow.createCell(4).setCellValue(record.getGenerationTime() != null ?
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(record.getGenerationTime()) : "");
            dataRow.createCell(5).setCellValue(record.getGeneratedByName() != null ? record.getGeneratedByName() : "");
            dataRow.createCell(6).setCellValue(formatFileSize(record.getFileSize()));
            dataRow.createCell(7).setCellValue(record.getDownloadCount() != null ? record.getDownloadCount() : 0);

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            response.getOutputStream().flush();

            // 更新下载次数
            record.setDownloadCount((record.getDownloadCount() == null ? 0 : record.getDownloadCount()) + 1);
            financingReportManagementMapper.updateById(record);

            log.info("报表下载成功, 记录ID: {}", recordId);
        } catch (Exception e) {
            log.error("下载报表失败", e);
            throw new ServiceException(500, "下载报表失败: " + e.getMessage());
        }
    }

    /** 获取报表类型名称 */
    private String getReportTypeName(String type) {
        if (type == null) return "-";
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("BALANCE", "余额报表");
        typeMap.put("COST", "成本报表");
        typeMap.put("RISK", "风险报表");
        typeMap.put("TREND", "趋势报表");
        typeMap.put("EFFICIENCY", "效率报表");
        typeMap.put("FORECAST", "预测报表");
        return typeMap.getOrDefault(type, type);
    }

    /** 获取周期类型名称 */
    private String getPeriodTypeName(String type) {
        if (type == null) return "-";
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("DAILY", "日报");
        typeMap.put("WEEKLY", "周报");
        typeMap.put("MONTHLY", "月报");
        typeMap.put("QUARTERLY", "季报");
        typeMap.put("ANNUAL", "年报");
        typeMap.put("YEARLY", "年报");
        typeMap.put("CUSTOM", "自定义");
        return typeMap.getOrDefault(type, type);
    }

    /** 获取状态名称 */
    private String getStatusName(String status) {
        if (status == null) return "-";
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("PENDING", "待生成");
        statusMap.put("GENERATING", "生成中");
        statusMap.put("COMPLETED", "已完成");
        statusMap.put("FAILED", "失败");
        statusMap.put("CANCELLED", "已取消");
        return statusMap.getOrDefault(status, status);
    }

    /** 格式化文件大小 */
    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes == 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double size = bytes;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    // ==================== 模板管理实现 ====================

    @Override
    public PageInfo<TblReportTemplate> getTemplateList(ReportTemplateQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("templateCode", queryDTO.getTemplateCode());
        params.put("templateName", queryDTO.getTemplateName());
        params.put("authorityId", queryDTO.getAuthorityId());
        params.put("templateType", queryDTO.getTemplateType());
        params.put("isEnabled", queryDTO.getIsEnabled());
        params.put("companyId", queryDTO.getCompanyId());
        List<TblReportTemplate> list = reportTemplateMapper.selectTemplateList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblReportTemplate getTemplateById(String templateId) {
        return reportTemplateMapper.selectTemplateById(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportTemplate addTemplate(ReportTemplateDTO dto) {
        log.info("新增报表模板: templateName={}, templateCode={}", dto.getTemplateName(), dto.getTemplateCode());

        // 检查模板代码是否已存在
        if (dto.getTemplateCode() != null) {
            TblReportTemplate existing = reportTemplateMapper.selectByTemplateCode(dto.getTemplateCode());
            if (existing != null) {
                throw new ServiceException("模板代码已存在: " + dto.getTemplateCode());
            }
        }

        TblReportTemplate template = new TblReportTemplate();
        BeanUtils.copyProperties(dto, template);
        template.setTemplateId(UUID.randomUUID().toString().replace("-", ""));
        template.setDeleteFlag(0);
        template.setCreatedTime(new Date());
        if (template.getIsEnabled() == null) {
            template.setIsEnabled(1);
        }

        reportTemplateMapper.insert(template);
        log.info("新增报表模板成功: templateId={}", template.getTemplateId());
        return template;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportTemplate updateTemplate(ReportTemplateDTO dto) {
        log.info("更新报表模板: templateId={}, templateName={}", dto.getTemplateId(), dto.getTemplateName());

        if (dto.getTemplateId() == null || dto.getTemplateId().isEmpty()) {
            throw new ServiceException("模板ID不能为空");
        }

        TblReportTemplate existing = reportTemplateMapper.selectTemplateById(dto.getTemplateId());
        if (existing == null) {
            throw new ServiceException("模板不存在: " + dto.getTemplateId());
        }

        // 检查模板代码是否与其他模板冲突
        if (dto.getTemplateCode() != null && !dto.getTemplateCode().equals(existing.getTemplateCode())) {
            TblReportTemplate codeExisting = reportTemplateMapper.selectByTemplateCode(dto.getTemplateCode());
            if (codeExisting != null) {
                throw new ServiceException("模板代码已存在: " + dto.getTemplateCode());
            }
        }

        BeanUtils.copyProperties(dto, existing, "templateId", "deleteFlag", "createdBy", "createdTime");
        existing.setUpdatedTime(new Date());

        reportTemplateMapper.updateById(existing);
        log.info("更新报表模板成功: templateId={}", existing.getTemplateId());
        return existing;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String templateId) {
        log.info("删除报表模板: templateId={}", templateId);

        TblReportTemplate existing = reportTemplateMapper.selectTemplateById(templateId);
        if (existing == null) {
            throw new ServiceException("模板不存在: " + templateId);
        }

        existing.setDeleteFlag(1);
        existing.setUpdatedTime(new Date());
        reportTemplateMapper.updateById(existing);
        log.info("删除报表模板成功: templateId={}", templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteTemplates(List<String> templateIds) {
        log.info("批量删除报表模板: templateIds={}", templateIds);

        if (templateIds == null || templateIds.isEmpty()) {
            throw new ServiceException("模板ID列表不能为空");
        }

        reportTemplateMapper.batchDeleteByIds(templateIds);
        log.info("批量删除报表模板成功: count={}", templateIds.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateStatus(String templateId, Integer isEnabled) {
        log.info("更新模板状态: templateId={}, isEnabled={}", templateId, isEnabled);

        TblReportTemplate existing = reportTemplateMapper.selectTemplateById(templateId);
        if (existing == null) {
            throw new ServiceException("模板不存在: " + templateId);
        }

        reportTemplateMapper.updateTemplateStatus(templateId, isEnabled);
        log.info("更新模板状态成功: templateId={}", templateId);
    }
}
