package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.ProcessSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
public interface IProcessSettingService extends IService<ProcessSetting> {


    public ProcessSetting getByModuel(String module);
    ProcessSetting getStatusByModuel(String moudleType);
}
