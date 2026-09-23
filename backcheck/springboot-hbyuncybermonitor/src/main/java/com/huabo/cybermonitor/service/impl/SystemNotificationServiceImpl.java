package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.SystemNotification;
import com.huabo.cybermonitor.mapper.SystemNotificationMapper;
import com.huabo.cybermonitor.service.ISystemNotificationService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.SystemNotificationQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统通知公告服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SystemNotificationServiceImpl extends ServiceImpl<SystemNotificationMapper, SystemNotification> implements ISystemNotificationService {

    @Autowired
    private SystemNotificationMapper notificationMapper;

    @Override
    public IPage<SystemNotification> getNotificationList(SystemNotificationQueryVO queryVO) {
        Page<SystemNotification> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return notificationMapper.selectNotificationList(page, queryVO);
    }

    @Override
    public SystemNotification getNotificationDetail(String notificationId) {
        if (StringUtils.isEmpty(notificationId)) {
            return null;
        }
        return notificationMapper.selectById(notificationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNotification(SystemNotification notification) {
        try {
            // 设置默认值
            if (notification.getNotificationStatus() == null) {
                notification.setNotificationStatus(SystemNotification.STATUS_DRAFT);
            }
            if (notification.getIsTop() == null) {
                notification.setIsTop(false);
            }
            if (notification.getIsPopup() == null) {
                notification.setIsPopup(false);
            }
            if (notification.getSendEmail() == null) {
                notification.setSendEmail(false);
            }
            if (notification.getSendSms() == null) {
                notification.setSendSms(false);
            }
            if (notification.getReadCount() == null) {
                notification.setReadCount(0);
            }
            notification.setCreateTime(LocalDateTime.now());
            notification.setUpdateTime(LocalDateTime.now());

            return save(notification);
        } catch (Exception e) {
            log.error("新增系统通知失败", e);
            throw new RuntimeException("新增系统通知失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNotification(SystemNotification notification) {
        try {
            notification.setUpdateTime(LocalDateTime.now());
            return updateById(notification);
        } catch (Exception e) {
            log.error("更新系统通知失败", e);
            throw new RuntimeException("更新系统通知失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotification(String notificationId) {
        try {
            return removeById(notificationId);
        } catch (Exception e) {
            log.error("删除系统通知失败", e);
            throw new RuntimeException("删除系统通知失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteNotification(List<String> notificationIds) {
        try {
            return removeByIds(notificationIds);
        } catch (Exception e) {
            log.error("批量删除系统通知失败", e);
            throw new RuntimeException("批量删除系统通知失败：" + e.getMessage());
        }
    }

    @Override
    public boolean publishNotification(String notificationId) {
        return false;
    }

    @Override
    public boolean unpublishNotification(String notificationId) {
        return false;
    }

    @Override
    public boolean batchPublishNotification(List<String> notificationIds) {
        return false;
    }

    @Override
    public boolean batchUnpublishNotification(List<String> notificationIds) {
        return false;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean topNotification(String notificationId) {
        try {
            return notificationMapper.updateNotificationTopStatus(notificationId, true) > 0;
        } catch (Exception e) {
            log.error("置顶系统通知失败", e);
            throw new RuntimeException("置顶系统通知失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean untopNotification(String notificationId) {
        try {
            return notificationMapper.updateNotificationTopStatus(notificationId, false) > 0;
        } catch (Exception e) {
            log.error("取消置顶系统通知失败", e);
            throw new RuntimeException("取消置顶系统通知失败：" + e.getMessage());
        }
    }

    @Override
    public boolean batchTopNotification(List<String> notificationIds) {
        return false;
    }

    @Override
    public boolean batchUntopNotification(List<String> notificationIds) {
        return false;
    }

    @Override
    public List<SystemNotification> getNotificationsByType(String notificationType) {
        return notificationMapper.selectNotificationsByType(notificationType);
    }

    @Override
    public List<SystemNotification> getNotificationsByLevel(String notificationLevel) {
        return notificationMapper.selectNotificationsByLevel(notificationLevel);
    }

    @Override
    public List<SystemNotification> getNotificationsByStatus(String notificationStatus) {
        return notificationMapper.selectNotificationsByStatus(notificationStatus);
    }

    @Override
    public List<SystemNotification> getPublishedNotifications() {
        return notificationMapper.selectPublishedNotifications();
    }

    @Override
    public List<SystemNotification> getTopNotifications() {
        return notificationMapper.selectTopNotifications();
    }

    @Override
    public List<SystemNotification> getPopupNotifications() {
        return null;
    }

    @Override
    public List<SystemNotification> getUserNotifications(String userId) {
        return null;
    }

    @Override
    public List<SystemNotification> getRoleNotifications(String roleId) {
        return null;
    }

    @Override
    public boolean validateNotificationTitle(String notificationTitle, String excludeId) {
        return false;
    }

    @Override
    public boolean readNotification(String notificationId, String userId, String readIp, String userAgent, String readDevice) {
        return false;
    }

    @Override
    public boolean batchReadNotification(List<String> notificationIds, String userId, String readIp, String userAgent, String readDevice) {
        return false;
    }

    @Override
    public Integer getUserUnreadCount(String userId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> getUserUnreadNotifications(String userId, Integer limit) {
        return null;
    }

    @Override
    public boolean sendNotificationEmail(String notificationId) {
        return false;
    }

    @Override
    public boolean sendNotificationSms(String notificationId) {
        return false;
    }

    @Override
    public Integer schedulePublishNotifications() {
        return 0;
    }

    @Override
    public Integer autoUpdateExpiredNotifications() {
        return 0;
    }


    @Override
    public Map<String, Object> getNotificationStatistics() {
        return notificationMapper.selectNotificationStatistics();
    }

    @Override
    public List<Map<String, Object>> getNotificationTypeDistribution() {
        return notificationMapper.selectNotificationTypeDistribution();
    }

    @Override
    public List<Map<String, Object>> getNotificationLevelDistribution() {
        return notificationMapper.selectNotificationLevelDistribution();
    }

    @Override
    public List<Map<String, Object>> getNotificationStatusDistribution() {
        return notificationMapper.selectNotificationStatusDistribution();
    }

    @Override
    public List<Map<String, Object>> getNotificationPublishTrend(String startDate, String endDate) {
        return notificationMapper.selectNotificationPublishTrend(startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getNotificationReadStatistics() {
        return null;
    }

    @Override
    public List<SystemNotification> getPopularNotifications(Integer limit) {
        return null;
    }

    @Override
    public List<SystemNotification> getLatestNotifications(Integer limit) {
        return null;
    }

    @Override
    public Integer cleanExpiredNotifications(Integer days) {
        return 0;
    }

    @Override
    public void exportNotificationList(SystemNotificationQueryVO queryVO, HttpServletResponse response) {
        try {
            List<SystemNotification> notificationList = notificationMapper.selectNotificationListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "通知标题", "通知类型", "通知级别", "通知状态", "是否置顶", "是否弹窗", 
                "发布范围", "发布时间", "过期时间", "阅读次数", "创建人", "创建时间"
            };

            ExcelUtil excelUtil = new ExcelUtil("系统通知列表", headers);

            // 添加数据行
            for (int i = 0; i < notificationList.size(); i++) {
                SystemNotification notification = notificationList.get(i);
                Object[] row = {
                    notification.getNotificationTitle(),
                    getNotificationTypeLabel(notification.getNotificationType()),
                    getNotificationLevelLabel(notification.getNotificationLevel()),
                    getNotificationStatusLabel(notification.getNotificationStatus()),
                    notification.getIsTop() ? "是" : "否",
                    notification.getIsPopup() ? "是" : "否",
                    getPublishScopeLabel(notification.getPublishScope()),
                    notification.getPublishTime(),
                    notification.getExpireTime(),
                    notification.getReadCount(),
                    notification.getCreateBy(),
                    notification.getCreateTime()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "系统通知列表.xls");
        } catch (Exception e) {
            log.error("导出系统通知列表失败", e);
            throw new RuntimeException("导出系统通知列表失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadNotificationTemplate(HttpServletResponse response) {

    }

    @Override
    public Map<String, Object> importNotificationList(MultipartFile file) {
        return null;
    }

    @Override
    public String getNotificationTypeLabel(String notificationType) {
        if (StringUtils.isEmpty(notificationType)) {
            return "";
        }
        switch (notificationType) {
            case SystemNotification.TYPE_SYSTEM:
                return "系统通知";
            case SystemNotification.TYPE_BUSINESS:
                return "业务通知";
            case SystemNotification.TYPE_SECURITY:
                return "安全通知";
            case SystemNotification.TYPE_MAINTENANCE:
                return "维护通知";
            case SystemNotification.TYPE_UPDATE:
                return "更新通知";
            case SystemNotification.TYPE_ALERT:
                return "告警通知";
            case SystemNotification.TYPE_ANNOUNCEMENT:
                return "公告通知";
            case SystemNotification.TYPE_REMINDER:
                return "提醒通知";
            default:
                return notificationType;
        }
    }

    @Override
    public String getNotificationLevelLabel(String notificationLevel) {
        if (StringUtils.isEmpty(notificationLevel)) {
            return "";
        }
        switch (notificationLevel) {
            case SystemNotification.LEVEL_INFO:
                return "信息";
            case SystemNotification.LEVEL_WARNING:
                return "警告";
            case SystemNotification.LEVEL_ERROR:
                return "错误";
            case SystemNotification.LEVEL_CRITICAL:
                return "严重";
            case SystemNotification.LEVEL_SUCCESS:
                return "成功";
            default:
                return notificationLevel;
        }
    }

    @Override
    public String getNotificationStatusLabel(String notificationStatus) {
        if (StringUtils.isEmpty(notificationStatus)) {
            return "";
        }
        switch (notificationStatus) {
            case SystemNotification.STATUS_DRAFT:
                return "草稿";
            case SystemNotification.STATUS_PUBLISHED:
                return "已发布";
            case SystemNotification.STATUS_EXPIRED:
                return "已过期";
            case SystemNotification.STATUS_CANCELLED:
                return "已取消";
            case SystemNotification.STATUS_SCHEDULED:
                return "定时发布";
            default:
                return notificationStatus;
        }
    }

    @Override
    public String getPublishScopeLabel(String publishScope) {
        if (StringUtils.isEmpty(publishScope)) {
            return "";
        }
        switch (publishScope) {
            case SystemNotification.SCOPE_ALL:
                return "全部用户";
            case SystemNotification.SCOPE_USERS:
                return "指定用户";
            case SystemNotification.SCOPE_ROLES:
                return "指定角色";
            case SystemNotification.SCOPE_DEPARTMENTS:
                return "指定部门";
            case SystemNotification.SCOPE_CUSTOM:
                return "自定义";
            default:
                return publishScope;
        }
    }
}
