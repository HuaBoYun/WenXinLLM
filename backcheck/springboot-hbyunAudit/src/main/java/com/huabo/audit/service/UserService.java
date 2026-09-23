package com.huabo.audit.service;

import com.hbfk.util.JsonBean;

import java.math.BigDecimal;

public interface UserService {

	
	//==
	JsonBean findAllPageBeanPid(String token, Integer pageNumber, Integer pageSize, BigDecimal orgid) throws Exception;
}
