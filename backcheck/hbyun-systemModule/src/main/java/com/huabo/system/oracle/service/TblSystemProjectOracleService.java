package com.huabo.system.oracle.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemProjectOracleService {

	/**
	 * 系统项目表列表 查询
	 * @return
	 */
	List<TblSystemProjectOracle> getList(TblSystemProjectQueryParam param);

	/**
	 * 系统项目表 新增/更新
	 * @param param
	 * @return
	 */
	TblSystemProjectOracle saveOrUpdate(TblSystemProjectOracle param);

	/**
	 * 系统项目表详情 查询
	 * @param id
	 * @return
	 */
	TblSystemProjectOracle findById(BigDecimal id);

	/**
	 * 系统项目表 刪除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 查询编码流程平台项目编码是否存在
	 * @param uniqueIdentification
	 * @return
	 */
	Boolean isUniqueIdentification(String uniqueIdentification);

	/**
	 * 主题仓库下发获取系统功能模块列表
	 * @param token
	 * @return
	 * @throws Exception
	 */
	com.hbfk.util.JsonBean getThemeModuleList(String token) throws Exception;

	/**
	 * 初始化授予所有公司所有模块
	 * @throws Exception
	 */
	void syncSystemModuleInit() throws Exception;

}
