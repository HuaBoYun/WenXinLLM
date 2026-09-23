package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.mapper.MonitorModelMapper;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-14
 */
@Service
public class MonitorModelServiceImpl extends ServiceImpl<MonitorModelMapper, MonitorModel> implements IMonitorModelService {


    @Override
    public boolean validateByNumberAndOrg(String number, String orgid) {
        return false;
    }


    @Override
    public IPage ShowPage(BigDecimal modelId, IPage ip){
        int c = this.baseMapper.count(modelId);
        long start  = (ip.getCurrent() -1 ) * ip.getSize();
        long end   = start +  ip.getSize();
        List<Map<String,Object>> list = this.baseMapper.show(modelId,start,end);
        ip.setTotal(c);
        ip.setRecords(list);
        return ip;
    }

}
