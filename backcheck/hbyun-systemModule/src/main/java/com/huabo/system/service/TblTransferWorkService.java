package com.huabo.system.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblTransferWork;

public interface TblTransferWorkService {

	/**
	 * 获取分页数据
	 */
	JsonBean getPageList(String token, Integer pageNumber, Integer pageSize, TblTransferWork work) throws Exception;

	/**
	 * 修改或者保存交接数据
	 */
	JsonBean mengerEntity(String token, TblTransferWork work) throws Exception;

	/**
	 * 修改移交状态
	 */
	JsonBean enableStatus(String token, BigDecimal transferid, Integer transtatus) throws Exception;

	/**
	 * 获取详情
	 */
	JsonBean getDetail(String token, BigDecimal transferid) throws Exception;

	
}
