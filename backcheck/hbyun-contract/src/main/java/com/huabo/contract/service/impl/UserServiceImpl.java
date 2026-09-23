package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.mapper.TblStaffMapper;
import com.huabo.contract.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private TblStaffMapper tblStaffMapper;
	
	@Resource
	private UserProvider userProvider;
	
//	@Autowired
//    private ActivityPluginsService activityPluginsService;

	@Override
	public JsonBean findAllPageBeanPid(String token, Integer pageNumber, Integer pageSize,BigDecimal orgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		if(null == orgid) {
			orgid = loginStaff.getCurrentOrg().getOrgid();
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	IPage<TblStaff> page = new Page<TblStaff>(pageNumber,pageSize);
    	IPage<TblStaff> pageList = this.tblStaffMapper.selectTreeListByPageInfo(page,orgid);
    	
    	PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageList.getRecords());
    	pageInfo.setTotalRecord((int)pageList.getTotal());
//    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
//    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

}
