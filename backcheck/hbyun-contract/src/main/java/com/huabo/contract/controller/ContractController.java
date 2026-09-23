package com.huabo.contract.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BizException;
import com.hbfk.util.DateUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblContractCollection;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwBasicuninspection;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblFlow;
import com.huabo.contract.entity.TblLegalArbitrationrecord;
import com.huabo.contract.entity.TblLegalArbitratsettlement;
import com.huabo.contract.entity.TblLegalAssetporotect;
import com.huabo.contract.entity.TblLegalCloseSum;
import com.huabo.contract.entity.TblLegalCloseinformation;
import com.huabo.contract.entity.TblLegalDisputregistration;
import com.huabo.contract.entity.TblLegalExecumgr;
import com.huabo.contract.entity.TblLegalFrozenaccount;
import com.huabo.contract.entity.TblLegalLitigationsettlement;
import com.huabo.contract.entity.TblLegalNegotiatedsettlemen;
import com.huabo.contract.entity.TblLegalNegotiaterecord;
import com.huabo.contract.entity.TblLegalProceedingsrecord;
import com.huabo.contract.entity.TblLegalQualification;
import com.huabo.contract.entity.TblOrgBankaccount;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.entity.TblYyXdfCompany;
import com.huabo.contract.entity.TblYyXdfTeam;
import com.huabo.contract.entity.TbllegalAttorney;
import com.huabo.contract.service.TblAttachmentService;
import com.huabo.contract.service.TblContractCollectionService;
import com.huabo.contract.service.TblContractInvoicesmanagemenService;
import com.huabo.contract.service.TblContractPaymenService;
import com.huabo.contract.service.TblContractPlannodeService;
import com.huabo.contract.service.TblContractSpnodeService;
import com.huabo.contract.service.TblCounterpartBankInfoService;
import com.huabo.contract.service.TblCyhwBasicuninspectionService;
import com.huabo.contract.service.TblCyhwProjectbudgetService;
import com.huabo.contract.service.TblCyhwUnitService;
import com.huabo.contract.service.TblFlowService;
import com.huabo.contract.service.TblLegalArbitrationAttService;
import com.huabo.contract.service.TblLegalArbitrationrecordService;
import com.huabo.contract.service.TblLegalArbitratsettlementService;
import com.huabo.contract.service.TblLegalAssetporotectService;
import com.huabo.contract.service.TblLegalCloseSumService;
import com.huabo.contract.service.TblLegalCloseinformationService;
import com.huabo.contract.service.TblLegalDisputregistrationService;
import com.huabo.contract.service.TblLegalExecumgrService;
import com.huabo.contract.service.TblLegalFrozenaccountService;
import com.huabo.contract.service.TblLegalLitigationsettlementService;
import com.huabo.contract.service.TblLegalLsettlementAttService;
import com.huabo.contract.service.TblLegalNegotiateRecordService;
import com.huabo.contract.service.TblLegalNegotiatedsettlemenService;
import com.huabo.contract.service.TblLegalNegotiateeAttService;
import com.huabo.contract.service.TblLegalProceedingsrecordService;
import com.huabo.contract.service.TblLegalQualificationService;
import com.huabo.contract.service.TblOrgBankAccountService;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import com.huabo.contract.service.TblYyXdfTeamService;
import com.huabo.contract.service.TbllegalAttorneyService;
import com.huabo.contract.service.TblyyxdfCompanyService;
import com.huabo.contract.util.Tree;
import com.huabo.contract.vo.StaffResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 合同控制器
 * <p>提供合同的新增、修改、删除、审批、对方单位维护等核心业务接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="合同Controller",description="合同Controller")
public class ContractController {
	
	private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

	private static final String DOCDIC = PropertyFileReader.getItem("file.path");
	private static final String separator = System.getProperty("file.separator");
	
	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private TblCyhwProjectbudgetService tblCyhwProjectbudgetService;
	
	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Resource
	private TblCyhwUnitService tblCyhwUnitService;
	
	@Resource
	private TblContractSpnodeService tblContractSpnodeService;
	
	@Resource
	private TblLegalDisputregistrationService tblLegalDisputregistrationService;
	
	@Resource
	private TblCounterpartBankInfoService tblCounterpartBankInfoService;
	
	@Resource
	private TblyyxdfCompanyService tblyyxdfCompanyService;
	
	@Resource
	private TblYyXdfTeamService tblYyXdfTeamService;
	
	@Resource
	private TblContractCollectionService tblContractCollectionService;
	
	@Resource
	private TblContractPlannodeService tblContractPlannodeService;
	
	@Resource
	private TblOrgBankAccountService tblOrgBankAccountService;
	
	@Resource
	private TblContractInvoicesmanagemenService tblContractInvoicesmanagemenService;
	
	@Resource
	private TblContractPaymenService tblContractPaymenService;
	
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	private TblStaffService tblStaffService;
	
	@Resource
	private TblCyhwBasicuninspectionService tblCyhwBasicuninspectionService;
	
	@Resource
	public HttpServletRequest request;
	
	@Resource
	private TblLegalArbitratsettlementService tblLegalArbitratsettlementService;
	
	@Resource
	private TblLegalNegotiatedsettlemenService tblLegalNegotiatedsettlemenService;
	
	@Resource
	private TblLegalNegotiateRecordService tblLegalNegotiateRecordService;
	
	@Resource
	private TblLegalNegotiateeAttService tblLegalNegotiateeAttService;
	
	@Resource
	private TblFlowService tblFlowService;
	
	@Resource
	private TblLegalLitigationsettlementService tblLegalLitigationsettlementService;
	
	@Resource
	private TblLegalProceedingsrecordService tblLegalProceedingsrecordService;
	
	@Resource
	private TblLegalLsettlementAttService tblLegalLsettlementAttService;
	
	@Resource
	private TblLegalArbitrationrecordService tblLegalArbitrationrecordService;
	
	@Resource
	private TblLegalArbitrationAttService tblLegalArbitrationAttService;
	
	@Resource
	private TblLegalCloseinformationService tblLegalCloseinformationService;
	
	@Resource
	private TblLegalQualificationService tblLegalQualificationService;
	
	@Resource
	private TblLegalFrozenaccountService tblLegalFrozenaccountService;
	
	@Resource
    private TblLegalAssetporotectService tblLegalAssetporotectService;
	
	@Resource
    private TblLegalExecumgrService tblLegalExecumgrService;
    
    @Resource
    private TblLegalCloseSumService tblLegalCloseSumService;
    
    @Resource
    private TbllegalAttorneyService tbllegalAttorneyService;
	
	@Value("${file.path}")
	private String fileUrl;
	
	@Value("${fwgl.belongGroupName:}")
	private String belongGroupName;
	
	@Value("${fwgl.legalPersonnel:}")
	private String legalPersonnel;
	
