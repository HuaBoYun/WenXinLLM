package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLegalPersonnelOracle;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalFullimePercentageResult;
import com.huabo.legal.vo.result.LegalPersonnelCountResult;

import java.util.List;

public interface TblFwglLegalPersonnelOracleService {

	/**
	 * 法务人员列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLegalPersonnelOracle> getList(TblFwglLegalPersonnelQueryParam param);

	/**
	 * 法务人员 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalPersonnelOracle saveOrUpdate(TblFwglLegalPersonnelOracle param);

	/**
	 * 法务人员详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalPersonnelOracle findById(Long id);

	/**
	 * 法务人员 刪除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 法务人员数量
	 * @param param
	 * @return
	 */
	List<LegalPersonnelCountResult> getLegalPersonnelCount(TblFwglParam param);

	/**
	 * 法务人数数量-专职法务人鱼/兼职法务人 占比
	 * @param param
	 * @return
	 */
	LegalFullimePercentageResult getLegalFullimePercentage(TblFwglParam param);

	/**
	 * 总人数
	 * @param param
	 * @return
	 */
	LegalFullimePercentageResult getCount(TblFwglParam param);
}
