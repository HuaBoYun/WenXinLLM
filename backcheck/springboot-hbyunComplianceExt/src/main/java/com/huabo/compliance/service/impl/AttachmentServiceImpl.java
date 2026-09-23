package com.huabo.compliance.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.mapper.TblAttachmentMapper;
import com.huabo.compliance.service.ActivityPluginsService;
import com.huabo.compliance.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class AttachmentServiceImpl implements AttachmentService {
	
	@Autowired
	TblAttachmentMapper tblAttachmentMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean dgfjlistPageList(String token, Integer pageNumber, Integer pageSize, String attname)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		
		Integer orgid = loginStaff.getLinkOrg().getOrgid().intValue();
		
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	
    	PageInfo<TblAttachment> pageInfo = new PageInfo<TblAttachment>();
//    	TblAttachment.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjEntermeeting);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblAttachmentMapper.selectListByPageInfo(pageInfo,attname,orgid));
    	pageInfo.setTotalRecord(this.tblAttachmentMapper.selectCountByPageInfo(pageInfo,attname,orgid));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public TblAttachment findById(String id) {
		//return this.tblAttachmentMapper.selectById(new BigDecimal(id));
		return null;
	}

	@Override
	public void modify(TblAttachment a) {
		tblAttachmentMapper.updateTblBugsInTblAttachment(a);
	}


}
