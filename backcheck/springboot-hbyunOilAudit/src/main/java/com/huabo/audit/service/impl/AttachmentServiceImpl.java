package com.huabo.audit.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.util.PageInfo;
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

		com.github.pagehelper.PageInfo<TblAttachment> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblAttachmentMapper.selectListByPageInfo(pageInfo,attname,orgid);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}


}
