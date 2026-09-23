package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceDtyOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceDtyOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceDtyOracleService;
import com.huabo.compliance.vo.param.TblComplianceDtyQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblComplianceDtyOracleServiceImpl implements TblComplianceDtyOracleService {

	@Resource
	private TblComplianceDtyOracleMapper tblComplianceDtyOracleMapper;

	@Override
	public PageInfo<TblComplianceDtyOracle> getList(TblComplianceDtyQueryParam param) {
		Example example = new Example(TblComplianceDtyOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getPostName())) {
			criteria.andLike("postName", "%" + param.getPostName() + "%");
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
				.doSelectPageInfo(() -> tblComplianceDtyOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceDtyOracle saveOrUpdate(TblComplianceDtyOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceDtyOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceDtyOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceDtyOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceDtyOracle findById(Integer id) {
		TblComplianceDtyOracle dty = tblComplianceDtyOracleMapper.selectByPrimaryKey(id);
		if (dty == null) {
			throw new ServiceException(400, 50001);
		}
		return dty;
	}

	/**
	 * 根据id查询 重点岗位合规责任 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceDtyOracleMapper.selectCount(TblComplianceDtyOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
