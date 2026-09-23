package com.huabo.system.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSecrectLevel;

public interface SecrectLevelService {

	JsonBean saveInfo(String token, TblSecrectLevel secrect) throws Exception;

	JsonBean modifyInfo(String token, TblSecrectLevel secrect) throws Exception;

	JsonBean removeInfo(String token, BigDecimal levelId) throws Exception;

	JsonBean getDetail(String token, BigDecimal levelId) throws Exception;

	JsonBean getPageInfo(String token, TblSecrectLevel secrect, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean getSecrectListByType(String token, Integer levelType) throws Exception;

	JsonBean getScopeSecrectListByType(String token, Integer levelType) throws Exception;

	JsonBean getSecrectListForRight(String token, BigDecimal rightId) throws Exception;

	JsonBean getSecrectListByManage(String token, BigDecimal levelId) throws Exception;

	JsonBean getSecrectListByLoginUser(String token) throws Exception;

}
