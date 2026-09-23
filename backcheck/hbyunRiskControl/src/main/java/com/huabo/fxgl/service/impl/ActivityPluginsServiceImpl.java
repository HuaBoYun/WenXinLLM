package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.ProcessSetting;
import com.huabo.fxgl.service.ActivityPluginsService;
import com.huabo.fxgl.service.IProcessSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zuoshun
 * @version V1.0
 * @Package com.huabo.fxgl.service.impl
 * @date 2022/8/18 14:42
 */
@Service
public class ActivityPluginsServiceImpl implements ActivityPluginsService {

    @Autowired
    private IProcessSettingService processSettingService;

    @Override
    public String getoNState(String busType) {
        ProcessSetting moduel = processSettingService.getStatusByModuel(busType);
        if (moduel!=null){
            if (moduel.getStatus().equals(ProcessSetting.OFF)){
                return ActivityPluginsService.OFF;/*开启状态*/
            }else {
                return ActivityPluginsService.ON; /*关闭状态*/
            }
        }
        return ActivityPluginsService.ERROR;  /*错误状态*/
    }



    /*@Override
    public String getoNState(String busType) {
        ProcessSetting moduel = this.processSettingService.getByModuel(busType);
        if(null!=moduel){
            if(moduel.getStatus().equals(ProcessSetting.OFF)){
                return ActivityPluginsService.OFF;
            }else{
                return ActivityPluginsService.ON;
            }
        }
        return ActivityPluginsService.ERROR;
    }*/

}
