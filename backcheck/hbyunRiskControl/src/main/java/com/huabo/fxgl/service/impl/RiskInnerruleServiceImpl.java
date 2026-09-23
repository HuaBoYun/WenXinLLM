package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.RiskInnerrule;
import com.huabo.fxgl.mapper.RiskInnerruleMapper;
import com.huabo.fxgl.service.IRiskInnerruleService;
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
public class RiskInnerruleServiceImpl extends ServiceImpl<RiskInnerruleMapper, RiskInnerrule> implements IRiskInnerruleService {

    @Autowired
    private RiskInnerruleMapper riskInnerruleMapper;

    @Override
    public List<Innerrule> findInnerRuleByRiskId(String riskid, Innerrule innerrule) {
        QueryWrapper<Innerrule> queryWrapper = new QueryWrapper<>();
        if (innerrule != null && innerrule.getRulename() != null && !"".equals(innerrule.getRulename())) {
            queryWrapper.like("RULENAME",innerrule.getRulename());
        }
        if (innerrule != null && innerrule.getRulenumber() != null && !"".equals(innerrule.getRulenumber())) {
            queryWrapper.like("RULENUMBER",innerrule.getRulenumber());
        }
        return riskInnerruleMapper.selectInnerRuleByRiskId(riskid,queryWrapper);
    }

    @Override
    public Integer isexist(RiskInnerrule riskInnerRule) {
        return riskInnerruleMapper.selectisexist(riskInnerRule.getRiskid(),riskInnerRule.getInnrulid());
    }
}
