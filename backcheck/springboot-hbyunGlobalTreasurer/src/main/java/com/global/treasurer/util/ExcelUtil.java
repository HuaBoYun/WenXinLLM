package com.global.treasurer.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel导出工具类
 *
 * @author AI Developer
 * @since 2026-02-27
 */
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 导出Excel文件（使用EasyExcel）
     *
     * @param response HTTP响应对象
     * @param dataList 数据列表
     * @param clazz 实体类
     * @param fileName 文件名
     * @param sheetName 工作表名称
     */
    public static void exportExcel(HttpServletResponse response, List<?> dataList, Class<?> clazz, String fileName, String sheetName) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

            // 使用EasyExcel写入数据
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(dataList);

            log.info("Excel导出成功: {}, 数据量: {}", fileName, dataList.size());
        } catch (IOException e) {
            log.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 导出Excel文件（自定义样式）
     *
     * @param response HTTP响应对象
     * @param dataList 数据列表
     * @param clazz 实体类
     * @param fileName 文件名
     * @param sheetName 工作表名称
     */
    public static void exportExcelWithStyle(HttpServletResponse response, List<?> dataList, Class<?> clazz, String fileName, String sheetName) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

            // 设置表头样式
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 设置表头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            // 设置表头背景色
            headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            // 设置表头字体
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 12);
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);

            // 设置内容样式
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 设置内容居左
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            // 设置内容字体
            WriteFont contentWriteFont = new WriteFont();
            contentWriteFont.setFontHeightInPoints((short) 11);
            contentWriteCellStyle.setWriteFont(contentWriteFont);

            // 设置列宽自适应策略
            LongestMatchColumnWidthStyleStrategy columnWidthStyleStrategy = new LongestMatchColumnWidthStyleStrategy();

            // 使用EasyExcel写入数据（带样式）
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .registerWriteHandler(new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle))
                    .registerWriteHandler(columnWidthStyleStrategy)
                    .doWrite(dataList);

            log.info("Excel导出成功（带样式）: {}, 数据量: {}", fileName, dataList.size());
        } catch (IOException e) {
            log.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
}
