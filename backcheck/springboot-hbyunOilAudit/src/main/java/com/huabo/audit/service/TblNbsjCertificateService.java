package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjCertificate;
import com.huabo.audit.util.R;

public interface TblNbsjCertificateService {
	
	public JsonBean getNbsjCertificateListPage(String token, Integer pageNumber, Integer pageSize,String projectName,String auditMatter,String auditAbstract,Integer projectId) throws Exception;
	
	JsonBean certificateSave(TblNbsjCertificate certificate, String token,String attids)throws Exception;
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	JsonBean certificateDelete(Integer certificateId, String token) throws Exception;
	
	JsonBean certificateDetail(String token, Integer certificateId) throws Exception;
	
	public JsonBean getNbsjCertificateList(String token,TblNbsjCertificate cate, Integer pageNumber, Integer pageSize,String sheetid) throws Exception;

	JsonBean getCertificateList(String token, Integer sheetid) throws Exception;


    JsonBean certificateSubmit(String token, Integer certificateId, String examination) throws Exception;

	JsonBean getCertificate(String token, Integer certificateId, String taskId, Integer cyId) throws Exception;

	JsonBean dealCertificate(String token, Integer cyId, String taskId, String transition, String optDesc, Integer certificateId, String processDefinitionId, String processInstanceId) throws Exception;

}
