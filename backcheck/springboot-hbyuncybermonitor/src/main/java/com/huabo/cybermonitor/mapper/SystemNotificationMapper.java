package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.SystemNotification;
import com.huabo.cybermonitor.vo.SystemNotificationQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统通知公告 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface SystemNotificationMapper extends BaseMapper<SystemNotification> {

    /**
     * 分页查询系统通知列表
     *
     * @param page    分页对象
     * @param queryVO 查询条件
     * @return 系统通知列表
     */
    IPage<SystemNotification> selectNotificationList(Page<SystemNotification> page, @Param("queryVO") SystemNotificationQueryVO queryVO);

    /**
     * 根据通知类型查询通知列表
     *
     * @param notificationType 通知类型
     * @return 系统通知列表
     */
    List<SystemNotification> selectNotificationsByType(@Param("notificationType") String notificationType);

    /**
     * 根据通知级别查询通知列表
     *
     * @param notificationLevel 通知级别
     * @return 系统通知列表
     */
    List<SystemNotification> selectNotificationsByLevel(@Param("notificationLevel") String notificationLevel);

    /**
     * 根据通知状态查询通知列表
     *
     * @param notificationStatus 通知状态
     * @return 系统通知列表
     */
    List<SystemNotification> selectNotificationsByStatus(@Param("notificationStatus") String notificationStatus);

    /**
     * 查询已发布的通知列表
     *
     * @return 系统通知列表
     */
    List<SystemNotification> selectPublishedNotifications();

    /**
     * 查询置顶的通知列表
     *
     * @return 系统通知列表
     */
    List<SystemNotification> selectTopNotifications();

    /**
     * 查询弹窗通知列表
     *
     * @return 系统通知列表
     */
    List<SystemNotification> selectPopupNotifications();

    /**
     * 查询用户相关的通知列表
     *
     * @param userId 用户ID
     * @return 系统通知列表
     */
    List<SystemNotification> selectUserNotifications(@Param("userId") String userId);

    /**
     * 查询角色相关的通知列表
     *
     * @param roleId 角色ID
     * @return 系统通知列表
     */
    List<SystemNotification> selectRoleNotifications(@Param("roleId") String roleId);

    /**
     * 更新通知状态
     *
     * @param notificationId     通知ID
     * @param notificationStatus 通知状态
     * @return 影响行数
     */
    int updateNotificationStatus(@Param("notificationId") String notificationId, @Param("notificationStatus") String notificationStatus);

    /**
     * 批量更新通知状态
     *
     * @param notificationIds    通知ID列表
     * @param notificationStatus 通知状态
     * @return 影响行数
     */
    int batchUpdateNotificationStatus(@Param("notificationIds") List<String> notificationIds, @Param("notificationStatus") String notificationStatus);

    /**
     * 更新通知置顶状态
     *
     * @param notificationId 通知ID
     * @param isTop          是否置顶
     * @return 影响行数
     */
    int updateNotificationTopStatus(@Param("notificationId") String notificationId, @Param("isTop") Boolean isTop);

    /**
     * 批量更新通知置顶状态
     *
     * @param notificationIds 通知ID列表
     * @param isTop           是否置顶
     * @return 影响行数
     */
    int batchUpdateNotificationTopStatus(@Param("notificationIds") List<String> notificationIds, @Param("isTop") Boolean isTop);

    /**
     * 增加通知阅读次数
     *
     * @param notificationId 通知ID
     * @return 影响行数
     */
    int incrementReadCount(@Param("notificationId") String notificationId);

    /**
     * 查询通知统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> selectNotificationStatistics();

    /**
     * 查询通知类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectNotificationTypeDistribution();

    /**
     * 查询通知级别分布统计
     *
     * @return 级别分布统计
     */
    List<Map<String, Object>> selectNotificationLevelDistribution();

    /**
     * 查询通知状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> selectNotificationStatusDistribution();

    /**
     * 查询通知发布趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 发布趋势统计
     */
    List<Map<String, Object>> selectNotificationPublishTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询通知阅读统计
     *
     * @return 阅读统计
     */
    List<Map<String, Object>> selectNotificationReadStatistics();

    /**
     * 查询热门通知列表
     *
     * @param limit 限制数量
     * @return 热门通知列表
     */
    List<SystemNotification> selectPopularNotifications(@Param("limit") Integer limit);

    /**
     * 查询最新通知列表
     *
     * @param limit 限制数量
     * @return 最新通知列表
     */
    List<SystemNotification> selectLatestNotifications(@Param("limit") Integer limit);

    /**
     * 查询过期的通知列表
     *
     * @return 过期通知列表
     */
    List<SystemNotification> selectExpiredNotifications();

    /**
     * 自动更新过期通知状态
     *
     * @return 影响行数
     */
    int autoUpdateExpiredNotifications();

    /**
     * 清理过期的通知记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer deleteExpiredNotifications(@Param("days") Integer days);

    /**
     * 导出通知列表
     *
     * @param queryVO 查询条件
     * @return 系统通知列表
     */
    List<SystemNotification> selectNotificationListForExport(@Param("queryVO") SystemNotificationQueryVO queryVO);

    /**
     * 查询通知标题唯一性
     *
     * @param notificationTitle 通知标题
     * @param excludeId         排除的通知ID
     * @return 系统通知
     */
    SystemNotification selectByNotificationTitle(@Param("notificationTitle") String notificationTitle, @Param("excludeId") String excludeId);

    /**
     * 查询定时发布的通知列表
     *
     * @return 定时发布通知列表
     */
    List<SystemNotification> selectScheduledNotifications();

    /**
     * 查询需要发送邮件的通知列表
     *
     * @return 需要发送邮件的通知列表
     */
    List<SystemNotification> selectEmailNotifications();

    /**
     * 查询需要发送短信的通知列表
     *
     * @return 需要发送短信的通知列表
     */
    List<SystemNotification> selectSmsNotifications();
}
