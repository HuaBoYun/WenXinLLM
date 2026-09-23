package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaConferenceApplyOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaConferenceApplyOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaConferenceApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.util.MeetingRoomBookerUtil;
import com.huabo.central.enterprises.audit.vo.param.TblCeaConferenceApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ConferencePlaceTimeResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TblCeaConferenceApplyOracleServiceImpl implements TblCeaConferenceApplyOracleService {

	@Resource
	private TblCeaConferenceApplyOracleMapper tblCeaConferenceApplyOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;


	@Override
	public PageInfo<TblCeaConferenceApplyOracle> getList(TblCeaConferenceApplyQueryParam param) {
		Example example = new Example(TblCeaConferenceApplyOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getConferenceName())) {
			criteria.andLike("conferenceName", "%" + param.getConferenceName() + "%");
		}
		//首页列表展示逻辑修改为根据当前时间<会议开始时间  + 已审批 才展示
		if (Objects.equals(param.getHomeType(), 1)) {
			criteria.andEqualTo("state", 6);
			Date date = new Date();
			criteria.andGreaterThan("conferenceTimeStart", date);
		} else {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(param.getDeptIds())) {
				criteria.andCondition(
						" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
								.getDeptIds() + ")))");
			} else {
				criteria.andEqualTo("creator", param.getCreator());
			}
		}
		//		if (Objects.nonNull(param.getBelongGroup())){
		//			List<Integer> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(param.getBelongGroup());
		//			criteria.andIn("belongGroup", tblOrganizationAll);
		//		}
		if (Objects.nonNull(param.getConferenceTimeStartStart())) {
			criteria.andGreaterThanOrEqualTo("conferenceTimeStart", param.getConferenceTimeStartStart());
		}
		if (Objects.nonNull(param.getConferenceTimeStartEnd())) {
			criteria.andLessThanOrEqualTo("conferenceTimeStart", DateUtil.addDays(param.getConferenceTimeStartEnd(), 1));
		}
		if (Objects.nonNull(param.getConferenceTimeEndStart())) {
			criteria.andGreaterThanOrEqualTo("conferenceTimeEnd", param.getConferenceTimeEndStart());
		}
		if (Objects.nonNull(param.getConferenceTimeEndEnd())) {
			criteria.andLessThanOrEqualTo("conferenceTimeEnd", DateUtil.addDays(param.getConferenceTimeEndEnd(), 1));
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaConferenceApplyOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaConferenceApplyOracle saveOrUpdate(TblCeaConferenceApplyOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaConferenceApplyOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaConferenceApplyOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaConferenceApplyOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaConferenceApplyOracle findById(Long id) {
		TblCeaConferenceApplyOracle model = tblCeaConferenceApplyOracleMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 相同地址会议时间是否冲突校验般判断
	 * @param conferencePlace
	 * @param conferenceTimeStart
	 * @param conferenceTimeEnd
	 */
	@Override
	public void doConferencePlace(String conferencePlace, Date conferenceTimeStart, Date conferenceTimeEnd, Long id) {
		Weekend<TblCeaConferenceApplyOracle> weekend = Weekend.of(TblCeaConferenceApplyOracle.class);
		WeekendCriteria<TblCeaConferenceApplyOracle, Object> weekendCriteria = weekend.weekendCriteria();
		weekendCriteria.andEqualTo(TblCeaConferenceApplyOracle::getConferencePlace, conferencePlace);
		if (Objects.nonNull(id)) {
			weekendCriteria.andNotEqualTo(TblCeaConferenceApplyOracle::getId, id);
		}
		List<TblCeaConferenceApplyOracle> list = tblCeaConferenceApplyOracleMapper.selectByExample(weekend);
		if (CollectionUtil.isEmpty(list)) {
			return;
		}
		List<ConferencePlaceTimeResult> collect = list.stream().map(item -> {
			ConferencePlaceTimeResult conferencePlaceTimeResult = new ConferencePlaceTimeResult();
			BeanUtils.copyProperties(item, conferencePlaceTimeResult);
			return conferencePlaceTimeResult;
		}).collect(Collectors.toList());

		MeetingRoomBookerUtil booker = new MeetingRoomBookerUtil();
		collect.forEach(item -> booker
				.book((int) item.getConferenceTimeStart().getTime(), (int) item.getConferenceTimeEnd().getTime(), item.getConferencePlace(),
						collect));
		boolean book = booker.book((int) conferenceTimeStart.getTime(), (int) conferenceTimeEnd.getTime(), conferencePlace, collect);
		if (Boolean.FALSE.equals(book)) {
			throw new ServiceException(400, "会议冲突");
		}
	}


	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaConferenceApplyOracleMapper.selectCount(TblCeaConferenceApplyOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
