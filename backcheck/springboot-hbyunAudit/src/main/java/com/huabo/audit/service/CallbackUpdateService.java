package com.huabo.audit.service;



import java.math.BigDecimal;

public interface CallbackUpdateService {

	public void updateStatus(BigDecimal cId,String status);
}
