package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TblTemplateDuService {

	JsonBean saveEntity(String token, String tempType, String tempTitle, String tempContent) throws Exception;

	JsonBean removeEntity(String token, Integer tempId) throws Exception;

	JsonBean getList(String token, String tempType) throws Exception;

	JsonBean get(String token, BigDecimal tempId) throws Exception;

}