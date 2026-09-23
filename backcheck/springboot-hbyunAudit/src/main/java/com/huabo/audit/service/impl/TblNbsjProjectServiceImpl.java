package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.vo.*;
import com.huabo.audit.util.PageResult;
/*import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;*/
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAduitProGramService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblAutonoNumberService;
import com.huabo.audit.service.TblCirculationService;
import com.huabo.audit.service.TblMyTaskService;
import com.huabo.audit.service.TblNbsjAuthorizationService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjProjectteamService;
import com.huabo.audit.service.TblNbsjTempleteService;
import com.huabo.audit.service.TblProcessAnalusisUserService;
import com.huabo.audit.service.TblProcessAnalysisService;
import com.huabo.audit.service.TblStaffNewService;
import com.huabo.audit.service.TblTargetTypeService;
import com.huabo.audit.util.DateUtils;
import com.huabo.audit.util.HttpClient;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.util.R;
import com.huabo.audit.vo.result.QualityParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import cn.hutool.core.util.StrUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * 描述:审计项目ID实现类
 *
 * @author: ziyao
 * @date: 2022-04-12
 */
@Service
public class TblNbsjProjectServiceImpl implements TblNbsjProjectService {

	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
	private TblNbsjProjectteamMapper tblNbsjProjectteamMapper;
	
	@Resource
	private ActivityPluginsService activityPluginsService;
	
	@Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
	
    @Resource
   	public TblCirculationMapper tblCirculationMapper;
    
/*    @Resource
	private RuntimeService runtimeService;
	@Resource 
	private TaskService taskService;*/
	
    @Resource
    private ProcessService processService;
    
    @Resource
    private TblAuditOptionService tblAuditOptionService;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private TblAduitProGramMapper tblAduitProGramMapper;
    
    @Resource
    private TblNbsjTeamstaffMapper tblNbsjTeamstaffMapper;
    
    @Resource
    private TblNbsjAuthorizationService tblNbsjAuthorizationService;
    
    @Resource
    private TblNbsjOperateMapper tblNbsjOperateMapper;
    
    @Resource
    private TblNbsjQuestionMapper tblNbsjQuestionMapper;
    
    @Resource
    private TblNbsjProjectDataMapper tblNbsjProjectDataMapper;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private TblNbsjTempleteService tblNbsjTempleteService;
    
    @Resource
    private TblNbsjTempleteMapper tblNbsjTempleteMapper;
    
    @Resource
    private TblTargetTypeService tblTargetTypeService;
    
    @Resource
    private TblAduitProGramService tblAduitProGramService;
    
    @Resource
    private TblNbsjAuthorizationMapper tblNbsjAuthorizationMapper;
    @Resource
	private TblCirculationService tblCirculationService;

    
    @Resource
    private TblStaffNewService tblStaffNewService;
    
    @Resource
    private TblStaffMapper tblStaffMapper;
    
    @Resource
    private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
    
    @Resource
    private TblNbsjProjectteamService tblNbsjProjectteamService;
    
    @Resource
    private TblTargetTypeMapper tblTargetTypeMapper;
    
    @Autowired
    private TblAutonoNumberService tblAutonoNumberService;
    
  
    @Resource
    private TblProcessAnalysisMapper tblProcessAnalysisMapper;
    
    @Resource
    private TblProcessAnalusisUserMapper  tblProcessAnalusisUserMapper;
    
    @Resource
    private TblMyTaskMapper tblMytaskMapper;
    @Resource
	private TblFlowMapper tblFlowMapper;
    
    @Resource
  	private TblProcessAnalysisService tblProcessAnalysisService;

    @Resource
    private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;

    @Resource
  	private  TblProcessAnalusisUserService tblProcessAnalusisUserService;
    
    @Resource
 	private TblMyTaskService tblMyTaskService;
    
    @Resource
	private TblProcessSettingMapper processSettingMapper;
    
    @Resource
	private TblNbsjTypeMapper tblNbsjTypeMapper;

	@Resource
	private TblOrganizationMapper tblOrganizationMapper;

	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean getZgprojectPageList(String token, Integer pageNumber, Integer pageSize,
			TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
		tblnbsjProjectVo.setOrgId(t1.getOrgid());
		tblnbsjProjectVo.setStaffId(loginStaff.getStaffid());
		if(loginStaff.getTrole()!=null) {
			tblnbsjProjectVo.setRolename(loginStaff.getTrole().getRname());
		}
		
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
		com.github.pagehelper.PageInfo<TblNbsjProject> info = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> tblNbsjProjectMapper.findList(tblnbsjProjectVo));
		if (CollectionUtil.isNotEmpty(info.getList())) {
			PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(info);
			resultMap.put("pageInfo", build);
		}
//    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
//    	pageInfo.setPageSize(pageSize);
//    	pageInfo.setCurrentPage(pageNumber);
//    	pageInfo.setTlist(this.tblNbsjProjectMapper.selectZgListByPageInfo(pageInfo,tblnbsjProjectVo));
//    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectZgCountByPageInfo(pageInfo,tblnbsjProjectVo));
//    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
//    	resultMap.put("pageInfo", pageInfo);
    	
    	//==查询当前实施的项目！
		TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp != null) {
			BigDecimal projectId = tnp.getProjectId();
	    	resultMap.put("currProjectId", projectId);
		}
		
    	
    	return ResponseFormat.retParam(1,200,resultMap);
	}

    
    @Override
	public JsonBean getNbsjProjecGdtPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo tblnbsjProjectVo,
			String projectStartDate, String projectEndDate) throws Exception {
		
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return ResponseFormat.retParam(0,20006,null);
//		}
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	 
		
		com.github.pagehelper.PageInfo<TblNbsjProject> info = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> tblNbsjProjectMapper.findgdList(tblnbsjProjectVo));
		if (CollectionUtil.isNotEmpty(info.getList())) {
			PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(info);
			resultMap.put("pageInfo", build);
		}
//    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	pageInfo.setPageSize(pageSize);
//    	pageInfo.setCurrentPage(pageNumber);
//    	//project.setOrgId(loginStaff.getCurrentOrg().getOrgid().intValue());
//    	//project.setStaffId(loginStaff.getStaffid().intValue());
//        
//        pageInfo.setTlist(this.tblNbsjProjectMapper.selectProjectgdListByPageInfo(pageInfo,projectStartDate,projectEndDate,project));
//        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectProjectgdListByPageCount(projectStartDate,projectEndDate,project));
//        resultMap.put("pageInfo", pageInfo);
        
		return ResponseFormat.retParam(1,200,resultMap);
		  
	}

    
    
    @Override
	public JsonBean getprojectLisbystaffidt(String token, TblnbsjProjectVo project) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
        
        List<TblNbsjProject> list = this.tblNbsjProjectMapper.selectBystaffid(project.getStaffId());
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
		  
	}
    
    
	
	//获取当前实施项目；
		@Override
		public TblNbsjProject getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
			BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
			if(projectId == null) {
				return null;
			}
			return this.tblNbsjProjectMapper.selectPJById(projectId);
		}
	
	@Override
	public JsonBean getAuditFileInfoList(String token, String projectName, Integer pageNumber, Integer pageSize)
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
		
    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	pageInfo.setTlist(this.tblNbsjProjectMapper.selectAuditFileListPageInfo(pageInfo,projectName,loginStaff.getCurrentOrg().getOrgid()));
    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectAuditFileCountByPage(pageInfo,projectName,loginStaff.getCurrentOrg().getOrgid()));
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	
	@Override
	public JsonBean getNbsjProjectPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo project,
			String projectStartDate, String projectEndDate) throws Exception {
		
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
    	
    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	project.setOrgId(loginStaff.getCurrentOrg().getOrgid());
        if(!JudgeRoleRight.judgeRoleRight(SystemStaticValue.SJLBQX,loginStaff.getRoleNames())){
        	project.setStaffId(loginStaff.getStaffid());
        }
        
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectProjectListByPageInfo(pageInfo,projectStartDate,projectEndDate,project));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectProjectCountByPageInfo(pageInfo,projectStartDate,projectEndDate,project));
        
        TblNbsjStaffSelect oldtblNbsjStaffSelect = this.tblNbsjStaffSelectMapper.selectByUserId(loginStaff.getStaffid());
        
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_XMGL.name());
        
        resultMap.put("currentProjectid", oldtblNbsjStaffSelect.getProjectId());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        
		return ResponseFormat.retParam(1,200,resultMap);
		  
	}

	@Override
	 public JsonBean projectProposalPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
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
	     
	   //==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		BigDecimal projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
		tblnbsjProjectVo.setProjectId(projectId);
	     
	    /* com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
	     pageInfo.setPageSize(pageSize);
	     pageInfo.setCurrentPage(pageNumber);
	     //查询实施项目引用归档项目
	     pageInfo.setTlist(this.tblNbsjProjectMapper.selectListByPageInfo(pageInfo,tblnbsjProjectVo));
	     pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectCountByPageInfo(pageInfo,tblnbsjProjectVo));
	     pageInfo.getTotalPage();*/
		//查询实施项目引用归档项目

		//密级数据权限查询
		String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.ORGID", "TNA.ORGID","TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());

		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.selectListByPageInfoXml(tblnbsjProjectVo,sql));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", build);
	     return ResponseFormat.retParam(1,200,resultMap);
	 }

	 @Override
	 public JsonBean findProjectProposalDetail(String token, BigDecimal dataId) throws Exception {
		  TblStaffUtil loginStaff = userProvider.get();
		  if(loginStaff == null) {
		   return ResponseFormat.retParam(0,20006,null);
		  }
		  Map<String,Object> resultMap = new HashMap<String,Object>(0);
		  
		  TblNbsjProject plan = this.tblNbsjProjectMapper.selectById(dataId);
		  resultMap.put("Project", plan);
		  return ResponseFormat.retParam(1,200,resultMap);
	 }


	@Override
	public JsonBean getAuditFileDetailTreeInfo(String token, BigDecimal projectId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		/*TblNbsjProject project = null;
        if (projectId != null) {
            project = tblNbsjProjectMapper.selectEntityById(projectId);
        }else {
        	project =  this.tblNbsjProjectMapper.getSelectProject();
        }
		IF()*/
		
		
		
		return null;
	}


	@Override
	public JsonBean sjgdNewPageList(String token, Integer pageNumber, Integer pageSize,
			TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
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
	     /*com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
	     pageInfo.setPageSize(pageSize);
	     pageInfo.setCurrentPage(pageNumber);*/
		 PageResult<TblNbsjProject> build = null;
	        //审计责任人、审计负责人可以看所有归档的项目
		 
	        if(loginStaff.getRoleNames()!=null&&(loginStaff.getRoleNames().indexOf("审计中心人员")>=0)) {
				//链表分页  xml 写法
	        	
//	        	 String mjsql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", "TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
	        	String mjsql = null;
				com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
						.doSelectPageInfo(() -> tblNbsjProjectMapper.selectSjgdNewListByPageInfoXml(tblnbsjProjectVo,loginStaff,mjsql));

				//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
				 build = new PageResult<TblNbsjProject>().build(pageInfo);
	        	//pageInfo.setTlist(this.tblNbsjProjectMapper.selectSjgdNewListByPageInfo(pageInfo,tblnbsjProjectVo));
	        	//pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectSjgdNewCountByPageInfo(pageInfo,tblnbsjProjectVo));
	        }else {
	        	//项目经理看自己的项目
	        	//工作人员看借阅的项目
	   	       tblnbsjProjectVo.setStaffId(loginStaff.getStaffid());
	   	       
	   	       String mjsql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.ORGID", "TNA.ORGID", "TNA.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
				//链表分页  xml 写法
				com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
						.doSelectPageInfo(() -> tblNbsjProjectMapper.selectSjgdNewListByStaffidPageInfoXml(tblnbsjProjectVo,loginStaff,mjsql));

				//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
				build = new PageResult<TblNbsjProject>().build(pageInfo);
	        	/*pageInfo.setTlist(this.tblNbsjProjectMapper.selectSjgdNewListByStaffidPageInfo(pageInfo,tblnbsjProjectVo));
	        	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectSjgdNewCountByStaffidPageInfo(pageInfo,tblnbsjProjectVo));*/
	        }
	     //pageInfo.getTotalPage();
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", build);
	     return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findSjgdNewDetail(String token, BigDecimal projectid) throws Exception {
		 TblStaffUtil loginStaff = userProvider.get();
		  if(loginStaff == null) {
		   return ResponseFormat.retParam(0,20006,null);
		  }
		  Map<String,Object> resultMap = new HashMap<String,Object>(0);
		  
		  TblNbsjProject plan = this.tblNbsjProjectMapper.selectById(projectid);
		  resultMap.put("Project", plan);
		  return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dajyNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
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
	     
	     BigDecimal staffid = loginStaff.getStaffid();
	     BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		tblnbsjProjectVo.setStaffId(staffid);
		tblnbsjProjectVo.setOrgId(orgid);
	    /* com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
	     pageInfo.setPageSize(pageSize);
	     pageInfo.setCurrentPage(pageNumber);*/
		//链表分页  xml 写法
		
//		 String mjsql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "NP.ORGID", "NP.ORGID", "NP.CREATESTAFFID", "NP.SECRECTLEVELID", "NP.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
		String mjsql = null;
		
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.selectDajyNewListByPageInfoXml(tblnbsjProjectVo,loginStaff,mjsql));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
	     /*pageInfo.setTlist(this.tblNbsjProjectMapper.selectDajyNewListByPageInfo(pageInfo,orgid,staffid,tblnbsjProjectVo));
	     pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectDajyNewCountByPageInfo(pageInfo,orgid,staffid,tblnbsjProjectVo));
	     pageInfo.getTotalPage();*/
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", build);
	     return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean jyrzNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		  if(loginStaff == null) {
		   return ResponseFormat.retParam(0,20006,null);
		  }
		  
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		tblnbsjProjectVo.setOrgId(orgid);
		tblnbsjProjectVo.setStaffId(loginStaff.getStaffid());
	     if(pageNumber == null) {
	      pageNumber = 1;
	     }
	     if(pageSize==null) {
	      pageSize=15;
	     }
	     Map<String,Object> resultMap = new HashMap<String,Object>(0);
	     
	    /* com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
	     pageInfo.setPageSize(pageSize);
	     pageInfo.setCurrentPage(pageNumber);
	     pageInfo.setTlist(this.tblNbsjProjectMapper.selectJyrzNewListByPageInfo(pageInfo,orgid,Integer.parseInt(loginStaff.getStaffid().toString()),tblnbsjProjectVo));
	     pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectJyrzNewCountByPageInfo(pageInfo,orgid,Integer.parseInt(loginStaff.getStaffid().toString()),tblnbsjProjectVo));
	     pageInfo.getTotalPage();*/

		//链表分页  xml 写法
	     
	     String mjsql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", "TNA.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
	     
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.selectJyrzNewListByPageInfoXml(tblnbsjProjectVo,loginStaff,mjsql));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", build);
	     return ResponseFormat.retParam(1,200,resultMap);
	}


