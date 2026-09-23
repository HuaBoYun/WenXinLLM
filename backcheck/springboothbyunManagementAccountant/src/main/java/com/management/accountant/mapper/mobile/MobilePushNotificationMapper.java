package com.management.accountant.mapper.mobile;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobilePushNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动推送通知 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface MobilePushNotificationMapper extends BaseMapper<MobilePushNotification> {

    // ==================== 基础查询方法 ====================
    
    /**
     * 根据推送通知编码查询
     */
    MobilePushNotification getByPushNotificationCode(@Param("pushNotificationCode") String pushNotificationCode);
    
    /**
     * 根据应用配置ID查询推送通知列表
     */
    List<MobilePushNotification> getByAppConfigId(@Param("appConfigId") String appConfigId);
    
    /**
     * 根据推送通知类型查询
     */
    List<MobilePushNotification> getByPushNotificationType(@Param("pushNotificationType") String pushNotificationType);
    
    /**
     * 根据推送通知分类查询
     */
    List<MobilePushNotification> getByPushNotificationCategory(@Param("pushNotificationCategory") String pushNotificationCategory);
    
    /**
     * 根据推送通知模块查询
     */
    List<MobilePushNotification> getByPushNotificationModule(@Param("pushNotificationModule") String pushNotificationModule);
    
    /**
     * 根据目标用户ID查询推送通知
     */
    List<MobilePushNotification> getByTargetUserId(@Param("targetUserId") String targetUserId);
    
    /**
     * 根据目标设备ID查询推送通知
     */
    List<MobilePushNotification> getByTargetDeviceId(@Param("targetDeviceId") String targetDeviceId);
    
    /**
     * 根据推送状态查询
     */
    List<MobilePushNotification> getByPushStatus(@Param("pushStatus") String pushStatus);
    
    /**
     * 根据发送状态查询
     */
    List<MobilePushNotification> getBySendStatus(@Param("sendStatus") String sendStatus);
    
    /**
     * 获取已发送的推送通知
     */
    List<MobilePushNotification> getSentPushNotifications();
    
    /**
     * 获取未发送的推送通知
     */
    List<MobilePushNotification> getUnsentPushNotifications();
    
    /**
     * 获取已过期的推送通知
     */
    List<MobilePushNotification> getExpiredPushNotifications();
    
    /**
     * 获取待发送的推送通知
     */
    List<MobilePushNotification> getPendingPushNotifications();
    
    /**
     * 获取需要重试的推送通知
     */
    List<MobilePushNotification> getRetryPushNotifications();

    // ==================== 分页查询方法 ====================
    
    /**
     * 分页查询推送通知
     */
    IPage<MobilePushNotification> getPushNotificationPage(Page<MobilePushNotification> page, @Param("params") Map<String, Object> params);
    
    /**
     * 条件分页查询推送通知
     */
    IPage<MobilePushNotification> getPushNotificationPageByCondition(Page<MobilePushNotification> page, @Param("condition") MobilePushNotification condition);
    
    /**
     * 高级搜索分页查询
     */
    IPage<MobilePushNotification> advancedSearchPage(Page<MobilePushNotification> page, @Param("params") Map<String, Object> params);

    // ==================== 统计查询方法 ====================
    
    /**
     * 统计推送通知总数
     */
    Long countPushNotifications();
    
    /**
     * 根据推送通知类型统计
     */
    Long countByPushNotificationType(@Param("pushNotificationType") String pushNotificationType);
    
    /**
     * 根据推送通知分类统计
     */
    Long countByPushNotificationCategory(@Param("pushNotificationCategory") String pushNotificationCategory);
    
    /**
     * 根据推送通知模块统计
     */
    Long countByPushNotificationModule(@Param("pushNotificationModule") String pushNotificationModule);
    
    /**
     * 根据推送状态统计
     */
    Long countByPushStatus(@Param("pushStatus") String pushStatus);
    
    /**
     * 根据发送状态统计
     */
    Long countBySendStatus(@Param("sendStatus") String sendStatus);
    
    /**
     * 根据目标用户ID统计
     */
    Long countByTargetUserId(@Param("targetUserId") String targetUserId);
    
    /**
     * 根据目标设备ID统计
     */
    Long countByTargetDeviceId(@Param("targetDeviceId") String targetDeviceId);
    
    /**
     * 根据创建时间统计
     */
    Long countByCreateTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 批量操作方法 ====================
    
    /**
     * 批量更新推送状态
     */
    int batchUpdatePushStatus(@Param("pushNotificationIds") List<String> pushNotificationIds, @Param("pushStatus") String pushStatus);
    
    /**
     * 批量更新发送状态
     */
    int batchUpdateSendStatus(@Param("pushNotificationIds") List<String> pushNotificationIds, @Param("sendStatus") String sendStatus);
    
    /**
     * 批量标记为已发送
     */
    int batchMarkAsSent(@Param("pushNotificationIds") List<String> pushNotificationIds);
    
    /**
     * 批量标记为已接收
     */
    int batchMarkAsReceived(@Param("pushNotificationIds") List<String> pushNotificationIds);
    
    /**
     * 批量标记为已阅读
     */
    int batchMarkAsRead(@Param("pushNotificationIds") List<String> pushNotificationIds);
    
    /**
     * 批量标记为已点击
     */
    int batchMarkAsClicked(@Param("pushNotificationIds") List<String> pushNotificationIds);
    
    /**
     * 批量删除推送通知
     */
    int batchDeletePushNotifications(@Param("pushNotificationIds") List<String> pushNotificationIds);

    // ==================== 推送管理方法 ====================
    
    /**
     * 更新推送状态
     */
    int updatePushStatus(@Param("pushNotificationId") String pushNotificationId, @Param("pushStatus") String pushStatus);
    
    /**
     * 更新发送状态
     */
    int updateSendStatus(@Param("pushNotificationId") String pushNotificationId, @Param("sendStatus") String sendStatus);
    
    /**
     * 更新发送时间
     */
    int updateSentTime(@Param("pushNotificationId") String pushNotificationId, @Param("sentTime") LocalDateTime sentTime);
    
    /**
     * 更新接收时间
     */
    int updateReceivedTime(@Param("pushNotificationId") String pushNotificationId, @Param("receivedTime") LocalDateTime receivedTime);
    
    /**
     * 更新阅读时间
     */
    int updateReadTime(@Param("pushNotificationId") String pushNotificationId, @Param("readTime") LocalDateTime readTime);
    
    /**
     * 更新点击时间
     */
    int updateClickedTime(@Param("pushNotificationId") String pushNotificationId, @Param("clickedTime") LocalDateTime clickedTime);
    
    /**
     * 更新发送次数
     */
    int updateSendCount(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 更新重试次数
     */
    int updateRetryCount(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 更新错误次数
     */
    int updateErrorCount(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 更新错误信息
     */
    int updateErrorMessage(@Param("pushNotificationId") String pushNotificationId, @Param("errorMessage") String errorMessage);
    
    /**
     * 更新推送响应
     */
    int updatePushResponse(@Param("pushNotificationId") String pushNotificationId, @Param("pushResponse") String pushResponse);
    
    /**
     * 更新推送结果
     */
    int updatePushResult(@Param("pushNotificationId") String pushNotificationId, @Param("pushResult") String pushResult);

    // ==================== 搜索查询方法 ====================
    
    /**
     * 根据关键词搜索推送通知
     */
    List<MobilePushNotification> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据标签搜索推送通知
     */
    List<MobilePushNotification> searchByTags(@Param("tags") List<String> tags);
    
    /**
     * 查找相似的推送通知
     */
    List<MobilePushNotification> findSimilarPushNotifications(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 查找热门推送通知
     */
    List<MobilePushNotification> findPopularPushNotifications(@Param("limit") Integer limit);
    
    /**
     * 查找推荐的推送通知
     */
    List<MobilePushNotification> findRecommendedPushNotifications(@Param("userId") String userId, @Param("limit") Integer limit);
    
    /**
     * 查找失败的推送通知
     */
    List<MobilePushNotification> findFailedPushNotifications();
    
    /**
     * 查找高优先级推送通知
     */
    List<MobilePushNotification> findHighPriorityPushNotifications();

    // ==================== 分析统计方法 ====================
    
    /**
     * 获取推送通知使用统计
     */
    Map<String, Object> getPushNotificationUsageStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取推送通知性能统计
     */
    Map<String, Object> getPushNotificationPerformanceStats(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 获取推送通知效果统计
     */
    Map<String, Object> getPushNotificationEffectStats(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 获取推送通知健康评分
     */
    BigDecimal getPushNotificationHealthScore(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 生成推送通知报告
     */
    Map<String, Object> generatePushNotificationReport(@Param("params") Map<String, Object> params);
    
    /**
     * 获取推送通知监控数据
     */
    List<Map<String, Object>> getPushNotificationMonitoringData(@Param("params") Map<String, Object> params);
    
    /**
     * 获取推送通知异常记录
     */
    List<Map<String, Object>> getPushNotificationExceptionRecords(@Param("pushNotificationId") String pushNotificationId);
    
    /**
     * 获取推送通知转化率统计
     */
    Map<String, Object> getPushNotificationConversionStats(@Param("params") Map<String, Object> params);

    // ==================== 维护管理方法 ====================
    
    /**
     * 清理过期的推送通知
     */
    int cleanExpiredPushNotifications();
    
    /**
     * 清理已发送的推送通知
     */
    int cleanSentPushNotifications(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 清理失败的推送通知
     */
    int cleanFailedPushNotifications();
    
    /**
     * 重建推送通知索引
     */
    int rebuildPushNotificationIndex();
    
    /**
     * 优化推送通知存储
     */
    int optimizePushNotificationStorage();
    
    /**
     * 同步推送通知状态
     */
    int syncPushNotificationStatus();
    
    /**
     * 验证推送通知配置
     */
    List<String> validatePushNotificationConfig();
    
    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview();
}
