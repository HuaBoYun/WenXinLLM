package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaMailMgtOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaMailMgtOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaMailMgtService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaMailMgtQueryParam;
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
public class CeaMailMgtServiceImpl implements CeaMailMgtService {

	@Resource
	private TblCeaMailMgtOracleService tblCeaMailMgtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;


	/**
	 * 中石油邮箱管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaMailMgtOracle> getTblCeaMailMgtList(TblCeaMailMgtQueryParam param) {
		PageInfo<TblCeaMailMgtOracle> pageInfo = tblCeaMailMgtOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> applyIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyId())).distinct()
					.map(TblCeaMailMgtOracle::getApplyId).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaMailMgtOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(applyIds)) {
				creatorList.addAll(applyIds);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			List<Long> applyBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyBelongGroup())).distinct()
					.map(TblCeaMailMgtOracle::getApplyBelongGroup).distinct().collect(Collectors.toList());
			Map<Long, String> belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(applyBelongGroups, ","));
			Map<Long, TblStaffOracle> userInfoForIdMap = tblStaffOracleService.getUserInfoForIdMap(applyIds);
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setStaff(userInfoForIdMap.getOrDefault(item.getApplyId(), null));
				item.setApplyBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getApplyBelongGroup(), ""));
			});
			PageResult<TblCeaMailMgtOracle> build = new PageResult<TblCeaMailMgtOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 中石油邮箱管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaMailMgtOracle> saveOrUpdateTblCeaMailMgt(TblCeaMailMgtOracle param) {
		TblCeaMailMgtOracle model = tblCeaMailMgtOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 中石油邮箱管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaMailMgt(Long id) {
		tblCeaMailMgtOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 中石油邮箱管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaMailMgtOracle> getTblCeaMailMgt(Long id) {
		TblCeaMailMgtOracle result = tblCeaMailMgtOracleService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getApplyId())) {
			result.setStaff(tblStaffOracleService.getUserInfoForId(result.getApplyId()));
		}
		if (Objects.nonNull(result.getApplyBelongGroup())) {
			result.setApplyBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(result.getApplyBelongGroup()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
