package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblTestplan;
import com.baomidou.mybatisplus.extension.service.IService;
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
	
	void savePlan(TblTestplan tblTestplan)throws Exception;
	
	void update(TblTestplan tblTestplan) throws Exception;

}
