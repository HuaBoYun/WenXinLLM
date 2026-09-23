package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaExpatriateApplyOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExpatriateApplyOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExpatriateApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaExpatriateApplyOracleServiceImpl implements TblCeaExpatriateApplyOracleService {

	@Resource
	private TblCeaExpatriateApplyOracleMapper tblCeaExpatriateApplyOracleMapper;

	@Override
	public PageInfo<TblCeaExpatriateApplyOracle> getList(TblCeaExpatriateApplyQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaExpatriateApplyOracleMapper.getList(param));
	}

	@Override
	public TblCeaExpatriateApplyOracle saveOrUpdate(TblCeaExpatriateApplyOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaExpatriateApplyOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaExpatriateApplyOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaExpatriateApplyOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaExpatriateApplyOracle findById(Long id) {
		TblCeaExpatriateApplyOracle model = tblCeaExpatriateApplyOracleMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 外派人员台账 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyAllList(UserAllQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaExpatriateApplyOracleMapper.findTblCeaExpatriateApplyAllList(param));
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaExpatriateApplyOracleMapper.selectCount(TblCeaExpatriateApplyOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
