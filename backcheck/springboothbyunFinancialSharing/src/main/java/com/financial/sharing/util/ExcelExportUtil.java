package com.financial.sharing.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具类
 * 基于EasyExcel实现，支持大数据量导出
 *
 * @author system
 * @since 2024-12-19
 */
public class ExcelExportUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);

    /**
     * 默认导出
     *
     * @param response HTTP响应
     * @param data 数据列表
     * @param clazz 实体类
     * @param fileName 文件名
     * @param sheetName 工作表名
     */
    public static <T> void export(HttpServletResponse response, List<T> data, Class<T> clazz,
                                  String fileName, String sheetName) {
        try {
            // 设置响应头
            setResponseHeader(response, fileName);

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(data);

        } catch (IOException e) {
            logger.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 带样式的导出
     *
     * @param response HTTP响应
     * @param data 数据列表
     * @param clazz 实体类
     * @param fileName 文件名
     * @param sheetName 工作表名
     * @param styleStrategy 样式策略
     */
    public static <T> void exportWithStyle(HttpServletResponse response, List<T> data, Class<T> clazz,
                                           String fileName, String sheetName,
                                           HorizontalCellStyleStrategy styleStrategy) {
        try {
            // 设置响应头
            setResponseHeader(response, fileName);

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(styleStrategy)
                    .sheet(sheetName)
                    .doWrite(data);

        } catch (IOException e) {
            logger.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 多Sheet导出
     *
     * @param response HTTP响应
     * @param fileName 文件名
     * @param sheets Sheet数据列表
     */
    public static void exportMultipleSheets(HttpServletResponse response, String fileName,
                                           List<SheetData<?>> sheets) {
        ExcelWriter excelWriter = null;
        try {
            // 设置响应头
            setResponseHeader(response, fileName);

            // 创建ExcelWriter
            excelWriter = EasyExcel.write(response.getOutputStream()).build();

            // 写入多个Sheet
            for (int i = 0; i < sheets.size(); i++) {
                SheetData<?> sheetData = sheets.get(i);
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetData.getSheetName())
                        .head(sheetData.getHeadClass())
                        .build();

                excelWriter.write(sheetData.getData(), writeSheet);
            }

        } catch (IOException e) {
            logger.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 动态表头导出（用于Map数据）
     *
     * @param response HTTP响应
     * @param data 数据列表
     * @param headers 表头
     * @param fileName 文件名
     * @param sheetName 工作表名
     */
    public static void exportDynamicHeaders(HttpServletResponse response, List<Map<String, Object>> data,
                                           List<String> headers, String fileName, String sheetName) {
        try {
            // 设置响应头
            setResponseHeader(response, fileName);

            // 构建动态表头
            List<List<String>> headList = new ArrayList<>();
            for (String header : headers) {
                headList.add(Arrays.asList(header));
            }

            // 转换数据为List<List<Object>>
            List<List<Object>> dataList = new ArrayList<>();
            for (Map<String, Object> row : data) {
                List<Object> rowData = new ArrayList<>();
                for (String key : headers) {
                    rowData.add(row.get(key));
                }
                dataList.add(rowData);
            }

            // 写入Excel
            EasyExcel.write(response.getOutputStream())
                    .head(headList)
                    .sheet(sheetName)
                    .doWrite(dataList);

        } catch (IOException e) {
            logger.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 分批导出（用于大数据量）
     *
     * @param response HTTP响应
     * @param dataProvider 数据提供者
     * @param clazz 实体类
     * @param fileName 文件名
     * @param sheetName 工作表名
     * @param batchSize 每批数据量
     */
    public static <T> void exportBatch(HttpServletResponse response, DataProvider<T> dataProvider,
                                       Class<T> clazz, String fileName, String sheetName, int batchSize) {
        ExcelWriter excelWriter = null;
        try {
            // 设置响应头
            setResponseHeader(response, fileName);

            // 创建ExcelWriter
            excelWriter = EasyExcel.write(response.getOutputStream(), clazz).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();

            // 分批写入数据
            int pageNo = 0;
            boolean hasMore = true;
            while (hasMore) {
                List<T> batchData = dataProvider.getData(pageNo, batchSize);
                if (batchData == null || batchData.isEmpty()) {
                    hasMore = false;
                } else {
                    excelWriter.write(batchData, writeSheet);
                    pageNo++;
                }
            }

        } catch (IOException e) {
            logger.error("Excel导出失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 创建默认样式策略
     *
     * @return 样式策略
     */
    public static HorizontalCellStyleStrategy createDefaultStyleStrategy() {
        // 头部样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // 内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 设置响应头
     *
     * @param response HTTP响应
     * @param fileName 文件名
     * @throws IOException
     */
    private static void setResponseHeader(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
    }

    /**
     * 数据提供者接口
     *
     * @param <T> 数据类型
     */
    public interface DataProvider<T> {
        /**
         * 获取数据
         *
         * @param pageNo 页码
         * @param pageSize 页面大小
         * @return 数据列表
         */
        List<T> getData(int pageNo, int pageSize);
    }

    /**
     * Sheet数据封装类
     *
     * @param <T> 数据类型
     */
    public static class SheetData<T> {
        private List<T> data;
        private Class<T> headClass;
        private String sheetName;

        public SheetData(List<T> data, Class<T> headClass, String sheetName) {
            this.data = data;
            this.headClass = headClass;
            this.sheetName = sheetName;
        }

        public List<T> getData() { return data; }
        public void setData(List<T> data) { this.data = data; }
        public Class<T> getHeadClass() { return headClass; }
        public void setHeadClass(Class<T> headClass) { this.headClass = headClass; }
        public String getSheetName() { return sheetName; }
        public void setSheetName(String sheetName) { this.sheetName = sheetName; }
    }
}