package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjPlanProjectMapper;
import com.huabo.audit.oracle.vo.TblNbsjPlanProjectVo;
import com.huabo.audit.service.TblNbsjPlanProjectService;


@Service
public class TblNbsjPlanProjectServiceImpl implements TblNbsjPlanProjectService {

	@Resource
	public TblNbsjPlanProjectMapper tblNbsjPlanProjectMapper;
	
	@Resource
	public TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
	
	@Resource
    private UserProvider userProvider;
	
	public JsonBean mergePlanProjectManageInfoList(List<TblNbsjPlanProject> projectList, String planId, String plancode,
			String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<TblNbsjPlanProject> newList = new ArrayList<TblNbsjPlanProject>(0);
		for (TblNbsjPlanProject project : projectList) {
			if(planId != null) {
				project.setPlanid(new BigDecimal(planId));
			}else {
				TblNbsjAuditplan plan = new TblNbsjAuditplan();
				plan.setPlancode(plancode);
				plan.setCreatestaffid(loginStaff.getStaffid());
				plan.setCreatetime(new Date());
				plan.setStatus(0);
				this.tblNbsjAuditplanMapper.insertEntity(plan);
				project.setPlanid(plan.getPlanid());
			}
			
			if(project.getPlanprojectid() != null) {
				//修改
				this.tblNbsjPlanProjectMapper.updateEntity(project);
			}else {
				//新增
				this.tblNbsjPlanProjectMapper.insertEntity(project);
			}
			newList.add(project);
		}
		resultMap.put("projectList", newList);
		return ResponseFormat.retParam(1,200,resultMap);
		
	};
	
	@Override
	public JsonBean mergePlanProjectManageInfo(TblNbsjPlanProject project, BigDecimal planId, String plancode,
			String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		if(project.getPlanid()==null) {
//			return ResponseFormat.retParam(0,"计划id不能为空",null);
//		}
		if(planId != null) {
		}else {
			TblNbsjAuditplan plan = new TblNbsjAuditplan();
			plan.setPlancode(plancode);
			plan.setCreatestaffid(loginStaff.getStaffid());
			plan.setCreatetime(new Date());
			plan.setStatus(0);
//			System.out.println(DateUtil.parseDate(project.getFinishtime(), "yyyy-MM-dd HH:mm:ss"));
			this.tblNbsjAuditplanMapper.insertEntity(plan);
			project.setPlanid(plan.getPlanid());
		}
		
		
		if(project.getPlanprojectid() != null) {
			//修改
			this.tblNbsjPlanProjectMapper.updateEntity(project);
		}else {
			//新增
			this.tblNbsjPlanProjectMapper.insertEntity(project);
		}
		resultMap.put("planProject", project);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean removePlanProjectInfo(String token, Integer planprojectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		this.tblNbsjPlanProjectMapper.deletePlanProjectInfo(planprojectid);
		
		return ResponseFormat.retParam(1,70003,null);
	}

	
	@Override
	public JsonBean findPlanProjectListInfoByPlanId(String token, Integer planId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		List<TblNbsjPlanProject> planList =  this.tblNbsjPlanProjectMapper.selectPlanProjectListInfoByPlanId(planId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("planList", planList);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getPlanProjectListByPlanIdPageInfo(String token, TblNbsjPlanProjectVo project)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		com.hbfk.util.PageInfo<TblNbsjPlanProject> pageInfo = new com.hbfk.util.PageInfo<TblNbsjPlanProject>();
		pageInfo.setCurrentPage(project.getPageNum());
		pageInfo.setPageSize(project.getPageSize());
		pageInfo.setTlist(this.tblNbsjPlanProjectMapper.selectPlanProjectListByPageInfo(project,pageInfo));
		pageInfo.setTotalRecord(this.tblNbsjPlanProjectMapper.selectPlanProjectCountByPageInfo(project,pageInfo));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findProjectListInfoByWspJhw(String token,Integer planId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
		
		List<TblNbsjProject> planList =  this.tblNbsjPlanProjectMapper.selectProjectListInfoByWspJhw(planId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("planList", planList);
		return ResponseFormat.retParam(1,200,resultMap);
	}


}
