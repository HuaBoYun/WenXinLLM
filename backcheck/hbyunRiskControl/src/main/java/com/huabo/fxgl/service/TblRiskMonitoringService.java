package com.huabo.fxgl.service;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.TblRiskMonitoringCreationDto;
import com.huabo.fxgl.entity.TblFillIssued;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskMonitoringCreation;
import com.huabo.fxgl.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface TblRiskMonitoringService {

 
    JsonBean getList(String token,Integer pageNumber,Integer pageSize,TblRiskMonitoringCreationDto dto)throws Exception;
//    
     JsonBean  saveOrUpdate(TblRiskMonitoringCreation tblRiskMonitoringCreation, String attIds, String token)throws Exception;
//    
    JsonBean detail(String id)throws Exception;
    
    JsonBean delete(String id)throws Exception;
//    
     JsonBean monitoringIssued(String majorid,String staffids,String staffnames)throws Exception;
//    
     JsonBean monitoringIssuedNew( List<TblFillIssued> issued,String ids,String token)throws Exception;

     JsonBean  getMonDictonaryInfo(String token)throws Exception;

     JsonBean  saveMonDictonaryDept(String token,JSONArray arr)throws Exception;
     
     Map<String, Object>  getMonDictonaryDeptList(String token)throws Exception;
     
     JsonBean getIssuedList(String token,String id)throws Exception;
     
     JsonBean getRiskMonDeptList(String token)throws Exception;
     
     List<Tree> getNodeAllbm(BigDecimal nodeId);

}
