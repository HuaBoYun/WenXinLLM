package com.huabo.monitor.service;





import com.huabo.monitor.entity.TblOuterrule;

import java.math.BigDecimal;

/**
 * Created: 2022/11/25
 */
public interface TblOuterRuleService {
    TblOuterrule findById(BigDecimal id);
}
