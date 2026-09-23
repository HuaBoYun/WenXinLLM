package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblTesttaskAtt;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-09-09
 */
public interface ITblTesttaskAttService extends IService<TblTesttaskAtt> {

	
	void saveEntity(BigDecimal attid,BigDecimal testtaskid) throws Exception;

	TblTesttaskAtt getOne(BigDecimal attid) throws Exception;

	
}
