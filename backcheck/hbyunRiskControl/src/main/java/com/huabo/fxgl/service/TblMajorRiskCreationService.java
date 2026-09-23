package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface TblMajorRiskCreationService {

 
    JsonBean getMajorRiskCreateList(String token,Integer pageNumber,Integer pageSize,String nd,String jd)throws Exception;
    
    JsonBean mjorSaveOrUpdate(TblMajorRiskCreate tblMajorRiskCreate, String token)throws Exception;
    
    JsonBean mjorDetail(String id)throws Exception;
    
    JsonBean mjorDelete(String id)throws Exception;
    
    JsonBean majorIssued(String majorid,String  ids,String staffids,String staffnames)throws Exception;
    
    JsonBean majorIssuedValidate(String majorid,String  ids)throws Exception;

    
    
    JsonBean mjorRiskSaveOrUpdate(TblRiskImplementGroupEntity tblRiskImplementGroupEntity,String token)throws Exception;
 
    JsonBean mjorRiskDelete(String id)throws Exception;
    
    JsonBean mjorRiskDetail(String id)throws Exception;
    
    Map<String,Object> importMjorRisk(String token,MultipartFile file,BigDecimal id);

}
