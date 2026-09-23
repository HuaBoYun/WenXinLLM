package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Staff;

import java.math.BigDecimal;
import java.util.List;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
public interface IRiskRiskmarkingService extends IService<RiskRiskmarking> {
    List<RiskRiskmarking> checkSubmit(BigDecimal planId);
//    IPage<RiskRiskmarking> findRiskByRiskAndUserId(RiskAssplan riskAssplan, IPage page, BigDecimal staffid);
    void deleteByMark(Set<RiskRiskmarking> markingSet);

    List<RiskRiskmarking> findByRiskId(BigDecimal assriskid);
    
    IPage<RiskRiskmarking> getMarkingList(BigDecimal planId, IPage pageBean);
}
