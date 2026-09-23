package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjEntermeetingEntity;
import com.huabo.audit.oracle.vo.ExecPhraseVo;
import com.huabo.audit.oracle.vo.TblNbsjEntermeetingVo;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

/**
 * 进场纪要
 */
public interface TblNbsjEntermeetingService{
    /**
     * 进场纪要列表
     * @param vo 查询参数
     * @return
     */
	PageInfo<TblNbsjEntermeetingEntity> approachList(ExecPhraseVo vo);

//	PageBean findAll(TblNbsjEntermeetingEntity met, Integer projectId, String pstartDates, String pendDates,
//			Integer pageNumber, int pageSize);

//	List<TblNbsjEntermeetingEntity> isNoteCode(String code, String string);
	
	TblNbsjEntermeetingEntity findEntityByEnterId(String enterid);

	
    JsonBean inMeetRecordListPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjEntermeetingVo tblNbsjEntermeetingVo) throws Exception;
    
    JsonBean inMeetRecordAdd(TblNbsjEntermeetingEntity met, String token,String attids)throws Exception;
    
    JsonBean metDelete(Integer enterid, String token) throws Exception;
    
    JsonBean findNbsjEntermeetingDetail(String token, Integer enterid) throws Exception;
    
    JsonBean metCalcel(Integer enterid, String token) throws Exception;

    
    R removeAttInfoByAttId(String token, String attId) throws Exception;
    
    
}
