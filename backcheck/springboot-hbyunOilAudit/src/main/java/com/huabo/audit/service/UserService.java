package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;

public interface UserService {

	
	//==
	JsonBean findAllPageBeanPid(String token, Integer pageNumber, Integer pageSize, BigDecimal orgid) throws Exception;
}
