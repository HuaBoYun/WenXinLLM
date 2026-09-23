package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.SystemMonitor;
import com.huabo.cybermonitor.vo.SystemMonitorQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统监控记录 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface SystemMonitorMapper extends BaseMapper<SystemMonitor> {

    /**
     * 分页查询系统监控记录列表
     *
     * @param page    分页对象
     * @param queryVO 查询条件
     * @return 系统监控记录列表
     */
    IPage<SystemMonitor> selectMonitorList(Page<SystemMonitor> page, @Param("queryVO") SystemMonitorQueryVO queryVO);

    /**
     * 根据监控类型查询监控记录列表
     *
     * @param monitorType 监控类型
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectMonitorsByType(@Param("monitorType") String monitorType);

    /**
     * 根据监控状态查询监控记录列表
     *
     * @param monitorStatus 监控状态
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectMonitorsByStatus(@Param("monitorStatus") String monitorStatus);

    /**
     * 根据告警级别查询监控记录列表
     *
     * @param alertLevel 告警级别
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectMonitorsByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 根据服务器查询监控记录列表
     *
     * @param serverIp 服务器IP
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectMonitorsByServer(@Param("serverIp") String serverIp);

    /**
     * 根据应用查询监控记录列表
     *
     * @param applicationName 应用名称
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectMonitorsByApplication(@Param("applicationName") String applicationName);

    /**
     * 查询未处理的监控记录列表
     *
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectUnprocessedMonitors();

    /**
     * 查询高优先级告警记录列表
     *
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectHighPriorityAlerts();

    /**
     * 更新监控记录处理状态
     *
     * @param monitorId     监控记录ID
     * @param processStatus 处理状态
     * @param processBy     处理人
     * @param processRemark 处理备注
     * @return 影响行数
     */
    int updateMonitorProcessStatus(@Param("monitorId") String monitorId,
                                   @Param("processStatus") String processStatus,
                                   @Param("processBy") String processBy,
                                   @Param("processRemark") String processRemark);

    /**
     * 批量更新监控记录处理状态
     *
     * @param monitorIds    监控记录ID列表
     * @param processStatus 处理状态
     * @param processBy     处理人
     * @param processRemark 处理备注
     * @return 影响行数
     */
    int batchUpdateMonitorProcessStatus(@Param("monitorIds") List<String> monitorIds,
                                        @Param("processStatus") String processStatus,
                                        @Param("processBy") String processBy,
                                        @Param("processRemark") String processRemark);

    /**
     * 查询监控统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> selectMonitorStatistics();

    /**
     * 查询监控类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectMonitorTypeDistribution();

    /**
     * 查询监控状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> selectMonitorStatusDistribution();

    /**
     * 查询告警级别分布统计
     *
     * @return 告警级别分布统计
     */
    List<Map<String, Object>> selectAlertLevelDistribution();

    /**
     * 查询服务器监控分布统计
     *
     * @return 服务器分布统计
     */
    List<Map<String, Object>> selectServerDistribution();

    /**
     * 查询应用监控分布统计
     *
     * @return 应用分布统计
     */
    List<Map<String, Object>> selectApplicationDistribution();

    /**
     * 查询监控趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 趋势统计
     */
    List<Map<String, Object>> selectMonitorTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询告警趋势统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 告警趋势统计
     */
    List<Map<String, Object>> selectAlertTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询性能监控统计
     *
     * @return 性能监控统计
     */
    Map<String, Object> selectPerformanceStatistics();

    /**
     * 查询系统健康状况
     *
     * @return 系统健康状况
     */
    Map<String, Object> selectSystemHealthStatus();

    /**
     * 清理过期的监控记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer deleteExpiredMonitors(@Param("days") Integer days);

    /**
     * 导出监控记录列表
     *
     * @param queryVO 查询条件
     * @return 系统监控记录列表
     */
    List<SystemMonitor> selectMonitorListForExport(@Param("queryVO") SystemMonitorQueryVO queryVO);

    /**
     * 查询最新监控数据
     *
     * @param monitorType 监控类型
     * @param monitorItem 监控项目
     * @param serverIp    服务器IP
     * @return 最新监控数据
     */
    SystemMonitor selectLatestMonitorData(@Param("monitorType") String monitorType,
                                          @Param("monitorItem") String monitorItem,
                                          @Param("serverIp") String serverIp);

    /**
     * 查询监控项目列表
     *
     * @param monitorType 监控类型
     * @return 监控项目列表
     */
    List<String> selectMonitorItems(@Param("monitorType") String monitorType);

    /**
     * 查询服务器列表
     *
     * @return 服务器列表
     */
    List<Map<String, Object>> selectServerList();

    /**
     * 查询应用列表
     *
     * @return 应用列表
     */
    List<Map<String, Object>> selectApplicationList();
}
