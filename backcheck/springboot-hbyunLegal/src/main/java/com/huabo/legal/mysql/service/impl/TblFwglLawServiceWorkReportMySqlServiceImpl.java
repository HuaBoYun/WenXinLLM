package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLawServiceWorkReportMySql;
import com.huabo.legal.mysql.mapper.TblFwglLawServiceWorkReportMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLawServiceWorkReportMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceWorkReportMySqlServiceImpl implements TblFwglLawServiceWorkReportMySqlService {

	@Resource
	private TblFwglLawServiceWorkReportMySqlMapper tblFwglLawServiceWorkReportMySqlMapper;

	/**
	 * 根据常年法律服务id 法律服务-工作记录列表 查询
	 * @param workReportId
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceWorkReportMySql> getList(String workReportId) {
		Example example = new Example(TblFwglLawServiceWorkReportMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(workReportId)) {
			criteria.andIn("workReportId", Arrays.asList(workReportId.split(",")));
		}
		example.setOrderByClause(" WORKREPORTID desc ");
		return tblFwglLawServiceWorkReportMySqlMapper.selectByExample(example);
	}

	/**
	 * 法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkReportMySql saveOrUpdate(TblFwglLawServiceWorkReportMySql param) {
		Date now = new Date();
		if (param.getWorkReportId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkReportMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getWorkReportId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkReportMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getWorkReportId());
	}

	/**
	 * 法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkReportMySql findById(Integer id) {
		TblFwglLawServiceWorkReportMySql lawServiceWorkReport = tblFwglLawServiceWorkReportMySqlMapper.selectByPrimaryKey(id);
		if (lawServiceWorkReport == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceWorkReport;
	}

	/**
	 * 法律服务-工作记录 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLawServiceWorkReportMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法律服务-工作记录 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLawServiceWorkReportMySqlMapper.selectCount(TblFwglLawServiceWorkReportMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
