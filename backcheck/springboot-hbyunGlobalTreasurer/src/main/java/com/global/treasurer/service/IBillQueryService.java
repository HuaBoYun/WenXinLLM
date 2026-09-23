package com.global.treasurer.service;

import java.util.List;
import java.util.Map;

/**
 * 票据查询Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface IBillQueryService {

    /**
     * 票据台账查询
     *
     * @param params 查询参数
     * @return 台账数据
     */
    Map<String, Object> getInstrumentLedger(Map<String, Object> params);

    /**
     * 票据统计分析
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getInstrumentStatistics(Map<String, Object> params);

    /**
     * 票据到期提醒
     *
     * @param params 查询参数
     * @return 到期提醒列表
     */
    List<Map<String, Object>> getMaturityAlerts(Map<String, Object> params);

    /**
     * 票据到期日历
     *
     * @param params 查询参数
     * @return 日历数据
     */
    List<Map<String, Object>> getMaturityCalendar(Map<String, Object> params);

    /**
     * 票据报表查询
     *
     * @param params 查询参数
     * @return 报表数据
     */
    Map<String, Object> getInstrumentReport(Map<String, Object> params);

    /**
     * 票据详情查询
     *
     * @param instrumentId 票据ID
     * @return 票据详情
     */
    Map<String, Object> getInstrumentDetail(Long instrumentId);

    /**
     * 票据流转历史查询
     *
     * @param instrumentId 票据ID
     * @return 流转历史
     */
    List<Map<String, Object>> getInstrumentHistory(Long instrumentId);
}

