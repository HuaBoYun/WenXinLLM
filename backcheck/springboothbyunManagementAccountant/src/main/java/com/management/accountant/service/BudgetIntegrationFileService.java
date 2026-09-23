package com.management.accountant.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 预算文件集成Service接口
 * 
 * @description 预算文件集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIntegrationFileService {

    /**
     * 文件上传
     * 
     * @param file 上传文件
     * @param fileType 文件类型
     * @return 上传结果
     */
    Map<String, Object> uploadFile(MultipartFile file, String fileType);

    /**
     * 文件下载
     * 
     * @param params 下载参数
     * @return 下载结果
     */
    Map<String, Object> downloadFile(Map<String, Object> params);

    /**
     * 文件解析
     * 
     * @param params 解析参数
     * @return 解析结果
     */
    Map<String, Object> parseFile(Map<String, Object> params);

    /**
     * 文件转换
     * 
     * @param params 转换参数
     * @return 转换结果
     */
    Map<String, Object> convertFile(Map<String, Object> params);

    /**
     * 批量导入
     * 
     * @param params 导入参数
     * @return 导入结果
     */
    Map<String, Object> batchImport(Map<String, Object> params);

    /**
     * 批量导出
     * 
     * @param params 导出参数
     * @return 导出结果
     */
    Map<String, Object> batchExport(Map<String, Object> params);

    /**
     * 文件模板管理
     * 
     * @param params 模板参数
     * @return 模板信息
     */
    Map<String, Object> manageTemplate(Map<String, Object> params);
}

