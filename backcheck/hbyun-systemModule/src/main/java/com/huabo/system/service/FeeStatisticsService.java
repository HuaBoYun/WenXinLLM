package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.dto.FeeStatisticsQueryDTO;
import com.huabo.system.dto.FeeDrilldownQueryDTO;

/**
 * 费用统计服务
 */
public interface FeeStatisticsService {
    JsonBean queryStatistics(String token, FeeStatisticsQueryDTO dto) throws Exception;
    JsonBean drilldown(String token, FeeDrilldownQueryDTO dto) throws Exception;
}
