package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.ZhAssessEntity;

public interface ZhAssessService {
	/**
	 * 分页查询当前公司所有审计类型
	 * @param startIndex
	 * @param orgid
	 * @param pageSize
	 * @return
	 */
//	public PageBean findAll(Integer startIndex,String orgid,Integer pageSize);
	/**
	 * 新增，修改审计类型
	 * @param tnt
	 */
	public void save(ZhAssessEntity tnt);
	/**
	 * 根据id查询审计类型详情
	 * @param id
	 * @return
	 */
	public ZhAssessEntity findByid(String id);
	/**
	 * 删除
	 * @param tnt
	 */
	public void del(ZhAssessEntity tnt);
	/**
	 * 查询可使用的模板类型
	 * @param orgid
	 * @return
	 */
	public List<ZhAssessEntity> findAll(String orgid);
	/**
	 * 根据formid查询数据
	 * @param formId
	 * @return
	 */
	public List<ZhAssessEntity> findByFormId(BigDecimal formId);
}
