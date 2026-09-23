package com.huabo.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblFlowIOuterrule;
import com.huabo.system.mapper.TblFlowIOuterruleMapper;
import com.huabo.system.service.TblFlowIOuterruleService;

@Service
public class TblFlowIOuterruleServiceImpl implements TblFlowIOuterruleService {

    @Resource
    private TblFlowIOuterruleMapper tblFlowIOuterruleMapper;

    @Override
    public void saveTblFlowInnerRule(TblFlowIOuterrule inner) {
        tblFlowIOuterruleMapper.insertinner(inner);
    }

    @Override
    public void delteTblFlowInnerRule(TblFlowIOuterrule outer) {
        tblFlowIOuterruleMapper.delteTblFlowInnerRule(outer);
    }

}
