package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineTopicExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglAnnualExamineTopicExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglAnnualExamineTopicExtMySqlService;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglAnnualExamineTopicExtMySqlServiceImpl implements TblFwglAnnualExamineTopicExtMySqlService {

	@Resource
	private TblFwglAnnualExamineTopicExtMySqlMapper tblFwglAnnualExamineTopicExtMySqlMapper;

	/**
	 * 年度考核-考核题目列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public List<TblFwglAnnualExamineTopicExtMySql> getList(TblFwglAnnualExamineTopicExtQueryParam param) {
		Example example = new Example(TblFwglAnnualExamineTopicExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getExamineType() != null) {
			criteria.andEqualTo("examineType", param.getExamineType());
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
		example.setOrderByClause(" ANNUALEXAMINETOPICEXTID desc ");
		return tblFwglAnnualExamineTopicExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineTopicExtMySql saveOrUpdate(TblFwglAnnualExamineTopicExtMySql param) {
		Date now = new Date();
		if (param.getAnnualExamineTopicExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineTopicExtMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getAnnualExamineTopicExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglAnnualExamineTopicExtMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAnnualExamineTopicExtId());
	}

	/**
	 * 年度考核-考核题目 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglAnnualExamineTopicExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 年度考核-考核题目详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglAnnualExamineTopicExtMySql findById(Integer id) {
		TblFwglAnnualExamineTopicExtMySql annualExamineTopicExt = tblFwglAnnualExamineTopicExtMySqlMapper.selectByPrimaryKey(id);
		if (annualExamineTopicExt == null) {
			throw new ServiceException(400, 50001);
		}
		return annualExamineTopicExt;
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglAnnualExamineTopicExtMySqlMapper.selectCount(TblFwglAnnualExamineTopicExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
