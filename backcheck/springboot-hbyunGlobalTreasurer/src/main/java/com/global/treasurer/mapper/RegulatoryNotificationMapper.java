package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblRegulatoryNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 监管通知Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface RegulatoryNotificationMapper extends BaseMapper<TblRegulatoryNotification> {

    /**
     * 分页查询监管通知列表
     *
     * @param params 查询参数
     * @return 监管通知列表
     */
    List<TblRegulatoryNotification> selectNotificationList(Map<String, Object> params);

    /**
     * 根据ID查询监管通知详情
     *
     * @param notificationId 通知ID
     * @return 监管通知
     */
    TblRegulatoryNotification selectNotificationById(@Param("notificationId") String notificationId);

    /**
     * 查询未读通知
     *
     * @return 监管通知列表
     */
    List<TblRegulatoryNotification> selectUnreadNotifications();

    /**
     * 批量删除监管通知（逻辑删除）
     *
     * @param notificationIds 通知ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("notificationIds") List<String> notificationIds);

    /**
     * 更新通知状态
     *
     * @param notificationId 通知ID
     * @param sendStatus 发送状态
     * @return 影响行数
     */
    int updateNotificationStatus(@Param("notificationId") String notificationId, @Param("sendStatus") String sendStatus);
}

