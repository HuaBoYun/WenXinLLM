package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclareGroup;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectDeclareGroupMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectDeclareGroupService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareGroupQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaProjectDeclareGroupServiceImpl implements TblCeaProjectDeclareGroupService {

	@Resource
	private TblCeaProjectDeclareGroupMapper tblCeaProjectDeclareGroupMapper;


	@Override
	public PageInfo<TblCeaProjectDeclareGroup> getList(TblCeaProjectDeclareGroupQueryParam param) {
		Example example = new Example(TblCeaProjectDeclareGroup.class);
		Example.Criteria criteria = example.createCriteria();
		if (Objects.nonNull(param.getState())) {
			criteria.andEqualTo("state", param.getState());
		}
		if (StringUtils.isNotBlank(param.getNum())) {
			criteria.andLike("num", "%" + param.getNum() + "%");
		}
		if (StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" (creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + "))) ");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectDeclareGroupMapper.selectByExample(example));
	}

	@Override
	public TblCeaProjectDeclareGroup saveOrUpdate(TblCeaProjectDeclareGroup param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaProjectDeclareGroupMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaProjectDeclareGroupMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaProjectDeclareGroupMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaProjectDeclareGroup findById(Long id) {
		TblCeaProjectDeclareGroup model = tblCeaProjectDeclareGroupMapper.selectByPrimaryKey(id);
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
		int count = tblCeaProjectDeclareGroupMapper.selectCount(TblCeaProjectDeclareGroup.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
