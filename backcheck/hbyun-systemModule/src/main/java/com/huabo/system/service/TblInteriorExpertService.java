package com.huabo.system.service;

import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblInteriorExpert;
import java.math.BigDecimal;
import java.util.Map;

public interface TblInteriorExpertService {
    TblInteriorExpert findInterior(TblInteriorExpert interior);

    void save(TblInteriorExpert tie);

    void update(TblInteriorExpert tblinter);

    Map<String, Object> getExperList(Find find, String staffId,Integer pageNumber,Integer pageSize,String token);

    TblInteriorExpert findInteriorExpertId(BigDecimal userid);

    Map<String, Object> removeNbzj(BigDecimal interiorid);
}
