package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblNbsjCertificate;
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import com.huabo.audit.oracle.entity.TblNbsjEntermeetingEntity;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.entity.TblNbsjLeavemeetingEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjWorkreport;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblNbsjDoubtfulpointVo;
import com.huabo.audit.oracle.vo.TblNbsjEntermeetingVo;
import com.huabo.audit.oracle.vo.TblNbsjFactbookVo;
import com.huabo.audit.oracle.vo.TblNbsjLeavemeetingVo;
import com.huabo.audit.oracle.vo.TblNbsjOperateVo;
import com.huabo.audit.oracle.vo.TblNbsjQuestionVo;
import com.huabo.audit.oracle.vo.TblNbsjWorkReportVo;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblAuditOptionService;
import com.huabo.audit.service.TblNbsjCertificateService;
import com.huabo.audit.service.TblNbsjDoubtfulpointService;
import com.huabo.audit.service.TblNbsjEntermeetingService;
import com.huabo.audit.service.TblNbsjFactbookService;
import com.huabo.audit.service.TblNbsjLeavemeetingService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjQuestionService;
import com.huabo.audit.service.TblNbsjQuestionaffirmService;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.service.TblNbsjWorkReportService;
import com.huabo.audit.service.TblOrganizaService;
import com.huabo.audit.service.TblTargetTypeService;
import com.huabo.audit.service.UserService;
import com.huabo.audit.util.DateUtils;
import com.huabo.audit.util.FileUtil;
import com.huabo.audit.util.FillAttribute;
import com.huabo.audit.util.FreeMarkerUtil;
import com.huabo.audit.util.MD5Encrypt;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计实施阶段
 */
@RestController
@Slf4j
@Tag(name="审计实施阶段",description="审计实施阶段")
@RequestMapping(value = "/auditImplement")
public class NbsjImplementController {

	@Resource
	public TblNbsjProjectService tblnbsjProjectService;

	@Resource
	public TblNbsjEntermeetingService tblNbsjEntermeetingService;

	@Resource
	public AttachmentService attachmentService;

	@Autowired
	public FreeMarkerConfig freeMarkerConfig;

	@Resource
	public TblNbsjLeavemeetingService tblNbsjLeavemeetingService;

	@Resource
	public TblTargetTypeService tblTargetTypeService;

	@Resource
	public TblNbsjSheetService tBlNbsjSheetService;

	@Resource
	public TblNbsjFactbookService tblNbsjFactbookService;

	@Resource
	public TblNbsjQuestionService tblNbsjQuestionService;

	@Resource
	public TblNbsjQuestionaffirmService tblNbsjQuestionaffirmService;

	@Resource
	public TblNbsjOperateService tblNbsjOperateService;

	@Resource
	public TblNbsjWorkReportService tblNbsjWorkReportService;
	
	@Resource
	public TblNbsjDoubtfulpointService tblNbsjDoubtfulpointService;
	
	@Resource
	public UserService userService;
	
	@Resource
	public TblOrganizaService tblOrganizaService;
	
	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Resource
	private TblAuditOptionService tblAuditOptionService;

	private static final String DOCDIC = PropertyFileReader.getItem("doc.path");

	@Resource
	private TblNbsjProjectService  tblNbsjProjectService;
	
	@Resource
    private TblNbsjCertificateService tblNbsjCertificateService;
	
	@Resource
    private UserProvider userProvider;
	
	/**
	 * 审计实施-进场纪要列表
	 */
	@OperationLog(
			success = "进场纪要",
			busType = "智能审计",
			fail = "进场纪要",
			operationType = OperationType.SELECT,
			subType = "审计实施-进场纪要列表查询"
	)
	@GetMapping("/sjss/in_meet_record_list")
	@Operation(summary = "审计实施-进场纪要")
	public JsonBean in_meet_record_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjEntermeetingVo tblNbsjEntermeetingVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			if(null != projectId) {
				tblNbsjEntermeetingVo.setProgectid(projectId);
			}
			
