package com.huabo.audit.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAduitProGramService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblNbsjAdvicenoteService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjFactbookService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjProjectDataProService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.util.FileUtil;
import com.huabo.audit.util.FreeMarkerUtil;
import com.huabo.audit.util.R;
import com.huabo.audit.util.SnowflakeIdWorker;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计准备阶段
 */
@RestController
@Slf4j
@Tag(name="审计准备阶段",description="审计准备阶段")
@RequestMapping(value = "/auditReady")
public class NbsjReadyController {
	
	@Resource
	public TblAduitProGramService tblAduitProGramService;
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
	public TblNbsjProjectDataProService dataProService;
	
	@Resource
	public AttachmentService attachmentService;
	
	@Resource
	public ActivityPluginsService activityPluginsService;
	
	@Resource
	public TblNbsjAdvicenoteService tblNbsjAdvicenoteService;
	
	@Resource
	public ProcessService processService;
	
	@Resource
	public TblNbsjSheetService tBlNbsjSheetService;
	
	@Resource
	public TblNbsjFactbookService tblNbsjFactbookService;
	
	@Resource
    public TblNbsjAuditplanService tblNbsjAuditplanService;
	
	@Resource
	public TblNbsjOperateService tblNbsjOperateService;
	
	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Resource
	public FreeMarkerConfig freeMarkerConfig;
	
	@Resource
    private UserProvider userProvider;
	
