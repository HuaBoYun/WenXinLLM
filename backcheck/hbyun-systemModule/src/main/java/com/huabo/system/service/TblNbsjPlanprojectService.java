package com.huabo.system.service;

import java.util.List;

import com.huabo.system.entity.TblNbsjPlanproject;

public interface TblNbsjPlanprojectService {
	
    List<TblNbsjPlanproject> findByPlanId(String planid);
}
