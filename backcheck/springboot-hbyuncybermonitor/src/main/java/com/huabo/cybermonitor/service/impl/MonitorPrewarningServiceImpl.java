package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.MonitorPrewarning;
import com.huabo.cybermonitor.mapper.MonitorPrewarningMapper;
import com.huabo.cybermonitor.service.IMonitorPrewarningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-08-12
 */
@Service
public class MonitorPrewarningServiceImpl extends ServiceImpl<MonitorPrewarningMapper, MonitorPrewarning> implements IMonitorPrewarningService {

    @Override
    public List<MonitorPrewarning> getResultBySignId(BigDecimal modelId, BigDecimal solutionResultId) {
        return this.baseMapper.getResultBySignId(modelId,solutionResultId);
    }
}
