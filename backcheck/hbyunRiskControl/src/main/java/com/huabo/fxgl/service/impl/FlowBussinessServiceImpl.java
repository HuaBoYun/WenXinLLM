package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.FlowBussiness;
import com.huabo.fxgl.mapper.FlowBussinessMapper;
import com.huabo.fxgl.service.IFlowBussinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Service
public class FlowBussinessServiceImpl extends ServiceImpl<FlowBussinessMapper, FlowBussiness> implements IFlowBussinessService {
@Autowired
private FlowBussinessMapper flowBussinessMapper;
    @Override
    public FlowBussiness findUniqueByFlowId(BigDecimal flowid) throws Exception {
        List<FlowBussiness> list = flowBussinessMapper.findUniqueByFlowId(flowid);

        if(list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public BigDecimal newBissinessId() {
        return new BigDecimal(baseMapper.selectMaxBussinessId() + 1);
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过riskId集合查询FlowBussiness
     * @Date 2022/8/10
     * @param riskIdslist riskId的集合
     * @return java.util.List<com.huabo.fxgl.entity.FlowBussiness>
     * @url:
     **/
    @Override
    public List<FlowBussiness> getByRiskIds(List<BigDecimal> riskIdslist) {
        QueryWrapper<FlowBussiness> queryWrapper=new QueryWrapper<>();
        if (riskIdslist!=null&&riskIdslist.size()>0){
            queryWrapper.in("FLOWID",riskIdslist);
            return list(queryWrapper);
        }
        return null;
    }
}
