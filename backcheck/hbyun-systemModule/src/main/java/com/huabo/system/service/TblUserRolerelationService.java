package com.huabo.system.service;

import java.math.BigDecimal;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblUserRolerelation;

public interface TblUserRolerelationService {

	void setEndRoleDateByStaffIds(String roleid, String staffids) throws Exception;

	void saveEntity(TblUserRolerelation rela) throws Exception;

	void dealUserRoleRelation(BigDecimal staffId, String roleIdStrs, String iscanpre, TblStaffUtil loginStaff, String preRoleStrs) throws Exception;

	
}
