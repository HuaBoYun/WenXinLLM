package com.huabo.contract.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.contract.mapper.TblLegalArbitrationAttMapper;
import com.huabo.contract.service.TblLegalArbitrationAttService;

@Service
public class TblLegalArbitrationAttServiceImpl implements TblLegalArbitrationAttService {

    @Resource
    private TblLegalArbitrationAttMapper tblLegalArbitrationAttMapper;

    @Override
    public void deleteRelation(String attid) throws Exception {
    	tblLegalArbitrationAttMapper.deleteRelation(attid);
    }
}
