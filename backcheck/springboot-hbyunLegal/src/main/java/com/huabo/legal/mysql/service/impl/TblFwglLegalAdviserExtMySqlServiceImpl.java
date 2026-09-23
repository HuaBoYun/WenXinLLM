package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLegalAdviserExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglLegalAdviserExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLegalAdviserExtMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalAdviserExtMySqlServiceImpl implements TblFwglLegalAdviserExtMySqlService {

	@Resource
	private TblFwglLegalAdviserExtMySqlMapper tblFwglLegalAdviserExtMySqlMapper;

	/**
	 * 根据总法律顾问ID 总法律顾问-工作经历列表 查询
	 * @param adviserExtIds 总法律顾问扩展IDS
	 * @return
	 */
	@Override
	public List<TblFwglLegalAdviserExtMySql> getList(String adviserExtIds) {
		Example example = new Example(TblFwglLegalAdviserExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(adviserExtIds)) {
			criteria.andIn("adviserExtId", Arrays.asList(adviserExtIds.split(",")));
		}
		example.setOrderByClause(" ADVISEREXTID desc ");
		return tblFwglLegalAdviserExtMySqlMapper.selectByExample(example);
	}

	/**
	 * 总法律顾问-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserExtMySql saveOrUpdate(TblFwglLegalAdviserExtMySql param) {
		Date now = new Date();
		if (param.getAdviserExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserExtMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getAdviserExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserExtMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAdviserExtId());
	}

	/**
	 * 总法律顾问-工作经历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserExtMySql findById(Integer id) {
		TblFwglLegalAdviserExtMySql legalAdviserExt = tblFwglLegalAdviserExtMySqlMapper.selectByPrimaryKey(id);
		if (legalAdviserExt == null) {
			throw new ServiceException(400, 50001);
		}
		return legalAdviserExt;
	}

	/**
	 * 总法律顾问-工作经历 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLegalAdviserExtMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务人员-工作经历 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLegalAdviserExtMySqlMapper.selectCount(TblFwglLegalAdviserExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
