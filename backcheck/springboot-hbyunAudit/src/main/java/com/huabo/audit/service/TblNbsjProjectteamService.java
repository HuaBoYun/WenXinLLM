package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjProjectTeamEntity;

import java.math.BigDecimal;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-19
*/
public interface TblNbsjProjectteamService {

	JsonBean findProjectteamList(String token, BigDecimal projectid) throws Exception;
	
	JsonBean findProjectteamUsrList(String token, BigDecimal projectid) throws Exception;
	JsonBean findProjectUsrList(String token, BigDecimal projectid) throws Exception;
	JsonBean getPjteamInfoById(String token, BigDecimal teamId) throws Exception;
	
	JsonBean pjItemAdd(String token, TblNbsjProjectTeamEntity team)throws Exception;
	
	JsonBean pjItemDelete(String token, BigDecimal teamId) throws Exception;
	

}
