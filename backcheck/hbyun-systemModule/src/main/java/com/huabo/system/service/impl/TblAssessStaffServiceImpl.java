package com.huabo.system.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.mapper.TblAssessStaffMapper;
import com.huabo.system.service.TblAssessStaffService;

@Service
public class TblAssessStaffServiceImpl implements TblAssessStaffService {

    @Resource
    private TblAssessStaffMapper tblAssessStaffMapper;

    @Override
    public String getStatusByUserAsss(BigDecimal staffid, BigDecimal assid, BigDecimal orgid) {
    	return tblAssessStaffMapper.getStatusByUserAsss(staffid, assid, orgid);
    }
}
