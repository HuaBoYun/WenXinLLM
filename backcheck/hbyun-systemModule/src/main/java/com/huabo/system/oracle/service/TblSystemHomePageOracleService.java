package com.huabo.system.oracle.service;

import java.math.BigDecimal;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemHomePageOracle;
import com.huabo.system.vo.param.TblSystemHomePageQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageStateParam;

public interface TblSystemHomePageOracleService {

	/**
	 * 系统首页配置列表 查询
	 * @return
	 */
	PageInfo<TblSystemHomePageOracle> getList(TblSystemHomePageQueryParam param);

	/**
	 * 系统首页配置 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemHomePageOracle saveOrUpdate(TblSystemHomePageOracle param);

	/**
	 * 系统首页配置 详情 查询
	 * @param id
	 * @return
	 */
	TblSystemHomePageOracle findById(BigDecimal id);

	/**
	 * 系统首页配置 刪除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 系统首页配置-状态变更
	 * @param param
	 */
	void updateStateTblSystemHomePage(TblSystemHomePageStateParam param);

	/**
	 * 首页配置-公司
	 * @param belongGroup
	 * @return
	 */
	TblSystemHomePageOracle getTblSystemHomePageAuthCompany(BigDecimal belongGroup);
}
