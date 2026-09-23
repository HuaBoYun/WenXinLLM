package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblNbsjWorkreport;

public interface TblAduitProGramService {
	
	/**
	 * 根据节点id删除审计程序
	 * <p>Description:</p>
	 * @author SongXiangYing
	 * @date 2016年4月25日 下午5:25:40
	 * @param targetId
	 */
	public void deleteByTargetId(Integer targetId);
	
	/**
	 * 根据模板id删除审计程序
	 * <p>Description:</p>
	 * @author SongXiangYing
	 * @date 2016年4月25日 下午5:26:24
	 * @param tempId
	 */
	public void deleteByTempId(Integer tempId);
	
	/**
	 * 根据分类id查询所有模板
	 * @param tempid
	 * @return
	 */
	public List<TblAduitProGramEntity> findByALL(String tempid);
	
	
	public void deleteZy(String tempid);
	
	
	
	JsonBean defZyPageList(String token, Integer pageNumber, Integer pageSize, BigDecimal tempId, BigDecimal targetId, BigDecimal projectId) throws Exception;
    
    JsonBean findDefZyDetail(String token, BigDecimal programid) throws Exception;
    
    JsonBean defZyLeftTreeList(String token) throws Exception;
    
    JsonBean defCatAdd(TblAduitProGramEntity apg, String token)throws Exception;
    
    JsonBean defCatDel(BigDecimal programId, String token) throws Exception;
    
    
}