	  private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	
	/**
     * 审计指引-左侧树菜单
     */
	@OperationLog(
			success = "左侧树",
			busType = "智能审计",
			fail = "左侧树",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计指引-左侧树菜单"
	)
	@GetMapping("/sjgj/getTreeZy")
	@Operation(summary = "审计指引-左侧树菜单")
	public JsonBean getTreeZy(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="nodeId",description="nodeId",required=false) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
			@Parameter(name="tempId",description="tempId",required=false) @RequestParam(value = "tempId", required = false) BigDecimal tempId,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId,
			@Parameter(name = "type", description = "mb,fp(审计指引传mb)", required = false) @RequestParam(value = "type", required = false) String type) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjOperateService.getTreeZy(token, tempId, nodeId, type,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 审计指引列表
	 */
	@OperationLog(
			success = "审计指引列表",
			busType = "智能审计",
			fail = "审计指引列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计指引——获取审计指引列表"
	)
	@GetMapping("/sjyj/def_cat_list_zy")
	@Operation(summary = "审计指引列表")
	public JsonBean def_cat_list_zy(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="tempId",description="tempId",required=false) @RequestParam(value = "tempId", required = false) BigDecimal tempId,
			@Parameter(name="targetId",description="targetId",required=false) @RequestParam(value = "targetId", required = false) BigDecimal targetId,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblAduitProGramService.defZyPageList(token, pageNumber, pageSize,tempId,targetId,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "审计计划流程页面",
			busType = "智能审计",
			fail = "审计计划流程页面",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计计划管理—办理审计计划流程页面数据获取页面"
	)
    @RequestMapping(value = "/getAuditPlanApprovalInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "审计计划管理-办理审计计划流程页面数据获取页面")
    public JsonBean getAuditPlanApprovalInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planId", description = "审计计划主键 ", required = true) @RequestParam(value = "planId", required = true) BigDecimal planId,
    		@Parameter(name = "taskId", description = "工作流任务主键 ", required = false) @RequestParam(value = "taskId", required = false) String taskId,
    		@Parameter(name = "cyId", description = "审批记录主键 ", required = true) @RequestParam(value = "cyId", required = true) BigDecimal cyId,
    		@Parameter(name = "v", description = "v", required = false)@RequestParam(value ="v", required = false)String v) {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjAuditplanService.getAuditPlanApprovalInfo(token,planId,taskId,cyId,v);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }

	@OperationLog(
			success = "审计计划流程审核页面",
			busType = "智能审计",
			fail = "审计计划流程审核页面",
			operationType = OperationType.APPROVE,
			subType = "基础配置——审计计划管理-办理审批流程，通过或驳回"
	)
    @RequestMapping(value = "/dealAuditPlanApporvalInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理-办理审批流程，通过或驳回")
	public JsonBean dealAuditPlanApporvalInfo(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "cyId", description = "审批记录主键 ", required = true) @RequestParam(value = "cyId", required = true) BigDecimal cyId,
			@Parameter(name = "taskId", description = "工作流任务主键 ", required = true) @RequestParam(value = "taskId", required = true) String taskId,
			@Parameter(name = "planId", description = "升级计划主键 ", required = true) @RequestParam(value = "planId", required = true) BigDecimal planId,
			@Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改 ", required = true) @RequestParam(value = "transition", required = true) String transition,
			@Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value = "optDesc", required = true) String optDesc,
			@Parameter(name = "processDefinitionId", description = "processDefinitionId", required = true)@RequestParam(name="processDefinitionId",required=true)String processDefinitionId,
			 @Parameter(name = "processInstanceId", description = "processInstanceId", required = true)@RequestParam(name="processInstanceId",required=true)String processInstanceId) {
    	
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjAuditplanService.dealAuditPlanApporvalInfo(token,cyId,taskId,transition,optDesc,planId,processDefinitionId,processInstanceId);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
    
	/**
	 * 审计指引-新增与修改
	 */
	@OperationLog(
			success = "审计指引-新增与修改",
			busType = "智能审计",
			fail = "审计指引-新增与修改",
			operationType = OperationType.ADD,
			subType = "基础配置——审计指引列表"
	)
	@RequestMapping(value = "/sjyj/def_cat_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计指引-新增与修改")
    public JsonBean def_cat_add(HttpServletRequest request,@Parameter(name = "apg", description = "实体", required = true)TblAduitProGramEntity apg,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAduitProGramService.defCatAdd(apg,token);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 审计指引-删除
	 */
	@OperationLog(
			success = "审计指引删除",
			busType = "智能审计",
			fail = "审计指引删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计指引-删除"
	)
	@GetMapping("/sjyj/def_cat_del")
	@Operation(summary = "审计指引-删除")
    public JsonBean def_cat_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "programId", description = "主键", required = true)@RequestParam(value = "programId", required = true) BigDecimal programId) {
        
        try {
			return tblAduitProGramService.defCatDel(programId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	/**
     * 审计指引 详情
     */
	@OperationLog(
			success = "审计指引详情",
			busType = "智能审计",
			fail = "审计指引详情",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计指引-详情"
	)
    @GetMapping("/sjss/def_cat_detail_zy")
    @Operation(summary = "审计指引--详情")
    public JsonBean def_cat_detail_zy(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "programid", description = "主键", required = true)@RequestParam(value = "programid", required = true) BigDecimal programid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblAduitProGramService.findDefZyDetail(token,programid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    /**
     * 审计指引 左侧树
     */
	@OperationLog(
			success = "审计指引左侧树",
			busType = "智能审计",
			fail = "审计指引左侧树",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计指引-审计指引左侧树"
	)
    @GetMapping("/sjyj/def_left_tree_zy")
    @Operation(summary = "审计指引左侧树")
	public JsonBean def_left_tree_zy(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblAduitProGramService.defZyLeftTreeList(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    
    /**
     * 审计通知书列表
     */
	@OperationLog(
			success = "审计通知书列表",
			busType = "智能审计",
			fail = "审计通知书列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施——审计通知列表"
	)
    @GetMapping("/sjzb/notice_list")
    @Operation(summary = "审计通知书列表")
	public JsonBean notice_list(HttpServletRequest request,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjAdvicenoteService.noticePageList(token, pageNumber, pageSize,tblNbsjAdvicenoteVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	@OperationLog(
			success = "审计通知——下发",
			busType = "智能审计",
			fail = "审计通知——下发",
			operationType = OperationType.DISPATCH,
			subType = "基础配置——审计实施—审计通知-下发"
	)
    @GetMapping("/sjzb/xftzs")
    @Operation(summary = "审计通知书-下发")
    public JsonBean xftzs(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "userids", description = "下发人员ids", required = true) @RequestParam("userids") String userids,
    		@Parameter(name = "usernames", description = "下发人员名称", required = true) @RequestParam("usernames") String usernames,
    		@Parameter(name = "ids", description = "通知书ids", required = true) @RequestParam("ids") String ids) throws Exception {
    	JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjAdvicenoteService.xfry(ids, userids, usernames, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }
    
    
    
	/**
     * 审计通知书 详情
     */
	@OperationLog(
			success = "审计通知——详情",
			busType = "智能审计",
			fail = "审计通知——详情",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施—审计通知-详情内容"
	)
    @GetMapping("/sjzb/notice_disp")
    @Operation(summary = "审计指引--详情")
    public JsonBean notice_disp(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "adviceid", description = "主键", required = true)@RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjAdvicenoteService.findNoticeDetail(token,adviceid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    /**
	 * 审计通知书-新增与修改
	 */
	@OperationLog(
			success = "审计通知——新增",
			busType = "智能审计",
			fail = "审计通知——新增",
			operationType = OperationType.ADD,
			subType = "基础配置——获取审计实施—审计通知-新增"
	)
	@RequestMapping(value = "/sjzb/notice_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计通知书-新增与修改")
    public JsonBean nkgj_projectdp_add(HttpServletRequest request,@Parameter(name = "notice", description = "实体", required = true)TblNbsjAdvicenoteEntity notice,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjAdvicenoteService.noticeAdd(notice,token,attids);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
    /**
     * 审计通知书-导出
     * @param request
     * @param response
     * @param id
     * @throws Exception
     */
	@OperationLog(
			success = "审计通知书——导出",
			busType = "智能审计",
			fail = "审计通知——导出",
			operationType = OperationType.EXPORT,
			subType = "基础配置——审计通知书-导出"
	)
    @RequestMapping(value = "/expOuterRuleFile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
   	@Operation(summary = "审计通知书-导出")
	public void expOuterRuleFile(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "adviceid", description = "主键", required = true) @RequestHeader("adviceid")String adviceid)throws Exception{
		Map<String, String> map = new HashMap<String,String>();
    	TblNbsjAdvicenoteEntity info = tblNbsjAdvicenoteService.findById(adviceid);
		map.put("repdesc", info.getContent());
		String fileName =info.getAdvicename()+".doc";
		String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
		Boolean flag =(Boolean)FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
		if(!flag){//如何静态文件不存在，重新生成
			FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
		}
		String fileName1 = FREEMARKER_PATH +"/"+ fileName;
		FileUtil.downLoad(fileName1, response, false, fileName);
		FileUtil.deleteFile(fileName);
	}
	/**
	 * 审计通知书-附件列表
	 */
	@OperationLog(
			success = "审计通知附件",
			busType = "智能审计",
			fail = "审计通知附件",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施——审计通知——附件列表"
	)
    @GetMapping("/sjzb/notice_file_list")
	@Operation(summary = "工作日志-附件列表")
	public JsonBean notice_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "adviceid", description = "业务主键", required = true) @RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.noticeFileList(token,adviceid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 审计通知书-附件删除
     */
	@OperationLog(
			success = "审计通知附件删除",
			busType = "智能审计",
			fail = "审计通知附件删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计实施——审计通知——附件删除"
	)
    @GetMapping("/sjzb/notice_file_del")
    @Operation(summary = "审计通知书-附件删除")
    public R notice_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") BigDecimal attId) throws Exception {
    	return this.tblNbsjAdvicenoteService.removeAttInfoByAttId(token, attId);
    }
	
	/**
	 * 审计通知书-删除
	 */
	@OperationLog(
			success = "审计通知删除",
			busType = "智能审计",
			fail = "审计通知删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计实施——删除审计通知"
	)
	@GetMapping("/sjzb/notice_del")
	@Operation(summary = "审计通知书-删除")
    public JsonBean notice_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "adviceid", description = "主键", required = true)@RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {
        
        try {
			return tblNbsjAdvicenoteService.noticeDelete(adviceid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	/**
	 * 审计通知书-作废
	 */
	@OperationLog(
			success = "审计通知作废",
			busType = "智能审计",
			fail = "审计通知作废",
			operationType = OperationType.UPDATE,
			subType = "基础配置——审计实施——作废审计通知"
	)
	@GetMapping("/sjzb/notice_cancel")
	@Operation(summary = "审计通知书-作废")
    public JsonBean notice_cancel(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "adviceid", description = "主键", required = true)@RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {
        
        try {
			return tblNbsjAdvicenoteService.noticeCancel(adviceid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
 
 
    /**
     * 项目资料准备
     * /sjzl/data_list
     */
	@OperationLog(
			success = "项目资料列表页",
			busType = "智能审计",
			fail = "项目资料列表页",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施——项目资料列表"
	)
	@GetMapping("/sjzl/dataproject_list")
	@Operation(summary = "项目资料准备列表")
	public JsonBean dataproject_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,DataProVo dataProVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "projectId", description = "projectId", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId,
			@Parameter(name = "orgid", description = "orgid", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = dataProService.dataproPageList(token, pageNumber, pageSize,dataProVo,orgid,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
     * 项目资料准备 预览
     */
	@OperationLog(
			success = "项目资料列详情",
			busType = "智能审计",
			fail = "项目资料列详情",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施——项目资料详情内容"
	)
    @GetMapping("/sjzl/dataproject_view")
    @Operation(summary = "项目资料准备--预览")
    public JsonBean dataproject_view(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "dataId", description = "主键", required = true)@RequestParam(value = "dataId", required = true) BigDecimal dataId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = dataProService.findDataProDetail(token,dataId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    /**
	 * 项目资料准备-新增与修改
	 */
	@OperationLog(
			success = "项目资料列新增",
			busType = "智能审计",
			fail = "项目资料列新增",
			operationType = OperationType.ADD,
			subType = "基础配置——审计实施——项目资料"
	)
	@RequestMapping(value = "/sjzl/dataproject_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "项目资料准备-新增与修改")
    public JsonBean dataproject_save(HttpServletRequest request,TblNbsjProjectDataEntity pd,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "mbids", description = "模板ID ", required = false)@RequestParam(value = "mbids", required = false)  String mbids,
			 @Parameter(name = "jykids", description = "审计经验库id ", required = false)@RequestParam(value = "jykids", required = false)  String jykids,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.dataProService.dataproAdd(pd,token,attids,mbids,jykids);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 项目资料准备-附件列表
	 */
	@OperationLog(
			success = "项目资料附件",
			busType = "智能审计",
			fail = "项目资料附件",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施——项目资料附件列表"
	)
    @GetMapping("/sjzl/dataproject_file_list")
	@Operation(summary = "项目资料准备-附件列表")
	public JsonBean dataproject_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "dataId", description = "业务主键", required = true) @RequestParam(value = "dataId", required = true) BigDecimal dataId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.dataprojectFileList(token,dataId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    /**
     * 项目资料准备-附件下载
     */
	@OperationLog(
			success = "项目资料附件下载",
			busType = "智能审计",
			fail = "项目资料附件下载",
			operationType = OperationType.DOWNLOAD,
			subType = "基础配置——审计实施——项目资料附件列表zip附件下载"
	)
     @RequestMapping(value = "/download_zipfile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
     @Operation(summary = "zip附件下载接口")
     public void download_zipfile(HttpServletRequest request, HttpServletResponse response,
     		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
     		@Parameter(name = "attIds", description = "附件主键ID", required = true) @RequestParam("attIds") String attIds) throws Exception {
     	TblStaffUtil loginStaff = userProvider.get();
 		if(loginStaff == null) {
 			 System.out.println(R.fail("用户已失效！"));
 		}
 		List<com.hbfk.entity.TblAttachment> atts=new ArrayList<com.hbfk.entity.TblAttachment>();
 	   for(String s:attIds.split(",") ){
      	 TblAttachment tblAttachmentEntity = tblAttachmentService.selectEntityById(new BigDecimal(s));
           com.hbfk.entity.TblAttachment tblAttachment = new com.hbfk.entity.TblAttachment();
           tblAttachment.setAttname(tblAttachmentEntity.getAttname());
           tblAttachment.setAttpath(tblAttachmentEntity.getAttpath());
           tblAttachment.setFileName(tblAttachmentEntity.getAttname());
           tblAttachment.setAttsize(tblAttachmentEntity.getAttsize());
           atts.add(tblAttachment);
      }
 	   FtpUtil.downloadFilesAsZip(atts,response);
     }
     
     
     
    /**
     * 项目资料准备-附件删除
     */
	@OperationLog(
			success = "项目资料附件删除",
			busType = "智能审计",
			fail = "项目资料附件删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计实施——项目资料附件列表附件删除"
	)
    @GetMapping("/sjzl/dataproject_file_del")
    @Operation(summary = "项目资料准备-附件删除")
    public R report_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.dataProService.removeAttInfoByAttId(token, attId);
    }
    
    
    /**
     * 项目资料准备-下发附件给项目人员
     */
	@OperationLog(
			success = "项目资料下发",
			busType = "智能审计",
			fail = "项目资料下发",
			operationType = OperationType.DISPATCH,
			subType = "基础配置——审计实施——项目资料下发附件给项目人员"
	)
    @GetMapping("/sjzl/xfry")
    @Operation(summary = "项目资料准备-下发附件给项目人员")
    public JsonBean xfry(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "staffids", description = "项目组人员ID字符串", required = true) @RequestParam("staffids") String staffids,
    		@Parameter(name = "attids", description = "附件主键ID字符串", required = true) @RequestParam("attids") String attids) throws Exception {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.xfry(token, staffids, attids);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"下发失败",null);
		}
		return jsonBean;
    }
    
	
	/**
	 * 项目资料准备-删除
	 */
	@OperationLog(
			success = "项目资料删除",
			busType = "智能审计",
			fail = "项目资料删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——审计实施——项目资料删除"
	)
	@GetMapping("/sjzl/dataproject_del")
	@Operation(summary = "项目资料准备-删除")
    public JsonBean dataproject_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "dataId", description = "主键", required = true)@RequestParam(value = "dataId", required = true) BigDecimal dataId) {
        
        try {
			return dataProService.dataproDelete(dataId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

	@OperationLog(
			success = "项目资料附件上传",
			busType = "智能审计",
			fail = "项目资料附件上传",
			operationType = OperationType.UPLOAD,
			subType = "基础配置——审计实施——项目资料附件上传"
	)
	 @PostMapping("/sjzl/dataproject_file_upload")
	    @Operation(summary = "项目资料准备-附件列表--附件上传")
	    public R fileUpload(HttpServletRequest request, 
	    		@Parameter(name = "file", description = "附件上传entity", required = true)MultipartFile[] file, 
	    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	    		@Parameter(name = "dataId", description = "业务主键", required = true) @RequestParam(value = "dataId", required = true) BigDecimal dataId) throws Exception {
	        String attPath = "";
	        TblAttachment tblAttachmentEntity = new TblAttachment();
	        TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return R.fail("用户已失效！");
			}
			if(dataId == null) {
				return R.fail("项目资料不存在！");
			}
	        for (MultipartFile multipartFile : file) {
	            try {
	                InputStream inputStream = multipartFile.getInputStream();
	                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
	                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
	                String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());
	                attPath = FtpUtil.uploadFilePath(imageName + name, inputStream);
	                if (StrUtil.isEmpty(attPath)) {
	                    return R.fail("文件上传失败");
	                }
	                //tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
	                tblAttachmentEntity.setAttpath(imageName+name);
	                tblAttachmentEntity.setAttsize(multipartFile.getSize() / 1024);
	                tblAttachmentEntity.setUploadtime(new Date());
	                tblAttachmentEntity.setUploader(loginStaff.getRealname());
	                tblAttachmentEntity.setAttname(fileName);
	                tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
                    tblAttachmentService.save(tblAttachmentEntity);
//	                tblAttachmentService.saveEntity(tblAttachmentEntity);
	                //上传附件之后 保存到项目资料中间表
	    			this.dataProService.dataproAddFile(dataId,tblAttachmentEntity.getAttid());

	            } catch (Exception e) {
	                e.printStackTrace();
	                return R.fail("上传失败");
	            }
	        }
	        //返回当前添加的文件 前端回显
	        return R.success(tblAttachmentEntity);
	    }


	@OperationLog(
			success = "同步项目附件上传",
			busType = "智能审计",
			fail = "同步项目附件上传",
			operationType = OperationType.UPLOAD,
			subType = "基础配置——审计实施——项目资料-附件列表-同步项目附件上传"
	)
	 @PostMapping("/sjzl/tbxmfj")
	 @Operation(summary = "项目资料准备-同步项目附件上传")
	 public JsonBean tbxmfj(HttpServletRequest request, 
	    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	    		@Parameter(name = "dataId", description = "业务主键", required = true) @RequestParam(value = "dataId", required = true) BigDecimal dataId) throws Exception {
		 JsonBean jsonBean = null;
		 try {
			jsonBean= dataProService.savexmfj(token,dataId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"同步失败",null);
		}
		 return jsonBean;
	   }

	@OperationLog(
			success = "上传附件保存",
			busType = "智能审计",
			fail = "上传附件保存",
			operationType = OperationType.ADD,
			subType = "基础配置——审计实施——项目资料-附件列表上传附件保存"
	)
	 @PostMapping("/sjzl/saveFj")
	 @Operation(summary = "项目资料准备-上传附件保存")
	 public JsonBean saveFj(HttpServletRequest request, 
	    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	    		@RequestBody JSONArray arr) throws Exception {
		 JsonBean jsonBean = null;
		 try {
			 jsonBean= dataProService.saveFj(token,arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return jsonBean;
	   }



		@OperationLog(
				success = "项目资料下发",
				busType = "智能审计",
				fail = "项目资料下发",
				operationType = OperationType.DISPATCH,
				subType = "基础配置——审计实施——项目资料-下发"
		)
	   @GetMapping("/sjzl/issueProject")
	    @Operation(summary = "项目资料准备-下发")
	    public JsonBean issueProject(HttpServletRequest request, HttpServletResponse response,
	    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	    		@Parameter(name = "staffId", description = "下发人员ID", required = true) @RequestParam("staffId") String staffId,
	    		@Parameter(name = "dataId", description = "项目资料ID", required = true) @RequestParam("dataId") String dataId,
	    		@Parameter(name = "projectId", description = "项目资料ID", required = false) @RequestParam(value="projectId", required = false) String projectId) throws Exception {
	    	JsonBean jsonBean = null;
			try {
				jsonBean = dataProService.issueProject(token, staffId, dataId,projectId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	    }
	/**
	 * 前期审计资料列表
	 */
	@OperationLog(
			success = "前期审计资料列表",
			busType = "智能审计",
			fail = "前期审计资料列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——前期审计资料列表"
	)
	@GetMapping("/sjzb/project_proposal_yw")
	@Operation(summary = "前期审计资料列表")
	public JsonBean project_proposal_yw(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.projectProposalPageList(token, pageNumber, pageSize,tblnbsjProjectVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "前期审计资料明细",
			busType = "智能审计",
			fail = "前期审计资料明细",
			operationType = OperationType.SELECT,
			subType = "基础配置——前期审计资料列-明细"
	)
	@GetMapping("/sjzb/project_proposal_detail")
	@Operation(summary = "前期审计资料--明细")
    public JsonBean project_proposal_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectId", description = "主键", required = true)@RequestParam(value = "projectId", required = true) BigDecimal projectId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.findProjectProposalDetail(token,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 前期审计资料--导入资料列表
	 */
	@OperationLog(
			success = "前期审计资料导入",
			busType = "智能审计",
			fail = "前期审计资料导入",
			operationType = OperationType.IMPORT,
			subType = "基础配置——前期审计资料列-导入资料列表"
	)
	@GetMapping("/sjgd/audit_plan_list_planId_in")
	@Operation(summary = "前期审计资料--导入资料列表")
	public JsonBean audit_plan_list_planId_in(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "projectname", description = "项目名称", required = false) @RequestParam(value = "projectname", required = false) String projectname) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.auditPlanListPlanIdIn(token, pageNumber, pageSize,projectname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 前期审计资料--导入资料选择保存
	 */
	@OperationLog(
			success = "前期审计资料导入保存",
			busType = "智能审计",
			fail = "前期审计资料保存",
			operationType = OperationType.ADD,
			subType = "基础配置——前期审计资料列-导入资料选择保存"
	)
	@RequestMapping(value = "/sjgd/audit_plan_list_planId_in_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "前期审计资料--导入资料选择保存")
    public JsonBean audit_plan_list_planId_in_save(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "ids", description = "选择导入的项目ids(,分隔)", required = false)String ids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.auditPlanInAdd(ids,token);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
			e.printStackTrace();
		}
		return jsonBean;
    }
	
	//审计通知书下发列表
	@OperationLog(
			success = "下发列表",
			busType = "智能审计",
			fail = "下发列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——获取审计实施——审计通知下发人员列表"
	)
	@GetMapping("/sjzb/notice_issue_list")
	@Operation(summary = "审计通知书下发列表")
	public JsonBean notice_issue_list(HttpServletRequest request,
								@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
								@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
								@Parameter(name = "AdviceId", description = "当前记录ID", required = false) @RequestParam(value = "AdviceId", required = false) String adviceId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjAdvicenoteService.noticeIssuePageList(token, pageNumber, pageSize,adviceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
}
