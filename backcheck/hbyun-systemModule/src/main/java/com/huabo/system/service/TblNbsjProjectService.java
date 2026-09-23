package com.huabo.system.service;

import com.huabo.system.entity.TblNbsjProject;

import java.math.BigDecimal;

public interface TblNbsjProjectService {
	
    TblNbsjProject findBySheetid(BigDecimal spid);
}
