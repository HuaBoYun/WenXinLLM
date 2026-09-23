package com.huabo.audit.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblStaff;

public interface TblStaffService {

	Map<String, Object> findAllPageBeanPid(String username,String ralename,String pid, Integer pageNumber, Integer pageSize, String token, String staffId);
	
	JsonBean findByAllPageBean(String useranme, Integer pageNumber, Integer pageSize, String token, TblStaff  staff) throws Exception;
	
	JsonBean findUsrDetail(String token, Integer staffid) throws Exception;

	com.github.pagehelper.PageInfo<TblStaff> queryAuditObjectStaffAll(Integer pageNumber, Integer pageSize);



}
