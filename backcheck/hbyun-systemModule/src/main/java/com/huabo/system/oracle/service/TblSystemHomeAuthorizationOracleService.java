package com.huabo.system.oracle.service;

import com.huabo.system.entity.TblSystemHomeAuthorizationOracle;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationQueryParam;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemHomeAuthorizationOracleService {

	/**
	 * 系统首页授权列表 查询
	 * @return
	 */
	List<TblSystemHomeAuthorizationOracle> getList(TblSystemHomeAuthorizationQueryParam param);

	/**
	 * 系统首页授权 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemHomeAuthorizationOracle saveOrUpdate(TblSystemHomeAuthorizationOracle param);

	/**
	 * 系统首页授权 详情 查询
	 * @param id
	 * @return
	 */
	TblSystemHomeAuthorizationOracle findById(BigDecimal id);

	/**
	 * 系统首页授权 刪除
	 * @param homePageId
	 */
	void delete(BigDecimal homePageId);

	/**
	 * 删除该公司的授权记录
	 * @param item
	 */
	void deleteAuthCompany(BigDecimal item);
}
