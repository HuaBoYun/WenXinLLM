package com.huabo.contract.controller;


import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractContentPdf;
import com.huabo.contract.entity.TblContractExamFile;
import com.huabo.contract.entity.TblContractInformation;
import com.huabo.contract.entity.TblContractLend;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblContractTran;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.service.TblAttachmentService;
import com.huabo.contract.service.TblContractContentPdfService;
import com.huabo.contract.service.TblContractExamFileService;
import com.huabo.contract.service.TblContractInformationService;
import com.huabo.contract.service.TblContractLendService;
import com.huabo.contract.service.TblContractPlannodeService;
import com.huabo.contract.service.TblContractTypeofService;
import com.huabo.contract.service.TblCyhwProjectbudgetService;
import com.huabo.contract.service.TblCyhwUnitService;
import com.huabo.contract.service.TblFlowService;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import com.huabo.contract.util.HttpClient;
import com.huabo.contract.util.Tree;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同管理控制器
 * <p>提供合同管理的动态报表、借阅状态检查、合同统计等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="合同管理Controller",description="合同管理Controller")
public class ContractManageController {

	private static final String GROUP_STRUCTURE = PropertyFileReader.getItem("group.structure");
	
	@Resource
	private TblCyhwUnitService tblCyhwUnitService;
	
	@Resource
	private TblContractTypeofService contractTypeofService;

	@Resource
	private TblFlowService tblFlowService;
 
	@Resource
	private TblOrganizaService tblOrganizaService;

	@Resource
	private TblStaffService tblStaffService;

	@Resource
	public TblContractInformationService tblContractInformationService;

	@Resource
	private TblContractPlannodeService tblContractPlannodeService;

	@Resource
	public TblAttachmentService attachmentService;

	@Resource
	private TblContractLendService tblContractLendService;

	@Resource
	private TblCyhwProjectbudgetService tblCyhwProjectbudgetService;

	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Resource
	private TblContractContentPdfService tblContractContentPdfService;

	@Resource
	private TblContractExamFileService tblContractExamFileService;
	
