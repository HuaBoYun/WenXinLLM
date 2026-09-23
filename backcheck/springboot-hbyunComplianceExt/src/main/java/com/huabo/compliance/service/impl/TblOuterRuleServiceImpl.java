package com.huabo.compliance.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.compliance.entity.TblOuterrule;
import com.huabo.compliance.mapper.TblOuterruleMapper;
import com.huabo.compliance.service.TblOuterRuleService;

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
