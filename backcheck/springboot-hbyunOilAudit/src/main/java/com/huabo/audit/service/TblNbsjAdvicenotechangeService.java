package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteChangeEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteChangeEntity;

public interface TblNbsjAdvicenotechangeService {
	//==

	JsonBean noticeChangePageList(String token, Integer pageNumber, Integer pageSize, TblYqnsAdvicenoteChangeEntity tblNbsjAdvicenoteVo) throws Exception;

	JsonBean noticeChangeAdd(TblYqnsAdvicenoteChangeEntity notice, String token,String attids)throws Exception;

    JsonBean noticeChangeDelete(Integer changeid, String token) throws Exception;

    JsonBean findDetail(String token, Integer changeid) throws Exception;

    public TblYqnsAdvicenoteChangeEntity findById(Integer changeid) throws Exception;
    /**
     * 提交审批
     * @param token
     * @param changeid
     * @return
     * @throws Exception
     */
	JsonBean submitTblAdvicenoteArrpoval(String token, Integer changeid) throws Exception ;
	/**
	 * 查看审批页面
	 * @param token
	 * @param changeid
	 * @param taskId
	 * @param cyId
	 * @return
	 * @throws Exception
	 */
	JsonBean getTblAdvicenoteApprovalInfo(String token, Integer changeid, String taskId, Integer cyId) throws Exception;
	/**
	 * 办理
	 * @param token
	 * @param cyId
	 * @param taskId
	 * @param transition
	 * @param optDesc
	 * @param changeid
	 * @return
	 * @throws Exception
	 */
	JsonBean dealTblAdvicenoteApporval(String token, Integer cyId, String taskId, String transition, String optDesc,
			Integer changeid) throws Exception;
	
	/**
	 * 根据通知书，查询关联的通知变更内容
	 * @param adviceid
	 * @param token
	 * @return
	 * @throws Exception
	 */
	JsonBean findbyadviceidAll(BigDecimal adviceid, String token) throws Exception;

}
