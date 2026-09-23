package com.huabo.legal.mysql.service.impl;

import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditMySql;
import com.huabo.legal.mysql.entity.TblFwglLawServiceLawyerMySql;
import com.huabo.legal.mysql.mapper.TblFwglLawServiceLawyerMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLawServiceLawyerMySqlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLawServiceLawyerMySqlServiceImpl implements TblFwglLawServiceLawyerMySqlService {

	@Resource
	private TblFwglLawServiceLawyerMySqlMapper tblFwglLawServiceLawyerMySqlMapper;

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-律师信息列表 查询
	 * @param lawyerId
	 * @return
	 */
	@Override
	public List<TblFwglLawServiceLawyerMySql> getList(String lawyerId) {
		Example example = new Example(TblFwglLawServiceLawyerMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(lawyerId)) {
			criteria.andIn("lawyerId", Arrays.asList(lawyerId.split(",")));
		}
		example.setOrderByClause(" LAWYERID desc ");
		return tblFwglLawServiceLawyerMySqlMapper.selectByExample(example);
	}

	/**
	 * 法律服务-律师信息 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLawServiceLawyerMySql saveOrUpdate(TblFwglLawServiceLawyerMySql param) {
		Date now = new Date();
		if (param.getLawyerId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLawServiceLawyerMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getLawyerId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLawServiceLawyerMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawyerId());
	}

	/**
	 * 法律服务-律师信息详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLawServiceLawyerMySql findById(Integer id) {
		TblFwglLawServiceLawyerMySql lawServiceLawyer = tblFwglLawServiceLawyerMySqlMapper.selectByPrimaryKey(id);
		if (lawServiceLawyer == null) {
			throw new ServiceException(400, 50001);
		}
		return lawServiceLawyer;
	}

	/**
	 * 法律服务-律师信息 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglLawServiceLawyerMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法律服务-律师信息 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLawServiceLawyerMySqlMapper.selectCount(TblFwglLawServiceLawyerMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
