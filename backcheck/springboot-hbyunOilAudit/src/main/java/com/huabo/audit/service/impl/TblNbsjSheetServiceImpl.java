package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.*;
import com.huabo.audit.util.HttpClient;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

import cn.hutool.core.util.StrUtil;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-20
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjSheetServiceImpl extends ServiceImpl<TblNbsjSheetMapper, TblNbsjSheetEntity> implements TblNbsjSheetService {
    @Autowired
    private TblNbsjSheetMapper tblNbsjSheetMapper;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
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
    private TblNbkzRiskMapper tblNbkzRiskMapper;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private TblNbsjQuestionMapper tblNbsjQuestionMapper;
    
    @Resource
    private TaskRuntime taskruntime;
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
	private TblNbsjCertificateMapper tblNbsjCertificateMapper;
    @Resource
    private TblNbsjOperateMapper tblNbsjOperateMapper;
    @Resource
    private TblNbsjBugMapper tblNbsjBugMapper;

	@Resource
	private TblProcessAnalusisUserMapper tblProcessAnalusisUserMapper;

	@Resource
	private TblProcessAnalysisMapper tblProcessAnalysisMapper;

	@Resource
	private TblStaffMapper tblStaffMapper;

	@Resource
	private TblMyTaskMapper tblMyTaskMapper;

	@Resource
	private TblCirculationService tblCirculationService;

	@Resource
	private TblNbsjQuestionMapper questionmapper;
	
	@Resource
	private TblProcessSettingMapper processSettingMapper;

	@Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

	@Autowired
	private ImplementPlanMapper implementPlanMapper;

	@Autowired
	private TblYqnsAuditMyManuscriptMapper tblYqnsAuditMyManuscriptMapper;

	@Resource
    private UserProvider userProvider;


	@Override
	public JsonBean submitArrpoval(String token, Integer sheetid, String examination) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		TblNbsjSheetEntity sheet = tblNbsjSheetMapper.getById(sheetid.toString());
		if (sheet == null) {
			return ResponseFormat.retParam(0, 50002, null);
		}
		try {
			

//           Map<String, Object> variables= new HashMap<String, Object>();
//           variables.put(ProcessVariableEnum.model.toString(), cir);
//
//  			AgentEntity ae = new AgentEntity();
//  			if(sheet.getFirststaffid()==null||sheet.getSecondstaffid()==null) {
//  				return ResponseFormat.retParam(0,"请添加复核人",null);
//  			}
//  			ae.setReviewer(sheet.getFirststaffid().toString());
//  			ae.setReviewer_2(sheet.getSecondstaffid().toString());
//		   variables.put(ProcessVariableEnum.agent.toString(), ae);
//
//           ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ProcessEnum.SJ_DGFH.name(),cir.getCyid().toString(), variables);
//           Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			String busType=ProcessEnum.SJ_DGFH.name();
			Integer orgid=Integer.parseInt(user.getLinkOrg().getOrgid().toString());
			List<TblProcessSettingEntity> settings = processSettingMapper.selectByOrgid(busType, orgid);
			List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(ProcessEnum.SJ_DGFH.name());
			String setting=ProcessEnum.SJ_DGFH.name();
			if(settings!=null && settings.size()>0) {
				setting=settings.get(0).getModule();
				list=this.tblProcessAnalysisMapper.getByFlowSetting(setting);
			}
			
		
			HashMap<String, Object> fields = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				for (TblProcessAnalysis tblAnalysis : list) {
					TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(),sheetid.toString());
					if (analysisUser == null) {
						analysisUser = new TblProcessAnalusisUser();
						analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
						analysisUser.setFromid(sheetid.toString());
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
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("reviewer")) {
                            //一级复核人参数
                            if (sheet.getFirststaffid() == null) {
                                resultMap.put("code", "0");
                                resultMap.put("msg", "一级复核人需配置！");
                               // return resultMap;
                            }
                            fields.put("reviewer", sheet.getFirststaffid().toString());
                            System.out.println("reviewer:" + sheet.getFirststaffid());
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("reviewers")) {
                            //二级复核人参数
                            if (sheet.getSecondstaffid() == null) {
                                resultMap.put("code", "0");
                                resultMap.put("msg", "二级复核人需配置！");
                               // return resultMap;
                            }
                            fields.put("reviewers", sheet.getSecondstaffid().toString());
                            System.out.println("reviewers:" + sheet.getSecondstaffid());
                        }else {
                            //除此之外的参数都设置为当前提交人
                            if (StrUtil.isNotBlank(tblAnalysis.getUserid())) {
                                fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
                            }
                        }
						this.tblProcessAnalusisUserMapper.insertSetting(analysisUser);
					}else {
						 //提交人参数
                        if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                            fields.put("tcuserid", user.getStaffid().toString());
                           // analysisUser.setStaffid(tblAnalysis.getRolename());
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("reviewer")) {
                            //一级复核人参数
                            if (sheet.getFirststaffid() == null) {
                                resultMap.put("code", "0");
                                resultMap.put("msg", "一级复核人需配置！"); 
                               // return resultMap;
                            }
                            fields.put("reviewer", sheet.getFirststaffid().toString());
                            System.out.println("reviewer:" + sheet.getFirststaffid());
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("reviewers")) {
                            //二级复核人参数
                            if (sheet.getSecondstaffid() == null) {
                                resultMap.put("code", "0");
                                resultMap.put("msg", "二级复核人需配置！");
                               // return resultMap;
                            }
                            fields.put("reviewers", sheet.getSecondstaffid().toString());
                            System.out.println("reviewers:" + sheet.getSecondstaffid());
                        }else {
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
			String object = (String) map.get("result");
			String processInstanceId = (String) map.get("processInstanceId");
			String processDefinitionKey = (String) map.get("processDefinitionKey");
			TblCirculation cir = this.tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_DGFH, sheet.getSheetCode() ,sheet.getSheetName(), TblCirculation.URL_DGFH + sheetid, user.getStaffid(), processInstanceId, processDefinitionKey, sheet.getSheetId().toString());
			if (object != null && object.equals("true")) {
				List<TblMyTask> tasks = HttpClient.findByTask("", user.getStaffid().toString(), 1, 10000);
				if (tasks != null && tasks.size() > 0) {
					for (TblMyTask task : tasks) {
						if (task.getProcessInstanceId().equals(processInstanceId)) {
							Map<String, Object> map1 = HttpClient.handleProcessJson(user.getStaffid().toString(), "通过", task.getTaskId());
							String result = (String) map1.get("result");
							if (result != null && result.equals("true")) {
								String blande = "";
								//查询执行人
								String nextapprover = HttpClient.nextapprover(cir.getBusinesskey());
								TblProcessAnalysis analysis = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande,setting);
								Integer number = 1;
								String usertaskid = analysis.getUsertaskid();
								Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
								blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
								TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande,setting);
								TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), sheetid.toString());
								task.setFromid(sheetid.toString());
								task.setApprover(user.getRealname());
								task.setUsrid(user.getStaffid().toString());
								task.setExamination(examination);
								task.setProcessName(setting);
								if (user.getTrole() != null && user.getTrole().getRname() != null) {
									task.setApprovalrole(user.getTrole().getRname());
								}
								task.setApprovaldate(new Date());
								task.setResult("通过");
								task.setCirid(cir.getCyid().toString());
								if (nextapprover.contains("reviewer")) {
									//TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(user.getLinkDetp().getOrgid().toString());
									task.setHandle(sheet.getYjfh());
								} else {
									task.setHandle(nextapprover);
								}
								task.setAnalid(analysisUser.getAnalid().toString());
								//this.tblMytaskMapper.insertMyTaskSetting(task);
								this.tblMyTaskMapper.insertMyTaskSetting(task);

							}
						}
					}
				}
				resultMap.put("code", "1");
				resultMap.put("msg", "审批已提交！");
			} else {
				resultMap.put("code", "0");
				resultMap.put("msg", "流程提交失败！");
				log.error("发送审批失败");
			}

			cir.setTaskid(sheetid.toString());
			//流程定义Id
