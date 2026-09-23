package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblSystemRefReminderV;
import com.huabo.fxgl.mapper.TblSystemRefReminderV1Mapper;
import com.huabo.fxgl.service.StatisticsService;
import com.huabo.fxgl.util.MyJsonBean;
import com.huabo.fxgl.util.MyResponseFormat;
import com.huabo.fxgl.vo.BusinessTableInfoResult;
import com.huabo.fxgl.vo.StatisticsReminderMonthlyPenetrateResult;
import com.huabo.fxgl.vo.StatisticsReminderParam;
import com.huabo.fxgl.vo.StatisticsReminderResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Resource
	private TblSystemRefReminderV1Mapper tblSystemRefReminderV1Mapper;
	@Resource
	private UserProvider userProvider;

	/**
	 * 催办统计
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<StatisticsReminderResult> getStatisticsReminderList(StatisticsReminderParam param) {
		TblStaffUtil loginStaff = null;
		try {
			loginStaff = userProvider.get();
		} catch (Exception e) {
			return MyResponseFormat.retParam(0, 20006, null);
		}
		if (loginStaff == null) {
			return MyResponseFormat.retParam(0, 20006, null);
		}
		StatisticsReminderResult result = new StatisticsReminderResult();
		LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
		LocalDate endOfYear = LocalDate.now().withDayOfYear(1).withYear(LocalDate.now().getYear() + 1);
		//一体化管控措施-数据
		QueryWrapper<TblSystemRefReminderV> wrapper = new QueryWrapper<TblSystemRefReminderV>();
		wrapper.eq("REMINDERBUSINESSTABLE", "TBL_CONTROL_ENTRIES");
		wrapper.ge("CREATEDTIME", startOfYear).lt("CREATEDTIME", endOfYear);
		List<TblSystemRefReminderV> list = tblSystemRefReminderV1Mapper.selectList(wrapper);
		if (CollectionUtil.isNotEmpty(list)) {
			//key-业务ID  value-催办记录
			Map<Long, List<TblSystemRefReminderV>> map = list.stream()
					.collect(Collectors.groupingBy(TblSystemRefReminderV::getReminderBusinessTableId));
			int size = map.size();
			result.setIntegratedControlMeasuresTotal(size);
			//已完成
			int a = 0;
			//未完成
			int b = 0;
			//超期
			int c = 0;
			for (Map.Entry<Long, List<TblSystemRefReminderV>> it : map.entrySet()) {
				Optional<TblSystemRefReminderV> optional = it.getValue().stream().max(Comparator.comparing(TblSystemRefReminderV::getCreatedTime));
				TblSystemRefReminderV tblSystemRefReminderV = optional.get();
				//完成数量
				if (Objects.nonNull(tblSystemRefReminderV.getCompleteTime())) {
					a = a + 1;
				} else {
					b = b + 1;
				}
				if (Objects.nonNull(tblSystemRefReminderV.getCompleteTime())) {
					//有完成时间，对比完成时间是否大于超期时间
					if (tblSystemRefReminderV.getReminderTime().before(tblSystemRefReminderV.getCompleteTime())) {
						c = c + 1;
					}
				}
			}
			result.setIntegratedControlMeasuresCompleteCount(a);
			result.setIntegratedControlMeasuresNoCompleteCount(b);
			result.setIntegratedControlMeasuresOverdueCount(c);
		}
		//月度评估-数据
		QueryWrapper<TblSystemRefReminderV> wrapper1 = new QueryWrapper<TblSystemRefReminderV>();
		wrapper1.eq("REMINDERBUSINESSTABLE", "TBL_RISK_MONTHLY_EVALUATION");
		wrapper1.ge("CREATEDTIME", startOfYear).lt("CREATEDTIME", endOfYear);
		List<TblSystemRefReminderV> list1 = tblSystemRefReminderV1Mapper.selectList(wrapper1);
		if (CollectionUtil.isNotEmpty(list1)) {
			//key-业务ID  value-催办记录
			Map<Long, List<TblSystemRefReminderV>> map = list1.stream()
					.collect(Collectors.groupingBy(TblSystemRefReminderV::getReminderBusinessTableId));
			int size = map.size();
			result.setMonthlyTotal(size);
			//已完成
			int a = 0;
			//未完成
			int b = 0;
			//超期
			int c = 0;
			for (Map.Entry<Long, List<TblSystemRefReminderV>> it : map.entrySet()) {
				Optional<TblSystemRefReminderV> optional = it.getValue().stream().max(Comparator.comparing(TblSystemRefReminderV::getCreatedTime));
				TblSystemRefReminderV tblSystemRefReminderV = optional.get();
				//完成数量
				if (Objects.nonNull(tblSystemRefReminderV.getCompleteTime())) {
					a = a + 1;
				} else {
					b = b + 1;
				}
				YearMonth currentYearMonth = YearMonth.now();
				// 获取当前月份的第5日
				LocalDate fifthDayOfMonth = currentYearMonth.atDay(5);
				Instant instant = fifthDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
				Date date = Date.from(instant);
				if (Objects.nonNull(tblSystemRefReminderV.getCompleteTime())) {
					//有完成时间，对比完成时间是否大于超期时间
					if (date.before(tblSystemRefReminderV.getCompleteTime())) {
						c = c + 1;
					}
				}
			}
			result.setMonthlyCompleteCount(a);
			result.setMonthlyNoCompleteCount(b);
			result.setMonthlyOverdueCount(c);
		}
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 催办统计-穿透-月度评估
	 * @param type 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
	 * @return
	 */
	@Override
	public MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> getStatisticsReminderMonthlyPenetrateList(Integer type) {
		TblStaffUtil loginStaff = null;
		try {
			loginStaff = userProvider.get();
		} catch (Exception e) {
			return MyResponseFormat.retParam(0, 20006, null);
		}
		if (loginStaff == null) {
			return MyResponseFormat.retParam(0, 20006, null);
		}
		LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
		LocalDate endOfYear = LocalDate.now().withDayOfYear(1).withYear(LocalDate.now().getYear() + 1);
		//一体化管控措施-数据
		QueryWrapper<TblSystemRefReminderV> wrapper = new QueryWrapper<TblSystemRefReminderV>();
		wrapper.eq("REMINDERBUSINESSTABLE", "TBL_RISK_MONTHLY_EVALUATION");
		wrapper.ge("CREATEDTIME", startOfYear).lt("CREATEDTIME", endOfYear);
		List<TblSystemRefReminderV> list = tblSystemRefReminderV1Mapper.selectList(wrapper);
		List<StatisticsReminderMonthlyPenetrateResult> arrayList = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(list)) {
			//key-业务ID  value-催办记录
			Map<Long, List<TblSystemRefReminderV>> map = list.stream()
					.collect(Collectors.groupingBy(TblSystemRefReminderV::getReminderBusinessTableId));
			//已完成
			int a = 0;
			//未完成
			int b = 0;
			//超期
			int c = 0;
			for (Map.Entry<Long, List<TblSystemRefReminderV>> it : map.entrySet()) {
				Optional<TblSystemRefReminderV> optional = it.getValue().stream().max(Comparator.comparing(TblSystemRefReminderV::getCreatedTime));
				TblSystemRefReminderV v = optional.get();
				StatisticsReminderMonthlyPenetrateResult result = new StatisticsReminderMonthlyPenetrateResult();
				result.setMsg(v.getReminderContent());
				result.setStaffId(v.getReminderStaffId());
				result.setTime(v.getReminderTime());
//				if (Objects.nonNull(v.getReminderStaffId())) {
//					String name = tblSystemRefReminderV1Mapper.getStaffName(v.getReminderStaffId());
//					result.setStaffName(name);
//				}
				BusinessTableInfoResult info = tblSystemRefReminderV1Mapper.getBusinessTableInfo(v.getReminderBusinessTableId());
				if (Objects.nonNull(info)) {
					result.setNo(info.getNo());
					result.setName(info.getName());
				}
				if (Objects.equals(type, 1)) {
					arrayList.add(result);
					continue;
				}

				//完成数量
				if (Objects.nonNull(v.getCompleteTime())) {
					a = a + 1;
					if (Objects.equals(type, 2)) {
						arrayList.add(result);
						continue;
					}
				} else {
					b = b + 1;
					if (Objects.equals(type, 3)) {
						arrayList.add(result);
						continue;
					}
				}
				YearMonth currentYearMonth = YearMonth.now();
				// 获取当前月份的第5日
				LocalDate fifthDayOfMonth = currentYearMonth.atDay(5);
				Instant instant = fifthDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant();
				Date date = Date.from(instant);
				if (Objects.nonNull(v.getCompleteTime())) {
					//有完成时间，对比完成时间是否大于超期时间
					if (date.before(v.getCompleteTime())) {
						c = c + 1;
						if (Objects.equals(type, 3)) {
							arrayList.add(result);
							continue;
						}
					}
				}
			}
		}
		return MyResponseFormat.retParam(200, 200, arrayList);
	}

	/**
	 * 催办统计-穿透-一体化管控措施
	 * @param type 1-催办总数量 2-完成数量 3-未完成数量 4-超期数量
	 * @return
	 */
	@Override
	public MyJsonBean<List<StatisticsReminderMonthlyPenetrateResult>> getStatisticsReminderControlPenetrateList(Integer type) {
		TblStaffUtil loginStaff = null;
		try {
			loginStaff = userProvider.get();
		} catch (Exception e) {
			return MyResponseFormat.retParam(0, 20006, null);
		}
		if (loginStaff == null) {
			return MyResponseFormat.retParam(0, 20006, null);
		}
		LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
		LocalDate endOfYear = LocalDate.now().withDayOfYear(1).withYear(LocalDate.now().getYear() + 1);
		//一体化管控措施-数据
		QueryWrapper<TblSystemRefReminderV> wrapper = new QueryWrapper<TblSystemRefReminderV>();
		wrapper.eq("REMINDERBUSINESSTABLE", "TBL_CONTROL_ENTRIES");
		wrapper.ge("CREATEDTIME", startOfYear).lt("CREATEDTIME", endOfYear);
		List<TblSystemRefReminderV> list = tblSystemRefReminderV1Mapper.selectList(wrapper);
		List<StatisticsReminderMonthlyPenetrateResult> arrayList = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(list)) {
			//key-业务ID  value-催办记录
			Map<Long, List<TblSystemRefReminderV>> map = list.stream()
					.collect(Collectors.groupingBy(TblSystemRefReminderV::getReminderBusinessTableId));
			//已完成
			int a = 0;
			//未完成
			int b = 0;
			//超期
			int c = 0;
			for (Map.Entry<Long, List<TblSystemRefReminderV>> it : map.entrySet()) {
				Optional<TblSystemRefReminderV> optional = it.getValue().stream().max(Comparator.comparing(TblSystemRefReminderV::getCreatedTime));
				TblSystemRefReminderV v = optional.get();
				StatisticsReminderMonthlyPenetrateResult result = new StatisticsReminderMonthlyPenetrateResult();
				result.setMsg(v.getReminderContent());
				result.setStaffId(v.getReminderStaffId());
				result.setTime(v.getReminderTime());
//				if (Objects.nonNull(v.getReminderStaffId())) {
//					String name = tblSystemRefReminderV1Mapper.getStaffName(v.getReminderStaffId());
//					result.setStaffName(name);
//				}
				BusinessTableInfoResult info = tblSystemRefReminderV1Mapper.getBusinessTableInfo1(v.getReminderBusinessTableId());
				if (Objects.nonNull(info)) {
					result.setNo(info.getNo());
					result.setName(v.getReminderContent());
				}
				if (Objects.equals(type, 1)) {
					arrayList.add(result);
					continue;
				}
				//完成数量
				if (Objects.nonNull(v.getCompleteTime())) {
					a = a + 1;
					if (Objects.equals(type, 2)) {
						arrayList.add(result);
						continue;
					}
				} else {
					b = b + 1;
					if (Objects.equals(type, 3)) {
						arrayList.add(result);
						continue;
					}
				}
				if (Objects.nonNull(v.getCompleteTime())) {
					//有完成时间，对比完成时间是否大于超期时间
					if (v.getReminderTime().before(v.getCompleteTime())) {
						c = c + 1;
					}
				}
			}
		}
		return MyResponseFormat.retParam(200, 200, arrayList);
	}
}
