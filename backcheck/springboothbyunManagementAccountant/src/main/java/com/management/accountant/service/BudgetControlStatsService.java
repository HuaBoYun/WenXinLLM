package com.management.accountant.service;

import com.management.accountant.vo.result.BudgetControlHealthVO;
import com.management.accountant.vo.result.BudgetControlStatsVO;

/**
 * 预算控制统计Service接口
 * 
 * @description 预算控制统计数据服务
 * @author AI Assistant
 * @date 2025-01-30
 */
public interface BudgetControlStatsService {

    /**
     * 获取预算控制统计数据
     * 
     * @return 统计数据
     */
    BudgetControlStatsVO getControlStats();

    /**
     * 获取预算控制健康度
     * 
     * @return 健康度数据
     */
    BudgetControlHealthVO getControlHealth();
}

