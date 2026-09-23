package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglFirmLawyerExtMySql;
import com.huabo.legal.mysql.mapper.TblFwglFirmLawyerExtMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglFirmLawyerExtMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglFirmLawyerExtMySqlServiceImpl implements TblFwglFirmLawyerExtMySqlService {

	@Resource
	private TblFwglFirmLawyerExtMySqlMapper tblFwglFirmLawyerExtMysqlMapper;

	/**
	 * 根据公司律师扩展ID 查询 公司律师扩展详情列表
	 * @param lawyerExtId 公司律师扩展ID
	 * @return
	 */
	@Override
	public List<TblFwglFirmLawyerExtMySql> getList(String lawyerExtId) {
		Example example = new Example(TblFwglFirmLawyerExtMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(lawyerExtId)) {
			criteria.andIn("lawyerExtId", Arrays.asList(lawyerExtId.split(",")));
		}
		example.setOrderByClause(" LAWYEREXTID desc ");
		return tblFwglFirmLawyerExtMysqlMapper.selectByExample(example);
	}

	/**
	 * 查询 公司律师扩展详情
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglFirmLawyerExtMySql findById(Integer id) {
		TblFwglFirmLawyerExtMySql tblFwglFirmLawyerExt = tblFwglFirmLawyerExtMysqlMapper.selectByPrimaryKey(id);
		if (tblFwglFirmLawyerExt == null) {
			throw new ServiceException(400, 50001);
		}
		return tblFwglFirmLawyerExt;
	}

	/**
	 * 公司律师-简历 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglFirmLawyerExtMysqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 公司律师-简历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglFirmLawyerExtMySql saveOrUpdate(TblFwglFirmLawyerExtMySql param) {
		Date now = new Date();
		if (param.getLawyerExtId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerExtMysqlMapper.insertSelective(param);
		} else {
			if (idById(param.getLawyerExtId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerExtMysqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawyerExtId());
	}

	/**
	 * 根据id查询公司律师-简历是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglFirmLawyerExtMysqlMapper.selectCount(TblFwglFirmLawyerExtMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}

}
