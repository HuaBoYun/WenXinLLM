package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.TblAssetQuality;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 资产质量管理服务接口
 */
public interface AssetQualityService {

    /**
     * 分页查询资产质量列表
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<TblAssetQuality> getList(Map<String, Object> params);

    /**
     * 获取统计数据（评估资产数、优质资产数、风险资产数、平均质量评分）
     * @return 统计数据
     */
    Map<String, Object> getStatistics();

    /**
     * 获取图表数据（质量分布、趋势等）
     * @return 图表数据
     */
    Map<String, Object> getChartData();

    /**
     * 获取图表数据（带参数）
     * @param params 查询参数
     * @return 图表数据
     */
    Map<String, Object> getChartData(Map<String, Object> params);

    /**
     * 新增资产质量记录
     * @param entity 资产质量实体
     * @return 新增后的实体
     */
    TblAssetQuality add(TblAssetQuality entity);

    /**
     * 更新资产质量记录
     * @param entity 资产质量实体
     * @return 更新后的实体
     */
    TblAssetQuality update(TblAssetQuality entity);

    /**
     * 删除资产质量记录
     * @param assetQualityId 资产质量ID
     * @return 是否成功
     */
    boolean delete(String assetQualityId);

    /**
     * 批量评估
     * @param ids 资产质量ID列表
     * @return 是否成功
     */
    boolean batchAssess(List<String> ids);

    /**
     * 批量监控
     * @param ids 资产质量ID列表
     * @return 是否成功
     */
    boolean batchMonitor(List<String> ids);

    /**
     * 导出数据
     * @param params 查询参数
     * @return 数据列表
     */
    List<TblAssetQuality> exportData(Map<String, Object> params);

    /**
     * 导出数据（别名方法）
     * @param params 查询参数
     * @return 数据列表
     */
    List<TblAssetQuality> export(Map<String, Object> params);

    /**
     * 生成质量报告
     * @param params 报告参数
     * @return 报告数据
     */
    Map<String, Object> generateReport(Map<String, Object> params);
}
