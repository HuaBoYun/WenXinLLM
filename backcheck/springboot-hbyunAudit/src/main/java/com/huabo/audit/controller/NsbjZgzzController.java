package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.entity.Pamas;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblBeforeZgzzListEntity;
import com.huabo.audit.oracle.entity.TblRectificationIssues;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.entity.TblZgzzRctevaluation;
import com.huabo.audit.oracle.entity.TblZgzzRectificationimpl;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.entity.TblZgzzReport;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.oracle.vo.TblZgzzRctevaluationVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;
import com.huabo.audit.oracle.vo.TblZgzzReportVo;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblZgzzIssuesilistService;
import com.huabo.audit.service.TblZgzzRctevaluationService;
import com.huabo.audit.service.TblZgzzRectificationimplService;
import com.huabo.audit.service.TblZgzzRectificationplanService;
import com.huabo.audit.service.TblZgzzReportService;
import com.huabo.audit.util.SnowflakeIdWorker;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 智能审计模块拆分(审计整改)
 * @author Lenovo
 *
 */
@RestController
@Tag(name="整改追责",description="整改追责")
@RequestMapping(value = "/zgzz")
public class NsbjZgzzController {
	
	@Resource
	private TblZgzzIssuesilistService tblZgzzIssuesilistService;
	
	@Resource
	private TblZgzzRectificationplanService tblZgzzRectificationplanService;
	
	@Resource
	private TblZgzzRectificationimplService tblZgzzRectificationimplService;
	
	@Resource
	private TblZgzzRctevaluationService tblZgzzRctevaluationService;
	
	@Resource
	private TblZgzzReportService tblZgzzReportService;
	
	@Autowired
    TblAttachmentService tblAttachmentService;
	
	@Resource
    private UserProvider userProvider;
	
	private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	
	
