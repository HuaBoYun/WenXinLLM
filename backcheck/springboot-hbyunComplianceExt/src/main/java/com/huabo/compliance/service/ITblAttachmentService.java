package com.huabo.compliance.service;

import com.huabo.compliance.entity.TblAttachment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

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

}
