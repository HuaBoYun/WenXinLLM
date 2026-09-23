package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalNegotiaterecord;
import com.huabo.contract.mapper.TblLegalNegotiaterecordMapper;
import com.huabo.contract.service.TblLegalNegotiateRecordService;

@Service
public class TblLegalNegotiateRecordServiceImpl implements TblLegalNegotiateRecordService {

    @Resource
    private TblLegalNegotiaterecordMapper tblLegalNegotiaterecordMapper;

	@Override
	public List<TblLegalNegotiaterecord> findListBynegotiaId(BigDecimal negotiaId) throws Exception {
		return this.tblLegalNegotiaterecordMapper.findListBynegotiaId(negotiaId);
	}

	@Override
	public void removeNegitiateRecord(BigDecimal recordId) {
		this.tblLegalNegotiaterecordMapper.removeNegitiateRecord(recordId);
	}

	@Override
	public void saveNegotiateRecord(TblLegalNegotiaterecord record) throws Exception {
		this.tblLegalNegotiaterecordMapper.saveNegotiateRecord(record);
	}

	@Override
	public TblLegalNegotiaterecord findById(BigDecimal recordId) throws Exception {
		return this.tblLegalNegotiaterecordMapper.findById(recordId);
	}

	@Override
	public void findByNegotiaId(PageInfo<TblLegalNegotiaterecord> pageInfo, BigDecimal negotiaId) throws Exception {
		IPage<TblLegalNegotiaterecord> page = new Page<TblLegalNegotiaterecord>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalNegotiaterecord> pageList = tblLegalNegotiaterecordMapper.findByNegotiaid(page, negotiaId);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public void modifyNegotiateRecord(TblLegalNegotiaterecord oldRecord) throws Exception {
		this.tblLegalNegotiaterecordMapper.updateModifyNegotiateRecord(oldRecord);
	}

}
