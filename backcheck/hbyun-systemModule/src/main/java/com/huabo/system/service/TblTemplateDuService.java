package com.huabo.system.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.hbfk.util.JsonBean;

@Service
public interface TblTemplateDuService {

	JsonBean saveEntity(String token, String tempType, String tempTitle, String tempContent) throws Exception;

	JsonBean removeEntity(String token, BigDecimal tempId) throws Exception;

	JsonBean getList(String token, String tempType) throws Exception;

	JsonBean get(String token, BigDecimal tempId) throws Exception;

}