package com.huabo.system.service;

import com.hbfk.entity.TblAttachment;
import com.hbfk.util.JsonBean;

import java.math.BigDecimal;
import java.util.List;

public interface TblAttachmentService {
    TblAttachment selectAtt(BigDecimal attid);

    List<TblAttachment> findAllByTblNBSJSheet(String toString);

	JsonBean getPrivewAttInfo(String token, BigDecimal attId, Integer attType) throws Exception;

	JsonBean getPrivewAttInfoNew(String token, BigDecimal attId, Integer attType) throws Exception;
}
