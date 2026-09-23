package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.MonitorSolutionresultVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface IMonitorSolutionresultService extends IService<MonitorSolutionresult> {



   IPage<MonitorSolutionresultVo>  getSolutionresultVoPage(IPage<MonitorSolutionresultVo>  page , String sql );

}
