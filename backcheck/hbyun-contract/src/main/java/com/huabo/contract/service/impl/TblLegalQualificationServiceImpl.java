package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalQualification;
import com.huabo.contract.mapper.TblLegalQualificationMapper;
import com.huabo.contract.service.TblLegalQualificationService;

@Service
public class TblLegalQualificationServiceImpl implements TblLegalQualificationService {


    @Resource
    private TblLegalQualificationMapper tblLegalQualificationMapper;

    @Override
    public void findListByPageInfo(PageInfo<TblLegalQualification> pageInfo, TblLegalQualification qualification,BigDecimal disputeid) {
    	IPage<TblLegalQualification> page = new Page<TblLegalQualification>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
    	IPage<TblLegalQualification> pageList = tblLegalQualificationMapper.findListByPageInfo(page, qualification, disputeid);
    	pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
    }

    @Override
    public TblLegalQualification findById(BigDecimal qualId) {
    	return tblLegalQualificationMapper.findByQualId(qualId);
    }

    @Override
    public void saveQualification(TblLegalQualification qualification) {
    	tblLegalQualificationMapper.saveQualification(qualification);
    }

    @Override
    public void updateModifyQualification(TblLegalQualification old) {
    	tblLegalQualificationMapper.updateModifyQualification(old);
    }

    @Override
    public void removeQualification(BigDecimal qualId) {
    	this.tblLegalQualificationMapper.removeQualification(qualId);
    }
}
