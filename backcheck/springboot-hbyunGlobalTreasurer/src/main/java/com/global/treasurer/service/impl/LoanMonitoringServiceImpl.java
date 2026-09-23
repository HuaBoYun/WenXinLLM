package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LoanMonitoringDTO;
import com.global.treasurer.entity.TblLoanMonitoring;
import com.global.treasurer.mapper.LoanMonitoringMapper;
import com.global.treasurer.service.LoanMonitoringService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 贷款监控Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Service
public class LoanMonitoringServiceImpl implements LoanMonitoringService {

    @Autowired
    private LoanMonitoringMapper loanMonitoringMapper;

    @Override
    public PageInfo<TblLoanMonitoring> getMonitoringList(LoanMonitoringDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("loanId", dto.getLoanId());
        params.put("alertType", dto.getAlertType());
        params.put("alertLevel", dto.getAlertLevel());
        params.put("alertStatus", dto.getAlertStatus());
        List<TblLoanMonitoring> list = loanMonitoringMapper.selectMonitoringList(params);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblLoanMonitoring> getMonitoringByLoanId(String loanId) {
        return loanMonitoringMapper.selectByLoanId(loanId);
    }

    @Override
    public TblLoanMonitoring getMonitoringById(Long monitoringId) {
        return loanMonitoringMapper.selectById(monitoringId);
    }

    @Override
    @Transactional
    public TblLoanMonitoring addAlert(LoanMonitoringDTO dto) {
        TblLoanMonitoring monitoring = new TblLoanMonitoring();
        // 不设置 monitoringId，让 MyBatis-Plus 使用雪花算法自动生成
        monitoring.setLoanId(dto.getLoanId());
        monitoring.setAlertType(dto.getAlertType());
        monitoring.setAlertLevel(dto.getAlertLevel());
        monitoring.setAlertTitle(dto.getAlertTitle());
        monitoring.setAlertMessage(dto.getAlertMessage());
        monitoring.setOverdueDays(dto.getOverdueDays());
        monitoring.setOverdueAmount(dto.getOverdueAmount());
        monitoring.setAlertStatus(dto.getAlertStatus());
        monitoring.setRemark(dto.getRemark());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.hasText(dto.getDueDate())) {
                monitoring.setDueDate(sdf.parse(dto.getDueDate()));
            }
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误", e);
        }

        if (!StringUtils.hasText(monitoring.getAlertStatus())) {
            monitoring.setAlertStatus("PENDING");
        }
        monitoring.setAlertDate(new Date());
        monitoring.setDeleteFlag(0);
        monitoring.setCreatedTime(new Date());

        loanMonitoringMapper.insert(monitoring);
        return monitoring;
    }

    @Override
    @Transactional
    public TblLoanMonitoring updateAlert(LoanMonitoringDTO dto) {
        if (dto.getMonitoringId() == null) {
            throw new RuntimeException("预警ID不能为空");
        }

        TblLoanMonitoring monitoring = loanMonitoringMapper.selectById(dto.getMonitoringId());
        if (monitoring == null) {
            throw new RuntimeException("预警记录不存在");
        }

        // 更新字段
        monitoring.setLoanId(dto.getLoanId());
        monitoring.setAlertType(dto.getAlertType());
        monitoring.setAlertLevel(dto.getAlertLevel());
        monitoring.setAlertTitle(dto.getAlertTitle());
        monitoring.setAlertMessage(dto.getAlertMessage());
        monitoring.setOverdueDays(dto.getOverdueDays());
        monitoring.setOverdueAmount(dto.getOverdueAmount());
        monitoring.setAlertStatus(dto.getAlertStatus());
        monitoring.setRemark(dto.getRemark());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.hasText(dto.getDueDate())) {
                monitoring.setDueDate(sdf.parse(dto.getDueDate()));
            }
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误", e);
        }

        monitoring.setUpdatedTime(new Date());
        loanMonitoringMapper.updateById(monitoring);
        return monitoring;
    }

    @Override
    @Transactional
    public void handleAlert(Long monitoringId, Long handlerId, String handlerName,
                           String handleOpinion, String status) {
        TblLoanMonitoring monitoring = loanMonitoringMapper.selectById(monitoringId);
        if (monitoring == null) {
            throw new RuntimeException("预警记录不存在");
        }

        monitoring.setHandlerId(handlerId);
        monitoring.setHandlerName(handlerName);
        monitoring.setHandleOpinion(handleOpinion);
        monitoring.setAlertStatus(status);
        monitoring.setHandleDate(new Date());
        monitoring.setUpdatedTime(new Date());

        loanMonitoringMapper.updateById(monitoring);
    }

    @Override
    public List<TblLoanMonitoring> getPendingAlerts(String loanId) {
        return loanMonitoringMapper.selectPendingAlerts(loanId);
    }

    @Override
    public int countPendingAlerts(Long loanId) {
        return loanMonitoringMapper.countPendingAlerts(loanId);
    }

    @Override
    @Transactional
    public void deleteMonitoring(Long monitoringId) {
        loanMonitoringMapper.deleteById(monitoringId);
    }

    @Override
    @Transactional
    public void checkAndGenerateMaturityAlerts() {
        // 此方法由定时任务调用，检查即将到期的贷款并生成预警
        // 实际实现需要查询贷款表，这里只是框架
    }

    @Override
    @Transactional
    public void checkAndGenerateOverdueAlerts() {
        // 此方法由定时任务调用，检查逾期的还款计划并生成预警
        // 实际实现需要查询还款计划表，这里只是框架
    }
}

