package com.huabo.contract.service;

import java.util.Map;

import com.huabo.contract.entity.TblYyXdfCompany;

public interface TblyyxdfCompanyService {

	Map<String, Object> findByCompay(Integer pageNumber, Integer pageSize, String teamid, String fxtype, String companyname) throws Exception;

	Map<String, Object> saveOrupdateYYCompany(String pageid, String priceid, TblYyXdfCompany company) throws Exception;

}
