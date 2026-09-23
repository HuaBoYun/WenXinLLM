package com.management.accountant.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导入导出工具类
 * 
 * @description 基于EasyExcel实现的Excel工具类
 * @author AI Assistant
 * @date 2026-02-09
 */
@Slf4j
public class ExcelUtil {

    /**
     * 导出Excel
     * 
     * @param data 数据列表
     * @param clazz 数据类型
     * @param fileName 文件名
     * @param response HTTP响应
     */
    public static <T> void exportExcel(List<T> data, Class<T> clazz, String fileName, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            
            // 防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            
            // 写入Excel
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet("数据")
                    .doWrite(data);
                    
            log.info("导出Excel成功，文件名: {}, 数据量: {}", fileName, data.size());
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("导出Excel失败：" + e.getMessage());
        }
    }

    /**
     * 导入Excel - 读取为Map列表
     *
     * @param file 上传的文件
     * @return Map列表
     */
    public static List<Map<String, Object>> importExcelToMap(MultipartFile file) {
        try {
            List<Object> data = EasyExcel.read(file.getInputStream())
                    .sheet()
                    .doReadSync();

            log.info("导入Excel成功(Map)，数据量: {}", data.size());

            // 转换为 List<Map<String, Object>> 格式
            List<Map<String, Object>> result = new ArrayList<>();
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i) instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> rowData = (Map<String, Object>) data.get(i);
                        result.add(rowData);
                    }
                }
            }

            return result;
        } catch (IOException e) {
            log.error("导入Excel失败", e);
            throw new RuntimeException("导入Excel失败：" + e.getMessage());
        }
    }

    /**
     * 导入Excel
     *
     * @param file 上传的文件
     * @param clazz 数据类型
     * @return 数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) {
        try {
            List<T> data = EasyExcel.read(file.getInputStream())
                    .head(clazz)
                    .sheet()
                    .doReadSync();
                    
            log.info("导入Excel成功，数据量: {}", data.size());
            return data;
        } catch (IOException e) {
            log.error("导入Excel失败", e);
            throw new RuntimeException("导入Excel失败：" + e.getMessage());
        }
    }

    /**
     * 下载Excel模板
     * 
     * @param clazz 数据类型
     * @param fileName 文件名
     * @param response HTTP响应
     */
    public static void downloadTemplate(Class<?> clazz, String fileName, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            
            // 防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + "_模板.xlsx");
            
            // 写入空模板
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet("模板")
                    .doWrite(new ArrayList<>());
                    
            log.info("下载模板成功，文件名: {}", fileName);
        } catch (IOException e) {
            log.error("下载模板失败", e);
            throw new RuntimeException("下载模板失败：" + e.getMessage());
        }
    }

    /**
     * 导出多个Sheet的Excel
     * 
     * @param dataMap Sheet名称和数据的映射
     * @param clazz 数据类型
     * @param fileName 文件名
     * @param response HTTP响应
     */
    public static <T> void exportMultiSheetExcel(java.util.Map<String, List<T>> dataMap, Class<T> clazz, 
                                                   String fileName, HttpServletResponse response) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            
            // 防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            
            // 创建ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz).build();
            
            // 写入多个Sheet
            int sheetNo = 0;
            for (java.util.Map.Entry<String, List<T>> entry : dataMap.entrySet()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo++, entry.getKey()).build();
                excelWriter.write(entry.getValue(), writeSheet);
            }
            
            // 关闭流
            excelWriter.finish();
            
            log.info("导出多Sheet Excel成功，文件名: {}, Sheet数量: {}", fileName, dataMap.size());
        } catch (IOException e) {
            log.error("导出多Sheet Excel失败", e);
            throw new RuntimeException("导出多Sheet Excel失败：" + e.getMessage());
        }
    }

    /**
     * 验证Excel文件
     * 
     * @param file 上传的文件
     * @return true-有效，false-无效
     */
    public static boolean validateExcelFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        
        // 检查文件扩展名
        return fileName.endsWith(".xlsx") || fileName.endsWith(".xls");
    }

    /**
     * 获取Excel文件的行数
     * 
     * @param file 上传的文件
     * @return 行数
     */
    public static int getExcelRowCount(MultipartFile file) {
        try {
            List<Object> data = EasyExcel.read(file.getInputStream())
                    .sheet()
                    .doReadSync();
            return data.size();
        } catch (IOException e) {
            log.error("获取Excel行数失败", e);
            return 0;
        }
    }
}

