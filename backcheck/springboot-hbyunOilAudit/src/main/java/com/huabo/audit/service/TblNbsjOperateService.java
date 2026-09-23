package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.oracle.vo.TblNbsjOperateVo;

public interface TblNbsjOperateService extends IService<TblNbsjOperateEntity>{
	/**
	 * 根据用户获取节点下的任务列表
	 * <p>Description:</p>
	 * @author SongXiangYing
	 * @date 2016年5月11日 上午11:03:59
	 * @param targetId
	 * @param projectId
	 * @param staffId
	 * @param pageNumber
	 * @param pageSize
	 * @param bsunitname 
	 * @return
	 */
//	public PageBean findPorgramByUser(Integer targetId,Integer projectId,Integer staffId,Integer pageNumber,int pageSize, String bsunitname);
	/**
	 * 获取节点下的任务列表
	 * <p>Title:</p>
	 * <p>Description:</p>
	 * @author SongXiangYing
	 * @date 2016年5月25日 下午4:48:50
	 */
//	public PageBean findPorgram(Integer targetId,Integer projectId,Integer pageNumber,int pageSize);
	
	public void finsh(String id);
	
	
	public int getCount(Integer projectId);
	
	public TblNbsjOperateEntity findByAuthId(Integer authId);
	/**
	 * 查询项目的完成情况
	 * @param projectid
	 * @param finishStatus 任务完成状态  1完成  0未完成
	 * @return
	 */
	public List<TblNbsjOperateEntity> findByProjectId(Integer projectid,Integer finishStatus);
	public void delete(TblNbsjOperateEntity oper);
	
	
	//==
	JsonBean checkListMyPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjOperateVo tblNbsjOperateVo) throws Exception;
    
    JsonBean checkListMyDetail(String token, Integer operateid) throws Exception;
    
    JsonBean checkListFinal(Integer operateid, Integer programId, String token) throws Exception;
	
    //==
    JsonBean checkAllListMyPageList(String token, Integer pageNumber, Integer pageSize,String businessType,Integer targetId) throws Exception;
	
    JsonBean pjArchiveMyPageList(String token) throws Exception;
    
    //==
    JsonBean projRwList(String token, Integer pageNumber, Integer pageSize ,Integer projectid,Integer targetId) throws Exception;
    
    //==
    JsonBean getTree(String token, Integer projectId, Integer nodeId,String type) throws Exception;

    JsonBean getMyWorkTree(String token,BigDecimal projectId) throws Exception;
    
    //==
	JsonBean xmgdSave(String token, String selectedData, Integer numPrice, String dateEndTime, String filCode,
			String filName, Integer worktime) throws Exception;


	JsonBean getTreeZy(String token, Integer tempId, Integer nodeId, String type)throws Exception;
    
	 JsonBean findPorgramByUser(String token,Integer targetId,Integer pageNumber,Integer pageSize,String bsunitname) throws Exception;
	 
	 public JsonBean getRwfpTree(String token, BigDecimal projectId, BigDecimal nodeId,String type) throws Exception;
	 
	 public JsonBean checkAllListMyrwPageList(String token, Integer pageNumber, Integer pageSize,String businessType,BigDecimal targetId,BigDecimal projectId)
				throws Exception ;
	 
	 JsonBean MyTaskFinish(String token, Integer operateid) throws Exception;

}
