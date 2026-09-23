package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblProcessSettingEntity;
import com.huabo.compliance.mapper.TblProcessSettingMapper;
import com.huabo.compliance.service.ActivityPluginsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lyz
 * @description
 */
@Service
public class ActivityPluginsServiceImpl implements ActivityPluginsService {

    @Autowired
    TblProcessSettingMapper tblProcessSettingMapper;
    @Override
    public String getoNState(String busType) {
        TblProcessSettingEntity moduel = null;
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
