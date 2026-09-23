package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglSubjectManagementMySql;
import com.huabo.legal.mysql.mapper.TblFwglSubjectManagementMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglSubjectManagementMySqlService;
import com.huabo.legal.vo.param.TblFwglSubjectManagementQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglSubjectManagementMySqlServiceImpl implements TblFwglSubjectManagementMySqlService {

	@Resource
	private TblFwglSubjectManagementMySqlMapper tblFwglSubjectManagementMySqlMapper;

	/**
	 * 课题管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglSubjectManagementMySql> getList(TblFwglSubjectManagementQueryParam param) {
		Example example = new Example(TblFwglSubjectManagementMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getSubjectManagementName())) {
			criteria.andLike("subjectManagementName", "%" + param.getSubjectManagementName() + "%");
		}
		if (param.getType() != null) {
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
		example.setOrderByClause(" SUBJECTMANAGEMENTID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglSubjectManagementMySqlMapper.selectByExample(example));
	}

	/**
	 * 课题管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglSubjectManagementMySql saveOrUpdate(TblFwglSubjectManagementMySql param) {
		Date now = new Date();
		if (param.getSubjectManagementId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglSubjectManagementMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getSubjectManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglSubjectManagementMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getSubjectManagementId());
	}

	/**
	 * 课题管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglSubjectManagementMySql findById(Integer id) {
		TblFwglSubjectManagementMySql subjectManagement = tblFwglSubjectManagementMySqlMapper.selectByPrimaryKey(id);
		if (subjectManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return subjectManagement;
	}

	/**
	 * 课题管理 刪除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglSubjectManagementMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 课题管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglSubjectManagementMySqlMapper.selectCount(TblFwglSubjectManagementMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
