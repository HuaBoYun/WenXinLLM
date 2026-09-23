package com.huabo.compliance.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.compliance.entity.TblInnerrule;
import com.huabo.compliance.mapper.TblOuterruleMapper;
import com.huabo.compliance.service.TblInnerRuleService;

/**
 * Created: 2022/11/25
 */
@Service
public class TblInnerRuleServiceImpl implements TblInnerRuleService {
    @Resource
    TblOuterruleMapper tblOuterruleMapper;

    @Override
    public TblInnerrule findById(BigDecimal id) {
        return null;
    }
}
