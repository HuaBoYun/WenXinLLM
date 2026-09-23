package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInternalWebsiteApplyOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaInternalWebsiteApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaInternalWebsiteApplyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInternalWebsiteApplyQueryParam;
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
public class CeaInternalWebsiteApplyServiceImpl implements CeaInternalWebsiteApplyService {

	@Resource
	private TblCeaInternalWebsiteApplyOracleService tblCeaInternalWebsiteApplyOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 内部网站申信息发布 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaInternalWebsiteApplyOracle> getTblCeaInternalWebsiteApplyList(TblCeaInternalWebsiteApplyQueryParam param) {
		PageInfo<TblCeaInternalWebsiteApplyOracle> pageInfo = tblCeaInternalWebsiteApplyOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			//联系人、供单位负责人、创建人
			List<Long> providerPeoples = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getProviderPeople())).distinct()
					.map(TblCeaInternalWebsiteApplyOracle::getProviderPeople).collect(Collectors.toList());
			List<Long> contacts = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getContact())).distinct()
					.map(TblCeaInternalWebsiteApplyOracle::getContact).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaInternalWebsiteApplyOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(providerPeoples)) {
				creatorList.addAll(providerPeoples);
			}
			if (CollectionUtil.isNotEmpty(contacts)) {
				creatorList.addAll(contacts);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			//信息提供单位
			List<Long> infoProviderWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getInfoProviderWorkUnit())).distinct()
					.map(TblCeaInternalWebsiteApplyOracle::getInfoProviderWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(infoProviderWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setProviderPeopleName(creatorUserInfoMap.getOrDefault(item.getProviderPeople(), ""));
				item.setContactName(creatorUserInfoMap.getOrDefault(item.getContact(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setInfoProviderWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getInfoProviderWorkUnit(), ""));
			});
			PageResult<TblCeaInternalWebsiteApplyOracle> build = new PageResult<TblCeaInternalWebsiteApplyOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 内部网站申信息发布 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaInternalWebsiteApplyOracle> saveOrUpdateTblCeaInternalWebsiteApply(TblCeaInternalWebsiteApplyOracle param) {
		TblCeaInternalWebsiteApplyOracle result = tblCeaInternalWebsiteApplyOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 内部网站申信息发布 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaInternalWebsiteApply(Long id) {
		tblCeaInternalWebsiteApplyOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 内部网站申信息发布 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaInternalWebsiteApplyOracle> getTblCeaInternalWebsiteApply(Long id) {
		TblCeaInternalWebsiteApplyOracle result = tblCeaInternalWebsiteApplyOracleService.findById(id);
		if (Objects.nonNull(result.getContact())) {
			result.setContactName(tblStaffOracleService.getCreatorUserInfo(result.getContact()));
		}
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getProviderPeople())) {
			result.setProviderPeopleName(tblStaffOracleService.getCreatorUserInfo(result.getProviderPeople()));
		}
		if (Objects.nonNull(result.getInfoProviderWorkUnit())) {
			result.setInfoProviderWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(result.getInfoProviderWorkUnit()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