	/**
	 * 相对方维护列表页面
	 * @param request
	 * @param token
	 * @param flowId
	 * @param staffId
	 * @param pageNumber
	 * @param pageSize
	 * @param budget
	 * @return
	 */
	@RequestMapping(value = "/oppositePartyMaintenance",method = {RequestMethod.POST} )
	@Operation(summary = "相对方维护列表页面")
	public String oppositePartyMaintenance(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "flowId", description = "流程Id主键", required = true)String flowId,
			@Parameter(name = "staffId", description = "用户Id主键", required = false)String staffId,
			@RequestParam(name="pageNumber",required=false,defaultValue="1")@Parameter(name = "currentPage", description = "当前页", required = false)Integer pageNumber,
			@RequestParam(name="pageSize",required=false,defaultValue="20")@Parameter(name = "pageSize", description = "每页数量", required = false)Integer pageSize
			,TblCyhwProjectbudget budget){
		String result = null;
		try {
			if(pageNumber == null) {
				pageNumber = 1;
			}
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.findBudgetListByStaffOrg(token,flowId,staffId,budget,pageNumber,pageSize);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 加载相对方新增页面前置条件
	 * @param request
	 * @param flowId
	 * @param token
	 * @param contractId
	 * @param choiceSearch
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value="/oppositePartyToAddPage",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "加载相对方新增页面前置条件")
	public String oppositePartyToAddPage(HttpServletRequest request,@Parameter(name = "flowId", description = "流程Id主键", required = true)String flowId,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "contractId", description = "合同用印查询合同信息", required = false)Integer contractId,@Parameter(name = "choiceSearch", description = "用来判断查询显示还是隐藏默认值hide", required = false)String choiceSearch,@Parameter(name = "staffId", description = "当前登录用户Id", required = false)String staffId) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.loadAddOppositePartyInfo(flowId,contractId,choiceSearch,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}

	
	
	/**
	 * 保存相对方用户信息
	 */
	@RequestMapping(value = "/saveOppositeParty",method = {RequestMethod.POST})
	@Operation(summary = "保存相对方用户信息")
	public String oppositePartySave(HttpServletRequest request,
			TblCyhwProjectbudget tcpb,
			@Parameter(name = "linkDeptId", description = "关联部门Id", required = false)String linkDeptId,@Parameter(name = "createDate", description = "创建日期", required = false)String createDate,
			@Parameter(name = "pstartDate1", description = "证件有效期开始日期", required = false)String pstartDate1,	@Parameter(name = "pendDate1", description = "证件有效期结束日期", required = false)String pendDate1,
			@Parameter(name = "recortParent", description = "关联合同Id", required = false)String recortParent,@Parameter(name = "contractDeptId", description = "报送科室单位", required = false)String contractDeptId,
			@Parameter(name = "bgDeptId", description = "保管部门Id", required = false)String bgDeptId,@Parameter(name = "bgstaffid", description = "保管人Id", required = false)String bgstaffid,
			@Parameter(name = "pageEffectDate", description = "保管有效期开始日期", required = false)String pageEffectDate,@Parameter(name = "pageSafeDate", description = "保管有效期结束日期", required = false)String pageSafeDate,
			@Parameter(name = "attids", description = "附件Id数组", required = false)String attids,@Parameter(name = "staffId", description = "登录用户Id", required = false)String staffId
			,@Parameter(name = "pstartdateStr", description = "证件有效期开始日期", required = false)String pstartdateStr
			,@Parameter(name = "penddateStr", description = "证件有效期结束日期", required = false)String penddateStr
			){
		String result = null;
		// 判断参数是否为空
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			if(StringUtils.isNotBlank(pstartdateStr)) {
				tcpb.setPstartdate(sdf.parse(pstartdateStr));
			}
			if(StringUtils.isNotBlank(penddateStr)) {
				tcpb.setPenddate(sdf.parse(penddateStr));
			}
			
			if(createDate != null && !"".equals(createDate)){
				tcpb.setCreatetime(sdf.parse(createDate));
			}else{
				tcpb.setCreatetime(new Date());
			}
			//证件有效期
			if(pstartDate1 != null && !"".equals(pstartDate1)){
				tcpb.setPstartdate(sdf.parse(pstartDate1));
			}
			if(pendDate1 != null && !"".equals(pendDate1)){
				tcpb.setPenddate(sdf.parse(pendDate1));
			}

			if(recortParent != null && !"".equals("recortParent")){
				tcpb.setRecordparent(new BigDecimal(recortParent));
			}


			if(linkDeptId != null && !"".equals(linkDeptId)){
				tcpb.setLinkdepr(new BigDecimal(linkDeptId));
			}

			if(contractDeptId != null && !"".equals(contractDeptId)){
				tcpb.setReporttodept(new BigDecimal(contractDeptId));
			}

			if(bgDeptId != null && !"".equals(bgDeptId)){
				tcpb.setSafeorg(new BigDecimal(bgDeptId));
			}

			if(bgstaffid != null && !"".equals(bgstaffid)){
				tcpb.setSafestaff(new BigDecimal(bgstaffid));
			}

			if(pageEffectDate != null && !"".equals(pageEffectDate)){
				tcpb.setEffectdate(sdf.parse(pageEffectDate));
			}

			if(pageSafeDate != null && !"".equals(pageSafeDate)){
				tcpb.setSafedate(sdf.parse(pageSafeDate));
			}
			Map<String,Object> resultMap =  this.tblCyhwProjectbudgetService.SaveOppositeParty(tcpb,attids,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方删除资质文件
	 * @param request
	 * @param attid
	 * @return
	 */
	@Operation(summary = "相对方管理删除资质文件")
	@RequestMapping(value = "/removeOppsiteFile",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	public String removeOppsiteFile(HttpServletRequest request,@Parameter(name = "attid", description = "附件ID", required = true)String attid){
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			this.tblCyhwProjectbudgetService.removeOppsiteFile(attid);
			return JsonBean.success();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return JsonBean.error("删除失败");
	}
	
	/**
	 * 获取相对方信息状态
	 */
	@RequestMapping(value="/getProjectrBudgetStatue",produces = "application/html; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "获取相对方信息状态")
	public String getProjectrBudgetStatue(HttpServletRequest request,
			@RequestParam(name = "budgetId" , required=true )@Parameter(name = "budgetId", description = "相对方信息主键", required = true)BigDecimal budgetId){
		try {
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Integer statue = this.tblCyhwProjectbudgetService.findStatueById(budgetId);
			if(statue!=null && statue==1){
				return JsonBean.error("流程审批中");
			}else if(statue!=null && statue==3){
				return JsonBean.error("流程已通过");
			}else if((statue!=null && statue==4) ||(statue!=null && statue==5) || (statue!=null && statue==6)){
				return JsonBean.error("流程已完成");
			}else{
				return JsonBean.success();
			}

		} catch (Exception e) {
			log.error("异常信息：", e);
			return JsonBean.error("失败");
		}

	}
	
	
	@RequestMapping(value = "/getOppoRelaInfo",method = {RequestMethod.GET},produces = "application/json; charset=utf-8")
	@Operation(summary = "通过取消黑名单主键或黑名单主键获取相对方相关信息")
	public JsonBean getOppoRelaInfo(HttpServletRequest request,
			@RequestParam(name = "removeid" , required=false )@Parameter(name = "removeid", description = "黑名单取消信息主键", required = false)String removeid,
			@RequestParam(name = "blackid" , required=false )@Parameter(name = "blackid", description = "加入黑名单信息主键", required = false)String blackid){
		JsonBean result = null;
		try {
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			
			result = this.tblCyhwProjectbudgetService.getOppoRelaInfo(removeid,blackid);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}

		return result;
	}
	
	/**
	 *相对方信息修改加载相对方信息
	 */
	@RequestMapping(value = "/findOppsiteInfo",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "相对方信息修改加载相对方信息")
	public String findOppsiteInfo(HttpServletRequest request,
			@RequestParam(name = "budgetId" , required=true )@Parameter(name = "budgetId", description = "相对方信息主键", required = true)BigDecimal budgetId){
		String result = null;
		try {
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.findOppsiteAllInfoById(budgetId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}

		return result;
	}
	
	
	
	@RequestMapping(value = "/oppositePartyMenger",method = {RequestMethod.POST})
	@Operation(summary = "保存修改后的相对方信息")
	public String oppositePartyMenger(HttpServletRequest request,
		TblCyhwProjectbudget tcpb,
		@Parameter(name = "linkDeptId", description = "关联部门Id", required = false)BigDecimal linkDeptId,@Parameter(name = "itemtype", description = "itemtype", required = false)String itemtype,
		@Parameter(name = "pstartDate1", description = "证件有效期开始日期", required = false)String pstartDate1,	@Parameter(name = "pendDate1", description = "证件有效期结束日期", required = false)String pendDate1,
		@Parameter(name = "recortParent", description = "合同主键ID", required = false)String recortParent,@Parameter(name = "contractDeptId", description = "关联科室Id", required = false)BigDecimal contractDeptId,
		@Parameter(name = "bgDeptId", description = "保管部门ID", required = false)String bgDeptId,@Parameter(name = "bgstaffid", description = "保管用户ID", required = false)String bgstaffid,
		@Parameter(name = "pageEffectDate", description = "档案有效期开始日期", required = false)String pageEffectDate,@Parameter(name = "pageSafeDate", description = "档案有效期结束日期", required = false)String pageSafeDate,
		@Parameter(name = "attids", description = "附件ID数组", required = false)String attids
		,@Parameter(name = "pstartdateStr", description = "证件有效期开始日期", required = false)String pstartdateStr
		,@Parameter(name = "penddateStr", description = "证件效期结束日期", required = false)String penddateStr){
		String result = null;
		//判断传入的参数是否为空
		try {
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			tcpb.setReporttodept(contractDeptId);
			tcpb.setLinkdepr(linkDeptId);
			if(StringUtils.isNotBlank(pstartdateStr)) {
				tcpb.setPstartdate(sdf.parse(pstartdateStr));
			}
			if(StringUtils.isNotBlank(penddateStr)) {
				tcpb.setPenddate(sdf.parse(penddateStr));
			}
			if(recortParent != null && !"".equals(recortParent)){
				tcpb.setRecordparent(new BigDecimal(recortParent));
			}
			if(bgDeptId != null && !"".equals(bgDeptId)){
				tcpb.setSafeorg(new BigDecimal(bgDeptId));
			}
			if(bgstaffid != null && !"".equals(bgstaffid)){
				tcpb.setSafestaff(new BigDecimal(bgstaffid));
			}
			if(pageEffectDate != null && !"".equals(pageEffectDate)){
				tcpb.setEffectdate(sdf.parse(pageEffectDate));
			}
			if(pageSafeDate != null && !"".equals(pageSafeDate)){
				tcpb.setSafedate(sdf.parse(pageSafeDate));
			}
			Map<String,Object> resultMap = this.tblCyhwProjectbudgetService.mengerOppsitePartyById(tcpb,attids);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 删除相对方信息
	 * @param request
	 * @param flowId  流程信息主键
	 * @param token
	 * @param staffId
	 * @param budgetId 删除的相对方信息主键
	 * @return
	 */
	@RequestMapping(value = "/removeOppsiteParty",method = {RequestMethod.POST})
	@Operation(summary = "删除相对方信息")
	public String removeOppsitepart(HttpServletRequest request,
			@Parameter(name = "budgetId", description = "删除的相对方信息主键", required = true)String budgetId) {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.removeOppsitePartyInfo(budgetId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	@RequestMapping(value = "/removeOppsitePartyNoLc",method = {RequestMethod.POST})
	@Operation(summary = "删除相对方信息--无流程版本")
	public String removeOppsitePartyNoLc(HttpServletRequest request,
			@Parameter(name = "budgetId", description = "删除的相对方信息主键", required = true)@RequestParam(value="budgetId",required=false)String budgetId) {
		String result = null;
		try {
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.removeOppsitePartyInfoNoLc(budgetId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	@RequestMapping(value = "/getattInfo",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary = "根据合同id获取生成水印文件信息")
	public String getattInfo(HttpServletRequest request,@Parameter(name = "contractId", description = "合同主键", required = true)BigDecimal contractId){
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Map<String,Object>  resultMap = tblAttachmentService.findAttachmentListByhtId(contractId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}

		return result;
	}
	
	/**相对方管理加入黑名单
	 *
	 * @param request
	 * @param blackType 黑名单类型
	 * @param budgetId 想对方主键
	 * @param datetext  黑名单有效期
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value="/saveOppsitePartyBlack",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方管理加入黑名单")
	public String saveOppsitePartyBlack(HttpServletRequest request,
		@Parameter(name = "blacktype", description = "黑名单类型", required = true)@RequestParam(value="blacktype",required=true)Integer blacktype,
		@Parameter(name = "budgetid", description = "相对方主键", required = true)@RequestParam(value="budgetid",required=true)BigDecimal budgetid,
		@Parameter(name = "brid", description = "黑名单记录审批", required = false)@RequestParam(value="brid",required=false)String brid,
		@Parameter(name = "pageEffectDate", description = "黑名单有效期", required = false)@RequestParam(value="pageEffectDate",required=false)String pageEffectDate,
		@Parameter(name = "backreason", description = "加入黑名单原因", required = false)@RequestParam(value="backreason",required=false)String backreason) {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.saveOppsitePartyBlack(budgetid,blacktype,pageEffectDate,backreason,brid);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方预警页面展示
	 * @return
	 */
	@RequestMapping(value = "/oppsiteWarningList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "相对方预警列表页面")///cwgl/xdf_warningList老项目
	public  String xdf_warningList(HttpServletRequest request,
	@RequestParam(name = "pageNumber" , required = false , defaultValue = "1")@Parameter(name="pageNumber",description="pageNumber",required=false)Integer pageNumber,
	@RequestParam(name = "pageSize" , required = false , defaultValue = "20") @Parameter(name = "pageSize", description = "每页数量", required = false)Integer pageSize,
								   TblCyhwProjectbudget budget,
								   @RequestParam(value = "cateId",required = false)Integer cateId,
								   @RequestParam(value = "isFlowdb",required = false)Integer isFlowdb,
								   @RequestParam(value = "view",required = false)Integer view){
		String result = null;
		try {

			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.finOppsiteWarningList(pageNumber,budget,isFlowdb,"HTGL001",view,pageSize);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	@RequestMapping(value = "/projectrBudgetDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "向对方预警-向对方名称查看")
	public String projectrBudgetDetail(HttpServletRequest request
											 //,@RequestParam(value="flowid",required=true)BigDecimal flowid
											 ,@RequestParam(value="budgetid",required=true)BigDecimal budgetid) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = this.tblCyhwProjectbudgetService.projectrBudgetDetail(budgetid);
		return result;
	}
	
	@RequestMapping(value = "/contract/contractByList", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方预警-合同签订数量")
	public String contractByList(Model model,HttpServletResponse response,HttpServletRequest request,
								 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
								 @RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
								 @RequestParam(value = "budgetid",required = false)String budgetid)throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }

		String result = null;
		Map<String,Object>  resultMap = this.tblCyhwUnitService.findListByXdf(pageSize,pageNumber,budgetid);
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 相对方预警-异常履约数量
	 */
	@RequestMapping(value = "/contract/abnormalPerformance", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方预警-异常履约数量")
	public String abnormalPerformance(Model model,
									  HttpServletResponse response,
									  HttpServletRequest request,
									  @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
									  @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
									  @RequestParam(value = "budgetid",required = false)String budgetid)throws Exception{
		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object>  resultMap = tblContractSpnodeService.findListByXdf(pageNumber,pageSize,budgetid);
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 相对方黑名单列表查询
	 * @return
	 */
	@RequestMapping(value = "/oppsitePartyBlackList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "相对方黑名单管理列表")///nbkz/cwgl/hmd_list
	public String oppsitePartyBlackList(HttpServletRequest request,
		TblCyhwProjectbudget tcbp,
		@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
		@RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
		@RequestParam(value = "staffId",required = false)@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId,
		@RequestParam(value = "choose",required = false)@Parameter(name="choose",description="choose",required=false)String choose){
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCyhwProjectbudgetService.findOppsiteBlackList(pageNumber,tcbp,"HTGL001",pageSize,staffId,choose);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	
	/**
	 * 相对方管理-黑名单管理-相对方名称查看
	 * @param request
	 * @param flowid
	 * @param budgetid
	 * @return
	 */
	@RequestMapping(value = "/addOppsitePartyBlackList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "相对方管理-黑名单管理-相对方名称查看")
	public String addOppsitePartyBlackList(HttpServletRequest request
			,@RequestParam(value="budgetid",required=true)BigDecimal budgetid) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = tblCyhwProjectbudgetService.addOppsitePartyBlackList(budgetid);
		// 将resultMap转换为JSON字符串并返回
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 相对方上传的资质下载
	 */
	@RequestMapping(value = "/downloadFtp/{type}", produces = "application/json; charset=utf-8")
	@Operation(summary = "相对方预警-相对方名称-资质信息下载")
	public void downloadFtp(@PathVariable String type,BigDecimal id,
							HttpServletResponse response,HttpServletRequest request) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ;
        }
		boolean bool =false;
		if(id!=null ){
			//通过id查询
			TblAttachment tblAttachment = this.tblAttachmentService.get(id);
			if(tblAttachment!=null){
				if(type!=null && type.equals("upload")){
					bool= FtpUtil.downUploadFile(tblAttachment, response);
				}
				if(type!=null && type.equals("uploadPython")){
					bool= FtpUtil.downUploadFilePython(tblAttachment, response);
				}
				if(type!=null && type.equals("send")){
					bool= FtpUtil.downloadSendFile(tblAttachment, response);
				}
				if(type!=null && type.equals("template")){
					bool= FtpUtil.downloadTemplateFile(tblAttachment, response);
				}
			}else{
				logger.info("附件不存在");
			}
		}else{
			if(type!=null && type.equals("czsc")){
				String fileName=request.getParameter("fileName");
				String name=request.getParameter("name");
				bool= FtpUtil.downUploadFile(name,fileName, response);
			}
		}
		if(bool){
			logger.info("下载成功");
		}else{
			logger.info("下载失败");
		}

	}
	
	/**
	 * 取消黑名单
	 * @param request
	 * @param budgetid
	 * @return
	 */
	@RequestMapping(value = "/removeBlackList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "相对方管理-黑名单管理-取消黑名单")
	public JsonBean removeBlackList(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			 @RequestParam(value="budgetid",required=true)@Parameter(name = "budgetid", description = "相对方主键", required = true)BigDecimal budgetid,
			@RequestParam(value="brid",required=true)@Parameter(name = "brid", description = "黑名单记录表主键", required = true)String brid,
			@RequestParam(value="rmid",required=false)@Parameter(name = "rmid", description = "取消黑名单记录主键", required = false)String rmid,
			@RequestParam(value="remreason",required=false)@Parameter(name = "remreason", description = "取消黑名单原因", required = false)String remreason){
		JsonBean json = null;
		try {
			
			json = this.tblCyhwProjectbudgetService.removeOppsitePartyBlack(budgetid,brid,rmid,remreason,token);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return json;
	}
	
	/**
	 * 相对方黑名单名单导出功能
	 */
	@RequestMapping(value = "/cwgl/blacklist_export",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "相对方黑名单导出接口")
	public String blacklist_export(HttpServletRequest request, HttpServletResponse response,TblCyhwProjectbudget tcbp,
								   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		log.info("合同管理--相对方管理--黑名单管理---导出Excel");
		response.setContentType("application/binary;charset=UTF-8");
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
		}
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		String result = null;
		try {
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("黑名单管理".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			List<TblCyhwProjectbudget> blacklistExport = this.tblCyhwProjectbudgetService.blacklistExport(orgid,tcbp,staff);
			if(blacklistExport == null) {
				return "导出失败";
			}
			String[] titles = {"相对方编号","相对方名称","法定代表人","内部单位","注册资本（万元）","黑名单期限","黑名单有效期"};
			List<Object[]> blacklist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			int i = 0;
			
			for (TblCyhwProjectbudget budget : blacklistExport) {
				objs = new Object[7];
				objs[0] = budget.getCounterpartno();
				objs[1] = budget.getBudgetname();
				objs[2] = budget.getProjectstagegoal()==null?"0":budget.getProjectstagegoal();
				objs[3] = budget.getServicetype();
				objs[4] = budget.getTotaltmoney();
				objs[6] = budget.getOthermoney();
				objs[7] = budget.getEffectdate()==null?"":DateUtil.parseDate(budget.getEffectdate(), DateUtil.DATE_SMALL_STR);
				blacklist.add(objs);
			}
			ImportOrExportExcelUtil.exportExcel(titles, blacklist, outputStream, null);
		} catch (Exception e) {
			log.info("内控合规---问题汇总---缺陷管理---导出Excel失败");
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 合同管理模块上传附件接口
	 * @param request
	 * @param response
	 * @param map
	 * @param token
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/uploadFileAttInfo", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "合同管理模块上传附件接口")
	public String uploadFileAttInfo(HttpServletRequest request, HttpServletResponse response, Model map,MultipartFile file,
			@RequestParam(value="bid",required=false)BigDecimal bid,
			@RequestParam(value="type",required=false)Integer type) throws Exception{
		BigDecimal aid = null;
		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		
		Map<String,Object> resultMap = this.tblAttachmentService.uploadAttachment(file);
		JSONObject jsonObj = new JSONObject(resultMap);
		 result = jsonObj.toString();
		// 如果bid和type都不为null，将上传的附件与bid和type关联
		 if(bid != null && type != null){
			 TblAttachment tblAttachment = (TblAttachment)resultMap.get("data");
			 aid = tblAttachment.getAttid();
			 tblLegalDisputregistrationService.saveBidType(type,bid,aid);
		 }
	
		
		/*// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		try {
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				 MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 调用tblAttachmentService的uploadAttachment方法处理文件上传
				 Map<String,Object> resultMap =  this.tblAttachmentService.uploadAttachment(multiRequest,file);
				 JSONObject jsonObj = new JSONObject(resultMap);
				 result = jsonObj.toString();
				// 如果bid和type都不为null，将上传的附件与bid和type关联
				 if(bid != null && type != null){
					 TblAttachment tblAttachment = (TblAttachment)resultMap.get("data");
					 aid = tblAttachment.getAttid();
					 tblLegalDisputregistrationService.saveBidType(type,bid,aid);
				 }
            }
		} catch (Exception e) {
			log.error("异常信息：", e);
		}*/
		return result;
	}
	
	
	/**
	 * 附件查询"
	 * @param bid 业务id
	 * @param type 1-纠纷登记，2-协商过程，3-诉讼过程，4-仲裁过程
	 * @return
	 */
	@RequestMapping(value = "/contract/findAttacheMentListByBid", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "附件查询")
	public String findAttacheMentByBid(HttpServletRequest request,@RequestParam(value="bid",required=true)BigDecimal bid,
												 @RequestParam(value="type",required=true)Integer type){

		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblLegalDisputregistrationService.findAttacheMentByBid(type,bid);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 *
	 * @param aid 附件id
	 * @param type	1-纠纷登记，2-协商过程，3-诉讼过程，4-仲裁过程
	 * @return
	 */
	@RequestMapping(value = "/contract/deleAttacheMentByBid", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "附件删除")
	public String deleAttacheMentByBid(HttpServletRequest request,@RequestParam(value="aid",required=true)BigDecimal aid,
									   @RequestParam(value="type",required=true)Integer type) throws Exception{

		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblLegalDisputregistrationService.deleteAttacheMentByBid(type,aid);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 文件下载
	 * @param id
	 * @param httpServletResponse
	 * @return
	 */

	@Operation(summary="下载")
	@RequestMapping(value = "/download", produces = "application/json; charset=utf-8",method = {RequestMethod.GET})
	public void fileDownLoad(HttpServletRequest request,@RequestParam("id") String id, HttpServletResponse httpServletResponse) throws Exception {
		TblStaffUtil staff = userProvider.get();
		TblAttachment tblAttachmentEntity = tblAttachmentService.findById(id);
		FtpUtil.downUploadFile(tblAttachmentEntity,httpServletResponse);
		//return JsonBean.success();
	}
	
	/**
	 * 相对方维护-银行账户管理列表
	 * @param budgetId
	 * @param pageNumber
	 * @param pageSize
	 * @param bank
	 * @return
	 */
	@RequestMapping(value = "/contract/counterpartManageList", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方维护-银行账户管理列表")
	public String contract_counterpartManageList(HttpServletRequest request,
		@Parameter(description = "budgetId", required = true)BigDecimal budgetId,
		@RequestParam(value = "pageNumber",required = false,defaultValue = "1")@Parameter(description="pageNumber",required=false)Integer pageNumber,
		@RequestParam(value = "pageSize",required = false,defaultValue = "20")@Parameter(description="pageSize",required=false)Integer pageSize,
		TblCounterpartBankinfo bank){
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCounterpartBankInfoService.findAllListByBankInfo(budgetId,pageNumber,pageSize,bank);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方银行账户保存/修改方法
	 * @return
	 */
	@RequestMapping(value = "/contract/counterpartBankSave", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方银行账户保存/修改方法")
	@ResponseBody
	public String contract_counterpartBankSave(HttpServletRequest request,
												@Parameter(description = "budgetId", required = true)BigDecimal budgetId,
												TblCounterpartBankinfo bank){
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCounterpartBankInfoService.saveCounterPartBankInfo(budgetId,bank);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方银行账户删除
	 */
	@RequestMapping(value = "/contract/counterpartBankRemove", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方银行账户删除")
	@ResponseBody
	public String contract_counterpartBankRemove(HttpServletRequest request,
												 @Parameter(description = "bankId", required = true)String bankId) throws Exception{
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCounterpartBankInfoService.removeBank(bankId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方银行账户启用、弃用
	 */
	@RequestMapping(value = "/contract/changeBankInfoStatus", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方银行账户启用、弃用")
	@ResponseBody
	public String contract_changeBankInfoStatus(HttpServletRequest request,
												@Parameter(description = "bankId", required = true)String bankId,
												@Parameter(description = "statusValue", required = true)Integer bankstatus){
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblCounterpartBankInfoService.modifyBankStatus(bankId,bankstatus);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方监控列表
	 */
	@RequestMapping(value = "/fxyj/fxyj_list", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方监控列表")
	public String fxyj_fxyj_list(HttpServletRequest request,
			@RequestParam(value = "pageNumber",required = false,defaultValue = "1")@Parameter(description = "pageNumber", required = false)Integer pageNumber,
			@RequestParam(value = "pageSize",required = false,defaultValue = "20")@Parameter(description = "pageSize", required = false)Integer pageSize,
			@Parameter(description = "teamid", required = false)String teamid,
			@Parameter(name = "fxtype", description = "fxtype", required = false)String fxtype,
			@Parameter(name = "companyName", description = "companyName", required = false)String companyname) {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblyyxdfCompanyService.findByCompay(pageNumber,pageSize,teamid,fxtype,companyname);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**相对方监控新增、修改分组
	 *
	 * @param team
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/saveteam", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方监控新增、修改分组")
	@ResponseBody
	public String saveteam(HttpServletRequest request,TblYyXdfTeam team) throws Exception{
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblYyXdfTeamService.saveOrupdateTeam(team);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 *相对方监控分组列表
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/riskwarning/main", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方监控分组列表")
	public String riskwarning_main(HttpServletRequest request) throws Exception {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblYyXdfTeamService.findBYuseridAndCompanid();
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 相对方监控添加公司
	 */
	@RequestMapping(value = "/savecompany", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "相对方监控添加公司")
	@ResponseBody
	public String savecompany(HttpServletRequest request,
			@Parameter(name = "pageid", description = "pageid", required = false)String pageid,
							  @Parameter(name = "priceid", description = "priceid", required = false)String priceid,
							  TblYyXdfCompany company) throws Exception{
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			Map<String,Object>  resultMap = this.tblyyxdfCompanyService.saveOrupdateYYCompany(pageid,priceid,company);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	/**
	 * 合同管理-财务管理-收款管理-列表
	 * collectionOrgName :付款单位
	 * contractName：对应合同
	 * contractNo：合同编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/contract/collectionManagemen", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "合同管理-财务管理-收款管理-列表")
	public String contract_collectionManagemen(HttpServletResponse response,
											   HttpServletRequest request,
											   TblContractCollection collection,
											   @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											   @RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
											   TblCyhwUnit unit,
											   @RequestParam(value = "contractname",required = false)String contractname,
											   @RequestParam(value = "contractno",required = false)String contractno)throws Exception{
		// 设置合同对象
		collection.setContract(unit);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = this.tblContractCollectionService.findCollectionListByPageInfo(contractname,contractno,collection,pageNumber,pageSize);
		JSONObject jsonObject = new JSONObject(resultMap);
		return jsonObject.toString();
	}
	
	/**
	 * 财务管理模块-收款管理 新增功能
	 * @return
	 */
	@RequestMapping(value = "/contract/saveCollectionManagemen", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "合同管理-财务管理-收款管理-新增接口")
	public String saveCollectionManagemen(HttpServletRequest request,
										  @RequestParam(value="nodeid",required=true)BigDecimal nodeid,
										  @RequestParam(value="contractid",required=true)BigDecimal contractid,
										  @RequestParam(value="invoiceid",required=true)BigDecimal invoiceid,
										  @RequestParam(value="invoicemoney",required=true)BigDecimal invoicemoney,
										  @RequestParam(value="bankbankid",required=true)BigDecimal bankbankid,
										  @RequestParam(value="bankid",required=true)BigDecimal bankid,
										  TblContractCollection collection) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = this.tblContractCollectionService.mengerCollectionEntity(nodeid,contractid,invoiceid,invoicemoney,bankbankid,bankid,collection);
		return result;
	}
	
	@RequestMapping(value = "/contract/collectionChoiceContract", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同管理-财务管理-收款管理-新增-收款合同")
	public String contract_collectionChoiceContract(HttpServletRequest request,
													@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
													@RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
													TblCyhwUnit unit,
													@RequestParam(value = "contractno",required = false)String contractno,
													@RequestParam(value = "contractname",required = false)String contractname) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = this.tblCyhwUnitService.findCollectionChoiceContract(unit,pageNumber,pageSize,contractno,contractname);
		JSONObject jsonObj = new JSONObject(resultMap);
		return jsonObj.toString();
	}
	
	/**
	 * 新增应收款项
	 */
	@RequestMapping(value = "/choicePlanNodeConlletion", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同管理-财务管理-收款管理-新增-对应收款项（合同收款管理，通过合同选择对应的履行内容）")
	public String contract_choicePlanNodeConlletion(HttpServletRequest request,
													@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
													@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
													TblContractPlannode node,
													@RequestParam(value="contractid",required=false)BigDecimal contractid
													) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			PageInfo<TblContractPlannode> pageInfo = new PageInfo<TblContractPlannode>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			node.setProjectid(contractid);
//			pageInfo.setCondition(node);
			this.tblContractPlannodeService.findPlanNodeListForCollection(pageInfo,node);
			resultMap.put("date", pageInfo);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 新增付款的银行账号
	 */
	@RequestMapping(value = "/choiceCounterPartBankInfo", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同管理-财务管理-收款管理-新增-付款银行账号（合同收款管理 选择发票信息）/付款管理-新增-收款银行账号")
	public String contract_choiceCounterPartBankInfo(HttpServletRequest request,
													 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
													 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
													 @RequestParam(value="budgetId",required=false)BigDecimal budgetId,
													 TblCounterpartBankinfo bank) throws Exception {
		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		PageInfo<TblCounterpartBankinfo> pageInfo = new PageInfo<TblCounterpartBankinfo>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		bank.setBudgetid(budgetId);
		bank.setBankstatus(BigDecimal.valueOf(1));
		//pageInfo.setCondition(bank);
		resultMap = this.tblCounterpartBankInfoService.findListByPageInfo(pageInfo,bank);
		JSONObject jsonObj = new JSONObject(resultMap);
		result = jsonObj.toString();
		return result;
	}
	
	/**
	 * 付款账户银行账号新增
	 */
	@RequestMapping(value = "/choiceOrgselfBankInfo", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同管理-财务管理-收款管理-新增-收款账户（右侧选择接口）【付款管理-新增-付款银行账号】")
	public String contract_choiceOrgselfBankInfo(HttpServletRequest request,
			@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
			@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
			TblOrgBankaccount bank) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = this.tblOrgBankAccountService.findListByPageInfo(pageNumber,pageSize,bank);
		JSONObject jsonObj = new JSONObject(resultMap);
		return jsonObj.toString();
	}
	
	/**
	 * 发票新增
	 */
	@RequestMapping(value = "/collectionChoiceInvoice", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "合同管理-财务管理-收款管理-新增-发票信息/财务管理-新增-发票号")
	public String contract_collectionChoiceInvoicen(HttpServletRequest request,
													@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
													@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
													@RequestParam(value="budgetId",required=false)BigDecimal budgetId,
													TblContractInvoicesmanagemen invoice) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = this.tblContractInvoicesmanagemenService.findInvoiceInfoListForCollection(pageNumber,pageSize,budgetId,invoice);
		JSONObject jsonObj = new JSONObject(resultMap);
		return jsonObj.toString();
	}
	
	@RequestMapping(value = "/contract/removeCollectionManagemen", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "财务管理-收款管理-删除")
	public String contract_removeCollectionManagemen(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value="collectionId",required=true)BigDecimal collectionId)throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		this.tblContractCollectionService.removeContractCollection(collectionId);
		return JsonBean.success("删除成功");
	}
	
	@RequestMapping(value = "/contract/paymentManagemen", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "财务管理-付款管理-列表")
	public String contract_paymentManagemen(HttpServletResponse response,HttpServletRequest request,
											@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											@RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
											TblCyhwUnit unit,TblContractPayment payment)throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = this.tblContractPaymenService.findPaymentManagemenByPageInfo(pageNumber,pageSize,payment,unit);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		return jsonObjectMV.toString();
	}
	
	@RequestMapping(value = "/savePaymentManagemen",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "财务管理-付款管理-新增")
	public String savePaymentManagemen(HttpServletRequest request,
									   TblContractPayment payment ,
									   @RequestParam(value="contractid",required=true)BigDecimal contractid,//合同id
									 //  @RequestParam(value="budgetid",required=false)Integer budgetid,//付款去掉
									   @RequestParam(value="applyStaffId",required=false)BigDecimal applyStaffId,//部门id
									   @RequestParam(value="applyOrgId",required=true)BigDecimal applyOrgId,//部门id
									   @RequestParam(value="bankbankid",required=false)BigDecimal bankbankid,//付款
									   @RequestParam(value="bankid",required=false)BigDecimal bankid,//收款
									   @RequestParam(value="nodeid",required=true)BigDecimal nodeid,//付款计划id
									   @RequestParam(value="invoiceid",required=true)BigDecimal invoiceid,//发票id
									   @RequestParam(value="applyDateStr",required=false)String applyDateStr,//申请日期
									   @RequestParam(value="payLateDateStr",required=false)String payLateDateStr//最晚付款日期
									) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		payment.setApplyorg(applyOrgId);
		payment.setCounterbank(bankbankid);
		payment.setOrgbank(bankid);
		if(applyDateStr != null && !"".equals(applyDateStr)) {
			payment.setApplydate(sdf.parse(applyDateStr));
		}
		if(payLateDateStr != null && !"".equals(payLateDateStr)) {
			payment.setPaymentlatedate(sdf.parse(payLateDateStr));
		}
		//payment.setBudgetid(new BigDecimal(budgetid));
		payment.setContractid(contractid);
		payment.setNodeid(nodeid);
		payment.setInvoiceid(invoiceid);
		
		return this.tblContractPaymenService.mengerPaymentInfo(payment,invoiceid,applyStaffId);
	}
	
	// 部门申请走/findOrganizationByTreeNbkz

	@RequestMapping(value = "/pjlx/findOrganizationByTreeAllss", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "付款管理-新增-经办人选择接口-左侧列表(纠纷登记-新建-纠纷承办人-左侧列表)")
	public @ResponseBody String pjlxfindOrganizationByTree(BigDecimal nodeId, String type,
		BigDecimal orgId,
		HttpServletRequest request,
		@RequestParam(value = "json",required = false)String json,
		@RequestParam(value = "idname",required = false)String idname,
		@RequestParam(value = "textname",required = false)String textname) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		if (null == nodeId) {
			nodeId = orgId;
			if (null == orgId) {
				nodeId = staff.getLinkOrg().getOrgid();
			}
		}
		if (StringUtils.isNotBlank(type)) {
			List<Tree> list = this.tblOrganizaService.getTree(nodeId);
			for (Tree tree : list) {
				if (!tree.getIsParent()) {
					tree.setTarget("mainFramex");
					tree.setUrl("/nbkz/pjlx/list?pid=" + tree.getId() + "&idname=" + idname + "&textname=" + textname);
				}
			}
			json = JSONObject.toJSONString(list);
		} else {
			List<Tree> list = this.tblOrganizaService.getNodeAll(nodeId);
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
	
	@RequestMapping(value = "/pjlx/list",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "付款管理-新增-经办人选择接口-右侧列表接口（纠纷登记-新建-纠纷继承人-左侧列表）(协商过程-新建-协商过程信息-新建-我方谈判人)")
	public String pjlxuserListss(HttpServletRequest request,TblOrganization organization,
									   @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
									   @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
									   @RequestParam(value = "pid",required = false)String pid,
									   @RequestParam(value = "localset",required = false)String localset) throws Exception {

		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		if (pid != null && !pid.equals("")) {
			orgid = new BigDecimal(pid);
		}

		Map<String, Object> resultMap = tblStaffService.findAllPageBeanPid(pageNumber, pageSize, orgid,organization);

		if (localset != null && !"".equals(localset)) {
			String[] locals = localset.split(",");
			resultMap.put("idname", locals[0]);
			resultMap.put("textname", locals[1]);
		} else {
			resultMap.put("idname", request.getParameter("idname"));
			resultMap.put("textname", request.getParameter("textname"));
		}
		resultMap.put("pid", pid);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/paymentChoiceContract",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "付款管理-新增-合同名称选择接口")
	public String contract_paymentChoiceContract(HttpServletRequest request,
			TblCyhwUnit unit,
			@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
			@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			BigDecimal pid = staff.getCurrentOrg().getOrgid();

			PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			unit.setOrgid(pid);
			this.tblCyhwUnitService.findPaymentChoiceContract(pageInfo,unit);
			resultMap.put("date", pageInfo);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;

	}
	
	@RequestMapping(value = "/choicePlanNodePayment",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "付款管理-新增-付款计划")
	public String contract_choicePlanNodePayment(HttpServletRequest request,
												 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
												 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
												 TblContractPlannode node,
												 @RequestParam(value="contractId",required=true)BigDecimal contractId) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			PageInfo<TblContractPlannode> pageInfo = new PageInfo<TblContractPlannode>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			node.setProjectid(contractId);
			this.tblContractPlannodeService.findPlanNodeListForPayment(pageInfo,node);
			resultMap.put("date", pageInfo);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/contract/removePaymentManagemen",method = {RequestMethod.POST})
	@Operation(summary = "财务管理-付款管理-删除")
	public String contract_removePaymentManagemen(HttpServletRequest request,@RequestParam(value="parmentId",required=true)BigDecimal paymentId) throws Exception {
		this.tblContractPaymenService.removePaymentInfo(paymentId);
		return JsonBean.success("修改成功");
	}
	
	@RequestMapping(value = "/contract/invoicesManageMen", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "财务管理-发票管理-列表")
	public String contract_invoicesManageMen(HttpServletResponse response,
											 HttpServletRequest request,
											 TblContractInvoicesmanagemen invoice,
											 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)throws Exception{

		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		PageInfo<TblContractInvoicesmanagemen> pageInfo = new PageInfo<TblContractInvoicesmanagemen>();

		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		BigDecimal staffid = staff.getStaffid();
		if(!JudgeRoleRight.judgeRoleRight("合同管理员",staff.getRoleNames())) {
			invoice.setCreatestaff(staffid);
		}
		invoice.setInvoiceogr(orgid);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		this.tblContractInvoicesmanagemenService.findContractInvociesManaeMenPageInfo(pageInfo,invoice);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/contract/saveInvoiceManageMen",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "财务管理-发票管理-新增")
	public String contract_saveInvoiceManageMen(HttpServletRequest request,
												@RequestParam(value="budgetId",required=true)BigDecimal budgetId,
												@RequestParam(value="startdate1",required=true)String startdate1,
												@RequestParam(value="enddate1",required=true)String enddate1,
												TblContractInvoicesmanagemen invoice) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = this.tblContractInvoicesmanagemenService.mengerInvoicemanageMen(budgetId,startdate1,enddate1,invoice);
		return result;
	}
	
	@RequestMapping(value = "/contract/invoiceCounterpartInfoList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "财务管理-发票管理-新增-票据相对方选择接口")
	public String contract_invoiceCounterpartInfoList(HttpServletRequest request,
			@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
			@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
			TblCyhwProjectbudget tcpb) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		BigDecimal pid = staff.getCurrentOrg().getOrgid();

		PageInfo<TblCyhwProjectbudget> pageInfo = new PageInfo<TblCyhwProjectbudget>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		tcpb.setOrgid(pid);
		tcpb.setInspectionstatus(6);
		tcpb.setIsblack(2);
		tblCyhwProjectbudgetService.invoiceCounterpartInfoListByPageInfo(pageInfo,tcpb);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		return jsonObjectMV.toString();
	}

	@RequestMapping(value = "/contract/getContractBudgetList",method = {RequestMethod.GET})
	@Operation(summary = "财务管理-收付款管理-选择合同相对方")
	public String getContractBudgetList(HttpServletRequest request,
													  @RequestParam(value = "contractId") BigDecimal contractId) throws Exception {

		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (Objects.isNull(staff)) {
			throw new BizException("用户未登录！");
		}

		List<TblCyhwProjectbudget> contractBudgetList = tblCyhwProjectbudgetService.getContractBudgetList(contractId);
		resultMap.put("date", contractBudgetList);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		return jsonObjectMV.toString();
	}

	@RequestMapping(value = "/contract/modifyInvoiceStatus", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "财务管理-发票管理-操作（状态）")
	public String contract_modifyInvoiceStatusn(HttpServletResponse response,
		HttpServletRequest request,
		@RequestParam(value="invoiceId",required=true)BigDecimal invoiceId,
		@RequestParam(value="status",required=true)Integer status)throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		this.tblContractInvoicesmanagemenService.modifyInvoiceStatus(invoiceId,status);
		return JsonBean.success("修改成功");
	}
	
	@RequestMapping(value = "/contract/removeInvoiceManageMen", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "财务管理-发票管理-删除")
	public String contract_removeInvoiceManageMen(HttpServletResponse response,
												  HttpServletRequest request,
												  @RequestParam(value="invoiceId",required=true)BigDecimal invoiceId)throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		this.tblContractInvoicesmanagemenService.removeInvoiceInfo(invoiceId);
		return JsonBean.success("删除成功");
	}
	
	/**
	 * 发票管理查看详情
	 */
	@RequestMapping(value = "/contract/viewInvoice", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "财务管理-发票管理-查看详情")
	public String contract_viewInvoice(HttpServletResponse response,HttpServletRequest request,
			@RequestParam(value="invoiceId",required=true)BigDecimal invoiceId)throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblContractInvoicesmanagemen invoice = this.tblContractInvoicesmanagemenService.findInvoiceInfoByInvoiceId(invoiceId);
		resultMap.put("invoice",invoice);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 2-已开票，3-未收款，4-已收款，5-已退票
	 */
	@RequestMapping(value = "/contract/viewCollectionManagemen",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "财务管理-收款管理-查看详情")
	public String contract_viewCollectionManagemen(HttpServletRequest request,
												   @RequestParam(value="collectionId",required=true)BigDecimal collectionId) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblContractCollection collection = this.tblContractCollectionService.findCollectionInfoByCollectId(collectionId);
			if (Objects.nonNull(collection)){
				if (Objects.nonNull(collection.getStaffid1())) {
					collection.setRealname1(tblStaffService.getStaffName(collection.getStaffid1()));
				}
				if (Objects.nonNull(collection.getStaffid2())) {
					collection.setRealname2(tblStaffService.getStaffName(collection.getStaffid2()));
				}
				if (Objects.nonNull(collection.getStaffid3())) {
					collection.setRealname3(tblStaffService.getStaffName(collection.getStaffid3()));
				}
				if (Objects.nonNull(collection.getStaffid4())) {
					collection.setRealname4(tblStaffService.getStaffName(collection.getStaffid4()));
				}
				if (Objects.nonNull(collection.getStaffid5())) {
					collection.setRealname5(tblStaffService.getStaffName(collection.getStaffid5()));
				}
				if (StringUtils.isNotBlank(collection.getStaffids1())) {
					collection.setRealnames1(tblStaffService.getStaffNames(collection.getStaffids1()));
				}
				if (StringUtils.isNotBlank(collection.getStaffids2())) {
					collection.setRealnames2(tblStaffService.getStaffNames(collection.getStaffids2()));
				}
				if (StringUtils.isNotBlank(collection.getStaffids3())) {
					collection.setRealnames3(tblStaffService.getStaffNames(collection.getStaffids3()));
				}
				if (StringUtils.isNotBlank(collection.getStaffids4())) {
					collection.setRealnames4(tblStaffService.getStaffNames(collection.getStaffids4()));
				}
				if (StringUtils.isNotBlank(collection.getStaffids5())) {
					collection.setRealnames5(tblStaffService.getStaffNames(collection.getStaffids5()));
				}
				if (Objects.nonNull(collection.getOrgid1())) {
					collection.setOrgname1(tblOrganizaService.getOrgName(collection.getOrgid1()));
				}
				if (Objects.nonNull(collection.getOrgid2())) {
					collection.setOrgname2(tblOrganizaService.getOrgName(collection.getOrgid2()));
				}
				if (Objects.nonNull(collection.getOrgid3())) {
					collection.setOrgname3(tblOrganizaService.getOrgName(collection.getOrgid3()));
				}
				if (Objects.nonNull(collection.getOrgid4())) {
					collection.setOrgname4(tblOrganizaService.getOrgName(collection.getOrgid4()));
				}
				if (Objects.nonNull(collection.getOrgid5())) {
					collection.setOrgname5(tblOrganizaService.getOrgName(collection.getOrgid5()));
				}
				if (StringUtils.isNotBlank(collection.getOrgids1())) {
					collection.setOrgnames1(tblOrganizaService.getOrgNames(collection.getOrgids1()));
				}
				if (StringUtils.isNotBlank(collection.getOrgids2())) {
					collection.setOrgnames2(tblOrganizaService.getOrgNames(collection.getOrgids2()));
				}
				if (StringUtils.isNotBlank(collection.getOrgids3())) {
					collection.setOrgnames3(tblOrganizaService.getOrgNames(collection.getOrgids3()));
				}
				if (StringUtils.isNotBlank(collection.getOrgids4())) {
					collection.setOrgnames4(tblOrganizaService.getOrgNames(collection.getOrgids4()));
				}
				if (StringUtils.isNotBlank(collection.getOrgids5())) {
					collection.setOrgnames5(tblOrganizaService.getOrgNames(collection.getOrgids5()));
				}
			}
			resultMap.put("collection", collection);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 付款管理点击标题查看详情
	 */
	@RequestMapping(value = "/contract/viewPaymentManagemen",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "财务管理-付款管理-查看详情")
	public String contract_viewPaymentManagemen(HttpServletRequest request,
												@RequestParam(value="paymentId",required=true)BigDecimal paymentId) throws Exception {

		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblContractPayment payment = this.tblContractPaymenService.findPaymentInfoByParmentId(paymentId);
		resultMap.put("payment", payment);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/contract/bankAccountDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "财务管理-银行账户-查看详情")
	public String  contract_bankAccountDetail(HttpServletRequest request,
												   @RequestParam(value="bankId",required=true)BigDecimal bankId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblOrgBankaccount bank = this.tblOrgBankAccountService.findById(bankId);
		resultMap.put("bank", bank);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/contract/bankAccountList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "财务管理-银行账户-列表")
	public String contract_bankAccountList(HttpServletRequest request,
												 @RequestParam(value="choiceSearch",required=false)String choiceSearch,
												 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
												 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
												 TblOrgBankaccount bank) {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
			if(staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return String.valueOf(resultMap);
			}
			BigDecimal pid = staff.getCurrentOrg().getOrgid();
			PageInfo<TblOrgBankaccount> pageInfo = new PageInfo<TblOrgBankaccount>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			tblOrgBankAccountService.findListByPageInfoPid(pageInfo,pid,bank);
			resultMap.put("date", pageInfo);
			if(choiceSearch == null || "".equals(choiceSearch)) {
	  			choiceSearch = "hide";
	  		}
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		resultMap.put("choiceSearch", choiceSearch );
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/contract/orgBankInfoSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
    @ResponseBody
	@Operation(summary = "财务管理-银行账户-新增")
	public String contract_orgBankInfoSave(HttpServletRequest request,
										   TblOrgBankaccount bank,
										    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	try {
    		TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			if(bank.getBankid() != null) {
				TblOrgBankaccount oldEntity = this.tblOrgBankAccountService.findByBankId(bank.getBankid());
				oldEntity.setBankaccname(bank.getBankaccname());
				oldEntity.setBankaccnum(bank.getBankaccnum());
				oldEntity.setBankcode(bank.getBankcode());
				oldEntity.setBankkhyh(bank.getBankkhyh());
				oldEntity.setBankname(bank.getBankname());
				oldEntity.setBankstate(bank.getBankstate());
				oldEntity.setBankstatus(bank.getBankstatus());
				oldEntity.setBankyhlb(bank.getBankyhlb());
				this.tblOrgBankAccountService.UpdateModifyBankInfo(oldEntity);
				return JsonBean.success("修改成功");
			}else {
				BigDecimal pid = staff.getCurrentOrg().getOrgid();
				bank.setOrgid(pid);
				bank.setCreatedate(new Date());
				bank.setBankid(RandomUtil.uuBigDecimalId());
				this.tblOrgBankAccountService.savebankInfo(bank);
				return JsonBean.success("新增成功!");
			}
		} catch (Exception e) {
			log.error("异常信息：", e);
			//return JsonBean.error("失败");
		}
		return bank.getBankid().toString();
	}
	
	@RequestMapping(value = "/contract/removeOrgBankInfo",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "财务管理-银行账户-删除")
	public String contract_removeOrgBankInfo(HttpServletRequest request,@RequestParam(value="bankId",required=true)BigDecimal bankId) throws Exception {
		String result = "0";
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			result = tblOrgBankAccountService.removeOrgBankInfo(bankId);
		} catch (Exception e) {
			log.error("异常信息：", e);
			//return JsonBean.error("删除失败");
		}
		return JsonBean.success("删除成功");
	}
	
	/**
	 * 合同首页查询柱状图饼状图
	 */
	@RequestMapping(value ="/contract/getReportContractData",method = {RequestMethod.GET})
	@Operation(summary = "合同首页")
	public @ResponseBody String getReportContractData(HttpServletRequest request,@RequestParam(value="year",required=false)Integer year,
			@RequestParam(value="month",required=false)Integer month) throws Exception{
		String result = null;
  		try {
  			log.info("合同首页接口");
  			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.getReportContractData(year,month);
			resultMap.put("month", month);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}
	
	/**
	 * 纠纷管理
	 */
	@RequestMapping(value = "/legal/getDisputeNo",method = {RequestMethod.GET},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷登记-查看详情")
	public JsonBean legal_getDisputeNo(HttpServletRequest request) throws Exception {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			jsonBean =  this.tblLegalDisputregistrationService.getDisputeNo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 法务管理 - 纠纷登记 - 查看
	 * @param request
	 * @param disputeId
	 * @return
	 */
	@RequestMapping(value = "/legal/disputeRegisterDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷登记-查看详情")
	public String legal_disputeRegisterDetail(HttpServletRequest request,
													@RequestParam(value="disputeId",required=true)BigDecimal disputeId) throws Exception {

		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findBydisputeId(disputeId);
		resultMap.put("dispute", dispute);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 纠纷等级页面查询
	 * @param request
	 * @param dispute
	 * @param pageNumber
	 * @param pageSize
	 * @param companyId
	 * @param flowid
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/legal/caseInformationList",method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@Operation(summary = "纠纷登记页面列表")
	public String legal_caseInfomationList(HttpServletRequest request,
										   TblLegalDisputregistration dispute,
										   @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
										   @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
										   @RequestParam(value = "companyId",required = false)String companyId) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal pid = staff.getCurrentOrg().getOrgid();

		PageInfo<TblLegalDisputregistration> pageInfo = new PageInfo<TblLegalDisputregistration>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		dispute.setContractname(dispute.getContractname());
		this.tblLegalDisputregistrationService.findListByPageInfo(companyId,pageInfo,dispute,pid);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/legal/checkCaseList",method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@Operation(summary = "隶属纠纷列表")
	public String checkCaseList(HttpServletRequest request,
										   TblLegalDisputregistration dispute,
										   @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
										   @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
										   @RequestParam(value = "companyId",required = false)String companyId,
										   @RequestParam(value="flowid",required=false)String flowid) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
//		TblFlow flow = tblFlowService.findById(flowid);
		PageInfo<TblLegalDisputregistration> pageInfo = new PageInfo<TblLegalDisputregistration>();
		//dispute.setLinkorg(pid);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		//pageInfo.setCondition(dispute);
		dispute.setContractname(dispute.getContractname());
		dispute.setDisputestatus(6);
		this.tblLegalDisputregistrationService.findListLSByPageInfo(companyId,pageInfo,dispute,pid);
		resultMap.put("date", pageInfo);
//		resultMap.put("flow", flow);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 法务管理 - 经济登记 - 新增
	 */
	@RequestMapping(value = "/legal/caseInformationSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-纠纷登记-新增")
	public String legal_caseInformationSave(HttpServletRequest request,
											 TblLegalDisputregistration dispute,
											 @Parameter(description = "contractid", required = false)@RequestParam(value = "contractid",required = false)BigDecimal contractid,
											 @Parameter(description = "zxstaffid", required = false)@RequestParam(value = "zxstaffid",required = false)BigDecimal zxstaffid,
											 @Parameter(description = "enddate1", required = false)@RequestParam(value = "enddate1",required = false)String enddate1) throws Exception {
		try {
			//解析用户 token
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			BigDecimal pid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffid = staff.getStaffid();
			//设置案件信息
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 设置纠纷承担者为当前员工ID
			dispute.setDisputeundertaker(staffid);
			if (contractid != null) {
				// 根据合同ID查找合同信息
				TblCyhwUnit unit = this.tblCyhwUnitService.findContractById(contractid);
				// 修改合同状态
				this.tblCyhwUnitService.modifyJiuFenContractStatus(new BigDecimal(unit.getContractstatus()), 12, unit.getContractid());
				dispute.setContractinfo(contractid);
			}
			if (StringUtils.isNotBlank(enddate1)) {
				// 设置最后处理日期
				dispute.setLastdealdate(sdf.parse(enddate1));
			}
			dispute.setLinkorg(pid);
			dispute.setCreatestaff(staffid);
			dispute.setDisputestatus(0);
			dispute.setCreatetime(new Date());
			//调用服务层方法保存案件信息
			this.tblLegalDisputregistrationService.addDiputregistration(dispute);
			//返回结果
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			resultMap.put("data", dispute.getDisputeid());
			resultMap.put("msg", "新增成功");
			resultMap.put("code", 1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			return jsonObjectMV.toString();
		}catch (Exception e) {
			log.error("接口异常：",e);
		}
		return JsonBean.error("保存失败");
	}
	
	@RequestMapping(value = "/legal/caseInformationModify",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-纠纷登记-修改")
	public String legal_caseInformationModify(HttpServletRequest request,
											   TblLegalDisputregistration dispute,
											   @RequestParam(value="contractId",required=false)BigDecimal contractId,
											   @RequestParam(value="zxstaffid",required=false)BigDecimal zxstaffid,
											   @RequestParam(value="enddate1",required=false)String enddate1,
											   @RequestParam(value="attids",required=false)String attids) {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TblLegalDisputregistration oldDispute = this.tblLegalDisputregistrationService.findByDisputeId(dispute.getDisputeid());
//			if(zxstaffid != null) {
//				TblStaff staff = this.tblStaffService.findById(zxstaffid.toString());
//
//			}
			oldDispute.setDisputeundertaker(zxstaffid);
//			dispute.setContractinfo(contractId);
			if(oldDispute.getContractinfo() != null) {
				TblCyhwUnit oldunit = this.tblCyhwUnitService.findContractById(contractId);
				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(oldunit.getContractstatus()),oldunit.getHistoryStatus(),oldDispute.getContractinfo());
			}
			if(contractId != null) {
				TblCyhwUnit unit = this.tblCyhwUnitService.findContractById(contractId);
				this.tblCyhwUnitService.modifyJiuFenContractStatus(new BigDecimal(unit.getContractstatus()), 12, unit.getContractid());
				dispute.setContractinfo(contractId);
				oldDispute.setContractinfo(contractId);
			}
			
//			if(enddate1 != null || !"".equals(enddate1)){
//				oldDispute.setLastdealdate(sdf.parse(enddate1));
//			}
			if(StringUtils.isNotBlank(enddate1)){
				oldDispute.setLastdealdate(sdf.parse(enddate1));
			}
			//oldDispute.setLastdealdate(dispute.getLastdealdate());
			oldDispute.setAttorney(dispute.getAttorney());
			oldDispute.setAttorneyphont(dispute.getAttorneyphont());
			oldDispute.setIsattorney(dispute.getIsattorney());
			oldDispute.setDefendant(dispute.getDefendant());
			oldDispute.setDisputecours(dispute.getDisputecours());
			oldDispute.setDisputeitem(dispute.getDisputeitem());
			oldDispute.setDisputeno(dispute.getDisputeno());
			oldDispute.setDisputetype(dispute.getDisputetype());
			oldDispute.setIsuegent(dispute.getIsuegent());
			oldDispute.setWhethersued(dispute.getWhethersued());
			oldDispute.setPlaintiff(dispute.getPlaintiff());
			oldDispute.setUrgentmemo(dispute.getUrgentmemo());
			oldDispute.setSolutionsuggestions(dispute.getSolutionsuggestions());
			oldDispute.setBusinessdate(dispute.getBusinessdate());
			if(dispute!=null && dispute.getLitigationamount()!=null)
			oldDispute.setLitigationamount(dispute.getLitigationamount());
			if (StringUtils.isNotBlank(attids)){
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					TblAttachment att = tblAttachmentService.findById(ids[i].trim());
					oldDispute.getAttList().add(att);
				}
			}
			this.tblLegalDisputregistrationService.modifyDiputregistration(oldDispute);
			//return JsonBean.success("修改成功");
			/*TblCyhwUnit tcu = new TblCyhwUnit();
			tcu.setJbstaff(dispute.getJbstaff());
			tcu.setContractname(dispute.getContractname());
			this.tblCyhwUnitService.saveCyhwUnitTcu(tcu);*/
			return JsonBean.success("修改成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return dispute.getDisputeid().toString();
	}
	
	@RequestMapping(value = "/legal/findContractInfo",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷登记-新增-合同名称")
	public String legal_findContractInfo(HttpServletRequest request,
										 TblCyhwUnit tcu,
										 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
										 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		PageInfo<TblCyhwUnit> pageInfo = new PageInfo<TblCyhwUnit>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
//		TblCyhwUnit tcu = new TblCyhwUnit();
		tcu.setOrgid(orgid);
		pageInfo.setCondition(tcu);
		tblCyhwUnitService.findLegalContractListByPageInfo(pageInfo,tcu);
		resultMap.put("data", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/uploadutf8",method = {RequestMethod.POST})
	@Operation(summary = "法务管理-纠纷登记-新增-上传附件")
	public String uploadutf8(HttpServletRequest request, HttpServletResponse response, String filecollbackurl) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String myFileName = "";
		// 判断 request 是否有文件上传,即多部分请求
		try {
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						myFileName = file.getOriginalFilename();
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {
							System.out.println(myFileName);
							int lastIndexOf = myFileName.lastIndexOf(".");
							String type = myFileName.substring(lastIndexOf);
							// 重命名上传后的文件名
							long timeInMillis = Calendar.getInstance().getTimeInMillis();
							String fileName = timeInMillis + type;
							// 定义上传路径

							String path = DOCDIC + "/" + fileName;
							File localFile = new File(path);
							if (!localFile.exists()) {
								localFile.mkdirs();
							}

							String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
							String newname=myFileName.replace(oldname, timeInMillis+"");
							try {
								boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
								if(flag){
									logger.info("上传成功");
								}else{
									logger.info("上传失败");
								}
							} catch (Exception e) {
								log.error("异常信息：", e);
							}
							file.transferTo(localFile);
							resultMap.put("attname", URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
							resultMap.put("attsize", file.getSize()/1000);
							resultMap.put("attpath", newname);
							resultMap.put("fileName", myFileName);
							resultMap.put("id", request.getParameter("id"));
						}
					}
					Enumeration<String> enu = request.getParameterNames();
					while (enu.hasMoreElements()) {
						String paraName = enu.nextElement();
						if (!paraName.equals("url")) {
							resultMap.put(paraName, request.getParameter(paraName));
						}
					}
					// 记录上传该文件后的时间
					int finaltime = (int) System.currentTimeMillis();
					System.out.println(finaltime - pre);
				}

			}
		} catch (IOException e) {
			log.error("异常信息：", e);
		}
		return "redirect:" + filecollbackurl;
	}
	
	@RequestMapping(value = "/sjzb/xmzlzb_fj", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "上传附件-返回attid")
	public @ResponseBody String xmzlzb_fj(HttpServletRequest request,
										  HttpServletResponse response,TblAttachment att) throws Exception {
		TblStaffUtil staff = userProvider.get();
		response.reset();
		try {
			att.setAttname(URLDecoder.decode(att.getAttname(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("异常信息：", e);
		}
		att.setUploader(staff.getRealname());
		att.setUploadtime(new Date());
		att.setAttid(RandomUtil.uuBigDecimalId());
		tblAttachmentService.add(att);//原
		TblCyhwBasicuninspection tcp = new TblCyhwBasicuninspection();
		tcp.setFilename(att.getFileName());
		tcp.setInspectionid(RandomUtil.uuBigDecimalId());
		tblCyhwBasicuninspectionService.insertBybudget(tcp);
		return JSONObject.toJSONString(att);
	}
	
	@RequestMapping(value = "/deleteFileRelation",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "删除附件")
	public @ResponseBody String deleteFileRelation(HttpServletRequest request,
						@RequestParam(value = "attid",required = false)String attid) throws Exception{
			tblLegalDisputregistrationService.deleteRelation(attid);
			tblAttachmentService.delete(attid);
			return JsonBean.success();
	}
	
	@RequestMapping(value = "/downloadFile",produces = "application/json; charset=utf-8")
	@Operation(summary = "附件下载")
	public ResponseEntity<byte[]> download(BigDecimal id, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return null;
        }
		TblAttachment tblAttachment = this.tblAttachmentService.get(id);
		//String path = DOCDIC +separator+ tblAttachment.getAttpath();
		String path = fileUrl + tblAttachment.getAttpath();
		File file = new File(path);
		HttpHeaders headers = new HttpHeaders();
		String fileName = processFileName(request, tblAttachment.getAttname());// 为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.set("Content-disposition", "attachment;filename=" + fileName);
		headers.set("Content-Length", String.valueOf(file.length()));
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
	}
	
	public static String processFileName(HttpServletRequest request, String fileNames) {
		String codedfilename = null;
		try {
			Integer index = fileNames.indexOf(".");
			if (index < 0) {
				fileNames = fileNames + ".xls";
			}
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

				String name = java.net.URLEncoder.encode(fileNames, "UTF8");

				codedfilename = name;
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

				codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
			}
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return codedfilename;
	}
	
	@RequestMapping(value = "/legal/caseInformationRemove",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-纠纷登记-删除")
	public String legal_caseInformationRemove(HttpServletRequest request,
											  @RequestParam(value="disputeId",required=false)BigDecimal disputeId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		TblLegalDisputregistration oldDispute = this.tblLegalDisputregistrationService.findByDidputeid(disputeId);
		//TblCyhwUnit oldunit = oldDispute.getContract();
		if (oldDispute != null) {
			//查询
			TblCyhwUnit oldunit = this.tblLegalArbitratsettlementService.findContractByDisputeId(disputeId);
			if (oldunit!=null) {
				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(oldunit.getContractstatus()),oldunit.getHistoryStatus(),oldDispute.getContractinfo());
			}
		}
		//是否删除具体文件？
		//删除附件关联表
		this.tblLegalDisputregistrationService.deleteAttacheMents(1,disputeId);
		this.tblLegalDisputregistrationService.removecaseInformation(disputeId);
		return JsonBean.success("删除成功");
	}
	
	/**
	 * 协商过程页面数据查询
	 */
	@RequestMapping(value = "/legal/negotiatedSettlementInfoList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-协商过程-列表")
	public String legal_negotiatedSettlementInfoList(HttpServletRequest request,
													 TblLegalNegotiatedsettlemen negotia,
											@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											@RequestParam(value="flowid",required=false)String flowid,
											 @RequestParam(value = "disputeid",required = false)BigDecimal disputeid) throws Exception {
		//TblOrganization orgInfo = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		//通过token获取当前用户
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		//通过token获取当前用户的OrgId
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		//TblFlow flow = tblFlowService.findById(flowid);
		if (pageNumber == null) {
			pageNumber = 1;
		}
		PageInfo<TblLegalNegotiatedsettlemen> pageInfo = new PageInfo<TblLegalNegotiatedsettlemen>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		this.tblLegalNegotiatedsettlemenService.findListByPage(pageInfo,negotia,pid,disputeid);
		resultMap.put("date", pageInfo);
		//resultMap.put("flow", flow);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 点击纠纷类型查询对应详情
	 */
	@RequestMapping(value = "/legal/negotiatedSettlementDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-协商过程-详情")
	public String  legal_negotiatedSettlementDetail(HttpServletRequest request,@RequestParam(value="negotiaId",required=true)BigDecimal negotiaId) throws Exception {
		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblLegalNegotiatedsettlemen negotiated = this.tblLegalNegotiatedsettlemenService.findById(negotiaId);
		resultMap.put("negotiated", negotiated);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 协商过程新增
	 */
	@RequestMapping(value = "/legal/negotiatedSettlemenSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-协商过程-新增/修改")
	public String legal_negotiatedSettlemenSave(HttpServletRequest request,
												 TblLegalNegotiatedsettlemen negotiated,
												 @RequestParam(value="disputeId",required=false)BigDecimal disputeId,
												 @RequestParam(value="attids",required=false)String attids) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffid = staff.getStaffid();
			if(disputeId != null) {
				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findByDisputeId(disputeId);
//				TblCyhwUnit unit = dispute.getContract();
//				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),13,unit.getContractid());
				negotiated.setDispuinfo(dispute.getDisputeid());
			}
			//LocalDateTime dateTime = LocalDateTime.now();
			//negotiated.setCreatetime(dateTime);
			negotiated.setLinkorg(orgid);
			negotiated.setCreatestaff(staffid);
			negotiated.setCreatetime(new Date());
			negotiated.setSchemeStatus(new BigDecimal("0"));
			negotiated.setNegotiaid(RandomUtil.uuBigDecimalId());
			this.tblLegalNegotiatedsettlemenService.addDiputregistration(negotiated,attids);
			//return JsonBean.success("新增成功");
			resultMap.put("data", negotiated.getNegotiaid() );
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			//return JsonBean.error("失败");
			log.error("异常信息：", e);
		}
		return JsonBean.error("保存失败");
	}
	
	@RequestMapping(value = "/legal/negotiatedSettlemenModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-协商过程-新建-协商过程基本信息-修改保存按钮")
	public String legal_negotiatedSettlemenModify(HttpServletRequest request,
												   TblLegalNegotiatedsettlemen negotiated,
												   @RequestParam(value="disputeId",required=false)BigDecimal disputeId,
												   @RequestParam(value="attids",required=false)String attids) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalNegotiatedsettlemen oldNegotiated = this.tblLegalNegotiatedsettlemenService.findById(negotiated.getNegotiaid());
				if(disputeId != null) {
				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findById(disputeId);
//				TblCyhwUnit unit = dispute.getContract();
//				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),14,unit.getContractid());
//				TblCyhwUnit oldunit = oldNegotiated.getDispu().getContract();
//				this.tblCyhwUnitService.modifyContract(oldunit.getHiscontractstatus(),oldunit.getHiscontractstatus(),oldunit.getContractid());
				oldNegotiated.setDispuinfo(dispute.getDisputeid());
			}
			oldNegotiated.setCounterpart(negotiated.getCounterpart());
			oldNegotiated.setCounterpartphone(negotiated.getCounterpartphone());
			oldNegotiated.setIsaggree(negotiated.getIsaggree());
			oldNegotiated.setNegetiaresult(negotiated.getNegetiaresult());
			oldNegotiated.setJudicialsettlement(negotiated.getJudicialsettlement());
			oldNegotiated.setCourtname(negotiated.getCourtname());
			oldNegotiated.setSolutionmode(negotiated.getSolutionmode());
			if (StringUtils.isNotBlank(attids)){
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					TblAttachment att = tblAttachmentService.findById(ids[i].trim());
					oldNegotiated.getAttList().add(att);
				}
			}
			this.tblLegalNegotiatedsettlemenService.modifyNegotiatedSettlement(oldNegotiated);
			return JsonBean.success("修改成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return negotiated.getNegotiaid().toString();
	}
	
	@RequestMapping(value = "/legal/legalNegotiatedSettlemenRemove",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-协商过程-删除")
	public String legal_legalNegotiatedSettlemenRemove(HttpServletRequest request,@RequestParam(value="negotiaId",required=false)BigDecimal negotiaId) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalNegotiatedsettlemen negotiated = this.tblLegalNegotiatedsettlemenService.findById(negotiaId);
//			TblCyhwUnit unit = negotiated.getDispuIn().getContract();
//			this.tblCyhwUnitService.modifyContrac(unit.getHiscontractstatus(),new BigDecimal(12),unit.getContractid());
			if (negotiated != null && negotiated.getDisputeid() != null) {
				TblCyhwUnit oldunit = this.tblLegalArbitratsettlementService.findContractByDisputeId(negotiated.getDisputeid());
				if (oldunit!=null) {
					this.tblCyhwUnitService.modifyContractStatus(oldunit.getHiscontractstatus(), 12, oldunit.getContractid());
				}
			}
			//删除过程阶段信息
			List<TblLegalNegotiaterecord> recordList = this.tblLegalNegotiateRecordService.findListBynegotiaId(negotiaId);
			if (recordList !=null) {
				recordList.forEach((record) -> {
					this.tblLegalNegotiateRecordService.removeNegitiateRecord(record.getRecordid());
				});
			}

			//删除附件关联表
			this.tblLegalDisputregistrationService.deleteAttacheMents(2, negotiaId);
			this.tblLegalNegotiatedsettlemenService.removeLegalNegotiatedSettlemen(negotiaId);
			return JsonBean.success("删除成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return JsonBean.error("删除失败");
	}
	
	@RequestMapping(value = "/legal/findcaseInformationInfo",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-协商过程基本信息-新增-隶属纠纷选择接口")
	public String legal_findcaseInformationInfo(HttpServletRequest request,TblLegalDisputregistration dispute,
													  @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
													  @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
												@RequestParam(value="choiceType",required=false)Integer choiceType,
												@RequestParam(value="oid",required=false)BigDecimal oid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal pid = staff.getCurrentOrg().getOrgid();

		PageInfo<TblLegalDisputregistration> pageInfo = new PageInfo<TblLegalDisputregistration>();
		//dispute.setLinkorg(pid);
		dispute.setUniqueResult(1);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		//pageInfo.setCondition(dispute);
		this.tblLegalDisputregistrationService.findListByPageInfoDispute(pageInfo,dispute,pid,choiceType,pid);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/legal/removeNegotiatedRecord",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-协商过程-新建-协商过程信息-删除")
	public void legal_removeNegotiatedRecord(HttpServletRequest request,
											 @RequestParam(value="recordId",required=false)BigDecimal recordId) throws Exception {
		//TblLegalNegotiatedsettlemen record = this.tblLegalNegotiatedsettlemenService.findById(recordId);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ;
        }
		this.tblLegalNegotiateRecordService.removeNegitiateRecord(recordId);
	}


	@RequestMapping(value = "/legal/negotiatedSettlemenToModify",method = {RequestMethod.POST})
	@Operation(summary = "法务管理-协商过程基本信息-新增回显")
	public String legal_negotiatedSettlemenToModify(HttpServletRequest request,
														  @RequestParam(value="negotiaId",required=true)BigDecimal negotiaId,@RequestParam(value="choiceSearch",required=false)String choiceSearch,
														  @RequestParam(value="flowid",required=true)String flowid) throws Exception {
		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		//TblFlow flow = tblFlowService.findById(flowid);
		TblLegalNegotiatedsettlemen negotiated = this.tblLegalNegotiatedsettlemenService.findById(negotiaId);
		List<TblLegalNegotiaterecord> recordList = this.tblLegalNegotiateRecordService.findListBynegotiaId(negotiaId);
		resultMap.put("recordList", recordList);
		//resultMap.put("flow", flow);
		resultMap.put("negotiated", negotiated);
		resultMap.put("choiceSearch", choiceSearch);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}



	@RequestMapping(value = "/legal/LegalnegotiatedRecordSave",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-协商过程-新建-协商过程信息-新建按钮（协商过程-新建-协商阶段-保存）")
	public BigDecimal legal_negotiatedRecordSave(HttpServletRequest request,
											  TblLegalNegotiaterecord record,
											  @RequestParam(value="negotiaid",required=false)BigDecimal negotiaid,
											  @RequestParam(value="zxstaffid",required=false)String zxstaffid//我方谈判人
											  ) throws Exception {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return null;
	        }
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffid = staff.getStaffid();//录入人
			record.setLinkorg(orgid);
			record.setCreatestaff(staffid);
			record.setOurnegotiator(new BigDecimal(zxstaffid));
			record.setNegotiateinfo(negotiaid);
			record.setRecordid(RandomUtil.uuBigDecimalId());
			this.tblLegalNegotiateRecordService.saveNegotiateRecord(record);
//			TblStaff ts = new TblStaff();
//			ts.setUsername(record.getZxstaffname());//我方谈判人
//			ts.setRealname(record.getCreatename());//录入人
//			this.tblStaffService.saveTs(ts);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return record.getRecordid();
	}


	@RequestMapping(value = "/legal/negotiateRecordToModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "协商过程-新建-协商阶段-保存-回显")
	public String legal_negotiateRecordToModify(HttpServletRequest request,
													  @RequestParam(value="recordId",required=true)BigDecimal recordId,
													  @RequestParam(value="flowid",required=true)BigDecimal flowid,
												@RequestParam(value="negotiaId",required=true)BigDecimal negotiaId) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
//			if (negotiaId != null){
//				TblLegalNegotiatedsettlemen tll =  tblLegalNegotiatedsettlemenService.findNegotiaId(negotiaId);
//				resultMap.put("tll",tll);
//			}else {
//
//			}
			TblLegalNegotiaterecord record = this.tblLegalNegotiateRecordService.findById(recordId);
			resultMap.put("record", record);
			resultMap.put("flowid", flowid);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/negotiateRecord",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "协商过程-新建-协商阶段-列表")
	public String legal_negotiateRecordToModify(HttpServletRequest request,
												@RequestParam(value="flowid",required=true)BigDecimal flowid,
												@Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
												@Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
												@RequestParam(value="negotiaId",required=true)BigDecimal negotiaId) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		PageInfo<TblLegalNegotiaterecord> pageInfo = new PageInfo<TblLegalNegotiaterecord>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);

		this.tblLegalNegotiateRecordService.findByNegotiaId(pageInfo,negotiaId);
		resultMap.put("date", pageInfo);
		resultMap.put("flowid", flowid);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}




	@RequestMapping(value = "/legal/negotiatedRecordModify",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-协商过程-新建-协商过程信息-协商阶段-修改保存按钮")
	public BigDecimal legal_negotiatedRecordModify(HttpServletRequest request,
												TblLegalNegotiaterecord record,
												@RequestParam(value="zxstaffid",required=false)String zxstaffid) {
		try {
			TblStaffUtil loginstaff = userProvider.get();
	        if (loginstaff == null) {
	        	return null;
	        }
			TblLegalNegotiaterecord oldRecord = this.tblLegalNegotiateRecordService.findById(record.getRecordid());
			
			if(zxstaffid != null) {
				TblStaff staff = this.tblStaffService.findById(zxstaffid.toString());
				oldRecord.setOurnegotiator(staff.getStaffid());
			}
			oldRecord.setRecordcounterpart(record.getRecordcounterpart());
			oldRecord.setNegotiationmode(record.getNegotiationmode());
			oldRecord.setNegotiationrecord(record.getNegotiationrecord());
			oldRecord.setNegetiationmemoe(record.getNegetiationmemoe());
			oldRecord.setCourtname(record.getCourtname());
			oldRecord.setCourtparter(record.getCourtparter());
			oldRecord.setCourtlink(record.getCourtlink());
			this.tblLegalNegotiateRecordService.modifyNegotiateRecord(oldRecord);
			TblStaff ts = new TblStaff();
			ts.setUsername(oldRecord.getZxstaffname());//我方谈判人
			ts.setRealname(oldRecord.getCreatename());//录入人
			this.tblStaffService.updateTs(ts);
		} catch (Exception e) {
		}
		return record.getRecordid();
	}





	@RequestMapping(value = "/deleteFileRelationAtt",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "协商过程-新建-协商过程信息-最下面删除附件")
	public @ResponseBody String deleteFileRelationAtt(HttpServletRequest request,
												   @RequestParam(value = "attid",required = false)String attid) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		tblLegalNegotiateeAttService.deleteRelation(attid);
		//TblAttachment att = tblAttachmentService.findById(attid);
		tblAttachmentService.delete(attid);
		return JsonBean.success();
	}

	/**
	 * 诉讼过程信息查询
	 * @param request
	 * @param litigation
	 * @param pageNumber
	 * @param pageSize
	 * @param flowid
	 * @param disputeid
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/legal/litigationSettlement",method = {RequestMethod.POST},produces = {"application/json;charset=UTF-8"})
	@Operation(summary = "法务管理-诉讼过程列表")
	public String legal_litigationSettlementList(HttpServletRequest request,
													   TblLegalLitigationsettlement litigation,
													   @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
													   @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
													   @RequestParam(value="flowid",required=false)String flowid,
												  @RequestParam(value = "disputeid",required = false) BigDecimal disputeid,
												  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal pid = staff.getCurrentOrg().getOrgid();

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			//TblFlow flow = tblFlowService.findById(flowid);

			PageInfo<TblLegalLitigationsettlement> pageInfo = new PageInfo<TblLegalLitigationsettlement>();
			litigation.setLinkorg(pid);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			this.tblLegalLitigationsettlementService.findListByPageInfo(pageInfo,litigation,disputeid);
			request.getSession().setAttribute("flowid", flowid);
			resultMap.put("data", pageInfo);
			//resultMap.put("flow", flow);
		} catch (Exception e) {
			log.error("异常信息：",e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	/**
	 * 诉讼过程查看详情
	 * @param request
	 * @param litigationId
	 * @return
	 */
	@RequestMapping(value = "/legal/litigationSettlementDetail",method = {RequestMethod.POST},produces = {"application/json;charset=UTF-8"})
	@Operation(summary = "法务管理-诉讼过程-查看详情")
	public String legal_litigationSettlementDetail(HttpServletRequest request,
														 @RequestParam(value="litigationId",required=true)BigDecimal litigationId) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		TblLegalLitigationsettlement litigation = this.tblLegalLitigationsettlementService.findById(litigationId);
		if (litigation != null && litigation.getDisputeid() !=null){
			//因为sql关联表的问题 只能单独查数据
			TblLegalDisputregistration disputregistration = tblLegalDisputregistrationService.findById(litigation.getDisputeid());
			litigation.setDisputeidname(disputregistration.getDisputeno());
		}
		if (litigation != null && litigation.getDisputeunder() !=null){
			TblStaff byId = tblStaffService.findById(litigation.getDisputeunder().toString());
			litigation.setDisputeundername(byId.getRealname());
		}
		resultMap.put("litigation", litigation);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	/**
	 * 诉讼过程新建
	 * @param request
	 * @param litigation
	 * @param disputeId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/legal/litigationSettlementSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-诉讼过程-新建接口")
	public String legal_litigationSettlementSave(HttpServletRequest request,
												  TblLegalLitigationsettlement litigation,
												  @RequestParam(value="disputeId",required=false)BigDecimal disputeId
											//	  @RequestParam(value="attids",required=false)String attids,
//												  @RequestParam(value="shouliDate",required=false)String shouliDate,
//												  @RequestParam(value="jieanDate",required=false)String jieanDate
//													,@RequestParam(value="openDate",required=false)String openDate,
												  ) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			String orgid = staff.getCurrentOrg().getOrgid().toString();
			BigDecimal staffid = staff.getStaffid();
			if(disputeId != null) {
				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findById(disputeId);
//				TblCyhwUnit unit = dispute.getContract();
//				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),14,unit.getContractid());
				litigation.setDisputeinfo(dispute.getDisputeid());
			}
			//SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

			litigation.setLinkorg(new BigDecimal(orgid));
			litigation.setCreatestaff(staffid);
			litigation.setCreatetime(new Date());
			litigation.setLitigationid(RandomUtil.uuBigDecimalId());
			this.tblLegalLitigationsettlementService.addLitigationSettlement(litigation);
			resultMap.put("data", litigation.getLitigationid() );
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
			//return JsonBean.success("新增成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return litigation.getLitigationid().toString();
	}



	@RequestMapping(value = "/legal/proceedingsRecordSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-诉讼过程-新建-诉讼过程记录-新建按钮")
	public String legal_proceedingsRecordSave(HttpServletRequest request, TblLegalProceedingsrecord proceed,
											   @RequestParam(value="litigationId",required=false)BigDecimal litigationId,
											    @Parameter(name = "attids", description = "上传附件的ID", required = false) @RequestParam(value="attids", required = false) String attids) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			String result = null;
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
//			TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffid = staff.getStaffid();
			TblLegalLitigationsettlement litigation = this.tblLegalLitigationsettlementService.findById(litigationId);
			proceed.setLinkorg(orgid);
			proceed.setCreatestaff(staffid);
			proceed.setState(0);//新增为0状态为未审批
			proceed.setLitigationinfo(litigation.getLitigationid());
			this.tblLegalProceedingsrecordService.saveProceedingRecord(proceed,attids);
//			TblStaff ts = new TblStaff();
//			ts.setUsername(proceed.getUsername());
//			tblStaffService.saveTs(ts);
//			return JsonBean.success("新增成功");
			resultMap.put("data", proceed.getProceedid());
			resultMap.put("code", 1);
			resultMap.put("msg", "添加成功");
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}

		return proceed.getProceedid().toString();
	}


	@RequestMapping(value = "/legal/proceedingsRecord",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-诉讼过程-新建-诉讼过程记录-列表")
	public String legal_proceedingsRecord(HttpServletRequest request,
												@RequestParam(value="flowid",required=true)Integer flowid,
												@Parameter(name="pageNumber",description="当前页",required=false)Integer pageNumber,
												@Parameter(name="pageSize",description="每页数量",required=false)Integer pageSize,
												@RequestParam(value="litigationId",required=true)BigDecimal litigationId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		PageInfo<TblLegalProceedingsrecord> pageInfo = new PageInfo<TblLegalProceedingsrecord>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);

		this.tblLegalProceedingsrecordService.findByNegotiaId(pageInfo,litigationId);
		resultMap.put("date", pageInfo);
		resultMap.put("flowid", flowid);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@GetMapping("/legal/proceedingsRecord/{id}")
	@Operation(summary = "法务管理-诉讼过程-新建-诉讼过程记录-详情 id为列表的proceedid")
	public TblLegalProceedingsrecord legal_proceedingsRecordModify(HttpServletRequest request,@PathVariable("id") BigDecimal id) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return null;
	        }
			TblLegalProceedingsrecord proceedingsrecord = this.tblLegalProceedingsrecordService.findById(id);
			if (proceedingsrecord != null && proceedingsrecord.getCreatestaff() != null){
				TblStaff byId = tblStaffService.findById(proceedingsrecord.getCreatestaff().toString());
				proceedingsrecord.setUsername(byId.getRealname());
			}
			return proceedingsrecord;
		} catch (Exception e) {
			log.error("异常信息：", e);
			return null;
		}
	}

	@GetMapping("/legal/listatt")
	@Operation(summary = "法务管理-诉讼过程-诉讼过程记录-查询附件")
	public JsonBean listatt(HttpServletRequest request, @RequestParam(value="litigationId",required=false)BigDecimal litigationId) throws Exception {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			jsonBean = tblLegalProceedingsrecordService.getAttListBylitigationId(litigationId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@RequestMapping(value = "/legal/delRecordAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "法务管理-诉讼过程-诉讼过程记录-查询附件")
    public JsonBean delRecordAttInfo(HttpServletRequest request,
    		@Parameter(name = "attid", description = "附件ID ", required = true)@RequestParam(value = "attid", required = true)  BigDecimal attid) throws Exception {
    	JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			jsonBean = this.tblLegalProceedingsrecordService.delAttListBylitigationId(attid);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	@RequestMapping(value = "/legal/proceedingsRecordModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-诉讼过程-新建-诉讼过程记录-修改")
	public String legal_proceedingsRecordModify(HttpServletRequest request,
												 TblLegalProceedingsrecord proceed,
												 @Parameter(name = "attids", description = "上传附件的ID", required = false) @RequestParam(value="attids", required = false) String attids,
												 @RequestParam(value="litigationId",required=false)BigDecimal litigationId) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalProceedingsrecord oldproceed = this.tblLegalProceedingsrecordService.findById(proceed.getProceedid());
			oldproceed.setProceedno(proceed.getProceedno());
			oldproceed.setPorceedstage(proceed.getPorceedstage());
			oldproceed.setCourt(proceed.getCourt());
			oldproceed.setCourtlink(proceed.getCourtlink());
			oldproceed.setCourtcontact(proceed.getCourtcontact());
			oldproceed.setIsexternallawyer(proceed.getIsexternallawyer());
			oldproceed.setLawyearword(proceed.getLawyearword());
			oldproceed.setLawyearname(proceed.getLawyearname());
			oldproceed.setLawyearlink(proceed.getLawyearlink());
			oldproceed.setNegotiator(proceed.getNegotiator());
			oldproceed.setNegotiatorlink(proceed.getNegotiatorlink());
			oldproceed.setPresedingjudge(proceed.getPresedingjudge());
			oldproceed.setCasepromotion(proceed.getCasepromotion());
			oldproceed.setExistingdifficulties(proceed.getExistingdifficulties());
			oldproceed.setMeasurespromote(proceed.getMeasurespromote());
			oldproceed.setFilingtime(proceed.getFilingtime());
			oldproceed.setPaymentremindtime(proceed.getPaymentremindtime());
			oldproceed.setOpeningtime(proceed.getOpeningtime());
			oldproceed.setJudgetiem(proceed.getJudgetiem());
			System.out.println(oldproceed);
			this.tblLegalProceedingsrecordService.modifyNegotiateRecord(oldproceed,attids);
			return JsonBean.success("修改成功");
		} catch (Exception e) {
		}
		return proceed.getProceedid().toString();
	}


	@RequestMapping(value = "/legal/removeLegalProceedingsRecord",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-诉讼过程-新建-诉讼过程记录-删除")
	public void legalremoveLegalProceedingsRecord(HttpServletRequest request,
												  @RequestParam(value="proceedId",required=false)BigDecimal proceedId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ;
        }
		//TblLegalProceedingsrecord oldproceed = this.tblLegalProceedingsRecordService.findById(proceedId);
		tblLegalProceedingsrecordService.removeNegitiateRecord(proceedId);
	}


	@RequestMapping(value = "/ssgcjlfj/deleteFileRelation",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "法务管理-诉讼过程-新建-诉讼过程记录-附近-删除")
	public @ResponseBody String ssgcjlfjDeleteFileRelation(HttpServletRequest request,
												   @RequestParam(value = "attid",required = false)String attid) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		tblLegalLsettlementAttService.deleteRelation(attid);
		TblAttachment att = tblAttachmentService.findById(attid);
		tblAttachmentService.deleteAttid(att);
		return JsonBean.success();
	}


	@RequestMapping(value = "/legal/litigationSettlementModify",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-诉讼过程-修改保存按钮")
	public String legal_litigationSettlementModify(HttpServletRequest request,
													TblLegalLitigationsettlement litigation,
													@RequestParam(value="disputeId",required=false)BigDecimal disputeId,
													@RequestParam(value="attids",required=false)String attids
													//,@RequestParam(value="shouliDate",required=false)String shouliDate,
												//	@RequestParam(value="jieanDate",required=false)String jieanDate
			//,@RequestParam(value="openDate",required=false)String openDate
	) throws Exception {
		try {
//			TblLegalLitigationsettlement oldLitigation = this.tblLegalLitigationsettlementService.findByLitigationid(litigation.getLitigationid());
//			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
//			if(disputeId != null) {
//				//TblCyhwUnit oldunit  = oldLitigation.getDispute().getContract();
//				//this.tblCyhwUnitService.modifyContractStatus(oldunit.getHiscontractstatus(),12,oldunit.getContractid());
//				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findById(disputeId);
//				//TblCyhwUnit unit =dispute.getContract();
//				//this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),14,unit.getContractid());
//				oldLitigation.setDispute(dispute);
//			}
//
//			oldLitigation.setFirstcourt(litigation.getFirstcourt());
//			oldLitigation.setPresidingjudge(litigation.getPresidingjudge());
//			oldLitigation.setCollegialpanel(litigation.getCollegialpanel());
//			oldLitigation.setLitigationamount(litigation.getLitigationamount());
//			oldLitigation.setLitigationresult(litigation.getLitigationresult());
//			oldLitigation.setIseffect(litigation.getIseffect());
//			oldLitigation.setJudgemoney(litigation.getJudgemoney());
//			oldLitigation.setActionobject(litigation.getActionobject());
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			if (StringUtils.isNotBlank(attids)){
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					this.tblLegalLsettlementAttService.insertAttRelation(litigation.getLitigationid(),ids[i]);
					return JsonBean.success("新增成功");
				}
			}
			this.tblLegalLitigationsettlementService.updateModifyLitigationSettlement(litigation);
			return JsonBean.success("修改成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return litigation.getLitigationid().toString();
	}



	@RequestMapping(value = "/legal/litigationSettlementRemove",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-诉讼过程-删除")
	//和老代码一模一样，但是老代码不能删除，单删本表时提示有外键约束，不知道怎么解决老代码的删除逻辑，所以只能完全照搬
	public String legal_litigationSettlementRemove(HttpServletRequest request,@RequestParam(value="litigationId",required=false)BigDecimal litigationId) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalLitigationsettlement oldLitigation = this.tblLegalLitigationsettlementService.findById(litigationId);
			if (oldLitigation != null && oldLitigation.getDisputeid() != null) {
				TblCyhwUnit oldunit = this.tblLegalArbitratsettlementService.findContractByDisputeId(oldLitigation.getDisputeid());
				if (oldunit!=null) {
					this.tblCyhwUnitService.modifyContractStatus(oldunit.getHiscontractstatus(), 12, oldunit.getContractid());
				}
			}

			//删除过程记录
			List<TblLegalProceedingsrecord> recordList = this.tblLegalProceedingsrecordService.findListByLitigationid(litigationId);
			recordList.forEach((record) -> {
				this.tblLegalProceedingsrecordService.removeNegitiateRecord(record.getProceedid());
			});

			//删除附件关联表
			this.tblLegalDisputregistrationService.deleteAttacheMents(3,litigationId);
			this.tblLegalLitigationsettlementService.removeLitigationSettlement(oldLitigation.getLitigationid());
			return JsonBean.success("删除成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return JsonBean.error("删除失败");
	}


	@RequestMapping(value = "/legal/ArbitratSettlementDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-查看详情")
	public String ArbitratSettlementDetail(HttpServletRequest request,
												 @RequestParam(value="arbitraId",required=true)BigDecimal arbitraId) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalArbitratsettlement arbitrat = this.tblLegalArbitratsettlementService.findById(arbitraId);
			resultMap.put("arbitrat", arbitrat);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/ArbitratSettlementInfoList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-列表")
	public String ArbitratSettlementInfoList(HttpServletRequest request,
												   TblLegalArbitratsettlement negotia,
												   @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
												   @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
												   @RequestParam(value="flowid",required=false)String flowid,
												  @RequestParam(value = "disputeid",required = false)BigDecimal disputeid) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);

		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		//TblFlow flow = tblFlowService.findById(flowid);

		PageInfo<TblLegalArbitratsettlement> pageInfo = new PageInfo<TblLegalArbitratsettlement>();
		negotia.setLinkorg(orgid);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		//pageInfo.setCondition(negotia);
		this.tblLegalArbitratsettlementService.findListByPageInfo(pageInfo,negotia,disputeid);
		resultMap.put("date", pageInfo);
		//resultMap.put("flow", flow);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/legal/ArbitratSettlementSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-新建按钮/保存按钮")
	@ResponseBody
	public String ArbitratSettlementSave(HttpServletRequest request,
										  TblLegalArbitratsettlement arbitrat,
										  @RequestParam(value="negotiaId",required=false)BigDecimal negotiaId
//										  @RequestParam(value="asDealDateST",required=false) String asdealdate,
//										  @RequestParam(value="asFirsthearingDateST",required=false) String asfirsthearingdate,
//										  @RequestParam(value="arbitratiionEndDateST",required=false) String arbitrationenddate
										  ) {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffId = staff.getStaffid();
			if(negotiaId != null) {
				TblLegalNegotiatedsettlemen negotiateInfo = this.tblLegalNegotiatedsettlemenService.findById(negotiaId);
//				TblCyhwUnit unit = new TblCyhwUnit();
//				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),15,unit.getContractid());
				arbitrat.setNegotiateinfo(negotiateInfo.getNegotiaid());
			}

			arbitrat.setLinkorg(orgid);
			arbitrat.setCreatestaff(staffId);
			arbitrat.setCreatetime(new Date());
			arbitrat.setArbitraid(RandomUtil.uuBigDecimalId());
			this.tblLegalArbitratsettlementService.addDiputregistration(arbitrat);
			resultMap.put("data", arbitrat.getArbitraid());
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
			//return JsonBean.success("新增成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return arbitrat.getArbitraid().toString();
	}


	@RequestMapping(value = "/legal/findNegotiatedSettlemenA",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-新建-仲裁基本信息-协商信息选择接口")
	public String findNegotiatedSettlemenA(HttpServletRequest request,
												 TblLegalNegotiatedsettlemen negotiate,
												 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
												 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);

		//TblOrganization orgInfo = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		PageInfo<TblLegalNegotiatedsettlemen> pageInfo = new PageInfo<TblLegalNegotiatedsettlemen>();
		negotiate.setLinkorg(orgid);
		negotiate.setJudicialsettlement(new BigDecimal(2));
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		//pageInfo.setCondition(negotiate);
		this.tblLegalNegotiatedsettlemenService.findListForLitiationList(pageInfo,negotiate);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/legal/ArbitratSettlementModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-仲裁过程-修改按钮")
	public String legal_negotiatedSettlemenModify(HttpServletRequest request,
												   TblLegalArbitratsettlement arbitrat,
												   @RequestParam(value="negotiaId",required=false)BigDecimal negotiaId,
												   @RequestParam(value="attids",required=false)String attids) throws Exception {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
//			TblLegalArbitratsettlement oldArbitrat = this.tblLegalArbitratsettlementService.findById(arbitrat.getArbitraid());
//			if(negotiaId != null) {
//				TblLegalNegotiatedsettlemen negotiateInfo = this.tblLegalNegotiatedsettlemenService.findById(negotiaId);
//				oldArbitrat.setNegotiateinfo(negotiateInfo.getNegotiaid());
//			}
//			oldArbitrat.setCourtfirst(arbitrat.getCourtfirst());
//			oldArbitrat.setArbitrationamount(arbitrat.getArbitrationamount());
//			oldArbitrat.setArbitrationresult(arbitrat.getArbitrationresult());
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			if (StringUtils.isNotBlank(attids)){
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					TblAttachment att = tblAttachmentService.findById(ids[i].trim());
					arbitrat.getAttList().add(att);
				}
			}
			this.tblLegalArbitratsettlementService.modifyNegotiatedSettlement(arbitrat);
			return JsonBean.success("修改成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return arbitrat.getArbitraid().toString();
	}

	@RequestMapping(value = "/legal/ArbitratSettlementToModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-新建按钮/保存按钮/修改回显")
	public String ArbitratSettlementToModify(HttpServletRequest request,
												   @RequestParam(value="arbitraId",required=true)BigDecimal arbitraId,
												   @RequestParam(value="choiceSearch",required=false)String choiceSearch,
												   @RequestParam(value="flowid",required=true)String flowid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblFlow flow = tblFlowService.findById(flowid);
			TblLegalArbitratsettlement arbitrat = this.tblLegalArbitratsettlementService.findById(arbitraId);
			List<TblLegalArbitrationrecord> recordList = this.tblLegalArbitrationrecordService.findListBynegotiaId(arbitraId);
			resultMap.put("recordList", recordList);
			resultMap.put("flow", flow);
			resultMap.put("arbitrat", arbitrat);
			resultMap.put("choiceSearch", choiceSearch);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/ArbitrationRecordSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-仲裁过程-新建-仲裁过程信息-新增")
	public String ArbitrationRecordSave(HttpServletRequest request,
										 TblLegalArbitrationrecord record,
										 @RequestParam(value="arbitraId",required=false)BigDecimal arbitraId,
										 @RequestParam(value="zxstaffid",required=false)String zxstaffid) throws Exception {
		try {
			TblStaffUtil loginstaff = userProvider.get();
	        if (loginstaff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			TblStaffUtil st = userProvider.get();
			BigDecimal orgid = st.getCurrentOrg().getOrgid();
			BigDecimal stId = st.getStaffid();
			//TblLegalArbitratsettlement arbitrat = this.tblLegalArbitratsettlementService.findByarbitraid(arbitraId);

			if(zxstaffid != null) {
				TblStaff staff = this.tblStaffService.findById(zxstaffid);
				record.setCreatestaff(staff.getStaffid());
			}
			record.setLinkorg(orgid);
			record.setCreatestaff(stId);
			//record.setCreatetime(new Date());
			record.setArbitrationinfo(arbitraId);
			record.setArrecordid(RandomUtil.uuBigDecimalId());
			record.setCreatetime(new Date());
			this.tblLegalArbitrationrecordService.saveNegotiateRecord(record);
			return JsonBean.success("新增成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return record.getArrecordid().toString();
	}
	


	@RequestMapping(value = "/legal/ArbitrationRecordToModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-新建-仲裁过程信息-新增保存回显")
	public String ArbitrationRecordToModify(HttpServletRequest request,
												  @RequestParam(value="arrecordId",required=true)BigDecimal arrecordId,
											@RequestParam(value="flowid",required=true)BigDecimal flowid) throws Exception {

		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblLegalArbitrationrecord record = this.tblLegalArbitrationrecordService.findById(arrecordId);
		resultMap.put("record", record);
		resultMap.put("flowid", flowid);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@SneakyThrows
	@RequestMapping(value = "/legal/ArbitrationRecordModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-仲裁过程-新建-仲裁过程信息-修改保存")
	public String ArbitrationRecordModify(HttpServletRequest request,
										   TblLegalArbitrationrecord record,
										   //@RequestParam(value="dealDateST",required=false)String dealDateST,
										   @RequestParam(value="zxstaffid",required=false)String zxstaffid) throws Exception {
//		TblLegalArbitrationrecord oldRecord = this.tblLegalArbitrationrecordService.findById(record.getArrecordid());
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		if(record.getDealdate() != null && !"".equals(record.getDealdate())) {
//			oldRecord.setDealdate(sdf.parse(record.getDealdate().toString()));
//		}
//		oldRecord.setArstage(record.getArstage());
//		oldRecord.setArcontactperson(record.getArcontactperson());
//		oldRecord.setOurcontractperson(record.getOurcontractperson());
//		oldRecord.setArrecordmode(record.getArrecordmode());
//		oldRecord.setArrecordmemo(record.getArrecordmemo());
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		this.tblLegalArbitrationrecordService.modifyNegotiateRecord(record);
		return JsonBean.success("修改成功");
		//return record.getArrecordid();
	}


	@RequestMapping(value = "/legal/ArbitrationRecord",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-仲裁过程-新增-仲裁过程信息-列表")
	public String ArbitrationRecord(HttpServletRequest request,
											 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											 @RequestParam(value="flowid",required=false)String flowid,
									@RequestParam(value="arrecordid",required=false) BigDecimal arrecordid) throws Exception{

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }

		PageInfo<TblLegalArbitrationrecord> pageInfo = new PageInfo<TblLegalArbitrationrecord>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		this.tblLegalArbitrationrecordService.findListByPageInfo(pageInfo,arrecordid);
		request.getSession().setAttribute("flowid", flowid);
		resultMap.put("data", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/zcgcxjfjsc/deleteFileRelation",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "法务管理-仲裁过程-新建-仲裁过程信息-附件-删除")
	public @ResponseBody String zcgcxjfjscDeleteFileRelation(HttpServletRequest request,
														   @RequestParam(value = "attid",required = false)String attid) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		tblLegalArbitrationAttService.deleteRelation(attid);
		TblAttachment att = tblAttachmentService.findById(attid);
		tblAttachmentService.deleteAttid(att);
		return JsonBean.success("删除成功");
	}


	@RequestMapping(value = "/legal/removeArbitrationRecord",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法务管理-仲裁过程-新建-仲裁过程信息-删除")
	public String removeArbitrationRecord(HttpServletRequest request,
										@RequestParam(value="arrecordId",required=false)BigDecimal arrecordId) {
		try {
			//TblLegalArbitrationrecord record = this.tblLegalArbitrationrecordService.findById(arrecordId);
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			this.tblLegalArbitrationrecordService.removeNegitiateRecord(arrecordId);
			return JsonBean.success("删除成功");
		} catch (Exception e) {
		}
		return JsonBean.error("删除失败");
	}


	@RequestMapping(value = "/legal/ArbitratSettlementRemove",method = {RequestMethod.POST})
	@ResponseBody
	@Operation(summary = "法务管理-仲裁过程-删除")
	public String ArbitratSettlementRemove(HttpServletRequest request,@RequestParam(value="arbitraId",required=false)BigDecimal arbitraId) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalArbitratsettlement arbitrat = this.tblLegalArbitratsettlementService.findById(arbitraId);
			if (arbitrat != null && arbitrat.getDisputeid() != null) {
				//TblCyhwUnit oldunit =arbitrat.getNegotiate().getDispu().getContract();
//				TblCyhwUnit oldunit = new TblCyhwUnit();
				TblCyhwUnit oldunit = this.tblLegalArbitratsettlementService.findContractByDisputeId(arbitrat.getDisputeid());
				if (oldunit!=null) {
					this.tblCyhwUnitService.modifyContractStatus(oldunit.getHiscontractstatus(), 13, oldunit.getContractid());
				}
			}
			List<TblLegalArbitrationrecord> recordList = this.tblLegalArbitrationrecordService.findListBynegotiaId(arbitraId);
			recordList.forEach((record) -> {
				this.tblLegalArbitrationrecordService.removeNegitiateRecord(record.getArrecordid());
			});
			//删除附件关联表
			this.tblLegalDisputregistrationService.deleteAttacheMents(4,arbitraId);
			this.tblLegalArbitratsettlementService.removeLegalNegotiatedSettlemen(arbitrat.getArbitraid());
			return JsonBean.success("删除成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return JsonBean.error("删除失败");
	}


	@RequestMapping(value = "/legal/disputeSettlementList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷结案列表")
	public String legal_disputeSettlementList(HttpServletRequest request,
											  TblLegalCloseinformation closeInfo,
											  @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											  @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											  @RequestParam(value="flowid",required=false)String flowid,
											   @RequestParam(value ="disputeid",required = false )BigDecimal disputeid,
											   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		//TblFlow flow = tblFlowService.findById(flowid);
		PageInfo<TblLegalCloseinformation> pageInfo = new PageInfo<TblLegalCloseinformation>();
		closeInfo.setLinkorg(orgid);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		this.tblLegalCloseinformationService.findListByPageInfo(pageInfo,closeInfo,disputeid);
		resultMap.put("date", pageInfo);
		//resultMap.put("flow", flow);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/legal/disputeSettlementDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷结案-查看详情")
	public String legal_disputeSettlementDetail(HttpServletRequest request,
													  @RequestParam(value="closeId",required=true)BigDecimal closeId) throws Exception {
		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblLegalCloseinformation oldCloseInfo = this.tblLegalCloseinformationService.findById(closeId);
			resultMap.put("closeInfo", oldCloseInfo);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/disputeSettlementSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷结案-新增")
	@ResponseBody
	public String legal_disputeSettlementSave(HttpServletRequest request,
											   TblLegalCloseinformation closeInfo,
											   @RequestParam(value="disputeId",required=false)BigDecimal disputeId,
//											  @RequestParam(value="jieanDate",required=false)String jieanDate,
											   @RequestParam(value="flowid",required=true)String flowid) throws Exception {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffid = staff.getStaffid();
			if(disputeId != null) {
				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findById(disputeId);
				if(dispute.getContractinfo() != null) {
					TblCyhwUnit unit =  this.tblCyhwUnitService.getEntity(dispute.getContractinfo());
//					this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),16,unit.getContractid());
					this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),unit.getHiscontractstatus().intValue(),unit.getContractid());
				}
//				TblCyhwUnit unit = dispute.getContractinfo();
//				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),16,unit.getContractid());
				closeInfo.setDisputinfo(dispute.getDisputeid());
			}
			closeInfo.setLinkorg(orgid);
			closeInfo.setCreatestaff(staffid);
			closeInfo.setCreatetime(new Date());
			this.tblLegalCloseinformationService.addDisputeSettlement(closeInfo);
			return JsonBean.success("新增成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
//		String result = null;
//		Map<String,Object> resultMap = new HashMap<String, Object>(0);
//		Integer closeid = closeInfo.getCloseid();
//		TblFlow flow = tblFlowService.findById(flowid);
//		TblLegalCloseinformation mation = this.tblLegalCloseinformationService.findById(closeid);
//		resultMap.put("flow", flow);
//		resultMap.put("mation", mation);
//		JSONObject jsonObjectMV = new JSONObject(resultMap);
//		result = jsonObjectMV.toString();
		return closeInfo.getCloseid().toString();
	}
	
	@RequestMapping(value = "/legal/disputeSettlementToModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷结案-保存回显、修改回显")
	public String  legal_disputeSettlementToModify(HttpServletRequest request,
														@RequestParam(value="closeId",required=true)BigDecimal closeId,
														@RequestParam(value="flowid",required=true)String flowid)  throws Exception{

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			//TblFlow flow = tblFlowService.findById(flowid);
			TblLegalCloseinformation closeInfo = this.tblLegalCloseinformationService.findById(closeId);
			//resultMap.put("flow", flow);
			resultMap.put("closeInfo", closeInfo);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/disputeSettlementModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-纠纷结案-修改")
	@ResponseBody
	public String legal_disputeSettlementModify(HttpServletRequest request,
												 TblLegalCloseinformation closeInfo,
												 @RequestParam(value="disputeid",required=false)BigDecimal disputeid) throws Exception{
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalCloseinformation oldCloseInfo = this.tblLegalCloseinformationService.findById(closeInfo.getCloseid());
			TblLegalDisputregistration predispute = this.tblLegalDisputregistrationService.findById(oldCloseInfo.getDisputinfo());
			if(predispute != null && predispute.getContractinfo() != null) {
				TblCyhwUnit preunit =  this.tblCyhwUnitService.getEntity(predispute.getContractinfo());
				this.tblCyhwUnitService.modifyContractStatus(preunit.getHiscontractstatus(),preunit.getHiscontractstatus().intValue(),preunit.getContractid());
			}
			if(disputeid != null) {
				//TblCyhwUnit unit = dispute.getContract();
//				this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),16,unit.getContractid());
//				TblCyhwUnit oldunit = oldCloseInfo.getDispute().getContract();
				//this.tblCyhwUnitService.modifyContractStatus(oldunit.getHiscontractstatus(),new Integer(String.valueOf(oldunit.getHiscontractstatus())),oldunit.getContractid());
				
				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findById(disputeid);
				if(dispute.getContractinfo() != null) {
					TblCyhwUnit unit =  this.tblCyhwUnitService.getEntity(dispute.getContractinfo());
					this.tblCyhwUnitService.modifyContractStatus(new BigDecimal(unit.getContractstatus()),unit.getHiscontractstatus().intValue(),unit.getContractid());
				}
				oldCloseInfo.setDisputinfo(dispute.getDisputeid());
			}
			oldCloseInfo.setJudgementamount(closeInfo.getJudgementamount());
			oldCloseInfo.setCloseresult(closeInfo.getCloseresult());
			oldCloseInfo.setManagerecommond(closeInfo.getManagerecommond());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			if(closeInfo.getClosedate() != null) {
//				oldCloseInfo.setClosedate(sdf.parse(closeInfo.getClosedate().toString()));
//			}
			oldCloseInfo.setClosedate(closeInfo.getClosedate());

			this.tblLegalCloseinformationService.updateModifyDisputeSettlementModify(oldCloseInfo);
			return JsonBean.success("修改成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return closeInfo.getCloseid().toString();
	}


	@RequestMapping(value = "/legal/disputeSettlementRemove",method = {RequestMethod.POST})
	@Operation(summary = "法务管理-纠纷结案-删除")
	@ResponseBody
	public String legal_disputeSettlementRemove(HttpServletRequest request,
											  @RequestParam(value="closeId",required=false)BigDecimal closeId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		TblLegalCloseinformation oldCloseInfo = this.tblLegalCloseinformationService.findById(closeId);
		TblLegalDisputregistration predispute = this.tblLegalDisputregistrationService.findById(oldCloseInfo.getDisputinfo());
		if(predispute != null && predispute.getContractinfo() != null) {
			TblCyhwUnit preunit =  this.tblCyhwUnitService.getEntity(predispute.getContractinfo());
			this.tblCyhwUnitService.modifyContractStatus(preunit.getHiscontractstatus(),preunit.getHiscontractstatus().intValue(),preunit.getContractid());
		}
		this.tblLegalCloseinformationService.removeDisputeSettlementRemove(closeId);
		return JsonBean.success("删除成功");
	}


	@RequestMapping(value = "/legal/qualificationList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-资质保全-列表")
	public String legal_qualificationList(HttpServletRequest request,
										  TblLegalQualification qualification,
										  @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
										  @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
												@RequestParam(value="flowid",required=false)String flowid,
										   @RequestParam(value = "disputeid",required = false)BigDecimal disputeid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		//TblFlow flow = tblFlowService.findById(flowid);
		PageInfo<TblLegalQualification> pageInfo = new PageInfo<TblLegalQualification>();
		qualification.setLinkorg(orgid);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		//pageInfo.setCondition(qualification);
		this.tblLegalQualificationService.findListByPageInfo(pageInfo,qualification,disputeid);
		request.getSession().setAttribute("flowid", flowid);
		resultMap.put("date", pageInfo);
		//resultMap.put("flow", flow);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/legal/qualificationDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-资质保全-查看详情")
	public String legal_qualificationDetail(HttpServletRequest request,
												  @RequestParam(value="qualId",required=true)BigDecimal qualId) throws Exception {

		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblLegalQualification qua = this.tblLegalQualificationService.findById(qualId);
			resultMap.put("qualification", qua);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/qualificationSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "资质保全-新建")
	public String qualificationSave(HttpServletRequest request,
									 TblLegalQualification qualification,
									 @RequestParam(value="disputeId",required=false)BigDecimal disputeId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		BigDecimal staffId = staff.getStaffid();
		if(disputeId != null) {
			TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findDisputeId(disputeId);
			qualification.setDisputeinfo(dispute.getDisputeid());
		}
		qualification.setLinkorg(orgid);
		qualification.setCreatestaff(staffId);
		qualification.setQualid(RandomUtil.uuBigDecimalId());
		this.tblLegalQualificationService.saveQualification(qualification);
//		TblLegalDisputregistration tration = new TblLegalDisputregistration();
//		tration.setDisputeitem(qualification.getDisputeitem());
//		tration.setWhethersued(qualification.getWhethersued());
//		this.tblLegalDisputregistrationService.saveDiputregistration(tration);
		return JsonBean.success("新增成功");
		//return qualification.getQualid();
	}


	@RequestMapping(value = "/legal/qualificationToModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "资质保全-新建、修改回显")
	public String legal_qualificationToModify(HttpServletRequest request,
													@RequestParam(value="qualId",required=true)BigDecimal qualId,
													@RequestParam(value="flowid",required=true)String flowid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblFlow flow = tblFlowService.findByFlowid(flowid);
			TblLegalQualification qualification = this.tblLegalQualificationService.findById(qualId);
			resultMap.put("flow", flow);
			resultMap.put("qualification", qualification);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/qualificationModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "资质保全-修改")
	@ResponseBody
	public String legal_qualificationModify(HttpServletRequest request,
											 TblLegalQualification qualification,
											 @RequestParam(value="disputeid",required=false)BigDecimal disputeid
	) {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalQualification old = this.tblLegalQualificationService.findById(qualification.getQualid());
			if(disputeid != null) {
				TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findById(disputeid);
				old.setDisputeinfo(dispute.getDisputeid());
			}
			old.setApplypreservation(qualification.getApplypreservation());
			old.setPreservedamount(qualification.getPreservedamount());
			old.setPreservednature(qualification.getPreservednature());
			old.setIsperformed(qualification.getIsperformed());
			old.setExceteamount(qualification.getExceteamount());
			old.setIscancel(qualification.getIscancel());
			old.setStillfrozen(qualification.getStillfrozen());
			old.setPreservation(qualification.getPreservation());
			this.tblLegalQualificationService.updateModifyQualification(old);
			return JsonBean.success("修改成功");
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return qualification.getQualid().toString();
	}


	@RequestMapping(value = "/legal/qualificationRemove",method = {RequestMethod.POST})
	@Operation(summary = "资质保全-删除")
	@ResponseBody
	public String legal_qualificationRemove(HttpServletRequest request,
											@RequestParam(value="qualId",required=false)BigDecimal qualId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		this.tblLegalQualificationService.removeQualification(qualId);
		return JsonBean.success("删除成功");
	}


	@RequestMapping(value = "/legal/frozenInformationList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-列表")
	public String legal_frozenInformationList(HttpServletRequest request,
													TblLegalFrozenaccount frozenAccount,
											  @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											  @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
													@RequestParam(value="flowid",required=false)String flowid,
											   @RequestParam(value = "disputeid",required = false)BigDecimal disputeid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();

		//TblFlow flow = tblFlowService.findById(flowid);
		if (pageNumber == null) {
			pageNumber = 1;
		}
		PageInfo<TblLegalFrozenaccount> pageInfo = new PageInfo<TblLegalFrozenaccount>();
		frozenAccount.setLinkorg(orgid);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		//pageInfo.setCondition(frozenAccount);
		this.tblLegalFrozenaccountService.findListByPageInfo(pageInfo,frozenAccount,disputeid);
		request.getSession().setAttribute("flowid", flowid);
		resultMap.put("date", pageInfo);
		//resultMap.put("flow", flow);

		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/legal/frozenAccountDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-查看详情")
	public String legal_frozenAccountDetail(HttpServletRequest request,
												  @RequestParam(value="inforId",required=true)BigDecimal inforId) throws Exception {

		String result = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblLegalFrozenaccount frozen = this.tblLegalFrozenaccountService.findInforid(inforId);
		resultMap.put("frozen", frozen);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}



	@RequestMapping(value = "/legal/frozenAccountAdd",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-新增回显")
	public String legal_frozenAccountAdd(HttpServletRequest request,
											   @RequestParam(value="flowid",required=false)String flowid) {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblFlow flow = tblFlowService.findBy(flowid);
			resultMap.put("flow", flow);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/frozenAccountSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-新增")
	@ResponseBody
	public String legal_frozenAccountSave(HttpServletRequest request,
										   TblLegalFrozenaccount frozen,
										   TblLegalProceedingsrecord srecord,
										   @RequestParam(value="proceedId",required=false)BigDecimal proceedId,
										   @RequestParam(value="startDateOpen",required=false)String startDateOpen,
										   @RequestParam(value="endDateOpen",required=false)String endDateOpen) {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
//			TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			BigDecimal staffid = staff.getStaffid();
//			if(proceedId != null) {
//				TblLegalProceedingsrecord dispute = this.tblLegalProceedingsrecordService.findById(proceedId);
//
//			}
			frozen.setProceedinfo(proceedId);
			if(startDateOpen != null && !"".equals(startDateOpen)) {
				frozen.setStartdate(sdf.parse(startDateOpen));
			}
			if(endDateOpen != null && !"".equals(endDateOpen)) {
				frozen.setEnddate(sdf.parse(endDateOpen));
			}
			TblLegalProceedingsrecord legal = new TblLegalProceedingsrecord();
			frozen.setLinkorg(orgid);
			frozen.setCreatestaff(staffid);
			frozen.setInforid(RandomUtil.uuBigDecimalId());
			this.tblLegalFrozenaccountService.saveFrozenAccount(frozen);
//			legal.setPorceedstage(frozen.getPorceedstage());
//			legal.setProceedno(frozen.getProceedno());
//			legal.setCourt(frozen.getCourt());
//			//legal.setDisputeitem(frozen.getDisputeitem());
//			this.tblLegalProceedingsrecordService.saveProceedingRecord(legal);
//			TblLegalDisputregistration stration = new TblLegalDisputregistration();
//			stration.setDisputeitem(frozen.getDisputeitem());
//			stration.setPlaintiff(frozen.getPlaintiff());
//			stration.setDefendant(frozen.getDefendant());
//			this.tblLegalDisputregistrationService.saveDiputregistration(stration);
			return JsonBean.success("新增成功");
		} catch (Exception e) {
			//return Integer.valueOf("新增失败");
		}
		return JsonBean.success("新增成功");
	}


	@RequestMapping(value = "/legal/findProceedingsRecordInfo",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-新增-诉讼阶段选择接口")
	public String legal_findProceedingsRecordInfo(HttpServletRequest request,
														TblLegalProceedingsrecord record,
														@RequestParam(value="idname",required=true)String idname,
														@RequestParam(value="textname",required=true)String textname,
														@RequestParam(value="othername",required=false)String othername,
														@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
														@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		try {

			PageInfo<TblLegalProceedingsrecord> pageInfo = new PageInfo<TblLegalProceedingsrecord>();
			record.setLinkorg(orgid);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			//pageInfo.setCondition(record);
			this.tblLegalProceedingsrecordService.findListByPageInfo(pageInfo,record);
			resultMap.put("date", pageInfo);
			resultMap.put("textname", textname);
			resultMap.put("idname", idname);
			resultMap.put("othername", othername);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/frozenAccountToModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-新增保存、修改保存回显")
	public String legal_frozenAccountToModify(HttpServletRequest request,
													@RequestParam(value="inforId",required=true)BigDecimal inforId,
													@RequestParam(value="flowid",required=true)String flowid) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		try {
			TblFlow flow = tblFlowService.findById(flowid);
			TblLegalFrozenaccount frozen = this.tblLegalFrozenaccountService.findById(inforId);
			resultMap.put("flow", flow);
			resultMap.put("frozen", frozen);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}


	@RequestMapping(value = "/legal/frozenAccountModify",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-账户冻结-修改")
	@ResponseBody
	public String legal_frozenAccountModify(HttpServletRequest request,
											 TblLegalFrozenaccount frozen,
											 @RequestParam(value="proceedid",required=false)BigDecimal proceedid) {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
		//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//TblLegalFrozenaccount oldFrozen = this.tblLegalFrozenaccountService.findById(frozen.getInforid());
			if(proceedid != null) {
				TblLegalProceedingsrecord dispute = this.tblLegalProceedingsrecordService.findById(proceedid);
				frozen.setProceedinfo(dispute.getProceedid());
			}
//			if(startDateOpen != null) {
//				oldFrozen.setStartdate(sdf.parse(startDateOpen));
//			}
//			if(endDateOpen != null) {
//				oldFrozen.setEnddate(sdf.parse(endDateOpen));
//			}
//			TblLegalProceedingsrecord legal = new TblLegalProceedingsrecord();
//			oldFrozen.setFrozenblank(frozen.getFrozenblank());
//			oldFrozen.setFrozenaccount(frozen.getFrozenaccount());
//			oldFrozen.setApplyamount(frozen.getApplyamount());
//			oldFrozen.setFrozenamount(frozen.getFrozenamount());
//			oldFrozen.setKouhuaamount(frozen.getKouhuaamount());
//			oldFrozen.setAccountnature(frozen.getAccountnature());
//			oldFrozen.setFrozenmemo(frozen.getFrozenmemo());
			this.tblLegalFrozenaccountService.updateModifyFrozenAccount(frozen);
//			legal.setProceedid(oldFrozen.getProceedinfo());
//			legal.setPorceedstage(frozen.getPorceedstage());
//			this.tblLegalProceedingsrecordService.modifyNegotiateRecord(legal);
			TblLegalProceedingsrecord legal = new TblLegalProceedingsrecord();
			legal.setPorceedstage(frozen.getPorceedstage());
			legal.setProceedno(frozen.getProceedno());
			legal.setCourt(frozen.getCourt());
			legal.setProceedid(proceedid);
			//legal.setDisputeitem(frozen.getDisputeitem());
			this.tblLegalProceedingsrecordService.modifyNegotiateRecord(legal,null);
			return JsonBean.success("修改成功");
//			TblLegalDisputregistration stration = new TblLegalDisputregistration();
//			stration.setDisputeitem(frozen.getDisputeitem());
//			stration.setPlaintiff(frozen.getPlaintiff());
//			stration.setDefendant(frozen.getDefendant());
//			this.tblLegalDisputregistrationService.updateDiputregistration(stration);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return frozen.getInforid().toString();
	}


	@RequestMapping(value = "/legal/frozenAccountRemove",method = {RequestMethod.POST})
	@Operation(summary = "法务管理-账户冻结-删除")
	@ResponseBody
	public String legal_frozenAccountRemove(HttpServletRequest request,
										  @RequestParam(value="inforId",required=false)BigDecimal inforId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		this.tblLegalFrozenaccountService.removeFrozenAccount(inforId);
		return JsonBean.success("修改成功");
	}


	@RequestMapping(value = "/legal/legalAccountDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-法务台账-查看详情")
	public String legal_legalAccountDetail(HttpServletRequest request,
												 @RequestParam(value="disputeId",required=true)BigDecimal disputeId,
										   @RequestParam(value="flowid",required=false)String flowid) throws Exception {

		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		try {
			BigDecimal orgid = staff.getCurrentOrg().getOrgid();
			//TblFlow flow = tblFlowService.findById(flowid);
			TblLegalDisputregistration dispute = this.tblLegalDisputregistrationService.findByOrgid(disputeId,orgid);
			resultMap.put("dispute", dispute);
			//resultMap.put("flow", flow);
		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}

	@RequestMapping(value = "/legal/legalAccountList", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-法务台账-列表")
	public String legal_legalAccountList(HttpServletRequest request, TblLegalDisputregistration dispute,
			@Parameter(name = "companyId", description = "公司id", required = false) @RequestParam(value = "companyId", required = false) String companyId,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestParam(value = "flowid", required = false) String flowid) throws Exception {
		boolean flags = false;
		String result = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		String staffid = staff.getStaffid().toString();
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		String linkOrgorgid = staff.getLinkOrg().getOrgid().toString();
		String orgname = staff.getLinkOrg().getOrgname();
		if (StringUtils.equals(orgname, belongGroupName)) {
			StaffResult userInfoExam = tblStaffService.getUserInfoExam(Integer.valueOf(staffid), Integer.valueOf(linkOrgorgid), legalPersonnel);
			if (userInfoExam != null) {
				flags = true;
			}
		}
		try {
			//TblFlow flow = tblFlowService.findById(flowid);
			PageInfo<TblLegalDisputregistration> pageInfo = new PageInfo<TblLegalDisputregistration>();
			//dispute.setLinkorg(orgid);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			//pageInfo.setCondition(dispute);
			this.tblLegalDisputregistrationService.findListByPageInfo(companyId,pageInfo,dispute,orgid);
			/*if (CollectionUtil.isNotEmpty(pageInfo.getTlist())){
				boolean finalFlags = flags;
				pageInfo.getTlist().forEach(x->x.setIsDeleteDisplay(finalFlags));
			}*/
			request.getSession().setAttribute("flowid", flowid);
			resultMap.put("date", pageInfo);
			//resultMap.put("flow", flow);

		} catch (Exception e) {
			log.error("异常信息：",e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	
	@RequestMapping(value = "/legal/legalAlList", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-典型案例库列表")
	public String legal_legalAlList(HttpServletRequest request, TblLegalDisputregistration dispute,
			@Parameter(name = "companyId", description = "公司id", required = false) @RequestParam(value = "companyId", required = false) String companyId,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "flowid", required = false) String flowid) throws Exception {
		boolean flags = false;
		String result = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		String staffid = staff.getStaffid().toString();
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		String linkOrgorgid = staff.getLinkOrg().getOrgid().toString();
		String orgname = staff.getLinkOrg().getOrgname();
		if (StringUtils.equals(orgname, belongGroupName)) {
			StaffResult userInfoExam = tblStaffService.getUserInfoExam(Integer.valueOf(staffid), Integer.valueOf(linkOrgorgid), legalPersonnel);
			if (userInfoExam != null) {
				flags = true;
			}
		}
		try {
			TblFlow flow = tblFlowService.findById(flowid);
			PageInfo<TblLegalDisputregistration> pageInfo = new PageInfo<TblLegalDisputregistration>();
			//dispute.setLinkorg(orgid);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			//pageInfo.setCondition(dispute);
			dispute.setIsclassiccase(new BigDecimal("1"));
			this.tblLegalDisputregistrationService.findListByPageInfo(companyId,pageInfo,dispute,orgid);
			/*if (CollectionUtil.isNotEmpty(pageInfo.getTlist())){
				boolean finalFlags = flags;
				pageInfo.getTlist().forEach(x->x.setIsDeleteDisplay(finalFlags));
			}*/
			request.getSession().setAttribute("flowid", flowid);
			resultMap.put("date", pageInfo);
			resultMap.put("flow", flow);

		} catch (Exception e) {
			log.error("异常信息：",e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	

	@RequestMapping(value = "/legal/dellegalAlList", method = {RequestMethod.DELETE}, produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-典型案例库-列表-删除")
	public String legal_dellegalAlList(
			@Parameter(name = "disputeId", description = "公司id", required = true) @RequestParam(value = "disputeId") BigDecimal disputeId,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		String result = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		try {
			this.tblLegalDisputregistrationService.delClassicCase(disputeId);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return JsonBean.success("删除成功");
	}
	
	/**
	 * 资产保全
	 */
	@RequestMapping(value = "/legal/assetporotectList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "资产保全-列表")
	public String legal_assetporotectList(HttpServletRequest request,
											@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											@Parameter(name = "litigationid", description = "诉讼过程ID", required = false) @RequestParam(value="litigationid",required=false)BigDecimal litigationid,
											@Parameter(name = "arbitraid", description = "仲裁过程ID", required = false) @RequestParam(value = "arbitraid",required = false)BigDecimal arbitraid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		if (pageNumber == null) {
			pageNumber = 1;
		}
		PageInfo<TblLegalAssetporotect> pageInfo = new PageInfo<TblLegalAssetporotect>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		this.tblLegalAssetporotectService.findListByPage(pageInfo,litigationid,arbitraid);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	@RequestMapping(value = "/legal/assetporotectSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "资产保全-新增,修改")
	public String legal_assetporotectSave(HttpServletRequest request,
											TblLegalAssetporotect tla) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			String orgid = staff.getCurrentOrg().getOrgid().toString();
			BigDecimal staffid = staff.getStaffid();

			tla.setCreatestaffid(staffid);
			tla.setCreatetime(new Date());
			this.tblLegalAssetporotectService.addLegalAssetporotectService(tla);
			resultMap.put("code",1);
			resultMap.put("date", tla);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return tla.getLitigationid().toString();
	}
	
	@RequestMapping(value = "/legal/assetporotectDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "资产保全-查詢明細")
	public String legal_assetporotectDetail(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			TblLegalAssetporotect tla = this.tblLegalAssetporotectService.getLegalAssetporotectById(id);
			resultMap.put("data",tla);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/assetporotectDelete",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "资产保全-刪除")
	public String legal_assetporotectDelete(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			this.tblLegalAssetporotectService.deleteLegalAssetporotectById(id);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	
	//======================================================================================
	/**
	 * 执行管理
	 */
	@RequestMapping(value = "/legal/legalExecumgrList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "执行管理-列表")
	public String legal_legalExecumgrList(HttpServletRequest request,TblLegalExecumgr tla,
											@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											@Parameter(name = "litigationid", description = "诉讼过程ID", required = false) @RequestParam(value="litigationid",required=false)BigDecimal litigationid,
											@Parameter(name = "arbitraid", description = "仲裁过程ID", required = false) @RequestParam(value = "arbitraid",required = false)BigDecimal arbitraid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		// 解析用户token，获取用户信息
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		if (pageNumber == null) {// 如果页码参数为空，则默认为1
			pageNumber = 1;
		}
		tla.setOrgid(staff.getCurrentOrg().getOrgid());
		// 创建分页信息对象
		PageInfo<TblLegalExecumgr> pageInfo = new PageInfo<TblLegalExecumgr>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		// 调用服务层方法，根据分页信息和诉讼/仲裁ID查找执行管理列表
		this.tblLegalExecumgrService.findListByPage(pageInfo,litigationid,arbitraid,tla);
		resultMap.put("date", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	/**
	 * 纠纷台账-执行管理列表数据
	 */
	@RequestMapping(value = "/legal/legalExecumgrbydisidList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "纠纷台账-执行管理列表数据")
	public JsonBean legalExecumgrbydisidList(HttpServletRequest request,
											@Parameter(name = "disputeid", description = "纠纷ID", required = false) @RequestParam(value="disputeid",required=false)BigDecimal disputeid,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		this.tblLegalExecumgrService.findListBydisputeid(disputeid,resultMap);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@RequestMapping(value = "/legal/legalExecumgrSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "执行管理-新增,修改")
	public String legal_legalExecumgrSave(HttpServletRequest request,
											TblLegalExecumgr tla,
												   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			String orgid = staff.getCurrentOrg().getOrgid().toString();
			BigDecimal staffid = staff.getStaffid();
			tla.setOrgid(staff.getCurrentOrg().getOrgid());
			tla.setCreatestaffid(staffid);
			tla.setCreatetime(new Date());
			tla.setStatus(0);//初始状态为0，未审批
			this.tblLegalExecumgrService.addLegalExecumgr(tla);
			resultMap.put("code",1);
			resultMap.put("date", tla);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return tla.getLitigationid().toString();
	}
	
	@RequestMapping(value = "/legal/legalExecumgrDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "执行管理-查詢明細")
	public String legal_legalExecumgrDetail(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			TblLegalExecumgr tla = this.tblLegalExecumgrService.getLegalExecumgrById(id);
			resultMap.put("data",tla);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/legalExecumgrDelete",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "执行管理-刪除")
	public String legal_legalExecumgrDelete(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			this.tblLegalExecumgrService.deleteLegalExecumgrById(id);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	
	
	//======================================================================
	/**
	 * 结案总结
	 */
	@RequestMapping(value = "/legal/legalCloseSumSave",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "结案总结-新增,修改")
	public String legal_legalCloseSumSave(HttpServletRequest request,
											TblLegalCloseSum tla,
												   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			String orgid = staff.getCurrentOrg().getOrgid().toString();
			BigDecimal staffid = staff.getStaffid();

			tla.setCreatestaffid(staffid);
			this.tblLegalCloseSumService.addLegalCloseSum(tla);
			resultMap.put("code",1);
			resultMap.put("date", tla);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return tla.getLitigationid().toString();
	}
	
	@RequestMapping(value = "/legal/legalCloseSumDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "结案总结-查詢明細")
	public String legal_legalCloseSumDetail(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			TblLegalCloseSum tla = this.tblLegalCloseSumService.getLegalCloseSumById(id);
			resultMap.put("data",tla);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/legalCloseSumDetailBySSZC",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "结案总结-通过诉讼过程、仲裁过程查询")
	public String legal_legalCloseSumDetail(HttpServletRequest request,
					@Parameter(name = "litigationid", description = "诉讼过程ID", required = false) @RequestParam(value="litigationid",required=false)BigDecimal litigationid,
					@Parameter(name = "arbitraid", description = "仲裁过程ID", required = false) @RequestParam(value = "arbitraid",required = false)BigDecimal arbitraid,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			TblLegalCloseSum tla = this.tblLegalCloseSumService.getLegalCloseSumBySSZCId(litigationid,arbitraid);
			resultMap.put("data",tla);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/legalCloseSumDelete",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "结案总结-刪除")
	public String legal_legalCloseSumDelete(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			this.tblLegalCloseSumService.deleteLegalCloseSumById(id);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	
	//=====================================================================================
	/**
	 * 代理律师 TBL_LEGAL_ATTORNEY
	 */
	@RequestMapping(value = "/legal/legalAttorney",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "代理律师-列表（不分页）")
	public String legal_legalAttorneyList(HttpServletRequest request,
					@RequestParam(value="disputeid",required=false)BigDecimal disputeid,
					@RequestParam(value="negotiationid",required=false)BigDecimal negotiationid,
					@RequestParam(value="lawsuitid",required=false)BigDecimal lawsuitid,
					@RequestParam(value="arbitrationid",required=false)BigDecimal arbitrationid,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			if(disputeid !=null){
				List<TbllegalAttorney> list = this.tbllegalAttorneyService.getlegalAttorneyByDisputeid(disputeid);
				resultMap.put("data",list);
				resultMap.put("code",1);
				JSONObject jsonObjectMV = new JSONObject(resultMap);
				result = jsonObjectMV.toString();
				return result;
			}
			if(negotiationid !=null){
				List<TbllegalAttorney> list = this.tbllegalAttorneyService.getlegalAttorneyByNegotiationid(negotiationid);
				resultMap.put("data",list);
				resultMap.put("code",1);
				JSONObject jsonObjectMV = new JSONObject(resultMap);
				result = jsonObjectMV.toString();
				return result;
			}
			if(lawsuitid !=null){
				List<TbllegalAttorney> list = this.tbllegalAttorneyService.getlegalAttorneyByLawsuitid(lawsuitid);
				resultMap.put("data",list);
				resultMap.put("code",1);
				JSONObject jsonObjectMV = new JSONObject(resultMap);
				result = jsonObjectMV.toString();
				return result;
			}
			if(arbitrationid !=null){
				List<TbllegalAttorney> list = this.tbllegalAttorneyService.getlegalAttorneyByArbitrationid(arbitrationid);
				resultMap.put("data",list);
				resultMap.put("code",1);
				JSONObject jsonObjectMV = new JSONObject(resultMap);
				result = jsonObjectMV.toString();
				return result;
			}
		} catch (Exception e) {
			log.error("异常信息：",e);
		}
		return null;
	}

	/**
	 * 代理律师
	 */
	@RequestMapping(value = "/legal/legalAttorneySave", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "代理律师-新增,修改")
	public String legal_legalAttorneySave(HttpServletRequest request, TbllegalAttorney tla,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			String orgid = staff.getCurrentOrg().getOrgid().toString();
			BigDecimal staffid = staff.getStaffid();

			tla.setCreatestaffid(staffid);
			tla.setCreatetime(new Date());
			this.tbllegalAttorneyService.addlegalAttorney(tla);
			resultMap.put("code", 1);
			resultMap.put("date", tla);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常处理：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/legalAttorneyDetail",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "代理律师-查詢明細")
	public String legal_legalAttorneyDetail(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			TbllegalAttorney tla = this.tbllegalAttorneyService.getlegalAttorneyById(id);
			resultMap.put("data",tla);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/legalAttorneyDelete",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "代理律师-刪除")
	public String legal_legalAttorneyDelete(HttpServletRequest request,
					@RequestParam(value="id",required=false)BigDecimal id,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			this.tbllegalAttorneyService.deletelegalAttorneyById(id);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}

	/**
	 * 经营风险 - 合同交付分析 - 年度合同分析
	 * @param request
	 * @param token
	 * @param year
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/contract/contractAnalysis",method = {RequestMethod.GET})
	@Operation(summary = "交付分析--年度合同分析")
	public @ResponseBody String ContractAnalysis(HttpServletRequest request,@RequestParam(value="year",required=false)Integer year
			) throws Exception{
		String result = null;
  		try {
  			TblStaffUtil staff = userProvider.get();
  	        if (staff == null) {
  	        	return JsonBean.error("用户已失效");
  	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.getContractAnalysis(year);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}
	
	@RequestMapping(value ="/contract/ContractCollection",method = {RequestMethod.GET})
	@Operation(summary = "交付分析--各部门合同收款汇总表")
	public @ResponseBody String ContractCollection(HttpServletRequest request,@RequestParam(value="year",required=false)Integer year
			) throws Exception{
		String result = null;
  		try {
  			TblStaffUtil staff = userProvider.get();
  	        if (staff == null) {
  	        	return JsonBean.error("用户已失效");
  	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.getContractCollection(year);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}

	/**
	 * 经营风险 - 合同交付分析 - 交付合同计划情况
	 * @param request
	 * @param token
	 * @param year
	 * @param quarter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/contract/orgContractPlan",method = {RequestMethod.GET})
	@Operation(summary = "交付分析--各部门交付合同计划情况")
	public @ResponseBody String orgContractPlan(HttpServletRequest request,
			@RequestParam(value="year",required=false)Integer year,@RequestParam(value="quarter",required=false)Integer quarter
    ) throws Exception{
		String result = null;
  		try {
  			TblStaffUtil staff = userProvider.get();
  	        if (staff == null) {
  	        	return JsonBean.error("用户已失效");
  	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.orgContractPlan(year,quarter);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}

	/**
	 * 经营风险 - 合同交付分析 - 各部门合同付款明细
	 * @param request
	 * @param token
	 * @param year
	 * @param quarter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/contract/orgPayContract",method = {RequestMethod.GET})
	@Operation(summary = "交付分析--各部门合同付款明细表")
	public @ResponseBody String orgPayContract(HttpServletRequest request,
			@RequestParam(value="year",required=false)Integer year,@RequestParam(value="quarter",required=false)Integer quarter
    ) throws Exception{
		String result = null;
  		try {
  			TblStaffUtil staff = userProvider.get();
  	        if (staff == null) {
  	        	return JsonBean.error("用户已失效");
  	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.orgPayContract(year,quarter);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}
	
	
	@RequestMapping(value ="/contract/contractLegalAprStat",method = {RequestMethod.GET})
	@Operation(summary = "本年合同法律合规审查数量、金额")
	public @ResponseBody String contractLegalAprStat(HttpServletRequest request,
			@RequestParam(value="year",required=false)Integer year,
			@RequestParam(value="quarter",required=false)Integer quarter
    ) throws Exception{
		String result = null;
  		try {
  			TblStaffUtil staff = userProvider.get();
  	        if (staff == null) {
  	        	return JsonBean.error("用户已失效");
  	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.contractLegalAprStat(year,quarter);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}
	
	
	@RequestMapping(value ="/contract/legalLitigationCaseStat",method = {RequestMethod.GET})
	@Operation(summary = "在手法律诉讼案件数量，涉诉金额")
	public @ResponseBody String legalLitigationCaseStat(HttpServletRequest request,
			@RequestParam(value="year",required=false)Integer year,
			@RequestParam(value="quarter",required=false)Integer quarter
    ) throws Exception{
		String result = null;
  		try {
  			TblStaffUtil staff = userProvider.get();
  	        if (staff == null) {
  	        	return JsonBean.error("用户已失效");
  	        }
  			Map<String,Object>  resultMap = this.tblCyhwUnitService.legalLitigationCaseStat(year,quarter);
			resultMap.put("year", year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
  		} catch (Exception e) {
  			ResponseFormat.retParam(0,1000,e.getMessage());
  		}
  		return result;
	}
	
	
	@RequestMapping(value = "/legal/addClassicCase",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "添加典型案例库")
	public String legal_addClassicCase(HttpServletRequest request,
					@RequestParam(value="disputeid",required=false)BigDecimal disputeid) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			this.tblLegalDisputregistrationService.addClassicCase(disputeid);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/legal/classicCaseList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "法务管理-典型案例库-列表")
	public String legal_classicCaseList(HttpServletRequest request,TblLegalDisputregistration dispute,
										 @Parameter(name = "companyId", description = "公司id", required = false)
										 @RequestParam(value = "companyId")String companyId,
										 @RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
										 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
											   @RequestParam(value="flowid",required=false)String flowid) throws Exception {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return String.valueOf(resultMap);
		}
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();
		try {
			//TblFlow flow = tblFlowService.findById(flowid);
			PageInfo<TblLegalDisputregistration> pageInfo = new PageInfo<TblLegalDisputregistration>();
			//dispute.setLinkorg(orgid);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			//pageInfo.setCondition(dispute);
			this.tblLegalDisputregistrationService.findClassicCaseListByPageInfo(companyId,pageInfo,dispute,orgid);
			request.getSession().setAttribute("flowid", flowid);
			resultMap.put("date", pageInfo);
			//resultMap.put("flow", flow);

		} catch (Exception e) {
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
	}
	
	
	@RequestMapping(value = "/legal/caseChronicle",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "案件大事记")
	public String legal_caseChronicle(HttpServletRequest request,
					@RequestParam(value="disputeid",required=false)BigDecimal disputeid,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		String result = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }

			this.tblLegalDisputregistrationService.addClassicCase(disputeid);
			resultMap.put("code",1);
			JSONObject jsonObjectMV = new JSONObject(resultMap);
			result = jsonObjectMV.toString();
			return result;
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return null;
	}
	
	
	
	@RequestMapping(value = "/legal/disputeStatistics",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "纠纷统计分析")
	public String disputeStatistics(HttpServletRequest request,
			         @Parameter(name = "year", description = "年度", required = false)@RequestParam(value="year",required=false)Integer year) {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			log.info("纠纷统计分析");
  			Map<String,Object>  resultMap = this.tblLegalDisputregistrationService.setDisputeStatistics(year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
	
	
	 
	@RequestMapping(value = "/legal/proceedTimeAxis",method = {RequestMethod.POST},produces = {"application/json;charset=UTF-8"})
	@Operation(summary = "法务管理-诉讼过程-查看诉讼记录时间轴")
	public String proceedTimeAxis(HttpServletRequest request,
			@RequestParam(value="litigationId",required=true)@Parameter(name = "litigationId", description = "litigationId")String litigationId) {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
  			Map<String,Object>  resultMap = this.tblLegalProceedingsrecordService.getproceedTimeAxis(litigationId);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		 return result;
	}



	@RequestMapping(value = "/legal/disputeMoneyList",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@ResponseBody
	@Operation(summary = "法律纠纷案件数量、涉及金额分析")
	public String disputeMoneyList(HttpServletRequest request,
			         @Parameter(name = "year", description = "年度", required = false)@RequestParam(value="year",required=false)Integer year) {
		String result = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return JsonBean.error("用户已失效");
	        }
			log.info("法律纠纷案件数量、涉及金额分析");
  			Map<String,Object>  resultMap = this.tblLegalDisputregistrationService.setDisputeMoneyList(year);
  			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		return result;
	}
}
