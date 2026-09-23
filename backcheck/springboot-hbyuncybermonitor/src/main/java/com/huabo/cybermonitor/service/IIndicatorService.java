package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.Indicator;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.Organization;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface IIndicatorService extends IService<Indicator> {

    Organization getHYFirst();

    void modifyStatusRun(List<Indicator> list);

    void modifyStatusRunError(List<Indicator> list);

    List<Indicator> showAllWithYj(String id);

    public Integer selectIndicatorNumber(String number);
}
