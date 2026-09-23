package com.huabo.monitor.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.monitor.entity.TblOuterrule;
import com.huabo.monitor.mapper.TblOuterruleMapper;
import com.huabo.monitor.service.TblOuterRuleService;

/**
 * Created: 2022/11/25
 */
@Service
public class TblOuterRuleServiceImpl implements TblOuterRuleService {
    @Resource
    TblOuterruleMapper tblOuterruleMapper;

    @Override
    public TblOuterrule findById(BigDecimal id) {
        return null;
    }
}
