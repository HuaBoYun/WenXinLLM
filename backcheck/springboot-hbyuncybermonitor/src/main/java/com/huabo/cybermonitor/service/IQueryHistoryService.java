package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.QueryHistory;
import com.huabo.cybermonitor.vo.QueryHistoryQueryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 查询历史记录服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IQueryHistoryService extends IService<QueryHistory> {

    /**
     * 分页查询查询历史记录列表
     *
     * @param queryVO 查询条件
     * @return 查询历史记录列表
     */
    IPage<QueryHistory> getHistoryList(QueryHistoryQueryVO queryVO);

    /**
     * 获取查询历史记录详情
     *
     * @param historyId 历史记录ID
     * @return 查询历史记录详情
     */
    QueryHistory getHistoryDetail(String historyId);

    /**
     * 记录查询历史
     *
     * @param queryName       查询名称
     * @param queryType       查询类型
     * @param queryConditions 查询条件
     * @param querySql        查询SQL
     * @param resultCount     结果数量
     * @param executionTime   执行时间
     * @param queryStatus     查询状态
     * @param errorMessage    错误信息
     * @param queryBy         查询人
     * @param ipAddress       IP地址
     * @param userAgent       用户代理
     * @return 是否成功
     */
    boolean recordQueryHistory(String queryName, String queryType, String queryConditions,
                               String querySql, Integer resultCount, Long executionTime,
                               String queryStatus, String errorMessage, String queryBy,
                               String ipAddress, String userAgent);

    /**
     * 删除查询历史记录
     *
     * @param historyId 历史记录ID
     * @return 是否成功
     */
    boolean deleteHistory(String historyId);

    /**
     * 批量删除查询历史记录
     *
     * @param historyIds 历史记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteHistory(List<String> historyIds);

    /**
     * 根据查询类型查询历史记录列表
     *
     * @param queryType 查询类型
     * @return 查询历史记录列表
     */
    List<QueryHistory> getHistoriesByType(String queryType);

    /**
     * 根据查询人查询历史记录列表
     *
     * @param queryBy 查询人
     * @return 查询历史记录列表
     */
    List<QueryHistory> getHistoriesByQueryBy(String queryBy);

    /**
     * 根据查询状态查询历史记录列表
     *
     * @param queryStatus 查询状态
     * @return 查询历史记录列表
     */
    List<QueryHistory> getHistoriesByStatus(String queryStatus);

    /**
     * 查询慢查询记录
     *
     * @param threshold 执行时间阈值（毫秒）
     * @return 查询历史记录列表
     */
    List<QueryHistory> getSlowQueries(Long threshold);

    /**
     * 查询失败的查询记录
     *
     * @return 查询历史记录列表
     */
    List<QueryHistory> getFailedQueries();

    /**
     * 获取查询统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getQueryStatistics();

    /**
     * 获取查询类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getQueryTypeDistribution();

    /**
     * 获取查询状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getQueryStatusDistribution();

    /**
     * 获取查询性能统计
     *
     * @return 性能统计
     */
    Map<String, Object> getQueryPerformanceStatistics();

    /**
     * 根据时间范围获取查询趋势
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 查询趋势
     */
    List<Map<String, Object>> getQueryTrend(String startDate, String endDate);

    /**
     * 清理过期的查询历史记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer cleanExpiredHistories(Integer days);

    /**
     * 导出查询历史记录列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportHistoryList(QueryHistoryQueryVO queryVO, HttpServletResponse response);

    /**
     * 获取查询类型标签
     *
     * @param queryType 查询类型
     * @return 类型标签
     */
    String getQueryTypeLabel(String queryType);

    /**
     * 获取查询状态标签
     *
     * @param queryStatus 查询状态
     * @return 状态标签
     */
    String getQueryStatusLabel(String queryStatus);
}
