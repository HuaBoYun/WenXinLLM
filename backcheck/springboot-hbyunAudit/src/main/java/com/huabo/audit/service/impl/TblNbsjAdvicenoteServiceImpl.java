package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteIssueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
/*import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;*/
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAdvicenoteMapper;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblNbsjAdvicenoteService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

import cn.hutool.core.util.StrUtil;
@Service
public class TblNbsjAdvicenoteServiceImpl  implements TblNbsjAdvicenoteService {

	@Autowired
    private TblNbsjAdvicenoteMapper tblNbsjAdvicenoteMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
   	public TblCirculationMapper tblCirculationMapper;
/*    @Resource
	private RuntimeService runtimeService;
	@Resource 
	private TaskService taskService;
	*/
    @Resource
    private ProcessService processService;
    
    @Resource
    private TblAuditOptionService tblAuditOptionService;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
	private TblNbsjProjectService tblNbsjProjectService;
    
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
    	
    	if(null == tblNbsjAdvicenoteVo.getProjectId()){
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		BigDecimal projectId = tnp.getProjectId();
    		if(null == projectId) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		tblNbsjAdvicenoteVo.setProjectId(projectId);
    	}
    	
    	PageInfo<TblNbsjAdvicenoteEntity> pageInfo = new PageInfo<TblNbsjAdvicenoteEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjAdvicenoteMapper.selectListByPageInfo(pageInfo,tblNbsjAdvicenoteVo,loginStaff));
    	pageInfo.setTotalRecord(this.tblNbsjAdvicenoteMapper.selectCountByPageInfo(pageInfo,tblNbsjAdvicenoteVo,loginStaff));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean noticeAdd(TblNbsjAdvicenoteEntity notice, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjAdvicenoteMapper.selectPlanCodeByOrgid(notice);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		BigDecimal projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
		notice.setProject(tnp);
    	
//		notice.setTblCreater(loginStaff);
		notice.setProgectid(projectId.toString());
		notice.setCreatestaffid(loginStaff.getStaffid()+"");
		notice.setCreatrtime(new Date());
		notice.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(notice.getAdviceid() != null) {
			//修改；
			//this.tblNbsjAdvicenoteMapper.updateEntity(notice);
			tblNbsjAdvicenoteMapper.updateByPrimaryKeySelective(notice);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationAdvice(notice.getAdviceid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAdvice(id, notice.getAdviceid());
				}
			}
		}else {
			//新增；
			//this.tblNbsjAdvicenoteMapper.insertEntity(notice);
			notice.setAdviceid(RandomUtil.uuBigDecimalId());
			notice.setOrgid(loginStaff.getLinkDetp().getOrgid());
			tblNbsjAdvicenoteMapper.insertSelective(notice);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAdvice(id, notice.getAdviceid());
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
		TblNbsjAdvicenoteEntity plan = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		
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
		
		TblNbsjAdvicenoteEntity plan = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		resultMap.put("Doubtfulpoint", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public TblNbsjAdvicenoteEntity findById(String adviceid) throws Exception{
		TblNbsjAdvicenoteEntity notice = this.tblNbsjAdvicenoteMapper.selectById(new BigDecimal(adviceid));
		return notice;
	}

	@Override
	public JsonBean submitTblAdvicenoteArrpoval(String token, BigDecimal adviceid) throws Exception {
		
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
        /*   ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_JHTZD.name(),cir.getCyid().toString(), variables);
           Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
           cir.setTaskid(task.getId());
           //流程定义Id
           String businessKey = processInstance.getBusinessKey();
           String definitionId = processInstance.getProcessDefinitionId();
           cir.setBusinesskey(businessKey);
           cir.setDefinitionid(definitionId);
           this.tblCirculationMapper.updateCirculationInfoById(cir);
           taskService.complete(task.getId());*/
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
	public JsonBean getTblAdvicenoteApprovalInfo(String token, BigDecimal adviceid, String taskId, BigDecimal cyId)
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
	public JsonBean dealTblAdvicenoteApporval(String token, BigDecimal cyId, String taskId, String transition,
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
		TblNbsjAdvicenoteEntity plan = this.tblNbsjAdvicenoteMapper.selectById(adviceid);
		
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
	public R removeAttInfoByAttId(String token, BigDecimal attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        return this.deleteRealtionAttInfo(attId);
	}

	private R deleteRealtionAttInfo(BigDecimal attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbsjAdvicenoteMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
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
				tblNbsjAdvicenoteMapper.xfry(id, userids, usernames);
			}
			return ResponseFormat.retParam(1,200,null);
		}
		
		
		return ResponseFormat.retParam(0,50001,null);		
	}

	//查询下发列表页
	@Override
	public JsonBean noticeIssuePageList(String token, Integer pageNumber, Integer pageSize, String adviceId) throws Exception {
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
		PageInfo<TblNbsjAdvicenoteIssueEntity> pageInfo = new PageInfo<TblNbsjAdvicenoteIssueEntity>();

		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjAdvicenoteMapper.selectListByIssuePageInfo(adviceId));
		pageInfo.setTotalRecord(this.tblNbsjAdvicenoteMapper.selectCountIssueByPageInfo(pageInfo,adviceId));
		pageInfo.getTotalPage();
//		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
//		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}
}
