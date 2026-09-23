package com.huabo.compliance.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.compliance.constant.YesNo;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceInspectImpOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceInspectImpOracleMapper;
import com.huabo.compliance.oracle.service.TblComplianceInspectImpOracleService;
import com.huabo.compliance.oracle.service.TblComplianceRectificationOracleService;
import com.huabo.compliance.vo.param.TblComplianceInspectImpQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TblComplianceInspectImpOracleServiceImpl implements TblComplianceInspectImpOracleService {

	@Resource
	private TblComplianceInspectImpOracleMapper tblComplianceInspectImpOracleMapper;
	@Resource
	private TblComplianceRectificationOracleService tblComplianceRectificationOracleService;

	@Override
	public PageInfo<TblComplianceInspectImpOracle> getList(TblComplianceInspectImpQueryParam param) {
		Example example = new Example(TblComplianceInspectImpOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getQuestionType())) {
			criteria.andLike("questionType", "%" + param.getQuestionType() + "%");
		}
		if (StringUtils.isNotBlank(param.getSuspectedIssue())) {
			criteria.andLike("suspectedIssue", "%" + param.getSuspectedIssue() + "%");
		}
		if (param.getIsConfirm() != null) {
			criteria.andEqualTo("isConfirm", param.getIsConfirm());
		}
		if (param.getIsRectification() != null) {
			criteria.andEqualTo("isRectification", param.getIsRectification());
		}
		if (param.getState() != null) {
			criteria.andEqualTo("state", param.getState());
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (Objects.equals(param.getAuthorityType(), 0)) {
			if (Objects.nonNull(param.getCreator())) {
				criteria.andEqualTo("creator", param.getCreator());
			}
		}
		if (Objects.equals(param.getIsRepeat(), 1)) {
			List<Integer> isRepeatAllList = tblComplianceRectificationOracleService.getIsRepeatAllList();
			if (CollectionUtil.isNotEmpty(isRepeatAllList)) {
				criteria.andNotIn("id", isRepeatAllList);
			}
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblComplianceInspectImpOracleMapper.selectByExample(example));
	}

	@Override
	public TblComplianceInspectImpOracle saveOrUpdate(TblComplianceInspectImpOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblComplianceInspectImpOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblComplianceInspectImpOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblComplianceInspectImpOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblComplianceInspectImpOracle findById(Integer id) {
		TblComplianceInspectImpOracle inspectImp = tblComplianceInspectImpOracleMapper.selectByPrimaryKey(id);
		if (inspectImp == null) {
			throw new ServiceException(400, 50001);
		}
		return inspectImp;
	}

	/**
	 * 根据检查实施IDS 查询信息 Map
	 * @param impIds
	 * @return
	 */
	@Override
	public Map<Integer, TblComplianceInspectImpOracle> getInspectImpMap(List<Integer> impIds) {
		Example example = new Example(TblComplianceInspectImpOracle.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", impIds);
		List<TblComplianceInspectImpOracle> list = tblComplianceInspectImpOracleMapper.selectByExample(example);
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyMap();
		}
		return list.stream().collect(Collectors.toMap(TblComplianceInspectImpOracle::getId, item -> item));
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblComplianceInspectImpOracleMapper.selectCount(TblComplianceInspectImpOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
