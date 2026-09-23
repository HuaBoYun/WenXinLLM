package com.global.treasurer.service;

import com.hbfk.entity.TblStaffUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 票据报表Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface IBillReportService {

    /**
     * 生成票据报告
     *
     * @param params     报告参数
     * @param loginStaff 当前登录用户
     * @return 报告信息
     */
    Map<String, Object> generateBillReport(Map<String, Object> params, TblStaffUtil loginStaff);

    /**
     * 获取票据报告列表
     *
     * @param params 查询参数
     * @return 报告列表
     */
    Map<String, Object> getBillReports(Map<String, Object> params);

    /**
     * 下载票据报告
     *
     * @param reportId 报告ID
     * @param response HTTP响应
     */
    void downloadBillReport(Long reportId, HttpServletResponse response);

    /**
     * 获取报告详情
     *
     * @param reportId 报告ID
     * @return 报告详情
     */
    Map<String, Object> getBillReportDetail(Long reportId);

    /**
     * 删除票据报告
     *
     * @param reportIds 报告ID数组
     * @return 是否成功
     */
    boolean deleteBillReports(Long[] reportIds);
}

