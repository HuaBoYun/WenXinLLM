package com.huabo.cybermonitor.service.impl;


import com.huabo.cybermonitor.entity.TblNbsjProject;
import com.huabo.cybermonitor.mapper.TblNbsjProjectMapper;
import com.huabo.cybermonitor.mapper.TblNbsjStaffSelectMapper;
import com.huabo.cybermonitor.service.TblNbsjProjectService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;


/**
 * 描述:审计项目ID实现类
 *
 * @author: ziyao
 * @date: 2022-04-12
 */
@Service
public class TblNbsjProjectServiceImpl implements TblNbsjProjectService {

	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	

	@Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
	

	//获取当前实施项目；
	@Override
	public TblNbsjProject getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
		Integer projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
		if(projectId == null) {
			return null;
		}
		return this.tblNbsjProjectMapper.selectPJById(projectId);
	}

}
