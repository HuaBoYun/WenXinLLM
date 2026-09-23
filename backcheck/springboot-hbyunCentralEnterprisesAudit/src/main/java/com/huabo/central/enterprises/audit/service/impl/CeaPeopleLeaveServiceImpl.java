package com.huabo.central.enterprises.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaCancelHolidayOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaPeopleLeaveOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaPeopleLeaveService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaPeopleLeaveQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CeaPeopleLeaveServiceImpl implements CeaPeopleLeaveService {

	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblCeaPeopleLeaveOracleService tblCeaPeopleLeaveOracleService;
	@Resource
	private TblCeaCancelHolidayOracleMapper tblCeaCancelHolidayOracleMapper;

	/**
	 * 人员请假单 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveList(TblCeaPeopleLeaveQueryParam param) {
		PageInfo<TblCeaPeopleLeaveOracle> pageInfo = tblCeaPeopleLeaveOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> staffIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getStaffId()))
					.map(TblCeaPeopleLeaveOracle::getStaffId).distinct().collect(Collectors.toList());
			Map<Long, TblStaffOracle> userInfoForIdMap = tblStaffOracleService.getUserInfoForIdMap(staffIds);
			List<Long> creators = pageInfo.getList().stream().map(TblCeaPeopleLeaveOracle::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setStaff(userInfoForIdMap.getOrDefault(item.getStaffId(), null));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				TblCeaCancelHolidayOracle ceaCancelHoliday = new TblCeaCancelHolidayOracle();
				ceaCancelHoliday.setRelationId(item.getId());
				//查询该请假单有没有对应的销假单
				TblCeaCancelHolidayOracle model = tblCeaCancelHolidayOracleMapper.selectOne(ceaCancelHoliday);
				if (Objects.nonNull(model)) {
					item.setRelationId(model.getId());
				}
			});
			PageResult<TblCeaPeopleLeaveOracle> build = new PageResult<TblCeaPeopleLeaveOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 人员请假单 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaPeopleLeaveOracle> saveOrUpdateTblCeaPeopleLeave(TblCeaPeopleLeaveOracle param) {
		//校验带薪休假
		long leaveDays = DateUtil.betweenDay(param.getLeavePeriodTimeStart(), param.getLeavePeriodTimeEnd(), true);
		leaveDays = leaveDays + 1;
		//请假天数
		param.setLeavedays((int) leaveDays);
		if (Objects.equals("带薪年休假", param.getLeaveReason())) {
			checkSaveOrUpdateTblCeaPeopleLeave(param);
		}
		TblCeaPeopleLeaveOracle tblCeaPeopleLeave = tblCeaPeopleLeaveOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, tblCeaPeopleLeave);
	}

	/**
	 * 校验带薪休假
	 * @param param
	 */
	private void checkSaveOrUpdateTblCeaPeopleLeave(TblCeaPeopleLeaveOracle param) {
		//人员请假天数
		long leaveDays = DateUtil.betweenDay(param.getLeavePeriodTimeStart(), param.getLeavePeriodTimeEnd(), true);
		leaveDays = leaveDays + 1;
		log.info("想要请假天数：{}", leaveDays);
		Date entryTime = param.getParworkdate();
		Date now = new Date();
		// 返回相差年数
		long betweenYear = DateUtil.betweenYear(entryTime, now, true);
		log.info("相差多少年：{}", betweenYear);
		if (betweenYear == 0) {
			throw new ServiceException(400, "工作不满一年");
		}
		long year = 0L;
		Date startTime = com.vip.vjtools.vjkit.time.DateUtil.beginOfYear(now);
		Date endTime = com.vip.vjtools.vjkit.time.DateUtil.nextYear(now);
		TblCeaPeopleLeaveQueryParam queryParam = new TblCeaPeopleLeaveQueryParam();
		queryParam.setStaffId(param.getStaffId());
		queryParam.setStartTime(startTime);
		queryParam.setEndTime(endTime);
		//该用户今天创建的请假单列表
		List<TblCeaPeopleLeaveOracle> list = tblCeaPeopleLeaveOracleService.getList(queryParam).getList();
		if (CollectionUtil.isNotEmpty(list)) {
			for (TblCeaPeopleLeaveOracle item : list) {
				//已请假天数
				long day = DateUtil.betweenDay(item.getLeavePeriodTimeStart(), item.getLeavePeriodTimeEnd(), true);
				day = day + 1;
				year += day;
			}
		}
		log.info("已请假天数总和：{}", year);
		//累计工作时间满1年不满10年的，每年休假5天
		if (1 <= betweenYear && betweenYear < 10) {
			if (5 - year <= 0) {
				throw new ServiceException(400, "累计工作时间满1年不满10年的，每年休假5天,已无假期日");
			}
		}
		//满10年不满20年的，每年休假10天
		if (10 <= betweenYear && betweenYear < 20) {
			if (10 - year <= 0) {
				throw new ServiceException(400, "满10年不满20年的，每年休假10天,已无假期日");
			}
		}
		//满20年以上的，每年休假15天
		if (betweenYear >= 20) {
			if (15 - year <= 0) {
				throw new ServiceException(400, "满20年以上的，每年休假15天,已无假期日");
			}
		}
	}

	/**
	 * 人员请假单 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaPeopleLeave(Long id) {
		tblCeaPeopleLeaveOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 人员请假单 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeave(Long id) {
		TblCeaPeopleLeaveOracle ceaPeopleLeave = tblCeaPeopleLeaveOracleService.findById(id);
		ceaPeopleLeave.setStaff(tblStaffOracleService.getUserInfoForId(ceaPeopleLeave.getStaffId()));
		TblCeaCancelHolidayOracle ceaCancelHoliday = new TblCeaCancelHolidayOracle();
		ceaCancelHoliday.setRelationId(ceaPeopleLeave.getId());
		//查询该请假单有没有对应的销假单
		TblCeaCancelHolidayOracle model = tblCeaCancelHolidayOracleMapper.selectOne(ceaCancelHoliday);
		if (Objects.nonNull(model)) {
			ceaPeopleLeave.setRelationId(model.getId());
		}
		return MyResponseFormat.retParam(200, 200, ceaPeopleLeave);
	}

	/**
	 * 人员请假台账
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveAllList(TblCeaPeopleLeaveQueryParam param) {
		PageInfo<TblCeaPeopleLeaveOracle> pageInfo = tblCeaPeopleLeaveOracleService.getTblCeaPeopleLeaveAllList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> staffIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getStaffId()))
					.map(TblCeaPeopleLeaveOracle::getStaffId).distinct().collect(Collectors.toList());
			Map<Long, TblStaffOracle> userInfoForIdMap = tblStaffOracleService.getUserInfoForIdMap(staffIds);
			List<Long> creators = pageInfo.getList().stream().map(TblCeaPeopleLeaveOracle::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setStaff(userInfoForIdMap.getOrDefault(item.getStaffId(), null));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				TblCeaCancelHolidayOracle ceaCancelHoliday = new TblCeaCancelHolidayOracle();
				ceaCancelHoliday.setRelationId(item.getId());
				//查询该请假单有没有对应的销假单
				TblCeaCancelHolidayOracle model = tblCeaCancelHolidayOracleMapper.selectOne(ceaCancelHoliday);
				if (Objects.nonNull(model)) {
					item.setRelationId(model.getId());
				}
			});
			PageResult<TblCeaPeopleLeaveOracle> build = new PageResult<TblCeaPeopleLeaveOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}
}
