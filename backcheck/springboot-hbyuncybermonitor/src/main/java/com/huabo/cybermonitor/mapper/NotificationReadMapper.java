package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.NotificationRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通知阅读记录 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface NotificationReadMapper extends BaseMapper<NotificationRead> {

    /**
     * 根据通知ID查询阅读记录列表
     *
     * @param notificationId 通知ID
     * @return 阅读记录列表
     */
    List<NotificationRead> selectReadsByNotificationId(@Param("notificationId") String notificationId);

    /**
     * 根据用户ID查询阅读记录列表
     *
     * @param userId 用户ID
     * @return 阅读记录列表
     */
    List<NotificationRead> selectReadsByUserId(@Param("userId") String userId);

    /**
     * 根据阅读状态查询阅读记录列表
     *
     * @param readStatus 阅读状态
     * @return 阅读记录列表
     */
    List<NotificationRead> selectReadsByStatus(@Param("readStatus") String readStatus);

    /**
     * 查询用户对特定通知的阅读记录
     *
     * @param notificationId 通知ID
     * @param userId         用户ID
     * @return 阅读记录
     */
    NotificationRead selectUserNotificationRead(@Param("notificationId") String notificationId, @Param("userId") String userId);

    /**
     * 更新阅读状态
     *
     * @param readId     阅读记录ID
     * @param readStatus 阅读状态
     * @return 影响行数
     */
    int updateReadStatus(@Param("readId") String readId, @Param("readStatus") String readStatus);

    /**
     * 批量更新阅读状态
     *
     * @param readIds    阅读记录ID列表
     * @param readStatus 阅读状态
     * @return 影响行数
     */
    int batchUpdateReadStatus(@Param("readIds") List<String> readIds, @Param("readStatus") String readStatus);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param userId         用户ID
     * @param readIp         阅读IP
     * @param userAgent      用户代理
     * @param readDevice     阅读设备
     * @return 影响行数
     */
    int markNotificationAsRead(@Param("notificationId") String notificationId,
                               @Param("userId") String userId,
                               @Param("readIp") String readIp,
                               @Param("userAgent") String userAgent,
                               @Param("readDevice") String readDevice);

    /**
     * 批量标记通知为已读
     *
     * @param notificationIds 通知ID列表
     * @param userId          用户ID
     * @param readIp          阅读IP
     * @param userAgent       用户代理
     * @param readDevice      阅读设备
     * @return 影响行数
     */
    int batchMarkNotificationsAsRead(@Param("notificationIds") List<String> notificationIds,
                                     @Param("userId") String userId,
                                     @Param("readIp") String readIp,
                                     @Param("userAgent") String userAgent,
                                     @Param("readDevice") String readDevice);

    /**
     * 查询用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    Integer selectUnreadCountByUserId(@Param("userId") String userId);

    /**
     * 查询用户未读通知列表
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 未读通知列表
     */
    List<Map<String, Object>> selectUnreadNotificationsByUserId(@Param("userId") String userId, @Param("limit") Integer limit);

    /**
     * 查询通知阅读统计
     *
     * @param notificationId 通知ID
     * @return 阅读统计
     */
    Map<String, Object> selectNotificationReadStatistics(@Param("notificationId") String notificationId);

    /**
     * 查询用户阅读统计
     *
     * @param userId 用户ID
     * @return 用户阅读统计
     */
    Map<String, Object> selectUserReadStatistics(@Param("userId") String userId);

    /**
     * 查询阅读趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 阅读趋势统计
     */
    List<Map<String, Object>> selectReadTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询活跃用户统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 活跃用户统计
     */
    List<Map<String, Object>> selectActiveUserStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 清理过期的阅读记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer deleteExpiredReads(@Param("days") Integer days);

    /**
     * 查询设备分布统计
     *
     * @return 设备分布统计
     */
    List<Map<String, Object>> selectDeviceDistribution();

    /**
     * 查询阅读时间分布统计
     *
     * @return 阅读时间分布统计
     */
    List<Map<String, Object>> selectReadTimeDistribution();
}
