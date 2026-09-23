package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.Flowdes;
import com.huabo.fxgl.mapper.FlowdesMapper;
import com.huabo.fxgl.service.IFlowdesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-08
 */
@Service
public class FlowdesServiceImpl extends ServiceImpl<FlowdesMapper, Flowdes> implements IFlowdesService {

    @Autowired
    private FlowdesMapper flowdesMapper;

    @Override
    public List<Flowdes> returnFlowdesByRiskid(String riskid) {
        return flowdesMapper.selectFlowdesByRiskid(riskid);
    }
}
