package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaDigitalCertificateOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaDigitalCertificateOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaDigitalCertificateOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaDigitalCertificateQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaDigitalCertificateOracleServiceImpl implements TblCeaDigitalCertificateOracleService {

	@Resource
	private TblCeaDigitalCertificateOracleMapper tblCeaDigitalCertificateOracleMapper;

	@Override
	public PageInfo<TblCeaDigitalCertificateOracle> getList(TblCeaDigitalCertificateQueryParam param) {
		Example example = new Example(TblCeaDigitalCertificateOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getServiceType())) {
			criteria.andEqualTo("serviceType", param.getServiceType());
		}
		if (StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + ")))");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getUserName())) {
			criteria.andLike("userName", "%" + param.getUserName() + "%");
		}
		if (StringUtils.isNotBlank(param.getStaffCode())) {
			criteria.andLike("staffCode", "%" + param.getStaffCode() + "%");
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaDigitalCertificateOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaDigitalCertificateOracle saveOrUpdate(TblCeaDigitalCertificateOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaDigitalCertificateOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaDigitalCertificateOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaDigitalCertificateOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaDigitalCertificateOracle findById(Long id) {
		TblCeaDigitalCertificateOracle model = tblCeaDigitalCertificateOracleMapper.selectByPrimaryKey(id);
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
		int count = tblCeaDigitalCertificateOracleMapper.selectCount(TblCeaDigitalCertificateOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

