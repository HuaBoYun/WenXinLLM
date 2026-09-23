package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-14
 */
public interface IMonitorModelService extends IService<MonitorModel> {

    /**
     * 判断是否存在
     * @param number
     * @param orgid
     * @return
     */
    boolean validateByNumberAndOrg(String number,String orgid);


    IPage ShowPage(BigDecimal modelId,IPage ip);
}
