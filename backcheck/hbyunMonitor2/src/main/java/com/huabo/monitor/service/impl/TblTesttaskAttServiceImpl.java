package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblTesttaskAtt;
import com.huabo.monitor.mapper.TblTesttaskAttMapper;
import com.huabo.monitor.service.ITblTesttaskAttService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-09
 */
@Service
public class TblTesttaskAttServiceImpl extends ServiceImpl<TblTesttaskAttMapper, TblTesttaskAtt> implements ITblTesttaskAttService {

	@Resource
	TblTesttaskAttMapper tblTesttaskAttMapper;
	
	@Override
	public void saveEntity(BigDecimal attid,BigDecimal testtaskid) throws Exception {
		// TODO Auto-generated method stub
		tblTesttaskAttMapper.insertEntity(attid,testtaskid);
	}

	@Override
	public TblTesttaskAtt getOne(BigDecimal attid) throws Exception {
		// TODO Auto-generated method stub
		return 	 tblTesttaskAttMapper.getOne(attid);

	}

}
