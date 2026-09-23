package com.financial.sharing.service;

import com.financial.sharing.vo.param.SubjectBalanceExportParam;

import javax.servlet.http.HttpServletResponse;

/**
 * 科目余额导出服务接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface SubjectBalanceExportService {

    /**
     * 导出科目余额
     *
     * @param param 导出参数
     * @param response HTTP响应
     */
    void exportBalance(SubjectBalanceExportParam param, HttpServletResponse response);

    /**
     * 异步导出科目余额
     *
     * @param param 导出参数
     * @return 任务ID
     */
    String asyncExportBalance(SubjectBalanceExportParam param);

    /**
     * 查询导出进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    Object getExportProgress(String taskId);

    /**
     * 导出预览
     *
     * @param param 导出参数
     * @return 预览数据
     */
    Object exportPreview(SubjectBalanceExportParam param);

    /**
     * 取消导出任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean cancelExport(String taskId);
}