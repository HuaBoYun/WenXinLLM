package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjBugEntity;
import com.huabo.audit.util.PageResult;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
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
	public JsonBean defZyPageList(String token, Integer pageNumber, Integer pageSize,BigDecimal tempId,BigDecimal targetId,BigDecimal projectId) throws Exception {
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
    	
    	if(null==projectId) {
    		if(null == tempId) {
        		//==查询当前实施的项目！
        		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        		if(tnp == null) {
        			return ResponseFormat.retParam(0,30003,resultMap);
        		} 
        		projectId = tnp.getProjectId();
        		if(null == projectId) {
        			return ResponseFormat.retParam(0,30003,resultMap);
        		}
        		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
    			//tempId = project.getTbltempletezy().getTempleteId();
        		tempId=project.getTbltemplete().getTempleteId();
        	}
    	}else {
    		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
    		//tempId = project.getTbltempletezy().getTempleteId();
    		  tempId=project.getTbltemplete().getTempleteId();
    	}
		TblAduitProGramEntity proGramEntity = new TblAduitProGramEntity();
		proGramEntity.setTempId(tempId);
		proGramEntity.setTargetId(targetId);
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblAduitProGramEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblAduitProGramMapper.selectListByPageInfoXml(proGramEntity));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblAduitProGramEntity> build = new PageResult<TblAduitProGramEntity>().build(pageInfo);
    	
    /*	PageInfo<TblAduitProGramEntity> pageInfo = new PageInfo<TblAduitProGramEntity>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblAduitProGramMapper.selectListByPageInfo(pageInfo,projectId,tempId,targetId));
    	pageInfo.setTotalRecord(this.tblAduitProGramMapper.selectCountByPageInfo(pageInfo,projectId,tempId,targetId));
    	pageInfo.getTotalPage();*/
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", build);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findDefZyDetail(String token, BigDecimal programId) throws Exception {
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
			BigDecimal projectId = tnp.getProjectId();
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,null);
			}
			TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
			BigDecimal tempId = project.getTbltempletezy().getTempleteId()	;
			apg.setTempId(tempId);
		}
    	
//		apg.setCreatestaffid(loginStaff.getStaffid()+"");
		apg.setCreateTime(new Date());
		apg.setUpdateTime(new Date());
		apg.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(apg.getProgramId() != null) {
			//修改；
			this.tblAduitProGramMapper.updateByPrimaryKeySelective(apg);
		}else {
			apg.setProgramId(RandomUtil.uuBigDecimalId());
			//新增；
			this.tblAduitProGramMapper.insert(apg);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("ProGram",apg);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean defCatDel(BigDecimal programId, String token) throws Exception {
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
