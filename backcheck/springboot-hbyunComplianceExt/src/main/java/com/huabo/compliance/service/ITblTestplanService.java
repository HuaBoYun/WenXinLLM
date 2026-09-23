package com.huabo.compliance.service;

import com.huabo.compliance.entity.TblTestplan;
import com.hbfk.util.JsonBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
public interface ITblTestplanService {
	
	void add(TblTestplan tblTestplan) throws Exception;

	JsonBean selectList(String plannumber, String planname, String planstatus, Date starttime_min, Date starttime_max, Integer pageNumber, Integer pageSize);

	void deleteById(BigDecimal testplanid);
	
	JsonBean updateById(BigDecimal testplanid);
	
	JsonBean saveAll(List<String> plans);
	
	JsonBean select(String planname, String planyear, Integer pageNumber, Integer pageSize);
	
	TblTestplan getById(BigDecimal testplanid);
	
	void save(TblTestplan tblTestplan)throws Exception;
	
	void update(TblTestplan tblTestplan) throws Exception;

}
