package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.Tree;
import com.huabo.audit.oracle.entity.TblTargetTypeEntity;

public interface TblTargetTypeService extends IService<TblTargetTypeEntity>{
	
	public List<TblTargetTypeEntity> getRoot(BigDecimal tempId);
	
	/**
	 * 查询是否有子集
	 * <p>Description:</p>
	 * @author SongXiangYing
	 * @date 2016年4月25日 下午3:31:57
	 * @param tempId
	 * @param targetId
	 * @return
	 */
	public int getCount(BigDecimal tempId, BigDecimal targetId);
	
	/**
	 * 根据模板id删除审计目标
	 * <p>Description:</p>
	 * @author SongXiangYing
	 * @date 2016年4月26日 下午3:22:29
	 * @param tempId
	 */
	public void deleteByTempId(BigDecimal tempId);
    
    /**
     * 获取用户下面所以分类统计
     * <p>Description:</p>
     * @author SongXiangYing
     * @date 2016年5月11日 上午11:21:32
     * @param projectId
     * @param staffId
     * @return
     */
    public List<TblTargetTypeEntity> getNodeListByUser(BigDecimal projectId,BigDecimal staffId);
    /**
    * 获取所以分类统计
    * <p>Description:</p>
    * @author SongXiangYing
    * @date 2016年5月11日 上午11:21:32
    * @param projectId
    * @param staffId
    * @return
    */
    public List<TblTargetTypeEntity> getNodeList(BigDecimal projectId);
    /**
     * 根据模板id查询所有分类，进行复制添加审计引用
     * @param tempid
     * @return
     */
    public List<TblTargetTypeEntity> findByAllMB(String tempid);
    public List<TblTargetTypeEntity> findByAllMBs(String tempid);
    public List<TblTargetTypeEntity> findByALLPatrnt(String parentid);
    /**
     * 根据模板ID删除分类
     * @param tempId
     */
    public void deleteByZY(String tempId);
    public List<Tree> getRootMb(BigDecimal tempId,String url);
    public List<Tree> getTreeMb(BigDecimal parentId,BigDecimal tempId,String url);
    
    public List<Tree> getTreeByUserMb(BigDecimal tempId,BigDecimal projectId, BigDecimal staffId,String url);
    
    
    public List<Tree> getTreeByUserMb(BigDecimal parentId,BigDecimal tempId,BigDecimal projectId, BigDecimal staffId,String url);
	public void merge(TblTargetTypeEntity targetType);
	public void delete(TblTargetTypeEntity targetType);
	
	
	/**
	 * 获取根节点
	 */
	public List<Tree> getTree(BigDecimal parentId,BigDecimal tempId,String url);
	
	public List<Tree> getTreezy(BigDecimal parentId, BigDecimal tempId, String url,String topname);
	
	//List<TblTargetTypeEntity> getRoot(Integer tempId);
	
	public List<Tree> getRoot(BigDecimal tempId,String url);
	
	public List<Tree> getTreeByUser(BigDecimal tempId,Integer projectId, BigDecimal staffId,String url);
	
	public List<Tree> getTreeByUser(Integer parentId,Integer tempId,Integer projectId, BigDecimal staffId,String url);
	
}
