package com.huabo.audit.service;

import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjProposalEntity;
import com.huabo.audit.oracle.vo.TblNbsjProposalVo;
import com.huabo.audit.util.R;

public interface TblNbsjProposalService {

	public void delete(TblNbsjProposalEntity leave);
	public void update(TblNbsjProposalEntity leave);
	public void save(TblNbsjProposalEntity leave);
	public List<TblNbsjProposalEntity> findAll();
	public List<TblNbsjProposalEntity> isNoteCode(String code);
//	public PageBean findAll(TblNbsjProposalEntity sal,Integer projectId,String startTime,String endTime,Integer pageNumber, int pageSize);
	public TblNbsjProposalEntity get(String noteid);
	/**
	 * 查询编号是否重复
	 * @param code
	 * @param projectId
	 * @return
	 */
	public List<TblNbsjProposalEntity> isNoteCode(String code,String projectId);
	
	
	
	//==
	JsonBean suggestPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjProposalVo tblNbsjProposalVo,Integer projectId) throws Exception;
	
	JsonBean suggestAdd(TblNbsjProposalEntity proposal, String token,String attids)throws Exception;
    
    JsonBean suggestCancel(Integer proid, String token) throws Exception;
    
    JsonBean findSuggestDetail(String token, Integer proid) throws Exception;
    
    JsonBean suggestDelete(Integer proid, String token) throws Exception;
    
    R removeAttInfoByAttId(String token, String attId) throws Exception;
}
