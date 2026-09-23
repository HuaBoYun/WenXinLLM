package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.BugOuterrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.mapper.OuterruleMapper;
import com.huabo.fxgl.service.IOuterruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Service
public class OuterruleServiceImpl extends ServiceImpl<OuterruleMapper, Outerrule> implements IOuterruleService {
    @Autowired
    private OuterruleMapper outerruleMapper;
    @Override
    public IPage<Outerrule> findOuterRuleByBugPageBean(IPage page, String bugid, String orgid) {
        IPage<Outerrule> innerRuleByBugPageBean = outerruleMapper.findOuterRuleByBugPageBean(bugid, orgid, page);
        return  innerRuleByBugPageBean;

    }

    @Override
    public IPage<Outerrule> findOuterRuleByFlow(IPage page, String flowid) {
        return baseMapper.findOuterRuleByFlowId(page, flowid);
    }

}
