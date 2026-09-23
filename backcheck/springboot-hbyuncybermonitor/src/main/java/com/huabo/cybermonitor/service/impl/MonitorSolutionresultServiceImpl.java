package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.MonitorSolutionresultVo;
import com.huabo.cybermonitor.mapper.MonitorSolutionresultMapper;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class MonitorSolutionresultServiceImpl extends ServiceImpl<MonitorSolutionresultMapper, MonitorSolutionresult> implements IMonitorSolutionresultService {

    @Resource
    MonitorSolutionresultMapper monitorSolutionresultMapper;
    @Override
    public IPage<MonitorSolutionresultVo> getSolutionresultVoPage(IPage<MonitorSolutionresultVo> page, String sql) {
        return this.monitorSolutionresultMapper.getSolutionresultVoPage(page,sql);
    }
}
