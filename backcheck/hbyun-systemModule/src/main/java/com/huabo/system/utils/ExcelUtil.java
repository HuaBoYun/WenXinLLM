package com.huabo.system.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.date.DateTime;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ExcelUtil {

    private HSSFWorkbook wb = null;
    private HSSFSheet sheet = null;

    public ExcelUtil(HSSFWorkbook wb, HSSFSheet sheet) {
        this.wb = wb;
        this.sheet = sheet;
    }

    public ExcelUtil() {
        this.wb = new HSSFWorkbook();
    }

    public ExcelUtil(String sheetName, String[] titles) {
        this.wb = new HSSFWorkbook();
        this.sheet = this.wb.createSheet(sheetName);
        HSSFCellStyle headStyle = this.getHeadStyle();
        HSSFRow headRow = this.sheet.createRow(0);
        HSSFCell cell = null;

        for(int i = 0; i < titles.length; ++i) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
            this.sheet.setColumnWidth(i, 5000);
        }

    }

    public HSSFRow getNextRow(int row) {
        return this.sheet.createRow(row);
    }

    public HSSFSheet getHSSFSheet(String sheetName) {
        this.sheet = this.wb.createSheet(sheetName);
        return this.sheet;
    }

    public void writeOut(String filename) throws IOException {
        OutputStream out = new FileOutputStream(filename);
        this.wb.write(out);
        out.close();
        this.wb.close();
    }

    public void writeOut(HttpServletResponse response, String fileName) {
        ServletOutputStream os = null;
        response.setContentType("application/ms-excel;");

        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        } catch (UnsupportedEncodingException var16) {
            var16.printStackTrace();
        }

        try {
            os = response.getOutputStream();
            this.wb.write(os);
            os.flush();
            os.close();
        } catch (IOException var14) {
            var14.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

    }

    public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs) {
        int toprowNum = region.getFirstRow();

        for(int i = toprowNum; i <= region.getLastRow(); ++i) {
            HSSFRow row = this.sheet.getRow(i);

            for(int j = region.getFirstColumn(); j <= region.getLastColumn(); ++j) {
                HSSFCell cell = row.getCell(j);
                cell.setCellStyle(cs);
            }
        }

    }

    public HSSFCellStyle getHeadStyle() {
        HSSFCellStyle cellStyle = this.wb.createCellStyle();
     // 新版本使用 IndexedColors 和枚举
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(false);
        HSSFFont font = this.wb.createFont();
        font.setBold(true);  // 新版本方法
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);  // 设置字号为10磅
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
        
    }

    public HSSFCellStyle getBodyStyle() {
        HSSFCellStyle cellStyle = this.wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        HSSFFont font = this.wb.createFont();
        font.setFontHeight((short)200);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
    }

    public void createTitle(String[] titles, String[] titleTwo, String[] titlescored, String sheetname) throws Exception {
        this.sheet = this.wb.createSheet(sheetname);
        CellRangeAddress callRangeAddress1 = new CellRangeAddress(0, 1, 0, 0);
        CellRangeAddress callRangeAddress2 = new CellRangeAddress(0, 1, 1, 1);
        CellRangeAddress callRangeAddress3 = new CellRangeAddress(0, 1, 2, 2);
        CellRangeAddress callRangeAddress4 = new CellRangeAddress(0, 0, 3, 6);
        CellRangeAddress callRangeAddress5 = new CellRangeAddress(0, 1, 7, 7);
        CellRangeAddress callRangeAddress6 = new CellRangeAddress(0, 1, 8, 8);
        CellRangeAddress callRangeAddress7 = new CellRangeAddress(0, 1, 9, 9);
        CellRangeAddress callRangeAddress8 = new CellRangeAddress(0, 0, 10, 11);
        CellRangeAddress callRangeAddress10 = new CellRangeAddress(0, 1, 12, 12);
        CellRangeAddress callRangeAddress11 = new CellRangeAddress(0, 1, 13, 13);
        CellRangeAddress callRangeAddress12 = new CellRangeAddress(0, 1, 14, 14);
        CellRangeAddress callRangeAddress13 = new CellRangeAddress(0, 1, 15, 15);
        CellRangeAddress callRangeAddress14 = new CellRangeAddress(0, 1, 16, 16);
        CellRangeAddress callRangeAddress15 = new CellRangeAddress(0, 1, 17, 17);
        CellRangeAddress callRangeAddress16 = new CellRangeAddress(0, 1, 18, 18);
        CellRangeAddress callRangeAddress17 = new CellRangeAddress(0, 1, 19, 19);
        CellRangeAddress callRangeAddress18 = new CellRangeAddress(0, 1, 20, 20);
        CellRangeAddress callRangeAddress25 = new CellRangeAddress(0, 1, 21, 21);
        CellRangeAddress callRangeAddress19 = new CellRangeAddress(1, 1, 4, 4);
        CellRangeAddress callRangeAddress20 = new CellRangeAddress(1, 1, 5, 5);
        CellRangeAddress callRangeAddress21 = new CellRangeAddress(1, 1, 6, 6);
        CellRangeAddress callRangeAddress22 = new CellRangeAddress(1, 1, 7, 7);
        CellRangeAddress callRangeAddress23 = new CellRangeAddress(1, 1, 10, 10);
        CellRangeAddress callRangeAddress24 = new CellRangeAddress(1, 1, 11, 11);
        this.sheet.addMergedRegion(callRangeAddress1);
        this.sheet.addMergedRegion(callRangeAddress2);
        this.sheet.addMergedRegion(callRangeAddress3);
        this.sheet.addMergedRegion(callRangeAddress4);
        this.sheet.addMergedRegion(callRangeAddress5);
        this.sheet.addMergedRegion(callRangeAddress6);
        this.sheet.addMergedRegion(callRangeAddress7);
        this.sheet.addMergedRegion(callRangeAddress8);
        this.sheet.addMergedRegion(callRangeAddress10);
        this.sheet.addMergedRegion(callRangeAddress11);
        this.sheet.addMergedRegion(callRangeAddress12);
        this.sheet.addMergedRegion(callRangeAddress13);
        this.sheet.addMergedRegion(callRangeAddress14);
        this.sheet.addMergedRegion(callRangeAddress15);
        this.sheet.addMergedRegion(callRangeAddress16);
        this.sheet.addMergedRegion(callRangeAddress17);
        this.sheet.addMergedRegion(callRangeAddress18);
        this.sheet.addMergedRegion(callRangeAddress19);
        this.sheet.addMergedRegion(callRangeAddress20);
        this.sheet.addMergedRegion(callRangeAddress21);
        this.sheet.addMergedRegion(callRangeAddress22);
        this.sheet.addMergedRegion(callRangeAddress23);
        this.sheet.addMergedRegion(callRangeAddress24);
        this.sheet.addMergedRegion(callRangeAddress25);
        HSSFRow row2 = this.sheet.createRow(0);
        System.out.println(titles.length);

        for(int i = 0; i < titles.length; ++i) {
            HSSFCell cell2 = row2.createCell(i);
            cell2.setCellStyle(this.getHeadStyle());
            cell2.setCellValue(titles[i]);
        }

        HSSFRow row3 = this.sheet.createRow(1);

        HSSFCell cell2;
        int i;
        for(i = 0; i < titles.length; ++i) {
            cell2 = row3.createCell(i);
            cell2.setCellStyle(this.getHeadStyle());
        }

        for(i = 0; i < titles.length; ++i) {
            if (i < titlescored.length) {
                cell2 = row3.createCell(i + 3);
                cell2.setCellStyle(this.getHeadStyle());
                cell2.setCellValue(titlescored[i]);
            }

            if (i < titleTwo.length) {
                cell2 = row3.createCell(i + 10);
                cell2.setCellStyle(this.getHeadStyle());
                cell2.setCellValue(titleTwo[i]);
            }
        }

    }

    public static HashMap<String, ArrayList<String[]>> analysisFile(MultipartFile file) throws Exception {
        HashMap<String, ArrayList<String[]>> hashMap = new HashMap();
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        if (filename.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (filename.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }

        ArrayList<String[]> arrayList = new ArrayList();
        if (workbook != null) {
            Sheet sheet = null;

            for(int sheetNum = 0; sheetNum < ((Workbook)workbook).getNumberOfSheets(); ++sheetNum) {
                sheet = ((Workbook)workbook).getSheetAt(sheetNum);
                if (sheet == null) {
                    hashMap.put("文件sheet为空!", arrayList);
                    return hashMap;
                }

                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();

                for(int rowNum = firstRowNum; rowNum <= lastRowNum; ++rowNum) {
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        short firstCellNum = row.getFirstCellNum();
                        short lastCellNum = row.getLastCellNum();
                        String[] strings = new String[lastCellNum];

                        for(int cellNum = firstCellNum; cellNum < lastCellNum; ++cellNum) {
                            Cell cell = row.getCell(cellNum);
                            if (cell != null && !"".equals(cell) && cell.getCellType() != CellType.BLANK) {
                                String cellValue = "";
                                cellValue = getCellValue(cell);
                                strings[cellNum] = cellValue;
                            } else {
                                strings[cellNum] = "";
                            }
                        }

                        arrayList.add(strings);
                    }
                }
            }
        }

        hashMap.put("OK", arrayList);
        inputStream.close();
        return hashMap;
    }

    public static String getCellValue(Cell cell) {
    	CellType cellType = cell.getCellType();//获取当前数据类型
        Object cellValue = null;

        switch (cellType) {
            case NUMERIC://数字类型(日期、普通数据)
            	if (DateUtil.isCellDateFormatted(cell)) {
                    Date dateCellValue = cell.getDateCellValue();
                    cellValue = new DateTime(dateCellValue).toString("yyyy-MM-dd");
                } else {
                    // 防止数字太长，转为String
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.toString();
                }
                break;
            case STRING://String
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN://布尔
                cellValue = cell.getBooleanCellValue();
                break;
            case BLANK://空null
                cellValue = null;
                break;
            case ERROR://错误类型
                System.out.println("数据类型错误");
                break;
            default:
                System.out.println("未知类型");
                throw new RuntimeException("EXCEL 导入未知类型错误");
        }
        return cellValue.toString();
    }
}
