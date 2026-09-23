package com.huabo.system.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemLoginPageOracleMapper;
import com.huabo.system.oracle.service.TblSystemLoginPageOracleService;
import com.huabo.system.vo.param.TblSystemLoginPageQueryParam;
import com.huabo.system.vo.param.TblSystemLoginPageStateParam;

import tk.mybatis.mapper.entity.Example;

@Service
public class TblSystemLoginPageOracleServiceImpl implements TblSystemLoginPageOracleService {

	@Resource
	private TblSystemLoginPageOracleMapper tblSystemLoginPageOracleMapper;

	/**
	 * 系统登录页列表 查询
	 * @return
	 */
	@Override
	public PageInfo<TblSystemLoginPageOracle> getList(TblSystemLoginPageQueryParam param) {
		QueryWrapper<TblSystemLoginPageOracle> wrapper = new QueryWrapper<TblSystemLoginPageOracle>();
		TblSystemLoginPageOracle model = new TblSystemLoginPageOracle();
		if (param.getState() != null && param.getState() != "") {
			wrapper.eq("state",  param.getState());
			model.setState(Integer.valueOf(param.getState()));
		}
		if (StringUtils.isNotBlank(param.getLoginName()) && param.getLoginName() != "") {
			wrapper.like("loginName", param.getLoginName());
			model.setLoginName(param.getLoginName());
		}
		if (Objects.nonNull(param.getBelongGroup())) {
			wrapper.eq("belongGroup", param.getBelongGroup());
			model.setBelongGroup(BigDecimal.valueOf(param.getBelongGroup()));
		}
		
		IPage<TblSystemLoginPageOracle> page = new Page<TblSystemLoginPageOracle>(param.getPageNumber(),param.getPageSize());
		List<TblSystemLoginPageOracle> pageList = this.tblSystemLoginPageOracleMapper.selectAll(model);
//		IPage<TblSystemLoginPageOracle> pageList = this.tblSystemLoginPageOracleMapper.selectPage(page, wrapper);
		PageInfo<TblSystemLoginPageOracle> pageInfo = new PageInfo<TblSystemLoginPageOracle>();
		pageInfo.setPageSize(param.getPageSize());
		pageInfo.setPageNum(param.getPageNumber());
		pageInfo.setTotal(pageList.size());
		pageInfo.setList(pageList);
		return pageInfo;
	}

	/**
	 * 系统登录页 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblSystemLoginPageOracle saveOrUpdate(TblSystemLoginPageOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			QueryWrapper<TblSystemLoginPageOracle> wapper = new QueryWrapper<TblSystemLoginPageOracle>();
			wapper.eq("STATE", YesNo.YES);
			int count = tblSystemLoginPageOracleMapper.selectCount(wapper).intValue();
			if (count == 0) {
				param.setState(YesNo.YES);
			}
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblSystemLoginPageOracleMapper.insert(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setUpdatedTime(now);
			tblSystemLoginPageOracleMapper.updateById(param);
		}
		return findById(param.getId());
	}

	/**
	 * 系统登录页 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemLoginPageOracle findById(BigDecimal id) {
		TblSystemLoginPageOracle systemLoginPage = tblSystemLoginPageOracleMapper.selectById(id);
		if (systemLoginPage == null) {
			throw new ServiceException(400, 50001);
		}
		return systemLoginPage;
	}

	/**
	 * 系统登录页 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		int count = tblSystemLoginPageOracleMapper.selectCount(null).intValue();
		if (count == 1) {
			throw new ServiceException(400, "系统登录页，仅剩一条不允许删除");
		}
		tblSystemLoginPageOracleMapper.deleteById(id);
	}

	/**
	 * 系统登录页-状态变更
	 * @param param
	 */
	@Override
	public void updateStateTblSystemLoginPage(TblSystemLoginPageStateParam param) {
		if (param.getState() == YesNo.YES) {
			//全禁用 再启用当前这条数据
			TblSystemLoginPageOracle model = new TblSystemLoginPageOracle();
			model.setState(YesNo.NO);
			tblSystemLoginPageOracleMapper.update(model, null);
			tblSystemLoginPageOracleMapper.updateById(TblSystemLoginPageOracle.ofState(param.getId(), param.getState()));
		}
	}

	@Override
	public TblSystemLoginPageOracle getTblSystemLoginPageInfo(BigDecimal belongGroup) {
		QueryWrapper<TblSystemLoginPageOracle> wrapper = new QueryWrapper<TblSystemLoginPageOracle>();
		wrapper.eq("BELONGGROUP", belongGroup);
		return tblSystemLoginPageOracleMapper.selectOne(wrapper);
	}

	/**
	 * 根据id查询 系统登录页 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(BigDecimal id) {
		QueryWrapper<TblSystemLoginPageOracle> wrapper = new QueryWrapper<TblSystemLoginPageOracle>();
		wrapper.eq("ID", id);
		int count = tblSystemLoginPageOracleMapper.selectCount(wrapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
}