	/**
	 * 整改方案台账
	 */
	@OperationLog(
			success = "分页查询",
			busType = "整改追责",
			fail = "分页查询",
			operationType = OperationType.SELECT,
			subType = "整改跟踪--整改方案台账分页查询"
	)
	@GetMapping("/getRectificationPlanLedger")
	@Operation(summary = "整改方案台账分页查询")
	public JsonBean getRectificationPlanLedger(HttpServletRequest request, TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			plan.setCreateStaff(loginStaff.getStaffid());
			plan.setDeptIds(loginStaff.getDeptIds());
			plan.setSelectType(4); //标识为整改方案台账查询
			jsonBean = this.tblZgzzRectificationplanService.getRectificationPlanList(loginStaff,plan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改导出",
			busType = "整改追责",
			fail = "整改导出",
			operationType = OperationType.EXPORT,
			subType = "整改跟踪--整改方案台账导出"
	)
	@GetMapping("/exportRectificationPlanLedger")
	@Operation(summary = "整改方案台账导出")
	public void exportRectificationPlanLedger(HttpServletRequest request, HttpServletResponse response, TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ;
			}
			plan.setCreateStaff(loginStaff.getStaffid());
			plan.setDeptIds(loginStaff.getDeptIds());
			jsonBean = this.tblZgzzRectificationplanService.exportRectificationPlanLedger(loginStaff,plan,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 整改方案台账结束
	 */
	
	/**
	 * 整改问题清单台账查询
	 * 	
	 */
	@OperationLog(
			success = "分页列表",
			busType = "整改追责",
			fail = "分页列表",
			operationType = OperationType.SELECT,
			subType = "整改方案--整改清单台账获取分页列表"
	)
	@GetMapping("/getRectificationIssuesLedgetList")
	@Operation(summary = "整改清单台账获取分页列表")
	public JsonBean getRectificationIssuesLedgetList(HttpServletRequest request,TblZgzzIssuesilistVo issu,
			//TblRectificationIssuesVo issues,TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
			//,@Parameter(name = "isAll", description = "是否只查看最后一次整改清单的整改信息  1-是  0-否  默认0", required = false) @RequestParam(value="isAll", required = false,defaultValue = "0") Integer isAll
			) {
		JsonBean jsonBean = null;
		try {
			/*issues.setIssues(issu);
			issues.setPlan(plan);
			jsonBean = this.tblZgzzRectificationplanService.getRectificationIssuesLedgetList(token,issues,isAll);*/
			jsonBean = this.tblZgzzIssuesilistService.getRectificationIssuesLedgetList(token,issu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "台账导出",
			busType = "整改追责",
			fail = "台账导出",
			operationType = OperationType.EXPORT,
			subType = "整改跟踪--整改方案--整改清单台账列表导出操作"
	)
	@GetMapping(value = "/exportIssuesLedgetList", produces = "application/json; charset=utf-8")
	@Operation(summary = "整改清单台账列表导出")
	public void exportIssuesLedgetList(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblZgzzIssuesilistVo issu
			//,TblRectificationIssuesVo issues,TblZgzzRectificationplanVo plan
			//,@Parameter(name = "isAll", description = "是否只导出最后一次整改清单的整改信息  1-是  0-否  默认0", required = false) @RequestParam(value="isAll", required = false,defaultValue = "0") Integer isAll
			) {
		JsonBean jsonBean = null;
		try {
			/*issues.setIssues(issu);
			issues.setPlan(plan);
			jsonBean = this.tblZgzzRectificationplanService.exportIssuesLedgetList(token,issues,response,isAll);*/
			jsonBean = this.tblZgzzIssuesilistService.exportIssuesLedgetList(token,issu,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//整改落实台账
	@OperationLog(
			success = "台账列表",
			busType = "整改追责",
			fail = "台账列表",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改清单落实台账列表内容"
	)
	@GetMapping("/getRecitfiacationImpleLedger")
	@Operation(summary = "整改清单落实台账")
	public JsonBean getRecitfiacationImpleLedger(HttpServletRequest request, HttpServletResponse response, 
		TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues,TblZgzzRectificationplanVo plan,TblZgzzRectificationimplVo reimpl,
		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = this.tblZgzzRectificationplanService.getRecitfiacationImpleLedger(loginStaff,reiss,issues,plan,response,reimpl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	//整改落实台账-整改清单
		@OperationLog(
				success = "台账导出",
				busType = "整改追责",
				fail = "台账导出",
				operationType = OperationType.EXPORT,
				subType = "整改跟踪——整改清单导出-整改清单"
		)
		@GetMapping("/exportMyRectificationList")
		@Operation(summary = "整改清单导出-整改清单")
		public void exportMyRectificationList(HttpServletRequest request, HttpServletResponse response, 
				TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues,TblZgzzRectificationplanVo plan,TblZgzzRectificationimplVo reimpl,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
			JsonBean jsonBean = null;
			try {
				//验证用户登录是否失效
				TblStaffUtil loginStaff = userProvider.get();
				if(loginStaff == null) {
					return ;
				}
				jsonBean = this.tblZgzzRectificationplanService.exportMyRectificationList(loginStaff,reiss,issues,plan,response,reimpl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//整改落实台账-问题清单
		@OperationLog(
				success = "清单列表页",
				busType = "整改追责",
				fail = "清单列表页",
				operationType = OperationType.SELECT,
				subType = "整改跟踪——整改清单导出-问题清单列表"
		)
		@GetMapping("/exportMyRectificationIssuesList")
		@Operation(summary = "整改清单导出-问题清单")
		public void exportMyRectificationIssuesList(HttpServletRequest request, HttpServletResponse response, 
				TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues,TblZgzzRectificationplanVo plan,TblZgzzRectificationimplVo reimpl,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
			JsonBean jsonBean = null;
			try {
				//验证用户登录是否失效
				TblStaffUtil loginStaff = userProvider.get();
				if(loginStaff == null) {
					return ;
				}
				jsonBean = this.tblZgzzRectificationplanService.exportMyRectificationIssuesList(loginStaff,reiss,issues,plan,response,reimpl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//整改落实台账-责任清单
		@OperationLog(
				success = "责任清单导出 ",
				busType = "整改追责",
				fail = "责任清单导出",
				operationType = OperationType.EXPORT,
				subType = "整改跟踪——整改落实台账，责任清单导出"
		)
		@GetMapping("/exportMyRectificationResponseList")
		@Operation(summary = "整改清单导出-责任清单")
		public void exportMyRectificationResponseList(HttpServletRequest request, HttpServletResponse response, 
				TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues,TblZgzzRectificationplanVo plan,TblZgzzRectificationimplVo reimpl,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
			JsonBean jsonBean = null;
			try {
				//验证用户登录是否失效
				TblStaffUtil loginStaff = userProvider.get();
				if(loginStaff == null) {
					return ;
				}
				jsonBean = this.tblZgzzRectificationplanService.exportMyRectificationResponseList(loginStaff,reiss,issues,plan,response,reimpl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	/**
	 * 整改报告开始
	 */
	@OperationLog(
			success = "整改报告修改 ",
			busType = "整改追责",
			fail = "整改报告修改",
			operationType = OperationType.ADD,
			subType = "整改跟踪——整改报告修改或保存"
	)
	@PostMapping("/saveReport")
	@Operation(summary = "整改报告修改或保存")
	public JsonBean saveReport(HttpServletRequest request,TblZgzzReport report,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="planStrs",description="选中的方案和落实显示，jsonArrray-[{\",required=false)") @RequestParam(value="planStrs", required = false) String planStrs,
			@Parameter(name = "attIds", description = "当前页面新保存的附件主键数组", required = false) @RequestParam(value="attIds", required = false) String[] attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzReportService.saveReport(token,report,planStrs,attIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	@OperationLog(
			success = "整改报告删除",
			busType = "整改追责",
			fail = "整改报告删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改报告删除"
	)
	@PostMapping("/delReport")
	@Operation(summary = "整改报告删除")
	public JsonBean delReport(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "reportid", description = "项目名称", required = true) @RequestParam(value="reportid", required = true) String reportid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzReportService.removeReport(token,reportid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改报告附件删除",
			busType = "整改追责",
			fail = "整改报告附件删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改报告删除附件关系"
	)
	@PostMapping("/delReportFile")
	@Operation(summary = "整改报告删除附件关系")
	public JsonBean delReportFile(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "reportid", description = "整改报告主键", required = true) @RequestParam(value="reportid", required = true) String reportid,
			@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value="attId", required = true) String attId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzReportService.removeReportFile(token,reportid,attId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改报告详情",
			busType = "整改追责",
			fail = "整改报告详情",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改报告获取详情信息"
	)
	@GetMapping("/getReportDetail")
	@Operation(summary = "整改报告获取详情信息接口")
	public JsonBean getReportDetail(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "reportid", description = "整改清单主键", required = true) @RequestParam(value="reportid", required = true) String reportid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzReportService.getReportDetail(token,reportid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改报告列表页",
			busType = "整改追责",
			fail = "整改报告列表页",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改报告列表页获取接口"
	)
	@GetMapping("/getReportList")
	@Operation(summary = "整改报告列表页获取接口")
	public JsonBean getReportList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblZgzzReportVo report) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzReportService.getReportList(token,report);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "会议纪要列表",
			busType = "整改追责",
			fail = "会议纪要列表",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——会议纪要文件列表查询"
	)
	@GetMapping("/getReportMeetFileList")
    @Operation(summary = "整改报告-会议纪要文件列表")
    public JsonBean getReportMeetFileList(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="reportid") @RequestParam("reportid")String reportid) throws Exception {
    	JsonBean jsonBean =null;
        try {
        	jsonBean=tblZgzzReportService.getReportMeetFileList(token, reportid);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "获取失败", null);
		}
        return jsonBean;
    }

	@OperationLog(
			success = "会议纪要上传",
			busType = "整改追责",
			fail = "会议纪要上传",
			operationType = OperationType.UPLOAD,
			subType = "整改跟踪——上传会议纪要文件"
	)
	@RequestMapping(value="/importReportMeetFile",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
	@Operation(summary = "整改报告-上传会议纪要文件")
	public String importReportMeetFile(@Parameter(name = "file", description = "file", required = true)MultipartFile file,
										@Parameter(description = "reportid", required = true) @RequestParam("reportid")String reportid,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			  return ResponseFormat.retParam(0, 20006, null).toString();
		}
		 String attPath = "";
		 JsonBean jsonBean =null;
		try {
			TblAttachment tblAttachmentEntity = new TblAttachment();
			 InputStream inputStream = file.getInputStream();
             long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
             String fileName = new String(file.getOriginalFilename().getBytes()); //重新编码
             String name=fileName.substring(fileName.lastIndexOf("."), fileName.length());
             attPath = FtpUtil.uploadFilePath(imageName+name+"", inputStream);
             if (StrUtil.isEmpty(attPath)) {
            	 return ResponseFormat.retParam(0, "上传会议文件失败", null).toString();
             }
             tblAttachmentEntity.setAttpath(attPath + imageName+name);
             tblAttachmentEntity.setAttsize(file.getSize() / 1024);
             tblAttachmentEntity.setUploadtime(new Date());
             tblAttachmentEntity.setUploader(user.getRealname());
             tblAttachmentEntity.setAttname(fileName);
             tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
             tblAttachmentService.saveEntity(tblAttachmentEntity);
             jsonBean = this.tblZgzzReportService.uploadattbyid(reportid, tblAttachmentEntity.getAttid().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean.toString();
	}

	@OperationLog(
			success = "会议纪要上传",
			busType = "整改追责",
			fail = "会议纪要上传",
			operationType = OperationType.UPLOAD,
			subType = "整改跟踪——上传会议纪要文件"
	)
	@RequestMapping(value="/saveReportMeetFileR",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
	@Operation(summary = "整改报告-上传会议纪要文件")
	public String importReportMeetFile(@Parameter(description = "attid", required = true) @RequestParam("attid")String attid,
										@Parameter(description = "reportid", required = true) @RequestParam("reportid")String reportid,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			  return ResponseFormat.retParam(0, 20006, null).toString();
		}
		 JsonBean jsonBean =null;
		try {
			jsonBean = this.tblZgzzReportService.uploadattbyid(reportid, attid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean.toString();
	}

	@OperationLog(
			success = "会议纪要删除",
			busType = "整改追责",
			fail = "会议纪要删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——删除会议纪要文件"
	)
	@PostMapping("/deleteReportMeetFile")
    @Operation(summary = "整改报告-删除会议纪要文件")
    public JsonBean deleteReportMeetFile(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attid") @RequestParam("attid")String attid) throws Exception {
    	JsonBean jsonBean =null;
        try {
        	jsonBean=tblZgzzReportService.delattbyid(attid, token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "删除失败", null);
		}
        return jsonBean;
    }

	@OperationLog(
			success = "类型查询",
			busType = "整改追责",
			fail = "类型查询",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改方案分页通过整改报告类型查询"
	)
	@GetMapping("/getRectificationPlanByReportType")
	@Operation(summary = "整改方案分页通过整改报告类型查询")
	public JsonBean getRectificationPlanByReportType(HttpServletRequest request, TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "reporttype", description = "报告类型 1-整改方案报告，2整改落实报告", required = true) @RequestParam(value="reporttype", required = true) Integer reporttype) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			jsonBean = this.tblZgzzRectificationplanService.getRectificationPlanListByReportType(loginStaff,plan,reporttype);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "获取整改清单信息内容",
			busType = "整改追责",
			fail = "获取整改清单信息内容",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改报告，选择方案后获取整改清单内容信息"
	)
	@GetMapping("/getRectificationIssuesListByReportType")
	@Operation(summary = "整改报告，选择方案后获取整改清单内容信息【{{planIdStrs}}】")
	public JsonBean getRectificationIssuesListByReportType(HttpServletRequest request,TblZgzzIssuesilistVo iv,
			TblRectificationIssuesVo riv,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "reporttype", description = "报告类型 1-整改方案报告，2整改落实报告", required = true) @RequestParam(value="reporttype", required = true) Integer reporttype,
			@Parameter(name = "planIdStrs", description = "整改方案主键用逗号拼接", required = true) @RequestParam(value="planIdStrs", required = true) String planIdStrs) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			riv.setIssues(iv);
			jsonBean = this.tblZgzzRectificationimplService.getRectificationIssuesListByReportType(loginStaff,riv,reporttype,planIdStrs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 整改报告结束
	 */
	
	/**
	 * 未销号问题查看 
	 */
	@OperationLog(
			success = "未销号问题列表",
			busType = "整改追责",
			fail = "未销号问题列表",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——未销号问题列表页获取"
	)
	@GetMapping("/getUnresolvedIssuesList")
	@Operation(summary = "未销号问题  列表页获取")
	public JsonBean getUnresolvedIssuesList(HttpServletRequest request, TblRectificationIssuesVo issues,TblZgzzIssuesilistVo issu,
			TblZgzzRctevaluationVo rcval,TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			issu.setStatus(9);
			issues.setIssues(issu);
			issues.setValua(rcval);
			issues.setPlan(plan);
			jsonBean = this.tblZgzzRectificationplanService.getUnresolvedIssuesList(token,issues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 未销号问题二次发起整改 
	 */
	@OperationLog(
			success = "二次整改",
			busType = "整改追责",
			fail = "二次整改",
			operationType = OperationType.UPDATE,
			subType = "整改跟踪——对未销号问题再次发起整改"
	)
	@GetMapping("/issuesRectificationAgain")
	@Operation(summary = "未销号问题 再次发起整改")
	public JsonBean issuesRectificationAgain(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId
			) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.issuesRectificationAgain(token,issuesId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	
	
	/**
	 * 后续整改查看 
	 */
	@OperationLog(
			success = "后续整改查看",
			busType = "整改追责",
			fail = "后续整改查看",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——后续整改——查看后续整改列表页"
	)
	@GetMapping("/gethxzgList")
	@Operation(summary = "后续整改查看")
	public JsonBean gethxzgList(HttpServletRequest request, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues,TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getMyhxzgList(token,reiss,issues,plan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}



	@OperationLog(
			success = "后续整改保存",
			busType = "整改追责",
			fail = "后续整改保存",
			operationType = OperationType.UPDATE,
			subType = "整改跟踪——后续整改-保存落实信息"
	)
	@PostMapping("/saveHxRectificationImpl")
	@Operation(summary = "后续整改-保存落实信息")
	public JsonBean saveHxRectificationImpl(HttpServletRequest request,TblZgzzRectificationimpl impl,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attIds", description = "新上传的附件主键数组", required = false) @RequestParam(value="attIds", required = false) String[] attIds) {
		JsonBean jsonBean = null;
		try {
			impl.setStatus(0);
			impl.setZcstatus(1);
			jsonBean = this.tblZgzzRectificationimplService.saveRectificationImpl(token,impl,attIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	/**
	 * 整改落实- 转发人员列表
	 */
	@OperationLog(
			success = "获取转发人员列表",
			busType = "整改追责",
			fail = "获取转发人员列表",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改落实-获取转发人员列表信息"
	)
	@GetMapping("/getzfUserlist")
	@Operation(summary = "整改落实- 转发人员列表")
	public JsonBean getzfUserlist(HttpServletRequest request, 
			@Parameter(name = "relaId", description = "关系表主键", required = true) @RequestParam(value="relaId", required = true) String relaId,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			try {
				jsonBean = this.tblZgzzRectificationplanService.getbyUserlist(token, relaId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	/**
	 * 整改评价 接口开始
	 */
	@OperationLog(
			success = "整改评价列表",
			busType = "整改追责",
			fail = "整改评价列表",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改评价列表页信息"
	)
	@GetMapping("/getRectificationValuation")
	@Operation(summary = "整改评价-分页列表")
	public JsonBean getRectificationValuation(HttpServletRequest request, TblZgzzRectificationplanVo plan, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			/*TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			plan.setCreateStaff(loginStaff.getStaffid());//整改评价 列表只能评价自己创建的方案
			plan.setStatus(9); //查询开始整改的方案
			plan.setSelectType(3); //标识为整改评价查询
			jsonBean = this.tblZgzzRectificationplanService.getRectificationPlanList(loginStaff,plan);*/
			
			try {
				//jsonBean = this.tblZgzzRectificationplanService.getMyRectificationList(token,reiss,issues,plan);
				jsonBean = this.tblZgzzRectificationplanService.getRectificationValuation(token,reiss,issues,plan);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改评价保存",
			busType = "整改追责",
			fail = "整改评价保存",
			operationType = OperationType.ADD,
			subType = "整改跟踪——整改评价，保存、修改整改评价信息"
	)
	@PostMapping("/saveRectificationValuation")
	@Operation(summary = "整改评价，保存、修改整改评价信息")
	public JsonBean saveRectificationValuation(HttpServletRequest request,TblZgzzRctevaluation valua,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attIds", description = "新上传的附件主键数组", required = false) @RequestParam(value="attIds", required = false) String[] attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRctevaluationService.saveRectificationImpl(token,valua,attIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "评价状态修改",
			busType = "整改追责",
			fail = "评价状态修改",
			operationType = OperationType.UPDATE,
			subType = "整改跟踪——整改评价，全部评价完成后，修改整改方案状态"
	)
	@PostMapping("/completeRectificationEval")
	@Operation(summary = "整改评价，全部评价完成后，修改整改方案状态")
	public JsonBean completeRectificationEval(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "整改方案主键", required = true) @RequestParam(value="planId", required = true) String planId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.completeRectificationEval(token,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "评价附件删除",
			busType = "整改追责",
			fail = "评价附件删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改评价，删除附件"
	)
	@PostMapping("/removeRectiValuaAttRela")
	@Operation(summary = "整改评价，删除附件关系")
	public JsonBean removeRectiValuaAttRela(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value="attId", required = true) String attId,
			@Parameter(name = "evalId", description = "整改评价主键", required = true) @RequestParam(value="evalId", required = true) String evalId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRctevaluationService.removeRectiValuaAttRela(token,attId,evalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "评价详情获取",
			busType = "整改追责",
			fail = "评价详情获取",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改评价根据主键获取详情"
	)
	@GetMapping("/getZgzzeEvaluationDetail")
	@Operation(summary = "整改评价-根据主键获取详情")
	public JsonBean getZgzzeEvaluationDetail(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "evalId", description = "整改评价主键", required = true) @RequestParam(value="evalId", required = true) String evalId) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			jsonBean = this.tblZgzzRctevaluationService.getZgzzeEvaluationDetail(loginStaff,evalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 整改评价接口结束
	 */
	
	/**
	 * 我的整改（整改落实）相关接口 开始
	 */
	@OperationLog(
			success = "整改分页信息获取",
			busType = "整改追责",
			fail = "整改分页信息获取",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——我的整改 ，列表分页数据"
	)
	@GetMapping("/getMyRectificationList")
	@Operation(summary = "我的整改 ，列表分页数据")
	public JsonBean getMyRectificationList(HttpServletRequest request, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues,TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getMyRectificationList(token,reiss,issues,plan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改信息保存",
			busType = "整改追责",
			fail = "整改信息保存",
			operationType = OperationType.ADD,
			subType = "整改跟踪——我的整改 ，保存落实信息"
	)
	@PostMapping("/saveRectificationImpl")
	@Operation(summary = "我的整改-保存落实信息")
	public JsonBean saveIssuesRelaPlan(HttpServletRequest request,TblZgzzRectificationimpl impl,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attIds", description = "新上传的附件主键数组", required = false) @RequestParam(value="attIds", required = false) String[] attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationimplService.saveRectificationImpl(token,impl,attIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改附件删除",
			busType = "整改追责",
			fail = "整改附件删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——我的整改-落实信息-删除附件"
	)
	@PostMapping("/removeRectificationImplAtt")
	@Operation(summary = "我的整改-落实信息-删除附件")
	public JsonBean removeRectificationImplAtt(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "implId", description = "落实信息主键", required = true) @RequestParam(value="implId", required = true) String implId,
			@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value="attId", required = true) String attId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationimplService.removeRectificationImplAtt(token,implId,attId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改落实详情",
			busType = "整改追责",
			fail = "整改落实详情",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——我的整改 获取落实信息详情"
	)
	@GetMapping("/getRectificationImplDetailInfo")
	@Operation(summary = "我的整改 获取落实信息详情")
	public JsonBean getRectificationImplDetailInfo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "implId", description = "方案清单关系表主键", required = true) @RequestParam(value="implId", required = true) String implId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationimplService.getRectificationImplDetailInfo(token,implId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	/**
	 * 我的整改（整改落实）相关接口结束
	 */
	
	
	/**
	 * 整改分派 相关接口开始
	 */
	@OperationLog(
			success = "分派列表查询",
			busType = "整改追责",
			fail = "分派列表查询",
			operationType = OperationType.SELECT,
			subType = "整改跟踪-整改分派列表分页查询"
	)
	@GetMapping("/getRectificationAllocationList")
	@Operation(summary = "整改分派列表分页查询")
	public JsonBean getRectificationAllocationList(HttpServletRequest request, TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			plan.setResponse(loginStaff.getStaffid());//设置整改方案的查询条件整改责任人 意为整改分派查询 查询自己的分派方案
			plan.setSelectType(2); //标识为整改分派查询
			jsonBean = this.tblZgzzRectificationplanService.getRectificationPlanList(loginStaff,plan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "列表页获取",
			busType = "整改追责",
			fail = "列表页获取",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改分派 ,整改评价_列表页获取整改清单列表分页"
	)
	@GetMapping("/getRectificationAllocationIssuesList")
	@Operation(summary = "整改分派 ,整改评价  列表页获取整改清单列表分页")
	public JsonBean getRectificationAllocationIssuesList(HttpServletRequest request, TblRectificationIssuesVo issues,TblZgzzIssuesilistVo issu,
			TblZgzzRctevaluationVo rcval,TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			issues.setIssues(issu);
			issues.setValua(rcval);
			issues.setPlan(plan);
			jsonBean = this.tblZgzzRectificationplanService.getRectificationAllocationIssuesList(token,issues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "落实人保存",
			busType = "整改追责",
			fail = "落实人保存",
			operationType = OperationType.UPDATE,
			subType = "整改跟踪——整改分派 选择整改落实人 后 保存整改落实人信息"
	)
	@PostMapping("/saveIssuesRelaImpementer")
	@Operation(summary = "整改分派 选择整改落实人 后 保存整改落实人信息")
	public JsonBean saveIssuesRelaImpementer(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "impementerId", description = "选中的整改落实人主键", required = true) @RequestParam(value="impementerId", required = true) BigDecimal impementerId,
			@Parameter(name = "relaId", description = "关系表主键", required = true) @RequestParam(value="relaId", required = true) String relaId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.saveIssuesRelaImpementer(token,impementerId,relaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 整改分派相关接口结束
	 */
	
	/**
	 * 整改方案 相关接口 开始
	 */
	@OperationLog(
			success = "通知查询",
			busType = "整改追责",
			fail = "通知查询",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改通知分页查询"
	)
	@GetMapping("/getRectificationPlanList")
	@Operation(summary = "整改方案分页查询")
	public JsonBean getRectificationPlanList(HttpServletRequest request, TblZgzzRectificationplanVo plan,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			jsonBean = this.tblZgzzRectificationplanService.getRectificationPlanList(loginStaff,plan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改详情",
			busType = "整改追责",
			fail = "整改详情",
			operationType = OperationType.SELECT,
			subType = "整改跟踪--整改方案获取详情"
	)
	@GetMapping("/getRectificationPlanDetail")
	@Operation(summary = "整改方案获取详情接口")
	public JsonBean getRectificationPlanDetail(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "项目名称", required = true) @RequestParam(value="planId", required = true) String planId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getRectificationPlanDetail(token,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单数据详情",
			busType = "整改追责",
			fail = "整改清单数据详情",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——获取关联整改清单所有相关数据详情"
	)
	@GetMapping("/getIssuesAllDetailInfo")
	@Operation(summary = "获取关联整改清单所有相关数据详情")
	public JsonBean getIssuesDetailByPlan(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "relaId", description = "方案清单关系表主键", required = true) @RequestParam(value="relaId", required = true) String relaId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getIssuesDetailByPlan(token,relaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单数据详情",
			busType = "整改追责",
			fail = "整改清单数据详情",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——获取关联整改清单所有相关数据详情"
	)
	@GetMapping("/getIssuesAllDetailInfoByPlanIssuesId")
	@Operation(summary = "获取关联整改清单所有相关数据详情")
	public JsonBean getIssuesAllDetailInfoByPlanIssuesId(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "整改方案主键", required = true) @RequestParam(value="planId", required = true) String planId,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getIssuesAllDetailInfoByPlanIssuesId(token,planId,issuesId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	@OperationLog(
			success = "业务表数据保存",
			busType = "整改追责",
			fail = "业务表数据保存",
			operationType = OperationType.ADD,
			subType = "整改跟踪——整改方案与整改清单关系业务表中的数据进行保存"
	)
	@PostMapping("/saveIssuesRelaPlan")
	@Operation(summary = "保存整改方案与整改清单关系业务表中的数据")
	public JsonBean saveIssuesRelaPlan(HttpServletRequest request,TblRectificationIssues rela,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "orgIds", description = "承办部门组织数组", required = false) @RequestParam(value="orgIds", required = false) String[] orgIds,
			@Parameter(name = "attIds", description = "附件ID主键数组", required = false) @RequestParam(value="attIds", required = false) String[] attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.saveIssuesRelaPlan(token,rela,orgIds,attIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "方案附件删除",
			busType = "整改追责",
			fail = "方案附件删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改方案清单中间关系表删除附件关系"
	)
	@PostMapping("/delPlanIssuesFile")
	@Operation(summary = "整改方案清单中间关系表删除附件关系")
	public JsonBean delPlanIssuesFile(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "relaId", description = "整改方案清单中间表主键", required = true) @RequestParam(value="relaId", required = true) String relaId,
			@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value="attId", required = true) String attId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.removePlanIssuesFile(token,relaId,attId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "状态获取",
			busType = "整改追责",
			fail = "状态获取",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改方案获取当前状态"
	)
	@GetMapping("/getRectificationStatus")
	@Operation(summary = "整改方案获取当前状态")
	public JsonBean getRectificationStatus(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "整改方案主键", required = true) @RequestParam(value="planId", required = true) String planId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getRectificationStatus(token,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "方案删除",
			busType = "整改追责",
			fail = "方案删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改方案——删除了整改方案"
	)
	@PostMapping("/delRectification")
	@Operation(summary = "整改方案删除")
	public JsonBean delRectification(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "项目方案主键", required = true) @RequestParam(value="planId", required = true) String planId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.removeRectification(token,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "方案附件删除",
			busType = "整改追责",
			fail = "方案附件删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改方案——删除了整改方案的附件"
	)
	@PostMapping("/delRectificationFile")
	@Operation(summary = "整改方案删除附件关系")
	public JsonBean delRectificationFile(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "项目方案主键", required = true) @RequestParam(value="planId", required = true) String planId,
			@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value="attId", required = true) String attId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.removeRectificationFile(token,planId,attId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "关联清单删除",
			busType = "整改追责",
			fail = "关联清单删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——删除了整改方案关联的整改清单"
	)
	@PostMapping("/delRectificationIssues")
	@Operation(summary = "整改方案删除关联的整改清单")
	public JsonBean delRectificationIssues(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planId", description = "项目名称", required = true) @RequestParam(value="planId", required = true) String planId,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.removeRectificationIssues(token,planId,issuesId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}



	@OperationLog(
			success = "自动编号",
			busType = "整改追责",
			fail = "自动编号",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——自动生成编号"
	)
	    @GetMapping(value = "/code/findAutoNumber")
	    @Operation(summary = "编号自动生成格式  ：编号-公司代码-年度-四位流水号")
	    public JsonBean findAutoNumber(HttpServletResponse response,
	                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                                   @Parameter(name="tblName",description="tblName") @RequestParam(value = "tblName") String tblName,
	                                   @Parameter(name="column",description="column") @RequestParam(value = "column") String column,
	                                   @Parameter(name="orgCol",description="orgCol") @RequestParam(value = "orgCol") String orgCol,
	                                   @Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId) throws Exception {
	        TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
	        String flowNextId = null;
	        try {
	            flowNextId = tblZgzzRectificationplanService.findFlowNextId(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId,
	                    null, null, null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        response.reset();
	        return ResponseFormat.retParam(1, 200, flowNextId);
	    }
	
	/**
	 * @param request
	 * @param token
	 * @param rectification
	 * @return
	 */
	@OperationLog(
			success = "通知修改/保存",
			busType = "整改追责",
			fail = "通知修改/保存",
			operationType = OperationType.ADD,
			subType = "整改跟踪--整改通知修改或保存"
	)
	@PostMapping("/saveRectificationPlan")
	@Operation(summary = "整改方案修改或保存")
	public JsonBean saveRectificationPlan(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody TblZgzzRectificationplan rectification
			) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.saveRectificationPlan(token,rectification);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "通知修改/保存",
			busType = "整改追责",
			fail = "通知修改/保存",
			operationType = OperationType.ADD,
			subType = "整改跟踪--整改通知修改或保存"
	)
	@PostMapping("/modifyRectificationPlanStatus")
	@Operation(summary = "整改方案修改或保存")
	public JsonBean modifyRectificationPlanStatus(HttpServletRequest request,TblZgzzRectificationplan rectification,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.modifyRectificationPlanStatus(token,rectification);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	@OperationLog(
			success = "项目列表获取",
			busType = "整改追责",
			fail = "项目列表获取",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——通过整改方案新增、修改获取了项目列表信息"
	)
	@GetMapping("/getSolutionProjectList")
	@Operation(summary = "整改方案新增、修改获取项目列表接口")
	public JsonBean getSolutionProjectList(HttpServletRequest request,TblZgzzProjectVo project,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "planType", description = "方案类别 1-审计 2-风控 3-外部", required = true) @RequestParam(value = "planType", required = true) Integer planType) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzRectificationplanService.getSolutionProjectList(token,project,planType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单获取",
			busType = "整改追责",
			fail = "整改清单获取",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——整改方案新增、修改，选择方案后获取整该方案下的所有整改清单"
	)
	@GetMapping("/getAfterProjectIssues")
	@Operation(summary = "整改方案新增、修改，选择方案后获取整该方案下的所有整改清单")
	public JsonBean getAfterProjectIssues(HttpServletRequest request,TblZgzzIssuesilistVo issues,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getAfterProjectIssues(token,issues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 整改方案 相关接口 结束
	 */
	
	/**
	 * 整改清单相关接口 
	 */
	@OperationLog(
			success = "整改清单新增内控问题",
			busType = "整改追责",
			fail = "整改清单新增内控问题",
			operationType = OperationType.ADD,
			subType = "整改跟踪——整改清单新增/修改获取内控问题接口"
	)
	@GetMapping("/problemLedgerList")
	@Operation(summary = "整改清单新增/修改获取内控问题接口")
	public JsonBean problemLedgerList(HttpServletRequest request,TblTesttaskProblemFind problem,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getProblemLedgerList(token,problem,pageNumber,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单新增审计问题",
			busType = "整改追责",
			fail = "整改清单新增审计问题",
			operationType = OperationType.ADD,
			subType = "整改跟踪——整改清单新增/修改获取审计问题接口"
	)
	@GetMapping("/summaryAuditList")
	@Operation(summary = "整改清单新增/修改获取审计问题接口")
	public JsonBean summaryAuditList(HttpServletRequest request,TBlNbsjSheetVo tBlNbsjSheetVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getSummaryAudit_list(token,tBlNbsjSheetVo,pageNumber,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单新增审计来源问题",
			busType = "整改追责",
			fail = "整改清单新增审计来源问题",
			operationType = OperationType.ADD,
			subType = "整改跟踪——整改清单新增/修改审计来源获取被审计对象"
	)
	@GetMapping("/auditedObjectList")
	@Operation(summary = "整改清单新增/修改审计来源获取被审计对象")
	public JsonBean auditedObjectList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "sheetId", description = "底稿主键", required = false) @RequestParam(value = "sheetId", required = false) BigDecimal sheetId,
			@Parameter(name = "projectId", description = "项目主键", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId,
			@Parameter(name = "auditOrgId", description = "被审计单位主键", required = false) @RequestParam(value = "auditOrgId", required = false) BigDecimal auditOrgId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getAuditedObjectList(token,sheetId,auditOrgId,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	@OperationLog(
			success = "整改清单列表",
			busType = "整改追责",
			fail = "整改清单列表",
			operationType = OperationType.SELECT,
			subType = "问题汇总——查询整改清单列表页信息"
	)
	@GetMapping("/getIssuesList")
	@Operation(summary = "整改清单列表页获取接口")
	public JsonBean getIssuesList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblZgzzIssuesilistVo issues) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getIssuesList(token,issues);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "清单导出",
			busType = "整改追责",
			fail = "清单导出",
			operationType = OperationType.EXPORT,
			subType = "问题汇总——整改清单列表导出"
	)
	@GetMapping(value = "/exportIssuesList", produces = "application/json; charset=utf-8")
	@Operation(summary = "整改清单列表导出")
	public void exportIssuesList(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblZgzzIssuesilistVo issues) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.exportIssuesList(token,issues,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OperationLog(
			success = "整改清单详情",
			busType = "整改追责",
			fail = "整改清单详情",
			operationType = OperationType.SELECT,
			subType = "问题汇总——整改清单获取详情信息"
	)
	@GetMapping("/getIssuesDetail")
	@Operation(summary = "整改清单获取详情信息接口")
	public JsonBean getIssuesDetail(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getIssues(token,issuesId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单修改",
			busType = "整改追责",
			fail = "整改清单修改",
			operationType = OperationType.ADD,
			subType = "整改跟踪-整改清单修改或保存"
	)
	@PostMapping("/saveIssues")
	@Operation(summary = "整改清单修改或保存")
	public JsonBean saveIssues(HttpServletRequest request,TblZgzzIssuesilist issues,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectName", description = "项目名称", required = false) @RequestParam(value="projectName", required = false) String projectName,
			@Parameter(name = "projectNo", description = "项目编号", required = false) @RequestParam(value="projectNo", required = false) String projectNo,
			@Parameter(name = "attIds", description = "当前页面新保存的附件主键数组", required = false) @RequestParam(value="attIds", required = false) String[] attIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.saveIssues(token,issues,projectName,projectNo,attIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单状态修改",
			busType = "整改追责",
			fail = "整改清单状态修改",
			operationType = OperationType.DELETE,
			subType = "整改跟踪-整改清单修改状态"
	)
	@GetMapping("/modifyIssuesStatus")
	@Operation(summary = "整改清单修改状态")
	public JsonBean modifyIssuesStatus(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId,
			@Parameter(name = "status", description = "整改清单状态 7-整改中 ，8-整改完成，9-未销号问题 、 10-再次整改 , 11-关闭", required = true) @RequestParam(value="status", required = true) Integer status) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.modifyIssuesStatus(token,issuesId,status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改清单删除",
			busType = "整改追责",
			fail = "整改清单删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪-删除整改清单"
	)
	@PostMapping("/delIssues")
	@Operation(summary = "整改清单删除")
	public JsonBean delIssues(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.removeIssues(token,issuesId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "清单附件删除",
			busType = "整改跟追责",
			fail = "清单附件删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——整改清单除附件"
	)
	@PostMapping("/delIssuesFile")
	@Operation(summary = "整改清单删除附件关系")
	public JsonBean delIssuesFile(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "issuesId", description = "整改清单主键", required = true) @RequestParam(value="issuesId", required = true) String issuesId,
			@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value="attId", required = true) String attId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.removeIssuesFile(token,issuesId,attId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 整改清单相关接口 -- 结束 
	 */
//	("/saveIssues")
	//整改清单 ——Excel 导入
	@OperationLog(
			success = "清单导入",
			busType = "整改追责",
			fail = "清单导入",
			operationType = OperationType.IMPORT,
			subType = "整改跟踪——通过整改清单导入功能进行数据新增"
	)
	@PostMapping("import")
	@Operation(summary = "整改清单——导入")
	public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
							   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	) throws IOException {
		InputStream in = file.getInputStream();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
		try {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				tblZgzzIssuesilistService.resolveSheet(sheet, token);
			}
			return ResponseFormat.retParam(1, 200);
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("整改追责——整改清单 列表导入失败", e);
			return ResponseFormat.retParam(0, 1000, e.getMessage());
		} finally {
			// 读取完毕则关闭流
			in.close();
			workbook.close();
		}
	}


	//往期整改清单导入
	@OperationLog(
			success = "往期整改清单导入",
			busType = "整改追责",
			fail = "往期整改清单导入",
			operationType = OperationType.IMPORT,
			subType = "整改跟踪——通过往期整改清单的导入功能进行对往期整改清单进行数据新增"
	)
	@PostMapping("importBefore")
	@Operation(summary = "往期整改清单——导入")
	public JsonBean importBefore(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
							   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	) throws IOException {
		InputStream in = file.getInputStream();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
		try {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				if (sheet == null){
					break;
				}
				String result = tblZgzzIssuesilistService.resolveSheetBefore(sheet, token);
				if(!result.equals("完成")){
					return ResponseFormat.retParam(0, result, result);
				}
			}
			return ResponseFormat.retParam(1, 200);
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("整改追责——往期整改清单 列表导入失败", e);
			return ResponseFormat.retParam(0, 1000, e.getMessage());
		} finally {
			// 读取完毕则关闭流
			in.close();
			workbook.close();
		}
	}

	//往期整改清单查询接口
	@OperationLog(
			success = "往期整改清单查询",
			busType = "整改追责",
			fail = "往期整改清单查询",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——获取整改跟踪——往期整改清单列表页内容"
	)
	@GetMapping("/getBeforeList")
	@Operation(summary = "往期整改清单查询接口")
	public JsonBean getBeforeList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblBeforeZgzzListEntity tblBeforeZgzzListEntity,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize

	){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.getbeforeZgzzList(token,tblBeforeZgzzListEntity,pageNumber,pageSize);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@OperationLog(
			success = "往期整改清单导出",
			busType = "整改追责",
			fail = "往期整改清单导出",
			operationType = OperationType.EXPORT,
			subType = "整改跟踪——往期整改清单导出-整改清单"
	)
	@GetMapping("/exportBeforeZgList")
	@Operation(summary = "往期整改清单导出-整改清单")
	public void exportBeforeZgList(HttpServletRequest request, HttpServletResponse response, 
			TblBeforeZgzzListEntity tblBeforeZgzzListEntity,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ;
			}
			jsonBean = this.tblZgzzIssuesilistService.exportBeforeZgList(loginStaff,tblBeforeZgzzListEntity,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OperationLog(
			success = "往期整改清单列表页",
			busType = "整改追责",
			fail = "往期整改清单列表页",
			operationType = OperationType.SELECT,
			subType = "整改跟踪——往期整改清单导出-问题清单列表"
	)
	@GetMapping("/exportBeforeIssuesList")
	@Operation(summary = "往期整改清单导出-问题清单")
	public void exportBeforeIssuesList(HttpServletRequest request, HttpServletResponse response, 
			TblBeforeZgzzListEntity tblBeforeZgzzListEntity,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ;
			}
			jsonBean = this.tblZgzzIssuesilistService.exportBeforeIssuesList(loginStaff,tblBeforeZgzzListEntity,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//整改落实台账-责任清单
	@OperationLog(
			success = "往期过往清单责任清单导出 ",
			busType = "整改追责",
			fail = "往期过往清单责任清单导出",
			operationType = OperationType.EXPORT,
			subType = "整改跟踪——往期过往清单台账，责任清单导出"
	)
	@GetMapping("/exportBeforeResponseList")
	@Operation(summary = "整改清单导出-责任清单")
	public void exportBeforeResponseList(HttpServletRequest request, HttpServletResponse response, 
			TblBeforeZgzzListEntity tblBeforeZgzzListEntity,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			//验证用户登录是否失效
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ;
			}
			jsonBean = this.tblZgzzIssuesilistService.exportBeforeResponseList(loginStaff,response,tblBeforeZgzzListEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@OperationLog(
			success = "往期整改清单删除",
			busType = "整改追责",
			fail = "往期整改清单删除",
			operationType = OperationType.DELETE,
			subType = "整改跟踪——删除往期整改清单内容"
	)
	@GetMapping("/deleteBeforeList")
	@Operation(summary = "批量删除往期整改清单")
	public JsonBean deleteBeforeList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ids", description = "删除的id集合", required = true) @RequestParam(value = "ids", required = true) String ids
	){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.deleteBeforeList(token,ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200);
	}

	@OperationLog(
			success = "导出当前疑点",
			busType = "智能审计",
			fail = "导出当前疑点",
			operationType = OperationType.EXPORT,
			subType = "审计实施—疑点管理-导出当前疑点"
	)
	@GetMapping(value = "/exportYdgl", produces = "application/json; charset=utf-8")
	@Operation(summary = "导出当前疑点")
	public void exportYdgl(HttpServletRequest request, HttpServletResponse response,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 Pamas pamas) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblZgzzIssuesilistService.importdealSendDoubtful(token,pamas,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
