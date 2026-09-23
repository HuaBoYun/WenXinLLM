package com.huabo.audit.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.util.PageResult;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.DateUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblMyTask;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.huabo.audit.oracle.entity.TblNbsjType;
import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;
import com.huabo.audit.oracle.entity.TblProcessAnalysis;
import com.huabo.audit.oracle.entity.TblProcessSettingEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblFlowMapper;
import com.huabo.audit.oracle.mapper.TblMyTaskMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjPlanProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblProcessAnalusisUserMapper;
import com.huabo.audit.oracle.mapper.TblProcessAnalysisMapper;
import com.huabo.audit.oracle.mapper.TblProcessSettingMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.vo.TblNbsjAuditPlanVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblAutonoNumberService;
import com.huabo.audit.service.TblCirculationService;
import com.huabo.audit.service.TblMyTaskService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblProcessAnalusisUserService;
import com.huabo.audit.service.TblProcessAnalysisService;
import com.huabo.audit.util.HttpClient;
import com.huabo.audit.util.ModuleCodeDef;
import com.huabo.audit.util.R;

import cn.hutool.core.util.StrUtil;

/**
 * 描述:计划编号实现类
 * author: lyz
 * date: 2022-04-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjAuditplanServiceImpl implements TblNbsjAuditplanService {
    @Autowired
    private ActivityPluginsService activityPluginsService;
    @Autowired
    private TblNbsjProjectMapper tblNbsjProjectMapper;
    @Resource
    private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    @Resource
	public TblNbsjPlanProjectMapper tblNbsjPlanProjectMapper;
    
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
    
    @Autowired
    private TblAutonoNumberService tblAutonoNumberService;
    
    @Resource
    private TblAuditOptionService tblAuditOptionService;
    
    @Resource
    private TblStaffMapper tblStaffMapper;
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
  	private  TblProcessAnalusisUserService tblProcessAnalusisUserService;
    
    @Resource
 	private TblMyTaskService tblMyTaskService;
    
	@Resource
	private TblCirculationService tblCirculationService;
	
	
	@Resource
	private TblProcessSettingMapper processSettingMapper;
	
	@Resource
    private TblNbsjProjectService tblNbsjProjectService;
	
	@Resource
    private UserProvider userProvider;
	
    @Override
    public InputStream lookCurrentProcessImage(String taskId) {
		// 获取当前任务
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task == null){
			return null;
		}
		//获取当前模型
		BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        ProcessDiagramGenerator ge = new DefaultProcessDiagramGenerator();
        InputStream resource = ge.generateDiagram(bpmnModel, "png",
                runtimeService.getActiveActivityIds(task.getExecutionId()),
                new ArrayList<String>(), "宋体", "宋体", null, 1.0d);
        return resource;
    }
    @Override
	public JsonBean getNbsjAuditPlanListForMergeNbsjProject(String token) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	List<TblNbsjAuditplan> planList = this.tblNbsjAuditplanMapper.selectPlanListForMergeNbsjProject(loginStaff.getCurrentOrg().getOrgid());
    	resultMap.put("planList", planList);
		return ResponseFormat.retParam(1,200,planList);
	}
    @Override
	public JsonBean planManagePageList(String token, Integer pageNumber, Integer pageSize,TblNbsjAuditPlanVo tblNbsjAuditPlanVo, String planStartDate, String planEndDate) throws Exception {
    	
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
    /*	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjAuditplan> pageInfo = new PageInfo<TblNbsjAuditplan>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	pageInfo.setTlist(this.tblNbsjAuditplanMapper.selectNbsjAuditPlanListByPageInfo(pageInfo,planStartDate,planEndDate,loginStaff.getCurrentOrg().getOrgid(),tblNbsjAuditPlanVo));
    	pageInfo.setTotalRecord(this.tblNbsjAuditplanMapper.selectNbsjAuditPlanCountByPageInfo(pageInfo,planStartDate,planEndDate,loginStaff.getCurrentOrg().getOrgid(),tblNbsjAuditPlanVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);*/

		//链表分页  xml 写法
		com.github.pagehelper.PageInfo<TblNbsjAuditplan> pageInfo = PageMethod.startPage(pageNumber,pageSize)
				.doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjAuditplanMapper.findList(planStartDate,planEndDate, loginStaff.getCurrentOrg().getOrgid(),tblNbsjAuditPlanVo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjAuditplan> build = new PageResult<TblNbsjAuditplan>().build(pageInfo);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("identifier", activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name()));
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
    

    
    @Override
	public JsonBean mergePlanManageInfo(TblNbsjAuditplan plan, String token, String planStartTime, String planEndTime,String attIds) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjAuditplanMapper.selectPlanCodeByOrgid(plan);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
    	
		
		if(planStartTime != null && !"".equals(planStartTime)) {
			plan.setStarttime(DateUtil.formatDate(planStartTime,"yyyy-MM-dd"));
		}
		if(planEndTime != null && !"".equals(planEndTime)) {
			plan.setEndtime(DateUtil.formatDate(planEndTime,"yyyy-MM-dd"));
		}
		
		
		if(plan.getPlanid() != null) {
			//修改；
			this.tblNbsjAuditplanMapper.updateEntity(plan);
		}else {
			//新增；
			plan.setCreatestaffid(loginStaff.getStaffid());
			plan.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
			plan.setCreatetime(new Date());
			plan.setStatus(0);
			this.tblNbsjAuditplanMapper.insertEntity(plan);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		if(attIds != null && !"".equals(attIds)) {
			String[] attId = attIds.split(",");
			for (String aid : attId) {
				this.tblNbsjAuditplanMapper.insertAttInfoForPlan(plan.getPlanid(),aid);
			}
		}
		resultMap.put("auditPlan",plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

    @Override
	public JsonBean findNbsjAuditPlanDetail(String token, Integer planid) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planid);
		List<TblNbsjPlanProject> planList =  this.tblNbsjPlanProjectMapper.selectPlanProjectListInfoByPlanId(planid);
		plan.setPlanProjectList(planList);
		resultMap.put("auditPlan", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

    
    /**
     * @description 删除计划
     * @author lyz
     * @date 2022/4/19 11:03
     */
    @Override
    public JsonBean deletePlanManageByPlanId(Integer planId, String token) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
		
		if(plan == null) {
			return ResponseFormat.retParam(1,50001,null);
		}
		
		if (plan.getOpinionstatus() == TblNbsjAuditplan.SPNO) {//
			//删除审计计划中的审计项目
			this.tblNbsjPlanProjectMapper.deletePlanProjectByPlanId(planId);
			//删除所有附件
			List<String> attIdList = this.tblNbsjAuditplanMapper.findAttIdListByPlanId(planId);
			for (String attId : attIdList) {
				this.deleteRealtionAttInfo(attId);
			}
			this.tblNbsjAuditplanMapper.deleteAuditPlanEntityById(planId);
			return ResponseFormat.retParam(1,200,null);
        } else {
            return ResponseFormat.retParam(1,50001,null);
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
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
//        if (att == null) {
//            return R.fail("附件不存在或已经删除");
//        }
//        int index = att.getAttpath().lastIndexOf("/") + 1; //右边第一个/
//        String path = att.getAttpath().substring(0, index);
//        String fileName = att.getAttpath().substring(index);
//        try {
//            b = FtpUtil.removeFile(fileName, path);
//        } catch (Exception e) {
//        	return R.fail("文件删除失败！");
//        }
//        if (!b) {
//        	return R.fail("文件删除失败！");
//        }
        this.tblNbsjAuditplanMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}

	@Override
	public JsonBean getNbsjAuditPlanDateInfo(String token, Integer planId) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
		resultMap.put("planStartDate", DateUtil.parseDate(plan.getStarttime(),"yyyy-MM-dd"));
		resultMap.put("planEndDate", DateUtil.parseDate(plan.getEndtime(), "yyyy-MM-dd"));
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean getAuditPlanAttInfo(String token, Integer planId) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListByAuditPlanId(planId);
		resultMap.put("attList", attList);
		return ResponseFormat.retParam(1,200,attList);
	}
	
	@Override
    public Map<String, Object> viewOppsiteActiviti(Integer planId,String businessKey) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(planId.toString());
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("plan", plan);
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
                TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
                List<TblMyTask> list = this.tblMytaskMapper.findByLendid(planId.toString());
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("plan", plan);
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
	public JsonBean submitAuditplanArrpoval(String token, Integer planId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblCirculation cir = null;
	try {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
		if(plan == null) {
			return ResponseFormat.retParam(0,50002,null);
		}
		if (plan.getOpinionstatus() != null && plan.getOpinionstatus()  == 1) {
			return ResponseFormat.retParam(0,30007,null);
		}
		if (plan.getOpinionstatus() != null && plan.getOpinionstatus() == 3) {
			return ResponseFormat.retParam(0,30009,null);
		}
		if ((plan.getOpinionstatus() != null && plan.getOpinionstatus() == 2) || (plan.getOpinionstatus() != null && plan.getOpinionstatus()== 4) || (plan.getOpinionstatus() == 5)) {
			resultMap.put("codes", "0");
			resultMap.put("msg", "流程进行中！");
			return ResponseFormat.retParam(0,30002,resultMap);
		}
			List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(ProcessEnum.SJ_JHGL.name());
			String busType=ProcessEnum.SJ_JHGL.name();
			Integer orgid=Integer.parseInt(user.getLinkOrg().getOrgid().toString());
			List<TblProcessSettingEntity> settings = processSettingMapper.selectByOrgid(busType, orgid);
			String setting=ProcessEnum.SJ_JHGL.name();
			if(settings!=null && settings.size()>0) {
				setting=settings.get(0).getModule();
				list=this.tblProcessAnalysisMapper.getByFlowSetting(setting);
			}
			
			HashMap<String, Object> fields = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				for (TblProcessAnalysis tblAnalysis : list) {
					TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(),planId.toString());
					if (analysisUser == null) {
						analysisUser = new TblProcessAnalusisUser();
						analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
						analysisUser.setFromid(planId.toString());
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
			plan.setOpinionstatus(TblNbsjAuditplan.SPKA);
	        this.tblNbsjAuditplanMapper.updateEntity(plan);
	        cir = this.tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_JHSP,plan.getPalnyear(), plan.getPlanname(), TblCirculation.URL_JHSP+plan.getPlanid(), user.getStaffid(), processInstanceId, processDefinitionKey, planId.toString());
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
								TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), planId.toString());
								task.setFromid(planId.toString());
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
				resultMap.put("codes", "1");
				resultMap.put("msg", "审批已提交！");
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
       /* TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
        TblCirculation cir = new TblCirculation();
        try {
           cir.setCytype(TblCirculation.TYPE_JHSP);
           cir.setCycode(plan.getPalnyear());
           cir.setCyname(plan.getPlanname());
           cir.setCydate(new Date());
           cir.setCystate(TblCirculation.STATE_FQ);
           cir.setCyurl(TblCirculation.URL_JHSP+plan.getPlanid());
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
           plan.setOpinionstatus(TblNbsjAuditplan.SPKA);
           this.tblNbsjAuditplanMapper.updateEntity(plan);
        } catch (Exception e) {
        	e.printStackTrace();
        	if(cir != null && cir.getCyid() != null) {
        		this.tblCirculationMapper.deleteEntityById(cir.getCyid());
        	}
        	return ResponseFormat.retParam(0,30002,null);
        }*/
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getAuditPlanApprovalInfo(String token, Integer planId, String taskId,Integer cyId,String v) throws Exception {

		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planId);
		//TblCirculation cy = this.tblCirculationMapper.selectCiculaInfoById(planId.toString());
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
        List<TblMyTask> taskList = this.tblMytaskMapper.selectByFormId(planId.toString());
        resultMap.put("cz", "sp");
    	resultMap.put("cyId", cyId);
		resultMap.put("taskList", taskList);
		resultMap.put("url", HttpClient.jkurl + cy.getBusinesskey());
		resultMap.put("plan", plan);
		resultMap.put("v", v);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dealAuditPlanApporvalInfo(String token, Integer cyId, String taskId, String transition,
			String optDesc, String planId,String processDefinitionId,String processInstanceId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(StringUtils.isBlank(planId)){
			return ResponseFormat.retParam(0,20006,null);
		}
		JsonBean jsonBean = null;
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		try {
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(Integer.valueOf(planId));
		if(taskId!=null && !taskId.equals("")){
			Map<String, Object> map = HttpClient.handleProcessJson(staff.getStaffid().toString(),transition,taskId);
			String object = (String) map.get("result");
			if(object!=null && object.equals("true")){
				TblMyTask task=new TblMyTask();
				TblMyTask oldtask = tblMyTaskService.findOndbyFrom(planId);
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
					analysisUser = tblProcessAnalusisUserService.findOnd(analysis.getAnalid().toString(), planId);
				}
				TblCirculation circulation = tblCirculationMapper.findById(cyId.toString());
				//查询执行人 
				String nextapprover = HttpClient.nextapprover(circulation.getBusinesskey());
				System.out.println("nextapprover===================:"+nextapprover);
				task.setApprovaldate(new Date());
				if(staff.getTrole()!=null && staff.getTrole().getRname()!=null){
					task.setApprovalrole(staff.getTrole().getRname());
				}
				task.setApprover(staff.getRealname());
				task.setExamination(optDesc);
				task.setFromid(planId);
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
					plan.setOpinionstatus(TblNbsjAuditplan.SPWC);
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
				
				if(plan.getOpinionstatus() ==2 ){
					plan.setOpinionstatus(TblNbsjAuditplan.SPKA);
					circulation.setCystate("审批中");
				}
				if(transition!=null && transition.equals("退回")){
					circulation.setCystate("需调整");
					 plan.setOpinionstatus(TblNbsjAuditplan.SPTZ);
				}
//				if(transition!=null && transition.equals("终止")){
//					circulation.setCystate("终止");
//					task.setHandle("无");
//				}
				tblMyTaskService.insertMyTaskSetting(task);
				tblCirculationMapper.updateCirculationInfoById(circulation);
				 this.tblNbsjAuditplanMapper.updateEntity(plan);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		return jsonBean;
	}
		/*TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
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
			opt.setRelationId(new BigDecimal(planId));
			opt.setOptState(transition);
			jsonBean = this.tblAuditOptionService.saveAuditOptionInfo(cy,opt);
		}*/
		
		 return ResponseFormat.retParam(1,200,null);
		//return jsonBean;
	}

	@Override
	public JsonBean getAuditPlanViewInfo(String token, String planName, String planYear, Integer pageNumber,
			Integer pageSize) throws Exception {
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
    	
    	TblNbsjAuditplan tblNbsjAuditplan = new TblNbsjAuditplan();
    	PageInfo<TblNbsjAuditplan> pageInfo = new PageInfo<TblNbsjAuditplan>();
    	tblNbsjAuditplan.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
    	tblNbsjAuditplan.setPlanname(planName);
    	tblNbsjAuditplan.setPalnyear(planYear);
    	pageInfo.setCondition(tblNbsjAuditplan);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	pageInfo.setTlist(this.tblNbsjAuditplanMapper.selectAuditPlanListView(pageInfo));
    	pageInfo.setTotalRecord(this.tblNbsjAuditplanMapper.selectAuditPlanCountView(pageInfo));
    	resultMap.put("pageInfo", pageInfo);
    	return  ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findNbsjAuditPlanViewDetail(String token, Integer planid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjAuditplan plan = this.tblNbsjAuditplanMapper.selectNbsjAuditPlanEntityById(planid);
		List<TblNbsjProject> projectList =  this.tblNbsjProjectMapper.selectProjectListInfoByPlanId(planid);
		//plan.setProjectList(projectList);
		resultMap.put("auditPlan", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean addAutoPlan(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblNbsjAuditplan plan = new TblNbsjAuditplan();
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//查询是否已存在 当前用户、本年度、年度计划
		List<TblNbsjAuditplan> list=tblNbsjAuditplanMapper.selectPlanListByyeartz(year+"", "调整计划",loginStaff.getLinkOrg().getOrgid());
		if(list!=null && list.size()>0) {
			plan=list.get(0);
			
			resultMap.put("tips","已存在调整计划");
		}else {
			//新增；
			plan.setCreatestaffid(loginStaff.getStaffid());
			plan.setAuditorgid(loginStaff.getLinkOrg().getOrgid());
			plan.setCreatetime(new Date());
			plan.setStatus(0);
			
			
			//计划编号
//			String cnttype = project.getCntType();
//			TblNbsjType tblNbsjType = this.tblNbsjTypeMapper.selectNbsjType(cnttype);
			String planCode = year+"-"+loginStaff.getLinkOrg().getOrgnumber()+"-";
			//查询自增数据
			String no = this.tblNbsjAuditplanMapper.selectMaxPlanCode(planCode);
			if(no != null) {
//				 no = no.replace(planCode, "");
			}else {
				no = "0";
			}
			planCode += (Integer.parseInt(no)+1);
			//年度—公司编号—审计类型编号—自增数据
			plan.setPlancode(planCode);
			
			plan.setPlanname("调整计划");
			plan.setPalnyear(year+"");
			plan.setPlantype("调整计划");
			
			this.tblNbsjAuditplanMapper.insertEntity(plan);
		}
		
		//计划外、未审批的项目，更新planid
		this.tblNbsjPlanProjectMapper.updatePjPlanByWspJhw(plan.getPlanid().intValue(),loginStaff.getLinkOrg().getOrgid());
		
		resultMap.put("auditPlan",plan);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean getAutoCodeBySjtz(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String advicecoed = year+"-"+montrStr+"-"+ModuleCodeDef.SJTZ+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxAdviceCode(advicecoed);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(advicecoed, "");
		}else {
			no = "0";
		}
		advicecoed += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",advicecoed);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByYdgl(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String dpnumber = year+"-"+montrStr+"-"+ModuleCodeDef.YDGL+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxDpnumber(dpnumber);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(dpnumber, "");
		}else {
			no = "0";
		}
		dpnumber += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",dpnumber);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByWddg(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String sheetCode = year+"-"+montrStr+"-"+ModuleCodeDef.WDDG+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxSheetcode(sheetCode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(sheetCode, "");
		}else {
			no = "0";
		}
		sheetCode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",sheetCode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeBySjjys(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String procode = year+"-"+montrStr+"-"+ModuleCodeDef.JYS+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxProcode(procode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(procode, "");
		}else {
			no = "0";
		}
		procode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",procode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByQxgl(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String bugnumber = year+"-"+montrStr+"-"+ModuleCodeDef.QXGL+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxBugnumber(bugnumber);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(bugnumber, "");
		}else {
			no = "0";
		}
		bugnumber += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",bugnumber);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByFxfx(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String risknumber = year+"-"+montrStr+"-"+ModuleCodeDef.FXFX+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxRisknumber(risknumber);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(risknumber, "");
		}else {
			no = "0";
		}
		risknumber += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",risknumber);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByZgfa(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String solutioncode = year+"-"+montrStr+"-"+ModuleCodeDef.ZGFA+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxSolutioncode(solutioncode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(solutioncode, "");
		}else {
			no = "0";
		}
		solutioncode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",solutioncode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	//===========================================================
	@Override
	public JsonBean getAutoCodeBySjmb(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String templeteCode = year+"-"+montrStr+"-"+ModuleCodeDef.NSSSJZY+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxTempleteCode(templeteCode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(templeteCode, "");
		}else {
			no = "0";
		}
		templeteCode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",templeteCode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeBySjzy(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String templeteCode = year+"-"+montrStr+"-"+ModuleCodeDef.SJZYMBK+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxTempleteCode(templeteCode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(templeteCode, "");
		}else {
			no = "0";
		}
		templeteCode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",templeteCode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByGlzd(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String rulecode = year+"-"+montrStr+"-"+ModuleCodeDef.GLZD+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxRulecode(rulecode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(rulecode, "");
		}else {
			no = "0";
		}
		rulecode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",rulecode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByXmzl(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String projectDatapreId = year+"-"+montrStr+"-"+ModuleCodeDef.XMZL+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxProjectDatapreId(projectDatapreId);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(projectDatapreId, "");
		}else {
			no = "0";
		}
		projectDatapreId += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",projectDatapreId);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByNewSjmb(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String mbcode = year+"-"+montrStr+"-"+ModuleCodeDef.SJMB+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxMbcodes(mbcode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(projectDatapreId, "");
		}else {
			no = "0";
		}
		mbcode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",mbcode);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getAutoCodeByNewSjjyk(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String code = year+"-"+montrStr+"-"+ModuleCodeDef.SJJYK+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjAuditplanMapper.selectMaxCode(code);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(projectDatapreId, "");
		}else {
			no = "0";
		}
		code += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",code);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
}
	
