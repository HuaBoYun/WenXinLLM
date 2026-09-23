package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBorrowRecordMapper;
import com.huabo.audit.oracle.mapper.TblNbsjPlanProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblNbsjBorrowRecordService;
import com.huabo.audit.service.TblNbsjProjectService;
@Service
public class TblNbsjBorrowRecordServiceImpl extends ServiceImpl<TblNbsjBorrowRecordMapper, TblNbsjBorrowRecordEntity> implements TblNbsjBorrowRecordService {

	@Autowired
    private TblNbsjBorrowRecordMapper tblNbsjBorrowRecordMapper;
    @Autowired
    private ActivityPluginsService activityPluginsService;
    @Resource
    private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    @Resource
	public TblNbsjPlanProjectMapper tblNbsjPlanProjectMapper;
    @Resource
    public TblNbsjProjectMapper tblNbsjProjectMapper;
    
    @Resource
   	public TblCirculationMapper tblCirculationMapper;
    
    @Resource
	private RuntimeService runtimeService;
	@Resource
	private HistoryService historyService;
	@Resource
	private RepositoryService repositoryService;
	@Resource 
	private TaskService taskService;
    @Resource
    private ProcessService processService;
    
    @Resource
    private TblAuditOptionService tblAuditOptionService;
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    @Resource
    public TblStaffMapper tblStaffMapper;
    
    @Resource
    private UserProvider userProvider;
	
	@Override
	public boolean saveBatch(Collection<TblNbsjBorrowRecordEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjBorrowRecordEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjBorrowRecordEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjBorrowRecordEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjBorrowRecordEntity getOne(Wrapper<TblNbsjBorrowRecordEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjBorrowRecordEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjBorrowRecordEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjBorrowRecordMapper getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjBorrowRecordEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	//==
	@Override
	public JsonBean tjspBorrow(TblNbsjBorrowRecordEntity br, String token,String borrowDate,String backDate) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
//		Integer count = this.tblNbsjBorrowRecordMapper.selectPlanCodeByOrgid(br);
//		if(count > 0) {
//			return ResponseFormat.retParam(0,202,null);
//		}
    	
		br.setStaffid(loginStaff.getStaffid().intValue());
		br.setStatus(TblNbsjBorrowRecordEntity.SPNO);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(br.getBorrowid() != null) {
			br.setBackDate(backDate);
			br.setBorrowDate(borrowDate);
			//修改；
			this.tblNbsjBorrowRecordMapper.updateEntity(br);
		}else {
			br.setBackDate(backDate);
			br.setBorrowDate(borrowDate);
			
			//新增；
			this.tblNbsjBorrowRecordMapper.insertEntity(br);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("BorrowRecord",br);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	//==借阅日志-借阅次数列表
	@Override
	public JsonBean jyrzCountPageList(String token, Integer pageNumber, Integer pageSize,Integer projectid) throws Exception {
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
	     
	     PageInfo<TblNbsjBorrowRecordEntity> pageInfo = new PageInfo<TblNbsjBorrowRecordEntity>();

		Page<TblNbsjBorrowRecordEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> this.tblNbsjBorrowRecordMapper.selectListByInfo(projectid));
		pageInfo = new PageInfoUtil<TblNbsjBorrowRecordEntity>().parsePageInfo(page);

	     String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
	     resultMap.put("identifier", identifier);
	     resultMap.put("pageInfo", pageInfo);
	     return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean submitRecordApproval(String token, Integer borrowid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblNbsjBorrowRecordEntity borrowrecord = tblNbsjBorrowRecordMapper.selectById(borrowid);
		if(borrowrecord == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
		TblNbsjProject project = tblNbsjProjectMapper.getById(borrowrecord.getProjectid().toString());
		if(project == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
		TblCirculation cir = new TblCirculation();
		try {
			
			cir.setCytype(TblNbsjBorrowRecordEntity.TYPE_DAJY);
			cir.setCycode(project.getProjectCode());
			cir.setCyname(project.getPrjoectName()+"借阅");
			cir.setCydate(new Date());
			cir.setCystate(TblCirculation.STATE_FQ);
			cir.setCyurl(TblCirculation.URL_SJBG+borrowrecord.getBorrowid());
			cir.setCyStaffid(user.getStaffid().toString());
			this.tblCirculationMapper.saveTblCirculation(cir);
			
			Map<String, Object> variables= new HashMap<String, Object>();
			variables.put(ProcessVariableEnum.model.toString(), cir);
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_JHGL.name(),cir.getCyid().toString(), variables);
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			cir.setTaskid(task.getId());
			//流程定义Id
			String businessKey = processInstance.getBusinessKey();
			String definitionId = processInstance.getProcessDefinitionId();
			cir.setBusinesskey(businessKey);
			cir.setDefinitionid(definitionId);
			this.tblCirculationMapper.updateCirculationInfoById(cir);
			taskService.complete(task.getId());
			borrowrecord.setStatus(TblNbsjBorrowRecordEntity.SPKA);
			tblNbsjBorrowRecordMapper.updateEntity(borrowrecord);
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
	public JsonBean getRecordApprovalInfo(String token, Integer borrowid, String taskId, Integer cyId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjBorrowRecordEntity borrowrecord = tblNbsjBorrowRecordMapper.selectById(borrowid);
		if(borrowrecord == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
    	TblNbsjProject project = tblNbsjProjectService.getProjectById(borrowrecord.getProjectid());
    	if(project == null) {
    		return ResponseFormat.retParam(0,50002,null);
    	}
    	TblStaff selectStaff = tblStaffMapper.getById(borrowrecord.getStaffid().toString());
    	borrowrecord.setStaffname(selectStaff.getRealname());
        List<String> btnList = null;
    	if(borrowrecord!=null){
    		if(StringUtils.isNotBlank(taskId) && borrowrecord.getStatus()!=TblNbsjBorrowRecordEntity.SPTZ){
    			btnList = processService.getButtonsForTransition(taskId);
    			resultMap.put("btnList", btnList);
    		}
    		List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(borrowrecord.getBorrowid().toString(),cyId);
    		if (cyId != null){
    			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
    			resultMap.put("cy", cy);
    		}
    		resultMap.put("taskId", taskId);
    		resultMap.put("aoptionList", ao);
    		resultMap.put("cyId", cyId);
    		resultMap.put("project", project);
    		resultMap.put("borrowrecord", borrowrecord);
    	}
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dealRecordApporvalInfo(String token, Integer cyId, String taskId, String transition, String optDesc,
			String borrowid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjBorrowRecordEntity borrowrecord = tblNbsjBorrowRecordMapper.getById(borrowid);
		if(borrowrecord == null) {
			return ResponseFormat.retParam(0,50002,null);
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
			opt.setRelationId(new BigDecimal(borrowid));
			opt.setOptState(transition);
			jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
		}
		return jsonBean;
	}

	@Override
	public JsonBean tjspBorrowDetail(Integer borrowId,String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjBorrowRecordEntity borrowrecord = tblNbsjBorrowRecordMapper.selectByIdBorrw(borrowId);
		return ResponseFormat.retParam(1,200,borrowrecord);
	}

}
