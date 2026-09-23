package com.huabo.audit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.*;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblNbsjAdvicenotechangeServiceImpl implements TblNbsjAdvicenotechangeService {

	@Resource
    private TblNbsjAdvicenotechangeMapper tblNbsjAdvicenotechangeMapper;
    
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
	private TblNbsjProjectService tblNbsjProjectService;

	@Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

	@Autowired
	private ImplementPlanMapper implementPlanMapper;

	@Autowired
	private TblNbsjAdvicenoteMapper tblNbsjAdvicenoteMapper;

	@Autowired
	private TblOrganizationMapper tblOrganizationMapper;

	@Autowired
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;

	//==
	@Override
	public JsonBean noticeChangePageList(String token, Integer pageNumber, Integer pageSize,TblYqnsAdvicenoteChangeEntity tblYqnsAdvicenoteChange) throws Exception {
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
    	
//    	if(null == tblYqnsAdvicenoteChange.getProgectid()){
//    		//==查询当前实施的项目！
//			ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//    		if(tnp == null) {
//    			return ResponseFormat.retParam(0,30003,resultMap);
//    		}
//    		BigDecimal projectId = tnp.getId();
//    		if(null == projectId) {
//    			return ResponseFormat.retParam(0,30003,resultMap);
//    		}
//			tblYqnsAdvicenoteChange.setProgectid(projectId);
//    	}
//    	
    	tblYqnsAdvicenoteChange.setCreatestaffid(loginStaff.getStaffid().toString());
    	tblYqnsAdvicenoteChange.setOrgid(loginStaff.getDeptIds());
    	PageInfo<TblYqnsAdvicenoteChangeEntity> pageInfo = new PageInfo<TblYqnsAdvicenoteChangeEntity>();

		com.github.pagehelper.PageInfo<TblYqnsAdvicenoteChangeEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjAdvicenotechangeMapper.selectListByPageInfo(pageInfo,tblYqnsAdvicenoteChange);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());

    	//String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	//resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	//获取当前实施项目；
	public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
		BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
		if (projectId == null) {
			return null;
		}
		return implementPlanMapper.selectById(projectId.toString());
	}

	@Override
	public JsonBean noticeChangeAdd(TblYqnsAdvicenoteChangeEntity noticeChange, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		//==查询当前实施的项目！
//		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//		if(tnp == null) {
//			return ResponseFormat.retParam(0,30003,null);
//		}
//		BigDecimal projectId = tnp.getId();
//		if(null == projectId) {
//			return ResponseFormat.retParam(0,30003,null);
//		}
//		noticeChange.setProject(tnp);
    	
//
//		noticeChange.setProgectid(projectId);
		noticeChange.setCreatestaffid(loginStaff.getStaffid()+"");
		noticeChange.setCreatrtime(new Date());
		noticeChange.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(noticeChange.getChangeid() != null) {
			//修改；
			tblNbsjAdvicenotechangeMapper.updateByPrimaryKeySelective(noticeChange);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationAdvice(noticeChange.getChangeid().intValue());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAdvice(id, noticeChange.getChangeid().intValue());
				}
			}
		}else {
			//新增；
			tblNbsjAdvicenotechangeMapper.insertSelective(noticeChange);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAdvice(id, noticeChange.getChangeid().intValue());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Doubtfulpoint",noticeChange);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean noticeChangeDelete(Integer changeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		this.tblNbsjAdvicenotechangeMapper.deletebychangeid(changeid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findDetail(String token, Integer changeid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) { 
			return ResponseFormat.retParam(0,20006,null); 
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		TblYqnsAdvicenoteChangeEntity plan = this.tblNbsjAdvicenotechangeMapper.getById(changeid);
		if (plan.getAdviceid() != null ){
			TblYqnsAdvicenoteEntity advicenoteEntity = tblNbsjAdvicenoteMapper.selectById(plan.getAdviceid());
			plan.setTblYqnsAdvicenote(advicenoteEntity);
		}
		if (plan.getOrgid() != null){
			TblOrganization organization = tblOrganizationMapper.selectOrgById(new BigDecimal(plan.getOrgid()));
			plan.setOrganization(organization);
		}
		List<TblAttachment> attachmentList = tblNbsjAdvicenotechangeMapper.selectAtt(plan.getChangeid());
		plan.setTblNoteAtts(attachmentList);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(plan);

		resultMap.put("Doubtfulpoint", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public TblYqnsAdvicenoteChangeEntity findById(Integer changeid) throws Exception{
		TblYqnsAdvicenoteChangeEntity notice = this.tblNbsjAdvicenotechangeMapper.getById(changeid);
		return notice;
	}

	@Override
	public JsonBean submitTblAdvicenoteArrpoval(String token, Integer changeid) throws Exception {
		
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		TblYqnsAdvicenoteChangeEntity advice = this.tblNbsjAdvicenotechangeMapper.getById(changeid);
		if(advice == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
        TblCirculation cir = new TblCirculation();
        try {
           
           cir.setCytype(TblCirculation.TYPE_SJTZS);
           cir.setCydate(new Date());
           cir.setCystate(TblCirculation.STATE_FQ);
           cir.setCyurl(TblCirculation.URL_SJTZS+changeid);
           cir.setCyStaffid(user.getStaffid().toString());
           this.tblCirculationMapper.saveTblCirculation(cir);
           
           Map<String, Object> variables= new HashMap<String, Object>();
           variables.put(ProcessVariableEnum.model.toString(), cir);
           ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_JHTZD.name(),cir.getCyid().toString(), variables);
           Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
           cir.setTaskid(task.getId());
           //流程定义Id
           String businessKey = processInstance.getBusinessKey();
           String definitionId = processInstance.getProcessDefinitionId();
           cir.setBusinesskey(businessKey);
           cir.setDefinitionid(definitionId);
           this.tblCirculationMapper.updateCirculationInfoById(cir);
           taskService.complete(task.getId());
           advice.setStatus(TblNbsjAdvicenoteEntity.SPZ);
			tblNbsjAdvicenotechangeMapper.updateByPrimaryKey(advice);
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
	public JsonBean getTblAdvicenoteApprovalInfo(String token, Integer changeid, String taskId, Integer cyId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblYqnsAdvicenoteChangeEntity advice = this.tblNbsjAdvicenotechangeMapper.getById(changeid);
		if(advice == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
        List<String> btnList = null;
    	if(advice!=null){
    		//审批按钮
    		if(StrUtil.isNotBlank(taskId) && advice.getStatus()!= TblNbsjAdvicenoteEntity.XTZ){
    			btnList = processService.getButtonsForTransition(taskId);
    			resultMap.put("btnList", btnList);
    		}
    		//审批记录
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(changeid.toString(),cyId);
    		if (cyId != null){
    			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
    			resultMap.put("cy", cy);
    		}
    		
    		resultMap.put("taskId", taskId);
    		resultMap.put("aoptionList", ao);
    		resultMap.put("cyId", cyId);
    		resultMap.put("advice", advice);
    	}
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dealTblAdvicenoteApporval(String token, Integer cyId, String taskId, String transition,
			String optDesc, Integer adviceid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblYqnsAdvicenoteChangeEntity advice = this.tblNbsjAdvicenotechangeMapper.getById(adviceid);
		if(advice == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
		JsonBean jsonBean = null;
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
				return ResponseFormat.retParam(0,30002,null);
			}
			//添加审批意见
			TblAuditOption opt = new TblAuditOption();
			opt.setCyid(cy.getCyid());
			opt.setOptStaffid(staff.getStaffid());
			opt.setStaffidName(staff.getRealname());
			opt.setOptDesc(optDesc);
			opt.setRelationId(new BigDecimal(adviceid.toString()));
			opt.setOptState(transition);
			jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
		}
		return jsonBean;
	}
	
	
	
	@Override
	public JsonBean findbyadviceidAll(BigDecimal adviceid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	resultMap.put("list", this.tblNbsjAdvicenotechangeMapper.findbyadviceidAll(adviceid));
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	

}
