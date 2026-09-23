package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblNbsjAdvicenoteService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import com.spire.ms.System.Collections.ArrayList;

import cn.hutool.core.util.StrUtil;
@Service
public class TblNbsjAdvicenoteServiceImpl  implements TblNbsjAdvicenoteService {

	@Autowired
    private TblNbsjAdvicenoteMapper tblNbsjAdvicenoteMapper;
	
	@Autowired
    private TblNbsjAdviceAprMapper tblNbsjAdviceAprMapper;
    
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

	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	
	@Resource
	private FundAuditProjectMapper fundAuditProjectMapper;
	   
	   
	@Resource
	private EnginAuditProjectMapper enginAuditProjectMapper;

	@Resource
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;
    
    
//	@Override
//	public void delete(TblNbsjAdvicenoteEntity note) {
//		
//		BigDecimal id = note.getAdviceid();
//		
//		baseMapper.delete(id+"");
//
//	}
//
//	@Override
//	public void update(TblNbsjAdvicenoteEntity note) {
//		// TODO Auto-generated method stub
//
//	}
//
////	@Override
////	public void save(TblNbsjAdvicenoteEntity note) {
////		// TODO Auto-generated method stub
////
////	}
//
//	@Override
//	public List<TblNbsjAdvicenoteEntity> isNoteCode(String code) {
//		List<TblNbsjAdvicenoteEntity> list = baseMapper.isNoteCode(code,"");
//		return list;
//	}
//
//	@Override
//	public TblNbsjAdvicenoteEntity get(String noteid) {
//		TblNbsjAdvicenoteEntity tblNbsjAdvicenoteEntity = baseMapper.get(noteid);
//		return tblNbsjAdvicenoteEntity;
//	}
//
//	@Override
//	public List<TblNbsjAdvicenoteEntity> isNoteCode(String code, String projectId) {
//		List<TblNbsjAdvicenoteEntity> list = baseMapper.isNoteCode(code,projectId);
//		return list;
//	}

	//获取当前实施项目；
	public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
		BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
		if (projectId == null) {
			return null;
		}
		return implementPlanMapper.selectById(projectId.toString());
	}
	
	
	//==
	@Override
	public JsonBean noticePageList(String token, Integer pageNumber, Integer pageSize,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo) throws Exception {
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
    	
    	PageInfo<TblYqnsAdvicenoteEntity> pageInfo = new PageInfo<TblYqnsAdvicenoteEntity>();

		com.github.pagehelper.PageInfo<TblYqnsAdvicenoteEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjAdvicenoteMapper.selectListByPageInfo(pageInfo,tblNbsjAdvicenoteVo,loginStaff.getStaffid(),loginStaff.getDeptIds());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}); 

		List<TblYqnsAdvicenoteEntity> list = pageInfo2.getList();
		List<TblYqnsAdvicenoteEntity> newlist = new ArrayList();
		for (TblYqnsAdvicenoteEntity plan : list) {
			List<TblAttachment> attachmentList = tblNbsjAdvicenoteMapper.selectAtt(plan.getAdviceid());
			plan.setTblNoteAtts(attachmentList);
			newlist.add(plan);
		}
		
		
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(newlist);
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());

    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean noticeAdd(TblYqnsAdvicenoteEntity notice, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		Integer count = this.tblNbsjAdvicenoteMapper.selectPlanCodeByOrgid(notice);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null); 
		}

//		//==查询当前实施的项目！
		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			tnp=implementPlanMapper.selectById(notice.getProgectid());
		}
//		BigDecimal projectId = tnp.getId();
//		if(null == projectId) {
//			return ResponseFormat.retParam(0,30003,null);
//		}
//		notice.setProject(tnp); 
    	
