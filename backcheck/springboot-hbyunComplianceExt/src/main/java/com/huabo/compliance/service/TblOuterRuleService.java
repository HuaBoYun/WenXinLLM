package com.huabo.compliance.service;





import com.huabo.compliance.entity.TblOuterrule;

import java.math.BigDecimal;

/**
 * Created: 2022/11/25
 */
public interface TblOuterRuleService {
    TblOuterrule findById(BigDecimal id);
}
