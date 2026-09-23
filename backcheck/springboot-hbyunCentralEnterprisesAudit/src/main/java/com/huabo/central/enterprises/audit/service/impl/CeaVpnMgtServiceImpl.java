package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaVpnMgtOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaVpnMgtOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaVpnMgtService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaVpnMgtQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CeaVpnMgtServiceImpl implements CeaVpnMgtService {

	@Resource
	private TblCeaVpnMgtOracleService tblCeaVpnMgtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * VPN账号管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaVpnMgtOracle> getTblCeaVpnMgtList(TblCeaVpnMgtQueryParam param) {
		PageInfo<TblCeaVpnMgtOracle> pageInfo = tblCeaVpnMgtOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaVpnMgtOracle::getCreator).distinct().collect(Collectors.toList());
			List<Long> staffIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getStaffId())).map(TblCeaVpnMgtOracle::getStaffId)
					.distinct().collect(Collectors.toList());
			List<Long> thisBelongGroups = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getThisBelongGroup()))
					.map(TblCeaVpnMgtOracle::getThisBelongGroup).distinct().collect(Collectors.toList());
			Map<Long, TblStaffOracle> userInfoForIdMap = tblStaffOracleService.getUserInfoForIdMap(staffIds);
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			Map<Long, String> belongGroupInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(thisBelongGroups, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setStaff(userInfoForIdMap.getOrDefault(item.getStaffId(), null));
				item.setThisBelongGroupName(belongGroupInfoMap.getOrDefault(item.getThisBelongGroup(), ""));
			});
			PageResult<TblCeaVpnMgtOracle> build = new PageResult<TblCeaVpnMgtOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * VPN账号管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaVpnMgtOracle> saveOrUpdateTblCeaVpnMgt(TblCeaVpnMgtOracle param) {
		TblCeaVpnMgtOracle model = tblCeaVpnMgtOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * VPN账号管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaVpnMgt(Long id) {
		tblCeaVpnMgtOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * VPN账号管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaVpnMgtOracle> getTblCeaVpnMgt(Long id) {
		TblCeaVpnMgtOracle result = tblCeaVpnMgtOracleService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getThisBelongGroup())) {
			result.setThisBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(result.getThisBelongGroup()));
		}
		if (Objects.nonNull(result.getStaffId())) {
			result.setStaff(tblStaffOracleService.getUserInfoForId(result.getStaffId()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
