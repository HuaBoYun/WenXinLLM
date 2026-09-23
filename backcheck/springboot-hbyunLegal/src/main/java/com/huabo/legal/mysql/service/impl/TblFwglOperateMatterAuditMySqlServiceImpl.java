package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditMySql;
import com.huabo.legal.mysql.entity.TblFwglOperateMatterAuditMySql;
import com.huabo.legal.mysql.mapper.TblFwglOperateMatterAuditMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglOperateMatterAuditMySqlService;
import com.huabo.legal.vo.param.TblFwglOperateMatterAuditQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglOperateMatterAuditMySqlServiceImpl implements TblFwglOperateMatterAuditMySqlService {

	@Resource
	private TblFwglOperateMatterAuditMySqlMapper tblFwglOperateMatterAuditMySqlMapper;

	/**
	 * 经营事项审列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglOperateMatterAuditMySql> getList(TblFwglOperateMatterAuditQueryParam param) {
		Example example = new Example(TblFwglInstitutionAuditMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getOperateMatterName())) {
			criteria.andLike("operateMatterName", "%" + param.getOperateMatterName() + "%");
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
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglOperateMatterAuditMySqlMapper.selectByExample(example));
	}

	/**
	 * 经营事项审 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglOperateMatterAuditMySql saveOrUpdate(TblFwglOperateMatterAuditMySql param) {
		Date now = new Date();
		if (param.getOperateMatterAuditId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglOperateMatterAuditMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getOperateMatterAuditId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglOperateMatterAuditMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOperateMatterAuditId());
	}

	/**
	 * 经营事项审 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglOperateMatterAuditMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 经营事项审详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglOperateMatterAuditMySql findById(Integer id) {
		TblFwglOperateMatterAuditMySql operateMatterAudit = tblFwglOperateMatterAuditMySqlMapper.selectByPrimaryKey(id);
		if (operateMatterAudit == null) {
			throw new ServiceException(400, 50001);
		}
		return operateMatterAudit;
	}

	/**
	 * 根据id查询 经营事项审 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglOperateMatterAuditMySqlMapper.selectCount(TblFwglOperateMatterAuditMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
