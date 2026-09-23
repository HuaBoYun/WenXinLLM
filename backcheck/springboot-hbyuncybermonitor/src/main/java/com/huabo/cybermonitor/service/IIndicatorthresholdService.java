package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.cybermonitor.entity.Indicatorthreshold;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface IIndicatorthresholdService extends IService<Indicatorthreshold> {


    List<Indicatorthreshold> QueryByIndicatorId(String indicatorid);
}
