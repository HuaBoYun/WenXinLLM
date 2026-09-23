package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblIndustryInner;

public interface TblIndustryInnerService {
	
    List<TblIndustryInner> findByList(String var1);

    void save(TblIndustryInner ii);

    void delete(TblIndustryInner ii);

    void deleteByOrgid(BigDecimal orgid);
}
