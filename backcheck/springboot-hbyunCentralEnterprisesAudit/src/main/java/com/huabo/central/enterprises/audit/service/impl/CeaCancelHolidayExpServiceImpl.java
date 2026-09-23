package com.huabo.central.enterprises.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayExp;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaExpatriateApplyOracleMapper;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaPeopleLeaveOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaCancelHolidayExpService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExpatriateApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaPeopleLeaveOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaCancelHolidayExpService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaCancelHolidayExpServiceImpl implements CeaCancelHolidayExpService {

	@Resource
	private TblCeaCancelHolidayExpService tblCeaCancelHolidayExpService;
	@Resource
	private TblCeaPeopleLeaveOracleService tblCeaPeopleLeaveOracleService;
	@Resource
	private TblCeaPeopleLeaveOracleMapper tblCeaPeopleLeaveOracleMapper;
	@Resource
	private TblCeaExpatriateApplyOracleMapper tblCeaExpatriateApplyOracleMapper;
	@Resource
	private TblCeaExpatriateApplyOracleService tblCeaExpatriateApplyOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 销假单 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaCancelHolidayExp> getTblCeaCancelHolidayExpList(TblCeaCancelHolidayQueryParam param) {
		PageInfo<TblCeaCancelHolidayExp> pageInfo = tblCeaCancelHolidayExpService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			//销假人、创建人
			List<Long> peoples = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getPeople())).distinct()
					.map(TblCeaCancelHolidayExp::getPeople).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaCancelHolidayExp::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(peoples)) {
				creatorList.addAll(peoples);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			//信息提供单位
			List<Long> peopleWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getPeopleWorkUnit())).distinct()
					.map(TblCeaCancelHolidayExp::getPeopleWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(peopleWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setPeopleName(creatorUserInfoMap.getOrDefault(item.getPeople(), ""));
				item.setPeopleWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getPeopleWorkUnit(), ""));
			});
			PageResult<TblCeaCancelHolidayExp> build = new PageResult<TblCeaCancelHolidayExp>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 销假单 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<TblCeaCancelHolidayExp> saveOrUpdateTblCeaCancelHolidayExp(TblCeaCancelHolidayExp param) {
		//1-请假单 2-外派任务
		if (Objects.equals(param.getCancelType(), 1)) {
			TblCeaPeopleLeaveOracle ceaPeopleLeaveFind = new TblCeaPeopleLeaveOracle();
			ceaPeopleLeaveFind.setId(param.getRelationId());
			TblCeaPeopleLeaveOracle ceaPeopleLeave = tblCeaPeopleLeaveOracleMapper.selectOne(ceaPeopleLeaveFind);
			if (Objects.isNull(ceaPeopleLeave)) {
				throw new ServiceException(400, "关联ID不存在");
			}
			Date leavePeriodTimeStart = ceaPeopleLeave.getLeavePeriodTimeStart();
			Date fillFormTime = param.getFillFormTime();
			//销假时间 - 请假开始时间 = 等于实际请假天数
			long leaveDays = DateUtil.betweenDay(fillFormTime, leavePeriodTimeStart, true);
			leaveDays = leaveDays + 1;
			//实际请假天数 大于 预计请假天数 = 超假 反之 提前
			if (leaveDays > ceaPeopleLeave.getLeavedays()) {
				param.setHolidayType("超假");
			} else {
				param.setHolidayType("提前");
			}
			TblCeaPeopleLeaveOracle ceaPeopleLeaveUpdate = new TblCeaPeopleLeaveOracle();
			ceaPeopleLeaveUpdate.setId(ceaPeopleLeave.getId());
			ceaPeopleLeaveUpdate.setActualleavedays((int) leaveDays);
			tblCeaPeopleLeaveOracleService.saveOrUpdate(ceaPeopleLeaveUpdate);
		} else {
			TblCeaExpatriateApplyOracle expatriateApplyFind = new TblCeaExpatriateApplyOracle();
			expatriateApplyFind.setId(param.getRelationId());
			TblCeaExpatriateApplyOracle expatriateApply = tblCeaExpatriateApplyOracleMapper.selectOne(expatriateApplyFind);
			if (Objects.isNull(expatriateApply)) {
				throw new ServiceException(400, "关联ID不存在");
			}
			Date leavePeriodTimeStart = expatriateApply.getApplyExpatriateTime();
			Date fillFormTime = param.getFillFormTime();
			//销假时间 - 申请外出时间 = 等于实际外出天数
			long leaveDays = DateUtil.betweenDay(fillFormTime, leavePeriodTimeStart, true);
			leaveDays = leaveDays + 1;
			//实际请假天数 大于 预计请假天数 = 超假 反之 提前
			if (leaveDays > expatriateApply.getLeavedays()) {
				param.setHolidayType("超假");
			} else {
				param.setHolidayType("提前");
			}
			TblCeaExpatriateApplyOracle expatriateApplyUpdate = new TblCeaExpatriateApplyOracle();
			expatriateApplyUpdate.setId(expatriateApply.getId());
			expatriateApplyUpdate.setActualleavedays((int) leaveDays);
			tblCeaExpatriateApplyOracleService.saveOrUpdate(expatriateApplyUpdate);
		}
		TblCeaCancelHolidayExp result = tblCeaCancelHolidayExpService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 销假单 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaCancelHolidayExp(Long id) {
		tblCeaCancelHolidayExpService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 销假单 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaCancelHolidayExp> getTblCeaCancelHolidayExp(Long id) {
		TblCeaCancelHolidayExp result = tblCeaCancelHolidayExpService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getPeople())) {
			result.setPeopleName(tblStaffOracleService.getCreatorUserInfo(result.getPeople()));
		}
		if (Objects.nonNull(result.getPeopleWorkUnit())) {
			result.setPeopleWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(result.getPeopleWorkUnit()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
