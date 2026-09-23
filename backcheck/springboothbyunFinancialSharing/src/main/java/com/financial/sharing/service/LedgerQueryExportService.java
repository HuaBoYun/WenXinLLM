package com.financial.sharing.service;

import com.financial.sharing.vo.param.LedgerQueryExportParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 总账查询导出服务接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface LedgerQueryExportService {

    /**
     * 导出总账查询数据
     *
     * @param param 导出参数
     * @param response HTTP响应
     */
    void exportLedgerQuery(LedgerQueryExportParam param, HttpServletResponse response);

    /**
     * 异步导出总账查询数据
     *
     * @param param 导出参数
     * @return 任务ID
     */
    String asyncExportLedgerQuery(LedgerQueryExportParam param);

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
    Object exportPreview(LedgerQueryExportParam param);

    /**
     * 取消导出任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean cancelExport(String taskId);

    /**
     * 获取导出模板列表
     *
     * @return 模板列表
     */
    Map<String, Object> getExportTemplates();
}