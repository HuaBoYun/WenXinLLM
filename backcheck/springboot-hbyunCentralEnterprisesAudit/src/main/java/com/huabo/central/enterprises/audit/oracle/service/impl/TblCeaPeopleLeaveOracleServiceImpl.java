package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaPeopleLeaveOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaPeopleLeaveOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaPeopleLeaveQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaPeopleLeaveOracleServiceImpl implements TblCeaPeopleLeaveOracleService {

	@Resource
	private TblCeaPeopleLeaveOracleMapper tblCeaPeopleLeaveOracleMapper;

	@Override
	public PageInfo<TblCeaPeopleLeaveOracle> getList(TblCeaPeopleLeaveQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaPeopleLeaveOracleMapper.getList(param));
	}

	@Override
	public TblCeaPeopleLeaveOracle saveOrUpdate(TblCeaPeopleLeaveOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaPeopleLeaveOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaPeopleLeaveOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaPeopleLeaveOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaPeopleLeaveOracle findById(Long id) {
		TblCeaPeopleLeaveOracle ipManage = tblCeaPeopleLeaveOracleMapper.selectByPrimaryKey(id);
		if (ipManage == null) {
			throw new ServiceException(400, 50001);
		}
		return ipManage;
	}

	/**
	 * 人员请假台账
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveAllList(TblCeaPeopleLeaveQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaPeopleLeaveOracleMapper.getTblCeaPeopleLeaveAllList(param));
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaPeopleLeaveOracleMapper.selectCount(TblCeaPeopleLeaveOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

