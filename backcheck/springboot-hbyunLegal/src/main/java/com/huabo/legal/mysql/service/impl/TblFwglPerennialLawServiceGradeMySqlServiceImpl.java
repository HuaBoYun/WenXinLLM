package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPerennialLawServiceGradeMySql;
import com.huabo.legal.mysql.mapper.TblFwglPerennialLawServiceGradeMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPerennialLawServiceGradeMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPerennialLawServiceGradeMySqlServiceImpl implements TblFwglPerennialLawServiceGradeMySqlService {

	@Resource
	private TblFwglPerennialLawServiceGradeMySqlMapper tblFwglPerennialLawServiceGradeMySqlMapper;

	/**
	 * 根据 常年法律服务-评分列表 查询
	 * @param gradeId
	 * @return
	 */
	@Override
	public List<TblFwglPerennialLawServiceGradeMySql> getList(String gradeId) {
		Example example = new Example(TblFwglPerennialLawServiceGradeMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(gradeId)) {
			criteria.andIn("gradeId", Arrays.asList(gradeId.split(",")));
		}
		example.setOrderByClause(" GRADEID desc ");
		return tblFwglPerennialLawServiceGradeMySqlMapper.selectByExample(example);
	}

	/**
	 * 常年法律服务-评分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPerennialLawServiceGradeMySql saveOrUpdate(TblFwglPerennialLawServiceGradeMySql param) {
		Date now = new Date();
		if (param.getGradeId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPerennialLawServiceGradeMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getGradeId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPerennialLawServiceGradeMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getGradeId());
	}

	/**
	 * 常年法律服务-评分详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPerennialLawServiceGradeMySql findById(Integer id) {
		TblFwglPerennialLawServiceGradeMySql perennialLawServiceGrade = tblFwglPerennialLawServiceGradeMySqlMapper.selectByPrimaryKey(id);
		if (perennialLawServiceGrade == null) {
			throw new ServiceException(400, 50001);
		}
		return perennialLawServiceGrade;
	}

	/**
	 * 常年法律服务-评分 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPerennialLawServiceGradeMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 常年法律服务-评分 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglPerennialLawServiceGradeMySqlMapper.selectCount(TblFwglPerennialLawServiceGradeMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
