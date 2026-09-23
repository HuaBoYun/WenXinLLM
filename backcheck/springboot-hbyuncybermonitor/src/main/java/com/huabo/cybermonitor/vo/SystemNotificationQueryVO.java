package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统通知查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemNotificationQueryVO extends BaseVo {

    /**
     * 通知标题
     */
    private String notificationTitle;

    /**
     * 通知类型
     */
    private String notificationType;

    /**
     * 通知级别
     */
    private String notificationLevel;

    /**
     * 通知状态
     */
    private String notificationStatus;

    /**
     * 发布范围
     */
    private String publishScope;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 发布时间开始
     */
    private String publishTimeStart;

    /**
     * 发布时间结束
     */
    private String publishTimeEnd;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 关键词搜索
     */
    private String keyword;
}
