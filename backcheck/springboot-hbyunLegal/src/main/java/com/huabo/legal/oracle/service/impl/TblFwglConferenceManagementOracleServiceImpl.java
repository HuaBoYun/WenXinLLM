package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.oracle.mapper.TblFwglConferenceManagementOracleMapper;
import com.huabo.legal.oracle.service.TblFwglConferenceManagementOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglConferenceManagementOracleServiceImpl implements TblFwglConferenceManagementOracleService {

	@Resource
	private TblFwglConferenceManagementOracleMapper tblFwglConferenceManagementOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 会议管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglConferenceManagementOracle> getList(TblFwglConferenceManagementQueryParam param) {
		Example example = new Example(TblFwglConferenceManagementOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getIsHome() != null) {
			if (StringUtils.isNotBlank(param.getCompere())) {
				if (param.getIsHome() == 1) {
					criteria.andLike("participants", "%" + param.getCompere() + "%");
				} else {
					criteria.andLike("compere", "%" + param.getCompere() + "%");
				}
			}
		}
		if (StringUtils.isNotBlank(param.getConferenceName())) {
			criteria.andLike("conferenceName", "%" + param.getConferenceName() + "%");
		}
		if (param.getConferenceBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("conferenceTime", param.getConferenceBeginDate());
		}
		if (param.getConferenceEndDate() != null) {
			criteria.andLessThan("conferenceTime", DateUtil.addDays(param.getConferenceEndDate(), 1));
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
		example.setOrderByClause(" CONFERENCEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglConferenceManagementOracleMapper.selectByExample(example));
	}

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglConferenceManagementOracle saveOrUpdate(TblFwglConferenceManagementOracle param) {
		Date now = new Date();
		if (param.getConferenceId() == null) {
			param.setConferenceId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglConferenceManagementOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getConferenceId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglConferenceManagementOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getConferenceId());
	}

	/**
	 * 会议管理 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglConferenceManagementOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 会议管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglConferenceManagementOracle findById(Long id) {
		TblFwglConferenceManagementOracle conferenceManagement = tblFwglConferenceManagementOracleMapper.selectByPrimaryKey(id);
		if (conferenceManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return conferenceManagement;
	}

	/**
	 * 根据id查询 会议管理 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglConferenceManagementOracleMapper.selectCount(TblFwglConferenceManagementOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
