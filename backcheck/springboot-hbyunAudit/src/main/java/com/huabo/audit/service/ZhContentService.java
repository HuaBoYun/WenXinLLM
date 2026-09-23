package com.huabo.audit.service;

import java.util.List;

import com.huabo.audit.oracle.entity.ZhContentEntity;

public interface ZhContentService {
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
	public void save(ZhContentEntity tnt);
	/**
	 * 根据id查询审计类型详情
	 * @param id
	 * @return
	 */
	public ZhContentEntity findByid(String id);
	/**
	 * 删除
	 * @param tnt
	 */
	public void del(ZhContentEntity tnt);
	/**
	 * 查询可使用的模板类型
	 * @param orgid
	 * @return
	 */
	public List<ZhContentEntity> findAll(String orgid);
	/**
	 * 根据当前用户的不同，查询不同类型的模板数据
	 * @param orgid
	 * @return
	 */
	public List<ZhContentEntity> findByType(Integer type);
}
