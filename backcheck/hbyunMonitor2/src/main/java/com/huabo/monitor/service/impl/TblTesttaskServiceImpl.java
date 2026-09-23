package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblTesttask;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.mapper.TblTesttaskMapper;


import com.huabo.monitor.service.ITblTesttaskService;

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
		task.setTesttaskid(RandomUtil.uuBigDecimalId());
		tblTesttaskMapper.insert(task); //insertEntity
	}

	@Override
	public void updateTesttsak(TblTesttask task) throws Exception{
		tblTesttaskMapper.updateById(task); //updateEntity
	}

	@Override
	public void delTesttsak(BigDecimal testtaskid) throws Exception{
		tblTesttaskMapper.TblTesttask(testtaskid);
	}


}
