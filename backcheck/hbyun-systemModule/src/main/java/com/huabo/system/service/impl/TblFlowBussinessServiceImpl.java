package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblFlowBussiness;
import com.huabo.system.mapper.TblFlowBussinessMapper;
import com.huabo.system.service.TblFlowBussinessService;

@Service
public class TblFlowBussinessServiceImpl implements TblFlowBussinessService {

    @Resource
    private TblFlowBussinessMapper tblFlowBussinessMapper;

    @Override
    public List<TblFlowBussiness> findByFlowud(String flowid, TblFlowBussiness bussiness) {
        return this.tblFlowBussinessMapper.findByBussinessnumber(flowid, bussiness);

    }

    @Override
    public void save(TblFlowBussiness riskBussiness) {
        tblFlowBussinessMapper.insert(riskBussiness);
    }


    @Override
    public void delete(TblFlowBussiness flowid) {
        tblFlowBussinessMapper.deleteByBussinessid(flowid.getBussinessid());
    }

    @Override
    public TblFlowBussiness findUniqueByFlowId(BigDecimal flowid) {
        List<TblFlowBussiness> list = tblFlowBussinessMapper.findByflowid(flowid);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

}
