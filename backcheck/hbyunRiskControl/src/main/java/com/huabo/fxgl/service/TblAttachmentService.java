package com.huabo.fxgl.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblAttachment;


public interface TblAttachmentService {


	/* Map<String,Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String token, String staffId, MultipartFile file) throws Exception;

	TblAttachment get(BigDecimal id);

    void add(TblAttachment att);

	TblAttachment findById(String attid);

	void delete(String attid);

	Map<String, Object> removeAttachmentByattId(Integer attid);

    void deleteAttid(TblAttachment att);


    Map<String, Object> findAttachmentListById(Integer contractId);
    
    
    //==
    JsonBean htlxFileList(String token,Integer projectid) throws Exception;
    
    public Map<String, Object> findAttachmentListByhtId(Integer contractId);
    
    Map<String, Object> findAttachmentListByMyTaskId(Integer id);*/

	void saveEntity(TblAttachment tblAttachmentEntity) throws Exception;
    
	Integer insertQxglFile(BigDecimal  moduleId,BigDecimal attid)throws Exception;
	
	Integer insertFxydFile(BigDecimal  moduleId,BigDecimal attid)throws Exception;

}
