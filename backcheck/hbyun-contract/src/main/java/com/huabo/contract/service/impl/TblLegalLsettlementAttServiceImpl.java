package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.contract.mapper.TblLegalLsettlementAttMapper;
import com.huabo.contract.service.TblLegalLsettlementAttService;

@Service
public class TblLegalLsettlementAttServiceImpl implements TblLegalLsettlementAttService {

    @Resource
    private TblLegalLsettlementAttMapper tblLegalLsettlementAttMapper;

    @Override
    public void deleteRelation(String attid)  throws Exception {
        tblLegalLsettlementAttMapper.deleteRelation(attid);
    }

    @Override
    public void insertAttRelation(BigDecimal litigationid, String id)  throws Exception {
        tblLegalLsettlementAttMapper.insertAttRelation(litigationid, id);
    }
}
