package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjProjectTeamEntity;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-19
*/
public interface TblNbsjProjectteamService {

	JsonBean findProjectteamList(String token, Integer projectid) throws Exception;
	
	JsonBean findProjectteamUsrList(String token, Integer projectid) throws Exception;
	
	JsonBean getPjteamInfoById(String token, Integer teamId) throws Exception;
	
	JsonBean pjItemAdd(String token,TblNbsjProjectTeamEntity team,String zystaffids,Integer leaderid,Integer projectid)throws Exception;
	
	JsonBean pjItemDelete(String token, Integer teamId) throws Exception;
	

}
