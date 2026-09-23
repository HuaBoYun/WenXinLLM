package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglXfExamMySql;
import com.huabo.legal.mysql.mapper.TblFwglXfExamMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglXfExamMySqlService;
import com.huabo.legal.vo.param.TblFwglXfExamQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglXfExamMySqlServiceImpl implements TblFwglXfExamMySqlService {

	@Resource
	private TblFwglXfExamMySqlMapper tblFwglXfExamMySqlMapper;

	/**
	 * 学法考试列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglXfExamMySql> getList(TblFwglXfExamQueryParam param) {
		Example example = new Example(TblFwglXfExamMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getExamName())) {
			criteria.andLike("examName", "%" + param.getExamName()+ "%");
		}
		if (StringUtils.isNotEmpty(param.getType())) {
			criteria.andEqualTo("type", param.getType());
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
		example.setOrderByClause(" EXAMID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglXfExamMySqlMapper.selectByExample(example));
	}

	/**
	 * 学法考试 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglXfExamMySql saveOrUpdate(TblFwglXfExamMySql param) {
		Date now = new Date();
		if (param.getExamId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglXfExamMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getExamId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglXfExamMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getExamId());
	}

	/**
	 * 学法考试详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglXfExamMySql findById(Integer id) {
		TblFwglXfExamMySql xfExam = tblFwglXfExamMySqlMapper.selectByPrimaryKey(id);
		if (xfExam == null) {
			throw new ServiceException(400, 50001);
		}
		return xfExam;
	}

	/**
	 * 学法考试 刪除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglXfExamMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 学法考试 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglXfExamMySqlMapper.selectCount(TblFwglXfExamMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
