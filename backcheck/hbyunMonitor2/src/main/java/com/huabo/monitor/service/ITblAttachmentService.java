package com.huabo.monitor.service;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.vo.FileUploadRes;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-29
 */
public interface ITblAttachmentService extends IService<TblAttachment> {


    List<TblAttachment> findtTblAttachmentByTask(BigDecimal taskid);

    void deleteAttAndTaskAtt(BigDecimal attid);
    
	void saveEntity(TblAttachment tblAttachmentEntity) throws Exception;
	
	void delEntity(BigDecimal attid) throws Exception;
     void fileDownLoad(HttpServletResponse response, String fileId, Boolean isCa)throws Exception ;


    //密标新增
    BigDecimal selectSecretLabel(String attachmentLevel) throws Exception;

    Integer getAttachmentList(String formlevel,String attachmentLevelId) throws Exception;

    public List<FileUploadRes> fileUpload(MultipartFile[] file, Boolean isCa
    		,@Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel,BigDecimal attachmentLevelId)  throws Exception;

	public TblAttachment getOne(BigDecimal attid) throws Exception ;

}
