package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorModelresult;
import com.huabo.cybermonitor.mapper.MonitorModelresultMapper;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IMonitorModelresultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class MonitorModelresultServiceImpl extends ServiceImpl<MonitorModelresultMapper, MonitorModelresult> implements IMonitorModelresultService {
    @Resource
    YhrPageMapper yhrPageMapper;

    @Override
    public void queryPage(IPage ip, String modelid) {
        long start = (ip.getCurrent() - 1) * ip.getSize();
        long end = start + ip.getSize();

        String sql ="select tmm.*, to_char(SAVETIME,'yyyy-mm-dd HH24:mi:ss') savetimeformat,TBL_STAFF.REALNAME  from tbl_Monitor_ModelResult tmm inner join tbl_staff on tmm.STAFFID = TBL_STAFF.STAFFID where   regexp_like ( signid,'^[[:digit:]]+$')  and  modelid = "+modelid+" order by saveTime desc";
        String sqlCount = "select count(resultid) from tbl_Monitor_ModelResult tmm inner join tbl_staff on tmm.STAFFID = TBL_STAFF.STAFFID where   regexp_like ( signid,'^[[:digit:]]+$')  and  modelid = "+modelid+" order by saveTime desc";

        ip.setRecords(yhrPageMapper.queryList(start,end,sql));
        ip.setTotal(yhrPageMapper.queryCount(sqlCount));

    }
}