//	@Override
//	public JsonBean projectPageList(String token, Integer pageNumber, Integer pageSize,
//			TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return ResponseFormat.retParam(0,20006,null);
//		}
//		
//		TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
//		tblnbsjProjectVo.setOrgId(t1.getOrgid());
//		tblnbsjProjectVo.setStaffId(loginStaff.getStaffid());
//		if(loginStaff.getTrole()!=null) {
//			tblnbsjProjectVo.setRolename(loginStaff.getTrole().getRname());
//		}
//		
//    	if(pageNumber == null) {
//    		pageNumber = 1;
//    	}
//    	if(pageSize==null) {
//    		pageSize=15;
//    	}
//    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
//    	
//    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
////    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
////    	pageInfo.setCondition(tblNbsjWorkReport);
//    	pageInfo.setPageSize(pageSize);
//    	pageInfo.setCurrentPage(pageNumber);
//    	pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJListByPageInfo(pageInfo,tblnbsjProjectVo));
//    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJCountByPageInfo(pageInfo,tblnbsjProjectVo));
//    	pageInfo.getTotalPage();
//    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
//    	resultMap.put("identifier", identifier);
//    	resultMap.put("pageInfo", pageInfo);
//    	
//    	//==查询当前实施的项目！
//		TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//		if(tnp != null) {
//			BigDecimal projectId = tnp.getProjectId();
//	    	resultMap.put("currProjectId", projectId);
//		}
//		
//    	
//    	return ResponseFormat.retParam(1,200,resultMap);
//	}
//
//
//	@Override
//	public JsonBean projectAdd(TblNbsjProject pj, String token,String planStartDate,String planEndDate,String attids,String pd_dx,String pjTeamJson) throws Exception {
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return ResponseFormat.retParam(0,20006,null);
//		}
//		pj.setPlanStartDate(planStartDate);
//		pj.setPlanEndDate(planEndDate);
//		BigDecimal belongsto ;
//		if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")){
//			belongsto = pj.getAuditStaffId();
//		}else {
//			belongsto = pj.getAuditOrgId();
//		}
//		if(pj.getCospomsordepartment()!=null) {
//			 TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(pj.getCospomsordepartment().toString());
//			 if(bmfzr!=null) {
//				 pj.setCospomsordepartmentstaffid(bmfzr.getStaffid());
//			 }
//		}
//		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		//通过计划id判断，是第一步保存或第二步保存
//		if(pj!=null && null == pj.getPlanId()){
//			TblNbsjAuditplan plan = autoPlanSave(pj, loginStaff,token);
//			if(null != plan && plan.getPlanid()!=null) {
//				resultMap.put("plan",plan);
//				if(null != plan.getPlanid()) {
//					pj.setPlanId(plan.getPlanid());
//				}
//			}
//		}
//		
//		xmlbUpdate(pj, loginStaff, pj.getPmId(), planEndDate, planStartDate, pj.getTempzyId(), pj.getTempId(),
//				pj.getProtempid(), pd_dx, belongsto, pj.getPlanId(),attids,pjTeamJson,token);
//		
//		resultMap.put("WorkReport",pj);
//		return ResponseFormat.retParam(1,200,resultMap);
//	}


	@Override
	public JsonBean projectDelete(BigDecimal projectid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjProject plan = this.tblNbsjProjectMapper.selectById(projectid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		if (plan.getExamineType().equals(TblNbsjProject.EXAMINETYPE1) || plan.getExamineType().equals(0)) {
			this.tblNbsjProjectMapper.deleteById(projectid);
			
			//删除小组
			this.tblNbsjProjectMapper.deleteLinkTeamById(projectid);
			
			//删除模板
			
			return ResponseFormat.retParam(1,200,null);
        } else {
            return ResponseFormat.retParam(0,50001,null);
        }
		
		
	}


	@Override
	public JsonBean findProjectDetail(String token,BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		
		if(null == projectid) {
			TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(1,10001,resultMap);
			}
			
			projectid = tnp.getProjectId();
		}
		
		
		TblNbsjProject plan = this.tblNbsjProjectMapper.selectPJById(projectid);
		
		//Integer aorgid = plan.getAuditOrgId();
		BigDecimal astaffid = plan.getAuditStaffId();
		
		if(null == astaffid) {
			plan.setIsBmAudit(1);
		}else {
			plan.setIsBmAudit(0);
		}
		
		resultMap.put("pj", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	
	@Override
	public JsonBean projectRwfpPageList(String token, Integer pageNumber, Integer pageSize,
			TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
		tblnbsjProjectRwfpVo.setOrgId(t1.getOrgid());
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	tblnbsjProjectRwfpVo.setPmId(loginStaff.getStaffid());
    			
    	com.github.pagehelper.PageInfo<TblNbsjProject> info = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> tblNbsjProjectMapper.findrwList(tblnbsjProjectRwfpVo));
		if (CollectionUtil.isNotEmpty(info.getList())) {
			PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(info);
			resultMap.put("pageInfo", build);
		}
    	
//    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
////    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
////    	pageInfo.setCondition(tblNbsjWorkReport);
//    	pageInfo.setPageSize(pageSize);
//    	pageInfo.setCurrentPage(pageNumber);
//    	pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJRwfpListByPageInfo(pageInfo,tblnbsjProjectRwfpVo));
//    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJRwfpCountByPageInfo(pageInfo,tblnbsjProjectRwfpVo));
//    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
//    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean projectZxylPageList(String token, Integer pageNumber, Integer pageSize,
			TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
		tblnbsjProjectZXYLVo.setOrgId(t1.getOrgid());
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJZxylListByPageInfo(pageInfo,tblnbsjProjectZXYLVo));
    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJZxylCountByPageInfo(pageInfo,tblnbsjProjectZXYLVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean findSSProjectDetail(String token,BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		if(null == projectid) {
			//==查询当前实施的项目！
			TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
			projectid = tnp.getProjectId();
		}
		
		
		TblNbsjProject plan = this.tblNbsjProjectMapper.selectPJById(projectid);
		
		Integer count = this.tblNbsjOperateMapper.getCount(projectid);
		Integer questionCount = this.tblNbsjQuestionMapper.getQuestionCount(projectid);
		Integer staffCount = this.tblNbsjTeamstaffMapper.getCountByProject(projectid);
		
		resultMap.put("pj", plan);
		resultMap.put("sjtrry", staffCount);
		resultMap.put("jcqd", count);
		resultMap.put("fawt", questionCount);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean projectSS(String token, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblNbsjProject project = this.tblNbsjProjectMapper.getById(projectid+"");
		
		
		//当前实施项目
		TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(null != tnp) {
			BigDecimal curr_projectid = tnp.getProjectId();
			if(projectid.equals(curr_projectid)) {
				JsonBean json = new JsonBean(0, "当前项目已实施！", null);
				return json;
			}
		}
		
		if (null != project && null!=project.getUpdateStatus() && project.getUpdateStatus().equals(TblNbsjProject.UPDATEYES)) {
            if (project.getStatus() != null && project.getStatus() == 0) {
                return ResponseFormat.retParam(0,"该项目负责人未启动，不能实施",null);
            } else if (null != project.getStatus() && project.getStatus().equals(3) && project.getStatus().equals(4)) {
                return ResponseFormat.retParam(0,80002,null);
            } else {
            	if (null == project.getImplementTime()) {
                    project.setImplementTime(new Date());
                }
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(loginStaff.getStaffid());
            	this.tblNbsjStaffSelectMapper.insertEntity(loginStaff.getStaffid(),project.getProjectId());
            	this.tblNbsjProjectMapper.updateImplementTime(project.getProjectId(),DateUtil.parseDate(project.getImplementTime(),"yyyy-MM-dd HH:mm:ss"));
        		return ResponseFormat.retParam(1,200,null);
            }
        } else {
        	return ResponseFormat.retParam(0,80003,null);
        }
		
		
	}


	@Override
	public TblNbsjProject getSSProjectDetail(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return null;
		}
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp != null) {
			return tnp;
		}
		
		return  null;
		
	}


	@Override
	public TblNbsjProject getProjectById(BigDecimal projectid) throws Exception {
		TblNbsjProject plan = this.tblNbsjProjectMapper.selectPJById(projectid);
		return plan;
	}


	@Override
	public JsonBean pjPmModi(String token, BigDecimal pmId, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//修改；
		this.tblNbsjProjectMapper.updatePjPm(pmId,projectid);
			
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		resultMap.put("WorkReport",pj);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean pjStart(String token, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//修改；
		this.tblNbsjProjectMapper.updatePjStart(projectid);
			
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		resultMap.put("WorkReport",pj);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean submitProjectArrpoval(String token, BigDecimal projectId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
	    TblCirculation cir = null;
	   try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			TblNbsjProject project = tblNbsjProjectMapper.selectById(projectId);
			if (project == null) {
				return ResponseFormat.retParam(0, 50002, null);
			}
			if (project.getExamineType() != null && project.getExamineType() == 2) {
				return ResponseFormat.retParam(0, 30007, null);
			}
			if (project.getExamineType() != null && project.getExamineType() == 4) {
				return ResponseFormat.retParam(0, 30009, null);
			}
			if (project.getPmId()==null) {
				return ResponseFormat.retParam(0, "项目负责人未填写，无法提交审批", null);
			}
			
			if (project.getTempId()==null) {
				return ResponseFormat.retParam(0, "审计模板未选择，无法及提交审批", null);
			}
			
			if ((project.getExamineType() != null && project.getExamineType() == 3)
					|| (project.getExamineType() != null && project.getExamineType() == 5)
					|| (project.getExamineType() == 6)) {
				resultMap.put("codes", "0");
				resultMap.put("msg", "流程进行中！");
				return ResponseFormat.retParam(0, 30002, resultMap);
			}
			List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(ProcessEnum.SJ_XMGL.name());
			String busType=ProcessEnum.SJ_XMGL.name();
			Integer orgid=Integer.parseInt(user.getLinkOrg().getOrgid().toString());
			List<TblProcessSettingEntity> settings = processSettingMapper.selectByOrgid(busType, orgid);
			String setting=ProcessEnum.SJ_XMGL.name();
			if(settings!=null && settings.size()>0) {
				setting=settings.get(0).getModule();
				list=this.tblProcessAnalysisMapper.getByFlowSetting(setting);
			}
			
			HashMap<String, Object> fields = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				for (TblProcessAnalysis tblAnalysis : list) {
					TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(),projectId.toString());
					if (analysisUser == null) {
						analysisUser = new TblProcessAnalusisUser();
						analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
						analysisUser.setFromid(projectId.toString());
						analysisUser.setSpdate(new Date());
						if (tblAnalysis.getUserid() != null) {
							analysisUser.setStaffid(user.getRealname());
						} else {
							analysisUser.setStaffid(tblAnalysis.getRolename());
						}
                        //项目管理流程参数
                        //提交人参数
                        if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                            fields.put("tcuserid", user.getStaffid().toString());
                           // analysisUser.setStaffid(tblAnalysis.getRolename());
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("bmfzr")) {
                            //部门负责人参数
                            TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                            if (bmfzr == null) {
                                resultMap.put("code", "0");
                                resultMap.put("msg", "部门负责人需配置！");
                               // return resultMap;
                            }
                            fields.put("bmfzr", bmfzr.getStaffid().toString());
                            System.out.println("bmfzr:" + bmfzr.getStaffid());
                        } else {
                            //除此之外的参数都设置为当前提交人
                            if (StrUtil.isNotBlank(tblAnalysis.getUserid())) {
                                fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
                            }
                        }
						this.tblProcessAnalusisUserMapper.insertSetting(analysisUser);
					}else {
						 if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                                fields.put("tcuserid", user.getStaffid().toString());
                            } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("bmfzr")) {
                                //部门负责人参数
                                TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                                if (bmfzr == null) {
                                    resultMap.put("code", "0");
                                    resultMap.put("msg", "部门负责人需配置！");
                                   // return resultMap;
                                }
                                fields.put("bmfzr", bmfzr.getStaffid().toString());
                                System.out.println("bmfzr:" + bmfzr.getStaffid());
                            } else {
                                //除此之外的参数都设置为当前提交人
                                if (StrUtil.isNotBlank(tblAnalysis.getUserid())) {
                                    fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
                                }
                            }
					}
				}
			}
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(fields);
			Map<String, Object> map = HttpClient.startProcessAll(setting, jsonObject.toString());
			String prcessresult = (String) map.get("result");
			String processInstanceId = (String) map.get("processInstanceId");
			String processDefinitionKey = (String) map.get("processDefinitionKey");
			cir = tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_XMSP,project.getProjectCode(),project.getPrjoectName(),TblCirculation.URL_XMSP+project.getProjectId(), user.getStaffid(), processInstanceId, processDefinitionKey,projectId.toString());
			if (prcessresult != null && prcessresult.equals("true")) {
				List<TblMyTask> tasks = HttpClient.findByTask("", user.getStaffid().toString(), 1, 10000);
				if (tasks != null && tasks.size() > 0) {
					for (TblMyTask task : tasks) {
						if (task.getProcessInstanceId().equals(processInstanceId)) {
							Map<String, Object> map1 = HttpClient.handleProcessJson(user.getStaffid().toString(), "通过", task.getTaskId());
							String result = (String) map1.get("result");
							if (result != null && "true".equals(result)) {
								String blande = "";
                                //查询执行人
                                String nextapprover = HttpClient.nextapprover(cir.getBusinesskey());
								TblProcessAnalysis analysis = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, setting);
                                Integer number = 1;
                                String usertaskid = analysis.getUsertaskid();
                                Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                                blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
								TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande,setting);
								TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), projectId.toString());
								task.setFromid(projectId.toString());
								task.setApprover(user.getRealname());
								task.setUsrid(user.getStaffid().toString());
								task.setExamination("提交审批");
								task.setProcessName(setting);
								if (user.getTrole() != null && user.getTrole().getRname() != null) {
									task.setApprovalrole(user.getTrole().getRname());
								}
								task.setApprovaldate(new Date());
								task.setResult("通过");
								task.setCirid(cir.getCyid().toString());
                                if (nextapprover.contains("bmfzr")) {
                                    TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                                    task.setHandle(bmfzr.getRealname());
                                } else {
                                    task.setHandle(nextapprover);
                                }
								task.setAnalid(analysisUser.getAnalid().toString());
								this.tblMytaskMapper.insertMyTaskSetting(task);
							}
						}
					}

				}
				project.setExamineType(TblNbsjProject.EXAMINETYPE2);
				tblNbsjProjectMapper.updateEntity(project);
				resultMap.put("codes", "1");
				resultMap.put("msg", "审批已提交！");
				this.tblCirculationMapper.updateCirculationInfoById(cir);
			} else {
				resultMap.put("codes", "0");
				resultMap.put("msg", "流程提交失败！");
				if(cir != null && cir.getCyid() != null) {
	        		this.tblCirculationMapper.deleteEntityById(cir.getCyid());
	        	}
	        	return ResponseFormat.retParam(0,30002,resultMap);
			}
		} catch (Exception e) {
			resultMap.put("codes", "0");
			resultMap.put("msg", "流程提交失败！");
        	e.printStackTrace();
        	if(cir != null && cir.getCyid() != null) {
        		this.tblCirculationMapper.deleteEntityById(cir.getCyid());
        	}
        	return ResponseFormat.retParam(0,30002,resultMap);
        }	
        /*
         * TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	TblNbsjProject project = tblNbsjProjectMapper.selectById(projectId);
		if(project == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
        TblCirculation cir = new TblCirculation();
        try {
           cir.setCytype(TblCirculation.TYPE_XMSP);
           cir.setCycode(project.getProjectCode());
           cir.setCyname(project.getPrjoectName());
           cir.setCydate(new Date());
           cir.setCystate(TblCirculation.STATE_FQ);
           cir.setCyurl(TblCirculation.URL_XMSP+projectId);
           cir.setCyStaffid(user.getStaffid().toString());
           this.tblCirculationMapper.saveTblCirculation(cir);
           
           Map<String, Object> variables= new HashMap<String, Object>();
           variables.put(ProcessVariableEnum.model.toString(), cir);
           ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_XMGL.name(),cir.getCyid().toString(), variables);
           Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
           cir.setTaskid(task.getId());
           //流程定义Id
           String businessKey = processInstance.getBusinessKey();
           String definitionId = processInstance.getProcessDefinitionId();
           cir.setBusinesskey(businessKey);
           cir.setDefinitionid(definitionId);
           this.tblCirculationMapper.updateCirculationInfoById(cir);
           taskService.complete(task.getId());
           project.setExamineType(TblNbsjProject.EXAMINETYPE2);
           tblNbsjProjectMapper.updateEntity(project);
        } catch (Exception e) {
        	e.printStackTrace();
        	if(cir != null && cir.getCyid() != null) {
        		this.tblCirculationMapper.deleteEntityById(cir.getCyid());
        	}
        	return ResponseFormat.retParam(0,30002,null);
        }	
         * */
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getProjectApprovalInfo(String token, BigDecimal projectId, String taskId,BigDecimal cyId,String v) throws Exception {
     	TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjProject project = tblNbsjProjectMapper.selectById(projectId);
		//TblCirculation cy = this.tblCirculationMapper.selectCiculaInfoById(projectId.toString());
		TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
		List<TblMyTask> list = null;
        if (staff.getTrole() != null && staff.getTrole().getRname() != null) {
        	resultMap.put("rolename", staff.getTrole().getRname());
            list = HttpClient.findByTask(staff.getTrole().getRname(), staff.getStaffid().toString(), 1, 10000);
        } else {
            list = HttpClient.findByTask("", staff.getStaffid().toString(), 1, 10000);
        }
        if (list != null && list.size() > 0) {
            Map<String, Object> map = null;
            String resultData = null;
            String[] results = null;
            for (TblMyTask myTask : list) {
                if (cy != null && myTask.getProcessInstanceId().equals(cy.getBusinesskey())) {
                    map = HttpClient.lczxProcessJson(myTask.getTaskId());
                    resultData = (String) map.get("data");
                    results = resultData.replace("\"", "").split(",");
                    if (results.length > 0 && !results[0].equals("")) {
                        List<String> stringB = Arrays.asList(results);
                        resultMap.put("btnList", stringB);
                        resultMap.put("number", stringB.size());
                    } else {
                    	resultMap.put("btnList", null);
                    	resultMap.put("number", 0);
                    }
                    resultMap.put("task", myTask);
                    break;
                }
            }

        }
        
        resultMap.put("cy", cy);
        if(null != staff.getRoleNames()) {
        	if(staff.getRoleNames().indexOf("部门负责人")>=0) {
        		resultMap.put("is_bmfzr", "1");
            }
        }
        List<TblMyTask> taskList = this.tblMytaskMapper.selectByFormId(projectId.toString());
        resultMap.put("cz", "sp");
    	resultMap.put("cyId", cyId);
		resultMap.put("taskList", taskList);
		resultMap.put("url", HttpClient.jkurl + cy.getBusinesskey());
		resultMap.put("project", project);
		resultMap.put("v", v);
     	/*
     	 * TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		
    	TblNbsjProject project = tblNbsjProjectMapper.selectById(projectId);
        List<String> btnList = null;
    	if(project!=null){
    		if(StringUtils.isNotBlank(taskId) && project.getExamineType() != TblNbsjProject.EXAMINETYPE5){
    			btnList = processService.getButtonsForTransition(taskId);
    			resultMap.put("btnList", btnList);
    		}
    	
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(projectId.toString(),cyId);
    		if (cyId != null){
    			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
    			resultMap.put("cy", cy);
    		}
    		
    		resultMap.put("taskId", taskId);
    		resultMap.put("aoptionList", ao);
    		resultMap.put("cyId", cyId);
    		resultMap.put("project", project);
    	}
     	 * */
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dealProjectApporvalInfo(String token, BigDecimal cyId, String taskId, String transition,
			String optDesc, String projectId,String processDefinitionId,String processInstanceId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		if(StringUtils.isBlank(projectId)){
			return ResponseFormat.retParam(0,20006,null);
		}
		JsonBean jsonBean = null;
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		try {
			TblNbsjProject project = tblNbsjProjectMapper.selectById(Integer.valueOf(projectId));
			if(taskId!=null && !taskId.equals("")){
				Map<String, Object> map = HttpClient.handleProcessJson(staff.getStaffid().toString(),transition,taskId);
				String object = (String) map.get("result");
				if(object!=null && object.equals("true")){
					TblMyTask task=new TblMyTask();
					TblMyTask oldtask = tblMyTaskService.findOndbyFrom(projectId);
					TblProcessAnalysis findOnd = null;
					if(oldtask==null){
						findOnd = tblProcessAnalysisService.findOndBytakdid("");
					}else{
						findOnd= tblProcessAnalysisService.findOnd(oldtask.getAnalid());
					}
					Integer number=1;
					String usertaskid = findOnd.getUsertaskid();
					Integer num=Integer.parseInt(usertaskid.substring(usertaskid.length()-1,usertaskid.length()))+number;
					String blande=usertaskid.substring(0,usertaskid.length()-1)+num;
					TblProcessAnalysis analysis =null;
					if(transition!=null && "退回".equals(transition)){
						analysis = tblProcessAnalysisService.findOndBytakdidstart("",findOnd.getProcessname());
					}else{
						analysis = tblProcessAnalysisService.findOndBytakdidAnId(blande,findOnd.getProcessname());
					}
					TblProcessAnalusisUser analysisUser = null;
					if(analysis!=null && analysis.getAnalid()!=null){
						analysisUser = tblProcessAnalusisUserService.findOnd(analysis.getAnalid().toString(), projectId);
					}
					TblCirculation circulation = tblCirculationMapper.getOneBytaskid(projectId);
					//查询执行人 
					String nextapprover = HttpClient.nextapprover(circulation.getBusinesskey());
					System.out.println("nextapprover===================:"+nextapprover);
					
					task.setApprovaldate(new Date());
					if(staff.getTrole()!=null && staff.getTrole().getRname()!=null){
						task.setApprovalrole(staff.getTrole().getRname());
					}
					task.setApprover(staff.getRealname());
					task.setExamination(optDesc);
					task.setFromid(projectId.toString());
					task.setProcessDefinitionId(processDefinitionId);
					task.setUsrid(staff.getStaffid().toString());
					task.setCirid(cyId.toString());
					task.setResult(transition);
					task.setProcessName(oldtask.getProcessName());
					task.setTaskId(taskId);
					task.setProcessInstanceId(processInstanceId);
					if(transition.equals("完成")){
						task.setHandle("无");
						circulation.setCystate("已完成");
						project.setExamineType(TblNbsjProject.EXAMINETYPE4);
					}else{
						TblStaff findById = tblStaffMapper.getById(circulation.getCyStaffid().toString());//表单提交人
						if(nextapprover.contains("bmfzr")) {
							TblStaff bmfzr = tblStaffMapper.findByJobName("部门负责人", findById.getOrgid().toString());
							task.setHandle(bmfzr.getRealname());
						}else if(nextapprover.contains("fgld")) {
							//分管领导参数
							TblStaff fgld = tblStaffMapper.findByStaffManOrgs(findById.getOrgid().toString());
							task.setHandle(fgld.getRealname());
						}else if(nextapprover.contains("tcuserid")) {
							//退回到创建人
							task.setHandle(findById.getRealname());
						}else {
							task.setHandle(nextapprover);//角色
						}
						task.setAnalid(analysis.getAnalid().toString());
					}
					
					if (project.getExamineType() == 5) {
						project.setExamineType(TblNbsjProject.EXAMINETYPE2);
						circulation.setCystate("审批中");
					}
					if(transition!=null && transition.equals("退回")){
						circulation.setCystate("需调整");
						project.setExamineType(TblNbsjProject.EXAMINETYPE5);
					}
					
//					if(transition!=null && transition.equals("终止")){
//						circulation.setCystate("终止");
//						task.setHandle("无");
//					}
					tblMyTaskService.insertMyTaskSetting(task);
					tblCirculationMapper.updateCirculationInfoById(circulation);
					tblNbsjProjectMapper.updateEntity(project);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			 return ResponseFormat.retParam(0,3002,null);
		}
		/*if (null != cy) {
		Map<String, Object> variables = new HashMap<>();
		variables.put(ProcessVariableEnum.model.toString(), cy);
		if (StringUtils.isNotBlank(transition)) {
			variables.put(ProcessVariableEnum.transition.toString(), transition);
		}
		try {
			taskService.claim(taskId, staff.getStaffid().toString());
			taskService.complete(taskId, variables);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,30002,null);
		}
		//添加审批意见
		TblAuditOption opt = new TblAuditOption();
		opt.setCyid(cy.getCyid());
		opt.setOptStaffid(staff.getStaffid());
		opt.setStaffidName(staff.getRealname());
		opt.setOptDesc(optDesc);
		opt.setRelationId(new BigDecimal(projectId));
		opt.setOptState(transition);
		jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
	}
	jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
	*/
		 return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean jsfpRoleManageSave(String token, String ids, BigDecimal teamId, BigDecimal projectId) throws Exception {
		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);//this.tblnbsjProjectService.getId(projectId);
        TblNbsjTeamstaffEntity teamStaff = this.tblNbsjTeamstaffMapper.selectById(teamId); 
        List<TblNbsjAuthorizationEntity> list = new ArrayList<TblNbsjAuthorizationEntity>();
        List<TblNbsjAuthorizationEntity> updatelist = new ArrayList<TblNbsjAuthorizationEntity>();
        
        TblStaffUtil staff = userProvider.get(); 
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblStaff tblStaff = new TblStaff();
		tblStaff.setStaffid(staff.getStaffid());
        
        if (null != project && null != teamStaff) {
            String[] str_ids = ids.split(",");
            for (String str : str_ids) {
            	
                TblAduitProGramEntity aduitProGram = this.tblAduitProGramMapper.selectById(new BigDecimal(str));
//            	TblAduitProGramEntity aduitProGram = this.tblAduitProGramMapper.selectById(Integer.valueOf(str));
//                TblNbsjAuthorizationEntity authorization = this.tblNbsjAuthorizationService.get(new BigDecimal(projectId), aduitProGram.getProgramId());、
            	TblNbsjAuthorizationEntity authorization = this.tblNbsjAuthorizationService.get(projectId, aduitProGram.getProgramId());
				if (null != authorization) {
                    authorization.setAduitProGram(aduitProGram);
                    authorization.setAuthTime(new Date());
                    authorization.setProject(project);
                    authorization.setTeamStaff(teamStaff);
                    authorization.setAuthStaff(tblStaff);
                    updatelist.add(authorization);
                } else {
                    authorization = new TblNbsjAuthorizationEntity();
                    authorization.setAduitProGram(aduitProGram);
                    authorization.setAuthTime(new Date());
                    authorization.setProject(project);
                    authorization.setTeamStaff(teamStaff);
                    authorization.setAuthStaff(tblStaff);
                    list.add(authorization);
                }
            }
            this.tblNbsjAuthorizationService.merge(updatelist);
            this.tblNbsjAuthorizationService.save(list);
//            this.tblNbsjProjectMapper.updateEntity(project);
//            tblnbsjProjectService.update(project);
            //全部分配完成
			BigDecimal tempId = project.getTbltemplete().getTempleteId();//审计模板id
        	List<TblAduitProGramEntity> listTap = this.tblAduitProGramMapper.findByTMId(tempId);
        	boolean is = false;
        	if (listTap.size()>=0){
        			List<TblNbsjAuthorizationEntity> byProjectId = tblNbsjAuthorizationService.getByProjectId(projectId);
        			if (byProjectId.size()== listTap.size()) {
        				is = true;
        			}else {
        				is = false;
        			}
        	}
        	if(is==true) {
                //项目分配中
//                project.setFpStatus(2);//已分配
                this.tblNbsjProjectMapper.updateFpStatus(2,projectId);
        	}else {
                //项目分配中
//                project.setFpStatus(1);//分配中
                this.tblNbsjProjectMapper.updateFpStatus(1,projectId);
        	}
            
            Map<String,Object> resultMap = new HashMap<String,Object>(0);
            return ResponseFormat.retParam(1,200,resultMap);
        }
        return ResponseFormat.retParam(0,30001,null);
	}

	@Override
	public JsonBean auditPlanListPlanIdIn(String token, Integer pageNumber, Integer pageSize,String projectname) throws Exception {
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
	     
	     BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
	     
//	     TblNbsjProject sspj = getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
	     
	     Map<String,Object> resultMap = new HashMap<String,Object>(0);
	     
	     /*com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
//	     tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//	     pageInfo.setCondition(tblNbsjSheet);
	     pageInfo.setPageSize(pageSize);
	     pageInfo.setCurrentPage(pageNumber);
	     pageInfo.setTlist(this.tblNbsjProjectMapper.selectAuditPlanListByPageInfo(pageInfo,projectname,orgid));
	     pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectAuditPlanCountByPageInfo(pageInfo,projectname,orgid));
	     pageInfo.getTotalPage();*/
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.selectAuditPlanListByPageInfoXml(projectname,orgid));
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", build);
	     return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean auditPlanInAdd(String ids, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		if (StringUtils.isNotBlank(ids)) {
            String[] id = ids.split(",");
            
            TblNbsjProject selectProject = getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            
            if (null != selectProject) {
                for (String string : id) {
                	TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(new BigDecimal(string));
                	
                    if (null != project) {
                        TblNbsjProjectDataEntity data = new TblNbsjProjectDataEntity();
                        data.setOldProjectId(project.getProjectId());
                        data.setProjectid(selectProject.getProjectId());
//                        this.tblNbsjProjectDataService.save(data);
                        
                        this.tblNbsjProjectDataMapper.saveOld(data);
                    }
                }
                return ResponseFormat.retParam(1,200,resultMap);
            }
            return ResponseFormat.retParam(0,30001,resultMap);
//            return JsonBean.error("未找到实施项目");
        }
		return ResponseFormat.retParam(0,30001,resultMap);
//        return JsonBean.error("导入失败");
        
	}
	@Override
	public JsonBean getGkProjectInfo(String token, Integer pageNumber, Integer pageSize, TblGkProjectVo project) throws Exception {
    	
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
    /*	project.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
    	PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(tblNbsjProjectMapper.getGkProjectInfo(pageInfo,project));
    	pageInfo.setTotalRecord(tblNbsjProjectMapper.getGkProjectInfoListCount(project));
    	pageInfo.getTotalPage();*/
		project.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		//  SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE &lt; 100 START WITH ORGID = #{question.ORGID} CONNECT BY PRIOR ORGID = FATHERORGID
		/**
		 * 返回当前公司所有的下属公司子集ID以及部门
		 */
		String strs = selectFatherIdStrsByChidrenOrgId(project.getORGID());
		
		//安全保密SQL
		String secrectSql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TP.ORGID", "TP.ORGID",
				"TP.CREATESTAFFID", "TP.SECRECTLEVELID", "TP.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
				
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.getGkProjectInfoXml(project,strs,loginStaff,secrectSql));
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
    	resultMap.put("pageInfo", build);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	public String selectFatherIdStrsByChidrenOrgId(String orgid) throws Exception {

		List<String> orgIdList = this.tblOrganizationMapper.selectFatherIdStrsByChidrenOrgId(orgid);
		if(orgIdList == null || orgIdList.size() == 0){
			return orgid;
		}
		String orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenFatherIdStrsByOrgIds(orgIdIdStrs,orgid+","+orgIdIdStrs);
	}

	private String getChidrenFatherIdStrsByOrgIds(String orgIdIdStrs, String totalIds) throws Exception {
		List<String> orgIdList = this.tblOrganizationMapper.selectFatherIdStrsByChidrenOrgId(orgIdIdStrs);
		if(orgIdList == null || orgIdList.size() == 0){
			return totalIds;
		}
		orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenFatherIdStrsByOrgIds(orgIdIdStrs,totalIds+","+orgIdIdStrs);
	}

	//返回当前公司所有的下属公司子集ID
	public String selectChidrenIdStrsByFatherOrgId(String orgId) throws Exception {

		List<String> orgIdList = this.tblOrganizationMapper.selectChildrenIdListByFatherOrgId(orgId);
		if(orgIdList == null || orgIdList.size() == 0){
			return orgId;
		}
		String orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenIdStrsByFatherOrgIds(orgIdIdStrs,orgId+","+orgIdIdStrs);
	}

	private String getChidrenIdStrsByFatherOrgIds(String orgIdIdStrs, String totalIds) throws Exception {
		List<String> orgIdList = this.tblOrganizationMapper.selectChildrenIdListByFatherOrgId(orgIdIdStrs);
		if(orgIdList == null || orgIdList.size() == 0){
			return totalIds;
		}
		orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenIdStrsByFatherOrgIds(orgIdIdStrs,totalIds+","+orgIdIdStrs);
	}


	@Override
	public JsonBean getGkQuestionInfo(String token, Integer pageNumber, Integer pageSize, TblGkQuestionVo question) throws Exception {
		
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
	/*	question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(tblNbsjProjectMapper.getGkQuestionInfoList(pageInfo,question));
		pageInfo.setTotalRecord(tblNbsjProjectMapper.getGkQuestionInfoListCount(question));
		pageInfo.getTotalPage();*/
		question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		/**
		 * 返回当前公司所有的下属公司子集ID以及部门
		 */
		String strs = selectFatherIdStrsByChidrenOrgId(question.getORGID());
		
		//安全保密SQL
		String secrectSql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TP.ORGID", "TP.ORGID", "TP.CREATESTAFFID", "TP.SECRECTLEVELID", "TP.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
		
		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.getGkQuestionInfoListXml(question,strs,loginStaff,secrectSql));
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean getGkZgContentInfo(String token, Integer pageNumber, Integer pageSize, TblGkZgQuestionVo question) throws Exception {
		
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
		/*question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(tblNbsjProjectMapper.getGkZgContentInfoList(pageInfo,question));
		pageInfo.setTotalRecord(tblNbsjProjectMapper.getGkZgContentInfoListCount(question));
		pageInfo.getTotalPage();*/

		question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		/**
		 * 返回当前公司所有的下属公司子集ID
		 */
		String strs = selectChidrenIdStrsByFatherOrgId(question.getORGID());
		//安全保密SQL
		String secrectSql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TP.ORGID", "TP.ORGID", "TP.CREATESTAFFID",
				"TP.SECRECTLEVELID", "TP.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());

		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblNbsjProjectMapper.getGkZgContentInfoListXml(question,strs,loginStaff,secrectSql));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	private TblNbsjAuditplan autoPlanSave(TblNbsjProject project,TblStaffUtil loginStaff,String token) throws Exception {
		TblNbsjAuditplan planone=new TblNbsjAuditplan();
		
		String pjType = project.getProjecttype();
		
		if(project.getProjecttype()!=null && project.getProjecttype().equals("计划内")) {
			List<TblNbsjAuditplan> list=tblNbsjAuditplanMapper.selectPlanListByyear(project.getPlanYear(), "年度计划",loginStaff.getLinkOrg().getOrgid());
			if(list!=null && list.size()>0) {
				planone=list.get(0);
				return planone;
			}else {
				//没有查到计划，则自动生成计划
//				//计划编号  自动获取
//				JsonBean autoNumber = this.tblAutonoNumberService.findFlowNextId("TBL_NBSJ_AUDITPLAN", "PLANCODE", "AUDITORGID", 274,null, null, null,token);
//				Object autoData = autoNumber.getData();
//				if(null != autoData) {
//					Map<String,Object> autoMap = (Map<String, Object>) autoData;
//					String planCode = (String) autoMap.get("data");
//					planone.setPlancode(planCode);
//				}else {
//					//自动编号生成异常！！！
//				}
				//计划编号
				String audittype = project.getAuditType();
				TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjTypeByName(audittype,loginStaff.getLinkDetp().getOrgid()+"");
//				TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjType(audittype);
				String auditCode = "";
				if(null != tblNbsjType) {
					auditCode = tblNbsjType.getAuditCode()+"";
				}
				String planCode = project.getPlanYear()+"-"+loginStaff.getLinkOrg().getOrgnumber()+"-";//+auditCode+"-"
				//查询自增数据
				String no = this.tblNbsjAuditplanMapper.selectMaxPlanCode(planCode);
				if(no != null) {
//					 no = no.replace(planCode, "");
				}else {
					no = "0";
				}
				planCode += (Integer.parseInt(no)+1);
				//年度—公司编号—审计类型编号—自增数据
				planone.setPlancode(planCode);
				
				//计划名称//yyyy年度计划
				planone.setPlanname(project.getPlanYear()+"年度计划");
				//计划年度
				planone.setPalnyear(project.getPlanYear());
				//计划类别
				planone.setPlantype("年度计划");
				//计划时间begin-end
				planone.setStarttime(project.getStartDate());
				planone.setEndtime(project.getEndDate());
				//编制人、编制日期、审计单位
				planone.setCreatestaffid(loginStaff.getStaffid());
				planone.setCreatetime(new Date());
				planone.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
				//保存计划
				this.tblNbsjAuditplanMapper.insertEntity(planone);
				
				//查询
//				List<TblNbsjAuditplan> reclist=tblNbsjAuditplanMapper.selectPlanListByyear(project.getPlanYear(), "年度计划");
				return planone;
			}
		}else {
//			//计划外-单独生成一个 计划外项目
//			//计划编号
//			String audittype = project.getAuditType();
//			TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjTypeByName(audittype,loginStaff.getLinkDetp().getOrgid()+"");
////			TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjType(audittype);
//			String auditCode = "";
//			if(null != tblNbsjType) {
//				auditCode = tblNbsjType.getAuditCode()+"";
//			}
//			
//			String planCode = project.getPlanYear()+"-"+loginStaff.getLinkOrg().getOrgnumber()+"-"+auditCode+"-";
//			//查询自增数据
//			String no = "0";
//			try {
//				no = this.tblNbsjAuditplanMapper.selectMaxPlanCode(planCode);
//			} catch (Exception e) {
//				no = "0";
//			}
//			if(no != null) {
////				 no = no.replace(planCode, "");
//			}else {
//				no = "0";
//			}
//			planCode += (Integer.parseInt(no)+1);
//			//年度—公司编号—审计类型编号—自增数据
//			planone.setPlancode(planCode);
//			
//			//计划名称
//			planone.setPlanname("计划外项目");
//			//计划年度
//			planone.setPalnyear(project.getPlanYear());
//			//计划类别
//			planone.setPlantype("计划外项目");
//			//计划时间begin-end
//			planone.setStarttime(project.getStartDate());
//			planone.setEndtime(project.getEndDate());
//			//编制人、编制日期、审计单位
//			planone.setCreatestaffid(loginStaff.getStaffid());
//			planone.setCreatetime(new Date());
//			planone.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//			//保存计划
//			//this.tblNbsjAuditplanMapper.insertEntity(planone);
//			
//			return planone;
		}
		return planone;
	}
	
	
//	private TblNbsjProject xmlbUpdate(TblNbsjProject project,TblStaffUtil loginStaff,BigDecimal pmid,String enddate,String startdate,BigDecimal tempzyid,
//			BigDecimal tempid,String protempid,String pd_dx,BigDecimal belongsto,BigDecimal plan,String attids,String pjTeamJson,String token ) throws Exception {
//		
//		TblOrganization organization1 = new TblOrganization();
//		organization1.setOrgid(loginStaff.getCurrentOrg().getOrgid());
//        
//        if (project.getProjectId() != null) {
//			// 修改开始
//            TblNbsjProject tblnbsjProject = this.tblNbsjProjectMapper.selectPJById(project.getProjectId());//tblnbsjProjectService.getId(project.getProjectid().toString());
//            try {
//            	tblnbsjProject.setPlanStartDate(startdate);
//            	tblnbsjProject.setPlanEndDate(enddate);
//            	 if (startdate != null && startdate.length() > 0) {
//                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                     tblnbsjProject.setStartDate(sdf.parse(startdate));
//                 }
//                 if (enddate != null && enddate.length() > 0) {
//                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                     tblnbsjProject.setEndDate(sdf.parse(enddate));
//                 }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            tblnbsjProject.setOrgIds(project.getOrgIds());
//            tblnbsjProject.setOrgIdNames(project.getOrgIdNames());
//            tblnbsjProject.setAuditType(project.getAuditType());
//            tblnbsjProject.setPlanYear(project.getPlanYear());
//            tblnbsjProject.setCosts(project.getCosts());
//            tblnbsjProject.setPrjoectName(project.getPrjoectName());
//            tblnbsjProject.setProjectSource(project.getProjectSource());
//            tblnbsjProject.setPurpose(project.getPurpose());
//            tblnbsjProject.setScopes(project.getScopes());
//            tblnbsjProject.setPursuant(project.getPursuant());
//            tblnbsjProject.setComments(project.getComments());
//            tblnbsjProject.setProDesc(project.getProDesc());
//            tblnbsjProject.setProSjfs(project.getProSjfs());
//            tblnbsjProject.setImplementaion(project.getImplementaion());
//            tblnbsjProject.setAuditrequirements(project.getAuditrequirements());
//            tblnbsjProject.setCospomsordepartment(project.getCospomsordepartment());
//            tblnbsjProject.setImplementaionsteps(project.getImplementaionsteps());
//            tblnbsjProject.setProjecttype(project.getProjecttype());
//            tblnbsjProject.setExternAlassig(project.getExternAlassig());
//            tblnbsjProject.setSjap(project.getSjap());
//            tblnbsjProject.setXmgs(project.getXmgs());
//            tblnbsjProject.setCospomsordepartmentstaffid(project.getCospomsordepartmentstaffid());
//            tblnbsjProject.setCntType(project.getCntType());
//            
//            if(null!=tempzyid){
////                TblNbsjTempleteEntity templetezy = tblNbsjTempleteService.get(new BigDecimal(tempzyid));//指引模板
//                TblNbsjTempleteEntity templetezy = tblNbsjTempleteMapper.findbyid(tempzyid+"");//指引模板
//                tblnbsjProject.setTempzyId(tempzyid);//setTbltempletezy(templetezy);
//            }
//            tblnbsjProject.setCreateTime(new Date());
//            tblnbsjProject.setUpdateStatus(TblNbsjProject.UPDATEYES);
//            if(tblnbsjProject.getExamineType()!=null){tblnbsjProject.setExamineType(project.getExamineType());}
//            if(pmid!=null && pmid.toString().trim().length()>0) {
//            	 tblnbsjProject.setPmId(pmid);
//            }
//			//修改审计模板
//			if (tempid!=null) { 
//				// 如果审计模板再次修改
//				//if(!tempid.equals(protempid)){
//					TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(tempid+"");//tblNbsjTempleteService.get(new BigDecimal(tempid));
//					TblNbsjTempleteEntity temp = new TblNbsjTempleteEntity();
//					temp.setTempType("3");
//					temp.setCreateDate(templete.getCreateDate());
//					temp.setTempleteCode(templete.getTempleteCode());
//					temp.setTempleteDesc(templete.getTempleteDesc());
//					Set<TblOrganization> orgs = templete.getOrganizations();
//					if (orgs != null && orgs.size() > 0) {
//						for (TblOrganization org : orgs) {
//							temp.getOrganizations().add(org);
//						}
//					}
//					temp.setStaffId(templete.getStaffId());
//					temp.setTempleteType(templete.getTempleteType());
//					temp.setUpdateStaffId(templete.getUpdateStaffId());
//					temp.setUpdateDate(templete.getUpdateDate());
//					temp.setStatus(templete.getStatus());
//					temp.setTempleteName(templete.getTempleteName());
////					tblNbsjTempleteService.save(temp);
//				    temp.setTempleteId(RandomUtil.uuBigDecimalId());
//					tblNbsjTempleteMapper.insert(temp);
//					tblnbsjProject.setTempId(temp.getTempleteId());
//					tblnbsjProject.setOrgInfo(organization1);
//				    tblnbsjProject.setOrgId(loginStaff.getCurrentOrg().getOrgid());
//					// if(!tempid.equals(tblnbsjProject.getTbltemplete().getTempleteId().toString())){
//					List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(tempid+"");
//					if (list != null && list.size() > 0) {
//						for (TblTargetTypeEntity tblTargetType : list) {
//							TblTargetTypeEntity target = new TblTargetTypeEntity();
//							target.setCreateTime(tblTargetType.getCreateTime());
//							target.setNbsjTemplete(temp);
//							target.setParentId(tblTargetType.getParentId());
//							target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);//TEMP_NUMBER 0 ;ZY_NUMBER 1
//							target.setTargetDesc(tblTargetType.getTargetDesc());
//							target.setTargetName(tblTargetType.getTargetName());
//							target.setTargetId(RandomUtil.uuBigDecimalId());
//							target.setTempId(temp.getTempleteId());
////							tblTargetTypeService.save(target);
//							tblTargetTypeMapper.insert(target);
//							List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
//							if (grams != null && grams.size() > 0) {
//								for (TblAduitProGramEntity gram : grams) {
//									TblAduitProGramEntity prog = new TblAduitProGramEntity();
//									prog.setBioData(gram.getBioData());
//									prog.setTargetId(target.getTargetId());
//									prog.setControl(gram.getControl());
//									prog.setBusinessType(gram.getBusinessType());
//									prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
//									prog.setCreateTime(gram.getCreateTime());
//									prog.setNbsjTemplete(temp);
//									prog.setTempId(temp.getTempleteId());
//									prog.setRiskPoint(gram.getRiskPoint());
//									prog.setRiskSource(gram.getRiskSource());
//									prog.setSuditProcess(gram.getSuditProcess());
//									prog.setUpdateTime(gram.getUpdateTime());
//									prog.setProgramId(RandomUtil.uuBigDecimalId());
////									tblAduitProGramService.save(prog);
//									tblAduitProGramMapper.insert(prog);
//								}
//							}
//							capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
//						}
//					}
//					//删除之前分配的任务
//					//删除任务
//					
//						 List<TblNbsjAuthorizationEntity> nbsjauthorizatoin = this.tblNbsjAuthorizationService.getByProjectId(project.getProjectId());
//						 if(null!=nbsjauthorizatoin&&nbsjauthorizatoin.size()>0) {
//							 for (TblNbsjAuthorizationEntity tblNbsjAuthorization : nbsjauthorizatoin) {
//								 if(null!=nbsjauthorizatoin) {
//									 List<TblNbsjOperateEntity> listoper = tblNbsjOperateMapper.findByAuthId(tblNbsjAuthorization.getAuthId());//tblNbsjOperateService.findByAuthId(tblNbsjAuthorization.getAuthId());
//									 TblNbsjOperateEntity oper = null;
//									 if(listoper!=null && listoper.size()>0){
//										 oper = listoper.get(0);
//										}
//									 tblNbsjOperateMapper.deleteByOpid(oper.getOperateid());
////									 tblNbsjOperateService.delete(oper);
////									 this.tblNbsjAuthorizationService.delete(tblNbsjAuthorization);
//									 this.tblNbsjAuthorizationMapper.deleteByAuthid(tblNbsjAuthorization.getAuthId());
//								 }
//							 }
//						 }
//					//一是已经是在项目里的，已经复制出来的，可以删除
//					//二是没在项目里的，没复制出来的，不能删除
//					if(protempid!=null && StringUtils.isNotEmpty(protempid)) {
//						tblAduitProGramService.deleteZy(protempid);
//						tblTargetTypeService.deleteByZY(protempid);
//						tblNbsjTempleteService.delete(new BigDecimal(protempid));
//					}
//				//}
//			}
//            
//            if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")){
//                tblnbsjProject.setAuditStaffId(belongsto);//.setTblnbsjstaffs(staff);
//                tblnbsjProject.setAuditOrgId(null);
//            }else {
//            	tblnbsjProject.setAuditStaffId(null);
//            }
//            //被审计单位
//            
//            if(null != plan) {
//                 TblNbsjAuditplan jplan = tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(plan);//tblnbsjauditPlanService.get(plan);
//                 tblnbsjProject.setPlanId(plan);//etTblnbsjPlan(jplan);
//            }
//            
//            tblNbsjProjectMapper.updateById(tblnbsjProject);
//            
//            BigDecimal projectId = project.getProjectId();
//            
//          //保存附件
//            this.tblAttachmentMapper.deleteAttmentRelationProject(projectId);
//            if (StringUtils.isNotBlank(attids)){
//            	String[] ids = attids.split(",");
//            	for (int i = 0; i < ids.length; i++) {
//            		String id = ids[i].trim();
//        			this.tblAttachmentMapper.insertAttmentRelationProject(id, projectId);
//            	}
//            }
//            
//            //==项目小组
//    		if(null != pjTeamJson && !"".equals(pjTeamJson.trim())) {
//    			List<TblNbsjProjectTeamEntity> srList = new ArrayList<TblNbsjProjectTeamEntity>();
//    			srList = JSONObject.parseArray(pjTeamJson,TblNbsjProjectTeamEntity.class);
//    			for (TblNbsjProjectTeamEntity pjTeam : srList) {
//    				//
//    				String zystaffids = pjTeam.getZystaffids();
//					BigDecimal leaderid = pjTeam.getLeaderId();
////    				Integer projectid = pjTeam.getProjectid();
//					this.tblNbsjProjectteamService.pjItemAdd(token,pjTeam,zystaffids,leaderid,projectId);
//    			}
//    		}
//            
//            return tblnbsjProject;
//        }else {
//        	//新建项目
//            try {
//                if (startdate != null && startdate.length() > 0) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    project.setStartDate(sdf.parse(startdate));
//                }
//                if (enddate != null && enddate.length() > 0) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    project.setEndDate(sdf.parse(enddate));
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            
//            if(null != plan) {
////            	 Integer planid = Integer.valueOf(plan);
//                 TblNbsjAuditplan jplan = tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(plan);//tblnbsjauditPlanService.get(plan);
//                 project.setPlanId(plan);//setTblnbsjPlan(jplan);
//            }
//            TblStaff pm = new TblStaff();
//           
//            project.setOrgInfo(organization1);//.setTblorg(organization1);
//			project.setOrgId(loginStaff.getCurrentOrg().getOrgid());
//
//			//第一次新增审计模板
//			if (tempid!=null) {
//				//选择的方案模板(来自方案模板)
//				TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(tempid+"");
//				//复制审计模板
//				TblNbsjTempleteEntity temp = new TblNbsjTempleteEntity();
//				temp.setTempType("3");
//				temp.setCreateDate(templete.getCreateDate());
//				temp.setTempleteCode(templete.getTempleteCode());
//				temp.setTempleteDesc(templete.getTempleteDesc());
//				Set<TblOrganization> orgs = templete.getOrganizations();
//				if (orgs != null && orgs.size() > 0) {
//					for (TblOrganization org : orgs) {
//						temp.getOrganizations().add(org);
//					}
//				}
//				temp.setStaffId(templete.getStaffId());
//				temp.setTempleteType(templete.getTempleteType());
//				temp.setUpdateStaffId(templete.getUpdateStaffId());
//				temp.setUpdateDate(templete.getUpdateDate());
//				temp.setStatus(templete.getStatus());
//				temp.setTempleteName(templete.getTempleteName());
//				temp.setTempleteId(RandomUtil.uuBigDecimalId());
//				temp.setOrgId(templete.getOrgId());
//				tblNbsjTempleteMapper.insert(temp);
//				
//				project.setTempId(temp.getTempleteId());
//				if (null != tempzyid) {
//					TblNbsjTempleteEntity templetezy = tblNbsjTempleteMapper.findbyid(tempzyid+"");
//					project.setTempzyId(tempzyid);//setTbltempletezy(templetezy);
//				}
//
//				List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(tempid+"");
//				if (list != null && list.size() > 0) {
//					for (TblTargetTypeEntity tblTargetType : list) {
//						TblTargetTypeEntity target = new TblTargetTypeEntity();
//						target.setCreateTime(tblTargetType.getCreateTime());
//						target.setNbsjTemplete(temp);
//						target.setParentId(tblTargetType.getParentId());
//						target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);
//						target.setTargetDesc(tblTargetType.getTargetDesc());
//						target.setTargetName(tblTargetType.getTargetName());
//						target.setTargetId(RandomUtil.uuBigDecimalId());
//						target.setTempId(temp.getTempleteId());
//						tblTargetTypeMapper.insert(target);
//						List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
//						if (grams != null && grams.size() > 0) {
//							for (TblAduitProGramEntity gram : grams) {
//								TblAduitProGramEntity prog = new TblAduitProGramEntity();
//								prog.setBioData(gram.getBioData());
//								prog.setTargetId(target.getTargetId());
//								prog.setControl(gram.getControl());
//								prog.setBusinessType(gram.getBusinessType());
//								prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
//								prog.setCreateTime(gram.getCreateTime());
//								prog.setTempId(temp.getTempleteId());
//								prog.setRiskPoint(gram.getRiskPoint());
//								prog.setRiskSource(gram.getRiskSource());
//								prog.setSuditProcess(gram.getSuditProcess());
//								prog.setUpdateTime(gram.getUpdateTime());
//								prog.setProgramId(RandomUtil.uuBigDecimalId());
////								tblAduitProGramService.save(prog);
//								tblAduitProGramMapper.insert(prog);
//							}
//						}
//						capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
//					}
//				}
//			}
//			if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")){
//				project.setAuditStaffId(belongsto);//.setTblnbsjstaffs(staff);
//	          }else {
//	        	 // project.setAuditOrgId(Integer.valueOf(belongsto));
//	          }
//			
//            //被审计单位
//            project.setStatus(0);
//            project.setFpStatus(0);//项目任务分配状态
//            project.setAssigbedControlTime(new Date());
//            project.setAssigbedumpeTime(new Date());
//            project.setAssigbedpmTime(new Date());
//            project.setCreateTime(new Date());
//            project.setUpdateStatus(TblNbsjProject.UPDATEYES);
////            TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
//            project.setCreateStaffId(loginStaff.getStaffid());//setTblcreater(user);
//            project.setCyrrentStatre(TblNbsjProject.NO_SELECT);
//            project.setOrgId(loginStaff.getCurrentOrg().getOrgid());
//            
//            //项目编号
//            String audittype = project.getAuditType();
//            TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjTypeByName(audittype,loginStaff.getLinkDetp().getOrgid()+"");
////			TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjType(audittype);
//			String auditCode = "";
//			if(null != tblNbsjType) {
//				auditCode = tblNbsjType.getAuditCode()+"";
//			}
//			String projectCode = project.getPlanYear()+"-"+loginStaff.getLinkOrg().getOrgnumber()+"-";//+auditCode+"-"
//			//查询自增数据
//			String no = this.tblNbsjAuditplanMapper.selectMaxProjectCode(projectCode);
//			if(no != null) {
////				 no = no.replace(projectCode, "");
//			}else {
//				no = "0";
//			}
//			projectCode += (Integer.parseInt(no)+1);
//			//年度—公司编号—审计类型编号—自增数据
//			project.setProjectCode(projectCode);
//            
//			project.setProjectId(RandomUtil.uuBigDecimalId());
//            tblNbsjProjectMapper.insert(project);
//
//			BigDecimal projectId = project.getProjectId();
//            
//          //保存附件
//            if (StringUtils.isNotBlank(attids)){
//            	String[] ids = attids.split(",");
//            	for (int i = 0; i < ids.length; i++) {
//            		String id = ids[i].trim();
//        			this.tblAttachmentMapper.insertAttmentRelationProject(id, projectId);
//            	}
//            }
//            
//            //审计小组
//            
//            if(null != pjTeamJson && !"".equals(pjTeamJson.trim())) {
//    			List<TblNbsjProjectTeamEntity> srList = new ArrayList<TblNbsjProjectTeamEntity>();
//    			srList = JSONObject.parseArray(pjTeamJson,TblNbsjProjectTeamEntity.class);
//    			for (TblNbsjProjectTeamEntity pjTeam : srList) {
//    				//
//    				String zystaffids = pjTeam.getZystaffids();
//					BigDecimal leaderid = pjTeam.getLeaderId();
//					this.tblNbsjProjectteamService.pjItemAdd(token,pjTeam,zystaffids,leaderid,projectId);
//    			}
//    		}
//            
//            return project;
//        }
//    }
	
	public void capyMb(String parentid, TblNbsjTempleteEntity temp, String newparentid) throws Exception {
		List<TblTargetTypeEntity> list = tblTargetTypeService.findByALLPatrnt(parentid);
		if (list != null && list.size() > 0) {
			for (TblTargetTypeEntity tblTargetType : list) {
				TblTargetTypeEntity target = new TblTargetTypeEntity();
				target.setCreateTime(tblTargetType.getCreateTime());
				target.setNbsjTemplete(temp);
				target.setParentId(new BigDecimal(newparentid));
				target.setStatus(TblTargetTypeEntity.ZY_NUMBER);
				target.setTargetDesc(tblTargetType.getTargetDesc());
				target.setTargetName(tblTargetType.getTargetName());
				target.setTargetId(RandomUtil.uuBigDecimalId());
				target.setTempId(temp.getTempleteId());
//				tblTargetTypeService.save(target);
				tblTargetTypeMapper.insertEntity(target);
				List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
				if (grams != null && grams.size() > 0) {
					for (TblAduitProGramEntity gram : grams) {
						TblAduitProGramEntity prog = new TblAduitProGramEntity();
						prog.setBioData(gram.getBioData());
						prog.setTargetId(target.getTargetId());
						prog.setBusinessType(gram.getBusinessType());
						prog.setStatus(TblAduitProGramEntity.ZY_NUMBER);
						prog.setCreateTime(gram.getCreateTime());
						prog.setControl(gram.getControl());
						//prog.setIsUseProgram(0);
						prog.setTempId(temp.getTempleteId());
						prog.setRiskPoint(gram.getRiskPoint());
						prog.setRiskSource(gram.getRiskSource());
						prog.setSuditProcess(gram.getSuditProcess());
						prog.setUpdateTime(gram.getUpdateTime());
						prog.setProgramId(RandomUtil.uuBigDecimalId());
//						tblAduitProGramService.save(prog);
						tblAduitProGramMapper.insertEntity(prog);
					}
				}
				capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
			}
		}
	}
	
	
	@Override
	public R removeAttInfoByAttId(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        return this.deleteRealtionAttInfo(attId);
	}

	private R deleteRealtionAttInfo(String attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(new BigDecimal(attId));
        this.tblNbsjProjectMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}




	@Override
	public JsonBean getTempeleBizCnt(String token, BigDecimal templeteId) throws Exception {
		
		
		PageInfo<TblAduitProGramEntity> pageInfo = new PageInfo<TblAduitProGramEntity>();
    	pageInfo.setPageSize(15);
    	pageInfo.setCurrentPage(1);
		
		Integer cnt = this.tblAduitProGramMapper.selectCountByPageInfo(pageInfo,null,templeteId,null);
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	resultMap.put("cnt", cnt);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean getUserAuditItem(String token,BigDecimal staffid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	//查询人员-的参与的审计项目-状态为归档
    	List<TblNbsjProject> list=this.tblNbsjProjectMapper.selectAuditItems(staffid);
    	 
    	resultMap.put("data", list);
    	 
    	return ResponseFormat.retParam(1,200,resultMap);
	}




	@Override
	public JsonBean getProjectPlanList(String token, BigDecimal projectId, BigDecimal planId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		List<TblNbsjAuditplan> list = new ArrayList<TblNbsjAuditplan>();
		
		if(null==planId || "".equals(planId)) {
			TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
			list=tblNbsjAuditplanMapper.selectPlanListByyear(project.getPlanYear(), "年度计划",loginStaff.getLinkOrg().getOrgid());
		}else {
			list=tblNbsjAuditplanMapper.selectPlanListPlanId(planId);
		}

		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", list);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	@Override
    public Map<String, Object> viewOppsiteActiviti(BigDecimal projectId,String businessKey) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
    			TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(projectId.toString());
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("project", projectId);
                dataMap.put("taskList", list);
                dataMap.put("url", HttpClient.jkurl + businessKey);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
    			TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(projectId.toString());
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("project", project);
                dataMap.put("taskList", list);
                dataMap.put("url", HttpClient.jkurl + businessKey);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        }
    }


	@Override
	public JsonBean findProjectItemReport(String token, Integer pageNumber, Integer pageSize, Integer year)
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
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if(year == null) {
			year = calendar.get(Calendar.YEAR);
		}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);

		PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjProjectMapper.findProjectItemReport(pageInfo, year));
		pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectProjectItemReportCount(year));
		pageInfo.getTotalPage();
		resultMap.put("pageInfo", pageInfo);

    	return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean findAuditTypeCount(String token, Integer year) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if(year == null) {
			year = calendar.get(Calendar.YEAR);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(0);
		List<TblNbsjProject> objs = tblNbsjProjectMapper.findAuditTypeCount(year);
		if(objs!=null && objs.size()>0) {
			for (TblNbsjProject obj : objs) {
				Map<String,Object> resultMap  = new HashMap<String,Object>(0);
				resultMap.put("name", obj.getAuditType());
				resultMap.put("value",obj.getSl());
				list.add(resultMap);
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean selectPlanyfcount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<QualityParam> list = this.tblNbsjProjectMapper.selectPlanyfcount(queryYear);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@Override
	public JsonBean selectPlanZtcount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<QualityParam> list = this.tblNbsjProjectMapper.selectPlanZtcount(queryYear);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean selectPlancount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<QualityParam> list = this.tblNbsjProjectMapper.selectPlancount(queryYear);
//		List<QualityParam> list1 = new ArrayList<QualityParam>();
//		if(list!=null && list.size()>0) {
//			for (QualityParam qualityParam : list) {
//				List<TblNbsjProject> zslist = tblNbsjProjectMapper.selectPlancountDetailList(queryYear.toString(), qualityParam.getBsjdw(), null, null, null);
//				if(zslist!=null) {
//					qualityParam.setZs(zslist.size()+"");
//				}
//				list1.add(qualityParam);
//			}
//		}
		
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean selectPlancountDetail(String token, String year, String companyName, String auditType, String status, String month, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		if(pageNumber == null) {
			pageNumber = 1;
		}
		if(pageSize==null) {
			pageSize=10;
		}

		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		// 使用PageMethod进行分页查询,根据参数动态构建查询条件
		com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						// 统一调用一个Mapper方法,在SQL中根据参数动态构建查询条件
						tblNbsjProjectMapper.selectPlancountDetailList(loginStaff.getUsername(),year, companyName, auditType, status, month);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

		// 计算实施天数
		for(TblNbsjProject project : pageInfo.getList()) {
			if(project.getStartDate() != null && project.getEndDate() != null) {
				long days = (project.getEndDate().getTime() - project.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
				project.setDays((int)days);
			}
		}

		// 分页参数转换成分页对象
		PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean selectAuditTypeDetail(String token, String year, String auditType, Integer pageNumber, Integer pageSize) throws Exception {
		JsonBean jsonBean = null;
		try {
			// 设置分页参数默认值
			if (pageNumber == null || pageNumber < 1) {
				pageNumber = 1;
			}
			if (pageSize == null || pageSize < 1) {
				pageSize = 10;
			}

			System.out.println("审计项目类型详情查询参数 - year: " + year + ", auditType: " + auditType + ", pageNumber: " + pageNumber + ", pageSize: " + pageSize);

			Map<String,Object> resultMap = new HashMap<String,Object>(0);

			// 构建分页参数
			com.hbfk.util.PageInfo<TblNbsjProject> pageInfo = new com.hbfk.util.PageInfo<TblNbsjProject>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);

			// 查询总数
			Integer totalRecord = tblNbsjProjectMapper.selectAuditTypeDetailCount(year, auditType);
			System.out.println("查询总数: " + totalRecord);

			// 查询数据列表
			List<TblNbsjProject> list = tblNbsjProjectMapper.findAuditTypeDetail(pageInfo, year, auditType);
			System.out.println("查询列表大小: " + (list != null ? list.size() : 0));

			// 构建分页结果
			PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>();
			build.setTlist(list);
			build.setTotalRecord(totalRecord);
			build.setCurrentPage(pageNumber);
			build.setPageSize(pageSize);

			resultMap.put("pageInfo", build);
			jsonBean = ResponseFormat.retParam(1, 200, resultMap);

			System.out.println("返回结果: " + jsonBean.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("查询异常: " + e.getMessage());
		}
		return jsonBean;
	}

	@Override
	public JsonBean sjsl(String token, Integer year, Integer pageNumber, Integer pageSize) {
//		PageHelper.startPage(pageNumber, pageSize);
		try {
			
			com.github.pagehelper.PageInfo<SjjscDpVo> pageInfo = PageMethod.startPage(pageNumber, pageSize)
					.doSelectPageInfo(() -> {
						try {
							tblNbsjProjectMapper.selectSjjscDpVoByYear(year);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
			PageResult<SjjscDpVo> build = new PageResult<SjjscDpVo>().build(pageInfo);
//			List<SjjscDpVo> list = this.tblNbsjProjectMapper.selectSjjscDpVoByYear(year);
			Integer integer = this.tblNbsjProjectMapper.CountSjjscDpVoByYear(year);
			Map<String,Object> map = new HashMap<>();
			map.put("total", integer);
			map.put("list", build.getTlist());
			map.put("pageNumber",pageNumber);
			map.put("pageSize",pageSize);
			return ResponseFormat.retParam(1, 200, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JsonBean zgwt(String token, Integer year, Integer pageNumber, Integer pageSize) {
//		PageHelper.startPage(pageNumber, pageSize);
		try {
			com.github.pagehelper.PageInfo<ZgwtVo> pageInfo = PageMethod.startPage(pageNumber, pageSize)
					.doSelectPageInfo(() -> {
						try {
							tblNbsjProjectMapper.zgwt(year);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
			PageResult<ZgwtVo> build = new PageResult<ZgwtVo>().build(pageInfo);
			
//			List<ZgwtVo> zgwts = this.tblNbsjProjectMapper.zgwt(year);
			Integer integer = this.tblNbsjProjectMapper.countZgwt(year);
			Map<String,Object> map = new HashMap<>();
			map.put("total", integer);
			map.put("list", build.getTlist());
			map.put("pageNumber",pageNumber);
			map.put("pageSize",pageSize);
			return ResponseFormat.retParam(1, 200, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public JsonBean findReportYearList(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		List<Integer> list = tblNbsjProjectMapper.findReportYearList();
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean findNbsjProjectCountByCompanyId(String token, Integer year) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if(year == null) {
			year = calendar.get(Calendar.YEAR);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(0);
		List<TblNbsjProject> objs = tblNbsjProjectMapper.findNbsjProjectCountByCompanyId(year);
		if(objs!=null && objs.size()>0) {
			for (TblNbsjProject obj : objs) {
				Map<String,Object> resultMap  = new HashMap<String,Object>(0);
				resultMap.put("name", obj.getAuditOrgName());
				resultMap.put("value",obj.getSl());
				list.add(resultMap);
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean selectSjsQuestList(Date startdate, Date enddate, String projectid, String token) throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		// TODO Auto-generated method stub
				TblSjsQuestion q=new TblSjsQuestion();
				String dateSql1="";
				String dateSql2="";
				String dateSql3="";
				
				if(startdate != null) {
				  if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						dateSql1 += " AND  STARTDATE >= '"+DateUtils.parseDate(startdate, "yyyy-MM-dd")+"'";
					}else {
						dateSql1 += " AND TO_DATE(TO_CHAR(STARTDATE,'YYYY-MM-DD'), 'YYYY-MM-DD') >= TO_DATE('"+DateUtils.parseDate(startdate, "yyyy-MM-dd")+"', 'YYYY-MM-DD')";
					}
				}
				if(enddate != null) {
					if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						dateSql1 += " AND ENDDATE<= '"+DateUtils.parseDate(enddate, "yyyy-MM-dd")+"'";
					}else {
						dateSql1 += " AND TO_DATE(TO_CHAR(ENDDATE,'YYYY-MM-DD'), 'YYYY-MM-DD') <= TO_DATE('"+DateUtils.parseDate(enddate, "yyyy-MM-dd")+"', 'YYYY-MM-DD')";
					}
				}
				if(StringUtils.isNotBlank(projectid)){
					dateSql1 +=" and projectid='"+projectid+"'";
				}
				//一、审计项目
				String sql1 = "SELECT "
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE  in ('贯彻落实国家重大政策措施审计','财政财务收支审计','固定资产投资审计','内部控制和风险管理审计','经济责任审计','信息系统审计','境外审计','其他')   "+dateSql1+" ) value1,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='贯彻落实国家重大政策措施审计'  "+dateSql1+" ) value2,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='财政财务收支审计'  "+dateSql1+" ) value3,"
						+ "(select nvl(COUNT(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='固定资产投资审计'  "+dateSql1+" ) value4,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='内部控制和风险管理审计'  "+dateSql1+" ) value5,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='经济责任审计'  "+dateSql1+" ) value6,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='信息系统审计'  "+dateSql1+" ) VALUE7,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6  AND AUDITTYPE='境外审计'   "+dateSql1+")  value8,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE ='其他'  "+dateSql1+") value9,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND externalAssig='2'  "+dateSql1+" ) value10,"
					    + "(select nvl(sum(workingHours),0) from TBL_NBSJ_PROJECT where examinetype=6 "+dateSql1+" ) value11 "
						+ " from dual";
				System.out.println("项目SQL："+sql1);
				
				JSONObject ob= tblNbsjProjectMapper.getObjBySql(sql1);
				if(ob!=null){  
					  q.setSjxm(ob.getString("VALUE1"));
					  q.setZdzccssj(ob.getString("VALUE2"));
					  q.setCwszsj(ob.getString("VALUE3"));
					  q.setGdzc(ob.getString("VALUE4"));
					  q.setFxgl(ob.getString("VALUE5"));
					  q.setZrsj(ob.getString("VALUE6"));
					  q.setXtsj(ob.getString("VALUE7"));
					  q.setJwsj(ob.getString("VALUE8"));
					  q.setQtsj(ob.getString("VALUE9"));
					  q.setWbsj(ob.getString("VALUE10"));
					  q.setSjgzl(ob.getString("VALUE11"));
				}
				//五、审计发现问题整改（金额类）   六、审计发现问题整改（非金额类）
				if(startdate != null) {
					if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						dateSql2 += " AND  CREATETIEM >= '"+DateUtils.parseDate(startdate, "yyyy-MM-dd")+"'";
					}else {
						dateSql2 += " AND TO_DATE(TO_CHAR(CREATETIEM,'YYYY-MM-DD'), 'YYYY-MM-DD') >= TO_DATE('"+DateUtils.parseDate(startdate, "yyyy-MM-dd")+"', 'YYYY-MM-DD')";
					}
				}
				if(enddate != null) {
						if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						dateSql2 += " AND CREATETIEM<= '"+DateUtils.parseDate(enddate, "yyyy-MM-dd")+"'";
					}else {
						dateSql2 += " AND TO_DATE(TO_CHAR(CREATETIEM,'YYYY-MM-DD'), 'YYYY-MM-DD') <= TO_DATE('"+DateUtils.parseDate(enddate, "yyyy-MM-dd")+"', 'YYYY-MM-DD')";
					}
				}
				if(StringUtils.isNotBlank(projectid)){
					dateSql2 +=" and projectid='"+projectid+"'";
				}
				sql1 = "SELECT "
						+ "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null  "+dateSql2+" ) value1,"
						+ "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '调整会计账目'  "+dateSql2+") value2,"
						+ "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '收回资金' "+dateSql2+") value3,"
						+ "(SELECT nvl(sum(ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '挽回损失'  "+dateSql2+") value4,"
						+ "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '补缴税费' "+dateSql2+") value5,"
						+ "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '归还原资金渠道'  "+dateSql2+") value6,"
						+ "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '其他' "+dateSql2+") value7,"
						+ "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null   "+dateSql2+") value8,"
						+ "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null AND PROBLEMTYPE = '新制定制度' "+dateSql2+" ) value9,"
						+ "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null AND PROBLEMTYPE = '修订完善制度' "+dateSql2+" ) value10,"
						+ "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null AND PROBLEMTYPE = '优化完善业务流程' "+dateSql2+" ) value11,"
						+ "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2  and r.rectification is not null AND PROBLEMTYPE = '其他' "+dateSql2+" ) value12"
						+ " from DUAL";
				JSONObject ob2 = tblNbsjProjectMapper.getObjBySql(sql1);
				if(ob2!=null){  
					q.setWtzgjel(ob2.getString("VALUE1"));// 金额类,
					q.setTzzm(ob2.getString("VALUE2"));//调整会计账目,
					q.setShzj(ob2.getString("VALUE3"));//收回资金,
					q.setWhss(ob2.getString("VALUE4"));//挽回损失,
					q.setBjsf(ob2.getString("VALUE5"));//补缴税费,
					q.setGhqd(ob2.getString("VALUE6"));//归还原资金渠道,
					q.setQtje(ob2.getString("VALUE7"));//其他金额,
					q.setFje(ob2.getString("VALUE8"));// 非金额类,
					q.setXzd(ob2.getString("VALUE9"));//新制定制度,
					q.setXdzd(ob2.getString("VALUE10"));//修订完善制度,
					q.setYhlc(ob2.getString("VALUE11"));//优化完善业务流程,
					q.setQt(ob2.getString("VALUE12"));//其他
				}
				
				if(startdate != null) {
			        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						dateSql3 += " AND  CREATETIME >= '"+DateUtils.parseDate(startdate, "yyyy-MM-dd")+"'";
					}else {
						dateSql3 += " AND TO_DATE(TO_CHAR(CREATETIME,'YYYY-MM-DD'), 'YYYY-MM-DD') >= TO_DATE('"+DateUtils.parseDate(startdate, "yyyy-MM-dd")+"', 'YYYY-MM-DD')";
					}
				}
				if(enddate != null) {
				        if(DateBaseConfig.DATABASETYPE.equals("MySql")) {
						dateSql3 += " AND CREATETIME<= '"+DateUtils.parseDate(enddate, "yyyy-MM-dd")+"'";
					}else {
						dateSql3 += " AND TO_DATE(TO_CHAR(CREATETIME,'YYYY-MM-DD'), 'YYYY-MM-DD') <= TO_DATE('"+DateUtils.parseDate(enddate, "yyyy-MM-dd")+"', 'YYYY-MM-DD')";
					}
				}
				if(StringUtils.isNotBlank(projectid)){
					dateSql3 +=" and projectid='"+projectid+"'";
				}
				sql1 = "select "
						+ "(select nvl(sum(relatedMoney),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' "
						+ dateSql3 + " ) VALUE1,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='1'  "
						+ dateSql3 + " ) value2,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2'  "
						+ dateSql3 + " ) value3,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='1'  "
						+ dateSql3 + " ) value4,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='2'  "
						+ dateSql3 + " ) value5,"
						+ "(select nvl(sum(relatedMoney),0)  FROM TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='3'  "
						+ dateSql3 + " ) value6,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='4'  "
						+ dateSql3 + " ) value7,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='5'  "
						+ dateSql3 + " ) value8,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='6'  "
						+ dateSql3 + " ) value9,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='7'  "
						+ dateSql3 + " ) value10,"
						+ "(select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='8'  "
						+ dateSql3 + " ) value11," +

				"(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是'  " + dateSql3 + " ) value12,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' " + dateSql3
						+ " ) value13, " + "(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='2' "
						+ dateSql3 + " ) value14, " +

				"(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='2' and detailtype='21'  "
						+ dateSql3 + " ) value15,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='2' and detailtype='22'  "
						+ dateSql3 + " ) value16,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='2' and detailtype='23'  "
						+ dateSql3 + " ) value17,"
						+ "(select nvl(count(*),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='2' and detailtype='24'  "
						+ dateSql3 + " ) value18" + " FROM dual";
				JSONObject ob3 = tblNbsjProjectMapper.getObjBySql(sql1);
				if(ob3!=null){  
					q.setWtje(ob3.getString("VALUE1"));// 金额类,
					q.setJxlwtje(ob3.getString("VALUE2"));//绩效类问题金额,
					q.setHgxwtje(ob3.getString("VALUE3"));//  合规性问题金额,
					q.setKjhs(ob3.getString("VALUE4"));//会计核算方面,
					q.setWgsy(ob3.getString("VALUE5"));//违规使用资金,
					q.setJlcd(ob3.getString("VALUE6"));//截留沉淀资金,
					q.setSslf(ob3.getString("VALUE7"));//损失浪费,
					q.setNyzj(ob3.getString("VALUE8"));//挪用资金,
					q.setTsls(ob3.getString("VALUE9"));//偷漏税费,
					q.setWgqd(ob3.getString("VALUE10"));//违规取得收入,
					q.setWtqtje(ob3.getString("VALUE11"));//其他金额类,
					q.setWtzs(ob3.getString("VALUE12"));//问题总数,
					q.setJelzs(ob3.getString("VALUE13"));//金额类总数, 
					q.setFjelzs(ob3.getString("VALUE14"));//非金额类总数, 
					q.setGjzc(ob3.getString("VALUE15"));//国家政策措施落实方面,
					q.setFzgh(ob3.getString("VALUE16"));//发展规划与战略决策方面,
					q.setNbkz(ob3.getString("VALUE17"));//内部控制与风险管理方面,
					q.setWtqt(ob3.getString("VALUE18"));//其他
				}	
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", q);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	
	
	
	public List<Object[]> getGkProjectInfoExport(String token,TblGkProjectVo project) throws Exception {
    	
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return null;
		}
		project.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		List<cn.hutool.json.JSONObject> list = tblNbsjProjectMapper.getGkProjectInfoExport(project);
		List<Object[]> contractlist = new ArrayList<Object[]>(0);
		Object[] objs = null;
		if(list!=null && list.size()>0) {
			for (int j = 0; j < list.size(); j++) {
				cn.hutool.json.JSONObject obj=list.get(j);
				objs = new Object[11];
				objs[0] = obj.get("PROJECTCODE")!=null?obj.get("PROJECTCODE").toString():"";
				objs[1] = obj.get("PRJOECTNAME")!=null?obj.get("PRJOECTNAME").toString():"";
				objs[2] = obj.get("AUDITORGNAME")!=null?obj.get("AUDITORGNAME").toString():"";
				objs[3] = obj.get("ORGNAME")!=null?obj.get("ORGNAME").toString():"";
				objs[4] = obj.get("PMNAME")!=null?obj.get("PMNAME").toString():"";
				objs[5] = obj.get("STATUS")!=null? obj.get("STATUS").toString():"";
				objs[6] = obj.get("PLANYEAR")!=null?obj.get("PLANYEAR").toString():"";
				objs[7] = obj.get("STARTDATE")!=null?obj.get("STARTDATE").toString():"";
				objs[8] = obj.get("ENDDATE")!=null?obj.get("ENDDATE").toString():"";
				objs[9] = obj.get("DAYNUMBER")!=null?obj.get("DAYNUMBER").toString():"";
				objs[10] = obj.get("COSTS")!=null?obj.get("COSTS").toString():"";
				contractlist.add(objs);
	           }
		
		}
    	return contractlist;
	}
	
	
	
	public List<Object[]> getGkQuestionInfoExport(String token,TblGkQuestionVo question) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return null;
		}
		question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		List<cn.hutool.json.JSONObject> list = tblNbsjProjectMapper.getGkQuestionInfoExport(question);
		
		List<Object[]> contractlist = new ArrayList<Object[]>(0);
		Object[] objs = null;
		if(list!=null && list.size()>0) {
			for (int j = 0; j < list.size(); j++) {
				cn.hutool.json.JSONObject obj=list.get(j);
				objs = new Object[9];
				objs[0] = obj.get("PROJECTCODE")!=null?obj.get("PROJECTCODE").toString():"";
				objs[1] = obj.get("PRJOECTNAME")!=null?obj.get("PRJOECTNAME").toString():"";
				objs[2] = obj.get("INTERNALTYPE")!=null?obj.get("INTERNALTYPE").toString():"";
				objs[3] = obj.get("PLANYEAR")!=null?obj.get("PLANYEAR").toString():"";
				objs[4] = obj.get("QUESTITLE")!=null?obj.get("QUESTITLE").toString():"";
				objs[5] = obj.get("AUDITDISCOVERABLE")!=null?obj.get("AUDITDISCOVERABLE").toString():"";
				objs[6] = obj.get("AUDITORGNAME")!=null? obj.get("AUDITORGNAME").toString():"";
				objs[7] = obj.get("ORGNAME")!=null?obj.get("ORGNAME").toString():"";
				objs[8] = obj.get("FINDREALNAME")!=null?obj.get("FINDREALNAME").toString():"";
				contractlist.add(objs);
	           }
		
		}
    	return contractlist;
	}
	
	public List<Object[]>  getGkZgContentInfoExport(String token, TblGkZgQuestionVo question) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return null;
		}
		question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
		List<cn.hutool.json.JSONObject> list =tblNbsjProjectMapper.getGkZgContentInfoExport(question);

		List<Object[]> contractlist = new ArrayList<Object[]>(0);
		Object[] objs = null;
		if(list!=null && list.size()>0) {
			for (int j = 0; j < list.size(); j++) {
				cn.hutool.json.JSONObject obj=list.get(j);
				objs = new Object[9];
				objs[0] = obj.get("PROJECTCODE")!=null?obj.get("PROJECTCODE").toString():"";
				objs[1] = obj.get("PRJOECTNAME")!=null?obj.get("PRJOECTNAME").toString():"";
				objs[2] = obj.get("AUDITORGNAME")!=null?obj.get("AUDITORGNAME").toString():"";
				objs[3] = obj.get("ORGNAME")!=null?obj.get("ORGNAME").toString():"";
				objs[4] = obj.get("PLANYEAR")!=null?obj.get("PLANYEAR").toString():"";
				objs[5] = obj.get("WTZS")!=null? obj.get("WTZS").toString():"";
				objs[6] = obj.get("FQZGS")!=null?obj.get("FQZGS").toString():"";
				objs[7] = obj.get("YZG")!=null?obj.get("YZG").toString():"";
				objs[8] = obj.get("WZG")!=null?obj.get("WZG").toString():"";
				contractlist.add(objs);
	           }
		
		}
    	return contractlist;
	}
	
	
	
	@Override
	public JsonBean projectAdd(TblNbsjProject pj, String token,String planStartDate,String planEndDate,String attids,String pd_dx,String pjTeamJson) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null); 
		}
		
		pj.setPlanStartDate(planStartDate);
		pj.setPlanEndDate(planEndDate);
		
		Integer count = this.tblNbsjProjectMapper.selectPlanCodeByOrgid(pj);
		
		if(pj.getCospomsordepartment()!=null) {
			 TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(pj.getCospomsordepartment().toString());
			 if(bmfzr!=null) {
				 pj.setCospomsordepartmentstaffid(bmfzr.getStaffid());
			 }
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//通过计划id判断，是第一步保存或第二步保存
//		if(pj!=null && null == pj.getPlanId()){
//			TblNbsjAuditplan plan = autoPlanSave(pj, loginStaff,token);
//			if(null != plan && plan.getPlanid()!=null) {
//				resultMap.put("plan",plan);
//				pj.setPlanId(plan.getPlanid());
//			}
//		}

		xmlbUpdate(pj, token, pj.getPmId()+"", planEndDate, planStartDate, pj.getTempzyId()+"", pj.getTempId()+"",
				pj.getProtempid()+"", pd_dx, null, pj.getPlanId()+"",attids,pjTeamJson);
		
		resultMap.put("WorkReport",pj);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	private TblNbsjProject xmlbUpdate(TblNbsjProject project,String token,String pmid,String enddate,String startdate,String tempzyid,
			  String tempid,String protempid,String pd_dx,String belongsto,String plan,String attids,String pjTeamJson ) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
			return null;
		}
		
		TblOrganization organization1 = new TblOrganization();
		organization1.setOrgid(loginStaff.getCurrentOrg().getOrgid());
		
		if (project.getProjectId() != null) {
			// 修改开始
			TblNbsjProject tblnbsjProject = this.tblNbsjProjectMapper.selectPJById(project.getProjectId());//tblnbsjProjectService.getId(project.getProjectid().toString());
			tblnbsjProject.setAuditType(project.getAuditType());
			tblnbsjProject.setPlanStartDate(project.getPlanStartDate());
			tblnbsjProject.setPlanEndDate(project.getPlanEndDate());
			tblnbsjProject.setPlanYear(project.getPlanYear());
			tblnbsjProject.setCosts(project.getCosts());
			tblnbsjProject.setPrjoectName(project.getPrjoectName());
			tblnbsjProject.setProjectSource(project.getProjectSource());
			tblnbsjProject.setPurpose(project.getPurpose());
			tblnbsjProject.setScopes(project.getScopes());
			tblnbsjProject.setPursuant(project.getPursuant());
			tblnbsjProject.setComments(project.getComments());
			tblnbsjProject.setProDesc(project.getProDesc());
			tblnbsjProject.setProSjfs(project.getProSjfs());
			//
			tblnbsjProject.setProjectorgid(project.getProjectorgid());
			tblnbsjProject.setProjectorgname(project.getProjectorgname());
			//批复的起止年限 和 实际的起止年限
			tblnbsjProject.setAppproyearstart(project.getAppproyearstart());
			tblnbsjProject.setAppproyearend(project.getAppproyearend());
			tblnbsjProject.setActproyearstart(project.getActproyearstart());
			tblnbsjProject.setActproyearend(project.getActproyearend());
//			tblnbsjProject.setAppproyear(project.getAppproyear());
//			tblnbsjProject.setActproyear(project.getActproyear());
			tblnbsjProject.setProjectmgdeptid(project.getProjectmgdeptid());
			tblnbsjProject.setProjectmgdeptname(project.getProjectmgdeptname());
			tblnbsjProject.setProjectorgaddress(project.getProjectorgaddress());
			tblnbsjProject.setProjectlinkman(project.getProjectlinkman());
			tblnbsjProject.setProjectlinktel(project.getProjectlinktel());
			tblnbsjProject.setProjecttype(project.getProjecttype());
			tblnbsjProject.setSecretlevel(project.getSecretlevel());	
			tblnbsjProject.setSecrectLevelId(project.getSecrectLevelId());
			tblnbsjProject.setStaffScopeIds(project.getStaffScopeIds());
			tblnbsjProject.setStaffScopeNames(project.getStaffScopeNames());
			
			tblnbsjProject.setPprojectName(project.getPprojectName());
			tblnbsjProject.setTargetName(project.getTargetName());
			tblnbsjProject.setExternAlassig(project.getExternAlassig());
			
			
			if(tempzyid == "null" || tempzyid.equals("")){
				TblNbsjTempleteEntity templetezy = tblNbsjTempleteMapper.findbyid(tempzyid);//指引模板
				tblnbsjProject.setTbltempletezy(templetezy);
			}
			tblnbsjProject.setCreateTime(new Date());
			tblnbsjProject.setUpdateStatus(TblNbsjProject.UPDATEYES);
			
			tblnbsjProject.setPmId(new BigDecimal(pmid));
			//修改审计模板
			if (StringUtils.isNotEmpty(tempid)) {
				// 如果审计模板再次修改
				TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(tempid);//tblNbsjTempleteService.get(new BigDecimal(tempid));
				TblNbsjTempleteEntity temp = new TblNbsjTempleteEntity();
				temp.setTempType("3");
				temp.setCreateDate(templete.getCreateDate());
				temp.setTempleteCode(templete.getTempleteCode());
				temp.setTempleteDesc(templete.getTempleteDesc());
				Set<TblOrganization> orgs = templete.getOrganizations();
				if (orgs != null && orgs.size() > 0) {
					for (TblOrganization org : orgs) {
						temp.getOrganizations().add(org);
					}
				}
				temp.setStaffId(templete.getStaffId());
				temp.setTempleteType(templete.getTempleteType());
				temp.setUpdateStaffId(templete.getUpdateStaffId());
				temp.setUpdateDate(templete.getUpdateDate());
				temp.setStatus(templete.getStatus());
				temp.setTempleteName(templete.getTempleteName());
				tblNbsjTempleteMapper.insertEntity(temp);
				
				tblnbsjProject.setTempId(temp.getTempleteId());
				tblnbsjProject.setOrgInfo(organization1);
				List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(tempid);
				if (list != null && list.size() > 0) {
				for (TblTargetTypeEntity tblTargetType : list) {
					TblTargetTypeEntity target = new TblTargetTypeEntity();
					target.setCreateTime(tblTargetType.getCreateTime());
					target.setNbsjTemplete(temp);
					target.setParentId(tblTargetType.getParentId());
					target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);//TEMP_NUMBER 0 ;ZY_NUMBER 1
					target.setTargetDesc(tblTargetType.getTargetDesc());
					target.setTargetName(tblTargetType.getTargetName());
					tblTargetTypeMapper.insertEntity(target);
					List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
					if (grams != null && grams.size() > 0) {
						for (TblAduitProGramEntity gram : grams) {
							TblAduitProGramEntity prog = new TblAduitProGramEntity();
							prog.setBioData(gram.getBioData());
							prog.setTargetId(target.getTargetId());
							prog.setControl(gram.getControl());
							prog.setBusinessType(gram.getBusinessType());
							prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
							prog.setCreateTime(gram.getCreateTime());
							prog.setNbsjTemplete(temp);
							prog.setTempId(temp.getTempleteId());
							prog.setRiskPoint(gram.getRiskPoint());
							prog.setRiskSource(gram.getRiskSource());
							prog.setSuditProcess(gram.getSuditProcess());
							prog.setUpdateTime(gram.getUpdateTime());
							tblAduitProGramMapper.insertEntity(prog);
						}
					}
					capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
					}
				}
				//删除之前分配的任务
				//删除任务
				
				List<TblNbsjAuthorizationEntity> nbsjauthorizatoin = this.tblNbsjAuthorizationService.getByProjectId(project.getProjectId());
				if(null!=nbsjauthorizatoin&&nbsjauthorizatoin.size()>0) {
					for (TblNbsjAuthorizationEntity tblNbsjAuthorization : nbsjauthorizatoin) {
						if(null!=nbsjauthorizatoin) {
							List<TblNbsjOperateEntity> listoper = tblNbsjOperateMapper.findByAuthId(tblNbsjAuthorization.getAuthId());//tblNbsjOperateService.findByAuthId(tblNbsjAuthorization.getAuthId());
							TblNbsjOperateEntity oper = null;
							if(listoper!=null && listoper.size()>0){
								oper = listoper.get(0);
							}
							tblNbsjOperateMapper.deleteByOpid(oper.getOperateid());
							this.tblNbsjAuthorizationMapper.deleteByAuthid(tblNbsjAuthorization.getAuthId());
						}
					}
				}
			}
			try {
				if (startdate != null && startdate.length() > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					tblnbsjProject.setStartDate(sdf.parse(startdate));
				}
				if (enddate != null && enddate.length() > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					tblnbsjProject.setEndDate(sdf.parse(enddate));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tblnbsjProject.setAuditOrgId(project.getAuditOrgId());
			tblnbsjProject.setAuditOrgName(project.getAuditOrgName());
			
			BigDecimal planid = new BigDecimal(plan);
			TblNbsjAuditplan jplan = tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planid);
			tblnbsjProject.setTblnbsjPlan(jplan);
			
			tblNbsjProjectMapper.updateEntity(tblnbsjProject);
			
			BigDecimal projectId = project.getProjectId();
			
			//保存附件
			this.tblAttachmentMapper.deleteAttmentRelationProject(projectId);
			if (StringUtils.isNotBlank(attids)){
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i].trim();
					this.tblAttachmentMapper.insertAttmentRelationProject(id, projectId);
				}
			}
			
			//==项目小组
			if(null != pjTeamJson && !"".equals(pjTeamJson.trim())) {
				List<TblNbsjProjectTeamEntity> srList = new ArrayList<TblNbsjProjectTeamEntity>();
				srList = com.alibaba.fastjson.JSONObject.parseArray(pjTeamJson,TblNbsjProjectTeamEntity.class);
				for (TblNbsjProjectTeamEntity pjTeam : srList) {
					//
					String zystaffids = pjTeam.getZystaffids();
					BigDecimal leaderid = pjTeam.getLeaderid();
					this.tblNbsjProjectteamService.pjItemAdd(token,pjTeam);
				}
			}
			
			return tblnbsjProject;
		}else {
			//新建项目
			try {
				if (startdate != null && startdate.length() > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					project.setStartDate(sdf.parse(startdate));
				}
				if (enddate != null && enddate.length() > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					project.setEndDate(sdf.parse(enddate));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			BigDecimal planid = new BigDecimal(plan);
			TblNbsjAuditplan jplan = tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planid);//tblnbsjauditPlanService.get(plan);
			project.setTblnbsjPlan(jplan);
			TblStaff pm = new TblStaff();
			pm.setStaffid(new BigDecimal(pmid));
			project.setPmStaff(pm);//.setTblPm(pm);
			project.setOrgInfo(organization1);//.setTblorg(organization1);
			//第一次新增审计模板
			if (StringUtils.isNotEmpty(tempid)) {
				//选择的方案模板(来自方案模板)
				TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(tempid);
				//复制审计模板
				TblNbsjTempleteEntity temp = new TblNbsjTempleteEntity();
				temp.setTempType("3");
				temp.setCreateDate(templete.getCreateDate());
				temp.setTempleteCode(templete.getTempleteCode());
				temp.setTempleteDesc(templete.getTempleteDesc());
				Set<TblOrganization> orgs = templete.getOrganizations();
				if (orgs != null && orgs.size() > 0) {
					for (TblOrganization org : orgs) {
						temp.getOrganizations().add(org);
					}
				}
				temp.setStaffId(templete.getStaffId());
				temp.setTempleteType(templete.getTempleteType());
				temp.setUpdateStaffId(templete.getUpdateStaffId());
				temp.setUpdateDate(templete.getUpdateDate());
				temp.setStatus(templete.getStatus());
				temp.setTempleteName(templete.getTempleteName());
				tblNbsjTempleteMapper.insertEntity(temp);
				
				project.setTempId(temp.getTempleteId());
				if (StringUtils.isNotEmpty(tempzyid)) {
					TblNbsjTempleteEntity templetezy = tblNbsjTempleteMapper.findbyid(tempzyid);
					project.setTbltempletezy(templetezy);
				}
				
				List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(tempid);
				if (list != null && list.size() > 0) {
					for (TblTargetTypeEntity tblTargetType : list) {
					TblTargetTypeEntity target = new TblTargetTypeEntity();
					target.setCreateTime(tblTargetType.getCreateTime());
					target.setNbsjTemplete(temp);
					target.setParentId(tblTargetType.getParentId());
					target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);
					target.setTargetDesc(tblTargetType.getTargetDesc());
					target.setTargetName(tblTargetType.getTargetName());
					tblTargetTypeMapper.insertEntity(target);
					List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
					if (grams != null && grams.size() > 0) {
						for (TblAduitProGramEntity gram : grams) {
							TblAduitProGramEntity prog = new TblAduitProGramEntity();
							prog.setBioData(gram.getBioData());
							prog.setTargetId(target.getTargetId());
							prog.setControl(gram.getControl());
							prog.setBusinessType(gram.getBusinessType());
							prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
							prog.setCreateTime(gram.getCreateTime());
							prog.setTempId(temp.getTempleteId());
							prog.setRiskPoint(gram.getRiskPoint());
							prog.setRiskSource(gram.getRiskSource());
							prog.setSuditProcess(gram.getSuditProcess());
							prog.setUpdateTime(gram.getUpdateTime());
							tblAduitProGramMapper.insertEntity(prog);
						}
					}
					capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
					}
				}
			}
			
			//被审计单位
			project.setStatus(0);
			project.setFpStatus(0);//项目任务分配状态
			project.setAssigbedControlTime(new Date());
			project.setAssigbedumpeTime(new Date());
			project.setAssigbedpmTime(new Date());
			project.setCreateTime(new Date());
			project.setUpdateStatus(TblNbsjProject.UPDATEYES);
			project.setCreateStaffId(loginStaff.getStaffid());
			project.setCyrrentStatre(TblNbsjProject.NO_SELECT);
			project.setOrgId(loginStaff.getCurrentOrg().getOrgid());
			
			tblNbsjProjectMapper.insertEntity(project);
			BigDecimal projectId = project.getProjectId();
			
			//保存附件
			if (StringUtils.isNotBlank(attids)){
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i].trim();
					this.tblAttachmentMapper.insertAttmentRelationProject(id, projectId);
				}
			}
			
			//审计小组
			if(null != pjTeamJson && !"".equals(pjTeamJson.trim())) {
				List<TblNbsjProjectTeamEntity> srList = new ArrayList<TblNbsjProjectTeamEntity>();
				srList = com.alibaba.fastjson.JSONObject.parseArray(pjTeamJson,TblNbsjProjectTeamEntity.class);
				for (TblNbsjProjectTeamEntity pjTeam : srList) {
					String zystaffids = pjTeam.getZystaffids();
					BigDecimal leaderid = pjTeam.getLeaderid();
					this.tblNbsjProjectteamService.pjItemAdd(token,pjTeam);
				}
			}
			
			return project;
		}
	}
	
	@Override
	public JsonBean projectPageList(String token, Integer pageNumber, Integer pageSize,
									TblnbsjProjectVo tblnbsjProjectVo, String sortFields, String sortFlag) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
		tblnbsjProjectVo.setOrgId(t1.getOrgid());
		tblnbsjProjectVo.setStaffId(loginStaff.getStaffid());
		if(loginStaff.getTrole()!=null) {
			tblnbsjProjectVo.setRolename(loginStaff.getTrole().getRname());
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJListByPageInfo(pageInfo,tblnbsjProjectVo,sortFields,sortFlag,loginStaff));
    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJCountByPageInfo(pageInfo,tblnbsjProjectVo,loginStaff));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	
    	//==查询当前实施的项目！
		TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp != null) {
			BigDecimal projectId = tnp.getProjectId();
	    	resultMap.put("currProjectId", projectId);
		}
    	
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean projectRwfpPageList(String token, Integer pageNumber, Integer pageSize,
										TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo, String sortFields, String sortFlag) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
		tblnbsjProjectRwfpVo.setOrgId(t1.getOrgid());
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	tblnbsjProjectRwfpVo.setPmId(loginStaff.getStaffid());
    			
    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	List<TblNbsjProject> proList=this.tblNbsjProjectMapper.selectPJRwfpListByPageInfo(pageInfo,tblnbsjProjectRwfpVo,sortFields,sortFlag,loginStaff);
    	for(TblNbsjProject p :proList){
    		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(p.getProjectId());
    		BigDecimal tempId = null;
        	if(null !=project) {
        		tempId = project.getTbltemplete().getTempleteId();
        	}
        	StringBuffer buffer=new StringBuffer();
			buffer.append("[");
			List<String> list=tblNbsjOperateMapper.findList(tempId);
            for(String str:list){
            	buffer.append("{\"formId\":"+p.getProjectId()+",\"distributionTitle\":\""+p.getPrjoectName()+"\",\"reciver\":"+str+",\"isread\":0,\"moduleType\":\"znsj\"}");
            }
            p.setJsonString(buffer.append("]").toString());
    	}
    	pageInfo.setTlist(proList);
    	pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJRwfpCountByPageInfo(pageInfo,tblnbsjProjectRwfpVo,loginStaff));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean zgwtDetail(String token, Integer year, String orgName, String projectId, String audittype, String projectsource, String queryType, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		if(pageNumber == null) {
			pageNumber = 1;
		}
		if(pageSize == null) {
			pageSize = 10;
		}

		try {
			com.hbfk.util.PageInfo<ZgwtDetailVo> pageInfo = new com.hbfk.util.PageInfo<ZgwtDetailVo>();
			pageInfo.setPageSize(pageSize);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setTlist(this.tblNbsjProjectMapper.findZgwtDetail(year, orgName, projectId, audittype, projectsource, queryType, pageInfo));
			pageInfo.setTotalRecord(this.tblNbsjProjectMapper.countZgwtDetail(year, orgName, projectId, audittype, projectsource, queryType));
			pageInfo.getTotalPage();

			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("pageInfo", pageInfo);

			return ResponseFormat.retParam(1,200,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"查询整改问题详细数据失败: " + e.getMessage());
		}
	}

	@Override
	public JsonBean zgwtIssuesDetail(String token, Integer year, String orgName, String queryType, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		if(pageNumber == null) {
			pageNumber = 1;
		}
		if(pageSize == null) {
			pageSize = 10;
		}

		try {
			// 构建查询VO
			TblZgzzIssuesilistVo issuesVo = new TblZgzzIssuesilistVo();
			issuesVo.setPageNum(pageNumber);
			issuesVo.setPageSize(pageSize);
			issuesVo.setCreateStaff(loginStaff.getStaffid());
			issuesVo.setDeptIds(loginStaff.getDeptIds());

			// 设置年度过滤条件
			if(year != null) {
				issuesVo.setYear(year.toString());
			}

			// 设置主管部门过滤条件（通过责任部门）
			if(StringUtils.isNotBlank(orgName)) {
				issuesVo.setResponsibleDeptName(orgName);
			}

			// 设置审计对象类型为单位（1-单位）
			issuesVo.setAuditObjectType(1);

			// 根据查询类型设置状态过滤条件
			// queryType: yzg(已整改) | wzg(未整改) | zs(整改总数) | yxh(已销号) | wxh(未销号) | xhzs(销号总数)
			// 注意：这里需要关联查询 TBL_RECTIFICATION_ISSUES 和 TBL_ZGZZ_RECTIFICATIONIMPL 表来获取整改状态
			// 由于当前 selectListByPageInfo 方法不支持这种复杂关联，我们需要使用状态字段来过滤
			// 根据统计SQL的逻辑：
			// - 已整改：CONCLUSION IN ('已整改', '已整改到位')
			// - 未整改：CONCLUSION NOT IN ('已整改', '已整改到位')
			// - 已销号：ISXH='1'
			// - 未销号：ISXH='0' OR ISXH IS NULL

			// 设置查询类型，传递给Mapper用于特殊处理
			issuesVo.setQueryType(queryType);

			// 分页查询整改清单数据
			com.github.pagehelper.PageInfo<TblZgzzIssuesilistVo> page = PageMethod.startPage(pageNumber, pageSize)
					.doSelectPageInfo(() -> this.tblZgzzIssuesilistMapper.selectListByPageInfo(issuesVo, loginStaff));

			com.hbfk.util.PageInfo<TblZgzzIssuesilistVo> pageInfo = new com.hbfk.util.PageInfo<TblZgzzIssuesilistVo>();
			pageInfo.setPageSize(pageSize);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setTlist(page.getList());
			pageInfo.setTotalRecord((int)page.getTotal());
			pageInfo.getTotalPage();

			return ResponseFormat.retParam(1,200,pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"查询整改问题一览表详细数据失败: " + e.getMessage());
		}
	}

	/**
	 * 项目数趋势变化查询（最近12个月）
	 */
	@Override
	public JsonBean selectProjectTrend(String token) {
		try {
			// 直接调用Mapper查询，返回原始数据
			List<Map<String, Object>> trendList = this.tblNbsjProjectMapper.selectProjectTrend();
			return ResponseFormat.retParam(1,200,trendList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"查询项目数趋势变化失败: " + e.getMessage());
		}
	}

	/**
	 * 整改问题状态统计查询
	 */
	@Override
	public JsonBean selectIssuesStatusStatistics(String token) {
		try {
			// 直接调用Mapper查询，返回原始数据
			List<Map<String, Object>> statisticsList = this.tblNbsjProjectMapper.selectIssuesStatusStatistics();
			return ResponseFormat.retParam(1,200,statisticsList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"查询整改问题状态统计失败: " + e.getMessage());
		}
	}


}
