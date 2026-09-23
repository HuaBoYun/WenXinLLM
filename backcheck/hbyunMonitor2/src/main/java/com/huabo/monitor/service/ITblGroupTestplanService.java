package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblTestplan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

 
public interface ITblGroupTestplanService {
	
	void add(TblGroupTestplan tblTestplan) throws Exception;

	void deleteById(BigDecimal testplanid);
	
	TblGroupTestplan getById(BigDecimal testplanid);
	
	void savePlan(TblGroupTestplan tblTestplan)throws Exception;
	
	void update(TblGroupTestplan tblTestplan) throws Exception;

	JsonBean toIssued(String id,String staffids, String staffnames)throws Exception;

	JsonBean startIssued(String id,String token)throws Exception;
}
