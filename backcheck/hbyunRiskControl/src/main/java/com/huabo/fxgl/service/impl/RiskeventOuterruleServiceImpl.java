package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.RiskeventOuterrule;
import com.huabo.fxgl.mapper.RiskeventOuterruleMapper;
import com.huabo.fxgl.service.IOuterruleService;
import com.huabo.fxgl.service.IRiskeventOuterruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Slf4j
@Service
public class RiskeventOuterruleServiceImpl extends ServiceImpl<RiskeventOuterruleMapper, RiskeventOuterrule> implements IRiskeventOuterruleService {
    @Autowired
    private IOuterruleService outerruleService;

    @Override
    public List<RiskeventOuterrule> getByEventId(String eventid) {
        QueryWrapper<RiskeventOuterrule> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("RISEVEID",eventid);
        List<RiskeventOuterrule> riskeventOuterrules= list(queryWrapper);
        if (riskeventOuterrules!=null&&riskeventOuterrules.size()>0){
            riskeventOuterrules.stream().map(item -> {
                 Outerrule outerrule = outerruleService.getById(item.getOutrulid());
                item.setOutrulName(outerrule.getRulename());
                return item;
            }).collect(Collectors.toList());
        }
        return riskeventOuterrules;
    }
}
