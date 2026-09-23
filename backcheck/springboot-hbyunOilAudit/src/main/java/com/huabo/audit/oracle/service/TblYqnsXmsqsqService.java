package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsXmsqsq;
import com.huabo.audit.oracle.vo.XmdqVo;

/**
* 描述: Service
* @author: ziyao  
* @date: 2022-04-20
*/
public interface TblYqnsXmsqsqService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param xmqd
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsXmsqsq xmqd,String attids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param xmdqid
	 */
	JsonBean findByid(String token, BigDecimal xmdqid) throws Exception;
 
	/**
	 * 查询底稿附件
	 * @param token
	 * @param xmdqid
	 */
	JsonBean findattlistByid(String token, BigDecimal xmdqid) throws Exception;
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param tBlNbsjSheetVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,XmdqVo vo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteone(String token, BigDecimal xmdqid) throws Exception;
	
	/**
	 * 删除附件
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteatt(String token, String attid) throws Exception;
	
	
}
