package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsLetter;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblYqnsLetterService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsLetter sheet,String attids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param sheetid
	 */
	JsonBean findByid(String token, BigDecimal sheetid) throws Exception;
 
	/**
	 * 查询底稿附件
	 * @param token
	 * @param sheetid
	 */
	JsonBean findattlistByid(String token, BigDecimal sheetid) throws Exception;
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param tBlNbsjSheetVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteone(String token, BigDecimal sheetid) throws Exception;
	
	/**
	 * 删除
	 * @param token
	 * @param sheetid
	 */
	JsonBean deleteatt(String token, BigDecimal attid) throws Exception;
	
}
