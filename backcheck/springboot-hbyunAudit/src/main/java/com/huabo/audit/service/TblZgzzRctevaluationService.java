package com.huabo.audit.service;

import javax.annotation.Resource;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblZgzzRctevaluation;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.mapper.TblZgzzRctevaluationMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationimplMapper;

public interface TblZgzzRctevaluationService {

	/**
	 * 整改评价保存
	 * @param token		用户登录令牌
	 * @param valua		整改评价保存信息
	 * @param attIds	附件主键数组
	 * @return
	 * @throws Exception
	 */
	JsonBean saveRectificationImpl(String token, TblZgzzRctevaluation valua, String[] attIds) throws Exception;

	/**
	 * 整改评价删除附件中间关系表
	 * @param token		用户登录令牌
	 * @param attId		附件主键
	 * @param evalId	整改评价主键
	 * @return
	 * @throws Exception
	 */
	JsonBean removeRectiValuaAttRela(String token, String attId, String evalId) throws Exception;

	/**
	 * 我的整改，获取所有登录用户整改的整改清单数据
	 * @param token		用户登录令牌
	 * @param reiss		整改方案清单中间表筛选条件
	 * @return
	 */
	JsonBean getZgzzeEvaluationDetail(TblStaffUtil loginStaff, String evalId) throws Exception;
	
}
