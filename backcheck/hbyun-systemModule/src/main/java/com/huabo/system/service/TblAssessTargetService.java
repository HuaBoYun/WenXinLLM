package com.huabo.system.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAssessTarget;

public interface TblAssessTargetService {
	
    void MyMark(BigDecimal staffid, String assid, String assName, PageInfo<TblAssessTarget> pageInfo);
}
