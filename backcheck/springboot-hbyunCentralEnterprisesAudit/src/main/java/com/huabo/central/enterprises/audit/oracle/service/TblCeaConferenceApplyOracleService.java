package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaConferenceApplyOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaConferenceApplyQueryParam;

import java.util.Date;

public interface TblCeaConferenceApplyOracleService {

	/**
	 * 会议申请 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaConferenceApplyOracle> getList(TblCeaConferenceApplyQueryParam param);

	/**
	 * 会议申请 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaConferenceApplyOracle saveOrUpdate(TblCeaConferenceApplyOracle param);

	/**
	 * 会议申请 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 会议申请 详情查询
	 * @param id
	 * @return
	 */
	TblCeaConferenceApplyOracle findById(Long id);

	/**
	 * 相同地址会议时间是否冲突校验般判断
	 * @param conferencePlace
	 * @param conferenceTimeStart
	 * @param conferenceTimeEnd
	 */
	void doConferencePlace(String conferencePlace, Date conferenceTimeStart, Date conferenceTimeEnd, Long id);
}
