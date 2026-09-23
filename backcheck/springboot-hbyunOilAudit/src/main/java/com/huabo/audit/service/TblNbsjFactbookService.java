package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.vo.TblNbsjFactbookVo;
import com.huabo.audit.util.R;

public interface TblNbsjFactbookService {
    /**
     * 提交审批
     * @param token
     * @param adviceid
     * @return
     * @throws Exception
     */
	JsonBean submitArrpoval(String token, Integer factid) throws Exception ;
	/**
	 * 查看审批页面
	 * @param token
	 * @param adviceid
	 * @param taskId
	 * @param cyId
	 * @return
	 * @throws Exception
	 */
	JsonBean getApprovalInfo(String token, Integer factid, String taskId, Integer cyId) throws Exception;
	/**
	 * 办理
	 * @param token
	 * @param cyId
	 * @param taskId
	 * @param transition
	 * @param optDesc
	 * @param adviceid
	 * @return
	 * @throws Exception
	 */
	JsonBean dealApporval(String token, Integer cyId, String taskId, String transition, String optDesc,
			String factid) throws Exception;
	/**
	 * @author tyb
	 * 2016-5-15上午11:20:36
	 * 查询所有
	 */
//	public PageBean findAll(Find find,Integer projectId,String userid,Integer startIndex,Integer pageSize);
	
	/**
	 * @author tyb
	 * 2016-5-16上午10:17:11
	 * 根据当前用户按确认人并且传阅查询所有
	 */
//	public PageBean findAllByQRRCY(String userid,Integer startIndex,Integer pageSize);
	
	/**
	 * @author tyb
	 * 2016-5-15上午11:20:22
	 * 添加方法
	 */
	public void addNbsjFactBook(TblNbsjFactbookEntity nbsjFactbook); 
	
	/**
	 * @author tyb
	 * 2016-5-15上午11:21:12
	 * 查询对象
	 */
	public TblNbsjFactbookEntity geTblNbsjFactbook(String factid);
	
	/**
	 * @author tyb
	 * 2016-5-15上午11:57:54
	 * 删除对象
	 */
	public void delNbsjFactBook(TblNbsjFactbookEntity nbsjFactbook);
	
	/**
	 * @author tyb
	 * 2016-5-15下午12:16:45
	 * 修改对象
	 */
	public void updateNbsjFactBook(TblNbsjFactbookEntity nbsjFactbook);
	
	
	public void merge(TblNbsjFactbookEntity nbsjFactbook);
	
//	public PageBean findAll(Find find,Integer integer,Integer startIndex,Integer pageSize);
	
	
	JsonBean confirmationPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjFactbookVo tblNbsjFactbookVo) throws Exception;
	
	JsonBean confirmationAdd(TblNbsjFactbookEntity fb, String token,String questionIds,String attids)throws Exception;
    
    JsonBean confirmationDelete(Integer factid, String token) throws Exception;
    
    JsonBean findNbsjFactbookDetail(String token, Integer factid) throws Exception;
    
    R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	
}
