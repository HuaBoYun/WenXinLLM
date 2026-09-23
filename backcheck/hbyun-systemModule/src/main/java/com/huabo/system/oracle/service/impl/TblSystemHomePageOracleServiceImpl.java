package com.huabo.system.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemHomePageOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemHomePageOracleMapper;
import com.huabo.system.oracle.service.TblSystemHomePageOracleService;
import com.huabo.system.vo.param.TblSystemHomePageQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageStateParam;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TblSystemHomePageOracleServiceImpl implements TblSystemHomePageOracleService {

	@Resource
	private TblSystemHomePageOracleMapper tblSystemHomePageOracleMapper;

	/**
	 * 系统首页配置列表 查询
	 * @return
	 */
	@Override
	public PageInfo<TblSystemHomePageOracle> getList(TblSystemHomePageQueryParam param) {
		QueryWrapper<TblSystemHomePageOracle> wrapper = new QueryWrapper<TblSystemHomePageOracle>();
		TblSystemHomePageOracle model = new TblSystemHomePageOracle();
		if (param.getState() != null && param.getState() != "") {
			wrapper.eq("state", param.getState());
			model.setState(Integer.valueOf(param.getState()));
		}


		wrapper.orderByDesc("state").orderByDesc("id");
		IPage<TblSystemHomePageOracle> page = new Page<TblSystemHomePageOracle>(param.getPageNumber(),param.getPageSize());
//		IPage<TblSystemHomePageOracle> pageList = tblSystemHomePageOracleMapper.selectPage(page, wrapper);
		List<TblSystemHomePageOracle> list =  tblSystemHomePageOracleMapper.selectAll(model);
		PageInfo<TblSystemHomePageOracle> pageInfo = new PageInfo<TblSystemHomePageOracle>();
		pageInfo.setPageNum(param.getPageNumber());
		pageInfo.setPageSize(param.getPageSize());
		pageInfo.setTotal(list.size());
		pageInfo.setList(list);
		return pageInfo;
	}

	/**
	 * 系统首页配置 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemHomePageOracle saveOrUpdate(TblSystemHomePageOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemHomePageOracleMapper.insert(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setUpdatedTime(now);
			tblSystemHomePageOracleMapper.updateById(param);
		}
		return findById(param.getId());
	}

	/**
	 * 系统首页配置 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemHomePageOracle findById(BigDecimal id) {
		TblSystemHomePageOracle systemHomePage = tblSystemHomePageOracleMapper.selectById(id);
		if (systemHomePage == null) {
			throw new ServiceException(400, 50001);
		}
		return systemHomePage;
	}

	/**
	 * 系统首页配置 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		int count = tblSystemHomePageOracleMapper.selectCount(null).intValue();
		if (count == 1) {
			throw new ServiceException(400, "系统登录页，仅剩一条不允许删除");
		}
		tblSystemHomePageOracleMapper.deleteById(id);
	}

	/**
	 * 系统首页配置-状态变更
	 * @param param
	 */
	@Override
	public void updateStateTblSystemHomePage(TblSystemHomePageStateParam param) {
		if (param.getState() == YesNo.YES) {
			//全禁用 再启用当前这条数据
			TblSystemHomePageOracle model = new TblSystemHomePageOracle();
			model.setState(YesNo.NO);
			tblSystemHomePageOracleMapper.update(model, null);
			tblSystemHomePageOracleMapper.updateById(TblSystemHomePageOracle.ofState(param.getId(), param.getState()));
		}
	}


	/**
	 * 首页配置-公司
	 * @param belongGroup
	 * @return
	 */
	@Override
	public TblSystemHomePageOracle getTblSystemHomePageAuthCompany(BigDecimal belongGroup) {
		return tblSystemHomePageOracleMapper.findAuthCompany(belongGroup);
	}

	/**
	 * 根据id查询 系统首页配置 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(BigDecimal id) {
		QueryWrapper<TblSystemHomePageOracle> wrapper = new QueryWrapper<TblSystemHomePageOracle>();
		wrapper.eq("ID", id);
		int count = tblSystemHomePageOracleMapper.selectCount(wrapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
}
