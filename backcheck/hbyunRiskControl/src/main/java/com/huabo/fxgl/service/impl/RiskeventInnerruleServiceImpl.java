package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.RiskeventInnerrule;
import com.huabo.fxgl.mapper.RiskeventInnerruleMapper;
import com.huabo.fxgl.service.IInnerruleService;
import com.huabo.fxgl.service.IRiskeventInnerruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@Service
public class RiskeventInnerruleServiceImpl extends ServiceImpl<RiskeventInnerruleMapper, RiskeventInnerrule> implements IRiskeventInnerruleService {
    @Autowired
    private IInnerruleService iInnerruleService;

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description
     * @Date 2022/8/11
     * @param eventid
     * @return java.util.List<com.huabo.fxgl.entity.RiskeventInnerrule>
     * @url:
     **/
    @Override
    public List<RiskeventInnerrule> getByEventId(String eventid) {
        QueryWrapper<RiskeventInnerrule> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("RISEVEID",eventid);
        List<RiskeventInnerrule> RiskeventInnerrules= list(queryWrapper);
        if (RiskeventInnerrules!=null&&RiskeventInnerrules.size()>0){
            RiskeventInnerrules.stream().map(item -> {
               item.setInnrulName(iInnerruleService.getById(item.getInnrulid()).getRulename());
                return item;
            }).collect(Collectors.toList());
        }
        return RiskeventInnerrules;
    }
}
