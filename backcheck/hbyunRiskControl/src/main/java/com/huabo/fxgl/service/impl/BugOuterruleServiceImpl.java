package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.BugOuterrule;
import com.huabo.fxgl.mapper.BugOuterruleMapper;
import com.huabo.fxgl.service.IBugOuterruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.BugOuterrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.mapper.BugOuterruleMapper;
import com.huabo.fxgl.service.IBugOuterruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.service.IOuterruleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */
@Service
public class BugOuterruleServiceImpl extends ServiceImpl<BugOuterruleMapper, BugOuterrule> implements IBugOuterruleService {
    @Autowired
    private BugOuterruleMapper bugOuterruleMapper;
    @Override
    public List<BugOuterrule> isIfFlowInner(String flowid, String innerid) {
        List<BugOuterrule> ifFlowInner = bugOuterruleMapper.isIfFlowInner(flowid, innerid);
        return ifFlowInner;
    }
    @Override
    public void deleteByBudId(String bugId) {
        QueryWrapper<BugOuterrule> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BUGID",bugId);
        this.remove(queryWrapper);
    }

    @Autowired
    private IOuterruleService iOuterruleService;

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description
    * @Date 2022/8/8
    * @param id
    * @return java.util.List<com.huabo.fxgl.entity.Outerrule>
    * @url:
    **/
    @Override
    public List<Outerrule> getOuterRulesByBugId(String bugId) {

        QueryWrapper<BugOuterrule> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BUGID",bugId);
        final List<BugOuterrule> list = this.list(queryWrapper);
        if(list==null||list.size()<=0){
            return new LinkedList<>();
        }
        final List<BigDecimal> collect = list.stream().map(BugOuterrule::getOutrulid).collect(Collectors.toList());
        return iOuterruleService.listByIds(collect);
    }






}
