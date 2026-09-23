package com.huabo.monitor.service;





import com.huabo.monitor.entity.TblInnerrule;

import java.math.BigDecimal;

/**
 * Created: 2022/11/25
 */
public interface TblInnerRuleService {
    TblInnerrule findById(BigDecimal id);
}
