package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblTesttaskAtt;
import com.huabo.compliance.mapper.TblTesttaskAttMapper;
import com.huabo.compliance.service.ITblTesttaskAttService;
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

}
