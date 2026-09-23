package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTesttask;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
public interface ITblTesttaskService extends IService<TblTesttask> {
     void saveTesttsak(TblTesttask task)throws Exception;
	
	  void updateTesttsak(TblTesttask task)throws Exception;
	
	  void delTesttsak(BigDecimal testtaskid)throws Exception;
	

}
