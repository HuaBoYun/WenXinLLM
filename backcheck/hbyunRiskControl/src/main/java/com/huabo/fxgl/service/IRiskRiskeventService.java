package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskRiskevent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Riskevent;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IRiskRiskeventService extends IService<RiskRiskevent> {

    List<BigDecimal> getRiskIdsByEventId(String eventid);
}
