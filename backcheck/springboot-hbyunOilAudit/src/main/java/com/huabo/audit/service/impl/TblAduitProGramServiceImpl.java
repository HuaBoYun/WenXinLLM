package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblAduitProGramMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblAduitProGramService;
import com.huabo.audit.service.TblNbsjProjectService;
@Service
public class TblAduitProGramServiceImpl  implements TblAduitProGramService {

	@Autowired
    private TblAduitProGramMapper tblAduitProGramMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
    
    @Resource
    private UserProvider userProvider;
	
	@Override
	public void deleteByTargetId(Integer targetId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByTempId(Integer tempId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteZy(String tempid) {
		// TODO Auto-generated method stub
		
	}


	
	
	
	@Override
	public JsonBean defZyPageList(String token, Integer pageNumber, Integer pageSize,Integer tempId,Integer targetId,Integer projectId) throws Exception {
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
    	
//    	if(null==projectId) {
//    		if(null == tempId) {
//        		//==查询当前实施的项目！
//        		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//        		if(tnp == null) {
//        			return ResponseFormat.retParam(0,30003,resultMap);
//        		}
//        		projectId = tnp.getProjectId();
//        		if(null == projectId) {
//        			return ResponseFormat.retParam(0,30003,resultMap);
//        		}
//        		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
//    			//tempId = project.getTbltempletezy().getTempleteId();
//        		tempId=project.getTbltemplete().getTempleteId();
//        	}
//    	}else {
//    		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
//    		//tempId = project.getTbltempletezy().getTempleteId();
//    		tempId=project.getTbltemplete().getTempleteId();
//    	}
    	
    	
    	
    	PageInfo<TblAduitProGramEntity> pageInfo = new PageInfo<TblAduitProGramEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);

		Integer finalProjectId = projectId;
		Integer finalTempId = tempId;
		com.github.pagehelper.PageInfo<TblAduitProGramEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblAduitProGramMapper.selectListByPageInfo(pageInfo, finalProjectId, finalTempId,targetId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findDefZyDetail(String token, String programId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblAduitProGramEntity plan = this.tblAduitProGramMapper.selectById(programId);
		resultMap.put("auditProGram", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean defZyLeftTreeList(String token) throws Exception {
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return ResponseFormat.retParam(0,20006,null);
//		}
//		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		
//		List<TblAduitProGramEntity> plan = this.tblAduitProGramMapper.selectByTree();
//		resultMap.put("leftTree", plan);
//		return ResponseFormat.retParam(1,200,resultMap);
		return null;
	}

	@Override
	public JsonBean defCatAdd(TblAduitProGramEntity apg, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
//		Integer count = this.tblAduitProGramMapper.selectPlanCodeByOrgid(apg);
//		if(count > 0) {
//			return ResponseFormat.retParam(0,202,null);
//		}
		
		if(null == apg.getTempId()) {
			//==查询当前实施的项目！
			TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,30003,null);
			}
			Integer projectId = tnp.getProjectId();
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,null);
			}
			TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
			BigDecimal tempId = project.getTbltempletezy().getTempleteId();
			apg.setTempId(tempId);
		}
    	
//		apg.setCreatestaffid(loginStaff.getStaffid()+"");
		apg.setCreateTime(new Date());
		apg.setUpdateTime(new Date());
		apg.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(apg.getProgramId() != null) {
			//修改；
			this.tblAduitProGramMapper.updateEntity(apg);
		}else {
			//新增；
			this.tblAduitProGramMapper.insertEntity(apg);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("ProGram",apg);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean defCatDel(String programId, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblAduitProGramEntity plan = this.tblAduitProGramMapper.selectById(programId);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjWorkReportMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblAduitProGramMapper.deleteById(programId);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public List<TblAduitProGramEntity> findByALL(String tempid) {
		return this.tblAduitProGramMapper.findByALLTempid(tempid);
		 
	}
	
}
