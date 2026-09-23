package com.financial.sharing.service;

import com.hbfk.util.JsonBean;
import com.financial.sharing.dto.VisualizationQueryParam;

import javax.servlet.http.HttpServletResponse;

/**
 * 报表可视化服务接口
 */
public interface ReportVisualizationService {

    /**
     * 获取可视化综合数据
     *
     * @param param 查询参数
     * @return 可视化数据
     */
    JsonBean getVisualizationData(VisualizationQueryParam param);

    /**
     * 导出可视化报告
     *
     * @param param 查询参数
     * @param exportType 导出类型(pdf/excel)
     * @param response HTTP响应
     * @return 操作结果
     */
    JsonBean exportVisualizationReport(VisualizationQueryParam param, String exportType, HttpServletResponse response);
}
