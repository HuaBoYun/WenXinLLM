package com.huabo.legal.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.service.HomeService;
import com.huabo.legal.util.JsonBean;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalCompanyLawyerResult;
import com.huabo.legal.vo.result.LegalFullimePercentageResult;
import com.huabo.legal.vo.result.LegalInstitutionResult;
import com.huabo.legal.vo.result.LegalPersonnelCardEmploymentRateResult;
import com.huabo.legal.vo.result.LegalPersonnelCountResult;
import com.huabo.legal.vo.result.TblFwglComplianceResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-首页所有接口",description="法务管理-首页所有接口")
@RequestMapping(value = "/api-auth/home")
@Slf4j
public class HomeController {

	@Resource
	private HomeService homeService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "消息通知")
	@PostMapping("/conference/getList")
	public JsonBean<PageResult<TblFwglConferenceManagementOracle>> getTblFwglConferenceManagementList(@RequestHeader("token") String token,
			@RequestBody TblFwglConferenceManagementQueryParam param) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		param.setCompere(loginStaff.getStaffid().toString());
		JsonBean<PageResult<TblFwglConferenceManagementOracle>> jsonBean = null;
		try {
			jsonBean = homeService.getTblFwglConferenceManagementList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("消息通知 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员持证上岗占比")
	@PostMapping("/legal/personnel")
	public JsonBean<LegalPersonnelCardEmploymentRateResult> getLegalPersonnelCardEmploymentRate(@RequestBody TblFwglParam param) {
		JsonBean<LegalPersonnelCardEmploymentRateResult> jsonBean = null;
		try {
			jsonBean = homeService.getLegalPersonnelCardEmploymentRate(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员持证上岗占比 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "公司律师人数占比")
	@PostMapping("/firm/legal/proportion")
	public JsonBean<LegalPersonnelCardEmploymentRateResult> getFirmLegalProportion(@RequestBody TblFwglParam param) {
		JsonBean<LegalPersonnelCardEmploymentRateResult> jsonBean = null;
		try {
			jsonBean = homeService.getFirmLegalProportion(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师人数占比 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "本年度制度法律合规审查数量")
	@PostMapping("/institution/compliance")
	public JsonBean<List<TblFwglComplianceResult>> getInstitutionCompliance(@RequestBody TblFwglParam param) {
		JsonBean<List<TblFwglComplianceResult>> jsonBean = null;
		try {
			jsonBean = homeService.getInstitutionCompliance(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("本年度制度法律合规审查数量 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "本年重大决策法律合规审查数量")
	@PostMapping("/business/institution/compliance")
	public JsonBean<List<TblFwglComplianceResult>> getBusinessInstitutionCompliance(@RequestBody TblFwglParam param) {
		JsonBean<List<TblFwglComplianceResult>> jsonBean = null;
		try {
			jsonBean = homeService.getBusinessInstitutionCompliance(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("本年重大决策法律合规审查数量 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员数量")
	@PostMapping("/legal/personnel/count")
	public JsonBean<List<LegalPersonnelCountResult>> getLegalPersonnelCount(@RequestBody TblFwglParam param) {
		JsonBean<List<LegalPersonnelCountResult>> jsonBean = null;
		try {
			jsonBean = homeService.getLegalPersonnelCount(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员数量 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "公司律师人数")
	@PostMapping("/company/lawyer/count")
	public JsonBean<List<LegalCompanyLawyerResult>> getLegalCompanyLawyer(@RequestBody TblFwglParam param) {
		JsonBean<List<LegalCompanyLawyerResult>> jsonBean = null;
		try {
			jsonBean = homeService.getLegalCompanyLawyer(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师人数 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人数数量-专职法务人/兼职法务人 占比")
	@PostMapping("/legal/fullime/percentage")
	public JsonBean<LegalFullimePercentageResult> getLegalFullimePercentage(@RequestBody TblFwglParam param) {
		JsonBean<LegalFullimePercentageResult> jsonBean = null;
		try {
			jsonBean = homeService.getLegalFullimePercentage(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人数数量-专职法务人鱼/兼职法务人 占比 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "全体系经营事项及制度审核数量")
	@PostMapping("/legal/institution")
	public JsonBean<List<LegalInstitutionResult>> getLegalInstitution(@RequestBody TblFwglParam param) {
		JsonBean<List<LegalInstitutionResult>> jsonBean = null;
		try {
			jsonBean = homeService.getLegalInstitution(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("全体系经营事项及制度审核数量 ...接口 异常", e);
		}
		return jsonBean;
	}

}
