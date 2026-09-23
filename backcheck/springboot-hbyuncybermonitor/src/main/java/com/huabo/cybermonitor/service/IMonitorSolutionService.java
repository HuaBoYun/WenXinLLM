package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-21
 */
public interface IMonitorSolutionService extends IService<MonitorSolution> {

    List<Indicator> getMonitorSolutionIndicators();


    void addyjModel(String[] modelids, String souceid);

    MonitorSolution queryOne(String selectedid);

    List<MonitorRule> queryMonitorRuleList(String selectedid);
}
