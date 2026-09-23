package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huabo.system.entity.TblOrgNo;

public interface TblAutonoNumberService {
    int deleteOrgNo(BigDecimal orgid);


    Map<String, Object> getNumberList(BigDecimal orgid, BigDecimal fatherrightid, Integer pageNumber, Integer pageSize);

    String getNumberTree(String token, String staffId, BigDecimal parentid);

    Map<String,Object>  updateNumber(String token, String staffId, TblOrgNo tblOrgNo);

    void saveOrgNumber(BigDecimal orgid);

	String findRootNumberByParentId(String string, String string2, String string3, String string4, String string5,
			String string6, BigDecimal orgid, int i, Object object, Object object2, Object object3, Object object4,
			BigDecimal bigDecimal);
}