//		notice.setTblCreater(loginStaff);
//		notice.setProgectid(projectId.toString());
		notice.setCreatestaffid(loginStaff.getStaffid()+"");
		notice.setCreatrtime(new Date());
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(notice.getAdviceid() != null) {
			//修改；
			//this.tblNbsjAdvicenoteMapper.updateEntity(notice);
			tblNbsjAdvicenoteMapper.updateByPrimaryKeySelective(notice);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationAdvice(notice.getAdviceid().intValue());
			if (notice.getAttids() != null && !"".equals(notice.getAttids())) {
				String[] ids = notice.getAttids().split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAdvice(id, notice.getAdviceid().intValue());
				}
			}
			
			
			
			if(tnp!=null && tnp.getZykstype()!=null && !tnp.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", tnp.getXmapbid()));
	        	if(fund!=null) {
	        		if(notice.getZcstaffid()!=fund.getGroupLeaderId()) {
		        		fund.setGroupLeader(notice.getTeamleader());
		        		fund.setGroupLeaderId(notice.getZcstaffid());
		        	}
		        	
		        	if(notice.getZsstaffid()!=fund.getApproverId()) {
		        		fund.setApprover(notice.getMainreviewer());
		        		fund.setApproverId(notice.getZsstaffid());
		        	}
		        	
		        	if(notice.getFzstaffids()!=fund.getFzzStafffId()) {
		        		fund.setFzzName(notice.getFznames());
		        		fund.setFzzStafffId(notice.getZsstaffid());
		        	}
		        	
		        	if(!notice.getAssistapproverid().equals(fund.getAssistApproverId())) {
		        		fund.setAssistApprover(notice.getHelpreviewer());
		        		fund.setAssistApproverId(notice.getAssistapproverid());
		        	}
		        	
		        	fundAuditProjectMapper.updateById(fund);
	        	}
	        	
	        }
	        if(tnp!=null && tnp.getZykstype()!=null && tnp.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", tnp.getXmapbid()));
	        	 if(endin!=null) {
	        		 if(notice.getZcstaffid()!=endin.getGroupLeaderId()) {
		        		 endin.setGroupLeader(notice.getTeamleader());
			        		endin.setGroupLeaderId(notice.getZcstaffid());
			        	}
			        	
			        	if(notice.getZsstaffid()!=endin.getApproverId()) {
			        		endin.setApprover(notice.getMainreviewer());
			        		endin.setApproverId(notice.getZsstaffid());
			        	}
			        	
			        	if(notice.getFzstaffids()!=endin.getFzzStafffId()) {
			        		endin.setFzzName(notice.getFznames()); 
			        		endin.setFzzStafffId(notice.getZsstaffid());
			        	}
			        	
			        	if(!notice.getAssistapproverid().equals(endin.getAssistApproverId())) {
			        		endin.setAssistApprover(notice.getHelpreviewer());
			        		endin.setAssistApproverId(notice.getAssistapproverid());
			        	}
		        	 enginAuditProjectMapper.updateById(endin);
	        	 }
	        	 
	        }
			
		}else {
			notice.setStatus(0);
			//新增； 
			//this.tblNbsjAdvicenoteMapper.insertEntity(notice); 
			tblNbsjAdvicenoteMapper.insertSelective(notice);
			//==附件
			if (notice.getAttids() != null && !"".equals(notice.getAttids())) {
				String[] ids = notice.getAttids().split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAdvice(id, notice.getAdviceid().intValue());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Doubtfulpoint",notice);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean noticeDelete(BigDecimal adviceid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblYqnsAdvicenoteEntity plan = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjAdvicenoteMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjAdvicenoteMapper.deleteById(adviceid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findNoticeDetail(String token, BigDecimal adviceid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		TblYqnsAdvicenoteEntity plan = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		if (plan!=null && plan.getOrgids() != null ){
			TblOrganization organization = tblOrganizationMapper.selectById(plan.getOrgids());
			if(organization!=null) {
				plan.setOrganization(organization);
				plan.setOrgName(organization.getOrgname());
			}
			
		}
		List<TblAttachment> attachmentList = tblNbsjAdvicenoteMapper.selectAtt(plan.getAdviceid());
		plan.setTblNoteAtts(attachmentList);
		
		//审计通知审批
		if(plan.getAdviceaprid() != null) {
			TblYqnsAdviceAprEntity adviceapr = this.tblNbsjAdvicenoteMapper.selectAprById(plan.getAdviceaprid());
			if (adviceapr!=null && adviceapr.getOrgids() != null ){
				adviceapr.setOrganization(tblOrganizationMapper.selectById(adviceapr.getOrgids()));
			}
			//关联项目信息
			if(adviceapr!=null && adviceapr.getProgectid() != null) {
				ImplementPlanEntity implementPlanEntity = implementPlanMapper.selectById(adviceapr.getProgectid());
				//构建预留字段返回
				reservePropertyService.buildReserveProperty(implementPlanEntity);
				resultMap.put("project", implementPlanEntity);
			}
			//构建预留字段返回
			reservePropertyService.buildReserveProperty(adviceapr);
			resultMap.put("adviceapr", adviceapr);

		}

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(plan);
		resultMap.put("advice", plan);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public TblYqnsAdvicenoteEntity findById(BigDecimal adviceid) throws Exception{
		TblYqnsAdvicenoteEntity notice = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		return notice;
	}

	@Override
	public JsonBean submitTblAdvicenoteArrpoval(String token, Integer adviceid) throws Exception {
		
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	
		TblNbsjAdvicenoteEntity advice = this.tblNbsjAdvicenoteMapper.getById(adviceid.toString());
		if(advice == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
        TblCirculation cir = new TblCirculation();
        try {
           
           cir.setCytype(TblCirculation.TYPE_SJTZS);
           cir.setCycode(advice.getAdvicecoed());
           cir.setCyname(advice.getAdvicename());
           cir.setCydate(new Date());
           cir.setCystate(TblCirculation.STATE_FQ);
           cir.setCyurl(TblCirculation.URL_SJTZS+adviceid);
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
           advice.setContent(null);
           tblNbsjAdvicenoteMapper.updateEntity(advice);
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
	public JsonBean getTblAdvicenoteApprovalInfo(String token, Integer adviceid, String taskId, Integer cyId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjAdvicenoteEntity advice = this.tblNbsjAdvicenoteMapper.getById(adviceid.toString());
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
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(adviceid.toString(),cyId);
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
			String optDesc, String adviceid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjAdvicenoteEntity advice = this.tblNbsjAdvicenoteMapper.getById(adviceid.toString());
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
	public JsonBean noticeCancel(BigDecimal adviceid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblYqnsAdvicenoteEntity plan = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjAdvicenoteMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjAdvicenoteMapper.calcelById(adviceid);
		return ResponseFormat.retParam(1,200,null);
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
        this.tblNbsjAdvicenoteMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	
	@Override
	public JsonBean noticeAprPageList(String token, Integer pageNumber, Integer pageSize,String advicename,String xctype) throws Exception {
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
    	
    	PageInfo<TblYqnsAdviceAprEntity> pageInfo = new PageInfo<TblYqnsAdviceAprEntity>();

		com.github.pagehelper.PageInfo<TblYqnsAdviceAprEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjAdvicenoteMapper.selectAprListByPageInfo(pageInfo,advicename, loginStaff.getStaffid(),loginStaff.getDeptIds(),xctype );
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

    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean findNoticeAprDetail(String token, BigDecimal adviceid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		TblYqnsAdviceAprEntity plan = this.tblNbsjAdvicenoteMapper.selectAprById(adviceid);
		if (plan.getOrgids() != null ){
			plan.setOrganization(tblOrganizationMapper.selectById(plan.getOrgids()));
		}
		
		ImplementPlanEntity implementPlanEntity = implementPlanMapper.selectById(plan.getProgectid());

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(plan);
		reservePropertyService.buildReserveProperty(implementPlanEntity);

		resultMap.put("advice", plan);
		resultMap.put("project", implementPlanEntity);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean noticeAprAdd(TblYqnsAdviceAprEntity notice, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		notice.setCreatestaffid(loginStaff.getStaffid()+"");
		notice.setCreatrtime(new Date());
		notice.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(notice.getAdviceid() != null) {
			//修改；
			tblNbsjAdviceAprMapper.updateByPrimaryKeySelective(notice);
		}else {
			//新增；
			tblNbsjAdviceAprMapper.insertSelective(notice);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("advice",notice);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean noticeAprDelete(BigDecimal adviceid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblYqnsAdviceAprEntity plan = this.tblNbsjAdvicenoteMapper.selectAprById(adviceid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		this.tblNbsjAdvicenoteMapper.deleteAprById(adviceid);
		return ResponseFormat.retParam(1,200,null);
	}
	
	
	
	@Override
	public JsonBean noticespAprPageList(String token, Integer pageNumber, Integer pageSize,String advicename) throws Exception {
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
    	
    	PageInfo<TblYqnsAdviceAprEntity> pageInfo = new PageInfo<TblYqnsAdviceAprEntity>();

		com.github.pagehelper.PageInfo<TblYqnsAdviceAprEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> { 
					try {
						this.tblNbsjAdvicenoteMapper.selectAprListByspPageInfo(pageInfo,advicename, loginStaff.getStaffid(),loginStaff.getDeptIds() );
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

    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
}
