package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.AgentEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblNbsjQuestionaffirmEntity;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjFactbookMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.vo.TblNbsjFactbookVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblNbsjFactbookService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjQuestionaffirmService;
import com.huabo.audit.util.R;

import cn.hutool.core.util.StrUtil;
@Service
public class TblNbsjFactbookServiceImpl extends ServiceImpl<TblNbsjFactbookMapper, TblNbsjFactbookEntity> implements TblNbsjFactbookService {

	@Autowired
    private TblNbsjFactbookMapper tblNbsjFactbookMapper;
    @Autowired
    private ActivityPluginsService activityPluginsService;
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
	private TblNbsjQuestionaffirmService tblNbsjQuestionaffirmService;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    @Resource
    public TblNbsjProjectMapper tblNbsjProjectMapper;
    
    @Resource
    private UserProvider userProvider;
    
	@Override
	public void addNbsjFactBook(TblNbsjFactbookEntity nbsjFactbook) {
		// TODO Auto-generated method stub

	}

	@Override
	public TblNbsjFactbookEntity geTblNbsjFactbook(String factid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delNbsjFactBook(TblNbsjFactbookEntity nbsjFactbook) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNbsjFactBook(TblNbsjFactbookEntity nbsjFactbook) {
		// TODO Auto-generated method stub

	}

	@Override
	public void merge(TblNbsjFactbookEntity nbsjFactbook) {
		// TODO Auto-generated method stub

	}

	
	
	////==
	@Override
	public JsonBean confirmationPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjFactbookVo tblNbsjFactbookVo) throws Exception {
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
    	
    	
    	if(null == tblNbsjFactbookVo.getProjectid()) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,null);
    		}
    		Integer projectId = tnp.getProjectId();
    		if(null == projectId) {
    			return ResponseFormat.retParam(0,30003,null);
    		}
    		tblNbsjFactbookVo.setProjectid(projectId);
    	}
    	
    	
    	PageInfo<TblNbsjFactbookEntity> pageInfo = new PageInfo<TblNbsjFactbookEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjFactbookMapper.selectListByPageInfo(pageInfo,tblNbsjFactbookVo));
    	pageInfo.setTotalRecord(this.tblNbsjFactbookMapper.selectCountByPageInfo(pageInfo,tblNbsjFactbookVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean confirmationAdd(TblNbsjFactbookEntity fb, String token,String questionIds,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjFactbookMapper.selectPlanCodeByOrgid(fb);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
//		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
		
		fb.setProjectid(projectId);
    	
//		fb.setAuditStaffId(loginStaff.getStaffid().intValue());
		fb.setCreatetime(new Date());
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(fb.getFactid() != null) {
			//修改；
			this.tblNbsjFactbookMapper.updateEntity(fb);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationFactbook(fb.getFactid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationFactbook(id, fb.getFactid());
				}
			}
			
			//==
			if(null != questionIds) {
				String[] questionIdss = questionIds.split(",");
				for (int i = 0; i < questionIdss.length; i++) {
	            	tblNbsjQuestionaffirmService.deleteTblNbsjQuestionaffirmBySql(questionIdss[i], projectId, fb.getFactid());
	            }
			}
		}else {
			//新增；
			fb.setStatus(TblNbsjFactbookEntity.STATUS_1);
			this.tblNbsjFactbookMapper.insertEntity(fb);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblAttachmentMapper.insertAttmentRelationFactbook(id, fb.getFactid());
				}
			}
		}
		
		//关联审计发现
		if(null != questionIds) {
			String[] questionIdss = questionIds.split(",");
			for (int i = 0; i < questionIdss.length; i++) {
	            TblNbsjQuestionaffirmEntity nq = new TblNbsjQuestionaffirmEntity();
	            nq.setTblNbsjFactbook(fb);
	            TblNbsjQuestionEntity n = new TblNbsjQuestionEntity();
	            n.setQuestionId(Integer.valueOf(questionIdss[i]));
	            nq.setTblNbsjQuestion(n);
	            nq.setFactid(fb.getFactid());
	            tblNbsjQuestionaffirmService.saveTblNbsjQuestionaffirm(nq);
	        }
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Question",fb);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean confirmationDelete(Integer factid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjFactbookEntity plan = this.tblNbsjFactbookMapper.selectById(factid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjFactbookMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjFactbookMapper.deleteById(factid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findNbsjFactbookDetail(String token, Integer factid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjFactbookEntity plan = this.tblNbsjFactbookMapper.selectById(factid);
		resultMap.put("sheet", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean submitArrpoval(String token, Integer factid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	
		TblNbsjFactbookEntity fact = tblNbsjFactbookMapper.getById(factid.toString());
		TblNbsjProject project = tblNbsjProjectMapper.getById(fact.getProjectid().toString());
        TblCirculation cir = new TblCirculation();
        try {
           cir.setCytype(TblCirculation.TYPE_SSQRS);
           cir.setCycode(fact.getFactcode());
           cir.setCyname(project.getPrjoectName());
           cir.setCydate(new Date());
           cir.setCystate(TblCirculation.STATE_FQ);
           cir.setCyurl(TblCirculation.URL_SSQRS+factid);
           cir.setCyStaffid(user.getStaffid().toString());
           this.tblCirculationMapper.saveTblCirculation(cir);
           
           Map<String, Object> variables= new HashMap<String, Object>();
           variables.put(ProcessVariableEnum.model.toString(), cir);
           
           AgentEntity ae = new AgentEntity();
 			if(fact.getFhstaffid()==null) {
 				return ResponseFormat.retParam(0,"请添加复核人",null);
 			}
 			if(fact.getFactstaffid()==null) {
 				return ResponseFormat.retParam(0,"请添加事实确认人",null);
 			}
	        ae.setProjectPerson(fact.getFhstaffid().toString());
	        ae.setResponsible(fact.getFactstaffid().toString());
		    variables.put(ProcessVariableEnum.agent.toString(), ae);
		   
           ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_SSQRS.name(),cir.getCyid().toString(), variables);
           Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
           cir.setTaskid(task.getId());
           //流程定义Id
           String businessKey = processInstance.getBusinessKey();
           String definitionId = processInstance.getProcessDefinitionId();
           cir.setBusinesskey(businessKey);
           cir.setDefinitionid(definitionId);
           this.tblCirculationMapper.updateCirculationInfoById(cir);
           taskService.complete(task.getId());
           fact.setStatus(TblNbsjFactbookEntity.STATUS_2);
           tblNbsjFactbookMapper.updateEntity(fact);
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
	public JsonBean getApprovalInfo(String token, Integer factid, String taskId, Integer cyId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjFactbookEntity fact = tblNbsjFactbookMapper.selectById(factid);
        List<String> btnList = null;
    	if(fact!=null){
    		//审批按钮
    		if(StrUtil.isNotBlank(taskId) && fact.getStatus()!= TblNbsjFactbookEntity.STATUS_3){
    			btnList = processService.getButtonsForTransition(taskId);
    			resultMap.put("btnList", btnList);
    		}
    		//审批记录
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(factid.toString(),cyId);
    		if (cyId != null){
    			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
    			resultMap.put("cy", cy);
    		}
        	TblNbsjProject project = tblNbsjProjectService.getProjectById(fact.getProjectid());
    		resultMap.put("taskId", taskId);
    		resultMap.put("aoptionList", ao);
    		resultMap.put("cyId", cyId);
    		resultMap.put("fact", fact);
    		resultMap.put("project", project);
    	}
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dealApporval(String token, Integer cyId, String taskId, String transition, String optDesc,
			String factid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(1,20006,null);
		}
		JsonBean jsonBean = null;
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
		if (null != cy) {
			Map<String, Object> variables = new HashMap<>();
			variables.put(ProcessVariableEnum.model.toString(), cy);
			if (StrUtil.isNotBlank(transition)) {
				variables.put(ProcessVariableEnum.transition.toString(), transition);
			}
			try {
				taskService.claim(taskId, staff.getStaffid().toString());
				taskService.complete(taskId, variables);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseFormat.retParam(1,30002,null);
			}
			//添加审批意见
			TblAuditOption opt = new TblAuditOption();
			TblCirculation cy2 = this.tblCirculationMapper.findById(cyId.toString());
			opt.setCyid(cy2.getCyid());
			opt.setOptStaffid(staff.getStaffid());
			opt.setStaffidName(staff.getRealname());
			opt.setOptDesc(optDesc);
			opt.setRelationId(new BigDecimal(factid.toString()));
			opt.setOptState(transition);
			jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy2,opt);
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
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbsjFactbookMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	
}
