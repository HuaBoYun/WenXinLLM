package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPracticeExamineMySql;
import com.huabo.legal.mysql.mapper.TblFwglPracticeExamineMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPracticeExamineMySqlService;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglPracticeExamineMySqlServiceImpl implements TblFwglPracticeExamineMySqlService {

	@Resource
	private TblFwglPracticeExamineMySqlMapper tblFwglPracticeExamineMySqlMapper;

	/**
	 * 执业考核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPracticeExamineMySql> getList(TblFwglPracticeExamineQueryParam param) {
		Example example = new Example(TblFwglPracticeExamineMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPracticeExamineName())) {
			criteria.andLike("practiceExamineName", "%" + param.getPracticeExamineName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getPracticeExamineBelongGroup())) {
			criteria.andEqualTo("practiceExamineBelongGroup", param.getPracticeExamineBelongGroup());
		}
		if (param.getExamineGrade() != null) {
			criteria.andEqualTo("examineGrade", param.getExamineGrade());
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
		example.setOrderByClause(" PRACTICEEXAMINEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPracticeExamineMySqlMapper.selectByExample(example));
	}

	/**
	 * 执业考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeExamineMySql saveOrUpdate(TblFwglPracticeExamineMySql param) {
		Date now = new Date();
		if (param.getPracticeExamineId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeExamineMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeExamineId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeExamineMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeExamineId());
	}

	/**
	 * 执业考核 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPracticeExamineMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 执业考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeExamineMySql findById(Integer id) {
		TblFwglPracticeExamineMySql practiceExamine = tblFwglPracticeExamineMySqlMapper.selectByPrimaryKey(id);
		if (practiceExamine == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceExamine;
	}

	/**
	 * 根据id查询 执业考核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglPracticeExamineMySqlMapper.selectCount(TblFwglPracticeExamineMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
