package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceManualMgtOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceManualMgtOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceManualMgtOracleService;
import com.huabo.compliance.vo.param.TblComplianceManualMgtQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblComplianceManualMgtOracleServiceImpl implements TblComplianceManualMgtOracleService {

	@Resource
	private TblComplianceManualMgtOracleMapper tblComplianceManualMgtOracleMapper;

	@Override
	public PageInfo<TblComplianceManualMgtOracle> getList(TblComplianceManualMgtQueryParam param) {
		Example example = new Example(TblComplianceManualMgtOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getManualName())) {
			criteria.andLike("manualName", "%" + param.getManualName() + "%");
		}
		if (param.getCreator() != null) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblComplianceManualMgtOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceManualMgtOracle saveOrUpdate(TblComplianceManualMgtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceManualMgtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceManualMgtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceManualMgtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceManualMgtOracle findById(Integer id) {
		TblComplianceManualMgtOracle manualMgt = tblComplianceManualMgtOracleMapper.selectByPrimaryKey(id);
		if (manualMgt == null) {
			throw new ServiceException(400, 50001);
		}
		return manualMgt;
	}

	/**
	 * 根据id查询 合规管理员信息管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceManualMgtOracleMapper.selectCount(TblComplianceManualMgtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
