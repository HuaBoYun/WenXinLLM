package com.huabo.audit.service;

import java.util.List;

import com.huabo.audit.oracle.entity.TblStaff;

public interface TblStaffNewService {
//	public PageBean findUserByOrgId(Integer orgid,Integer pageNumber,int pageSize);
	public List<TblStaff> findUserByOrgId(Integer orgid);
	public TblStaff get(Integer id);
}
