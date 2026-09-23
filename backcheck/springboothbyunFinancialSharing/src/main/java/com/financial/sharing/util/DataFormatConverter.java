package com.financial.sharing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 数据格式转换工具
 */
@Slf4j
@Component
public class DataFormatConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 转换为Excel格式
     */
    public byte[] convertToExcel(List<?> data, Class<?> clazz) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            if (data == null || data.isEmpty()) {
                return outputStream.toByteArray();
            }

            // 创建工作表
            Sheet sheet = workbook.createSheet("数据导出");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            Field[] fields = clazz.getDeclaredFields();
            int colIndex = 0;

            for (Field field : fields) {
                Cell headerCell = headerRow.createCell(colIndex++);
                headerCell.setCellValue(getColumnName(field));
            }

            // 填充数据
            int rowIndex = 1;
            for (Object item : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                colIndex = 0;

                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    Cell cell = dataRow.createCell(colIndex++);
                    setCellValue(cell, value);
                }
            }

            // 自动调整列宽
            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("转换为Excel失败", e);
            throw new RuntimeException("转换为Excel失败: " + e.getMessage());
        }
    }

    /**
     * 转换为PDF格式
     */
    public byte[] convertToPdf(List<?> data, Class<?> clazz) {
        // 这里需要使用PDF生成库，如iText或PDFBox
        // 暂时返回模拟数据
        try {
            String htmlContent = generateHtmlTable(data, clazz);
            return convertHtmlToPdf(htmlContent);
        } catch (Exception e) {
            log.error("转换为PDF失败", e);
            throw new RuntimeException("转换为PDF失败: " + e.getMessage());
        }
    }

    /**
     * 转换为CSV格式
     */
    public byte[] convertToCsv(List<?> data, Class<?> clazz) {
        StringBuilder csvBuilder = new StringBuilder();

        if (data == null || data.isEmpty()) {
            return csvBuilder.toString().getBytes();
        }

        Field[] fields = clazz.getDeclaredFields();

        // 添加标题行
        for (int i = 0; i < fields.length; i++) {
            csvBuilder.append(getColumnName(fields[i]));
            if (i < fields.length - 1) {
                csvBuilder.append(",");
            }
        }
        csvBuilder.append("\n");

        // 添加数据行
        for (Object item : data) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(item);
                    csvBuilder.append(formatCsvValue(value));
                } catch (IllegalAccessException e) {
                    csvBuilder.append("");
                }
                if (i < fields.length - 1) {
                    csvBuilder.append(",");
                }
            }
            csvBuilder.append("\n");
        }

        return csvBuilder.toString().getBytes();
    }

    /**
     * 转换为JSON格式
     */
    public String convertToJson(List<?> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("转换为JSON失败", e);
            throw new RuntimeException("转换为JSON失败: " + e.getMessage());
        }
    }

    /**
     * 转换为XML格式
     */
    public String convertToXml(List<?> data, String rootName, String itemName) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<").append(rootName).append(">\n");

        if (data != null) {
            for (Object item : data) {
                xmlBuilder.append("  <").append(itemName).append(">\n");
                xmlBuilder.append(convertObjectToXml(item, 2));
                xmlBuilder.append("  </").append(itemName).append(">\n");
            }
        }

        xmlBuilder.append("</").append(rootName).append(">");
        return xmlBuilder.toString();
    }

    // 私有辅助方法
    private String getColumnName(Field field) {
        // 这里可以根据字段注解获取列名
        // 暂时使用字段名
        return field.getName();
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value ? "是" : "否");
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private String formatCsvValue(Object value) {
        if (value == null) {
            return "";
        }
        String strValue = value.toString();
        // 如果包含逗号或引号，需要用引号包围
        if (strValue.contains(",") || strValue.contains("\"")) {
            return "\"" + strValue.replace("\"", "\"\"") + "\"";
        }
        return strValue;
    }

    private String generateHtmlTable(List<?> data, Class<?> clazz) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"><style>");
        html.append("table { border-collapse: collapse; width: 100%; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("</style></head><body>");
        html.append("<table>");

        // 表头
        html.append("<tr>");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            html.append("<th>").append(getColumnName(field)).append("</th>");
        }
        html.append("</tr>");

        // 数据行
        for (Object item : data) {
            html.append("<tr>");
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    html.append("<td>").append(value != null ? value.toString() : "").append("</td>");
                } catch (IllegalAccessException e) {
                    html.append("<td></td>");
                }
            }
            html.append("</tr>");
        }

        html.append("</table></body></html>");
        return html.toString();
    }

    private byte[] convertHtmlToPdf(String htmlContent) {
        // 这里需要使用HTML转PDF的库，如Flying Saucer或PDFBox
        // 暂时返回模拟数据
        return htmlContent.getBytes();
    }

    private String convertObjectToXml(Object obj, int indent) {
        StringBuilder xml = new StringBuilder();
        String indentStr = repeat("  ", indent);

        if (obj == null) {
            return indentStr + "<null/>\n";
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                xml.append(indentStr).append("<").append(field.getName()).append(">");
                xml.append(value != null ? value.toString() : "");
                xml.append("</").append(field.getName()).append(">\n");
            } catch (IllegalAccessException e) {
                // 忽略访问异常
            }
        }

        return xml.toString();
    }

    /**
     * Java 8兼容的字符串重复方法
     * @param str 要重复的字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    private String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}