//            String businessKey = processInstance.getBusinessKey();
//            String definitionId = processInstance.getProcessDefinitionId();
		
			//taskService.complete(task.getId());
			sheet.setState(TblNbsjSheetEntity.STATE2);
			tblNbsjSheetMapper.updateEntity(sheet);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, 30002, null);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	/**
	 * 审计底稿-查看办理页面
	 *
	 * @param token
	 * @param sheetid
	 * @param taskId
	 * @param cyId
	 * @return
	 * @throws Exception
	 */
	@Override
	public JsonBean getApprovalInfo(String token, Integer sheetid,  Integer cyId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblNbsjSheetEntity sheet = this.tblNbsjSheetMapper.selectById(sheetid);//tblNbsjSheetMapper.getById(sheetid.toString());

		if (sheet == null) {
			return ResponseFormat.retParam(0, 50001, null);
		}

		//==
		List<TblNbsjSheetReportEntity> listSP = this.tblNbsjSheetMapper.selectListSheetReport(sheetid);
		if (null != listSP) {
			for (TblNbsjSheetReportEntity sp : listSP) {
				String orgids = sp.getSjdeptIds();
				String sysOrgName = "";
				List<TblOrganization> listOrg = this.tblNbkzRiskMapper.selectListOrgInId(orgids);
				if (null != listOrg) {
					for (int i = 0; i < listOrg.size(); i++) {
						TblOrganization org = listOrg.get(i);
						String orgname = org.getOrgname();
						sysOrgName += orgname;
						if ((listOrg.size() - 1) > i) {
							sysOrgName += ",";
						}
					}
				}
				sp.setSjdeptNames(sysOrgName);
			}
		}
		resultMap.put("listSP", listSP);


		if (cyId != null) {
			TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
			resultMap.put("cy", cy);
			List<TblMyTask> list = null;
			if (staff.getTrole() != null && staff.getTrole().getRname() != null) {
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
			resultMap.put("taskId", cy.getTaskid());
			resultMap.put("url", HttpClient.jkurl + cy.getBusinesskey());
		}

		
		//resultMap.put("aoptionList", ao);
		resultMap.put("cyId", cyId);
		resultMap.put("sheet", sheet);
		List<TblMyTask> taskList = this.tblMyTaskMapper.selectByFormId(sheet.getSheetId().toString());
		resultMap.put("taskList", taskList);
		TblNbsjProject project = tblNbsjProjectMapper.selectPJById(sheet.getProjectId());
		resultMap.put("project", project);

		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean dealApporval(String token, Integer cyId, String taskId, String transition, String optDesc,
								 String sheetid , String processDefinitionId , String processInstanceId) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblNbsjSheetEntity sheet = tblNbsjSheetMapper.getById(sheetid.toString());
		if (sheet == null) {
			return ResponseFormat.retParam(0, 50002, null);
		}
		JsonBean jsonBean = null;
		TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());

		if (null != cy) {
			Map<String, Object> variables = new HashMap<>();
			variables.put(ProcessVariableEnum.model.toString(), cy);
			if (StrUtil.isNotBlank(transition)) {
				variables.put(ProcessVariableEnum.transition.toString(), transition);
			}
			Map<String,Object> resultMap = new HashMap<String, Object>(0);
			String orgid = null;
			try {

//                taskService.claim(taskId, staff.getStaffid().toString());
//                taskService.complete(taskId, variables);

				//taskruntime.claim(TaskPayloadBuilder.claim().withTaskId(taskId).withAssignee(staff.getStaffid().toString()).build());
				//taskruntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).withVariables(variables).build());
				//TblCyhwUnit aw = tblCyhwUnitService.getEntity(NumberUtil.newBigInteger(sheetid));

				if(taskId!=null && !taskId.equals("")){

					Map<String, Object> map = HttpClient.handleProcessJson(user.getStaffid().toString(),transition,taskId);
					String object = (String) map.get("result");
					if(object!=null && object.equals("true")){
						TblMyTask task=new TblMyTask();
						List<TblMyTask> myTasks = tblMyTaskMapper.findOndbyFrom(sheetid);
						TblProcessAnalysis findOnd = null;
						if(CollectionUtils.isEmpty(myTasks)){
							findOnd = tblProcessAnalysisMapper.findOndBytakdid("");
						}else{
							findOnd= tblProcessAnalysisMapper.findOndAnalysis(myTasks.get(0).getAnalid());
						}
						Integer number=1;
						String usertaskid = findOnd.getUsertaskid();
						Integer num=Integer.parseInt(usertaskid.substring(usertaskid.length()-1,usertaskid.length()))+number;
						String blande=usertaskid.substring(0,usertaskid.length()-1)+num;

						TblProcessAnalysis analysis =null;
						if(transition!=null && transition.equals("退回") && user.getTrole()!=null && !user.getTrole().getRname().equals("律师")){
							analysis = tblProcessAnalysisMapper.findOndBytakdidstart("",findOnd.getProcessname());
						}else if(transition!=null && transition.equals("退回") && user.getTrole()!=null && user.getTrole().getRname().equals("律师")){
							analysis = tblProcessAnalysisMapper.findOndBytakdidAnId("usertask2",findOnd.getProcessname());
						}else{
							analysis = tblProcessAnalysisMapper.findOndBytakdidAnId(blande,findOnd.getProcessname());
						}
						TblProcessAnalusisUser analysisUser = null;
						if(analysis!=null && analysis.getAnalid()!=null){
							analysisUser = tblProcessAnalusisUserMapper.findOnd(analysis.getAnalid().toString(), sheetid);
						}
						TblCirculation circulation = tblCirculationMapper.findById(cyId.toString());
						//查询执行人
						String nextapprover = HttpClient.nextapprover(circulation.getBusinesskey());
						task.setApprovaldate(new Date());
						if(user.getTrole()!=null && user.getTrole().getRname()!=null){
							task.setApprovalrole(user.getTrole().getRname());
						}
						task.setApprover(user.getRealname());
						task.setExamination(optDesc);
						task.setFromid(sheetid);
						task.setProcessDefinitionId(processDefinitionId);
						task.setUsrid(user.getStaffid().toString());
						task.setCirid(cyId.toString());
						task.setResult(transition);
						task.setProcessName(myTasks.get(0).getProcessName());
						task.setTaskId(taskId);
						task.setProcessInstanceId(processInstanceId);
						//task.setImgbasestr(imgBaseStr);
						task.setAnalid("");
						if(analysis!=null) {
							task.setAnalid(analysis.getAnalid().toString());
						}
						if(StringUtils.isBlank(nextapprover)){//transitionName.equals("完成")||
							task.setHandle("无");
							circulation.setCystate("已完成");
							//审核完成
							sheet.setState(TblNbsjSheetEntity.STATE4);
							List<TblNbsjQuestionEntity>  listq = questionmapper.findNbsjQuestionBySheetIdSp(sheet.getSheetId());
							for (TblNbsjQuestionEntity ques : listq) {
								ques.setStatus(TblNbsjQuestionEntity.STATUSYES);
								questionmapper.updateEntity(ques);
							}
						}else{
							//退回or通过
							TblStaff findById = tblStaffMapper.getById(circulation.getCyStaffid());//表单提交人
							if(nextapprover.contains("reviewer")) {

								task.setHandle(sheet.getYjfh());
							}else if (nextapprover.contains("reviewers")) {
								//分管领导

								task.setHandle(sheet.getEjfh());
							}else if(nextapprover.contains("tcuserid")) {
								//退回到创建人
								task.setHandle(findById.getRealname());
							}else {
								//角色
								task.setHandle(nextapprover);
							}
						}
						if(sheet.getState()==2){
							circulation.setCystate("审批中");
							sheet.setState(TblNbsjSheetEntity.STATE2);
						}
						if(transition!=null && transition.equals("退回") || transition!=null && transition.equals("驳回")){
							circulation.setCystate("需调整");
							sheet.setState(TblNbsjSheetEntity.STATE5);
						}



						tblCirculationService.upateTblCirculation(circulation);

						tblNbsjSheetMapper.updateEntity(sheet);

						tblMyTaskMapper.insertMyTaskSetting(task);
					}


					return ResponseFormat.retParam(1, "办理成功！", "");
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseFormat.retParam(0, 30002, null);
			}
		}
		return jsonBean;

	}

    
	@Override
	public JsonBean projectStandardDgPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
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
    	
    	if(null == tBlNbsjSheetVo.getProjectId() ){
			//==查询当前实施的项目！
			ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,30003,null);
			}
			Integer projectId = tnp.getId().intValue();
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,null);
			}
    		tBlNbsjSheetVo.setProjectId(new BigDecimal(projectId));
    	}
    	if(tBlNbsjSheetVo.getStaffid()==null || tBlNbsjSheetVo.getStaffid().equals("")) {
    		tBlNbsjSheetVo.setStaffid(loginStaff.getStaffid().toString());
    	}
    	
    	PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = new PageInfo<TblYqnsAuditMyManuscriptEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
		/*TblNbsjProject project = tblNbsjProjectMapper.selectPJById(tBlNbsjSheetVo.getProjectid());
		resultMap.put("project", project);*/
    	pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo,null));
    	pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo,null));
    	pageInfo.getTotalPage();
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
	public JsonBean projectStandardDgAdd(TblNbsjSheetEntity sheet, String token,String attids,String srJson,String certificateIds,String bugIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjSheetMapper.selectPlanCodeByOrgid(sheet);
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
		sheet.setProjectId(projectId);
    	
		sheet.setAuditStaffId(loginStaff.getStaffid().intValue());
		sheet.setCreateTime(new Date());
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(sheet.getSheetId() != null) {
			//修改；
			if(sheet.getRiskLevel()!=null&&sheet.getRiskLevel().equals("否")){
				sheet.setBelongType(null);
				sheet.setDetailType(null);
				sheet.setHgDetailType(null);
				sheet.setRelatedMoney(0.0);
				sheet.setAuditDiscoverable(null);
			}
			this.tblNbsjSheetMapper.updateEntity(sheet);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationSheet(sheet.getSheetId());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationSheet(id, sheet.getSheetId());
				}
			}
			//先清除 报告内容/问题描述
			this.tblNbsjSheetMapper.deleteSheetReportBySheetId(sheet.getSheetId());
			//先清除审计取证单关联
			tblNbsjCertificateMapper.deleteSheetCertificate(sheet.getSheetId());
			//清除缺陷关联底稿
			if(org.apache.commons.lang.StringUtils.isNotBlank(bugIds)){
				tblNbsjBugMapper.deleteSheetBug(sheet.getSheetId());
			}
		}else {
			//新增；
			sheet.setState(0);//流程状态改为流程平台格式，0为未审批
			this.tblNbsjSheetMapper.insertEntity(sheet);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationSheet(id, sheet.getSheetId());
				}
			}
		}
		
		//====报告内容/问题描述===========
		if(null != srJson && !"".equals(srJson.trim())) {
			List<TblNbsjSheetReportEntity> srList = new ArrayList<TblNbsjSheetReportEntity>();
			srList = JSONObject.parseArray(srJson,TblNbsjSheetReportEntity.class);
			for (TblNbsjSheetReportEntity sr : srList) {
				sr.setSheetId(sheet.getSheetId());
				this.tblNbsjSheetMapper.insertSheetReport(sr);
			}
		}
	 
		//关联审计取证单
				if(null != certificateIds) {
					String[] cerIds = certificateIds.split(",");
					for (int i = 0; i < cerIds.length; i++) {
						if(org.apache.commons.lang.StringUtils.isNotBlank(cerIds[i])){
						   tblNbsjCertificateMapper.insertSheetCertificate(sheet.getSheetId(), Integer.valueOf(cerIds[i]));
						}
						}
				}
		//==审计发现
		String riskLevel = sheet.getRiskLevel();
		if("是".equals(riskLevel)) {
			
			List<TblNbsjQuestionEntity> listQuestion = this.tblNbsjQuestionMapper.findNbsjQuestionBySheetIdSp(sheet.getSheetId());
			if(null==listQuestion || listQuestion.size()==0) {
				TblNbsjQuestionEntity question = new TblNbsjQuestionEntity();
				question.setNbsjSheet(sheet);
				question.setStatus(TblNbsjQuestionEntity.NUSTATUS);
				question.setGroupStatus(TblNbsjQuestionEntity.GROUPNO);
//				this.tblNbsjQuestionService.save(question);
				this.tblNbsjQuestionMapper.insertEntity(question);
			}
			
		}
		
		//底稿关联缺陷bugid
	     if(org.apache.commons.lang.StringUtils.isNotBlank(bugIds)){
	    	 String[] ids=bugIds.split(",");
	    	 for (int i = 0; i < ids.length; i++) {
					if(org.apache.commons.lang.StringUtils.isNotBlank(ids[i])){
					   tblNbsjBugMapper.insertSheetBug(sheet.getSheetId(), Integer.valueOf(ids[i]));
					}
					}
	     }
		
		// 我的底稿-业务单元关联底稿后，修改我的任务状态为执行中(2)状态
		if (sheet.getTargetId() != null) {
			this.tblNbsjOperateMapper.InExecuion(sheet.getTargetId());
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Sheet",sheet);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean projectStandardDgDelete(Integer sheetid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjSheetEntity plan = this.tblNbsjSheetMapper.selectById(sheetid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjSheetMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(0,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		
		//删除报告内容
		this.tblNbsjSheetMapper.deleteSheetReportBySheetId(sheetid);
		//删除审计发现
		this.tblNbsjQuestionMapper.deleteBySheet(sheetid);
		//删除底稿
		this.tblNbsjSheetMapper.deleteById(sheetid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean dgAllPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
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
		//TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		//if(tnp == null) {
		//	return ResponseFormat.retParam(0,30003,resultMap);
		//}
		//Integer projectId = tnp.getProjectId();
		//if(null == projectId) {
		//	return ResponseFormat.retParam(0,30003,resultMap);
		//}
		//tBlNbsjSheetVo.setProjectid(projectId);
    	PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = new PageInfo<TblYqnsAuditMyManuscriptEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo,null));
    	pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo,null));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean dgglPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
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
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		tBlNbsjSheetVo.setProjectId(new BigDecimal(projectId));
		TblNbsjProject project = tblNbsjProjectMapper.selectPJById(projectId);
		resultMap.put("project", project);
    	PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = new PageInfo<TblYqnsAuditMyManuscriptEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet); 
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo,tBlNbsjSheetVo.getOperateid()));
    	pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo,tBlNbsjSheetVo.getOperateid()));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean dgglPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) throws Exception {
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
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		tBlNbsjSheetVo.setProjectId(new BigDecimal(projectId));
		TblNbsjProject project = tblNbsjProjectMapper.selectPJById(projectId);
		resultMap.put("project", project);
    	PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = new PageInfo<TblYqnsAuditMyManuscriptEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo,operateid));
    	pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo,operateid));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	@Override
	public JsonBean chooseProjectSheet(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
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
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		tBlNbsjSheetVo.setProjectId(new BigDecimal(projectId));
    	PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = new PageInfo<TblYqnsAuditMyManuscriptEntity>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo,null));
    	pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo,null));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	} 
	
	@Override
	public JsonBean ifPmOrLeader(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	//==查询当前实施的项目！
    	try {
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			resultMap.put("ifLeader", false);
	    	return ResponseFormat.retParam(1,200,resultMap);

		}
		Integer projectId = tnp.getProjectId();
		int size=this.tblNbsjSheetMapper.selectifPmOrLeader(loginStaff.getStaffid(),projectId);
		if(size>0){
			resultMap.put("ifLeader", true);
		}else{
			resultMap.put("ifLeader", false);
		}
    	} catch (Exception e) {
        	return ResponseFormat.retParam(1,1003,resultMap);

		}
    	return ResponseFormat.retParam(1,200,resultMap);
	} 
	
	@Override
	public JsonBean findNbsjSheetDetail(String token, Integer sheetid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		TblYqnsAuditMyManuscriptEntity plan = this.tblYqnsAuditMyManuscriptMapper.selectById(sheetid);
		resultMap.put("sheet", plan);
		ImplementPlanEntity project = implementPlanMapper.selectById(plan.getProjectId().toString());
		resultMap.put("project", project);
		
		//==
		List<TblNbsjSheetReportEntity> listSP = this.tblNbsjSheetMapper.selectListSheetReport(sheetid);
		if (null != listSP) {
			for (TblNbsjSheetReportEntity sp : listSP) {
				String orgids = sp.getSjdeptIds();
				
				String sysOrgName = "";
				List<TblOrganization> listOrg = this.tblNbkzRiskMapper.selectListOrgInId(orgids);
				if(null != listOrg) {
					for (int i = 0; i < listOrg.size(); i++) {
						TblOrganization org = listOrg.get(i);
						String orgname = org.getOrgname();
						sysOrgName += orgname;
						if((listOrg.size()-1) > i) {
							sysOrgName += ",";
						}
					}
				}
				sp.setSjdeptNames(sysOrgName);
			}
		}
		resultMap.put("listSP", listSP);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}



	//    /**
