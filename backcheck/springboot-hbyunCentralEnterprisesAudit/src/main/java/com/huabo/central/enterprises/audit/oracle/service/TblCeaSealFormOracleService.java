package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSealFormOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSealFormQueryParam;

public interface TblCeaSealFormOracleService {

	/**
	 * 印信使用单 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaSealFormOracle> getList(TblCeaSealFormQueryParam param);

	/**
	 * 印信使用单 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaSealFormOracle saveOrUpdate(TblCeaSealFormOracle param);

	/**
	 * 印信使用单 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 印信使用单 详情查询
	 * @param id
	 * @return
	 */
	TblCeaSealFormOracle findById(Long id);

	/**
	 * 印信使用单 台账-上移
	 * @param id
	 * @return
	 */
	void updateTblCeaSealFormMoveUp(Long id);

	/**
	 * 印信使用单 台账-下移
	 * @param id
	 * @return
	 */
	void updateTblCeaSealFormMoveDown(Long id);
}
