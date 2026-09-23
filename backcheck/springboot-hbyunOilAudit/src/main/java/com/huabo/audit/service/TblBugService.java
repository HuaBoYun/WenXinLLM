package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblBugEntity;

public interface TblBugService {

    TblBugEntity findById(BigDecimal bigDecimal);
    
}
