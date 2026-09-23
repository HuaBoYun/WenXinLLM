package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceImOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceImOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceImOracleService;
import com.huabo.compliance.vo.param.TblComplianceImQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblComplianceImOracleServiceImpl implements TblComplianceImOracleService {

	@Resource
	private TblComplianceImOracleMapper tblComplianceImOracleMapper;

	@Override
	public PageInfo<TblComplianceImOracle> getList(TblComplianceImQueryParam param) {
		Example example = new Example(TblComplianceImOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getAdministratorName())) {
			criteria.andLike("administratorName", "%" + param.getAdministratorName() + "%");
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (Objects.equals(param.getAuthorityType(), 0)) {
			if (Objects.nonNull(param.getCreator())) {
				criteria.andEqualTo("creator", param.getCreator());
			}
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblComplianceImOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceImOracle saveOrUpdate(TblComplianceImOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceImOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceImOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceImOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceImOracle findById(Integer id) {
		TblComplianceImOracle im = tblComplianceImOracleMapper.selectByPrimaryKey(id);
		if (im == null) {
			throw new ServiceException(400, 50001);
		}
		return im;
	}

	/**
	 * 根据id查询 合规管理员信息管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceImOracleMapper.selectCount(TblComplianceImOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
