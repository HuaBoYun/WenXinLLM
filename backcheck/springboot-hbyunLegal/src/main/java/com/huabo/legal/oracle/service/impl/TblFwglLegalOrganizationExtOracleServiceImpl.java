package com.huabo.legal.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalOrganizationExtOracle;
import com.huabo.legal.oracle.mapper.TblFwglLegalOrganizationExtOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLegalOrganizationExtOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalOrganizationExtOracleServiceImpl implements TblFwglLegalOrganizationExtOracleService {

	@Resource
	private TblFwglLegalOrganizationExtOracleMapper tblFwglLegalOrganizationExtOracleMapper;

	/**
	 * 法务机构及负责人-年度法律审核情况 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationExtOracle saveOrUpdate(TblFwglLegalOrganizationExtOracle param) {
		Date now = new Date();
		if (param.getOrganizationExtId() == null) {
			param.setOrganizationExtId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getOrganizationExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOrganizationExtId());
	}

	/**
	 * 法务机构及负责人-年度法律审核情况详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationExtOracle findById(Long id) {
		TblFwglLegalOrganizationExtOracle legalOrganizationExt = tblFwglLegalOrganizationExtOracleMapper.selectByPrimaryKey(id);
		if (legalOrganizationExt == null) {
			throw new ServiceException(400, 50001);
		}
		return legalOrganizationExt;
	}

	/**
	 * 法务机构及负责人-年度法律审核情况 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLegalOrganizationExtOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据法务机构及负责人扩展ID 法务机构及负责人-年度法律审核情况列表 查询
	 * @param organizationExtId 法务机构及负责人扩展ID
	 */
	@Override
	public List<TblFwglLegalOrganizationExtOracle> getList(String organizationExtId) {
		Example example = new Example(TblFwglLegalOrganizationExtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(organizationExtId)) {
			criteria.andIn("organizationExtId", Arrays.asList(organizationExtId.split(",")));
		}
		example.setOrderByClause(" ORGANIZATIONEXTID desc ");
		return tblFwglLegalOrganizationExtOracleMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 法务机构及负责人-年度法律审核情况 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLegalOrganizationExtOracleMapper.selectCount(TblFwglLegalOrganizationExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
