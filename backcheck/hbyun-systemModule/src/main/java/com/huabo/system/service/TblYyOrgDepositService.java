package com.huabo.system.service;



import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblYyOrgDeposit;
import com.huabo.system.entity.TblYyUserQuery;
import com.hbfk.util.PageInfo;

import java.math.BigDecimal;
import java.util.Map;


public interface TblYyOrgDepositService {

	/**
	 * 分页查询 个人查询信息
	 * @param pageNumber	起始页
	 * @param pageSize		每页数量
	 * @param yuq			筛选条件
	 * @param token			用户登录令牌
	 * @param staffId		用户主键
	 * @return
	 */
    Map<String, Object> findCostPircePageInfo(Integer pageNumber, Integer pageSize, TblYyUserQuery yuq, String token, String staffId);

}
