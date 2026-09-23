package com.huabo.audit.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsResearch;
import com.huabo.audit.oracle.entity.TblYqnsResearchtb;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.oracle.vo.XmdqVo;

/**
* 描述: Service
* @author: t
*/
public interface TblYqnsResearchtbService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param tb
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsResearchtb tb,String glids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param jdid
	 */
	JsonBean findByid(String token, BigDecimal jdid) throws Exception;
  
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param SjdwjdVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,SjdwjdVo vo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param jdid
	 */
	JsonBean deleteone(String token, BigDecimal jdid) throws Exception;
	
	

	/**
	 * 根据主表查询子表数据
	 * @param token
	 * @param tbid
	 */
	JsonBean findbyzb(String token, BigDecimal tbid) throws Exception;
	
	
	/**
	 * 删除子表数据
	 * @param token
	 * @param chid
	 */
	JsonBean deletezbone(String token, BigDecimal chid) throws Exception;
	
	
	
	/**
	 * 子表保存或修改
	 * @param token
	 * @param xmqd
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdatezb(String token,TblYqnsResearch ch) throws Exception ;
	
	
	/**
	 * 查询子表详情
	 * @param token
	 * @param chid
	 */
	JsonBean findByzbid(String token, BigDecimal chid) throws Exception;
	
	
	/**
	 * 理论研究台账
	 * @param token
	 * @param tbid
	 */
	JsonBean findbyztz(String token, XmdqVo ch,Integer pageNumber, Integer pageSize) throws Exception;
	
	/**
	 * 导出
	 * @param token
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<TblYqnsResearch> findexportlist(String token, XmdqVo vo) throws Exception;
	
}
