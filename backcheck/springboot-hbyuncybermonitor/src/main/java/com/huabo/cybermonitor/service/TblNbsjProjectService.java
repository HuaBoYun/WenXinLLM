package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.TblNbsjProject;

import java.math.BigDecimal;

/**
* 描述: 审计项目IDService
* @author: ziyao
* @date: 2022-04-12
*/
public interface TblNbsjProjectService {
	//获取当前用户实施项目
	TblNbsjProject getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception;
}
