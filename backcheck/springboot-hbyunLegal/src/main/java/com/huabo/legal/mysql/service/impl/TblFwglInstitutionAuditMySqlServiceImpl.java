package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditMySql;
import com.huabo.legal.mysql.mapper.TblFwglInstitutionAuditMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglInstitutionAuditMySqlService;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglInstitutionAuditMySqlServiceImpl implements TblFwglInstitutionAuditMySqlService {

	@Resource
	private TblFwglInstitutionAuditMySqlMapper tblFwglInstitutionAuditMySqlMapper;

	/**
	 * 制度审核/经营事项审核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglInstitutionAuditMySql> getList(TblFwglInstitutionAuditQueryParam param) {
		Example example = new Example(TblFwglInstitutionAuditMySql.class);
		Example.Criteria criteria = example.createCriteria();
//		if (param.getType() != null) {
//			criteria.andEqualTo("type", param.getType());
//		}
		if (StringUtils.isNotEmpty(param.getInstitutionName())) {
			criteria.andLike("institutionName","%" +  param.getInstitutionName() + "%");
		}
		if (param.getAuditType() != null) {
			criteria.andEqualTo("auditType", param.getAuditType());
		}
		if (param.getState() != null) {
			criteria.andEqualTo("state", param.getState());
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
		example.setOrderByClause(" INSTITUTIONAUDITID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglInstitutionAuditMySqlMapper.selectByExample(example));
	}

	/**
	 * 制度审核/经营事项审核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditMySql saveOrUpdate(TblFwglInstitutionAuditMySql param) {
		Date now = new Date();
		if (param.getInstitutionAuditId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getInstitutionAuditId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getInstitutionAuditId());
	}

	/**
	 * 制度审核/经营事项审核 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglInstitutionAuditMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 制度审核/经营事项审核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditMySql findById(Integer id) {
		TblFwglInstitutionAuditMySql institutionAudit = tblFwglInstitutionAuditMySqlMapper.selectByPrimaryKey(id);
		if (institutionAudit == null) {
			throw new ServiceException(400, 50001);
		}
		return institutionAudit;
	}

	/**
	 * 根据id查询 制度审核/经营事项审核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglInstitutionAuditMySqlMapper.selectCount(TblFwglInstitutionAuditMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
