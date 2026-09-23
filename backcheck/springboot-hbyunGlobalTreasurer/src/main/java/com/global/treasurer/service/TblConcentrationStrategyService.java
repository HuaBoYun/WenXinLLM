package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblConcentrationStrategy;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * 归集策略Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblConcentrationStrategyService {

    /**
     * 分页查询归集策略列表
     */
    PageInfo<TblConcentrationStrategy> getStrategyPage(Integer pageNum, Integer pageSize,
                                                       String strategyName, String strategyType, String strategyStatus);

    /**
     * 根据ID查询归集策略
     */
    TblConcentrationStrategy getStrategyById(Long strategyId);

    /**
     * 保存归集策略
     */
    TblConcentrationStrategy saveStrategy(TblConcentrationStrategy strategy);

    /**
     * 更新归集策略
     */
    void updateStrategy(TblConcentrationStrategy strategy);

    /**
     * 启用归集策略
     */
    void enableStrategy(Long strategyId);

    /**
     * 停用归集策略
     */
    void disableStrategy(Long strategyId);

    /**
     * 删除归集策略
     */
    void deleteStrategy(Long strategyId);

    /**
     * 批量启用策略
     */
    void batchEnableStrategy(List<Long> strategyIds);

    /**
     * 批量停用策略
     */
    void batchDisableStrategy(List<Long> strategyIds);

    /**
     * 测试策略
     */
    String testStrategy(Long strategyId);

    /**
     * 获取所有启用的策略
     */
    List<TblConcentrationStrategy> getActiveStrategies();

    /**
     * 统计总数
     */
    long count();

    /**
     * 按状态统计数量
     */
    long countByStatus(String status);

    /**
     * 导出归集策略
     */
    void exportStrategy(String strategyName, String strategyType, String strategyStatus,
                      ServletOutputStream outputStream) throws Exception;
}

