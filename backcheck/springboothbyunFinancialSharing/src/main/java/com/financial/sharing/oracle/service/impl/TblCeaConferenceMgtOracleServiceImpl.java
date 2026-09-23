package com.financial.sharing.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.financial.sharing.constant.YesNo;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblCeaConferenceMgtOracle;
import com.financial.sharing.oracle.mapper.TblCeaConferenceMgtOracleMapper;
import com.financial.sharing.oracle.service.TblCeaConferenceMgtOracleService;
import com.financial.sharing.vo.param.TblCeaConferenceMgtQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaConferenceMgtOracleServiceImpl implements TblCeaConferenceMgtOracleService {

	@Resource
	private TblCeaConferenceMgtOracleMapper tblCeaConferenceMgtOracleMapper;

	@Override
	public PageInfo<TblCeaConferenceMgtOracle> getList(TblCeaConferenceMgtQueryParam param) {
		Example example = new Example(TblCeaConferenceMgtOracle.class);
		Example.Criteria criteria = example.createCriteria();
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
		if (StringUtils.isNotBlank(param.getConferenceName())) {
			criteria.andLike("conferenceName", "%" + param.getConferenceName() + "%");
		}
		if (Objects.nonNull(param.getCreator())) {
			String attendees = "'," + param.getCreator() + ",'";
			criteria.andCondition(
					" (creator = " + param.getCreator() + " or " + "recipient = " + param.getCreator() + " or " + "conferenceCompere = " + param
							.getCreator() + " or " + "(attendees like " + "concat('%',concat(" + attendees + ",'%')) and handleState = 1)" + " )");
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNum(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaConferenceMgtOracleMapper.getList(param));
	}

	@Override
	public TblCeaConferenceMgtOracle saveOrUpdate(TblCeaConferenceMgtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(0);
			//初始办理状态
			param.setHandleState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaConferenceMgtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaConferenceMgtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaConferenceMgtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaConferenceMgtOracle findById(Long id) {
		TblCeaConferenceMgtOracle model = tblCeaConferenceMgtOracleMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaConferenceMgtOracleMapper.selectCount(TblCeaConferenceMgtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
