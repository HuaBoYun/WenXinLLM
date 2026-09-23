package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.RiskInnerrule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IRiskInnerruleService extends IService<RiskInnerrule> {
    public List<Innerrule> findInnerRuleByRiskId(String riskid, Innerrule innerrule);

    public Integer isexist(RiskInnerrule riskInnerRule);
}
