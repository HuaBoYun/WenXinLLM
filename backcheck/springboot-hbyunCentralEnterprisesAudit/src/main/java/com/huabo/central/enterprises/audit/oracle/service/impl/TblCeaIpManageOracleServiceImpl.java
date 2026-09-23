package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpManageOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaIpManageOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaIpManageOracleService;
import com.huabo.central.enterprises.audit.util.SnowflakeIdWorker;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpManageQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaIpManageOracleServiceImpl implements TblCeaIpManageOracleService {

	@Resource
	private TblCeaIpManageOracleMapper tblCeaIpManageOracleMapper;

	@Override
	public PageInfo<TblCeaIpManageOracle> getList(TblCeaIpManageQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaIpManageOracleMapper.getList(param));
	}

	@Override
	public TblCeaIpManageOracle saveOrUpdate(TblCeaIpManageOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaIpManageOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaIpManageOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaIpManageOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaIpManageOracle findById(Long id) {
		TblCeaIpManageOracle ipManage = tblCeaIpManageOracleMapper.selectByPrimaryKey(id);
		if (ipManage == null) {
			throw new ServiceException(400, 50001);
		}
		return ipManage;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaIpManageOracleMapper.selectCount(TblCeaIpManageOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
