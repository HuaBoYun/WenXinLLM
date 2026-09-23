package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.QueryHistory;
import com.huabo.cybermonitor.vo.QueryHistoryQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 查询历史记录 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface QueryHistoryMapper extends BaseMapper<QueryHistory> {

    /**
     * 分页查询查询历史记录列表
     *
     * @param page    分页参数
     * @param queryVO 查询条件
     * @return 查询历史记录列表
     */
    IPage<QueryHistory> selectHistoryList(Page<QueryHistory> page, @Param("queryVO") QueryHistoryQueryVO queryVO);

    /**
     * 根据查询类型查询历史记录列表
     *
     * @param queryType 查询类型
     * @return 查询历史记录列表
     */
    List<QueryHistory> selectHistoriesByType(@Param("queryType") String queryType);

    /**
     * 根据查询人查询历史记录列表
     *
     * @param queryBy 查询人
     * @return 查询历史记录列表
     */
    List<QueryHistory> selectHistoriesByQueryBy(@Param("queryBy") String queryBy);

    /**
     * 根据查询状态查询历史记录列表
     *
     * @param queryStatus 查询状态
     * @return 查询历史记录列表
     */
    List<QueryHistory> selectHistoriesByStatus(@Param("queryStatus") String queryStatus);

    /**
     * 查询慢查询记录
     *
     * @param threshold 执行时间阈值（毫秒）
     * @return 查询历史记录列表
     */
    List<QueryHistory> selectSlowQueries(@Param("threshold") Long threshold);

    /**
     * 查询失败的查询记录
     *
     * @return 查询历史记录列表
     */
    List<QueryHistory> selectFailedQueries();

    /**
     * 获取查询统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> selectQueryStatistics();

    /**
     * 获取查询类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectQueryTypeDistribution();

    /**
     * 获取查询状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> selectQueryStatusDistribution();

    /**
     * 获取查询性能统计
     *
     * @return 性能统计
     */
    Map<String, Object> selectQueryPerformanceStatistics();

    /**
     * 根据时间范围获取查询趋势
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 查询趋势
     */
    List<Map<String, Object>> selectQueryTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 清理过期的查询历史记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    int deleteExpiredHistories(@Param("days") Integer days);

    /**
     * 导出查询历史记录列表
     *
     * @param queryVO 查询条件
     * @return 查询历史记录列表
     */
    List<QueryHistory> selectHistoryListForExport(@Param("queryVO") QueryHistoryQueryVO queryVO);
}
