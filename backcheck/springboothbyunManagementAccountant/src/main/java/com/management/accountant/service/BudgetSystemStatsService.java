package com.management.accountant.service;

import com.management.accountant.vo.result.BudgetSystemActivityVO;
import com.management.accountant.vo.result.BudgetSystemHealthVO;
import com.management.accountant.vo.result.BudgetSystemModuleVO;
import com.management.accountant.vo.result.BudgetSystemStatsVO;
import com.management.accountant.vo.result.BudgetSystemMatrixVO;

import java.util.List;

/**
 * 预算体系统计Service接口
 * 
 * @description 预算体系管理统计数据服务
 * @author AI Assistant
 * @date 2025-01-30
 */
public interface BudgetSystemStatsService {

    /**
     * 获取预算体系统计数据
     * 
     * @return 统计数据
     */
    BudgetSystemStatsVO getSystemStats();

    /**
     * 获取预算体系模块列表
     * 
     * @return 模块列表
     */
    List<BudgetSystemModuleVO> getSystemModules();

    /**
     * 获取预算体系健康度
     * 
     * @return 健康度数据
     */
    BudgetSystemHealthVO getSystemHealth();

    /**
     * 获取最近活动记录
     * 
     * @param limit 限制数量
     * @return 活动列表
     */
    List<BudgetSystemActivityVO> getRecentActivities(Integer limit);

    /**
     * 获取预算体系矩阵数据
     *
     * @return 矩阵数据（组织、维度、指标及其关联关系）
     */
    BudgetSystemMatrixVO getSystemMatrix();
}

