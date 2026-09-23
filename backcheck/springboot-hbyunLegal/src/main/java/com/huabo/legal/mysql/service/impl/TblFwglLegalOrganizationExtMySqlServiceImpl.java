package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLegalOrganizationExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglLegalOrganizationExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLegalOrganizationExtMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalOrganizationExtMySqlServiceImpl implements TblFwglLegalOrganizationExtMySqlService {

	@Resource
	private TblFwglLegalOrganizationExtMySqlMapper tblFwglLegalOrganizationExtMySqlMapper;

	/**
	 * 法务机构及负责人-年度法律审核情况 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationExtMySql saveOrUpdate(TblFwglLegalOrganizationExtMySql param) {
		Date now = new Date();
		if (param.getOrganizationExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationExtMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getOrganizationExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalOrganizationExtMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOrganizationExtId());
	}

	/**
	 * 法务机构及负责人-年度法律审核情况详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalOrganizationExtMySql findById(Integer id) {
		TblFwglLegalOrganizationExtMySql legalOrganizationExt = tblFwglLegalOrganizationExtMySqlMapper.selectByPrimaryKey(id);
		if (legalOrganizationExt == null) {
			throw new ServiceException(400, 50001);
		}
		return legalOrganizationExt;
	}

	/**
	 * 法务机构及负责人-年度法律审核情况 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLegalOrganizationExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据法务机构及负责人扩展ID 法务机构及负责人-年度法律审核情况列表 查询
	 * @param organizationExtId 法务机构及负责人扩展ID
	 */
	@Override
	public List<TblFwglLegalOrganizationExtMySql> getList(String organizationExtId) {
		Example example = new Example(TblFwglLegalOrganizationExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(organizationExtId)) {
			criteria.andIn("organizationExtId", Arrays.asList(organizationExtId.split(",")));
		}
		example.setOrderByClause(" ORGANIZATIONEXTID desc ");
		return tblFwglLegalOrganizationExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 法务机构及负责人-年度法律审核情况 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLegalOrganizationExtMySqlMapper.selectCount(TblFwglLegalOrganizationExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
