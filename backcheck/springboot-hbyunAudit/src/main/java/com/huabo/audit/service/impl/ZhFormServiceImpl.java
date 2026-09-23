package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.huabo.audit.oracle.entity.ZhFormEntity;
import com.huabo.audit.oracle.mapper.ZhFormMapper;
import com.huabo.audit.oracle.vo.ZhFormVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ZhFormService;
import com.huabo.audit.util.PageInfo;
@Service
public class ZhFormServiceImpl implements ZhFormService {
	
	@Autowired
	private ZhFormMapper zhFormMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public void save(ZhFormEntity tnt) {
		// TODO Auto-generated method stub

	}

	@Override
	public ZhFormEntity findByid(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void del(ZhFormEntity tnt) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ZhFormEntity> findByProject(Integer projectId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	//==
	@Override
	public JsonBean reportPageList(String token, Integer pageNumber, Integer pageSize, ZhFormVo zhFormVo)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<ZhFormEntity> pageInfo = new PageInfo<ZhFormEntity>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.zhFormMapper.selectListByPageInfo(pageInfo,zhFormVo));
    	pageInfo.setTotalRecord(this.zhFormMapper.selectCountByPageInfo(pageInfo,zhFormVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean reportAdd(ZhFormEntity zf, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.zhFormMapper.selectPlanCodeByOrgid(zf);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
    	
//		zf.setCreatestaffid(loginStaff.getStaffid()+"");
		zf.setSelfDate(new Date());
		zf.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(zf.getFormid() != null) {
			//修改；
			this.zhFormMapper.updateEntity(zf);
		}else {
			//新增；
			this.zhFormMapper.insertEntity(zf);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("ZhForm",zf);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean reportDelete(Integer formid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		ZhFormEntity plan = this.zhFormMapper.selectById(formid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.zhFormMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(0,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.zhFormMapper.deleteById(formid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findNbsjWorkReportDetail(String token, Integer formid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		ZhFormEntity plan = this.zhFormMapper.selectById(formid);
		resultMap.put("zf", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

}
