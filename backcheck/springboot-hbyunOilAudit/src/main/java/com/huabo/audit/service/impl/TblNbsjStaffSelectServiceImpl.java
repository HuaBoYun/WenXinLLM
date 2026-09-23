package com.huabo.audit.service.impl;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.service.TblNbsjStaffSelectService;

@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjStaffSelectServiceImpl implements TblNbsjStaffSelectService {
	
	@Autowired
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

	@Override
	public void deleteByprojectId(Integer projectId) throws Exception {
		
		this.tblNbsjStaffSelectMapper.deleteNbsjByProject(projectId);
	}

}
