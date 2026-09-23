package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglInstitutionAuditExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglInstitutionAuditExtMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglInstitutionAuditExtMySqlServiceImpl implements TblFwglInstitutionAuditExtMySqlService {

	@Resource
	private TblFwglInstitutionAuditExtMySqlMapper tblFwglInstitutionAuditExtMySqlMapper;

	/**
	 * 制度审核-制度列表 查询
	 * @param institutionAuditExtId
	 * @return
	 */
	@Override
	public List<TblFwglInstitutionAuditExtMySql> getList(String institutionAuditExtId) {
		Example example = new Example(TblFwglInstitutionAuditExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(institutionAuditExtId)) {
			criteria.andIn("institutionAuditExtId", Arrays.asList(institutionAuditExtId.split(",")));
		}
		example.setOrderByClause(" INSTITUTIONAUDITEXTID desc ");
		return tblFwglInstitutionAuditExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 制度审核-制度新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditExtMySql saveOrUpdate(TblFwglInstitutionAuditExtMySql param) {
		Date now = new Date();
		if (param.getInstitutionAuditExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditExtMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getInstitutionAuditExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglInstitutionAuditExtMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getInstitutionAuditExtId());
	}

	/**
	 * 制度审核-制度 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglInstitutionAuditExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 制度审核-制度 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglInstitutionAuditExtMySql findById(Integer id) {
		TblFwglInstitutionAuditExtMySql institutionAuditExt = tblFwglInstitutionAuditExtMySqlMapper.selectByPrimaryKey(id);
		if (institutionAuditExt == null) {
			throw new ServiceException(400, 50001);
		}
		return institutionAuditExt;
	}


	/**
	 * 根据id查询 制度审核-制度是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglInstitutionAuditExtMySqlMapper.selectCount(TblFwglInstitutionAuditExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
