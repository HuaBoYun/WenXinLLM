package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaDigitalCertificateOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaDigitalCertificateQueryParam;

public interface CeaDigitalCertificateService {

	/**
	 * 数字证书管理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaDigitalCertificateOracle> getTblCeaDigitalCertificateList(TblCeaDigitalCertificateQueryParam param);

	/**
	 * 数字证书管理 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaDigitalCertificateOracle> saveOrUpdateTblCeaDigitalCertificate(TblCeaDigitalCertificateOracle param);

	/**
	 * 数字证书管理 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaDigitalCertificate(Long id);

	/**
	 * 数字证书管理 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaDigitalCertificateOracle> getTblCeaDigitalCertificate(Long id);
}
