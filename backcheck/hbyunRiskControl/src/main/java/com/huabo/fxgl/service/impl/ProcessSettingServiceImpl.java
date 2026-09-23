package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.ProcessSetting;
import com.huabo.fxgl.mapper.ProcessSettingMapper;
import com.huabo.fxgl.service.IProcessSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Service
public class ProcessSettingServiceImpl extends ServiceImpl<ProcessSettingMapper, ProcessSetting> implements IProcessSettingService {

    @Override
    public ProcessSetting getStatusByModuel(String busType) {
        QueryWrapper<ProcessSetting> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("MODULE",busType);
        final List<ProcessSetting> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public ProcessSetting getByModuel(String module) {
        QueryWrapper<ProcessSetting> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("MODULE",module);
        List<ProcessSetting> moduel = this.list(queryWrapper);
        if(moduel.size()>0){
            return moduel.get(0);
        }
        return null;
    }
}
