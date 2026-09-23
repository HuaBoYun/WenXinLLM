package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.ReportDataImportResult;
import com.financial.sharing.enterpriseReport.dto.ReportDataQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportData;
import com.financial.sharing.enterpriseReport.service.ReportDataImportService;
import com.financial.sharing.enterpriseReport.service.ReportDataService;
import com.hbfk.util.user.UserProvider;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报表数据导入导出Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReportDataImportServiceImpl implements ReportDataImportService {

    @Autowired
    private ReportDataService reportDataService;

    @Override
    public void downloadTemplate(HttpServletResponse response) throws Exception {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("报表数据导入模板");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "任务ID*", "模板ID*", "指标ID*", "组织ID*", "期间*",
            "维度值(JSON)", "数据值", "数据来源", "单元格颜色", "是否可编辑"
        };

        // 创建标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 设置标题
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 20 * 256);
        }

        // 创建示例数据行
        Row exampleRow = sheet.createRow(1);
        exampleRow.createCell(0).setCellValue("task001");
        exampleRow.createCell(1).setCellValue("template001");
        exampleRow.createCell(2).setCellValue("indicator001");
        exampleRow.createCell(3).setCellValue("org001");
        exampleRow.createCell(4).setCellValue("202401");
        exampleRow.createCell(5).setCellValue("{\"dim1\":\"value1\"}");
        exampleRow.createCell(6).setCellValue("1000.00");
        exampleRow.createCell(7).setCellValue("MANUAL");
        exampleRow.createCell(8).setCellValue("#FFFFFF");
        exampleRow.createCell(9).setCellValue("Y");

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("报表数据导入模板.xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 输出到响应流
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportDataImportResult importExcel(MultipartFile file) throws Exception {
        ReportDataImportResult result = new ReportDataImportResult();

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // 获取总行数(排除标题行)
        int totalRows = sheet.getLastRowNum();
        result.setTotalRows(totalRows);

        List<TblReportData> dataList = new ArrayList<>();

        // 从第2行开始读取(第1行是标题,第2行是示例)
        for (int i = 2; i <= totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            try {
                // 读取单元格数据
                String taskId = getCellValue(row.getCell(0));
                String templateId = getCellValue(row.getCell(1));
                String indicatorId = getCellValue(row.getCell(2));
                String orgId = getCellValue(row.getCell(3));
                String period = getCellValue(row.getCell(4));
                String dimensionValues = getCellValue(row.getCell(5));
                String dataValue = getCellValue(row.getCell(6));
                String dataSource = getCellValue(row.getCell(7));
                String cellColor = getCellValue(row.getCell(8));
                String isEditable = getCellValue(row.getCell(9));

                // 验证必填项
                if (taskId == null || taskId.isEmpty()) {
                    result.addError(i + 1, "任务ID不能为空");
                    continue;
                }
                if (templateId == null || templateId.isEmpty()) {
                    result.addError(i + 1, "模板ID不能为空");
                    continue;
                }
                if (indicatorId == null || indicatorId.isEmpty()) {
                    result.addError(i + 1, "指标ID不能为空");
                    continue;
                }
                if (orgId == null || orgId.isEmpty()) {
                    result.addError(i + 1, "组织ID不能为空");
                    continue;
                }
                if (period == null || period.isEmpty()) {
                    result.addError(i + 1, "期间不能为空");
                    continue;
                }

                // 创建数据对象
                TblReportData data = new TblReportData();
                data.setTaskId(taskId);
                data.setTemplateId(templateId);
                data.setIndicatorId(indicatorId);
                data.setOrgId(orgId);
                data.setPeriod(period);
                data.setDimensionValues(dimensionValues);
                data.setDataValue(dataValue);
                data.setDataSource(dataSource != null && !dataSource.isEmpty() ? dataSource : "MANUAL");
                data.setCellColor(cellColor);
                data.setIsEditable(isEditable != null && !isEditable.isEmpty() ? isEditable : "Y");
                data.setTenantId(tenantId);
                data.setCreateUser(userId);
                data.setCreateTime(now);
                data.setUpdateUser(userId);
                data.setUpdateTime(now);

                dataList.add(data);
                result.addSuccess();

            } catch (Exception e) {
                result.addError(i + 1, "数据解析失败: " + e.getMessage());
            }
        }

        workbook.close();
        inputStream.close();

        // 批量保存数据
        if (!dataList.isEmpty()) {
            reportDataService.batchSaveReportData(dataList);
        }

        return result;
    }

    @Override
    public void exportExcel(Map<String, String> params, HttpServletResponse response) throws Exception {
        String tenantId = UserUtils.getUser().getOrgid().toString();

        // 构建查询参数
        ReportDataQueryParam queryParam = new ReportDataQueryParam();
        queryParam.setTaskId(params.get("taskId"));
        queryParam.setTemplateId(params.get("templateId"));
        queryParam.setIndicatorId(params.get("indicatorId"));
        queryParam.setOrgId(params.get("orgId"));
        queryParam.setPeriod(params.get("period"));
        queryParam.setDataSource(params.get("dataSource"));

        // 查询数据
        List<TblReportData> dataList = reportDataService.getList(queryParam);

        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("报表数据");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "任务名称", "模板名称", "指标名称", "组织ID", "期间",
            "维度值", "数据值", "数据来源", "单元格颜色", "是否可编辑", "创建时间"
        };

        // 创建标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 设置标题
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 20 * 256);
        }

        // 创建数据样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // 填充数据
        int rowNum = 1;
        for (TblReportData data : dataList) {
            Row row = sheet.createRow(rowNum++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(data.getTaskName() != null ? data.getTaskName() : "");
            cell0.setCellStyle(dataStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(data.getTemplateName() != null ? data.getTemplateName() : "");
            cell1.setCellStyle(dataStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(data.getIndicatorName() != null ? data.getIndicatorName() : "");
            cell2.setCellStyle(dataStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(data.getOrgId() != null ? data.getOrgId() : "");
            cell3.setCellStyle(dataStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(data.getPeriod() != null ? data.getPeriod() : "");
            cell4.setCellStyle(dataStyle);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(data.getDimensionValues() != null ? data.getDimensionValues() : "");
            cell5.setCellStyle(dataStyle);

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(data.getDataValue() != null ? data.getDataValue() : "");
            cell6.setCellStyle(dataStyle);

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(data.getDataSource() != null ? data.getDataSource() : "");
            cell7.setCellStyle(dataStyle);

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(data.getCellColor() != null ? data.getCellColor() : "");
            cell8.setCellStyle(dataStyle);

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(data.getIsEditable() != null ? data.getIsEditable() : "");
            cell9.setCellStyle(dataStyle);

            Cell cell10 = row.createCell(10);
            cell10.setCellValue(data.getCreateTime() != null ? data.getCreateTime().toString() : "");
            cell10.setCellStyle(dataStyle);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("报表数据_" + System.currentTimeMillis() + ".xlsx", "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 输出到响应流
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 获取单元格值
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}


