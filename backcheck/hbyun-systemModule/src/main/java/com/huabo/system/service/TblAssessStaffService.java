package com.huabo.system.service;

import java.math.BigDecimal;

public interface TblAssessStaffService {
    String getStatusByUserAsss(BigDecimal staffid, BigDecimal assid, BigDecimal orgid);
}
