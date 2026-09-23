package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemDistribution;
import com.huabo.system.vo.TblSystemDistributionVo;

public interface TblSystemDistributionService {

	/**
	 * 业务单据下发通知保存接口
	 * @param token
	 * @param distribution	下发通知实体
	 * @param fromType		下发单据类型
	 * @return
	 * @throws Exception
	 */
	JsonBean saveDistribution(String token, TblSystemDistribution distribution, String formType) throws Exception;

	/**
	 * 消息办理 分页获取待处理的下发信息
	 * @param token				用户登录令牌
	 * @param distribution		筛选条件实体
	 * @param pageSize			每页数量
	 * @param pageNumber		当前页
	 * @return
	 */
	JsonBean getDistributionListPage(String token, TblSystemDistributionVo distribution, Integer pageSize,
			Integer pageNumber)  throws Exception;

	/**
	 * 消息办理 点击后 修改为已阅，并清除redis数据
	 * @param token			用户登录令牌
	 * @param distribution	保存信息
	 * @return
	 * @throws Exception
	 */
	JsonBean modifyDistributionInfo(String token, TblSystemDistribution distribution) throws Exception;
	
	
	public JsonBean saveDistributions(String token, String jsondistribution, String formType) throws Exception;

	JsonBean getDistributionType(String token, TblSystemDistributionVo distribution) throws Exception;
	
	
	/**
	 *    	业务单据下发撤回删除
	 * @param token
	 * @param distribution	下发通知实体
	 * @param fromType		下发单据类型
	 * @return
	 * @throws Exception
	 */
	JsonBean deleteDistribution(String token,  String ids) throws Exception;

	JsonBean batchModifyDistributionInfo(String token, String[] ids) throws Exception;
	
	
	/**
	 * 获取下发消息数量
	 * @param token				用户登录令牌
	 * @param distribution		筛选条件实体
	 * @return
	 */
	Integer getDistributionCount(String token)  throws Exception;
	
}
