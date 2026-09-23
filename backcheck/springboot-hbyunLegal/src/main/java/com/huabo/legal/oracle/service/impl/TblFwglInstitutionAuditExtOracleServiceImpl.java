package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglInstitutionAuditExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglInstitutionAuditExtOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglInstitutionAuditExtOracleServiceImpl implements TblFwglInstitutionAuditExtOracleService {

	@Resource
	private TblFwglInstitutionAuditExtOracleMapper tblFwglInstitutionAuditExtOracleMapper;

	/**
	 * 制度审核-制度列表 查询
	 * @param institutionAuditExtId
	 * @return
	 */
	@Override
	public List<TblFwglInstitutionAuditExtOracle> getList(String institutionAuditExtId) {
		Example example = new Example(TblFwglInstitutionAuditExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(institutionAuditExtId)) {
			criteria.andIn("institutionAuditExtId", Arrays.asList(institutionAuditExtId.split(",")));
		}
		example.setOrderByClause(" INSTITUTIONAUDITEXTID desc ");
		return tblFwglInstitutionAuditExtOracleMapper.selectByExample(example);
	}

	/**
	 * 制度审核-制度新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditExtOracle saveOrUpdate(TblFwglInstitutionAuditExtOracle param) {
		Date now = new Date();
		if (param.getInstitutionAuditExtId() == null) {
			param.setInstitutionAuditExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getInstitutionAuditExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getInstitutionAuditExtId());
	}

	/**
	 * 制度审核-制度 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglInstitutionAuditExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 制度审核-制度 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditExtOracle findById(Long id) {
		TblFwglInstitutionAuditExtOracle institutionAuditExt = tblFwglInstitutionAuditExtOracleMapper.selectByPrimaryKey(id);
		if (institutionAuditExt == null) {
			throw new ServiceException(400, 50001);
		}
		return institutionAuditExt;
	}


	/**
	 * 根据id查询 制度审核-制度是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglInstitutionAuditExtOracleMapper.selectCount(TblFwglInstitutionAuditExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
