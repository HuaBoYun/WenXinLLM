package com.huabo.fxgl.util;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ExcelUtil {
	private HSSFWorkbook wb = null;  
    private HSSFSheet sheet = null;  
  
    /** 
     * @param wb 
     * @param sheet 
     */  
    public ExcelUtil(HSSFWorkbook wb, HSSFSheet sheet)  
    {  
        this.wb = wb;  
        this.sheet = sheet;  
    }  
    public ExcelUtil()  
    {  
    	wb = new HSSFWorkbook();  
    }
   
    public ExcelUtil(String sheetName, String[] titles){
    	wb = new HSSFWorkbook();  
    	sheet = wb.createSheet(sheetName);
		HSSFCellStyle headStyle = this.getHeadStyle();  

        
        HSSFRow headRow = sheet.createRow(0);  
        HSSFCell cell = null;  
        for (int i = 0; i < titles.length; i++)  
        {  
            cell = headRow.createCell(i);  
            cell.setCellStyle(headStyle);  
            cell.setCellValue(titles[i]);  
            sheet.setColumnWidth(i, 5000);
        }          
    }
    public HSSFRow getNextRow(int row){
    	return sheet.createRow(row); 
    }
        public HSSFSheet getHSSFSheet(String sheetName){
    	this.sheet = wb.createSheet(sheetName);
    	return this.sheet;
    }
    public void writeOut(String filename) throws IOException {
    	OutputStream out = new FileOutputStream(filename);
    	wb.write(out);
    	out.close();
    	wb.close();
    }
    public void writeOut(HttpServletResponse response, String fileName){
    	ServletOutputStream os = null;
    	response.setContentType("application/ms-excel;");  
    	try {
			 response.addHeader("Content-Disposition", "attachment;filename=" + new String( fileName.getBytes("GB2312"), "ISO8859-1" )); 
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    
    /** 
     * 合并单元格后给合并后的单元格加边框 
     *  
     * @param region 
     * @param cs 
     */  
    public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs)  
    {  
  
        int toprowNum = region.getFirstRow();  
        for (int i = toprowNum; i <= region.getLastRow(); i++)  
        {  
            HSSFRow row = sheet.getRow(i);  
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++)  
            {  
                HSSFCell cell = row.getCell(j);// XSSFCellUtil.getCell(row,  
                                                // (short) j);  
                cell.setCellStyle(cs);  
            }  
        }  
    }  
  
    /** 
     * 设置表头的单元格样式 
     *  
     * @return 
     */  
    public HSSFCellStyle getHeadStyle()  
    {  
        // 创建单元格样式  
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        // 设置单元格的背景颜色为淡蓝色  
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);  
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        // 设置单元格居中对齐  
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);  
        // 设置单元格垂直居中对齐  
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);  
        // 创建单元格内容显示不下时自动换行  
        cellStyle.setWrapText(true);  
        // 设置单元格字体样式  
        HSSFFont font = wb.createFont();  
        // 设置字体加粗  
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontName("宋体");  
        font.setFontHeight((short) 200);  
        cellStyle.setFont(font);  
        // 设置单元格边框为细线条  
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);  
        return cellStyle;  
    }  
  
    /** 
     * 设置表体的单元格样式 
     *  
     * @return 
     */  
    public HSSFCellStyle getBodyStyle()  
    {  
        // 创建单元格样式  
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        // 设置单元格居中对齐  
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 设置单元格垂直居中对齐  
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 创建单元格内容显示不下时自动换行  
        cellStyle.setWrapText(true);  
        // 设置单元格字体样式  
        HSSFFont font = wb.createFont();  
        // 设置字体加粗  
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontName("宋体");  
        font.setFontHeight((short) 200);  
        cellStyle.setFont(font);  
        // 设置单元格边框为细线条  
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);  
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);  
        return cellStyle;  
    }  
    
    
    public void createTitle(String[] titles, String[] titleTwo,String[] titlescored,String sheetname) throws Exception {
		sheet = this.wb.createSheet(sheetname);
		//第一行
		CellRangeAddress callRangeAddress1 = new CellRangeAddress(0,1,0,0);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress2 = new CellRangeAddress(0,1,1,1);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress3 = new CellRangeAddress(0,1,2,2);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress4 = new CellRangeAddress(0,0,3,6);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress5 = new CellRangeAddress(0,1,7,7);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress6 = new CellRangeAddress(0,1,8,8);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress7 = new CellRangeAddress(0,1,9,9);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress8 = new CellRangeAddress(0,0,10,11);//起始行,结束行,起始列,结束列  整改责任人
		CellRangeAddress callRangeAddress10 = new CellRangeAddress(0,1,12,12);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress11 = new CellRangeAddress(0,1,13,13);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress12 = new CellRangeAddress(0,1,14,14);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress13 = new CellRangeAddress(0,1,15,15);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress14 = new CellRangeAddress(0,1,16,16);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress15 = new CellRangeAddress(0,1,17,17);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress16 = new CellRangeAddress(0,1,18,18);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress17 = new CellRangeAddress(0,1,19,19);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress18 = new CellRangeAddress(0,1,20,20);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress25 = new CellRangeAddress(0,1,21,21);//起始行,结束行,起始列,结束列
		//风险分类
		CellRangeAddress callRangeAddress19 = new CellRangeAddress(1,1,4,4);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress20 = new CellRangeAddress(1,1,5,5);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress21 = new CellRangeAddress(1,1,6,6);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress22 = new CellRangeAddress(1,1,7,7);//起始行,结束行,起始列,结束列
		//整改责任人
		CellRangeAddress callRangeAddress23 = new CellRangeAddress(1,1,10,10);//起始行,结束行,起始列,结束列
		CellRangeAddress callRangeAddress24 = new CellRangeAddress(1,1,11,11);//起始行,结束行,起始列,结束列
		//加样式
        sheet.addMergedRegion(callRangeAddress1);
        sheet.addMergedRegion(callRangeAddress2);
        sheet.addMergedRegion(callRangeAddress3);
        sheet.addMergedRegion(callRangeAddress4);
        sheet.addMergedRegion(callRangeAddress5);
        sheet.addMergedRegion(callRangeAddress6);
        sheet.addMergedRegion(callRangeAddress7);
        sheet.addMergedRegion(callRangeAddress8);
        sheet.addMergedRegion(callRangeAddress10);
        sheet.addMergedRegion(callRangeAddress11);
        sheet.addMergedRegion(callRangeAddress12);
        sheet.addMergedRegion(callRangeAddress13);
        sheet.addMergedRegion(callRangeAddress14);
        sheet.addMergedRegion(callRangeAddress15);
        sheet.addMergedRegion(callRangeAddress16);
        sheet.addMergedRegion(callRangeAddress17);
        sheet.addMergedRegion(callRangeAddress18);
        sheet.addMergedRegion(callRangeAddress19);
        sheet.addMergedRegion(callRangeAddress20);
        sheet.addMergedRegion(callRangeAddress21);
        sheet.addMergedRegion(callRangeAddress22);
        sheet.addMergedRegion(callRangeAddress23);
        sheet.addMergedRegion(callRangeAddress24);
        sheet.addMergedRegion(callRangeAddress25);
        
        HSSFRow row2 = (HSSFRow) sheet.createRow(0);
        System.out.println(titles.length);
        //第一行整体表格样式
        for(int i=0;i<titles.length;i++){
            HSSFCell cell2 = row2.createCell(i);
            //加载单元格样式
            cell2.setCellStyle(this.getHeadStyle());
            cell2.setCellValue(titles[i]);
        }
        HSSFRow row3= (HSSFRow) sheet.createRow(1);
    	//第二行整体表格样式
        for(int i=0;i<titles.length;i++){
        	HSSFCell cell4 = row3.createCell(i);
        	cell4.setCellStyle(this.getHeadStyle());
        }
        //第二行个别表格样式
        for(int i=0;i<titles.length;i++){
        	if(i<titlescored.length){
        		HSSFCell cell3 = row3.createCell(i+3);
        		cell3.setCellStyle(this.getHeadStyle());
        		cell3.setCellValue(titlescored[i]);
        	}
        	if(i<titleTwo.length){
        		HSSFCell cell2 = row3.createCell(i+10);
        		cell2.setCellStyle(this.getHeadStyle());
        		cell2.setCellValue(titleTwo[i]);
        	}
        	
        }
        
      /*  HSSFRow row4= (HSSFRow) sheet.createRow(1);
        for(int i=0;i<titlescored.length;i++)
        {
            HSSFCell cell2 = row4.createCell(i+2);
            //加载单元格样式
            cell2.setCellStyle(this.getHeadStyle());
            cell2.setCellValue(titlescored[i]);
        }*/
	}
    
    /**
                  * 解析前端页面传上来的Excel 返回map
     * @param file
     * @return
     * @throws Exception
     */
	public static HashMap<String, ArrayList<String[]>> analysisFile(MultipartFile file) throws Exception {
		HashMap<String, ArrayList<String[]>> hashMap = new HashMap<>();
        //获取workbook对象
        Workbook workbook = null;
        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        //根据后缀名是否excel文件
        if(filename.endsWith("xls")){
            //2003
            workbook = new HSSFWorkbook(inputStream);
        }else if(filename.endsWith("xlsx")){
            //2007
            workbook = new XSSFWorkbook(inputStream);
        }
        ArrayList<String[]> arrayList = new ArrayList<>();
		
        if(workbook != null){
        	Sheet sheet = null;
        	for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                //获取第一个sheet
        		sheet = workbook.getSheetAt(sheetNum);
        		 //获取当前sheet开始行和结束行
                if(sheet == null){
                    hashMap.put("文件sheet为空!",arrayList);
                    return hashMap;
                }
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                for(int rowNum = firstRowNum;rowNum <= lastRowNum;rowNum++){
                    //获取当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null) {
                    	continue;
                    }
                    //获取当前行的开始列和结束列
                    short firstCellNum = row.getFirstCellNum();
                    short lastCellNum = row.getLastCellNum();

                    //获取总行数
                    //int lastCellNum2 = row.getPhysicalNumberOfCells();
                    String[] strings = new String[lastCellNum];
                    //循环当前行
                    for(int cellNum = firstCellNum;cellNum < lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        if( cell == null || "".equals(cell) || cell.getCellType()== Cell.CELL_TYPE_BLANK ){
                            /*hashMap.put("第"+(rowNum+1)+"行,第"+(cellNum+1)+"列为空",arrayList);
                            return hashMap;*/
                        	strings[cellNum] = "";
                        }else {
                        	 String  cellValue = "";
                             cellValue = getCellValue(cell);
                             strings[cellNum] = cellValue;
                        }
                    }
                    arrayList.add(strings);
                }
        	}
        }
        hashMap.put("OK",arrayList);
        inputStream.close();
        return hashMap;
	}

	
	//把每一个cell转换为string
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //把数字转换成string，防止12.0这种情况
        if(cell.getCellType() == cell.CELL_TYPE_NUMERIC){
            cell.setCellType(cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字0
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串1
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                //cellValue = String.valueOf(cell.getCellFormula());
                try {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    cellValue = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
