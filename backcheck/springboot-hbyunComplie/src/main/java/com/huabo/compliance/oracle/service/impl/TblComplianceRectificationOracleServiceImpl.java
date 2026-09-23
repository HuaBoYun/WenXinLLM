package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceRectificationOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceRectificationOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceRectificationOracleService;
import com.huabo.compliance.vo.param.TblComplianceRectificationQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblComplianceRectificationOracleServiceImpl implements TblComplianceRectificationOracleService {

	@Resource
	private TblComplianceRectificationOracleMapper tblComplianceRectificationOracleMapper;

	@Override
	public PageInfo<TblComplianceRectificationOracle> getList(TblComplianceRectificationQueryParam param) {
		Example example = new Example(TblComplianceRectificationOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getRectificationState() != null) {
			criteria.andEqualTo("rectificationState", param.getRectificationState());
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
				.doSelectPageInfo(() -> tblComplianceRectificationOracleMapper.selectByExample(example));
	}

	/**
	 * 已存在所有的检查实施IDS
	 * @return
	 */
	@Override
	public List<Integer> getIsRepeatAllList() {
		List<Integer> list = tblComplianceRectificationOracleMapper.getIsRepeatAllList();
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		return list;
	}

	@Override
	public TblComplianceRectificationOracle saveOrUpdate(TblComplianceRectificationOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceRectificationOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceRectificationOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceRectificationOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceRectificationOracle findById(Integer id) {
		TblComplianceRectificationOracle rectification = tblComplianceRectificationOracleMapper.selectByPrimaryKey(id);
		if (rectification == null) {
			throw new ServiceException(400, 50001);
		}
		return rectification;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceRectificationOracleMapper.selectCount(TblComplianceRectificationOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
