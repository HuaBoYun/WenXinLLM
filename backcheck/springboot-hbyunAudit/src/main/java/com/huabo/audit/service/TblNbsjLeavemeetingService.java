package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjLeavemeetingEntity;
import com.huabo.audit.oracle.vo.TblNbsjLeavemeetingVo;
import com.huabo.audit.util.R;

import java.math.BigDecimal;

public interface TblNbsjLeavemeetingService extends IService<TblNbsjLeavemeetingEntity>{
//	public void delete(TblNbsjLeavemeetingEntity leave);
//	public void update(TblNbsjLeavemeetingEntity leave);
//	public void save(TblNbsjLeavemeetingEntity leave);
//	public List<TblNbsjLeavemeetingEntity> findAll();
//	public List<TblNbsjLeavemeetingEntity> isNoteCode(String code);
////	public PageBean findAll(TblNbsjLeavemeetingEntity leave,Integer projectId,String startTime,String endTime,Integer pageNumber, int pageSize);
//	public TblNbsjLeavemeetingEntity get(String noteid);
//	public List<TblNbsjLeavemeetingEntity> isNoteCode(String code,String projectid);
	
	
	JsonBean outMeetRecordListPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo) throws Exception;
	
	JsonBean outMeetRecordAdd(TblNbsjLeavemeetingEntity lev, String token,String attids)throws Exception;
    
    JsonBean outmetDelete(BigDecimal leaveid, String token) throws Exception;
    
    JsonBean findNbsjLeavemeetingDetail(String token, BigDecimal leaveid) throws Exception;
    
    JsonBean outMetCalcel(BigDecimal leaveid, String token) throws Exception;
    
    R removeAttInfoByAttId(String token, String attId) throws Exception;
	
}
