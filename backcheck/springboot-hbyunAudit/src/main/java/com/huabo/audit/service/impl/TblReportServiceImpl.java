package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.util.DateUtils;
import com.huabo.audit.util.PageResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
/*import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;*/
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.AgentEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblMyTask;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;
import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;
import com.huabo.audit.oracle.entity.TblProcessAnalysis;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblFlowMapper;
import com.huabo.audit.oracle.mapper.TblMyTaskMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjPlanProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffscoreDetailsMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffscoreMapper;
import com.huabo.audit.oracle.mapper.TblProcessAnalusisUserMapper;
import com.huabo.audit.oracle.mapper.TblProcessAnalysisMapper;
import com.huabo.audit.oracle.mapper.TblReportMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.vo.TblReportVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblCirculationService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblProcessAnalusisUserService;
import com.huabo.audit.service.TblProcessAnalysisService;
import com.huabo.audit.service.TblReportService;
import com.huabo.audit.util.HttpClient;
import com.huabo.audit.util.R;
import com.huabo.audit.util.PageInfo;

import cn.hutool.core.util.StrUtil;
import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;

@Service
public class TblReportServiceImpl implements TblReportService {
	
	@Autowired
	private TblReportMapper tblReportMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    @Resource
    private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    @Resource
	public TblNbsjPlanProjectMapper tblNbsjPlanProjectMapper;
    
    @Resource
   	public TblCirculationMapper tblCirculationMapper;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
/*    @Resource
	private RuntimeService runtimeService;
	@Resource
	private HistoryService historyService;
	@Resource
	private RepositoryService repositoryService;
	@Resource 
	private TaskService taskService;*/
    @Resource
    private ProcessService processService;
    
    @Resource
    private TblAuditOptionService tblAuditOptionService;
    
    @Resource
    private TblNbsjStaffscoreMapper tblNbsjStaffscoreMapper;
    
    @Resource
    public TblStaffMapper tblStaffMapper;
    
    @Resource
	private TblFlowMapper tblFlowMapper;
    
    @Resource
	private TblProcessAnalysisMapper tblProcessAnalysisMapper;
    
    @Resource
	private TblProcessAnalusisUserMapper tblProcessAnalusisUserMapper;
    
    @Resource
	private TblCirculationService tblCirculationService;
    
    @Resource
	private TblMyTaskMapper tblMytaskMapper;
    
    @Autowired
    private TblProcessAnalysisService tblProcessAnalysisService;
    
    @Resource
    private TblProcessAnalusisUserService tblProcessAnalusisUserService;
    
