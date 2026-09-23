package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.R;



public interface TblAttachmentService extends IService<TblAttachment> {

	void saveEntity(TblAttachment tblAttachmentEntity) throws Exception;

	TblAttachment selectEntityById(BigDecimal attId) throws Exception;

	void removeEntityById(BigDecimal attid) throws Exception;
	
	
	//==
	JsonBean inMeetRecordFileList(String token,BigDecimal enterid) throws Exception;
	
	JsonBean outMeetRecordFileList(String token,BigDecimal leaveid) throws Exception;
	
	JsonBean dgFileList(String token,BigDecimal sheetid) throws Exception;
	
	JsonBean workReportFileList(String token,BigDecimal reportid) throws Exception;
	
	JsonBean dpFileList(String token,BigDecimal dpointid) throws Exception;
	
	JsonBean projectFileList(String token,BigDecimal projectId) throws Exception;
	
	JsonBean dataprojectFileList(String token,BigDecimal dataId) throws Exception;
	
	JsonBean noticeFileList(String token,BigDecimal adviceid) throws Exception;

	JsonBean reportFileList(String token, BigDecimal reportid) throws Exception;
	
	JsonBean reportOpinionFileList(String token, BigDecimal reportid) throws Exception;
	
	JsonBean reportFinalFileList(String token, BigDecimal reportid) throws Exception;
	
	
	JsonBean auditSuggestFileList(String token, BigDecimal proid) throws Exception;
	
	JsonBean confirmationFileList(String token,BigDecimal factid) throws Exception;
	
	 public R uploadAttachment(MultipartHttpServletRequest multiRequest, String token,MultipartFile file,String fileUrl);
	
//	Map<String, Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String token, String staffId, MultipartFile file) throws Exception;
	 
	JsonBean outFileList(String token,BigDecimal outerId) throws Exception;
	
	JsonBean certificateFileList(String token,BigDecimal certificateId) throws Exception;
	
	JsonBean xfry(String token,String staffids,String attids) throws Exception;

	//密标新增
	BigDecimal selectSecretLabel(String attachmentLevel) throws Exception;

	Integer getAttachmentList(String formlevel,String attachmentLevelId) throws Exception;
}
