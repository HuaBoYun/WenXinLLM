package com.huabo.system.service;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.huabo.system.entity.TblImplog;

public interface TblImplogService{
	Map<String, Object> findByTblImplogList(String token, String staffId, String type, Integer pageNumber, Integer pageSize);

	/**
	 * 导入excel程序以后将数据保存到数据库
	 */

	
	/**
	 * 查询导入日志的数据，根据类型查询
	 * @param user
	 * @param type
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */

}
