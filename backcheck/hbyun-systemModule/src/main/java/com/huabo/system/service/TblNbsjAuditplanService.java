package com.huabo.system.service;

import java.util.List;

import com.huabo.system.entity.TblNbsjAuditplan;

public interface TblNbsjAuditplanService {
	
    TblNbsjAuditplan get(String planid);

    List<TblNbsjAuditplan> findCheckJH(String planid);

}
