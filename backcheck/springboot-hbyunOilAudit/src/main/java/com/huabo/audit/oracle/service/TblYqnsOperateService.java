package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsOperate;

/**
* 描述: Service
* @author: t
*/
public interface TblYqnsOperateService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param oper
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsOperate oper) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param operid
	 */
	JsonBean findByid(String token, BigDecimal operid) throws Exception;
  
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize 
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,Integer status) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param operid
	 */
	JsonBean deleteone(String token, BigDecimal operid) throws Exception;
	

	/**
	 * 操作完成
	 * @param token
	 * @param operid
	 */
	JsonBean complete(String token, BigDecimal operid) throws Exception;

	JsonBean findhzAllList(String token, Integer pageNumber, Integer pageSize,String ssmkid) throws Exception;
}
