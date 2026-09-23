package com.huabo.audit.service.impl;

import cn.hutool.core.util.StrUtil;
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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.util.PageInfoUtil;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.vo.*;
import com.huabo.audit.service.*;
import com.huabo.audit.oracle.mapper.TblAduitProGramMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblFlowMapper;
import com.huabo.audit.oracle.mapper.TblMyTaskMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuthorizationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjOperateMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectDataMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectteamMapper;
import com.huabo.audit.oracle.mapper.TblNbsjQuestionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTeamstaffMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTempleteMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTypeMapper;
import com.huabo.audit.oracle.mapper.TblProcessAnalusisUserMapper;
import com.huabo.audit.oracle.mapper.TblProcessAnalysisMapper;
import com.huabo.audit.oracle.mapper.TblProcessSettingMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblTargetTypeMapper;
import com.huabo.audit.oracle.vo.TblGkProjectVo;
import com.huabo.audit.oracle.vo.TblGkQuestionVo;
import com.huabo.audit.oracle.vo.TblGkZgQuestionVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectRwfpVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectZXYLVo;
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
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

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
    private TblProcessAnalusisUserMapper tblProcessAnalusisUserMapper;

    @Resource
    private TblMyTaskMapper tblMytaskMapper;
    @Resource
    private TblFlowMapper tblFlowMapper;

    @Resource
    private TblProcessAnalysisService tblProcessAnalysisService;
    @Resource
    private TblProcessAnalusisUserService tblProcessAnalusisUserService;

    @Resource
    private TblMyTaskService tblMyTaskService;

    @Resource
    private TblProcessSettingMapper processSettingMapper;

    @Resource
    private TblNbsjTypeMapper tblNbsjTypeMapper;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public JsonBean getZgprojectPageList(String token, Integer pageNumber, Integer pageSize,
                                         TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
        tblnbsjProjectVo.setOrgId(t1.getOrgid().intValue());
        tblnbsjProjectVo.setStaffId(loginStaff.getStaffid().intValue());
        if (loginStaff.getTrole() != null) {
            tblnbsjProjectVo.setRolename(loginStaff.getTrole().getRname());
        }


        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        /*Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectZgListByPageInfo(pageInfo, tblnbsjProjectVo));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectZgCountByPageInfo(pageInfo, tblnbsjProjectVo));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);

        //==查询当前实施的项目！
        TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp != null) {
            Integer projectId = tnp.getProjectId();
            resultMap.put("currProjectId", projectId);
        }


        return ResponseFormat.retParam(1, 200, resultMap);*/

        //链表分页  xml 写法
        com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjProjectMapper.findZgList(tblnbsjProjectVo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
        PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        resultMap.put("identifier", activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name()));
        resultMap.put("currProjectId", tnp == null ? null : tnp.getProjectId());
        resultMap.put("pageInfo", build);
        return ResponseFormat.retParam(1, 200, build);
    }


    @Override
    public JsonBean getNbsjProjecGdtPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo project,
                                             String projectStartDate, String projectEndDate) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        //project.setOrgId(loginStaff.getCurrentOrg().getOrgid().intValue());
        //project.setStaffId(loginStaff.getStaffid().intValue());

        pageInfo.setTlist(this.tblNbsjProjectMapper.selectProjectgdListByPageInfo(pageInfo, projectStartDate, projectEndDate, project));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectProjectgdListByPageCount(projectStartDate, projectEndDate, project));
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);

    }


    @Override
    public JsonBean getprojectLisbystaffidt(String token, TblnbsjProjectVo project) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        List<TblNbsjProject> list = this.tblNbsjProjectMapper.selectBystaffid(project.getStaffId());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", list);
        return ResponseFormat.retParam(1, 200, resultMap);

    }


    //获取当前实施项目；
    @Override
    public TblNbsjProject getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
//        return this.tblNbsjProjectMapper.selectPJById(projectId);
        return null;
    }

    @Override
    public JsonBean getAuditFileInfoList(String token, String projectName, Integer pageNumber, Integer pageSize)
            throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);

        pageInfo.setTlist(this.tblNbsjProjectMapper.selectAuditFileListPageInfo(pageInfo, projectName, loginStaff.getCurrentOrg().getOrgid()));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectAuditFileCountByPage(pageInfo, projectName, loginStaff.getCurrentOrg().getOrgid()));
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean getNbsjProjectPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo project,
                                           String projectStartDate, String projectEndDate) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
    /*	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	project.setOrgId(loginStaff.getCurrentOrg().getOrgid().intValue());
        if(!JudgeRoleRight.judgeRoleRight(SystemStaticValue.SJLBQX,loginStaff.getRoleNames())){
        	project.setStaffId(loginStaff.getStaffid().intValue());
        }
        
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectProjectListByPageInfo(pageInfo,projectStartDate,projectEndDate,project));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectProjectCountByPageInfo(pageInfo,projectStartDate,projectEndDate,project));
        
        TblNbsjStaffSelect oldtblNbsjStaffSelect = this.tblNbsjStaffSelectMapper.selectByUserId(loginStaff.getStaffid());
        
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_XMGL.name());
        
        resultMap.put("currentProjectid", oldtblNbsjStaffSelect.getProjectId());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        
		return ResponseFormat.retParam(1,200,resultMap);*/

        project.setOrgId(loginStaff.getCurrentOrg().getOrgid().intValue());
        if (!JudgeRoleRight.judgeRoleRight(SystemStaticValue.SJLBQX, loginStaff.getRoleNames())) {
            project.setStaffId(loginStaff.getStaffid().intValue());
        }

        //链表分页  xml 写法
        com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjProjectMapper.findList(projectStartDate, projectEndDate, project);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
        PageResult<TblNbsjProject> build = new PageResult<TblNbsjProject>().build(pageInfo);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("currentProjectid", this.tblNbsjStaffSelectMapper.selectByUserId(loginStaff.getStaffid()).getProjectId());
        resultMap.put("identifier", activityPluginsService.getoNState(ProcessEnum.SJ_XMGL.name()));
        resultMap.put("pageInfo", build);
        return ResponseFormat.retParam(1, 200, resultMap);

    }

    @Override
    public JsonBean projectProposalPageList(String token, Integer pageNumber, Integer pageSize, TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        //==查询当前实施的项目！
        TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp == null) {
            return ResponseFormat.retParam(0, 30003, null);
        }
        Integer projectId = tnp.getProjectId();
        if (null == projectId) {
            return ResponseFormat.retParam(0, 30003, null);
        }
        tblnbsjProjectVo.setProjectId(projectId);

        com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