	@Resource
	private UserProvider userProvider;


	
	/**
	 * 合同分析动态报表查询
	 * @param request
	 * @param contractId
	 * @return
	 */
	@Operation(summary = "合同分析动态报表查询")
	@RequestMapping(value = "/contract/dynamicReport",method = {RequestMethod.GET})
	public JsonBean contract_dynamicReport(
			HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "筛选条件年份", required = false)@RequestParam(value="year",required=false)Integer year,
			@Parameter(name = "quarte", description = "筛选条件季度，1-第一季度，2-第二季度，3-第三季度，4-第四季度", required = false)@RequestParam(value="quarte",required=false)Integer quarte,
			@Parameter(name = "indexYtype", description = "Y轴指标类型，1-合同金额，2-合同数量，3-两个都查询", required = true)@RequestParam(value="indexYtype",required=true)Integer indexYtype,
			@Parameter(name = "indexXtype", description = "x轴维度类型，1-合同类型，2-承办部门，3-承办人，4-单位，5-签订起止日期", required = true)@RequestParam(value="indexXtype",required=true)Integer indexXtype,
			@Parameter(name = "contractTypeText", description = "X轴维度-合同类型文本，可以选择查询合同类型文本，为空默然查询所有", required = false)@RequestParam(value="contractTypeText",required=false)String contractTypeText,
			@Parameter(name = "contractDeptIds", description = "X轴维度-承办部门主键，用逗号拼接，为空默认查询当前公司下的所有部门", required = false)@RequestParam(value="contractDeptIds",required=false)String contractDeptIds,
			@Parameter(name = "contractStaffIds", description = "X轴维度-承办人主键，用逗号拼接，为空默认查询当前公司下的所有承办人", required = false)@RequestParam(value="contractStaffIds",required=false)String contractStaffIds,
			@Parameter(name = "contractOrgIds", description = "X轴维度-单位主键，用逗号拼接，为空默认查询当前公司及其下属单位", required = false)@RequestParam(value="contractOrgIds",required=false)String contractOrgIds,
			@Parameter(name = "startDate", description = "X轴维度-起始日期，传入示例：YYYY-MM-DD，为空默认当前年份1月1号", required = false)@RequestParam(value="startDate",required=false)String startDate,
			@Parameter(name = "endDate", description = "X轴维度-结束日期，传入示例：YYYY-MM-DD，为空默认当前年份12月31号", required = false)@RequestParam(value="endDate",required=false)String endDate)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean =  this.tblCyhwUnitService.dynamicReport(token, indexYtype,indexXtype,contractTypeText,contractDeptIds,contractStaffIds,contractOrgIds,startDate,endDate,year,quarte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 核验借阅状态--需在借阅期内并且审核状态通过
	 * @param request
	 * @param contractId
	 * @return
	 */
	@Operation(summary = "获取合同借阅状态")
	@RequestMapping(value = "/contract/checkLendStatus",method = {RequestMethod.GET})
	public JsonBean checkLendStatusA(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "contractId", description = "借阅合同Id", required = true)@RequestParam(value="contractId",required=true)String contractId){
		
		JsonBean jsonBean = null;
		try {
			jsonBean =  this.tblCyhwUnitService.checkLendStatus(token, contractId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 合同范本列表
	 * @param request
	 * @param pageNumber
	 * @param cyhwUnit
	 * @return
	 */
	@RequestMapping(value = "/cwgl/zcgl_mainck",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本列表")
	public String zcgl_mainck(HttpServletRequest request,
		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		@RequestParam(name="flowId",required=true)@Parameter(name = "flowId", description = "流程Id主键", required = true)String flowId,
		@RequestParam(name="staffId",required=false)@Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId,
		@RequestParam(name="pageNumber",required=false,defaultValue = "1")@Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
		@RequestParam(name="pageSize",required=false,defaultValue = "10")@Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
		@RequestParam(name="cateId",required=false)@Parameter(name="cateId",required=false)String cateId,
		@RequestParam(name="isFlowdb",required=false)@Parameter(name="isFlowdb",required=false)String isFlowdb,
		@RequestParam(name="number",required=false)@Parameter(name="number",required=false)String number,
		@RequestParam(name="lawyerpersion",required=false)@Parameter(name="lawyerpersion",required=false)String lawyerpersion,
		@RequestParam(name="xdfaddress",required=false)@Parameter(name="xdfaddress",required=false)String xdfaddress,
		@Parameter(name = "contractdept", description = "承办部门查询主键", required = false)@RequestParam(value="contractdept",required=false)BigDecimal contractdept,
									TblCyhwUnit cyhwUnit){
		String result = null;//
		try {
			cyhwUnit.setContractdept(contractdept);
			Map<String,Object>  resultMap = this.tblCyhwUnitService.findListContractBiangengBypageInfo(token,flowId,staffId,pageNumber,pageSize,cyhwUnit);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 合同范本-新建合同类型
	 * @param request
	 * @param flowId 流程Id主键
	 * @param flowname
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/cyhw/choosetContractType",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本-新建合同类型")
	public String choosetContractType(HttpServletRequest request,
											@Parameter(description = "flowid", required = false) BigDecimal flowId,
											@Parameter(description = "flowname", required = false) BigDecimal flowname,
											@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											@Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.contractTypeofService.findAllListToCreateContract(token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**合同范本-新建页面
	 *
	 * @param request
	 * @param flowId
	 * @param typeId
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/cyhw/cyhwUnitAdd",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本-新建页面")
	public String cyhwUnitAdd(HttpServletRequest request,@Parameter(description = "flowid", required = true)BigDecimal flowId,
									@Parameter(description = "typeId", required = true)BigDecimal typeId,
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									@Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId,
									@Parameter(name="contractNo",description="合同编号，关联合同时候传入",required=false) String contractNo){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.contractTypeofService.toAddContract(flowId,typeId,token,contractNo);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同范本、合同订立、合同变更-新建修改-保存
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "/cyhwUnitSave",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本-新建修改-保存")
	public String cyhwUnitSave(HttpServletRequest request,
							   @Parameter(name = "flowId", description = "流程主键Id", required = false)@RequestParam(value="flowId",required=false)String flowId,
							   @Parameter(name="startdate",required=false)String startdate,
							   @Parameter(name="enddate",required=false)String enddate,
							   @Parameter(name="changedate",required=false)String changedate,
							   @Parameter(name = "contractxdf", description = "相对方信息json字符串格式", required = false)String contractxdf,
							   @Parameter(name = "attids", description = "附件Id数组", required = false)String attids,
							   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							   @RequestParam(name="goalStatus",required=false)@Parameter(name = "goalStatus", description = "合同即将改变的状态", required = false)Integer goalStatus,
							   TblCyhwUnit tcu){
		String result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			tcu.setCreatetime(new Date());
			if(startdate != null && !"".equals(startdate)) {
				tcu.setStartdate(sdf.parse(startdate));
			}
			if(enddate != null && !"".equals(enddate)) {
				tcu.setEnddate(sdf.parse(enddate));
			}
			if(changedate != null && !"".equals(changedate)) {
				tcu.setChangedate(sdf.parse(changedate));
			}
			Map<String,Object> resultMap = this.tblCyhwUnitService.saveCyhwUnit(tcu,attids,contractxdf,token,flowId,goalStatus);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			result = "-1";
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *合同范本-修改查询状态
	 * @param request
	 * @param contractId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCyhwUnitStatue",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本-修改查询状态")
	public String getCyhwUnitStatue(HttpServletRequest request,
									@Parameter(description = "contractId", required = true)BigDecimal contractId){
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Integer statue = tblCyhwUnitService.getStatueById(contractId);
			if(statue!=null && statue==1){
				return JsonBean.error("流程审批中");
			}else if((statue!=null && statue==4) ||(statue!=null && statue==5) || (statue!=null && statue==6)){
				return JsonBean.error("流程已完成");
			}else if(statue != null && statue == 7) {
				return JsonBean.error("合同正在执行，无法进行此项操作");
			}else if(statue != null && statue == 8) {
				return JsonBean.error("已归档");
			}else if(statue != null && statue == 9) {
				return JsonBean.error("已暂停");
			}else if(statue != null && statue == 10) {
				return JsonBean.error("已变更，请前往合同变更进行此项操作");
			}else if(statue != null && statue == 11) {
				return JsonBean.error("已终止");
			}else if(statue != null && statue == 12) {
				return JsonBean.error("纠纷中");
			}else if(statue != null && statue == 13) {
				return JsonBean.error("协商中");
			}else if(statue != null && statue == 14) {
				return JsonBean.error("诉讼中");
			}else if(statue != null && statue == 15) {
				return JsonBean.error("仲裁中");
			}else{
				return JsonBean.success();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return JsonBean.error("失败");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getCyhwUnitStatueBG",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本-修改查询状态")
	public String getCyhwUnitStatueBG(HttpServletRequest request,
									@Parameter(description = "contractId", required = true)BigDecimal contractId){
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Integer statue = tblCyhwUnitService.getStatueById(contractId);
			if(statue!=null && statue==1){
				//return JsonBean.error("流程审批中");
				return JsonBean.errors();
			}else if((statue!=null && statue==4) ||(statue!=null && statue==5) || (statue!=null && statue==6)){
				return JsonBean.errors();
			}else if(statue != null && statue == 7) {
				return JsonBean.errors();
			}else if(statue != null && statue == 8) {
				return JsonBean.errors();
			}else if(statue != null && statue == 9) {
				return JsonBean.errors();
			}else if(statue != null && statue == 10) {
				return JsonBean.errors();
			}else if(statue != null && statue == 11) {
				return JsonBean.errors();
			}else if(statue != null && statue == 12) {
				return JsonBean.errors();
			}else if(statue != null && statue == 13) {
				return JsonBean.errors();
			}else if(statue != null && statue == 14) {
				return JsonBean.errors();
			}else if(statue != null && statue == 15) {
				return JsonBean.errors();
			}else{
				return JsonBean.success();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return JsonBean.error("失败");
		}
	}

	/**
	 * 合同范本-删除
	 * @param request
	 * @param contractId 合同主键
	 * @param flowId
	 * @return
	 */
	@RequestMapping(value = "/cyhwUnitDelete",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本/合同订立-删除")
	public String cyhwUnitDelete(HttpServletRequest request,
									   @Parameter(description = "contractId", required = true)BigDecimal contractId,
									   @Parameter(description = "flowId", required = false)BigDecimal flowId){
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwUnitService.removeCyhwUnit(contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同范本-合同类型列表-右
	 * @param request
	 * @param pageNumber
	 * @param pageSize
	 * @param choiceTypeName 合同类型名称
	 * @param typeId 合同类型主键
	 * @param token
	 * @param staffId
	 * @return
	 */

	@RequestMapping(value = "/contract/typeOfContractList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同范本-合同类型列表-右")
	public String typeOfContractList(HttpServletRequest request,
										   @Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
										   @Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
									 		@Parameter(name = "choiceTypeName", description = "合同类型名称", required = false)String choiceTypeName,
										   @Parameter(name = "typeId", description = "合同类型主键", required = false)String typeId,
										   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										   @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.contractTypeofService.findPageInfoList(pageNumber,pageSize,typeId,choiceTypeName,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同范本-合同类型-左侧列表
	 * @param choiceSearch
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/contractTypeFather", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "合同范本-合同类型-左侧列表")
	public @ResponseBody String contract_contractTypeFather(@RequestParam(value="choiceSearch",required=false)String choiceSearch,
															@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
															@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String json = "";
		try {
			List<Tree> list;
			list = this.contractTypeofService.getFatherTree();//staff.getCurrentOrg().getOrgid()
			for (Tree tree : list) {
				tree.setTarget("mainFramex");
				tree.setUrl("/cyhw/contract/contractList?pid=&choiceSearch=" +choiceSearch);
				for (Tree ct : tree.getChildren()) {
					ct.setTarget("mainFramex");
					ct.setUrl("/cyhw/contract/contractList?pid=" + ct.getId() +"&choiceSearch=" +choiceSearch);
				}
			}
			json = JSONObject.toJSONString(list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * 合同范本-合同类型-新建保存
	 * @param request
	 * @param
	 * @param parentid  父级id
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/saveContractType", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "合同范本-合同类型-新建保存")
	public @ResponseBody String contract_saveContractType(HttpServletRequest request,
														  @Parameter(name="typeNameArr",required=true) String[] typeName,
														  @Parameter(name="fatherId",required=true) BigDecimal parentid,
														  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
														  @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId){

		String result = null;
		try {
			Map<String,Object>  resultMap = this.contractTypeofService.saveContractType(typeName,parentid,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同范本-合同类型-修改
	 * @param request
	 * @param typeId  合同类型主键
	 * @param typeName 合同类型名称
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/modifyContractType", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "合同范本-合同类型-修改")
	public @ResponseBody String contract_modifyContractType(HttpServletRequest request,
															@Parameter(description = "typeId", required = true) String typeId,
															@Parameter(description = "typeName", required = true) String typeName,
															@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
															@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId){
		String result = null;
		try {

			Map<String,Object>  resultMap = this.contractTypeofService.modifyContractType(token,staffId,typeId,typeName);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同范本-合同类型-删除
	 * @param request
	 * @param typeId 合同类型主键
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/removeContractType", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "合同范本-合同类型-删除")
	public @ResponseBody String contract_removeContractType(HttpServletRequest request,
															@RequestParam(value="typeId",required=true) String typeId,
															@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
															@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.contractTypeofService.removeContractType(typeId,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同订立-新建相对方信息选择按钮
	 * @param request
	 * @param pageNumber
	 * @param pageSize
	 * @param recordType
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/findHtInfo/windowOpen",method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-新建相对方信息选择按钮")
	public String findHtInfo(HttpServletRequest request,
			@Parameter(name="pageNumber",description="当前页",required=false)@RequestParam(name= "pageNumber" ,required = false ,defaultValue = "1")Integer pageNumber,
			@Parameter(name="pageSize",description="每页数量",required=false)@RequestParam(name= "pageSize" ,required = false ,defaultValue = "20")Integer pageSize,
			@Parameter(name = "recordType", description = "recordType", required = true)String recordType,
			@Parameter(name = "budgetname", description = "budgetname", required = false)String budgetname,
			@Parameter(name = "counterpartno", description = "counterpartno", required = false)String counterpartno,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.findListWindowOpenByPageInfo(pageNumber,pageSize,recordType,token,staffId,budgetname,counterpartno);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同订立-新建执行单位选择按钮
	 * @param response
	 * @param token
	 * @param staffId
	 */
	@RequestMapping(value = "/organ/csfa_findOrganizationByTree",method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-新建执行单位选择按钮")
	public void csfa_findOrganizationByTree(HttpServletResponse response,
											@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId) {
		boolean is = false;
		if(StringUtils.isNotBlank(GROUP_STRUCTURE)){
			is = false;
		}
		String orgtree = tblOrganizaService.findOrgByAllGSJT(token,staffId);
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write("[" + orgtree + "]");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	/**
	  * 合同订立-新建执行部门选择按钮
	  * @param request
	  * @param response
	  * @param str
	  * @param token
	  * @param staffId
	  */
	 @RequestMapping(value = "/findOrganizationByTreeNbkz", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	 @Operation(summary = "合同订立-新建执行部门选择按钮")
	 public JsonBean findOrganizationByTreeNukz(HttpServletRequest request, HttpServletResponse response,
	   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		  JsonBean jsonBean = null;
		  try {
		   jsonBean =  this.tblOrganizaService.findOrgDeptTree(token);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  return jsonBean;
	 }

	/**
	 * 合同订立-新建执行人选择左侧按钮
	 * @param nodeId
	 * @param type
	 * @param orgId
	 * @param idname
	 * @param textname
	 * @param token
	 * @param staffId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/htdl/findOrganizationByTreeAllss", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-新建执行人选择左侧按钮")
	public @ResponseBody String htdlfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId", required = false)BigDecimal nodeId,
														   @Parameter(name = "type", description = "type", required = false)String type,
														   @Parameter(name = "orgId", description = "orgId", required = false)BigDecimal orgId,
														   @Parameter(name = "idname", description = "idname", required = false)String idname,
														   @Parameter(name = "textname", description = "textname", required = false)String textname,
														   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
														   @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId,
														   HttpServletRequest request) throws Exception {
		
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return JsonBean.error("用户已失效");
        }

		String json = "";
		if (null == nodeId) {
			nodeId = orgId;
			if (null == orgId) {
				
				nodeId = user.getCurrentOrg().getOrgid();
			}
		}
		if (StringUtils.isNotBlank(type)) {
			List<Tree> list = this.tblOrganizaService.getTrees(nodeId);
			for (Tree tree : list) {
				if (!tree.getIsParent()) {
					tree.setTarget("mainFramex");
					tree.setUrl("/nbkz/pjlx/list?pid=" + tree.getId() + "&idname=" + idname + "&textname=" + textname);
				}
			}
			json = JSONObject.toJSONString(list);
		} else {
			List<Tree> list = this.tblOrganizaService.getNodeAlls(nodeId);
			for (Tree tree : list) {
				setUrlByTree(tree, "/nbkz/pjlx/list?idname=" + idname + "&textname=" + textname + "&pid=");
			}
			json = JSONObject.toJSONString(list);
		}
		return json;
	}

	private void setUrlByTree(Tree tree, String url) {
		for (Tree tre : tree.getChildren()) {
			/// if (!tre.getIsParent()) {
			tre.setTarget("mainFramex");
			tre.setUrl(url + tre.getId());
			// }
			if (tre.getChildren().size() > 0) {
				setUrlByTree(tre, url);
			}
		}
	}

	/**
	 *合同订立-新建执行人选择右侧按钮
	 * @param request
	 * @param pid
	 * @param pageNumber
	 * @param pageSize
	 * @param token
	 * @param staffId
	 * @return
	 */

	@RequestMapping(value = "/htdl/list", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-新建执行人选择右侧按钮")
	public String htdluserListss(HttpServletRequest request,
									   @Parameter(name = "pid", description = "pid", required = true)String pid,
									   @Parameter(description="pageNumber",required=false)Integer pageNumber,
									   @Parameter(description="pageSize",required=false)Integer pageSize,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									   @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblStaffService.findAllPageBeanPid(pid,pageNumber,pageSize,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**合同订立-新建立项信息选择按钮
	 *
	 * @param request
	 * @param pageNumber
	 * @param pageSize
	 * @param formId
	 * @param jsonArry
	 * @param paramArry
	 * @param eleName
	 * @param valueId
	 * @param queryType
	 * @param oldFormId
	 * @param textname
	 * @return
	 */
	@RequestMapping(value = "/form/chooseFormValueElePage", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-新建立项信息选择按钮")
	public String form_chooseFormValueElePage(HttpServletRequest request,
													@Parameter(description="pageNumber",required=false) Integer pageNumber,
													@Parameter(description="pageSize",required=false) Integer pageSize,
													@Parameter(description = "formId", required = false) BigDecimal formId,
													@Parameter(description = "jsonArry", required = false) String jsonArry,
													@Parameter(description = "paramArry", required = false) String paramArry,
													@Parameter(description = "eleName", required = false) String eleName,
													@Parameter(description = "valueId", required = false) String valueId,
													@Parameter(description = "queryType", required = false) Integer queryType,
													@Parameter(description = "oldFormId", required = false) BigDecimal oldFormId,
											  		@Parameter(description = "eleName", required = false) String textname){


		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblStaffService.findHttpClient(formId,jsonArry,paramArry,eleName,valueId,pageNumber,pageSize,queryType,oldFormId,textname);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**合同订立-运输货物信息-保存
	 *
	 * @param request
	 * @param information
	 * @return
	 */
	@RequestMapping(value = "/contract/information_toSave", method = {RequestMethod.POST})
	@Operation(summary = "合同订立-运输货物信息-保存")
	@ResponseBody
	public String information_toSave(HttpServletRequest request,TblContractInformation information,
									 @Parameter(description = "contractid", required = true)BigDecimal contractid){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractInformationService.saveContractInfoMation(contractid,information);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同订立-运输货物信息-删除
	 * @param request
	 * @param infoId 货物主键
	 * @return
	 */
	@RequestMapping(value = "/contract/information_toRemove", method = {RequestMethod.POST})
	@Operation(summary = "合同订立-运输货物信息-删除")
	@ResponseBody
	public String information_toRemove(HttpServletRequest request,
									   @Parameter(description = "infoId", required = true)BigDecimal infoId){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractInformationService.removeContractInfoMation(infoId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**合同订立-合同阶段信息-新增/修改
	 *
	 * @param request
	 * @param node
	 * @param contractid
	 * @param jbunitid
	 * @param jbstaffid
	 * @return
	 */
	@RequestMapping(value = "/contract/plan_toSave", method = {RequestMethod.POST})
	@Operation(summary = "合同订立-合同阶段信息-新增/修改")
	@ResponseBody
	public String plan_toSave(HttpServletRequest request,TblContractPlannode node,
							  @Parameter(description = "contractid", required = true)BigDecimal contractid,
							  @Parameter(description = "jbunitid", required = true)BigDecimal jbunitid,
							  @Parameter(description = "jbstaffid", required = true)BigDecimal jbstaffid){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractPlannodeService.saveContractPlannode(node,contractid,jbunitid,jbstaffid);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同订立-合同阶段信息-回显
	 * @param request
	 * @param nodeId
	 * @return
	 */
	@RequestMapping(value = "/contract/plan_toModify", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-合同阶段信息-回显")
	public String plan_toModify(HttpServletRequest request,@Parameter(description = "nodeId", required = true)@RequestParam(value="nodeId",required=true)BigDecimal nodeId){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractPlannodeService.selectById(nodeId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同订立-合同阶段信息-删除
	 * @param request

	 * @return
	 */
	@RequestMapping(value = "/contract/plan_toRemove", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-合同阶段信息-删除")
	@ResponseBody
	public String plan_toRemove(HttpServletRequest request,@Parameter(description = "nodeId", required = true)BigDecimal nodeId){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractPlannodeService.removeContractPlannode(nodeId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同订立-附件删除
	 * @param request
	 * @param attid 附件主键
	 * @return
	 */
	@RequestMapping(value = "/contract/deleteFileRelation", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-附件删除")
	public @ResponseBody String deleteFileRelation(HttpServletRequest request,
												   @Parameter(description = "attid", required = true)BigDecimal attid){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.attachmentService.removeAttachmentByattId(attid);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/contract/deleteAttachmentByBedgetId", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同用印-附件删除")
	public @ResponseBody String deleteAttachmentByBedgetId(HttpServletRequest request,
												   @Parameter(description = "attid", required = true)BigDecimal attid){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object> resultMap = this.attachmentService.removeAttachmentByattId(attid);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**合同订立-定时提醒
	 *
	 * @param request
	 * @param tcu
	 * @param content
	 * @param date
	 * @param token
	 * @param staffId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/contract/addTipContract", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同订立-定时提醒")
	@ResponseBody
	public String addTipContract(HttpServletRequest request,TblCyhwUnit tcu,
								 @Parameter(description = "tipcontent", required = true)String content,
								 @Parameter(description = "tipdate", required = true)String date,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId) throws Exception{


		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblContractPlannodeService.sendTipEmail(tcu,content,date,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**合同订立/合同变更-提交审批 验证合同履行阶段金额
	 *
	 * @param request
	 * @param contractId
	 * @param examination
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/htgl/checkStageInfo",method = {RequestMethod.GET},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同订立/合同变更-提交审批验证合同履行阶段金额")
	public @ResponseBody String checkStageInfo(HttpServletRequest request,
		@Parameter(name = "contractId", description = "合同主键", required = true) @RequestParam(value="contractId",required=true) BigDecimal contractId,
		@Parameter(name = "token", description = "登录用户token", required = true)@RequestHeader("token") String token){

		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.checkStageInfo(contractId,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 合同订立/合同变更-详情
	 * @param request
	 * @param flowId
	 * @param flowname
	 * @param contractId
	 * @param cflag
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contractToDetail",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同订立/合同变更-详情")
	public String contractToDetail(HttpServletRequest request,
			@RequestParam(value="flowId",required=false) @Parameter(description = "flowId", required = false)BigDecimal flowId,
			@RequestParam(value="flowname",required=false)@Parameter(description = "flowname", required = false)String flowname,
										 @Parameter(description = "contractId", required = true)BigDecimal contractId,
			@RequestParam(value="cflag",required=false)@Parameter(description = "cflag", required = false)Integer cflag,
										 @Parameter(name = "token", description = "登录用户token  前端必须传", required = false) @RequestHeader("token") String token,
			@Parameter(name = "staffId", description = "登录用户主键", required = false)@RequestParam(value="flowname",required=false)String staffId,
			@RequestParam(value="goalStatus",required=false) @Parameter(description = "合同状态", required = false)Integer goalStatus){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.findeContractInfo(flowId,flowname,contractId,cflag,token,staffId,goalStatus);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同-货物详情
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/contractToInfomationDetail",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同-货物详情")
	public String contractToInfomationDetail(@Parameter(description = "contractId", required = true)BigDecimal contractId){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractInformationService.findInformationListById(contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同-阶段详情
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/contractToPlanNodeDetail",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同-阶段详情")
	public String contractToPlanNodeDetail(@Parameter(description = "contractId", required = false)@RequestParam(value="contractId",required=false)BigDecimal contractId){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblContractPlannodeService.findPlannodeListById(contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同-附件详情
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/contractToAttachmentDetail",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同-附件详情")
	public String contractToAttachmentDetail(@Parameter(description = "contractId", required = true)BigDecimal contractId ){

		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblAttachmentService.findAttachmentListById(contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}



	/**
	 * 合同订立-引用合同范本
	 * @param request
	 * @param contractType
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/contract/findContractTemplate",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同订立-引用合同范本")
	@ResponseBody
	public String contract_findContractTemplate(HttpServletRequest request,
												@Parameter(description = "contractType", required = true)String contractType,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
		String result = "";
		try {
			List<TblCyhwUnit> unilList = this.tblCyhwUnitService.findContractTemp(token,contractType);
			result = JSON.toJSONString(unilList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同用印-列表
	 * @param request
	 * @param unit
	 * @param pageNumber
	 * @param pageSize
	 * @param cateId
	 * @param flowId
	 * @param isFlowdb
	 * @param choose
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/constract/contractSeal",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同用印-列表")
	public  String constract_contractSeal(HttpServletRequest request, TblCyhwUnit unit,
										 	 @Parameter(description="pageNumber",required=false)Integer pageNumber,
										  	@Parameter(description="pageSize",required=false)Integer pageSize,
												@Parameter(name = "cateId", description = "cateId", required = false)String cateId,
												@Parameter(name = "flowId", description = "flowId", required = true)String flowId,
												@Parameter(name = "isFlowdb", description = "isFlowdb", required = false)String isFlowdb,
												@Parameter(name = "condition", description = "condition", required = false)String choose,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
												@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.findContractSealList(pageNumber,pageSize,cateId,flowId,isFlowdb,choose,token,staffId,unit);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@RequestMapping(value = "/projectrBudgetToModify",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同用印-用印回显")
	public String projectrBudgetToModify(HttpServletRequest request,
											   @Parameter(description = "flowid", required = true)BigDecimal flowId,
											   @Parameter(description = "budgetId", required = true)BigDecimal budgetId){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.projectrBudgetToModify(flowId,budgetId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同用印-用印新增页面
	 * @param request
	 * @param flowId
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/projectrBudgetToAdd",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同用印-用印新增页面")
	public String projectrBudgetAdd(HttpServletRequest request,
										  @Parameter(description = "flowid", required = true)BigDecimal flowId,
										  @Parameter(description = "contractId", required = true)BigDecimal contractId){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.projectrBudgettoAdd(flowId,contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 合同用印-用印保存
	 * @param request
	 * @param flowId
	 * @param attids
	 * @param token
	 * @param tcpb
	 * @return
	 */
	@RequestMapping(value = "/projectrBudgetModify",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同用印-用印保存")
	@ResponseBody
	public String projectrBudgetModify(HttpServletRequest request,
									   @Parameter(name = "flowid", description = "流程信息主键", required = true)String flowId,
									   @Parameter(name = "createtime", description = "用印日期", required = true)String createtime,
									   @Parameter(name = "recordparent", description = "合同主键ID", required = false)String contractid,
									   @Parameter(name = "attids", description = "附件ID数组", required = false)String attids,
									   @Parameter(name = "sealorgid", description = "印章所属主体id", required = true)BigDecimal sealorgid,
									   @Parameter(name = "singingId", description = "签署文件id", required = false)BigDecimal singingId,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									   TblCyhwProjectbudget tcpb){
		String result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(createtime != null && !"".equals(createtime)) {
				tcpb.setCreatetime(sdf.parse(createtime));
			}else{
				tcpb.setCreatetime(new Date());
			}
			if(contractid != null && !"".equals(contractid)){
				tcpb.setRecordparent(new BigDecimal(contractid));
			}
			Map<String,Object> resultMap = this.tblCyhwProjectbudgetService.insertOrUpdateBybudget(tcpb,token,attids,flowId,sealorgid,singingId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		return result;
	}

	/**
	 * 合同归档列表
	 * @param request
	 * @param unit
	 * @param pageNumber
	 * @param pageSize
	 * @param flowId
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/constract/filContractList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同归档列表")
	public String filContractList(HttpServletRequest request,TblCyhwUnit unit,
			@RequestParam(value="pageNumber",required=false,defaultValue = "1")@Parameter(description="pageNumber",required=false)Integer pageNumber,
			@RequestParam(value="pageSize",required=false,defaultValue = "20")@Parameter(description="pageSize",required=false)Integer pageSize,
			@RequestParam(value="flowId",required=true)@Parameter(name = "flowId", description = "flowId", required = true)String flowId,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestParam(value="staffId",required=false)@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.findeFilContractList(pageNumber,pageSize,flowId,token,staffId,unit);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	 /**
     * 合同归档 获取自动生成归档编号
     *
     * @param request
     * @param contractId
     * @param bindno
     * @return
     */
    @RequestMapping(value = "/contract/generateNo", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = " 合同归档 获取自动生成归档编号")
    @ResponseBody
    public JsonBean contract_generateNo(HttpServletRequest request,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblCyhwUnitService.generateNo(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
	
	
	/**
	 * 合同借阅  返回合同借阅的合同集合
	 * @return
	 */
	@RequestMapping(value = "/chooseLendContractList",method = {RequestMethod.GET},produces = "application/html; charset=utf-8")
	@Operation(summary = "获取合同借阅选择的合同集合数据")
	public String chooseLendContractList(HttpServletRequest request,TblCyhwUnit unit,
										@Parameter(description="pageNumber",required=false)Integer pageNumber,
										@Parameter(description="pageSize",required=false)Integer pageSize,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
		JsonBean jsonBean =  this.tblCyhwUnitService.chooseLendContractList(pageNumber,pageSize,token,unit);
		
		return jsonBean.toString();
	}
	
	
	

	/**
	 * 合同变更-撤回
	 * @param request
	 * @param contractId
	 * @param flowname
	 * @return
	 */
	@RequestMapping(value = "/cyhw/updBasicUnitinspectionStatue",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同变更-撤回")
	@ResponseBody
	public String updBasicUnitinspectionStatue(HttpServletRequest request,
											   @RequestParam(value="contractId",required=true)BigDecimal contractId,
											   @RequestParam(value="flowname",required=true)String flowname){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwUnitService.updBasicUnitinspectionStatue(contractId,flowname);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 合同归档-归档
	 * @param request
	 * @param contractId
	 * @return
	 */
	@RequestMapping(value = "/contract/contract_filStatus",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同归档-归档")
	@ResponseBody
	public String contract_filStatus(HttpServletRequest request,
									 @RequestParam(value="contractId",required=true)String contractId,
									 @RequestParam(value = "bindno", required = false) String bindno){
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwUnitService.updateContractStatus(contractId,"8",bindno);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**合同归档-借阅保存
	 *
	 * @param request
	 * @param lend
	 * @param contractId
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/saveLeadInfo",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同归档-借阅保存")
	@ResponseBody
	public String contract_saveLeadInfo(HttpServletRequest request,TblContractLend lend,
										@Parameter(description = "contractId", required = false)String contractId,
										@Parameter(description = "lenddate", required = true)String lenddate,
										@Parameter(description = "returndate", required = true)String returndate,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										@Parameter(name = "staffId", description = "登录用户Id", required = false)String staffId) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblContractLendService.saveTblContractLead(contractId,lenddate,returndate,token,staffId,lend);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	@RequestMapping(value = "/contract/getLeadInfo",method = {RequestMethod.GET},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同归档-获取借阅详情")
	@ResponseBody
	public String contract_getLeadInfo(HttpServletRequest request,
		@Parameter(description = "lendId", required = true) @RequestParam(value="lendId",required=true)String lendId,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	)throws Exception{
		
		return this.tblContractLendService.getLendInfo(token,lendId);
	}
	
	@RequestMapping(value = "/contract/getBlackCounterPartInfo",method = {RequestMethod.GET},produces = "application/html; charset=utf-8")
	@Operation(summary = "相对方管理-获取黑名单信息")
	@ResponseBody
	public String contract_saveLeadInfo(HttpServletRequest request,
										@Parameter(description = "budgetId", required = true) @RequestParam(value="budgetId",required=true)BigDecimal budgetId,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.findbudgteInfoById(budgetId,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * 合同归档-详情
	 * @param request
	 * @param pageNumber
	 * @param pageSize
	 * @param contractId
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/contract/lendContractList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同借阅记录列表")
	public String lendContractList(HttpServletRequest request,
										 @Parameter(description="pageNumber",required=false)Integer pageNumber,
										 @Parameter(description="pageSize",required=false)Integer pageSize,
		@Parameter(description = "contractId", required = false)@RequestParam(name="contractId",required=false)String contractId,
										 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										 @Parameter(name = "staffId", description = "登录用户Id", required = false)String staffId){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblContractLendService.findByContractId(pageNumber,pageSize,contractId,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/importContractPdf",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
	@Operation(summary = "上传合同正文PDF")
	public String importRiskClassExcel(@Parameter(name = "file", description = "file", required = true)MultipartFile file,
										@Parameter(description = "contractId", required = true)BigDecimal contractId,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		String result = null;
		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return JsonBean.error("用户已失效");
        }
		//若已存在合同正文PDF,先删除
		/*TblContractContentPdf oldPdf = this.tblContractContentPdfService.findInfoByContractId(contractId);
		if (oldPdf != null) {
			FtpUtil.removeFile(oldPdf.getContentPdfPath(), FtpUtil.constractfilepath);
			this.tblContractContentPdfService.removeInfo(oldPdf.getContentPdfId().intValue());
		}
		*/
		//
		String fileName = file.getOriginalFilename();
		long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String oldname = fileName.substring(0,fileName.lastIndexOf("."));
		String newname=fileName.replace(oldname,""+timeInMillis);
		long size = file.getSize()/1024;
		try {
			boolean flag = FtpUtil.constractUploadFile(newname, file.getInputStream());
//			if(flag){
//				logger.info("上传成功");
//			}else{
//				logger.info("上传失败");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TblContractContentPdf signing = new TblContractContentPdf();
		signing.setConstractId(contractId);
		signing.setContentPdfName(fileName);
		signing.setContentPdfSize(new BigDecimal(size));
		signing.setContentPdfType(0);
		signing.setContentPdfStatus(0);
		signing.setContentPdfPath(newname);
		signing.setUploader(user.getStaffid());
		signing.setRealname(user.getRealname());
		signing.setUploadTime(new Date());
		Map<String,Object> resultMap = this.tblContractContentPdfService.saveEntity(signing);
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}
	
	@RequestMapping(value = "/contractPdfList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "查看合同正文PDF")
	public String contract_showSealFileList(@Parameter(description = "contractId", required = false)BigDecimal contractId,
											@Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
											@Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
											TblContractContentPdf tca)throws Exception{
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String, Object> resultMap = this.tblContractContentPdfService.findFileListByContractIdPageInfo(contractId,pageNumber,pageSize,tca);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="/downContractPdf",method=RequestMethod.GET,produces = "application/html; charset=utf-8")
	@Operation(summary = "下载合同正文PDF")
	public void downloadFtp(@RequestParam(value="contentPdfId",required=true)BigDecimal contentPdfId, HttpServletResponse response,HttpServletRequest request) {
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return ;
	        }
			TblContractContentPdf signing = this.tblContractContentPdfService.findInfoById(contentPdfId);
			if (signing != null) {
				TblContractContentPdf sing = new TblContractContentPdf();
				sing.setContentPdfId(contentPdfId);
				sing.setContentPdfName(signing.getContentPdfName());
				sing.setContentPdfPath(signing.getContentPdfPath());
				FtpUtil.downUploadContentContentPdf(signing.getContentPdfPath(),signing.getContentPdfName(), response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value="/downloadOldpdfFile",method=RequestMethod.GET,produces = "application/html; charset=utf-8")
	@Operation(summary = "初始合同文本--下载历史合同")
	public String downloadOldpdfFile(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestParam(value="contentPdfId",required=true)BigDecimal contentPdfId, HttpServletResponse response,HttpServletRequest request) {
		JSONObject jsonObj = new JSONObject();
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }

			TblContractContentPdf signing = this.tblContractContentPdfService.findInfoById(contentPdfId);
			if (signing != null &&StringUtils.isNotBlank(signing.getOaattid())) {
				  String oldUrl=HttpClient.dowloadFileOaUrl+signing.getContentPdfPath()+"?token="+
		                	getOaToken(user.getUsername())+"&fileName="+signing.getContentPdfName(); 
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


	
	@RequestMapping(value = "/deleteContractPdf",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "删除合同正文PDF")
	public String deleteFile(@RequestParam(value="contentPdfId",required=true)BigDecimal contentPdfId,
						   HttpServletResponse response,HttpServletRequest request) {
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			boolean bool = false;
			TblContractContentPdf signing = this.tblContractContentPdfService.findInfoById(contentPdfId);
			if (signing != null) {
				bool = FtpUtil.removeFile(signing.getContentPdfPath(), FtpUtil.constractfilepath);
				this.tblContractContentPdfService.removeInfo(contentPdfId);
			} else {
//				logger.info("附件不存在");
			}
			if (bool) {
//				logger.info("删除成功");
			} else {
//				logger.info("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonBean.success();
	}
	
	
	@RequestMapping(value = "/contractTranList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同移交列表")
	public  String contractTranList(HttpServletRequest request, TblContractTran tct,
										 	 @Parameter(description="pageNumber",required=false)Integer pageNumber,
										  	@Parameter(description="pageSize",required=false)Integer pageSize,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.findContractTranList(pageNumber,pageSize,token,tct);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 合同移交-新增修改
	 */
	@RequestMapping(value = "/contractTranListSave",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
	@Operation(summary = "合同移交-新增修改")
    public String contractTranListSave(HttpServletRequest request,@Parameter(name="tct",description="",required=false)TblContractTran tct,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		String result = null;
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwUnitService.contractTranListSave(tct,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
    }
	
	
	@RequestMapping(value = "/contractTranInfo",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同移交-查询明细")
	public  String contractTranInfo(HttpServletRequest request, 
										  	@Parameter(description = "tranId", required = false)BigDecimal tranId,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.contractTranInfo(tranId,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/sealedContractList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同移交-选择合同列表（已用印合同）")
	public  String sealedContractList(HttpServletRequest request, TblCyhwUnit unit,
										 	 @Parameter(description="pageNumber",required=false)Integer pageNumber,
										  	@Parameter(description="pageSize",required=false)Integer pageSize,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.sealedContractList(pageNumber,pageSize,token,unit);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/contractTranDelete",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同移交-删除")
	public  String contractTranDelete(HttpServletRequest request, 
										  	@Parameter(description = "tranId", required = false)BigDecimal tranId,
												@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwUnitService.contractTranDelete(tranId,token);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	@RequestMapping(value = "/toSendBudgetMessage",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "合同用印-发送消息至用印管理员")
	@ResponseBody
	public String toSendBudgetMessage(HttpServletRequest request,
									   @Parameter(name = "contractid", description = "合同主键ID", required = true)@RequestParam(value="contractid",required=true)String contractid,
									   @Parameter(name = "counterpartcode", description = "印章", required = false)@RequestParam(value="counterpartcode",required=false)String counterpartcode,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
        /* String content =null;
		String url = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
        	 TblStaffUtil staff = DealUserToken.parseUserToken(token);
             if (staff == null) {
                 return "用户失效!";
             }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            TblCyhwUnit unit=tblCyhwUnitService.getEntity(new BigDecimal(contractid));
           //获取用印管理员对象
            List<TblStaff> RoleList=tblStaffService.getRoleList(Integer.parseInt(staff.getCurrentOrg().getOrgid().toString()),"用印管理员");
             JSONArray arr=new JSONArray();
            if(RoleList!=null){
            for(TblStaff s:RoleList){
             JSONObject obj=new JSONObject();
            obj.put("thirdpartyMessageId",contractid+s.getStaffid());//第三方待办主键（保证唯一） 必填
            obj.put("thirdpartyRegisterCode", HttpClient.oaCode);//为第三方配置的系统注册编码 必填
            obj.put("messageContent", "合同编号："+unit.getContractno()+"合同名称："+unit.getContractname()+"印章名称:"+counterpartcode+"  已申请用印！");//待办标题 必填  
            obj.put("thirdpartySenderId",staff.getStaffid());////第三方待办发起人主键（保证唯一） 非必填 
            obj.put("thirdpartyReceiverId",s.getStaffid());////第三方待办发起人姓名 必填
            obj.put("creation_date", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
            obj.put("messageType", 0);//0：PC；
            obj.put("messageURL", "");//PC 端穿透链接
            obj.put("noneBindingSender", staff.getUsername());//OA 对应的发起人员==当前登录人员 staff.getUsername()
            obj.put("noneBindingReceiver",s.getUsername());//OA 对应的处理人员  s.getUsername()
            obj.put("messageURL","");
            obj.put("messageH5URL","");
            obj.put("appParam","");
            url = HttpClient.oaUrl+HttpClient.singleMessage;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            JSONObject object=new JSONObject();
            object.put("messages", arr);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaToken());
            StringEntity postingString = new StringEntity(obj.toString(),
                    "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
              content = EntityUtils.toString(response.getEntity());
            System.out.println(content);
         }
          }
        } catch (Exception e) {
        		e.printStackTrace();
        		 return   JsonBean.error();
        }*/
     return   JsonBean.success();
	}
	
	//获取对方token数据
	public String getOaToken() {
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
	            post.setHeader("Content-Type","application/json;charset=utf-8");
	            StringEntity postingString = new StringEntity(obj.toString(),
	                    "utf-8");
	            post.setEntity(postingString);
	            HttpResponse response = httpClient.execute(post);
	            String content = EntityUtils.toString(response.getEntity());
	            JSONObject result=JSONObject.parseObject(content);
	            token= result.get("id").toString();
	            System.out.println(token);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取token失败："+e.getMessage());
			return token;
		}
		return token;
	}
		
	
	@RequestMapping(value = "/getOAYbList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "获取用户待办列表/已办列表")
	@ResponseBody
	public String getOAYbList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(description="pageNumber",required=false)@RequestParam(value="pageNumber",required=false)Integer pageNumber,
			@Parameter(description="pageSize",required=false)@RequestParam(value="pageSize",required=false)Integer pageSize,
			@Parameter(name="username",description="username",required=false)@RequestParam(value="username",required=false)String username
          ){
         String content =null;
		String url = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
        	TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				return JsonBean.error("用户已失效");
	        }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            JSONObject obj=new JSONObject();
            obj.put("loginName",staff.getUsername());//staff.getUsername()
            obj.put("page",pageNumber);
            obj.put("pageSize",pageSize);
            obj.put("state","col_done");//状态,待发（col_waitSend），已办（col_done）、已发（col_sent）、不传默认为待办
            url = HttpClient.oaUrl+HttpClient.getOAYblist;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaDocumentToken(staff.getUsername()));//"seeyon1"
            StringEntity postingString = new StringEntity(obj.toString(),
                    "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
              content = EntityUtils.toString(response.getEntity());
            System.out.println(content);
        } catch (Exception e) {
        		e.printStackTrace();
        		 return   JsonBean.error();
        }
     return   content;
	}

	@RequestMapping(value = "/getOAList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "获取用户所有公文列表/协同列表")
	@ResponseBody
	public String getOAList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "type", description = "edoc为公文，col为协同", required = true)@RequestParam(value="type",required=true)String type,
			@Parameter(description="pageNumber",required=false)@RequestParam(value="pageNumber",required=false)Integer pageNumber,
			@Parameter(description="pageSize",required=false)@RequestParam(value="pageSize",required=false)Integer pageSize,
			@Parameter(name = "state", description = "_type为col时必须填写-4为已办", required = false)@RequestParam(value="state",required=false)String state,
			@Parameter(name = "appType", description = "_type为edoc时必须填写；值为19代表发文，20为收文，21为签报", required = false)@RequestParam(value="appType",required=false)String appType,
			@Parameter(name = "textfield", description = "实现模糊查询", required = false)@RequestParam(value="textfield",required=false)String textfield,
			@Parameter(name="username",description="username",required=false)@RequestParam(value="username",required=false)String username
          ){
         String content =null;
		String url = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
        	TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				return JsonBean.error("用户已失效");
	        }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            JSONObject obj=new JSONObject();
            obj.put("_type",type);//edoc为公文，col为协同
            obj.put("loginName",staff.getUsername());//staff.getUsername()
            obj.put("page",pageNumber);
            obj.put("pageSize",pageSize);
            obj.put("state",state);//_type为col时必须填写；值为2代表已发，3为待办，4为已办
            obj.put("appType", appType);//_type为edoc时必须填写；值为19代表发文，20为收文，21为签报
            if(textfield!=null && textfield.trim().length()>0) {
            	obj.put("condition","subject");
                obj.put("textfield",textfield);
            }
            url = HttpClient.oaUrl+HttpClient.getOAlist;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaDocumentToken(staff.getUsername()));
            StringEntity postingString = new StringEntity(obj.toString(),
                    "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
              content = EntityUtils.toString(response.getEntity());
            System.out.println(content);
        } catch (Exception e) {
        		e.printStackTrace();
        		 return   JsonBean.error();
        }
     return   content;
	}
	
	//获取公文列表TOKEN数据
		public String getOaDocumentToken(String loginname) {
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
		            obj.put("loginName", loginname);//为第三方配置的系统注册编码
		            post.setHeader("Content-Type","application/json;charset=utf-8");
		            StringEntity postingString = new StringEntity(obj.toString(),
		                    "utf-8");
		            post.setEntity(postingString);
		            HttpResponse response = httpClient.execute(post);
		            String content = EntityUtils.toString(response.getEntity());
		            JSONObject result=JSONObject.parseObject(content);
		            token= result.get("id").toString();
		            System.out.println(token);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("获取token失败："+e.getMessage());
				return token;
			}
			return token;
		}
		
		@ResponseBody
		@RequestMapping(value = "/cyhwUnitOAListSave",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
		@Operation(summary = "合同审批--保存OA选择公文/协同信息")
		public String cyhwUnitOAListSave(HttpServletRequest request,
								   @Parameter(name = "contractId", description = "合同Id", required = false)@RequestParam(value="contractId",required=false)String contractId,
								   @Parameter(name = "oaList", description = "OA公文json字符串格式", required = false)String oaList,
								   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
								  ){
			String result = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				TblStaffUtil user = userProvider.get();
				if (user == null) {
					return JsonBean.error("用户已失效");
		        }
				Map<String,Object> resultMap = this.tblCyhwUnitService.cyhwUnitOAListSave(token,contractId,oaList);
				JSONObject jsonObj = new JSONObject(resultMap);
				result = jsonObj.toString();
			} catch (Exception e) {
				result = "-1";
				e.printStackTrace();
			}
			return result;
		}
		
		@ResponseBody
		@RequestMapping(value = "/getCyhwUnitOAList",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
		@Operation(summary = "合同审批--获取OA选择公文/协同信息起草后列表")
		public String getCyhwUnitOAList(HttpServletRequest request,
								   @Parameter(name = "contractId", description = "合同Id", required = false)@RequestParam(value="contractId",required=false)String contractId,
								   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
								  ){
			String result = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,Object> resultMap = this.tblCyhwUnitService.getCyhwUnitOAList(token,contractId);
				JSONObject jsonObj = new JSONObject(resultMap);
				result = jsonObj.toString();
			} catch (Exception e) {
				result = "-1";
				e.printStackTrace();
			}
			return result;
		}
		
		@ResponseBody
		@RequestMapping(value = "/remoceOaDocument",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
		@Operation(summary = "合同审批--删除OA选择公文/协同信息")
		public String remoceOaDocument(HttpServletRequest request,
								   @Parameter(name = "documentId", description = "公文Id", required = false)@RequestParam(value="documentId",required=false)String documentId,
								   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
								  ){
			String result = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Map<String,Object> resultMap = this.tblCyhwUnitService.removeOADocument(token, documentId);
				JSONObject jsonObj = new JSONObject(resultMap);
				result = jsonObj.toString();
			} catch (Exception e) {
				result = "-1";
				e.printStackTrace();
			}
			return result;
		}
		
		
		
		@ResponseBody
		@RequestMapping(value = "/getOaurl",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
		@Operation(summary = "获取OA跳转url")
		public String geturl(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
								  ){
			String result = null;
			try {
				TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ResponseFormat.retParam(0, 20006, null).toString();
				}
				//String ticket=DesUtil.encode(loginStaff.getUsername()+";"+System.currentTimeMillis(), "yo6!se@#$%en5^&*12(34)");
				Map<String,Object> resultMap = new HashMap<String, Object>(0);
				/*resultMap.put("oaurl", HttpClient.wyoaUrl);
				resultMap.put("ticket", ticket);*/
				result=ResponseFormat.retParam(1, 200, resultMap).toString();
			} catch (Exception e) {
				result = "-1";
				e.printStackTrace();
			}
			return result;
		}
		public static void main(String[] args) {

			try {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
				String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
				 Connection connGRC = DriverManager.getConnection(url,  "zsco_tzxmk", "kqdnf@2022");
				  Statement stmtGRC = connGRC.createStatement();
				  stmtGRC.setQueryTimeout(0);
				  StringBuffer sb = new StringBuffer("select * from ADMIN.V_ZSCO_TZGHXMK  ");
				  ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
				  ResultSetMetaData meta = rsOracle.getMetaData();
				  int columncount = meta.getColumnCount();
				  System.out.println("====="+columncount);
					while (rsOracle.next()) {
						for (int i = 1; i <= columncount; i++) {
							System.out.println(meta.getColumnName(i)+":"+rsOracle.getObject(i).toString());
							
						}
					}
				  rsOracle.close();
				  stmtGRC.close();
				  connGRC.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		
		
		@RequestMapping(value="/importContractExam",method=RequestMethod.POST,produces = "application/html; charset=utf-8")
		@Operation(summary = "上传审核合同文本")
		@ResponseBody
		public String importContractExam(@Parameter(name = "file", description = "file", required = true)MultipartFile file,
											@Parameter(description = "contractId", required = true)@RequestParam(value="contractId",required=false)BigDecimal contractId,
											@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
			String result=null;
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
//				if(flag){
//					logger.info("上传成功");
//				}else{
//					logger.info("上传失败");
//				}
			
			TblContractExamFile signing = new TblContractExamFile();
			signing.setConstractId(contractId);
			signing.setFileName(fileName);
			signing.setFileSize(new BigDecimal(size));
			signing.setFileType(0);
			signing.setFileStatus(0);
			signing.setFilePath(newname);
			signing.setUploader(user.getStaffid());
			signing.setUploaderName(user.getRealname());
			signing.setUploadTime(new Date());
			Map<String,Object> resultMap = this.tblContractExamFileService.saveEntity(signing);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return JsonBean.error();
			}
			return result;
		}
		
		
		@RequestMapping(value = "/contractExamList",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
		@Operation(summary = "查看审核合同文本列表")
		public String contractExamList(@Parameter(description = "id", required = true)@RequestParam(value="id",required=true)BigDecimal id,
												@Parameter(name="pageNumber",description="当前页",required=false)@RequestParam(value="pageNumber",required=false)Integer pageNumber,
												@Parameter(name="pageSize",description="每页数量",required=false)@RequestParam(value="pageSize",required=false)Integer pageSize,
												TblContractExamFile file)throws Exception{
			String result = null;
			try {
				TblStaffUtil user = userProvider.get();
				if (user == null) {
					return JsonBean.error("用户已失效");
		        }
				Map<String, Object> resultMap = this.tblContractExamFileService.findFileListByContractIdPageInfo(id,pageNumber,pageSize,file);
				JSONObject jsonObj = new JSONObject(resultMap);
				result = jsonObj.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		@RequestMapping(value="/downContractExam",method=RequestMethod.GET,produces = "application/html; charset=utf-8")
		@Operation(summary = "下载审核合同文本")
		public void downContractExam(@Parameter(description = "id", required = true)@RequestParam(value="id",required=true)BigDecimal id, HttpServletResponse response,HttpServletRequest request) {
			try {
				TblStaffUtil user = userProvider.get();
				TblContractExamFile file = this.tblContractExamFileService.findInfoById(id);
				if (file != null) {
					FtpUtil.downUploadContentContentPdf(file.getFilePath(),file.getFileName(), response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/deleteContractExam",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
		@Operation(summary = "删除审核合同文本")
		public String deleteContractExam(@Parameter(description = "id", required = false)@RequestParam(value="id",required=true)BigDecimal id,
							   HttpServletResponse response,HttpServletRequest request) {
			try {
				boolean bool = false;
				TblStaffUtil user = userProvider.get();
				if (user == null) {
					return JsonBean.error("用户已失效");
		        }
				TblContractExamFile file = this.tblContractExamFileService.findInfoById(id);
				if (file != null) {
					bool = FtpUtil.removeFile(file.getFilePath(), FtpUtil.constractfilepath);
					this.tblContractExamFileService.removeInfo(id);
				} else {
//					logger.info("附件不存在");
				}
				if (bool) {
//					logger.info("删除成功");
				} else {
//					logger.info("删除失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return JsonBean.success();
		}

}
