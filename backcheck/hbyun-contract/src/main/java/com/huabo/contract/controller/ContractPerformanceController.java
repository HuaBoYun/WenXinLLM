package com.huabo.contract.controller;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractAppendixsigning;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblContractSpnode;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.service.TblContractAppendixsigningService;
import com.huabo.contract.service.TblContractPlannodeService;
import com.huabo.contract.service.TblContractSpnodeService;
import com.huabo.contract.service.TblCyhwUnitService;
import com.huabo.contract.util.HttpClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同履行控制器
 * <p>提供合同履行阶段的我的合同、变更人员、履行记录等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="合同履行Controller",description="合同履行Controller")
public class ContractPerformanceController {
	private static final Log logger = LogFactory.getLog(ContractPerformanceController.class);
	@Resource
	private TblCyhwUnitService tblCyhwUnitService;
	@Resource
	private TblContractPlannodeService tblContractPlannodeService;
	@Resource
	private TblContractSpnodeService tblContractSpnodeService;
	@Resource
	private TblContractAppendixsigningService tblContractAppendixsigningService;
	
	@Resource
	private UserProvider userProvider;

	/**
	 *我的合同-列表
	 * @param request
	 * @param unit
	 * @param flowId
	 * @param pageNumber
	 * @param pageSize
	 * @param token
	 * @param staffId
	 * @return
	 */

