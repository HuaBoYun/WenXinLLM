package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpManageOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaIpManageOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaIpManageService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpManageQueryParam;
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
public class CeaIpManageServiceImpl implements CeaIpManageService {

	@Resource
	private TblCeaIpManageOracleService ceaIpManageOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * IP地址管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaIpManageOracle> getTblCeaIpManageList(TblCeaIpManageQueryParam param) {
		PageInfo<TblCeaIpManageOracle> pageInfo = ceaIpManageOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			//申请人、创建人
			List<Long> applyPeoples = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyPeople())).distinct()
					.map(TblCeaIpManageOracle::getApplyPeople).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaIpManageOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(applyPeoples)) {
				creatorList.addAll(applyPeoples);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			//科室、部门
			List<Long> workUnitList = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyWorkUnit()))
					.map(TblCeaIpManageOracle::getApplyWorkUnit).collect(Collectors.toList());
			//单位、集团、公司
			List<Long> belongGroupList = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyBelongGroup()))
					.map(TblCeaIpManageOracle::getApplyBelongGroup).collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(workUnitList, ","));
			Map<Long, String> belongGroupUserInfoMap = tblStaffOracleService.getIdUserInfoMap(StringUtils.join(belongGroupList, ","));
			pageInfo.getList().forEach(x -> {
				x.setCreatorName(creatorUserInfoMap.getOrDefault(x.getCreator(), ""));
				x.setApplyPeopleName(creatorUserInfoMap.getOrDefault(x.getApplyPeople(), ""));
				x.setApplyWorkUnitName(workUnitUserInfoMap.getOrDefault(x.getApplyWorkUnit(), ""));
				x.setApplyBelongGroupName(belongGroupUserInfoMap.getOrDefault(x.getApplyBelongGroup(), ""));
			});
			PageResult<TblCeaIpManageOracle> build = new PageResult<TblCeaIpManageOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * IP地址管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaIpManageOracle> saveOrUpdateTblCeaIpManage(TblCeaIpManageOracle param) {
		TblCeaIpManageOracle ipManage = ceaIpManageOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, ipManage);
	}

	/**
	 * IP地址管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaIpManage(Long id) {
		ceaIpManageOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * IP地址管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaIpManageOracle> getTblCeaIpManage(Long id) {
		TblCeaIpManageOracle result = ceaIpManageOracleService.findById(id);
		if (Objects.nonNull(result.getApplyPeople())) {
			result.setApplyPeopleName(tblStaffOracleService.getCreatorUserInfo(result.getApplyPeople()));
		}
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getApplyBelongGroup())) {
			result.setApplyBelongGroupName(tblStaffOracleService.getIdUserInfo(result.getApplyBelongGroup()));
		}
		if (Objects.nonNull(result.getApplyWorkUnit())) {
			result.setApplyWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(result.getApplyWorkUnit()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
