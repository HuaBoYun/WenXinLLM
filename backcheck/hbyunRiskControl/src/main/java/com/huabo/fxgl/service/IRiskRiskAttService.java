package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskRiskAtt;
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
public interface IRiskRiskAttService extends IService<RiskRiskAtt> {
    public List<RiskRiskAtt> findRiskAttByRiskid(String riskid);
}
