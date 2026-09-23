package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLegalOrganizationMySql;
import com.huabo.legal.mysql.mapper.TblFwglLegalOrganizationMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLegalOrganizationMySqlService;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglLegalOrganizationMySqlServiceImpl implements TblFwglLegalOrganizationMySqlService {

	@Resource
	private TblFwglLegalOrganizationMySqlMapper tblFwglLegalOrganizationMySqlMapper;

	/**
	 * 法务机构及负责人列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLegalOrganizationMySql> getList(TblFwglLegalOrganizationQueryParam param) {
		Example example = new Example(TblFwglLegalOrganizationMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getResponsiblePerson())) {
			criteria.andLike("responsiblePerson", "%" + param.getResponsiblePerson() + "%");
		}
		if (StringUtils.isNotBlank(param.getContact())) {
			criteria.andLike("contact", "%" + param.getContact() + "%");
		}
		if (StringUtils.isNotBlank(param.getOrganizationName())) {
			criteria.andLike("organizationName", "%" + param.getOrganizationName() + "%");
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
		example.setOrderByClause(" ORGANIZATIONID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLegalOrganizationMySqlMapper.selectByExample(example));
	}

	/**
	 * 法务机构及负责人 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationMySql saveOrUpdate(TblFwglLegalOrganizationMySql param) {
		Date now = new Date();
		if (param.getOrganizationId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getOrganizationId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOrganizationId());
	}

	/**
	 * 法务机构及负责人详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationMySql findById(Integer id) {
		TblFwglLegalOrganizationMySql legalOrganization = tblFwglLegalOrganizationMySqlMapper.selectByPrimaryKey(id);
		if (legalOrganization == null) {
			throw new ServiceException(400, 50001);
		}
		return legalOrganization;
	}

	/**
	 * 法务机构及负责人 刪除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLegalOrganizationMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务机构及负责人 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLegalOrganizationMySqlMapper.selectCount(TblFwglLegalOrganizationMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
