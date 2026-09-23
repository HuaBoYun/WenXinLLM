package com.huabo.audit.service;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.R;



public interface TblAttachmentService{

	void saveEntity(TblAttachment tblAttachmentEntity) throws Exception;

	TblAttachment selectEntityById(String attId) throws Exception;

	void removeEntityById(BigDecimal attid) throws Exception;
	
	
	//==
	JsonBean inMeetRecordFileList(String token,Integer enterid) throws Exception;
	
	JsonBean outMeetRecordFileList(String token,Integer leaveid) throws Exception;
	
	JsonBean dgFileList(String token,Integer sheetid) throws Exception;
	
	JsonBean workReportFileList(String token,Integer reportid) throws Exception;
	
	JsonBean dpFileList(String token,Integer dpointid) throws Exception;
	
	JsonBean projectFileList(String token,Integer projectId) throws Exception;
	
	JsonBean dataprojectFileList(String token,Integer dataId) throws Exception;
	
	JsonBean noticeFileList(String token,Integer adviceid) throws Exception;

	JsonBean reportFileList(String token, Integer reportid) throws Exception;
	
	JsonBean auditSuggestFileList(String token, Integer proid) throws Exception;
	
	JsonBean confirmationFileList(String token,Integer factid) throws Exception;
	
	 public R uploadAttachment(MultipartHttpServletRequest multiRequest, String token,MultipartFile file,String fileUrl);
	
//	Map<String, Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String token, String staffId, MultipartFile file) throws Exception;
	 
	JsonBean outFileList(String token,Integer outerId) throws Exception;
	
	JsonBean certificateFileList(String token,Integer certificateId) throws Exception;
	
	JsonBean xfry(String token,String staffids,String attids) throws Exception;
	
}
