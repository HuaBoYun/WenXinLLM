package com.huabo.finance.unit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {

	public static ByteArrayOutputStream exportToExcel(List<String> headers, List<List<Object>> data) 
	        throws IOException {
	        
	        // 1. 创建工作簿和工作表
	        try (Workbook workbook = new XSSFWorkbook()) {
	            Sheet sheet = workbook.createSheet("Sheet1");
	            
	            // 2. 创建样式
	            CellStyle headerStyle = createHeaderStyle(workbook);
	            
	            // 3. 创建表头行
	            Row headerRow = sheet.createRow(0);
	            for (int i = 0; i < headers.size(); i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(headers.get(i));
	                cell.setCellStyle(headerStyle);
	                // 设置列宽（可选）
	                sheet.setColumnWidth(i, 20 * 256); // 20个字符宽度
	            }
	            
	            // 4. 填充数据行
	            for (int rowIdx = 0; rowIdx < data.size(); rowIdx++) {
	                Row row = sheet.createRow(rowIdx + 1);
	                List<Object> rowData = data.get(rowIdx);
	                
	                for (int colIdx = 0; colIdx < rowData.size(); colIdx++) {
	                    Cell cell = row.createCell(colIdx);
	                    Object value = rowData.get(colIdx);
	                    
	                    if (value instanceof Number) {
	                        cell.setCellValue(((Number) value).doubleValue());
	                    } else if (value instanceof Boolean) {
	                        cell.setCellValue((Boolean) value);
	                    } else {
	                        cell.setCellValue(value != null ? value.toString() : "");
	                    }
	                }
	            }
	            
	            // 6. 写入输出流
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            workbook.write(outputStream);
	            return outputStream;
	        }
	    }
	    
	    private static CellStyle createHeaderStyle(Workbook workbook) {
	        CellStyle style = workbook.createCellStyle();
	        
	        // 设置背景色
	        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        
	        // 设置边框
	        style.setBorderBottom(BorderStyle.THIN);
	        style.setBorderTop(BorderStyle.THIN);
	        style.setBorderLeft(BorderStyle.THIN);
	        style.setBorderRight(BorderStyle.THIN);
	        
	        // 设置字体
	        Font font = workbook.createFont();
	        font.setBold(true);
	        style.setFont(font);
	        return style;
	    }
}
