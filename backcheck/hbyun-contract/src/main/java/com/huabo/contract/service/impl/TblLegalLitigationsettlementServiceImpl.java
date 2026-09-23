package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalLitigationsettlement;
import com.huabo.contract.mapper.TblLegalLitigationsettlementMapper;
import com.huabo.contract.service.TblLegalLitigationsettlementService;

@Service
public class TblLegalLitigationsettlementServiceImpl implements TblLegalLitigationsettlementService {

    @Resource
    private TblLegalLitigationsettlementMapper tblLegalLitigationsettlementMapper;

    @Override
    public void findListByPageInfo(PageInfo<TblLegalLitigationsettlement> pageInfo, TblLegalLitigationsettlement litigation,BigDecimal disputeid) throws Exception {
    	IPage<TblLegalLitigationsettlement> page = new Page<TblLegalLitigationsettlement>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
    	IPage<TblLegalLitigationsettlement> pageList = tblLegalLitigationsettlementMapper.findListByPageInfo(page, litigation, disputeid);
    			
    	pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
    }

    @Override
    public TblLegalLitigationsettlement findById(BigDecimal litigationId) throws Exception {
        return this.tblLegalLitigationsettlementMapper.findById(litigationId);
    }

    @Override
    public void addLitigationSettlement(TblLegalLitigationsettlement litigation) throws Exception {
    	this.tblLegalLitigationsettlementMapper.saveLitigationSettlement(litigation);
    }

    @Override
    public void updateModifyLitigationSettlement(TblLegalLitigationsettlement oldLitigation) throws Exception {
    	this.tblLegalLitigationsettlementMapper.updateModifyLitigationSettlement(oldLitigation);
    }

    @Override
    public void removeLitigationSettlement(BigDecimal litigationid) throws Exception {
    	tblLegalLitigationsettlementMapper.deleteLitigationId(litigationid);
    }

    @Override
    public TblLegalLitigationsettlement findByLitigationid(BigDecimal litigationid) throws Exception {
        return tblLegalLitigationsettlementMapper.findByLitigationid(litigationid);
    }
}
