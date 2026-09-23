package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglFirmLawyerMySql;
import com.huabo.legal.mysql.mapper.TblFwglFirmLawyerMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglFirmLawyerMySqlService;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Slf4j
@Service
public class TblFwglFirmLawyerMySqlServiceImpl implements TblFwglFirmLawyerMySqlService {

	@Autowired
	private TblFwglFirmLawyerMySqlMapper tblFwglFirmLawyerMySqlMapper;

	/**
	 * 查询公司律师列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglFirmLawyerMySql> getList(TblFwglFirmLawyerQueryParam param) {

		Example example = new Example(TblFwglFirmLawyerMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getLawyerName())) {
			criteria.andLike("lawyerName", "%" + param.getLawyerName() + "%");
		}
		if (StringUtils.isNotBlank(param.getCategory())) {
			criteria.andEqualTo("category", param.getCategory());
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
		example.setOrderByClause(" LAWYERID desc");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglFirmLawyerMySqlMapper.selectByExample(example));
	}

	/**
	 * 公司律师 新增/更新
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public TblFwglFirmLawyerMySql saveOrUpdate(TblFwglFirmLawyerMySql param) {
		Date now = new Date();
		if (param.getLawyerId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getLawyerId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawyerId());
	}

	/**
	 * 公司律师 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglFirmLawyerMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 查询公司律师
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglFirmLawyerMySql findById(Integer id) {
		TblFwglFirmLawyerMySql firmLawyer = tblFwglFirmLawyerMySqlMapper.selectByPrimaryKey(id);
		if (firmLawyer == null) {
			throw new ServiceException(400, 50001);
		}
		return firmLawyer;
	}

	/**
	 * 根据id查询公司律师是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglFirmLawyerMySqlMapper.selectCount(TblFwglFirmLawyerMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}


}
