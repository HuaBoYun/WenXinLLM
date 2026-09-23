package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPracticeActivityMySql;
import com.huabo.legal.mysql.mapper.TblFwglPracticeActivityMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPracticeActivityMySqlService;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPracticeActivityMySqlServiceImpl implements TblFwglPracticeActivityMySqlService {

	@Resource
	private TblFwglPracticeActivityMySqlMapper tblFwglPracticeActivityMySqlMapper;

	/**
	 * 执业活动列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPracticeActivityMySql> getList(TblFwglPracticeActivityQueryParam param) {
		Example example = new Example(TblFwglPracticeActivityMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getActivityName())) {
			criteria.andLike("activityName", "%" + param.getActivityName() + "%");
		}
		if (param.getActivityCategory() != null) {
			criteria.andEqualTo("activityCategory", param.getActivityCategory());
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
		example.setOrderByClause(" PRACTICEACTIVITYID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPracticeActivityMySqlMapper.selectByExample(example));
	}

	/**
	 * 执业活动 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeActivityMySql saveOrUpdate(TblFwglPracticeActivityMySql param) {
		Date now = new Date();
		if (param.getPracticeActivityId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeActivityMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeActivityId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeActivityMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeActivityId());
	}

	/**
	 * 执业活动 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPracticeActivityMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 执业活动详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeActivityMySql findById(Integer id) {
		TblFwglPracticeActivityMySql practiceActivity = tblFwglPracticeActivityMySqlMapper.selectByPrimaryKey(id);
		if (practiceActivity == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceActivity;
	}

	/**
	 * 根据人员ID,类型 查询执业活动列表
	 * @param staffId
	 * @param type
	 */
	@Override
	public List<TblFwglPracticeActivityMySql> getPracticeActivityList(Integer staffId, Integer type) {
		return tblFwglPracticeActivityMySqlMapper.findArticleList(staffId, type);
	}

	/**
	 * 根据id查询 执业活动 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglPracticeActivityMySqlMapper.selectCount(TblFwglPracticeActivityMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
