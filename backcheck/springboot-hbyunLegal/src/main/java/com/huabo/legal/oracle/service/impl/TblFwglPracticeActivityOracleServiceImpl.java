package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglPracticeActivityOracle;
import com.huabo.legal.oracle.mapper.TblFwglPracticeActivityOracleMapper;
import com.huabo.legal.oracle.service.TblFwglPracticeActivityOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglPracticeActivityOracleServiceImpl implements TblFwglPracticeActivityOracleService {

	@Resource
	private TblFwglPracticeActivityOracleMapper tblFwglPracticeActivityOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 执业活动列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPracticeActivityOracle> getList(TblFwglPracticeActivityQueryParam param) {
		Example example = new Example(TblFwglPracticeActivityOracle.class);
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
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
			criteria.andIn("belongGroup", tblOrganizationAll);
			//			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
			//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup() + ") ");
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" PRACTICEACTIVITYID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPracticeActivityOracleMapper.selectByExample(example));
	}

	/**
	 * 执业活动 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeActivityOracle saveOrUpdate(TblFwglPracticeActivityOracle param) {
		Date now = new Date();
		if (param.getPracticeActivityId() == null) {
			param.setPracticeActivityId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeActivityOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeActivityId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeActivityOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeActivityId());
	}

	/**
	 * 执业活动 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglPracticeActivityOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 执业活动详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeActivityOracle findById(Long id) {
		TblFwglPracticeActivityOracle practiceActivity = tblFwglPracticeActivityOracleMapper.selectByPrimaryKey(id);
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
	public List<TblFwglPracticeActivityOracle> getPracticeActivityList(Long staffId, Integer type) {
		List<TblFwglPracticeActivityOracle> articleList = tblFwglPracticeActivityOracleMapper.findArticleList(staffId, type);
		if (CollectionUtil.isNotEmpty(articleList)) {
			articleList.forEach(x -> x.setArticlePublishPublishingCarrier(x.getArticlePublishCarrier()));
		}
		return articleList;
	}

	/**
	 * 根据id查询 执业活动 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglPracticeActivityOracleMapper.selectCount(TblFwglPracticeActivityOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
