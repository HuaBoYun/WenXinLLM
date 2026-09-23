package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.TblAttachment;


public interface TblAttachmentService {

	public Map<String, Object> findAttachmentListByhtId(BigDecimal contractId) throws Exception;

	public TblAttachment get(BigDecimal id) throws Exception;

	public Map<String, Object> uploadAttachment(MultipartFile file) throws Exception;

	public TblAttachment findById(String id) throws Exception;

	public void add(TblAttachment att) throws Exception;

	public void delete(String attid) throws Exception;

	public void deleteAttid(TblAttachment att) throws Exception;

	Map<String, Object> removeAttachmentByattId(BigDecimal attid);

	public Map<String, Object> findAttachmentListById(BigDecimal contractId);

	Map<String,Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String token, String staffId, MultipartFile file) throws Exception;

	JsonBean htlxFileList(String token,BigDecimal projectid) throws Exception;

	void saveEntity(TblAttachment tblAttachmentEntity) throws Exception;	

}
