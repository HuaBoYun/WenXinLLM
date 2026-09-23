package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPlanManagementMySql;
import com.huabo.legal.mysql.mapper.TblFwglPlanManagementMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPlanManagementMySqlService;
import com.huabo.legal.vo.param.TblFwglPlanManagementQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglPlanManagementMySqlServiceImpl implements TblFwglPlanManagementMySqlService {

	@Resource
	private TblFwglPlanManagementMySqlMapper tblFwglPlanManagementMySqlMapper;

	/**
	 * 规划管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPlanManagementMySql> getList(TblFwglPlanManagementQueryParam param) {
		Example example = new Example(TblFwglPlanManagementMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPlanManagementName())) {
			criteria.andLike("planManagementName", "%" + param.getPlanManagementName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getType())) {
			criteria.andEqualTo("type", param.getType());
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
		example.setOrderByClause(" PLANMANAGEMENTID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPlanManagementMySqlMapper.selectByExample(example));
	}

	/**
	 * 规划管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPlanManagementMySql saveOrUpdate(TblFwglPlanManagementMySql param) {
		Date now = new Date();
		if (param.getPlanManagementId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPlanManagementMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPlanManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPlanManagementMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPlanManagementId());
	}

	/**
	 * 规划管理 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPlanManagementMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 规划管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPlanManagementMySql findById(Integer id) {
		TblFwglPlanManagementMySql planManagement = tblFwglPlanManagementMySqlMapper.selectByPrimaryKey(id);
		if (planManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return planManagement;
	}

	/**
	 * 根据id查询 规划管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglPlanManagementMySqlMapper.selectCount(TblFwglPlanManagementMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
