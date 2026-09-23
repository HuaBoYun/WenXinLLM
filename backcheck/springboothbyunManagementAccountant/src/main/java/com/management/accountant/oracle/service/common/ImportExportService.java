package com.management.accountant.oracle.service.common;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 通用导入导出Service接口
 * 
 * @description 提供统一的导入导出功能
 * @author AI Assistant
 * @date 2026-02-06
 */
public interface ImportExportService {

    /**
     * 导入Excel数据
     * 
     * @param file Excel文件
     * @param importType 导入类型
     * @param companyId 公司ID
     * @param userId 用户ID
     * @return 导入结果
     */
    Map<String, Object> importExcel(MultipartFile file, String importType,
                                    String companyId, String userId);

    /**
     * 导出Excel数据
     * 
     * @param exportType 导出类型
     * @param params 导出参数
     * @param companyId 公司ID
     * @param response HTTP响应
     */
    void exportExcel(String exportType, Map<String, Object> params,
                    String companyId, HttpServletResponse response);

    /**
     * 下载导入模板
     * 
     * @param templateType 模板类型
     * @param response HTTP响应
     */
    void downloadTemplate(String templateType, HttpServletResponse response);

    /**
     * 获取导入历史记录
     * 
     * @param importType 导入类型
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 导入历史记录
     */
    Map<String, Object> getImportHistory(String importType, String companyId,
                                        Integer pageNo, Integer pageSize);

    /**
     * 验证导入数据
     * 
     * @param file Excel文件
     * @param importType 导入类型
     * @return 验证结果
     */
    Map<String, Object> validateImportData(MultipartFile file, String importType);
}

