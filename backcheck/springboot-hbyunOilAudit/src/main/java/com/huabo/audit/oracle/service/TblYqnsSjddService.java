package com.huabo.audit.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsSjdd;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblYqnsSjddService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param xmqd
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,List<TblYqnsSjdd> sjdd) throws Exception ;
	
	
	/**
	 * 删除
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteone(String token, BigDecimal rwid) throws Exception;
	
}