    @Resource
    private TblNbsjStaffscoreDetailsMapper tblNbsjStaffscoreDetailsMapper;
    
    
    @Resource
    private UserProvider userProvider;
    
    
	@Override
	public void add(TblReportEntity tblReport) {
		// TODO Auto-generated method stub

	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void detele(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public TblReportEntity findByid(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(TblReportEntity tblReport) {
		// TODO Auto-generated method stub

	}

	@Override
	public List search(String name, String time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblReportEntity> findReportByProjectId(Integer projectid, String reportType, Integer reportStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblReportEntity> findNoReportByProjectId(Integer projectid) {

		List<TblReportEntity> list = this.tblReportMapper.findNoReportByProjectId(projectid);
		
		if(null!=list&&list.size()!=0) {
			return list;
		}
		return null;
		
	}

	
	
	//==
	@Override
	public JsonBean zdyPageList(String token, Integer pageNumber, Integer pageSize, TblReportVo tblReportVo,BigDecimal projectId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	
    	if(null == projectId) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		projectId = tnp.getProjectId();
    	}
//		//分页查询
//		Example example = new Example(TblReportEntity.class);
//		Example.Criteria criteria = example.createCriteria();
//		//查询 需要判空 在查询 精准查询
//		if (Objects.nonNull(projectId)) {
//			criteria.andEqualTo("projectId", projectId);
//		}
//		//查询 需要判空 在查询 精准查询
//		if (org.apache.commons.lang3.StringUtils.isNotBlank(tblReportVo.getType())) {
//			criteria.andEqualTo("type", tblReportVo.getType());
//		}
//		//查询 需要判空 在查询 模糊查询
//		if (org.apache.commons.lang3.StringUtils.isNotBlank(tblReportVo.getReportname())) {
//			criteria.andLike("reportname", "%" + tblReportVo.getReportname() + "%");
//		}
//		//查询 需要判空 在查询 某个字段 大于且等于 某个时间
//		if (tblReportVo.getStartDate() != null && tblReportVo.getStartDate() != "") {
//			criteria.andGreaterThanOrEqualTo("reporttime", DateUtils.parse(tblReportVo.getStartDate()));
//		}
//		//查询 需要判空 在查询 某个字段 小于且等于 某个时间
//		if (tblReportVo.getEndDate() != null && tblReportVo.getEndDate() != "") {
//			criteria.andLessThanOrEqualTo("reporttime", DateUtils.parse(tblReportVo.getEndDate()));
//		}
//
//		//创建时间倒序
//		example.setOrderByClause(" reportid DESC");
//
//		//单表分页
//		PageInfo<TblReportEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
//				.doSelectPageInfo(() -> tblReportMapper.selectByExample(example));
//		PageResult<TblReportEntity> build = new PageResult<TblReportEntity>().build(pageInfo);
    	
    	PageInfo<TblReportEntity> pageInfo = new PageInfo<TblReportEntity>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblReportMapper.selectListByPageInfo(pageInfo,tblReportVo,orgid.intValue(),projectId.intValue(),loginStaff));
    	pageInfo.setTotalRecord(this.tblReportMapper.selectCountByPageInfo(pageInfo,tblReportVo,orgid.intValue(),projectId.intValue(),loginStaff));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean zdyAdd(TblReportEntity report, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
		report.setOrgid(orgid);
		
		
		//==
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		BigDecimal projectId = tnp.getProjectId();
		
		report.setProjectId(projectId);
		
		
//		Integer count = this.tblReportMapper.selectPlanCodeByOrgid(report);
//		if(count > 0) {
//			return ResponseFormat.retParam(0,202,null);
//		}
    	
//		report.setCreatestaffid(loginStaff.getStaffid()+"");
		report.setSendTime(new Date());
		report.setReportstatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(report.getReportid() != null) {
			//修改；
			//this.tblReportMapper.updateEntity(report);
			tblReportMapper.updateByPrimaryKeySelective(report);
			//==附件，先删除 再重新添加
			this.tblReportMapper.deleteAttmentRelationREPORT(report.getReportid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblReportMapper.insertAttmentRelationREPORT(id, report.getReportid());
				}
			}
		}else {
			//新增；
			//this.tblReportMapper.insertEntity(report);
			report.setReportid(RandomUtil.uuBigDecimalId());
			report.setCreatestaffid(loginStaff.getStaffid());
			tblReportMapper.insertSelective(report);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblReportMapper.insertAttmentRelationREPORT(id, report.getReportid());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("report",report);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean opinion_file(BigDecimal reportid, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//==附件，先删除 再重新添加
		this.tblReportMapper.deleteAttRelaOpinionREPORT(reportid);
		if (attids != null && !"".equals(attids)) {
			String[] ids = attids.split(",");
			for (String id : ids) {
				this.tblReportMapper.insertAttRelaOpinionREPORT(id, reportid);
			}
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean final_file(BigDecimal reportid, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//==附件，先删除 再重新添加
		this.tblReportMapper.deleteAttRelaFinalREPORT(reportid);
		if (attids != null && !"".equals(attids)) {
			String[] ids = attids.split(",");
			for (String id : ids) {
				this.tblReportMapper.insertAttRelaFinalREPORT(id, reportid);
			}
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean zdyDelete(BigDecimal reportid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblReportEntity plan = this.tblReportMapper.selectById(reportid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblReportMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(0,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblReportMapper.deleteById(reportid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findZdyReportDetail(String token, BigDecimal reportid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblReportEntity plan = this.tblReportMapper.selectById(reportid);
		resultMap.put("report", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	/**
	 * 报告编制-导出
	 */
	@Override
	public TblReportEntity findReportByreportId(String reportid){
		return this.tblReportMapper.getById(reportid);
	}

	@Override
	public JsonBean submitReportFhApproval(String token, BigDecimal reportid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	
    	TblReportEntity report = tblReportMapper.selectById(reportid);
        TblCirculation cir = new TblCirculation();
        try {
           
           cir.setCytype(TblCirculation.TYPE_SJBGFH);
           cir.setCycode(report.getReportid().toString());
           cir.setCyname(report.getReportname());
           cir.setCydate(new Date());
           cir.setCystate(TblCirculation.STATE_FQ);
           cir.setCyurl(TblCirculation.URL_SJBGFH+report.getReportid());
           cir.setCyStaffid(user.getStaffid().toString());
           this.tblCirculationMapper.saveTblCirculation(cir);
           
           Map<String, Object> variables= new HashMap<String, Object>();
           variables.put(ProcessVariableEnum.model.toString(), cir);
   			AgentEntity ae = new AgentEntity();
   			ae.setReviewer(report.getFhstaffid());
   			if(report.getFhstaffid()==null) {
   				return ResponseFormat.retParam(0,"请添加报告复核人",null);
   			}
		   variables.put(ProcessVariableEnum.agent.toString(), ae);
         /*  ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_SJBGFH.name(),cir.getCyid().toString(), variables);
           Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
           cir.setTaskid(task.getId());
           //流程定义Id
           String businessKey = processInstance.getBusinessKey();
           String definitionId = processInstance.getProcessDefinitionId();
           cir.setBusinesskey(businessKey);
           cir.setDefinitionid(definitionId);
           this.tblCirculationMapper.updateCirculationInfoById(cir);
           taskService.complete(task.getId());*/
           report.setReportstatus(TblReportEntity.FHZ);
           report.setRepdesc(null);
           tblReportMapper.updateEntity(report);
        } catch (Exception e) {
        	e.printStackTrace();
        	if(cir != null && cir.getCyid() != null) {
        		this.tblCirculationMapper.deleteEntityById(cir.getCyid());
        	}
        	return ResponseFormat.retParam(0,30002,null);
        }	
        return ResponseFormat.retParam(1,200,null);

	}
	@Override
	public JsonBean submitReportZqyjApproval(String token, BigDecimal reportid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblReportEntity report = tblReportMapper.selectById(reportid);
		TblCirculation cir = new TblCirculation();
		try {
			
			cir.setCytype(TblCirculation.TYPE_SJBGZQYJ);
			cir.setCycode(report.getReportid().toString());
			cir.setCyname(report.getReportname());
			cir.setCydate(new Date());
			cir.setCystate(TblCirculation.STATE_FQ);
			cir.setCyurl(TblCirculation.URL_SJBGZQYJ+report.getReportid());
			cir.setCyStaffid(user.getStaffid().toString());
			this.tblCirculationMapper.saveTblCirculation(cir);
			
			Map<String, Object> variables= new HashMap<String, Object>();
			variables.put(ProcessVariableEnum.model.toString(), cir);
			AgentEntity ae = new AgentEntity();
			ae.setTakeAdvice(report.getZqyjstaffid().toString());
			if(report.getFhstaffid()==null) {
				return ResponseFormat.retParam(0,"请添加报告征求意见人",null);
			}
			variables.put(ProcessVariableEnum.agent.toString(), ae);
		/*	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_ZQYJ.name(),cir.getCyid().toString(), variables);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			cir.setTaskid(task.getId());
			//流程定义Id
			String businessKey = processInstance.getBusinessKey();
			String definitionId = processInstance.getProcessDefinitionId();
			cir.setBusinesskey(businessKey);
			cir.setDefinitionid(definitionId);
			this.tblCirculationMapper.updateCirculationInfoById(cir);
			taskService.complete(task.getId());*/
			report.setReportstatus(TblReportEntity.ZQYJ);
			report.setRepdesc(null);
			tblReportMapper.updateEntity(report);
		} catch (Exception e) {
			e.printStackTrace();
			if(cir != null && cir.getCyid() != null) {
				this.tblCirculationMapper.deleteEntityById(cir.getCyid());
			}
			return ResponseFormat.retParam(0,30002,null);
		}	
		return ResponseFormat.retParam(1,200,null);
		
	}
	public JsonBean submitReportSpApproval(String token, BigDecimal reportid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblReportEntity report = tblReportMapper.selectById(reportid);
		TblCirculation cir = new TblCirculation();
		try {
			
			cir.setCytype(TblCirculation.TYPE_SJBG);
			cir.setCycode(report.getReportid().toString());
			cir.setCyname(report.getReportname());
			cir.setCydate(new Date());
			cir.setCystate(TblCirculation.STATE_FQ);
			cir.setCyurl(TblCirculation.URL_SJBG+report.getReportid());
			cir.setCyStaffid(user.getStaffid().toString());
			this.tblCirculationMapper.saveTblCirculation(cir);
			
			Map<String, Object> variables= new HashMap<String, Object>();
			variables.put(ProcessVariableEnum.model.toString(), cir);
		/*	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_SJBG.name(),cir.getCyid().toString(), variables);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			cir.setTaskid(task.getId());
			//流程定义Id
			String businessKey = processInstance.getBusinessKey();
			String definitionId = processInstance.getProcessDefinitionId();
			cir.setBusinesskey(businessKey);
			cir.setDefinitionid(definitionId);
			this.tblCirculationMapper.updateCirculationInfoById(cir);
			taskService.complete(task.getId());*/
	        report.setRepdesc(null);
			report.setReportstatus(TblReportEntity.SPZ);
			tblReportMapper.updateEntity(report);
		} catch (Exception e) {
			e.printStackTrace();
			if(cir != null && cir.getCyid() != null) {
				this.tblCirculationMapper.deleteEntityById(cir.getCyid());
			}
			return ResponseFormat.retParam(1,30002,null);
		}	
		return ResponseFormat.retParam(1,200,null);
		
	}
	@Override
	public JsonBean getReportFhApprovalInfo(String token, BigDecimal reportid, String taskId, BigDecimal cyId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	TblReportEntity report = tblReportMapper.selectById(reportid);
        List<String> btnList = null;
    	if(report!=null){
    		if(StringUtils.isNotBlank(taskId) && (report.getReportstatus()!=TblReportEntity.FHTZ||report.getReportstatus()!=TblReportEntity.XTZ)){
    			btnList = processService.getButtonsForTransition(taskId);
    			resultMap.put("btnList", btnList);
    		}
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(report.getReportid().toString(),cyId);
    		if (cyId != null){
    			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
    			resultMap.put("cy", cy);
    		}
    		resultMap.put("taskId", taskId);
    		resultMap.put("aoptionList", ao);
    		resultMap.put("cyId", cyId);
    		resultMap.put("report", report);
    	}
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean dealReportFhApporvalInfo(String token, BigDecimal cyId, String taskId, String transition,
			String optDesc, String reportid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		JsonBean jsonBean = null;
		TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
		if (null != cy) {
			Map<String, Object> variables = new HashMap<>();
			variables.put(ProcessVariableEnum.model.toString(), cy);
			if (StringUtils.isNotBlank(transition)) {
				variables.put(ProcessVariableEnum.transition.toString(), transition);
			}
			try {
				/*taskService.claim(taskId, staff.getStaffid().toString());
				taskService.complete(taskId, variables);*/
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
			opt.setRelationId(new BigDecimal(reportid));
			opt.setOptState(transition);
			jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
		}
		return jsonBean;
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
        this.tblReportMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	
	
	public JsonBean submitAuditUserApproval(String token, BigDecimal staffid) throws Exception {
         try {
         	TblStaffUtil user = userProvider.get();
             if (user == null) {
            	 return ResponseFormat.retParam(0,20006,null);
             }
         	
            TblStaff staff = tblStaffMapper.selectById(staffid);
         	boolean flag = false;
         	Integer code = null;
         	if(staff.getAprStatus() != null) {
	         	switch (staff.getAprStatus()) {
						case 1:
							code = 30007;
			                flag = true;
							break;
						case 2:
							code = 30008;
		                    flag = true;
		                    break;
						case 3:
							code = 30009;
		                    flag = true;
		                    break;
						case 4:
						case 5:
						case 6:
							code = 30010;
		                    flag = true;
		                    break;
					}
	             if(flag) {
	            	 return ResponseFormat.retParam(0,code,null);
	             }
         	}
				List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(ProcessEnum.SJYH.name());
				HashMap<String, Object> fields = new HashMap<String, Object>();
				if (list != null && list.size() > 0) {
					for (TblProcessAnalysis tblAnalysis : list) {
						TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(), staffid.toString());
						if (analysisUser == null) {
							analysisUser = new TblProcessAnalusisUser();
							analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
							analysisUser.setFromid(staffid.toString());
							analysisUser.setSpdate(new Date());
							if (tblAnalysis.getUserid() != null) {
								analysisUser.setStaffid(user.getRealname());
							} else {
								analysisUser.setStaffid(tblAnalysis.getRolename());
							}
                         //合同订立流程参数
                         //提交人参数
                         if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                             fields.put("tcuserid", user.getStaffid().toString());
                             analysisUser.setStaffid(tblAnalysis.getRolename());
                         } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("bmfzr")) {
                             //部门负责人参数
                             TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                             if (bmfzr == null) {
                            	 return ResponseFormat.retParam(0,30011,null);
                             }
                             fields.put("bmfzr", bmfzr.getStaffid().toString());
                             System.out.println("bmfzr:" + bmfzr.getStaffid());
                         }else if(tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("fgld")) {
								//分管领导参数
								TblStaff fgld = tblStaffMapper.findByStaffFgOrgs(user.getLinkDetp().getOrgid().toString());
								if (fgld == null) {
									 return ResponseFormat.retParam(0,30012,null);
                                }
								fields.put("fgld", fgld.getStaffid().toString());
						 }else {
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
	                                	return ResponseFormat.retParam(0,30011,null);
	                                }
	                                fields.put("bmfzr", bmfzr.getStaffid().toString());
	                                System.out.println("bmfzr:" + bmfzr.getStaffid());
	                            }else if(tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("fgld")) {
									//分管领导参数
									TblStaff fgld = tblStaffMapper.findByStaffFgOrgs(user.getLinkDetp().getOrgid().toString());
									if (fgld == null) {
										 return ResponseFormat.retParam(0,30012,null);
	                                }
									fields.put("fgld", fgld.getStaffid().toString());
								} else {
	                                //除此之外的参数都设置为当前提交人
	                                if (StrUtil.isNotBlank(tblAnalysis.getUserid())) {
	                                    fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
	                                }
	                            }
						}
					}
				}

				JSONObject jsonObject = JSONObject.fromObject(fields);
				Map<String, Object> map = HttpClient.startProcessAll(ProcessEnum.SJYH.name(), jsonObject.toString());
				staff.setAprStatus(TblStaff.SPZ);
				tblStaffMapper.updateStaff(staff);
				String prcessresult = (String) map.get("result");
				String processInstanceId = (String) map.get("processInstanceId");
				String processDefinitionKey = (String) map.get("processDefinitionKey");
				TblCirculation cir = null;
				
				cir = this.tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_SJYH,staff.getUsername(), staff.getRealname() , TblCirculation.URL_SJYH + staffid, user.getStaffid(), processInstanceId, processDefinitionKey, staffid.toString());
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
								 TblProcessAnalysis analysis = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, ProcessEnum.SJYH.name());
                                 Integer number = 1;
                                 String usertaskid = analysis.getUsertaskid();
                                 Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                                 blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
									TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, ProcessEnum.SJYH.name());
									TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), staffid.toString());
									task.setFromid(staffid.toString());
									task.setApprover(user.getRealname());
									task.setUsrid(user.getStaffid().toString());
									task.setExamination("提交审批");
									task.setProcessName(ProcessEnum.SJYH.name());
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
					 return ResponseFormat.retParam(1,200,null);
				} else {
					 return ResponseFormat.retParam(0,30002,null);
				}
			} catch (Exception e) {
				 return ResponseFormat.retParam(0,30002,null);
			}
	}
	
	@Override
	public JsonBean dealAuditUserApporvalInfo(String token, String staffid,String taskId,BigDecimal cyId,String transitionName,String examination,String processInstanceId,String processDefinitionId)throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			 TblStaff spstaff = tblStaffMapper.selectById(new BigDecimal(staffid));
			if(taskId!=null && !taskId.equals("")){
				Map<String, Object> map = HttpClient.handleProcessJson(staff.getStaffid().toString(),transitionName,taskId);
				String object = (String) map.get("result");
				if(object!=null && object.equals("true")){
					TblMyTask task =new TblMyTask();
					List<TblMyTask> oldtaskList = this.tblMytaskMapper.findOndbyFrom(staffid);
					TblMyTask oldtask = oldtaskList.get(0);
					TblProcessAnalysis findOnd = null;
					if(oldtask == null){
						findOnd = this.tblProcessAnalysisMapper.findOndBytakdid("");
					}else{
						findOnd = this.tblProcessAnalysisMapper.findOndAnalysis(oldtask.getAnalid());
					}
					
					Integer number=1;
					String usertaskid = findOnd.getUsertaskid();
					Integer num=Integer.parseInt(usertaskid.substring(usertaskid.length()-1,usertaskid.length()))+number;
					String blande=usertaskid.substring(0,usertaskid.length()-1)+num;
					TblProcessAnalysis analysis =null;
					if(transitionName!=null && transitionName.equals("退回")){
						analysis = tblProcessAnalysisService.findOndBytakdidstart("",findOnd.getProcessname());
					}else{
						analysis = tblProcessAnalysisService.findOndBytakdidAnId(blande,findOnd.getProcessname());
					}
					TblProcessAnalusisUser analysisUser = null;
					if(analysis!=null && analysis.getAnalid()!=null){
						analysisUser = tblProcessAnalusisUserService.findOnd(analysis.getAnalid().toString(), staffid);
					}
					TblCirculation circulation = tblCirculationService.getOneBytaskid(staffid);
					String nextapprover = HttpClient.nextapprover(circulation.getBusinesskey());
					task.setApprovaldate(new Date());
					if(staff.getTrole()!=null && staff.getTrole().getRname()!=null){
						task.setApprovalrole(staff.getTrole().getRname());
					}
					task.setApprover(staff.getRealname());
					task.setExamination(examination);
					task.setFromid(staffid);
					task.setProcessDefinitionId(processDefinitionId);
					task.setUsrid(staff.getStaffid().toString());
					task.setCirid(cyId.toString());
					task.setResult(transitionName);
					task.setProcessName(oldtask.getProcessName());
					task.setTaskId(taskId);
					task.setProcessInstanceId(processInstanceId);
					if(StringUtils.isBlank(nextapprover)){//transitionName.equals("完成")||
						task.setHandle("无");
						circulation.setCystate("已完成");
						spstaff.setAprStatus(TblStaff.YWC);
					}else{
						//退回or通过
						TblStaff findById = tblStaffMapper.getById(circulation.getCyStaffid());//表单提交人
						if(nextapprover.contains("bmfzr")) {
							TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(staff.getLinkDetp().getOrgid().toString());
							task.setHandle(bmfzr.getRealname());
						}else if (nextapprover.contains("fgld")) {
                        	//分管领导
                            TblStaff fgld = tblStaffMapper.findByStaffFgOrgs(findById.getOrgid().toString());
                            task.setHandle(fgld.getRealname());
                        }else if(nextapprover.contains("tcuserid")) {
							//退回到创建人
							task.setHandle(findById.getRealname());
						}else {
							//角色
							task.setHandle(nextapprover);
						}
					}
					if(spstaff.getAprStatus() ==2 ){
						spstaff.setAprStatus(TblStaff.SPZ);
						circulation.setCystate("审批中");
					}
					if(transitionName!=null && transitionName.equals("退回")){
						circulation.setCystate("需调整");
						spstaff.setAprStatus(TblStaff.XTZ);
					}

					if(transitionName!=null && transitionName.equals("终止")){
						circulation.setCystate("终止");
						task.setHandle("无");
						spstaff.setAprStatus(TblStaff.YZZ);
					}
					tblMytaskMapper.insertMyTaskSetting(task);
					tblCirculationService.upateTblCirculation(circulation);
					tblStaffMapper.updateStaff(spstaff);
				}
	            return ResponseFormat.retParam(1,200,null);
			}else{
				return ResponseFormat.retParam(0,30002,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,30002,null);
		}
	}
	
	@Override
	public JsonBean getAuditUserApprovalInfo(String token, BigDecimal staffid, BigDecimal cyId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblStaff spstaff = tblStaffMapper.selectById(staffid);
		TblCirculation cy = this.tblCirculationMapper.selectCiculaInfoById(staffid.toString());
		
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
        resultMap.put("spstaff", spstaff);
        if(null != staff.getRoleNames()) {
        	if(staff.getRoleNames().indexOf("部门负责人")>=0) {
        		resultMap.put("is_bmfzr", "1");
            }
        }
        resultMap.put("cz", "sp");
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	
	
	//////////////////////////////////////////
	
	public JsonBean submitStaffScoreApproval(String token, Integer staffScoreid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblNbsjStaffscore tnss=tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(new BigDecimal(staffScoreid));
		
		TblCirculation cir = new TblCirculation();
		try {
			
			cir.setCytype(TblCirculation.TYPE_PJSH);
			cir.setCycode(tnss.getStaffScoreid().toString());
			cir.setCyname(tnss.getAuditor().getRealname());
			cir.setCydate(new Date());
			cir.setCystate(TblCirculation.STATE_FQ);
			cir.setCyurl(TblCirculation.URL_PJSH+tnss.getStaffScoreid());
			cir.setCyStaffid(user.getStaffid().toString());
			this.tblCirculationMapper.saveTblCirculation(cir);
			
			Map<String, Object> variables= new HashMap<String, Object>();
			variables.put(ProcessVariableEnum.model.toString(), cir);
		/*	ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.PJSH.name(),cir.getCyid().toString(), variables);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			cir.setTaskid(task.getId());
			//流程定义Id
			String businessKey = processInstance.getBusinessKey();
			String definitionId = processInstance.getProcessDefinitionId();
			cir.setBusinesskey(businessKey);
			cir.setDefinitionid(definitionId);
			this.tblCirculationMapper.updateCirculationInfoById(cir);
			taskService.complete(task.getId());*/
//			staff.setRepdesc(null);
			tnss.setStatus(TblNbsjStaffscore.STATE_SP);
			tblNbsjStaffscoreMapper.updateEntity(tnss);
		} catch (Exception e) {
			e.printStackTrace();
			if(cir != null && cir.getCyid() != null) {
				this.tblCirculationMapper.deleteEntityById(cir.getCyid());
			}
			return ResponseFormat.retParam(0,30002,null);
		}
		return ResponseFormat.retParam(1,200,null);
		
	}
	
	@Override
	public JsonBean dealStaffScoreApporvalInfo(String token, Integer cyId, String taskId, String transition,
			String optDesc, String staffScoreid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		JsonBean jsonBean = null;
		TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
		if (null != cy) {
			Map<String, Object> variables = new HashMap<>();
			variables.put(ProcessVariableEnum.model.toString(), cy);
			if (StringUtils.isNotBlank(transition)) {
				variables.put(ProcessVariableEnum.transition.toString(), transition);
			}
			try {
				/*taskService.claim(taskId, staff.getStaffid().toString());
				taskService.complete(taskId, variables);*/
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
			opt.setRelationId(new BigDecimal(staffScoreid));
			opt.setOptState(transition);
			jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
		}
		return jsonBean;
	}
	
	@Override
	public JsonBean getStaffScoreApprovalInfo(String token, BigDecimal staffScoreid, BigDecimal cyId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjStaffscore tnss=tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(staffScoreid);
		if(tnss!=null) {
			List<TblNbsjStaffscoreDetails> details = tblNbsjStaffscoreDetailsMapper.findTblNbsjStaffscoreDetails(tnss.getStaffScoreid());
			tnss.setTblNbsjStaffscoreDetails(details);
		}
		TblCirculation cy = this.tblCirculationMapper.selectCiculaInfoById(staffScoreid.toString());
		
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
        resultMap.put("tnss", tnss);
        if(null != staff.getRoleNames()) {
        	if(staff.getRoleNames().indexOf("部门负责人")>=0) {
        		resultMap.put("is_bmfzr", "1");
            }
        }
        resultMap.put("cz", "sp");
        /*List<String> btnList = null;
    	if(tnss!=null){
    		if(StringUtils.isNotBlank(taskId) && (tnss.getStatus()!=TblNbsjStaffscore.XTZ)){
    			btnList = processService.getButtonsForTransition(taskId);
    			resultMap.put("btnList", btnList);
    		}
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(tnss.getStaffScoreid().toString(),cyId);
    		if (cyId != null){
    			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
    			resultMap.put("cy", cy);
    		}
    		resultMap.put("taskId", taskId);
    		resultMap.put("aoptionList", ao);
    		resultMap.put("cyId", cyId);
    		resultMap.put("tnss", tnss);
    	}*/
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
    public Map<String, Object> submitStaffScoreApproval(BigDecimal staffScoreid, String examination, String token) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
            	
            	TblStaffUtil user = userProvider.get();
                if (user == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
            	
            	TblNbsjStaffscore entity = tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(staffScoreid);
            	boolean flag = false;
            	if(entity.getStatus() != null) {
	            	switch (entity.getStatus()) {
						case 1:
							resultMap.put("code", "0");
			                resultMap.put("msg", "流程审批中！");
			                flag = true;
							break;
						case 2:
							resultMap.put("code", "0");
		                    resultMap.put("msg", "流程调整中，请去我的待办提交！");
		                    flag = true;
		                    break;
						case 3:
							resultMap.put("code", "0");
		                    resultMap.put("msg", "流程已通过！");
		                    flag = true;
		                    break;
						case 4:
						case 5:
						case 6:
							resultMap.put("code", "0");
		                    resultMap.put("msg", "流程已完成！");
		                    flag = true;
		                    break;
					}
	                if(flag) {
	                	return resultMap;
	                }
            	}
				List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(TblNbsjStaffscore.settingId);
				HashMap<String, Object> fields = new HashMap<String, Object>();
				if (list != null && list.size() > 0) {
					for (TblProcessAnalysis tblAnalysis : list) {
						TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(), staffScoreid.toString());
						if (analysisUser == null) {
							analysisUser = new TblProcessAnalusisUser();
							analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
							analysisUser.setFromid(staffScoreid.toString());
							analysisUser.setSpdate(new Date());
							if (tblAnalysis.getUserid() != null) {
								analysisUser.setStaffid(user.getRealname());
							} else {
								analysisUser.setStaffid(tblAnalysis.getRolename());
							}
                            //提交人参数
                            if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                                fields.put("tcuserid", user.getStaffid().toString());
                                analysisUser.setStaffid(tblAnalysis.getRolename());
                            } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("bmfzr")) {
                                //部门负责人参数
                                TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
                                if (bmfzr == null) {
                                    resultMap.put("code", "0");
                                    resultMap.put("msg", "部门负责人需配置！");
                                    return resultMap;
                                }
                                fields.put("bmfzr", bmfzr.getStaffid().toString());
                                System.out.println("bmfzr:" + bmfzr.getStaffid());
                            }else if(tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("fgld")) {
								//分管领导参数
								TblStaff fgld = tblStaffMapper.findByStaffFgOrgs(user.getLinkDetp().getOrgid().toString());
								if (fgld == null) {
                                    resultMap.put("code", "0");
                                    resultMap.put("msg", "分管领导需配置！");
                                    return resultMap;
                                }
								fields.put("fgld", fgld.getStaffid().toString());
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
	                                    return resultMap;
	                                }
	                                fields.put("bmfzr", bmfzr.getStaffid().toString());
	                                System.out.println("bmfzr:" + bmfzr.getStaffid());
	                            }else if(tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("fgld")) {
									//分管领导参数
									TblStaff fgld = tblStaffMapper.findByStaffFgOrgs(user.getCurrentOrg().getOrgid().toString());
									if (fgld == null) {
	                                    resultMap.put("code", "0");
	                                    resultMap.put("msg", "分管领导需配置！");
	                                    return resultMap;
	                                }
									fields.put("fgld", fgld.getStaffid().toString());
	                            } else {
	                                //除此之外的参数都设置为当前提交人
	                                if (StrUtil.isNotBlank(tblAnalysis.getUserid())) {
	                                    fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
	                                }
	                            }
						}
					}
				}

				JSONObject jsonObject = JSONObject.fromObject(fields);
				Map<String, Object> map = HttpClient.startProcessAll(TblNbsjStaffscore.settingId, jsonObject.toString());
				String prcessresult = (String) map.get("result");
				String processInstanceId = (String) map.get("processInstanceId");
				String processDefinitionKey = (String) map.get("processDefinitionKey");
				entity.setStatus(TblNbsjStaffscore.STATE_SP);
	            tblNbsjStaffscoreMapper.updateEntity(entity);
				TblCirculation cir = null;
				cir = this.tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_PJSH,entity.getStaffScoreid().toString() , entity.getAuditor().getRealname(), TblNbsjStaffscore.settingUrl + staffScoreid, user.getStaffid(), processInstanceId, processDefinitionKey, staffScoreid.toString());
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
									TblProcessAnalysis analysis = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, TblNbsjStaffscore.settingId);
                                    Integer number = 1;
                                    String usertaskid = analysis.getUsertaskid();
                                    Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                                    blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
									TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, TblNbsjStaffscore.settingId);
									TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), staffScoreid.toString());
									task.setFromid(staffScoreid.toString());
									task.setApprover(user.getRealname());
									task.setUsrid(user.getStaffid().toString());
									task.setExamination("提交审批");
									task.setProcessName(TblNbsjStaffscore.settingId);
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
					resultMap.put("codes", "1");
					resultMap.put("msg", "审批已提交！");
				} else {
					resultMap.put("codes", "0");
					resultMap.put("msg", "流程提交失败！");
//					log.error("发送审批失败");
				}
			} catch (Exception e) {
//				log.error("发送审批失败");
				e.printStackTrace();
				resultMap.put("codes", "0");
				resultMap.put("msg", "流程提交失败！");
			}
			return resultMap;
    }

	@Override
	public JsonBean getStaffScoreApprovalView(String token, BigDecimal staffScoreid, String taskId) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0,20006,null);
        }
		
		if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            try {
            	TblNbsjStaffscore entity = tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(staffScoreid);
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(staffScoreid.toString());
                resultMap.put("tnss", entity);
                resultMap.put("taskList", list);
                resultMap.put("url", HttpClient.jkurl + taskId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
		return ResponseFormat.retParam(1,200,resultMap);
		
		/*else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblCyhwUnit unit = this.tblCyhwUnitMapper.selectByContractId(new BigDecimal(contractId));
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(contractId);
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("budget", unit);
                dataMap.put("taskList", list);
                dataMap.put("url", HttpClient.jkurl + taskId);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        }*/
	}

	@Override
	public JsonBean dealStaffScoreApprovalInfo(String token, BigDecimal staffScoreid, String taskId, BigDecimal cyId,
			String transitionName, String examination, String processInstanceId, String processDefinitionId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			TblNbsjStaffscore entity = tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(staffScoreid);
			if(taskId!=null && !taskId.equals("")){
				Map<String, Object> map = HttpClient.handleProcessJson(staff.getStaffid().toString(),transitionName,taskId);
				String object = (String) map.get("result");
				if(object!=null && object.equals("true")){
					TblMyTask task =new TblMyTask();
					List<TblMyTask> oldtaskList = this.tblMytaskMapper.findOndbyFrom(staffScoreid.toString());
					TblMyTask oldtask = oldtaskList.get(0);
					//TblAnalysis ond = tblProcessAnalusisUserService.findOnd(oldtask.getAnalid());
					TblProcessAnalysis findOnd = null;
					if(oldtask == null){
						findOnd = this.tblProcessAnalysisMapper.findOndBytakdid("");
					}else{
						findOnd = this.tblProcessAnalysisMapper.findOndAnalysis(oldtask.getAnalid());
					}
					
					Integer number=1;
					String usertaskid = findOnd.getUsertaskid();
					Integer num=Integer.parseInt(usertaskid.substring(usertaskid.length()-1,usertaskid.length()))+number;
					String blande=usertaskid.substring(0,usertaskid.length()-1)+num;
					TblProcessAnalysis analysis =null;
					if(transitionName!=null && transitionName.equals("退回")){
						analysis = tblProcessAnalysisService.findOndBytakdidstart("",findOnd.getProcessname());
					}else{
						analysis = tblProcessAnalysisService.findOndBytakdidAnId(blande,findOnd.getProcessname());
					}
					TblProcessAnalusisUser analysisUser = null;
					if(analysis!=null && analysis.getAnalid()!=null){
						analysisUser = tblProcessAnalusisUserService.findOnd(analysis.getAnalid().toString(), staffScoreid.toString());
					}
					TblCirculation circulation = tblCirculationService.getOneBytaskid(staffScoreid.toString());
					String nextapprover = HttpClient.nextapprover(circulation.getBusinesskey());
					task.setApprovaldate(new Date());
					if(staff.getTrole()!=null && staff.getTrole().getRname()!=null){
						task.setApprovalrole(staff.getTrole().getRname());
					}
					task.setApprover(staff.getRealname());
					task.setExamination(examination);
					task.setFromid(staffScoreid.toString());
					task.setProcessDefinitionId(processDefinitionId);
					task.setUsrid(staff.getStaffid().toString());
					task.setCirid(cyId.toString());
					task.setResult(transitionName);
					task.setProcessName(oldtask.getProcessName());
					task.setTaskId(taskId);
					task.setProcessInstanceId(processInstanceId);
					if(StringUtils.isBlank(nextapprover)){//transitionName.equals("完成")||
						task.setHandle("无");
						circulation.setCystate("已完成");
						entity.setStatus(TblNbsjStaffscore.STATE_WC);
					}else{
						//退回or通过
						TblStaff findById = tblStaffMapper.getById(circulation.getCyStaffid());//表单提交人
						if(nextapprover.contains("bmfzr")) {
							TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(staff.getLinkDetp().getOrgid().toString());
							task.setHandle(bmfzr.getRealname());
						}else if (nextapprover.contains("fgld")) {
                        	//分管领导
                            TblStaff fgld = tblStaffMapper.findByStaffFgOrgs(findById.getOrgid().toString());
                            task.setHandle(fgld.getRealname());
                        }else if(nextapprover.contains("tcuserid")) {
							//退回到创建人
							task.setHandle(findById.getRealname());
						}else {
							//角色
							task.setHandle(nextapprover);
						}
					}
					if(entity.getStatus() ==2 ){
						entity.setStatus(TblNbsjStaffscore.STATE_SP);
						circulation.setCystate("审批中");
					}
					if(transitionName!=null && transitionName.equals("退回")){
						circulation.setCystate("需调整");
						entity.setStatus(TblNbsjStaffscore.STATE_TZ);
					}

					if(transitionName!=null && transitionName.equals("终止")){
						circulation.setCystate("终止");
						task.setHandle("无");
						entity.setStatus(TblNbsjStaffscore.STATE_ZZ);
					}
					tblMytaskMapper.insertMyTaskSetting(task);
					tblCirculationService.upateTblCirculation(circulation);
					tblNbsjStaffscoreMapper.updateEntity(entity);
				}
	            return ResponseFormat.retParam(1,200,null);
			}else{
				return ResponseFormat.retParam(0,30002,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,30002,null);
		}
	}

	@Override
	public JsonBean getAuditUserApprovalView(String token, BigDecimal staffid, String taskId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0,20006,null);
        }
		
		if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            try {
            	TblNbsjStaffscore entity = tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(staffid);
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(staffid.toString());
                resultMap.put("tnss", entity);
                resultMap.put("taskList", list);
                resultMap.put("url", HttpClient.jkurl + taskId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean xfry(String ids,String userids,String usernames, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(ids!=null && ids.length()>0) {
			String[] idlist = ids.split(",");
			for (String id : idlist) {
				tblReportMapper.xfry(id, userids, usernames);
			}
			return ResponseFormat.retParam(1,200,null);
		}
		
		
		return ResponseFormat.retParam(0,50001,null);		
	}
}
