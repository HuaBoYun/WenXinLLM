package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaGroupOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaGroupOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaGroupService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaGroupQueryParam;
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
public class CeaGroupServiceImpl implements CeaGroupService {

	@Resource
	private TblCeaGroupOracleService tblCeaGroupOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;


	/**
	 * 群组 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaGroupOracle> getTblCeaGroupList(TblCeaGroupQueryParam param) {
		PageInfo<TblCeaGroupOracle> pageInfo = tblCeaGroupOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			//联系人、供单位负责人、创建人
			List<Long> staffIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getStaffId())).distinct()
					.map(TblCeaGroupOracle::getStaffId).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaGroupOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(staffIds)) {
				creatorList.addAll(staffIds);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			pageInfo.getList().forEach(item -> {
				item.setStaffName(creatorUserInfoMap.getOrDefault(item.getStaffId(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				if (StringUtils.isNotBlank(item.getStaffIds())) {
					item.setStaffs(tblStaffOracleService.getCreatorUserInfos(item.getStaffIds()));
				}
			});
			PageResult<TblCeaGroupOracle> build = new PageResult<TblCeaGroupOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 群组 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaGroupOracle> saveOrUpdateTblCeaGroup(TblCeaGroupOracle param) {
		TblCeaGroupOracle model = tblCeaGroupOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 群组 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaGroup(Long id) {
		tblCeaGroupOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 群组 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaGroupOracle> getTblCeaGroup(Long id) {
		TblCeaGroupOracle result = tblCeaGroupOracleService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getStaffId())) {
			result.setStaffName(tblStaffOracleService.getCreatorUserInfo(result.getStaffId()));
		}
		if (StringUtils.isNotBlank(result.getStaffIds())) {
			result.setStaffs(tblStaffOracleService.getCreatorUserInfos(result.getStaffIds()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
