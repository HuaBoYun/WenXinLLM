package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalArbitrationrecord;
import com.huabo.contract.mapper.TblLegalArbitrationrecordMapper;
import com.huabo.contract.service.TblLegalArbitrationrecordService;

@Service
public class TblLegalArbitrationrecordServiceImpl implements TblLegalArbitrationrecordService {


    @Resource
    private TblLegalArbitrationrecordMapper tblLegalArbitrationrecordMapper;

    @Override
    public List<TblLegalArbitrationrecord> findListBynegotiaId(BigDecimal arbitraId) throws Exception{
    	return tblLegalArbitrationrecordMapper.findListBynegotiaId(arbitraId);
    }

    @Override
    public void saveNegotiateRecord(TblLegalArbitrationrecord record) throws Exception {
    	this.tblLegalArbitrationrecordMapper.saveNegotiateRecord(record);
    }

    @Override
    public TblLegalArbitrationrecord findById(BigDecimal arrecordId) throws Exception {
        return tblLegalArbitrationrecordMapper.findByArrecordId(arrecordId);
    }

    @Override
    public void modifyNegotiateRecord(TblLegalArbitrationrecord oldRecord) throws Exception {
    	tblLegalArbitrationrecordMapper.modifyNegotiateRecord(oldRecord);
    }

    @Override
    public void removeNegitiateRecord(BigDecimal arrecordId) {
    	tblLegalArbitrationrecordMapper.removeNegitiateRecord(arrecordId);
    }

    @Override
    public void findListByPageInfo(PageInfo<TblLegalArbitrationrecord> pageInfo, BigDecimal arrecordid) throws Exception {
    	IPage<TblLegalArbitrationrecord> page = new Page<TblLegalArbitrationrecord>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
    	IPage<TblLegalArbitrationrecord> pageList =  tblLegalArbitrationrecordMapper.findListByPageInfo(page, arrecordid);
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
    }
}