//	     tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//	     pageInfo.setCondition(tblNbsjSheet);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectListByPageInfo(pageInfo, tblnbsjProjectVo));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectCountByPageInfo(pageInfo, tblnbsjProjectVo));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findProjectProposalDetail(String token, Integer dataId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        TblNbsjProject plan = this.tblNbsjProjectMapper.selectById(dataId);
        resultMap.put("Project", plan);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean getAuditFileDetailTreeInfo(String token, Integer projectId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
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
	     PageInfo<TblNbsjProject> pageInfo = null;
	        //审计责任人、审计负责人可以看所有归档的项目
	        if(loginStaff.getTrole()!=null&&(loginStaff.getTrole().getRname().equals("审计部负责人")||loginStaff.getTrole().getRname().equals("审计责任人"))) {
				Page<TblNbsjProject> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> this.tblNbsjProjectMapper.selectSjgdNewListByVo(tblnbsjProjectVo));
				pageInfo = new PageInfoUtil<TblNbsjProject>().parsePageInfo(page);
	        }else {
	        	//项目经理看自己的项目
	        	//工作人员看借阅的项目
	   	       tblnbsjProjectVo.setStaffId(Integer.parseInt(loginStaff.getStaffid().toString()));

				Page<TblNbsjProject> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> this.tblNbsjProjectMapper.selectSjgdNewListByStaffidVo(tblnbsjProjectVo));
				pageInfo = new PageInfoUtil<TblNbsjProject>().parsePageInfo(page);

	        }
	     pageInfo.getTotalPage();
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", pageInfo);
	     return ResponseFormat.retParam(1,200,resultMap);
	}

    @Override
    public JsonBean findSjgdNewDetail(String token, Integer projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        TblNbsjProject plan = this.tblNbsjProjectMapper.selectById(projectid);
        resultMap.put("Project", plan);
        return ResponseFormat.retParam(1, 200, resultMap);
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
	     
	     PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//	     tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//	     pageInfo.setCondition(tblNbsjSheet);

		Page<TblNbsjProject> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> this.tblNbsjProjectMapper.selectDajyNewListByPageInfo(orgid,staffid,tblnbsjProjectVo));
		pageInfo = new PageInfoUtil<TblNbsjProject>().parsePageInfo(page);
	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", pageInfo);
	     return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean jyrzNewPageList(String token, Integer pageNumber, Integer pageSize,TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		  if(loginStaff == null) {
		   return ResponseFormat.retParam(0,20006,null);
		  }
		  
		  Integer orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
	  
	     if(pageNumber == null) {
	      pageNumber = 1;
	     }
	     if(pageSize==null) {
	      pageSize=15;
	     }
	     Map<String,Object> resultMap = new HashMap<String,Object>(0);
	     
	     PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//	     tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//	     pageInfo.setCondition(tblNbsjSheet);
		Page<TblNbsjProject> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> this.tblNbsjProjectMapper.selectJyrzNewListByPageInfo(orgid,Integer.parseInt(loginStaff.getStaffid().toString()),tblnbsjProjectVo));
		pageInfo = new PageInfoUtil<TblNbsjProject>().parsePageInfo(page);

	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", pageInfo);
	     return ResponseFormat.retParam(1,200,resultMap);
	}


    //==
    @Override
    public JsonBean projectPageList(String token, Integer pageNumber, Integer pageSize,
                                    TblnbsjProjectVo tblnbsjProjectVo) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
        tblnbsjProjectVo.setOrgId(t1.getOrgid().intValue());
        tblnbsjProjectVo.setStaffId(loginStaff.getStaffid().intValue());
        if (loginStaff.getTrole() != null) {
            tblnbsjProjectVo.setRolename(loginStaff.getTrole().getRname());
        }


        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJListByPageInfo(pageInfo, tblnbsjProjectVo));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJCountByPageInfo(pageInfo, tblnbsjProjectVo));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);

        //==查询当前实施的项目！
        TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp != null) {
            Integer projectId = tnp.getProjectId();
            resultMap.put("currProjectId", projectId);
        }


        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean projectAdd(TblNbsjProject pj, String token, String planStartDate, String planEndDate, String attids, String pd_dx, String pjTeamJson) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        pj.setPlanStartDate(planStartDate);
        pj.setPlanEndDate(planEndDate);

        Integer count = this.tblNbsjProjectMapper.selectPlanCodeByOrgid(pj);
        /*
         * if(count > 0) { return ResponseFormat.retParam(0,202,null); }
         */

        String belongsto = "";
        if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")) {
            belongsto = pj.getAuditStaffId() + "";
        } else {
            belongsto = pj.getAuditOrgId() + "";
        }

        if (pj.getCospomsordepartment() != null) {
            TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(pj.getCospomsordepartment().toString());
            if (bmfzr != null) {
                pj.setCospomsordepartmentstaffid(bmfzr.getStaffid());
            }

        }


        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        //通过计划id判断，是第一步保存或第二步保存
        if (pj != null && null == pj.getPlanId()) {
            TblNbsjAuditplan plan = autoPlanSave(pj, loginStaff, token);
            if (null != plan && plan.getPlanid() != null) {
                resultMap.put("plan", plan);
                if (null != plan.getPlanid()) {
                    pj.setPlanId(plan.getPlanid().intValue());
                }
            }
        }

        xmlbUpdate(pj, loginStaff, pj.getPmId(), planEndDate, planStartDate, pj.getTempzyId(), pj.getTempId(),
                pj.getProtempid(), pd_dx, belongsto, pj.getPlanId(), attids, pjTeamJson, token);


