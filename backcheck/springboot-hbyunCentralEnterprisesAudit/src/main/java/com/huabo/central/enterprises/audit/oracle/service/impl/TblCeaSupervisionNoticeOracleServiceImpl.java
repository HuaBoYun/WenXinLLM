package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSupervisionNoticeOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaSupervisionNoticeOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaSupervisionNoticeOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSupervisionNoticeQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaSupervisionNoticeOracleServiceImpl implements TblCeaSupervisionNoticeOracleService {

	@Resource
	private TblCeaSupervisionNoticeOracleMapper tblCeaSupervisionNoticeOracleMapper;

	@Override
	public PageInfo<TblCeaSupervisionNoticeOracle> getList(TblCeaSupervisionNoticeQueryParam param) {
		Example example = new Example(TblCeaSupervisionNoticeOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (CollectionUtil.isNotEmpty(param.getIds())) {
			criteria.andIn("id", param.getIds());
		}
		if (StringUtils.isNotBlank(param.getNoticeNumber())) {
			criteria.andLike("noticeNumber","%" + param.getNoticeNumber() + "%");
		}
		if (StringUtils.isNotBlank(param.getNoticeName())) {
			criteria.andLike("noticeName", "%" + param.getNoticeName() + "%");
		}
		if (Objects.nonNull(param.getCreator())){
			if (StringUtils.isNotBlank(param.getDeptIds())) {
				criteria.andCondition(
						" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
								.getDeptIds() + ")))");
			} else {
				criteria.andEqualTo("creator", param.getCreator());
			}
		}
		if (Objects.nonNull(param.getState())) {
			criteria.andEqualTo("state", param.getState());
		}
		if (Objects.nonNull(param.getTransactor())) {
			criteria.andEqualTo("transactor", param.getTransactor());
		}

		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaSupervisionNoticeOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaSupervisionNoticeOracle saveOrUpdate(TblCeaSupervisionNoticeOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaSupervisionNoticeOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaSupervisionNoticeOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaSupervisionNoticeOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaSupervisionNoticeOracle findById(Long id) {
		TblCeaSupervisionNoticeOracle model = tblCeaSupervisionNoticeOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaSupervisionNoticeOracleMapper.selectCount(TblCeaSupervisionNoticeOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
