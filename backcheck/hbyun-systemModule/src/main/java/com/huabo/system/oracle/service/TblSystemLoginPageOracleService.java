package com.huabo.system.oracle.service;

import java.math.BigDecimal;

import com.github.pagehelper.PageInfo;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.vo.param.TblSystemLoginPageQueryParam;
import com.huabo.system.vo.param.TblSystemLoginPageStateParam;

public interface TblSystemLoginPageOracleService {

	/**
	 * 系统登录页列表 查询
	 * @return
	 */
	PageInfo<TblSystemLoginPageOracle> getList(TblSystemLoginPageQueryParam param);

	/**
	 * 系统登录页 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemLoginPageOracle saveOrUpdate(TblSystemLoginPageOracle param);

	/**
	 * 系统登录页 详情 查询
	 * @param id
	 * @return
	 */
	TblSystemLoginPageOracle findById(BigDecimal id);

	/**
	 * 系统登录页 刪除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 系统登录页-状态变更
	 * @param param
	 */
	void updateStateTblSystemLoginPage(TblSystemLoginPageStateParam param);

	/**
	 * 登录页配置-登录页信息
	 * @param belongGroup
	 */
	TblSystemLoginPageOracle getTblSystemLoginPageInfo(BigDecimal belongGroup);
}
