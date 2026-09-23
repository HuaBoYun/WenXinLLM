package com.management.accountant.service;

import java.util.Map;

/**
 * 管理会计智慧首页 - 业务层接口
 *
 * @author system
 * @date 2025-01-21
 */
public interface MaSmartHomeService {

    /**
     * 获取管理会计首页汇总数据
     *
     * @return 包含 welcomeStats / kpiCards / pendingCounts 的Map
     */
    Map<String, Object> getSmartHomeData();
}
