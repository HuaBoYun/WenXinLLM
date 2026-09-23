package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.entity.Pamas;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;



public interface OperationService{

	JsonBean dealSendDigao(String token, String modelTye, Pamas pamas) throws Exception;

	JsonBean dealSendDigaoListFile(String token, String modelTye, Pamas pamas, Integer pageNumber, Integer pageSize, TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

	JsonBean saveSendDigaoListFile(String token, String modelTye, Pamas pamas, BigDecimal workId) throws Exception;

	JsonBean dealSendDoubtful(String token, String modelTye, Pamas pamas) throws Exception;

	JsonBean dealSendDefect(String token, String modelTye, Pamas pamas) throws Exception;

	JsonBean dealSendEvidence(String token, String modelTye, Pamas pamas) throws Exception;

}
