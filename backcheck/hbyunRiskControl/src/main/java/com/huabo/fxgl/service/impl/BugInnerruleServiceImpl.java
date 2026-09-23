package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.BugInnerrule;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.mapper.BugInnerruleMapper;
import com.huabo.fxgl.service.IBugInnerruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.service.IInnerruleService;
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
public class BugInnerruleServiceImpl extends ServiceImpl<BugInnerruleMapper, BugInnerrule> implements IBugInnerruleService {
    @Autowired
    private BugInnerruleMapper bugInnerruleMapper;
    @Override
    public List<BugInnerrule> isIfFlowInner(String flowid, String innerid) {
        List<BugInnerrule> ifFlowInner = bugInnerruleMapper.isIfFlowInner(flowid, innerid);
        return ifFlowInner;

    }

    @Override
    public void deleteByBudId(String BugId) {
        QueryWrapper<BugInnerrule> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BUGID",BugId);
        this.remove(queryWrapper);
    }


    @Autowired
    private IInnerruleService iInnerruleService;

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 通过BugInnerRuler中间表，查询InnerRuler信息
    * @Date 2022/8/8
    * @param id BUG表的Id
    * @return java.util.List<com.huabo.fxgl.entity.Innerrule>
    * @url:
    **/
    @Override
    public List<Innerrule> getInnerRulesByBugId(String bugId) {
        QueryWrapper<BugInnerrule> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BUGID",bugId);
        final List<BugInnerrule> list = this.list(queryWrapper);
        if(list==null||list.size()<=0){
            return new LinkedList<>();
        }
        final List<BigDecimal> collect = list.stream().map(item -> item.getInnrulid()).collect(Collectors.toList());
       return iInnerruleService.listByIds(collect);
    }




}
