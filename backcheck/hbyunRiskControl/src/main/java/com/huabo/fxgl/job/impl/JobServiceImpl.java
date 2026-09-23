package com.huabo.fxgl.job.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.fxgl.entity.TblControlEntries;
import com.huabo.fxgl.entity.TblRiskMonthlyEvaluationEntity;
import com.huabo.fxgl.entity.TblSystemRefReminderV;
import com.huabo.fxgl.job.JobService;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.mapper.TblRiskMonthlyEvaluationMapper;
import com.huabo.fxgl.mapper.TblSystemRefReminderV1Mapper;
import com.vip.vjtools.vjkit.time.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class JobServiceImpl implements JobService {

	@Resource
	private TblControlEntriesMapper tblControlEntriesMapper;
	@Resource
	private TblSystemRefReminderV1Mapper tblSystemRefReminderV1Mapper;
	@Resource
	private TblRiskMonthlyEvaluationMapper tblRiskMonthlyEvaluationMapper;

	/**
	 * 自动催办触发（重大风险-月度评估-一体化管控措施 提前3天内容 催办提醒）
	 * TBL_CONTROL_ENTRIES
	 * @return
	 */
	@Override
	public void reminder() {
		Date now = new Date();
		QueryWrapper<TblControlEntries> wrapper = new QueryWrapper<TblControlEntries>();
		wrapper.isNotNull("FIELD1").ne("FIELD1", "");
		wrapper.isNotNull("FIELD2");
		wrapper.isNotNull("FIELD13");
		List<TblControlEntries> controlEntriesList = tblControlEntriesMapper.selectList(wrapper);
		controlEntriesList.forEach(item -> {
			Date date = DateUtil.subDays(item.getField2(), 3);
			if (Objects.isNull(item.getField5())) {
				//没有完成时间，按照预计完成时间提前
				if (date.before(now)) {
					doSaveRefReminder(item, now);
				}
			} else {
				//已有完成时间 不需要催办 更新状态即可
				doUpdateRefReminder(item);
			}
		});
	}

	/**
	 * 自动催办触发（重大风险-月度评估 催办提醒）
	 */
	@Override
	public void reminderMonthlyEvaluateContent() {
		QueryWrapper<TblRiskMonthlyEvaluationEntity> wrapper = new QueryWrapper<TblRiskMonthlyEvaluationEntity>();
		wrapper.isNull("REASON");
		List<TblRiskMonthlyEvaluationEntity> list = tblRiskMonthlyEvaluationMapper.selectList(wrapper);
		list.forEach(item->{
			TblSystemRefReminderV v = tblSystemRefReminderV1Mapper.getByrefopmId(item.getId().longValue());
			if(v!=null) {
				return;
			}
			TblSystemRefReminderV save = new TblSystemRefReminderV();
			save.setIsRead(0);
			save.setReminderStaffId(item.getCreateStaffid().longValue());
			save.setReminderContent("重大风险-月度评估-风险变化趋势/上升下降原因/是否新增风险 未填写");
			save.setReminderBusinessTableId(item.getId().longValue());
			save.setReminderBusinessTable("TBL_RISK_MONTHLY_EVALUATION");
			Date now = new Date();
			save.setReminderTime(now);
			save.setCreatedTime(now);
			save.setId(RandomUtil.uuLongId());
			save.setState(0);
			tblSystemRefReminderV1Mapper.insert(save);
		});
		
		QueryWrapper<TblRiskMonthlyEvaluationEntity> wapper = new QueryWrapper<TblRiskMonthlyEvaluationEntity>();
		wapper.isNotNull("REASON");
		List<TblRiskMonthlyEvaluationEntity> ylist = tblRiskMonthlyEvaluationMapper.selectList(wapper);
		ylist.forEach(item->{
			TblSystemRefReminderV v = tblSystemRefReminderV1Mapper.getByrefopmId(item.getId().longValue());
			v.setIsComplete(1);
			v.setIsRead(1);
			tblSystemRefReminderV1Mapper.updateById(v);
		});
	}

	private void doUpdateRefReminder(TblControlEntries model) {
		QueryWrapper<TblSystemRefReminderV> wrapper = new QueryWrapper<TblSystemRefReminderV>();
		wrapper.eq("REMINDERBUSINESSTABLEID", model.getId());
		TblSystemRefReminderV tblSystemRefReminderV = new TblSystemRefReminderV();
		tblSystemRefReminderV.setIsComplete(1);
		tblSystemRefReminderV.setIsRead(1);
		tblSystemRefReminderV.setCompleteTime(model.getField5());
		tblSystemRefReminderV1Mapper.update(tblSystemRefReminderV, wrapper);
	}

	private void doSaveRefReminder(TblControlEntries model, Date now) {
		TblSystemRefReminderV save = new TblSystemRefReminderV();
		save.setIsRead(0);
		save.setReminderStaffId(model.getField13().longValue());
		save.setReminderContent(model.getField1());
		save.setReminderBusinessTableId(model.getId().longValue());
		save.setReminderBusinessTable("TBL_CONTROL_ENTRIES");
		save.setReminderTime(model.getField2());
		save.setCreatedTime(now);
		save.setId(RandomUtil.uuLongId());
		save.setState(0);
		tblSystemRefReminderV1Mapper.insert(save);
	}
}
