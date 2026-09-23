package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAuthorityApplyOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaAuthorityApplyOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAuthorityApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAuthorityApplyQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaAuthorityApplyOracleServiceImpl implements TblCeaAuthorityApplyOracleService {

	@Resource
	private TblCeaAuthorityApplyOracleMapper tblCeaAuthorityApplyOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public PageInfo<TblCeaAuthorityApplyOracle> getList(TblCeaAuthorityApplyQueryParam param) {
		Example example = new Example(TblCeaAuthorityApplyOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getApplyRole())) {
			criteria.andLike("applyRole", "%" + param.getApplyRole() + "%");
		}
		if (StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + ")))");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
//		if (Objects.nonNull(param.getBelongGroup())){
//			List<Integer> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(param.getBelongGroup());
//			criteria.andIn("belongGroup", tblOrganizationAll);
//		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaAuthorityApplyOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaAuthorityApplyOracle saveOrUpdate(TblCeaAuthorityApplyOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaAuthorityApplyOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaAuthorityApplyOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaAuthorityApplyOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaAuthorityApplyOracle findById(Long id) {
		TblCeaAuthorityApplyOracle model = tblCeaAuthorityApplyOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaAuthorityApplyOracleMapper.selectCount(TblCeaAuthorityApplyOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
