package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAuthorityApplyOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAuthorityApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaAuthorityApplyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAuthorityApplyQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CeaAuthorityApplyServiceImpl implements CeaAuthorityApplyService {

	@Resource
	private TblCeaAuthorityApplyOracleService tblCeaAuthorityApplyOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public MyJsonBean<TblCeaAuthorityApplyOracle> getTblCeaAuthorityApplyList(TblCeaAuthorityApplyQueryParam param) {
		PageInfo<TblCeaAuthorityApplyOracle> pageInfo = tblCeaAuthorityApplyOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			//申请人、创建人
			List<Long> applyIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyId())).distinct()
					.map(TblCeaAuthorityApplyOracle::getApplyId).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaAuthorityApplyOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(applyIds)) {
				creatorList.addAll(applyIds);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			//信息提供单位
			List<Long> applyWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyWorkUnit())).distinct()
					.map(TblCeaAuthorityApplyOracle::getApplyWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(applyWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setApplyName(creatorUserInfoMap.getOrDefault(item.getApplyId(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setApplyWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getApplyWorkUnit(), ""));
			});
			PageResult<TblCeaAuthorityApplyOracle> build = new PageResult<TblCeaAuthorityApplyOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	@Override
	public MyJsonBean<TblCeaAuthorityApplyOracle> saveOrUpdateTblCeaAuthorityApply(TblCeaAuthorityApplyOracle param) {
		TblCeaAuthorityApplyOracle model = tblCeaAuthorityApplyOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);

	}

	@Override
	public MyJsonBean<Void> deleteTblCeaAuthorityApply(Long id) {
		tblCeaAuthorityApplyOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	@Override
	public MyJsonBean<TblCeaAuthorityApplyOracle> getTblCeaAuthorityApply(Long id) {
		TblCeaAuthorityApplyOracle result = tblCeaAuthorityApplyOracleService.findById(id);
		if (Objects.nonNull(result.getApplyId())) {
			result.setApplyName(tblStaffOracleService.getCreatorUserInfo(result.getApplyId()));
			TblStaffOracle userInfoForId = tblStaffOracleService.getUserInfoForId(result.getApplyId());
			if (Objects.nonNull(userInfoForId)){
				result.setApplyWorkUnitName(userInfoForId.getWorkUnitName());
			}
		}
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
