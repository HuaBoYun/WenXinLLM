package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.RiskRiskevent;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.mapper.RiskRiskeventMapper;
import com.huabo.fxgl.service.IRiskRiskeventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Service
public class RiskRiskeventServiceImpl extends ServiceImpl<RiskRiskeventMapper, RiskRiskevent> implements IRiskRiskeventService {
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过RiskEventId查询TBL_RISK_RISKEVENT中间表
     * 获取RiskId
     * @Date 2022/8/10
     * @param eventid
     * @return java.util.List<java.math.BigDecimal>
     * @url:
     **/
    @Override
    public List<BigDecimal> getRiskIdsByEventId(String eventid) {
        QueryWrapper<RiskRiskevent> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("RISEVEID",eventid);
        List<RiskRiskevent> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
            return list.stream().map(RiskRiskevent::getRiskid).collect(Collectors.toList());
        }
        return null;
    }
}
