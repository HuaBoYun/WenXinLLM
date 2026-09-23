package com.global.treasurer.service;

import com.hbfk.entity.TblStaffUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 票据导出导入Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface IBillExportService {

    /**
     * 导出票据台账
     *
     * @param params   查询参数
     * @param response HTTP响应
     */
    void exportBillLedger(Map<String, Object> params, HttpServletResponse response);

    /**
     * 导出票据统计报表
     *
     * @param params   查询参数
     * @param response HTTP响应
     */
    void exportBillStatistics(Map<String, Object> params, HttpServletResponse response);

    /**
     * 获取票据历史记录
     *
     * @param billId 票据ID
     * @return 历史记录列表
     */
    List<Map<String, Object>> getBillHistory(Long billId);

    /**
     * 批量导入票据
     *
     * @param file      Excel文件
     * @param loginStaff 当前登录用户
     * @return 导入结果
     */
    Map<String, Object> batchImportBills(MultipartFile file, TblStaffUtil loginStaff);

    /**
     * 下载票据导入模板
     *
     * @param response HTTP响应
     */
    void downloadImportTemplate(HttpServletResponse response);

    /**
     * 导出票据数据
     *
     * @param params   查询参数
     * @param response HTTP响应
     */
    void exportBillData(Map<String, Object> params, HttpServletResponse response);
}

