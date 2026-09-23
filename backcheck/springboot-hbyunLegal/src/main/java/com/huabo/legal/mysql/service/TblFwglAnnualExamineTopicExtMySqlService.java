package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineTopicExtMySql;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtQueryParam;

import java.util.List;

public interface TblFwglAnnualExamineTopicExtMySqlService {

	/**
	 * 年度考核-考核题目列表 查询
	 * @param param
	 * @return
	 */
	List<TblFwglAnnualExamineTopicExtMySql> getList(TblFwglAnnualExamineTopicExtQueryParam param);

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglAnnualExamineTopicExtMySql saveOrUpdate(TblFwglAnnualExamineTopicExtMySql param);

	/**
	 * 年度考核-考核题目 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 年度考核-考核题目详情 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualExamineTopicExtMySql findById(Integer id);
}
