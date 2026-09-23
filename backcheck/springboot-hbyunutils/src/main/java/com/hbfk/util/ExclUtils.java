package com.hbfk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.hutool.core.date.DateTime;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExclUtils {
    private static final String DOCDIC = PropertyFileReader.getItem("file.path");
    private static final String separator = System.getProperty("file.separator");

    /**
     * 生成excl  默认列宽4000
     * <p>Title:</p>
     * <p>Description:</p>
     *
     * @throws IOException
     * @author SongXiangYing
     * @date 2016年9月6日 下午2:02:27
     */
    public static String[] generateExcl(String sheetName, String[] heads, List<List<Object>> dataList) throws IOException {
    	int[] headslength = new int[heads.length];
        for (int i = 0; i < heads.length; i++) {
            headslength[i] = 4000;
        }
        return generateExcl(sheetName, heads, headslength, dataList);
    }

    public static String[] generateExclByArray(String sheetName, String[] heads, List<Object[]> dataList) throws IOException {
    	int[] headslength = new int[heads.length];
        for (int i = 0; i < heads.length; i++) {
            headslength[i] = heads[i].length() * 1000;
        }
        return generateExclArray(sheetName, heads, headslength, dataList);
    }

    /**
     * 生成excl
     *
     * <p>Title:</p>
     * <p>Description:</p>
     *
     * @throws IOException
     * @author SongXiangYing
     * @date 2016年9月6日 下午1:02:53
     */
    public static String[] generateExcl(String sheetName, String[] heads, int[] headslength, List<List<Object>> dataList) throws IOException {
	        HSSFFont titleFont = null;
	        HSSFCellStyle titleStyle = null; // 标题样式
	        HSSFCellStyle contentStyle = null; // 行信息内容样式
	        HSSFSheet sheet = null;
	        HSSFWorkbook workBook = new HSSFWorkbook();
	        sheet = workBook.createSheet(sheetName);
	        ExclUtils.setExcelStyle(workBook, titleFont, titleStyle, contentStyle);
	        HSSFRow titleRow = sheet.createRow((short) 0);
	        titleRow.setHeightInPoints(20);
	        //List<String>  cNames = (List<String>)map.get("column");
	        for (int i = 0; i < heads.length; i++) {
	            HSSFCell cell = titleRow.createCell(i);
	            cell.setCellStyle(titleStyle);
	            cell.setCellValue(heads[i]);
	            sheet.setColumnWidth((short) 0, headslength[i]);// 设置列宽
	        }
	        for (int i = 0; i < dataList.size(); i++) {
	            HSSFRow row = sheet.createRow((short) 1 + i); // 新建一行
	            List<Object> list = (List<Object>) dataList.get(i);
	            for (int j = 1; j < list.size(); j++) {
	                HSSFCell cell = row.createCell(j);
	                cell.setCellStyle(contentStyle);
	                cell.setCellValue(list.get(j).toString());
	            }
	        }
	        String fileName = UUID.randomUUID().toString() + ".xls";
	        String ftpname = fileName;
	        fileName = ExclUtils.getDatePath() + fileName;
	        String path = DOCDIC + separator + fileName;
	        File localFile = new File(DOCDIC + separator + ExclUtils.getDatePath());
	        if (!localFile.exists()) {
	            localFile.mkdirs();
	        }
	        File targetFile1 = new File(path);
	        if (!targetFile1.exists()) {
	            targetFile1.createNewFile();
	        }
	        FileOutputStream fileOut = new FileOutputStream(path);
	        workBook.write(fileOut);
	        long size = fileOut.getChannel().size();
	        fileOut.flush();
	        fileOut.close();
	
	        try {
	            InputStream input = new FileInputStream(path);
	            boolean flag = FtpUtil.uploadFile(ftpname, input);
	            if (flag) {
	                System.out.println("上传成功");
	            } else {
	                System.out.println("上传失败");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new String[]{ftpname, size + ""};
    }


    public static String[] generateExclArray(String sheetName, String[] heads, int[] headslength, List<Object[]> dataList) throws IOException {
        HSSFFont titleFont = null;
        HSSFCellStyle titleStyle = null; // 标题样式
        HSSFCellStyle contentStyle = null; // 行信息内容样式
        HSSFSheet sheet = null;
        HSSFWorkbook workBook = new HSSFWorkbook();
        sheet = workBook.createSheet(sheetName);
        ExclUtils.setExcelStyle(workBook, titleFont, titleStyle, contentStyle);
        HSSFRow titleRow = sheet.createRow((short) 0);
        titleRow.setHeightInPoints(20);
        //List<String>  cNames = (List<String>)map.get("column");
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = titleRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(heads[i]);
            sheet.setColumnWidth((short) 0, headslength[i]);// 设置列宽
        }
        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow row = sheet.createRow((short) 1 + i); // 新建一行
            Object[] list = (Object[]) dataList.get(i);
            for (int j = 0; j < list.length; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(contentStyle);
                cell.setCellValue(list[j] == null ? "" : list[j].toString());
            }
        }
        String fileName = UUID.randomUUID().toString() + ".xls";
        String ftpname = fileName;
        fileName = ExclUtils.getDatePath() + fileName;
        String path = DOCDIC + separator + fileName;
        File localFile = new File(DOCDIC + separator + ExclUtils.getDatePath());
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        File targetFile1 = new File(path);
        if (!targetFile1.exists()) {
            targetFile1.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        workBook.write(fileOut);
        long size = fileOut.getChannel().size();
        fileOut.flush();
        fileOut.close();
        try {
            InputStream input = new FileInputStream(path);
            boolean flag = FtpUtil.uploadFile(ftpname, input);
            if (flag) {
                System.out.println("上传成功");
            } else {
                System.out.println("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{ftpname, size + ""};
    }

    private static void setExcelStyle(HSSFWorkbook workBook, HSSFFont titleFont,
                                      HSSFCellStyle titleStyle, HSSFCellStyle contentStyle) {
        /** 样式初始化 */
        // 设置列标题字体，样式
        titleFont = workBook.createFont();
        titleFont.setBold(true);
        // 标题列样式
        titleStyle = workBook.createCellStyle();
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(titleFont);
        // 内容列样式
        contentStyle = workBook.createCellStyle();
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentStyle.setAlignment(HorizontalAlignment.LEFT);

    }

    private static String getDatePath() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        return year + separator + (month + 1) + separator + date + separator;
    }


    /**
     * 获取excel列类型的的值
     *
     * @param cell
     * @return
     */
    public static Object getCellValueForStr(Cell cell) {
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
        return cellValue;
    }
}
