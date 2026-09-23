package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglLawServiceLawyerMySql;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;

import java.util.List;

public interface TblFwglLawServiceLawyerMySqlService {

	/**
	 * 根据常年法律服务id/专项法律服务id 法律服务-律师信息列表 查询
	 * @param lawyerId 律师id
	 * @return
	 */
	List<TblFwglLawServiceLawyerMySql> getList(String lawyerId);

	/**
	 * 法律服务-律师信息 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLawServiceLawyerMySql saveOrUpdate(TblFwglLawServiceLawyerMySql param);

	/**
	 * 法律服务-律师信息详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLawServiceLawyerMySql findById(Integer id);

	/**
	 * 法律服务-律师信息 删除
	 * @param id
	 */
	void delete(Integer id);
}
