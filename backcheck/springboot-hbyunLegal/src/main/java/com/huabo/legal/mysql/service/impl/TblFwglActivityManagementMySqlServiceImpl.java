package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglActivityManagementMySql;
import com.huabo.legal.mysql.mapper.TblFwglActivityManagementMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglActivityManagementMySqlService;
import com.huabo.legal.vo.param.TblFwglActivityManagementQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglActivityManagementMySqlServiceImpl implements TblFwglActivityManagementMySqlService {

	@Resource
	private TblFwglActivityManagementMySqlMapper tblFwglActivityManagementMySqlMapper;

	/**
	 * 活动管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglActivityManagementMySql> getList(TblFwglActivityManagementQueryParam param) {
		Example example = new Example(TblFwglActivityManagementMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getActivityTopic())) {
			criteria.andLike("activityTopic", "%" + param.getActivityTopic() + "%");
		}
		if (StringUtils.isNotEmpty(param.getUnitName())) {
			criteria.andLike("unitName", "%" + param.getUnitName() + "%");
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ACTIVITYMANAGEMENTID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglActivityManagementMySqlMapper.selectByExample(example));
	}

	/**
	 * 活动管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglActivityManagementMySql saveOrUpdate(TblFwglActivityManagementMySql param) {
		Date now = new Date();
		if (param.getActivityManagementId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglActivityManagementMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getActivityManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglActivityManagementMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getActivityManagementId());
	}

	/**
	 * 活动管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglActivityManagementMySql findById(Integer id) {
		TblFwglActivityManagementMySql activityManagement = tblFwglActivityManagementMySqlMapper.selectByPrimaryKey(id);
		if (activityManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return activityManagement;
	}

	/**
	 * 活动管理 刪除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglActivityManagementMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 活动管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglActivityManagementMySqlMapper.selectCount(TblFwglActivityManagementMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
