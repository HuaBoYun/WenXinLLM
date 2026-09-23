package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
public interface IStaffService extends IService<Staff> {
    public void findAllPageBeanPid(IPage ip, Organization organization);



    public void  setRealName(IPage<MonitorSolution> page);

    void setRealNameForMonitorSolutionresult(IPage<MonitorSolutionresult> a);

    void setRealNameIndicator(List<Indicator> set);
}
