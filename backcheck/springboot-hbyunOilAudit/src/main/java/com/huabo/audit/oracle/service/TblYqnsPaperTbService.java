package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsPaperTb;
import com.huabo.audit.oracle.vo.SjdwjdVo;

/**
* 描述: Service
* @author: t
*/
public interface TblYqnsPaperTbService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param xmqd
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsPaperTb tb,String glids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param xmdqid
	 */
	JsonBean findByid(String token, BigDecimal jdid) throws Exception;
  
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param tBlNbsjSheetVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,SjdwjdVo vo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteone(String token, BigDecimal jdid) throws Exception;
	
	 
	
}
