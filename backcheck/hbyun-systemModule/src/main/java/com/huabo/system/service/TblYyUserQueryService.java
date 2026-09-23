package com.huabo.system.service;


import java.util.Map;

public interface TblYyUserQueryService {

    Map<String, Object> selectUserQueryById(Integer recordId) throws Exception;

}
