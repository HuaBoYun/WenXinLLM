package com.huabo.central.enterprises.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayExp;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaCancelHolidayExpMapper;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaCancelHolidayOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExpatriateApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaExpatriateApplyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExpatriateApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;
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
public class CeaExpatriateApplyServiceImpl implements CeaExpatriateApplyService {

	@Resource
	private TblCeaExpatriateApplyOracleService tblCeaExpatriateApplyOracleService;
	@Resource
	private TblCeaCancelHolidayOracleMapper tblCeaCancelHolidayOracleMapper;
	@Resource
	private TblCeaCancelHolidayExpMapper tblCeaCancelHolidayExpMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 外派任务 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyList(TblCeaExpatriateApplyQueryParam param) {
		PageInfo<TblCeaExpatriateApplyOracle> pageInfo = tblCeaExpatriateApplyOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			//联系人、供单位负责人、创建人
			List<Long> applyPeople = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyPeople())).distinct()
					.map(TblCeaExpatriateApplyOracle::getApplyPeople).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaExpatriateApplyOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(applyPeople)) {
				creatorList.addAll(applyPeople);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			//信息提供单位
			List<Long> applyWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyWorkUnit())).distinct()
					.map(TblCeaExpatriateApplyOracle::getApplyWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(applyWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setApplyPeopleName(creatorUserInfoMap.getOrDefault(item.getApplyPeople(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setApplyWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getApplyWorkUnit(), ""));
				TblCeaCancelHolidayExp ceaCancelHoliday = new TblCeaCancelHolidayExp();
				ceaCancelHoliday.setRelationId(item.getId());
				//查询该外派任务有没有对应的销假单
				TblCeaCancelHolidayExp model = tblCeaCancelHolidayExpMapper.selectOne(ceaCancelHoliday);
				if (Objects.nonNull(model)) {
					item.setRelationId(model.getId());
				}
			});
			PageResult<TblCeaExpatriateApplyOracle> build = new PageResult<TblCeaExpatriateApplyOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 外派任务 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaExpatriateApplyOracle> saveOrUpdateTblCeaExpatriateApply(TblCeaExpatriateApplyOracle param) {
		//计算预计外出天数
		if (Objects.nonNull(param.getApplyExpatriateTime()) && Objects.nonNull(param.getApplyReturnTime())) {
			long leaveDays = DateUtil.betweenDay(param.getApplyExpatriateTime(), param.getApplyReturnTime(), true);
			leaveDays = leaveDays + 1;
			param.setLeavedays((int) leaveDays);
		}
		TblCeaExpatriateApplyOracle model = tblCeaExpatriateApplyOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 外派任务 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaExpatriateApply(Long id) {
		tblCeaExpatriateApplyOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 外派任务 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApply(Long id) {
		TblCeaExpatriateApplyOracle result = tblCeaExpatriateApplyOracleService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getApplyPeople())) {
			result.setApplyPeopleName(tblStaffOracleService.getCreatorUserInfo(result.getApplyPeople()));
		}
		if (Objects.nonNull(result.getApplyWorkUnit())) {
			result.setApplyWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(result.getApplyWorkUnit()));
		}
		TblCeaCancelHolidayExp ceaCancelHoliday = new TblCeaCancelHolidayExp();
		ceaCancelHoliday.setRelationId(result.getId());
		//查询该外派任务有没有对应的销假单
		TblCeaCancelHolidayExp model = tblCeaCancelHolidayExpMapper.selectOne(ceaCancelHoliday);
		if (Objects.nonNull(model)) {
			result.setRelationId(model.getId());
		}
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 外派人员台账 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyAllList(UserAllQueryParam param) {
		PageInfo<TblCeaExpatriateApplyOracle> pageInfo = tblCeaExpatriateApplyOracleService.getTblCeaExpatriateApplyAllList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())){
			List<Long> creatorList = new ArrayList<>();
			//联系人、供单位负责人、创建人
			List<Long> applyPeople = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyPeople())).distinct()
					.map(TblCeaExpatriateApplyOracle::getApplyPeople).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaExpatriateApplyOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(applyPeople)) {
				creatorList.addAll(applyPeople);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			//信息提供单位
			List<Long> applyWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyWorkUnit())).distinct()
					.map(TblCeaExpatriateApplyOracle::getApplyWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(applyWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setApplyPeopleName(creatorUserInfoMap.getOrDefault(item.getApplyPeople(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setApplyWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getApplyWorkUnit(), ""));
				TblCeaCancelHolidayExp ceaCancelHoliday = new TblCeaCancelHolidayExp();
				ceaCancelHoliday.setRelationId(item.getId());
				//查询该外派任务有没有对应的销假单
				TblCeaCancelHolidayExp model = tblCeaCancelHolidayExpMapper.selectOne(ceaCancelHoliday);
				if (Objects.nonNull(model)) {
					item.setRelationId(model.getId());
				}
			});
		}
		PageResult<TblCeaExpatriateApplyOracle> build = new PageResult<TblCeaExpatriateApplyOracle>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}
}
