package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.mapper.MonitorRuleMapper;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class MonitorRuleServiceImpl extends ServiceImpl<MonitorRuleMapper, MonitorRule> implements IMonitorRuleService {

    @Override
    public void findRuleBySolut(IPage ip, String solutionid) {
        long start = (ip.getCurrent() - 1) * ip.getSize();
        long end = start + ip.getSize();
        List<Map<String,Object>> list = this.baseMapper.findRuleBySolut(start,end,solutionid);
        ip.setRecords(list);
        ip.setTotal(this.baseMapper.findCount(solutionid));
    }
}
