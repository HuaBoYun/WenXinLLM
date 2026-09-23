package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskRiskevent;
import com.huabo.fxgl.entity.Risktolerability;
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
public interface IRisktolerabilityService extends IService<Risktolerability> {
    public List<Risktolerability> findRiskTolerByRiskid(String riskid);
}
