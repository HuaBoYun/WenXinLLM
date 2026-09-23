package com.huabo.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblNbsjAuditplan;
import com.huabo.system.mapper.TblNbsjAuditplanMapper;
import com.huabo.system.service.TblNbsjAuditplanService;

@Service
public class TblNbsjAuditplanServiceImpl implements TblNbsjAuditplanService {

    @Resource
    private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;


    @Override
    public TblNbsjAuditplan get(String planid) {
        return tblNbsjAuditplanMapper.find(planid);
    }

    @Override
    public List<TblNbsjAuditplan> findCheckJH(String planid) {
        return tblNbsjAuditplanMapper.findCheckJH(planid);
    }

}
