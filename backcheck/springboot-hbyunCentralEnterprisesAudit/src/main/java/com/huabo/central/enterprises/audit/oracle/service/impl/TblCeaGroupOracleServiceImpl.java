package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaGroupOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaGroupOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaGroupOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaGroupQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaGroupOracleServiceImpl implements TblCeaGroupOracleService {

	@Resource
	private TblCeaGroupOracleMapper tblCeaGroupOracleMapper;

	@Override
	public PageInfo<TblCeaGroupOracle> getList(TblCeaGroupQueryParam param) {
		Example example = new Example(TblCeaGroupOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (Objects.nonNull(param.getGroupName())) {
			criteria.andLike("groupName", "%" + param.getGroupName() + "%");
		}
		if (StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + ")))");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaGroupOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaGroupOracle saveOrUpdate(TblCeaGroupOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaGroupOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaGroupOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaGroupOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaGroupOracle findById(Long id) {
		TblCeaGroupOracle model = tblCeaGroupOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaGroupOracleMapper.selectCount(TblCeaGroupOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
