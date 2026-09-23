package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.oracle.service.*;
import com.huabo.legal.service.HomeService;
import com.huabo.legal.util.JsonBean;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.ResponseFormat;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.*;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

	@Resource
	private TblFwglConferenceManagementOracleService tblFwglConferenceManagementOracleService;
	@Resource
	private TblFwglPracticeApplyOracleService tblFwglPracticeApplyOracleService;
	@Resource
	private TblFwglInstitutionAuditOracleService tblFwglInstitutionAuditOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblFwglLegalPersonnelOracleService tblFwglLegalPersonnelOracleService;

	/**
	 * 消息通知
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<PageResult<TblFwglConferenceManagementOracle>> getTblFwglConferenceManagementList(TblFwglConferenceManagementQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglConferenceManagementOracle> pageInfo = tblFwglConferenceManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<UserInfo> comperes;
			Map<String, String> comperesMap = new HashMap<>();
			Map<String, String> participantMap = new HashMap<>();
			//查询会议主持人
			String compere = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCompere()))
					.map(TblFwglConferenceManagementOracle::getCompere).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(compere)) {
				comperes = tblStaffOracleService.getCreatorUserInfos(compere);
				if (CollectionUtil.isNotEmpty(comperes)) {
					comperesMap = comperes.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
			}
			//查询会议参与者
			List<String> participant = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getParticipants()))
					.map(TblFwglConferenceManagementOracle::getParticipants).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(participant)) {
				participant.forEach(x -> {
					List<UserInfo> participantList = tblStaffOracleService.getCreatorUserInfos(x);
					if (CollectionUtil.isNotEmpty(participantList)) {
						String participants = participantList.stream().map(UserInfo::getRealName).collect(Collectors.joining(","));
						participantMap.put(x, participants);
					}
				});
			}
			Map<String, String> finalComperesMap = comperesMap;
			pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCompere())).forEach(x -> {
				x.setCompereName(finalComperesMap.getOrDefault(x.getCompere(), ""));
				x.setParticipantsNames(participantMap.getOrDefault(x.getParticipants(), ""));
			});
			PageResult<TblFwglConferenceManagementOracle> build = new PageResult<TblFwglConferenceManagementOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 法务人员持证上岗率
	 * @return
	 */
	@Override
	public JsonBean<LegalPersonnelCardEmploymentRateResult> getLegalPersonnelCardEmploymentRate(TblFwglParam param) {
		LegalPersonnelCardEmploymentRateResult legalPersonnelCardEmploymentRate = tblFwglPracticeApplyOracleService
				.getLegalPersonnelCardEmploymentRate(param);
		return ResponseFormat.retParam(200, 200, legalPersonnelCardEmploymentRate);
	}

	/**
	 * 公司律师人数占比
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<LegalPersonnelCardEmploymentRateResult> getFirmLegalProportion(TblFwglParam param) {
		LegalPersonnelCardEmploymentRateResult firmLegalProportion = tblFwglPracticeApplyOracleService.getFirmLegalProportion(param);
		return ResponseFormat.retParam(200, 200, firmLegalProportion);
	}

	/**
	 * 本年度制度法律合规审查数量
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<List<TblFwglComplianceResult>> getInstitutionCompliance(TblFwglParam param) {
		Date date = new Date();
		Date beginOfYear = DateUtil.beginOfYear(date);
		Date endYear = DateUtil.nextYear(date);
		List<TblFwglComplianceResult> result = tblFwglInstitutionAuditOracleService.getInstitutionAuditCompliance(param, beginOfYear, endYear);
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 本年重大决策法律合规审查数量
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<List<TblFwglComplianceResult>> getBusinessInstitutionCompliance(TblFwglParam param) {
		Date date = new Date();
		Date beginOfYear = DateUtil.beginOfYear(date);
		Date endYear = DateUtil.nextYear(date);
		List<TblFwglComplianceResult> result = tblFwglInstitutionAuditOracleService
				.getBusinessInstitutionAuditCompliance(param, beginOfYear, endYear);
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 法务人员数量
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<List<LegalPersonnelCountResult>> getLegalPersonnelCount(TblFwglParam param) {
		List<LegalPersonnelCountResult> result = tblFwglLegalPersonnelOracleService.getLegalPersonnelCount(param);
		if (CollectionUtil.isNotEmpty(result)) {
			Map<String, String> map = new HashMap<>();
			List<String> collect = result.stream().map(LegalPersonnelCountResult::getBelongGroup).collect(Collectors.toList());
			List<UserInfo> belongGroupIdUserInfos = tblStaffOracleService.getBelongGroupIdUserInfoOrgmeno(StringUtils.join(collect, ","));
			if (CollectionUtil.isNotEmpty(belongGroupIdUserInfos)) {
				map = belongGroupIdUserInfos.stream().collect(Collectors.toMap(UserInfo::getBelongGroupId, UserInfo::getBelongGroupName));
			}
			Map<String, String> finalMap = map;
			result.forEach(x -> x.setBelongGroupName(finalMap.getOrDefault(x.getBelongGroup(), "")));
		}
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 公司律师人数
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<List<LegalCompanyLawyerResult>> getLegalCompanyLawyer(TblFwglParam param) {
		List<LegalCompanyLawyerResult> result = tblFwglPracticeApplyOracleService.getLegalCompanyLawyer(param);
		if (CollectionUtil.isNotEmpty(result)) {
			Map<String, String> map = new HashMap<>();
			List<String> collect = result.stream().map(LegalCompanyLawyerResult::getBelongGroup).collect(Collectors.toList());
			List<UserInfo> belongGroupIdUserInfos = tblStaffOracleService.getBelongGroupIdUserInfoOrgmeno(StringUtils.join(collect, ","));
			if (CollectionUtil.isNotEmpty(belongGroupIdUserInfos)) {
				map = belongGroupIdUserInfos.stream().collect(Collectors.toMap(UserInfo::getBelongGroupId, UserInfo::getBelongGroupName));
			}
			Map<String, String> finalMap = map;
			result.forEach(x -> x.setBelongGroupName(finalMap.getOrDefault(x.getBelongGroup(), "")));
		}
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 法务人数数量-专职法务人鱼/兼职法务人 占比
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<LegalFullimePercentageResult> getLegalFullimePercentage(TblFwglParam param) {
		//总人数
		LegalFullimePercentageResult res = tblFwglLegalPersonnelOracleService.getCount(param);
		//专职人员数量
		LegalFullimePercentageResult legalFullimePercentage = tblFwglLegalPersonnelOracleService.getLegalFullimePercentage(param);
		legalFullimePercentage.setCount(res.getCount() - legalFullimePercentage.getFullimeCount());
		return ResponseFormat.retParam(200, 200, legalFullimePercentage);
	}

	/**
	 * 全体系经营事项及制度审核数量
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<List<LegalInstitutionResult>> getLegalInstitution(TblFwglParam param) {
		List<LegalInstitutionResult> results = tblFwglInstitutionAuditOracleService.getLegalInstitution(param);
		return ResponseFormat.retParam(200, 200, results);
	}
}
