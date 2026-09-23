package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.JsonBean;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.FlowMapper;
import com.huabo.cybermonitor.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-08-11
 */
@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {

    @Override
    public List<Flow> showListWithIndicatorid(BigDecimal id) {
        return this.baseMapper.showListWithIndicatorid(id);
    }

    @Override
    public List<Controlmatrix> findAllControlmatrix(BigDecimal indicatorid) {
        return this.baseMapper.findAllControlmatrix(indicatorid);
    }

    @Override
    public List<Riskevent> findAllRISKEVENT(BigDecimal indicatorid) {
        return this.baseMapper.findAllRISKEVENT(indicatorid);
    }


    @Autowired
    IIndicatorService iIndicatorService;

    @Autowired
    IIndicatorFlowService indicatorFlowService;

    @Autowired
    IIndicatorCmService iIndicatorCmService;

    @Autowired
    IIndicatorRiskeventService iIndicatorRiskeventService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonBean deleteIndicatorById(String id) {

        if (id != null && !"".equals(id)) {
            Indicator in = iIndicatorService.getById(id);

            if(Objects.isNull(in)){
                return new JsonBean(500, id + "没有找到指标",null);
            }

            //删除中间件 TBL_INDICATOR_flow
            QueryWrapper<IndicatorFlow> query = new QueryWrapper<>();
            query.eq("indicatorid",id);
            indicatorFlowService.remove(query);

            List<Flow> flows = showListWithIndicatorid(in.getIndicatorid());
            for (Flow tblFlow : flows) {
                removeById(tblFlow.getFlowid());
            }



            List<Controlmatrix>  cons = findAllControlmatrix(in.getIndicatorid());
            for (Controlmatrix con : cons) {
                removeById(con.getConmatid());
            }


//            中间表
            QueryWrapper<IndicatorCm> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("indicatorid", in.getIndicatorid());
            iIndicatorCmService.remove(queryWrapper1);

            List<Riskevent> risks = findAllRISKEVENT(in.getIndicatorid());
            for (Riskevent en : risks) {
                removeById(en.getRiseveid().toString());
            }

            //中间表
            QueryWrapper<IndicatorRiskevent> queryWrapper2 = new QueryWrapper<>();
            queryWrapper1.eq("indicatorid", in.getIndicatorid());
            iIndicatorRiskeventService.remove(queryWrapper2);

            iIndicatorService.removeById(id);
        }

        return new JsonBean(200,"删除成功",null);
    }
}
