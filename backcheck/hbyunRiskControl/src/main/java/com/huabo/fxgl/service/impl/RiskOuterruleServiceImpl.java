package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.RiskOuterrule;
import com.huabo.fxgl.mapper.RiskOuterruleMapper;
import com.huabo.fxgl.service.IRiskOuterruleService;
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
public class RiskOuterruleServiceImpl extends ServiceImpl<RiskOuterruleMapper, RiskOuterrule> implements IRiskOuterruleService {

    @Autowired RiskOuterruleMapper riskOuterruleMapper;

    @Override
    public List<Outerrule> findOuterRuleByRiskId(String riskid, Outerrule outerrule) {
        QueryWrapper<Outerrule> queryWrapper = new QueryWrapper<>();
        if (outerrule != null && outerrule.getRulename() != null && !"".equals(outerrule.getRulename())) {
            queryWrapper.like("RULENAME",outerrule.getRulename());
        }
        if (outerrule != null && outerrule.getRulenumber() != null && !"".equals(outerrule.getRulenumber())) {
            queryWrapper.like("RULENUMBER",outerrule.getRulenumber());
        }
        return riskOuterruleMapper.selectOuterRuleByRiskId(riskid,queryWrapper);
    }

    @Override
    public Integer isexist(RiskOuterrule riskOuterrule) {
        return riskOuterruleMapper.selectisexist(riskOuterrule.getRiskid(),riskOuterrule.getOutrulid());
    }

}
