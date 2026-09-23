package com.huabo.audit.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.service.TblNbsjStaffSelectService;

import java.math.BigDecimal;

@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjStaffSelectServiceImpl implements TblNbsjStaffSelectService {
	
	@Autowired
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

	@Override
	public void deleteByprojectId(BigDecimal projectId) throws Exception {
		
		this.tblNbsjStaffSelectMapper.deleteNbsjByProject(projectId);
	}

}
