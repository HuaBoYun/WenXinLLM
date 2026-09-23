package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPracticeApplyExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglPracticeApplyExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPracticeApplyExtMySqlService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPracticeApplyExtMySqlServiceImpl implements TblFwglPracticeApplyExtMySqlService {

	@Resource
	private TblFwglPracticeApplyExtMySqlMapper tblFwglPracticeApplyExtMySqlMapper;

	/**
	 * 根据执业申请-简历ID 执业申请-简历列表 查询
	 * @param practiceApplyExtId 执业申请-简历ID
	 * @return
	 */
	@Override
	public List<TblFwglPracticeApplyExtMySql> getList(String practiceApplyExtId) {
		Example example = new Example(TblFwglPracticeApplyExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (practiceApplyExtId != null) {
			criteria.andIn("practiceApplyExtId", Arrays.asList(practiceApplyExtId.split(",")));
		}
		example.setOrderByClause(" PRACTICEAPPLYEXTID desc ");
		return tblFwglPracticeApplyExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 执业申请-简历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyExtMySql saveOrUpdate(TblFwglPracticeApplyExtMySql param) {
		Date now = new Date();
		if (param.getPracticeApplyExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyExtMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeApplyExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyExtMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeApplyExtId());
	}

	/**
	 * 执业申请-简历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyExtMySql findById(Integer id) {
		TblFwglPracticeApplyExtMySql practiceApplyExt = tblFwglPracticeApplyExtMySqlMapper.selectByPrimaryKey(id);
		if (practiceApplyExt == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceApplyExt;
	}

	/**
	 * 执业申请-简历 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPracticeApplyExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 执业申请-简历 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglPracticeApplyExtMySqlMapper.selectCount(TblFwglPracticeApplyExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

