package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskMonitoringDTO;
import com.global.treasurer.dto.RiskMonitoringQueryDTO;
import com.global.treasurer.entity.TblRiskMonitoring;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 风险监控Service接口
 *
 * @author 华博云开发团队
 * @since 2025-03-24
 */
public interface IRiskMonitoringService extends IService<TblRiskMonitoring> {

    PageInfo<TblRiskMonitoring> selectRiskMonitoringList(RiskMonitoringQueryDTO queryDTO);

    TblRiskMonitoring selectRiskMonitoringById(Long recordId);

    TblRiskMonitoring insertRiskMonitoring(RiskMonitoringDTO dto);

    TblRiskMonitoring updateRiskMonitoring(RiskMonitoringDTO dto);

    boolean deleteRiskMonitoring(Long recordId);

    boolean batchDeleteRiskMonitorings(List<Long> recordIds);

    TblRiskMonitoring triggerAlert(Long recordId, String alertMessage);

    TblRiskMonitoring handleMonitoring(Long recordId, String actionTaken);

    Map<String, Object> getOverview(Long orgId);

    void exportRiskMonitoring(Map<String, Object> body, HttpServletResponse response);
}

