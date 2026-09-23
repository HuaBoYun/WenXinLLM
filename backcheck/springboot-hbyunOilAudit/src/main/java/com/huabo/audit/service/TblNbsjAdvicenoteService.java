package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAdviceAprEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteEntity;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.util.R;

public interface TblNbsjAdvicenoteService {
	//==

	JsonBean noticePageList(String token, Integer pageNumber, Integer pageSize,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo) throws Exception;

	JsonBean noticeAdd(TblYqnsAdvicenoteEntity notice, String token, String attids)throws Exception;

    JsonBean noticeDelete(BigDecimal adviceid, String token) throws Exception;

    JsonBean findNoticeDetail(String token, BigDecimal adviceid) throws Exception;

    public TblYqnsAdvicenoteEntity findById(BigDecimal adviceid) throws Exception;
    /**
     * 提交审批
     * @param token
     * @param adviceid
     * @return
     * @throws Exception
     */
	JsonBean submitTblAdvicenoteArrpoval(String token, Integer adviceid) throws Exception ;
	/**
	 * 查看审批页面
	 * @param token
	 * @param adviceid
	 * @param taskId
	 * @param cyId
	 * @return
	 * @throws Exception
	 */
	JsonBean getTblAdvicenoteApprovalInfo(String token, Integer adviceid, String taskId, Integer cyId) throws Exception;
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
	JsonBean dealTblAdvicenoteApporval(String token, Integer cyId, String taskId, String transition, String optDesc,
			String adviceid) throws Exception;


	JsonBean noticeCancel(BigDecimal adviceid, String token) throws Exception;

	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	JsonBean noticeAprPageList(String token, Integer pageNumber, Integer pageSize,String advicename,String xctype) throws Exception;
	
	JsonBean findNoticeAprDetail(String token, BigDecimal adviceid) throws Exception;
	
	JsonBean noticeAprAdd(TblYqnsAdviceAprEntity notice, String token)throws Exception;
	
	JsonBean noticeAprDelete(BigDecimal adviceid, String token) throws Exception;
	
	/**
	 * 查询已审批完成的内容
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param advicename
	 * @return
	 * @throws Exception
	 */
	JsonBean noticespAprPageList(String token, Integer pageNumber, Integer pageSize,String advicename) throws Exception;
	
}