//		Integer orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
//		pj.setOrgId(orgid);
//    	
//		pj.setCreateStaffId(loginStaff.getStaffid().intValue());
//		pj.setCreateTime(new Date());
//		pj.setStatus(0);
//		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
//		
//		if(pj.getProjectId() != null) {
//			//修改；
//			this.tblNbsjProjectMapper.updateEntity(pj);
//			//==附件，先删除 再重新添加
//			this.tblAttachmentMapper.deleteAttmentRelationProject(pj.getProjectId());
//			if (attids != null && !"".equals(attids)) {
//				String[] ids = attids.split(",");
//				for (String id : ids) {
//					this.tblAttachmentMapper.insertAttmentRelationProject(id, pj.getProjectId());
//				}
//			}
//		}else {
//			//新增；
//			this.tblNbsjProjectMapper.insertEntity(pj);
//			//==附件
//			if (attids != null && !"".equals(attids)) {
//				String[] ids = attids.split(",");
//				for (int i = 0; i < ids.length; i++) {
//					String id = ids[i];
//					this.tblAttachmentMapper.insertAttmentRelationProject(id, pj.getProjectId());
//				}
//			}
//		}

        resultMap.put("WorkReport", pj);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean projectDelete(Integer projectid, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblNbsjProject plan = this.tblNbsjProjectMapper.selectById(projectid);

        if (plan == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }

        if (plan.getExamineType().equals(TblNbsjProject.EXAMINETYPE1) || plan.getExamineType().equals(0)) {
            this.tblNbsjProjectMapper.deleteById(projectid);

            //删除小组
            this.tblNbsjProjectMapper.deleteLinkTeamById(projectid);

            //删除模板

            return ResponseFormat.retParam(1, 200, null);
        } else {
            return ResponseFormat.retParam(0, 50001, null);
        }


    }


    @Override
    public JsonBean findProjectDetail(String token, Integer projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);


        if (null == projectid) {
            TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, resultMap);
            }

            projectid = tnp.getProjectId();
        }


        TblNbsjProject plan = this.tblNbsjProjectMapper.selectPJById(projectid);

        //Integer aorgid = plan.getAuditOrgId();
        Integer astaffid = plan.getAuditStaffId();

        if (null == astaffid) {
            plan.setIsBmAudit(1);
        } else {
            plan.setIsBmAudit(0);
        }

        resultMap.put("pj", plan);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean projectRwfpPageList(String token, Integer pageNumber, Integer pageSize,
                                        TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
        tblnbsjProjectRwfpVo.setOrgId(t1.getOrgid().intValue());

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        tblnbsjProjectRwfpVo.setPmId(loginStaff.getStaffid().intValue());

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJRwfpListByPageInfo(pageInfo, tblnbsjProjectRwfpVo));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJRwfpCountByPageInfo(pageInfo, tblnbsjProjectRwfpVo));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean projectZxylPageList(String token, Integer pageNumber, Integer pageSize,
                                        TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblOrganizationUtil t1 = loginStaff.getCurrentOrg();
        tblnbsjProjectZXYLVo.setOrgId(t1.getOrgid().intValue());

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectPJZxylListByPageInfo(pageInfo, tblnbsjProjectZXYLVo));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectPJZxylCountByPageInfo(pageInfo, tblnbsjProjectZXYLVo));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean findSSProjectDetail(String token, Integer projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        if (null == projectid) {
            //==查询当前实施的项目！
            TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, resultMap);
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

        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean projectSS(String token, Integer projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblNbsjProject project = this.tblNbsjProjectMapper.getById(projectid + "");


        //当前实施项目
        TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (null != tnp) {
            Integer curr_projectid = tnp.getProjectId();
            if (projectid.equals(curr_projectid)) {
                JsonBean json = new JsonBean(0, "当前项目已实施！", null);
                return json;
            }
        }

        if (null != project && null != project.getUpdateStatus() && project.getUpdateStatus().equals(TblNbsjProject.UPDATEYES)) {
            if (project.getStatus() != null && project.getStatus() == 0) {
                return ResponseFormat.retParam(0, "该项目负责人未启动，不能实施", null);
            } else if (null != project.getStatus() && project.getStatus().equals(3) && project.getStatus().equals(4)) {
                return ResponseFormat.retParam(0, 80002, null);
            } else {
                if (null == project.getImplementTime()) {
                    project.setImplementTime(new Date());
                }
                this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(loginStaff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(loginStaff.getStaffid(),new BigDecimal( project.getProjectId()));
                this.tblNbsjProjectMapper.updateImplementTime(project.getProjectId(), DateUtil.parseDate(project.getImplementTime(), "yyyy-MM-dd HH:mm:ss"));
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            return ResponseFormat.retParam(0, 80003, null);
        }


    }


    @Override
    public TblNbsjProject getSSProjectDetail(String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        //==查询当前实施的项目！
        TblNbsjProject tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp != null) {
            return tnp;
        }

        return null;

    }


    @Override
    public TblNbsjProject getProjectById(Integer projectid) throws Exception {
        TblNbsjProject plan = this.tblNbsjProjectMapper.selectPJById(projectid);
        return plan;
    }


    @Override
    public JsonBean pjPmModi(String token, BigDecimal pmId, Integer projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //修改；
        this.tblNbsjProjectMapper.updatePjPm(pmId, projectid);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
//		resultMap.put("WorkReport",pj);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean pjStart(String token, Integer projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //修改；
        this.tblNbsjProjectMapper.updatePjStart(projectid);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
//		resultMap.put("WorkReport",pj);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean submitProjectArrpoval(String token, Integer projectId) throws Exception {
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
            if (project.getPmId() == null) {
                return ResponseFormat.retParam(0, "项目负责人未填写，无法提交审批", null);
            }

            if (project.getTempId() == null) {
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
            String busType = ProcessEnum.SJ_XMGL.name();
            Integer orgid = Integer.parseInt(user.getLinkOrg().getOrgid().toString());
            List<TblProcessSettingEntity> settings = processSettingMapper.selectByOrgid(busType, orgid);
            String setting = ProcessEnum.SJ_XMGL.name();
            if (settings != null && settings.size() > 0) {
                setting = settings.get(0).getModule();
                list = this.tblProcessAnalysisMapper.getByFlowSetting(setting);
            }

            HashMap<String, Object> fields = new HashMap<String, Object>();
            if (list != null && list.size() > 0) {
                for (TblProcessAnalysis tblAnalysis : list) {
                    TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(), projectId.toString());
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
                    } else {
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
            cir = tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_XMSP, project.getProjectCode(), project.getPrjoectName(), TblCirculation.URL_XMSP + project.getProjectId(), user.getStaffid(), processInstanceId, processDefinitionKey, projectId.toString());
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
                                TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, setting);
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
                if (cir != null && cir.getCyid() != null) {
                    this.tblCirculationMapper.deleteEntityById(cir.getCyid());
                }
                return ResponseFormat.retParam(0, 30002, resultMap);
            }
        } catch (Exception e) {
            resultMap.put("codes", "0");
            resultMap.put("msg", "流程提交失败！");
            e.printStackTrace();
            if (cir != null && cir.getCyid() != null) {
                this.tblCirculationMapper.deleteEntityById(cir.getCyid());
            }
            return ResponseFormat.retParam(0, 30002, resultMap);
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
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean getProjectApprovalInfo(String token, Integer projectId, String taskId, Integer cyId, String v) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
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
        if (null != staff.getRoleNames()) {
            if (staff.getRoleNames().indexOf("部门负责人") >= 0) {
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
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean dealProjectApporvalInfo(String token, Integer cyId, String taskId, String transition,
                                            String optDesc, String projectId, String processDefinitionId, String processInstanceId) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (StringUtils.isBlank(projectId)) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblNbsjProject project = tblNbsjProjectMapper.selectById(Integer.valueOf(projectId));
            if (taskId != null && !taskId.equals("")) {
                Map<String, Object> map = HttpClient.handleProcessJson(staff.getStaffid().toString(), transition, taskId);
                String object = (String) map.get("result");
                if (object != null && object.equals("true")) {
                    TblMyTask task = new TblMyTask();
                    TblMyTask oldtask = tblMyTaskService.findOndbyFrom(projectId);
                    TblProcessAnalysis findOnd = null;
                    if (oldtask == null) {
                        findOnd = tblProcessAnalysisService.findOndBytakdid("");
                    } else {
                        findOnd = tblProcessAnalysisService.findOnd(oldtask.getAnalid());
                    }
                    Integer number = 1;
                    String usertaskid = findOnd.getUsertaskid();
                    Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                    String blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
                    TblProcessAnalysis analysis = null;
                    if (transition != null && "退回".equals(transition)) {
                        analysis = tblProcessAnalysisService.findOndBytakdidstart("", findOnd.getProcessname());
                    } else {
                        analysis = tblProcessAnalysisService.findOndBytakdidAnId(blande, findOnd.getProcessname());
                    }
                    TblProcessAnalusisUser analysisUser = null;
                    if (analysis != null && analysis.getAnalid() != null) {
                        analysisUser = tblProcessAnalusisUserService.findOnd(analysis.getAnalid().toString(), projectId);
                    }
                    TblCirculation circulation = tblCirculationMapper.getOneBytaskid(projectId);
                    //查询执行人
                    String nextapprover = HttpClient.nextapprover(circulation.getBusinesskey());
                    System.out.println("nextapprover===================:" + nextapprover);

                    task.setApprovaldate(new Date());
                    if (staff.getTrole() != null && staff.getTrole().getRname() != null) {
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
                    if (transition.equals("完成")) {
                        task.setHandle("无");
                        circulation.setCystate("已完成");
                        project.setExamineType(TblNbsjProject.EXAMINETYPE4);
                    } else {
                        TblStaff findById = tblStaffMapper.getById(circulation.getCyStaffid().toString());//表单提交人
                        if (nextapprover.contains("bmfzr")) {
                            TblStaff bmfzr = tblStaffMapper.findByJobName("部门负责人", findById.getOrgid().toString());
                            task.setHandle(bmfzr.getRealname());
                        } else if (nextapprover.contains("fgld")) {
                            //分管领导参数
                            TblStaff fgld = tblStaffMapper.findByStaffManOrgs(findById.getOrgid().toString());
                            task.setHandle(fgld.getRealname());
                        } else if (nextapprover.contains("tcuserid")) {
                            //退回到创建人
                            task.setHandle(findById.getRealname());
                        } else {
                            task.setHandle(nextapprover);//角色
                        }
                        task.setAnalid(analysis.getAnalid().toString());
                    }

                    if (project.getExamineType() == 5) {
                        project.setExamineType(TblNbsjProject.EXAMINETYPE2);
                        circulation.setCystate("审批中");
                    }
                    if (transition != null && transition.equals("退回")) {
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
            return ResponseFormat.retParam(0, 3002, null);
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
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean jsfpRoleManageSave(String token, String ids, Integer teamId, Integer projectId) throws Exception {
        TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);//this.tblnbsjProjectService.getId(projectId);
        TblNbsjTeamstaffEntity teamStaff = this.tblNbsjTeamstaffMapper.selectById(teamId);//this.tblNbsjTeamstaffService.get(teamId);
        List<TblNbsjAuthorizationEntity> list = new ArrayList<TblNbsjAuthorizationEntity>();
        List<TblNbsjAuthorizationEntity> updatelist = new ArrayList<TblNbsjAuthorizationEntity>();

        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblStaff tblStaff = new TblStaff();
        tblStaff.setStaffid(staff.getStaffid());

        if (null != project && null != teamStaff) {
            String[] str_ids = ids.split(",");
            for (String str : str_ids) {

//                TblAduitProGramEntity aduitProGram = this.tblAduitProGramService.get(new BigDecimal(str));
                TblAduitProGramEntity aduitProGram = this.tblAduitProGramMapper.selectById(str);
//                TblNbsjAuthorizationEntity authorization = this.tblNbsjAuthorizationService.get(new BigDecimal(projectId), aduitProGram.getProgramId());、
//                TblNbsjAuthorizationEntity authorization = this.tblNbsjAuthorizationService.get(projectId, aduitProGram.getProgramId());
//                if (null != authorization) {
//                    authorization.setAduitProGram(aduitProGram);
//                    authorization.setAuthTime(new Date());
//                    authorization.setProject(project);
//                    authorization.setTeamStaff(teamStaff);
//                    authorization.setAuthStaff(tblStaff);
//                    updatelist.add(authorization);
//                } else {
//                    authorization = new TblNbsjAuthorizationEntity();
//                    authorization.setAduitProGram(aduitProGram);
//                    authorization.setAuthTime(new Date());
//                    authorization.setProject(project);
//                    authorization.setTeamStaff(teamStaff);
//                    authorization.setAuthStaff(tblStaff);
//                    list.add(authorization);
//                }
            }
            this.tblNbsjAuthorizationService.merge(updatelist);
            this.tblNbsjAuthorizationService.save(list);
//            this.tblNbsjProjectMapper.updateEntity(project);
//            tblnbsjProjectService.update(project);
            //全部分配完成
            BigDecimal tempId = project.getTbltemplete().getTempleteId();//审计模板id
            List<TblAduitProGramEntity> listTap = this.tblAduitProGramMapper.findByTMId(tempId);
            boolean is = false;
            if (listTap.size() >= 0) {
//                List<TblNbsjAuthorizationEntity> byProjectId = tblNbsjAuthorizationService.getByProjectId(projectId);
//                if (byProjectId.size() == listTap.size()) {
//                    is = true;
//                } else {
//                    is = false;
//                }
            }
            if (is == true) {
                //项目分配中
//                project.setFpStatus(2);//已分配
                this.tblNbsjProjectMapper.updateFpStatus(2, projectId);
            } else {
                //项目分配中
//                project.setFpStatus(1);//分配中
                this.tblNbsjProjectMapper.updateFpStatus(1, projectId);
            }

            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
        return ResponseFormat.retParam(0, 30001, null);
    }

    @Override
    public JsonBean auditPlanListPlanIdIn(String token, Integer pageNumber, Integer pageSize, String projectname) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }

        BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();

//	     TblNbsjProject sspj = getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());

        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        com.huabo.audit.util.PageInfo<TblNbsjProject> pageInfo = new com.huabo.audit.util.PageInfo<TblNbsjProject>();
//	     tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//	     pageInfo.setCondition(tblNbsjSheet);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblNbsjProjectMapper.selectAuditPlanListByPageInfo(pageInfo, projectname, orgid));
        pageInfo.setTotalRecord(this.tblNbsjProjectMapper.selectAuditPlanCountByPageInfo(pageInfo, projectname, orgid));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean auditPlanInAdd(String ids, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        if (StringUtils.isNotBlank(ids)) {
            String[] id = ids.split(",");

            TblNbsjProject selectProject = getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());

            if (null != selectProject) {
                for (String string : id) {
                    TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(Integer.valueOf(string));

                    if (null != project) {
                        TblNbsjProjectDataEntity data = new TblNbsjProjectDataEntity();
                        data.setOldProjectId(project.getProjectId());
                        data.setProjectid(selectProject.getProjectId());
//                        this.tblNbsjProjectDataService.save(data);

                        this.tblNbsjProjectDataMapper.saveOld(data);
                    }
                }
                return ResponseFormat.retParam(1, 200, resultMap);
            }
            return ResponseFormat.retParam(0, 30001, resultMap);
//            return JsonBean.error("未找到实施项目");
        }
        return ResponseFormat.retParam(0, 30001, resultMap);
//        return JsonBean.error("导入失败");

    }

    public JsonBean getGkProjectInfo(String token, Integer pageNumber, Integer pageSize, TblGkProjectVo project) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        project.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();

        com.github.pagehelper.PageInfo<Map<String, Object>> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjProjectMapper.getGkProjectInfo(pageInfo, project);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(pageInfo2.getList());
        pageInfo.setTotalRecord(Integer.valueOf(String.valueOf(pageInfo2.getTotal())));
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    public JsonBean getGkQuestionInfo(String token, Integer pageNumber, Integer pageSize, TblGkQuestionVo question) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<>(0);
        question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();

        com.github.pagehelper.PageInfo<Map<String, Object>> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        tblNbsjProjectMapper.getGkQuestionInfoList(pageInfo, question);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTotalRecord((int) pageInfo2.getTotal());
        pageInfo.setTlist(pageInfo2.getList());
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    public JsonBean getGkZgContentInfo(String token, Integer pageNumber, Integer pageSize, TblGkZgQuestionVo question) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<>(0);
        question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        com.github.pagehelper.PageInfo<Map<String, Object>> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        tblNbsjProjectMapper.getGkZgContentInfoList(pageInfo, question);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
        pageInfo.setTlist(pageInfo2.getList());
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    private TblNbsjAuditplan autoPlanSave(TblNbsjProject project, TblStaffUtil loginStaff, String token) throws Exception {
        TblNbsjAuditplan planone = new TblNbsjAuditplan();

        String pjType = project.getProjecttype();

        if (project.getProjecttype() != null && project.getProjecttype().equals("计划内")) {
            List<TblNbsjAuditplan> list = tblNbsjAuditplanMapper.selectPlanListByyear(project.getPlanYear(), "年度计划", loginStaff.getLinkOrg().getOrgid());
            if (list != null && list.size() > 0) {
                planone = list.get(0);
                return planone;
            } else {
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
                TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjTypeByName(audittype, loginStaff.getLinkDetp().getOrgid() + "");
//				TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjType(audittype);
                String auditCode = "";
                if (null != tblNbsjType) {
                    auditCode = tblNbsjType.getAuditCode() + "";
                }
                String planCode = project.getPlanYear() + "-" + loginStaff.getLinkOrg().getOrgnumber() + "-";//+auditCode+"-"
                //查询自增数据
                String no = this.tblNbsjAuditplanMapper.selectMaxPlanCode(planCode);
                if (no != null) {
//					 no = no.replace(planCode, "");
                } else {
                    no = "0";
                }
                planCode += (Integer.parseInt(no) + 1);
                //年度—公司编号—审计类型编号—自增数据
                planone.setPlancode(planCode);

                //计划名称//yyyy年度计划
                planone.setPlanname(project.getPlanYear() + "年度计划");
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
        } else {
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


    private TblNbsjProject xmlbUpdate(TblNbsjProject project, TblStaffUtil loginStaff, Integer pmid, String enddate, String startdate, Integer tempzyid,
                                      Integer tempid, String protempid, String pd_dx, String belongsto, Integer plan, String attids, String pjTeamJson, String token) throws Exception {

        TblOrganization organization1 = new TblOrganization();
        organization1.setOrgid(loginStaff.getCurrentOrg().getOrgid());

        if (project.getProjectId() != null) {
            // 修改开始
            TblNbsjProject tblnbsjProject = this.tblNbsjProjectMapper.selectPJById(project.getProjectId());//tblnbsjProjectService.getId(project.getProjectid().toString());
            try {
                tblnbsjProject.setPlanStartDate(startdate);
                tblnbsjProject.setPlanEndDate(enddate);
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
            tblnbsjProject.setOrgIds(project.getOrgIds());
            tblnbsjProject.setOrgIdNames(project.getOrgIdNames());
            tblnbsjProject.setAuditType(project.getAuditType());
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
            tblnbsjProject.setImplementaion(project.getImplementaion());
            tblnbsjProject.setAuditrequirements(project.getAuditrequirements());
            tblnbsjProject.setCospomsordepartment(project.getCospomsordepartment());
            tblnbsjProject.setImplementaionsteps(project.getImplementaionsteps());
            tblnbsjProject.setProjecttype(project.getProjecttype());
            tblnbsjProject.setExternAlassig(project.getExternAlassig());
            tblnbsjProject.setSjap(project.getSjap());
            tblnbsjProject.setXmgs(project.getXmgs());
            tblnbsjProject.setCospomsordepartmentstaffid(project.getCospomsordepartmentstaffid());
            tblnbsjProject.setCntType(project.getCntType());

            if (null != tempzyid) {
//                TblNbsjTempleteEntity templetezy = tblNbsjTempleteService.get(new BigDecimal(tempzyid));//指引模板
                TblNbsjTempleteEntity templetezy = tblNbsjTempleteMapper.findbyid(tempzyid + "");//指引模板
                tblnbsjProject.setTempzyId(tempzyid);//setTbltempletezy(templetezy);
            }
            tblnbsjProject.setCreateTime(new Date());
            tblnbsjProject.setUpdateStatus(TblNbsjProject.UPDATEYES);
            if (tblnbsjProject.getExamineType() != null) {
                tblnbsjProject.setExamineType(project.getExamineType());
            }
            if (pmid != null && pmid.toString().trim().length() > 0) {
                tblnbsjProject.setPmId(pmid);
            }
            //examinetype
           
            /*主审人
            String umpireid = request.getParameter("umpireid");
            TblStaff um = new TblStaff();
            um.setStaffid(new BigDecimal(umpireid));*/
            /*质控经理
            String controlid = request.getParameter("controlid");
            TblStaff con = new TblStaff();
            con.setStaffid(new BigDecimal(controlid));*/
            //tblnbsjProject.setTblcontrol(con);
            //tblnbsjProject.setTblumpe(um);
            //修改审计模板
            if (tempid != null) {
                // 如果审计模板再次修改
                //if(!tempid.equals(protempid)){
                TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(tempid + "");//tblNbsjTempleteService.get(new BigDecimal(tempid));
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
//					tblNbsjTempleteService.save(temp);
                tblNbsjTempleteMapper.insertEntity(temp);

//                tblnbsjProject.setTempId(temp.getTempleteId());
                tblnbsjProject.setOrgInfo(organization1);
                // if(!tempid.equals(tblnbsjProject.getTbltemplete().getTempleteId().toString())){
                List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(tempid + "");
                if (list != null && list.size() > 0) {
                    for (TblTargetTypeEntity tblTargetType : list) {
                        TblTargetTypeEntity target = new TblTargetTypeEntity();
                        target.setCreateTime(tblTargetType.getCreateTime());
                        target.setNbsjTemplete(temp);
                        target.setParentId(tblTargetType.getParentId());
                        target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);//TEMP_NUMBER 0 ;ZY_NUMBER 1
                        target.setTargetDesc(tblTargetType.getTargetDesc());
                        target.setTargetName(tblTargetType.getTargetName());
//							tblTargetTypeService.save(target);
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
//									tblAduitProGramService.save(prog);
                                tblAduitProGramMapper.insertEntity(prog);
                            }
                        }
                        capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
                    }
                }
                //删除之前分配的任务
                //删除任务

                List<TblNbsjAuthorizationEntity> nbsjauthorizatoin = this.tblNbsjAuthorizationService.getByProjectId(new BigDecimal(project.getProjectId()));
                if (null != nbsjauthorizatoin && nbsjauthorizatoin.size() > 0) {
                    for (TblNbsjAuthorizationEntity tblNbsjAuthorization : nbsjauthorizatoin) {
                        if (null != nbsjauthorizatoin) {
                            List<TblNbsjOperateEntity> listoper = tblNbsjOperateMapper.findByAuthId(tblNbsjAuthorization.getAuthId());//tblNbsjOperateService.findByAuthId(tblNbsjAuthorization.getAuthId());
                            TblNbsjOperateEntity oper = null;
                            if (listoper != null && listoper.size() > 0) {
                                oper = listoper.get(0);
                            }
                            tblNbsjOperateMapper.deleteByOpid(oper.getOperateid());
//									 tblNbsjOperateService.delete(oper);
//									 this.tblNbsjAuthorizationService.delete(tblNbsjAuthorization);
                            this.tblNbsjAuthorizationMapper.deleteByAuthid(tblNbsjAuthorization.getAuthId());
                        }
                    }
                }
                //一是已经是在项目里的，已经复制出来的，可以删除
                //二是没在项目里的，没复制出来的，不能删除
                if (protempid != null && StringUtils.isNotEmpty(protempid)) {
                    tblAduitProGramService.deleteZy(protempid);
                    tblTargetTypeService.deleteByZY(protempid);
                    tblNbsjTempleteService.delete(new BigDecimal(protempid));
                }
                //}
            }

            //项目对象
//            String pd_dx = request.getParameter("pd_dx");
//            String org = request.getParameter("belongsto");
            if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")) {
//                TblStaff staff = tblStaffMapper.getById(belongsto);//tblStaffNewService.get(new BigDecimal(belongsto));
                tblnbsjProject.setAuditStaffId(Integer.valueOf(belongsto));//.setTblnbsjstaffs(staff);
//                TblOrganization o = organizationService.findById(staff.getTblOrganization().getOrgid().toString());
//                tblnbsjProject.setTblnbsjorgs(null);
//                //tblnbsjProject.setTblnbsjorgs(o);
                tblnbsjProject.setAuditOrgId(null);
            } else {
                //tblnbsjProject.setAuditOrgId(Integer.valueOf(belongsto));
//                TblOrganization organization = organizationService.findById(org);
//                tblnbsjProject.setTblnbsjorgs(organization);
//                tblnbsjProject.setTblnbsjstaffs(null);
                tblnbsjProject.setAuditStaffId(null);
            }
            //被审计单位
            //String belongsto = request.getParameter("belongsto");
            //String belongstoText = request.getParameter("belongstoText");
            //project.setOrgids(belongsto);
            //project.setOrgidNames(belongstoText);

//            String plan = request.getParameter("plan");
            if (null != plan) {
//            	 Integer planid = Integer.valueOf(plan);
                TblNbsjAuditplan jplan = tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(plan);//tblnbsjauditPlanService.get(plan);
                tblnbsjProject.setPlanId(plan);//etTblnbsjPlan(jplan);
            }

//            tblnbsjProjectService.update(tblnbsjProject);
            tblNbsjProjectMapper.updateEntity(tblnbsjProject);

            Integer projectId = project.getProjectId();

            //保存附件
            this.tblAttachmentMapper.deleteAttmentRelationProject(projectId);
            if (StringUtils.isNotBlank(attids)) {
                String[] ids = attids.split(",");
                for (int i = 0; i < ids.length; i++) {
//            		TblAttachment att = attachmentService.findById(ids[i].trim());
                    String id = ids[i].trim();
                    //TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
//            		project.getTblprojectAtts().add(att);
                    this.tblAttachmentMapper.insertAttmentRelationProject(id, projectId);
                }
            }

            //==0.0
//            this.project_team_save(request, project.getProjectid().toString());
            //==项目小组
            if (null != pjTeamJson && !"".equals(pjTeamJson.trim())) {
                List<TblNbsjProjectTeamEntity> srList = new ArrayList<TblNbsjProjectTeamEntity>();
                srList = JSONObject.parseArray(pjTeamJson, TblNbsjProjectTeamEntity.class);
                for (TblNbsjProjectTeamEntity pjTeam : srList) {
                    //
                    String zystaffids = pjTeam.getZystaffids();
                    Integer leaderid = pjTeam.getLeaderId();
//    				Integer projectid = pjTeam.getProjectid();
                    this.tblNbsjProjectteamService.pjItemAdd(token, pjTeam, zystaffids, leaderid, projectId);
                }
            }

            return tblnbsjProject;
        } else {
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

            if (null != plan) {
//            	 Integer planid = Integer.valueOf(plan);
                TblNbsjAuditplan jplan = tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(plan);//tblnbsjauditPlanService.get(plan);
                project.setPlanId(plan);//setTblnbsjPlan(jplan);
            }
            TblStaff pm = new TblStaff();
            //  if(pmid!=null && pmid.length()>0) {
            //	 pm.setStaffid(new BigDecimal(pmid));
            //    project.setPmStaff(pm);//.setTblPm(pm);
            //}

            project.setOrgInfo(organization1);//.setTblorg(organization1);
//            if (tempid != null) {
            //第一次新增审计模板
            if (tempid != null) {
                //选择的方案模板(来自方案模板)
//				TblNbsjTempleteEntity templete = tblNbsjTempleteService.get(new BigDecimal(tempid));
                TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(tempid + "");
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
//				tblNbsjTempleteService.save(temp);
                tblNbsjTempleteMapper.insertEntity(temp);

//                project.setTempId(temp.getTempleteId());
                if (null != tempzyid) {
//					TblNbsjTempleteEntity templetezy = tblNbsjTempleteService.get(new BigDecimal(tempzyid));
                    TblNbsjTempleteEntity templetezy = tblNbsjTempleteMapper.findbyid(tempzyid + "");
                    project.setTempzyId(tempzyid);//setTbltempletezy(templetezy);
                }

                List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(tempid + "");
                if (list != null && list.size() > 0) {
                    for (TblTargetTypeEntity tblTargetType : list) {
                        TblTargetTypeEntity target = new TblTargetTypeEntity();
                        target.setCreateTime(tblTargetType.getCreateTime());
                        target.setNbsjTemplete(temp);
                        target.setParentId(tblTargetType.getParentId());
                        target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);
                        target.setTargetDesc(tblTargetType.getTargetDesc());
                        target.setTargetName(tblTargetType.getTargetName());
//						tblTargetTypeService.save(target);
                        tblTargetTypeMapper.insertEntity(target);
                        List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
                        if (grams != null && grams.size() > 0) {
                            for (TblAduitProGramEntity gram : grams) {
                                TblAduitProGramEntity prog = new TblAduitProGramEntity();
                                prog.setBioData(gram.getBioData());
//                                prog.setTargetId(target.getTargetId().intValue());
                                prog.setControl(gram.getControl());
                                prog.setBusinessType(gram.getBusinessType());
                                prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
                                prog.setCreateTime(gram.getCreateTime());
                                prog.setTempId(temp.getTempleteId());
                                prog.setRiskPoint(gram.getRiskPoint());
                                prog.setRiskSource(gram.getRiskSource());
                                prog.setSuditProcess(gram.getSuditProcess());
                                prog.setUpdateTime(gram.getUpdateTime());
//								tblAduitProGramService.save(prog);
                                tblAduitProGramMapper.insertEntity(prog);
                            }
                        }
                        capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
                    }
                }
            }
            //项目对象
//            String pd_dx = request.getParameter("pd_dx");
//            String org = request.getParameter("belongsto");
//            if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")){
//                TblStaff staff = tblStaffNewService.get(new BigDecimal(org));
//                project.setTblnbsjstaffs(staff);
//                TblOrganization o = organizationService.findById(staff.getTblOrganization().getOrgid().toString());
//                project.setTblnbsjorgs(null);
//            }else {
//                TblOrganization organization = organizationService.findById(org);
//                project.setTblnbsjorgs(organization);
//                project.setTblnbsjstaffs(null);
//            }
            if (StringUtils.isNotEmpty(pd_dx) && pd_dx.equals("yh")) {
                project.setAuditStaffId(Integer.valueOf(belongsto));//.setTblnbsjstaffs(staff);
            } else {
                // project.setAuditOrgId(Integer.valueOf(belongsto));
            }

            //被审计单位
            project.setStatus(0);
            project.setFpStatus(0);//项目任务分配状态
            project.setAssigbedControlTime(new Date());
            project.setAssigbedumpeTime(new Date());
            project.setAssigbedpmTime(new Date());
            project.setCreateTime(new Date());
            project.setUpdateStatus(TblNbsjProject.UPDATEYES);
//            TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
            project.setCreateStaffId(loginStaff.getStaffid().intValue());//setTblcreater(user);
            project.setCyrrentStatre(TblNbsjProject.NO_SELECT);
            project.setOrgId(loginStaff.getCurrentOrg().getOrgid().intValue());

            //项目编号
            String audittype = project.getAuditType();
            TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjTypeByName(audittype, loginStaff.getLinkDetp().getOrgid() + "");
//			TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjType(audittype);
            String auditCode = "";
            if (null != tblNbsjType) {
                auditCode = tblNbsjType.getAuditCode() + "";
            }
            String projectCode = project.getPlanYear() + "-" + loginStaff.getLinkOrg().getOrgnumber() + "-";//+auditCode+"-"
            //查询自增数据
            String no = this.tblNbsjAuditplanMapper.selectMaxProjectCode(projectCode);
            if (no != null) {
//				 no = no.replace(projectCode, "");
            } else {
                no = "0";
            }
            projectCode += (Integer.parseInt(no) + 1);
            //年度—公司编号—审计类型编号—自增数据
            project.setProjectCode(projectCode);

//          tblnbsjProjectService.save(project);
            tblNbsjProjectMapper.insertEntity(project);

            Integer projectId = project.getProjectId();

            //保存附件
            if (StringUtils.isNotBlank(attids)) {
                String[] ids = attids.split(",");
                for (int i = 0; i < ids.length; i++) {
//            		TblAttachment att = attachmentService.findById(ids[i].trim());
                    String id = ids[i].trim();
//            		TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
//            		project.getTblprojectAtts().add(att);
                    this.tblAttachmentMapper.insertAttmentRelationProject(id, projectId);
                }
            }

            //审计小组
//            this.project_team_save(request, project.getProjectid().toString());
            if (null != pjTeamJson && !"".equals(pjTeamJson.trim())) {
                List<TblNbsjProjectTeamEntity> srList = new ArrayList<TblNbsjProjectTeamEntity>();
                srList = JSONObject.parseArray(pjTeamJson, TblNbsjProjectTeamEntity.class);
                for (TblNbsjProjectTeamEntity pjTeam : srList) {
                    //
                    String zystaffids = pjTeam.getZystaffids();
                    Integer leaderid = pjTeam.getLeaderId();
                    //pjTeam.getProjectid();
                    this.tblNbsjProjectteamService.pjItemAdd(token, pjTeam, zystaffids, leaderid, projectId);
                }
            }

            return project;
        }
    }

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
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        return this.deleteRealtionAttInfo(attId);
    }

    private R deleteRealtionAttInfo(String attId) throws Exception {
        boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbsjProjectMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());

        return R.success();
    }


    @Override
    public JsonBean getTempeleBizCnt(String token, Integer templeteId) throws Exception {


        PageInfo<TblAduitProGramEntity> pageInfo = new PageInfo<TblAduitProGramEntity>();
        pageInfo.setPageSize(15);
        pageInfo.setCurrentPage(1);

        Integer cnt = this.tblAduitProGramMapper.selectCountByPageInfo(pageInfo, null, templeteId, null);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("cnt", cnt);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean getUserAuditItem(String token, BigDecimal staffid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        //查询人员-的参与的审计项目-状态为归档
        List<TblNbsjProject> list = this.tblNbsjProjectMapper.selectAuditItems(staffid);

        resultMap.put("data", list);

        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean getProjectPlanList(String token, Integer projectId, Integer planId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        List<TblNbsjAuditplan> list = new ArrayList<TblNbsjAuditplan>();

        if (null == planId || "".equals(planId)) {
            TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
            list = tblNbsjAuditplanMapper.selectPlanListByyear(project.getPlanYear(), "年度计划", loginStaff.getLinkOrg().getOrgid());
        } else {
            list = tblNbsjAuditplanMapper.selectPlanListPlanId(planId);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", list);

        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public Map<String, Object> viewOppsiteActiviti(Integer projectId, String businessKey) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
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
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblNbsjProject> pageInfo = new PageInfo<TblNbsjProject>();

        Integer finalYear = year;
        com.github.pagehelper.PageInfo<TblNbsjProject> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjProjectMapper.findProjectItemReport(pageInfo, finalYear);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(pageInfo2.getList());
        pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean findAuditTypeCount(String token, Integer year) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(0);
        List<TblNbsjProject> objs = tblNbsjProjectMapper.findAuditTypeCount(year);
        if (objs != null && objs.size() > 0) {
            for (TblNbsjProject obj : objs) {
                Map<String, Object> resultMap = new HashMap<String, Object>(0);
                resultMap.put("name", obj.getAuditType());
                resultMap.put("value", obj.getSl());
                list.add(resultMap);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", list);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean findReportYearList(String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        List<Integer> list = tblNbsjProjectMapper.findReportYearList();
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("list", list);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean findNbsjProjectCountByCompanyId(String token, Integer year) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(0);
        List<TblNbsjProject> objs = tblNbsjProjectMapper.findNbsjProjectCountByCompanyId(year);
        if (objs != null && objs.size() > 0) {
            for (TblNbsjProject obj : objs) {
                Map<String, Object> resultMap = new HashMap<String, Object>(0);
                resultMap.put("name", obj.getAuditOrgName());
                resultMap.put("value", obj.getSl());
                list.add(resultMap);
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", list);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean selectSjsQuestList(Date startdate, Date enddate, String projectid, String token) throws Exception {
        // TODO Auto-generated method stub
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // TODO Auto-generated method stub
        TblSjsQuestion q = new TblSjsQuestion();
        String dateSql1 = "";
        String dateSql2 = "";
        String dateSql3 = "";

        if (startdate != null) {
            if (DateBaseConfig.DATABASETYPE.equals("MySql")) {
                dateSql1 += " AND  STARTDATE >= '" + DateUtils.parseDate(startdate, "yyyy-MM-dd") + "'";
            } else {
                dateSql1 += " AND TO_DATE(TO_CHAR(STARTDATE,'YYYY-MM-DD'), 'YYYY-MM-DD') >= TO_DATE('" + DateUtils.parseDate(startdate, "yyyy-MM-dd") + "', 'YYYY-MM-DD')";
            }
        }
        if (enddate != null) {
            if (DateBaseConfig.DATABASETYPE.equals("MySql")) {
                dateSql1 += " AND ENDDATE<= '" + DateUtils.parseDate(enddate, "yyyy-MM-dd") + "'";
            } else {
                dateSql1 += " AND TO_DATE(TO_CHAR(ENDDATE,'YYYY-MM-DD'), 'YYYY-MM-DD') <= TO_DATE('" + DateUtils.parseDate(enddate, "yyyy-MM-dd") + "', 'YYYY-MM-DD')";
            }
        }
        if (StringUtils.isNotBlank(projectid)) {
            dateSql1 += " and projectid='" + projectid + "'";
        }
        //一、审计项目
        String sql1 = "SELECT "
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE  in ('贯彻落实国家重大政策措施审计','财政财务收支审计','固定资产投资审计','内部控制和风险管理审计','经济责任审计','信息系统审计','境外审计','其他')   " + dateSql1 + " ) value1,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='贯彻落实国家重大政策措施审计'  " + dateSql1 + " ) value2,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='财政财务收支审计'  " + dateSql1 + " ) value3,"
                + "(select nvl(COUNT(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='固定资产投资审计'  " + dateSql1 + " ) value4,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='内部控制和风险管理审计'  " + dateSql1 + " ) value5,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='经济责任审计'  " + dateSql1 + " ) value6,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='信息系统审计'  " + dateSql1 + " ) VALUE7,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6  AND AUDITTYPE='境外审计'   " + dateSql1 + ")  value8,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE ='其他'  " + dateSql1 + ") value9,"
                + "(select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND externalAssig='2'  " + dateSql1 + " ) value10,"
                + "(select nvl(sum(workingHours),0) from TBL_NBSJ_PROJECT where examinetype=6 " + dateSql1 + " ) value11 "
                + " from dual";
        System.out.println("项目SQL：" + sql1);

        JSONObject ob = tblNbsjProjectMapper.getObjBySql(sql1);
        if (ob != null) {
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
        if (startdate != null) {
            if (DateBaseConfig.DATABASETYPE.equals("MySql")) {
                dateSql2 += " AND  CREATETIEM >= '" + DateUtils.parseDate(startdate, "yyyy-MM-dd") + "'";
            } else {
                dateSql2 += " AND TO_DATE(TO_CHAR(CREATETIEM,'YYYY-MM-DD'), 'YYYY-MM-DD') >= TO_DATE('" + DateUtils.parseDate(startdate, "yyyy-MM-dd") + "', 'YYYY-MM-DD')";
            }
        }
        if (enddate != null) {
            if (DateBaseConfig.DATABASETYPE.equals("MySql")) {
                dateSql2 += " AND CREATETIEM<= '" + DateUtils.parseDate(enddate, "yyyy-MM-dd") + "'";
            } else {
                dateSql2 += " AND TO_DATE(TO_CHAR(CREATETIEM,'YYYY-MM-DD'), 'YYYY-MM-DD') <= TO_DATE('" + DateUtils.parseDate(enddate, "yyyy-MM-dd") + "', 'YYYY-MM-DD')";
            }
        }
        if (StringUtils.isNotBlank(projectid)) {
            dateSql2 += " and projectid='" + projectid + "'";
        }
        sql1 = "SELECT "
                + "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null  " + dateSql2 + " ) value1,"
                + "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '调整会计账目'  " + dateSql2 + ") value2,"
                + "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '收回资金' " + dateSql2 + ") value3,"
                + "(SELECT nvl(sum(ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '挽回损失'  " + dateSql2 + ") value4,"
                + "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '补缴税费' " + dateSql2 + ") value5,"
                + "(SELECT nvl(SUM (ZGMONTY),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '归还原资金渠道'  " + dateSql2 + ") value6,"
                + "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 1 and r.rectification is not null AND PROBLEMTYPE = '其他' " + dateSql2 + ") value7,"
                + "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null   " + dateSql2 + ") value8,"
                + "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null AND PROBLEMTYPE = '新制定制度' " + dateSql2 + " ) value9,"
                + "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null AND PROBLEMTYPE = '修订完善制度' " + dateSql2 + " ) value10,"
                + "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2 and r.rectification is not null AND PROBLEMTYPE = '优化完善业务流程' " + dateSql2 + " ) value11,"
                + "(SELECT nvl(SUM (ZGNUM),0) AS ZGNUMBER FROM TBL_NBSJ_ZGLS_PROBLEM  r  left join TBl_Nbsj_Sheet t on r.rectification=t.sheetid  WHERE DATATYPE = 2  and r.rectification is not null AND PROBLEMTYPE = '其他' " + dateSql2 + " ) value12"
                + " from DUAL";
        JSONObject ob2 = tblNbsjProjectMapper.getObjBySql(sql1);
        if (ob2 != null) {
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

        if (startdate != null) {
            if (DateBaseConfig.DATABASETYPE.equals("MySql")) {
                dateSql3 += " AND  CREATETIME >= '" + DateUtils.parseDate(startdate, "yyyy-MM-dd") + "'";
            } else {
                dateSql3 += " AND TO_DATE(TO_CHAR(CREATETIME,'YYYY-MM-DD'), 'YYYY-MM-DD') >= TO_DATE('" + DateUtils.parseDate(startdate, "yyyy-MM-dd") + "', 'YYYY-MM-DD')";
            }
        }
        if (enddate != null) {
            if (DateBaseConfig.DATABASETYPE.equals("MySql")) {
                dateSql3 += " AND CREATETIME<= '" + DateUtils.parseDate(enddate, "yyyy-MM-dd") + "'";
            } else {
                dateSql3 += " AND TO_DATE(TO_CHAR(CREATETIME,'YYYY-MM-DD'), 'YYYY-MM-DD') <= TO_DATE('" + DateUtils.parseDate(enddate, "yyyy-MM-dd") + "', 'YYYY-MM-DD')";
            }
        }
        if (StringUtils.isNotBlank(projectid)) {
            dateSql3 += " and projectid='" + projectid + "'";
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
        if (ob3 != null) {
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
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", q);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    public List<Object[]> getGkProjectInfoExport(String token, TblGkProjectVo project) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        project.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
        List<cn.hutool.json.JSONObject> list = tblNbsjProjectMapper.getGkProjectInfoExport(project);
        List<Object[]> contractlist = new ArrayList<Object[]>(0);
        Object[] objs = null;
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                cn.hutool.json.JSONObject obj = list.get(j);
                objs = new Object[11];
                objs[0] = obj.get("PROJECTCODE") != null ? obj.get("PROJECTCODE").toString() : "";
                objs[1] = obj.get("PRJOECTNAME") != null ? obj.get("PRJOECTNAME").toString() : "";
                objs[2] = obj.get("AUDITORGNAME") != null ? obj.get("AUDITORGNAME").toString() : "";
                objs[3] = obj.get("ORGNAME") != null ? obj.get("ORGNAME").toString() : "";
                objs[4] = obj.get("PMNAME") != null ? obj.get("PMNAME").toString() : "";
                objs[5] = obj.get("STATUS") != null ? obj.get("STATUS").toString() : "";
                objs[6] = obj.get("PLANYEAR") != null ? obj.get("PLANYEAR").toString() : "";
                objs[7] = obj.get("STARTDATE") != null ? obj.get("STARTDATE").toString() : "";
                objs[8] = obj.get("ENDDATE") != null ? obj.get("ENDDATE").toString() : "";
                objs[9] = obj.get("DAYNUMBER") != null ? obj.get("DAYNUMBER").toString() : "";
                objs[10] = obj.get("COSTS") != null ? obj.get("COSTS").toString() : "";
                contractlist.add(objs);
            }

        }
        return contractlist;
    }


    public List<Object[]> getGkQuestionInfoExport(String token, TblGkQuestionVo question) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
        List<cn.hutool.json.JSONObject> list = tblNbsjProjectMapper.getGkQuestionInfoExport(question);

        List<Object[]> contractlist = new ArrayList<Object[]>(0);
        Object[] objs = null;
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                cn.hutool.json.JSONObject obj = list.get(j);
                objs = new Object[8];
                objs[0] = obj.get("PROJECTCODE") != null ? obj.get("PROJECTCODE").toString() : "";
                objs[1] = obj.get("PRJOECTNAME") != null ? obj.get("PRJOECTNAME").toString() : "";
                objs[2] = obj.get("PLANYEAR") != null ? obj.get("PLANYEAR").toString() : "";
                objs[3] = obj.get("QUESTITLE") != null ? obj.get("QUESTITLE").toString() : "";
                objs[4] = obj.get("AUDITDISCOVERABLE") != null ? obj.get("AUDITDISCOVERABLE").toString() : "";
                objs[5] = obj.get("AUDITORGNAME") != null ? obj.get("AUDITORGNAME").toString() : "";
                objs[6] = obj.get("ORGNAME") != null ? obj.get("ORGNAME").toString() : "";
                objs[7] = obj.get("FINDREALNAME") != null ? obj.get("FINDREALNAME").toString() : "";
                contractlist.add(objs);
            }

        }
        return contractlist;
    }

    public List<Object[]> getGkZgContentInfoExport(String token, TblGkZgQuestionVo question) throws Exception {

        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        question.setORGID(loginStaff.getCurrentOrg().getOrgid().toString());
        List<cn.hutool.json.JSONObject> list = tblNbsjProjectMapper.getGkZgContentInfoExport(question);

        List<Object[]> contractlist = new ArrayList<Object[]>(0);
        Object[] objs = null;
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                cn.hutool.json.JSONObject obj = list.get(j);
                objs = new Object[9];
                objs[0] = obj.get("PROJECTCODE") != null ? obj.get("PROJECTCODE").toString() : "";
                objs[1] = obj.get("PRJOECTNAME") != null ? obj.get("PRJOECTNAME").toString() : "";
                objs[2] = obj.get("AUDITORGNAME") != null ? obj.get("AUDITORGNAME").toString() : "";
                objs[3] = obj.get("ORGNAME") != null ? obj.get("ORGNAME").toString() : "";
                objs[4] = obj.get("PLANYEAR") != null ? obj.get("PLANYEAR").toString() : "";
                objs[5] = obj.get("WTZS") != null ? obj.get("WTZS").toString() : "";
                objs[6] = obj.get("FQZGS") != null ? obj.get("FQZGS").toString() : "";
                objs[7] = obj.get("YZG") != null ? obj.get("YZG").toString() : "";
                objs[8] = obj.get("WZG") != null ? obj.get("WZG").toString() : "";
                contractlist.add(objs);
            }

        }
        return contractlist;

    }

}
