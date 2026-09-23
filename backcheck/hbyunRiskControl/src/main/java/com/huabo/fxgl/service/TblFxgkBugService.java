package com.huabo.fxgl.service;

import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblNbsjBugEntity;
import com.huabo.fxgl.vo.TblFxgkBugVo;

public interface TblFxgkBugService {

    /**
     * 缺陷管理-附件列表
     * @param token
     * @param bugId
     * @return
     */
    JsonBean defectFileList(String token,Integer bugId)throws Exception;


	
	//==
	JsonBean bugPageList(String token, Integer pageNumber, Integer pageSize,TblFxgkBugVo tblNbsjBugVo,Integer orgid) throws Exception;
	
	JsonBean bugAdd(TblNbsjBugEntity bug, String token,String attids)throws Exception;
    
    JsonBean bugDelete(Integer bugid, String token) throws Exception;
    
    JsonBean findBugDetail(String token, Integer bugid) throws Exception;

    /**
     * 缺陷管理-导出
     * @param token
     * @param orgId
     * @return
     * @throws Exception
     */
    JsonBean defect_file_export(String token, Integer orgId,HttpServletResponse response) throws Exception;
    
    JsonBean innerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, Integer bugid) throws Exception;
    
	JsonBean outerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, Integer bugid) throws Exception;
	
	JsonBean bugInnrulidsAdd(String token, Integer bugid, String innrulids) throws Exception;
	
	JsonBean bugOutrulidsAdd(String token, Integer bugid, String outrulids) throws Exception;
	
	JsonBean innerCommonLinkList(String token, Integer pageNumber, Integer pageSize, Integer bugid) throws Exception;
    
	JsonBean outerCommonLinkList(String token, Integer pageNumber, Integer pageSize, Integer bugid) throws Exception;
	
	JsonBean defectQxwtAdd(String token, Integer bugid, String bugids) throws Exception;
	
	JsonBean defectLinkList(String token, Integer pageNumber, Integer pageSize, Integer bugid) throws Exception;
	
	JsonBean bugInnrulidsDelete(String token, Integer bugid, String innrulids) throws Exception;
	
	JsonBean bugOutrulidsDelete(String token, Integer bugid, String outrulids) throws Exception;
	
	JsonBean bugFatherDelete(String token, Integer bugid, String bugids) throws Exception;
	
	JsonBean findBugCriterion(String token) throws Exception;
	
	
	void removeAttInfoByAttId(String token, String attId) throws Exception;
}
