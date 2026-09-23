package com.hbfk.util;

import java.util.List;


import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.hbfk.entity.TblAttachment;

public class FinancialDataExport {
	
	//标识总账 来区分写excel中合并单元格
	public final static String ZONGZHANG = "zongzhang";
	
	public static HSSFWorkbook makeLocalExcel(List<Object[]> objlist,String[] cNames,int[] cWidths,String[] names,String region){
		String name = names[0] + ".xls";
        HSSFFont titleFont = null;
        HSSFCellStyle titleStyle = null; // 标题样式
        HSSFCellStyle contentStyle = null; // 行信息内容样式
        HSSFWorkbook workBook = new HSSFWorkbook();
        setExcelStyle(workBook, titleFont, titleStyle, contentStyle);
        
        HSSFSheet sheet = workBook.createSheet(names[1]);
        
        if (StringUtils.isNotBlank(region)) {
            if (region.equals(ZONGZHANG)) {
                regionMethodByZZ(sheet, objlist.size());
            }
        }
        
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(20);
        
        for (int i = 0; i < cNames.length; i++) {
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellStyle(titleStyle);
            // 使用正确的 CellType 常量
            cell.setCellValue(cNames[i]);
            sheet.setColumnWidth(i, cWidths[i]); // 设置列宽
        }
        
        for (int i = 0; i < objlist.size(); i++) {
            Object[] obj = objlist.get(i);
            if (obj != null && obj.length > 1) {
                HSSFRow row = sheet.createRow(1 + i); // 新建一行
                for (int j = 0; j < obj.length; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(contentStyle);
                    cell.setCellValue(obj[j] == null ? "" : obj[j].toString());
                }
            }
        }
        return workBook;
	}
	
	private static void setExcelStyle(HSSFWorkbook workBook, HSSFFont titleFont, 
            HSSFCellStyle titleStyle, HSSFCellStyle contentStyle) {
		 // 设置列标题字体，样式
	    titleFont = workBook.createFont();
	    titleFont.setBold(true);
	    
	    // 标题列样式 - HSSF 使用 HSSFCellStyle 常量
	    titleStyle = workBook.createCellStyle();
	    titleStyle.setAlignment(HorizontalAlignment.CENTER);
	    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setFont(titleFont);
	    
	    // 内容列样式
	    contentStyle = workBook.createCellStyle();
	    contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
	}
	
	public static void regionMethodByZZ(HSSFSheet sheet, Integer size) {
		// 创建必要的行，避免合并时出现空指针
        for (int i = 1; i <= size + 3; i++) {
            if (sheet.getRow(i) == null) {
                sheet.createRow(i);
            }
        }
        
        for (int i = 1; i <= size; i += 3) {
            // 防止最后一组合并时行索引超出范围
            int endRow = Math.min(i + 2, size);
            CellRangeAddress region1 = new CellRangeAddress(i, endRow, 0, 0);
            sheet.addMergedRegion(region1);
            CellRangeAddress region2 = new CellRangeAddress(i, endRow, 1, 1);
            sheet.addMergedRegion(region2);
        }
	}
	
	
	    public static  void fileDownLoad(HttpServletResponse response, TblAttachment att, Boolean isCa)throws Exception {
	    	TblAttachmentUtil.fileDownLoad(response, att, isCa);
	    }
}
