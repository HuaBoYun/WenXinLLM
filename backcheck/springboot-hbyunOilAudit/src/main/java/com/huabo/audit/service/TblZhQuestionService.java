package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblZhQuestionEntity;

public interface TblZhQuestionService {
	/**
	 * 分页查询当前公司所有审计类型
	 * @param startIndex
	 * @param orgid
	 * @param pageSize
	 * @return
	 */
//	public PageBean findAll(Integer projectid,Integer startIndex,Integer pageSize);
	/**
	 * 新增，修改审计类型
	 * @param tzq
	 */
	public void save(TblZhQuestionEntity tzq);
	/**
	 * 根据id查询审计类型详情
	 * @param id
	 * @return
	 */
	public TblZhQuestionEntity findByid(String id);
	/**
	 * 删除
	 * @param tzq
	 */
	public void del(TblZhQuestionEntity tzq);
	/**
	 * 查询可使用的模板类型
	 * @param orgid
	 * @return
	 */
	public List<TblZhQuestionEntity> findAll(String orgid);
	public List<TblZhQuestionEntity> findCountByProjectId(String projectid);
	
	public TblZhQuestionEntity findByQuesid(String id);
	public TblZhQuestionEntity findById(BigDecimal id,Integer startIndex,Integer pageSize);
}
