package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglConferenceManagementMySql;
import com.huabo.legal.mysql.mapper.TblFwglConferenceManagementMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglConferenceManagementMySqlService;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglConferenceManagementMySqlServiceImpl implements TblFwglConferenceManagementMySqlService {

	@Resource
	private TblFwglConferenceManagementMySqlMapper tblFwglConferenceManagementMySqlMapper;

	/**
	 * 会议管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglConferenceManagementMySql> getList(TblFwglConferenceManagementQueryParam param) {
		Example example = new Example(TblFwglConferenceManagementMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getCompere())) {
			criteria.andLike("compere", "%" + param.getCompere() + "%");
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
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" CONFERENCEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglConferenceManagementMySqlMapper.selectByExample(example));
	}

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglConferenceManagementMySql saveOrUpdate(TblFwglConferenceManagementMySql param) {
		Date now = new Date();
		if (param.getConferenceId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglConferenceManagementMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getConferenceId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglConferenceManagementMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getConferenceId());
	}

	/**
	 * 会议管理 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglConferenceManagementMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 会议管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglConferenceManagementMySql findById(Integer id) {
		TblFwglConferenceManagementMySql conferenceManagement = tblFwglConferenceManagementMySqlMapper.selectByPrimaryKey(id);
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
	private Boolean idById(Integer id) {
		int count = tblFwglConferenceManagementMySqlMapper.selectCount(TblFwglConferenceManagementMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
