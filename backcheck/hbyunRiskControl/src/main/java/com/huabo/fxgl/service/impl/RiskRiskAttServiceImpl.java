package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.RiskRiskAtt;
import com.huabo.fxgl.mapper.RiskRiskAttMapper;
import com.huabo.fxgl.service.IRiskRiskAttService;
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
public class RiskRiskAttServiceImpl extends ServiceImpl<RiskRiskAttMapper, RiskRiskAtt> implements IRiskRiskAttService {

    @Autowired
    private RiskRiskAttMapper riskRiskAttMapper;

    @Override
    public List<RiskRiskAtt> findRiskAttByRiskid(String riskid) {
        return riskRiskAttMapper.selectRiskAttByRiskid(riskid);
    }
}
