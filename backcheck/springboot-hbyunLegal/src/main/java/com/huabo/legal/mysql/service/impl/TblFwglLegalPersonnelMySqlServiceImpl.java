package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLegalPersonnelMySql;
import com.huabo.legal.mysql.mapper.TblFwglLegalPersonnelMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLegalPersonnelMySqlService;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglLegalPersonnelMySqlServiceImpl implements TblFwglLegalPersonnelMySqlService {

	@Resource
	private TblFwglLegalPersonnelMySqlMapper tblFwglLegalPersonnelMySqlMapper;

	/**
	 * 法务人员列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLegalPersonnelMySql> getList(TblFwglLegalPersonnelQueryParam param) {
		Example example = new Example(TblFwglLegalPersonnelMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPersonnelName())) {
			criteria.andLike("personnelName", "%" + param.getPersonnelName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getPosition())) {
			criteria.andLike("position", "%" + param.getPosition() + "%");
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
		example.setOrderByClause("PERSONNELID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLegalPersonnelMySqlMapper.selectByExample(example));
	}

	/**
	 * 法务人员 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelMySql saveOrUpdate(TblFwglLegalPersonnelMySql param) {
		Date now = new Date();
		if (param.getPersonnelId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPersonnelId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPersonnelId());
	}

	/**
	 * 法务人员详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelMySql findById(Integer id) {
		TblFwglLegalPersonnelMySql legalPersonnel = tblFwglLegalPersonnelMySqlMapper.selectByPrimaryKey(id);
		if (legalPersonnel == null) {
			throw new ServiceException(400, 50001);
		}
		return legalPersonnel;
	}

	/**
	 * 法务人员 刪除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLegalPersonnelMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务人员 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLegalPersonnelMySqlMapper.selectCount(TblFwglLegalPersonnelMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
