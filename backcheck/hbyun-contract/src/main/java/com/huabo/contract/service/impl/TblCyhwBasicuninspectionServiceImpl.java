package com.huabo.contract.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.contract.mapper.TblCyhwBasicuninspectionMapper;
import com.huabo.contract.service.TblCyhwBasicuninspectionService;

@Service
public class TblCyhwBasicuninspectionServiceImpl implements TblCyhwBasicuninspectionService {

    @Resource
    private TblCyhwBasicuninspectionMapper tblCyhwBasicuninspectionMapper;

	@Override
	public void insertBybudget(com.huabo.contract.entity.TblCyhwBasicuninspection tcp) throws Exception {
		 tblCyhwBasicuninspectionMapper.insertBybudget(tcp);
	}

}