	@RequestMapping(value = "/contract/mineContract",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
	@Operation(summary = "我的合同-列表")
	public String mineContract(HttpServletRequest request,TblCyhwUnit unit,
									 @Parameter(name = "flowId", description = "流程Id主键", required = true)String flowId,
									 @Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
									 @Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
									 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									 @Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId,
									 @Parameter(name = "isDept", description = "是否查找当前部门下的所有合同  0-否  1-是 ", required = false)@RequestParam(value="isDept",required=false)Integer isDept){
		String result = null;
		try {
			Map<String, Object> resultMap = this.tblCyhwUnitService.findeContractListByContractStaff(flowId,pageNumber,pageSize,token, staffId,unit,isDept);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *我的合同-变更落实人列表
	 */

	@RequestMapping(value = "/getChangeContractStaffList",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	@Operation(summary = "我的合同--变更落实人列表")
	public String getChangeContractStaffList(HttpServletRequest request,
									 @Parameter(name="pageNumber",description="当前页",required=false)@RequestParam(name="pageNumber",required=false,defaultValue = "1")Integer pageNumber,
									 @Parameter(name="pageSize",description="每页数量",required=false)@RequestParam(name="pageSize",required=false,defaultValue = "15")Integer pageSize,
									 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									 @Parameter(name = "username", description = "查询条件用户账号", required = false)@RequestParam(name="username",required=false)String username,
									 @Parameter(name = "realname", description = "查询条件用户姓名", required = false)@RequestParam(name="realname",required=false)String realname){
		String result = null;
		try {
			Map<String, Object> resultMap = this.tblCyhwUnitService.getChangeContractStaffList(pageNumber,pageSize,token, username,realname);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *我的合同-变更合同落实人接口
	 */

	@RequestMapping(value = "/changeContractStaff",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
	@Operation(summary = "我的合同--变更落实人列表")
	public String changeContractStaff(HttpServletRequest request,
									 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									 @Parameter(name = "contractId", description = "合同主键", required = false)@RequestParam(name="contractId",required=false)String contractId,
									 @Parameter(name = "staffId", description = "落实人主键", required = false)@RequestParam(name="staffId",required=false)String staffId){
		String result = null;
		try {
			Map<String, Object> resultMap = this.tblCyhwUnitService.changeContractStaff(contractId,staffId,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 *履行跟踪-列表
	 * @param request
	 * @param unit
	 * @param flowId
	 * @param pageNumber
	 * @param pageSize
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/performanceTracking",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "履行跟踪-列表")
	public String contract_performanceTrackingt(HttpServletRequest request,TblCyhwUnit unit,
													  @Parameter(name = "flowId", description = "流程Id主键", required = true)String flowId,
													  @Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
													  @Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
													  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
													  @Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId){

		String result = null;
		try {
			Map<String, Object> resultMap = this.tblCyhwUnitService.findeContractListPerformanceTracking(flowId,pageNumber,pageSize,token, staffId,unit);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**履行落实-列表
	 *
	 * @param response
	 * @param request
	 * @param unit
	 * @param flowId
	 * @param pageNumber
	 * @param pageSize
	 * @param token
	 * @param staffId
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/contract/fulfillment",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "履行落实-列表")
	public String contract_fulfillment(HttpServletResponse response, HttpServletRequest request,TblCyhwUnit unit,
									   @Parameter(name = "flowId", description = "流程Id主键", required = true)String flowId,
									   @Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
									   @Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									   @Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId)throws Exception{
		String result = null;
		try {
			Map<String, Object> resultMap = this.tblCyhwUnitService.findFulfillmentContract(flowId,pageNumber,pageSize,token, staffId,unit);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 我的合同-执行
	 * @param request
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/contract/excuteContractId",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "我的合同-执行")
	@ResponseBody
	public String contract_excuteContractId(HttpServletRequest request,
											@RequestParam(value="contractId",required=true)String contractId){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblCyhwUnitService.contractExcuteContractId(contractId,"7");
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 我的合同-比对前置页面
	 * @param request
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/contractManagement/documentComparison",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "我的合同-比对前置页面")
	public String contractManagement_documentComparison(HttpServletRequest request,
														@Parameter(description = "contractId", required = true)String contractId){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblCyhwUnitService.findContractDes(contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *履行跟踪-跟踪
	 * @param model
	 * @param response
	 * @param request
	 * @param contractId
	 * @param pageNumber
	 * @param pageSize
	 * @param token
	 * @param staffId
	 * @param node
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/contract/contractTracking",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "履行跟踪-跟踪/履行落实-落实")
	public String contract_contractTracking(Model model,HttpServletResponse response,HttpServletRequest request,
											@Parameter(description = "contractId", required = false)BigDecimal contractId,
											@Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
											@Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
											@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											@Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId,
											TblContractPlannode node)throws Exception{

		String result = null;
		try {
			Map<String, Object> resultMap = this.tblContractPlannodeService.findWorkableContractNodeByPageInfo(contractId,pageNumber,pageSize,token,node);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *履行跟踪-跟踪审批回显
	 * @param planId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/contract/approvalContractPlanNode",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "履行跟踪-跟踪审批回显")
	public String contract_approvalContractPlanNode(
													@Parameter(description = "planId", required = true)BigDecimal planId)throws Exception{
		String result = null;
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		Map<String,Object> dataMap = new HashMap<String, Object>(0);
		TblContractPlannode node = this.tblContractPlannodeService.findWriteContractPlanNode(planId);
		List<TblAttachment> attList = this.tblContractPlannodeService.findeWriteContractPlanFileInfo(planId);
		dataMap.put("node",node);
		dataMap.put("attList",attList);
		resultMap.put("code", "1");
		resultMap.put("msg", "成功！");
		resultMap.put("data",dataMap);
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}

	/**
	 * 履行跟踪-审批——通过/驳回  履行落实-提交
	 * @param nodeId
	 * @param response
	 * @param request
	 * @param planStatus
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "/contract/modifyContractNodeStatus",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "履行跟踪-审批——通过/驳回")
	@ResponseBody
	public String downloadFtp_modifyContractNodeStatus(@Parameter(description = "nodeId", required = true)BigDecimal nodeId, HttpServletResponse response,HttpServletRequest request,
													 @Parameter(description = "planStatus", required = true)Integer planStatus,
		@RequestParam(name="feedback",required=false)@Parameter(name="feedback",description="feedback",required=false)String feedback) {

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblContractPlannodeService.modifyPlanNodeStatus(nodeId,planStatus,feedback);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**履行落实-落实保存
	 *
	 * @param spNode
	 * @param nodeId
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/contract/modifyContractSpNodeInfo",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "履行落实-落实保存")
	@ResponseBody
	public String contract_modifyContractSpNodeInfo(TblContractSpnode spNode,
													 @Parameter(description = "nodeId", required = true)BigDecimal nodeId,
													 @Parameter(description = "contractId", required = true)BigDecimal contractId)throws Exception{

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblContractSpnodeService.saveOrUpdateContractSpnode(spNode,nodeId,contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**履行落实-落实保存
	 *
	 * @param spNode
	 * @param nodeId
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/contract/completeSpNode",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "履行落实-落实完成")
	@ResponseBody
	public JsonBean contract_completeSpNode(HttpServletResponse response,HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(description = "nodeId", required = true)@RequestParam(value = "nodeId" , required = true)BigDecimal nodeId,
			@Parameter(description = "contractId", required = true)@RequestParam(value = "contractId" , required = true)BigDecimal contractId)throws Exception{
		JsonBean jsonBean = new JsonBean();
        jsonBean = this.tblContractSpnodeService.modifyCompleteSpNode(token,nodeId,contractId);
        return jsonBean;
	}

	/**我的合同-合同暂停/变更/终止
	 *
	 * @param request
	 * @param contractId
	 * @param goalStatus
	 * @return
	 */
	@RequestMapping(value = "/contract/changeContractStatus",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "我的合同-合同暂停/变更/终止")
	@ResponseBody
	public String contract_changeContractStatus(HttpServletRequest request,
												@RequestParam(value="contractId",required=true)String contractId,
												@RequestParam(value="goalStatus",required=true)Integer goalStatus){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblCyhwUnitService.modifyContractStatus(contractId,goalStatus);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 我的合同-附件删除列表
	 * @param contractId
	 * @param pageNumber
	 * @param pageSize
	 * @param tca
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/contract/showSealFileList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "我的合同-附件删除列表")
	public String contract_showSealFileList(@Parameter(description = "contractId", required = false)BigDecimal contractId,
											@Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
											@Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
											TblContractAppendixsigning tca)throws Exception{

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblContractAppendixsigningService.findFileListByContractIdPageInfo(contractId,pageNumber,pageSize,tca);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 我的合同-附件删除
	 * @param singingId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/contract/deleteFile",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "我的合同-附件删除")
	public String deleteFile(@RequestParam(value="singingId",required=true)BigDecimal singingId,
						   HttpServletResponse response,HttpServletRequest request) {
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			boolean bool = false;
			TblContractAppendixsigning signing = this.tblContractAppendixsigningService.findInfoById(singingId);
			/*String path = request.getSession().getServletContext().getRealPath("/WEB-INF/js/pdfview/web");
			String pdfname = signing.getSingingPath().substring(0,signing.getSingingPath().indexOf(".")-1)+".pdf";
			String pdfpath = path+"/"+pdfname;*/
			if (signing != null) {
				bool = FtpUtil.removeFile(signing.getSingingPath(), FtpUtil.constractfilepath);
				/*File pdffile = new File(pdfpath);
				pdffile.delete();
				pdffile = new File(path+signing.getSingingPath());
				pdffile.delete();*/
				this.tblContractAppendixsigningService.removeInfo(singingId);
			} else {
				logger.info("附件不存在");
			}
			if (bool) {
				logger.info("删除成功");
			} else {
				logger.info("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonBean.success();
	}

	/**
	 * 我的合同-附件上传
	 * @param file
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/contract/importFile",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
	@Operation(summary = "签署文件上传")
	public String importRiskClassExcel(@Parameter(name = "file", description = "file", required = true)MultipartFile file,
										@Parameter(description = "contractId", required = true)BigDecimal contractId,
										@Parameter(description = "budgetid", required = true)BigDecimal budgetid,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		String result = null;
		
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return JsonBean.error("用户已失效");
        }
		String fileName = file.getOriginalFilename();
		long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String oldname = fileName.substring(0,fileName.lastIndexOf("."));
		String newname=fileName.replace(oldname,""+timeInMillis);
		long size = file.getSize()/1024;
		try {
			boolean flag = FtpUtil.constractUploadFile(newname, file.getInputStream());
			if(flag){
				logger.info("上传成功");
			}else{
				logger.info("上传失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TblContractAppendixsigning signing = new TblContractAppendixsigning();
		signing.setConstractId(contractId);
		signing.setSingingName(fileName);
		signing.setSingingSize(new BigDecimal(size));
		signing.setSingingType(0);
		signing.setSingingStatus(0);
		signing.setSingingPath(newname);
		signing.setUploader(user.getStaffid());
		signing.setRealname(user.getRealname());
		signing.setUploadTime(new Date());
		Map<String,Object> resultMap = this.tblContractAppendixsigningService.saveEntity(signing,budgetid);
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 签署文件确认-确认后无法再次上传文件
	 * @param file
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/contract/qrstatus",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
	@Operation(summary = "签署文件上传确认完成")
	public String qrstatus( @Parameter(description = "contractId", required = true)BigDecimal contractId,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = this.tblCyhwUnitService.updateContractStatus(contractId.toString(), "7",null);
		JSONObject jsonObj = new JSONObject(resultMap);
		return jsonObj.toString();
	}

	
	
	/**
	 * 我的合同-附件下载
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/downloadFtp/upload",method=RequestMethod.GET,produces = "application/html; charset=utf-8")
	@Operation(summary = "签署文件下载")
	public void downloadFtp(@RequestParam(value="singingId",required=true)BigDecimal singingId, HttpServletResponse response,HttpServletRequest request) {
		try {
			TblStaffUtil user = userProvider.get();
			TblContractAppendixsigning signing = this.tblContractAppendixsigningService.findInfoById(singingId);
			if (signing != null) {
				com.hbfk.entity.TblContractAppendixsigning sing = new com.hbfk.entity.TblContractAppendixsigning();
				sing.setSingingId(singingId);
				sing.setSingingName(signing.getSingingName());
				sing.setSingingPath(signing.getSingingPath());
				FtpUtil.downUploadFileNewht(sing, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/downloadOldFile",method=RequestMethod.GET,produces = "application/html; charset=utf-8")
	@Operation(summary = "签署文件下载--历史合同")
	public String downloadOldFile(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestParam(value="singingId",required=true)BigDecimal singingId, HttpServletResponse response,HttpServletRequest request) {
		JSONObject jsonObj = new JSONObject();
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }

			TblContractAppendixsigning signing = this.tblContractAppendixsigningService.findInfoById(singingId);
			if (signing != null &&StringUtils.isNotBlank(signing.getOaattid())) {
				  String oldUrl=HttpClient.dowloadFileOaUrl+signing.getSingingPath()+"?token="+
		                	getOaToken(user.getUsername())+"&fileName="+signing.getSingingName(); 
					jsonObj.put("url", oldUrl);
			}
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//获取对方token数据
  	public String getOaToken(String username) {
  		String token=null;
  		try { 
  			 String url="";
  	            CloseableHttpClient httpClient = HttpClients.createDefault();
  	            RequestConfig requestConfig = RequestConfig.custom()
  	                    .setSocketTimeout(300 * 1000)
  	                    .setConnectTimeout(300 * 1000)
  	                    .build();
  	            url = HttpClient.oaUrl+HttpClient.getToken;
  	            HttpPost post = new HttpPost(url);
  	            post.setConfig(requestConfig);
  	            JSONObject obj=new JSONObject();
  	            obj.put("userName", HttpClient.restuname);//第三方待办主键（保证唯一）
  	            obj.put("password", HttpClient.restpassword);//为第三方配置的系统注册编码
  	           if(StringUtils.isNotBlank(username)){
	  	            obj.put("loginName", username);//为第三方配置的系统注册编码
	            }
  	            post.setHeader("Content-Type","application/json;charset=utf-8");
  	            StringEntity postingString = new StringEntity(obj.toString(),
  	                    "utf-8");
  	            post.setEntity(postingString);
  	            HttpResponse response = httpClient.execute(post);
  	            String content = EntityUtils.toString(response.getEntity());
  	            com.alibaba.fastjson.JSONObject result=com.alibaba.fastjson.JSONObject.parseObject(content);
  	            token= result.get("id").toString();
  	            System.out.println(token);
  		} catch (Exception e) {
  			e.printStackTrace();
  			log.error("获取token失败："+e.getMessage());
  			return token;
  		}
  		return token;
  	}

}
