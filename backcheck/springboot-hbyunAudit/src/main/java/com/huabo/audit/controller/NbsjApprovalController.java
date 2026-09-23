package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblNbsjAdvicenoteService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjAuditprogramService;
import com.huabo.audit.service.TblNbsjBorrowRecordService;
import com.huabo.audit.service.TblNbsjBugCriterionService;
import com.huabo.audit.service.TblNbsjCertificateService;
import com.huabo.audit.service.TblNbsjFactbookService;
import com.huabo.audit.service.TblNbsjInnerRuleService;
import com.huabo.audit.service.TblNbsjOuterruleService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.service.TblNbsjTargettypeService;
import com.huabo.audit.service.TblNbsjTempleteService;
import com.huabo.audit.service.TblNbsjTypeService;
import com.huabo.audit.service.TblOrganizaService;
import com.huabo.audit.service.TblOtherarticleService;
import com.huabo.audit.service.TblReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计工作台
 */
@RestController
@Slf4j
@Tag(name="审计流程",description="审计流程")
@RequestMapping(value ="/nbsjapproval")
public class NbsjApprovalController {
	
	
	@Resource
	public AttachmentService attachmentService;
	@Resource
	public TblNbsjTempleteService tblNbsjTempleteService;
	@Resource
	public TblNbsjTargettypeService tblNbsjTargettypeService;
	@Resource
	public TblNbsjAuditprogramService tblNbsjAuditprogramService;
	@Resource
	public TblNbsjOuterruleService tblNbsjOuterruleService;
	@Resource
	public TblNbsjInnerRuleService tblNbsjInnerRuleService;
	@Resource
	public TblNbsjBugCriterionService  tblNbsjBugCriterionService;
	@Resource
    private TblNbsjTypeService tblNbsjTypeService;
	@Resource
	public FreeMarkerConfig freeMarkerConfig;
    @Resource
    public TblOrganizaService tblOrganizaService;
    @Resource
    public TblOtherarticleService tblOtherarticleService;
	@Resource
	public TblNbsjSheetService tBlNbsjSheetService;
	@Resource
	public TblNbsjFactbookService tblNbsjFactbookService;
	@Resource
	public TblNbsjAdvicenoteService tblNbsjAdvicenoteService;
	@Resource
    public TblNbsjAuditplanService tblNbsjAuditplanService;
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	@Resource
	public TblReportService tblReportService;
	@Resource
	public TblNbsjBorrowRecordService tblNbsjBorrowRecordService;
	@Resource
	public TblNbsjCertificateService tblNbsjCertificateService;
    
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
    
    
    /**
     * 查看当前流程图
 	 */
	@OperationLog(
			success = "审计流程查看",
			busType = "审计流程",
			fail = "审计流程查看",
			operationType = OperationType.SELECT,
			subType = "智能审计——查看审计流程图"
	)
     @RequestMapping(value ="/picture", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
  	@Operation(summary = "查看流程图--审计通用")
     public void lookCurrentProcessImage(HttpServletRequest request, HttpServletResponse response,
    		 @Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) BigDecimal taskId)throws IOException {
         InputStream imageStream = tblNbsjAuditplanService.lookCurrentProcessImage(taskId);
         byte[] b = new byte[1024];
         int len;
         while ((len = imageStream.read(b, 0, 1024)) != -1) {
             response.getOutputStream().write(b, 0, len);
         }
     }

