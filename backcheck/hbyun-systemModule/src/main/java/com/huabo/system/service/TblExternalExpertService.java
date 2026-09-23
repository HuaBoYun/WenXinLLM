package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblExternalExpert;

public interface TblExternalExpertService {
    TblExternalExpert getExpert(BigDecimal var1);

    Map<String, Object> findByOrgId(Find find, Integer pageNumber, Integer pageSize,String token,String staffId,String company);

    void updateTblExternalExpert(TblExternalExpert tee);
    
    void saveTblExternalExpert(TblExternalExpert tee);

//   Map<String, Object> findByOrgId(Find find,Integer pageNumber,Integer pageSize,String token);
}
