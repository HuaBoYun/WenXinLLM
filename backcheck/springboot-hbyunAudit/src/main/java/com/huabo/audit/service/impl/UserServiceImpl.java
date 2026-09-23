package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.UserService;
import com.huabo.audit.util.PageInfo;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private TblStaffMapper tblStaffMapper;
	
	@Autowired
    private ActivityPluginsService activityPluginsService;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean findAllPageBeanPid(String token, Integer pageNumber, Integer pageSize, BigDecimal orgid) throws Exception {
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
    	
    	PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblStaffMapper.selectListByPageInfo(pageInfo,orgid));
    	pageInfo.setTotalRecord(this.tblStaffMapper.selectCountByPageInfo(pageInfo,orgid));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

}
