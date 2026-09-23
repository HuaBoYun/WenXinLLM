package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.service.business.ProjectSettlementService;
import com.huabo.audit.util.MyJsonBean;
import com.huabo.audit.util.MyResponseFormat;
import com.huabo.audit.util.TokenUtil;
import com.huabo.audit.vo.param.ProjectSettlementCompletionQueryParam;
import com.huabo.audit.vo.param.ProjectSettlementCostIntermediateQueryParam;
import com.huabo.audit.vo.result.ProjectSettlementCompletionResult;
import com.huabo.audit.vo.result.ProjectSettlementCostIntermediateResult;
import com.huabo.audit.vo.result.UserInfoParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 工程结算审计项目汇总
 */
@RestController
@Slf4j
@Tag(name="项目汇总",description="项目汇总")
public class OilAuditProjectSettlementController {

	@Resource
	private ProjectSettlementService projectSettlementService;
	
	@Resource
    private UserProvider userProvider;

	@Operation(summary = "工程结算审计项目汇总 列表查询")
	@PostMapping("/oil/audit/project-settlement/cost-intermediate/getList")
	public MyJsonBean<ProjectSettlementCostIntermediateResult> getProjectSettlementCostIntermediateList(@RequestHeader("token") String token,
			@RequestBody ProjectSettlementCostIntermediateQueryParam param) throws Exception {
		MyJsonBean<ProjectSettlementCostIntermediateResult> myJsonBean = null;
		//token校验
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		UserInfoParam userInfo = TokenUtil.getUserInfo(loginStaff);
		try {
			myJsonBean = projectSettlementService.getProjectSettlementCostIntermediateList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("工程结算审计项目汇总 列表查询 ...接口 异常", e);
			myJsonBean = MyResponseFormat.retParam(0, 1000, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary = "工程结算审计项目汇总 详情查询")
	@GetMapping("/oil/audit/project-settlement/cost-intermediate/get/{gcxmzjzjbid}")
	public MyJsonBean<ProjectSettlementCostIntermediateResult> getProjectSettlementCostIntermediate(@RequestHeader("token") String token,
			@PathVariable BigDecimal gcxmzjzjbid) {
		MyJsonBean<ProjectSettlementCostIntermediateResult> myJsonBean = null;
		
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(loginStaff);
			myJsonBean = projectSettlementService.getProjectSettlementCostIntermediate(gcxmzjzjbid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("工程结算审计项目汇总 详情查询 ...接口 异常", e);
			myJsonBean = MyResponseFormat.retParam(0, 1000, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary = "竣工决算审计项目汇总 列表查询")
	@PostMapping("/oil/audit/project-settlement/completion/getList")
	public MyJsonBean<ProjectSettlementCompletionResult> getProjectSettlementCompletionList(@RequestHeader("token") String token,
			@RequestBody ProjectSettlementCompletionQueryParam param) {
		MyJsonBean<ProjectSettlementCompletionResult> myJsonBean = null;
		//token校验
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(loginStaff);
			myJsonBean = projectSettlementService.getProjectSettlementCompletionList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("竣工决算审计项目汇总 列表查询 ...接口 异常", e);
			myJsonBean = MyResponseFormat.retParam(0, 1000, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary = "竣工决算审计项目汇总 详情查询")
	@GetMapping("/oil/audit/project-settlement/completion/get/{jsxmtzwcqkid}")
	public MyJsonBean<ProjectSettlementCompletionResult> getProjectSettlementCompletion(@RequestHeader("token") String token,
			@PathVariable Long jsxmtzwcqkid) {
		MyJsonBean<ProjectSettlementCompletionResult> myJsonBean = null;
		//token校验
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtil.getUserInfo(loginStaff);
			myJsonBean = projectSettlementService.getProjectSettlementCompletion(jsxmtzwcqkid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("竣工决算审计项目汇总 详情查询 ...接口 异常", e);
			myJsonBean = MyResponseFormat.retParam(0, 1000, e.getMessage());
		}
		return myJsonBean;
	}
}
