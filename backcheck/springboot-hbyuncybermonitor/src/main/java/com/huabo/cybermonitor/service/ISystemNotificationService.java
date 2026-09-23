package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.SystemNotification;
import com.huabo.cybermonitor.vo.SystemNotificationQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 系统通知公告服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface ISystemNotificationService extends IService<SystemNotification> {

    /**
     * 分页查询系统通知列表
     *
     * @param queryVO 查询条件
     * @return 系统通知列表
     */
    IPage<SystemNotification> getNotificationList(SystemNotificationQueryVO queryVO);

    /**
     * 获取系统通知详情
     *
     * @param notificationId 通知ID
     * @return 系统通知详情
     */
    SystemNotification getNotificationDetail(String notificationId);

    /**
     * 新增系统通知
     *
     * @param notification 系统通知
     * @return 是否成功
     */
    boolean addNotification(SystemNotification notification);

    /**
     * 更新系统通知
     *
     * @param notification 系统通知
     * @return 是否成功
     */
    boolean updateNotification(SystemNotification notification);

    /**
     * 删除系统通知
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean deleteNotification(String notificationId);

    /**
     * 批量删除系统通知
     *
     * @param notificationIds 通知ID列表
     * @return 是否成功
     */
    boolean batchDeleteNotification(List<String> notificationIds);

    /**
     * 发布通知
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean publishNotification(String notificationId);

    /**
     * 取消发布通知
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean unpublishNotification(String notificationId);

    /**
     * 批量发布通知
     *
     * @param notificationIds 通知ID列表
     * @return 是否成功
     */
    boolean batchPublishNotification(List<String> notificationIds);

    /**
     * 批量取消发布通知
     *
     * @param notificationIds 通知ID列表
     * @return 是否成功
     */
    boolean batchUnpublishNotification(List<String> notificationIds);

    /**
     * 置顶通知
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean topNotification(String notificationId);

    /**
     * 取消置顶通知
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean untopNotification(String notificationId);

    /**
     * 批量置顶通知
     *
     * @param notificationIds 通知ID列表
     * @return 是否成功
     */
    boolean batchTopNotification(List<String> notificationIds);

    /**
     * 批量取消置顶通知
     *
     * @param notificationIds 通知ID列表
     * @return 是否成功
     */
    boolean batchUntopNotification(List<String> notificationIds);

    /**
     * 根据通知类型查询通知列表
     *
     * @param notificationType 通知类型
     * @return 系统通知列表
     */
    List<SystemNotification> getNotificationsByType(String notificationType);

    /**
     * 根据通知级别查询通知列表
     *
     * @param notificationLevel 通知级别
     * @return 系统通知列表
     */
    List<SystemNotification> getNotificationsByLevel(String notificationLevel);

    /**
     * 根据通知状态查询通知列表
     *
     * @param notificationStatus 通知状态
     * @return 系统通知列表
     */
    List<SystemNotification> getNotificationsByStatus(String notificationStatus);

    /**
     * 查询已发布的通知列表
     *
     * @return 系统通知列表
     */
    List<SystemNotification> getPublishedNotifications();

    /**
     * 查询置顶的通知列表
     *
     * @return 系统通知列表
     */
    List<SystemNotification> getTopNotifications();

    /**
     * 查询弹窗通知列表
     *
     * @return 系统通知列表
     */
    List<SystemNotification> getPopupNotifications();

    /**
     * 查询用户相关的通知列表
     *
     * @param userId 用户ID
     * @return 系统通知列表
     */
    List<SystemNotification> getUserNotifications(String userId);

    /**
     * 查询角色相关的通知列表
     *
     * @param roleId 角色ID
     * @return 系统通知列表
     */
    List<SystemNotification> getRoleNotifications(String roleId);

    /**
     * 验证通知标题唯一性
     *
     * @param notificationTitle 通知标题
     * @param excludeId         排除的通知ID
     * @return 是否重复
     */
    boolean validateNotificationTitle(String notificationTitle, String excludeId);

    /**
     * 阅读通知
     *
     * @param notificationId 通知ID
     * @param userId         用户ID
     * @param readIp         阅读IP
     * @param userAgent      用户代理
     * @param readDevice     阅读设备
     * @return 是否成功
     */
    boolean readNotification(String notificationId, String userId, String readIp, String userAgent, String readDevice);

    /**
     * 批量阅读通知
     *
     * @param notificationIds 通知ID列表
     * @param userId          用户ID
     * @param readIp          阅读IP
     * @param userAgent       用户代理
     * @param readDevice      阅读设备
     * @return 是否成功
     */
    boolean batchReadNotification(List<String> notificationIds, String userId, String readIp, String userAgent, String readDevice);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    Integer getUserUnreadCount(String userId);

    /**
     * 获取用户未读通知列表
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @return 未读通知列表
     */
    List<Map<String, Object>> getUserUnreadNotifications(String userId, Integer limit);

    /**
     * 发送通知邮件
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean sendNotificationEmail(String notificationId);

    /**
     * 发送通知短信
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean sendNotificationSms(String notificationId);

    /**
     * 定时发布通知
     *
     * @return 发布数量
     */
    Integer schedulePublishNotifications();

    /**
     * 自动更新过期通知状态
     *
     * @return 更新数量
     */
    Integer autoUpdateExpiredNotifications();

    /**
     * 获取通知统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getNotificationStatistics();

    /**
     * 获取通知类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getNotificationTypeDistribution();

    /**
     * 获取通知级别分布统计
     *
     * @return 级别分布统计
     */
    List<Map<String, Object>> getNotificationLevelDistribution();

    /**
     * 获取通知状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getNotificationStatusDistribution();

    /**
     * 获取通知发布趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 发布趋势统计
     */
    List<Map<String, Object>> getNotificationPublishTrend(String startDate, String endDate);

    /**
     * 获取通知阅读统计
     *
     * @return 阅读统计
     */
    List<Map<String, Object>> getNotificationReadStatistics();

    /**
     * 获取热门通知列表
     *
     * @param limit 限制数量
     * @return 热门通知列表
     */
    List<SystemNotification> getPopularNotifications(Integer limit);

    /**
     * 获取最新通知列表
     *
     * @param limit 限制数量
     * @return 最新通知列表
     */
    List<SystemNotification> getLatestNotifications(Integer limit);

    /**
     * 清理过期的通知记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer cleanExpiredNotifications(Integer days);

    /**
     * 导出通知列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportNotificationList(SystemNotificationQueryVO queryVO, HttpServletResponse response);

    /**
     * 下载通知导入模板
     *
     * @param response HTTP响应
     */
    void downloadNotificationTemplate(HttpServletResponse response);

    /**
     * 批量导入通知
     *
     * @param file 导入文件
     * @return 导入结果
     */
    Map<String, Object> importNotificationList(MultipartFile file);

    /**
     * 获取通知类型标签
     *
     * @param notificationType 通知类型
     * @return 类型标签
     */
    String getNotificationTypeLabel(String notificationType);

    /**
     * 获取通知级别标签
     *
     * @param notificationLevel 通知级别
     * @return 级别标签
     */
    String getNotificationLevelLabel(String notificationLevel);

    /**
     * 获取通知状态标签
     *
     * @param notificationStatus 通知状态
     * @return 状态标签
     */
    String getNotificationStatusLabel(String notificationStatus);

    /**
     * 获取发布范围标签
     *
     * @param publishScope 发布范围
     * @return 范围标签
     */
    String getPublishScopeLabel(String publishScope);
}
