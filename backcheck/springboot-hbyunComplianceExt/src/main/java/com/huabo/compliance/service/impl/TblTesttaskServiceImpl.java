package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblTesttask;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.compliance.mapper.TblTesttaskMapper;


import com.huabo.compliance.service.ITblTesttaskService;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@Service

public class TblTesttaskServiceImpl extends ServiceImpl<TblTesttaskMapper, TblTesttask> implements ITblTesttaskService {
   

	
	@Resource
	private TblTesttaskMapper tblTesttaskMapper;
	
	@Override
	public void saveTesttsak(TblTesttask task) throws Exception{
		tblTesttaskMapper.insertEntity(task);
	}

	@Override
	public void updateTesttsak(TblTesttask task) throws Exception{
		tblTesttaskMapper.updateEntity(task);
	}

	@Override
	public void delTesttsak(BigDecimal testtaskid) throws Exception{
		tblTesttaskMapper.TblTesttask(testtaskid);
	}


}
