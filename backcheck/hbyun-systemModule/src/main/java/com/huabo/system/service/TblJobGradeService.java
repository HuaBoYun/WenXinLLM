package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblJobGrade;
import com.huabo.system.entity.TblRole;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblJobGradeService {
    List<TblJobGrade> findAll(BigDecimal gradeid);
    
    void saveJobGrade(TblJobGrade var);
    
    void updateJob(TblJobGrade newJob);
    
    TblJobGrade findByid(String var1);
    
    void delete(String var1);
    
	public void syncJobGrade(String data) throws Exception ;

}
