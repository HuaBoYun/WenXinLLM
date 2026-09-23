package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLegalPersonnelExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglLegalPersonnelExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLegalPersonnelExtMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalPersonnelExtMySqlServiceImpl implements TblFwglLegalPersonnelExtMySqlService {

	@Resource
	private TblFwglLegalPersonnelExtMySqlMapper tblFwglLegalPersonnelExtMySqlMapper;

	/**
	 * 法务人员-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelExtMySql saveOrUpdate(TblFwglLegalPersonnelExtMySql param) {
		Date now = new Date();
		if (param.getPersonnelExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelExtMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPersonnelExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalPersonnelExtMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPersonnelExtId());
	}

	/**
	 * 法务人员-工作经历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalPersonnelExtMySql findById(Integer id) {
		TblFwglLegalPersonnelExtMySql legalPersonnelExt = tblFwglLegalPersonnelExtMySqlMapper.selectByPrimaryKey(id);
		if (legalPersonnelExt == null) {
			throw new ServiceException(400, 50001);
		}
		return legalPersonnelExt;
	}

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLegalPersonnelExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据法务人员扩展ID 法务人员-工作经历列表 查询
	 * @param personnelExtId 法务人员扩展ID
	 */
	@Override
	public List<TblFwglLegalPersonnelExtMySql> getList(String personnelExtId) {
		Example example = new Example(TblFwglLegalPersonnelExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(personnelExtId)) {
			criteria.andIn("personnelExtId", Arrays.asList(personnelExtId.split(",")));
		}
		example.setOrderByClause(" PERSONNELEXTID desc ");
		return tblFwglLegalPersonnelExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 法务人员-工作经历 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLegalPersonnelExtMySqlMapper.selectCount(TblFwglLegalPersonnelExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
