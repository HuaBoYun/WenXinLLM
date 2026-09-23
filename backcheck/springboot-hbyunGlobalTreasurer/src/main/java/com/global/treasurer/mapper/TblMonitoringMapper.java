package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TblMonitoringMapper extends BaseMapper<TblMonitoring> {
    List<TblMonitoring> selectMonitoringPage(Map<String, Object> params);
    int countMonitoringList(Map<String, Object> params);
    List<TblMonitoring> selectAlerts(@Param("orgId") Long orgId);
    List<TblMonitoring> selectCriticalAlerts(@Param("orgId") Long orgId);
    Map<String, Object> selectHealthStatus(@Param("orgId") Long orgId);
    Map<String, Object> selectDashboardData(@Param("orgId") Long orgId);
    List<Map<String, Object>> selectMonitoringTrend(Map<String, Object> params);
}
