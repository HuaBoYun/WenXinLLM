package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.system.entity.TblJob;

public interface TblJobService {
    List<TblJob> findAll(BigDecimal var1);

    void saveJob(TblJob var);

    TblJob findByid(String var1);

    void delete(TblJob var1);

    Map<String, Object> list(Integer pageNumber,Integer pageSize,String token,String staffId);

    TblJob findByJobId(String toString);

    void updateJob(TblJob newJob);

    Map<String, Object> listJob(Integer pageNumber, Integer pageSize, String token, String orgIds, String jobName, String orgName);

    void deleteJob(BigDecimal jobid);
    
    Map<String, Object> syncPost(Integer operaType,String data) throws Exception;
    
   void syncJob(String data) throws Exception;

}
