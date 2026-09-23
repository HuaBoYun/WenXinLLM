package com.huabo.compliance.service;





import com.huabo.compliance.entity.TblInnerrule;

import java.math.BigDecimal;

/**
 * Created: 2022/11/25
 */
public interface TblInnerRuleService {
    TblInnerrule findById(BigDecimal id);
}
