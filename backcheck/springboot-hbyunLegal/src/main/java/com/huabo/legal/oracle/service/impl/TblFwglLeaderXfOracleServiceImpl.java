package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLeaderXfOracle;
import com.huabo.legal.oracle.mapper.TblFwglLeaderXfOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLeaderXfOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglLeaderXfQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLeaderXfOracleServiceImpl implements TblFwglLeaderXfOracleService {

	@Resource
	private TblFwglLeaderXfOracleMapper tblFwglLeaderXfOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 领导学法列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLeaderXfOracle> getList(TblFwglLeaderXfQueryParam param) {
		List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLeaderXfOracleMapper.getList(param, tblOrganizationAll));
	}

	/**
	 * 领导学法 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLeaderXfOracle saveOrUpdate(TblFwglLeaderXfOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增为0状态为未审批
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLeaderXfOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLeaderXfOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	/**
	 * 领导学法详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLeaderXfOracle findById(Long id) {
		TblFwglLeaderXfOracle tblFwglLeaderXf = tblFwglLeaderXfOracleMapper.selectByPrimaryKey(id);
		if (tblFwglLeaderXf == null) {
			throw new ServiceException(400, 50001);
		}
		return tblFwglLeaderXf;
	}

	/**
	 * 领导学法 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLeaderXfOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务机构及负责人 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLeaderXfOracleMapper.selectCount(TblFwglLeaderXfOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
