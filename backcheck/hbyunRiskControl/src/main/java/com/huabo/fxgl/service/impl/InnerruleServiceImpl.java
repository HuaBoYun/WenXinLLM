package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.mapper.InnerruleMapper;
import com.huabo.fxgl.service.IInnerruleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Service
public class InnerruleServiceImpl extends ServiceImpl<InnerruleMapper, Innerrule> implements IInnerruleService {
    @Autowired
    private InnerruleMapper innerruleMapper;

    @Override
    public IPage<Innerrule> findInnerRuleByBugPageBean(IPage page, String bugid, String orgid) {
        IPage<Innerrule> innerRuleByBugPageBean = innerruleMapper.findInnerRuleByBugPageBean(bugid, orgid, page);
        return innerRuleByBugPageBean;
    }

    @Override
    public IPage<Innerrule> findInnerRuleByFlow(IPage page, String flowid) {
        return baseMapper.selectInnerRuleByFlowId(page, flowid);
    }
}