//    * 条件查询 封装QueryWrapper
//    * @param model
//    * @return
//    */
//    @Override
//    public LambdaQueryWrapper<TblNbsjSheetEntity> onSelectWhere(TblNbsjSheetEntity model) {
//        LambdaQueryWrapper<TblNbsjSheetEntity>  queryWrapper=new QueryWrapper<TblNbsjSheetEntity>().lambda();
//        if (model == null) {
//            return queryWrapper;
//        }
//
//
//        return queryWrapper;
//    }
//
//    /**
//    *  封装保存方法
//    * @param model
//    * @return
//    */
//    @Override
//    public boolean saveTblNbsjSheet(TblNbsjSheetEntity model) {
//        //在保存和更新之前的操作 同步字段id、name
//        beforSaveandUpdate(model);
//
//        return save(model);
//    }
//
//    /**
//    *  封装更新方法
//    * @param model
//    * @return
//    */
//    @Override
//    public boolean updateTblNbsjSheet(TblNbsjSheetEntity model) {
//        if(model.getSheetId()==null || model.getSheetId()==0){
//            throw new CommercialException(ResultCode.BIZ_ERROR,"teamId不能为空！");
//        }
//        //在保存和更新之前的操作 同步字段id、name
//        beforSaveandUpdate(model);
//
//        return updateById(model);
//    }
//
    @Override
    public List<TblNbsjSheetEntity> selectByProjectId(Integer projectId) {

        return null;
    }
