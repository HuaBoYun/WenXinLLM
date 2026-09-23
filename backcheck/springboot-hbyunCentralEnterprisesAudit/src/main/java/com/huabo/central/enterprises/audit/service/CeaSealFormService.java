package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSealFormOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSealFormQueryParam;

public interface CeaSealFormService {

	/**
	 * 印信使用单 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaSealFormOracle> getTblCeaSealFormList(TblCeaSealFormQueryParam param);

	/**
	 * 印信使用单 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaSealFormOracle> saveOrUpdateTblCeaSealForm(TblCeaSealFormOracle param);

	/**
	 * 印信使用单 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaSealForm(Long id);

	/**
	 * 印信使用单 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaSealFormOracle> getTblCeaSealForm(Long id);

	/**
	 * 印信使用单 台账列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaSealFormOracle> getTblCeaSealFormAllList(TblCeaSealFormQueryParam param);

	/**
	 * 印信使用单 台账-上移
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> updateTblCeaSealFormMoveUp(Long id);

	/**
	 * 印信使用单 台账-下移
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> updateTblCeaSealFormMoveDown(Long id);
}
