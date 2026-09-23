package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaAssetMgtOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAssetMgtOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetMgtQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaAssetMgtOracleServiceImpl implements TblCeaAssetMgtOracleService {

	@Resource
	private TblCeaAssetMgtOracleMapper tblCeaAssetMgtOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public PageInfo<TblCeaAssetMgtOracle> getList(TblCeaAssetMgtQueryParam param) {
		Example example = new Example(TblCeaAssetMgtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (CollectionUtil.isNotEmpty(param.getIds())) {
			criteria.andIn("id", param.getIds());
		}
		if (StringUtils.isNotBlank(param.getAssetCode())) {
			criteria.andLike("assetCode", "%" + param.getAssetCode() + "%");
		}
		if (StringUtils.isNotBlank(param.getAssetName())) {
			criteria.andLike("assetName", "%" + param.getAssetName() + "%");
		}
		if (StringUtils.isNotBlank(param.getSpecificationType())) {
			criteria.andLike("specificationType", "%" + param.getSpecificationType() + "%");
		}
		if (StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" ( creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + "))) ");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}

		//		if (Objects.nonNull(param.getCreator())) {
		//			criteria.andEqualTo("creator", param.getCreator());
		//		}
		//		if (Objects.nonNull(param.getBelongGroup())){
		//			List<Integer> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(param.getBelongGroup());
		//			criteria.andIn("belongGroup", tblOrganizationAll);
		//		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaAssetMgtOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaAssetMgtOracle saveOrUpdate(TblCeaAssetMgtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaAssetMgtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaAssetMgtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaAssetMgtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaAssetMgtOracle findById(Long id) {
		TblCeaAssetMgtOracle model = tblCeaAssetMgtOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaAssetMgtOracleMapper.selectCount(TblCeaAssetMgtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
