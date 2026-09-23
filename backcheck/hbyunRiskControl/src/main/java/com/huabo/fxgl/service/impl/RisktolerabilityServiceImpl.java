package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.RiskRiskevent;
import com.huabo.fxgl.entity.Risktolerability;
import com.huabo.fxgl.mapper.RisktolerabilityMapper;
import com.huabo.fxgl.service.IRisktolerabilityService;
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
public class RisktolerabilityServiceImpl extends ServiceImpl<RisktolerabilityMapper, Risktolerability> implements IRisktolerabilityService {

    @Autowired
    private RisktolerabilityMapper risktolerabilityMapper;

    @Override
    public List<Risktolerability> findRiskTolerByRiskid(String riskid) {
        return risktolerabilityMapper.selectRiskTolerByRiskid(riskid);
    }
}