//
//    /**
//    * @description: 在保存和更新之前的操作 同步字段id、name
//    * @param model:
//    * @return: void
//    * @author: ziyao
//    * @date: 2022-04-20
//    */
//    private void beforSaveandUpdate(TblNbsjSheetEntity model){
//
//    }
//
//
//	@Override
//	public List<Object[]> OBJfindAllByProjectid(String string, BigDecimal staffid) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void merge(TblNbsjSheetEntity blNbsjSheet) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean getSheetByCode(Integer projectId, String sheetCode) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Integer findByCount(String targetId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TblNbsjSheetEntity> findDgByQues(Integer projectId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TblNbsjSheetEntity> findDgByProjectId(Integer projectId, Object object) {
//		// TODO Auto-generated method stub
//		return null;
//	}



	@Override
	public List<TblNbsjSheetEntity> OBJfindAllByProjectid(Integer projectid, String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return null;
		}
		
		List<TblNbsjSheetEntity> list = this.tblNbsjSheetMapper.getExportList(projectid);
		return list;
	}

	
	

	@Override
	public List<TblNbsjSheetEntity> OBJfindAllByProjectidstaff(Integer projectid, String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return null;
		}
		
		List<TblNbsjSheetEntity> list = this.tblNbsjSheetMapper.getExportListstaff(projectid,staff.getStaffid());
		return list;
	}

	
	@Override
	public JsonBean sheetReportDel(String token, Integer reportid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		this.tblNbsjSheetMapper.deleteSheetReportById(reportid);
		
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
        this.tblNbsjSheetMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	
	@Override
	public JsonBean getNbsjBugList(String token, Integer sheetid) throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	 List<TblNbsjBugEntity>  list=this.tblNbsjBugMapper.selectNbsjBugList(sheetid);
    	resultMap.put("data", list);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dgAllPageList(String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		Integer count = tblNbsjSheetMapper.selectSheetByprojectId(tnp.getProjectId());
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", count);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public   List<TblNbsjSheetEntity> getReportSheet(String token) throws Exception {
		// TODO Auto-generated method stub
		List<TblNbsjSheetEntity> objList=null;
		 TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return  objList;
		}
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
		if(tnp == null) {
			return null;
		}
         objList =tblNbsjSheetMapper.getReportSheet(tnp.getProjectId().toString());
		return objList;
	}
	
	
	
	
	@Override
	public JsonBean findbywtsl(String token,String type,String projectid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		if(type!=null && type.equals("1")) {
			List<TblNbsjSheetEntity> list = tblNbsjSheetMapper.findwtzs(projectid);
			resultMap.put("data", list);
			
		}
		
		if(type!=null && type.equals("2")) {
			List<TblNbsjSheetEntity> list = tblNbsjSheetMapper.finddzgwts(projectid);
			resultMap.put("data", list);
			
		}
		
		if(type!=null && type.equals("3")) {
			List<TblNbsjSheetEntity> list = tblNbsjSheetMapper.findyzgwts(projectid);
			resultMap.put("data", list);
			
		}
		
		if(type!=null && type.equals("4")) {
			List<TblNbsjSheetEntity> list = tblNbsjSheetMapper.findwzgwts(projectid);
			resultMap.put("data", list);
			
		}

		
		
    	return ResponseFormat.retParam(1,200,resultMap);
	}
}
