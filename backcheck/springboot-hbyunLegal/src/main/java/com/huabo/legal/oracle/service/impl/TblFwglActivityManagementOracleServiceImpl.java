package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglActivityManagementOracle;
import com.huabo.legal.oracle.mapper.TblFwglActivityManagementOracleMapper;
import com.huabo.legal.oracle.service.TblFwglActivityManagementOracleService;
import com.huabo.legal.vo.param.TblFwglActivityManagementQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglActivityManagementOracleServiceImpl implements TblFwglActivityManagementOracleService {

	@Resource
	private TblFwglActivityManagementOracleMapper tblFwglActivityManagementOracleMapper;

	/**
	 * 活动管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglActivityManagementOracle> getList(TblFwglActivityManagementQueryParam param) {
		Example example = new Example(TblFwglActivityManagementOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getActivityTopic())) {
			criteria.andLike("activityTopic", "%" + param.getActivityTopic() + "%");
		}
		if (StringUtils.isNotEmpty(param.getUnitName())) {
			criteria.andEqualTo("belongGroup", param.getUnitName());
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		//		if (StringUtils.isNotBlank(param.getBelongGroup())) {
		//			criteria.andCondition(" belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
		//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup());
		//		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ACTIVITYMANAGEMENTID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglActivityManagementOracleMapper.selectByExample(example));
	}

	/**
	 * 活动管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglActivityManagementOracle saveOrUpdate(TblFwglActivityManagementOracle param) {
		Date now = new Date();
		if (param.getActivityManagementId() == null) {
			param.setActivityManagementId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglActivityManagementOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getActivityManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglActivityManagementOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getActivityManagementId());
	}

	/**
	 * 活动管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglActivityManagementOracle findById(Long id) {
		TblFwglActivityManagementOracle activityManagement = tblFwglActivityManagementOracleMapper.selectByPrimaryKey(id);
		if (activityManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return activityManagement;
	}

	/**
	 * 活动管理 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglActivityManagementOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 活动管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglActivityManagementOracleMapper.selectCount(TblFwglActivityManagementOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
