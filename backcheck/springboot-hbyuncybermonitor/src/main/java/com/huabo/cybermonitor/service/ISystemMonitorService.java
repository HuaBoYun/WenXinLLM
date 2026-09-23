package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.SystemMonitor;
import com.huabo.cybermonitor.vo.SystemMonitorQueryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 系统监控记录服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface ISystemMonitorService extends IService<SystemMonitor> {

    /**
     * 分页查询系统监控记录列表
     *
     * @param queryVO 查询条件
     * @return 系统监控记录列表
     */
    IPage<SystemMonitor> getMonitorList(SystemMonitorQueryVO queryVO);

    /**
     * 获取系统监控记录详情
     *
     * @param monitorId 监控记录ID
     * @return 系统监控记录详情
     */
    SystemMonitor getMonitorDetail(String monitorId);

    /**
     * 新增系统监控记录
     *
     * @param monitor 系统监控记录
     * @return 是否成功
     */
    boolean addMonitor(SystemMonitor monitor);

    /**
     * 更新系统监控记录
     *
     * @param monitor 系统监控记录
     * @return 是否成功
     */
    boolean updateMonitor(SystemMonitor monitor);

    /**
     * 删除系统监控记录
     *
     * @param monitorId 监控记录ID
     * @return 是否成功
     */
    boolean deleteMonitor(String monitorId);

    /**
     * 批量删除系统监控记录
     *
     * @param monitorIds 监控记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteMonitor(List<String> monitorIds);

    /**
     * 根据监控类型查询监控记录列表
     *
     * @param monitorType 监控类型
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getMonitorsByType(String monitorType);

    /**
     * 根据监控状态查询监控记录列表
     *
     * @param monitorStatus 监控状态
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getMonitorsByStatus(String monitorStatus);

    /**
     * 根据告警级别查询监控记录列表
     *
     * @param alertLevel 告警级别
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getMonitorsByAlertLevel(String alertLevel);

    /**
     * 根据服务器查询监控记录列表
     *
     * @param serverIp 服务器IP
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getMonitorsByServer(String serverIp);

    /**
     * 根据应用查询监控记录列表
     *
     * @param applicationName 应用名称
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getMonitorsByApplication(String applicationName);

    /**
     * 查询未处理的监控记录列表
     *
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getUnprocessedMonitors();

    /**
     * 查询高优先级告警记录列表
     *
     * @return 系统监控记录列表
     */
    List<SystemMonitor> getHighPriorityAlerts();

    /**
     * 处理监控记录
     *
     * @param monitorId     监控记录ID
     * @param processStatus 处理状态
     * @param processBy     处理人
     * @param processRemark 处理备注
     * @return 是否成功
     */
    boolean processMonitor(String monitorId, String processStatus, String processBy, String processRemark);

    /**
     * 批量处理监控记录
     *
     * @param monitorIds    监控记录ID列表
     * @param processStatus 处理状态
     * @param processBy     处理人
     * @param processRemark 处理备注
     * @return 是否成功
     */
    boolean batchProcessMonitor(List<String> monitorIds, String processStatus, String processBy, String processRemark);

    /**
     * 记录监控数据
     *
     * @param monitorType      监控类型
     * @param monitorItem      监控项目
     * @param monitorValue     监控值
     * @param monitorUnit      监控单位
     * @param thresholdConfig  阈值配置
     * @param serverIp         服务器IP
     * @param serverName       服务器名称
     * @param applicationName  应用名称
     * @return 是否成功
     */
    boolean recordMonitorData(String monitorType, String monitorItem, String monitorValue,
                              String monitorUnit, String thresholdConfig, String serverIp,
                              String serverName, String applicationName);

    /**
     * 检查监控阈值并生成告警
     *
     * @param monitor 监控记录
     * @return 是否触发告警
     */
    boolean checkThresholdAndAlert(SystemMonitor monitor);

    /**
     * 发送告警通知
     *
     * @param monitor 监控记录
     * @return 是否成功
     */
    boolean sendAlertNotification(SystemMonitor monitor);

    /**
     * 获取监控统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getMonitorStatistics();

    /**
     * 获取监控类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getMonitorTypeDistribution();

    /**
     * 获取监控状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getMonitorStatusDistribution();

    /**
     * 获取告警级别分布统计
     *
     * @return 告警级别分布统计
     */
    List<Map<String, Object>> getAlertLevelDistribution();

    /**
     * 获取服务器监控分布统计
     *
     * @return 服务器分布统计
     */
    List<Map<String, Object>> getServerDistribution();

    /**
     * 获取应用监控分布统计
     *
     * @return 应用分布统计
     */
    List<Map<String, Object>> getApplicationDistribution();

    /**
     * 获取监控趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 趋势统计
     */
    List<Map<String, Object>> getMonitorTrend(String startDate, String endDate);

    /**
     * 获取告警趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 告警趋势统计
     */
    List<Map<String, Object>> getAlertTrend(String startDate, String endDate);

    /**
     * 获取性能监控统计
     *
     * @return 性能监控统计
     */
    Map<String, Object> getPerformanceStatistics();

    /**
     * 获取系统健康状况
     *
     * @return 系统健康状况
     */
    Map<String, Object> getSystemHealthStatus();

    /**
     * 获取实时监控数据
     *
     * @param monitorType 监控类型
     * @param serverIp    服务器IP
     * @return 实时监控数据
     */
    Map<String, Object> getRealTimeMonitorData(String monitorType, String serverIp);

    /**
     * 获取监控仪表板数据
     *
     * @return 仪表板数据
     */
    Map<String, Object> getMonitorDashboardData();

    /**
     * 清理过期的监控记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer cleanExpiredMonitors(Integer days);

    /**
     * 导出监控记录列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportMonitorList(SystemMonitorQueryVO queryVO, HttpServletResponse response);

    /**
     * 获取监控类型标签
     *
     * @param monitorType 监控类型
     * @return 类型标签
     */
    String getMonitorTypeLabel(String monitorType);

    /**
     * 获取监控状态标签
     *
     * @param monitorStatus 监控状态
     * @return 状态标签
     */
    String getMonitorStatusLabel(String monitorStatus);

    /**
     * 获取告警级别标签
     *
     * @param alertLevel 告警级别
     * @return 级别标签
     */
    String getAlertLevelLabel(String alertLevel);

    /**
     * 获取处理状态标签
     *
     * @param processStatus 处理状态
     * @return 状态标签
     */
    String getProcessStatusLabel(String processStatus);

    /**
     * 获取最新监控数据
     *
     * @param monitorType 监控类型
     * @param monitorItem 监控项目
     * @param serverIp    服务器IP
     * @return 最新监控数据
     */
    SystemMonitor getLatestMonitorData(String monitorType, String monitorItem, String serverIp);

    /**
     * 获取监控项目列表
     *
     * @param monitorType 监控类型
     * @return 监控项目列表
     */
    List<String> getMonitorItems(String monitorType);

    /**
     * 获取服务器列表
     *
     * @return 服务器列表
     */
    List<Map<String, Object>> getServerList();

    /**
     * 获取应用列表
     *
     * @return 应用列表
     */
    List<Map<String, Object>> getApplicationList();
}
