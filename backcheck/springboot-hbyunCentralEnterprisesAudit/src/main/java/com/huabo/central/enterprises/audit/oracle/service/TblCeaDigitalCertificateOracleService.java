package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaDigitalCertificateOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaDigitalCertificateQueryParam;

public interface TblCeaDigitalCertificateOracleService {

	/**
	 * 数字证书管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaDigitalCertificateOracle> getList(TblCeaDigitalCertificateQueryParam param);

	/**
	 * 数字证书管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaDigitalCertificateOracle saveOrUpdate(TblCeaDigitalCertificateOracle param);

	/**
	 * 数字证书管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 数字证书管理 详情查询
	 * @param id
	 * @return
	 */
	TblCeaDigitalCertificateOracle findById(Long id);
}

