package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsHjtz;
import com.huabo.audit.oracle.vo.XmdqVo;

/**
* 描述: Service
*/
public interface TblYqnsHjtzService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param ry
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsHjtz ry,String attids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param ryid
	 */
	JsonBean findByid(String token, BigDecimal ryid) throws Exception;
 
	/**
	 * 查询附件
	 * @param token
	 * @param ryid
	 */
	JsonBean findattlistByid(String token, BigDecimal ryid) throws Exception;
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param XmdqVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,XmdqVo vo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param ryid
	 */
	JsonBean deleteone(String token, BigDecimal ryid) throws Exception;
	
	/**
	 * 删除附件
	 * @param token
	 * @param ryid
	 */
	JsonBean deleteatt(String token, String attid) throws Exception;
	
	
	/**
	 * 下发人员
	 * @param token
	 * @param ryid
	 */
	JsonBean xfry(String token,String xfryids, String xfrynames,BigDecimal ryid) throws Exception;
	
	
}
