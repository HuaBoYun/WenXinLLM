package com.hbfk.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tyb 2016-5-17上午10:22:15 导入 导出excel
 */
public class ImportOrExportExcelUtil {
	/**
	 * @author tyb 2016-5-17上午10:29:32 导出 fileUrl:是否指定目录 如浏览器弹出下载框需把此参数设置为空
	 */
	public static void exportExcel(String[] titles, List<Object[]> objList1, ServletOutputStream outputStream, String fileUrl) {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer sheetNumber = objList1.size() % pageSize == 0 ? objList1.size() / pageSize : objList1.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		if(sheetNumber==0){ //数据为空只导出表头
			XSSFSheet sheet = workBook.createSheet("Sheet" + (1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			
			XSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles[i]);
				sheet.autoSizeColumn(i, true);
			}
		}
		for (int k = 0; k < sheetNumber; k++) {
			Integer listNumber = (k + 1) * pageSize > objList1.size() ? objList1.size() : (k + 1) * pageSize;
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = workBook.createSheet("Sheet" + (k + 1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles[i]);
				sheet.autoSizeColumn(i, true);
			}
			int jj = 1;
			// 构建表体数据
			if (objList1 != null && objList1.size() > 0) {
				for (int j = sizeNum; j < listNumber; j++) {
					XSSFRow bodyRow = sheet.createRow(jj);
					Object[] obj = objList1.get(j);
					for (int i = 0; i < obj.length; i++) {
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
					}
					jj++;
					sizeNum++;
				}
			}
		}
		try {
			if (StringUtils.isNotBlank(fileUrl) && !fileUrl.equals("1")) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotBlank(fileUrl)&& !fileUrl.equals("1")) {
					stream.flush();
					stream.close();
				} else {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void exportExcelSetWith(String[] titles, List<Object[]> objList1, ServletOutputStream outputStream,int[] cWidths, String fileUrl) {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer sheetNumber = objList1.size() % pageSize == 0 ? objList1.size() / pageSize : objList1.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		if(sheetNumber==0){ //数据为空只导出表头
			XSSFSheet sheet = workBook.createSheet("Sheet" + (1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			
			XSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles[i]);
				sheet.setColumnWidth(i, cWidths[i]);
			}
		}
		for (int k = 0; k < sheetNumber; k++) {
			Integer listNumber = (k + 1) * pageSize > objList1.size() ? objList1.size() : (k + 1) * pageSize;
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = workBook.createSheet("Sheet" + (k + 1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles[i]);
				sheet.setColumnWidth(i, cWidths[i]);
			}
			int jj = 1;
			// 构建表体数据
			if (objList1 != null && objList1.size() > 0) {
				for (int j = sizeNum; j < listNumber; j++) {
					XSSFRow bodyRow = sheet.createRow(jj);
					Object[] obj = objList1.get(j);
					for (int i = 0; i < obj.length; i++) {
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						//sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
					}
					jj++;
					sizeNum++;
				}
			}
		}
		try {
			if (StringUtils.isNotBlank(fileUrl) && !fileUrl.equals("1")) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotBlank(fileUrl)&& !fileUrl.equals("1")) {
					stream.flush();
					stream.close();
				} else {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void exportExcelsj(List<String> titles, List<Object[]> objList1, ServletOutputStream outputStream, String fileUrl) {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer sheetNumber = objList1.size() % pageSize == 0 ? objList1.size() / pageSize : objList1.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		for (int k = 0; k < sheetNumber; k++) {
			Integer listNumber = (k + 1) * pageSize > objList1.size() ? objList1.size() : (k + 1) * pageSize;
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = workBook.createSheet("Sheet" + (k + 1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = null;
			for (int i = 0; i < titles.size(); i++) {
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles.get(i));
				sheet.autoSizeColumn(i, true);
			}
			int jj = 1;
			// 构建表体数据
			if (objList1 != null && objList1.size() > 0) {
				for (int j = sizeNum; j < listNumber; j++) {
					XSSFRow bodyRow = sheet.createRow(jj);
					Object[] obj = objList1.get(j);
					for (int i = 0; i < obj.length; i++) {
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						//sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
					}
					jj++;
					sizeNum++;
				}
			}
		}
		try {
			if (StringUtils.isNotBlank(fileUrl)) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotBlank(fileUrl)) {
					stream.flush();
					stream.close();
				} else {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author tyb 2016-5-17上午10:29:43 导入
	 */
	public static void importExcel(String url) throws IOException {
		XSSFWorkbook workBook = new XSSFWorkbook(FileUtils.openInputStream(new File(url)));
		XSSFSheet sheet = workBook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i <= lastRowNum; i++) {
			XSSFRow row = sheet.getRow(i);
			int lastCellNum = row.getLastCellNum();
			for (int k = 0; k < lastCellNum; k++) {
				XSSFCell cell = row.getCell(k);
				String value = cell.getStringCellValue();
				System.out.print("   " + value);
			}
			System.out.println();
		}
	}



	public static void exportExcelIssuesLedgetList(String[] titles, List<List<Object>> dataList,ServletOutputStream outputStream,int[] cWidths, String fileUrl) throws Exception {
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer listNumber = 50000;
		Integer sheetNumber = dataList.size() % pageSize == 0 ? dataList.size() / pageSize : dataList.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		Integer rowColCount = 0;//跨行数量
		int index = 1;//每页下标 0-表头，从1开始
		int rowColIndex = 1;//跨行下标， 0-表头 ，一开始
		HSSFCellStyle titleStyle = null; // 标题样式
		HSSFCellStyle contentStyle = null; // 行信息内容样式
		HSSFFont titleFont = null;
		HSSFSheet sheet = null;
		HSSFRow titleRow = null;
		HSSFCell cell = null;
		List<Object> objs = new ArrayList<Object>(0);
		HSSFRow bodyRow = null;
		try {
			// 设置列标题字体，样式
			titleFont = workBook.createFont();
			titleFont.setBold(true);
			// 标题列样式
			titleStyle = workBook.createCellStyle();
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			titleStyle.setBorderTop(BorderStyle.THIN);
			titleStyle.setBorderBottom(BorderStyle.THIN);
			titleStyle.setBorderLeft(BorderStyle.THIN);
			titleStyle.setBorderRight(BorderStyle.THIN);
			titleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setFont(titleFont);
			// 内容列样式
			contentStyle = workBook.createCellStyle();
			contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        contentStyle.setAlignment(HorizontalAlignment.CENTER);
			contentStyle.setBorderTop(BorderStyle.THIN);
	        contentStyle.setBorderBottom(BorderStyle.THIN);
	        contentStyle.setBorderLeft(BorderStyle.THIN);
	        contentStyle.setBorderRight(BorderStyle.THIN);
	        contentStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	        contentStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	        contentStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	        contentStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			contentStyle.setWrapText(true);
			//setExcelStyle(workBook, titleFont, titleStyle, contentStyle);
			for (int k = 0; k < sheetNumber; k++) {
				listNumber = (k + 1) * pageSize > dataList.size() ? dataList.size() : (k + 1) * pageSize;
				// 在workbook中添加一个sheet,对应Excel文件中的sheet
				sheet = workBook.createSheet("Sheet" + (k + 1));
				titleRow = sheet.createRow((short) 0);
				titleRow.setHeightInPoints(60);
				for (int i = 0; i < titles.length; i++) {
					cell = titleRow.createCell(i);
					cell.setCellStyle(titleStyle);
					cell.setCellValue(titles[i]);
					sheet.setColumnWidth(i, cWidths[i]);
				}
				
				index = 1;
				// 构建表体数据
				
				if (dataList != null && dataList.size() > 0) {
					for (int j = sizeNum; j < listNumber; j++) {
						bodyRow = sheet.createRow(index); // 新建一行;
						bodyRow.setHeightInPoints(30);
						objs = dataList.get(j);
						//设置每行单元格格式
						rowColCount = Integer.parseInt(objs.get(objs.size()-1).toString());
						if(rowColCount > 1) {
							setCellRangeAddressExportIssues(sheet,index,index+rowColCount-1);
						}
						for (int i = 0; i < objs.size()-1; i++) {
							cell = bodyRow.createCell(i);
							cell.setCellStyle(contentStyle);
							cell.setCellValue(objs.get(i)==null?"":objs.get(i).toString());
							//sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
						}
						sizeNum++;
						index++;
						rowColIndex++;
					}
				}
			}
			
			if (StringUtils.isNotBlank(fileUrl)) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}		}finally {
			outputStream.flush();
			outputStream.close();
		}
	}



	private static void setCellRangeAddressExportIssues(HSSFSheet sheet,int index, int rowColCount) {
		CellRangeAddress region1 = new CellRangeAddress(index, rowColCount, (short) 0, (short) 0);
		sheet.addMergedRegion(region1);
		CellRangeAddress region2 = new CellRangeAddress(index, rowColCount, (short) 1, (short) 1);
		sheet.addMergedRegion(region2);
		CellRangeAddress region3 = new CellRangeAddress(index, rowColCount, (short) 2, (short) 2);
		sheet.addMergedRegion(region3);
	}



	public static void exportZglsZgqdExcel(String titleone, String titletwo, String[] titlethree, String[] titlefour,
			String[] titlefive, List<Object[]> objList, ServletOutputStream outputStream, String fileUrl) {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer sheetNumber = objList.size() % pageSize == 0 ? objList.size() / pageSize : objList.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		int cellIndex = 0;//生成列
		if(sheetNumber==0){ //数据为空只导出表头
			XSSFSheet sheet = workBook.createSheet("Sheet" + (1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = headRow.createCell(0);
			cell.setCellValue(titleone);
			sheet.autoSizeColumn(0, true);
			 cell.setCellStyle(headStyle);
			// first row (0-based) last row  (0-based) first column (0-based) last column (0-based)
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,51));
	        
	        for (int m = 1 ; m <= 51; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
	        
			headRow = sheet.createRow(1);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
			cell.setCellValue(titletwo);
			sheet.autoSizeColumn(0, true);
			cell.setCellStyle(headStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,51));
			
			
			for (int m = 1 ; m <= 51; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			headRow = sheet.createRow(2);
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlethree.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellValue(titlethree[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
					sheet.addMergedRegion(new CellRangeAddress(2,4,cellIndex,cellIndex));
					cellIndex++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+1));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+2;
					break;
				case 5:
				case 6:
				case 7:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+3));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+4;
					break;
				case 8:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+2));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+3;
					
					break;
				case 9:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+4));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+4);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+5;
					break;
				case 10:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+25));
					for(int m =1; m <= 25 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+26;
					break;
				default:
					break;
				}
			}
			
			headRow = sheet.createRow(3);
			cellIndex = 1;
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlefour.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefour[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 27:
				case 28:
				case 29:
				case 30:
				case 31:
				case 32:
				case 35:
				case 36:
				case 37:
					sheet.addMergedRegion(new CellRangeAddress(3,4,cellIndex,cellIndex));
					cellIndex ++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(3,4,cellIndex,cellIndex));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex + 4;
					break;
				case 25:
					sheet.addMergedRegion(new CellRangeAddress(3,3,cellIndex,cellIndex+7));
					for(int m =1; m <= 7 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex + 8;
					break;
				case 26:
				case 33:
				case 34:
					sheet.addMergedRegion(new CellRangeAddress(3,3,cellIndex,cellIndex+1));
					for(int m =1; m <= 1 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex + 2;
					break;
				}
			}
			
			cellIndex = 29;
			headRow = sheet.createRow(4);
			headRow.setHeight((short) 600);
			for(int m = 0 ; m <= 28 ; m++) {
				cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			for (int i = 0; i < titlefive.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefive[i]);
				sheet.autoSizeColumn(cellIndex, true);
				if(cellIndex == 38) {
					
					for(int m =1; m <= 6 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+7;
				}else {
					cellIndex++;
				}
			}
				cell = headRow.createCell(cellIndex+1);
	        	cell.setCellStyle(headStyle);
	        	cell = headRow.createCell(cellIndex+2);
	        	cell.setCellStyle(headStyle);
			
		}
		for (int k = 0; k < sheetNumber; k++) {
			Integer listNumber = (k + 1) * pageSize > objList.size() ? objList.size() : (k + 1) * pageSize;
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = workBook.createSheet("Sheet" + (k + 1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = headRow.createCell(0);
			cell.setCellValue(titleone);
			sheet.autoSizeColumn(0, true);
			 cell.setCellStyle(headStyle);
			// first row (0-based) last row  (0-based) first column (0-based) last column (0-based)
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,51));
	        
	        for (int m = 1 ; m <= 51; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
	        
			headRow = sheet.createRow(1);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
			cell.setCellValue(titletwo);
			sheet.autoSizeColumn(0, true);
			cell.setCellStyle(headStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,51));
			
			
			for (int m = 1 ; m <= 51; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			headRow = sheet.createRow(2);
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlethree.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellValue(titlethree[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
					sheet.addMergedRegion(new CellRangeAddress(2,4,cellIndex,cellIndex));
					cellIndex++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+1));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+2;
					break;
				case 5:
				case 6:
				case 7:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+3));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+4;
					break;
				case 8:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+2));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+3;
					
					break;
				case 9:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+4));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+4);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+5;
					break;
				case 10:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+25));
					for(int m =1; m <= 25 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+26;
					break;
				default:
					break;
				}
			}
			
			headRow = sheet.createRow(3);
			cell = headRow.createCell(0);
			cell.setCellStyle(headStyle);
			cellIndex = 1;
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlefour.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefour[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 27:
				case 28:
				case 29:
				case 30:
				case 31:
				case 32:
				case 35:
				case 36:
				case 37:
					sheet.addMergedRegion(new CellRangeAddress(3,4,cellIndex,cellIndex));
					cellIndex ++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(3,4,cellIndex,cellIndex));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex + 4;
					break;
				case 25:
					sheet.addMergedRegion(new CellRangeAddress(3,3,cellIndex,cellIndex+7));
					for(int m =1; m <= 7 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex + 8;
					break;
				case 26:
				case 33:
				case 34:
					sheet.addMergedRegion(new CellRangeAddress(3,3,cellIndex,cellIndex+1));
					for(int m =1; m <= 1 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex + 2;
					break;
				}
			}
			
			cellIndex = 29;
			headRow = sheet.createRow(4);
			headRow.setHeight((short) 600);
			for(int m = 0 ; m <= 28 ; m++) {
				cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			for (int i = 0; i < titlefive.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefive[i]);
				sheet.autoSizeColumn(cellIndex, true);
				if(cellIndex == 38) {
					
					for(int m =1; m <= 6 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+7;
				}else {
					cellIndex++;
				}
			}
				cell = headRow.createCell(cellIndex+1);
	        	cell.setCellStyle(headStyle);
	        	cell = headRow.createCell(cellIndex+2);
	        	cell.setCellStyle(headStyle);
			
			int jj = 5;
			// 构建表体数据
			if (objList != null && objList.size() > 0) {
				for (int j = sizeNum; j < listNumber; j++) {
					XSSFRow bodyRow = sheet.createRow(jj);
					Object[] obj = objList.get(j);
					for (int i = 0; i < obj.length; i++) {
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
					}
					jj++;
					sizeNum++;
				}
			}
		}
		try {
			if (StringUtils.isNotBlank(fileUrl) && !fileUrl.equals("1")) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotBlank(fileUrl)&& !fileUrl.equals("1")) {
					stream.flush();
					stream.close();
				} else {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	public static void exportZglsZgqdExcel(String titleone, String titletwo, String[] titlethree, String[] titlefour,
			List<Object[]> objList, ServletOutputStream outputStream,  String fileUrl) {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer sheetNumber = objList.size() % pageSize == 0 ? objList.size() / pageSize : objList.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		int cellIndex = 0;//生成列
		if(sheetNumber==0){ //数据为空只导出表头
			XSSFSheet sheet = workBook.createSheet("Sheet" + (1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = headRow.createCell(0);
			cell.setCellValue(titleone);
			sheet.autoSizeColumn(0, true);
			 cell.setCellStyle(headStyle);
			// first row (0-based) last row  (0-based) first column (0-based) last column (0-based)
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,14));
	        
	        for (int m = 1 ; m <= 14; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
	        
			headRow = sheet.createRow(1);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
			cell.setCellValue(titletwo);
			sheet.autoSizeColumn(0, true);
			cell.setCellStyle(headStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,14));
			
			
			for (int m = 1 ; m <= 14; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			headRow = sheet.createRow(2);
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlethree.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellValue(titlethree[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 7:
					sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex,cellIndex));
					cellIndex++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+1));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+2;
					break;
				case 5:
				case 6:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+3));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+4;
					break;
				default:
					break;
				}
			}
			
			headRow = sheet.createRow(3);
			cell = headRow.createCell(0);
			cell.setCellStyle(headStyle);
			cellIndex = 1;
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlefour.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefour[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				if(i == 1) {
					for(int m =1; m <= 3 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+4;
				}else {
					cellIndex++;
				}
			}
			cell = headRow.createCell(cellIndex);
			cell.setCellStyle(headStyle);
		}
		
		
		for (int k = 0; k < sheetNumber; k++) {
			Integer listNumber = (k + 1) * pageSize > objList.size() ? objList.size() : (k + 1) * pageSize;
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = workBook.createSheet("Sheet" + (k + 1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = headRow.createCell(0);
			cell.setCellValue(titleone);
			sheet.autoSizeColumn(0, true);
			 cell.setCellStyle(headStyle);
			// first row (0-based) last row  (0-based) first column (0-based) last column (0-based)
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,14));
	        
	        for (int m = 1 ; m <= 14; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
	        
			headRow = sheet.createRow(1);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
			cell.setCellValue(titletwo);
			sheet.autoSizeColumn(0, true);
			cell.setCellStyle(headStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,14));
			
			
			for (int m = 1 ; m <= 14; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			headRow = sheet.createRow(2);
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlethree.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellValue(titlethree[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 7:
					sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex,cellIndex));
					cellIndex++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+1));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+2;
					break;
				case 5:
				case 6:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+3));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+4;
					break;
				default:
					break;
				}
			}
			
			headRow = sheet.createRow(3);
			cell = headRow.createCell(0);
			cell.setCellStyle(headStyle);
			cellIndex = 1;
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlefour.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefour[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				if(i == 1) {
					for(int m =1; m <= 3 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+4;
				}else {
					cellIndex++;
				}
			}
			cell = headRow.createCell(cellIndex);
			cell.setCellStyle(headStyle);
			
			int jj = 4;
			// 构建表体数据
			if (objList != null && objList.size() > 0) {
				for (int j = sizeNum; j < listNumber; j++) {
					XSSFRow bodyRow = sheet.createRow(jj);
					Object[] obj = objList.get(j);
					for (int i = 0; i < obj.length; i++) {
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
					}
					jj++;
					sizeNum++;
				}
			}
		}
		try {
			if (StringUtils.isNotBlank(fileUrl) && !fileUrl.equals("1")) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotBlank(fileUrl)&& !fileUrl.equals("1")) {
					stream.flush();
					stream.close();
				} else {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}



	public static void exportZglsZerqdExcel(String titleone, String titletwo, String[] titlethree, String[] titlefour,
			List<Object[]> objList, ServletOutputStream outputStream, String fileUrl) throws Exception {
		XSSFWorkbook workBook = new XSSFWorkbook();
		// 数据查过五万添加一个sheet页
		int pageSize = 50000;
		Integer sheetNumber = objList.size() % pageSize == 0 ? objList.size() / pageSize : objList.size() / pageSize + 1;
		int sizeNum = 0;
		FileOutputStream stream = null;
		int cellIndex = 0;//生成列
		if(sheetNumber==0){ //数据为空只导出表头
			XSSFSheet sheet = workBook.createSheet("Sheet" + (1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = headRow.createCell(0);
			cell.setCellValue(titleone);
			sheet.autoSizeColumn(0, true);
			 cell.setCellStyle(headStyle);
			// first row (0-based) last row  (0-based) first column (0-based) last column (0-based)
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,23));
	        
	        for (int m = 1 ; m <= 23; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
	        
			headRow = sheet.createRow(1);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
			cell.setCellValue(titletwo);
			sheet.autoSizeColumn(0, true);
			cell.setCellStyle(headStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,23));
			
			
			for (int m = 1 ; m <= 23; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			headRow = sheet.createRow(2);
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlethree.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellValue(titlethree[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
				case 10:
					sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex,cellIndex));
					cellIndex++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+1));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+2;
					break;
				
				case 6:
				case 7:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+3));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+4;
					break;
				case 8:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+2));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+3;
					
					break;
				case 9:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+4));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+4);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+5;
					break;
				default:
					break;
				}
			}
			
			cellIndex = 1;
			headRow = sheet.createRow(3);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
	        cell.setCellStyle(headStyle);
			for (int i = 0; i < titlefour.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefour[i]);
				sheet.autoSizeColumn(cellIndex, true);
				if(cellIndex == 2) {
					
					for(int m =1; m <= 4 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+5;
				}else {
					cellIndex++;
				}
			}
			cell = headRow.createCell(cellIndex);
	        cell.setCellStyle(headStyle);
			
		}
		for (int k = 0; k < sheetNumber; k++) {
			Integer listNumber = (k + 1) * pageSize > objList.size() ? objList.size() : (k + 1) * pageSize;
			// 在workbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = workBook.createSheet("Sheet" + (k + 1));
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			headRow.setHeight((short) 600);
			XSSFCell cell = headRow.createCell(0);
			cell.setCellValue(titleone);
			sheet.autoSizeColumn(0, true);
			 cell.setCellStyle(headStyle);
			// first row (0-based) last row  (0-based) first column (0-based) last column (0-based)
	        sheet.addMergedRegion(new CellRangeAddress(0,0,0,23));
	        
	        for (int m = 1 ; m <= 23; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
	        
			headRow = sheet.createRow(1);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
			cell.setCellValue(titletwo);
			sheet.autoSizeColumn(0, true);
			cell.setCellStyle(headStyle);
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,23));
			
			
			for (int m = 1 ; m <= 23; m++) {
	        	cell = headRow.createCell(m);
	        	cell.setCellStyle(headStyle);
			}
			
			headRow = sheet.createRow(2);
			headRow.setHeight((short) 600);
			for (int i = 0; i < titlethree.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellValue(titlethree[i]);
				sheet.autoSizeColumn(cellIndex, true);
				cell.setCellStyle(headStyle);
				switch (i) {
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
				case 10:
					sheet.addMergedRegion(new CellRangeAddress(2,3,cellIndex,cellIndex));
					cellIndex++;
					break;
				case 1:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+1));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+2;
					break;
				
				case 6:
				case 7:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+3));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+4;
					break;
				case 8:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+2));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+3;
					
					break;
				case 9:
					sheet.addMergedRegion(new CellRangeAddress(2,2,cellIndex,cellIndex+4));
					cell = headRow.createCell(cellIndex+1);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+2);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+3);
		        	cell.setCellStyle(headStyle);
		        	cell = headRow.createCell(cellIndex+4);
		        	cell.setCellStyle(headStyle);
					cellIndex = cellIndex+5;
					break;
				default:
					break;
				}
			}
			
			cellIndex = 1;
			headRow = sheet.createRow(3);
			headRow.setHeight((short) 600);
			cell = headRow.createCell(0);
	        cell.setCellStyle(headStyle);
			for (int i = 0; i < titlefour.length; i++) {
				cell = headRow.createCell(cellIndex);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titlefour[i]);
				sheet.autoSizeColumn(cellIndex, true);
				if(cellIndex == 2) {
					
					for(int m =1; m <= 4 ;m++) {
						cell = headRow.createCell(cellIndex+m);
			        	cell.setCellStyle(headStyle);
					}
					cellIndex = cellIndex+5;
				}else {
					cellIndex++;
				}
			}
			cell = headRow.createCell(cellIndex);
	        cell.setCellStyle(headStyle);
			
			int jj = 4;
			// 构建表体数据
			if (objList != null && objList.size() > 0) {
				for (int j = sizeNum; j < listNumber; j++) {
					XSSFRow bodyRow = sheet.createRow(jj);
					Object[] obj = objList.get(j);
					for (int i = 0; i < obj.length; i++) {
						cell = bodyRow.createCell(i);
						cell.setCellStyle(bodyStyle);
						cell.setCellValue(obj[i] == null ? "" : obj[i].toString());
						sheet.autoSizeColumn(i, true); // 设置单元格宽度自适应
					}
					jj++;
					sizeNum++;
				}
			}
		}
		try {
			if (StringUtils.isNotBlank(fileUrl) && !fileUrl.equals("1")) {
				File file = new File(fileUrl);
				file.createNewFile();
				stream = FileUtils.openOutputStream(file);
				workBook.write(stream);
			} else {
				workBook.write(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (StringUtils.isNotBlank(fileUrl)&& !fileUrl.equals("1")) {
					stream.flush();
					stream.close();
				} else {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}