			jsonBean = tblNbsjEntermeetingService.inMeetRecordListPageList(token, pageNumber, pageSize,tblNbsjEntermeetingVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 进场纪要-新增与修改
	 */
	@OperationLog(
			success = "进场纪要新增",
			busType = "智能审计",
			fail = "进场纪要新增",
			operationType = OperationType.ADD,
			subType = "审计实施-进场纪要新增"
	)
	@RequestMapping(value = "/sjss/in_meet_record_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "进场纪要-新增与修改")
    public JsonBean in_meet_record_add(HttpServletRequest request,@Parameter(name = "met", description = "实体", required = true)TblNbsjEntermeetingEntity tblNbsjEntermeetingEntity,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjEntermeetingService.inMeetRecordAdd(tblNbsjEntermeetingEntity,token, attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 进场纪要-附件列表
	 */
	@OperationLog(
			success = "进场纪要附件",
			busType = "智能审计",
			fail = "进场纪要附件",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取进场纪要附件列表"
	)
    @GetMapping("/sjss/in_meet_record_file_list")
	@Operation(summary = "进场纪要-附件列表")
	public JsonBean in_meet_record_file_list(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "enterid", description = "业务主键", required = true) @RequestParam(value = "enterid", required = true) BigDecimal enterid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.inMeetRecordFileList(token,enterid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    /**
     * 进场纪要-附件删除
     */
	@OperationLog(
			success = "进场纪要附件删除",
			busType = "智能审计",
			fail = "进场纪要附件删除",
			operationType = OperationType.DELETE,
			subType = "审计实施-进场纪要附件删除"
	)
    @GetMapping("/sjss/in_meet_record_file_del")
    @Operation(summary = "进场纪要-附件删除")
    public R in_meet_record_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjEntermeetingService.removeAttInfoByAttId(token, attId);
    }

	/**
	 * 进场纪要-删除
	 */
	@OperationLog(
			success = "进场纪要附件删除",
			busType = "智能审计",
			fail = "进场纪要附件删除",
			operationType = OperationType.DELETE,
			subType = "审计实施-进场纪要删除"
	)
	@GetMapping("/sjss/met_delete")
	@Operation(summary = "进场纪要-根据主键删除信息")
    public JsonBean met_delete(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "enterid", description = "主键", required = true)@RequestParam(value = "enterid", required = true) BigDecimal enterid) {
        
        try {
			return tblNbsjEntermeetingService.metDelete(enterid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 进场纪要-明细
	 */
	@OperationLog(
			success = "进场纪要详情",
			busType = "智能审计",
			fail = "进场纪要详情",
			operationType = OperationType.SELECT,
			subType = "审计实施-进场纪要详情"
	)
	@GetMapping("/sjss/in_meet_record_disp")
    @Operation(summary = "进场纪要-明细")
    public JsonBean in_meet_record_disp(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "enterid", description = "主键", required = true)@RequestParam(value = "enterid", required = true) BigDecimal enterid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjEntermeetingService.findNbsjEntermeetingDetail(token,enterid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	/**
	 * 进场纪要-作废
	 */
	@OperationLog(
			success = "进场纪要废除",
			busType = "智能审计",
			fail = "进场纪要废除",
			operationType = OperationType.UPDATE,
			subType = "审计实施-进场纪要废除"
	)
	@GetMapping("/sjss/met_cancel")
	@Operation(summary = "进场纪要-作废")
    public JsonBean met_cancel(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "enterid", description = "主键", required = true)@RequestParam(value = "enterid", required = true) BigDecimal enterid) {
        
        try {
			return tblNbsjEntermeetingService.metCalcel(enterid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
//	/**
//	 * 进场纪要-导出
//	 */
//	@GetMapping("/sjss/in_meet_record_export")
//	@Operation(summary = "进场纪要-导出")
//	public void in_meet_record_export(HttpServletRequest request, HttpServletResponse response,
//			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
//			@Parameter(name = "enterid", description = "进场纪要主键", required = true)@RequestParam(value = "enterid", required = true) String enterid) throws Exception {
//
//		TblNbsjEntermeetingEntity met= tblNbsjEntermeetingService.findEntityByEnterId(enterid);
//		Map<String, String> map = new HashMap<String,String>();
//		map.put("repdesc", met.getContent()==null?"":met.getContent());//内容
//		String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/doc");
//		//String fileName =report.getReportname()+".doc";
//		String strname = MD5Encrypt.encrypByMd5(met.getEntername());
//		//加密文件
//		String fileName =strname+ DateUtils.dateToUnixTimestamp(DateUtils.getNowTime())+".doc";
//		  //中文文件
//		String filen = met.getEntername()+".doc";
//		Boolean flag =(Boolean)FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
//		fileName=new String(fileName.getBytes("utf-8"),"iso-8859-1");
//		if(!flag){//如何静态文件不存在，重新生成
//	        FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
//		}
//		String fileName1 = FREEMARKER_PATH +"/"+ fileName;
//		fileName1=new String(fileName1.getBytes("utf-8"),"iso-8859-1");
//		log.info("导出地址："+fileName1);
//		FileUtil.downLoad(fileName1, response, false, filen);
//		FileUtil.deleteFile(fileName1);
//	}


	
	/**
	 * 离场纪要列表
	 */
	@OperationLog(
			success = "离场纪要",
			busType = "智能审计",
			fail = "离场纪要",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取离场纪要列表"
	)
	@GetMapping("/sjss/out_meet_record_list")
	@Operation(summary = "离场纪要列表")
	public JsonBean out_meet_record_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			
			if(null != tblNbsjLeavemeetingVo.getProgectid()) {
				
			}
			
			jsonBean = tblNbsjLeavemeetingService.outMeetRecordListPageList(token, pageNumber, pageSize,tblNbsjLeavemeetingVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 离场纪要-新增与修改
	 */
	@OperationLog(
			success = "离场纪要",
			busType = "智能审计",
			fail = "离场纪要",
			operationType = OperationType.ADD,
			subType = "审计实施-离场纪要"
	)
	@RequestMapping(value = "/sjss/out_meet_record_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "离场纪要-新增与修改")
    public JsonBean out_meet_record_add(HttpServletRequest request,@Parameter(name = "lev", description = "实体", required = true)TblNbsjLeavemeetingEntity lev,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjLeavemeetingService.outMeetRecordAdd(lev,token,attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 离场纪要-附件列表
	 */
	@OperationLog(
			success = "离场纪要附件 ",
			busType = "智能审计",
			fail = "离场纪要",
			operationType = OperationType.SELECT,
			subType = "审计实施-离场纪要附件列表查询"
	)
    @GetMapping("/sjss/out_meet_record_file_list")
	@Operation(summary = "离场纪要-附件列表")
	public JsonBean out_meet_record_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "leaveid", description = "业务主键", required = true) @RequestParam(value = "leaveid", required = true) BigDecimal leaveid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.outMeetRecordFileList(token,leaveid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 离场纪要-附件删除
     */
	@OperationLog(
			success = "离场纪要附件删除",
			busType = "智能审计",
			fail = "离场纪要附件删除",
			operationType = OperationType.DELETE,
			subType = "审计实施-离场纪要附件删除"
	)
    @GetMapping("/sjss/out_meet_record_file_del")
    @Operation(summary = "离场纪要-附件删除")
    public R out_meet_record_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjLeavemeetingService.removeAttInfoByAttId(token, attId);
    }
    
	/**
	 * 离场纪要-删除
	 */
	@OperationLog(
			success = "离场纪要删除",
			busType = "智能审计",
			fail = "离场纪要删除",
			operationType = OperationType.DELETE,
			subType = "审计实施-删除离场纪要"
	)
	@GetMapping("/sjss/lev_delete")
	@Operation(summary = "离场纪要根据主键删除信息")
    public JsonBean lev_delete(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "leaveid", description = "主键", required = true)@RequestParam(value = "leaveid", required = true) BigDecimal leaveid) {
        
        try {
			return tblNbsjLeavemeetingService.outmetDelete(leaveid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 离场纪要-明细
	 */
	@OperationLog(
			success = "离场纪要明细",
			busType = "智能审计",
			fail = "离场纪要明细",
			operationType = OperationType.SELECT,
			subType = "审计实施-离场纪要详情明细"
	)
	@GetMapping("/sjss/out_meet_record_disp")
    @Operation(summary = "离场纪要-明细")
    public JsonBean out_meet_record_disp(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "leaveid", description = "主键", required = true)@RequestParam(value = "leaveid", required = true) BigDecimal leaveid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjLeavemeetingService.findNbsjLeavemeetingDetail(token,leaveid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	/**
	 * 离场纪要-作废
	 */
	@OperationLog(
			success = "离场纪要作废",
			busType = "智能审计",
			fail = "离场纪要作废",
			operationType = OperationType.UPDATE,
			subType = "审计实施-离场纪要作废"
	)
	@GetMapping("/sjss/out_met_cancel")
	@Operation(summary = "离场纪要-作废")
    public JsonBean out_met_cancel(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "leaveid", description = "主键", required = true)@RequestParam(value = "leaveid", required = true) BigDecimal leaveid) {
        
        try {
			return tblNbsjLeavemeetingService.outMetCalcel(leaveid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 离场纪要-导出
	 */
	@OperationLog(
			success = "离场纪要导出",
			busType = "智能审计",
			fail = "离场纪要导出",
			operationType = OperationType.EXPORT,
			subType = "审计实施-离场纪要列表导出"
	)
	@GetMapping("/sjss/out_meet_record_export")
	@Operation(summary = " 离场纪要-导出")
	public void out_meet_record_export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("selectid");
        TblNbsjLeavemeetingEntity met = new TblNbsjLeavemeetingEntity();
		if (id != null) {
             met = tblNbsjLeavemeetingService.getById(id);
		}
		Map<String, String> map = new HashMap<String,String>();
		map.put("repdesc", met.getContent()==null?"":met.getContent());//内容
		String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/WEB-INF/doc");
		//String fileName =report.getReportname()+".doc";
		String strname = MD5Encrypt.encrypByMd5(met.getLeavename());
		//加密文件
		String fileName =strname+ DateUtils.dateToUnixTimestamp(DateUtils.getNowTime())+".doc";
		  //中文文件
		String filen = met.getLeavename()+".doc";
		Boolean flag =(Boolean)FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
		fileName=new String(fileName.getBytes("utf-8"),"iso-8859-1");
		if(!flag){//如何静态文件不存在，重新生成
	        FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
		}
		String fileName1 = FREEMARKER_PATH +"/"+ fileName;
		fileName1=new String(fileName1.getBytes("utf-8"),"iso-8859-1");
//		logger.info("导出地址："+fileName1);
		FileUtil.downLoad(fileName1, response, false, filen);
		FileUtil.deleteFile(fileName1);
	}
	
	
	/**
	 * 我的任务清单列表
	 */
	@OperationLog(
			success = "我的任务",
			busType = "智能审计",
			fail = "我的任务列表",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取我的任务清单列表"
	)
	@GetMapping("/sjss/check_list_my")
	@Operation(summary = "我的任务清单列表")
	public JsonBean check_list_my(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjOperateVo tblNbsjOperateVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjOperateService.checkListMyPageList(token, pageNumber, pageSize,tblNbsjOperateVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 我的任务-明细
	 */
	@OperationLog(
			success = "我的任务明细",
			busType = "智能审计",
			fail = "我的任务明细",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取我的任务明细"
	)
	@GetMapping("/sjss/check_disp")
    @Operation(summary = "我的任务-明细")
    public JsonBean check_disp(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "programid", description = "主键", required = true)@RequestParam(value = "programid", required = true) BigDecimal programid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjOperateService.checkListMyDetail(token, programid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	/**
	 * 我的任务-完成
	 */
	@OperationLog(
			success = "我的任务明细",
			busType = "智能审计",
			fail = "我的任务明细",
			operationType = OperationType.UPDATE,
			subType = "审计实施-修改我的任务完成状态"
	)
	@GetMapping("/sjss/check_final")
	@Operation(summary = "我的任务-完成")
    public JsonBean check_final(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "operateid", description = "主键", required = true)@RequestParam(value = "operateid", required = true) BigDecimal operateid,
    		@Parameter(name = "programId", description = "主键", required = true)@RequestParam(value = "programId", required = true) BigDecimal programId) {
        
        try {
			return tblNbsjOperateService.checkListFinal(operateid,programId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	/**
	 * 工作日志列表
	 */
	@OperationLog(
			success = "工作日志",
			busType = "智能审计",
			fail = "工作日志",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取工作日志（工作小结）列表"
	)
	@GetMapping("/nbsj/xmgl/workReportList")
	@Operation(summary = "工作日志列表")
	public JsonBean workReportList(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjWorkReportVo tblNbsjWorkReportVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			if(projectId!=null){
				tblNbsjWorkReportVo.setProjectId(projectId);
			}
			jsonBean = tblNbsjWorkReportService.workReportPageList(token, pageNumber, pageSize,tblNbsjWorkReportVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 工作日志-新增与修改
	 */
	@OperationLog(
			success = "工作小结",
			busType = "智能审计",
			fail = "工作小结",
			operationType = OperationType.ADD,
			subType = "审计实施-获取工作日志（工作小结）新增"
	)
	@RequestMapping(value = "/nbsj/xmgl/workReportSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "工作日志-新增与修改")
    public JsonBean workReportSave(HttpServletRequest request,@Parameter(name = "lev", description = "实体", required = true)TblNbsjWorkreport wr,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			System.out.println(wr.getReportname());
			jsonBean = this.tblNbsjWorkReportService.workReportAdd(wr,token,attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 工作日志-附件列表
	 */
	@OperationLog(
			success = "工作小结附件",
			busType = "智能审计",
			fail = "工作小结附件",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取工作日志附件列表"
	)
    @GetMapping("/nbsj/xmgl/workReport_file_list")
	@Operation(summary = "工作日志-附件列表")
	public JsonBean workReport_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "reportid", description = "业务主键", required = true) @RequestParam(value = "reportid", required = true) BigDecimal reportid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.workReportFileList(token,reportid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 工作日志-附件删除
     */
	@OperationLog(
			success = "删除附件",
			busType = "智能审计",
			fail = "删除附件",
			operationType = OperationType.DELETE,
			subType = "审计实施-删除工作日志附件"
	)
    @GetMapping("/nbsj/xmgl/workReport_file_del")
    @Operation(summary = "工作日志-附件删除")
    public R workReport_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjWorkReportService.removeAttInfoByAttId(token, attId);
    }
    
	/**
	 * 工作日志-删除
	 */
	@OperationLog(
			success = "删除小结",
			busType = "智能审计",
			fail = "删除小结",
			operationType = OperationType.DELETE,
			subType = "审计实施-删除工作日志（工作小结）"
	)
	@GetMapping("/nbsj/xmgl/workReportDelete")
	@Operation(summary = "工作日志-删除")
    public JsonBean workReportDelete(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "reportid", description = "主键", required = true)@RequestParam(value = "reportid", required = true) BigDecimal reportid) {
        
        try {
			return tblNbsjWorkReportService.workReportDelete(reportid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 工作日志-明细
	 */
	@OperationLog(
			success = "小结明细",
			busType = "智能审计",
			fail = "小结明细",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取工作小结明细内容"
	)
	@GetMapping("/nbsj/xmgl/workReportDetail")
    @Operation(summary = "工作日志-明细")
    public JsonBean workReportDetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "reportid", description = "主键", required = true)@RequestParam(value = "reportid", required = true) BigDecimal reportid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjWorkReportService.findNbsjWorkReportDetail(token,reportid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	
	/**
	 * 我的底稿列表
	 */
	@OperationLog(
			success = "我的底稿",
			busType = "智能审计",
			fail = "我的底稿",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取项目归档我的底稿列表"
	)
	@GetMapping("/sjss/project_standard_listdg")
	@Operation(summary = "我的底稿列表")
	public JsonBean project_standard_listdg(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			
			if(null != projectId) {
				tBlNbsjSheetVo.setProjectid(projectId);
			}
			jsonBean = tBlNbsjSheetService.projectStandardDgPageList(token, pageNumber, pageSize,tBlNbsjSheetVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 我的底稿-新增与修改
	 */
	@OperationLog(
			success = "我的底稿",
			busType = "智能审计",
			fail = "我的底稿",
			operationType = OperationType.ADD,
			subType = "审计实施-我的底稿列表"
	)
	@RequestMapping(value = "/sjss/project_standard_dg_add",  method = {RequestMethod.POST})
	@Operation(summary = "我的底稿-新增与修改")
    public JsonBean project_standard_dg_add(HttpServletRequest request,@Parameter(name = "sheet", description = "实体", required = false) @RequestBody TblNbsjSheetEntity sheet,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "srJson", description = "报告内容json", required = false)String srJson)throws Exception{
		JsonBean jsonBean = null;
		try {
			
//			srJson = "[{\"reportConcent\": \"报告错误内容1111111\", \"sjdeptIds\": \"198328,198334\" },"
//					+ "{\"reportConcent\": \"报告错误内容222222\", \"sjdeptIds\": \"198328\" }]";
			
			jsonBean = this.tBlNbsjSheetService.projectStandardDgAdd(sheet,token,srJson);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
 
	
	/**
	 * 我的底稿-列表保存财务数据底稿附件
	 */
	@OperationLog(
			success = "我的底稿附件",
			busType = "智能审计",
			fail = "我的底稿附件",
			operationType = OperationType.ADD,
			subType = "审计实施-我的底稿新增附件"
	)
	@RequestMapping(value = "/sjss/project_dgfile_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "我的底稿-新增与修改")
    public JsonBean project_dgfile_save(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attid", description = "附件id", required = false)@RequestParam(value="attid", required = true)String attid,
			 @Parameter(name = "sheetid", description = "底稿id", required = false)@RequestParam(value="sheetid", required = true)BigDecimal sheetid)throws Exception{
		JsonBean jsonBean = null;
		try {
			
//			srJson = "[{\"reportConcent\": \"报告错误内容1111111\", \"sjdeptIds\": \"198328,198334\" },"
//					+ "{\"reportConcent\": \"报告错误内容222222\", \"sjdeptIds\": \"198328\" }]";
			
			jsonBean = this.tBlNbsjSheetService.projectSaveCwFile(sheetid,token,attid);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }

	@OperationLog(
			success = "审计取证单",
			busType = "智能审计",
			fail = "审计取证单",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取我的底稿-添加审计取证单--列表"
	)
    @RequestMapping(value = "/sjss/getNbsjCertificateListPage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "我的底稿-添加审计取证单--列表")
    public JsonBean getNbsjCertificateListPage(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,TblNbsjCertificate cate,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "sheetid", description = "底稿ID", required = false)@RequestParam(value = "sheetid", required = false) String sheetid,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = this.tblNbsjCertificateService.getNbsjCertificateList(token,cate,pageNumber,pageSize,sheetid);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
        return jsonBean;
    }

	@OperationLog(
			success = "审计取证单",
			busType = "智能审计",
			fail = "审计取证单",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取我的底稿--审计取证单列表"
	)
    @RequestMapping(value = "/sjss/getNbsjCertificateList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
  	@Operation(summary = "我的底稿修改及详情页面--审计取证单列表")
      public JsonBean getNbsjCertificateList(HttpServletRequest request,
      		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,@Parameter(name = "sheetid", description = "业务主键", required = true) @RequestParam(value = "sheetid", required = true) BigDecimal sheetid)
    {
      	JsonBean jsonBean = null;
      	try {
  			jsonBean = this.tblNbsjCertificateService.getCertificateList(token,sheetid);
  		} catch (Exception e) {
  			e.printStackTrace();
  			ResponseFormat.retParam(1,1000,e.getMessage());
  		}
          return jsonBean;
      }

	@OperationLog(
			success = "审计问题缺陷",
			busType = "整改追责",
			fail = "审计问题缺陷",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取审计问题缺陷的内部缺陷列表"
	)
    @RequestMapping(value = "/sjss/getNbsjBugList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
  	@Operation(summary = "我的底稿修改及详情页面--关联缺陷列表")
      public JsonBean getNbsjBugList(HttpServletRequest request,
      		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,@Parameter(name = "sheetid", description = "业务主键", required = true) @RequestParam(value = "sheetid", required = true) BigDecimal sheetid)
    {
      	JsonBean jsonBean = null;
      	try {
  			jsonBean = this.tBlNbsjSheetService.getNbsjBugList(token,sheetid);
  		} catch (Exception e) {
  			e.printStackTrace();
  			ResponseFormat.retParam(1,1000,e.getMessage());
  		}
          return jsonBean;
      }
	
	/**
	 * 我的底稿-附件列表
	 */
	@OperationLog(
			success = "附件列表",
			busType = "智能审计",
			fail = "附件列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取我的底稿--附件列表"
	)
    @GetMapping("/sjss/project_standard_dg_file_list")
	@Operation(summary = "我的底稿-附件列表")
	public JsonBean project_standard_dg_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "sheetid", description = "业务主键", required = true) @RequestParam(value = "sheetid", required = true) BigDecimal sheetid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.dgFileList(token,sheetid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 我的底稿-附件删除
     */
	@OperationLog(
			success = "附件删除",
			busType = "智能审计",
			fail = "附件删除",
			operationType = OperationType.DELETE,
			subType = "审计实施——我的底稿--附件删除"
	)
    @GetMapping("/sjss/project_standard_dg_file_del")
    @Operation(summary = "我的底稿-附件删除")
    public R project_standard_dg_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tBlNbsjSheetService.removeAttInfoByAttId(token, attId);
    }
    
	/**
	 * 我的底稿-删除（）
	 */
	@OperationLog(
			success = "底稿删除",
			busType = "智能审计",
			fail = "底稿删除",
			operationType = OperationType.DELETE,
			subType = "审计实施——删除我的底稿"
	)
	@GetMapping("/sjss/project_standard_dg_del")
	@Operation(summary = "我的底稿-删除")
    public JsonBean project_standard_dg_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "sheetid", description = "主键", required = true)@RequestParam(value = "sheetid", required = true) BigDecimal sheetid) {
        
        try {
			return tBlNbsjSheetService.projectStandardDgDelete(sheetid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
     * 我的底稿 详情
     */
	@OperationLog(
			success = "审计问题详情",
			busType = "整改追责",
			fail = "审计问题详情",
			operationType = OperationType.SELECT,
			subType = "审计实施——问题汇总--审计问题汇总详情页面"
	)
    @GetMapping("/sjss/project_standard_dg_detail")
    @Operation(summary = "我的底稿--详情")
    public JsonBean project_standard_dg_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "sheetid", description = "主键", required = true)@RequestParam(value = "sheetid", required = true) BigDecimal sheetid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tBlNbsjSheetService.findNbsjSheetDetail(token,sheetid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	@OperationLog(
			success = "底稿导出",
			busType = "智能审计",
			fail = "底稿导出",
			operationType = OperationType.EXPORT,
			subType = "审计实施——我的底稿--底稿内容导出为word"
	)
    @GetMapping("/sjss/exportSheetWord")
    @Operation(summary = "我的底稿--导出word")
    public JsonBean exportSheetWord(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "sheetid", description = "主键", required = true)@RequestParam(value = "sheetid", required = true) BigDecimal sheetid) throws Exception {
    	 response.setContentType("application/binary;charset=UTF-8");
    	return tBlNbsjSheetService.exportSheetWord(token,sheetid,response);
    }
    
    
    /**
     * 我的底稿-报告内容/问题描述-删除
     */
	@OperationLog(
			success = "底稿问题描述删除",
			busType = "智能审计",
			fail = "底稿问题描述删除",
			operationType = OperationType.DELETE,
			subType = "审计实施——我的底稿--报告内容/问题描述-删除"
	)
    @GetMapping("/sjss/sheet_report_del")
    @Operation(summary = "我的底稿-报告内容/问题描述-删除")
    public JsonBean sheet_report_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "reportid", description = "主键", required = true)@RequestParam(value = "reportid", required = true) BigDecimal reportid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tBlNbsjSheetService.sheetReportDel(token,reportid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    

	/**
	 * 我的底稿-导出
	 */
	@OperationLog(
			success = "底稿导出",
			busType = "智能审计",
			fail = "底稿导出",
			operationType = OperationType.EXPORT,
			subType = "审计实施——我的底稿--批量导出"
	)
	@RequestMapping(value = "/sjss/project_standard_dg_export",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@Operation(summary = "我的底稿-导出")
	public void project_standard_dg_export(HttpServletRequest request, HttpServletResponse response,
										   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
										   @Parameter(name = "type", description = "类型，区分：我的底稿：不传；底稿管理：1；非必填", required = false)@RequestParam(value = "type", required = false) String  type,
										   @Parameter(name = "projectid", description = "项目id，非必填", required = false)@RequestParam(value = "projectid", required = false) BigDecimal projectid,
										   @Parameter(name = "sheetCode", description = "底稿编号", required = false)@RequestParam(required = false) String sheetCode,
										   @Parameter(name = "sheetName", description = "底稿名称", required = false)@RequestParam(required = false) String sheetName,
										   @Parameter(name = "createstaff", description = "拟稿人", required = false)@RequestParam(required = false) BigDecimal createstaff,
										   @Parameter(name = "status", description = "审批状态", required = false)@RequestParam(required = false) Integer status
	) throws Exception {

		TblNbsjSheetEntity tblSheet = new TblNbsjSheetEntity();
		tblSheet.setSheetCode(sheetCode);
		tblSheet.setSheetName(sheetName);
		tblSheet.setCreatestaff(createstaff);
		tblSheet.setStatus(status);
		TblNbsjProject project = this.tblnbsjProjectService.getSSProjectDetail(token);
		if (projectid != null) {
			project = tblnbsjProjectService.getProjectById(projectid);
		}
		response.setContentType("application/binary;charset=UTF-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("底稿管理.xlsx"));
			//response.setHeader("Content-Disposition", "attachment; filename=" + new String("底稿管理".getBytes(), "UTF-8") + ".xlsx");// 组装附件名称和格式
			ServletOutputStream outputStream = response.getOutputStream();
			List<Object[]> objList1 = new ArrayList<Object[]>();
			List<TblNbsjSheetEntity> objList = tBlNbsjSheetService.OBJfindAllByProjectidstaff(project.getProjectId(), token,tblSheet);
			if(type!=null && type.equals("1")) {
				objList = tBlNbsjSheetService.OBJfindAllByProjectid(project.getProjectId(), token,tblSheet);
			}
			for (int i = 0; i < objList.size(); i++) {
//            	TblNbsjSheetEntity ob = new TblNbsjSheetEntity();
				TblNbsjSheetEntity o = objList.get(i);
//                System.arraycopy(o,0,ob,0,14);
				List<TblAuditOption> optionByRelationId = tblAuditOptionService.findOptionByRelationId("");//(o[14].toString());
				BigDecimal bd = optionByRelationId.size() > 0 ? optionByRelationId.get(0).getOptStaffid() : new BigDecimal(0);
				String str1 = "";
				String str2 = "";
				if (optionByRelationId.size() == 1) {
					TblAuditOption ao = optionByRelationId.get(0);
					if (ao.getCreateDate() != null) {
						str1 += "复核人:" + ao.getStaffidName() + "  复核时间：" + sdf.format(ao.getCreateDate()) + "  状态：" + ao.getOptState() + "  意见：" + ao.getOptDesc() + "\n";// 复核时间："+ao.getCreateDate()+"
					} else {
						str1 += "复核人:" + ao.getStaffidName() + "  复核时间：    状态：" + ao.getOptState() + "  意见：" + ao.getOptDesc() + "\n";// 复核时间："+ao.getCreateDate()+"
					}

				}
				if (optionByRelationId.size() == 2) {
					TblAuditOption ao = optionByRelationId.get(0);
					if (ao.getCreateDate() != null) {
						str1 += "复核人:" + ao.getStaffidName() + "  复核时间：" + sdf.format(ao.getCreateDate()) + "  状态：" + ao.getOptState() + "  意见：" + ao.getOptDesc() + "\n";// 复核时间："+ao.getCreateDate()+"
					} else {
						str1 += "复核人:" + ao.getStaffidName() + "  复核时间：    状态：" + ao.getOptState() + "  意见：" + ao.getOptDesc() + "\n";// 复核时间："+ao.getCreateDate()+"
					}
					TblAuditOption ao1 = optionByRelationId.get(1);
					if (ao1.getCreateDate() != null) {
						str2 += "复核人:" + ao1.getStaffidName() + "  复核时间：" + sdf.format(ao1.getCreateDate()) + "  状态：" + ao1.getOptState() + "  意见：" + ao1.getOptDesc() + "\n";
					} else {
						str2 += "复核人:" + ao1.getStaffidName() + "  复核时间：    状态：" + ao1.getOptState() + "  意见：" + ao1.getOptDesc() + "\n";
					}

				}
//				o.setYjfh(str2.equals("") ? "" : str2);
//				o.setEjfh(str1.equals("") ? "" : str1);

				Object[] tempob = new Object[14];
				tempob[0] = project.getPrjoectName();
				tempob[1] = o.getSheetCode();
				tempob[2] = o.getSheetName();
				if(o.getOrgIdNames()!=null) {
					tempob[3] = o.getOrgIdNames();
				}else {
					tempob[3] = o.getApprover();
				}
				
				tempob[4] = o.getBusinessAffiliation();
				tempob[5] = o.getRealname();
				//tempob[6] = o.getTargetName();
				tempob[6] = o.getRiskLevel();
				//tempob[8] = o.getTargetName();
				tempob[7] = o.getBusinessType();
				if(o.getCreateTime()!=null) {
					tempob[8] =sdf.format(o.getCreateTime()) ;
				}else {
					tempob[8]="";
				}
				
				tempob[9] = o.getQuesTitle();
				tempob[10] = o.getSuditProcess();
				tempob[11] = o.getSjbwl();
				tempob[12] = o.getYjfh();
				tempob[13] = o.getEjfh();

				objList1.add(tempob);
			}
			//String[] titles = {"所属项目名称","底稿编号 ", "底稿名称", "审计目标 ", "被审计对象", "拟稿人", "拟稿日期","一级复核","二级复核"};
 			String[] titles = { "所属项目名称", "底稿编号 ", "底稿名称", "被审计对象", "审计事项", "审计人员", /* " 审计目的", */ "是否发现问题",
 					/* " 审计分项", */ " 问题单元","底稿创建时间", "问题标题", "审计程序"/* ,"审计程序执行过程","审计发现","审计意见及建议" */, "审计备忘录", "一级复核人", "二级复核人"};
			
			//String[] titles = { "底稿编号 ", "底稿名称", "被审计对象", "拟稿人","审批人","拟稿日期","状态"};
			ImportOrExportExcelUtil.exportExcel(titles, objList1, outputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
	
	/**
	 * 底稿汇总
	 */
	@OperationLog(
			success = "底稿汇总",
			busType = "智能审计",
			fail = "底稿汇总",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取底稿汇总列表"
	)
	@GetMapping("/sjss/dg_listall")
	@Operation(summary = "底稿汇总列表")
	public JsonBean dgAllPageList(HttpServletRequest request, TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tBlNbsjSheetService.dgAllPageList(token, pageNumber, pageSize,tBlNbsjSheetVo);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	
	/**
	 * 我的任务-根据任务查看底稿管理列表
	 */
	@OperationLog(
			success = "底稿管理列表",
			busType = "智能审计",
			fail = "底稿管理列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——我的任务-根据任务查看底稿管理列表"
	)
	@GetMapping("/sjss/rwdggl_list")
	@Operation(summary = "底稿管理列表")
	public JsonBean rwdggl_list(HttpServletRequest request, TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "operateid", description = "任务id", required = false) @RequestParam(value = "operateid", required = false) BigDecimal operateid,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			tBlNbsjSheetVo.setOperateid(operateid);
			jsonBean = tBlNbsjSheetService.dgglPageList(token, pageNumber, pageSize,tBlNbsjSheetVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 底稿管理列表
	 */
	@OperationLog(
			success = "底稿管理列表",
			busType = "智能审计",
			fail = "底稿管理列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取底稿管理列表"
	)
	@GetMapping("/sjss/dggl_list")
	@Operation(summary = "底稿管理列表")
	public JsonBean dggl_list(HttpServletRequest request, TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tBlNbsjSheetService.dgglPageList(token, pageNumber, pageSize,tBlNbsjSheetVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
     * 底稿管理 详情
     */
	@OperationLog(
			success = "底稿管理列表",
			busType = "智能审计",
			fail = "底稿管理列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取底稿管理列表"
	)
    @GetMapping("/sjss/dggl_detail")
    @Operation(summary = "底稿管理--详情")
    public JsonBean dggl_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "sheetid", description = "主键", required = true)@RequestParam(value = "sheetid", required = true) BigDecimal sheetid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tBlNbsjSheetService.findNbsjSheetDetail(token,sheetid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	@OperationLog(
			success = "底稿汇总列表",
			busType = "智能审计",
			fail = "底稿汇总列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——我的底稿 -是否是汇总底稿-底稿汇总列表"
	)
    @GetMapping("/sjss/chooseProjectSheet")
	@Operation(summary = "我的底稿 -是否是汇总底稿-底稿汇总列表")
	public JsonBean chooseProjectSheet(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tBlNbsjSheetService.chooseProjectSheet(token, pageNumber, pageSize,tBlNbsjSheetVo);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}

	@OperationLog(
			success = "业务单元列表",
			busType = "智能审计",
			fail = "业务单元列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取我的底稿 -业务单元列表"
	)
	@GetMapping("/sjss/findPorgramByUser")
	@Operation(summary = "我的底稿 -业务单元列表")
	public JsonBean findPorgramByUser(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "bsunitname", description = "业务单元查询条件", required = false) @RequestParam(value = "bsunitname", required = false) String bsunitname) {

		JsonBean jsonBean = null;
		try {
			 jsonBean = tblNbsjOperateService.findPorgramByUser(token,null,pageNumber,pageSize,bsunitname);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
//	
//	@GetMapping("/sjss/check_list_myDg")
//	@Operation(summary = "我的底稿 -选择业务单元列表页面")
//	public JsonBean chooseProjectSheet(HttpServletRequest request, 
//			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize)
//    public ModelAndView check_list_myDg(Integer pageNumber, HttpServletRequest request) {
//        ModelAndView mv = new ModelAndView();
//        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
//        String bsunitname = request.getParameter("bsunitname");
//        TblnbsjProject project = this.tblnbsjProjectService.getSelectProject();
//        pageBean = this.tblNbsjOperateService.findPorgramByUser(null,project.getProjectid(), tblStaff.getStaffid(), pageNumber, pageBean.getPageSize(),bsunitname);
//        // pageBean = this.tblAduitProGramService.getProgramByUser(targetId,
//        // projectId, tblStaff.getStaffid(), pageNumber,
//        // pageBean.getPageSize());
//        mv.addObject("pageBean", pageBean);
//        mv.addObject("projectId", project.getProjectid());
//        mv.setViewName("nbsj/sjss/check_list_myDg");
//        //查询框代码
//        String choiceSearch = request.getParameter("choiceSearch");
//        if(choiceSearch == null || "".equals(choiceSearch)) {
//        	choiceSearch = "hide";
//        }
//        mv.addObject("choiceSearch",choiceSearch); 	
//        return mv;
//    }

	@OperationLog(
			success = "判断角色",
			busType = "智能审计",
			fail = "判断角色",
			operationType = OperationType.SELECT,
			subType = "判断当前人员是否为项目经理或者组长"
	)
	@GetMapping("/sjss/ifPmOrLeader")
	@Operation(summary = "判断当前人员是否为项目经理或者组长")
	public JsonBean ifPmOrLeader(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
	  {
		JsonBean jsonBean = null;
		try {
			jsonBean = tBlNbsjSheetService.ifPmOrLeader(token);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	
    /**
	 * 审计发现列表
	 */
	@OperationLog(
			success = "审计发现列表",
			busType = "智能审计",
			fail = "审计发现列表",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取审计发现列表"
	)
	@GetMapping("/xmzl/question_store_list")
	@Operation(summary = "审计发现列表")
	public JsonBean question_store_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjQuestionVo tblNbsjQuestionVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjQuestionService.questionStorePageList(token, pageNumber, pageSize,tblNbsjQuestionVo,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 审计发现-修改状态为不上报告
	 */
	@OperationLog(
			success = "审计发现",
			busType = "智能审计",
			fail = "审计发现",
			operationType = OperationType.UPDATE,
			subType = "审计发现——修改问题上报状态"
	)
	@GetMapping("/sjzg/isfalse")
	@Operation(summary = "审计发现-修改问题状态为不上报告")
    public JsonBean isfalse(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "ifsbg", description = "状态", required = true)@RequestParam(value = "ifsbg", required = true) Integer ifsbg,
    		@Parameter(name = "questionid", description = "主键", required = true)@RequestParam(value = "questionid", required = true) BigDecimal questionid) {
        
        try {
			return tblNbsjQuestionService.questionStorePageISfalse(questionid, token,ifsbg);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }


	@OperationLog(
			success = "审计发现导出",
			busType = "智能审计",
			fail = "审计发现导出",
			operationType = OperationType.EXPORT,
			subType = "审计发现——列表导出"
	)
	@GetMapping("/xmzl/question_store_export")
	@Operation(summary = "审计发现-导出")
	public JsonBean defect_file_export(HttpServletRequest request, HttpServletResponse response,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjQuestionVo tblNbsjQuestionVo) throws Exception {
		JsonBean jsonBean = null;
			try {
				TblStaffUtil loginStaff = userProvider.get();
				if(loginStaff == null) {
					return ResponseFormat.retParam(0,20006,null);
				}
				//==查询当前实施的项目！
				TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
				if(tnp == null) {
					return ResponseFormat.retParam(0,30003,null);
				}
				BigDecimal projectid = tnp.getProjectId();
				log.info("审计发现-导出Excel");
				response.setContentType("application/binary;charset=UTF-8");
				response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("审计发现.xlsx"));
				ServletOutputStream outputStream = response.getOutputStream();
				List<Object[]> objList1 = new ArrayList<Object[]>();
				
				List<TblNbsjSheetEntity>   list = tblNbsjQuestionService.getQuestionStorePageList(loginStaff,tblNbsjQuestionVo,projectid);
//				String[] titles = {"所属项目名称", "底稿编号 ", "底稿名称", "被审计单位", "审计事项","审计目的","业务单元",
//						 "问题标题", "审计程序","审计备忘录","发现人","是否事实确认","是否整改"};
				
				String[] titles = {"所属项目名称", "底稿编号 ", "是否上报告", "被审计对象", "问题类型","审计发现","发现人","审计事项"};
				for (int i = 0; i < list.size(); i++) {
					TblNbsjSheetEntity o = list.get(i);
					Object[] tempob = new Object[8];
					tempob[0] = o.getProjectName();
					tempob[1] = o.getSheetCode();
					if(o.getIfsbg()==null){
						tempob[2] = "是";
					}else{
						tempob[2]=o.getIfsbg()==1?"否":"是";
					}
					tempob[3] =  tnp.getOrgIdNames();
					tempob[4] = o.getInternalType();
					tempob[5] = o.getAuditDiscoverable();
					tempob[6] = o.getRealname();
					tempob[7] = o.getBusinessAffiliation();
					objList1.add(tempob);
				}
			 ImportOrExportExcelUtil.exportExcel(titles, objList1, outputStream, null); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		return jsonBean;
	}

	
	
	/**
	 * 审计发现-发起整改
	 */
	@OperationLog(
			success = "发起整改",
			busType = "智能审计",
			fail = "发起整改",
			operationType = OperationType.DISPATCH,
			subType = "审计发现——发起整改"
	)
	@GetMapping("/sjzg/zgfp_fqStatus")
	@Operation(summary = "审计发现-发起整改")
    public JsonBean zgfp_fqStatus(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "questionid", description = "主键", required = true)@RequestParam(value = "questionid", required = true) BigDecimal questionid) {
        
        try {
			return tblNbsjQuestionService.questionStorePageFQZG(questionid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
//	/**
//	 * 审计发现-新增与修改
//	 */
//	@RequestMapping(value = "/xmzl/question_store_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
//	@Operation(summary = "审计发现-新增与修改")
//    public JsonBean question_store_add(HttpServletRequest request,@Parameter(name = "tnq", description = "实体", required = true)TblNbsjQuestionDto tnq,
//			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
//		JsonBean jsonBean = null;
//		try {
//			jsonBean = this.tblNbsjQuestionService.questionStorePageAdd(tnq,token);
//		} catch (Exception e) {
//			ResponseFormat.retParam(1,1000,e.getMessage());
//		}
//		return jsonBean;
//    }
	/**
	 * 审计发现-删除
	 */
	@OperationLog(
			success = "审计发现删除",
			busType = "智能审计",
			fail = "审计发现删除",
			operationType = OperationType.DELETE,
			subType = "审计发现——删除"
	)
	@GetMapping("/xmzl/question_store_del")
	@Operation(summary = "审计发现-删除")
    public JsonBean question_store_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "questionid", description = "主键", required = true)@RequestParam(value = "questionid", required = true) BigDecimal questionid) {
        
        try {
			return tblNbsjQuestionService.questionStorePageDelete(questionid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	
	/**
	 * 事实确认书列表
	 */
	@OperationLog(
			success = "事实确认书",
			busType = "智能审计",
			fail = "事实确认书",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取事实确认书列表"
	)
	@GetMapping("/sjss/confirmation_list")
	@Operation(summary = "事实确认书列表")
	public JsonBean confirmation_list(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjFactbookVo tblNbsjFactbookVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			
			if(null != projectId) {
				tblNbsjFactbookVo.setProjectid(projectId);
			}
			
			jsonBean = tblNbsjFactbookService.confirmationPageList(token, pageNumber, pageSize,tblNbsjFactbookVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 事实确认书-新增与修改
	 */
	@OperationLog(
			success = "事实确认书新增",
			busType = "智能审计",
			fail = "事实确认书新增",
			operationType = OperationType.ADD,
			subType = "审计实施——事实确认书"
	)
	@RequestMapping(value = "/sjss/confirmation_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "事实确认书-新增与修改")
    public JsonBean confirmation_add(HttpServletRequest request,@Parameter(name = "fb", description = "实体", required = true)TblNbsjFactbookEntity fb,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "questionIds", description = "审计发现ids", required = false)String questionIds,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjFactbookService.confirmationAdd(fb,token,questionIds,attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 事实确认书-附件列表
	 */
	@OperationLog(
			success = "事实确认书附件",
			busType = "智能审计",
			fail = "事实确认书附件",
			operationType = OperationType.SELECT,
			subType = "审计实施——获取事实确认书附件列表"
	)
    @GetMapping("/sjss/confirmation_file_list")
	@Operation(summary = "事实确认书-附件列表")
	public JsonBean confirmation_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "factid", description = "业务主键", required = true) @RequestParam(value = "factid", required = true) BigDecimal factid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.confirmationFileList(token,factid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 事实确认书-附件删除
     */
	@OperationLog(
			success = "事实确认书附件删除",
			busType = "智能审计",
			fail = "事实确认书附件删除",
			operationType = OperationType.DELETE,
			subType = "审计实施——事实确认书删除附件"
	)
    @GetMapping("/sjss/confirmation_file_del")
    @Operation(summary = "事实确认书-附件删除")
    public R confirmation_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjFactbookService.removeAttInfoByAttId(token, attId);
    }
	
	/**
	 * 事实确认书-删除
	 */
	@OperationLog(
			success = "确认书删除",
			busType = "智能审计",
			fail = "确认书删除",
			operationType = OperationType.DELETE,
			subType = "审计实施——事实确认书删除"
	)
	@GetMapping("/sjss/confirmation_del")
	@Operation(summary = "事实确认书-删除")
    public JsonBean confirmation_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "factid", description = "主键", required = true)@RequestParam(value = "factid", required = true) BigDecimal factid) {
        
        try {
			return tblNbsjFactbookService.confirmationDelete(factid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
     * 事实确认书 详情
     */
	@OperationLog(
			success = "确认书详情",
			busType = "智能审计",
			fail = "确认书详情",
			operationType = OperationType.SELECT,
			subType = "审计实施——事实确认书详情"
	)
    @GetMapping("/sjss/confirmation_detail")
    @Operation(summary = "事实确认书--详情")
    public JsonBean confirmation_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "factid", description = "主键", required = true)@RequestParam(value = "factid", required = true) BigDecimal factid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjFactbookService.findNbsjFactbookDetail(token,factid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    /**
     * 事实确认书-新建修改-添加审计发现列表
     */
	@OperationLog(
			success = "确认书新增",
			busType = "智能审计",
			fail = "确认书新增",
			operationType = OperationType.ADD,
			subType = "审计实施——事实确认书"
	)
    @GetMapping("/sjss/confirmation_question_list")
	@Operation(summary = "事实确认书-新建修改-添加审计发现列表")
	public JsonBean confirmation_question_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjQuestionVo tblNbsjQuestionVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name="projectid",description="projectid",required=false) @RequestParam(value = "projectid", required = false) BigDecimal projectid) {
		JsonBean jsonBean = null;
		try {
			tblNbsjQuestionVo.setStatus("1");
			jsonBean = tblNbsjQuestionService.confirmationQuestionStorePageList(token, pageNumber, pageSize,tblNbsjQuestionVo,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 事实确认书-查询关联审计发现信息
     */
	@OperationLog(
			success = "确认书关联审计发现",
			busType = "智能审计",
			fail = "确认书关联审计发现",
			operationType = OperationType.SELECT,
			subType = "审计实施——事实确认书-查询内部关联审计发现信息"
	)
    @GetMapping("/sjss/confirmation_question_link")
	@Operation(summary = "事实确认书-查询关联审计发现信息")
	public JsonBean confirmation_question_link(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjQuestionVo tblNbsjQuestionVo,
			@Parameter(name="factid",description="factid",required=false) @RequestParam(value = "factid", required = false) BigDecimal factid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjQuestionService.confirmationQuestionStoreLink(token, factid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	/**
	 * 我的底稿列表
	 */
	@OperationLog(
			success = "归档疑点管理",
			busType = "智能审计",
			fail = "归档疑点管理",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取项目归档疑点管理列表"
	)
	@GetMapping("/sjss/project_standard_dp_list")
	@Operation(summary = "归档疑点管理")
	public JsonBean project_standard_dp_list(HttpServletRequest request,
											@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo,
											@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
											@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
											@Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {

			if(null != projectId) {
				tblNbsjDoubtfulpointVo.setProjectid(projectId);
			}
			jsonBean = tblNbsjDoubtfulpointService.dpPageList(token, pageNumber, pageSize,tblNbsjDoubtfulpointVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	
	/**
	 * 疑点管理
	 */
	@OperationLog(
			success = "疑点列表",
			busType = "智能审计",
			fail = "疑点列表",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取疑点管理列表"
	)
	@GetMapping("/nkgj/dp/dp_list")
	@Operation(summary = "疑点管理列表")
	public JsonBean dp_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjDoubtfulpointService.dpPageList(token, pageNumber, pageSize,tblNbsjDoubtfulpointVo);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return jsonBean;
	}
	/**
	 * 疑点管理-新增与修改
	 */
	@OperationLog(
			success = "疑点新增",
			busType = "智能审计",
			fail = "疑点新增",
			operationType = OperationType.ADD,
			subType = "审计实施-获取疑点管理"
	)
	@RequestMapping(value = "/nkgj/dp_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "疑点管理-新增与修改")
    public JsonBean nkgj_projectdp_add(HttpServletRequest request,@Parameter(name = "dp", description = "实体", required = true)TblNbsjDoubtfulpointEntity dp,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjDoubtfulpointService.dpAdd(dp,token,attids);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			return  ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 疑点管理-附件列表
	 */
	@OperationLog(
			success = "疑点附件",
			busType = "智能审计",
			fail = "疑点附件",
			operationType = OperationType.SELECT,
			subType = "审计实施-获取疑点附件列表"
	)
    @GetMapping("/nkgj/dp_file_list")
	@Operation(summary = "疑点管理-附件列表")
	public JsonBean dp_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "dpointid", description = "业务主键", required = true) @RequestParam(value = "dpointid", required = true) BigDecimal dpointid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.dpFileList(token,dpointid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 疑点管理-附件删除
     */
	@OperationLog(
			success = "疑点附件删除",
			busType = "智能审计",
			fail = "疑点附件删除",
			operationType = OperationType.DELETE,
			subType = "审计实施-疑点附件删除"
	)
    @GetMapping("/nkgj/dp_file_del")
    @Operation(summary = "疑点管理-附件删除")
    public R dp_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjDoubtfulpointService.removeAttInfoByAttId(token, attId);
    }
    
	/**
	 * 疑点管理-删除
	 */
	@OperationLog(
			success = "疑点删除",
			busType = "智能审计",
			fail = "疑点删除",
			operationType = OperationType.DELETE,
			subType = "审计实施-疑点删除"
	)
	@GetMapping("/nkgj/dp_del")
	@Operation(summary = "疑点管理-删除")
    public JsonBean dp_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "dpointid", description = "主键", required = true)@RequestParam(value = "dpointid", required = true) BigDecimal dpointid) {
        
        try {
			return tblNbsjDoubtfulpointService.dpDelete(dpointid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
     * 疑点管理 详情
     */
	@OperationLog(
			success = "疑点详情",
			busType = "智能审计",
			fail = "疑点详情",
			operationType = OperationType.DELETE,
			subType = "审计实施-疑点删除"
	)
    @GetMapping("/nkgj/dp_detail")
    @Operation(summary = "疑点管理--详情")
    public JsonBean dp_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "dpointid", description = "主键", required = true)@RequestParam(value = "dpointid", required = true) BigDecimal dpointid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjDoubtfulpointService.findDPDetail(token,dpointid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    
    
    
    
    
    
    /**
     * 选择人员列表
     */
	@OperationLog(
			success = "获取人员列表",
			busType = "智能审计",
			fail = "获取人员列表",
			operationType = OperationType.SELECT,
			subType = "根据部门ID获取人员列表"
	)
    @GetMapping("/user/user_list")//user/list
	@Operation(summary = "选择人员列表")
	public JsonBean user_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "orgid", description = "部门id", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = userService.findAllPageBeanPid(token, pageNumber, pageSize,orgid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    /**
     * 选择公司列表
     */
	@OperationLog(
			success = "获取公司列表",
			busType = "智能审计",
			fail = "获取公司列表",
			operationType = OperationType.SELECT,
			subType = "根据父级部门ID获取公司列表"
	)
    @GetMapping("/user/org_list")
	@Operation(summary = "选择公司列表")
	public JsonBean org_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pid", description = "父级部门id", required = false) @RequestParam(value = "pid", required = false) BigDecimal pid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblOrganizaService.getOrgTreeListByAuditObj(token, pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    


	public void setErrorJsp(String data, HttpServletRequest request) {
		request.getSession().removeAttribute("ErrorJsp");
		request.getSession().setAttribute("ErrorJsp", data);
	}
	
	 
}