	@OperationLog(
			success = "审计流程查看",
			busType = "审计流程",
			fail = "审计流程查看",
			operationType = OperationType.SELECT,
			subType = "审计计划-查看办理流程图片页面"
	)
 	@RequestMapping(value = "auditPlan_Process", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
 	@Operation(summary = "审计计划-查看办理流程图片页面")
 	public String auditPlan_Process(HttpServletRequest request,
 			@Parameter(name = "planId", description = "审计计划主键", required = true) @RequestParam(value ="planId", required = false) BigDecimal planId,
 									  @Parameter(name = "businessKey", description = "businessKey", required = false)@RequestParam(value="businessKey",required=true)String businessKey
 									  ) throws Exception {
 		String result = null;
 		try {
 			Map<String,Object>  resultMap = this.tblNbsjAuditplanService.viewOppsiteActiviti(planId,businessKey);
 			JSONObject jsonObj = new JSONObject(resultMap);
 			result = jsonObj.toString();
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return result;
 	}
	
    @RequestMapping(value ="/submitAuditPlanApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
 	@Operation(summary = "审计计划-提交审批")
     public JsonBean submitAuditPlanApproval(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "planId", description = "审计计划主键", required = true) @RequestParam(value ="planId", required = false) BigDecimal planId) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblNbsjAuditplanService.submitAuditplanArrpoval(token,planId);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
     
     @RequestMapping(value ="/getAuditPlanApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
 	@Operation(summary = "审计计划管理-查看办理页面")
     public JsonBean getAuditPlanApprovalInfo(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "planId", description = "审计计划主键", required = true) @RequestParam(value ="planId", required = true) BigDecimal planId,
     		@Parameter(name = "taskId", description = "工作流任务主键", required = false) @RequestParam(value ="taskId", required = false) String taskId,
     		 @Parameter(name = "v", description = "v", required = false)@RequestParam(value ="v", required = false)String v,
     		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId
     		) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblNbsjAuditplanService.getAuditPlanApprovalInfo(token,planId,taskId,cyId,v);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
     
     
     @RequestMapping(value ="/dealAuditPlanApporvalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计计划管理-办理审批流程，通过或驳回")
 	public JsonBean dealAuditPlanApporvalInfo(HttpServletRequest request,HttpServletResponse response,
 			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
 			@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
 			@Parameter(name = "taskId", description = "工作流任务主键||cy.businessKey", required = true) @RequestParam(value ="taskId", required = true) String taskId,
 			@Parameter(name = "planId", description = "审计计划主键", required = true) @RequestParam(value ="planId", required = true) BigDecimal planId,
 			@Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
 			@Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc,
 			@Parameter(name = "processDefinitionId", description = "processDefinitionId", required = true)@RequestParam(name="processDefinitionId",required=true)String processDefinitionId,
			 @Parameter(name = "processInstanceId", description = "processInstanceId", required = true)@RequestParam(name="processInstanceId",required=true)String processInstanceId) {
     	
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblNbsjAuditplanService.dealAuditPlanApporvalInfo(token,cyId,taskId,transition.trim(),optDesc,planId,processDefinitionId,processInstanceId);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
 	}
     @RequestMapping(value = "Project_Process", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
  	@Operation(summary = "审计项目-查看办理流程图片页面")
  	public String Project_Process(HttpServletRequest request,
  			@Parameter(name = "projectId", description = "审计项目主键", required = true) @RequestParam(value ="projectId", required = false) BigDecimal projectId,
  									  @Parameter(name = "businessKey", description = "businessKey", required = false)@RequestParam(value="businessKey",required=true)String businessKey
  									  ) throws Exception {
  		String result = null;
  		try {
  			Map<String,Object>  resultMap = this.tblnbsjProjectService.viewOppsiteActiviti(projectId,businessKey);
  			JSONObject jsonObj = new JSONObject(resultMap);
  			result = jsonObj.toString();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return result;
  	}
     
     @RequestMapping(value ="/submitProjectApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计项目-提交审批")
     public JsonBean submitProjectApproval(HttpServletRequest request,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		 @Parameter(name = "projectId", description = "审计项目主键", required = true) @RequestParam(value ="projectId", required = false) BigDecimal projectId) {
    	 JsonBean jsonBean = null;
    	 try {
    		 jsonBean = this.tblnbsjProjectService.submitProjectArrpoval(token,projectId);
    	 } catch (Exception e) {
    		 ResponseFormat.retParam(0,1000,e.getMessage());
    	 }
    	 return jsonBean;
     }
     
     @RequestMapping(value ="/getProjectApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
     @Operation(summary = "审计项目-查看办理页面")
     public JsonBean getProjectApprovalInfo(HttpServletRequest request,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		 @Parameter(name = "projectId", description = "审计项目主键", required = true) @RequestParam(value ="projectId", required = true) BigDecimal projectId,
    		 @Parameter(name = "taskId", description = "工作流任务主键", required = false) @RequestParam(value ="taskId", required = false) String taskId,
    		 @Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
    		 @Parameter(name = "v", description = "v", required = false)@RequestParam(value ="v", required = true)String v) {
    	 JsonBean jsonBean = null;
    	 try {
    		 jsonBean = this.tblnbsjProjectService.getProjectApprovalInfo(token,projectId,taskId,cyId,v);
    	 } catch (Exception e) {
    		 ResponseFormat.retParam(0,1000,e.getMessage());
    	 }
    	 return jsonBean;
     }
     
     
     @RequestMapping(value ="/dealProjectApporvalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计项目-办理审批流程，通过或驳回")
     public JsonBean dealProjectApporvalInfo(HttpServletRequest request,HttpServletResponse response,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		 @Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
    		 @Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
    		 @Parameter(name = "projectId", description = "审计项目主键", required = true) @RequestParam(value ="projectId", required = true) String projectId,
    		 @Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
    		 @Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc,
 			 @Parameter(name = "processDefinitionId", description = "processDefinitionId", required = true)@RequestParam(name="processDefinitionId",required=true)String processDefinitionId,
 			 @Parameter(name = "processInstanceId", description = "processInstanceId", required = true)@RequestParam(name="processInstanceId",required=true)String processInstanceId) {
    	 JsonBean jsonBean = null;
    	 try {
    		 jsonBean = this.tblnbsjProjectService.dealProjectApporvalInfo(token,cyId,taskId,transition.trim(),optDesc,projectId, processDefinitionId,processInstanceId);
    	 } catch (Exception e) {
    		 ResponseFormat.retParam(0,1000,e.getMessage());
    	 }
    	 return jsonBean;
     }
     @RequestMapping(value ="/submitTblAdvicenoteArrpoval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
  	@Operation(summary = "审计通知书-提交审批")
      public JsonBean submitTblAdvicenoteArrpoval(HttpServletRequest request,
      		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
      		@Parameter(name = "adviceid", description = "审计通知书id", required = true) @RequestParam(value ="adviceid", required = false) BigDecimal adviceid) {
      	JsonBean jsonBean = null;
  		try {
  			jsonBean = this.tblNbsjAdvicenoteService.submitTblAdvicenoteArrpoval(token,adviceid);
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return jsonBean;
      }
     @RequestMapping(value ="/getTblAdvicenoteApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
 	@Operation(summary = "审计通知书-查看办理页面")
     public JsonBean getTblAdvicenoteApprovalInfo(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "adviceid", description = "审计通知书id", required = true) @RequestParam(value ="adviceid", required = true) BigDecimal adviceid,
     		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
     		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblNbsjAdvicenoteService.getTblAdvicenoteApprovalInfo(token,adviceid,taskId,cyId);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
    @RequestMapping(value ="/dealTblAdvicenoteApporval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计通知书-办理审批流程，通过或驳回")
	public JsonBean dealTblAdvicenoteApporval(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
			@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
			@Parameter(name = "adviceid", description = "审计通知书id", required = true) @RequestParam(value ="adviceid", required = true) String adviceid,
			@Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
			@Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc) {
    	
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjAdvicenoteService.dealTblAdvicenoteApporval(token,cyId,taskId,transition.trim(),optDesc,adviceid);
		} catch (Exception e) {
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
	}

    @RequestMapping(value ="/submitTblNbsjSheetArrpoval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计底稿-提交审批")
    public JsonBean submitTblNbsjSheetArrpoval(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "sheetid", description = "审计底稿id", required = true) @RequestParam(value ="sheetid", required = false) BigDecimal sheetid,
			@Parameter(name = "examination", description = "审批意见", required = true)@RequestParam(value = "examination",required = false)String examination) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tBlNbsjSheetService.submitArrpoval(token,sheetid,examination);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    @RequestMapping(value ="/getTblNbsjSheetApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计底稿-查看办理页面")
    public JsonBean getTblNbsjSheetApprovalInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "sheetid", description = "审计底稿id", required = true) @RequestParam(value ="sheetid", required = true) BigDecimal sheetid,
    		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tBlNbsjSheetService.getApprovalInfo(token,sheetid,cyId);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    @RequestMapping(value ="/dealTblNbsjSheetApporval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计底稿-办理审批流程，通过或驳回")
    public JsonBean dealTblNbsjSheetApporval(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
    		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
    		@Parameter(name = "sheetid", description = "审计底稿id", required = true) @RequestParam(value ="sheetid", required = true) String sheetid,
    		@Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
    		@Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc,
            @Parameter(name = "processDefinitionId", description = "定义名称", required = true) @RequestParam(value = "processDefinitionId" , required = true)String processDefinitionId,
            @Parameter(name = "processInstanceId", description = "流程监控ID", required = true) @RequestParam (value = "processInstanceId" , required = true)String processInstanceId) {
    	
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tBlNbsjSheetService.dealApporval(token,cyId,taskId,transition.trim(),optDesc,sheetid,processDefinitionId,processInstanceId);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    @RequestMapping(value ="/submitTblNbsjFactbookArrpoval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "事实确认书-提交审批")
    public JsonBean submitTblNbsjFactbookArrpoval(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "factid", description = "事实确认书id", required = true) @RequestParam(value ="factid", required = false) BigDecimal factid) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tblNbsjFactbookService.submitArrpoval(token,factid);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    @RequestMapping(value ="/getTblNbsjFactbookApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "事实确认书-查看办理页面")
    public JsonBean getTblNbsjFactbookApprovalInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "factid", description = "事实确认书id", required = true) @RequestParam(value ="factid", required = true) BigDecimal factid,
    		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
    		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tblNbsjFactbookService.getApprovalInfo(token,factid,taskId,cyId);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    @RequestMapping(value ="/dealTblNbsjFactbookApporval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "事实确认书-办理审批流程，通过或驳回")
    public JsonBean dealTblNbsjFactbookApporval(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
    		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
    		@Parameter(name = "factid", description = "事实确认书id", required = true) @RequestParam(value ="factid", required = true) String factid,
    		@Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
    		@Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc) {
    	
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tblNbsjFactbookService.dealApporval(token,cyId,taskId,transition.trim(),optDesc,factid);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    
    @RequestMapping(value ="/submitReportZqyjApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
 	@Operation(summary = "审计报告-征求意见")
     public JsonBean submitReportZqyjApproval(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "reportid", description = "审计报告主键", required = true) @RequestParam(value ="reportid", required = false) BigDecimal reportid) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblReportService.submitReportZqyjApproval(token,reportid);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
    @RequestMapping(value ="/submitReportFhApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计报告-提交复核")
    public JsonBean submitReportFhApproval(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "reportid", description = "审计报告主键", required = true) @RequestParam(value ="reportid", required = false) BigDecimal reportid) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tblReportService.submitReportFhApproval(token,reportid);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
    @RequestMapping(value ="/submitReportSpApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计报告-提交审批")
    public JsonBean submitReportSpApproval(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "reportid", description = "审计报告主键", required = true) @RequestParam(value ="reportid", required = false) BigDecimal reportid) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = this.tblReportService.submitReportSpApproval(token,reportid);
    	} catch (Exception e) {
    		ResponseFormat.retParam(0,1000,e.getMessage());
    	}
    	return jsonBean;
    }
     
     @RequestMapping(value ="/getReportFhApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
 	@Operation(summary = "审计报告-查看办理页面")
     public JsonBean getReportFhApprovalInfo(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "reportid", description = "审计报告主键", required = true) @RequestParam(value ="reportid", required = true) BigDecimal reportid,
     		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
     		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblReportService.getReportFhApprovalInfo(token,reportid,taskId,cyId);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
     
     
     @RequestMapping(value ="/dealReportFhApporvalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计报告-办理审批流程，通过或驳回")
 	public JsonBean dealReportFhApporvalInfo(HttpServletRequest request,HttpServletResponse response,
 			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
 			@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
 			@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
 			@Parameter(name = "reportid", description = "审计报告主键", required = true) @RequestParam(value ="reportid", required = true) String reportid,
 			@Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
 			@Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc) {
     	
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblReportService.dealReportFhApporvalInfo(token,cyId,taskId,transition.trim(),optDesc,reportid);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
 	}
     @RequestMapping(value ="/submitRecordApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计档案借阅-提交审批")
     public JsonBean submitRecordApproval(HttpServletRequest request,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		 @Parameter(name = "borrowid", description = "主键", required = true) @RequestParam(value ="borrowid", required = false) BigDecimal borrowid) {
    	 JsonBean jsonBean = null;
    	 try {
    		 jsonBean = this.tblNbsjBorrowRecordService.submitRecordApproval(token,borrowid);
    	 } catch (Exception e) {
    		 ResponseFormat.retParam(0,1000,e.getMessage());
    	 }
    	 return jsonBean;
     }
     
     @RequestMapping(value ="/getRecordApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
     @Operation(summary = "审计档案借阅-查看办理页面")
     public JsonBean getRecordApprovalInfo(HttpServletRequest request,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		 @Parameter(name = "borrowid", description = "主键", required = true) @RequestParam(value ="borrowid", required = true) BigDecimal borrowid,
    		 @Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
    		 @Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
    	 JsonBean jsonBean = null;
    	 try {
    		 jsonBean = this.tblNbsjBorrowRecordService.getRecordApprovalInfo(token,borrowid,taskId,cyId);
    	 } catch (Exception e) {
    		 ResponseFormat.retParam(0,1000,e.getMessage());
    	 }
    	 return jsonBean;
     }
     
     
     @RequestMapping(value ="/dealRecordApporvalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计档案借阅-办理审批流程，通过或驳回")
     public JsonBean dealRecordApporvalInfo(HttpServletRequest request,HttpServletResponse response,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		 @Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
    		 @Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
    		 @Parameter(name = "borrowid", description = "主键", required = true) @RequestParam(value ="borrowid", required = true) String borrowid,
    		 @Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
    		 @Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc) {
    	 
    	 JsonBean jsonBean = null;
    	 try {
    		 jsonBean = this.tblNbsjBorrowRecordService.dealRecordApporvalInfo(token,cyId,taskId,transition.trim(),optDesc,borrowid);
    	 } catch (Exception e) {
    		 ResponseFormat.retParam(0,1000,e.getMessage());
    	 }
    	 return jsonBean;
     }
     
     
     
     @RequestMapping(value ="/submitAuditUserApproval", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计人员审核-提交审批")
     public JsonBean submitAuditUserApproval(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "staffid", description = "人员主键", required = true) @RequestParam(value ="staffid", required = false) BigDecimal staffid) {
     	JsonBean jsonBean = null;
     	try {
     		jsonBean = this.tblReportService.submitAuditUserApproval(token,staffid);
     	} catch (Exception e) {
     		ResponseFormat.retParam(0,1000,e.getMessage());
     	}
     	return jsonBean;
     }
     
     @RequestMapping(value ="/dealAuditUserApporvalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
     @Operation(summary = "审计人员审核-办理审批流程，通过或退回")
 	public JsonBean dealAuditUserApporvalInfo(HttpServletRequest request,HttpServletResponse response,
 			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
 			@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
 			@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
 			@Parameter(name = "staffid", description = "人员主键", required = true) @RequestParam(value ="staffid", required = true) String staffid,
 			@Parameter(name = "transitionName", description = "按钮名称 通过/退回", required = true) @RequestParam(value ="transitionName", required = true) String transitionName,
 			@Parameter(name = "examination", description = "审批意见", required = true) @RequestParam(value ="examination", required = true) String examination,
 			@Parameter(name = "processInstanceId", description = "processInstanceId", required = true) @RequestParam(value ="processInstanceId", required = true) String processInstanceId,
 	  		@Parameter(name = "processDefinitionId", description = "processDefinitionId", required = true) @RequestParam(value ="processDefinitionId", required = true) String processDefinitionId) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblReportService.dealAuditUserApporvalInfo(token,staffid,taskId,cyId,transitionName,examination,processInstanceId,processDefinitionId);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
 	}
     
     @RequestMapping(value ="/getAuditUserApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
 	@Operation(summary = "审计人员审核-查看办理页面")
     public JsonBean getAuditUserApprovalInfo(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "staffid", description = "人员主键", required = true) @RequestParam(value ="staffid", required = true) BigDecimal staffid,
     		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) throws Exception {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblReportService.getAuditUserApprovalInfo(token,staffid,cyId);
 		} catch (Exception e) {
 			e.printStackTrace();
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
     
     @RequestMapping(value ="/getAuditUserApprovalView", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
		@Operation(summary = "审计人员审核-查看办理流程图片页面")
   public JsonBean getAuditUserApprovalView(HttpServletRequest request,
   		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "staffid", description = "人员主键", required = true) @RequestParam(value ="staffid", required = true) BigDecimal staffid,
   		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId) {
   	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblReportService.getAuditUserApprovalView(token,staffid,taskId);
		} catch (Exception e) {
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
   }
     
     
     
     @RequestMapping(value ="/getStaffScoreApprovalInfo", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
 	@Operation(summary = "评价管理-查看办理页面")
     public JsonBean getStaffScoreApprovalInfo(HttpServletRequest request,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "staffScoreid", description = "评价主键", required = true) @RequestParam(value ="staffScoreid", required = true) BigDecimal staffScoreid,
     		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
     	JsonBean jsonBean = null;
 		try {
 			jsonBean = this.tblReportService.getStaffScoreApprovalInfo(token,staffScoreid,cyId);
 		} catch (Exception e) {
 			ResponseFormat.retParam(0,1000,e.getMessage());
 		}
 		return jsonBean;
     }
     
     @RequestMapping(value ="/getStaffScoreApprovalView", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
  		@Operation(summary = "评价管理-查看办理审批查看流程图片页面")
      public JsonBean getStaffScoreApprovalView(HttpServletRequest request,
      		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
      		@Parameter(name = "staffScoreid", description = "评价主键", required = true) @RequestParam(value ="staffScoreid", required = true) BigDecimal staffScoreid,
      		@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId) {
      	JsonBean jsonBean = null;
  		try {
  			jsonBean = this.tblReportService.getStaffScoreApprovalView(token,staffScoreid,taskId);
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return jsonBean;
      }
     
	@RequestMapping(value = "/submitStaffScoreApproval",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "评价管理-提交审批")
	public @ResponseBody String submitStaffScoreApproval(HttpServletRequest request,
												@Parameter(name = "staffScoreid", description = "评价主键", required = false)BigDecimal staffScoreid,
												@Parameter(name = "examination", description = "审批意见", required = false)String examination,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){

		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblReportService.submitStaffScoreApproval(staffScoreid,examination,token);//this.tblCyhwUnitService.htglTjsp(staffScoreid,examination,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
	@Operation(summary = "评价管理-通过退回办理方法")
	@PostMapping(value = "/dealStaffScoreApprovalInfo",produces = "application/json; charset=utf-8")
	public @ResponseBody JsonBean dealStaffScoreApprovalInfo(HttpServletRequest request,
			@Parameter(name = "staffScoreid", description = "评价主键", required = true)@RequestParam(value = "staffScoreid",required = true)BigDecimal staffScoreid,
		    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		    @Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
     		@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
     		@Parameter(name = "transitionName", description = "按钮名称 通过/退回", required = true) @RequestParam(value ="transitionName", required = true) String transitionName,
     		@Parameter(name = "examination", description = "审批意见", required = true) @RequestParam(value ="examination", required = true) String examination,
     		@Parameter(name = "processInstanceId", description = "processInstanceId", required = true) @RequestParam(value ="processInstanceId", required = true) String processInstanceId,
     		@Parameter(name = "processDefinitionId", description = "processDefinitionId", required = true) @RequestParam(value ="processDefinitionId", required = true) String processDefinitionId
		){
		
		JsonBean jsonBean = null;
  		try {
  			jsonBean = this.tblReportService.dealStaffScoreApprovalInfo(token,staffScoreid,taskId,cyId,transitionName,examination,processInstanceId,processDefinitionId);
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return jsonBean;
	}
	@RequestMapping(value ="/certificate_submit", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计取证单-提交审批")
	public JsonBean certificate_submit(HttpServletRequest request,
											   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
											   @Parameter(name = "certificateId", description = "审计取证单id", required = true) @RequestParam(value ="certificateId", required = false) BigDecimal certificateId,
											   @Parameter(name = "examination", description = "审批意见", required = true)@RequestParam(value = "examination",required = false)String examination) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjCertificateService.certificateSubmit(token,certificateId,examination);
		} catch (Exception e) {
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
	}

	@RequestMapping(value ="/getTblNbsjCertificate", produces ="application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "审计取证单-查看办理页面")
	public JsonBean getCertificate(HttpServletRequest request,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
								  				@Parameter(name = "certificateId", description = "审计取证单id", required = true) @RequestParam(value ="certificateId", required = false) BigDecimal certificateId,
												@Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
												@Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjCertificateService.getCertificate(token,certificateId,taskId,cyId);
		} catch (Exception e) {
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
	}
	@RequestMapping(value ="/dealTblNbsjCertificate", produces ="application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计取证单-办理审批流程，通过或驳回")
	public JsonBean dealTblNbsjCertificate(HttpServletRequest request,HttpServletResponse response,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
											 @Parameter(name = "cyId", description = "审批记录主键", required = true) @RequestParam(value ="cyId", required = true) BigDecimal cyId,
											 @Parameter(name = "taskId", description = "工作流任务主键", required = true) @RequestParam(value ="taskId", required = true) String taskId,
										     @Parameter(name = "certificateId", description = "审计取证单id", required = true) @RequestParam(value ="certificateId", required = false) BigDecimal certificateId,
											 @Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改", required = true) @RequestParam(value ="transition", required = true) String transition,
											 @Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value ="optDesc", required = true) String optDesc,
											 @Parameter(name = "processDefinitionId", description = "定义名称", required = true) @RequestParam(value = "processDefinitionId" , required = true)String processDefinitionId,
											 @Parameter(name = "processInstanceId", description = "流程监控ID", required = true) @RequestParam (value = "processInstanceId" , required = true)String processInstanceId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjCertificateService.dealCertificate(token,cyId,taskId,transition.trim(),optDesc,certificateId,processDefinitionId,processInstanceId);
		} catch (Exception e) {
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
	}
}
