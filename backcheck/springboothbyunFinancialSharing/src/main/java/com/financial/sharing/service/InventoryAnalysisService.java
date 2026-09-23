package com.financial.sharing.service;

import java.util.List;
import java.util.Map;

/**
 * 存货分析服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface InventoryAnalysisService {

    /**
     * 获取存货概览分析
     * 
     * @param param 查询参数
     * @return 概览数据
     */
    Map<String, Object> getOverviewAnalysis(Map<String, Object> param);

    /**
     * 获取存货结构分析
     * 
     * @param param 查询参数
     * @return 结构分析数据
     */
    Map<String, Object> getStructureAnalysis(Map<String, Object> param);

    /**
     * 获取周转率分析
     * 
     * @param param 查询参数
     * @return 周转率分析数据
     */
    Map<String, Object> getTurnoverAnalysis(Map<String, Object> param);

    /**
     * 获取ABC分类分析
     * 
     * @param param 查询参数
     * @return ABC分类数据
     */
    Map<String, Object> getAbcAnalysis(Map<String, Object> param);

    /**
     * 获取呆滞库存分析
     * 
     * @param param 查询参数
     * @return 呆滞库存数据
     */
    Map<String, Object> getStagnantAnalysis(Map<String, Object> param);

    /**
     * 获取分类存货排行
     * 
     * @param param 查询参数
     * @return 排行数据
     */
    List<Map<String, Object>> getCategoryRanking(Map<String, Object> param);

    /**
     * 获取周转率排行
     * 
     * @param param 查询参数
     * @return 排行数据
     */
    List<Map<String, Object>> getTurnoverRanking(Map<String, Object> param);

    /**
     * 获取呆滞库存预警
     * 
     * @param param 查询参数
     * @return 预警数据
     */
    List<Map<String, Object>> getStagnantAlert(Map<String, Object> param);

    /**
     * 生成存货报表
     * 
     * @param param 报表参数
     * @return 报表文件路径
     */
    String generateReport(Map<String, Object> param);

    /**
     * 获取存货结构报表
     * 
     * @param param 查询参数
     * @return 报表数据
     */
    List<Map<String, Object>> getStructureReport(Map<String, Object> param);

    /**
     * 获取周转率报表
     * 
     * @param param 查询参数
     * @return 报表数据
     */
    List<Map<String, Object>> getTurnoverReport(Map<String, Object> param);

    /**
     * 导出存货报表
     * 
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportReport(Map<String, Object> param);
}

