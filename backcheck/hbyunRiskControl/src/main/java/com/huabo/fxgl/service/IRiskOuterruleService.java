package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.RiskOuterrule;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */

public interface IRiskOuterruleService extends IService<RiskOuterrule> {
    public List<Outerrule> findOuterRuleByRiskId(String riskid, Outerrule outerrule);

    public Integer isexist(RiskOuterrule riskOuterrule);
}
