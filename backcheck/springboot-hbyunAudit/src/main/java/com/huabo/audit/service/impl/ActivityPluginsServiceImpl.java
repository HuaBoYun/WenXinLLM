package com.huabo.audit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huabo.audit.oracle.entity.TblProcessSettingEntity;
import com.huabo.audit.oracle.mapper.TblProcessSettingMapper;
import com.huabo.audit.service.ActivityPluginsService;

/**
 * @author lyz
 * @description
 */
@Service
public class ActivityPluginsServiceImpl  implements ActivityPluginsService {

    @Autowired
    TblProcessSettingMapper tblProcessSettingMapper;
    @Override
    public String getoNState(String busType) {
        TblProcessSettingEntity  moduel = null;
        List<TblProcessSettingEntity> tblProcessSettingEntities = tblProcessSettingMapper.selectByBusType(busType);
        if(tblProcessSettingEntities.size()>0){
            moduel = tblProcessSettingEntities.get(0);
        }
        if(null!=moduel){
            if(moduel.getStatus().equals(TblProcessSettingEntity.OFF)){
                return ActivityPluginsService.OFF;
            }else{
                return ActivityPluginsService.ON;
            }
        }
        return ActivityPluginsService.ERROR;
    }
}
