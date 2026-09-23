package com.financial.sharing.enterpriseReport.service;

import com.financial.sharing.enterpriseReport.dto.ReportDataImportResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 报表数据导入导出Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportDataImportService {

    /**
     * 下载导入模板
     * 
     * @param response HTTP响应
     * @throws Exception 异常
     */
    void downloadTemplate(HttpServletResponse response) throws Exception;

    /**
     * 导入Excel数据
     * 
     * @param file Excel文件
     * @return 导入结果
     * @throws Exception 异常
     */
    ReportDataImportResult importExcel(MultipartFile file) throws Exception;

    /**
     * 导出数据为Excel
     * 
     * @param params 查询参数
     * @param response HTTP响应
     * @throws Exception 异常
     */
    void exportExcel(Map<String, String> params, HttpServletResponse response) throws Exception;
}

