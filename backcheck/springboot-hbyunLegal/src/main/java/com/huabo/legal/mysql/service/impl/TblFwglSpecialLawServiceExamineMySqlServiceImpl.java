package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglSpecialLawServiceExamineMySql;
import com.huabo.legal.mysql.mapper.TblFwglSpecialLawServiceExamineMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglSpecialLawServiceExamineMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglSpecialLawServiceExamineMySqlServiceImpl implements TblFwglSpecialLawServiceExamineMySqlService {

	@Resource
	private TblFwglSpecialLawServiceExamineMySqlMapper tblFwglSpecialLawServiceExamineMySqlMapper;

	/**
	 * 根据 专项法律服务-考核列表 查询
	 * @param examineId
	 * @return
	 */
	@Override
	public List<TblFwglSpecialLawServiceExamineMySql> getList(String examineId) {
		Example example = new Example(TblFwglSpecialLawServiceExamineMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(examineId)) {
			criteria.andEqualTo("examineId", Arrays.asList(examineId.split(",")));
		}
		example.setOrderByClause(" EXAMINEID desc ");
		return tblFwglSpecialLawServiceExamineMySqlMapper.selectByExample(example);
	}

	/**
	 * 专项法律服务-考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglSpecialLawServiceExamineMySql saveOrUpdate(TblFwglSpecialLawServiceExamineMySql param) {
		Date now = new Date();
		if (param.getExamineId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglSpecialLawServiceExamineMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getExamineId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglSpecialLawServiceExamineMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getExamineId());
	}

	/**
	 * 专项法律服务-考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglSpecialLawServiceExamineMySql findById(Integer id) {
		TblFwglSpecialLawServiceExamineMySql specialLawServiceExamine = tblFwglSpecialLawServiceExamineMySqlMapper.selectByPrimaryKey(id);
		if (specialLawServiceExamine == null) {
			throw new ServiceException(400, 50001);
		}
		return specialLawServiceExamine;
	}

	/**
	 * 专项法律服务-考核 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglSpecialLawServiceExamineMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 专项法律服务-考核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglSpecialLawServiceExamineMySqlMapper.selectCount(TblFwglSpecialLawServiceExamineMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
