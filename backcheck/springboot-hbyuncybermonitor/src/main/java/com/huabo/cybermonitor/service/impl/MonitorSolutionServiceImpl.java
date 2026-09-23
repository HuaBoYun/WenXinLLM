package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionModel;
import com.huabo.cybermonitor.mapper.MonitorSolutionMapper;
import com.huabo.cybermonitor.mapper.MonitorSolutionModelMapper;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-21
 */
@Service
@Transactional
public class MonitorSolutionServiceImpl extends ServiceImpl<MonitorSolutionMapper, MonitorSolution> implements IMonitorSolutionService {

    @Resource
    MonitorSolutionModelMapper  monitorSolutionModelMapper;

    @Override
    public List<Indicator> getMonitorSolutionIndicators() {
        return this.baseMapper.getMonitorSolutionIndicators();
    }

    @Override

    public void addyjModel(String[] modelids, String souceid) {
        MonitorSolutionModel  monitorSolutionModel;
        for (int i = 0; i < modelids.length; i++) {
            monitorSolutionModel=new MonitorSolutionModel();
            monitorSolutionModel.setModelid(new BigDecimal(modelids[i]));
            monitorSolutionModel.setSolutionid(new BigDecimal(souceid));
            monitorSolutionModelMapper.insert(monitorSolutionModel);
        }

    }

    @Override
    public MonitorSolution queryOne(String selectedid) {

        return this.baseMapper.queryOne(selectedid);
    }

    @Override
    public List<MonitorRule> queryMonitorRuleList(String selectedid) {
        return this.baseMapper.queryMonitorRuleList(selectedid);
    }


}
