package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsProjectWeerklyEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ProjectWeerklyService {

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize,TblYqnsProjectWeerklyEntity week,String ids) throws Exception;

    JsonBean saveEntity(String token, TblYqnsProjectWeerklyEntity weerklyEntity) throws Exception;

    JsonBean updateEntity(String token, TblYqnsProjectWeerklyEntity weerklyEntity) throws Exception;

    JsonBean deleteone(String token, BigDecimal id) throws Exception;
    
    JsonBean getone(String token, BigDecimal id) throws Exception;
    
    List<TblYqnsProjectWeerklyEntity> findAlldc(String token, Integer pageNumber, Integer pageSize,TblYqnsProjectWeerklyEntity week,String ids) throws Exception;

    JsonBean findAuditProjectOperationStatusAllList(String token, Integer pageNumber, Integer pageSize,TblYqnsProjectWeerklyEntity week,String ids) throws Exception;


}
