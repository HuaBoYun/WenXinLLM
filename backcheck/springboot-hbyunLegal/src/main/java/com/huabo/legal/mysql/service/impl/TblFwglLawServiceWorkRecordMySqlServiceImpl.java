package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLawServiceWorkRecordMySql;
import com.huabo.legal.mysql.mapper.TblFwglLawServiceWorkRecordMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLawServiceWorkRecordMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceWorkRecordMySqlServiceImpl implements TblFwglLawServiceWorkRecordMySqlService {

	@Resource
	private TblFwglLawServiceWorkRecordMySqlMapper tblFwglLawServiceWorkRecordMySqlMapper;

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-工作记录列表 查询
	 * @param workRecordId
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceWorkRecordMySql> getList(String workRecordId) {
		Example example = new Example(TblFwglLawServiceWorkRecordMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(workRecordId)) {
			criteria.andIn("workRecordId", Arrays.asList(workRecordId.split(",")));
		}
		example.setOrderByClause(" WORKRECORDID desc ");
		return tblFwglLawServiceWorkRecordMySqlMapper.selectByExample(example);
	}

	/**
	 * 法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkRecordMySql saveOrUpdate(TblFwglLawServiceWorkRecordMySql param) {
		Date now = new Date();
		if (param.getWorkRecordId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkRecordMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getWorkRecordId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceWorkRecordMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getWorkRecordId());
	}

	/**
	 * 法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceWorkRecordMySql findById(Integer id) {
		TblFwglLawServiceWorkRecordMySql lawServiceWorkRecord = tblFwglLawServiceWorkRecordMySqlMapper.selectByPrimaryKey(id);
		if (lawServiceWorkRecord == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceWorkRecord;
	}

	/**
	 * 法律服务-工作记录 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLawServiceWorkRecordMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法律服务-工作记录 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLawServiceWorkRecordMySqlMapper.selectCount(TblFwglLawServiceWorkRecordMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}

