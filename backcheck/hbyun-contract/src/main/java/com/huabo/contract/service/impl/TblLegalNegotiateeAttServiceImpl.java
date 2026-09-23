package com.huabo.contract.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.contract.mapper.TblLegalNegotiateeAttMapper;
import com.huabo.contract.service.TblLegalNegotiateeAttService;

@Service
public class TblLegalNegotiateeAttServiceImpl implements TblLegalNegotiateeAttService {


    @Resource
    private TblLegalNegotiateeAttMapper tblLegalNegotiateeAttMapper;

    @Override
    public void deleteRelation(String attid) throws Exception {
    	this.tblLegalNegotiateeAttMapper.deleteRelation(attid);
    }
}
