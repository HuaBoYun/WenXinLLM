package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjCertificate;
import com.huabo.audit.util.R;

import java.math.BigDecimal;

public interface TblNbsjCertificateService {
	
	public JsonBean getNbsjCertificateListPage(String token, Integer pageNumber, Integer pageSize,String projectName,String auditMatter,String auditAbstract,BigDecimal projectId) throws Exception;
	
	JsonBean certificateSave(TblNbsjCertificate certificate, String token,String attids)throws Exception;
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	JsonBean certificateDelete(BigDecimal certificateId, String token) throws Exception;
	
	JsonBean certificateDetail(String token, BigDecimal certificateId) throws Exception;
	
	public JsonBean getNbsjCertificateList(String token,TblNbsjCertificate cate, Integer pageNumber, Integer pageSize,String sheetid) throws Exception;

	JsonBean getCertificateList(String token, BigDecimal sheetid) throws Exception;


    JsonBean certificateSubmit(String token, BigDecimal certificateId, String examination) throws Exception;

	JsonBean getCertificate(String token, BigDecimal certificateId, String taskId, BigDecimal cyId) throws Exception;

	JsonBean dealCertificate(String token, BigDecimal cyId, String taskId, String transition, String optDesc, BigDecimal certificateId, String processDefinitionId, String processInstanceId) throws Exception;

	public JsonBean saveStampedDocument(BigDecimal certificateId, String[] attids) throws Exception;

	public JsonBean removeStampedDocument(BigDecimal certificateId, String attid) throws Exception;

	public JsonBean getStampedDocumentList(BigDecimal certificateId) throws Exception;

}
