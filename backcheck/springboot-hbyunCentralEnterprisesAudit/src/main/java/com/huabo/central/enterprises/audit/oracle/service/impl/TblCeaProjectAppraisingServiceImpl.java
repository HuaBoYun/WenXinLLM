package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectAppraising;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectAppraisingMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectAppraisingService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectAppraisingQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ImplementationPlanResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TblCeaProjectAppraisingServiceImpl implements TblCeaProjectAppraisingService {

	@Resource
	private TblCeaProjectAppraisingMapper tblCeaProjectAppraisingMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public PageInfo<TblCeaProjectAppraising> getList(TblCeaProjectAppraisingQueryParam param) {
		Example example = new Example(TblCeaProjectAppraising.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getAppraisingName())) {
			criteria.andLike("appraisingName", "%" + param.getAppraisingName() + "%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" ( creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + "))) ");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectAppraisingMapper.selectByExample(example));
	}

	@Override
	public TblCeaProjectAppraising saveOrUpdate(TblCeaProjectAppraising param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaProjectAppraisingMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaProjectAppraisingMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaProjectAppraisingMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaProjectAppraising findById(Long id) {
		TblCeaProjectAppraising model = tblCeaProjectAppraisingMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 根据项目管理实施方案ID查询信息
	 * @param implementationPlanId
	 * @return
	 */
	@Override
	public List<ImplementationPlanResult> getImplementationPlanList(String implementationPlanId) {
		if (StringUtils.isBlank(implementationPlanId)) {
			return Collections.emptyList();
		}
		List<String> strings = Arrays.asList(implementationPlanId.split(","));
		List<ImplementationPlanResult> list = tblCeaProjectAppraisingMapper.findImplementationPlanList(strings);
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		list.forEach(item -> {
			if (Objects.nonNull(item.getImplementationProjectOrderId())) {
				item.setImplementationProjectOrderName(tblStaffOracleService.getCreatorUserInfo(item.getImplementationProjectOrderId()));
			}
		});
		return list;
	}

	/**
	 * 已被选择了的申报IDS
	 * @return
	 */
	@Override
	public List<Long> getNoQuality() {
		List<TblCeaProjectAppraising> list = tblCeaProjectAppraisingMapper.selectAll();
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<Long> addAll = new ArrayList<>();
		list.forEach(item -> {
			if (StringUtils.isNotBlank(item.getImplementationPlanId())) {
				List<Long> ids = Arrays.stream(item.getImplementationPlanId().split(",")).map(Long::valueOf).collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(ids)) {
					addAll.addAll(ids);
				}
			}
		});
		if (CollectionUtil.isEmpty(addAll)) {
			return Collections.emptyList();
		}
		return addAll;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaProjectAppraisingMapper.selectCount(TblCeaProjectAppraising.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}

}
