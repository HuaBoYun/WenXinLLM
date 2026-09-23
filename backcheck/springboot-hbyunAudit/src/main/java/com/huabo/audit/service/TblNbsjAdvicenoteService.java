package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.util.R;

import java.math.BigDecimal;

public interface TblNbsjAdvicenoteService {
//	public void delete(TblNbsjAdvicenoteEntity note);
//	public void update(TblNbsjAdvicenoteEntity note);
////	public void save(TblNbsjAdvicenoteEntity note);
//	public List<TblNbsjAdvicenoteEntity> isNoteCode(String code);
////	public PageBean findAll(Integer projectId,TblNbsjAdvicenoteEntity note,String startTime,String endTime,Integer pageNumber, int pageSize);
//	
//	public TblNbsjAdvicenoteEntity get(String noteid);
//	/**
//	 * 查看编号是否重复(同个审计项目下)
//	 * @param code
//	 * @param projectId
//	 * @return
//	 */
//	public List<TblNbsjAdvicenoteEntity> isNoteCode(String code,String projectId);
	
	
	//==
	
	JsonBean noticePageList(String token, Integer pageNumber, Integer pageSize,TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo) throws Exception;

	JsonBean noticeAdd(TblNbsjAdvicenoteEntity notice, String token,String attids)throws Exception;
    
    JsonBean noticeDelete(BigDecimal adviceid, String token) throws Exception;
    
    JsonBean findNoticeDetail(String token, BigDecimal adviceid) throws Exception;
    
    public TblNbsjAdvicenoteEntity findById(String adviceid) throws Exception;
    /**
     * 提交审批
     * @param token
     * @param adviceid
     * @return
     * @throws Exception
     */
	JsonBean submitTblAdvicenoteArrpoval(String token, BigDecimal adviceid) throws Exception ;
	/**
	 * 查看审批页面
	 * @param token
	 * @param adviceid
	 * @param taskId
	 * @param cyId
	 * @return
	 * @throws Exception
	 */
	JsonBean getTblAdvicenoteApprovalInfo(String token, BigDecimal adviceid, String taskId, BigDecimal cyId) throws Exception;
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
	JsonBean dealTblAdvicenoteApporval(String token, BigDecimal cyId, String taskId, String transition, String optDesc,
			String adviceid) throws Exception;
	
	
	JsonBean noticeCancel(BigDecimal adviceid, String token) throws Exception;
	
	R removeAttInfoByAttId(String token, BigDecimal attId) throws Exception;
	
	
	JsonBean xfry(String ids,String userids,String usernames, String token) throws Exception;

	//审计通知下发列表
	JsonBean noticeIssuePageList(String token, Integer pageNumber, Integer pageSize,String adviceId ) throws Exception;

}
