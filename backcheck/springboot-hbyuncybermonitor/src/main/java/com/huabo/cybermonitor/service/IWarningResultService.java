package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.WarningResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface IWarningResultService extends IService<WarningResult> {

    public String getLast();
}
