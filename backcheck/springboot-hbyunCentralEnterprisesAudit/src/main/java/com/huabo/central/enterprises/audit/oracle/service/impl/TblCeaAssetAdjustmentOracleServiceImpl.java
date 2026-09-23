package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetAdjustmentOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaAssetAdjustmentOracleMapper;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaAssetMgtOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAssetAdjustmentOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetAdjustmentQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblCeaAssetAdjustmentOracleServiceImpl implements TblCeaAssetAdjustmentOracleService {

	@Resource
	private TblCeaAssetAdjustmentOracleMapper tblCeaAssetAdjustmentOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Resource
	private TblCeaAssetMgtOracleMapper tblCeaAssetMgtOracleMapper;

	@Override
	public PageInfo<TblCeaAssetAdjustmentOracle> getList(TblCeaAssetAdjustmentQueryParam param) {
		if (Objects.nonNull(param.getBelongGroup())){
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(param.getBelongGroup());
			param.setTblOrganizationAll(tblOrganizationAll);
		}
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaAssetAdjustmentOracleMapper.getList(param));
	}

	@Override
	public TblCeaAssetAdjustmentOracle saveOrUpdate(TblCeaAssetAdjustmentOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaAssetAdjustmentOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaAssetAdjustmentOracleMapper.updateByPrimaryKeySelective(param);
		}
		//更新资产管理的在用人员
		if(null!=param.getAssetMgtId() && null!=param.getUseStaff()) {
			TblCeaAssetMgtOracle assetMgt = new TblCeaAssetMgtOracle();
			assetMgt.setId(param.getAssetMgtId());
			assetMgt.setUsePeople(param.getUseStaff());
			tblCeaAssetMgtOracleMapper.updateByPrimaryKeySelective(assetMgt);
		}
		
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaAssetAdjustmentOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaAssetAdjustmentOracle findById(Long id) {
		TblCeaAssetAdjustmentOracle model = tblCeaAssetAdjustmentOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaAssetAdjustmentOracleMapper.selectCount(TblCeaAssetAdjustmentOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
