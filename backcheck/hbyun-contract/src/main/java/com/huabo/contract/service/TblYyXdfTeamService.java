package com.huabo.contract.service;

import java.util.Map;

import com.huabo.contract.entity.TblYyXdfTeam;

public interface TblYyXdfTeamService {

	Map<String, Object> saveOrupdateTeam(TblYyXdfTeam team) throws Exception;

	Map<String, Object> findBYuseridAndCompanid() throws Exception;

}
