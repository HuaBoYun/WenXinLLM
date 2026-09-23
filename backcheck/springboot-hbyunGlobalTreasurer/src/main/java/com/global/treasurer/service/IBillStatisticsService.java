package com.global.treasurer.service;

import java.util.Map;

/**
 * 票据统计Service接口
 */
public interface IBillStatisticsService {
    Map<String, Object> getStatisticsOverview(Map<String, Object> params);
    Map<String, Object> getTypeStatistics(Map<String, Object> params);
    Map<String, Object> getTrendAnalysis(Map<String, Object> params);
}
